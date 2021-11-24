package com.pranavprksn.hedp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pranavprksn.hedp.ChatHome
import com.pranavprksn.hedp.R

class LoginActivity : AppCompatActivity() {
    private var loginButton: AppCompatButton? = null
    private var signupLink: TextView? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    public override fun onStart() {
        super.onStart()
        val currentUser = auth!!.currentUser
        if (currentUser != null) {
            startActivity(Intent(applicationContext, ChatHome::class.java))
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signupLink = findViewById(R.id.signup_link)
        loginButton = findViewById(R.id.login_button)
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("324570600050-mmlibt48s2ij5ga5e8cm8m60nmktoubb.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        findViewById<View>(R.id.google_button).setOnClickListener { view: View ->
            if (view.id == R.id.google_button) {
                signIn()
            }
        }

        loginButton?.setOnClickListener { v: View? ->
            val email_text = email?.text.toString()
            val password_text = password?.text.toString()
            if (email_text == "") {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
            }
            if (password_text == "") {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
            }
            if (!(email_text == "" || password_text == "")) {
                login_user(email_text, password_text)
            }
        }

        signupLink?.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, SignupActivity::class.java
                )
            )
        }
    }

    private fun login_user(email_text: String, password_text: String) {
        auth!!.signInWithEmailAndPassword(email_text, password_text)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    startActivity(Intent(applicationContext, ChatHome::class.java))
                    Toast.makeText(this@LoginActivity, "Successfully logged in", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    startActivity(Intent(applicationContext, ChatHome::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(applicationContext, "Invalid Login", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 1
        var auth: FirebaseAuth? = null
        var userid: String? = null
    }
}