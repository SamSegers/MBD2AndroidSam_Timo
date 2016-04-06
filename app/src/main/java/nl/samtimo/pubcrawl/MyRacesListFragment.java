package nl.samtimo.pubcrawl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyRacesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyRacesListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private ArrayList<Race> races;
    private View rootView;
    private RaceListAdapter adapter;
    private String newName;
    private Race selectedRace;

    public MyRacesListFragment() {
        races = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reloadRaces();
    }

    public void reloadRaces(){
        Request request = new Request(RequestMethod.GET, "users/racescreated", null, null);
        new RequestTask(this).execute(request);
    }

    public void loadRaces(String json){
        races.clear();
        try{
            JSONArray racesArr = new JSONArray(json);
            for (int i=0; i<racesArr.length(); i++) {
                JSONObject race = racesArr.getJSONObject(i);
                ArrayList<Pub> waypoints = new ArrayList<>();
                if(race.has("pubs")){
                    JSONArray waypointsArr = race.getJSONArray("pubs");
                    for(int j=0;j<waypointsArr.length();j++){
                        JSONObject waypoint = waypointsArr.optJSONObject(j);
                        if(waypoint!=null && waypoint.has("id") && waypoint.has("name"))
                            waypoints.add(new Pub(waypoint.getString("id"), waypoint.getString("name"), null));
                    }
                }
                races.add(new Race(race.getString("_id"), race.getString("name"), waypoints, null, null));
            }
            adapter.notifyDataSetChanged();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void addRace(){
        newName = String.valueOf(System.currentTimeMillis() / 1000);
        Request request = new Request(RequestMethod.POST, "races/new/"+newName, null, null);
        new RequestTask(this, "add").execute(request);
    }

    public void addRaceFinish(String result){
        try{
            JSONObject object = new JSONObject(result);
            adapter.add(new Race(object.getString("_id"), newName, null, null, null));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void setSeletectRaceName(String name){
        selectedRace.setName(name);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_races_list, container, false);

        // get the ListView from fragment_list
        ListView listView = (ListView) rootView.findViewById(R.id.list_races);
        // register ListView so I can use it with the context menu
        registerForContextMenu(listView);
        // create adapter, parameters: activity, layout of individual items, array of values
        adapter = new RaceListAdapter(getActivity(), races);
        // set the adapter to the ListView
        listView.setAdapter(adapter);
        // add actionlistener
        listView.setOnItemClickListener(this);

        return rootView;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedRace = (Race) parent.getItemAtPosition(position);
        if(selectedRace!=null) mListener.onListFragmentInteraction(selectedRace);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Race race);
    }
}
