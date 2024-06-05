package com.idnp2024a.beaconscanner.button

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

open class CustomButton @JvmOverloads constructor(
    activity: Activity? = null,
    viewId: Int? = null
){

    var button: Button? = null

    init {
        if (activity != null && viewId != null){
            button = activity.findViewById<Button>(viewId)
        }
    }
}