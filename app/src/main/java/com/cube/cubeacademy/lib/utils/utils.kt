package com.cube.cubeacademy.lib.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cube.cubeacademy.R

// renders error Toast
fun displayErrorToast(context: Context, requestName: String){
    Toast.makeText(context, "An error occurred $requestName. Try again later.", Toast.LENGTH_SHORT).show()
}

// applies color to a substring
fun TextView.colorSubstring(substring: String) {
    val fullText = this.text.toString()

    // check if the substring exists in the text, else set original text
    if (fullText.contains(substring)) {
        val spannableString = SpannableString(fullText)

        val startIndex = fullText.indexOf(substring)
        val endIndex = startIndex + substring.length

        val color = ContextCompat.getColor(this.context, R.color.pink)
        val colorSpan = ForegroundColorSpan(color)

        spannableString.setSpan(colorSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        this.text = spannableString
    } else {
        this.text = fullText
    }
}
