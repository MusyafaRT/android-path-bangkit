package com.example.intentapp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Person(val name: String?,
                  val age: Int?,
                  val email: String?,
                  val city: String?) : Parcelable