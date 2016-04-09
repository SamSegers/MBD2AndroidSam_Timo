package nl.samtimo.pubcrawl;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 31-03-16.
 */
public class RacesPubsListAdapter extends ArrayAdapter<Pub> {

    private final FragmentActivity context;
    private final ArrayList<Pub> items;

    private Race race;

    public RacesPubsListAdapter(FragmentActivity context, ArrayList<Pub> items) {
        super(context, R.layout.list_item_pub_selectable, items);

        this.context = context;
        this.items = items;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_pub_selectable, null, true);

        Pub pub = items.get(position);

        TextView txtLabel = (TextView) rowView.findViewById(R.id.item_label);
        txtLabel.setText(pub.getName());

        //TODO set race in class
        RacesDetailFragment racesDetailFragment = (RacesDetailFragment)context.getSupportFragmentManager().findFragmentById(R.id.fragment_races_detail);
        race = racesDetailFragment.getRace();

        if(race!=null){
            CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.item_checkbox);
            //System.out.println("race checkboxes: "+LoginActivity.user.isParticipating(race)+" "+!race.isCompleted());
            checkBox.setEnabled(LoginActivity.user.isParticipating(race) && race.isOngoing());

            ArrayList<Pub> tags = LoginActivity.user.getTags(race);
            if(tags!=null){
                for(int i=0;i<tags.size();i++){
                    if(tags.get(i).getId().equals(pub.getId())){
                        checkBox.setChecked(true);
                        break;
                    }
                }
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    RacesDetailFragment racesDetailFragment = (RacesDetailFragment) context.getSupportFragmentManager().findFragmentById(R.id.fragment_races_detail);
                    racesDetailFragment.updateTag(items.get(position), isChecked);
                }
            });
        }

        return rowView;
    };
}
