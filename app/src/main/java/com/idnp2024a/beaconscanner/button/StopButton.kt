package com.idnp2024a.beaconscanner.button

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.le.BluetoothLeScanner
import android.util.Log
import com.idnp2024a.beaconscanner.BleScanCallback

class StopButton @JvmOverloads constructor(
    activity: Activity? = null,
    viewId: Int? = null,
) : CustomButton(activity, viewId){

    @SuppressLint("MissingPermission")
    public fun bluetoothScanStop(TAG: String, bleScanCallback: BleScanCallback, btScanner: BluetoothLeScanner){
        Log.d(TAG, "btScan ...1")
        if (btScanner != null) {
            Log.d(TAG, "btScan ...2")
            btScanner!!.stopScan(bleScanCallback)
        } else {
            Log.d(TAG, "btScanner is null")
        }
    }
}
