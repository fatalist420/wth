package ru.dprk.wth


data class UserInfo(
    val Wallet: String,                                     //номер яндекс кошелька
    val FirstConnection: Long = System.currentTimeMillis(), //дата регистрации
    val Total: Int = 0                                      //начальный счет пользователя
)

data class TaskInfo(
    val DateCreate: Long = System.currentTimeMillis(), //дата создания задания
    val Count: Int,                                    //к-во звонков, которое необходимо совершить
    val Telephone: String,                             //номер телефона для звонков
    val Job: String,                                   //Задание при звонке
    val Action: Boolean = false                        //размещение задания неактивного по умолчанию
)

data class FirebaseData (
    var userID: String="userID",
    var job: String="job"
)