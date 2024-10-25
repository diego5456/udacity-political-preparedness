package com.example.android.politicalpreparedness.network.jsonadapter

import android.util.Log
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionData
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ElectionAdapter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    @FromJson
    fun electionFromJson(json: Map<String, Any>): Election {
        val id = (json["id"] as String).toInt() // Cast id to Int
        val name = json["name"] as String
        val electionDayString = json["electionDay"] as String
        val electionDay = dateFormat.parse(electionDayString) ?: Date() // Handle parsing
        val ocdDivisionId = json["ocdDivisionId"] as String // Treat as a String
        val division = divisionFromJson(ocdDivisionId)

        return Election(id, name, electionDay, division)
    }

    @ToJson
    fun electionToJson(electionData: ElectionData): Map<String, Any> {
        return mapOf(
            "id" to electionData.id.toString(),
            "name" to electionData.name,
            "electionDay" to SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(electionData.electionDay),
            "ocdDivisionId" to electionData.division // Return as String
        )
    }
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
            .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter,"")
            .substringBefore("/")
        return Division(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson (division: Division): String {
        return division.id
    }

}
