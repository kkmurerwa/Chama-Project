package dev.ronnie.chama.models

import java.io.Serializable

data class Bank(
    var bank_account: String? = null,
    var bank_amount: String? = null,
    var bankId : String? = null
):Serializable {
}