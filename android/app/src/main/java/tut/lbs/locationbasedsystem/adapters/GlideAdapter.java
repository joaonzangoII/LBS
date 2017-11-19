package tut.lbs.locationbasedsystem.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideAdapter {

    // For a simple image list:
    public static void setImage(Context context,
                                int url,
                                final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .into(imageView);
    }

    // For a simple image list:
    public static void setImage(Context context,
                                Bitmap image,
                                final ImageView imageView) {
        Glide.with(context)
                .load(image)
                .crossFade()
                .into(imageView);
    }

    // For a simple image list:
    public static void setImage(Context context,
                                String url,
                                final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .crossFade()
                .into(imageView);
    }

}
