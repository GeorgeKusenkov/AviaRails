package com.egasmith.aviarails.ui.features

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ViewStateHandler<T>(
    private val progressBar: ProgressBar,
    private val recyclerView: RecyclerView,
    private val context: Context
) {

    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    fun showData(data: List<T>, adapter: RecyclerView.Adapter<*>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = adapter
    }

    fun showError(message: String) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}