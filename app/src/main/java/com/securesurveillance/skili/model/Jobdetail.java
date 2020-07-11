package com.securesurveillance.skili.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adarsh on 9/25/2018.
 */

// Parcelling part

public class Jobdetail implements Serializable {
    String jobId;
    String jobTitle;
    String minRequiredExp;
    String maxRequiredExp;
    String jobLocation;
    String minOfferedSalary;
    String maxOfferedSalary;
    String ctcType;
    String salaryUnit;
    ArrayList<String> skills;
    String description;
    String employerDetails;
    String functionalArea;
    String status;
    String createdOn;
    ArrayList<String> otherSkills;

    // Constructor
    public Jobdetail(String jobId, String jobTitle, String minRequiredExp, String maxRequiredExp, String jobLocation,
                     String minOfferedSalary,
                     String maxOfferedSalary, String ctcType, String salaryUnit, String description, String employerDetails,
                     String functionalArea, String status, String createdOn, ArrayList<String> skills, ArrayList<String> otherSkills) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.minRequiredExp = minRequiredExp;
        this.maxRequiredExp = maxRequiredExp;
        this.jobLocation = jobLocation;
        this.minOfferedSalary = minOfferedSalary;
        this.maxOfferedSalary = maxOfferedSalary;
        this.ctcType = ctcType;
        this.salaryUnit = salaryUnit;
        this.description = description;
        this.employerDetails = employerDetails;
        this.functionalArea = functionalArea;
        this.status = status;
        this.createdOn = createdOn;
        this.skills = skills;
        this.otherSkills = otherSkills;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMinRequiredExp() {
        return minRequiredExp;
    }

    public void setMinRequiredExp(String minRequiredExp) {
        this.minRequiredExp = minRequiredExp;
    }

    public String getMaxRequiredExp() {
        return maxRequiredExp;
    }

    public void setMaxRequiredExp(String maxRequiredExp) {
        this.maxRequiredExp = maxRequiredExp;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getMinOfferedSalary() {
        return minOfferedSalary;
    }

    public void setMinOfferedSalary(String minOfferedSalary) {
        this.minOfferedSalary = minOfferedSalary;
    }

    public String getMaxOfferedSalary() {
        return maxOfferedSalary;
    }

    public void setMaxOfferedSalary(String maxOfferedSalary) {
        this.maxOfferedSalary = maxOfferedSalary;
    }

    public String getCtcType() {
        return ctcType;
    }

    public void setCtcType(String ctcType) {
        this.ctcType = ctcType;
    }

    public String getSalaryUnit() {
        return salaryUnit;
    }

    public void setSalaryUnit(String salaryUnit) {
        this.salaryUnit = salaryUnit;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployerDetails() {
        return employerDetails;
    }

    public void setEmployerDetails(String employerDetails) {
        this.employerDetails = employerDetails;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public ArrayList<String> getOtherSkills() {
        return otherSkills;
    }

    public void setOtherSkills(ArrayList<String> otherSkills) {
        this.otherSkills = otherSkills;
    }



}