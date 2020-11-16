package home.service.appmanage.online.adminapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import home.service.appmanage.online.adminapp.R
import kotlinx.android.synthetic.main.layout_loading_dialog.view.*

open class BaseActivity : AppCompatActivity() {
    var queue: RequestQueue? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(this)

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