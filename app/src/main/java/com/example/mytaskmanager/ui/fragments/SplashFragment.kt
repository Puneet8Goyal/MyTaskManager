package com.example.taskease.ui.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.taskease.R
import com.example.taskease.utils.AppConstants
import com.example.taskease.utils.launchNextPage


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.logo)
        setZoomForImage(imageView)
    }

    private fun setZoomForImage(view: View) {
        val sX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.1f, 1f)
        val sY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.1f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(sX, sY)
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = 1200
        animatorSet.setTarget(view)
        animatorSet.start()

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                requireContext().launchNextPage(
                    AppConstants.FragmentsTypes.taskTabsFragment,
                    finishAll = true
                )
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }

        })
    }


}