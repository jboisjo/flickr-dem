package ca.jboisjoli.jayboisjoli.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ca.jboisjoli.jayboisjoli.repository.api.RequestAPI
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photo
import ca.jboisjoli.jayboisjoli.ui.base.BaseNavigator
import ca.jboisjoli.jayboisjoli.ui.base.BaseViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

internal class SearchViewModel : BaseViewModel<BaseNavigator>() {

    private var postPhotosList = MutableLiveData<MutableList<Photo>>()
    private var postMorePhotoList = MutableLiveData<MutableList<Photo>>()

    internal val observablePhotosMoreList
        get() = postMorePhotoList as LiveData<MutableList<Photo>>

    internal val observablePhotosList
        get() = postPhotosList as LiveData<MutableList<Photo>>

    fun init(page: Int, name: String) {
        getPhotos(page, name)
    }

    private fun getPhotos(page: Int, name: String) {
        doAsync {
            val result = RequestAPI().getPhotos(page, name)
            uiThread {
                postPhotosList.postValue(result.photo)
            }
        }
    }

    fun loadMore(page: Int, name: String) {
        doAsync {
            val result = RequestAPI().getPhotos(page, name)
            uiThread {
                postMorePhotoList.postValue(result.photo)
            }
        }
    }
}