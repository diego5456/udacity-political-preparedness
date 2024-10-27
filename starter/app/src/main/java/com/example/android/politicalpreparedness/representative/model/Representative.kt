package com.example.android.politicalpreparedness.representative.model

import android.os.Parcelable
import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Representative (
        val official: @RawValue Official,
        val office: @RawValue Office
): Parcelable