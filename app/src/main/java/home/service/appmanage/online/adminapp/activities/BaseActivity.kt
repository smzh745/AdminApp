@file:Suppress("DEPRECATION")

package home.service.appmanage.online.adminapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.iid.FirebaseInstanceId
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.utils.Constants.TAGI
import home.service.appmanage.online.adminapp.utils.Constants.UPDATE_ADMIN_TOKEN_URL
import home.service.appmanage.online.adminapp.utils.RequestHandler
import home.service.appmanage.online.adminapp.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.layout_loading_dialog.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

open class BaseActivity : AppCompatActivity() {
    var queue: RequestQueue? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(this)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAGI, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token.toString()

                // Log and toast
                Log.d(TAGI, token)
                SharedPrefUtils.saveData(this, "deviceToken", token)
            })
        if (SharedPrefUtils.getBooleanData(this@BaseActivity, "isLoggedIn")) {
            updateToken()
        }
    }

    private fun updateToken() {
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST,
                UPDATE_ADMIN_TOKEN_URL,
                Response.Listener { response: String ->
                    try {
                        try {
                            val response_data = JSONObject(response)
                            if (response_data.getString("status") == "1") {
                                Log.d(TAGI, "updateToken: " + response_data.getString("data"))
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.d(TAGI, "updateToken: " + error.message)
                    try {
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["id"] = SharedPrefUtils.getStringData(this@BaseActivity, "id").toString()
                    params["token"] =
                        SharedPrefUtils.getStringData(this@BaseActivity, "deviceToken")
                            .toString()
                    return params
                }
            }


        RequestHandler.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

    //TODO: show dialog
    fun showDialog(message: String) {
        dialog = setProgressDialog(this, message)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun openActivity(activity: Activity) {
        val intent = Intent(this@BaseActivity, activity.javaClass)
        startActivity(intent)
    }

    //TODO:  navigate to new fragment
    fun navigateFragment(id: Int, bundle: Bundle) {
        findNavController(R.id.nav_host_fragment).navigate(id, bundle)

    }

    fun showToast(toast: String) {
        Toast.makeText(this@BaseActivity, toast, Toast.LENGTH_LONG).show()

    }

    //TODO: hide dialog
    fun hideDialog() {
        if (dialog?.isShowing!!) {
            dialog?.dismiss()
        }
    }

    @SuppressLint("InflateParams")
    private fun setProgressDialog(context: Context, message: String): AlertDialog {

        val builder = MaterialAlertDialogBuilder(
            context
        )
        builder.setCancelable(false)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.layout_loading_dialog, null)
        builder.setView(view)

        view.dialogText.text = message
        return builder.create()
    }

}