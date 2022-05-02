package ru.netology

class ChatNotFoundException(message: String) : RuntimeException(message)

fun main() {
    val service = ChatService
    val userOne = User(1, "Мальвина", mutableMapOf())
    val userTwo = User(2, "Клякса", mutableMapOf())
    val userTree = User(3, "Сармат", mutableMapOf())

    service.sendMessage(userTwo, userOne, "Привет! Как дела")
    service.sendMessage(userOne, userTwo, "Привет! Все нормуль")
    service.deleteChat(userTree, userTwo)
    service.sendMessage(userTree, userTwo, "Привет Сармат!")
    service.sendMessage(userTree, userOne, "Здорово Сармат!")

    service.sendMessage(userOne, userTree, "Привет, как жизнь?!")
    service.sendMessage(userTwo, userTree, "Привет Клякса! Как дела?")
    service.sendMessage(userTree, userTwo, "Сармат, все ОК!")
    service.sendMessage(userTree, userOne, "Неплохо, меня кстати зовут Мальвина")
    service.readingMessage(0, 0)
    service.readingMessage(1, 0)
    service.readingMessage(2, 1)
    service.readingMessage(6, 1)
    service.readingMessage(3, 2)

    println(service.chatStorage.values)
    println()
    println("Все чаты $userOne " + service.outputChats(userOne))
    println()
    println("Вывод списка сообщений чата в котором участвует $userTwo")
    println(service.getListOfMessages(userTwo, 1))

    service.editMessageInChat(userTwo, "Не, все плохо, Сармат", 0)
    println("Все чаты $userTwo " + service.outputChats(userTwo))
    println(service.getListOfMessages(userTwo, 0))

    service.deleteChat(userTree, userOne)
    println("Количество чатов с непрочитанными сообщениями: " + service.getUnreadChatsCount())

    println(service.getListOfChatsWithMessages(userTwo))

    service.deleteMessage(userOne, 1)
}