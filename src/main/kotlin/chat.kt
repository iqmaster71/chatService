package ru.netology

class Chat(
    val chatId: Int,
    var chatUsers: MutableList<Message>,
    val titles: String,
    var unreadMessages: Int
) {
    override fun toString(): String  {
        return when {
            unreadMessages > 0 -> "$titles индекс чата: $chatId\n $chatUsers"
            else -> "Новых сообщений нет"
        }
    }
}