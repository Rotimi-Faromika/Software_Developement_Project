package com.example.software_development_project.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.software_development_project.Interface.ItemClickListener;
import com.example.software_development_project.R;

import org.jetbrains.annotations.NotNull;

public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtUserName, txtUserPhone, orderDate;
    public ItemClickListener listener;
    public Button showReservationsButton;

    public ReservationViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        txtUserName = itemView.findViewById(R.id.reservation_user_name);
        txtUserPhone = itemView.findViewById(R.id.reservation_phone);
        orderDate = itemView.findViewById(R.id.reservation_date_time);
        showReservationsButton = itemView.findViewById(R.id.show_all_products);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onClick(View v) {
        //noinspection deprecation
        listener.onClick(v, getAdapterPosition(), false);
    }
}
