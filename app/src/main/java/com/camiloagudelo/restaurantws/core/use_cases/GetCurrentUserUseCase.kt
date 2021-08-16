package com.camiloagudelo.restaurantws.core.use_cases

import android.content.SharedPreferences
import com.camiloagudelo.restaurantws.ui.login.CurrentUser

class GetCurrentUserUseCase {
    operator fun invoke(prefs: SharedPreferences): CurrentUser? {
        return CurrentUser.fromJson(
            prefs.getString("current_user", null)
        )
    }
}