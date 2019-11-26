package  dev.ronnie.chama.groups
import dev.ronnie.chama.models.Groups

interface GroupListener {

    fun navigate(groups: Groups)
    fun toast(message: String)
    fun notifyDatasetChanged()

}