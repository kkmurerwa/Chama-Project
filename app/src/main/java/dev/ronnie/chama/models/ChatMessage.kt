package dev.ronnie.chama.models


data class ChatMessage(

    var message: String? = null,
    var user_id: String? = null,
    var timestamp: String? = null,
    var profile_image: String? = null,
    var name: String? = null
)
