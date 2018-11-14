package com.lmp.model

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmWrapper {

    private val realm = Realm.getInstance(getRealmConfiguration())

    companion object {
        fun init(context: Context) {
            Realm.init(context)
        }
    }

    fun getRealmConfiguration(): RealmConfiguration = RealmConfiguration.Builder().name("lastentries.realm").build()
}