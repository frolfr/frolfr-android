package com.frolfr.ui.round

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frolfr.R

class RoundReportingFragment : Fragment() {

//    private lateinit var binding: // TODO

//    private lateinit var viewModel: // TODO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hole_input, container, false)
    }
}