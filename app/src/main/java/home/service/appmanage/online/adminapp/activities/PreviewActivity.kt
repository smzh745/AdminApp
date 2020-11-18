package home.service.appmanage.online.adminapp.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import home.service.appmanage.online.adminapp.R
import home.service.appmanage.online.adminapp.utils.Constants.UPLOAD_DIRECTORY
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        Glide.with(this).load(UPLOAD_DIRECTORY + intent.getStringExtra("path")).into(imageView)
    }

    override fun onBackPressed() {
        finish()
    }
}