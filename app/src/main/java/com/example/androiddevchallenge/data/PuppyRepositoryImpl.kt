package com.example.androiddevchallenge.data

import com.example.androiddevchallenge.data.Api
import com.example.androiddevchallenge.domain.Puppy
import com.example.androiddevchallenge.domain.PuppyRepository

class PuppyRepositoryImpl(private val api: Api) : PuppyRepository {

    override fun getPuppies(): List<Puppy> = api.puppies

}