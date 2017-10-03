package com.azazellj.githubwatcher.binding;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.azazellj.githubwatcher.BuildConfig;
import com.azazellj.githubwatcher.R;
import com.azazellj.githubwatcher.util.DateUtil;
import com.bumptech.glide.Glide;

/**
 * Created by azazellj on 10/3/17.
 */

public final class BindingAdapters {

    /**
     * Load any image
     *
     * @param imageView any image view
     * @param imageUrl  image url
     */
    @BindingAdapter("imageUrl")
    public static void loadImage(final ImageView imageView, String imageUrl) {
        if (BuildConfig.DEBUG) Log.d("loadImage", imageUrl != null ? imageUrl : "");
        //show no image
        if (imageUrl != null && imageUrl.isEmpty()) {
            imageUrl = null;
        }

        String finalImageUrl = imageUrl;

        Glide.with(imageView.getContext())
                .load(finalImageUrl)
                .placeholder(android.R.drawable.gallery_thumb)
                .error(android.R.drawable.gallery_thumb)
                .crossFade()
                .dontAnimate()
                .into(imageView);
    }

    @BindingAdapter("formattedDate")
    public static void setDate(final TextView textView, final String formattedDate) {
        String date = DateUtil.getFormattedDate(formattedDate, DateUtil.PATTERN_DEFAULT);
        String finalDate = textView.getContext().getString(R.string.t_date_updated, date);

        textView.setText(finalDate);
    }
}
