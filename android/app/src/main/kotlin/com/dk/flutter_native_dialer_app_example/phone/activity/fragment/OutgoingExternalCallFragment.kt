package com.dk.flutter_native_dialer_app_example.phone.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dk.flutter_native_dialer_app_example.databinding.FragmentOutgoingExternalCallBinding
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsControlsViewModel
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsViewModel

class OutgoingExternalCallFragment: Fragment() {

    private val extCallsViewModel: ExternalCallsViewModel by activityViewModels()
    private val extControlsViewModel: ExternalCallsControlsViewModel by activityViewModels()

    private var _binding: FragmentOutgoingExternalCallBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOutgoingExternalCallBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * All business logics are handled here
         */
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            callsViewModel = extCallsViewModel
            controlsViewModel = extControlsViewModel
        }

    }
}