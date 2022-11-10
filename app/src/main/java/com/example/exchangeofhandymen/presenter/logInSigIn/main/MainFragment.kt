package com.example.exchangeofhandymen.presenter.logInSigIn.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.exchangeofhandymen.MainActivity
import com.example.exchangeofhandymen.R
import com.example.exchangeofhandymen.databinding.FragmentMainBinding
import com.example.exchangeofhandymen.entity.OnboardingItem
import com.example.exchangeofhandymen.presenter.logInSigIn.main.onBoarding.OnboardingItemsAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {



    lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

    private val auth = FirebaseAuth.getInstance()
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)


        if(auth.currentUser!=null){
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mainFragment, true)
                .build()

            val bundle=Bundle()
            bundle.putBoolean("newUser",false)



            findNavController().navigate(R.id.action_mainFragment_to_homeNavFragment,bundle,navOptions=navOptions)
        }



        binding.textSkip.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_logInFragment,null)
        }
        binding.buttonGetStart.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_logInFragment,null)
        }


        return binding.root
    }




    private fun setOnboardingItems() {
        onboardingItemsAdapter =
            OnboardingItemsAdapter(
                listOf(
                    OnboardingItem(R.drawable.job_search, "Поиск работы","Наша приложение нацелено на то ,чтоб помочь найти вам работу."),
                    OnboardingItem(R.drawable.job_do, "Процесс подбора","Мы нацелены на несложную работу посилную каждому из-вас"),
                    OnboardingItem(R.drawable.mountains, "Зарабатывай и иди вперед","Мы желаем вам удачи в поисках ваших первых подработок"),
                ))

       binding.oneBoardingViewPager.adapter=onboardingItemsAdapter
        binding.oneBoardingViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (binding.oneBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
        binding.imageNext.setOnClickListener {
            if(binding.oneBoardingViewPager.currentItem+1<onboardingItemsAdapter.itemCount){
                binding.oneBoardingViewPager.currentItem+=1
            }else{

            }
        }

    }


    override fun onResume() {
        super.onResume()
        binding.oneBoardingViewPager.setCurrentItem(0,false)
    }

    private fun setupIndicators(){
        val indicators= arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams:LinearLayout.LayoutParams=LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices){
            indicators[i]= ImageView(activity?.applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.indicator_inactive_background))
                it.layoutParams=layoutParams
                binding.indicatorContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position:Int){
        val childCount=binding.indicatorContainer.childCount
        for (i in 0 until childCount){
            val imageView=binding.indicatorContainer.getChildAt(i) as ImageView
            if(i ==position){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.indicator_active_background))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.indicator_inactive_background))
            }
        }
    }

}