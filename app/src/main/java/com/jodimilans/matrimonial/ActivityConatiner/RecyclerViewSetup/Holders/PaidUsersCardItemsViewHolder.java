package com.jodimilans.matrimonial.ActivityConatiner.RecyclerViewSetup.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jodimilans.matrimonial.R;


public class PaidUsersCardItemsViewHolder extends RecyclerView.ViewHolder {

    public CardView paidUserCard;
    public TextView profileUID;
    public ImageView copypaidUserID;
    public TextView planName;

    public PaidUsersCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        profileUID = itemView.findViewById(R.id.profile_id_db_for_admin);
        planName = itemView.findViewById(R.id.db_plan_purchased);
        paidUserCard = itemView.findViewById(R.id.paidUserCard);
        copypaidUserID = itemView.findViewById(R.id.copyPaidUsers);
        // addtoCart=itemView.findViewById(R.id.addtoCartButton);
        // TODO: Find and store views from itemView
    }
}
