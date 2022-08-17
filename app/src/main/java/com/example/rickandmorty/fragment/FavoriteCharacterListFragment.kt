package com.example.rickandmorty.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.AppRoomDatabase
import com.example.rickandmorty.DatabaseBuilder
import com.example.rickandmorty.adapter.CharacterCardAdapter
import com.example.rickandmorty.databinding.FragmentFavoriteCharacterListBinding
import com.example.rickandmorty.model.Result
import com.example.rickandmorty.model.RoomCharacterModel
import com.example.rickandmorty.model.toResult
import kotlinx.coroutines.launch

class FavoriteCharacterListFragment(context: Context) : Fragment() {

    private var thisContext: Context = context
    private var characterLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var characterAdapter: CharacterCardAdapter
    private lateinit var db: AppRoomDatabase
    private lateinit var binding: FragmentFavoriteCharacterListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fnLoadRoomCharacterData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            fnLoadRoomCharacterData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun fnLoadRoomCharacterData() {
        db = DatabaseBuilder.getInstance(thisContext)
        lifecycleScope.launch {
            val roomCharacterList: List<RoomCharacterModel> =
                db.roomCharacterDao().getAllCharacter()
            val convertedList =
                roomCharacterList.map { it.toResult() }.toMutableList() as ArrayList<Result>
            onCharactersFetched(convertedList)
            binding.tvInfoText.visibility = View.GONE
            if (convertedList.isEmpty()) {
                binding.tvInfoText.visibility = View.VISIBLE
            }
        }
    }

    private fun onCharactersFetched(characterList: ArrayList<Result>) {
        characterLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.characterRecyclerView.layoutManager = characterLayoutManager
        characterAdapter = context?.let { CharacterCardAdapter(it, characterList) }!!
        binding.characterRecyclerView.adapter = characterAdapter
    }

}