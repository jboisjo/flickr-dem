package ca.jboisjoli.jayboisjoli.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImageUrl(imageUrl: String) =
        Glide.with(this)
                .asDrawable()
                .load(imageUrl)
                .into(this)