package com.azazellj.githubwatcher.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by azazellj on 10/3/17.
 */

public class ApiError {
    @SerializedName("message")
    private String message;
    @SerializedName("documentation_url")
    private String documentationURL;

    public String getMessage() {
        return message;
    }
}
