package com.example.a12.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.bean.CalBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalAdapter extends RecyclerView.Adapter<CalAdapter.ViewHolder> {
    List<CalBean> mList;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView place, last, time,te;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.test_place);
            last = itemView.findViewById(R.id.test_last);
            time = itemView.findViewById(R.id.test_time);
            te = itemView.findViewById(R.id.tv_te);
        }

        public void setData(CalBean bean) {
            place.setText(bean.getPlace());
            last.setText(bean.getLast());
            time.setText(bean.getTime());
            te.setText(bean.getTe());
        }
    }
    public CalAdapter(List<CalBean> l_list) {
        mList = l_list;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_message, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CalBean bean = mList.get(position);
        holder.setData(bean);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
