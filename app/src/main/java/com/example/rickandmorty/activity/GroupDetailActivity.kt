package com.example.rickandmorty.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.adapter.CharacterCardAdapter
import com.example.rickandmorty.databinding.ActivityGroupDetailBinding
import com.example.rickandmorty.model.Result
import com.example.rickandmorty.model.RoomCharacterGroupModel

class GroupDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupDetailBinding

    //adapter
    private var characterLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var characterAdapter: CharacterCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadData()
        listener()
    }

    private fun listener() {
        binding.ivGroupDetailBackButton.setOnClickListener {
            finish()
        }
    }

    private fun loadData() {
        val groupDetail = intent.getSerializableExtra("CHARACTERS") as RoomCharacterGroupModel
        binding.groupDetailRecyclerView.visibility = View.VISIBLE
        binding.tvGroupDetailErrorMessage.visibility = View.GONE
        binding.groupDetailProgressBar.visibility = View.GONE
        binding.tvGroupName.text = groupDetail.name
        onCharactersFetched(groupDetail.groupItem)
    }


    private fun onCharactersFetched(characterList: ArrayList<Result>) {
        this.let {
            characterLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.groupDetailRecyclerView.layoutManager = characterLayoutManager
            characterAdapter = CharacterCardAdapter(it, characterList)
            binding.groupDetailRecyclerView.adapter = characterAdapter
        }
    }
}