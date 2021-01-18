package home.service.appmanage.online.adminapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.adapters.DriverAdapter
import home.service.appmanage.online.adminapp.models.Drivers
import home.service.appmanage.online.adminapp.models.Workers
import home.service.appmanage.online.adminapp.utils.Constants
import home.service.appmanage.online.adminapp.utils.Constants.FETCH_DRIVERS
import home.service.appmanage.online.adminapp.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_all_driver_fragments.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class AllDriverFragments : BaseFragment() {
    private var bookList: ArrayList<Drivers>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_driver_fragments, container, false)
        bookList = ArrayList()

        return root
    }

    override fun onResume() {
        super.onResume()
        bookList!!.clear()
        intiData()
    }

    private fun intiData() {
        showDialog(getString(R.string.loading))
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, FETCH_DRIVERS,
            Response.Listener<String?> { response ->
                // response
                Log.d(Constants.TAGI, response.toString())
                val jsonObjects = JSONObject(response.toString())

                if (jsonObjects.getInt("status") == 1) {
                    Log.d(Constants.TAGI, "ok status")

                    val jsonArray =
                        JSONArray(jsonObjects.getString("data"))
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        bookList!!.add(
                            Drivers(
                                jsonObject.getString("did"),
                                jsonObject.getString("profilepic"),
                                jsonObject.getString("dname"),
                                jsonObject.getString("dage"),
                                jsonObject.getString("father_name"),
                                jsonObject.getString("address"),
                                jsonObject.getString("email"),
                                jsonObject.getString("phone_num"),
                                jsonObject.getString("type"),
                                jsonObject.getString("cnic_no"),
                                jsonObject.getString("cnic_image"),
                                jsonObject.getString("car_color"),
                                jsonObject.getString("car_no"),
                                jsonObject.getString("car_copy_image"),
                                jsonObject.getString("car_engine_no"),
                                jsonObject.getString("lic_no"),
                                jsonObject.getString("lic_image"),
                                jsonObject.getBoolean("isActivated"),
                                jsonObject.getString("created_at"),
                                jsonObject.getString("token")
                            )
                        )

                    }
                    val adapter = DriverAdapter(bookList!!,requireActivity())
                    root!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    root!!.recyclerView.adapter = adapter
                    checkEmptyView()

                } else if (jsonObjects.getInt("status") == 0) {
                    Log.d(Constants.TAGI, "intiData: " + jsonObjects.getString("data"))
                    checkEmptyView()

                }
                hideDialog()
            },
            Response.ErrorListener { error -> // error
                Log.d(Constants.TAGI, "error: " + error!!.message)
                hideDialog()
                checkEmptyView()

            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> =
                    HashMap()

                params["uid"] =
                    SharedPrefUtils.getStringData(requireActivity(), "id").toString()
                return params
            }
        }
        queue!!.add(postRequest)
    }

    private fun checkEmptyView() {
        if (bookList!!.isEmpty()) {
            root!!.recyclerView.visibility = View.GONE
            root!!.emptyView.visibility = View.VISIBLE
        } else {
            root!!.recyclerView.visibility = View.VISIBLE
            root!!.emptyView.visibility = View.GONE
        }
    }

}