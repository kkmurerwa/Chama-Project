package dev.ronnie.chama.models



data class Activities(
    var deposits: Deposits? = null,
    var withDrawals: WithDrawals? = null,
    var tasks: Tasks? = null
) {
}