package ca.jboisjoli.jayboisjoli.ui.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import ca.jboisjoli.jayboisjoli.R
import ca.jboisjoli.jayboisjoli.ext.setImageUrl
import ca.jboisjoli.jayboisjoli.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail.*
import timber.log.Timber

internal class DetailActivity : BaseActivity<DetailViewModel>() {

    override val activityClass = this@DetailActivity
    override val viewModelClass = DetailViewModel::class.java
    override val layoutId = R.layout.activity_detail

    private lateinit var photoId: String
    private lateinit var photoUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        photoId = intent.extras["id"].toString()
        photoUrl = intent.extras["photoUrl"].toString()

        imageView.setImageUrl(photoUrl)

        viewModel.init(photoId)

        setButton()
        getDetailInfo()
    }

    private fun setButton() {
        btnBackDetail.setOnClickListener {
            finish()
        }
    }

    //We get liveData list from viewModel.
    private fun getDetailInfo() = viewModel.observablePhotoDetail.observe(this,
            Observer { detail ->
                title = detail?.title

                lblTitle.text = title
                lblDescription.text = detail?.description
                lblUsername.text = "Taken by : ${detail?.username}"
            })
}