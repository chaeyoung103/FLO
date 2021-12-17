package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding


class SongFragment: Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSongBinding.inflate(inflater,container,false)

        binding.albumMusicListLayout01.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }

        binding.albumMixButton.setOnClickListener {
            binding.albumMixButton.visibility = View.GONE
            binding.albumMixButtonOn.visibility = View.VISIBLE
        }

        binding.albumMixButtonOn.setOnClickListener {
            binding.albumMixButton.visibility = View.VISIBLE
            binding.albumMixButtonOn.visibility = View.GONE
        }

        return binding.root
        }
}