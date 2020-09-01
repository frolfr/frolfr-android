package com.frolfr.ui.bindingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

val dateFormat = SimpleDateFormat("MM/dd/yyyy")

@BindingAdapter("date")
fun TextView.setDate(item: Date?) {
    text = item?.let { dateFormat.format(it) } ?: ""
}