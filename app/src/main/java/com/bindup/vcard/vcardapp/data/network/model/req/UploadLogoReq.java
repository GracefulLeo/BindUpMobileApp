package com.bindup.vcard.vcardapp.data.network.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadLogoReq {

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("filepath")
    @Expose
    private String filepath;

    @SerializedName("file")
    @Expose
    private String file;

    public UploadLogoReq(String filename, Filepath filepath, String file) {
        this.filename = filename;
        switch (filepath) {
            case VCARDS:
                this.filepath = "public://vcards/" + filename;
                break;
            case GROUPS:
                this.filepath = "public://groups/" + filename;
                break;

        }
        this.file = file;
    }

    public enum Filepath {
        VCARDS,
        GROUPS
    }
}
