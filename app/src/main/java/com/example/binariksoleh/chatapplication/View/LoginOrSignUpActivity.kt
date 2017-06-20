package com.example.binariksoleh.chatapplication.Activity

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.binariksoleh.chatapplication.Helper.*
import com.example.binariksoleh.chatapplication.Model.UserModel
import com.example.binariksoleh.chatapplication.R
import com.example.binariksoleh.chatapplication.View.ChatRoomsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login_or_sign_up.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginOrSignUpActivity : AppCompatActivity() {

    private var actionFlag: Boolean = true
    private lateinit var firebaseAuthenticationManager: FirebaseAuth
    private lateinit var firebaseUsersReference: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHelper.hideStatusBar(this)
        setContentView(R.layout.activity_login_or_sign_up)
        signInText.setTextColor(Color.GRAY)
        firebaseAuthenticationManager = FirebaseAuth.getInstance()
        firebaseUsersReference = FirebaseDatabase.getInstance().getReference("users")

        firebaseUsersReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val user = FirebaseUsersHelper.getUserDataFromSnapshot(p0!!)
                if (user.name == FirebaseAuth.getInstance().currentUser?.email) {
                    SharedPreferencesHelper.setCurrentUserId(p0.key, applicationContext)
                }
            }

        })

        val user = firebaseAuthenticationManager.currentUser

        if (user == null) {
            /**
             * User is signed out
             */
        } else {
            /**
             * User is signed in
             * Load next Activity
             */
            startActivity(intentFor<ChatRoomsActivity>())

        }



        signUpText.setOnClickListener {
            actionFlag = true
            AnimationHelper.changeImageWithFadeAnimation(this, topImage, actionFlag)
            submitButton.setText(R.string.sign_up)
            signInText.setTextColor(Color.GRAY)
            signUpText.setTextColor(Color.WHITE)
            AnimationHelper.changeTextWithAnimation(submitButton, actionFlag)

        }

        signInText.setOnClickListener {
            actionFlag = false
            AnimationHelper.changeImageWithFadeAnimation(this, topImage, actionFlag)
            submitButton.setText(R.string.sign_in)
            signInText.setTextColor(Color.WHITE)
            signUpText.setTextColor(Color.GRAY)
            AnimationHelper.changeTextWithAnimation(submitButton, actionFlag)
        }

        submitButton.setOnClickListener {
            if (CredentialsVerifier.checkCredentials(userEmail, userPassword)) {
                progressDialog = indeterminateProgressDialog("Processing…")
                /**
                 * Sign up section is active
                 */
                if (actionFlag) {
                    val userModel = UserModel("REGISTERED_USER", CredentialsVerifier.validateString(userEmail.text.toString()), arrayListOf("-Kn49mHBgBFOA_zHZHon", "-Kn4PPCPSiSAXzrRW2br"))
                    firebaseUsersReference.push().setValue(userModel)
                    registerUser(CredentialsVerifier.validateString(userEmail.text.toString()), userPassword.text.toString())

                }
                /**
                 * Sign in section is active
                 */
                else {
                    val userModel = UserModel("REGISTERED_USER", CredentialsVerifier.validateString(userEmail.text.toString()), arrayListOf("-Kn49mHBgBFOA_zHZHon", "-Kn4PPCPSiSAXzrRW2br"))
                    loginUser(CredentialsVerifier.validateString(userEmail.text.toString()), userPassword.text.toString())
                }
            }
        }
    }


    /**
     * Performs a user registration via Firebase authentication manager
     * @param email - user's email
     * @param password - user's password (bigger than six characters long)
     */
    private fun registerUser(email: String, password: String) {
        firebaseAuthenticationManager.createUserWithEmailAndPassword(email.trim(), password.trim())
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    toast("Registration successful.")
                    startActivity(intentFor<ChatRoomsActivity>())
                }

                .addOnFailureListener {
                    progressDialog.dismiss()
                    toast("Error occurred. Please, try again later.")
                }


    }

    /**
     * Performs a user login via Firebase authentication manager
     * @param email - user's email
     * @param password - user's password (bigger than six characters long
     */
    private fun loginUser(email: String, password: String) {
        firebaseAuthenticationManager.signInWithEmailAndPassword(email, password)

                .addOnSuccessListener {
                    progressDialog.dismiss()
                    toast("Logged in successfully.")
                    startActivity(intentFor<ChatRoomsActivity>())

                }

                .addOnFailureListener {
                    progressDialog.dismiss()
                    toast("Error occurred. Please, try again.")
                }


    }

}
