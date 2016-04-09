package nl.samtimo.pubcrawl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
    private RacesListAdapter adapter;
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
        new RequestTask(this, "load").execute(request);
    }

    public void loadRaces(String json){
        races.clear();
        try{
            JSONArray racesArr = new JSONArray(json);
            for (int i=0; i<racesArr.length(); i++) {
                JSONObject jRace = racesArr.getJSONObject(i);
                ArrayList<Pub> waypoints = new ArrayList<>();
                if(jRace.has("pubs")){
                    JSONArray waypointsArr = jRace.getJSONArray("pubs");
                    for(int j=0;j<waypointsArr.length();j++){
                        JSONObject waypoint = waypointsArr.optJSONObject(j);
                        if(waypoint!=null && waypoint.has("id") && waypoint.has("name"))
                            waypoints.add(new Pub(waypoint.getString("id"), waypoint.getString("name"), false, null));
                    }
                }

                Date startDate = null;
                Date endDate = null;

                if(jRace.has("startDate")){
                    String strDate = jRace.getString("startDate");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//"yyyy-MM-dd HH:mm:ss z");
                    //format.setTimeZone(TimeZone.getTimeZone("GTM+1"));
                    startDate = format.parse(strDate);
                }

                if(jRace.has("endDate")){
                    String strDate = jRace.getString("endDate");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//"yyyy-MM-dd HH:mm:ss z");
                    //format.setTimeZone(TimeZone.getTimeZone("GTM+1"));
                    endDate = format.parse(strDate);
                }

                races.add(new Race(jRace.getString("_id"), jRace.getString("name"), waypoints, startDate, endDate));
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
            JSONObject jsonRace = new JSONObject(result);
            adapter.add(new Race(jsonRace.getString("_id"), newName));
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
        adapter = new RacesListAdapter(getActivity(), races);
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
