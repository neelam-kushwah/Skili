package com.securesurveillance.skili.model.responsemodel;

import com.securesurveillance.skili.model.SkillModel;

import java.util.ArrayList;

/**
 * Created by adarsh on 8/4/2018.
 */

public class GetIndividualProfileModel {
    boolean status;
    IndividualResultResponse result;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public IndividualResultResponse getResult() {
        return result;
    }

    public void setResult(IndividualResultResponse result) {
        this.result = result;
    }

    public class IndividualResultResponse {
        String mobileNumber;
        String firstName;
        String lastName;
        String email;
        String locationString;
        Eductaion education;
        Experience experience;
        ArrayList<SkillModel.InnerSkillModel> subCategories;

        public ArrayList<SkillModel.InnerSkillModel> getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(ArrayList<SkillModel.InnerSkillModel> subCategories) {
            this.subCategories = subCategories;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLocationString() {
            return locationString;
        }

        public void setLocationString(String locationString) {
            this.locationString = locationString;
        }


        public Eductaion getEducation() {
            return education;
        }

        public Experience getExperience() {
            return experience;
        }

        public void setExperience(Experience experience) {
            this.experience = experience;
        }

        public void setEducation(Eductaion education) {
            this.education = education;
        }

        public class Experience {
            String profileTitle;
            String expInYears;
            String annualSalary;
            String noticePeriodInDays;

            public String getProfileTitle() {
                return profileTitle;
            }

            public void setProfileTitle(String profileTitle) {
                this.profileTitle = profileTitle;
            }

            public String getExpInYears() {
                return expInYears;
            }

            public void setExpInYears(String expInYears) {
                this.expInYears = expInYears;
            }



            public String getNoticePeriodInDays() {
                return noticePeriodInDays;
            }

            public void setNoticePeriodInDays(String noticePeriodInDays) {
                this.noticePeriodInDays = noticePeriodInDays;
            }

            public String getAnnualSalary() {
                return annualSalary;
            }

            public void setAnnualSalary(String annualSalary) {
                this.annualSalary = annualSalary;
            }
        }

        public class Eductaion {
            String course;
            String specialization;
            String institute;
            String passingOutYear;
            String enrollmentYear;

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
    }

}
