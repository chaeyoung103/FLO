package com.example.flo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.LockerViewpagerAdapter
import com.example.flo.LockerRVAdapter
import com.example.flo.databinding.FragmentLockerBinding
import com.example.flo.databinding.FragmentStorageBinding
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    val information_2 = arrayListOf("저장한 곡","음악파일","저장앨범")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockderContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information_2[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener{
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root

    }

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView() {
        val jwt = getJwt(requireContext())

        if(jwt == ""){
            binding.lockerLoginTv.text = "로그인"

            binding.lockerLoginTv.setOnClickListener{
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener{
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

//    private fun login(): Int{
//        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//
//        return spf!!.getInt("jwt",0)
//    }

    private fun logout(){
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()

    }


}
