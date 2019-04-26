package com.albertech.easypannel.func.image;

import java.util.ArrayList;
import java.util.List;

public class ImagePicker {

    public interface OnImagePickListener {

        void onImagePick(List<String> picked);
    }


    private static final List<String> PICKED = new ArrayList<>();

    private static OnImagePickListener sListener;


    public static void setListener(OnImagePickListener listener) {
        sListener = listener;
    }

    public static void removeListener() {
        setListener(null);
    }

    static void updatePick(List<String> picked) {
        PICKED.clear();
        PICKED.addAll(picked);
        if (sListener != null) {
            sListener.onImagePick(PICKED);
        }
    }

}
