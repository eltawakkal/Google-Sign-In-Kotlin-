package com.example.googlesignin_kotlin

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

//    variables
    private  val RC_SIGN_IN: Int = 12
//    view
    lateinit var tvUserName: TextView
    lateinit var tvUserEmail: TextView
    lateinit var btGoogleSignIn: SignInButton
    lateinit var imgUser: CircleImageView

//    google
    lateinit var gso: GoogleSignInOptions
    lateinit var gsa: GoogleSignInAccount
    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView();

        btGoogleSignIn.setOnClickListener {
            singIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            updateUi(task)
        }
    }

    fun logout(view: View) {
        gsc.signOut()

        tvUserName.text = "User Name Here"
        tvUserEmail.text = "User Email Here"
    }

    private fun updateUi(completedTask: Task<GoogleSignInAccount>?) {
        try {
            gsa = completedTask?.getResult(ApiException::class.java)!!

            tvUserName.text = gsa.displayName
            tvUserEmail.text = gsa.email

            Glide.with(this).load(gsa.photoUrl).into(imgUser)

            Toast.makeText(this, "Url: " + gsa.photoUrl, Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            Log.d("kesalahan", "err: " + e.toString())
        }
    }

    private fun singIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun initView() {
        tvUserName = findViewById(R.id.tv_name_user)
        tvUserEmail = findViewById(R.id.tv_email_user)
        btGoogleSignIn = findViewById(R.id.bt_google_sign_in)
        imgUser = findViewById(R.id.img_user)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)
    }

}
