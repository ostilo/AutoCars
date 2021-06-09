package com.example.autocars.ui.base

import com.example.autocars.db.entities.Favorites
import com.example.autocars.db.entities.PopularList

interface IFavoriteCallback {
    fun passSearchLink1(
        video: Favorites?,
        position: Int
    )

}