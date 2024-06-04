package com.idnp2024a.beaconscanner.button

import android.app.Activity
import android.content.Context
import android.util.AttributeSet

class StartButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    activity: Activity? = null,
    viewId: Int? = null
) : CustomButton(context, attrs, defStyleAttr, activity, viewId){
    init {
        setupStartButtonBehavior()
    }

    private fun setupStartButtonBehavior(){
        this.setOnClickListener{
            // TODO: implement functions
            /**
            if (isLocationEnabled()) {
                bluetoothScanStart(bleScanCallback)
            } else {
                showPermissionDialog()
            }
            */
        }
    }
}