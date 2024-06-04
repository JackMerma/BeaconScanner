package com.idnp2024a.beaconscanner.button

import android.app.Activity
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.location.LocationManager
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat
import com.idnp2024a.beaconscanner.BleScanCallback
import com.idnp2024a.beaconscanner.MainActivityBLE
import com.idnp2024a.beaconscanner.permissions.Permission
import com.idnp2024a.beaconscanner.permissions.PermissionManager

class StartButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    activity: Activity? = null,
    viewId: Int? = null
) : CustomButton(context, attrs, defStyleAttr, activity, viewId){

    public fun setupStartButtonBehavior(TAG: String, btScanner: BluetoothLeScanner, permissionManager: PermissionManager, bleScanCallback: BleScanCallback, mainActivityBLE: MainActivityBLE, alertDialog: AlertDialog?){
        this.setOnClickListener{
            if (isLocationEnabled()) {
                bluetoothScanStart(TAG, btScanner, permissionManager, bleScanCallback)
            } else {
                showPermissionDialog(mainActivityBLE, alertDialog)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private fun bluetoothScanStart(TAG: String, btScanner: BluetoothLeScanner, permissionManager: PermissionManager, bleScanCallback: BleScanCallback) {
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

    private fun showPermissionDialog(mainActivityBLE: MainActivityBLE, alertDialog: AlertDialog?) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(mainActivityBLE)
        builder.setTitle("Alerta")
            .setMessage("El servicio de localizacion no esta activo")
            .setPositiveButton("Close") { dialog, which ->
                dialog.dismiss()
            }

        if (alertDialog == null) {
            // alertDialog = builder.Create()
        }
        if (!alertDialog!!.isShowing()) {
            alertDialog!!.show()
        }
    }
}