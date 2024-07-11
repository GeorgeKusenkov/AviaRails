package com.egasmith.aviarails.ui.features

import android.animation.ValueAnimator
import android.util.LayoutDirection
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout

class CardAnimator(private val cardView: CardView, private val constraintLayout: ConstraintLayout) {
    private var isExpanded = false
    private val originalSize: Pair<Int, Int> by lazy { Pair(cardView.width, cardView.height) }

    fun expandCard() {
        Log.d("STATESLAST", "expandCard: 1состояние expand: $isExpanded")
        if (isExpanded) return
        Log.d("STATESLAST", "expandCard: 2состояние expand: $isExpanded")
        animateCard(0f, 1f)
        isExpanded = true
        Log.d("STATESLAST", "expandCard: 3состояние expand: $isExpanded")
    }

    fun collapseCard() {
        Log.d("STATESLAST", "expandCard: 1collapse expand: $isExpanded")
        if (!isExpanded) return
        Log.d("STATESLAST", "expandCard: 2collapse expand: $isExpanded")
        animateCard(1f, 0f)
        isExpanded = false
        Log.d("STATESLAST", "expandCard: 3collapse expand: $isExpanded")

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
        val newHeight = if (progress == 1f) ViewGroup.LayoutParams.MATCH_PARENT else originalSize.second

        params.width = newWidth
        params.height = newHeight
        cardView.layoutParams = params
    }
}