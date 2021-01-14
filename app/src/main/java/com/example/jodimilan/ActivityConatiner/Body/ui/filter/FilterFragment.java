package com.example.jodimilan.ActivityConatiner.Body.ui.filter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.ActivityConatiner.Interfaces.LoadAllProfiles;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.Adapters.PeoplesCardRecyclerViewAdapter;
import com.example.jodimilan.ActivityConatiner.RecyclerViewSetup.ProductGridItemDecoration;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FilterFragment extends Fragment implements LoadAllProfiles {
    private final String TAG = getClass().getSimpleName();

    private EditText pHeight_edt, pCountry_edt, pState_edt, pCity_edt;
    private EditText cEducation_edt, cEmployedIn_edt, cOccupation_edt, cIncome_edt;
    private EditText sMarital_edt, sReligion_edt;
    RadioGroup rGender, rBody, rColour;
    Button filterButton;
    FirebaseFirestore database;
    ScrollView scrollView;
    private RecyclerView recyclerView;
    private TextView nouser;
    LinkedList<ProductEntry> listOfFilteredUsers;
    ProgressDialog progressDialog;

    private String inputheight;
    private String inputCountry;
    LoadAllProfiles loadAllProfiles;

    public String formatter() {
        return "FilterFragment{" +
                "inputheight='" + inputheight + '\'' +
                ", inputCountry='" + inputCountry + '\'' +
                ", inputState='" + inputState + '\'' +
                ", inputCity='" + inputCity + '\'' +
                ", inputEducation='" + inputEducation + '\'' +
                ", inputEmployment='" + inputEmployment + '\'' +
                ", inputOccupation='" + inputOccupation + '\'' +
                ", inputIncome='" + inputIncome + '\'' +
                ", inputMaritalStatus='" + inputMaritalStatus + '\'' +
                ", inputReligion='" + inputReligion + '\'' +
                ", gender='" + gender + '\'' +
                ", body='" + body + '\'' +
                ", colour='" + colour + '\'' +
                '}';
    }

    private String inputState;
    private String inputCity;
    private String inputEducation;
    private String inputEmployment;
    private String inputOccupation;
    private String inputIncome;
    private String inputMaritalStatus;
    private String inputReligion;
    private android.app.AlertDialog.Builder heightStatus, countryStatus, stateStatus,
            highestEducation, employedIn, occupation, income,
            maritalStatus, religion;
    private String gender;
    private String body;
    private String colour;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        database = FirebaseFirestore.getInstance();
        View root = inflater.inflate(R.layout.fragment_filter, container, false);

        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        loadAllProfiles = this;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        initializeViews(root);
        setRecyclerView(root);
        return root;
    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.filter_search_recView);
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

    private void queryAndAddtoList(List<String> filterList) {
        //TODO: Add search query for filter here
        listOfFilteredUsers = new LinkedList<>();

        Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "]" + "\nUSER ID: " + FirebaseAuth.getInstance().getUid());

        if (gender.equals("Male")) {
            inputCountry=inputCountry.toUpperCase();
            database.collection("Users")
                    .whereEqualTo(FormDataVariables.bGender, gender)
                    .whereEqualTo(FormDataVariables.bCountry, inputCountry)
                    .whereEqualTo(FormDataVariables.bHeight, inputheight)
                    .whereEqualTo(FormDataVariables.bEducation, inputEducation)
                    .whereEqualTo(FormDataVariables.bReligion, inputReligion)
                    .whereEqualTo(FormDataVariables.bColor, colour)
                    .whereEqualTo(FormDataVariables.bBody, body)
                    .whereIn(FormDataVariables.bIncome, filterList)
                    .get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    List<ProductEntry> filteredUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                        ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                        filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                    }
                    loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

                }
            });
        } else {
            database.collection("Users")
                    .whereEqualTo(FormDataVariables.bGender, "Female")
                    .whereEqualTo(FormDataVariables.bCountry, inputCountry)
                    .whereEqualTo(FormDataVariables.bEducation, inputEducation)
                    .whereEqualTo(FormDataVariables.bHeight, inputheight)
                    .whereEqualTo(FormDataVariables.bReligion, inputReligion)
                    .whereEqualTo(FormDataVariables.bColor, colour)
                    .whereEqualTo(FormDataVariables.bBody, body)
                    .whereIn(FormDataVariables.bIncome, filterList)
                    .get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    List<ProductEntry> filteredUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                        ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                        filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                    }
                    loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

                }
            });
        }


//
//        performValidationFilter(filterList);


    }

    private void performValidationFilter(List<String> filterList) {
        database.collection("Users")
                .whereIn(FormDataVariables.bCountry, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });


        database.collection("Users")
                .whereIn(FormDataVariables.bHeight, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bState, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bCity, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bColor, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bBody, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bEducation, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bEmployedIn, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bOccupation, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bIncome, filterList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });

        database.collection("Users")
                .whereIn(FormDataVariables.bMaritalStatus, filterList)
                .get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });


        database.collection("Users")
                .whereArrayContainsAny(FormDataVariables.bReligion, filterList)
                .get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
//                    Log.d(TAG, "queryAndAddtoList() called with: filterList = [" + filterList + "] \n DATA: \n" + product.toString());
                }
                loadAllProfiles.onProfilesLoadSuccess(filteredUsers);

            }
        });
    }

    private void retrieveInputFields(View root) {
        int selectedId = rGender.getCheckedRadioButtonId();
        int colorSelectID = rColour.getCheckedRadioButtonId();
        int bodySelectID = rBody.getCheckedRadioButtonId();
        RadioButton genderradioButton = root.findViewById(selectedId);
        RadioButton bodyradioButton = root.findViewById(bodySelectID);
        RadioButton colourradioButton = root.findViewById(colorSelectID);
        gender = genderradioButton.getText().toString();
        body = bodyradioButton.getText().toString();
        colour = colourradioButton.getText().toString();

        inputheight = pHeight_edt.getText().toString();
        inputCountry = pCountry_edt.getText().toString();
        inputState = pState_edt.getText().toString();
        inputCity = pCity_edt.getText().toString().toUpperCase();
        inputEducation = cEducation_edt.getText().toString().toUpperCase();
        inputEmployment = cEmployedIn_edt.getText().toString();
        inputOccupation = cOccupation_edt.getText().toString();
        inputIncome = cIncome_edt.getText().toString();
        inputMaritalStatus = sMarital_edt.getText().toString();
        inputReligion = sReligion_edt.getText().toString();

    }

    private void setUpPopupLists() {
        setListeners();
        // Personal Details
//        setHeightPopupList();
        setCounty_edt();
//        setState_edt();

        //Career Details
        setHighestEducation_edt();
        setEmployedIn_edt();
        setOccupation_edt();
        setIncome_edt();

        //Social Details
        setMaritalStatus_edt();

        setReligion_edt();

    }


    private void setHeightPopupList() {

        final List<String> status = new ArrayList<>();
        status.add("3.0ft-3.5ft");
        status.add("3.5ft-4.0ft");
        status.add("4.0ft-4.5ft");
        status.add("4.5ft-5.0ft");
        status.add("5.0ft-5.5ft");
        status.add("5.5ft-6.0ft");
        status.add("6.0ft-6.5ft");

       /* ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        heightStatus.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        heightStatus.setAdapter(adapter);
        heightStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taggy = (String) view.getTag();

                pHeight_edt.setText(status.get(position));//we set the selected element in the EditText

                heightStatus.dismiss();


            }
        });*/
        heightStatus = new android.app.AlertDialog.Builder(getActivity());
        heightStatus.setIcon(R.drawable.jodi_milan_logo);
        heightStatus.setTitle("Select Height:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        heightStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        heightStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            pHeight_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }

    private void setCounty_edt() {

        final List<String> status = new ArrayList<>();
        status.add("India");
        status.add("Australia");
        status.add("Canada");
        status.add("England");
        status.add("Russia");
        status.add("France");
        status.add("New Zealand");
        status.add("Itlay");
        status.add("Sout Africa");
        status.add("Pakistan");

      /*  ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        countryStatus.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        countryStatus.setAdapter(adapter);
        countryStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pCountry_edt.setText(status.get(position));//we set the selected element in the EditText
                countryStatus.dismiss();


            }
        });*/
        countryStatus = new android.app.AlertDialog.Builder(getActivity());
        countryStatus.setIcon(R.drawable.jodi_milan_logo);
        countryStatus.setTitle("Select Country-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        countryStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        countryStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            pCountry_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }

    private void setListeners() {
//        pHeight_edt.setOnClickListener(v -> heightStatus.show());
        pCountry_edt.setOnClickListener(v -> countryStatus.show());
//        pState_edt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stateStatus.show();
//            }
//        });

        cEducation_edt.setOnClickListener(v -> {
//                highestEducation.show();
            highestEducation.show();
        });

//        cEmployedIn_edt.setOnClickListener(v -> employedIn.show());

//        cOccupation_edt.setOnClickListener(v -> occupation.show());

        cIncome_edt.setOnClickListener(v -> income.show());

        sMarital_edt.setOnClickListener(v -> maritalStatus.show());


        sReligion_edt.setOnClickListener(v -> religion.show());


    }


    private void setHighestEducation_edt() {
        final List<String> status = new ArrayList<>();
        status.add("B.Tech/B.E.");
        status.add("B.A.");
        status.add("Ph.D");
        status.add("B.Ed");
        status.add("M.Ed");
        status.add("B.Sc");
        status.add("B.Com");
        status.add("C.A.");
        status.add("Bachelor in Archaeology");
        status.add("B.C.A");
        status.add("M.C.A");

//        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        highestEducation = new android.app.AlertDialog.Builder(getActivity());
        highestEducation.setIcon(R.drawable.jodi_milan_logo);
        highestEducation.setTitle("Highest Education-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        highestEducation.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        highestEducation.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            cEducation_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
      /*  highestEducation.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        highestEducation.setAdapter(adapter);
        highestEducation.setOnItemClickListener((parent, view, position, id) -> {
            cEducation_edt.setText(status.get(position));//we set the selected element in the EditText

            highestEducation.dismiss();

        });*/
    }

    private void setEmployedIn_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Amazon");
        status.add("Honda");
        status.add("Tata Motors");
        status.add("Microsoft");
        status.add("Wipro");
        status.add("TCS");
        status.add("Adobe");
        status.add("Car 24");
        status.add("OLA");
        status.add("Uber");
        status.add("Others");

        employedIn = new android.app.AlertDialog.Builder(getActivity());
        employedIn.setIcon(R.drawable.jodi_milan_logo);
        employedIn.setTitle("Employed In-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        employedIn.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        employedIn.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            cEmployedIn_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });

     /*   ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        employedIn.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        employedIn.setAdapter(adapter);
        employedIn.setOnItemClickListener((parent, view, position, id) -> {
            cEmployedIn_edt.setText(status.get(position));//we set the selected element in the EditText

            employedIn.dismiss();


        });*/
    }

    private void setOccupation_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Software Engineer");
        status.add("Product Engineer");
        status.add("Software Tester");
        status.add("Mechanical Engineer");
        status.add("Manufacturer");
        status.add("Product Supplier");
        status.add("Web Developer");
        status.add("Businessman");
        status.add("Builder");
        status.add("Enterpreneur");
        status.add("Others");

        occupation = new android.app.AlertDialog.Builder(getActivity());
        occupation.setIcon(R.drawable.jodi_milan_logo);
        occupation.setTitle("Select State-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        occupation.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        occupation.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            cOccupation_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }

    private void setIncome_edt() {
        final List<String> status = new ArrayList<>();
        status.add("10k-15k");
        status.add("15k-20k");
        status.add("20k-25k");
        status.add("25k-30k");
        status.add("30k-35k");
        status.add("35k-40k");
        status.add("40k-50k");
        status.add("50k-60k");
        status.add("60k-70k");
        status.add("70k-80k");
        status.add("80k-90k");
        status.add("90k-100k");
        status.add("More than 100k");

        income = new android.app.AlertDialog.Builder(getActivity());
        income.setIcon(R.drawable.jodi_milan_logo);
        income.setTitle("Select Income-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        income.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        income.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            cIncome_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }

    private void setMaritalStatus_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Never Married");
        status.add("Awaiting Divorce");
        status.add("Married");


        maritalStatus = new android.app.AlertDialog.Builder(getActivity());
        maritalStatus.setIcon(R.drawable.jodi_milan_logo);
        maritalStatus.setTitle("Select Marital Status-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        maritalStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        maritalStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            sMarital_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }


    private void setReligion_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Hindu");
        status.add("Muslim");
        status.add("Sikh");
        status.add("Jain");
        status.add("Christian");

        religion = new android.app.AlertDialog.Builder(getActivity());
        religion.setIcon(R.drawable.jodi_milan_logo);
        religion.setTitle("Select Religion-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice, status);

        religion.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        religion.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            sReligion_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeViews(View root) {
        nouser=root.findViewById(R.id.nouserfoundtxt);
        personalFilter(root);
        careerDetails(root);
        socialDetails(root);
        setUpPopupLists();
        scrollView = root.findViewById(R.id.filterScroller);
        filterButton = root.findViewById(R.id.filter_search_btn);
        filterButton.setOnClickListener(v -> {
            retrieveInputFields(root);
            progressDialog.setMessage("Searching...");
            progressDialog.show();
            Log.e(TAG, "Retrieve Info: \n" + formatter());
            List<String> filterList = new ArrayList<>();


            if (gender != null && !gender.equals("")) {
                filterList.add(gender);
            }
            if (body != null && !body.equals("")) {
                filterList.add(body);
            }
            if (colour != null && !colour.equals("")) {
                filterList.add(colour);
            }
            if (inputheight != null && !inputheight.equals("")) {
                filterList.add(inputheight);
            }
            if (inputCountry != null && !inputCountry.equals("")) {
                filterList.add(inputCountry);
            }
            if (inputState != null && !inputState.equals("")) {
//                filterList.add(inputState);
            }
            if (inputCity != null && !inputCity.equals("")) {
                filterList.add(inputCity);
            }
            if (inputEducation != null && !inputEducation.equals("")) {
                filterList.add(inputEducation);
            }
            if (inputEmployment != null && !inputEmployment.equals("")) {
//                filterList.add(inputEmployment);
            }
            if (inputOccupation != null && !inputOccupation.equals("")) {
                filterList.add(inputOccupation);
            }
            if (inputIncome != null && !inputIncome.equals("")) {
                filterList.add(inputIncome);
            }
            if (inputMaritalStatus != null && !inputMaritalStatus.equals("")) {
//                filterList.add(inputMaritalStatus);
            }
            if (inputReligion != null && !inputReligion.equals("")) {
                filterList.add(inputReligion);
            }
            try {
                queryAndAddtoList(filterList);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void personalFilter(View root) {
        pHeight_edt = root.findViewById(R.id.filterheight_edt);
        pCountry_edt = root.findViewById(R.id.filtercountry_edt);
        pState_edt = root.findViewById(R.id.filterstate_edt);
        pCity_edt = root.findViewById(R.id.filtercity_edt);
    }

    private void careerDetails(View root) {
        cEducation_edt = root.findViewById(R.id.filtereducation_edt);
        cEmployedIn_edt = root.findViewById(R.id.filteremployed_edt);
        cOccupation_edt = root.findViewById(R.id.filteroccupation_edt);
        cIncome_edt = root.findViewById(R.id.filterincome_edt);
    }

    private void socialDetails(View root) {
        sMarital_edt = root.findViewById(R.id.filtermarital_status_edt);
        sReligion_edt = root.findViewById(R.id.filterreligion_edt);

        rGender = root.findViewById(R.id.filtergroup_rdbtn);
        rBody = root.findViewById(R.id.filtergroupbodytype_rdbtn);
        rColour = root.findViewById(R.id.filtergroup_complexion_rdbtn);
    }

    @Override
    public void onProfilesLoadSuccess(List<ProductEntry> templates) {
//            listOfFilteredUsers.addAll(templates);
        Log.d(TAG, "onProfilesLoadSuccess() called with: templates = [" + templates + "]");
        scrollView.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(templates.size()==0){
            nouser.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();
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