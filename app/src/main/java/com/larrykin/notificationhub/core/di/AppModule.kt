package com.larrykin.notificationhub.core.di

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.larrykin.notificationhub.core.data.database.AppDatabase
import com.larrykin.notificationhub.core.data.repository.AppRepository
import com.larrykin.notificationhub.core.data.repository.NotificationRepository
import com.larrykin.notificationhub.core.domain.repository.IAppRepository
import com.larrykin.notificationhub.core.domain.repository.INotificationRepository
import com.larrykin.notificationhub.core.presentation.viewModels.AnalyticsViewModel
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import com.larrykin.notificationhub.core.presentation.viewModels.ProfileViewModel
import com.larrykin.notificationhub.core.presentation.viewModels.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module that provides application-level dependencies such as:
 * - Room Database instance
 * - DAOs
 * - Repositories
 *
 * -it uses scopes :-
 *  -[single] - Singleton scope. Provides a single instance of the dependency across the app lifecycle.]
 *  - [factory] - Factory scope. Provides a new instance of the dependency every time it is requested.
 *  - [viewModel] - ViewModel scope. Provides a new instance of the dependency for each ViewModel.
 *
 * This module ensures all dependencies are created as singletons across the app lifecycle.
 */
val appModule = module {

    //? MIGRATIONS
    /**
     * Migrations for Room database.
     * */
    /*    val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                     db.execSQL("ALTER TABLE notification_profiles ADD COLUMN isEnabled INTEGER NOT NULL DEFAULT 1")
            }
        }*/


    //? DATABASE

    /**
     * Provides a singleton instance of [AppDatabase] using Room.
     *
     * @return AppDatabase instance configured with destructive migrations fallback.
     */
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(), // Application context
            AppDatabase::class.java,
            "notificationHub.db" // Database name
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("Database", "Database created")
            }
        })
            // .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration(true)
            .build()
    }


    //? DAOs

    /**
     * Provides a singleton instance of AppNotificationSettingsDao.
     *
     * @return DAO for accessing app notification settings.
     */
    single { get<AppDatabase>().appNotificationSettingsDao() }

    /**
     * Provides a singleton instance of NotificationHistoryDao.
     *
     * @return DAO for accessing notification history records.
     */
    single { get<AppDatabase>().notificationHistoryDao() }

    /**
     * Provides a singleton instance of ProfileDao.
     *
     * @return DAO for accessing notification profiles.
     */
single { get<AppDatabase>().profileDao() }

    //? REPOSITORIES

    /**
     * Provides a singleton implementation of [INotificationRepository].
     *
     * get() is used to inject the required dependencies that are already provided by Koin (are in the current scope).
     * @return Repository responsible for managing notification data operations.
     */
    single<INotificationRepository> { NotificationRepository(get(), get()) }

    /**
     * AppRepository: Provides a singleton instance of the repository for accessing app-wide data.
     * */
    single<IAppRepository> { AppRepository(get(), get()) }


    //? VIEW MODELS

    /**
     * - MainViewModel: Handles app-wide state and notification permissions
     */
    viewModel {
        MainViewModel(get())
    }

    /**
     * -ProfileViewModel: Manages notification profiles
     * */
    viewModel {
        ProfileViewModel(get(), get())
    }

    /**
     * -Analytics: Manages analytics data and reports
     * */
    viewModel {
        AnalyticsViewModel()
    }

    /**
     * -Settings: Maanages app settings
     * */
    viewModel {
        SettingsViewModel()
    }

}