package com.likhit.todolistapp.domain.utils

sealed class OrderType {
    data object Ascending: OrderType()
    data object Descending: OrderType()
}