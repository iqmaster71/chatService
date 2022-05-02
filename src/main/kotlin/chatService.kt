package ru.netology

import java.time.LocalDateTime
import kotlin.io.println as println

object ChatService {
    var chatStorage: MutableMap<Int, Chat> = mutableMapOf()
    private var idChat: Int = 0
    private var idMessage: Int = 0
    private var timeMessageCall: LocalDateTime = LocalDateTime.now()


    //Создаем чат
    private fun createChat(
        titles: String,
        userTwo: User,
        userOne: User,
    ): Chat {
        val newChat = Chat(chatId = idChat, chatUsers = mutableListOf(), titles = titles, unreadMessages = 0)
        chatStorage[newChat.chatId] = newChat
        idChat++
        userTwo.memoryOfMyChats[newChat.chatId] = userOne.userId
        userOne.memoryOfMyChats[newChat.chatId] = userTwo.userId
        return newChat
    }

    fun sendMessage(
        userTwo: User,
        userOne: User,
        text: String
    ) {
        when {
            chatStorage.isNotEmpty() -> {
                when (userTwo.memoryOfMyChats.containsValue(userOne.userId)) {
                    true -> {
                        val number = userTwo.memoryOfMyChats.filterValues { it == userOne.userId }.keys
                        val chatId = chatStorage.keys.first { it == number.elementAt(0) }
                        createMessage(userTwo, userOne, text, chatId)
                    }
                    else -> {
                        println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
                        val chat = createChat(
                            "Чат между пользователями ${userTwo.userName} и ${userOne.userName}",
                            userTwo,
                            userOne
                        )
                        createMessage(userTwo, userOne, text, chat.chatId)
                    }
                }
            }
            else -> {
                println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
                val chat = createChat(
                    "Чат между пользователями ${userTwo.userName} и ${userOne.userName}",
                    userTwo,
                    userOne
                )
                createMessage(userTwo, userOne, text, chat.chatId)
            }
        }
    }

    private fun createMessage(
        userTwo: User,
        userOne: User,
        text: String,
        chatId: Int
    ) {
        val message = Message(
            messageId = idMessage,
            messageRecipientId = userTwo.userId,
            messageSenderId = userOne.userId,
            userNameSender = userOne.userName,
            text = text,
            dateTime = timeMessageCall,
            readabilityId = false
        )
        idMessage++
        chatStorage[chatId]!!.chatUsers.add(message)
        chatStorage[chatId]!!.unreadMessages++
    }

    fun readingMessage(messageId: Int, chatId: Int) {
        when (chatStorage[chatId]?.chatUsers?.first { messageId == it.messageId }?.readabilityId) {
            false -> {
                chatStorage[chatId]!!.chatUsers.first { messageId == it.messageId }.readabilityId = true
                println("Сообщение прочитано")
                chatStorage[chatId]!!.unreadMessages--
            }
            true -> println("Сообщение было прочитано")
            null -> println("Сообщения не существует")
        }
    }

    fun deleteChat(
        userTwo: User,
        userOne: User,
    ) {
        when {
            chatStorage.isNotEmpty() -> {
                when (userTwo.memoryOfMyChats.containsValue(userOne.userId)) {
                    true -> {
                        val number = userTwo.memoryOfMyChats.filterValues { it == userOne.userId }.keys
                        val chatId = chatStorage.keys.first { it == number.elementAt(0) }
                        chatStorage.remove(chatId)
                        println("Чат между пользователями ${userTwo.userName} и ${userOne.userName} удален")
                    }
                    else -> {
                        println("Чата между пользователями ${userTwo.userName} и ${userOne.userName} не существует")
                    }
                }
            }
            else -> {
                println("Чата между пользователями ${userTwo.userName} и ${userOne.userName} не существует")
            }
        }
    }

    fun outputChats(userOne: User): Collection<Chat> {
        when {
            chatStorage.isNotEmpty() -> {
                val number = userOne.memoryOfMyChats.keys
                return chatStorage.filter { number.contains(it.component1()) }.values
            }
            else -> {
                throw ChatNotFoundException("У пользователя ${userOne.userName} нет чатов")
            }
        }
    }

    fun getListOfMessages(userOne: User, idChat: Int): MutableList<Message> {
        val userOneChat = outputChats(userOne)
        val idChats = idChat
        return userOneChat.first { it.chatId == idChats }.chatUsers
    }

    fun editMessageInChat(userOne: User, text: String, id: Int) {
        val idMessages = 1
        when (chatStorage[id] != null) {
            true -> chatStorage[id]!!.chatUsers.first { it.messageId == idMessages }.text = text
            else -> throw ChatNotFoundException("У пользователя ${userOne.userName} таких сообщений нет")
        }
    }

    fun deleteMessage(userOne: User, chatId: Int): Boolean {
        return when (chatStorage[chatId]?.chatUsers?.removeAll { message -> message.messageRecipientId == userOne.userId || message.messageSenderId == userOne.userId }) {
            true -> {
                println("Сообщение удалено")
                true
            }
            false -> {
                println("Такого чата не существует")
                false
            }
            null -> {
                println("Такого чата не существует")
                false
            }
        }
    }

    fun getUnreadChatsCount(): Int {
        val unreadChat = chatStorage.values.filter { chat -> chat.unreadMessages != 0 }
        return unreadChat.size
    }

    fun getListOfChatsWithMessages(userOne: User): Collection<Chat> {
        val number = userOne.memoryOfMyChats.keys
        return chatStorage.filter { number.contains(it.component1()) }.values
    }
}