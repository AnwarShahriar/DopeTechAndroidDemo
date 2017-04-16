package me.anwarshahriar.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

public class File {
  @SerializedName("filename") String fileName;

  public String getFileName() {
    return fileName;
  }
}