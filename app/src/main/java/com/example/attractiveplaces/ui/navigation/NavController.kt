package com.example.attractiveplaces.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.attractiveplaces.ui.screens.FormScreen
import com.example.attractiveplaces.ui.screens.MapScreen
import com.example.attractiveplaces.viewmodels.PlaceViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val placeViewModel: PlaceViewModel = viewModel()

    NavHost(navController = navController, startDestination = "map") {
        composable("map") {
            MapScreen(
                placeViewModel = placeViewModel,
                onMapClick = { latLng ->
                    navController.navigate("form/${latLng.latitude}/${latLng.longitude}")
                }
            )
        }
        composable(
            route = "form/{lat}/{lng}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lng") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0
            val lng = backStackEntry.arguments?.getFloat("lng")?.toDouble() ?: 0.0
            FormScreen(
                placeViewModel = placeViewModel,
                latitude = lat,
                longitude = lng,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
