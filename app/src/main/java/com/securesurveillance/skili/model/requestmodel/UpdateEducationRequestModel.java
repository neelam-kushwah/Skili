package com.securesurveillance.skili.model.requestmodel;

import java.util.ArrayList;

/**
 * Created by adarsh on 12/11/2018.
 */

public class UpdateEducationRequestModel {
    private String _id;
    private ArrayList<Education> education;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<Education> getEducation() {
        return education;
    }

    public void setEducation(ArrayList<Education> education) {
        this.education = education;
    }

    public class Education{
        String institute;
        String course;
        String specialization;
        String passingOutYear;
        String enrollmentYear;
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInstitute() {
            return institute;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
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
