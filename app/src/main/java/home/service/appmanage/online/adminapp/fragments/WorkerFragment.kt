package home.service.appmanage.online.adminapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.adapters.WorkerAdapter
import home.service.appmanage.online.adminapp.models.Workers
import home.service.appmanage.online.adminapp.utils.Constants.FETCH_WORKERS
import home.service.appmanage.online.adminapp.utils.Constants.TAGI
import home.service.appmanage.online.adminapp.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_worker.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class WorkerFragment : BaseFragment() {

    private var bookList: ArrayList<Workers>? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    requireActivity().finishAffinity()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_worker, container, false)
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
            Method.POST, FETCH_WORKERS,
            Response.Listener<String?> { response ->
                // response
                Log.d(TAGI, response.toString())
                val jsonObjects = JSONObject(response.toString())

                if (jsonObjects.getInt("status") == 1) {
                    Log.d(TAGI, "ok status")

                    val jsonArray =
                        JSONArray(jsonObjects.getString("data"))
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        bookList!!.add(
                            Workers(
                                jsonObject.getString("wid"),
                                jsonObject.getString("profilepic"),
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("phonenum"),
                                jsonObject.getString("type"),
                                jsonObject.getString("cnicnumber"),
                                jsonObject.getString("cnicnumberimage"),
                                jsonObject.getBoolean("isActivated"),
                                jsonObject.getString("created_at")
                            )
                        )

                    }
                    val adapter = WorkerAdapter(bookList!!,requireActivity())
                    root!!.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    root!!.recyclerView.adapter = adapter
                    checkEmptyView()

                } else if (jsonObjects.getInt("status") == 0) {
                    Log.d(TAGI, "intiData: " + jsonObjects.getString("data"))
                    checkEmptyView()

                }
                hideDialog()
            },
            Response.ErrorListener { error -> // error
                Log.d(TAGI, "error: " + error!!.message)
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