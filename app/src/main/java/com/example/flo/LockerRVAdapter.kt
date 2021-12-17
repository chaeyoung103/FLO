package com.example.flo

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemStorageBinding

class LockerRVAdapter() : RecyclerView.Adapter<LockerRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    //클릭 인터페이스
    interface MyItemClickListener{
        fun onRemoveSong(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }



    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStorageBinding =
            ItemStorageBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: LockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.storageMenuBtnIv.setOnClickListener {
            removeSong(position)
            mItemClickListener.onRemoveSong(songs[position].id)
        }
    }

    //데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게됨
    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }


    //뷰홀더
    inner class ViewHolder(val binding: ItemStorageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.itemStorageTitleTv.text = song.title
            binding.itemStorageSingerTv.text = song.singer
            binding.itemStorageCoverImgIv.setImageResource(song.coverImg!!)
        }

    }
}