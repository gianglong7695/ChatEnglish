package gianglong.app.chat.longchat.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by VCCORP on 8/9/2017.
 */

public class ImageLoaderUtil {
    public static void displayImage(String url, ImageView imageView){
        Glide.with(imageView.getContext())
                .load(url)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public static void displayImageResize(String url, ImageView imageView, int width, int height){
        Glide.with(imageView.getContext())
                .load(url)
                .skipMemoryCache(true)
                .override(width, height)
                .into(imageView);
    }
}
