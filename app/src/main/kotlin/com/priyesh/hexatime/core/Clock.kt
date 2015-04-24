/*
 * Copyright 2015 Priyesh Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.priyesh.hexatime.core

import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import com.priyesh.hexatime.KEY_DIVIDER
import com.priyesh.hexatime.KEY_ENABLE_24_HOUR
import com.priyesh.hexatime.KEY_ENABLE_NUMBER_SIGN
import timber.log.Timber
import java.util.Calendar
import kotlin.properties.Delegates

public class Clock() : PreferenceDelegate {

    private var enable24Hour = false;
    private var enableNumberSign = true;
    private var dividerStyle = 0;

    private final val HOUR = Calendar.HOUR
    private final val HOUR_24 = Calendar.HOUR_OF_DAY
    private final val MINUTE = Calendar.MINUTE
    private final val SECOND = Calendar.SECOND

    private var calendar = Calendar.getInstance()
    private var paint = Paint()
    private var canvas: Canvas by Delegates.notNull()

    private fun hour() = if (enable24Hour) calendar.get(HOUR_24) else calendar.get(HOUR)
    private fun minute() = calendar.get(MINUTE)
    private fun second() = calendar.get(SECOND)

    private fun numberSign() = if (enableNumberSign) "#" else ""

    private fun divider() = when (dividerStyle) {
        0 -> ""
        1 -> "."
        2 -> ":"
        3 -> " "
        4 -> "|"
        5 -> "/"
        else -> ""
    }

    override fun onPreferenceChange(prefs: SharedPreferences, key: String) {
        when (key) {
            KEY_ENABLE_24_HOUR -> enable24Hour = prefs.getBoolean(key, false)
            KEY_ENABLE_NUMBER_SIGN -> enableNumberSign = prefs.getBoolean(key, true)
            KEY_DIVIDER -> dividerStyle = prefs.getString(key, "0").toInt()
        }
    }

    public fun getTime(): String {
        updateCalendar()
        return "${numberSign()}${hour()}${divider()}${minute()}${divider()}${second()}"
    }

    private fun updateCalendar() {
        calendar = Calendar.getInstance()
    }

    public fun getPaint(): Paint {
        return paint
    }

    public fun getX(): Float = (canvas.getWidth() / 2).toFloat()

    public fun getY(): Float = (canvas.getHeight() / 2) - (paint.descent() + paint.ascent()) / 2

    public fun updateCanvas(canvas: Canvas) {
        this.canvas = canvas
    }

}