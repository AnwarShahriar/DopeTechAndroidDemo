package me.anwarshahriar.rxjavademo.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Gist {
  @SerializedName("files") Map<String, File> files;

  public Map<String, File> getFiles() {
    return files;
  }
}