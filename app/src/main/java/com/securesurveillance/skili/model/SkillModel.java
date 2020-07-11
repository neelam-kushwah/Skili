package com.securesurveillance.skili.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adarsh on 8/18/2018.
 */

public class SkillModel {


    private ArrayList<InnerSkillModel> subcategory;

    private String name;
    private String id;

    public ArrayList<InnerSkillModel> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<InnerSkillModel> subcategory) {
        this.subcategory = subcategory;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   public class InnerSkillModel implements Parcelable{
        private boolean enableLink;
        private boolean enableImage;
        private boolean enableVideo;
        private String name;
        private String id;

       public  final Creator CREATOR = new Creator() {
           public InnerSkillModel createFromParcel(Parcel in) {
               return new InnerSkillModel(in);
           }

           public InnerSkillModel[] newArray(int size) {
               return new InnerSkillModel[size];
           }
       };
       // Parcelling part
       public InnerSkillModel(Parcel in){
           this.id = in.readString();
           this.name = in.readString();
       }

       @Override
       public int describeContents() {
           return 0;
       }

       @Override
       public void writeToParcel(Parcel dest, int flags) {
           dest.writeString(this.id);
           dest.writeString(this.name);
       }
        public boolean isEnableLink() {
            return enableLink;
        }

        public void setEnableLink(boolean enableLink) {
            this.enableLink = enableLink;
        }

        public boolean isEnableImage() {
            return enableImage;
        }

        public void setEnableImage(boolean enableImage) {
            this.enableImage = enableImage;
        }

        public boolean isEnableVideo() {
            return enableVideo;
        }

        public void setEnableVideo(boolean enableVideo) {
            this.enableVideo = enableVideo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


   }
}
