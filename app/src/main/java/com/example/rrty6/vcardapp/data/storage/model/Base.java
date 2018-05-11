package com.example.rrty6.vcardapp.data.storage.model;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

@DatabaseTable(tableName = "BASE")
//Entity for DB
public class Base implements Serializable{

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String base64 = null;

    public Base() {
    }

    public Base(String base64) {
        if (base64 != null && !base64.isEmpty()) {
            this.base64 = base64;
        }
    }


    public Base(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            this.base64 = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        }
    }

    //region==================================Setters=============================

    public void setId(Long id) {
        this.id = id;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    //endregion===============================Setters=============================


    //region==================================Getters=============================

    public Long getId() {
        return id;
    }

    public String getBase64() {
        return base64;
    }

    //endregion===============================Getters=============================

}
