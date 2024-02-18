package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat.databinding.ActivitySignUpBinding
import com.example.chat.model.Users
import com.example.chat.utils.displayMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var name : String
    private lateinit var email : String
    private lateinit var password : String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initializeEventsClick()

    }

    private fun initializeEventsClick() {
        binding.buttonSignUp.setOnClickListener() {
            if(validateUser()) {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email:String, password:String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password ).addOnCompleteListener{result ->
            if(result.isSuccessful) {


//                Save User
                val userId = result.result.user?.uid

                if(userId != null){
                    val users = Users(userId, name, email)
                    saveUserOnFirebase(users)
                }




            }
        }.addOnFailureListener { err ->
            try {
                throw err
            } catch (errorWeakPassword: FirebaseAuthWeakPasswordException) {
                errorWeakPassword.printStackTrace()
                displayMessage("Weak password, enter another one with letters, numbers and special characters")
            } catch (errorUserExist: FirebaseAuthUserCollisionException) {
                errorUserExist.printStackTrace()
                displayMessage("E-mail already register")
            } catch (errorCredentialsInvalid: FirebaseAuthInvalidCredentialsException) {
                errorCredentialsInvalid.printStackTrace()
                displayMessage("Invalid email, please enter another email")
            }
        }

    }

    private fun saveUserOnFirebase(users: Users) {
        firestore.collection("users").document(users.id).set(users)
            .addOnSuccessListener {
                displayMessage("User Register Success")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            } .addOnFailureListener {
                displayMessage("Error Registering User")
            }
    }

    private fun validateUser():Boolean {
        name = binding.editName.text.toString()
        email = binding.editEmail.text.toString()
        password = binding.editPassword.text.toString()
        if( name.isNotEmpty() ){

            binding.textInputName.error = null
            if( email.isNotEmpty() ){

                binding.textInputEmail.error = null
                if( password.isNotEmpty() ){
                    binding.textInputPassword.error = null
                    return true
                }else{
                    binding.textInputPassword.error = "Fill the password"
                    return false
                }

            }else{
                binding.textInputEmail.error = "Fill the e-mail!"
                return false
            }

        }else{
            binding.textInputName.error = "Fill the name"
            return false
        }

    }


    private fun initToolbar() {
        val toolbar = binding.includeTB.materialToolbar

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Back To Login"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}