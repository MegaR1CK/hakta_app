package com.example.hakta_app.models

data class CurrentTaskResponse(
	val goalType: String,
	val goalValue: Int,
	val description: String,
	val videos: List<String>,
	val photos: List<String>,
	val quest: Quest,
	val startDateConstraint: String,
	val completionTime: Int,
	val name: String,
	val audios: List<String>,
	val location: Location,
	val finishDate: String,
	val id: Int,
	val finishDateConstraint: String,
	val startDate: String,
	val status: String
)

data class Location(
	val lon: Double,
	val lat: Double
)

