package me.anwarshahriar.rxjavademo.model

import com.google.gson.annotations.SerializedName

class Gist {
  @SerializedName("files") var files: Map<String, File>? = null
    internal set
}