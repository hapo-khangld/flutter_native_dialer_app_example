package com.dk.flutter_native_dialer_app_example.phone.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dk.flutter_native_dialer_app_example.R
import com.dk.flutter_native_dialer_app_example.databinding.FragmentExternalSingleCallBinding
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsControlsViewModel
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsViewModel

class ExternalSingleCallFragment : Fragment() {

    private val TAG: String = ExternalSingleCallFragment::class.java.name

    private val externalCallsViewModel: ExternalCallsViewModel by activityViewModels()
    private val externalControlsViewModel: ExternalCallsControlsViewModel by activityViewModels()


    private lateinit var binding: FragmentExternalSingleCallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_external_single_call, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            controlsViewModel = externalControlsViewModel
            callsViewModel = externalCallsViewModel
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * All business logics are handled here
         */

        externalCallsViewModel.currentCall.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                val timer = binding.root.findViewById<Chronometer>(R.id.active_call_timer)
                //timer.base = SystemClock.elapsedRealtime() - it.details.connectTimeMillis
                timer.start()
            }
        }

    }

}