package com.example.rickandmorty.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.rickandmorty.fragment.CharacterGroupFragment
import com.example.rickandmorty.fragment.CharacterListFragment
import com.example.rickandmorty.fragment.FavoriteCharacterListFragment

//TODO migrate to viewpage2 later
class CharacterTabAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CharacterListFragment()
            }
            1 -> {
                FavoriteCharacterListFragment(context)
            }
            2 -> {
                CharacterGroupFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}