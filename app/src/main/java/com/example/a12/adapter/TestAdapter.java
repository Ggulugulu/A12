package com.example.a12.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a12.R;
import com.example.a12.module_style.TestBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private List<TestBean> mList;
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView title;
        TextView count;
        TextView time;
        TextView result;
        RelativeLayout test;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test = itemView.findViewById(R.id.ll_test);
            iv = itemView.findViewById(R.id.iv_test);
            title = itemView.findViewById(R.id.tv_title);
            count = itemView.findViewById(R.id.tv_count);
            time = itemView.findViewById(R.id.tv_time);
            result = itemView.findViewById(R.id.tv_result);

            test.setOnClickListener(this);
        }


        public void setData(TestBean bean){
            iv.setImageResource(R.drawable.item_test);
            title.setText(bean.getTitle());
            count.setText(bean.getCount());
            time.setText(bean.getTime());
            result.setText(bean.getResult());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
    public TestAdapter(List<TestBean> l_list) {
        mList = l_list;
    }


    @NonNull
    @NotNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_all, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TestAdapter.ViewHolder holder, int position) {
        TestBean bean = mList.get(position);
        holder.setData(bean);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }
}
