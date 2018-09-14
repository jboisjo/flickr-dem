package ca.jboisjoli.jayboisjoli.ui.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import ca.jboisjoli.jayboisjoli.R
import ca.jboisjoli.jayboisjoli.presenter.PhotoAdapter
import ca.jboisjoli.jayboisjoli.repository.model.entity.Photo
import ca.jboisjoli.jayboisjoli.ui.base.BaseActivity
import ca.jboisjoli.jayboisjoli.utils.InfiniteScrollListener
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.toast
import timber.log.Timber


internal class SearchActivity : BaseActivity<SearchViewModel>() {

    override val activityClass = this@SearchActivity
    override val viewModelClass = SearchViewModel::class.java
    override val layoutId = R.layout.activity_search

    private var titleSearch = "Search"
    private var searchQuery = ""
    private var page = 1

    private lateinit var photoSearchAdapter: PhotoAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var photoSearchList: MutableList<Photo>


    private var suggestionList: Array<String?> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(searchToolbar)

        title = titleSearch

        searchButton()
        setupObserver()
        setupRecyclerView()
        loadMore()

    }

    fun add(originalArray: Array<String?>, newItem: String): Array<String?> {
        val currentSize = originalArray.size
        val newSize = currentSize + 1
        val tempArray = arrayOfNulls<String>(newSize)
        for (i in 0 until currentSize) {
            tempArray[i] = originalArray[i]
        }
        tempArray[newSize - 1] = newItem
        return tempArray
    }

    private fun setupRecyclerView() {
        mLayoutManager = GridLayoutManager(this, 3)
        pullToLoadViewSearch.layoutManager = mLayoutManager as RecyclerView.LayoutManager?

        pullToLoadViewSearch.addOnScrollListener(object: InfiniteScrollListener() {
            override fun onLoadMore() {
                viewModel.loadMore(page++, searchQuery)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.material_search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    //We get liveData list from viewModel to load more elements (InfiniteLoading).
    private fun loadMore() = viewModel.observablePhotosMoreList.observe(this,
            Observer { photos ->
                photos?.forEach { photoSearchList.add(it) }
                photoSearchAdapter.notifyDataSetChanged()
            })

    //We get liveData list from viewModel.
    private fun setupObserver() = viewModel.observablePhotosList.observe(this,
            Observer { photos ->
                photos?.let { photo ->
                    photoSearchList = photo
                    photoSearchAdapter = PhotoAdapter(baseContext, photoSearchList) {
                    } }

                page++
                pullToLoadViewSearch.adapter = photoSearchAdapter
            })

    private fun searchButton() {

        btnBackDetail.setOnClickListener {
            onBackPressed()
        }

        search_view.setSuggestions(suggestionList)
        search_view.setEllipsize(true)
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchQuery = it }
                viewModel.init(page, searchQuery)

                if (!suggestionList.contains(searchQuery)) {
                    suggestionList = add(suggestionList, searchQuery)
                }

                search_view.setSuggestions(suggestionList)

                title = searchQuery

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                title = ""
            }

            override fun onSearchViewClosed() {
                if (title.isBlank()) title = titleSearch
            }
        })

    }

}