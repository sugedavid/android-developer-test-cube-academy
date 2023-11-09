package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.adapters.NominationsRecyclerViewAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.lib.utils.displayErrorToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nominationsAdapter: NominationsRecyclerViewAdapter
    private var nominations = mutableListOf<Nomination>()
    private var nominees = mutableListOf<Nominee>()

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        populateUI()
    }

    private fun populateUI() {
        /**
         * TODO: Populate the UI with data in this function
         * 		 You need to fetch the list of user's nominations from the api and put the data in the recycler view
         * 		 And also add action to the "Create new nomination" button to go to the CreateNominationActivity
         */
        binding.apply {
            // create Button
            createButton.setOnClickListener {
                // navigate to CreateNominationActivity
                val intent = Intent(this@MainActivity, CreateNominationActivity::class.java)
                startActivity(intent)
            }

            lifecycleScope.launch {
                // fetch nominations
                val nominationsResponse = repository.getAllNominations()
                if (nominationsResponse.isSuccessful) {
                    nominationsResponse.body()?.let { nominations.addAll(it.data) }
                } else displayErrorToast(
                    this@MainActivity, "fetching nominations"
                )

                // fetch nominees
                val nomineesResponse = repository.getAllNominees()
                if (nomineesResponse.isSuccessful) {
                    nomineesResponse.body()?.let { nominees.addAll(it.data) }
                } else displayErrorToast(
                    this@MainActivity, "fetching nominees"
                )

                // setup the RecyclerView
                nominationsAdapter = NominationsRecyclerViewAdapter(nominees)
                nominationsList.layoutManager = LinearLayoutManager(this@MainActivity)
                nominationsList.adapter = nominationsAdapter

                // show empty state if there are no nominations, else populate the nominations
                if (nominations.isEmpty()) {
                    emptyContainer.visibility = View.VISIBLE
                    nominationsList.visibility = View.GONE
                } else {
                    emptyContainer.visibility = View.GONE
                    nominationsList.visibility = View.VISIBLE
                    nominationsAdapter.submitList(nominations.reversed())
                }
            }

        }

    }
}