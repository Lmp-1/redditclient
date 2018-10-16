package com.lmp.gui

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.lmp.redditclient.R

class EventManager(val mainView: View) {

    fun hideLoader() {
        val loader = mainView.findViewById<RelativeLayout>(R.id.main_loading_content)
        loader.visibility = View.GONE
    }

    fun showConnectionFailError() {
        val connFailView = mainView.findViewById<RelativeLayout>(R.id.main_connection_fail)
        connFailView.visibility = View.VISIBLE
    }

    fun showErrorWithMessage(message: String?) {
        val errorView = mainView.findViewById<RelativeLayout>(R.id.main_error_with_message)

        if (message != null) {
            val textView = errorView.findViewById<TextView>(R.id.main_error_message_text)
            textView.text = message
        }
        errorView.visibility = View.VISIBLE
    }
}