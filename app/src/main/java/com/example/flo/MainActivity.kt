package com.example.flo

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    //Gson
    private val gson:Gson = Gson()
    //Song
    private lateinit var song: Song

    private var mediaPlayer: MediaPlayer? = null
    lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        inputDummySongs()
        inputDummyAlbums()

        initClickListener()


        binding.mainPlayerLayout.setOnClickListener{
            Log.d("nowSongId", song.id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.putInt("millis", player.millis2)
            editor.putBoolean("isPlaying", song.isPlaying)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)

            startActivity(intent)
            player.interrupt()
            mediaPlayer?.pause()
        }


        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            statusBarColor = Color.TRANSPARENT
        }

    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }
        song.millis = spf.getInt("millis",0)
        song.isPlaying = spf.getBoolean("isPlaying",false)
        player = Player(song.playTime, song.millis,song.isPlaying)
        player.start()
        setMiniPlayer(song)
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    private fun setMiniPlayer(song : Song){
        binding.mainminiplayerTitleTv.text = song.title
        binding.mainminiplayerSingerTv.text = song.singer
        binding.mainminiplayerProgressbarView.progress = song.millis/song.playTime

        setminiPlayerStatus(song.isPlaying)

        val music = resources.getIdentifier(song.music, "raw",this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isPlaying){
            binding.mainPauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
            mediaPlayer?.seekTo(player.millis2)
            mediaPlayer?.start()
        }
        else{
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }
    }

    private fun initClickListener() {

        binding.mainMiniplayerBtn.setOnClickListener {
            setminiPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setminiPlayerStatus(false)
        }

//        binding.mainPreviousBtn.setOnClickListener {
//            moveSong(-1)
//        }
//
//        binding.mainNextBtn.setOnClickListener {
//            moveSong(+1)
//        }

    }

    private fun setminiPlayerStatus(isPlaying: Boolean) {
        player.isPlaying = isPlaying
        song.isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.seekTo(player.millis2)
            mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE

            mediaPlayer?.pause()
        }
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty())
            return

        songDB.albumDao().insert(
            Album(
                1,
                "strawberry moon", "아이유 (IU)", R.drawable.strawberrymoon_album_cover
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "너를 생각해", "주시크 (Joosiq)", R.drawable.joosiq_album_cover
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "OHAYO MY NIGHT", "디핵 , PATEKO (파테코)", R.drawable.ohayomynight_album_cover
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "가을 타나 봐", "바이브 (VIBE)", R.drawable.vibe_album_cover
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "다정히내이름을부르면", "경서예지 , 전건호", R.drawable.gyungseo_album_cover
            )
        )
        songDB.albumDao().insert(
            Album(
                6,
                "낮 밤 (feat. 박재범)", "이영지", R.drawable.younggi_album_cover
            )
        )
        songDB.albumDao().insert(
            Album(
                7,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

    }

    //ROOM_DB
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "strawberry moon",
                "아이유 (IU)",
                0,
                212,
                false,
                "music_strawberry",
                R.drawable.strawberrymoon_album_cover,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "너를 생각해",
                "주시크 (Joosiq)",
                0,
                244,
                false,
                "music_joosiq",
                R.drawable.joosiq_album_cover,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "OHAYO MY NIGHT",
                "디핵 , PATEKO (파테코)",
                0,
                238,
                false,
                "music_ohayo",
                R.drawable.ohayomynight_album_cover,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "가을 타나 봐",
                "바이브 (VIBE)",
                0,
                232,
                false,
                "music_vibe",
                R.drawable.vibe_album_cover,
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "낮 밤 (feat. 박재범)",
                "이영지",
                0,
                215,
                false,
                "music_day",
                R.drawable.younggi_album_cover,
                false,
                5
            )
        )

        songDB.songDao().insert(
            Song(
                "다정히내이름을부르면",
                "경서예지, 전건호",
                0,
                231,
                false,
                "music_gyung",
                R.drawable.gyungseo_album_cover,
                false,
                6
            )
        )

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                217,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
                7
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                190,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
                7
            )
        )

        songDB.songDao().insert(
            Song(
                "Coin",
                "아이유 (IU)",
                0,
                197,
                false,
                "music_coin",
                R.drawable.img_album_exp2,
                false,
                7
            )
        )

        songDB.songDao().insert(
            Song(
                "봄 안녕 봄",
                "아이유 (IU)",
                0,
                325,
                false,
                "music_spring",
                R.drawable.img_album_exp2,
                false,
                7
            )
        )

        songDB.songDao().insert(
            Song(
                "Celebrity",
                "아이유 (IU)",
                0,
                195,
                false,
                "music_celebrity",
                R.drawable.img_album_exp2,
                false,
                7
            )
        )


        val _songs = songDB.songDao().getSongs()
        Log.d("DB DATA", _songs.toString())

    }

    inner class Player(private val playTime:Int,private var millis:Int,var isPlaying: Boolean):Thread(){
        var millis2 = millis
        override fun run(){
            try{
                while(true){
                    if(millis2/1000 >= playTime)
                        break
                    if(isPlaying){
                        sleep(1)
                        millis2++
                        runOnUiThread{
                            binding.mainminiplayerProgressbarView.progress=millis2/playTime
                        }
                    }
                }
            }catch(e : InterruptedException){
                Log.d("interrupt","쓰레드가 종료되었습니다.")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }

}

