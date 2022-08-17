package com.example.rickandmorty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.CreateRetrofitService
import com.example.rickandmorty.R
import com.example.rickandmorty.activity.CharacterActivity
import com.example.rickandmorty.adapter.CharacterCardAdapter
import com.example.rickandmorty.databinding.FragmentCharacterListBinding
import com.example.rickandmorty.model.CharacterModel
import com.example.rickandmorty.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterListFragment : Fragment() {
    private var characterLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var characterAdapter: CharacterCardAdapter
    private var status: String = ""
    private var gender: String = ""
    lateinit var binding: FragmentCharacterListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCharacterData(status, gender)
        refreshPage()
    }

    private fun refreshPage() {
        binding.allSwipeRefresh.setOnRefreshListener {
            (activity as CharacterActivity?)?.filterApply()
            binding.allSwipeRefresh.isRefreshing = false
        }
    }

    fun loadCharacterData(status: String, gender: String) {
        CreateRetrofitService.service.getCharacter(status, gender)
            .enqueue(object : Callback<CharacterModel> {
                override fun onResponse(
                    call: Call<CharacterModel>,
                    response: Response<CharacterModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onCharactersFetched(it.results)
                            binding.pbCharacterProgressBar.visibility = View.INVISIBLE
                            binding.llCharactersWrapper.visibility = View.VISIBLE
                        }
                    } else {
                        binding.pbCharacterProgressBar.visibility = View.INVISIBLE
                        binding.tvErrorMessage.visibility = View.VISIBLE
                        binding.tvErrorMessage.text = getText(R.string.request_failed)
                    }
                }

                override fun onFailure(call: Call<CharacterModel>, t: Throwable) {
                    t.printStackTrace()
                    binding.pbCharacterProgressBar.visibility = View.INVISIBLE
                    binding.tvErrorMessage.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = t.toString()
                }
            })
    }

    private fun onCharactersFetched(characterList: ArrayList<Result>) {
        context?.let {
            characterLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.characterRecyclerView.layoutManager = characterLayoutManager
            characterAdapter = CharacterCardAdapter(it, characterList)
            binding.characterRecyclerView.adapter = characterAdapter
        }
    }
}