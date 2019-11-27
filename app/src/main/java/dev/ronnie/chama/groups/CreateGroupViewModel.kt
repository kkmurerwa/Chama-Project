package  dev.ronnie.chama.groups

import android.view.View
import androidx.lifecycle.ViewModel

class CreateGroupViewModel : ViewModel() {

    var name: String? = null
    var listener: CreateGroupListener? = null

    fun create(view: View) {

        val create = CreateGroup(this)
        create.creatingGroup()

    }

}