package com.example.jodimilan.ActivityConatiner.SignUp.RegisterUser;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jodimilan.ActivityConatiner.Body.PictureSetter;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    public static final String MANDATORY = "All fields are mandatory";
    private EditText pDob_editText, pHeight_edt, pCountry_edt, pState_edt, pCity_edt;
    private EditText cEducation_edt, cEmployedIn_edt, cOccupation_edt, cIncome_edt;
    private EditText sMarital_edt, sChildren_edt, sMotherTongue_edt, sReligion_edt;
    private EditText lregFullName_edt, lregEmailId_edt, lregPassword_edt, lMobileno_edt;

    private EditText fathersName_edt, address_edt;
    private TextInputLayout fullnameLayout, fathersNameLayout, stateLayout,
            cityLayout, dateOfbirthLayout, heightLayout, countrylayout, addressLayout;

    private TextInputLayout highestEduLayout, employedInLayout, incomeLayout, occupationLayout;
    private TextInputLayout maritalStatusLayout, religionLayout, haveChildrenLayout, motherTongueLayout;
    private TextInputLayout emailIdLayout, passwordLayout, mobnoLayout;

    private Calendar myCalendar;

    private String carrerDetails[] = new String[4];
    private String socialDetails[] = new String[4];
    private String loginDetails[] = new String[4];
    private LinearLayout ll;
    RadioGroup rGender, rBody, rColour;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener dialogClickListener;
    private String data;
    private Toolbar toolbar;
    private FirebaseAuth fAuth;
    private android.app.AlertDialog.Builder heightStatus, countryStatus, stateStatus,
            highestEducation, employedIn, occupation, income,
            maritalStatus, haveChildren, motherTongue, religion;

    private String inputDob;
    private String inputheight;
    private String inputCountry;
    private String inputState;
    private String inputCity;
    private String inputEducation;
    private String inputEmployment;
    private String inputOccupation;
    private String inputIncome;
    private String inputMaritalStatus;
    private String inputHaveChildren;
    private String inputMotherTongue;
    private String inputReligion;
    private String inputFullName;
    private String inputFathersName;
    private String inputAddress;
    private String inputPassword;
    private String inputEmailID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
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
//        setState_edt();

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
                        String gender,body,colour;
                        int selectedId = rGender.getCheckedRadioButtonId();
                        int colorSelectID = rColour.getCheckedRadioButtonId();
                        int bodySelectID = rBody.getCheckedRadioButtonId();
                        RadioButton genderradioButton = findViewById(selectedId);
                        RadioButton bodyradioButton = findViewById(bodySelectID);
                        RadioButton colourradioButton = findViewById(colorSelectID);



                        if (personalDetailsCheckValidity() && careerDetailsCheckValidity()
                                && socialDetailsCheckValidity() && loginDetailsCheckValidity()
                                && selectedId != -1 && bodySelectID!= -1 &&  colorSelectID != -1) {
                            gender = genderradioButton.getText().toString();
                            body = bodyradioButton.getText().toString();
                            colour = colourradioButton.getText().toString();
                            String mobnp = lMobileno_edt.getText().toString();

                            Intent intent = new Intent(RegisterActivity.this, OTPVerificationActivity.class);
                            intent.putExtra(FormDataVariables.bGender, gender);
                            intent.putExtra(FormDataVariables.bFathersName, inputFathersName);
                            intent.putExtra(FormDataVariables.bFullName, inputFullName);
                            intent.putExtra(FormDataVariables.bDoB, inputDob);
                            intent.putExtra(FormDataVariables.bHeight, inputheight);
                            intent.putExtra(FormDataVariables.bState, inputState);
                            intent.putExtra(FormDataVariables.bCountry, inputCountry);
                            intent.putExtra(FormDataVariables.bCity, inputCity);
                            intent.putExtra(FormDataVariables.bAddress, inputAddress);
                            intent.putExtra(FormDataVariables.bColor, colour);
                            intent.putExtra(FormDataVariables.bBody, body);
                            intent.putExtra(FormDataVariables.bEducation, inputEducation);
                            intent.putExtra(FormDataVariables.bEmployedIn, inputEmployment);
                            intent.putExtra(FormDataVariables.bOccupation, inputOccupation);
                            intent.putExtra(FormDataVariables.bIncome, inputIncome);
                            intent.putExtra(FormDataVariables.bMaritalStatus, inputMaritalStatus);
                            intent.putExtra(FormDataVariables.bHaveChildren, inputHaveChildren);
                            intent.putExtra(FormDataVariables.bMotherTongue, inputMotherTongue);
                            intent.putExtra(FormDataVariables.bReligion, inputReligion);
                            intent.putExtra(FormDataVariables.bEmail, inputEmailID);
                            intent.putExtra(FormDataVariables.bPassword, inputPassword);
                            intent.putExtra(FormDataVariables.bMobile, mobnp);

                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                        }
//                        String email=lregEmailId_edt.getText().toString();
//                        String password=lregPassword_edt.getText().toString();
//                        if (email.length()>0 && password.length()>0) {
//                            createAccount(email,password);
//                        }
//                        else {
//                            Toast.makeText(RegisterActivity.this, "Please enter login credentials", Toast.LENGTH_SHORT).show();
//                        }
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
        fathersName_edt = findViewById(R.id.fathersName_edt);
        address_edt = findViewById(R.id.address_edt);
        lregEmailId_edt = findViewById(R.id.reg_emailId_edt);
        lregPassword_edt = findViewById(R.id.reg_pass_edt);
        lMobileno_edt = findViewById(R.id.regMobile_edt);

        emailIdLayout = findViewById(R.id.reg_emailId_ll);
        passwordLayout = findViewById(R.id.reg_pass_ll);
       mobnoLayout = findViewById(R.id.regMobile_ll);


    }

    private void personalDetails() {
        pHeight_edt = findViewById(R.id.height_edt);
        rGender = findViewById(R.id.group_rdbtn);
        rBody = findViewById(R.id.groupbodytype_rdbtn);
        rColour = findViewById(R.id.group_complexion_rdbtn);
        pCountry_edt = findViewById(R.id.country_edt);
        pState_edt = findViewById(R.id.state_edt);
        pCity_edt = findViewById(R.id.city_edt);
        ll = findViewById(R.id.reg_ll);


        heightLayout = findViewById(R.id.height_ll);
        countrylayout = findViewById(R.id.country_ll);
        stateLayout = findViewById(R.id.state_ll);
        cityLayout = findViewById(R.id.city_ll);
        fullnameLayout = findViewById(R.id.fullname_ll);
        fathersNameLayout = findViewById(R.id.fathersName_ll);
        dateOfbirthLayout = findViewById(R.id.dateOfBirth_ll);
        addressLayout = findViewById(R.id.address_ll);


    }

    private void careerDetails() {
        cEducation_edt = findViewById(R.id.education_edt);
        cEmployedIn_edt = findViewById(R.id.employed_edt);
        cOccupation_edt = findViewById(R.id.occupation_edt);
        cIncome_edt = findViewById(R.id.income_edt);

        highestEduLayout = findViewById(R.id.education_ll);
        employedInLayout = findViewById(R.id.employed_ll);
        occupationLayout = findViewById(R.id.occupation_ll);
        incomeLayout = findViewById(R.id.income_ll);

    }

    private void socialDetails() {
        sMarital_edt = findViewById(R.id.marital_status_edt);
        sChildren_edt = findViewById(R.id.have_children_edt);
        sMotherTongue_edt = findViewById(R.id.motherTongue_edt);
        sReligion_edt = findViewById(R.id.religion_edt);

        maritalStatusLayout = findViewById(R.id.marital_status_ll);
        haveChildrenLayout = findViewById(R.id.have_children_ll);
        motherTongueLayout = findViewById(R.id.motherTongue_ll);
        religionLayout = findViewById(R.id.religion_ll);
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
//        pState_edt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stateStatus.show();
//            }
//        });

        cEducation_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                highestEducation.show();
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

            String gender = "Nothing Selected";
            String body = "Nothing Selected";
            String colour = "Nothing Selected";

            inputDob = pDob_editText.getText().toString();
            inputheight = pHeight_edt.getText().toString();
            inputCountry = pCountry_edt.getText().toString();
            inputState = pState_edt.getText().toString();
            inputCity = pCity_edt.getText().toString().toUpperCase();
            inputEducation = cEducation_edt.getText().toString().toUpperCase();
            inputEmployment = cEmployedIn_edt.getText().toString();
            inputOccupation = cOccupation_edt.getText().toString();
            inputIncome = cIncome_edt.getText().toString();
            inputMaritalStatus = sMarital_edt.getText().toString();
            inputHaveChildren = sChildren_edt.getText().toString();
            inputMotherTongue = sMotherTongue_edt.getText().toString().toUpperCase();
            inputReligion = sReligion_edt.getText().toString();
            inputFullName =lregFullName_edt.getText().toString();
            inputFathersName =fathersName_edt.getText().toString();
            inputAddress =address_edt.getText().toString();
            inputEmailID =lregEmailId_edt.getText().toString();
            inputPassword =lregPassword_edt.getText().toString();

            data = "Gender: " + gender + "\n" +
                    "Full Name: " + inputFullName + "\n" +
                    "Fathers Name: " + inputFathersName + "\n" +
                    "DOB: " + inputDob + "\n" +
                    "Height: " + inputheight + "\n" +
                    "Country: " + inputCountry + "\n" +
                    "State: " + inputState + "\n" +
                    "Address: " + inputAddress + "\n" +
                    "City: " + inputCity + "\n" +
                    "Education: " + inputEducation + "\n" +
                    "Employed In: " + inputEmployment + "\n" +
                    "Occupation: " + inputOccupation + "\n" +
                    "Income: " + inputIncome + "\n" +
                    "Body: " + body + "\n" +
                    "Colour: " + colour + "\n" +
                    "Marital Status: " + inputMaritalStatus + "\n" +
                    "Have Children: " + inputHaveChildren + "\n" +
                    "Mother Tongue: " + inputMotherTongue +"\n" +
                    "Religion: " + inputReligion+"\n" +
                    "Email_Id: " + inputEmailID+"\n"+
                    "Password: " + inputPassword;


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
        heightStatus = new android.app.AlertDialog.Builder(this);
        heightStatus.setIcon(R.drawable.jodi_milan_logo);
        heightStatus.setTitle("Select Height:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        heightStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        heightStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            pHeight_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
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
        countryStatus = new android.app.AlertDialog.Builder(this);
        countryStatus.setIcon(R.drawable.jodi_milan_logo);
        countryStatus.setTitle("Select Country-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        countryStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        countryStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            pCountry_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
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

      /*  ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        stateStatus.setAnchorView(toolbar); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        stateStatus.setAdapter(adapter);
        stateStatus.setOnItemClickListener((parent, view, position, id) -> {
            pState_edt.setText(status.get(position));//we set the selected element in the EditText

            stateStatus.dismiss();


        });*/

        stateStatus = new android.app.AlertDialog.Builder(this);
        stateStatus.setIcon(R.drawable.jodi_milan_logo);
        stateStatus.setTitle("Select State-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        stateStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        stateStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            pState_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
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

//        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        highestEducation = new android.app.AlertDialog.Builder(this);
        highestEducation.setIcon(R.drawable.jodi_milan_logo);
        highestEducation.setTitle("Highest Education-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

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

        employedIn = new android.app.AlertDialog.Builder(this);
        employedIn.setIcon(R.drawable.jodi_milan_logo);
        employedIn.setTitle("Employed In-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

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

        occupation = new android.app.AlertDialog.Builder(this);
        occupation.setIcon(R.drawable.jodi_milan_logo);
        occupation.setTitle("Select State-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        occupation.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        occupation.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            cOccupation_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
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

        income = new android.app.AlertDialog.Builder(this);
        income.setIcon(R.drawable.jodi_milan_logo);
        income.setTitle("Select Income-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

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


        maritalStatus = new android.app.AlertDialog.Builder(this);
        maritalStatus.setIcon(R.drawable.jodi_milan_logo);
        maritalStatus.setTitle("Select Marital Status-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        maritalStatus.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        maritalStatus.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            sMarital_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
        });
    }

    private void setHaveChildren_edt() {
        final List<String> status = new ArrayList<>();
        status.add("Yes");
        status.add("No");

        haveChildren = new android.app.AlertDialog.Builder(this);
        haveChildren.setIcon(R.drawable.jodi_milan_logo);
        haveChildren.setTitle("Have children-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        haveChildren.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        haveChildren.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            sChildren_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
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

        motherTongue = new android.app.AlertDialog.Builder(this);
        motherTongue.setIcon(R.drawable.jodi_milan_logo);
        motherTongue.setTitle("Select Mother Tongue-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        motherTongue.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        motherTongue.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            sMotherTongue_edt.setText(strName);//we set the selected element in the EditText

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

        religion = new android.app.AlertDialog.Builder(this);
        religion.setIcon(R.drawable.jodi_milan_logo);
        religion.setTitle("Select Religion-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, status);

        religion.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
        religion.setAdapter(arrayAdapter, (dialog, which) -> {
            String strName = arrayAdapter.getItem(which);
            sReligion_edt.setText(strName);//we set the selected element in the EditText

//            Toast.makeText(this, "Item selected: "+ strName, Toast.LENGTH_SHORT).show();
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

    private boolean personalDetailsCheckValidity() {
        if (fullNameValidityInputs() && fathernameValidityInputs() && cityValidityInputs()
                && stateValidityInputs() && countryValidityInputs() && dateOfBirthValidityInputs() && addressValidityInputs() && heightValidityInputs()) {
            return true;
        }
        return false;
    }

    private boolean careerDetailsCheckValidity() {
        if (highestEducValidityInputs() && employedInValidityInputs() && occupationValidityInputs() && incomeValidityInputs()) {
            return true;
        }
        return false;
    }

    private boolean socialDetailsCheckValidity() {
        if (maritalValidityInputs() && haveChildrenValidityInputs() && religionValidityInputs() && motherTongueValidityInputs()) {
            return true;
        }
        return false;
    }

    private boolean loginDetailsCheckValidity() {
        if (emailIdValidityInputs() && passwordValidityInputs() && mobileNoValidityInputs()) {
            return true;
        }
        return false;
    }

    public boolean fullNameValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(lregFullName_edt.getText().toString())) {
            fullnameLayout.setErrorEnabled(true);
            fullnameLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            fullnameLayout.setErrorEnabled(false);
            fullnameLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean fathernameValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(fathersName_edt.getText().toString())) {
            fathersNameLayout.setErrorEnabled(true);
            fathersNameLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            fathersNameLayout.setErrorEnabled(false);
            fathersNameLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean cityValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(pCity_edt.getText().toString())) {
            cityLayout.setErrorEnabled(true);
            cityLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            cityLayout.setErrorEnabled(false);
            cityLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean stateValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(pState_edt.getText().toString())) {
            stateLayout.setErrorEnabled(true);
            stateLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            stateLayout.setErrorEnabled(false);
            stateLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean countryValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(pCountry_edt.getText().toString())) {
            countrylayout.setErrorEnabled(true);
            countrylayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            countrylayout.setErrorEnabled(false);
            countrylayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean dateOfBirthValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(pDob_editText.getText().toString())) {
            dateOfbirthLayout.setErrorEnabled(true);
            dateOfbirthLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            dateOfbirthLayout.setErrorEnabled(false);
            dateOfbirthLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean addressValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(address_edt.getText().toString())) {
            addressLayout.setErrorEnabled(true);
            addressLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            addressLayout.setErrorEnabled(false);
            addressLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean heightValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(pHeight_edt.getText().toString())) {
            heightLayout.setErrorEnabled(true);
            heightLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            heightLayout.setErrorEnabled(false);
            heightLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }


    public boolean highestEducValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(cEducation_edt.getText().toString())) {
            highestEduLayout.setErrorEnabled(true);
            highestEduLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            highestEduLayout.setErrorEnabled(false);
            highestEduLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    public boolean employedInValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(cEmployedIn_edt.getText().toString())) {

            employedInLayout.setErrorEnabled(true);
            employedInLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            employedInLayout.setErrorEnabled(false);
            employedInLayout.setError(null);
            contactStatus = true;
        }


        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean occupationValidityInputs() {
        boolean contactStatus;

        if (TextUtils.isEmpty(cOccupation_edt.getText().toString())) {

            occupationLayout.setErrorEnabled(true);
            occupationLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            occupationLayout.setErrorEnabled(false);
            occupationLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean incomeValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(cIncome_edt.getText().toString())) {
            incomeLayout.setErrorEnabled(true);
            incomeLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            incomeLayout.setErrorEnabled(false);
            incomeLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean maritalValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(sMarital_edt.getText().toString())) {

            maritalStatusLayout.setErrorEnabled(true);
            maritalStatusLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            maritalStatusLayout.setErrorEnabled(false);
            maritalStatusLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    public boolean haveChildrenValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(sChildren_edt.getText().toString())) {

            haveChildrenLayout.setErrorEnabled(true);
            haveChildrenLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            haveChildrenLayout.setErrorEnabled(false);
            haveChildrenLayout.setError(null);
            contactStatus = true;
        }


        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean motherTongueValidityInputs() {

        boolean contactStatus;

        if (TextUtils.isEmpty(sMotherTongue_edt.getText().toString())) {
            motherTongueLayout.setErrorEnabled(true);
            motherTongueLayout.setError(MANDATORY);
            contactStatus = false;
        } else {
            motherTongueLayout.setErrorEnabled(false);
            motherTongueLayout.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean religionValidityInputs() {
        boolean contactStatus;

        if (TextUtils.isEmpty(sReligion_edt.getText().toString())) {

            religionLayout.setErrorEnabled(true);
            religionLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            religionLayout.setErrorEnabled(false);
            religionLayout.setError(null);
            contactStatus = true;
        }


        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean emailIdValidityInputs() {
        boolean contactStatus;

        if (TextUtils.isEmpty(lregEmailId_edt.getText().toString())) {

            emailIdLayout.setErrorEnabled(true);
            emailIdLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            emailIdLayout.setErrorEnabled(false);
            emailIdLayout.setError(null);
            contactStatus = true;
        }


        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean passwordValidityInputs() {
        boolean contactStatus;

        if (TextUtils.isEmpty(lregPassword_edt.getText().toString()) && lregPassword_edt.getText().toString().length()<7 ) {

            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Password should be atleast 7 characters long.");
            contactStatus = false;
        } else {

            passwordLayout.setErrorEnabled(false);
            passwordLayout.setError(null);
            contactStatus = true;
        }


        if (contactStatus == true)
            return true;
        return false;


    }

    public boolean mobileNoValidityInputs() {
        boolean contactStatus;

        if (TextUtils.isEmpty(lMobileno_edt.getText().toString())) {

            mobnoLayout.setErrorEnabled(true);
            mobnoLayout.setError(MANDATORY);
            contactStatus = false;
        } else {

            mobnoLayout.setErrorEnabled(false);
            mobnoLayout.setError(null);
            contactStatus = true;
        }


        if (contactStatus == true)
            return true;
        return false;


    }


}
