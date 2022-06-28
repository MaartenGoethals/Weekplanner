package be.dekade.weekplanner.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import be.dekade.weekplanner.DagoverzichtFragment

private const val ARG_WEEKDAG = "weekdag"

class WeekdagPagerAdapter(f: Fragment): FragmentStateAdapter(f){
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        val fragment = DagoverzichtFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_WEEKDAG, position)
        }
        return fragment
    }
}
