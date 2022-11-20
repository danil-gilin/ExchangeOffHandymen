package com.example.exchangeofhandymen.entity

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class GeoPosition( val latitudegeo: Double,val  longitudegeo: Double)
    : GeoPoint(latitudegeo, longitudegeo), Parcelable{
       fun getTownName(context: Context):String{
           val geocoder = Geocoder(context, Locale("Ru"))
           val addresses:List<Address> = geocoder.getFromLocation( this.latitudegeo, this.longitudegeo, 1)
           val a= addresses[0]
           val cityName=a.locality?:a.adminArea
           return cityName
       }
    }
