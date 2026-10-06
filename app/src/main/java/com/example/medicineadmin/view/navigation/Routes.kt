package com.example.medicineadmin.view.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {

    @Serializable
    data object Dashboard : Routes

    @Serializable
    data object Users : Routes

    @Serializable
    data object Products : Routes

    @Serializable
    data object Categories : Routes

    @Serializable
    data object Poster : Routes

    @Serializable
    data object Orders : Routes

    @Serializable
    data object Sales : Routes

    @Serializable
    data object Settings : Routes

    @Serializable
    data object Notifications : Routes
}