package com.example.snap

import android.icu.text.CaseMap
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SnapShotDataClass(
    var id: String = "",
    var title: String = "",
    var photoUrl: String = "",
    var likeList: Map<String, Boolean> = mutableMapOf()

)
