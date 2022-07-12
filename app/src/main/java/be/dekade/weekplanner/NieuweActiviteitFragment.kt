package be.dekade.weekplanner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import be.dekade.weekplanner.databinding.FragmentNieuweActiviteitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NieuweActiviteitFragment : Fragment() {

    private val viewModel: NieuweActiviteitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNieuweActiviteitBinding.inflate(inflater, container, false)
        context?: return binding.root

        binding.viewModel=viewModel

        viewModel.eventActiviteitSubmitted.observe(viewLifecycleOwner, Observer { isSubmitted ->
            if(isSubmitted){
                viewModel.onSubmitComplete()
                findNavController().navigateUp()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }
}