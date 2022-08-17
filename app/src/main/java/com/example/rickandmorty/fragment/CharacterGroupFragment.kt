package com.example.rickandmorty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rickandmorty.AppRoomDatabase
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.GroupCardAdapter
import com.example.rickandmorty.databinding.FragmentCharacterGroupBinding
import com.example.rickandmorty.model.RoomCharacterGroupModel
import com.example.rickandmorty.service.CharacterDao

class CharacterGroupFragment : Fragment() {

    private var characterLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var characterAdapter: GroupCardAdapter

    //room database
    private lateinit var db: AppRoomDatabase
    private lateinit var characterDao: CharacterDao

    lateinit var binding: FragmentCharacterGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fnLoadRoomCharacterData()

        binding.swipeRefresh.setOnRefreshListener {
            fnLoadRoomCharacterData()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun fnLoadRoomCharacterData() {
        binding.progressBar.visibility = View.GONE
        db = context?.let {
            Room.databaseBuilder(
                it.applicationContext,
                AppRoomDatabase::class.java,
                "app_room_database"
            )
                .allowMainThreadQueries()
                .build()
        }!!

        characterDao = db.roomCharacterDao()
        val roomCharacterGroupList: MutableList<RoomCharacterGroupModel> = characterDao.getAllCharacterGroup()
        onCharacterGroupsFetched(roomCharacterGroupList)
        if (roomCharacterGroupList.size > 0) {
            binding.tvInfoMessage.visibility = View.GONE
        }
    }

    private fun onCharacterGroupsFetched(characterGroupList: MutableList<RoomCharacterGroupModel>) {
        context?.let {
            characterLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.characterGroupRecyclerView.layoutManager = characterLayoutManager
            characterAdapter = GroupCardAdapter(it, characterGroupList)
            binding.characterGroupRecyclerView.adapter = characterAdapter
        }
    }
}