package nl.samtimo.pubcrawl;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 31-03-16.
 */
public class MyRacesPubsListAdapter extends ArrayAdapter<Pub> {

    private final FragmentActivity context;
    private final ArrayList<Pub> items;

    public MyRacesPubsListAdapter(FragmentActivity context, ArrayList<Pub> items) {
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
        MyRacesDetailFragment myRacesDetailFragment = (MyRacesDetailFragment)context.getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_detail);
        ArrayList<Pub> waypoints = myRacesDetailFragment.getWaypoints();
        if(waypoints!=null){
            for(int i=0;i<waypoints.size();i++){
                if(waypoints.get(i).getId().equals(items.get(position).getId())){
                    checkBox.setChecked(true);
                    break;
                }
            }
            //checkBox.setChecked(waypoints.contains(items.get(position)));
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyRacesDetailFragment myRacesDetailFragment = (MyRacesDetailFragment)context.getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_detail);
                myRacesDetailFragment.updatePub(items.get(position), isChecked);
            }
        });

        return rowView;
    };

    public void testChecked(){
        for(int i=0;i<items.size();i++){
            //items.get(i)
            /*CheckBox checkBox = (CheckBox) context.findViewById(R.id.item_checkbox);
            MyRacesDetailFragment myRacesDetailFragment = (MyRacesDetailFragment)context.getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_detail);
            ArrayList<Pub> waypoints = myRacesDetailFragment.getWaypoints();
            checkBox.setChecked(waypoints.contains(items.get(i)));
            System.out.println("checkbox: "+checkBox.isChecked());*/
        }
        notifyDataSetChanged();
    }
}
