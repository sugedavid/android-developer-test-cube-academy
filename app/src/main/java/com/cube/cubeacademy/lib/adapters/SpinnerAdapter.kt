package com.cube.cubeacademy.lib.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.R
import com.cube.cubeacademy.lib.models.Nominee

class SpinnerAdapter(context: AppCompatActivity, nominees: List<Nominee>) :
    ArrayAdapter<Nominee>(context, 0, nominees) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(context)
                .inflate(R.layout.spinner_item_layout, parent, false)

        val nominee = getItem(position)
        val firstNameTextView = view.findViewById<TextView>(R.id.firstNameTextView)
        val lastNameTextView = view.findViewById<TextView>(R.id.lastNameTextView)

        firstNameTextView.text = nominee?.firstName
        lastNameTextView.text = nominee?.lastName

        return view
    }
}
