package nl.samtimo.pubcrawl;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PubsActivity extends FragmentColorActivity implements PubsListFragment.OnFragmentInteractionListener, PubsDetailFragment.OnFragmentInteractionListener {

    @Override
    protected void onStart(){
        super.onStart();
        findViewById(R.id.button_add).setVisibility(View.GONE);
        findViewById(R.id.layout_reviews).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs);

        Button addPubButton = (Button) findViewById(R.id.button_add);
        addPubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PubsDetailFragment detailFragment = (PubsDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_pubs_detail);
                detailFragment.addPub();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Pub pub) {
        PubsDetailFragment detailFragment = (PubsDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_pubs_detail);
        detailFragment.updateDetails(pub);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
