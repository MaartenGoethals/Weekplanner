package be.dekade.weekplanner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.dekade.weekplanner.databinding.FragmentNieuweActiviteitBinding

class NieuweActiviteitFragment : Fragment() {

    private var _binding: FragmentNieuweActiviteitBinding? = null

    companion object {
        fun newInstance() = NieuweActiviteitFragment()
    }

    private lateinit var viewModel: NieuweActiviteitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nieuwe_activiteit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NieuweActiviteitViewModel::class.java)
        // TODO: Use the ViewModel
    }

}