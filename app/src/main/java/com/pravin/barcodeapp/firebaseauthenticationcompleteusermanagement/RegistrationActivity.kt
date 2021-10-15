package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.GlobalStrings
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.UniversalProgressDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.editTextTextPassword
import kotlinx.android.synthetic.main.activity_registration.editTextTextPhoneNumber
import kotlinx.android.synthetic.main.activity_registration.phoneEt

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
            UniversalProgressDialog.show(this)
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

        SignInIfUserNotExists(fname, lname, phone, password)

    }


    private fun SignInIfUserNotExists(fname:String, lnname:String, phoneMail:String, password:String) {

        auth.fetchSignInMethodsForEmail(phoneMail+GlobalStrings.mailExtension).addOnCompleteListener {
            Log.e("***", "fetchSignInMethods: "+Thread.currentThread() )
            if (it.isSuccessful){
                val result: SignInMethodQueryResult? = it.result
                if (result!=null && result.signInMethods!= null && result.signInMethods!!.size > 0){
                    //User already exists
                    UniversalProgressDialog.hide()
                    phoneEt.setError("User Already exists")
                    phoneEt.requestFocus()
                    return@addOnCompleteListener
                }else{
                    //No User found for this mail
                    userNotExists(fname, lnname, phoneMail, password);
                }
            }else{
                Log.e("**", "fetchSignInMethods: Failed" )
                UniversalProgressDialog.hide()
            }
        }
    }

    private fun userNotExists(fname: String, lname: String, phone: String, password: String) {
        val otpVerificationIntent = Intent(this, OtpVerificationActivity::class.java)
        otpVerificationIntent.putExtra( GlobalStrings.fname,    fname)
        otpVerificationIntent.putExtra( GlobalStrings.lname,    lname)
        otpVerificationIntent.putExtra( GlobalStrings.phone,    phone)
        otpVerificationIntent.putExtra( GlobalStrings.password, password)

        UniversalProgressDialog.hide()
        startActivity(otpVerificationIntent)
    }


}