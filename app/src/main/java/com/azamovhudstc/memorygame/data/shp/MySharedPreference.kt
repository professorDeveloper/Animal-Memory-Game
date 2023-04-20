package com.azamovhudstc.memorygame.data.shp

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MySharedPreference @Inject constructor(@ApplicationContext context: Context) {

    var firstEasy: Boolean
        get() = sharedPreferences.getBoolean("first_easy", false)
        set(firstEasy) {
            editor.putBoolean("first_easy", firstEasy).apply()
        }

    var level: String
        get() = sharedPreferences.getString("level", "4x4\neasy").toString()
        set(level) {
            editor.putString("level", level).apply()
        }

    var firstMedium: Boolean
        get() = sharedPreferences.getBoolean("first_medium", false)
        set(firstMedium) {
            editor.putBoolean("first_medium", firstMedium).apply()
        }

    var firstHard: Boolean
        get() = sharedPreferences.getBoolean("first_hard", false)
        set(firstHard) {
            editor.putBoolean("first_hard", firstHard).apply()
        }
    var levelEasy: Int
    get() = sharedPreferences.getInt("level_easy", 0)
    set(firstHard) {
        editor.putInt("level_easy", firstHard).apply()
    }
    var levelMedium: Int
        get() = sharedPreferences.getInt("level_medium", 0)
        set(firstHard) {
            editor.putInt("level_medium", firstHard).apply()
        }
    var levelHard: Int
        get() = sharedPreferences.getInt("level_hard", 0)
        set(firstHard) {
            editor.putInt("level_hard", firstHard).apply()
        }
    var music: Boolean
        get() = sharedPreferences.getBoolean("music", true)
        set(music) {
            editor.putBoolean("music", music).apply()
        }

    var vibration: Boolean
        get() = sharedPreferences.getBoolean("vibration", true)
        set(vibration) {
            editor.putBoolean("vibration", vibration).apply()
        }

    companion object {
        var mySharedPreference: MySharedPreference? = null
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor: SharedPreferences.Editor

        fun init(context: Context): MySharedPreference? {
            if (mySharedPreference == null) {
                mySharedPreference = MySharedPreference(context)
            }
            return mySharedPreference
        }

        fun getInstance() = mySharedPreference!!
    }

    init {
        sharedPreferences = context.getSharedPreferences("app_name", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
}
