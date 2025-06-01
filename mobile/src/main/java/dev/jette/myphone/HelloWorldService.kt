package dev.jette.myphone

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInstaller.SessionInfo
import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

/**
 * Entry point for the hello world app.
 *
 *
 * [CarAppService] is the main interface between the app and the car host. For more
 * details, see the [Android for
 * Cars Library developer guide](https://developer.android.com/training/cars/navigation).
 */
class HelloWorldService : CarAppService() {


    override fun onCreateSession(sessionInfo: androidx.car.app.SessionInfo): Session {
        return object : Session() {
            override fun onCreateScreen(intent: Intent): Screen {
                return HelloWorldScreen(carContext)
            }
        }
    }

    override fun createHostValidator(): HostValidator {
        return if ((applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) !== 0) {
            HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
        } else {
            HostValidator.Builder(applicationContext)
                .addAllowedHosts(androidx.car.app.R.array.hosts_allowlist_sample)
                .build()
        }
    }
}