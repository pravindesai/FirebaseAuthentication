package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.GlobalStrings
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        val SIGN_IN_METHOD_PASSWORD = "PASSWORD"
        val SIGN_IN_METHOD_OTP = "OTP"
    }

    val auth:FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        loginButton.setOnClickListener {
            val phoneNumber = phoneEt.text.toString()
            val password = passwordEt.text.toString()

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

            LogInIfUserExists(phoneNumber, password, SIGN_IN_METHOD_PASSWORD)

        }

        otpLoginButton.setOnClickListener {

            val phoneNumber = phoneEt.text.toString()

                if (phoneNumber.isBlank() || phoneNumber.length<10){
                    editTextTextPhoneNumber.setError("Please Confirm!!")
                    editTextTextPhoneNumber.requestFocus()
                    return@setOnClickListener
                }

            LogInIfUserExists(phoneNumber, password = "", SIGN_IN_METHOD_OTP)
        }

        registrationButton.setOnClickListener {
            val registrationIntent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            registrationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(registrationIntent)
        }

    }

    private fun LogInIfUserExists(phoneMail:String, password:String, method:String) {

        auth.fetchSignInMethodsForEmail(phoneMail+GlobalStrings.mailExtension).addOnCompleteListener {
            Log.e("***", "fetchSignInMethods: "+Thread.currentThread() )
                if (it.isSuccessful){
                    val result: SignInMethodQueryResult? = it.result
                    if (result!=null && result.signInMethods!= null && result.signInMethods!!.size > 0){
                        //User already exists
                        userExists(phoneMail, password, method);
                    }else{
                        //No User found for this mail
                        phoneEt.setError("User not found")
                        phoneEt.requestFocus()
                        return@addOnCompleteListener
                    }
                }else{
                    Log.e("**", "fetchSignInMethods: Failed" )
                }
        }
    }


    private fun userExists(phoneMail: String, password: String, method: String) {
        when(method){
            SIGN_IN_METHOD_PASSWORD ->{
                auth.signInWithEmailAndPassword(phoneMail+GlobalStrings.mailExtension, password).addOnCompleteListener(this)
                { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("**", "signInWithEmail:success")
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

            SIGN_IN_METHOD_OTP->{
                val otpVerificationIntent = Intent(this, OtpVerificationActivity::class.java)
                otpVerificationIntent.putExtra(GlobalStrings.phone, phoneMail)
                startActivity(otpVerificationIntent)

            }
        }
    }


}