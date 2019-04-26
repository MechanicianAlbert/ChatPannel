package com.albertech.easypannel.func.image;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.albertech.common.base.activity.BaseActivity;
import com.albertech.common.util.Res;
import com.albertech.easypannel.R;

import java.util.ArrayList;
import java.util.List;


public class ImagePickActivity extends BaseActivity {

    private final ImageAdapter ADAPTER = new ImageAdapter() {
        @Override
        public void onSelectionCountChange(int count, boolean hasSelectedAll) {
            if (mTv != null) {
                mTv.setText(String.format(getApplicationContext().getResources().getString(R.string.str_image_pick_count), String.valueOf(count)));
            }
            if (mBtn != null) {
                mBtn.setText(count > 0 ? R.string.str_image_pick_confirm : R.string.str_image_pick_cancel);
            }
        }
    };

    private final Runnable QUERY = new Runnable() {
        @Override
        public void run() {
            Cursor c = null;
            try {
                c = getApplicationContext()
                        .getContentResolver()
                        .query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                new String[]{MediaStore.Files.FileColumns.DATA},
                                null,
                                null,
                                null
                        );
                final List<String> paths = new ArrayList<>();
                while (c != null && c.moveToNext()) {
                    paths.add(c.getString(c.getColumnIndex(MediaStore.Files.FileColumns.DATA)));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ADAPTER.updateData(paths);
                    }
                });
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
    };


    private RecyclerView mRv;
    private TextView mTv;
    private Button mBtn;


    public static void start(Context context) {
        context.startActivity(new Intent(context, ImagePickActivity.class));
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_image;
    }

    @Override
    protected void initView() {
        mRv = $(R.id.rv_image_pick);
        mTv = $(R.id.tv_pick_count);
        mBtn = $(R.id.btn_confirm_pick);

        mRv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4, LinearLayoutManager.VERTICAL, false));
        mTv.setText(String.format(getApplicationContext().getResources().getString(R.string.str_image_pick_count), String.valueOf(0)));
        mBtn.setText(R.string.str_image_pick_cancel);
    }

    @Override
    protected void initListener() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.updatePick(ADAPTER.getSelections());
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        mRv.setAdapter(ADAPTER);
        queryImage();
        ADAPTER.startSelecting();
    }

    private void queryImage() {
        new Thread(QUERY).start();
    }

}
