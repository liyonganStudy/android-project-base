package com.example.landy.projectbase.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by landy on 16/10/16.
 */

public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results") List<GankBeauty> beauties;
}
