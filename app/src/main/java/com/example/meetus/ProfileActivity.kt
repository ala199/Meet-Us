package com.example.meetus

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.example.meetus.databinding.ActivityMainBinding
import com.example.meetus.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProfileBinding

    companion object{
        val IMAGE_REQUEST_CODE=100
    }
    //تكوين ال root الرئيسى فى storage
    private val storageInstance:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    //سحب ال root للوصول الى storage
    //انشأ child اسمة uid الخاص بالمستخدم
    private val currentUserStorageRef:StorageReference
    get()=storageInstance.reference.child(mAuth.currentUser?.uid.toString())

    private val mAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //حجز مكان للنص فى toolbar
        setSupportActionBar(binding.profileToolbar)
        supportActionBar?.title="Me"
        //حجز مكان لايقونة الرجوع
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //فتح معرض الصور, ثم اختيار صورة
        binding.bigCircleImageViewProfileImage.setOnClickListener {
             val myIntentImage=Intent().apply {
                 type="image/*"
                 action=Intent.ACTION_GET_CONTENT
                 putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
             }
            startActivityForResult(Intent.createChooser(myIntentImage, "Select Image"),
                IMAGE_REQUEST_CODE)
        }

        binding.btnLogOut.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }
    }

    //اختيار صورة مؤقتا
    //هنا سوف نكتب الكود الذى يرفع الصورة الى storage لكى تحفظ فى firebase
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null ){
            binding.bigCircleImageViewProfileImage.setImageURI(data?.data)

            //تقليص الصورة على شكل bytes ووضعها فى متغير حتى يمكن رفعها على ال firebase
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
            val selectedImageBytes=outputStream.toByteArray()

            uploadProfileImage(selectedImageBytes)
        }
    }

    private fun uploadProfileImage(selectedImageBytes:  ByteArray) {
        val ref=currentUserStorageRef.child("profilePicture/${UUID.nameUUIDFromBytes(selectedImageBytes)}")
        ref.putBytes(selectedImageBytes).addOnCompleteListener{
            if (it.isSuccessful){

            }else{
                Toast.makeText(this, "Error:${it.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //الرجوع الى القائمة الرئيسية وهى home
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return false
    }
}