package com.cube.cubeacademy.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.adapters.CustomSpinnerAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.lib.utils.Process
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNominationBinding
    private var nominees = mutableListOf<Nominee>()
    private var nomineeId = ""
    private var reason = ""
    private var process = ""

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

        binding.apply {

            // fetch nominees
            lifecycleScope.launch {
                nominees.addAll(repository.getAllNominees())

                // cube's name spinner
                val items = mutableListOf(Nominee("", "Select", "Option"))
                items.addAll(nominees)

                val adapter = CustomSpinnerAdapter(this@CreateNominationActivity, items)
                nameSpinner.adapter = adapter

                nameSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            // assign nomineeId if hint is not selected
                            nomineeId = if (position == 0) {
                                ""
                            } else {
                                items[position].nomineeId
                            }
                            toggleSubmitBtn(submitButton)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            }

            // reasoning
            reasoningEditText.addTextChangedListener {
                reason = it.toString()
                toggleSubmitBtn(submitButton)
            }


            // radio drawables
            val veryUnfairDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.very_unfair)
            val unfairDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.unfair)
            val notSureDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.not_sure)
            val fairDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.fair)
            val veryFairDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.very_fair)


            // radioVeryUnfair
            radioVeryUnfair.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.VERY_UNFAIR.name.lowercase()
                toggleRadioState(radioVeryUnfair, isChecked, veryUnfairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // radioUnfair
            radioUnfair.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.UNFAIR.name.lowercase()
                toggleRadioState(radioUnfair, isChecked, unfairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // radioNotSure
            radioNotSure.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.NOT_SURE.name.lowercase()
                toggleRadioState(radioNotSure, isChecked, notSureDrawable)
                toggleSubmitBtn(submitButton)
            }

            // radioFair
            radioFair.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.FAIR.name.lowercase()
                toggleRadioState(radioFair, isChecked, fairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // radioVeryFair
            radioVeryFair.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.VERY_FAIR.name
                toggleRadioState(radioVeryFair, isChecked, veryFairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // submit btn
            submitButton.setOnClickListener {
                // create nomination
                lifecycleScope.launch {
                    val nomination = repository.createNomination(nomineeId, reason, process)
                    if (nomination != null) {
                        // navigate to NominationSubmittedActivity
                        val intent =
                            Intent(
                                this@CreateNominationActivity,
                                NominationSubmittedActivity::class.java
                            )
                        startActivity(intent)
                        finish()
                    }
                }
            }

            // back btn
            backButton.setOnClickListener {
                // navigate to MainActivity
                val intent = Intent(this@CreateNominationActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    // changes the submit button enabled state
    private fun toggleSubmitBtn(submitButton: Button) {
        submitButton.isEnabled =
            nomineeId.isNotEmpty() && reason.isNotEmpty() && process.isNotEmpty()
    }

    // changes the radio button style
    private fun toggleRadioState(radio: RadioButton, isChecked: Boolean, startDrawable: Drawable?){
        val radioActiveDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.radio_active)
        val radioInActiveDrawable = ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.radio_inactive)

        if (isChecked) {
            radio.elevation = 10F
            radio.setBackgroundResource(R.drawable.bg_radio_active_button)
            radio.setCompoundDrawablesRelativeWithIntrinsicBounds(radioActiveDrawable, null, startDrawable, null)
        }
        else {
            radio.elevation = 0F
            radio.setBackgroundResource(R.drawable.bg_radio_inactive_button)
            radio.setCompoundDrawablesRelativeWithIntrinsicBounds(radioInActiveDrawable, null, startDrawable, null)
        }
    }
}