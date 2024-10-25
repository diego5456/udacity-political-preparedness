package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
    val election: ElectionData,
    @Json(ignore = true) val pollingLocations: String? = null, //TODO: Future Use
    @Json(ignore = true) val contests: String? = null, //TODO: Future Use
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
)