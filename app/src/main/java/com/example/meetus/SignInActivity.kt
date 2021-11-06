package com.example.meetus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.meetus.databinding.ActivityMainBinding
import com.example.meetus.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val mAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSignIn.setOnClickListener {
            val email=binding.etEmailSignIn.text.toString().trim()
            val password=binding.etPasswordSignIn.text.toString().trim()

            if (email.isEmpty()){
                binding.etEmailSignIn.error="Email Required"
                binding.etEmailSignIn.requestFocus()
                return@setOnClickListener
            }
            if (password.length< 6){
                binding.etPasswordSignIn.error="Required 6 char"
                binding.etPasswordSignIn.requestFocus()
                return@setOnClickListener
            }

            signIn(email,password)
        }
    }

    private fun signIn(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                val intentMainActivity=Intent(this,MainActivity::class.java)
                startActivity(intentMainActivity)
                Toast.makeText(this, "Successfully Sign In", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}