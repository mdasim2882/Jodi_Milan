package com.example.jodimilan.ActivityConatiner.Body.ui.filter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class FilterFragment extends Fragment {
    private final String TAG=getClass().getSimpleName();

    private EditText  pHeight_edt, pCountry_edt, pState_edt, pCity_edt;
    private EditText cEducation_edt, cEmployedIn_edt, cOccupation_edt, cIncome_edt;
    private EditText sMarital_edt,  sReligion_edt;
    RadioGroup rGender, rBody, rColour;
  Button filterButton;
    FirebaseFirestore database;

    private String inputheight;
    private String inputCountry;


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
        database=FirebaseFirestore.getInstance();
        View root = inflater.inflate(R.layout.fragment_filter, container, false);

        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

        initializeViews(root);
        queryAndAddtoList();
        return root;
    }

    private void queryAndAddtoList() {
        //TODO: Add search query for filter here
        database.collection("Users").whereEqualTo(FormDataVariables.bGender, gender).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ProductEntry> filteredUsers = new ArrayList<>();
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    ProductEntry product = bannerSnapshot.toObject(ProductEntry.class);
                    filteredUsers.add(product);
                }

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

    private void initializeViews(View root) {
        filterButton=root.findViewById(R.id.filter_search_btn);
        filterButton.setOnClickListener(v->{retrieveInputFields(root);
            Log.e(TAG, "Retrieve Info: \n"+formatter() );

        });
        personalFilter(root);
        careerDetails(root);
        socialDetails(root);
    }

    private void personalFilter(View root) {
        pHeight_edt = root.findViewById(R.id.filterheight_edt);
        pCountry_edt = root.findViewById(R.id.filtercountry_edt);
        pState_edt = root.findViewById(R.id.filterstate_edt);
        pCity_edt = root.findViewById(R.id.filtercity_edt);
    }
    private void careerDetails(View root) {
        cEducation_edt = root.findViewById(R.id.education_edt);
        cEmployedIn_edt = root.findViewById(R.id.employed_edt);
        cOccupation_edt = root.findViewById(R.id.occupation_edt);
        cIncome_edt = root.findViewById(R.id.income_edt);
    }
    private void socialDetails(View root) {
        sMarital_edt = root.findViewById(R.id.filtermarital_status_edt);
        sReligion_edt = root.findViewById(R.id.filterreligion_edt);

        rGender = root.findViewById(R.id.filtergroup_rdbtn);
        rBody = root.findViewById(R.id.filtergroupbodytype_rdbtn);
        rColour = root.findViewById(R.id.filtergroup_complexion_rdbtn);
    }
}