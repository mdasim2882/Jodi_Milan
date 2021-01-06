package com.example.jodimilan.ActivityConatiner.Interfaces;


import com.example.jodimilan.HelperClasses.ProductEntry;

import java.util.List;

public interface LoadAllProfiles {

    void onProfilesLoadSuccess(List<ProductEntry> templates);

    void onProfilesLoadFailure(String message);
}
