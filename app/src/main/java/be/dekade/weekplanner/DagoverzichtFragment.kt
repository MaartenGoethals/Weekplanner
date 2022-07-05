package be.dekade.weekplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import be.dekade.weekplanner.adapters.ActiviteitListItemAdapter
import be.dekade.weekplanner.databinding.FragmentDagoverzichtBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DagoverzichtFragment : Fragment() {

    private var _binding: FragmentDagoverzichtBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: DagOverzichtViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(DagOverzichtViewModel::class.java)
        _binding = FragmentDagoverzichtBinding.inflate(inflater, container, false)
        val adapter = ActiviteitListItemAdapter()
        binding.activiteitenlijst.adapter = adapter

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
}