package ru.netology

import java.time.LocalDateTime
import kotlin.io.println as println

object ChatService {
    var chatStorage: MutableMap<Int, Chat> = mutableMapOf()
    var idChat: Int = 0
    var idMessage: Int = 0
    private var timeMessageCall: LocalDateTime = LocalDateTime.now()

    fun sendMessage(userTwo: User,
                    userOne: User,
                    text: String)
    {

        try {
            val chatId = userTwo.memoryOfMyChats.filter { userTwo.memoryOfMyChats.containsKey(userOne.userId) }.getValue(userOne.userId)

            val message = Message(
                messageId = idMessage ,
                chatId = chatId,
                messageRecipientId = userTwo.userId,
                messageSenderId = userOne.userId,
                userNameSender = userOne.userName,
                text = text,
                dateTime = timeMessageCall,
                readabilityId = false )
            idMessage++
            chatStorage.getValue(chatId).chatUsers.add(message)
            chatStorage.getValue(chatId).unreadMessages++
            println("Сообщение отправлено")
        } catch (e: NoSuchElementException){
//            println("Чата между ними нет")
            println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
            val newChat = Chat(chatId = idChat, chatUsers = mutableListOf(),titles = "Чат между пользователями ${userTwo.userName} и ${userOne.userName}", unreadMessages = 0)
            chatStorage[newChat.chatId] = newChat

            userTwo.memoryOfMyChats[userOne.userId] = newChat.chatId
            userOne.memoryOfMyChats[userTwo.userId] = newChat.chatId
            val message = Message(
                messageId = idMessage ,
                chatId = idChat,
                messageRecipientId = userTwo.userId,
                messageSenderId = userOne.userId,
                userNameSender = userOne.userName,
                text = text,
                dateTime = timeMessageCall,
                readabilityId = false )
            chatStorage.getValue(idChat).chatUsers.add(message)
            chatStorage.getValue(idChat).unreadMessages++
            idMessage++
            idChat++
        }
    }

    fun readingMessage(messageId: Int, chatId: Int){
        try {
            chatStorage.filter { !chatStorage.getValue(chatId).chatUsers.first { messageId == it.messageId }
                .readabilityId }.getValue(chatId).chatUsers.first { messageId == it.messageId }.readabilityId = true
            println("Cообщение прочитано")
        } catch (e: NoSuchElementException){
            println("Cообщения не существует")
        }
    }

    //Удалить чаты пользователя
    fun deleteChat(userTwo: User,
                   userOne: User,
    ) {
        try {
            chatStorage.remove(userOne.memoryOfMyChats[userTwo.userId])
        } catch (e: NoSuchElementException){
            println("Чата между пользователями не существует")
        }
    }

    fun outputChats(userOne: User): Collection<Chat>{
        return  chatStorage.filter{ userOne.memoryOfMyChats.isNotEmpty() }.filter{ userOne.memoryOfMyChats.values.contains(it.component1()) }.values
    }

    fun getListOfMessages(userOne: User, idChat: Int): MutableList<Message> {
        //            (0..idChat).shuffled().last()
        try {
            return outputChats(userOne).first { it.chatId == idChat }.chatUsers
        } catch (e: Exception) {
            throw ChatNotFoundException("У пользователя ${userOne.userName} нет чатов")
        }
    }

    fun editMessageInChat(userOne: User, text: String, idChat: Int){
//            (0..idChat).shuffled().last()
        val idMessages = 1
//            (0..idMessage).shuffled().last()
        try {
            chatStorage.getValue(idChat).chatUsers.first { it.messageId == idMessages }.text = text
        } catch (e: NoSuchElementException){
            println("У пользователя ${userOne.userName} нет чатов")
        }
    }

    fun deleteMessage(userOne: User, chatId: Int): Boolean {
        return try {
            chatStorage.getValue(chatId).chatUsers.removeAll{ message -> message.messageRecipientId == userOne.userId || message.messageSenderId == userOne.userId }
            println("Сообщения удалены")
            true
        } catch (e: NoSuchElementException) {
            println("Такого чата не существует")
            false
        }
    }

    fun getUnreadChatsCount(): Int {
        return chatStorage.values.filter { chat -> chat.unreadMessages !=0 }.size
    }

    fun getListOfChatsWithMessages(userOne: User): Collection<Chat> {
        return chatStorage.filter { userOne.memoryOfMyChats.keys.contains(it.component1()) }.values
    }
}