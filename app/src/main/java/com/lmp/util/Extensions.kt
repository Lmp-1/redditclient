package com.lmp.util

import android.content.res.Resources
import android.os.Build
import android.support.annotation.LayoutRes
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lmp.redditclient.R
import java.util.concurrent.TimeUnit

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attach: Boolean = false) : View =
        LayoutInflater.from(context).inflate(layoutRes, this, attach)

fun Long.formatWithSuffix(): String {
    val value = this
    if (value < 1000) return "" + value
    val exp = (Math.log(value.toDouble()) / Math.log(1000.0)).toInt()
    return String.format("%.1f%c",
            value / Math.pow(1000.0, exp.toDouble()),
            "kMGTPE"[exp - 1])  // first letters of abbreviations which stand for
                                // kilo, mega, giga, tera, peta and exa
}

fun Long.formatMillisAsBiggestTimeUnit(resources: Resources): String {
    // This could be implemented via Calendar but it requires minimum API level of 28.
    // Implementation using TimeUnit does not require high API levels and can be imported
    // and reused in other projects.
    lateinit var currentTimeUnitName: String
    var currentTimeUnit: Long = -1
    val value = this

    when {
        value < 1000L -> {
            // If time is less than 1 second, we say 'moments'.
            // Yes, reddit gives time in seconds (which I didn't know and it brought me some
            // frustration and sadness while I was looking for a reason of seeing '17 days ago'
            // everywhere), so this branch will never be used in this app, but...
            // It's much more common to store time in millis, not in seconds. By adding this branch
            // I made this function more universal and reusable in other projects.
            return resources.getString(R.string.moments)
        }
        value < TimeUnit.MINUTES.toMillis(1) -> { // If time is less than 1 minute, we return seconds
            currentTimeUnit = TimeUnit.MILLISECONDS.toSeconds(value)
            currentTimeUnitName = resources.getQuantityString(R.plurals.seconds, currentTimeUnit.toInt())
        }
        value < TimeUnit.HOURS.toMillis(1) -> { // return minutes
            currentTimeUnit = TimeUnit.MILLISECONDS.toMinutes(value)
            currentTimeUnitName = resources.getQuantityString(R.plurals.minutes, currentTimeUnit.toInt())
        }
        value < TimeUnit.DAYS.toMillis(1) -> { // return hours
            currentTimeUnit = TimeUnit.MILLISECONDS.toHours(value)
            currentTimeUnitName = resources.getQuantityString(R.plurals.hours, currentTimeUnit.toInt())
        }
        else -> {
            // if time is more than 1 day, we need another workaround.
            // TimeUnit doesn't have values for month and year, so we need to count this by ourselves.
            // I've decided not to bother myself with leap years and months with 28 days because
            // we don't need such accurate precision.
            val days = TimeUnit.MILLISECONDS.toDays(value)

            when (days) {
                in 365L..Long.MAX_VALUE -> {
                    currentTimeUnit = days / 365
                    currentTimeUnitName = resources.getQuantityString(R.plurals.years, currentTimeUnit.toInt())
                }
                in 30L..364L -> {
                    // We can't simply divide by 30, because if there is 360..364 days, this will return
                    // '12 months'. But '12 months' should be one year and it's going to be not so beautiful.
                    currentTimeUnit = (days / 30.417f).toLong()
                    currentTimeUnitName = resources.getQuantityString(R.plurals.months, currentTimeUnit.toInt())
                }
                in 1L..29L -> {
                    currentTimeUnit = days
                    currentTimeUnitName = resources.getQuantityString(R.plurals.days, currentTimeUnit.toInt())
                }
            }
        }
    }

    return "$currentTimeUnit $currentTimeUnitName"
}

fun String.setBold(): String {
    return "<b>" + this + "</b>"
}

fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    }
    else {
        Html.fromHtml(this)
    }
}