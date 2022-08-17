package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.AdapterCardCharacterCircleImageBinding
import com.example.rickandmorty.databinding.AdapterSelectCharacterForDialogBinding
import com.example.rickandmorty.model.Result
import com.squareup.picasso.Picasso


class CharacterGroupCardCircleImageAdapter(
    private val characterList: ArrayList<Result>
) : RecyclerView.Adapter<CharacterGroupCardCircleImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCardCharacterCircleImageBinding.inflate(
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
            Picasso.get().load(characterList[position].image).into(binding.circleGroupCharacterImage)
        }
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    inner class ViewHolder(val binding: AdapterCardCharacterCircleImageBinding): RecyclerView.ViewHolder(binding.root)
    /*
    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var characterImage: ImageView

        init {
            characterImage = view.findViewById(R.id.circleGroupCharacterImage)
        }
    }

     */

}