package com.example.parkinson_disease_detection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson_disease_detection.R;
import com.example.parkinson_disease_detection.models.Result;
import com.example.parkinson_disease_detection.utils.MyDate;

import java.util.List;

public class Adapter_Result extends RecyclerView.Adapter<Adapter_Result.MyViewHolder> {
    private List<Result> allResults;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public Adapter_Result(Context context, List<Result> allResults) {
        if (context != null) {
            this.allResults = allResults;
            this.mInflater = LayoutInflater.from(context);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.mInflater.inflate(R.layout.list_results, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Result result = this.allResults.get(position);

        holder.list_results_LBL_fullName.setText(result.getName());
        holder.list_results_LBL_date.setText(MyDate.makeDateString(result.getTime()));
        holder.list_results_LBL_result.setText(result.getResult());
    }

    @Override
    public int getItemCount() {
        return this.allResults.size();
    }

    public Result getItem(int id) {
        return this.allResults.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickResults(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView list_results_LBL_fullName;
        private TextView list_results_LBL_date;
        private TextView list_results_LBL_result;

        MyViewHolder(View itemView) {
            super(itemView);
            list_results_LBL_fullName = itemView.findViewById(R.id.list_results_LBL_fullName);
            list_results_LBL_date = itemView.findViewById(R.id.list_results_LBL_date);
            list_results_LBL_result = itemView.findViewById(R.id.list_results_LBL_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClickResults(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}