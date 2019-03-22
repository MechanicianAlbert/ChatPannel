package com.albertech.inputdemo.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<Bean> extends RecyclerView.Adapter<BaseHolder<Bean>> implements OnItemClickListener<Bean> {

    private final List<Bean> DATA = new ArrayList<>(30);


    public void updateData(List<Bean> data) {
        DATA.clear();
        DATA.addAll(data);
        notifyDataSetChanged();
    }


    protected Bean getItemData(int position) {
        return DATA.get(position);
    }


    @Override
    public final int getItemCount() {
        return DATA.size();
    }

    @NonNull
    @Override
    public final BaseHolder<Bean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutResByViewType(viewType), parent, false);
        BaseHolder<Bean> holder = getHolderByViewType(viewType, itemView);
        holder.setAdapter(this);
        return holder;
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseHolder<Bean> holder, int position) {
        holder.onBind(position, DATA.get(position));
    }

    @Override
    public void onItemClick(int position, Bean bean) {

    }


    protected abstract int getLayoutResByViewType(int viewType);

    protected abstract BaseHolder<Bean> getHolderByViewType(int viewType, View itemView);

}
