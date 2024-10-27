package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Official (
        val name: String,
        val address: List<Address>? = null,
        val party: String? = null,
        val phones: List<String>? = null,
        val urls: List<String>? = null,
        val photoUrl: String? = null,
        val channels: @RawValue List<Channel>? = null
): Parcelable