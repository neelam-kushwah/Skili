package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 12/20/2018.
 */

public class SearchJobsResponse {
    String message;
    boolean status;
    Result result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
      public   ArrayList<GetAllJobRecruiterDetailModel> rows;
        int unreadNotificationCount;

        public int getUnreadNotificationCount() {
            return unreadNotificationCount;
        }

        public void setUnreadNotificationCount(int unreadNotificationCount) {
            this.unreadNotificationCount = unreadNotificationCount;
        }
        public ArrayList<GetAllJobRecruiterDetailModel> getRows() {
            return rows;
        }

        public void setRows(ArrayList<GetAllJobRecruiterDetailModel> rows) {
            this.rows = rows;
        }

//      public  class Jobs{
//            String contactPerson;
//            String contactMobileNumber;
//            String jobLocation;
//            String lattitude;
//            String jobId;
//            String longitude;
//            String budget;
//            String budgetType;
//            String purposeOfJob;
//            String jobType;
//            String startDate;
//            String endDate;
//            String applicantCount;
//            String status;
//            String jobTitle;
//            String jobDescription;
//
//            public String getContactPerson() {
//                return contactPerson;
//            }
//
//            public void setContactPerson(String contactPerson) {
//                this.contactPerson = contactPerson;
//            }
//
//            public String getContactMobileNumber() {
//                return contactMobileNumber;
//            }
//
//            public void setContactMobileNumber(String contactMobileNumber) {
//                this.contactMobileNumber = contactMobileNumber;
//            }
//
//            public String getJobLocation() {
//                return jobLocation;
//            }
//
//            public void setJobLocation(String jobLocation) {
//                this.jobLocation = jobLocation;
//            }
//
//            public String getLattitude() {
//                return lattitude;
//            }
//
//            public void setLattitude(String lattitude) {
//                this.lattitude = lattitude;
//            }
//
//            public String getJobId() {
//                return jobId;
//            }
//
//            public void setJobId(String jobId) {
//                this.jobId = jobId;
//            }
//
//            public String getLongitude() {
//                return longitude;
//            }
//
//            public void setLongitude(String longitude) {
//                this.longitude = longitude;
//            }
//
//            public String getBudget() {
//                return budget;
//            }
//
//            public void setBudget(String budget) {
//                this.budget = budget;
//            }
//
//            public String getBudgetType() {
//                return budgetType;
//            }
//
//            public void setBudgetType(String budgetType) {
//                this.budgetType = budgetType;
//            }
//
//            public String getPurposeOfJob() {
//                return purposeOfJob;
//            }
//
//            public void setPurposeOfJob(String purposeOfJob) {
//                this.purposeOfJob = purposeOfJob;
//            }
//
//            public String getJobType() {
//                return jobType;
//            }
//
//            public void setJobType(String jobType) {
//                this.jobType = jobType;
//            }
//
//            public String getStartDate() {
//                return startDate;
//            }
//
//            public void setStartDate(String startDate) {
//                this.startDate = startDate;
//            }
//
//            public String getEndDate() {
//                return endDate;
//            }
//
//            public void setEndDate(String endDate) {
//                this.endDate = endDate;
//            }
//
//            public String getApplicantCount() {
//                return applicantCount;
//            }
//
//            public void setApplicantCount(String applicantCount) {
//                this.applicantCount = applicantCount;
//            }
//
//            public String getStatus() {
//                return status;
//            }
//
//            public void setStatus(String status) {
//                this.status = status;
//            }
//
//            public String getJobTitle() {
//                return jobTitle;
//            }
//
//            public void setJobTitle(String jobTitle) {
//                this.jobTitle = jobTitle;
//            }
//
//            public String getJobDescription() {
//                return jobDescription;
//            }
//
//            public void setJobDescription(String jobDescription) {
//                this.jobDescription = jobDescription;
//            }
//        }
    }
}
