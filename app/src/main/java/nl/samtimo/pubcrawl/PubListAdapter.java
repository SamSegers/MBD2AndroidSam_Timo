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
public class PubListAdapter extends ArrayAdapter<Pub> {

    private final Activity context;
    private final ArrayList<Pub> items;

    public PubListAdapter(Activity context, ArrayList<Pub> items) {
        super(context, R.layout.list_item_pub, items);

        this.context = context;
        this.items = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_race, null, true);

        TextView txtLabel = (TextView) rowView.findViewById(R.id.item_label);
        txtLabel.setText(items.get(position).getName());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_icon);
        items.get(position).initImage(imageView);

        return rowView;
    };
}
