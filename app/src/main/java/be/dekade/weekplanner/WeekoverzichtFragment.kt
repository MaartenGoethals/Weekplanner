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

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
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
            when(position){
                0 -> tab.text = getString(R.string.maandag)
                1 -> tab.text = getString(R.string.dinsdag)
                2 -> tab.text = getString(R.string.woensdag)
                3 -> tab.text = getString(R.string.donderdag)
                4 -> tab.text = getString(R.string.vrijdag)
                5 -> tab.text = getString(R.string.zaterdag)
                6 -> tab.text = getString(R.string.zondag)
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

        findNavController().navigate(R.id.nieuweActiviteitFragment)
    }
}