package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentStorageBinding

class StorageFragment: Fragment() {
    lateinit var binding : FragmentStorageBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStorageBinding.inflate(inflater,container,false)

        songDB = SongDatabase.getInstance(requireContext())!!


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerMusicStorageRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = LockerRVAdapter()
        //리스너 객체 생성 및 전달

        songRVAdapter.setMyItemClickListener(object : LockerRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false,songId)
            }
        })

        binding.lockerMusicStorageRecyclerview.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)

    }

}