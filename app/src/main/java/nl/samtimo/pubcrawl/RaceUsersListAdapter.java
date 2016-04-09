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
public class RaceUsersListAdapter extends ArrayAdapter<User> {

    private final Activity context;
    private final ArrayList<User> items;

    private Race currentRace;

    public RaceUsersListAdapter(Activity context, ArrayList<User> items) {
        super(context, R.layout.list_item_user, items);

        this.context = context;
        this.items = items;
    }

    public void setRace(Race race){
        currentRace = race;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_user, null, true);

        User user = items.get(position);

        TextView txtLabel = (TextView) rowView.findViewById(R.id.item_label);
        txtLabel.setText(user.getName());

        if(currentRace!=null){
            String count = String.valueOf(user.getTagCount(currentRace));
            TextView txtCount = (TextView) rowView.findViewById(R.id.item_count);
            txtCount.setText(count);
        }

        return rowView;
    };
}
