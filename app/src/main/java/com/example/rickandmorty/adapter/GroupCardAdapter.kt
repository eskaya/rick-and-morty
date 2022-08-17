package com.example.rickandmorty.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.activity.GroupDetailActivity
import com.example.rickandmorty.databinding.AdapterCharacterGroupCardBinding
import com.example.rickandmorty.databinding.AdapterSelectCharacterForDialogBinding
import com.example.rickandmorty.model.Result
import com.example.rickandmorty.model.RoomCharacterGroupModel

class GroupCardAdapter(
    private val context: Context,
    private val characterList: MutableList<RoomCharacterGroupModel>
) : RecyclerView.Adapter<GroupCardAdapter.ViewHolder>() {

    private var characterLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCharacterGroupCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(characterList[position]) {
                binding.tvGroupName.text = this.name
                binding.tvGroupDescription.text = this.description
                onCharacterGroupCircleImageAdapter(binding.groupItemRecyclerView, this.groupItem)

                //TODO: data gönderiminde serilizable'ı parcelaze olarak güncelle
                binding.groupCardView.setOnClickListener {
                    val intent = Intent(context, GroupDetailActivity::class.java)
                    intent.putExtra("CHARACTERS", characterList[position])
                    context.startActivity(intent)
                }

                val layoutParams = binding.groupCardView.layoutParams as ViewGroup.MarginLayoutParams
                if (position == (characterList.size) - 1) {
                    layoutParams.setMargins(0, 30, 0, 30)
                } else {
                    layoutParams.setMargins(0, 30, 0, 0)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    inner class ViewHolder(val binding: AdapterCharacterGroupCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun onCharacterGroupCircleImageAdapter(
        recyclerView: RecyclerView,
        characterGroupList: ArrayList<Result>
    ) {
        characterLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = characterLayoutManager
        val characterGroupImageAdapter = CharacterGroupCardCircleImageAdapter(characterGroupList)
        recyclerView.adapter = characterGroupImageAdapter
    }
}
