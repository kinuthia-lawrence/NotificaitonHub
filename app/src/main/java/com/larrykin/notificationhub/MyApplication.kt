package com.larrykin.notificationhub

import android.app.Application
import com.larrykin.notificationhub.core.di.appModule
import com.larrykin.notificationhub.core.di.activityModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate() //this will call the onCreate method of the Application class
        /**
         * initialize Koin with the Android context to make android dependencies injectable.
         * */
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(appModule, activityModule)
        }
    }
}