package com.example.jodimilan.ActivityConatiner.Interfaces;

import com.example.jodimilan.HelperClasses.ProductEntry;

import java.util.List;

public interface LoadPaidMembers {
    void onPaidMembersLoadSuccess(List<ProductEntry> templates);

    void onPaidMembersLoadFailure(String message);
}
