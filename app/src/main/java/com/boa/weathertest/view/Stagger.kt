package com.boa.weathertest.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.Fade
import androidx.transition.SidePropagation
import androidx.transition.TransitionValues

/**
 * Transition for stagger effect.
 */
// We extend Fade, so fade-in effect is handled by the parent. We customize and add a slight
// slide-up effect to it.
class Stagger : Fade(IN) {
    private val timeInterpolator: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
        PathInterpolatorCompat.create(0f, 0f, 0.2f, 1f)
    }

    init {
        // This duration is for a single item. See the comment below about propagation.
        duration = 300L / 2
        interpolator = timeInterpolator
        propagation = SidePropagation().apply {
            setSide(Gravity.BOTTOM)
            // We want the stagger effect to take as long as the duration of a single item.
            // In other words, the last item starts to fade in around the time when the first item
            // finishes animating. The overall animation will take about twice the duration of one
            // item fading in.
            setPropagationSpeed(1f)
        }
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        val view = startValues?.view ?: endValues?.view ?: return null
        // The parent can create an Animator for the fade-in.
        val fadeAnimator = super.createAnimator(sceneRoot, startValues, endValues) ?: return null
        return AnimatorSet().apply {
            playTogether(
                fadeAnimator,
                // We make the view to slide up a little as it fades in.
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.height * 0.5f, 0f)
            )
        }
    }
}
