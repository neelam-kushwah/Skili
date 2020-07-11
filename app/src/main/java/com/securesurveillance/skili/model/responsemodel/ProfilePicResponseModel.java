package com.securesurveillance.skili.model.responsemodel;

/**
 * Created by adarsh on 8/4/2018.
 */

public class ProfilePicResponseModel {


    String message;
    boolean status;
    LoginResponse result;

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


    public LoginResponse getResult() {
        return result;
    }

    public void setResult(LoginResponse result) {
        this.result = result;
    }

    public class LoginResponse {

        long id;
        String bucket;
        String name;
        String generation;
        String mediaLink;
        String mediaLinkThumbnailed;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGeneration() {
            return generation;
        }

        public void setGeneration(String generation) {
            this.generation = generation;
        }

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
    }

}
