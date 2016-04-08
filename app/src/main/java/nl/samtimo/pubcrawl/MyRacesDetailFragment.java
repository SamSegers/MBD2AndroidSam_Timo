package nl.samtimo.pubcrawl;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyRacesDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyRacesDetailFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Race selectedRace;

    public MyRacesDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_races_detail, container, false);
    }

    public void updateDetails(Race race){
        selectedRace = race;

        if(selectedRace.isCompleted()){
            getActivity().findViewById(R.id.button_save).setEnabled(false);
            getActivity().findViewById(R.id.button_status).setEnabled(false);
            getActivity().findViewById(R.id.list_my_races).setEnabled(false);
        }
        getActivity().findViewById(R.id.button_remove).setEnabled(true);

        TextView textHeader = (TextView)getActivity().findViewById(R.id.text_header);
        textHeader.setText(race.getName());
        EditText editName = (EditText)getActivity().findViewById(R.id.edit_name);
        editName.setText(race.getName());

        updateStatusButton();
    }

    public void updateStatusButton(){
        Button btnStatus = (Button)getActivity().findViewById(R.id.button_status);

        //if(racesUsersListFragment.contains(LoginActivity.user)){
        if(selectedRace!=null && selectedRace.isOngoing()){
            btnStatus.setText(R.string.button_stop_race);
            btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopRace();
                }
            });
        }else{
            btnStatus.setText(R.string.button_start_race);
            btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startRace();
                }
            });
        }
    }

    public void saveRace(){
        System.out.println(selectedRace.getId());
        EditText editName = (EditText)getActivity().findViewById(R.id.edit_name);
        String requestRoute = "races/"+selectedRace.getId()+"/update";

        //TODO find something better for this
        String requestBody = "{ \"name\": \""+editName.getText()+"\", \"pubs\": [";
        ArrayList<Pub> waypoints = selectedRace.getWaypoints();
        if(waypoints!=null) {
            boolean first = true;
            for (int i = 0; i < waypoints.size(); i++) {
                if (first == false) requestBody += ",";
                requestBody += "{ \"id\": \"" + waypoints.get(i).getId() + "\", \"name\": \"" + waypoints.get(i).getName() + "\"}";
                first = false;
            }
        }
        requestBody += "]}";

        System.out.println(requestBody);
        Request request = new Request(RequestMethod.PUT, requestRoute, requestBody, null);
        new RequestTask(this, "save").execute(request);
    }

    //TODO avoid changing the name when the request failed
    public void saveRaceFinish(){
        TextView textHeader = (TextView)getActivity().findViewById(R.id.text_header);
        EditText editName = (EditText)getActivity().findViewById(R.id.edit_name);
        selectedRace.setName(editName.getText().toString());
        textHeader.setText(selectedRace.getName());

        MyRacesListFragment myRacesListFragment = (MyRacesListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_list);
        myRacesListFragment.setSeletectRaceName(selectedRace.getName());
    }

    public void startRace(){
        Request request = new Request(RequestMethod.PUT, "races/"+selectedRace.getId()+"/start", null, null);
        new RequestTask(this, "start").execute(request);
    }

    public void startRaceFinish(){
        Button btnStatus = (Button)getActivity().findViewById(R.id.button_status);
        btnStatus.setText(R.string.button_stop_race);
        selectedRace.setStartDate();
    }

    public void stopRace(){
        Request request = new Request(RequestMethod.PUT, "races/"+selectedRace.getId()+"/end", null, null);
        new RequestTask(this, "stop").execute(request);
    }

    public void stopRaceFinish(){
        System.out.println("bloop");
        Button btnStatus = (Button)getActivity().findViewById(R.id.button_status);
        selectedRace.setEndDate();
    }

    //TODO think wheter I should use 'remove' or 'delete'
    public void removeRace(){
        Request request = new Request(RequestMethod.DELETE, "races/"+selectedRace.getId(), null, null);
        new RequestTask(this, "remove").execute(request);
    }

    public void removeRaceFinish(){
        MyRacesListFragment myRacesListFragment = (MyRacesListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_list);
        myRacesListFragment.reloadRaces();
    }

    public void updatePub(Pub pub, boolean inRace){
        if(selectedRace!=null) selectedRace.setWaypoint(pub, inRace);
    }

    public ArrayList<Pub> getWaypoints(){
        return selectedRace!=null?selectedRace.getWaypoints():null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onDetailFragmentInteraction(Uri uri);
    }
}
