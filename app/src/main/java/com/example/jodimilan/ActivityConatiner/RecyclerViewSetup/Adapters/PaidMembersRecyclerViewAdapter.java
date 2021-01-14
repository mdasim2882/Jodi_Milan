package com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Adapters;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jodimilan.ActivityConatiner.Body.ProfileDetailsActivity;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Holders.PaidUsersCardItemsViewHolder;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Holders.PeoplesCardItemsViewHolder;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.HelperClasses.PrefVariables;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;


public class PaidMembersRecyclerViewAdapter extends RecyclerView.Adapter<PaidUsersCardItemsViewHolder> {
    public static final String COME_FROM = "comeFrom";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String CART_BADGE = "CART_BADGE";
    public static final String UNIQUE_ID = "uniqueID";
    public static final String ITEM_TYPE = "itemType";

    public final String TAG = getClass().getSimpleName();
    Context context;
    private List<ProductEntry> productList;
    Activity activity;

    FirebaseAuth fAuth;

    public PaidMembersRecyclerViewAdapter(Context context, List<ProductEntry> actualCards) {
        this.productList = actualCards;
        this.context = context;
        activity = (Activity) context;
        fAuth=FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public PaidUsersCardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paid_user_card_holder, parent, false);

        return new PaidUsersCardItemsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaidUsersCardItemsViewHolder holder, int position) {
        // TODO: Put Recycler ViewHolder Cards binding code here in MDC-102
        String profileId = productList.get(position).getProfileID();
        String uid = productList.get(position).getUID();
        String planName=productList.get(position).getPlanBought();
        Log.d(TAG, "onBindViewHolder() called with: Profile ID: = [" + profileId + "], Plan Name = [" + planName + "]+\n" +
                "Profile ID: = [" +uid+"]");

        if(planName!=null && !planName.equals("")){
            Log.d(TAG, "onBindViewHolder() called with: Profile ID: = [" + profileId + "], Plan Name = [" + planName + "]");
            holder.planName.setText(planName);
            holder.profileUID.setText(profileId);
            holder.copypaidUserID.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(profileId);
                Toast.makeText(v.getContext(), "Copied", Toast.LENGTH_SHORT).show();});

        }

        if(uid !=null && planName==null){
            holder.profileUID.setText(profileId);
            holder.planName.setText("No Plan Purchased");
            holder.copypaidUserID.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(profileId);
                Toast.makeText(v.getContext(), "Copied", Toast.LENGTH_SHORT).show();});

        }else if(uid==null){
            holder.profileUID.setText("Not Registered Yet");
            holder.planName.setText("No Plan Purchased");

        }


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
