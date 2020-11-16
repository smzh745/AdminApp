@file:Suppress("LocalVariableName")

package home.service.appmanage.online.adminapp.activities

import android.os.Bundle
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.utils.Constants.LOGIN_URL
import home.service.appmanage.online.adminapp.utils.RequestHandler
import home.service.appmanage.online.adminapp.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (SharedPrefUtils.getBooleanData(this@MainActivity, "isLoggedIn")) {
            openActivity(HomeActivity())
        }
        login.setOnClickListener {
            if (password.text.isNullOrEmpty() ||
                email.text.isNullOrEmpty()
            ) {
                showToast(getString(R.string.please_fill_field))
            } else {
                loginAdmin()
            }
        }
    }

    private fun loginAdmin() {
        showDialog(getString(R.string.authenticating_user))
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST,
                LOGIN_URL,
                Response.Listener { response: String ->
                    try {
                        try {
                            val response_data = JSONObject(response)
                            if (response_data.getString("status") == "1") {
                                val id: String =
                                    response_data.getJSONObject("data").getString("id")
                                val name: String =
                                    response_data.getJSONObject("data").getString("name")
                                SharedPrefUtils.saveData(this@MainActivity, "id", id)
                                SharedPrefUtils.saveData(this@MainActivity, "name", name)
                                SharedPrefUtils.saveData(
                                    this@MainActivity,
                                    "isLoggedIn",
                                    response_data.getJSONObject("data").getBoolean("isLoggedIn")
                                )

                                openActivity(MainActivity())
                                finish()
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
                    params["email"] = email.text.toString()
                    params["password"] = password.text.toString()
                    return params
                }
            }


        RequestHandler.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}