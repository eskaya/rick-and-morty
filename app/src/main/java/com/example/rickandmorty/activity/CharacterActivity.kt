package com.example.rickandmorty.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.*
import com.example.rickandmorty.adapter.AddGroupCharacter
import com.example.rickandmorty.adapter.CharacterTabAdapter
import com.example.rickandmorty.databinding.ActivityCharacterBinding
import com.example.rickandmorty.fragment.CharacterListFragment
import com.example.rickandmorty.model.CharacterModel
import com.example.rickandmorty.model.Result
import com.example.rickandmorty.model.RoomCharacterGroupModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterBinding
    private lateinit var dialog: Dialog
    var status: String = ""
    var gender: String = ""

    //room database
    private lateinit var db: AppRoomDatabase
    // private lateinit var characterDao: CharacterDao

    private var characterLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var characterAdapter: AddGroupCharacter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*db = Room.databaseBuilder(
            applicationContext,
            AppRoomDatabase::class.java,
            "app_room_database"
        )
            .allowMainThreadQueries()
            .build()
        characterDao = db.roomCharacterDao()

         */
        db = DatabaseBuilder.getInstance(this)
        createFragmentPagerAdapter()
        listeners()
    }

    private fun listeners() {
        dialog = Dialog(this)
        binding.ivFilterIcon.setOnClickListener {
            showFilterDialog()
        }

        binding.ivAddGroup.setOnClickListener {
            loadCharacterDataForGroup()
        }
    }

    private fun showFilterDialog() {
        dialog.setContentView(R.layout.dialog_custom_filter_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        selectStatusItemForFilter()
        selectGenderItemForFilter()
        val applyButton = dialog.findViewById<Button>(R.id.applyButton)
        applyButton.setOnClickListener {
            filterApply()
        }

        val clearButton = dialog.findViewById<TextView>(R.id.clearAllButton)
        clearButton.setOnClickListener {
            filterClear()
        }
    }

    private fun showAddGroupDialog(results: ArrayList<Result>?) {
        if (results == null || results.isEmpty()) {
            Toast.makeText(this, "No characters found!", Toast.LENGTH_SHORT).show()
            return
        }

        dialog.setContentView(R.layout.dialog_custom_add_group_dialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val characterRecyclerView = dialog.findViewById<RecyclerView>(R.id.createGroupRecyclerView)
        val createButton = dialog.findViewById<Button>(R.id.createButton)

        characterLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        characterRecyclerView.layoutManager = characterLayoutManager
        characterAdapter = AddGroupCharacter(results)
        characterRecyclerView.adapter = characterAdapter

        createButton.setOnClickListener {
            createGroup()
        }

        dialog.show()
    }

    private fun selectStatusItemForFilter() {
        val statusRadioGroup = dialog.findViewById<RadioGroup>(R.id.rgStatus)
        statusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            status = when (checkedId) {
                R.id.rgAlive -> FilterVariables.alive
                R.id.rbDead -> FilterVariables.dead
                R.id.rbUnknown -> FilterVariables.unknown
                else -> ""
            }
        }
    }

    private fun selectGenderItemForFilter() {
        val statusRadioGroup = dialog.findViewById<RadioGroup>(R.id.rgGender)
        statusRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            gender = when (checkedId) {
                R.id.rgFemale -> FilterVariables.female
                R.id.rbMale -> FilterVariables.male
                R.id.rbUnknownGender -> FilterVariables.unknown
                R.id.rbGenderless -> FilterVariables.genderless
                else -> ""
            }
        }
    }

    private fun createFragmentPagerAdapter() {

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.all))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.favorite))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.group))
        setAllCaps(binding.tabLayout, false)
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter =
            CharacterTabAdapter(
                this@CharacterActivity,
                supportFragmentManager,
                binding.tabLayout.tabCount
            )
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
                if (tab.position == 2) {
                    binding.ivFilterIcon.visibility = View.GONE
                    binding.ivAddGroup.visibility = View.VISIBLE
                    binding.cvIsApplyFilter.visibility = View.GONE
                } else {
                    binding.ivFilterIcon.visibility = View.VISIBLE
                    binding.ivAddGroup.visibility = View.GONE
                    if (hasFilter())
                        binding.cvIsApplyFilter.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setAllCaps(view: View?, caps: Boolean) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) setAllCaps(view.getChildAt(i), caps)
        } else if (view is TextView) view.isAllCaps = caps
    }

    fun filterApply() {
        (supportFragmentManager.fragments[0] as CharacterListFragment).loadCharacterData(
            status,
            gender
        )
        dialog.dismiss()
        if (hasFilter()) {
            binding.cvIsApplyFilter.visibility = View.VISIBLE
        }
    }

    private fun hasFilter(): Boolean {
        return status.isNotEmpty() || gender.isNotEmpty()
    }

    private fun filterClear() {
        status = ""
        gender = ""
        (supportFragmentManager.fragments[0] as CharacterListFragment).loadCharacterData(
            status,
            gender
        )
        dialog.dismiss()
        binding.cvIsApplyFilter.visibility = View.GONE
    }

    private fun loadCharacterDataForGroup() {
        CreateRetrofitService.service.getCharacter("", "")
            .enqueue(object : Callback<CharacterModel> {
                override fun onResponse(
                    call: Call<CharacterModel>,
                    response: Response<CharacterModel>
                ) {
                    if (response.isSuccessful) {
                        showAddGroupDialog(response.body()?.results)
                    } else {
                        Toast.makeText(
                            this@CharacterActivity,
                            R.string.request_failed,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CharacterModel>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@CharacterActivity, R.string.check_to_log, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun createGroup() {
        val groupName = dialog.findViewById<EditText>(R.id.etGroupName).text
        val groupDescription = dialog.findViewById<EditText>(R.id.etDescription).text
        val numberOfCharacter = dialog.findViewById<EditText>(R.id.etNumberOfCharacter).text
        val groupItem = characterAdapter.groupItem

        if (groupName.isNotEmpty() && groupDescription.isNotEmpty() && numberOfCharacter.isNotEmpty() && groupItem.size > 0) {
            val roomCharacterGroup = RoomCharacterGroupModel(
                name = groupName.toString(),
                description = groupDescription.toString(),
                itemNumber = numberOfCharacter.toString(),
                groupItem = groupItem
            )

            lifecycleScope.launch {
                db.roomCharacterDao().insertCharacterGroup(roomCharacterGroup)
            }
            dialog.dismiss()
            Toast.makeText(this, R.string.add_new_group, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.make_sure_to_fill_in_all_fields, Toast.LENGTH_SHORT)
                .show()
        }
    }
}