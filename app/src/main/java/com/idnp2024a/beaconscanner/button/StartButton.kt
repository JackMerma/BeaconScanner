package com.idnp2024a.beaconscanner.button

import android.app.Activity
import android.bluetooth.le.BluetoothLeScanner
import android.util.Log
import com.idnp2024a.beaconscanner.BleScanCallback
import com.idnp2024a.beaconscanner.permissions.Permission
import com.idnp2024a.beaconscanner.permissions.PermissionManager

class StartButton @JvmOverloads constructor(
    activity: Activity? = null,
    viewId: Int? = null,
) : CustomButton(activity, viewId){

    public fun bluetoothScanStart(TAG: String, btScanner: BluetoothLeScanner, permissionManager: PermissionManager, bleScanCallback: BleScanCallback) {
        Log.d(TAG, "btScan ...1")
        if (btScanner != null) {
            Log.d(TAG, "btScan ...2")
            permissionManager
                .request(Permission.Location)
                .rationale("Bluetooth permission is needed")
                .checkPermission { isgranted ->
                    if (isgranted) {
                        btScanner!!.startScan(bleScanCallback)
                    } else {
                        Log.d(TAG, "Alert you don't have Bluetooth permission")
                    }
                }
        } else {
            Log.d(TAG, "btScanner is null")
        }
    }
}