package com.frolfr.api

import android.widget.TextView
import androidx.databinding.BindingAdapter

fun getScoreString(score: Int): String {
    return when {
        score <= 0 -> score.toString()
        else -> "+${score}"
    }
}

@BindingAdapter("score")
fun TextView.setScore(score: Int) {
    text = getScoreString(score)
}