package ru.alestrange.cultureSurgut

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import ru.alestrange.cultureSurgut.activities.*

class MainMenu {
    companion object{
        fun menuClickHandler(current: Activity, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.miInterest -> {
                    openActivity(current, InterestsActivity())
                    true
                }
                R.id.miHistory -> {
                    openActivity(current, HistoryActivity())
                    true
                }
                R.id.miHome -> {
                    openActivity(current, MainActivity())
                    true
                }
                R.id.miActive -> {
                    openActivity(current, SportActivity())
                    true
                }
                R.id.miNear -> {
                    openActivity(current, NearActivity())
                    true
                }
                R.id.miSearch -> {
                    openActivity(current, SearchActivity())
                    true
                }
                else -> true
            }
        }

        fun openActivity(current:Activity, next:Activity) {
            val randomIntent = Intent(current, next::class.java)
            randomIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            current.startActivityIfNeeded(randomIntent, 0)
        }

    }
}