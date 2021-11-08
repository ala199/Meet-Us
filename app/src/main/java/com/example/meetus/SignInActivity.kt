package com.example.meetus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.meetus.databinding.ActivityMainBinding
import com.example.meetus.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity(),TextWatcher {

    private lateinit var binding: ActivitySignInBinding
    private val mAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmailSignIn.addTextChangedListener(this@SignInActivity)
        binding.etPasswordSignIn.addTextChangedListener(this@SignInActivity)

        binding.btSignIn.setOnClickListener {
            val email=binding.etEmailSignIn.text.toString().trim()
            val password=binding.etPasswordSignIn.text.toString().trim()

            if (email.isEmpty()){
                binding.etEmailSignIn.error="Email Required"
                binding.etEmailSignIn.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmailSignIn.error="please enter valid email"
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

        binding.btnCreateAccount.setOnClickListener {
            val intentSignUpActivity=Intent(this,SignUpActivity::class.java)
            startActivity(intentSignUpActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser?.uid != null){
            val intentMainActivity=Intent(this@SignInActivity,MainActivity::class.java)
            startActivity(intentMainActivity)
        }
    }
    private fun signIn(email: String, password: String) {
        binding.progressSignIn.visibility=View.VISIBLE
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                binding.progressSignIn.visibility=View.INVISIBLE
                val intentMainActivity=Intent(this,MainActivity::class.java)
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentMainActivity)
                Toast.makeText(this, "Successfully Sign In", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressSignIn.visibility=View.INVISIBLE
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.btSignIn.isEnabled=binding.etEmailSignIn.text.trim().isNotEmpty()
                &&binding.etPasswordSignIn.text.trim().isNotEmpty()

    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
    override fun afterTextChanged(s: Editable?) {
    }


}