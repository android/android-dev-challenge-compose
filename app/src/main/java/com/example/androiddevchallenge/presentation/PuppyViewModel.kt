package com.example.androiddevchallenge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.data.Api
import com.example.androiddevchallenge.data.PuppyRepositoryImpl
import com.example.androiddevchallenge.domain.Puppy

class PuppyViewModel : ViewModel() {

    private val repository = PuppyRepositoryImpl(Api)

    private val _puppiesLiveData = MutableLiveData<List<Puppy>>()
    val puppiesLiveData : LiveData<List<Puppy>> = _puppiesLiveData

    fun getPuppies(){
        _puppiesLiveData.value = repository.getPuppies()
    }

}