package com.example.jodimilan.ActivityConatiner.Body.ui.subscription;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.ActivityConatiner.Body.ui.SelectPlanActivity;
import com.example.jodimilan.ActivityConatiner.SignUp.LoginActivity;
import com.example.jodimilan.ActivityConatiner.SignUp.RegisterUser.RegisterActivity;
import com.example.jodimilan.HelperClasses.PrefVariables;
import com.example.jodimilan.MainActivity;
import com.example.jodimilan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SubscriptionFragment extends Fragment {
    public static final String IS_REGISTERED = "isRegistered";
    Button chosePlan;
    public final String ISLOGIN = "islogin";

    public String fileName = "myfile.html";
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);

        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
//        TextView subscriptionFormat=root.findViewById(R.id.subscriptionText);
        WebView webView = (WebView) root.findViewById(R.id.simpleWebView);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            subscriptionFormat.setText(Html.fromHtml(getString(R.string.nice_html), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            subscriptionFormat.setText(Html.fromHtml(getString(R.string.nice_html)));
//        }


    chosePlan= root.findViewById(R.id.choose_a_plan_btn);
        chosePlan.setOnClickListener(v -> {
            Boolean registered = sharedPreferences.getBoolean(IS_REGISTERED, false);
            if(registered) {
                startActivity(new Intent(getActivity(), SelectPlanActivity.class));
            }
            else{
                Toast.makeText(getActivity(), "Register to continue...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        return root;
    }
}
