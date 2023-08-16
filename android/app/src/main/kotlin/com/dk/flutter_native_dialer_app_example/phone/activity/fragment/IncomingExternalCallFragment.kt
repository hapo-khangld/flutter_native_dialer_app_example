package com.dk.flutter_native_dialer_app_example.phone.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dk.flutter_native_dialer_app_example.R
import com.dk.flutter_native_dialer_app_example.databinding.FragmentIncomingExternalCallBinding
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsControlsViewModel
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsViewModel

class IncomingExternalCallFragment : Fragment() {

    private val extCallsViewModel: ExternalCallsViewModel by activityViewModels()
    private val extControlsViewModel: ExternalCallsControlsViewModel by activityViewModels()

    private lateinit var binding: FragmentIncomingExternalCallBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_incoming_external_call, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.controlsViewModel = extControlsViewModel
        binding.callsViewModel = extCallsViewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}