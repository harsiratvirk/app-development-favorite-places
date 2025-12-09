package com.example.attractiveplaces.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attractiveplaces.data.Place
import com.example.attractiveplaces.repositories.PlaceRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaceViewModel : ViewModel() {

    private val repository = PlaceRepository()

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showSnackbar = MutableSharedFlow<String>()
    val showSnackbar: SharedFlow<String> = _showSnackbar.asSharedFlow()

    init {
        loadPlaces()
    }

    fun loadPlaces() {
        viewModelScope.launch {
            _isLoading.value = true
            _places.value = repository.getPlaces()
            _isLoading.value = false
        }
    }

    fun addPlace(name: String, description: String?, streetAddress: String?, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.addPlace(name, description, streetAddress, latitude, longitude)
            loadPlaces()
            _showSnackbar.emit("Place added successfully!")
        }
    }
}
