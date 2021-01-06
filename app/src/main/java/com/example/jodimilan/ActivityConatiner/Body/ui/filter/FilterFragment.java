package com.example.jodimilan.ActivityConatiner.Body.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jodimilan.ActivityConatiner.Body.HomeActivity;
import com.example.jodimilan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FilterFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_filter, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
            textView.setText("FILTER FRAGMENT");
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }

        return root;
    }
}