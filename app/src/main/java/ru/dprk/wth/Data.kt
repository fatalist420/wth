package ru.dprk.wth


data class UserInfo(
    val Wallet: String,                                     //номер яндекс кошелька
    val FirstConnection: Long = System.currentTimeMillis(), //дата регистрации
    val Total: Int = 0                                      //начальный счет пользователя
)

data class TaskInfo (
    var userID: String? = null,
    var job: String? = null
)