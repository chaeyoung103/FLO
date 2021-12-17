package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.id)

        setViews(album)
        setClickListeners(album)
        //ROOM_DB
        val songs = getSongs(album.id) //앨범안에 있는 수록곡들을 불러옵니다.
        // 이 다음에 수록곡 프래그먼트에 songs을 전달해주는 식으로 사용하시면 됩니다.

        //set click listener
        binding.albumBackArrowIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        //init viewpager
        val albumAdapter = AlbumViewpagerAdapter(this)

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        binding.albumAlbumIv.setImageResource(album.coverImg!!)

        if(isLiked){
            binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album: Album){
        val userId: Int = getUserIdx(requireContext())

        binding.albumLikeIb.setOnClickListener{
            if(isLiked){
                binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId, album.id )
            }else{
                binding.albumLikeIb.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }

    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId,albumId)

        songDB.albumDao().likeAlbum((like))
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx(requireContext())

        val likeId: Int? = songDB.albumDao().isLikeAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikedAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

//    private fun getJwt(): Int{
//        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//
//        return spf!!.getInt("jwt",0)
//    }

    //ROOM_DB
    private fun getSongs(albumIdx: Int): ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.songDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }
}