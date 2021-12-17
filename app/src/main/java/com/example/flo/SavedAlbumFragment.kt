package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentLockerSavedalbumBinding
import com.example.flo.databinding.FragmentStorageBinding

class SavedAlbumFragment: Fragment() {
    lateinit var binding : FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater,container,false)

        albumDB = SongDatabase.getInstance(requireContext())!!


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerMusicSavedalbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumLockerAdpater = AlbumLockerAdpater()
        //리스너 객체 생성 및 전달

        albumLockerAdpater.setMyItemClickListener(object : AlbumLockerAdpater.MyItemClickListener{
            override fun onRemoveAlbum(albumId: Int) {
                albumDB.albumDao().getLikedAlbums(getUserIdx(requireContext()))
            }
        })

        binding.lockerMusicSavedalbumRecyclerview.adapter = albumLockerAdpater

        albumLockerAdpater.addAlbums(albumDB.albumDao().getLikedAlbums(getUserIdx(requireContext())) as ArrayList)

    }

//    private fun getJwt() :Int{
//        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
//        val jwt = spf!!.getInt("jwt",0)
//        Log.d("MAIN_ACT/GET_JWT","jwt_token: $jwt")
//
//        return jwt
//    }

}