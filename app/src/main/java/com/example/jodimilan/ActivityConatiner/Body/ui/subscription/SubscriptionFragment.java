package com.example.jodimilan.ActivityConatiner.Body.ui.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.ActivityConatiner.Body.ui.SelectPlanActivity;
import com.example.jodimilan.MainActivity;
import com.example.jodimilan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SubscriptionFragment extends Fragment {
    Button chosePlan;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        TextView subscriptionFormat=root.findViewById(R.id.subscriptionText);
        subscriptionFormat.setText(Html.fromHtml(getString(R.string.nice_html)));
    chosePlan= root.findViewById(R.id.choose_a_plan_btn);
        chosePlan.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SelectPlanActivity.class));
        });
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        return root;
    }
}
