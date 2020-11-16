package home.service.appmanage.online.adminapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Workers(
    val wId: String,
    val profilePic: String,
    val name: String,
    val email: String,
    val phoneNum: String,
    val type: String,
    val cnicNum: String,
    val cnicImage: String,
    val isActivated: Boolean,
    val createdAt: String
) : Parcelable