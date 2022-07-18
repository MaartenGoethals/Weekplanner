package be.dekade.weekplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import be.dekade.weekplanner.adapters.WeekdagPagerAdapter
import be.dekade.weekplanner.databinding.FragmentWeekoverzichtBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class WeekoverzichtFragment : Fragment() {
    //bindingproperties
    private var _binding: FragmentWeekoverzichtBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //ViewPagerproperties
    private lateinit var weekdagPagerAdapter: WeekdagPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeekoverzichtBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weekdagPagerAdapter = WeekdagPagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = weekdagPagerAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position + 2){ //position +2 makes Monday the first Tab
                Calendar.MONDAY -> tab.text = getString(R.string.maandag)
                Calendar.TUESDAY -> tab.text = getString(R.string.dinsdag)
                Calendar.WEDNESDAY -> tab.text = getString(R.string.woensdag)
                Calendar.THURSDAY -> tab.text = getString(R.string.donderdag)
                Calendar.FRIDAY -> tab.text = getString(R.string.vrijdag)
                Calendar.SATURDAY -> tab.text = getString(R.string.zaterdag)
                (Calendar.SUNDAY +7 ) -> tab.text = getString(R.string.zondag)
            }
        }.attach()

        binding.fab.setOnClickListener { view ->
            navigateToNieuweActiviteitFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNieuweActiviteitFragment(){
        val directions = WeekoverzichtFragmentDirections.actionFirstFragmentToNieuweActiviteitFragment()
        findNavController().navigate(directions)
    }
}