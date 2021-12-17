package com.example.flo

import android.service.quicksettings.Tile
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    var title: String = "",
    var singer: String = "",
    var millis : Int =0,
    var playTime: Int = 0,
    var isPlaying : Boolean = false,
    var music : String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int = 0
){
    @PrimaryKey(autoGenerate = true) var id: Int =0
}
