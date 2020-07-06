package com.frolfr.ui.createRound

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.frolfr.R
import com.frolfr.api.model.User2

class UsersAdapter(context: Context)
    : ArrayAdapter<User2>(context, R.layout.layout_player_item, R.id.text_player_item_name), Filterable {

    var items: MutableList<User2> = mutableListOf()
    val suggestions: MutableList<User2> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user = getItem(position) ?: return convertView!!
        var view = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            view = layoutInflater.inflate(R.layout.layout_player_item, parent, false)
        }
        view!!.findViewById<TextView>(R.id.text_player_item_name).text = "${user.firstName} ${user.lastName}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getFilter(): Filter {
        return userFilter
    }

    private var userFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence {
            val user = resultValue as User2
            return "${user.firstName} ${user.lastName}"
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if (items.isEmpty() && count > 0) {
                for (i in 0 until count) {
                    items.add(getItem(i)!!)
                }
            }
            return if (constraint != null) {
                suggestions.clear()
                for (user in items) {
                    val name = convertResultToString(user).toString()
                    if (name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(user)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(
            constraint: CharSequence?,
            results: FilterResults
        ) {
            if (results != null && results.count > 0) {
                val filterList: List<User2> = results.values as ArrayList<User2>
                clear()
                for (user in filterList) {
                    add(user)
                    notifyDataSetChanged()
                }
            }
        }
    }

}
