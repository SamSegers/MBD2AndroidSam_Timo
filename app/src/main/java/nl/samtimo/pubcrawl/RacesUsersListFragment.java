package nl.samtimo.pubcrawl;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RacesUsersListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RacesUsersListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private ArrayList<User> users;
    private View rootView;
    private UserListAdapter adapter;

    public RacesUsersListFragment() {
    }

    public void loadUsers(Race race){
        adapter.setRace(race);
        Request request = new Request(RequestMethod.GET, "races/"+race.getId()+"/users", null, null);
        new RequestTask(this).execute(request);
    }

    public void loadUsersFinish(String result){
        users.clear();
        try{
            JSONArray usersArr = new JSONArray(result);
            for (int i=0; i<usersArr.length(); i++) {
                JSONObject jsonUser = usersArr.getJSONObject(i);
                JSONArray jsonRaces = jsonUser.getJSONArray("race");
                ArrayList<Race> races = new ArrayList<>();
                for (int j=0; j<jsonRaces.length(); j++) {
                    JSONObject jsonRace = jsonRaces.getJSONObject(j);
                    JSONArray jsonPubs = jsonRace.getJSONArray("tagged");
                    ArrayList<Pub> pubs = new ArrayList<>();
                    for(int k=0;k<jsonPubs.length();k++){
                        pubs.add(new Pub(jsonPubs.getString(k), null, null));
                        /*JSONObject jsonPub = jsonPubs.getJSONObject(k);
                        pubs.add(new Pub(jsonPub.getString("id"), jsonPub.getString("name"), null));*/
                        //races.add(new Race(jsonRace.getString("id"), jsonRace.getString("name"), null, null, null));
                    }
                    races.add(new Race(jsonRace.getString("id"), null/*jsonRace.getString("name")*/, pubs, null, null));
                }
                users.add(new User(jsonUser.getString("_id"), jsonUser.getString("username"), races));
            }
            adapter.notifyDataSetChanged();

            RacesDetailFragment racesDetailFragment = (RacesDetailFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_detail);
            racesDetailFragment.updateJoinButton();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    //TODO uses contains()
    public boolean contains(User user){
        for(int i=0;i<users.size();i++)
            if(users.get(i).getId().equals(user.getId())) return true;
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_races_users_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_races_users);
        registerForContextMenu(listView);
        users = new ArrayList<>();
        adapter = new UserListAdapter(getActivity(), users);
        listView.setAdapter(adapter);
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

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
