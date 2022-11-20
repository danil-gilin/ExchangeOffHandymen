package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeofhandymen.domain.*
import com.example.exchangeofhandymen.entity.Constance
import com.example.exchangeofhandymen.entity.CustomException
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User
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
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val gettSkillsUseCase: GettSkillsUseCase,
    private val editUserUseCase: EditUserUseCase,
    private val deleteSkillUseCase: DeleteSkillUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    @ActivityContext private val activity: Context
) : ViewModel() {

    private val _state = MutableStateFlow<ProfEditState>(ProfEditState.Start)
    val state = _state.asStateFlow()




    fun gettSkills(skillsId: List<String>?) {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            if (skillsId != null) {
                var listSkills = gettSkillsUseCase.getSkills(skillsId)
                listSkills = listSkills.plus(Skill("my", "+", 0))
                _state.value = ProfEditState.Success(listSkills)
            } else {
                val listSkills = listOf(Skill("my", "+", 0))
                _state.value = ProfEditState.Success(listSkills)
            }

        }
    }

    fun editUser(user: User) {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            var errorName: String? = null
            var errorEmail: String? = null
            var errorDescription: String? = null
            var flagNotError = true
            try {
                if (user.name?.isEmpty() == true){
                    errorName="Имя не может быть пусты"
                    flagNotError = false
                }
                if (user.email?.isEmpty() == true){
                    errorEmail="Почта не может быть пустой"
                    flagNotError = false
                }
                if(!isEmailValid(user.email.toString()) && user.email != Constance.NoInformationText){
                    errorEmail="Почта введена неверно"
                    flagNotError = false
                }
                if (user.description?.isEmpty() == true){
                    errorDescription="Расскажите о себе"
                    flagNotError = false
                }
                if (flagNotError) {
                    editUserUseCase.editProfile(user)
                    _state.value = ProfEditState.SuccessSave
                } else {
                    throw CustomException("Ошибка редактирования")
                }
            } catch (e: Exception) {
                _state.value = ProfEditState.Error(errorName, errorEmail, errorDescription)
            }
        }
    }

    fun deleteUserSkill(skillId: ArrayList<String>) {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            try {
                deleteSkillUseCase.deleteSkill(skillId)
            } catch (e: Exception) {

            }
        }
    }

   private fun isEmailValid(email:String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun savePhoto( uri:Uri) {
       viewModelScope.launch {
           _state.value = ProfEditState.Loading
           try {
               val url = savePhotoUseCase.savePhoto(uri)
               _state.value = ProfEditState.SuccessPhoto(url)
           }catch (e:Exception){
               _state.value = ProfEditState.Error(null,null,null)
           }
       }
    }

    fun deletePhoto() {
        viewModelScope.launch {
            _state.value = ProfEditState.Loading
            try {
                deletePhotoUseCase.deletePhoto()
                _state.value = ProfEditState.SuccessPhoto("")
            }catch (e:Exception){
                _state.value = ProfEditState.Error(null,null,null)
            }
        }
    }


    fun saveGeo(
        fusedClient: FusedLocationProviderClient,
        required_permission: Array<String>
    ) {
        viewModelScope.launch {
            _state.value=ProfEditState.Loading
            try {
                if (required_permission.all { permission ->
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
                                _state.value = ProfEditState.SuccessSaveGeo(
                                    location.latitude,
                                    location.longitude

                                )

                            }
                        }.await()
                } else {
                    _state.value = ProfEditState.ErrorCustom("Дайте разрешение, чтоб определить ваше местоположение")
                }
            }catch (e:Exception){
                Log.d("erroGeo","Here 2")
                _state.value = ProfEditState.ErrorCustom("Не удалось получить данные")
            }
        }
    }


}