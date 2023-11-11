package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.adapters.NominationsRecyclerViewAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.utils.displayErrorToast
import com.cube.cubeacademy.lib.view_models.NominationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nominationsAdapter: NominationsRecyclerViewAdapter
    private val nominationViewModel: NominationViewModel by viewModels()

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        populateUI()
    }

    private fun populateUI() {
        binding.apply {

            // fetch nominees
            nominationViewModel.fetchNominees()

            // fetch nominations
            nominationViewModel.fetchNominations()

            // observe nominees
            nominationViewModel.nominees.observe(this@MainActivity) { nominees ->
                if (nominees.isNotEmpty()) {
                    // setup the RecyclerView
                    nominationsAdapter = NominationsRecyclerViewAdapter(nominees)
                    nominationsList.layoutManager = LinearLayoutManager(this@MainActivity)
                    nominationsList.adapter = nominationsAdapter
                }

                // observe nominations
                nominationViewModel.nominations.observe(this@MainActivity) { nominations ->
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

            // observe nominees fetch status
            nominationViewModel.nomineesSuccessful.observe(this@MainActivity) { isSuccessful ->
                if (!isSuccessful) displayErrorToast(
                    this@MainActivity, getString(R.string.fetching_nominees)
                )
            }

            // observe nominations fetch status
            nominationViewModel.nominationsSuccessful.observe(this@MainActivity) { isSuccessful ->
                if (!isSuccessful) displayErrorToast(
                    this@MainActivity, getString(R.string.fetching_nominations)
                )
            }

            // create Button
            createButton.setOnClickListener {
                // navigate to CreateNominationActivity
                val intent = Intent(this@MainActivity, CreateNominationActivity::class.java)
                startActivity(intent)
            }

        }

    }
}