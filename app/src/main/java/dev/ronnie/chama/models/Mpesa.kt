package dev.ronnie.chama.models

import java.io.Serializable

data class Mpesa(
    var mpesa_account: String? = null,
    var mpesa_amount: String? = null,
    var mpesaId: String? = null
):Serializable {
}