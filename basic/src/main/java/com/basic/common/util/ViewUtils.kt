package com.basic.common.util

import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import com.basic.common.extension.getColorCompat
import com.widget.R


object ViewUtils {
    /**
     * Dim the background when PopupWindow shows
     * Should be called from showAsDropDown function
     * because this is when container's parent is
     * initialized
     */
    fun isVisible(view: View, activity: Activity?): Boolean {
        if (!view.isShown) {
            return false
        }

        if (activity == null){
            return false
        }

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val actualPosition = Rect()
        view.getGlobalVisibleRect(actualPosition)
        val screen = Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        return actualPosition.intersect(screen)
    }

//    fun dimBehind(contentView: View) {
//        if (BehaviourPreferences.isDimmingOn()) {
//            val container = contentView.rootView
//            val windowManager = contentView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//            val layoutParams = container.layoutParams as WindowManager.LayoutParams
//            layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
//            layoutParams.dimAmount = getDimValue(contentView.context)
//            windowManager.updateViewLayout(container, layoutParams)
//        }
//    }

//    fun View.setMargins(marginLeft: Int, marginTop: Int, marginRight: Int, marginBottom: Int) {
//        val params: LinearLayout.LayoutParams =
//            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        params.setMargins(marginLeft, marginTop, marginRight, marginBottom)
//        this.layoutParams = params
//    }

//    private fun createGradientBackground(@ColorInt startColor: Int, @ColorInt endColor: Int, YOUR_COLOR: Int) =
//        GradientDrawable(
//                GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
//                startColor,
//                endColor
//        )).also {
//            it.shape = GradientDrawable.RECTANGLE
//            it.cornerRadius = AppearancePreferences.getCornerRadius().toFloat()
//            it.setStroke(1, YOUR_COLOR)
//        }

    /**
     * Get screen dim value based on interface theme
     */
//    fun getDimValue(context: Context): Float {
//        val outValue = TypedValue()
//        context.resources.getValue(dimen.screen_dim, outValue, true)
//        return outValue.float
//    }

    /**
     * Adds outline shadows to the view using the accent color
     * of the app
     *
     * @param contentView [View] that needs to be elevated with colored
     *                    shadow
     */
    fun addShadow(contentView: View) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            contentView.outlineAmbientShadowColor = contentView.context.getColorCompat(R.color.tools_theme)
            contentView.outlineSpotShadowColor = contentView.context.getColorCompat(R.color.tools_theme)
        }
    }

    /**
     * Makes the view go away
     */
    fun View.gone() {
        clearAnimation()
        this.visibility = View.GONE
    }
    fun View.show(){

        this.visibility = View.VISIBLE
    }
    /**
     * Makes the view go away
     *
     * @param animate adds animation to the process
     */
//    fun View.invisible(animate: Boolean) {
//        if (animate) {
//            clearAnimation()
//            this.animate()
//                .scaleY(0F)
//                .scaleX(0F)
//                .alpha(0F)
//                .setInterpolator(AccelerateInterpolator())
//                .setDuration(this.resources.getInteger(R.integer.animation_duration).toLong())
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationStart(animation: Animator?) {
//                        /* no-op */
//                    }
//
//                    override fun onAnimationEnd(animation: Animator?) {
//                        this@invisible.visibility = View.INVISIBLE
//                    }
//
//                    override fun onAnimationCancel(animation: Animator?) {
//                        /* no-op */
//                    }
//
//                    override fun onAnimationRepeat(animation: Animator?) {
//                        /* no-op */
//                    }
//                })
//                .start()
//        } else {
//            this.visibility = View.INVISIBLE
//        }
//    }

    /**
     * Makes the view come back
     *
     * @param animate adds animation to the process
     */
//    fun View.visible(animate: Boolean) {
//        if (animate) {
//            clearAnimation()
//            this.animate()
//                .scaleY(1F)
//                .scaleX(1F)
//                .alpha(1F)
//                .setInterpolator(LinearOutSlowInInterpolator())
//                .setDuration(this.resources.getInteger(R.integer.animation_duration).toLong())
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationStart(animation: Animator?) {
//                        this@visible.visibility = View.VISIBLE
//                    }
//
//                    override fun onAnimationEnd(animation: Animator?) {
//                        /* no-op */
//                    }
//
//                    override fun onAnimationCancel(animation: Animator?) {
//                        /* no-op */
//                    }
//
//                    override fun onAnimationRepeat(animation: Animator?) {
//                        /* no-op */
//                    }
//                })
//                .start()
//        } else {
//            this.visibility = View.VISIBLE
//        }
//    }

    // ViewExtensions

    fun View.fadOutAnimation(duration: Long = 300, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
        animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction {
                this.visibility = visibility
                completion?.let {
                    it()
                }
            }
    }

    fun View.fadInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(duration)
            .withEndAction {
                completion?.let {
                    it()
                }
            }
    }
}