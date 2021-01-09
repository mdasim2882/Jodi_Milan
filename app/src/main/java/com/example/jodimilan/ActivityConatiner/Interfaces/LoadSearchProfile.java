package com.example.jodimilan.ActivityConatiner.Interfaces;

import com.example.jodimilan.HelperClasses.ProductEntry;

import java.util.List;

public interface LoadSearchProfile {
    void onSearchSuccess(ProductEntry templates);

    void onSearchFailure(String message);

}
