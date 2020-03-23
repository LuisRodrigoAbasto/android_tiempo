package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.observers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.servidor.OpenWeatherServicio
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.utils.L
import com.orhanobut.logger.Logger.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LocalizacionObserver(var activity: Activity): LifecycleObserver {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var disposable:Disposable?=null
    private val openWeatherServicio by lazy {
        OpenWeatherServicio.crear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeListener(){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
    if(!ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION)){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener{location: Location? ->
                L.d("Latitud: ${location?.latitude}")
                L.d("Longitud: ${location?.longitude}")
                if(location!=null){
                    disposable = openWeatherServicio.tiempoActual(location.latitude,location.longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {temperatura->L.d("Temperatura $temperatura")},
                            { error->L.d("Error ${error.message}")})
                }
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseListener(){
        d("Estamos en el Listener de onPause")
    }
}