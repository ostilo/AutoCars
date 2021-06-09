package com.example.autocars.ui.base

import android.widget.ImageView
import com.example.autocars.db.entities.CarMedia

interface ICarMediaCallback {
  fun passVideoLink(
      video: CarMedia?,
      position: Int
  )

}