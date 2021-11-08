package com.example.meetus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.meetus.databinding.ActivityMainBinding
import com.example.meetus.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProfileBinding

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