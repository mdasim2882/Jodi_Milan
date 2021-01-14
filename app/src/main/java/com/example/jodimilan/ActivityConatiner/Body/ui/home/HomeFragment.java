package com.example.jodimilan.ActivityConatiner.Body.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.ActivityConatiner.Interfaces.LoadAllProfiles;
import com.example.jodimilan.ActivityConatiner.Interfaces.LoadPaidMembers;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Adapters.PaidMembersRecyclerViewAdapter;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Adapters.PeoplesCardRecyclerViewAdapter;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.ProductGridItemDecoration;
import com.example.jodimilan.HelperClasses.PrefVariables;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements LoadAllProfiles, LoadPaidMembers {
    private RecyclerView recyclerView;
    LoadAllProfiles loadMyConcepts;
    LoadPaidMembers loadPaidMembers;
    public final String TAG = getClass().getSimpleName();
    public static final String ADMIN_EMAIL = "jodimilanmatrimoni@gmail.com";
    public static final String DIAMOND_HEADING = "Diamond Plan (12 Months)";
    public static final String PLATINUM_PLAN_18_MONTHS = "Platinum Plan(18 Months)";
    public static final String TITANIUM_PLAN_28_MONTHS = "Titanium Plan(28 Months)";
    public static final String PERSONAL_PLAN_36_MONTHS = "Personal Plan (36 Months)";
    public static final String SILVER_PLAN_3_MONTHS = "Silver Plan (3 Months)";
    public static final String GOLD_PLAN_6_MONTHS = "Gold Plan (6 Months)";
    CollectionReference databaseUsers;
    private FirebaseAuth fAuth;

    boolean isAdmin = false;
    LinearLayout adminData, userStats;
    RecyclerView userDataRecyclerView;
    TextView totalRegisteredUsers;
    Button showPaidMembersbtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseUsers = FirebaseFirestore.getInstance().collection("Users");
        loadMyConcepts = this;
        loadPaidMembers = this;

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
        adminData = view.findViewById(R.id.layoutInfoUser);
        userStats = view.findViewById(R.id.userStats);
        totalRegisteredUsers = view.findViewById(R.id.registeredUserstxt);
        showPaidMembersbtn = view.findViewById(R.id.showPaid_mem_btn);
    }

    private void setAdminInfoView(View view) {
        // Set the user Recycler View
        userDataRecyclerView = view.findViewById(R.id.userDataRecyclerView);
        userDataRecyclerView.setHasFixedSize(true);
        userDataRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));


        showPaidMembersbtn.setOnClickListener(v -> {
            if (v.getTag().equals("show")) {
                userDataRecyclerView.setVisibility(View.VISIBLE);
                loadPaidUsers();
                v.setTag("hide");
                showPaidMembersbtn.setText("HIDE PAID MEMBERS");
            } else if (v.getTag().equals("hide")) {
                userDataRecyclerView.setVisibility(View.GONE);
                showPaidMembersbtn.setText("SHOW PAID MEMBERS");
                v.setTag("show");

            }

        });


    }

    private void loadPaidUsers() {
        databaseUsers.get().addOnCompleteListener(task -> {
            List<ProductEntry> products = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    if (product.getUID() != null && !product.getUID().equals("") && product.getPlanBought() != null)
                        products.add(product);
                }
                loadPaidMembers.onPaidMembersLoadSuccess(products);
            }
        })
                .addOnFailureListener(e -> loadMyConcepts.onProfilesLoadFailure(e.getMessage()));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setRecyclerView(root);
        fAuth = FirebaseAuth.getInstance();


        if (fAuth.getCurrentUser() != null) {
            if (fAuth.getCurrentUser().getEmail() != null && fAuth.getCurrentUser().getEmail().equals(ADMIN_EMAIL)) {

                adminData.setVisibility(View.VISIBLE);
                isAdmin = true;
                setAdminInfoView(root);
                loadEveryUsers();


            } else {
                //Set user plans here at the top of HomeFragment by setting recyclerView visibility to View.VISIBLE
                setUserPlansInfo(root);


                FirebaseFirestore privateDatabase = FirebaseFirestore
                        .getInstance();
                privateDatabase.collection("Users")
                        .document(fAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String plan = (String) documentSnapshot.get("planBought");


                                Log.d("HomeFragment", "onCreateView() called with: plan = [" + plan + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
                                if (plan != null) {
                                    if (plan.equals(PLATINUM_PLAN_18_MONTHS)) {
                                        loadUsersAsPerSubscription(PrefVariables.PLATINUM_PLAN_USER);
                                    } else if (plan.equals(GOLD_PLAN_6_MONTHS)) {
                                        loadUsersAsPerSubscription(PrefVariables.GOLD_PLAN_USER);
                                    } else if (plan.equals(DIAMOND_HEADING)) {
                                        loadUsersAsPerSubscription(PrefVariables.DIAMOND_PLAN_USER);

                                    } else if (plan.equals(SILVER_PLAN_3_MONTHS)) {
                                        loadUsersAsPerSubscription(PrefVariables.SILVER_PLAN_USER);
                                    } else if (plan.equals(TITANIUM_PLAN_28_MONTHS)) {
                                        loadUsersAsPerSubscription(PrefVariables.TITANIUM_PLAN_USER);
                                    } else if (plan.equals(PERSONAL_PLAN_36_MONTHS)) {
                                        loadUsersAsPerSubscription(PrefVariables.PERSONAL_PLAN_USER);
                                    }
                                } else {
                                    loadUsersAsPerSubscription(0);
                                }
                            }


                        });
            }
        } else {
            setUserPlansInfo(root);
            loadUsersAsPerSubscription(0);
        }

        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        return root;
    }

    private void setUserPlansInfo(View view) {
        userStats.setVisibility(View.GONE);
        showPaidMembersbtn.setVisibility(View.GONE);

        userDataRecyclerView = view.findViewById(R.id.userDataRecyclerView);
        userDataRecyclerView.setVisibility(View.VISIBLE);
        userDataRecyclerView.setHasFixedSize(true);
        userDataRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

    }

    private void loadEveryUsers() {

        databaseUsers.get().addOnCompleteListener(task -> {
            List<ProductEntry> products = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    if (product.getUID() != null && !product.getUID().equals(""))
                        products.add(product);
                }
                loadMyConcepts.onProfilesLoadSuccess(products);
            }
        }).addOnFailureListener(e -> loadMyConcepts.onProfilesLoadFailure(e.getMessage()));
    }

    private void loadUsersAsPerSubscription(int IDENTITY) {

        databaseUsers.get().addOnCompleteListener(task -> {
            List<ProductEntry> products = new ArrayList<>();
            List<ProductEntry> statusSubscription = new ArrayList<>();
            if (task.isSuccessful()) {
                int c = 0;
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {

                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    products.add(product);

                    if (fAuth.getCurrentUser() != null) {
                        if (product.getUID() != null && product.getUID().equals(fAuth.getCurrentUser().getUid())) {
                            statusSubscription.add(product);
                            Log.d(TAG, "loadUsersAsPerSubscription() called with: IDENTITY = [" + product.getProfileID() + "\n" + product.getPlanBought() + "]");
                        }
                    }


                    if (IDENTITY == 0 && c == 50) {
                        break;
                    } else if (IDENTITY == 1 && c == 120) {
                        break;
                    } else if (IDENTITY == 2 && c == 160) {
                        break;
                    } else if (IDENTITY == 3 && c == 200) {
                        break;
                    } else if (IDENTITY == 4 && c == 300) {
                        break;
                    } else if (IDENTITY == PrefVariables.PERSONAL_PLAN_USER && c == 620) {
                        break;
                    } else if (IDENTITY == PrefVariables.TITANIUM_PLAN_USER && c == 450) {
                        break;
                    }
                    c += 1;
                }
                loadMyConcepts.onProfilesLoadSuccess(products);
//                loadPaidMembers.onPaidMembersLoadSuccess(statusSubscription);
                Log.d(TAG, "loadUsersAsPerSubscription() called with: LIST S=\n = [" + statusSubscription + "]");
                if (statusSubscription.size() > 0) {
                    loadPaidMembers.onPaidMembersLoadSuccess(statusSubscription);
                } else {
                    ProductEntry p = new ProductEntry();
                    p.setUID("xxxxxxxxx");
                    p.setProfileID("Not Registered Yet");
                    p.setPlanBought("No Plan Purchased");
                    statusSubscription.add(p);
                    Log.d(TAG, "loadUsersAsPerSubscription() called with: IDENTITY = [" + p + "]");
                    loadPaidMembers.onPaidMembersLoadSuccess(statusSubscription);
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

        try {
            if (adminData.getVisibility() == View.VISIBLE && isAdmin)
                totalRegisteredUsers.setText("" + templates.size());


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Exception:\n" + e.getMessage() + "\nTotal User count: " + templates.size(), Toast.LENGTH_SHORT).show();
            Log.d("HomeFragment", "Exception:\n" + e.getMessage());
        }

    }

    @Override
    public void onProfilesLoadFailure(String message) {

    }

    @Override
    public void onPaidMembersLoadSuccess(List<ProductEntry> templates) {
        Log.d(TAG, "onPaidMembersLoadSuccess() called with: templates = [" + templates + "]");
        PaidMembersRecyclerViewAdapter adapter = new PaidMembersRecyclerViewAdapter(getActivity(), templates);
        userDataRecyclerView.setAdapter(adapter);
        int largePadding = 4;
        int smallPadding = 3;
        userDataRecyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));


        Log.d("Member Stats", "onPaidMembersLoadSuccess() called with: templates = [" + templates + "]");


    }

    @Override
    public void onPaidMembersLoadFailure(String message) {

    }
}