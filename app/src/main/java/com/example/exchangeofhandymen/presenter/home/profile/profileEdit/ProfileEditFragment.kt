package com.example.exchangeofhandymen.presenter.home.profile.profileEdit

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentProfileEditBinding

import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill
import com.example.exchangeofhandymen.entity.User.User
import com.example.exchangeofhandymen.presenter.home.profile.profileUser.profileAdapter.SkillsAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_woker.view.*
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileEditFragment()
       private const val GET_PHOTO="image/*"
        private val REQUIRED_PERMISSONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


    @Inject
    lateinit var profileEditFactory: ProfileEditFactory

    private val viewModel: ProfileEditViewModel by viewModels{profileEditFactory}
    private lateinit var binding: FragmentProfileEditBinding
    private var user: User?=null
    private  var skillsNameUser:List<String>?=null
    private val adapter= SkillsAdapter({addSkill()},{skill->deleteSkill(skill)})
    private var deleteSkill= arrayListOf<Skill>()
    private lateinit var uriAvatar:String
    private lateinit var fusedClient: FusedLocationProviderClient
    private var userGeo: GeoPosition?=null

    private val launcer=registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted->
    }
    private  val pickMedia=registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            Glide.with(requireContext()).load(uri).circleCrop().into(binding.imgUserAvatarEdit)
            viewModel.savePhoto(uri)
        } else {
            uriAvatar=""
        }
    }
   private val launcerGeo=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            map->
        if(map.values.isNotEmpty() && map.values.all { it }){
            startLocation()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileEditBinding.inflate(inflater)
        arguments.let {
            if(it !=null){
                user=it.getParcelable("user")
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            findNavController().currentBackStackEntryFlow.collect{
                val bundle=it.savedStateHandle.get<Bundle>("addFragment")
                if(bundle?.getParcelable<User>("user") !=null){
                    user=bundle?.getParcelable("user")
                    viewModel.gettSkills(user!!.skills)
                }
                val newskillName=bundle?.getString("skill")
                deleteSkill.remove(deleteSkill.find { it.name==newskillName })
            }

        }

        init()
        initUser()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
          viewModel.state.collect{
              when(it){
                  is  ProfEditState.Error -> {
                      binding.progressBarEdit.visibility=View.GONE
                      binding.descriptionEditLayout.error=it.errorDescription
                      binding.mailEditLayout.error=it.errorEmail
                      binding.nameEditLayout.error=it.errorName
                  }
                  ProfEditState.Loading -> {
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                    binding.progressBarEdit.visibility=View.VISIBLE
                  }
                  ProfEditState.Start -> {
                      binding.skillsEdit.scrollToPosition(adapter.currentList.size-1)
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      binding.progressBarEdit.visibility=View.GONE
                  }
                  is ProfEditState.Success -> {
                      binding.progressBarEdit.visibility=View.GONE
                      skillsNameUser= it.skills?.map { it.name } as ArrayList<String>?
                      adapter.submitList(it.skills)
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                  }
                  ProfEditState.SuccessSave -> {
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      binding.progressBarEdit.visibility=View.GONE
                      findNavController().popBackStack()
                  }
                  is ProfEditState.SuccessPhoto -> {
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      binding.progressBarEdit.visibility=View.GONE
                      Log.d("PHOTODELETE",it.PhotoUrl)
                      uriAvatar=it.PhotoUrl
                      if(it.PhotoUrl.isEmpty()) {
                          binding.imgUserAvatarEdit.setImageResource(R.drawable.backgrounf_img)
                      }
                  }
                  is ProfEditState.SuccessSaveGeo->{
                      binding.progressBarEdit.visibility=View.GONE
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      binding.txtGeoPositionGet.text= GeoPosition(it.lat, it.lon).getTownName(requireContext())
                      userGeo=GeoPosition(it.lat,it.lon)
                  }
                  is ProfEditState.ErrorCustom -> {
                      binding.progressBarEdit.visibility=View.GONE
                      binding.descriptionEditLayout.error=null
                      binding.mailEditLayout.error=null
                      binding.nameEditLayout.error=null
                      Toast.makeText(activity,it.customError,Toast.LENGTH_SHORT).show()
                  }
              }
          }
        }


        binding.btnSave.setOnClickListener{
            if(user!=null) {
                val decriptionEdit=binding.descriptionEdit.text.toString()
                val nameEdit=binding.nameEdit.text.toString()
                val emailEdit=binding.mailEdit.text.toString()
                val wokerFlag=checkWorkerFlag()
                val editUser= User(nameEdit,user!!.phone,emailEdit,user!!.rating,userGeo,user!!.skills,decriptionEdit,
                    wokerFlag,uriAvatar)
                viewModel.deleteUserSkill(deleteSkill.map { it.id } as ArrayList<String>)
                viewModel.editUser(editUser)
            }
        }

        binding.imgUserAvatarEdit.setOnClickListener {
            checkPermission()
        }

        binding.btnDeletePhoto.setOnClickListener{
            binding.imgUserAvatarEdit.setImageResource(R.drawable.backgrounf_img)
            viewModel.deletePhoto()
        }

        binding.exitEdit.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnGeo.setOnClickListener {
            fusedClient= LocationServices.getFusedLocationProviderClient(requireContext())
            checkGeoPermission()
        }

        binding.btnAuthOut.setOnClickListener {
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mainFragment, true)
                .build()
            viewModel.signOut()

            Firebase.auth.signOut()

            val navController = activity?.findNavController(R.id.nav_view)
            navController?.setGraph(R.navigation.nav_graph)
            navController?.navigate(R.id.action_global_logInFragment3,null,navOptions)
        }

        return binding.root
    }



    private fun checkWorkerFlag():Boolean {
        return binding.workerRadioGroup.checkedRadioButtonId ==binding.radioBtnWoker.id
    }

    private fun initUser() {
        if(user!=null) {
            binding.descriptionEdit.setText(user!!.description)
            binding.mailEdit.setText(user!!.email)
            binding.nameEdit.setText(user!!.name)
            if(user!!.wokerFlag){
                binding.workerRadioGroup.check(binding.radioBtnWoker.id)
            }else{
                binding.workerRadioGroup.check(binding.radioBtnEmployer.id)
            }
            viewModel.gettSkills(user!!.skills)
            uriAvatar=user!!.img
            userGeo= user!!.geoPoint
            if(userGeo!=null){
                binding.txtGeoPositionGet.text=userGeo!!.getTownName(requireContext())
            }

            if(user!!.img.isEmpty()){
                binding.imgUserAvatarEdit.setImageResource(R.drawable.backgrounf_img)
            }else{
                Glide.with( binding.imgUserAvatarEdit).load(user!!.img).circleCrop().into( binding.imgUserAvatarEdit)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.skillsEdit.adapter=adapter
        binding.skillsEdit.layoutManager=FlexboxLayoutManager(requireContext())

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)


        binding.descriptionEdit.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
    }

  private fun addSkill(){
        val bundle=Bundle()
        skillsNameUser= skillsNameUser?.minus(deleteSkill.map { it.name }.toSet())
      if(skillsNameUser?.size !=1){
          bundle.putStringArrayList("SkillsUser", skillsNameUser as ArrayList<String>)
      }
      val decriptionEdit=binding.descriptionEdit.text.toString()
      val nameEdit=binding.nameEdit.text.toString()
      val emailEdit=binding.mailEdit.text.toString()
      val wokerFlag=checkWorkerFlag()
        bundle.putParcelable("user",
            User(nameEdit,user!!.phone,emailEdit,user!!.rating,userGeo,user!!.skills,decriptionEdit,
            wokerFlag,uriAvatar)
        )
        bundle.putParcelableArrayList("deleteSkill",deleteSkill)

       findNavController().navigate(R.id.action_profileEditFragment_to_skillsAddFragment,bundle)
    }

    private fun deleteSkill(skill: Skill){
        deleteSkill.add(skill)

      adapter.submitList( adapter.currentList.minus(skill))
    }

    private fun pickImgFormGAlerry(){

        pickMedia.launch(GET_PHOTO)
    }


    private fun checkPermission(){
        val allGranted=ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
        if(!allGranted){
            launcer.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }else{
            pickImgFormGAlerry()
        }
    }

    private fun checkGeoPermission(){
        if(REQUIRED_PERMISSONS.all { permission->
                ContextCompat.checkSelfPermission(requireContext(),permission)==PackageManager.PERMISSION_GRANTED
            }){
        }else{
            launcerGeo.launch(REQUIRED_PERMISSONS)
        }
        startLocation()
    }

    private fun startLocation(){
        viewModel.saveGeo(fusedClient,REQUIRED_PERMISSONS)
    }

}