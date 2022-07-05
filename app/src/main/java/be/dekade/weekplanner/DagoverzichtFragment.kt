package be.dekade.weekplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import be.dekade.weekplanner.adapters.ARG_WEEKDAG
import be.dekade.weekplanner.adapters.ActiviteitListItemAdapter
import be.dekade.weekplanner.databinding.FragmentDagoverzichtBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class DagoverzichtFragment : Fragment() {

    private var _binding: FragmentDagoverzichtBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: DagOverzichtViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDagoverzichtBinding.inflate(inflater, container, false)
        val adapter = ActiviteitListItemAdapter()
        binding.activiteitenlijst.adapter = adapter

        updateViewModel()

        subscribeUi(adapter, binding)
        return binding.root

    }

    private fun subscribeUi(adapter: ActiviteitListItemAdapter, binding: FragmentDagoverzichtBinding) {
        viewModel.activiteiten.observe(viewLifecycleOwner) { result ->
            binding.heeftActiviteiten = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateViewModel(){
        val weekdag = arguments?.getInt(ARG_WEEKDAG)
        if (weekdag != null) {
            viewModel.setWeekDag(weekdag)
        }
    }
}