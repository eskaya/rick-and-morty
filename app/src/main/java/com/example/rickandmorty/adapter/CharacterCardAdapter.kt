package com.example.rickandmorty.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.activity.CharacterDetailActivity
import com.example.rickandmorty.databinding.AdapterCharacterCardBinding
import com.example.rickandmorty.model.Result
import com.squareup.picasso.Picasso

class CharacterCardAdapter(
    private val context: Context,
    private val characterList: ArrayList<Result>
) : RecyclerView.Adapter<CharacterCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCharacterCardBinding.inflate(
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
            with(characterList[position]){
                Picasso.get().load(this.image).into(binding.ivCharacter)
                binding.tvCharacterName.text = this.name
                binding.tvCharacterStatus.text = this.status
                binding.tvCharacterSpecies.text = this.species
                binding.tvLocationInfo.text = this.location?.name
                binding.tvFirstSeenInfo.text = this.origin?.name

                if (characterList[position].status == "Alive") {
                    binding.cvCharacterStatus.setCardBackgroundColor(Color.GREEN)
                } else {
                    binding.cvCharacterStatus.setCardBackgroundColor(Color.RED)
                }

                val layoutParams = binding.cvCharacterCard.layoutParams as ViewGroup.MarginLayoutParams
                if (position == (characterList.size) - 1) {
                    layoutParams.setMargins(0, 30, 0, 30)
                } else {
                    layoutParams.setMargins(0, 30, 0, 0)
                }
                binding.cvCharacterCard.setOnClickListener {
                    val intent = Intent(context, CharacterDetailActivity::class.java).apply {}
                    intent.putExtra("CHARACTER_ID", characterList[position].id);
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    inner class ViewHolder(val binding: AdapterCharacterCardBinding): RecyclerView.ViewHolder(binding.root)
}
