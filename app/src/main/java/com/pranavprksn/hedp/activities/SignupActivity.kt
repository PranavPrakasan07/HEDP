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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.pranavprksn.hedp.ChatHome
import com.pranavprksn.hedp.MainActivity
import com.pranavprksn.hedp.R
import java.util.*

class SignupActivity : AppCompatActivity() {
    private var signupButton: AppCompatButton? = null
    private var loginLink: TextView? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var contact: EditText? = null
    private var auth: FirebaseAuth? = null
    private var db = FirebaseFirestore.getInstance()
    private var user_details: MutableMap<String, String?> = HashMap()
    private var reference: DatabaseReference? = null

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                addUserToDB()
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signupButton = findViewById(R.id.signup_button)
        loginLink = findViewById(R.id.login_link)

        loginLink?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    applicationContext, SignupActivity::class.java
                )
            )
        })

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        contact = findViewById(R.id.contact_number)
        signupButton = findViewById(R.id.signup_button)
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

        signupButton?.setOnClickListener(View.OnClickListener { v: View? ->
            val email_text = email?.text.toString()
            val password_text = password?.text.toString()
            val contact_text = contact?.text.toString()
            if (email_text == "") {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
            }
            if (password_text == "") {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
            }
            if (contact_text == "") {
                Toast.makeText(this, "Enter your mobile number", Toast.LENGTH_SHORT).show()
            }
            if (!(email_text == "" || password_text == "" || contact_text == "")) {
                signup(email_text, password_text)
            }
        })
    }

    private fun signup(email_text: String, password_text: String) {
        auth!!.createUserWithEmailAndPassword(email_text, password_text)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    userid = FirebaseAuth.getInstance().uid
                    user_details["userid"] = userid
                    user_details["email"] =
                        Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)
                            ?.email
                    val sharedPreferences = getSharedPreferences("VERIFIED", MODE_PRIVATE)
                    val isVerified = sharedPreferences.getBoolean("mobile_verified", false)
                    val mobile_number = sharedPreferences.getString("mobile_number", null)

                    user_details["email"]?.let { addUserToDB() };

                    if (isVerified) {
                        user_details["mobile"] = mobile_number
                    }
                    assert(userid != null)
                    db.collection("users").document(userid!!)
                        .set(user_details, SetOptions.merge())
                        .addOnSuccessListener { unused: Void? ->
                            Log.d(
                                "TAG",
                                "Successful document write"
                            )
                        }
                        .addOnFailureListener { e: Exception? ->
                            Log.d(
                                "TAG",
                                "Failed document write"
                            )
                        }
                    startActivity(Intent(applicationContext, ChatHome::class.java))
                    Toast.makeText(applicationContext, "Signed up successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Signed up failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun addUserToDB() {

        val firebaseUser = auth!!.currentUser!!
        val userid = firebaseUser.uid
        val username = firebaseUser.displayName.toString()
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["id"] = userid
        hashMap["username"] = username
        hashMap["imageURL"] = firebaseUser.photoUrl.toString()
        hashMap["status"] = "offline"
        hashMap["search"] = username.lowercase(Locale.getDefault())

        reference!!.setValue(hashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("DBData", "Stored to Realtime DB")
                }
            }

    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    userid = FirebaseAuth.getInstance().uid
                    user_details["userid"] = userid
                    user_details["email"] =
                        Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)
                            ?.email
                    user_details["photo"] =
                        FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()
                    user_details["phone"] =
                        Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)
                            ?.phoneNumber
                    val sharedPreferences = getSharedPreferences("VERIFIED", MODE_PRIVATE)
                    val isVerified = sharedPreferences.getBoolean("mobile_verified", false)
                    val mobile_number = sharedPreferences.getString("mobile_number", null)
                    if (isVerified) {
                        user_details["mobile"] = mobile_number
                    }
                    assert(userid != null)
                    db.collection("users").document(userid!!)
                        .set(user_details, SetOptions.merge())
                        .addOnSuccessListener { unused: Void? ->
                            Log.d(
                                "TAG",
                                "Successful document write"
                            )
                        }
                        .addOnFailureListener { e: Exception? ->
                            Log.d(
                                "TAG",
                                "Failed document write"
                            )
                        }
                    startActivity(Intent(applicationContext, ChatHome::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(applicationContext, "Invalid Sign in", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 1
        var userid: String? = null
    }
}