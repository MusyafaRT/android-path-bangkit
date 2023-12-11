package com.example.tanahku.data.remote

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("meta")
	val meta: Meta,

	@field:SerializedName("token")
	val token: String
)

data class Data(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)

data class Meta(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
