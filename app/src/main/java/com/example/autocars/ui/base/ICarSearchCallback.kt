package com.example.autocars.ui.base

import android.widget.ImageView
import com.example.autocars.db.entities.CarMedia
import com.example.autocars.db.entities.PopularList

interface ICarSearchCallback {

  fun passSearchLink(
      video: PopularList?,
      position: Int
  )


}