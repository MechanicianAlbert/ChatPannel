package com.albertech.inputdemo.chatoperator.shortcut;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.albertech.editpanel.base.IFunc;
import com.albertech.inputdemo.base.BaseSingleTypeAdapter;
import com.albertech.inputdemo.R;
import com.albertech.inputdemo.chatoperator.InputPannelView;

import java.util.Map;

import static com.albertech.editpanel.base.IDefaultFuncStatus.FUNC_INPUT;
import static com.albertech.inputdemo.chatoperator.func.IFuncStatus.FUNC_ALBUM;

public class ShortcutAdapter extends BaseSingleTypeAdapter<Integer, ShortcutAdapter.Holder> implements AdapterView.OnItemClickListener {

    static class Holder extends BaseSingleTypeAdapter.BaseHolder {

        private View iv;


        Holder(View convertView) {
            super(convertView);
            iv = $(R.id.iv);
        }

    }


    private final Map<Integer, IFunc> FUNCS;
    private final InputPannelView IPV;


    private int mSelectedPosition = -1;


    public ShortcutAdapter(InputPannelView ipv) {
        IPV = ipv;
        FUNCS = IPV.getRegisteredFuncs();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSelectedPosition == position) {
            mSelectedPosition = -1;
            switch (position) {
                case 0:
                    IPV.setFuncStatus(FUNC_INPUT);
                    break;
            }
        } else {
            mSelectedPosition = position;
            switch (position) {
                case 0:
                    IPV.setFuncStatus(FUNC_ALBUM);
                    break;
            }
        }
        notifyDataSetChanged();
    }


    @Override
    protected Holder onCreateViewHolder(ViewGroup parent) {
        return new Holder(View.inflate(parent.getContext(), R.layout.item_shortcut, null));
    }

    @Override
    protected void onBindViewHolder(Holder holder, int position, Integer item) {
        holder.iv.setBackgroundResource(item);
        holder.iv.setSelected(mSelectedPosition == position);
    }
}
