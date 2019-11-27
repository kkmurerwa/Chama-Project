package dev.ronnie.chama.models

import java.io.Serializable

data class Tasks(
    var taskName: String? = null,
    var action: String? = null,
    var date: String? = null,
    var taskId: String? = null
): Serializable {
}