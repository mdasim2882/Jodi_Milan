package com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Holders.PeoplesCardItemsViewHolder;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;



public class PeoplesCardRecyclerViewAdapter extends RecyclerView.Adapter<PeoplesCardItemsViewHolder> {
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

    public PeoplesCardRecyclerViewAdapter(Context context, List<ProductEntry> actualCards) {
        this.productList = actualCards;
        this.context = context;
        activity = (Activity) context;
    }

    private DialogInterface.OnClickListener performDialogOperations(String productID, String productName, String productCost) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Toast.makeText(context, "Yes Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Toast.makeText(context, "No Clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }
            }
        };
    }

    @NonNull
    @Override
    public PeoplesCardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_profile_card, parent, false);

        return new PeoplesCardItemsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PeoplesCardItemsViewHolder holder, int position) {
        // TODO: Put Recycler ViewHolder Cards binding code here in MDC-102
        String productImage = productList.get(position).getPhotoLink();
        String getCardsUri = productImage;
        String fullName = productList.get(position).getFullName();
        String mobileno = productList.get(position).getMobile();
        String city = productList.get(position).getCity();
        String profileId = productList.get(position).getProfileID();
        String uid = productList.get(position).getUID();
        String profession = productList.get(position).getEmployedIn();


            Picasso.get().load(productImage).into(holder.imgCard);
            holder.personName.setText(fullName);
            holder.personProfession.setText(profession);
            holder.personLocation.setText(city);

        Log.e(TAG, "onBindViewHolder: "+"\n"+"Name: "+fullName+
                "\nMobile no.: "+mobileno+"\n"+
                "City: " +city+
                "\nProfile Id: " +profileId+
                "\nUID: "+uid );
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
