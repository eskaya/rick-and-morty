package com.example.rickandmorty.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rickandmorty.AppRoomDatabase
import com.example.rickandmorty.CreateRetrofitService
import com.example.rickandmorty.DatabaseBuilder
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.example.rickandmorty.model.CharacterDetailModel
import com.example.rickandmorty.model.RoomCharacterModel
import com.example.rickandmorty.model.toRoomCharacterModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var characterDetailData: CharacterDetailModel
    private lateinit var db: AppRoomDatabase
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        db = DatabaseBuilder.getInstance(this)

        val characterId = intent.getStringExtra("CHARACTER_ID")
        if (characterId != null) {
            getCharacterDetail(characterId)
            lifecycleScope.launch {
                val roomCharacter = db.roomCharacterDao().getCharacter(characterId)
                if(roomCharacter != null){
                    isFavorite = true
                    binding.ivFavorite.setImageResource(R.drawable.added_favirite)
                }
            }
        }
        listeners()
    }

    private fun listeners() {
        binding.ivBackButton.setOnClickListener {
            finish()
        }
        binding.ivFavorite.setOnClickListener {
            val roomCharacter = RoomCharacterModel(
                characterDetailData.name,
                characterDetailData.status,
                characterDetailData.species,
                characterDetailData.origin.name,
                characterDetailData.location.name,
                characterDetailData.image,
                characterDetailData.id
            )
            if (!isFavorite) {
                lifecycleScope.launch {
                    db.roomCharacterDao().insert(roomCharacter)
                    binding.ivFavorite.setImageResource(R.drawable.added_favirite)
                }
            } else {
                lifecycleScope.launch {
                    val convertedList = characterDetailData.toRoomCharacterModel()
                    db.roomCharacterDao().delete(convertedList)
                    binding.ivFavorite.setImageResource(R.drawable.favorite_icon)
                }
            }
        }
    }

    private fun getCharacterDetail(id: String) {
        binding.progressBar.visibility = View.VISIBLE

        CreateRetrofitService.service.getCharacterDetail(id)
            .enqueue(object : Callback<CharacterDetailModel> {
                override fun onResponse(
                    call: Call<CharacterDetailModel>,
                    response: Response<CharacterDetailModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            characterDetailData = it
                            createCharacterDetailPage(characterDetailData)
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.svDetailItemWrapper.visibility = View.VISIBLE
                        }
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.tvErrorMessage.text = getText(R.string.request_failed)
                        binding.tvErrorMessage.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<CharacterDetailModel>, t: Throwable) {
                    Toast.makeText(
                        this@CharacterDetailActivity,
                        R.string.request_failed,
                        Toast.LENGTH_LONG
                    )
                        .show()
                    t.printStackTrace()
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.tvErrorMessage.text = getText(R.string.request_failed)
                    binding.tvErrorMessage.visibility = View.VISIBLE
                }
            })
    }

    private fun createCharacterDetailPage(data: CharacterDetailModel) {
        Picasso.get().load(data.image).into(binding.ivCharacterDetail)
        binding.tvCharacterDetailName.text = data.name
        binding.tvCharacterDetailStatus.text = data.status
        binding.tvCharacterDetailSpecies.text = data.species
        binding.tvLocationInfo.text = data.location.name
        binding.tvFirstSeenInfo.text = data.origin.name

        if (data.status == "Alive") {
            binding.cvCharacterStatus.setCardBackgroundColor(Color.GREEN)
        } else {
            binding.cvCharacterStatus.setCardBackgroundColor(Color.RED)
        }
    }
}