package com.example.android.politicalpreparedness.network.models

import com.example.android.politicalpreparedness.network.divisionFromJson
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionResponse(
        val kind: String,
        val elections: List<ElectionData>
)

fun ElectionResponse.asDatabaseModel(): Array<Election>{
        return elections.map {
                Election(
                        id = it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = divisionFromJson(it.division)
                )
        }.toTypedArray()
}

fun ElectionResponse.asDomainModel(): List<ElectionData>{
        return elections.map {
                ElectionData(
                        id = it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = it.division.toString()
                )
        }
}
