package cz.mendelu.xspacek6.vehiclemanager.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {

        private const val DATE_FORMAT_CS = "dd. MM. yyyy"
        private const val DATE_FORMAT_EN = "yyyy/MM/dd"

        fun getDateString(unixTime: Long): String{
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = unixTime

            val format: SimpleDateFormat
            if (LanguageUtils.isLanguageCzech()){
                format = SimpleDateFormat(DATE_FORMAT_CS, Locale.GERMAN)
            } else {
                format = SimpleDateFormat(DATE_FORMAT_EN, Locale.ENGLISH)

            }
            return format.format(calendar.getTime())
        }

        fun getUnixTime(year: Int, month: Int, day: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            return calendar.timeInMillis
        }

        fun getUnixTime(localDate: LocalDate): Long {
            val calendar = Calendar.getInstance()
            calendar.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)
            return calendar.timeInMillis
        }


        fun getDaysBetween(initialDate: Long, finalDate: Long): Long {
            val startDate  = Instant.ofEpochSecond(initialDate).atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate  = Instant.ofEpochSecond(finalDate).atZone(ZoneId.systemDefault()).toLocalDate()
            return (endDate.toEpochDay() - startDate.toEpochDay()) / 1000
        }

        fun getPercentageOfPastDays(initialDate: Long, finalDate: Long): Float {
            val currentDate = Date().time
            return calculatePercentageOfPastDays(initialDate = initialDate, finalDate = finalDate, currentDate = currentDate)
        }

        fun calculatePercentageOfPastDays(initialDate: Long, finalDate: Long, currentDate: Long): Float {
            return if (initialDate < finalDate) {
                if(currentDate >= initialDate) {
                    if (currentDate <= finalDate) {
                        (1.0f - ((currentDate.toFloat() - initialDate.toFloat()) / (finalDate.toFloat() - initialDate.toFloat())))
                    } else {
                        0.0f
                    }
                } else {
                    1.0f
                }
            } else {
                0.0f
            }
        }

        fun addMonthsToDate(date: Long, months: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            calendar.add(Calendar.MONTH, months)
            return calendar.timeInMillis
        }



    }
}