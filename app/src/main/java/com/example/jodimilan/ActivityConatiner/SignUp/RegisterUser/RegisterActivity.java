package com.example.jodimilan.ActivityConatiner.SignUp.RegisterUser;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;

import com.example.jodimilan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText pDob_editText, pHeight_edt, pCountry_edt, pState_edt, pCity_edt;
    private EditText cEducation_edt, cEmployedIn_edt, cOccupation_edt, cIncome_edt;
    private EditText sMarital_edt, sChildren_edt, sMotherTongue_edt, sReligion_edt;
    private EditText lregFullName_edt, lregEmailId_edt, lregPassword_edt, lMobileno_edt;
    private Calendar myCalendar;
    private ListPopupWindow heightStatus, countryStatus, stateStatus,
            highestEducation, employedIn, occupation, income,
            maritalStatus, haveChildren, motherTongue, religion;
    private String carrerDetails[] = new String[4];
    private String socialDetails[] = new String[4];
    private String loginDetails[] = new String[4];
    private LinearLayout ll;
    RadioGroup radioGroup;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener dialogClickListener;
    private String data;
    private Toolbar toolbar;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth=FirebaseAuth.getInstance();
        findCalendarAndsetText();
        initializeViews();
        setUpToolbar();
        setUpPopupLists();


        setListeners();
        setAlertDialog();

    }

    private void setUpPopupLists() {
        // Personal Details
        setHeightPopupList();
        setCounty_edt();
        setState_edt();

        //Career Details
        setHighestEducation_edt();
        setEmployedIn_edt();
        setOccupation_edt();
        setIncome_edt();

        //Social Details
        setMaritalStatus_edt();
        setHaveChildren_edt();
        setMotherTongue_edt();
        setReligion_edt();

    }

    private void setAlertDialog() {
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                      //  Toast.makeText(RegisterActivity.this, "USER DATA \n" + data, Toast.LENGTH_SHORT).show();
                        String email=lregEmailId_edt.getText().toString();
                        String password=lregPassword_edt.getText().toString();
                        createAccount(email,password);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        showToaster("No I have opt for the correct details in the fields");
                        break;
                }
            }
        };

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Basic Details");
        builder.setMessage("You won't be allowed to edit the following fields after registration. Please edit NOW in case you want to make any changes. ").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);
    }

    private void createAccount(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = fAuth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Registration Done.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                    // ...
                });
    }

    private void showToaster(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void initializeViews() {
        personalDetails();
        careerDetails();
        socialDetails();
        loginDetails();
    }

    private void loginDetails() {
        lregFullName_edt = findViewById(R.id.regfullname_edt);
        lregEmailId_edt = findViewById(R.id.reg_emailId_edt);
        lregPassword_edt = findViewById(R.id.reg_pass_edt);
        lMobileno_edt = findViewById(R.id.regMobile_edt);
    }

    private void personalDetails() {
        pHeight_edt = findViewById(R.id.height_edt);
        radioGroup = findViewById(R.id.group_rdbtn);
        pCountry_edt = findViewById(R.id.country_edt);
        pState_edt = findViewById(R.id.state_edt);
        pCity_edt = findViewById(R.id.city_edt);
        ll = findViewById(R.id.reg_ll);
    }

    private void careerDetails() {
        cEducation_edt = findViewById(R.id.education_edt);
        cEmployedIn_edt = findViewById(R.id.employed_edt);
        cOccupation_edt = findViewById(R.id.occupation_edt);
        cIncome_edt = findViewById(R.id.income_edt);

    }

    private void socialDetails() {
        sMarital_edt = findViewById(R.id.marital_status_edt);
        sChildren_edt = findViewById(R.id.have_children_edt);
        sMotherTongue_edt = findViewById(R.id.motherTongue_edt);
        sReligion_edt = findViewById(R.id.religion_edt);
    }

    private void setListeners() {
        pHeight_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightStatus.show();
            }
        });
        pCountry_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryStatus.show();
            }
        });
        pState_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateStatus.show();
            }
        });

        cEducation_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highestEducation.show();
            }
        });

        cEmployedIn_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employedIn.show();
            }
        });

        cOccupation_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                occupation.show();
            }
        });

        cIncome_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income.show();
            }
        });

        sMarital_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maritalStatus.show();
            }
        });

        sChildren_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveChildren.show();
            }
        });

        sMotherTongue_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherTongue.show();
            }
        });

        sReligion_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               religion.show();
            }
        });

        Button nextbtn = findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton genderradioButton = (RadioButton) findViewById(selectedId);
            String gender = "Nothing Selected";
            if (selectedId != -1) {
                gender = genderradioButton.getText().toString();
            }
            data = "Gender: " + gender + "\n" +
                    "DOB: " + pDob_editText.getText().toString() + "\n" +
                    "Height: " + pHeight_edt.getText().toString() + "\n" +
                    "Country: " + pCountry_edt.getText().toString() + "\n" +
                    "State: " + pState_edt.getText().toString() + "\n" +
                    "City: " + pCity_edt.getText().toString().toUpperCase()+"\n"+
                    "Education: " + cEducation_edt.getText().toString().toUpperCase()+"\n"+
                    "Employed In: " + cEmployedIn_edt.getText().toString()+"\n"+
                    "Occupation: " + cOccupation_edt.getText().toString()+"\n"+
                    "Income: " + cIncome_edt.getText().toString()+"\n"+
                    "Marital Status: " + sMarital_edt.getText().toString()+"\n"+
                    "Have Children: " + sChildren_edt.getText().toString()+"\n"+
                    "Mother Tongue: " + sMotherTongue_edt.getText().toString().toUpperCase()+"\n"+
                    "Religion: " + sReligion_edt.getText().toString();


            Log.d("TAG", "setListeners: Clicked with data: -->\n" + data);
//            Snackbar.make(ll,"DATA:\n"+data, Snackbar.LENGTH_LONG).show();
            builder.show();

        });
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.personal_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //--------------------------------Setting POPUP LIST---------------------------------//
    private void setHeightPopupList() {
        LinearLayout ll = findViewById(R.id.reg_ll);

        final List<String> status = new ArrayList<>();
        status.add("Status 1");
        status.add("Status 2");
        status.add("Status 3");
        status.add("Status 4");
        status.add("Status 5");
        status.add("Status 6");
        status.add("Status 7");
        status.add("Status 8");
        status.add("Status 9");
        status.add("Status 10");

        heightStatus = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
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
        });
    }

    private void setCounty_edt() {
        LinearLayout ll = findViewById(R.id.reg_ll);

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

        countryStatus = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        countryStatus.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        countryStatus.setAdapter(adapter);
        countryStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pCountry_edt.setText(status.get(position));//we set the selected element in the EditText
                countryStatus.dismiss();


            }
        });
    }

    private void setState_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Uttar Pradesh");
        status.add("Karachi");
        status.add("Car 3");
        status.add("Car 4");
        status.add("Car 5");
        status.add("Car 6");
        status.add("Car 7");
        status.add("Car 8");
        status.add("Car 9");
        status.add("Car 10");

        stateStatus = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        stateStatus.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        stateStatus.setAdapter(adapter);
        stateStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pState_edt.setText(status.get(position));//we set the selected element in the EditText

                stateStatus.dismiss();


            }
        });
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

        highestEducation = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        highestEducation.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        highestEducation.setAdapter(adapter);
        highestEducation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cEducation_edt.setText(status.get(position));//we set the selected element in the EditText

                highestEducation.dismiss();

            }
        });
    }

    private void setEmployedIn_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Car 1");
        status.add("Car 2");
        status.add("Car 3");
        status.add("Car 4");
        status.add("Car 5");
        status.add("Car 6");
        status.add("Car 7");
        status.add("Car 8");
        status.add("Car 9");
        status.add("Car 10");

        employedIn = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        employedIn.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        employedIn.setAdapter(adapter);
        employedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cEmployedIn_edt.setText(status.get(position));//we set the selected element in the EditText

                employedIn.dismiss();


            }
        });
    }

    private void setOccupation_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Car 1");
        status.add("Car 2");
        status.add("Car 3");
        status.add("Car 4");
        status.add("Car 5");
        status.add("Car 6");
        status.add("Car 7");
        status.add("Car 8");
        status.add("Car 9");
        status.add("Car 10");

        occupation = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        occupation.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        occupation.setAdapter(adapter);
        occupation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cOccupation_edt.setText(status.get(position));//we set the selected element in the EditText

                occupation.dismiss();


            }
        });
    }

    private void setIncome_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Car 1");
        status.add("Car 2");
        status.add("Car 3");
        status.add("Car 4");
        status.add("Car 5");
        status.add("Car 6");
        status.add("Car 7");
        status.add("Car 8");
        status.add("Car 9");
        status.add("Car 10");

        income = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        income.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        income.setAdapter(adapter);
        income.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cIncome_edt.setText(status.get(position));//we set the selected element in the EditText

                income.dismiss();


            }
        });
    }

    private void setMaritalStatus_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Never Married");
        status.add("Awaiting Divorce");
        status.add("Married");


        maritalStatus = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        maritalStatus.setAnchorView(sMarital_edt); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        maritalStatus.setAdapter(adapter);
        maritalStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sMarital_edt.setText(status.get(position));//we set the selected element in the EditText

                maritalStatus.dismiss();


            }
        });
    }

    private void setHaveChildren_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Yes");
        status.add("No");

        haveChildren = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        haveChildren.setAnchorView(sChildren_edt); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        haveChildren.setAdapter(adapter);
        haveChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sChildren_edt.setText(status.get(position));//we set the selected element in the EditText

                haveChildren.dismiss();


            }
        });
    }

    private void setMotherTongue_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Hindi");
        status.add("Urdu");
        status.add("Hindi/Urdu");
        status.add("Bengali");
        status.add("Marathi");
        status.add("Punjabi");
        status.add("Telgu");
        status.add("Gujrati");
        status.add("Parsi");
        status.add("Madrasi");

        motherTongue = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        motherTongue.setAnchorView(sMotherTongue_edt); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        motherTongue.setAdapter(adapter);
        motherTongue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sMotherTongue_edt.setText(status.get(position));//we set the selected element in the EditText

                motherTongue.dismiss();


            }
        });
    }

    private void setReligion_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Hindu");
        status.add("Muslim");
        status.add("Sikh");
        status.add("Jain");
        status.add("Christian");

        religion = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        religion.setAnchorView(sReligion_edt); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        religion.setAdapter(adapter);
        religion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sReligion_edt.setText(status.get(position));//we set the selected element in the EditText

                religion.dismiss();


            }
        });
    }


    //------------------------------------------DATE-----------------------------------//
    private void findCalendarAndsetText() {
        myCalendar = Calendar.getInstance();

        pDob_editText = (EditText) findViewById(R.id.dateOfBirth_edt);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        pDob_editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        pDob_editText.setText(sdf.format(myCalendar.getTime()));
    }
}
