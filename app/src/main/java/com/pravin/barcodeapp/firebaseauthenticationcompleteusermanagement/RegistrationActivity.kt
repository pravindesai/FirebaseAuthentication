package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.ContentInfoCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.GlobalStrings
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.editTextTextPassword
import kotlinx.android.synthetic.main.activity_registration.editTextTextPhoneNumber

class RegistrationActivity : AppCompatActivity() {

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        alreadyHaveAccount.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(loginIntent)
        }

        registerButton.setOnClickListener {



            registerUser()
        }

    }

    private fun registerUser() {
        val fname   :String = fnameEt. text.toString()
        val lname   :String = lnameEt.text.toString()
        val phone   :String = phoneEt.text.toString()
        val password:String = passEt.text.toString()

        if (fname.trim().isEmpty() || fname.trim().isBlank()) {
            firstNameEt.setError("Required")
            return
        }
        if (lname.trim().isEmpty() || lname.trim().isBlank()) {
            lastNameEt.setError("Required")
            return
        }
        if (phone.trim().isEmpty() || phone.trim().isBlank()) {
            editTextTextPhoneNumber.setError("Required")
            return
        }
        if (password.trim().isEmpty() || password.trim().isBlank()) {
            editTextTextPassword.setError("Required")
            if (password.length<6){
                editTextTextPassword.setError("Password lenght should be 6 characters")
                editTextTextPassword.requestFocus()
                return
            }
            return
        }

        val otpVerificationIntent = Intent(this, OtpVerificationActivity::class.java)
        otpVerificationIntent.putExtra( GlobalStrings.fname,    fname)
        otpVerificationIntent.putExtra( GlobalStrings.lname,    lname)
        otpVerificationIntent.putExtra( GlobalStrings.phone,    phone)
        otpVerificationIntent.putExtra( GlobalStrings.password, password)

        startActivity(otpVerificationIntent)

    }
}