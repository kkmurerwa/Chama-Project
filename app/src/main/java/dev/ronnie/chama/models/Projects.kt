package dev.ronnie.chama.models

import java.io.Serializable

data class Projects(
    var name: String? = null,
    var statues: String? = null,
    var leader: String? = null,
    var runTime: String? = null,
    var cost: String? = null
): Serializable {
}