package com.cclss.chattering.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cclss.chattering.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var callbackManager: CallbackManager
    lateinit var googleSigneInClient: GoogleSignInClient

    lateinit var twitterAuthClient: TwitterAuthClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authConfig = TwitterAuthConfig(
            getString(R.string.twitter_consumer_key),
            getString(R.string.twitter_consumer_secret)
        )

        val twitterConfig = TwitterConfig.Builder(this)
            .twitterAuthConfig(authConfig)
            .build()

        Twitter.initialize(twitterConfig)
        twitterAuthClient = TwitterAuthClient()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        val provider = OAuthProvider.newBuilder("twitter.com")
        provider.addCustomParameter("lang","fr")

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSigneInClient = GoogleSignIn.getClient(this, gso)

        btnFb.setReadPermissions("email")
        // Callback registration
        btnFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
                handleFacebookAccessToken(loginResult!!.accessToken)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })

        btnGg.setOnClickListener {
            val signInIntent = googleSigneInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        val firebaseUser: FirebaseUser? = auth.currentUser

        btnTw.setOnClickListener {
//            firebaseUser?.startActivityForLinkWithProvider(this, provider.build())
//                ?.addOnSuccessListener {
//                    toast("로그인 성공")
//                }?.addOnFailureListener {
//                    toast("로그인 실패")
//            }
            twitterAuthClient?.authorize(this, object : Callback<TwitterSession>() {
                override fun failure(exception: TwitterException?) {
                    toast("트위터 로그인실패")
                }

                override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                    if (result != null) {
                        handleTwitterSession(result.data)
                        toast("트위터 로그인성공")
                    }
                }

            })
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                //구글 로그인이 성공했을 경우
                Toast.makeText(
                    baseContext, "구글 로그인 성공",
                    Toast.LENGTH_SHORT
                ).show()
                val account = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
            }
        } else {
            callbackManager?.let {
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
            twitterAuthClient?.let {
                twitterAuthClient.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun handleTwitterSession(session: TwitterSession) {
        Log.d("MainActivity", "handleTwitterSession:$session")
        val credential = TwitterAuthProvider.getCredential(
            session.authToken.token,
            session.authToken.secret
        )

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MainActivity", "signInWithCredential:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("MainActivity", "signInWithCredential:failure", task.exception)
                    toast("Authentication failed.")
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("login", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("login", "signInWithCredential:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun toast(msg: String) {
        Toast.makeText(
            this, msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}

