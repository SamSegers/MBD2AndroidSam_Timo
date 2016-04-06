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

    public RacesPubsListAdapter(FragmentActivity context, ArrayList<Pub> items) {
        super(context, R.layout.list_item_pub_selectable, items);

        this.context = context;
        this.items = items;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_pub_selectable, null, true);

        TextView txtLabel = (TextView) rowView.findViewById(R.id.item_label);
        txtLabel.setText(items.get(position).getName());

        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.item_checkbox);
        RacesDetailFragment racesDetailFragment = (RacesDetailFragment)context.getSupportFragmentManager().findFragmentById(R.id.fragment_races_detail);
        ArrayList<Pub> tags = LoginActivity.user.getTags(racesDetailFragment.getRace());
        //if(tags!=null) System.out.println("tag count in adapter: "+tags.size());
        if(tags!=null){
            for(int i=0;i<tags.size();i++){
                if(tags.get(i).getId().equals(items.get(position).getId())){
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

        return rowView;
    };
}
