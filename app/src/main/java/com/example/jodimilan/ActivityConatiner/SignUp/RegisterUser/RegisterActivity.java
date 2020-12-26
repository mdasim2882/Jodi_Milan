package com.example.jodimilan.ActivityConatiner.SignUp.RegisterUser;

import android.app.DatePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;

import com.example.jodimilan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText dob_editText, height_edt, county_edt, state_edt, city_edt;
    private Calendar myCalendar;
    private ListPopupWindow heightStatus, countryStatus, stateStatus;
    private LinearLayout ll;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findCalendarAndsetText();
        initializeViews();
        setUpToolbar();
        setHeightPopupList();
        setCounty_edt();
        setState_edt();


        setListeners();


    }

    private void initializeViews() {
        height_edt = findViewById(R.id.height_edt);
        radioGroup = findViewById(R.id.group_rdbtn);
        county_edt = findViewById(R.id.country_edt);
        state_edt = findViewById(R.id.state_edt);
        city_edt = findViewById(R.id.city_edt);
        ll = findViewById(R.id.reg_ll);
    }

    private void setListeners() {
        height_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightStatus.show();
            }
        });
        county_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryStatus.show();
            }
        });
        state_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateStatus.show();
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
            String data = "Gender: " + gender + "\n" +
                    "DOB: " + dob_editText.getText().toString() + "\n" +
                    "Height: " + height_edt.getText().toString() + "\n" +
                    "Country: " + county_edt.getText().toString() + "\n" +
                    "State: " + state_edt.getText().toString() + "\n" +
                    "City: " + city_edt.getText().toString().toUpperCase();


            Log.d("TAG", "setListeners: Clicked with data: -->\n" + data);
//            Snackbar.make(ll,"DATA:\n"+data, Snackbar.LENGTH_LONG).show();
            Toast.makeText(this, "USER DATA \n" + data, Toast.LENGTH_SHORT).show();

        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.personal_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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
        heightStatus.setAnchorView(findViewById(R.id.group_rdbtn)); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        heightStatus.setAdapter(adapter);
        heightStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String taggy = (String) view.getTag();

                height_edt.setText(status.get(position));//we set the selected element in the EditText

                heightStatus.dismiss();


            }
        });
    }

    private void setCounty_edt() {
        LinearLayout ll = findViewById(R.id.reg_ll);

        final List<String> status = new ArrayList<>();
        status.add("Race 1");
        status.add("Race 2");
        status.add("Race 3");
        status.add("Race 4");
        status.add("Race 5");
        status.add("Race 6");
        status.add("Race 7");
        status.add("Race 8");
        status.add("Race 9");
        status.add("Race 10");

        countryStatus = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        countryStatus.setAnchorView(findViewById(R.id.group_rdbtn)); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        countryStatus.setAdapter(adapter);
        countryStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                county_edt.setText(status.get(position));//we set the selected element in the EditText
                countryStatus.dismiss();


            }
        });
    }

    private void setState_edt() {


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

        stateStatus = new ListPopupWindow(RegisterActivity.this);
        ArrayAdapter adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.item_simple_status, R.id.tv_element, status);
        stateStatus.setAnchorView(findViewById(R.id.group_rdbtn)); //this let as set the popup below the EditText
        //this let as set the popup below the EditText

        stateStatus.setAdapter(adapter);
        stateStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_edt.setText(status.get(position));//we set the selected element in the EditText

                stateStatus.dismiss();


            }
        });
    }

    private void findCalendarAndsetText() {
        myCalendar = Calendar.getInstance();

        dob_editText = (EditText) findViewById(R.id.dateOfBirth_edt);
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

        dob_editText.setOnClickListener(new View.OnClickListener() {

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

        dob_editText.setText(sdf.format(myCalendar.getTime()));
    }
}
