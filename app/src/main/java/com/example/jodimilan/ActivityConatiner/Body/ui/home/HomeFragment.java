package com.example.jodimilan.ActivityConatiner.Body.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.ActivityConatiner.Interfaces.LoadAllProfiles;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Adapters.PeoplesCardRecyclerViewAdapter;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.ProductGridItemDecoration;
import com.example.jodimilan.HelperClasses.PrefVariables;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements LoadAllProfiles {
    private RecyclerView recyclerView;
    LoadAllProfiles loadMyConcepts;
    public static final String ADMIN_EMAIL = "jodimilanmatrimoni@gmail.com";
    public static final String DIAMOND_HEADING = "Diamond Plan (12 Months)";
    public static final String PLATINUM_PLAN_18_MONTHS = "Platinum Plan(18 Months)";
    public static final String TITANIUM_PLAN_28_MONTHS = "Titanium Plan(28 Months)";
    public static final String PERSONAL_PLAN_36_MONTHS = "Personal Plan (36 Months)";
    public static final String SILVER_PLAN_3_MONTHS = "Silver Plan (3 Months)";
    public static final String GOLD_PLAN_6_MONTHS = "Gold Plan (6 Months)";
    CollectionReference databaseUsers;
    private FirebaseAuth fAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseUsers = FirebaseFirestore.getInstance().collection("Users");
        loadMyConcepts = this;

    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.greetconceptsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        /*
         * Pass parameter as list of type ProductEntry
         * Must be retrieved from database to here only
         * ProductEntry contains three fields:
         * ImageView productImage
         * TextView productName, productCost;
         * */

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
       setRecyclerView(root);
        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser()!=null){
            if(fAuth.getCurrentUser().getEmail()!=null && fAuth.getCurrentUser().getEmail().equals(ADMIN_EMAIL)){
                loadEveryUsers();
            }
            else{
                FirebaseFirestore privateDatabase = FirebaseFirestore.getInstance();
                 privateDatabase.collection("Users").document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                     if (task.isSuccessful()) {
                             DocumentSnapshot documentSnapshot=task.getResult();
                             String plan= (String) documentSnapshot.get("planBought");


                         Log.d("HomeFragment", "onCreateView() called with: plan = [" + plan + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
                         if(plan!=null){
                             if (plan.equals(PLATINUM_PLAN_18_MONTHS)) {
                                 loadUsersAsPerSubscription(PrefVariables.PLATINUM_PLAN_USER);
                             } else if (plan.equals(GOLD_PLAN_6_MONTHS)) {
                                 loadUsersAsPerSubscription(PrefVariables.GOLD_PLAN_USER);
                             } else if (plan.equals(DIAMOND_HEADING)) {
                                 loadUsersAsPerSubscription(PrefVariables.DIAMOND_PLAN_USER);

                             } else if (plan.equals(SILVER_PLAN_3_MONTHS)) {
                                 loadUsersAsPerSubscription(PrefVariables.SILVER_PLAN_USER);
                             }else if (plan.equals(TITANIUM_PLAN_28_MONTHS)) {
                                 loadUsersAsPerSubscription(PrefVariables.TITANIUM_PLAN_USER);
                             }else if (plan.equals(PERSONAL_PLAN_36_MONTHS)) {
                                 loadUsersAsPerSubscription(PrefVariables.PERSONAL_PLAN_USER);
                             }
                         }
                             else{
                                 loadUsersAsPerSubscription(0);
                             }
                         }


                 });
            }
        }
        else{
            loadUsersAsPerSubscription(0);
        }

        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        return root;
    }
    private void loadEveryUsers() {
        databaseUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ProductEntry> products = new ArrayList<>();
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                        ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                        products.add(product);
                    }
                    loadMyConcepts.onProfilesLoadSuccess(products);
                }
            }
        }).addOnFailureListener(e -> loadMyConcepts.onProfilesLoadFailure(e.getMessage()));
    }
    private void loadUsersAsPerSubscription(int IDENTITY) {

        databaseUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ProductEntry> products = new ArrayList<>();
                if (task.isSuccessful()) {
                    int c=0;
                    for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {

                        ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                        products.add(product);

                        if (IDENTITY==0 && c == 50) {
                            break;
                        }else if (IDENTITY==1 && c == 120) {
                            break;
                        }else if (IDENTITY==2 && c == 160) {
                            break;
                        }else if (IDENTITY==3 && c == 200) {
                            break;
                        }else if (IDENTITY==4 && c == 300) {
                            break;
                        }else if (IDENTITY==PrefVariables.PERSONAL_PLAN_USER && c == 620) {
                            break;
                        }else if (IDENTITY==PrefVariables.TITANIUM_PLAN_USER && c == 450) {
                            break;
                        }
                        c+=1;
                    }
                    loadMyConcepts.onProfilesLoadSuccess(products);
                }
            }
        }).addOnFailureListener(e -> loadMyConcepts.onProfilesLoadFailure(e.getMessage()));
    }

    @Override
    public void onProfilesLoadSuccess(List<ProductEntry> templates) {
        PeoplesCardRecyclerViewAdapter adapter = new PeoplesCardRecyclerViewAdapter(getActivity(), templates);
        recyclerView.setAdapter(adapter);
        int largePadding = 4;
        int smallPadding = 3;
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
    }

    @Override
    public void onProfilesLoadFailure(String message) {

    }

}