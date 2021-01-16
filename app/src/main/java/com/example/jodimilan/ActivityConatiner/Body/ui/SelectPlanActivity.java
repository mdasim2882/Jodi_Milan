package com.example.jodimilan.ActivityConatiner.Body.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jodimilan.HelperClasses.PrefVariables;
import com.example.jodimilan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SelectPlanActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener, PaymentResultListener {
    private final String TAG=getClass().getSimpleName();
    public static final String DIAMOND_HEADING = "Diamond Plan (12 Months)";
    public static final String PLATINUM_PLAN_18_MONTHS = "Platinum Plan(18 Months)";
    public static final String TITANIUM_PLAN_28_MONTHS = "Titanium Plan(28 Months)";
    public static final String PERSONAL_PLAN_36_MONTHS = "Personal Plan (36 Months)";
    public static final String SILVER_PLAN_3_MONTHS = "Silver Plan (3 Months)";
    public static final String GOLD_PLAN_6_MONTHS = "Gold Plan (6 Months)";
    public static final String CHOOSE_A_PLAN = "Choose a plan";
    FirebaseAuth fAuth;
    String PAID_PRICE=null;
    String SELECT_ITEM=null;

    TextView featureOne,featurePrice,heading;
    Button purchaseButton;
    String[] country = {SILVER_PLAN_3_MONTHS, GOLD_PLAN_6_MONTHS, DIAMOND_HEADING,
            PLATINUM_PLAN_18_MONTHS,
            TITANIUM_PLAN_28_MONTHS, PERSONAL_PLAN_36_MONTHS};

    String[] price = {"Rs 5600", "Rs 8500","Rs 11000","Rs 15000","Rs 35000","Rs 55000"};
LinearLayout linearLayout;
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_home);
        initializeViews();
        setUpToolbar();
        Checkout.preload(getApplicationContext());
        fAuth=FirebaseAuth.getInstance();
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setSelection(0);
    }
    private void setUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.subscription_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    private void initializeViews() {
        spin = (Spinner) findViewById(R.id.spinnerPlans);
        purchaseButton =  findViewById(R.id.purchase_now_btn);
        spin.setOnItemSelectedListener(this);
        featureOne=findViewById(R.id.plan_detailsfirst);
        heading=findViewById(R.id.plan_heading);
        featurePrice=findViewById(R.id.plan_price);
        linearLayout=findViewById(R.id.plan_container);

        purchaseButton.setOnClickListener(v -> startPayment());
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
        String item=country[position];
//        if(!item.equals(CHOOSE_A_PLAN)){
            linearLayout.setVisibility(View.VISIBLE);
            featurePrice.setText(price[position]);
            PAID_PRICE=price[position];
            SELECT_ITEM=item;
            purchaseButton.setVisibility(View.VISIBLE);
            if(item.equals(SILVER_PLAN_3_MONTHS)){
                heading.setText(SILVER_PLAN_3_MONTHS);
                featureOne.setText(Html.fromHtml( "&#8888; Total access to contacts "+ 120));

            }   else if(item.equals(GOLD_PLAN_6_MONTHS)){
                heading.setText(GOLD_PLAN_6_MONTHS);
                featureOne.setText(Html.fromHtml("&#8888; Total access to contacts "+ 160));


            }  else if(item.equals(DIAMOND_HEADING)){
                heading.setText(DIAMOND_HEADING);
                featureOne.setText(Html.fromHtml("&#8888; Total access to contacts "+ 200));



            }    else if(item.equals(PLATINUM_PLAN_18_MONTHS)){
                heading.setText(PLATINUM_PLAN_18_MONTHS);
                featureOne.setText(Html.fromHtml("&#8888; Total access to contacts "+ 300));

            }   else if(item.equals(TITANIUM_PLAN_28_MONTHS)){
                heading.setText(TITANIUM_PLAN_28_MONTHS);
                featureOne.setText(Html.fromHtml("&#8888; Total access to contacts "+ 450));


            }   else if(item.equals(PERSONAL_PLAN_36_MONTHS)){
                heading.setText(PERSONAL_PLAN_36_MONTHS);
                featureOne.setText(Html.fromHtml("&#8888; Total access to contacts "+ 620));

            }
        }
//        else {
//            linearLayout.setVisibility(View.GONE);
//        }




//
//    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    //------------------------RAZOR PAY API METHODS-----------------------------------//
    public void startPayment() {
//    checkout.setKeyID("<YOUR_KEY_ID>");
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_puvvJbslTM7CKw");
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.jodi_milan_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            Log.e(TAG, "Payment: Amount " + PAID_PRICE);
            int payableAmount=Integer.parseInt(PAID_PRICE.substring(3))*100;
            options.put("name", "Jodi Milan");
            options.put("description", SELECT_ITEM);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//        options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#E91E63");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(payableAmount));//pass amount in currency subunits


//        JSONObject prefill=new JSONObject();
//        prefill.put("email", "mohdasim2882@gmail.com");
//        prefill.put("contact","+919580130679");
//            options.put("prefill.email", "mohdasim2882@gmail.com");
            options.put("prefill.contact",  "");

//        options.put("prefill", prefill);
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        try {
            Toast.makeText(this, "Payment ID: " + s, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show();

            Map<String, Object> purchseInfo = new HashMap<>();
            purchseInfo.put("purchaseTime", FieldValue.serverTimestamp());
            purchseInfo.put("purcahsePrice", PAID_PRICE);
            purchseInfo.put("purchaseItemName",SELECT_ITEM);
            purchseInfo.put("paymentID",s);

            FirebaseFirestore database=FirebaseFirestore.getInstance();
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("boughtBy", fAuth.getUid());
            itemData.put("planBought", SELECT_ITEM);
            itemData.put("purchaseInfo", purchseInfo);
            database.collection("Users").document(fAuth.getUid()).set(itemData, SetOptions.merge())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.e(TAG, "onComplete: QUERY EXECUTED_--------------> " + task.getResult());
                            }
                        });

        } catch (Exception e) {
            Log.e(TAG, "onPaymentSuccess: ErrorMessage " + e.getMessage());
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "Error code " + i + " -- Payment failed " + s);
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError" + e.getMessage());
        }
    }
}