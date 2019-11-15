package dev.ronnie.chama.models

import java.io.Serializable

data class WithDrawals(
    var account: String? = null,
    var amount: String? = null,
    var date: String? = null
) : Serializable{
}