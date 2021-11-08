package com.example.meetus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.meetus.databinding.ActivityMainBinding
import com.example.meetus.databinding.ActivitySignUpBinding
import com.example.meetus.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity(), TextWatcher {

    private lateinit var binding: ActivitySignUpBinding
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestorInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserDocRef:DocumentReference
    get() = firestorInstance.document("users/${mAuth.currentUser?.uid.toString()}")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etNameSignUp.addTextChangedListener(this)
        binding.etEmailSignUp.addTextChangedListener(this)
        binding.etPasswordSignUp.addTextChangedListener(this)

        binding.btSignup.setOnClickListener {
            val name = binding.etNameSignUp.text.toString().trim()
            val email = binding.etEmailSignUp.text.toString().trim()
            val password = binding.etPasswordSignUp.text.toString().trim()

            if (name.isEmpty()) {
                binding.etNameSignUp.error = "Name Required"
                binding.etNameSignUp.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.etEmailSignUp.error = "Email Required"
                binding.etEmailSignUp.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailSignUp.error = "Email Required"
                binding.etEmailSignUp.requestFocus()
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.etPasswordSignUp.error = "Required 6 char"
                binding.etPasswordSignUp.requestFocus()
                return@setOnClickListener
            }

            createNewAccount(name, email, password) {}

        }
        binding.tvBackToSignIn.setOnClickListener{
            val intentSignInActivity=Intent(this,SignInActivity::class.java)
            startActivity(intentSignInActivity)
        }
    }

    private fun createNewAccount(name: String, email: String, password: String, function: () -> Unit)
    {                binding.progressSignUp.visibility= View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            val newUser=User(name,"")
            currentUserDocRef.set(newUser)

            if (it.isSuccessful) {
                binding.progressSignUp.visibility=View.INVISIBLE
                val intentMianActivity = Intent(this, MainActivity::class.java)
                intentMianActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentMianActivity)
                Toast.makeText(this, "Successfully Sign Up", Toast.LENGTH_SHORT).show()

            }
            else {
                binding.progressSignUp.visibility=View.INVISIBLE
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.btSignup.isEnabled=binding.etNameSignUp.text.trim().isNotBlank()&&binding.etEmailSignUp.text.trim().isNotBlank()&&
                binding.etPasswordSignUp.text.trim().isNotBlank()
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

}