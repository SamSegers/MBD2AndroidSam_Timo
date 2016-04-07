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
 * Created by admin on 07-04-16.
 */
public class PubReviewListAdapter extends ArrayAdapter<Review>{
    private final Activity context;
    private final ArrayList<Review> items;

    public PubReviewListAdapter(Activity context, ArrayList<Review> items) {
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
