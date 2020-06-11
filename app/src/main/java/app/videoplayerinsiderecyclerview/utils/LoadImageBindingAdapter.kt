package app.videoplayerinsiderecyclerview.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import app.videoplayerinsiderecyclerview.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class LoadImageBindingAdapter {
    companion object{
        @JvmStatic
        @BindingAdapter(value = ["thumbnail", "error"], requireAll = false)
        fun loadImage(view: ImageView, profileImage: String?, error: Int) {
            if (!profileImage.isNullOrEmpty()) {
                Glide.with(view.context)
                    .setDefaultRequestOptions(
                        RequestOptions()
                        .placeholder(R.drawable.white_background)
                        .error(R.drawable.white_background))
                    .load(profileImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view)
            }
        }
    }
}