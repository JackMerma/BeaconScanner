package com.idnp2024a.beaconscanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanResult
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat
import com.idnp2024a.beaconscanner.button.CustomButton
import com.idnp2024a.beaconscanner.button.StartButton
import com.idnp2024a.beaconscanner.button.StopButton
import com.idnp2024a.beaconscanner.permissions.BTPermissions
import com.idnp2024a.beaconscanner.permissions.Permission
import com.idnp2024a.beaconscanner.permissions.PermissionManager


class MainActivityBLE : AppCompatActivity() {

    private companion object {
        const val TAG: String = "MainActivityBLE"
    }
    private var alertDialog: AlertDialog? = null
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var btScanner: BluetoothLeScanner
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var txtMessage: TextView;
    private val permissionManager = PermissionManager.from(this)
    private val movingAverage = MovingAverage(5)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ble)

        BTPermissions(this).check()
        initBluetooth()

        // Defining buttons
        var btnAdversting = CustomButton(this, R.id.btnAdversting)
        val btnStart = StartButton(this, R.id.btnStart)
        val btnStop = StopButton(this, R.id.btnStop)

        txtMessage = findViewById(R.id.txtMessage)

        val bleScanCallback = BleScanCallback(
            onScanResultAction,
            onBatchScanResultAction,
            onScanFailedAction
        )

        // Defining on click listeners
        btnStart.button?.setOnClickListener{
            if (isLocationEnabled()) {
                btnStart.bluetoothScanStart(TAG, btScanner, permissionManager, bleScanCallback)
            } else {
                showPermissionDialog()
            }
        }
        btnStop.button?.setOnClickListener {
            btnStop.bluetoothScanStop(TAG, bleScanCallback, btScanner)
        }
    }

    fun initBluetooth() {

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager!!.adapter

        if (bluetoothAdapter != null) {
            btScanner = bluetoothAdapter.bluetoothLeScanner
        } else {
            Log.d(TAG, "BluetoothAdapter is null")
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun showPermissionDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
            .setMessage("El servicio de localizacion no esta activo")
            .setPositiveButton("Close") { dialog, which ->
                dialog.dismiss()
            }
        if (alertDialog == null) {
            alertDialog = builder.create()
        }
        if (!alertDialog!!.isShowing()) {
            alertDialog!!.show()
        }
    }

    @SuppressLint("MissingPermission")
    val onScanResultAction: (ScanResult?) -> Unit = { result ->
        val scanRecord = result?.scanRecord
        val beacon = Beacon(result?.device?.address)
        beacon.manufacturer = result?.device?.name
        beacon.rssi = result?.rssi
        if (scanRecord != null && beacon.manufacturer == "ESP32 Beacon") {
            scanRecord?.bytes?.let { decodeiBeacon(it, beacon.rssi) }
        }
    }

    val onBatchScanResultAction: (MutableList<ScanResult>?) -> Unit = {
        if (it != null) {
            Log.d(TAG, "BatchScanResult " + it.toString())
        }
    }

    val onScanFailedAction: (Int) -> Unit = {
        Log.d(TAG, "ScanFailed " + it.toString())
    }

    fun decodeiBeacon(data: ByteArray, rssi: Int?) {
        val data_len = Integer.parseInt(Utils.toHexString(data.copyOfRange(0, 1)), 16)
        val data_type = Integer.parseInt(Utils.toHexString(data.copyOfRange(1, 2)), 16)
        val LE_flag = Integer.parseInt(Utils.toHexString(data.copyOfRange(2, 3)), 16)
        val len = Integer.parseInt(Utils.toHexString(data.copyOfRange(3, 4)), 16)
        val type = Integer.parseInt(Utils.toHexString(data.copyOfRange(4, 5)), 16)
        val company = Utils.toHexString(data.copyOfRange(5, 7))
        val subtype = Integer.parseInt(Utils.toHexString(data.copyOfRange(7, 8)), 16)
        val subtypelen = Integer.parseInt(Utils.toHexString(data.copyOfRange(8, 9)), 16)
        val iBeaconUUID = Utils.toHexString(data.copyOfRange(9, 25))
        val major = Integer.parseInt(Utils.toHexString(data.copyOfRange(25, 27)), 16)
        val minor = Integer.parseInt(Utils.toHexString(data.copyOfRange(27, 29)), 16)
        val txPower = Integer.parseInt(Utils.toHexString(data.copyOfRange(29, 30)), 16)

        // Applying to RSSI
        var smoothedRssi = movingAverage.next(rssi!!.toDouble())

        var factor = (-1 * txPower - rssi!!) / (10 * 4.0)
        var distance = Math.pow(10.0, factor)

        val display = "TxPower:$txPower \nRSSI:$rssi \nSmoothed RSSI:$smoothedRssi \nDistance:$distance"
        txtMessage.text = display

        Log.d(
            TAG,
            "DECODE data_len:$data_len data_type:$data_type LE_flag:$LE_flag len:$len type:$type subtype:$subtype subtype_len:$subtypelen company:$company UUID:$iBeaconUUID major:$major minor:$minor txPower:$txPower"
        )
    }
}