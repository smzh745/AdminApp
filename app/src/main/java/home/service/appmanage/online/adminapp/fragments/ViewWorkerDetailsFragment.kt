package home.service.appmanage.online.adminapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.activities.PreviewActivity
import home.service.appmanage.online.adminapp.models.Workers
import home.service.appmanage.online.adminapp.utils.Constants.UPLOAD_DIRECTORY
import kotlinx.android.synthetic.main.fragment_view_worker_details.view.*
import java.util.*


class ViewWorkerDetailsFragment : BaseFragment() {
    private var serviceList: ArrayList<Workers>? = null
    private var position: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceList = requireArguments().getParcelableArrayList("typed")
        position = requireArguments().getInt("position")
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            serviceList!![position].name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_worker_details, container, false)
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
        setSpinner(R.array.activate_Array, root!!.workerType)
        root!!.register.setOnClickListener {
            updateData()
        }
        root!!.imageLayoutProfile.setOnClickListener {
            openPreview(serviceList!![position].profilePic)
        }
        root!!.imageLayout.setOnClickListener {
            openPreview(serviceList!![position].cnicImage)

        }
        return root
    }

    private fun openPreview(s: String) {
        val intent = Intent(requireContext(), PreviewActivity::class.java)
        intent.putExtra("path", s)
        startActivity(intent)
    }

    private fun updateData() {
        val workerType: String = root!!.workerType.selectedItem.toString()
        if (workerType == "Choose") {
            showToast("Choose the Option")
        } else {
        }
    }

    fun setSpinner(array: Int, spinner: Spinner) {
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