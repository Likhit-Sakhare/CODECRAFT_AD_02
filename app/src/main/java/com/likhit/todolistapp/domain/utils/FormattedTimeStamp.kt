package com.likhit.todolistapp.domain.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formattedTimeStamp(timeStamp: Long): String{
    val sdf = SimpleDateFormat(
        "dd/MM/yyyy hh:mm a",
        Locale.getDefault()
    )
    return sdf.format(Date(timeStamp))
}