package com.dk.flutter_native_dialer_app_example.phone.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dk.flutter_native_dialer_app_example.databinding.FragmentCallsListBinding
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsControlsViewModel
import com.dk.flutter_native_dialer_app_example.phone.activity.viewModels.ExternalCallsViewModel

class ExternalCallsListFragment : Fragment() {

    private var _binding: FragmentCallsListBinding? = null
    private val callsViewModel: ExternalCallsViewModel by activityViewModels()
    private val controlsViewModel: ExternalCallsControlsViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCallsListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        binding.setCallClickListener {
            binding.dialerViewModel.startCall()
        }
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}