package com.example.attractiveplaces.repositories

import android.util.Log
import com.example.attractiveplaces.data.Place
import com.example.attractiveplaces.nettverk.Api

class PlaceRepository {
    suspend fun getPlaces(): List<Place> {
        return try {
            Api.retrofitService.getPlaces()
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error fetching places: ${e.message}")
            emptyList()
        }
    }

    suspend fun addPlace(name: String, description: String?, streetAddress: String?, latitude: Double, longitude: Double) {
        try {
            Api.retrofitService.addPlace(name, description, streetAddress, latitude, longitude)
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error adding place: ${e.message}")
        }
    }
}
