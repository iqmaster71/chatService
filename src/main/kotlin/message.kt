package ru.netology

import java.time.LocalDateTime

class Message(
    val messageId: Int,
    val messageRecipientId: Int,
    val messageSenderId: Int,
    val userNameSender: String,
    var text: String,
    val dateTime: LocalDateTime,

    var readabilityId: Boolean = false
) {
    override fun toString(): String {
        val readability = when {
            !readabilityId -> "!сообщение не прочитано!" + "\n "
            else -> ""
        }
        return "Сообщение от: $userNameSender" +
                "\n$dateTime" +
                "\n$text" +
                "\n " +
                "\n{Индекс сообщения - $messageId}" +
                "\n" + readability
    }
}