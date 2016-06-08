package com.frolfr.frolfrclient;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by wowens on 6/2/16.
 */
public class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("You clicked item " + position + " with id " + id);
        Toast.makeText(parent.getContext(), "You clicked item " + position + " with id " + id, Toast.LENGTH_SHORT);

        ((ListView) parent).setItemChecked(position, true);
    }
}
