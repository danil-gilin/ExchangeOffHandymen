package com.example.exchangeofhandymen.presenter.home.bag.newJob

sealed class NewJobState {
   data  class SuccessSaveGeo(val latitude: Double,val longitude: Double) : NewJobState()
    object Success: NewJobState()
    object Start: NewJobState()
    object Error: NewJobState()
    object Loading: NewJobState()
}