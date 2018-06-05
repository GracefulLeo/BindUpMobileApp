package com.bindup.vcard.vcardapp.data.storage.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

@DatabaseTable(tableName = "LOGO")
//Entity for DB
public class Logo implements Serializable{

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String fid;
    @DatabaseField
    private String filename;

    //Base string
    @DatabaseField
    private String logo;

    public Logo() {
    }

    //Constructor for UI
    public Logo(String filename, Bitmap bitmap) {
        if (filename != null && filename.length() != 0) {
            this.filename = filename;
        } else {
            Long tsLong = System.currentTimeMillis()/1000;
            this.filename = "photo-" + tsLong.toString() + ".png";
        }

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            this.logo = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        }
    }

    //Constructor for DB
    public Logo(String fid, String filename, String logo) {
        this.fid = fid;
        this.filename = filename;
        this.logo = logo;
    }


    //region==================================Setters=============================

    public void setId(Long id) {
        this.id = id;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    //endregion===============================Setters=============================


    //region==================================Getters=============================

    public Long getId() {
        return id;
    }

    public String getFid() {
        return fid;
    }

    public String getFilename() {
        return filename;
    }

    public String getLogo() {
        return logo;
    }

    //endregion===============================Getters=============================

    public Bitmap getLogoBitmap() {
        byte[] decodedString = Base64.decode(logo, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

//    @Override
//    public String toString() {
//        return "Logo{" +
//                "id=" + id +
//                ", fid='" + fid + '\'' +
//                ", filename='" + filename + '\'' +
//                ", logo(is empty)='" + logo.isEmpty() + '\'' +
//                '}';
//    }
}
