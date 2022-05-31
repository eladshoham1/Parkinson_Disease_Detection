package com.example.parkinson_disease_detection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson_disease_detection.R;

import java.util.List;

public class Adapter_Patient extends RecyclerView.Adapter<Adapter_Patient.MyViewHolder> {
    private List<String> allPatientsNames;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public Adapter_Patient(Context context, List<String> allPatientsNames) {
        if (context != null) {
            this.allPatientsNames = allPatientsNames;
            this.mInflater = LayoutInflater.from(context);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.mInflater.inflate(R.layout.list_patients, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.list_patients_LBL_fullName.setText(this.allPatientsNames.get(position));
    }

    @Override
    public int getItemCount() {
        return this.allPatientsNames.size();
    }

    public String getItem(int id) {
        return this.allPatientsNames.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickResults(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView list_patients_LBL_fullName;
        private Button list_patients_BTN_results;

        MyViewHolder(View itemView) {
            super(itemView);
            list_patients_LBL_fullName = itemView.findViewById(R.id.list_patients_LBL_fullName);
            list_patients_BTN_results = itemView.findViewById(R.id.list_patients_BTN_results);

            list_patients_BTN_results.setOnClickListener(new View.OnClickListener() {
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
