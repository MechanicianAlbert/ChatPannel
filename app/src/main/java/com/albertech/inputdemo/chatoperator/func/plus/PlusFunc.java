package com.albertech.inputdemo.chatoperator.func.plus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albertech.inputdemo.R;

public class PlusFunc extends PlusIFunc {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_func_emoji, container, false);
        TextView tv = root.findViewById(R.id.tv);
        tv.setText("加号");
        return root;
    }
}
