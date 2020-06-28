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
import com.frolfr.api.model.User

class PlayersAdapter(context: Context)
    : ArrayAdapter<User>(context, R.layout.layout_player_item, R.id.text_player_item_name), Filterable {

    var items: MutableList<User> = mutableListOf()
    val suggestions: MutableList<User> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user = getItem(position) ?: return convertView!!
        var view = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            view = layoutInflater.inflate(R.layout.layout_player_item, parent, false)
        }
        view!!.findViewById<TextView>(R.id.text_player_item_name).text = "${user.nameFirst} ${user.nameLast}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getFilter(): Filter {
        return playerFilter
    }

    private var playerFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence {
            val user = resultValue as User
            return "${user.nameFirst} ${user.nameLast}"
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
                val filterList: List<User> = results.values as ArrayList<User>
                clear()
                for (user in filterList) {
                    add(user)
                    notifyDataSetChanged()
                }
            }
        }
    }

}
