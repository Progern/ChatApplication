package com.example.binariksoleh.chatapplication.Helper

import android.widget.EditText
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo


class CredentialsVerifier {
    companion object {

        /**
         * @param field - EditText object
         * @return - true if the field is not empty
         */
        private fun checkField(field: EditText): Boolean {
            return !field.text.isEmpty()
        }

        /**
         * @return - true if the length of the user
         * password is bigger than 6 characters
         */
        private fun checkPasswordFieldForLength(userPassword: EditText): Boolean {
            return userPassword.text.toString().trim().length >= 6
        }

        /**
         * @param str - String that needs to be validated
         * in order to successfully be registered in Firebase
         */
        fun validateString(str: String): String {
            return str.trim().toLowerCase()
        }

        /**
         * Checks are all fields not empty
         * and is the password bigger than
         * six characters
         */
        fun checkCredentials(email: EditText, password: EditText): Boolean {
            if (!checkField(email)) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .playOn(email)
                return false
            }

            if (!checkField(password)) {
                YoYo.with(Techniques.Shake)
                        .duration(500)
                        .playOn(password)
                return false
            }

            if (!checkPasswordFieldForLength(password)) {
                YoYo.with(Techniques.Pulse)
                        .duration(500)
                        .playOn(password)
                return false
            }

            return true
        }
    }

}
