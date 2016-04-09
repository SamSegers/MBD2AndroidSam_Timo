package nl.samtimo.pubcrawl;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RacesDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RacesDetailFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    private ArrayList<Pub> pubs;
    private RacesPubsListAdapter adapter;

    private Pub selectedPub;
    private Race selectedRace;

    public RacesDetailFragment() {
        pubs = new ArrayList<>();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_races_detail, container, false);
        // get the ListView from fragment_list
        ListView listView = (ListView) rootView.findViewById(R.id.list_pubs);
        // register ListView so I can use it with the context menu
        registerForContextMenu(listView);
        // create adapter, parameters: activity, layout of individual items, array of values
        adapter = new RacesPubsListAdapter(getActivity(), pubs);
        // set the adapter to the ListView
        listView.setAdapter(adapter);
        // add actionlistener
        listView.setOnItemClickListener(this);

        return rootView;
    }

    public void updateDetails(Race race){
        selectedRace = race;

        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);
        racesUsersListFragment.loadUsers(race);

        TextView textView = (TextView)getActivity().findViewById(R.id.text_detail);
        textView.setText(race.getName());

        updateJoinButton();
        loadPubs();
    }

    public void updateJoinButton(){
        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);

        Button btnJoinRace = (Button)getActivity().findViewById(R.id.button_join_race);
        btnJoinRace.setVisibility(View.VISIBLE);
        btnJoinRace.setEnabled(!selectedRace.isCompleted());
        if(racesUsersListFragment.contains(LoginActivity.user)){
            btnJoinRace.setText(R.string.button_leave_race);
            btnJoinRace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leaveRace();
                }
            });
        }else{
            btnJoinRace.setText(R.string.button_join_race);
            btnJoinRace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    joinRace();
                }
            });
        }
    }

    public void leaveRace(){
        if(selectedRace!=null){
            Request request = new Request(RequestMethod.PUT, "races/"+selectedRace.getId()+"/leave", null, null);
            new RequestTask(this, "leave").execute(request);
        }else System.out.println("no race selected");
    }

    public void leaveRaceFinish(){
        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);
        racesUsersListFragment.loadUsers(selectedRace);
    }

    public void joinRace(){
        if(selectedRace!=null){
            Request request = new Request(RequestMethod.PUT, "races/"+selectedRace.getId()+"/join", null, null);
            new RequestTask(this, "join").execute(request);
        }else System.out.println("no race selected");
    }

    public void joinRaceFinish(){
        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);
        racesUsersListFragment.loadUsers(selectedRace);
    }

    public ArrayList<Pub> getWaypoints(){
        return selectedRace.getPubs();
    }

    public Race getRace(){
        return selectedRace;
    }

    public void loadPubs(){
        if(selectedRace!=null){
            try{
                pubs.clear();
                ArrayList<Pub> toLoadPubs = selectedRace.getPubs();
                for(int i=0;i<toLoadPubs.size();i++)
                    pubs.add(toLoadPubs.get(i));

                adapter.notifyDataSetChanged();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else System.out.println("no race selected");
    }

    public void updateTag(Pub pub, boolean checked){
        selectedPub = pub;
        if(checked){
            Request request = new Request(RequestMethod.PUT, "users/race/"+selectedRace.getId()+"/pub/"+pub.getId()+"/tag", null, null);
            new RequestTask(this, "tag").execute(request);
        }else{
            Request request = new Request(RequestMethod.PUT, "users/race/"+selectedRace.getId()+"/pub/"+pub.getId()+"/untag", null, null);
            new RequestTask(this, "untag").execute(request);
        }
    }

    public void updateTagFinish(String result){
        LoginActivity.user.addTag(selectedRace, selectedPub);
        joinRaceFinish();
    }

    public void updateUntagFinish(String result){
        LoginActivity.user.removeTag(selectedRace, selectedPub);
        joinRaceFinish();
    }

    // TODO: use something like this
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDetailFragmentInteraction(uri);
        }
    }*/

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
        void onDetailFragmentInteraction(Uri uri);
    }
}
