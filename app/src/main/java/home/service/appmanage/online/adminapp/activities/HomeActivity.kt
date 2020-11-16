@file:Suppress("SameParameterValue", "LocalVariableName")

package home.service.appmanage.online.adminapp.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.utils.Constants.CHANGE_PASS_URL
import home.service.appmanage.online.adminapp.utils.RequestHandler
import home.service.appmanage.online.adminapp.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.change_pass_layout.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class HomeActivity : BaseActivity() {
    private var headerView: View? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private fun closeNavigationDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (!SharedPrefUtils.getBooleanData(this@HomeActivity, "isLoggedIn")) {
            openActivity(MainActivity())
        }
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.workerFragment,
                R.id.change_pass,
                R.id.logout
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        val logout = navigationView.menu.findItem(R.id.logout)
        val changePasword = navigationView.menu.findItem(R.id.change_pass)

        logout.setOnMenuItemClickListener {
            SharedPrefUtils.saveData(this@HomeActivity, "isLoggedIn", false)
            finish()
            openActivity(MainActivity())
            true
        }
        changePasword.setOnMenuItemClickListener {
            closeNavigationDrawer()
            changePasswordDialog()
            true
        }
        headerView = navigationView.getHeaderView(0)
        headerView!!.name.text = SharedPrefUtils.getStringData(this@HomeActivity, "name")
        headerView!!.email.text = SharedPrefUtils.getStringData(this@HomeActivity, "email")
    }

    private fun changePasswordDialog() {
        val factory = LayoutInflater.from(this)
        @SuppressLint("InflateParams") val deleteDialogView: View =
            factory.inflate(R.layout.change_pass_layout, null)
        val deleteDialog = if (Build.VERSION.SDK_INT > 23) {

            MaterialAlertDialogBuilder(this@HomeActivity).create()
        } else {
            AlertDialog.Builder(this@HomeActivity).create()
        }

        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        deleteDialog.setView(deleteDialogView)
        deleteDialog.setCancelable(false)
        deleteDialogView.updateBtn.setOnClickListener {
            if (deleteDialogView.pass.text!!.isEmpty() || deleteDialogView.oldPass.text!!.isEmpty()) {
                showToast(getString(R.string.please_fill_field))
            } else {
                changePasswordUrl(
                    deleteDialogView.pass.text.toString(),
                    deleteDialogView.oldPass.text.toString(),
                    CHANGE_PASS_URL, deleteDialog
                )

            }
        }
        deleteDialogView.cancelBtn.setOnClickListener {
            deleteDialog.dismiss()
        }


        deleteDialog.show()
        deleteDialog.window!!.decorView.setBackgroundResource(android.R.color.transparent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun changePasswordUrl(
        newPass: String,
        oldPass: String,
        url: String,
        deleteDialog: AlertDialog
    ) {
        showDialog(getString(R.string.updating))
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.POST,
                url,
                Response.Listener { response: String ->
                    try {
                        try {
                            val response_data = JSONObject(response)
                            if (response_data.getBoolean("error")) {
                                showToast(response_data.getString("message"))
                            } else {
                                showToast(response_data.getString("message"))
                                deleteDialog.dismiss()
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
                    params["newPass"] = newPass
                    params["oldPass"] = oldPass
                    params["userId"] =
                        SharedPrefUtils.getStringData(this@HomeActivity, "id").toString()
                    return params
                }
            }


        RequestHandler.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }


}