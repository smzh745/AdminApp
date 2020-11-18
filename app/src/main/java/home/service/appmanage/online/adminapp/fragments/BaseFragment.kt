package home.service.appmanage.online.adminapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import home.service.appmanage.online.adminapp.R
import kotlinx.android.synthetic.main.layout_loading_dialog.view.*

open class BaseFragment : Fragment() {
    var root: View? = null
    private var dialog: AlertDialog? = null

    var queue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(requireActivity())


    }

    fun showToast(toast: String) {
        Toast.makeText(requireContext(), toast, Toast.LENGTH_LONG).show()

    }

    //TODO: show dialog
    fun showDialog(message: String) {
        dialog = setProgressDialog(requireActivity(), message)
        dialog!!.setCancelable(false)
        dialog!!.show()
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