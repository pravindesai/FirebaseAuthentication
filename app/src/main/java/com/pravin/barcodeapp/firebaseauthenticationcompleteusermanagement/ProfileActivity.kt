package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util.GlobalStrings
import kotlinx.android.synthetic.main.activity_main.*

class ProfileActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idTv.text = auth.currentUser?.phoneNumber

        logOutButton.setOnClickListener {
            auth.signOut()
            val logoutIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(logoutIntent)
            this.finish()
        }

    }
}