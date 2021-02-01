package com.jodimilans.matrimonial.ActivityConatiner.Interfaces;

import androidx.annotation.Keep;

import com.jodimilans.matrimonial.HelperClasses.ProductEntry;

import java.util.List;

@Keep
public interface LoadPaidMembers {
    void onPaidMembersLoadSuccess(List<ProductEntry> templates);

    void onPaidMembersLoadFailure(String message);
}
