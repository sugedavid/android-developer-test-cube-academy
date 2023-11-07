package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.databinding.ActivityNominationSubmittedBinding

class NominationSubmittedActivity : AppCompatActivity() {
	private lateinit var binding: ActivityNominationSubmittedBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityNominationSubmittedBinding.inflate(layoutInflater)
		setContentView(binding.root)

		populateUI()
	}

	private fun populateUI() {
		/**
		 * TODO: Add the logic for the two buttons (Don't forget that if you start to add a new nomination, the back button shouldn't come back here)
		 */
		// submit btn
		binding.submitButton.setOnClickListener {
			// navigate to CreateNominationActivity
			val intent = Intent(this, CreateNominationActivity::class.java)
			startActivity(intent)
			finish()
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