package com.securesurveillance.skili.model.responsemodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 12/20/2018.
 */

public class SearchCandidateResponse {
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
        ArrayList<Candidate> rows;
        int unreadNotificationCount;

        public int getUnreadNotificationCount() {
            return unreadNotificationCount;
        }

        public void setUnreadNotificationCount(int unreadNotificationCount) {
            this.unreadNotificationCount = unreadNotificationCount;
        }

        public ArrayList<Candidate> getRows() {
            return rows;
        }

        public void setRows(ArrayList<Candidate> rows) {
            this.rows = rows;
        }

        public class Candidate {
            String _id;
            String mobileNumber;
            String name;
            String firstName;
            String lastName;
            String profilePicThumbnail;
            String locationString;
            String latitude;
            String longitude;
            String role;
            String rating;
            String rateCard;
            String views;
            String post;
            String appliedJobs;
            String profileSummary;
            String website;
            String dob;
            String city;
            String reviewCounts;
            String charge;
            String chargeType;
            String favouriteApplicant;

            public String getFavouriteApplicant() {
                return favouriteApplicant;
            }

            public void setFavouriteApplicant(String favouriteApplicant) {
                this.favouriteApplicant = favouriteApplicant;
            }

            ArrayList<CandidateSkillResponseModel> skills;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getMobileNumber() {
                return mobileNumber;
            }

            public void setMobileNumber(String mobileNumber) {
                this.mobileNumber = mobileNumber;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getProfilePicThumbnail() {
                return profilePicThumbnail;
            }

            public void setProfilePicThumbnail(String profilePicThumbnail) {
                this.profilePicThumbnail = profilePicThumbnail;
            }

            public String getLocationString() {
                return locationString;
            }

            public void setLocationString(String locationString) {
                this.locationString = locationString;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public String getRateCard() {
                return rateCard;
            }

            public void setRateCard(String rateCard) {
                this.rateCard = rateCard;
            }

            public String getViews() {
                return views;
            }

            public void setViews(String views) {
                this.views = views;
            }

            public String getPost() {
                return post;
            }

            public void setPost(String post) {
                this.post = post;
            }

            public String getAppliedJobs() {
                return appliedJobs;
            }

            public void setAppliedJobs(String appliedJobs) {
                this.appliedJobs = appliedJobs;
            }

            public String getProfileSummary() {
                return profileSummary;
            }

            public void setProfileSummary(String profileSummary) {
                this.profileSummary = profileSummary;
            }

            public String getWebsite() {
                return website;
            }

            public void setWebsite(String website) {
                this.website = website;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getReviewCounts() {
                return reviewCounts;
            }

            public void setReviewCounts(String reviewCounts) {
                this.reviewCounts = reviewCounts;
            }

            public String getCharge() {
                return charge;
            }

            public void setCharge(String charge) {
                this.charge = charge;
            }

            public String getChargeType() {
                return chargeType;
            }

            public void setChargeType(String chargeType) {
                this.chargeType = chargeType;
            }

            public ArrayList<CandidateSkillResponseModel> getSkills() {
                return skills;
            }

            public void setSkills(ArrayList<CandidateSkillResponseModel> skills) {
                this.skills = skills;
            }

            public class CandidateSkillResponseModel {
                String category;
                boolean availibilityFullTime;
                boolean availabilityPartTime;
                ArrayList<String> subcategory;

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public boolean isAvailibilityFullTime() {
                    return availibilityFullTime;
                }

                public void setAvailibilityFullTime(boolean availibilityFullTime) {
                    this.availibilityFullTime = availibilityFullTime;
                }

                public boolean isAvailabilityPartTime() {
                    return availabilityPartTime;
                }

                public void setAvailabilityPartTime(boolean availabilityPartTime) {
                    this.availabilityPartTime = availabilityPartTime;
                }

                public ArrayList<String> getSubcategory() {
                    return subcategory;
                }

                public void setSubcategory(ArrayList<String> subcategory) {
                    this.subcategory = subcategory;
                }
            }


        }
    }
}
