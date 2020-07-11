package com.securesurveillance.skili.model.responsemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 12/9/2018.
 */

public class GetAllProfileDetailsModel {
    boolean status;
    String message;
    Result result;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Experience implements Serializable {

        int id;
        String employerName;
        String profileName;
        String jobStartDate;
        String jobEndDate;
        String jobDescription;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmployerName() {
            return employerName;
        }

        public void setEmployerName(String employerName) {
            this.employerName = employerName;
        }

        public String getProfileName() {
            return profileName;
        }

        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }

        public String getJobStartDate() {
            return jobStartDate;
        }

        public void setJobStartDate(String jobStartDate) {
            this.jobStartDate = jobStartDate;
        }

        public String getJobEndDate() {
            return jobEndDate;
        }

        public void setJobEndDate(String jobEndDate) {
            this.jobEndDate = jobEndDate;
        }

        public String getJobDescription() {
            return jobDescription;
        }

        public void setJobDescription(String jobDescription) {
            this.jobDescription = jobDescription;
        }
    }

    public class Education implements Serializable {

        int id;
        String course;
        String specialization;
        String institute;
        String passingOutYear;
        String enrollmentYear;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getInstitute() {
            return institute;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
        }

        public String getPassingOutYear() {
            return passingOutYear;
        }

        public void setPassingOutYear(String passingOutYear) {
            this.passingOutYear = passingOutYear;
        }

        public String getEnrollmentYear() {
            return enrollmentYear;
        }

        public void setEnrollmentYear(String enrollmentYear) {
            this.enrollmentYear = enrollmentYear;
        }
    }

    public class Result {
        String appliedJobs;
        List<Education> education;
        List<Experience> experience;
        String mobileNumber;
        String firstName;
        String lastName;
        String locationString;
        String latitude;
        String longitude;
        ProfilePic profilePic;
        ArrayList<Post> post;
        private String dob;
        private String email;
        private String website;
        private String city;
        private String rating;
        private String rateCard;
        private String reviewCounts;
        private String charge;
        private String chargeType;
        private boolean displayMobile;
        private String companyName;
        private String gstin;
        private String facebookLink;
        private String instagramLink;
        private String linkedInLink;

        public String getFacebookLink() {
            return facebookLink;
        }

        public void setFacebookLink(String facebookLink) {
            this.facebookLink = facebookLink;
        }

        public String getInstagramLink() {
            return instagramLink;
        }

        public void setInstagramLink(String instagramLink) {
            this.instagramLink = instagramLink;
        }

        public String getLinkedInLink() {
            return linkedInLink;
        }

        public void setLinkedInLink(String linkedInLink) {
            this.linkedInLink = linkedInLink;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getGstin() {
            return gstin;
        }

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public boolean isDisplayMobile() {
            return displayMobile;
        }

        public void setDisplayMobile(boolean displayMobile) {
            this.displayMobile = displayMobile;
        }

        public class Post {

            String postId;
            Media media;
            String postedOn;

            public String getPostId() {
                return postId;
            }

            public void setPostId(String postId) {
                this.postId = postId;
            }

            public Media getMedia() {
                return media;
            }

            public void setMedia(Media media) {
                this.media = media;
            }

            public String getPostedOn() {
                return postedOn;
            }

            public void setPostedOn(String postedOn) {
                this.postedOn = postedOn;
            }

            public class Media {
                String mediaLink;
                String mediaLinkThumbnailed;
                long id;

                public String getMediaLink() {
                    return mediaLink;
                }

                public void setMediaLink(String mediaLink) {
                    this.mediaLink = mediaLink;
                }

                public String getMediaLinkThumbnailed() {
                    return mediaLinkThumbnailed;
                }

                public void setMediaLinkThumbnailed(String mediaLinkThumbnailed) {
                    this.mediaLinkThumbnailed = mediaLinkThumbnailed;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }
            }
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

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAppliedJobs() {
            return appliedJobs;
        }

        public void setAppliedJobs(String appliedJobs) {
            this.appliedJobs = appliedJobs;
        }

        public List<Education> getEducation() {
            return education;
        }

        public void setEducation(List<Education> education) {
            this.education = education;
        }

        public List<Experience> getExperience() {
            return experience;
        }

        public void setExperience(List<Experience> experience) {
            this.experience = experience;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
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

        public String getLocationString() {
            return locationString;
        }

        public void setLocationString(String locationString) {
            this.locationString = locationString;
        }

        public ProfilePic getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(ProfilePic profilePic) {
            this.profilePic = profilePic;
        }

        public ArrayList<Post> getPost() {
            return post;
        }

        public void setPost(ArrayList<Post> post) {
            this.post = post;
        }

        List<ProfileRatingFeedbackDto> ratingFeedbacks;

        public List<ProfileRatingFeedbackDto> getRatingFeedbacks() {
            return ratingFeedbacks;
        }

        public void setRatingFeedbacks(List<ProfileRatingFeedbackDto> ratingFeedbacks) {
            this.ratingFeedbacks = ratingFeedbacks;
        }


    }

    public class ProfileRatingFeedbackDto implements Serializable {

        String star;
        String description;
        RatingBy ratingBy;

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public RatingBy getRatingBy() {
            return ratingBy;
        }

        public void setRatingBy(RatingBy ratingBy) {
            this.ratingBy = ratingBy;
        }

       public  class RatingBy implements Serializable {
            String name;
            String profilePic;
            String rating;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfilePic() {
                return profilePic;
            }

            public void setProfilePic(String profilePic) {
                this.profilePic = profilePic;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }
        }
    }


    public class ProfilePic {
        String mediaLink;

        public String getMediaLink() {
            return mediaLink;
        }

        public void setMediaLink(String mediaLink) {
            this.mediaLink = mediaLink;
        }
    }

}
