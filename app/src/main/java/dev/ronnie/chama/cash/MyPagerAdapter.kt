package dev.ronnie.chama.cash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val bankFrag = BanksFragment()
                bankFrag.arguments = BanksAndMpesaActivity.bundle
                bankFrag
            }
            else -> {
                val mpesaFrag = MpesaFragment()
                mpesaFrag.arguments = BanksAndMpesaActivity.bundle
                mpesaFrag
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {

        return when (position) {
            0 -> "Bank"
            else -> {
                return "Mpesa"
            }
        }
    }
}