package com.example.exchangeofhandymen.presenter.home.bag.newJob

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.SaveJobUseCase
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.job.Job
import com.example.exchangeofhandymen.presenter.home.profile.profileEdit.ProfEditState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class NewJobViewModel @Inject constructor(private val saveJobUseCase: SaveJobUseCase,
                                          @ActivityContext private val activity: Context) : ViewModel() {
    private val _state= MutableStateFlow<NewJobState>(NewJobState.Start)
    val state=_state.asStateFlow()

    fun saveJob(job: Job) {
        viewModelScope.launch {
            _state.value= NewJobState.Loading
            try {
                if (job.geopoint != null) {
                    saveJobUseCase.saveJob(job)
                    _state.value= NewJobState.Success
                } else {
                    _state.value= NewJobState.Error
                }
            }catch (e:Exception){
                _state.value=  NewJobState.Error
            }
        }
    }

    fun saveGeo(fusedClient: FusedLocationProviderClient, requiredPermissons: Array<String>) {
        viewModelScope.launch {
            _state.value= NewJobState.Loading
            try {
                if (requiredPermissons.all { permission ->
                        ContextCompat.checkSelfPermission(
                            activity,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    val location = fusedClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        object : CancellationToken() {
                            override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                                CancellationTokenSource().token

                            override fun isCancellationRequested() = false
                        })
                        .addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                Log.d("MyGeoer",location.longitude.toString())
                                Log.d("MyGeoer",location.latitude.toString())
                                _state.value = NewJobState.SuccessSaveGeo(
                                    location.latitude,
                                    location.longitude
                                )

                            }
                        }.await()
                } else {
                    _state.value = NewJobState.Error
                }
            }catch (e:Exception){
                _state.value = NewJobState.Error
            }
        }
    }
}