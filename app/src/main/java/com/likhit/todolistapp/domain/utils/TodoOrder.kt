package com.likhit.todolistapp.domain.utils

sealed class TodoOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): TodoOrder(orderType)
    class Date(orderType: OrderType): TodoOrder(orderType)
    class Color(orderType: OrderType): TodoOrder(orderType)

    fun copy(orderType: OrderType): TodoOrder{
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}