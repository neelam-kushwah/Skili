package com.securesurveillance.skili.apiHandler;

/**
 * Created by adarsh on 8/4/2018.
 */

public interface AppConstants {

    interface Urls {
      //  String BASE_URL = "http://34.93.221.56:8089/";//prod
        String BASE_URL = "http://13.233.129.62:8080/";//test
        String LOGIN_URL = "login/accountkit";
        String SKILLI_SIGNUP_URL = "skili/profile/register";
        String INDIVIDUAL_URL = "skili/individual/register";
        String RECRUITER_COMAPNY_URL = "skili/recruiter/register";
        String GET_CATEGORY_URL = "skili/rest/master/category/all";
        String INDIVIDUAL_EDUCATION_UPDATE = "skili/individual/update/education";
        String INDIVIDUAL_EXPERIENCE_UPDATE = "skili/individual/update/experience";
        String RECRUITER_UPDATE = "skili/recruiter/update";
        String POST_JOB = "skili/job/post";
        String GET_ALL_JOB_RECRUITER = "skili/job/getJobs";
        String GET_APPLICANT_LIST = "skili/job/getApplicants";
        String GET_FAVOURITE_APPLICANT_LIST = "skili/profile/favorite/APPLICANT";
        String GET_FAVOURITE_RECRUITER_LIST = "skili/profile/favorite/RECRUITER";

        String GET_FAVOURITE_JOB_LIST = "skili/profile/favorite/JOB";
        String DELETE_FAVOURITE_APPLICANT = "skili/profile/favorite-delete";
        String UPLOAD_PROFILE_PIC = "skili/profile/upload-profile-pic";
        String UPDATE_PROFILE_PIC = "skili/profile/update/profilepic";
        String GET_ALL_DETAILS_PROFILE = "skili/profile/alldetails";
        String UPLOAD_POST = "skili/profile/post";
        String UPDATE_EDUCATION = "skili/profile/update/education";
        String UPDATE_EXPERIENCE = "skili/profile/update/experience";
        String DELETE_EDUCATION = "skili/profile/education";
        String DELETE_EXPERIENCE = "skili/profile/experience";
        String UPDATE_BASIC_DETAILS = "skili/profile/update/personalInfo";
        String GLOBALFEED_DEFAULT = "skili/job/search";
        String CANDIDATE_SEARCH_DEFAULT = "skili/profile/search";
        String JOB_STATUS_UPDATE = "skili/job/status";
        String JOB_EDIT = "skili/job/edit";
        String ADD_FAV_APPLICANT = "skili/profile/favorite/APPLICANT";
        String ACCEPT_REJECT_APPLICANT = "skili/job/actionOnApplicant";
        String GET_APPLIED_JOB_LIST = "skili/job/getJobs/applied";
        String APPLY_JOB = "skili/job/apply";
        String ADD_FAV_JOB = "skili/profile/favorite/JOB";
        String ADD_FAV_RECRUITER = "skili/profile/favorite/RECRUITER";
        String UPDATE_PROFILE_SKILLS = "skili/profile/update/skills";
        String PROFILE_GET_SKILLS = "skili/profile/skills";
        String SUBMIT_FEEDBACK = "skili/profile/feedback";
        String DEACTIVATE_ACCOUNT = "skili/profile/deactivate";
        String GET_SETTING="skili/profile/setting";
        String FETCH_NOTIFICATION="skili/profile/notifications";
        String FILTER_PROFILE="skili/profile/filterdata/PROFILE";
        String FILTER_JOB="skili/profile/filterdata/JOB";
        String RATING_INDI="skili/job/rating";
        String UpdateFCMToken="skili/profile/fcmToken";
        String SEND_MESSAGE="skili/chat/sendmsg";
        String CONVERSATION_LIST_ONETOONE="skili/chat/conversation";
        String CONVERSATION_LIST="skili/chat/list";
        String UPDATE_SOCIAL_MEDIA_URL="skili/profile/update/soical";
        String GET_JOB_DETAIL="skili/job/details";
        String GET_ALL_RATING="skili/profile/allratingandfeeback";
        String DELETE_POST="skili/profile/post";
    }

    interface MPermission {
        int M_READ_SMS = 1;
        int M_SEND_SMS = 2;
      //  int M_CAMERA = 3;
        int M_READ_EXTERNAL_STORAGE = 4;
        int M_READ_PHONE_STATE = 5;
    }

}
