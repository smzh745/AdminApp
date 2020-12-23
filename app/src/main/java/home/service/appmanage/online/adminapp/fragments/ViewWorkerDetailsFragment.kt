package home.service.appmanage.online.adminapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.activities.PreviewActivity
import home.service.appmanage.online.adminapp.models.Workers
import home.service.appmanage.online.adminapp.utils.Constants.UPDATE_ACTIVE_URL
import home.service.appmanage.online.adminapp.utils.Constants.UPLOAD_DIRECTORY
import home.service.appmanage.online.adminapp.utils.RequestHandler
import kotlinx.android.synthetic.main.fragment_view_worker_details.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class ViewWorkerDetailsFragment : BaseFragment() {
    private var serviceList: ArrayList<Workers>? = null
    private var position: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            serviceList = requireArguments().getParcelableArrayList("typed")
            position = requireArguments().getInt("position")
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                serviceList!![position].name
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_worker_details, container, false)
//        try {
        serviceList = requireArguments().getParcelableArrayList("typed")
        position = requireArguments().getInt("position")
        Glide.with(requireActivity())
            .load(UPLOAD_DIRECTORY + serviceList!![position].profilePic)
            .into(root!!.imagePickProfile)
        Glide.with(requireActivity())
            .load(UPLOAD_DIRECTORY + serviceList!![position].cnicImage)
            .into(root!!.imagePick)
        root!!.name1.setText(serviceList!![position].name)
        root!!.email1.setText(serviceList!![position].email)
        root!!.number.setText(serviceList!![position].phoneNum)
        root!!.cnic_number.setText(serviceList!![position].cnicNum)

        root!!.imagePickProfile.setOnClickListener {
            openPreview(serviceList!![position].profilePic)
        }
        root!!.imagePick.setOnClickListener {
            openPreview(serviceList!![position].cnicImage)

        }
        /*  } catch (e: Exception) {
              e.printStackTrace()
          }*/
        setSpinner(R.array.activate_Array, root!!.workerType)
        root!!.register.setOnClickListener {
            updateData(serviceList?.get(position)!!)
        }
        return root
    }

    private fun openPreview(s: String) {
        val intent = Intent(requireContext(), PreviewActivity::class.java)
        intent.putExtra("path", s)
        startActivity(intent)
    }

    private fun updateData(workers: Workers) {
        val workerType: String = root!!.workerType.selectedItem.toString()
        var isActivate = false
        var status: String = ""
        if (workerType == "Choose") {
            showToast("Choose the Option")
        } else {
            if (workerType == "Activate") {
                isActivate = true
                status="activated"
            } else if (workerType == "Deactivate") {
                isActivate = false
                status="deactivate"

            }
            showDialog(getString(R.string.updating))
            val stringRequest: StringRequest =
                object : StringRequest(
                    Method.POST,
                    UPDATE_ACTIVE_URL,
                    Response.Listener { response: String ->
                        try {
                            try {
                                val response_data = JSONObject(response)
                                if (response_data.getString("status") == "1") {
                                    showToast(response_data.getString("data"))
                                } else {
                                    showToast(response_data.getString("data"))
                                }
                                hideDialog()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                hideDialog()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error: VolleyError ->
                        try {
                            hideDialog()
                            showToast(error.message!!)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                ) {
                    override fun getParams(): Map<String, String> {
                        val params: MutableMap<String, String> =
                            HashMap()
                        params["wid"] = workers.wId
                        params["isActivate"] = isActivate.toString()
                        params["token"] = workers.token
                        params["status"] = workerType

                        return params
                    }
                }


            RequestHandler.getInstance(requireContext()).addToRequestQueue(stringRequest)
        }
    }

    private fun setSpinner(array: Int, spinner: Spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            array, android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = adapter
    }
}