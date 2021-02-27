package com.example.androiddevchallenge.domain

import com.example.androiddevchallenge.domain.Puppy

interface PuppyRepository {
    fun getPuppies():List<Puppy>
}