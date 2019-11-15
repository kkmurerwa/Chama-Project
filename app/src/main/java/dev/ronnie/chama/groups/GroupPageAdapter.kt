package  dev.ronnie.chama.groups

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class GroupPageAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyGroupsFragment()
            }
            else -> {
                AllGroupsFragment()

            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {

        return when (position) {
            0 -> "My Groups"
            else -> {
                "All Groups"
            }
        }
    }
}