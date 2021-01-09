package com.example.jodimilan.ActivityConatiner.Body;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jodimilan.HelperClasses.FormDataVariables;
import com.example.jodimilan.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProfileDetailsActivity extends AppCompatActivity {
    public  final String TAG = getClass().getSimpleName();
    Bundle b;
  CardView above,below;
    private String inputDob;
    private String photoURL;
    private String inputheight;
    private String inputCountry;
    private String inputState;
    private String inputCity;
    private String gender;
    private String body;
    private String colour;
    private String mobileno;
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

    TextView name,contacno,height,fathersName,dateOfBirth,
            country,state,address,city,genderView,
            colorView,bodyTypeView,edcationview,employedInView,occupationView,
            incomeview,maritalStatusView,haveChildren,religionView,motherTongue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 0){
                    Log.d(TAG, "handleMessage: Exucuted-===> "+formatterText());
                    updateUI();
                }else{
                    Log.d(TAG, "handleMessage: ERRRRRRROOOOOR");
                }
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run(){

                if(doSomeWork()){
                    //we can't update the UI from here so we'll signal our handler and it will do it for us.
                    handler.sendEmptyMessage(0);
                }else{
                    handler.sendEmptyMessage(1);
                }
            }
        };
        thread.start();

        initializeViews();

    }

    private void initializeViews(){
        above=findViewById(R.id.pictureCard);
        below=findViewById(R.id.details_card);

        genderView=below.findViewById(R.id.genderData);
        name=below.findViewById(R.id.name_data);
        fathersName= below.findViewById(R.id.fath_name_data);
        dateOfBirth=below.findViewById(R.id.dOB_data);
        height=below.findViewById(R.id.height_data);
        country=below.findViewById(R.id.countryData);
        state=below.findViewById(R.id.stateData);
        address=below.findViewById(R.id.addressData);
        city=below.findViewById(R.id.cityData);
        colorView=below.findViewById(R.id.colordata);
        bodyTypeView=below.findViewById(R.id.bodydata);
        edcationview=below.findViewById(R.id.education_data);
        employedInView=below.findViewById(R.id.employeIn_data);
        occupationView=below.findViewById(R.id.occupation_data);
        incomeview=below.findViewById(R.id.incomeData);

        maritalStatusView=below.findViewById(R.id.maritalStatusData);
        motherTongue=below.findViewById(R.id.motherTonguedata);
        religionView=below.findViewById(R.id.religiondata);
        haveChildren=below.findViewById(R.id.haveChildren_data);

        contacno=below.findViewById(R.id.mobno_data);
    }
    private void updateUI() {
        name.setText(inputFullName);
        fathersName.setText(inputFathersName);
        contacno.setText(mobileno);
        genderView.setText(gender);
        colorView.setText(colour);
        bodyTypeView.setText(body);
        dateOfBirth.setText(inputDob);


        height.setText(inputheight);
        haveChildren.setText(inputHaveChildren);
        maritalStatusView.setText(inputMaritalStatus);
        religionView.setText(inputReligion);
        motherTongue.setText(inputMotherTongue);

        edcationview.setText(inputEducation);
        incomeview.setText(inputIncome);
        employedInView.setText(inputEmployment);
        occupationView.setText(inputOccupation);

        state.setText(inputState);
        city.setText(inputCity);
        country.setText(inputCountry);
        address.setText(inputAddress);

        ImageView profilePic=above.findViewById(R.id.userPicture);
    Glide.with(this).load(Uri.parse(photoURL)).into(profilePic);


    }

    private boolean doSomeWork() {
        Intent getter=getIntent();
        b=getter.getBundleExtra("bidiUser");
        Map<String, Serializable> map=bundleToMap(b);

        inputDob = (String) map.get(FormDataVariables.bDoB);
        inputheight = (String) map.get(FormDataVariables.bHeight);
        inputCountry = (String) map.get(FormDataVariables.bCountry);
        inputState = (String) map.get(FormDataVariables.bState);
        inputCity = (String) map.get(FormDataVariables.bCity);
        inputEducation = (String) map.get(FormDataVariables.bEducation);
        inputEmployment = (String) map.get(FormDataVariables.bEmployedIn);
        inputOccupation = (String) map.get(FormDataVariables.bOccupation);
        inputIncome = (String) map.get(FormDataVariables.bIncome);
        inputMaritalStatus = (String) map.get(FormDataVariables.bMaritalStatus);
        inputHaveChildren = (String) map.get(FormDataVariables.bHaveChildren);
        inputMotherTongue =(String) map.get(FormDataVariables.bMotherTongue);
        inputReligion = (String) map.get(FormDataVariables.bReligion);
        inputFullName =(String) map.get(FormDataVariables.bFullName);
        inputFathersName =(String) map.get(FormDataVariables.bFathersName);
        inputAddress =(String) map.get(FormDataVariables.bAddress);
        gender = (String) map.get(FormDataVariables.bGender);
        colour=(String) map.get(FormDataVariables.bColor);
        body=(String) map.get(FormDataVariables.bBody);
        mobileno=(String) map.get(FormDataVariables.bMobile);
        photoURL=(String)map.get(FormDataVariables.bProfilePicture);

        return true;
    }

    public String formatterText() {
        Map map=bundleToMap(b);
        return "ProfileDetailsActivity{"+"Bidi:"+map +
                "inputDob='" + inputDob + '\'' +
                ", inputheight='" + inputheight + '\'' +
                ", inputCountry='" + inputCountry + '\'' +
                ", inputState='" + inputState + '\'' +
                ", inputCity='" + inputCity + '\'' +
                ", gender='" + gender + '\'' +
                ", body='" + body + '\'' +
                ", colour='" + colour + '\'' +
                ", mobileno='" + mobileno + '\'' +
                ", inputEducation='" + inputEducation + '\'' +
                ", inputEmployment='" + inputEmployment + '\'' +
                ", inputOccupation='" + inputOccupation + '\'' +
                ", inputIncome='" + inputIncome + '\'' +
                ", inputMaritalStatus='" + inputMaritalStatus + '\'' +
                ", inputHaveChildren='" + inputHaveChildren + '\'' +
                ", inputMotherTongue='" + inputMotherTongue + '\'' +
                ", inputReligion='" + inputReligion + '\'' +
                ", inputFullName='" + inputFullName + '\'' +
                ", inputFathersName='" + inputFathersName + '\'' +
                ", PHOTO URL ='" + photoURL + '\'' +
                ", inputAddress='" + inputAddress + '\'' +
                '}';
    }
    public static Map<String, Serializable> bundleToMap(Bundle bundle) {
        Map<String, Serializable> result = new HashMap<>();
        if (bundle == null)
            return result;

        for (String key : bundle.keySet()) {
            result.put(key, bundle.getSerializable(key));
        }
        return result;
    }

}