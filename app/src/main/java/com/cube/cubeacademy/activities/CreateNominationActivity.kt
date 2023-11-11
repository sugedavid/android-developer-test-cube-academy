package com.cube.cubeacademy.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.fragments.BackModalClickListener
import com.cube.cubeacademy.fragments.BackModalFragment
import com.cube.cubeacademy.lib.adapters.SpinnerAdapter
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.lib.utils.Process
import com.cube.cubeacademy.lib.utils.colorSubstring
import com.cube.cubeacademy.lib.utils.displayErrorToast
import com.cube.cubeacademy.lib.view_models.CreateNominationViewModel
import com.cube.cubeacademy.lib.view_models.NominationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity(), BackModalClickListener {
    private lateinit var binding: ActivityCreateNominationBinding
    private val backModalFragment = BackModalFragment()
    private val nominationViewModel: NominationViewModel by viewModels()
    private val createNominationViewModel: CreateNominationViewModel by viewModels()
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
        binding.apply {

            // apply color to substring
            processTitleTextView.colorSubstring(getString(R.string.cube_of_the_month))
            nomineeLabelTextView.colorSubstring(getString(R.string.asterisk))
            reasoningLabelTextView.colorSubstring(getString(R.string.asterisk))

            // fetch nominees
            nominationViewModel.fetchNominees()

            // observe nominees
            nominationViewModel.nominees.observe(this@CreateNominationActivity) { nominees ->
                if (nominees.isNotEmpty()) {
                    // nominee Spinner
                    val items = mutableListOf(
                        Nominee(
                            "", getString(R.string.select),
                            getString(R.string.option)
                        )
                    )
                    items.addAll(nominees)

                    val adapter = SpinnerAdapter(this@CreateNominationActivity, items)
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
            }

            // observe nominees fetch status
            nominationViewModel.nomineesSuccessful.observe(this@CreateNominationActivity) { isSuccessful ->
                if (!isSuccessful) displayErrorToast(
                    this@CreateNominationActivity, getString(R.string.fetching_nominees)
                )
            }

            // reasoning EditText
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
                createNominationViewModel.createNomination(nomineeId, reason, process)
            }

            // observe create nomination status
            createNominationViewModel.createNominationsSuccessful.observe(this@CreateNominationActivity) { isSuccessful ->
                // navigate to NominationSubmittedActivity if successful, else show error message
                if (isSuccessful) {
                    val intent =
                        Intent(
                            this@CreateNominationActivity,
                            NominationSubmittedActivity::class.java
                        )
                    startActivity(intent)
                    finish()
                } else displayErrorToast(
                    this@CreateNominationActivity,
                    getString(R.string.submitting_your_nomination)
                )
            }

            // observe loading status
            createNominationViewModel.isLoading.observe(this@CreateNominationActivity) { isLoading ->
                // navigate to NominationSubmittedActivity if successful, else show error message
                if (isLoading) {
                    loadingIndicator.visibility = View.VISIBLE
                    submitButton.text = ""
                } else {
                    loadingIndicator.visibility = View.INVISIBLE
                    submitButton.text = getString(R.string.submit_nomination)
                }
            }

            // back Button
            backButton.setOnClickListener {
                handleBackNavigation()
            }

            onBackPressedDispatcher.addCallback(
                this@CreateNominationActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        handleBackNavigation()
                    }
                })
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

    // handle back button navigation
    fun handleBackNavigation() {
        // navigate to MainActivity if user has filled the form, else show modal
        if (nomineeId.isNotEmpty() || reason.isNotEmpty() || process.isNotEmpty()) {
            backModalFragment.setClickListener(this@CreateNominationActivity)
            backModalFragment.show(supportFragmentManager, backModalFragment.tag)
        } else
            finish()
    }

    override fun onLeavePageButtonClick() {
        finish()
    }

    override fun onCancelButtonClick() {
        backModalFragment.dismiss()
    }
}