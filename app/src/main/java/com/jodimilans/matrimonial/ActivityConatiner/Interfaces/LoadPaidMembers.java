package com.jodimilans.matrimonial.ActivityConatiner.Interfaces;

import com.jodimilans.matrimonial.HelperClasses.ProductEntry;

import java.util.List;

public interface LoadPaidMembers {
    void onPaidMembersLoadSuccess(List<ProductEntry> templates);

    void onPaidMembersLoadFailure(String message);
}
