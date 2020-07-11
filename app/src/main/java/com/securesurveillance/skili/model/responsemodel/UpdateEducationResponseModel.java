package com.securesurveillance.skili.model.responsemodel;

import com.securesurveillance.skili.model.requestmodel.UpdateEducationRequestModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adarsh on 12/11/2018.
 */

public class UpdateEducationResponseModel implements Serializable{

    String message;
    boolean status;
    private ArrayList<EducationResponse> result;


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

    public ArrayList<EducationResponse> getResult() {
        return result;
    }

    public void setResult(ArrayList<EducationResponse> result) {
        this.result = result;
    }

    public class EducationResponse {
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


}
