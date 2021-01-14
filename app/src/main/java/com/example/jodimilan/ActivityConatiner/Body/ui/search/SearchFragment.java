package com.example.jodimilan.ActivityConatiner.Body.ui.search;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.ActivityConatiner.Body.ProfileDetailsActivity;
import com.example.jodimilan.ActivityConatiner.Interfaces.LoadSearchProfile;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.HelperClasses.ProductEntry;
import com.example.jodimilan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;


public class SearchFragment extends Fragment implements LoadSearchProfile {
    public static final String SEARCH_RESULT = "SEARCH RESULT:";
    private final String TAG = getClass().getSimpleName();
    EditText searchID;
    TextView example;
    Button searchNowbtn;
    LoadSearchProfile loadSearchProfile;
    FirebaseFirestore database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loadSearchProfile = this;
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        database=FirebaseFirestore.getInstance();
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        searchID=root.findViewById(R.id.searchprofileID_edt);
        example=root.findViewById(R.id.exampleText);
        searchNowbtn=root.findViewById(R.id.search_now_btn);


        example.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(example.getText());
            return true;
        });



        searchNowbtn.setOnClickListener(v->{
            String profileID=searchID.getText().toString();

        findProfileByID(profileID);});
        return root;
    }

    private void findProfileByID(String profileID) {
        Log.d(TAG, "findProfileByID: Executed => "+profileID);

        try {
            database.collection("Users").whereEqualTo("profileID", profileID).get().addOnCompleteListener(task -> {
                ProductEntry productEntry=new ProductEntry();
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        productEntry = snapshot.toObject(ProductEntry.class);
                        Log.d(TAG, "findProfileByID: " + snapshot.get("FullName") + "\n" +
                                "DOCS ID: " + snapshot.getId());
                    }
                    loadSearchProfile.onSearchSuccess(productEntry);
                }

            }).addOnFailureListener(e -> {
                Log.d(TAG, "onFailure: Message"+e.getMessage() );
                loadSearchProfile.onSearchFailure(e.getMessage());
            });
        } catch (Exception e) {
            Log.d(TAG, "findProfileByID: Unexcepted error"+e.getMessage());
        }
    }

    @Override
    public void onSearchSuccess(ProductEntry person) {
        Intent userData=new Intent(getActivity(), ProfileDetailsActivity.class);
        Bundle intent=new Bundle();
        intent.putString(FormDataVariables.bGender,person.getGender());
        intent.putString(FormDataVariables.bHeight,person.getHeight());
        intent.putString(FormDataVariables.bFathersName, person.getFathersName());
        intent.putString(FormDataVariables.bFullName, person.getFullName());
        intent.putString(FormDataVariables.bDoB, person.getDOB());
        intent.putString(FormDataVariables.bState, person.getState());
        intent.putString(FormDataVariables.bCountry, person.getCountry());
        intent.putString(FormDataVariables.bCity, person.getCity());
        intent.putString(FormDataVariables.bAddress, person.getAddress());
        intent.putString(FormDataVariables.bColor, person.getColour());
        intent.putString(FormDataVariables.bBody, person.getBody());
        intent.putString(FormDataVariables.bEducation, person.getEducation());
        intent.putString(FormDataVariables.bEmployedIn, person.getEmployedIn());
        intent.putString(FormDataVariables.bOccupation, person.getOccupation());
        intent.putString(FormDataVariables.bIncome, person.getIncome());
        intent.putString(FormDataVariables.bMaritalStatus, person.getMaritalStatus());
        intent.putString(FormDataVariables.bHaveChildren, person.getHaveChildren());
        intent.putString(FormDataVariables.bMotherTongue, person.getMotherTongue());
        intent.putString(FormDataVariables.bReligion, person.getReligion());
        intent.putString(FormDataVariables.bMobile, person.getMobile());
        intent.putString(FormDataVariables.bProfilePicture, person.getPhotoLink());
        intent.putString(FormDataVariables.bProfileID, person.getProfileID());
        Log.d(TAG, "onSearchSuccess: SEARCH LINK PHOTO=====> " +
                "\n Mobile: "+person.getMobile()+"\nLink: "+person.getPhotoLink());
        userData.putExtra("bidiUser", intent);
        if (person.getProfileID()!=null)
            startActivity(userData);
        else{
            Toast.makeText(getActivity(), "User doesn't exist", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onSearchFailure(String message) {

    }
}