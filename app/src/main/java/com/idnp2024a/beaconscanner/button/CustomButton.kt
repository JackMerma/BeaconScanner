package com.idnp2024a.beaconscanner.button

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    activity: Activity? = null,
    viewId: Int? = null
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        if (activity != null && viewId != null){
            // TODO
            // initialize component
        }
    }
}