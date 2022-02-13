package ru.alestrange.cultureSurgut

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import kotlin.reflect.KClass

class MainMenu {
    companion object{
        fun menuClickHandler(current: Activity, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.miInterest -> {
                    openActivity(current,InterestsActivity())
                    true
                }
                R.id.miHistory -> {
                    openActivity(current,HistoryActivity())
                    true
                }
                R.id.miHome -> {
                    openActivity(current,MainActivity())
                    true
                }
                R.id.miActive -> {
                    openActivity(current,SportActivity())
                    true
                }
                R.id.miNear -> {
                    openActivity(current,NearActivity())
                    true
                }
                else -> true
            }
        }

        fun openActivity(current:Activity, next:Activity) {
            val randomIntent = Intent(current, next::class.java)
            current.startActivity(randomIntent)
        }

    }
}