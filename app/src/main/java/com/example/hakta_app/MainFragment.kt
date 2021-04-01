package com.example.hakta_app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hakta_app.models.BestQuestsResponse
import com.example.hakta_app.models.CurrentTaskResponse
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.squareup.picasso.Picasso
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.android.synthetic.main.card_current_quest.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    companion object {
        private const val PERMISSION_CODE = 45
        private const val PERM_CODE = 30
    }

    lateinit var locationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (checkPermission()) {
            if (isGeoEnabled()) {
                getLocation(object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        p0?.lastLocation?.let { moveMap(it) }
                    }
                })
            }
        }
        else {
            activity?.let {
                requestPermissions(arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_CODE)
            }
        }

        App.MAIN_API.getBestQuests(App.TOKEN ?: "")
            .enqueue(object : Callback<BestQuestsResponse> {
                override fun onFailure(call: Call<BestQuestsResponse>, t: Throwable) {
                    t.message?.let { activity?.let { it1 -> App.errorAlert(it1, it) } }
                }

                override fun onResponse(call: Call<BestQuestsResponse>,
                                        response: Response<BestQuestsResponse>) {
                    view.recycler_best_quests.layoutManager =
                        GridLayoutManager(activity, 2)
                    view.recycler_best_quests.adapter =
                        response.body()?.content?.let { BestQuestsAdapter(it) }
                }
            })

        App.MAIN_API.getCurrentTask(App.TOKEN ?: "")
            .enqueue(object : Callback<CurrentTaskResponse> {
                override fun onFailure(call: Call<CurrentTaskResponse>, t: Throwable) {
                    t.message?.let { activity?.let { it1 -> App.errorAlert(it1, it) } }
                }

                override fun onResponse(call: Call<CurrentTaskResponse>,
                                        response: Response<CurrentTaskResponse>) {
                    val currentQuest = response.body()?.quest
                    if (currentQuest != null) {
                        current_quest_name.text = currentQuest.name
                        current_quest_desc.text = currentQuest.description
                        Picasso.get()
                            .load(currentQuest.mainPhoto)
                            .fit()
                            .into(current_quest_pic)
                    }
                    else {
                        view.card_no_tasks.visibility = View.VISIBLE
                        view.card_current_quest.visibility = View.GONE
                        view.card_tasks.visibility = View.GONE
                    }
                }
            })
        return view
    }

    @SuppressLint("MissingPermission")
    fun getLocation(cb: LocationCallback) {
        if (isGeoEnabled()) {
            locationClient.lastLocation?.addOnCompleteListener { it2 ->
                val location = it2.result
                if (location == null) {
                    val request = LocationRequest()
                    request.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                    val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
                    val settingsClient = context?.let { it1 -> LocationServices.getSettingsClient(it1) }
                    val task = settingsClient?.checkLocationSettings(builder.build())
                    task
                        ?.addOnSuccessListener {
                            val isLocationPresent = it.locationSettingsStates.isLocationPresent
                            locationClient.requestLocationUpdates(request, cb, Looper.getMainLooper())
                        }
                        ?.addOnFailureListener {
                            if (it is ResolvableApiException) {
                                it.startResolutionForResult(activity, PERM_CODE)
                            }
                        }
                }
                else {
                    moveMap(location)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERM_CODE && resultCode == Activity.RESULT_OK) {
            getLocation(object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    p0?.lastLocation?.let { moveMap(it) }
                }
            })
        }
    }

    private fun isGeoEnabled(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun moveMap(location: android.location.Location) {
        map_view.map.move(
            CameraPosition(Point(location.latitude, location.longitude),
                16.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.75f),
            null
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults.first() == PackageManager.PERMISSION_GRANTED &&
            grantResults.last() == PackageManager.PERMISSION_GRANTED) {
            getLocation(object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    p0?.lastLocation?.let { moveMap(it) }
                }
            })
        }
    }

    private fun checkPermission(): Boolean {
        val finePermission = context?.let { ActivityCompat.checkSelfPermission(it,
                android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED
        val coarsePermission = context?.let { ActivityCompat.checkSelfPermission(it,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED
        return finePermission && coarsePermission

    }

    override fun onStop() {
        super.onStop()
        view?.map_view?.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        view?.map_view?.onStart()
        MapKitFactory.getInstance().onStart()
    }
}