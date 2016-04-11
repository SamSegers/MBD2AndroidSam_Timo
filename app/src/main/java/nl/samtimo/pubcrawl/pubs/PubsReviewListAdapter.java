package nl.samtimo.pubcrawl.pubs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.samtimo.pubcrawl.R;
import nl.samtimo.pubcrawl.Review;

/**
 * Created by admin on 07-04-16.
 */
public class PubsReviewListAdapter extends ArrayAdapter<Review>{
    private final Activity context;
    private final ArrayList<Review> items;

    public PubsReviewListAdapter(Activity context, ArrayList<Review> items) {
        super(context, R.layout.list_item_pub_review, items);

        this.context = context;
        this.items = items;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_pub_review, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.text_name);
        txtName.setText(items.get(position).getName());

        TextView txtBody = (TextView) rowView.findViewById(R.id.text_body);
        txtBody.setText(items.get(position).getBody());

        return rowView;
    };
}
