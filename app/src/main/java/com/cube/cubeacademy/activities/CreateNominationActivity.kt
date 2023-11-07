package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.di.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
	private lateinit var binding: ActivityCreateNominationBinding

	@Inject
	lateinit var repository: Repository

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityCreateNominationBinding.inflate(layoutInflater)
		setContentView(binding.root)

		populateUI()
	}

	private fun populateUI() {
		/**
		 * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
		 * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
		 * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
		 */

		// submit btn
		binding.submitButton.setOnClickListener {
			// navigate to NominationSubmittedActivity
			val intent = Intent(this, NominationSubmittedActivity::class.java)
			startActivity(intent)
		}

		// back btn
		binding.backButton.setOnClickListener {
			// navigate to MainActivity
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
			finish()
		}
	}
}