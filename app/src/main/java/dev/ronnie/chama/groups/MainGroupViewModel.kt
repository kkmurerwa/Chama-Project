package  dev.ronnie.chama.groups

import android.view.View
import androidx.lifecycle.ViewModel

class MainGroupViewModel : ViewModel() {

    var listener: MainGroupListener? = null

    fun openActivities(view: View) {
        listener!!.openActivities(1)

    }

    fun openFinacials(view: View) {
        listener!!.openActivities(2)

    }

    fun openProjects(view: View) {
        listener!!.openActivities(3)

    }


}