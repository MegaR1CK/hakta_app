package com.example.hakta_app.models

data class BestQuestsResponse(
	val meta: List<Any>,
	val content: List<Quest>
)

data class Quest(
	val endDate: String,
	val rating: Double,
	val description: String,
	val mainPhoto: String,
	val creationDate: String,
	val photos: List<String>,
	val tags: List<String>,
	val difficulty: Int,
	val authorName: String,
	val name: String,
	val id: Int,
	val category: Category,
	val startDate: String,
	val tasks: List<TasksItem>
)

data class TasksItem(
	val endDate: String,
	val name: String,
	val id: String,
	val taskCompletionTime: String,
	val startDate: String,
	val status: String
)

data class Category(
	val name: String,
	val description: String,
	val photo: String,
	val id: Int
)

