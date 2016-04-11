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

import java.net.URLDecoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyRacesPubsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyRacesPubsListFragment extends Fragment  implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private ArrayList<Pub> pubs;
    private View rootView;
    private MyRacesPubsListAdapter adapter;

    public MyRacesPubsListFragment() {
        pubs = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Request request = new Request(RequestMethod.GET, "users/pubs", null, null);
        new RequestTask(this).execute(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_races_pubs_list, container, false);
        // get the ListView from fragment_list
        ListView listView = (ListView) rootView.findViewById(R.id.list_my_races);
        // register ListView so I can use it with the context menu
        registerForContextMenu(listView);
        // create adapter, parameters: activity, layout of individual items, array of values
        adapter = new MyRacesPubsListAdapter(getActivity(), pubs);
        // set the adapter to the ListView
        listView.setAdapter(adapter);
        // add actionlistener
        listView.setOnItemClickListener(this);

        return rootView;
    }

    public void update(Race race){
        if(race!=null) adapter.setRace(race);
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

    public void loadPubs(String result){
        try{
            JSONObject pubsObj = new JSONObject(result);
            JSONArray pubsArr = pubsObj.getJSONArray("pub");
            for (int i=0; i<pubsArr.length(); i++) {
                JSONObject pub = pubsArr.getJSONObject(i);
                if(pub.has("id") && pub.has("name")){
                    String name = URLDecoder.decode(pub.getString("name"));
                    pubs.add(new Pub(pub.getString("id"), name, false, null));
                }
            }
            adapter.notifyDataSetChanged();
        }catch(Exception ex){
            ex.printStackTrace();
        }
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
        void onPubListFragmentInteraction(Uri uri);
    }
}
