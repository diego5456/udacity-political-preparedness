package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.Division


//fun parseElections()

fun divisionFromJson (ocdDivisionId: String): Division {
    val countryDelimiter = "country:"
    val stateDelimiter = "state:"
    val country = ocdDivisionId.substringAfter(countryDelimiter,"")
        .substringBefore("/")
    val state = ocdDivisionId.substringAfter(stateDelimiter,"")
        .substringBefore("/")
    return Division(ocdDivisionId, country, state)
}