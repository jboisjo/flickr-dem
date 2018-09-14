package ca.jboisjoli.jayboisjoli.ui.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import ca.jboisjoli.jayboisjoli.R
import ca.jboisjoli.jayboisjoli.presenter.PhotoAdapter
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photo
import ca.jboisjoli.jayboisjoli.ui.base.BaseActivity
import ca.jboisjoli.jayboisjoli.ui.search.SearchActivity
import ca.jboisjoli.jayboisjoli.utils.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor


internal class MainActivity : BaseActivity<MainViewModel>() {

    override val activityClass = this@MainActivity
    override val viewModelClass = MainViewModel::class.java
    override val layoutId = R.layout.activity_main

    private var page = 1
    private val mainString = "popular"

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var photoList: MutableList<Photo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(page, mainString)

        setupRecyclerView()
        setupPhotoList()
        setupSearchButton()
        loadMore()
    }

    private fun setupRecyclerView() {
        mLayoutManager = GridLayoutManager(this, 3)
        pullToLoadViewMain.layoutManager = mLayoutManager as RecyclerView.LayoutManager?

        pullToLoadViewMain.addOnScrollListener(object: InfiniteScrollListener() {
            override fun onLoadMore() {
                viewModel.loadMore(page++, mainString)
            }
        })
    }

    //We get liveData list from viewModel to load more elements (InfiniteLoading).
    private fun loadMore() = viewModel.observablePhotosMoreList.observe(this,
            Observer { photos ->
                photos?.forEach { photoList.add(it) }
                photoAdapter.notifyDataSetChanged()
            })

    //We get liveData list from viewModel.
    private fun setupPhotoList() = viewModel.observablePhotosList.observe(this,
            Observer { photos ->
                photos?.let { photo ->
                    photoList = photo
                    photoAdapter = PhotoAdapter(baseContext, photoList) {
                } }

                page++
                pullToLoadViewMain.adapter = photoAdapter
            })

    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            startActivity(intentFor<SearchActivity>())
        }

    }
}
