package com.example.jodimilan.HelperClasses;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class ProductEntry {
    String Address;
    String Body;
    String City;
    String Colour;
    String Country;
    String DOB;
    String Education;
    String EmailID;
    String EmployedIn;
    String FathersName;
    String FullName;
    String Gender;
    GeoPoint Geopoint;
    String HaveChildren;
    String Height;
    String Income;
    String MaritalStatus;
    String Mobile;
    String MotherTongue;
    String Occupation;
    String Religion;
    String State;
    String UID;
    String photoLink;
    String profileID;
    String expiryDaysLimit;
    String boughtBy;

    public String getExpiryDaysLimit() {
        return expiryDaysLimit;
    }

    public void setExpiryDaysLimit(String expiryDaysLimit) {
        this.expiryDaysLimit = expiryDaysLimit;
    }



    public String getBoughtBy() {
        return boughtBy;
    }

    public void setBoughtBy(String boughtBy) {
        this.boughtBy = boughtBy;
    }

    public String getPlanBought() {
        return planBought;
    }

    public void setPlanBought(String planBought) {
        this.planBought = planBought;
    }

    String planBought;

    private Map<String, Object> purchaseTime;





    public Map<String, Object> getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Map<String, Object> purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public ProductEntry()
    {

    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getEmployedIn() {
        return EmployedIn;
    }

    public void setEmployedIn(String employedIn) {
        EmployedIn = employedIn;
    }

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String fathersName) {
        FathersName = fathersName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public GeoPoint getGeopoint() {
        return Geopoint;
    }

    public void setGeopoint(GeoPoint geopoint) {
        Geopoint = geopoint;
    }

    public String getHaveChildren() {
        return HaveChildren;
    }

    public void setHaveChildren(String haveChildren) {
        HaveChildren = haveChildren;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getIncome() {
        return Income;
    }

    public void setIncome(String income) {
        Income = income;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getMotherTongue() {
        return MotherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        MotherTongue = motherTongue;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getReligion() {
        return Religion;
    }

    public void setReligion(String religion) {
        Religion = religion;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    @Override
    public String toString() {
        return "ProductEntry{" +
                "Address='" + Address + '\'' +
                ", Body='" + Body + '\'' +
                ", City='" + City + '\'' +
                ", Colour='" + Colour + '\'' +
                ", Country='" + Country + '\'' +
                ", DOB='" + DOB + '\'' +
                ", Education='" + Education + '\'' +
                ", EmailID='" + EmailID + '\'' +
                ", EmployedIn='" + EmployedIn + '\'' +
                ", FathersName='" + FathersName + '\'' +
                ", FullName='" + FullName + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Geopoint=" + Geopoint +
                ", HaveChildren='" + HaveChildren + '\'' +
                ", Height='" + Height + '\'' +
                ", Income='" + Income + '\'' +
                ", MaritalStatus='" + MaritalStatus + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", MotherTongue='" + MotherTongue + '\'' +
                ", Occupation='" + Occupation + '\'' +
                ", Religion='" + Religion + '\'' +
                ", State='" + State + '\'' +
                ", UID='" + UID + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", profileID='" + profileID + '\'' +
                '}';
    }
}
