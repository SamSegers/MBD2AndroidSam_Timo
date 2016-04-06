package nl.samtimo.pubcrawl;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button racesButton = (Button) findViewById(R.id.races_button);
        racesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, RacesActivity.class);
                startActivity(intent);
            }
        });

        Button myRacesButton = (Button) findViewById(R.id.my_races_button);
        myRacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MyRacesActivity.class);
                startActivity(intent);
            }
        });

        Button pubsButton = (Button) findViewById(R.id.pubs_button);
        pubsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PubsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_logout: logout(); break;
            case R.id.action_settings: openSettings(); break;
        }

        //noinspection SimplifiableIfStatement
        if (id==R.id.action_settings || id==R.id.action_logout) {
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    private void logout(){
        Request request = new Request(RequestMethod.GET, "logout", null, null);
        new RequestTask(this, "logout").execute(request);
    }

    public void logoutFinish(){
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void openSettings(){

    }
}
