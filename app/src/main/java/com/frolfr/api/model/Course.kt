package com.frolfr.api.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Course(
    val id: Int,
    val city: String,
    val state: String,
    val country: String,
    val name: String,
    val status: String,
    val location: String,
    @Json(name = "photo_ids") val photoIds: List<Int>,
    @Json(name = "last_played_at") val lastPlayedAt: String,
    @Json(name = "hole_ids") val holeIds: List<Int>,
    @Json(name = "scorecard_ids") val scorecardIds: List<Int>,
    @Json(name = "hole_count") val holeCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        emptyList(), // TODO("photoIds"),
        parcel.readString()!!,
        emptyList(), // TODO("holeIds"),
        emptyList(), // TODO("scorecardIds"),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(country)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(location)
        parcel.writeString(lastPlayedAt)
        parcel.writeInt(holeCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Course> {
        override fun createFromParcel(parcel: Parcel): Course {
            return Course(parcel)
        }

        override fun newArray(size: Int): Array<Course?> {
            return arrayOfNulls(size)
        }
    }
}