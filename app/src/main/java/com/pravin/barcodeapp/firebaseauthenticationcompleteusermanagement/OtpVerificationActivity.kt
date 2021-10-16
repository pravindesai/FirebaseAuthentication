package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.GlobalStrings
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.UniversalProgressDialog
import kotlinx.android.synthetic.main.activity_otp_verification.*
import java.util.*
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity() {

    private val TAG: String = "**OptVerificationActivity"
    lateinit var verificationCode:String
    lateinit var auth:FirebaseAuth

    lateinit var fname   :String
    lateinit var lname   :String
    lateinit var phone   :String
    lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

             auth = FirebaseAuth.getInstance()

             fname    = intent.getStringExtra(GlobalStrings.fname).toString()
             lname    = intent.getStringExtra(GlobalStrings.lname).toString()
             phone    = intent.getStringExtra(GlobalStrings.phone).toString()
             password = intent.getStringExtra(GlobalStrings.password  ).toString()

            sendVerificationCode(phone)

            verifyButton.setOnClickListener {
                UniversalProgressDialog.show(this)
                submitOtp()
            }

            otpEt.doAfterTextChanged {
                //submitOtp()
            }

    }

    private fun submitOtp() {
        val code:String = otpEt.text.toString()
        if (code.isBlank() || code.isEmpty() || code.length<6){
            editTextOtp.setError("")
            editTextOtp.requestFocus()
            UniversalProgressDialog.hide()
            return
        }
        signInWithPhoneAuthCredential(code)
    }

    //Turn on phone authentication from FirebaseProject->Authentication->Sign-InMethods
    //Turn on email authentication from FirebaseProject->Authentication->Sign-InMethods
    //Add SHA1 and SHA 256 key to firebase project REFERENCE: https://www.youtube.com/watch?v=lVSSZ84HBAU
    private fun sendVerificationCode(phone: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+phone)       // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            val code = credential.smsCode
            Log.e(TAG, "onVerificationCompleted: $code" )
            if (code!=null){
                signInWithPhoneAuthCredential(code)
            }else{
                signInWithPhoneAuthCredential(credential)
            }
            UniversalProgressDialog.hide()

        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("**", "onVerificationFailed", e)
            UniversalProgressDialog.hide()
            Toast.makeText(this@OtpVerificationActivity, "Verification failed.", Toast.LENGTH_SHORT).show()
            // Show a message and update the UI
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.e(TAG, "onCodeSent ")
            verificationCode = verificationId

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){

                if (auth.currentUser?.email.isNullOrBlank()) {
                    linkMailForPasswordAuth()
                }else{
                    Log.e(TAG, "signInWithPhoneAuthCredential: "+auth.currentUser?.email )
                }


                val intent = Intent(this@OtpVerificationActivity, ProfileActivity::class.java)
                intent.putExtra( GlobalStrings.fname,    fname)
                intent.putExtra( GlobalStrings.lname,    lname)
                intent.putExtra( GlobalStrings.phone,    phone)
                intent.putExtra( GlobalStrings.password, password)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                Log.e(TAG, "**signInWithPhoneAuthCredential: Uniques uid--> "+auth.currentUser?.uid )

                UniversalProgressDialog.hide()
                startActivity(intent)
                this.finishAffinity()
            }else{
                Toast.makeText(this@OtpVerificationActivity, "Failed*", Toast.LENGTH_SHORT).show()
                UniversalProgressDialog.hide()
            }
        }.addOnFailureListener {
            Toast.makeText(this@OtpVerificationActivity, "Failed", Toast.LENGTH_SHORT).show()
            UniversalProgressDialog.hide()
        }
    }
    private fun signInWithPhoneAuthCredential(code:String) {
            val credential = PhoneAuthProvider.getCredential(verificationCode, code)
            signInWithPhoneAuthCredential(credential)
    }

    //2021
    /*
    Firebase dose not provide Phone + Password verification after otp verification
    for that creating uniques mail based on phone number and using mail + password verification
     * */
    private fun linkMailForPasswordAuth() {
        val phoneMail = phone+GlobalStrings.mailExtension;
        val password = password;
        val authCredential:AuthCredential = EmailAuthProvider.getCredential(phoneMail, password);

        auth.currentUser?.linkWithCredential(authCredential)?.addOnCompleteListener {
            if (it.isSuccessful){
                Log.e(TAG, "linkMailForPasswordAuth: Linked Sucessfully" )
            }else{
                Log.e(TAG, "linkMailForPasswordAuth: Linked Failed "+it.exception )
            }
        }?.addOnFailureListener {
            Log.e(TAG, "linkMailForPasswordAuth: Linked Failed messsage "+it.message )
        }
    }

}

