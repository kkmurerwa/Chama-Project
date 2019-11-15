package dev.ronnie.chama.models

data class Cash(
    var bank: Bank? = null,
   var mpesa: Mpesa? = null
) {
}