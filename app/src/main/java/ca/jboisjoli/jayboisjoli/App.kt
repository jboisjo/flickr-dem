package ca.jboisjoli.jayboisjoli

import android.app.Application
import timber.log.Timber

internal class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Debugging tools
        if (BuildConfig.DEBUG) {

            // Timber initialization
            Timber.uprootAll()
            Timber.plant(Timber.DebugTree())
        }
    }
}