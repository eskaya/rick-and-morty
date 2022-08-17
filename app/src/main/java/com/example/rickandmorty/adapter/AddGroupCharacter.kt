package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.AdapterSelectCharacterForDialogBinding
import com.example.rickandmorty.model.Result
import com.squareup.picasso.Picasso

class AddGroupCharacter(
    private val characterList: ArrayList<Result>,
) : RecyclerView.Adapter<AddGroupCharacter.ViewHolder>() {

    val groupItem = arrayListOf<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSelectCharacterForDialogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(characterList[position]) {
                Picasso.get().load(this.image).into(binding.circleCharacterImage)
                binding.circleCharacterImage.setOnClickListener {
                    if (groupItem.contains(this)) {
                        groupItem.remove(this).toString()
                        binding.ivSelectItem.visibility = View.GONE
                    } else {
                        groupItem.add(this)
                        binding.ivSelectItem.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    inner class ViewHolder(val binding: AdapterSelectCharacterForDialogBinding) :
        RecyclerView.ViewHolder(binding.root)
}