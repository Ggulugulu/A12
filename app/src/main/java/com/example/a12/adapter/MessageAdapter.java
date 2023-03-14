package com.example.a12.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.module_style.MessageBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<MessageBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView wangming;
        TextView neirong;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_touxiang);
            wangming = itemView.findViewById(R.id.tv_wangming);
            neirong = itemView.findViewById(R.id.tv_neirong);
            time = itemView.findViewById(R.id.tv_time);
        }

        public void setData(MessageBean bean){
            iv.setImageResource(R.drawable.touxiang);
            wangming.setText(bean.getWangming());
            neirong.setText(bean.getNeirong());
            time.setText(bean.getTime());
        }
    }
    public MessageAdapter(List<MessageBean> l_list) {
        mList = l_list;
    }
    @NonNull
    @NotNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        final MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.ViewHolder holder, int position) {
        MessageBean bean = mList.get(position);
        holder.setData(bean);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
