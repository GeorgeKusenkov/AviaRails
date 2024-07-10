package com.egasmith.aviarails.ui.features

import android.animation.ValueAnimator
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout

class CardAnimator(private val cardView: CardView, private val constraintLayout: ConstraintLayout) {
    private var isExpanded = false
    private val originalSize: Pair<Int, Int> by lazy { Pair(cardView.width, cardView.height) }

    fun expandCard() {
        if (isExpanded) return
        animateCard(0f, 1f)
        isExpanded = true
    }

    fun collapseCard() {
        if (!isExpanded) return
        animateCard(1f, 0f)
        isExpanded = false
    }

    private fun animateCard(start: Float, end: Float) {
        ValueAnimator.ofFloat(start, end).apply {
            duration = 300
            addUpdateListener {
                updateCardSize(it.animatedValue as Float)
            }
            start()
        }
    }

    private fun updateCardSize(progress: Float) {
        val params = cardView.layoutParams as ConstraintLayout.LayoutParams
        val newWidth = (originalSize.first + (constraintLayout.width - originalSize.first) * progress).toInt()
        val newHeight = (originalSize.second + (constraintLayout.height - originalSize.second) * progress).toInt()

        params.width = newWidth
        params.height = newHeight
        cardView.layoutParams = params
    }
}