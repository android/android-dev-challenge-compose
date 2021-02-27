package com.example.androiddevchallenge.domain


interface PuppyRepository {
    fun getPuppies():List<Puppy>
    fun find(term: String): List<Puppy>
}