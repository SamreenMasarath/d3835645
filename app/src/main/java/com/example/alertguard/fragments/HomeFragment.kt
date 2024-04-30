package com.example.alertguard.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.alertguard.Singleton
import com.example.alertguard.databinding.FragmentHomeBinding
import com.example.alertguard.db.AppDatabase
import com.example.alertguard.db.models.ReportDataModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var fusedLocation: Location? = null

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i("Permission: ", "Granted")
        } else {
            Log.i("Permission: ", "Denied")
        }
        locationPermission()
    }

    private val requestSMSPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            sendSMS()
        } else {
            Log.i("Permission: ", "Denied")
        }
    }

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("TAG", "OK")
                getLocation()
            } else {
                Log.d("TAG", "CANCEL")
            }
        }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                enableLocation()
                getLocation()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                enableLocation()
                getLocation()
            }

            else -> {
                // No location access granted.
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.e("ThisLocationF", "showLocation: $location")
            fusedLocation = location
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!checkPermission(Manifest.permission.CAMERA)) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else locationPermission()

        binding.emergencyBtn.setOnClickListener {
            if (!checkPermission(Manifest.permission.SEND_SMS)){
                requestSMSPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                return@setOnClickListener
            }
            sendSMS()
        }
    }

    private fun sendSMS() {
        try {
            val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requireActivity().getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }

            val mapUri =
                Uri.parse("https://maps.google.com/maps/search/${fusedLocation?.latitude},${fusedLocation?.longitude}")
            smsManager.sendTextMessage(
                Singleton.userModel?.contact, null,
                "Your friend need your help at $mapUri",
                null, null
            )
            val thread = Thread {
                val reportDataModel = ReportDataModel(
                    email = Singleton.userModel?.email!!,
                    contact = Singleton.userModel?.contact!!,
                    latitude = fusedLocation?.latitude.toString(),
                    longitude = fusedLocation?.longitude.toString(),
                )
                AppDatabase.getDatabase().reportDataDao().insert(reportDataModel)
            }
            thread.start()
            thread.join()
            Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun locationPermission() {
        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else enableLocation()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 30 * 1000.toLong()
            fastestInterval = 5 * 1000.toLong()
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.getSettingsClient(requireContext())
                .checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                Log.d("location>>>>>>>", "${response.locationSettingsStates?.isGpsPresent}")
                if (response.locationSettingsStates?.isGpsPresent == true) {
                    getLocation()
                }
                //do something
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            e.status.resolution?.let { it1 ->
                                IntentSenderRequest.Builder(it1).build()
                            }
                        launcher.launch(intentSenderRequest)
                    } catch (_: IntentSender.SendIntentException) {
                    }
                }
            }
        }
    }

}