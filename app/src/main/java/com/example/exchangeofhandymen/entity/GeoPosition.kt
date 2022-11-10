package com.example.exchangeofhandymen.entity

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize

@Parcelize
class GeoPosition( val latitudegeo: Double,val  longitudegeo: Double)
    : GeoPoint(latitudegeo, longitudegeo), Parcelable
