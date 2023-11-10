package com.cube.cubeacademy.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
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
import com.cube.cubeacademy.lib.utils.displayErrorToast
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
                val nomineesResponse = repository.getAllNominees()
                if (nomineesResponse.isSuccessful) {
                    nomineesResponse.body()?.let { nominees.addAll(it.data) }
                }else displayErrorToast(
                    this@CreateNominationActivity, "fetching nominees"
                )

                // cube's name Spinner
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

            // radio Drawables
            val veryUnfairDrawable =
                ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.very_unfair)
            val unfairDrawable =
                ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.unfair)
            val notSureDrawable =
                ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.not_sure)
            val fairDrawable =
                ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.fair)
            val veryFairDrawable =
                ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.very_fair)


            // veryUnfair Radio
            veryUnfairRadio.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.VERY_UNFAIR.name.lowercase()
                toggleRadioState(veryUnfairRadio, isChecked, veryUnfairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // unfair Radio
            unfairRadio.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.UNFAIR.name.lowercase()
                toggleRadioState(unfairRadio, isChecked, unfairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // notSure Radio
            notSureRadio.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.NOT_SURE.name.lowercase()
                toggleRadioState(notSureRadio, isChecked, notSureDrawable)
                toggleSubmitBtn(submitButton)
            }

            // fair Radio
            fairRadio.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.FAIR.name.lowercase()
                toggleRadioState(fairRadio, isChecked, fairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // veryFair Radio
            veryFairRadio.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) process = Process.VERY_FAIR.name
                toggleRadioState(veryFairRadio, isChecked, veryFairDrawable)
                toggleSubmitBtn(submitButton)
            }

            // submit Button
            submitButton.setOnClickListener {
                // create nomination
                lifecycleScope.launch {
                    val nominationResponse = repository.createNomination(nomineeId, reason, process)
                    // navigate to NominationSubmittedActivity if successful, else show error message
                    if (nominationResponse != null && nominationResponse.isSuccessful) {
                        val intent =
                            Intent(
                                this@CreateNominationActivity,
                                NominationSubmittedActivity::class.java
                            )
                        startActivity(intent)
                        finish()
                    } else displayErrorToast(
                        this@CreateNominationActivity, "submitting your nomination"
                    )

                }
            }

            // back Button
            backButton.setOnClickListener {
                // navigate to MainActivity
                finish()
            }
        }

    }

    // updates the submit Button enabled state
    private fun toggleSubmitBtn(submitButton: Button) {
        submitButton.isEnabled =
            nomineeId.isNotEmpty() && reason.isNotEmpty() && process.isNotEmpty()
    }

    // updates the RadioButton style
    private fun toggleRadioState(radio: RadioButton, isChecked: Boolean, startDrawable: Drawable?) {
        val radioActiveDrawable =
            ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.radio_active)
        val radioInActiveDrawable =
            ContextCompat.getDrawable(this@CreateNominationActivity, R.drawable.radio_inactive)

        if (isChecked) {
            radio.elevation = 10F
            radio.setBackgroundResource(R.drawable.bg_radio_active_button)
            radio.setCompoundDrawablesRelativeWithIntrinsicBounds(
                radioActiveDrawable,
                null,
                startDrawable,
                null
            )
        } else {
            radio.elevation = 0F
            radio.setBackgroundResource(R.drawable.bg_radio_inactive_button)
            radio.setCompoundDrawablesRelativeWithIntrinsicBounds(
                radioInActiveDrawable,
                null,
                startDrawable,
                null
            )
        }
    }
}