package ru.netology

import org.junit.Test
import org.junit.Assert.*

class MainKtTest {

    private val userOne = User(1, "Мальвина", mutableMapOf())
    private val userTwo = User(2, "Клякса", mutableMapOf())
    private val userTree = User(3, "Сармат", mutableMapOf())
    private val service = ChatService

    private fun nU(){
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
    }

    @Test
    fun sendMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.chatStorage.isEmpty())
    }

    @Test(expected = NoSuchElementException::class)
    fun sendMessage_try() {
        nU()
        val newChat = Chat(chatId = 0, chatUsers = mutableListOf(),titles = "Чат между пользователями ${userTree.userName} и ${userOne.userName}", unreadMessages = 0)
        service.chatStorage[newChat.chatId] = newChat
        userTree.memoryOfMyChats[userOne.userId] = newChat.chatId
        userOne.memoryOfMyChats[userTree.userId] = newChat.chatId
        service.sendMessage(userOne, userTree, "Проверка2")
        service.sendMessage(userTree, userOne, "Проверка3")
        assertTrue(userOne.memoryOfMyChats.getValue(userOne.memoryOfMyChats.keys.first { it == userTree.userId }) == userTree.memoryOfMyChats.getValue(userOne.memoryOfMyChats.keys.first { it == userOne.userId }))

    }

    @Test
    fun readingMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        assertFalse(service.chatStorage.getValue(chatId).chatUsers.first{ messageId == it.messageId }.readabilityId)
    }

    @Test(expected = NoSuchElementException::class)
    fun readingMessage_Null() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 3
        assertFalse(service.chatStorage.getValue(chatId).chatUsers.first{ messageId == it.messageId }.readabilityId)
    }

    @Test
    fun deleteChat() {
        nU()
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.deleteChat(userOne, userTwo)
        assertTrue(service.chatStorage.isEmpty())
    }

    @Test
    fun deleteChat_No_Users_isEmpty() {
        nU()
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.deleteChat(userOne, userTree)
        assertTrue(service.chatStorage.size == 1)
    }

    @Test
    fun outputChats() {
        nU()
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTree, "Проверка")
        service.outputChats(userOne)
        assertTrue(service.chatStorage.size == 2)
    }

    @Test
    fun getListOfMessages() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.getListOfMessages(userOne,0).isEmpty())
    }

    @Test(expected = Exception::class)
    fun getListOfMessages_No() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertTrue(service.getListOfMessages(userTree,0).isEmpty())
    }

    @Test
    fun editMessageInChat() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка4")
        service.editMessageInChat(userOne, "Хорошо", 0)
        assertTrue(service.chatStorage[0]!!.chatUsers.first { it.messageId == 1 }.text == "Хорошо")
    }

    @Test
    fun deleteMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertTrue(service.deleteMessage(userOne, 0))
    }

    @Test
    fun deleteMessage_No() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.deleteMessage(userOne, 1))
    }

    @Test
    fun deleteMessage_Null() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.deleteMessage(userTree, 1))
    }

    @Test
    fun getUnreadChatsCount() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка4")
        assertTrue(service.getUnreadChatsCount() == 1)
    }

    @Test
    fun getListOfChatsWithMessages() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка4")
        val chat = service.getListOfChatsWithMessages(userOne)
        assertNotNull(chat)
    }
}