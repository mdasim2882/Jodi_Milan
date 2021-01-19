package com.jodimilans.matrimonial.ActivityConatiner.Body.ui.privacyPolicy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jodimilans.matrimonial.ActivityConatiner.Body.HomeActivity;
import com.jodimilans.matrimonial.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PrivacyPolicyFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_privacypolicy, container, false);
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        return root;
    }
}
