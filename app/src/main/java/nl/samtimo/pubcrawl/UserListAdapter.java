package nl.samtimo.pubcrawl;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 31-03-16.
 */
public class UserListAdapter extends ArrayAdapter<User> {

    private final Activity context;
    private final ArrayList<User> items;

    public UserListAdapter(Activity context, ArrayList<User> items) {
        super(context, R.layout.list_item_user, items);

        this.context = context;
        this.items = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_race, null, true);

        TextView txtLabel = (TextView) rowView.findViewById(R.id.item_label);
        txtLabel.setText(items.get(position).getName());

        return rowView;
    };
}
