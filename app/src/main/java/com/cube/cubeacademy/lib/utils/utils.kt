package com.cube.cubeacademy.lib.utils

import android.content.Context
import android.widget.Toast

// renders error Toast
fun displayErrorToast(context: Context, requestName: String){
    Toast.makeText(context, "An error occurred $requestName. Try again later.", Toast.LENGTH_SHORT).show()
}