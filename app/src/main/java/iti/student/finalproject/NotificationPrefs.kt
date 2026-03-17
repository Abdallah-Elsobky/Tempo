package iti.student.finalproject

import android.content.Context
import android.preference.PreferenceManager

object NotificationPrefs {
    private const val PREF_NOTIFICATIONS_ENABLED = "pref_notifications_enabled"
    private const val PREF_SOUND_ALERTS_ENABLED = "pref_sound_alerts_enabled"

    fun areNotificationsEnabled(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return prefs.getBoolean(PREF_NOTIFICATIONS_ENABLED, true)
    }

    fun isSoundAlertsEnabled(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return prefs.getBoolean(PREF_SOUND_ALERTS_ENABLED, true)
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        prefs.edit().putBoolean(PREF_NOTIFICATIONS_ENABLED, enabled).apply()
    }

    fun setSoundAlertsEnabled(context: Context, enabled: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        prefs.edit().putBoolean(PREF_SOUND_ALERTS_ENABLED, enabled).apply()
    }
}

