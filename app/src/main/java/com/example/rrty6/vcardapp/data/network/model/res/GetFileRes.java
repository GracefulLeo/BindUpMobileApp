package com.example.rrty6.vcardapp.data.network.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFileRes {

    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("filemime")
    @Expose
    private String filemime;
    @SerializedName("filesize")
    @Expose
    private String filesize;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("uri_full")
    @Expose
    private String uriFull;
    @SerializedName("target_uri")
    @Expose
    private String targetUri;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("image_styles")
    @Expose
    private List<Object> imageStyles = null;

    public String getFilename() {
        return filename;
    }

    public String getFile() {
        return file;
    }
}
