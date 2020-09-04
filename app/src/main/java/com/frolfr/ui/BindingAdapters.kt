package com.frolfr.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

val dateFormat = SimpleDateFormat("MM/dd/yyyy")

@BindingAdapter("date")
fun TextView.setDate(item: Date?) {
    text = item?.let { dateFormat.format(it) } ?: ""
}

@BindingAdapter("score")
fun TextView.setScore(score: Int) {
    text = when {
        score <= 0 -> score.toString()
        else -> "+${score}"
    }
}