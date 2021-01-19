package com.jodimilans.matrimonial.ActivityConatiner.Interfaces;


import com.jodimilans.matrimonial.HelperClasses.ProductEntry;

import java.util.List;

public interface LoadAllProfiles {

    void onProfilesLoadSuccess(List<ProductEntry> templates);

    void onProfilesLoadFailure(String message);
}
