package com.larrykin.notificationhub.core.di

import com.larrykin.notificationhub.MainActivity
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module that provides activity-level dependencies.
 *
 * This module is used to define dependencies that are specific to the activity lifecycle.
 * The dependencies defined here are scoped to the activity and will be created and destroyed when the activity is created and destroyed.
 * It uses Koin's scope feature to create a new scope for the activity.
 */
val activityModule = module {
    scope<MainActivity> {
        scoped(qualifier = named("hello")) { "Hello" }
        scoped(qualifier = named("bye")) { "Bye" }
    }
}