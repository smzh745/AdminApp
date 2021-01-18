package home.service.appmanage.online.adminapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Drivers(
    val dId: String,
    val profilePic: String,
    val name: String,
    val age:String,
    val fName:String,
    val address:String,
    val email: String,
    val phoneNum: String,
    val type: String,
    val cnicNum: String,
    val cnicImage: String,
    val carColor:String,
    val carNumber:String,
    val carImage:String,
    val carEngineNum:String,
    val licNum:String,
    val licImage:String,
    val isActivated: Boolean,
    val createdAt: String, val token: String
) : Parcelable