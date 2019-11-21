package dev.ronnie.chama.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.groups.MainGroupActivity
import dev.ronnie.chama.models.*


class FireBaseData {

    var userList: MutableList<User> = arrayListOf()
    var displaUserList: MutableList<DisplayUsers> = arrayListOf()
    var dislplayUserListLiveData = MutableLiveData<MutableList<DisplayUsers>>()
    var userListLiveData = MutableLiveData<MutableList<User>>()
    var depositList: MutableList<Deposits> = arrayListOf()
    var withDrawalsList: MutableList<WithDrawals> = arrayListOf()
    var taskList: MutableList<Tasks> = arrayListOf()
    var investmentList: MutableList<Investment> = arrayListOf()
    var banklist: MutableList<Bank> = arrayListOf()
    var mpesaList: MutableList<Mpesa> = arrayListOf()
    var mpesaListLiveData = MutableLiveData<MutableList<Mpesa>>()
    var investmentListLivedata = MutableLiveData<MutableList<Investment>>()
    var bankListLiveData = MutableLiveData<MutableList<Bank>>()
    var projectList: MutableList<Projects> = arrayListOf()
    var projecttListLiveData = MutableLiveData<MutableList<Projects>>()
    var depositListLiveData = MutableLiveData<MutableList<Deposits>>()
    var withDrawalsListLiveData = MutableLiveData<MutableList<WithDrawals>>()
    var tasksListLiveData = MutableLiveData<MutableList<Tasks>>()

    fun getDeposit(group: Groups): LiveData<MutableList<Deposits>> {
        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("activities")
            .child("deposits")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                depositList.clear()

                for (singleSnapshot in data.children) {

                    Log.d(
                        "GetGroups", "onDataChange: Deposits found: "
                                + singleSnapshot.value
                    )
                    try {
                        if (singleSnapshot.exists()) {

                            val deposits = singleSnapshot.getValue(Deposits::class.java)


                            depositList.add(deposits!!)

                            depositListLiveData.value = depositList

                            for (l in depositList) {

                                Log.d(
                                    "Deposits",
                                    "onDataChange: DepositsActivity List: " + l.account + ", " + l.amount + ", " + l.date
                                )

                            }

                        }
                    } catch (e: NullPointerException) {
                        Log.e(
                            "GetGroups",
                            "onDataChange: NullPointerException: " + e.message.toString()
                        )
                    }
                }
            }

            override fun onCancelled(data: DatabaseError) {

            }


        })

        return depositListLiveData
    }

    fun getWithDrawals(group: Groups): LiveData<MutableList<WithDrawals>> {

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("activities")
            .child("withdrawals")
        reference.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(data: DataSnapshot) {
                withDrawalsList.clear()

                for (singleSnapshot in data.children) {

                    Log.d(
                        "GetGroups", "onDataChange: Withdrawals found: "
                                + singleSnapshot.value
                    )
                    try {
                        if (singleSnapshot.exists()) {

                            val withDrawals = singleSnapshot.getValue(WithDrawals::class.java)


                            withDrawalsList.add(withDrawals!!)

                            withDrawalsListLiveData.value = withDrawalsList

                            for (l in withDrawalsList) {

                                Log.d(
                                    "WithDarawals",
                                    "onDataChange: WithDaraw List: " + l.account + ", " + l.amount + ", " + l.date
                                )
                            }
                        }
                    } catch (e: NullPointerException) {
                        Log.e(
                            "GetGroups",
                            "onDataChange: NullPointerException: " + e.message.toString()
                        )
                    }
                }
            }

            override fun onCancelled(data: DatabaseError) {

            }


        })

        return withDrawalsListLiveData


    }

    fun getTasks(group: Groups): LiveData<MutableList<Tasks>> {

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("activities")
            .child("tasks")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                taskList.clear()

                for (singleSnapshot in data.children) {

                    Log.d(
                        "GetGroups", "onDataChange: tasks found: "
                                + singleSnapshot.value
                    )
                    try {
                        if (singleSnapshot.exists()) {

                            val tasks = singleSnapshot.getValue(Tasks::class.java)

                            taskList.add(tasks!!)
                            tasksListLiveData.value = taskList

                            for (l in taskList) {

                                Log.d(
                                    "Tasks",
                                    "onDataChange: Task list: " + l.action + ", " + l.taskName + ", " + l.date
                                )
                            }
                        }
                    } catch (e: NullPointerException) {
                        Log.e(
                            "GetGroups",
                            "onDataChange: NullPointerException: " + e.message.toString()
                        )
                    }
                }
            }

            override fun onCancelled(data: DatabaseError) {

            }


        })

        return tasksListLiveData

    }

    fun getProjects(group: Groups): LiveData<MutableList<Projects>> {
        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("projects")
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                projectList.clear()

                for (singleSnapshot in data.children) {

                    Log.d(
                        "GetGroups", "onDataChange: tasks found: "
                                + singleSnapshot.value
                    )
                    try {
                        if (singleSnapshot.exists()) {

                            val projects = singleSnapshot.getValue(Projects::class.java)


                            projectList.add(projects!!)

                            projecttListLiveData.value = projectList

                            for (l in projectList) {

                                Log.d(
                                    "Tasks",
                                    "onDataChange: Task list: " + l.name + ", " + l.leader + ", " + l.statues
                                )
                            }
                        }
                    } catch (e: NullPointerException) {
                        Log.e(
                            "GetGroups",
                            "onDataChange: NullPointerException: " + e.message.toString()
                        )
                    }
                }
            }

            override fun onCancelled(data: DatabaseError) {

            }


        })

        return projecttListLiveData


    }

    fun getUsers(group: Groups): LiveData<MutableList<User>> {
        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("users")
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                userList.clear()
                for (singleSnapshot in data.children) {

                    Log.d(
                        "GetGroups", "onDataChange: users found: "
                                + singleSnapshot.value
                    )
                    try {
                        if (singleSnapshot.exists()) {
                            val usersString =
                                singleSnapshot.getValue(GroupUsers::class.java)!!.userId

                            val reference1 = FirebaseDatabase.getInstance().reference

                            val query =
                                reference1.child("Users")
                                    .orderByKey()
                                    .equalTo(usersString)
                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    for (snapshot in dataSnapshot.children) {
                                        Log.d(
                                            "getUsers",
                                            "onDataChange: Found users: " + snapshot.getValue(
                                                User::class.java
                                            )
                                        )
                                        val user = snapshot.getValue(User::class.java)

                                        userList.add(user!!)

                                        userListLiveData.value = userList
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })


                        }
                    } catch (e: NullPointerException) {
                        Log.e(
                            "GetGroups",
                            "onDataChange: NullPointerException: " + e.message.toString()
                        )
                    }
                }
            }

            override fun onCancelled(data: DatabaseError) {

            }


        })

        return userListLiveData


    }

    fun getDisplayUsers(group: Groups): LiveData<MutableList<DisplayUsers>> {
        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                displaUserList.clear()

                for (singleSnapshot in data.children) {

                    if (singleSnapshot.exists()) {
                        val usersString =
                            singleSnapshot.getValue(GroupUsers::class.java)!!.userId
                        val dateJoined =
                            singleSnapshot.getValue(GroupUsers::class.java)!!.date_joined

                        val reference1 = FirebaseDatabase.getInstance().reference

                        val query =
                            reference1.child("Users")
                                .orderByKey()
                                .equalTo(usersString)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (snapshot in dataSnapshot.children) {
                                        val firstName: String? =
                                            snapshot.getValue(User::class.java)?.fname
                                        val surName: String? =
                                            snapshot.getValue(User::class.java)?.sname
                                        val profile: String? =
                                            snapshot.getValue(User::class.java)?.profile_image
                                        val phone: String? =
                                            snapshot.getValue(User::class.java)?.phone


                                        val name: String =
                                            if (!firstName.isNullOrEmpty() && !surName.isNullOrEmpty()) {
                                                "$firstName $surName"
                                            } else {
                                                firstName!!
                                            }

                                        val users =
                                            DisplayUsers(
                                                name,
                                                phone,
                                                "Member since $dateJoined",
                                                usersString,
                                                profile
                                            )
                                        displaUserList.add(users)
                                        dislplayUserListLiveData.value = displaUserList

                                    }


                                }
                            }
                        })


                    }
                }
            }

            override fun onCancelled(data: DatabaseError) {

            }


        })

        return dislplayUserListLiveData


    }

    fun getBank(group: Groups): LiveData<MutableList<Bank>> {
        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("bank")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                banklist.clear()
                for (singleSnapshot in dataSnapshot.children) {
                    Log.d(
                        "Data", "banks found: "
                                + singleSnapshot.value
                    )
                    if (singleSnapshot.exists()) {
                        val bank = singleSnapshot.getValue(Bank::class.java)
                        banklist.add(bank!!)

                        bankListLiveData.value = banklist
                    }
                }

            }

        })
        return bankListLiveData
    }

    fun getMpesa(group: Groups): LiveData<MutableList<Mpesa>> {

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("mpesa")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mpesaList.clear()
                for (singleSnapshot in dataSnapshot.children) {
                    Log.d(
                        "Data", "mpesa found: "
                                + singleSnapshot.value
                    )
                    if (singleSnapshot.exists()) {
                        val mpesa = singleSnapshot.getValue(Mpesa::class.java)
                        mpesaList.add(mpesa!!)

                        mpesaListLiveData.value = mpesaList
                    }
                }

            }

        })
        return mpesaListLiveData


    }

    fun getInvestment(group: Groups): LiveData<MutableList<Investment>> {

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("financials")
            .child("investments")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                investmentList.clear()

                for (singleSnapshot in dataSnapshot.children) {
                    Log.d(
                        "Data", "investment found: "
                                + singleSnapshot.value
                    )
                    if (singleSnapshot.exists()) {

                        val investment = singleSnapshot.getValue(Investment::class.java)
                        investmentList.add(investment!!)
                        investmentListLivedata.value = investmentList
                    }
                }

            }

        })

        return investmentListLivedata
    }

    fun getAdmin(group: Groups) {

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("creator_id")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(
                    "Data", "Admin found: "
                            + dataSnapshot.value
                )
                MainGroupActivity.adminCode = dataSnapshot.value.toString()
            }

        })
    }

    fun getJoinRequest(group: Groups): MutableLiveData<MutableList<User>> {
        val joinRequestList: MutableList<User> = arrayListOf()
        val joinRequestListLiveData = MutableLiveData<MutableList<User>>()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("groups")
            .child(group.group_id)
            .child("join_requests")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                joinRequestList.clear()

                if (dataSnapshot.exists()) {

                    for (singleSnapshot in dataSnapshot.children) {

                        val userId = singleSnapshot.getValue(JoinRequest::class.java)!!.user_id

                        val query2 =
                            reference.child("Users")
                                .orderByKey()
                                .equalTo(userId)
                        query2.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                for (snapshot in dataSnapshot.children) {

                                    val user = snapshot.getValue(User::class.java)
                                    joinRequestList.add(user!!)
                                    joinRequestListLiveData.value = joinRequestList

                                }
                            }

                        })

                    }
                }
            }

        })
        return joinRequestListLiveData

    }
}