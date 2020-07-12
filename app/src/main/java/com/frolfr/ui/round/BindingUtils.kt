package com.frolfr.ui.round

import android.widget.TextView
import androidx.databinding.BindingAdapter

// TODO consolidate BindingUtils files somewhere...

@BindingAdapter("par")
fun TextView.setPar(item: RoundReportingViewModel) {
    text = item.getPar().toString()
}