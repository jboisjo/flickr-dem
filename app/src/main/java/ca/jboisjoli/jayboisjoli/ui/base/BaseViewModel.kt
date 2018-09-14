package ca.jboisjoli.jayboisjoli.ui.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

internal abstract class BaseViewModel<N : Any> : ViewModel() {

    lateinit var navigator: N
    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}