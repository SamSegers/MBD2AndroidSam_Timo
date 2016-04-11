package nl.samtimo.pubcrawl.pubs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import nl.samtimo.pubcrawl.authentication.LoginActivity;
import nl.samtimo.pubcrawl.Pub;
import nl.samtimo.pubcrawl.R;
import nl.samtimo.pubcrawl.request.Request;
import nl.samtimo.pubcrawl.request.RequestMethod;
import nl.samtimo.pubcrawl.request.RequestTask;
import nl.samtimo.pubcrawl.Review;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PubsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PubsDetailFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;
    private PubsListFragment pubsListFragment;
    private Pub seletedPub;
    private ArrayList<Review> reviews;
    private PubsReviewListAdapter adapter;

    public PubsDetailFragment() {
        // required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reviews = new ArrayList<>();
        pubsListFragment = (PubsListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_pubs_search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pubs_detail, container, false);
        // get the ListView from fragment_list
        ListView listView = (ListView) rootView.findViewById(R.id.list_pub_reviews);
        // register ListView so I can use it with the context menu
        registerForContextMenu(listView);
        // create adapter, parameters: activity, layout of individual items, array of values
        adapter = new PubsReviewListAdapter(getActivity(), reviews);
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

    }

    public void addPub(){
        String name = URLEncoder.encode(seletedPub.getName());
        Request request = new Request(RequestMethod.PUT, "users/pubs/"+seletedPub.getId()+"/name/"+name, null, null);
        new RequestTask(this, RequestType.ADD.ordinal()).execute(request);
    }

    public void addPubFinish(String json){
        LoginActivity.user.addPub(seletedPub);
        Button btnUpdate = (Button)getActivity().findViewById(R.id.button_pub_update);
        btnUpdate.setText(R.string.button_collection_remove);
        pubsListFragment.reloadPubs();
    }

    public void removePub(){
        Request request = new Request(RequestMethod.DELETE, "users/pubs/"+seletedPub.getId(), null, null);
        new RequestTask(this, RequestType.REMOVE.ordinal()).execute(request);
    }

    public void removePubFinish(){
        System.out.println("removePubFinish");
        LoginActivity.user.removePub(seletedPub);
        Button btnUpdate = (Button)getActivity().findViewById(R.id.button_pub_update);
        btnUpdate.setText(R.string.button_collection_add);
        pubsListFragment.reloadPubs();
    }

    public void updateDetails(Pub pub){
        seletedPub = pub;

        TextView textView = (TextView)getActivity().findViewById(R.id.text_header);
        textView.setText(pub.getName().toString());

        Button btnUpdate = (Button)getActivity().findViewById(R.id.button_pub_update);

        if(LoginActivity.user.isInCollection(pub)) btnUpdate.setText(R.string.button_collection_remove);
        else btnUpdate.setText(R.string.button_collection_add);

        Request request = new Request(RequestMethod.GET, "pubs/"+pub.getId(), null, null);
        new RequestTask(this, RequestType.INFO.ordinal()).execute(request);

        getActivity().findViewById(R.id.button_pub_update).setVisibility(View.VISIBLE);
    }

    public void updateStatus(){
        if(!LoginActivity.user.isInCollection(seletedPub)) addPub();
        else removePub();
    }

    public void loadInfo(String json){
        try{
            adapter.clear();
            JSONObject jsonResult = new JSONObject(json).getJSONObject("result");
            if(jsonResult.has("reviews")){
                JSONArray jsonReviews = jsonResult.getJSONArray("reviews");
                for (int i=0; i<jsonReviews.length(); i++) {
                    JSONObject jsonReview = jsonReviews.getJSONObject(i);
                    if(jsonReview.has("author_name") && jsonReview.has("text")){
                        reviews.add(new Review(jsonReview.getString("author_name"), jsonReview.getString("text")));
                        getActivity().findViewById(R.id.layout_reviews).setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDetailFragmentInteraction(Uri uri);
    }

    public enum RequestType{
        ADD,
        REMOVE,
        INFO
    }
}