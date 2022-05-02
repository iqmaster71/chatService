package ru.netology

class User(val userId: Int, val userName: String, val memoryOfMyChats: MutableMap<Int, Int>) {
    override fun toString(): String = userName
}