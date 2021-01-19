package com.jodimilans.matrimonial.ActivityConatiner.Interfaces;

import com.jodimilans.matrimonial.HelperClasses.ProductEntry;

public interface LoadSearchProfile {
    void onSearchSuccess(ProductEntry templates);

    void onSearchFailure(String message);

}
