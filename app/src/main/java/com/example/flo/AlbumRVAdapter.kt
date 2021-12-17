package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    //클릭 인터페이스
    interface MyItemClickListener{
        fun onItemClick(album: Album)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    //뷰홀더 생성->호출되는 함수->아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding=ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)

        return ViewHolder(binding)
    }

    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {  mItemClickListener.onItemClick(albumList[position])}

    }

    fun addItems(albums: ArrayList<Album>) {
        albumList.clear()
        albumList.addAll(albums)
        notifyDataSetChanged()
    }

    //데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게됨
    override fun getItemCount(): Int =albumList.size


    //뷰홀더
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }
}