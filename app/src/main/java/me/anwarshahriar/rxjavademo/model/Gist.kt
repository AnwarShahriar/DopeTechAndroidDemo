package me.anwarshahriar.rxjavademo.model

import com.google.gson.annotations.SerializedName

class Gist {
  @SerializedName("html_url") var htmlUrl: String? = null
    internal set
  @SerializedName("files") var files: Map<String, File>? = null
    internal set
}