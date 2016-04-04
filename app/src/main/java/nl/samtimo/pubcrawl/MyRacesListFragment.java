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
 * {@link MyRacesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MyRacesListFragment extends Fragment  implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private ArrayList<Pub> pubs;
    private View rootView;
    private MyRacesPubsListAdapter adapter;

    public MyRacesListFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_my_races_list, container, false);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) mListener.onListFragmentInteraction(uri);
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
        System.out.println(result);
        try{
            JSONObject pubs1 = new JSONObject(result);
            JSONArray pubsObj = pubs1.getJSONArray("pub");
            for (int i=0; i<pubsObj.length(); i++) {
                String pub = pubsObj.getString(i);
                pubs.add(new Pub("asdfs", pub, null));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        /*//XXX: this solved the problem of the adapter not updating :/
        ListView listView = (ListView) rootView.findViewById(R.id.list_pubs);
        adapter = new MyRacesPubsListAdapter(getActivity(), pubs);
        // set the adapter to the ListView
        listView.setAdapter(adapter);*/
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
        void onListFragmentInteraction(Uri uri);
    }
}
