package home.service.appmanage.online.adminapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.activities.BaseActivity
import home.service.appmanage.online.adminapp.models.Drivers
import home.service.appmanage.online.adminapp.utils.Constants.UPLOAD_DIRECTORY
import kotlinx.android.synthetic.main.worker_layout.view.*

class DriverAdapter(
    private val serviceList: ArrayList<Drivers>,
    private val context: Context
) : RecyclerView.Adapter<DriverAdapter.MyHolder>() {


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_layout, parent, false)
        return MyHolder(view!!)

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: MyHolder, p1: Int) {
        val service = serviceList[p1]
        p0.itemView.bookId.text = "Name: " + service.name
        p0.itemView.serviceType.text = "Driver Type: " + service.type
        p0.itemView.date.text = "Created At: " + service.createdAt

        Glide.with(context)
            .load(UPLOAD_DIRECTORY + service.profilePic)
            .into(p0.itemView.profileImage)
        if (service.isActivated) {
            p0.itemView.status.text = " Activated"
            p0.itemView.status.setTextColor(Color.GREEN)
        } else {
            p0.itemView.status.text = " Not Active"
            p0.itemView.status.setTextColor(Color.RED)
        }
        p0.itemView.detailsClickBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("position", p1)
            bundle.putParcelableArrayList("typed", serviceList)
            (context as BaseActivity).navigateFragment(R.id.viewDriverFragment, bundle)
        }
    }


}