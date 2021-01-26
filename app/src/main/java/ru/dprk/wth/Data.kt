package ru.dprk.wth


data class UserInfo(
    val wallet: String,                                     //номер яндекс кошелька
    val FirstConnection: Long = System.currentTimeMillis(), //дата регистрации
    val Total: Int = 0                                      //начальный счет пользователя
)

data class TaskInfo(
    var taskID :String? =null,
    var userID: String? = null,
    var job: String? = null,
    var number: String? = null,
    var count: Int? = 0,
    var price: Int? = null,
    var action: Boolean? = false,
    var dateCreate: Long? = System.currentTimeMillis(),
    var progress: Int? = 0
)

data class UserAction(
    var userID: String? = null,
    val lastConnection: Long = System.currentTimeMillis()
)

data class TaskLog(
    var userID: String? = null,
    var timeAction:Long = System.currentTimeMillis()
)