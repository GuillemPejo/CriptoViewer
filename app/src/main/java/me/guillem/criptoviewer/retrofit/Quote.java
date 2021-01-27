package me.guillem.criptoviewer.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * * Created by Guillem on 27/01/21.
 */
public class Quote implements Serializable
{

    @SerializedName("USD")
    @Expose
    private USD uSD;
    private final static long serialVersionUID = -5780538494495942860L;

    public USD getUSD() {
        return uSD;
    }

    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }

}