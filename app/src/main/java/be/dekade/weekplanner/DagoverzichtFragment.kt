package be.dekade.weekplanner

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import be.dekade.weekplanner.adapters.ARG_WEEKDAG
import be.dekade.weekplanner.adapters.ActiviteitListItemAdapter
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensDag
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.databinding.FragmentDagoverzichtBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class DagoverzichtFragment : Fragment() {

    @Inject
    lateinit var repository: ActiviteitEnDagGegevensRepository

    private var _binding: FragmentDagoverzichtBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: DagOverzichtViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateViewModel()
        _binding = FragmentDagoverzichtBinding.inflate(inflater, container, false)
        val adapter = ActiviteitListItemAdapter(ActiviteitListItemAdapter.UitstelClickListener {
            setUitstelUur(it)
        }, ActiviteitListItemAdapter.AfgewerktClickListener {
            lifecycleScope.launch {
                updateDagGegevens(it)
            }
        }, ActiviteitListItemAdapter.DetailClickListener {
            navigateToDetails(it)
        }
        )

        binding.setLifecycleOwner(this)
        binding.activiteitenlijst.adapter = adapter

        subscribeUi(adapter, binding)
        return binding.root
    }

    private fun subscribeUi(
        adapter: ActiviteitListItemAdapter,
        binding: FragmentDagoverzichtBinding
    ) {
        viewModel.activiteiten.observe(viewLifecycleOwner) { result ->
            binding.heeftActiviteiten = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }

    fun navigateToDetails(gegevensDag: ActiviteitEnDagGegevensDag) {
        val directions = WeekoverzichtFragmentDirections.actionFirstFragmentToActiviteitDetailFragment(gegevensDag.activiteit.activiteitId)
        findNavController().navigate(directions)
    }

    fun setUitstelUur(gegevensDag: ActiviteitEnDagGegevensDag) {
        val tsl = TimePickerDialog.OnTimeSetListener{ tp, hour, minute ->
            gegevensDag.dagGegevens.uitstelUur = hour
            gegevensDag.dagGegevens.uitstelMinuut = minute
            lifecycleScope.launch {
                updateDagGegevens(gegevensDag)
                binding.activiteitenlijst.adapter?.notifyDataSetChanged()
            }
        }
        val newFragment = TimePickerDialog(this.context, tsl, gegevensDag.activiteit.startuur, gegevensDag.activiteit.startminuut, true)
        newFragment.show()
    }

    suspend fun updateDagGegevens(gegevensDag: ActiviteitEnDagGegevensDag) =
        withContext(Dispatchers.IO) {
            repository.updateDagGegevens(gegevensDag.dagGegevens)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateViewModel() {
        val weekdag = arguments?.getInt(ARG_WEEKDAG)
        if (weekdag != null) {
            viewModel.setWeekDag(weekdag)
        }
    }
}