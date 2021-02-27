package com.example.androiddevchallenge.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Puppy(val name: String, val age: String, val breed: String, val imageUrl: String) :
    Parcelable