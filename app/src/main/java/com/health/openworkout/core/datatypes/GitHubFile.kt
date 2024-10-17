package com.health.openworkout.core.datatypes

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName

@Keep
class GitHubFile {
    @JvmField
    @SerializedName("type")
    var type: String? = null

    @JvmField
    @SerializedName("size")
    var size: Long = 0

    @JvmField
    @SerializedName("name")
    var name: String? = null

    @JvmField
    @SerializedName("path")
    var path: String? = null

    @JvmField
    @SerializedName("sha")
    var sha: String? = null

    @JvmField
    @SerializedName("url")
    var url: String? = null

    @JvmField
    @SerializedName("git_url")
    var gitURL: String? = null

    @JvmField
    @SerializedName("html_url")
    var htmlURL: String? = null

    @JvmField
    @SerializedName("download_url")
    var downloadURL: String? = null
}
