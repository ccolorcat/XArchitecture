package cc.colorcat.login.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Author: cxx
 * Date: 2020-07-17
 * GitHub: https://github.com/ccolorcat
 */
public class Course {
    //    @JSONField(name = "id")
//    @JsonProperty("id")
    @SerializedName("id")
    private int id;
    //    @JSONField(name = "name")
//    @JsonProperty("name")
    @SerializedName("name")
    private String name;
    //    @JSONField(name = "picSmall")
//    @JsonProperty("picSmall")
    @SerializedName("picSmall")
    private String picSmall;
    //    @JSONField(name = "picBig")
//    @JsonProperty("picBig")
    @SerializedName("picBig")
    private String picBig;
    //    @JSONField(name = "description")
//    @JsonProperty("description")
    @SerializedName("description")
    private String description;
    //    @JSONField(name = "learner")
//    @JsonProperty("learner")
    @SerializedName("learner")
    private int learner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicSmall() {
        return picSmall;
    }

    public void setPicSmall(String picSmall) {
        this.picSmall = picSmall;
    }

    public String getPicBig() {
        return picBig;
    }

    public void setPicBig(String picBig) {
        this.picBig = picBig;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLearner() {
        return learner;
    }

    public void setLearner(int learner) {
        this.learner = learner;
    }

    @NonNull
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picSmall='" + picSmall + '\'' +
                ", picBig='" + picBig + '\'' +
                ", description='" + description + '\'' +
                ", learner=" + learner +
                '}';
    }
}
