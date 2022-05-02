package ru.netology

import org.junit.Test
import org.junit.Assert.*

class MainKtTest {

    private val userOne = User(1, "Мальвина", mutableMapOf())
    private val userTwo = User(2, "Клякса", mutableMapOf())
    private val userTree = User(3, "Сармат", mutableMapOf())
    private val service = ChatService

    private fun nU() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
    }

    @Test
    fun createChat() {
        nU()
        service.createChat("Чат между пользователями ${userTree.userName} и ${userOne.userName}", userTree, userOne)
        assertFalse(service.chatStorage.isEmpty())
    }

    @Test
    fun sendMessage_chatStorage_isEmpty() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.chatStorage.isEmpty())
    }

    @Test
    fun sendMessage_chatStorage_not_isEmpty() {
        nU()
        service.sendMessage(userTwo, userTree, "Проверка1")
        service.sendMessage(userTwo, userTree, "Проверка2")
        assertTrue(service.chatStorage.size == 1)

    }

    @Test
    fun sendMessage_chatStorage_not_isEmpty_New_Chat() {
        nU()
        service.sendMessage(userTwo, userTree, "Проверка2")
        service.sendMessage(userTree, userOne, "Проверка3")
        assertTrue("Чат между пользователями ${userTree.userName} и ${userOne.userName}" == service.chatStorage[1]!!.titles)
    }

    @Test
    fun createMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertTrue(service.chatStorage[0]!!.chatUsers[0].text == "Проверка")
    }

    @Test
    fun readingMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        assertFalse(service.chatStorage[chatId]?.chatUsers?.first { messageId == it.messageId }?.readabilityId!!)
    }

    @Test
    fun readingMessage_Earlier() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        service.readingMessage(chatId, messageId)
        assertTrue(service.chatStorage[chatId]?.chatUsers?.first { messageId == it.messageId }?.readabilityId!!)
    }

    @Test
    fun readingMessage_Null() {
        service.chatStorage.clear()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        service.readingMessage(chatId, messageId)
        assertNull(service.chatStorage[chatId]?.chatUsers?.first { messageId == it.messageId }?.readabilityId)
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
    fun deleteChat_No_Users() {
        nU()
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

    @Test(expected = ChatNotFoundException::class)
    fun outputChats_Exception() {
        nU()
        service.outputChats(userOne)
    }

    @Test
    fun getListOfMessages() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.getListOfMessages(userOne, 0).isEmpty())
    }

    @Test
    fun editMessageInChat() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка4")
        service.editMessageInChat(userOne, "Хорошо", 0)
        assertTrue(service.chatStorage[0]!!.chatUsers.first { it.messageId == 1 }.text == "Хорошо")
    }

    @Test(expected = ChatNotFoundException::class)
    fun editMessageInChat_No() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка4")
        service.editMessageInChat(userOne, "Хорошо", 7)
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