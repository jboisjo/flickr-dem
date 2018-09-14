package ca.jboisjoli.jayboisjoli.ui.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ca.jboisjoli.jayboisjoli.repository.api.RequestAPI
import ca.jboisjoli.jayboisjoli.repository.model.entity.Detail
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photo
import ca.jboisjoli.jayboisjoli.ui.base.BaseNavigator
import ca.jboisjoli.jayboisjoli.ui.base.BaseViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber

internal class DetailViewModel : BaseViewModel<BaseNavigator>() {

    private var postPhotoDetail = MutableLiveData<Detail>()

    internal val observablePhotoDetail
        get() = postPhotoDetail as LiveData<Detail>

    fun init(id: String) {
        getDetailPhoto(id)
    }

    private fun getDetailPhoto(id: String) {
        doAsync {
            val result = RequestAPI().getPhotoDetail(id)
            uiThread {
                postPhotoDetail.postValue(result)
            }
        }
    }
}