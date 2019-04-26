package com.albertech.easypannel.func.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.albertech.common.base.recycler.select.SelectHolder;
import com.albertech.easypannel.R;


public class ImageHolder extends SelectHolder<ImageAdapter, String> {

    public ImageHolder(ImageAdapter adapter, @NonNull View itemView) {
        super(adapter, itemView);
    }

    @Override
    protected void onBind(int position, String path) {
        Bitmap b = BitmapFactory.decodeFile(path);
        ImageView iv = $(R.id.iv_image);
        iv.setImageBitmap(b);

        CheckBox cb = $(R.id.cb_image);
        cb.setChecked(isSelected(position));
    }
}
