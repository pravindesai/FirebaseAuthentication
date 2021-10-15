package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.GlobalStrings
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        loginButton.setOnClickListener {
            var phoneNumber = editTextTextPhoneNumber.text.toString()
            var password = editTextTextPassword.text.toString()

            if (phoneNumber.isBlank() || phoneNumber.length<10){
                editTextTextPhoneNumber.setError("Please Confirm!!")
                editTextTextPhoneNumber.requestFocus()
                return@setOnClickListener
            }

            if (password.isBlank()){
                editTextTextPhoneNumber.setError("required")
                editTextTextPhoneNumber.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(phoneNumber+GlobalStrings.mailExtension,
                password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("**", "signInWithEmail:success")
                        val user = auth.currentUser
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        startActivity(intent)
                        this.finish()
                    } else {
                        Log.e("**", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }



        }

        otpLoginButton.setOnClickListener {

            val phoneNumber = editTextTextPhoneNumber.text.toString()

                if (phoneNumber.isBlank() || phoneNumber.length<10){
                    editTextTextPhoneNumber.setError("Please Confirm!!")
                    editTextTextPhoneNumber.requestFocus()
                    return@setOnClickListener
                }

            val otpVerificationIntent = Intent(this, OtpVerificationActivity::class.java)
            otpVerificationIntent.putExtra(GlobalStrings.phone, phoneNumber)

            startActivity(otpVerificationIntent)
            this.finish()
        }

        registrationButton.setOnClickListener {
            val registrationIntent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }

    }
}