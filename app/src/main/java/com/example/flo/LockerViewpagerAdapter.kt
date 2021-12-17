package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewpagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StorageFragment()
            1 -> MusicfileFragment()
            else-> SavedAlbumFragment()
        }
    }
}