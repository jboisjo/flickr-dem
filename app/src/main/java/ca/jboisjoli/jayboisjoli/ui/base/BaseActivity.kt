package ca.jboisjoli.jayboisjoli.ui.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

internal abstract class BaseActivity<V : BaseViewModel<*>> : AppCompatActivity() {

    val viewModel by lazy { ViewModelProviders.of(activityClass).get(viewModelClass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    internal open fun setupView() {
        setContentView(layoutId)
    }

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    protected abstract val layoutId: Int

    /**
     * @return view model class
     */
    protected abstract val viewModelClass: Class<V>

    /**
     * @return activity class
     */
    protected abstract val activityClass: AppCompatActivity

}



