package com.example.flo

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity: AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var player: Player

    //미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null
    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initPlayList()
        initSong()
        initClickListener()
    }

    override fun onPause() {
        super.onPause()

        songs[nowPos].millis = (songs[nowPos].playTime * binding.songProgressbarView.progress)
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("millis", player.millis2)
        editor.putBoolean("isPlaying", songs[nowPos].isPlaying)
        editor.apply()
    }

    // 앱이 종료될 때 리소스 해제
    override fun onDestroy() {
        super.onDestroy()


        player.interrupt() // 스레드를 해제함
        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun startPlayer() {
        player = Player(songs[nowPos].playTime, songs[nowPos].millis,songs[nowPos].isPlaying)
        player.start()
        binding.songProgressbarView.setOnSeekBarChangeListener(SeekbarListener())

    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        songs[nowPos].millis = spf.getInt("millis",0)
        songs[nowPos].isPlaying = spf.getBoolean("isPlaying",true)

        startPlayer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songTime01Tv.text =
            String.format("%02d:%02d", song.millis/1000/60,song.millis/1000%60)
        binding.songTime02Tv.text =
            String.format("%02d:%02d", song.playTime/60,song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressbarView.progress = (song.millis / song.playTime)
//        val music = resources.getIdentifier(song.music, "raw", this.packageName)
//        mediaPlayer = MediaPlayer.create(this, music)

        setPlayerStatus(song.isPlaying)


        if (song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            songs[nowPos].millis = (songs[nowPos].playTime * binding.songProgressbarView.progress)

            val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("songId", songs[nowPos].id)
            editor.putInt("millis", player.millis2)
            editor.putBoolean("isPlaying", songs[nowPos].isPlaying)
            editor.apply()
            val intent = Intent(this@SongActivity, MainActivity::class.java)

            startActivity(intent)
            player.interrupt()
            mediaPlayer?.pause()
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

    }

    private fun moveSong(direct: Int){

        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        player.interrupt()
        startPlayer()

        mediaPlayer?.release() // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null // 미디어플레이어 해제

        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
    }


    private fun setPlayerStatus(isPlaying: Boolean) {
        player.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            val music = resources.getIdentifier(songs[nowPos].music, "raw", this.packageName)
            mediaPlayer = MediaPlayer.create(this, music)
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.seekTo(player.millis2)
            mediaPlayer?.start()
        } else {
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

            mediaPlayer?.pause()
        }
    }

    inner class SeekbarListener : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress : Int, fromuser: Boolean){
            player.millis2 = seekBar!!.progress*(songs[nowPos].playTime)
            binding.songTime01Tv.text = String.format("%02d:%02d",songs[nowPos].millis/1000/60,songs[nowPos].millis/1000%60)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar? ) {
            mediaPlayer?.seekTo(player.millis2)
            binding.songTime01Tv.text = String.format("%02d:%02d", songs[nowPos].millis / 1000 / 60, songs[nowPos].millis / 1000 % 60)
        }

    }

    inner class Player(private val playTime:Int, var millis: Int,var isPlaying: Boolean):Thread(){
        var millis2 = millis

        @SuppressLint("SetTextI18n")
        override fun run(){
            try{
                while(true){
                    if(millis2/1000 >= playTime)
                        break
                    if(isPlaying){
                        sleep(1)
                        millis2++
                        runOnUiThread{
                            binding.songProgressbarView.progress=(millis2/playTime).toInt()
                            binding.songTime01Tv.text=String.format("%02d:%02d",millis2/1000/60,millis2/1000%60)
                        }
                    }
                }
            }catch(e : InterruptedException){
                Log.d("interrupt","쓰레드가 종료되었습니다.")
            }
        }
    }

}
