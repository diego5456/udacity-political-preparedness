package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@JsonClass(generateAdapter = true)
data class ElectionData(
    val id: Int,
    val name: String,
    val electionDay: Date,
    @Json(name="ocdDivisionId") val division: String
): Parcelable


