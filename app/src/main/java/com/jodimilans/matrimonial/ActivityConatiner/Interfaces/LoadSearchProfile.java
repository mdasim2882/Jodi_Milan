package com.jodimilans.matrimonial.ActivityConatiner.Interfaces;

import androidx.annotation.Keep;

import com.jodimilans.matrimonial.HelperClasses.ProductEntry;


@Keep
public interface LoadSearchProfile {
    void onSearchSuccess(ProductEntry templates);

    void onSearchFailure(String message);

}
