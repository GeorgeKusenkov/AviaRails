package com.egasmith.aviarails.ui.features

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class TextInputManager(private val startCity: TextInputEditText, private val endCity: TextInputEditText) {
    private var startCityFilled = false
    private var endCityFilled = false

    fun onStartCityTextChanged(text: String) {
        startCityFilled = text.isNotEmpty()
        updateImeOptions()
    }

    fun onEndCityTextChanged(text: String) {
        if (containsOnlyCyrillic(text)) {
            endCityFilled = text.isNotEmpty()
        } else {
            endCityFilled = false
            if (text.isNotEmpty()) {
                endCity.setText("")
                Toast.makeText(endCity.context, "Пожалуйста, используйте только кириллицу", Toast.LENGTH_SHORT).show()
            }
        }
        updateImeOptions()
    }

    fun areFieldsFilled() = startCityFilled && endCityFilled

    fun updateImeOptions() {
        val imeOptions = if (areFieldsFilled()) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
        startCity.imeOptions = imeOptions
        endCity.imeOptions = imeOptions

        val imm = startCity.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.restartInput(startCity)
        imm?.restartInput(endCity)
    }

    private fun containsOnlyCyrillic(input: String): Boolean = input.matches(Regex("[а-яА-ЯёЁ -]+"))
}