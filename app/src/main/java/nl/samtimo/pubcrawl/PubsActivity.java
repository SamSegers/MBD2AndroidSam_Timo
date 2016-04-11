package nl.samtimo.pubcrawl;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PubsActivity extends ColorFragmentActivity implements PubsListFragment.OnFragmentInteractionListener, PubsDetailFragment.OnFragmentInteractionListener {

    private PubsDetailFragment pubsDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs);

        pubsDetailFragment = (PubsDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_pubs_detail);

        findViewById(R.id.button_pub_update).setVisibility(View.GONE);
        findViewById(R.id.layout_reviews).setVisibility(View.GONE);

        Button addPubButton = (Button) findViewById(R.id.button_pub_update);
        addPubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubsDetailFragment.updateStatus();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onListFragmentInteraction(Pub pub) {
        pubsDetailFragment.updateDetails(pub);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
