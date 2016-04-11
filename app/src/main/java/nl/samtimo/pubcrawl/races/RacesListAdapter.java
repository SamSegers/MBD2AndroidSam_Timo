package nl.samtimo.pubcrawl.races;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.samtimo.pubcrawl.R;
import nl.samtimo.pubcrawl.Race;

/**
 * Created by admin on 31-03-16.
 */
public class RacesListAdapter extends ArrayAdapter<Race> {

    private final Activity context;
    private final ArrayList<Race> items;

    public RacesListAdapter(Activity context, ArrayList<Race> items) {
        super(context, R.layout.list_item_race, items);

        this.context = context;
        this.items = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        Race race = items.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_race, null, true);

        rowView.setBackgroundColor(
            race.isOngoing()?Color.parseColor("#FFFAAD"):
            race.isCompleted()?Color.GRAY:Color.parseColor("#FFFFF2")
        );

        TextView txtLabel = (TextView) rowView.findViewById(R.id.item_label);
        txtLabel.setText(race.getName());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_icon);
        imageView.setImageResource(R.drawable.placeholder);
        race.initImage(imageView);

        return rowView;
    };
}
