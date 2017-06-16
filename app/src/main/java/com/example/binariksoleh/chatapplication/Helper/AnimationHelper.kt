package com.example.binariksoleh.chatapplication.Helper

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import com.example.binariksoleh.chatapplication.R
import android.animation.ValueAnimator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.widget.Button


class AnimationHelper {
    companion object {

        private fun createFadeInAnimation(activity: Activity): Animation {
            return AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        }


        private fun createFadeOutAnimation(activity: Activity): Animation {
            return AnimationUtils.loadAnimation(activity, R.anim.fade_out)
        }


        /**
         * @param activity - activity to load animation
         * @param view - view to apply animation and image change to
         * @param flag - for detecting what image should be set into ImageView
         */
        fun changeImageWithFadeAnimation(activity: Activity, imageView: ImageView, flag: Boolean) {
            val fadeOutAnimation = createFadeOutAnimation(activity)
            imageView.startAnimation(fadeOutAnimation)
            fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    /**
                     * No need to implement
                     */
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    /**
                     * No need to implement
                     */
                }

                override fun onAnimationEnd(animation: Animation?) {
                    imageView.startAnimation(createFadeInAnimation(activity))

                    if (flag) {
                        imageView.setImageResource(R.drawable.first_screen_bg)
                        imageView.scaleType = ImageView.ScaleType.FIT_XY
                    } else {
                        imageView.setImageResource(R.drawable.first_screen_alternate_bg)
                        imageView.scaleType = ImageView.ScaleType.FIT_XY

                    }
                }
            })
        }

        /**
         * @param button - button, which text needs to be changed
         * @param flag - boolean flag to detect how the text of the button needs to be changed
         */
        fun changeTextWithAnimation(button: Button, flag: Boolean) {
            val colorAnim = ObjectAnimator.ofInt(button, "textColor", Color.parseColor("#B0D7FF"), Color.WHITE)
            colorAnim.duration = 1000
            colorAnim.setEvaluator(ArgbEvaluator())
            if (flag) {
                button.setText(R.string.sign_up)
            } else {
                button.setText(R.string.sign_in)
            }
            colorAnim.start()

        }
    }


}
