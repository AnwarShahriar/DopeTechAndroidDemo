package me.anwarshahriar.rxjavademo.model

import com.google.gson.annotations.SerializedName

class File {
  @SerializedName("filename") var fileName: String? = null
    internal set
}