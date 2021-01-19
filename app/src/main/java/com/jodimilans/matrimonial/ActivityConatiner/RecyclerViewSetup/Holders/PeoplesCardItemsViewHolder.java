package com.jodimilans.matrimonial.ActivityConatiner.RecyclerViewSetup.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jodimilans.matrimonial.R;


public class PeoplesCardItemsViewHolder extends RecyclerView.ViewHolder {

    public CardView productCard;
    public ImageView imgCard;
    public TextView personName;
    public TextView personLocation;
    public TextView personProfession;

    public PeoplesCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        imgCard = itemView.findViewById(R.id.combinationImage);
        personName = itemView.findViewById(R.id.combination_title);
        personLocation = itemView.findViewById(R.id.combinationPrice);
        personProfession = itemView.findViewById(R.id.product_validity);
        productCard = itemView.findViewById(R.id.productcombinations);
        // addtoCart=itemView.findViewById(R.id.addtoCartButton);
        // TODO: Find and store views from itemView
    }
}
