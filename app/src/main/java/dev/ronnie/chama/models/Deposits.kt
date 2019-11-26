package dev.ronnie.chama.models

import java.io.Serializable
import java.util.*

data class Deposits(
    var account: String? = null,
    var amount: String? = null,
    var date: String? = null,
    var message: String? = null
) : Serializable {
}