package com.example.restofo.Helpers;
import android.graphics.Bitmap;
import com.squareup.picasso.Transformation;
public class RoundedTransformation implements Transformation {
    private final int radius;
    private final int margin;  // dp

    // радиус и отступ в пикселях
    public RoundedTransformation(int radius, int margin) {
        this.radius = radius;
        this.margin = margin;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap rounded = ImageHelper.getRoundedCornerBitmap(source, radius, margin);
        if (rounded != source) {
            source.recycle();
        }
        return rounded;
    }

    @Override
    public String key() {
        return "rounded(radius=" + radius + ", margin=" + margin + ")";
    }
}

