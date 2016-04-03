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
 * {@link PubsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PubsListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private OnFragmentInteractionListener mListener;

    private ArrayList<Pub> pubs;
    private View rootView;
    private PubListAdapter adapter;

    public PubsListFragment() {
        pubs = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Request request = new Request(RequestMethod.GET, "pubs", null, null);
        new RequestTask(this).execute(request);
    }

    public void addPubs(String json){
        try{
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray("results");
            for (int i=0; i<results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                pubs.add(new Pub(result.getString("id"), result.getString("name"), result.getString("icon")));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        //XXX: this solved the problem of the adapter not updating :/
        ListView listView = (ListView) rootView.findViewById(R.id.list_pubs);
        adapter = new PubListAdapter(getActivity(), pubs);
        // set the adapter to the ListView
        listView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pubs_list, container, false);
        // get the ListView from fragment_list
        ListView listView = (ListView) rootView.findViewById(R.id.list_pubs);
        // register ListView so I can use it with the context menu
        registerForContextMenu(listView);
        // create adapter, parameters: activity, layout of individual items, array of values
        adapter = new PubListAdapter(getActivity(), pubs);
        // set the adapter to the ListView
        listView.setAdapter(adapter);
        // add actionlistener
        listView.setOnItemClickListener(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearchFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchFragmentInteraction(Uri uri);
    }
}
