package com.jodimilans.matrimonial.ActivityConatiner.Interfaces;


import androidx.annotation.Keep;

import com.jodimilans.matrimonial.HelperClasses.ProductEntry;

import java.util.List;

@Keep
public interface LoadAllProfiles {

    void onProfilesLoadSuccess(List<ProductEntry> templates);

    void onProfilesLoadFailure(String message);
}
