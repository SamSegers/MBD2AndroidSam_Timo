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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PubsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PubsDetailFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private Pub seletedPub;

    private ArrayList<Review> reviews;
    private PubReviewListAdapter adapter;

    public PubsDetailFragment() {
        reviews = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pubs_detail, container, false);
        // get the ListView from fragment_list
        ListView listView = (ListView) rootView.findViewById(R.id.list_pub_reviews);
        // register ListView so I can use it with the context menu
        registerForContextMenu(listView);
        // create adapter, parameters: activity, layout of individual items, array of values
        adapter = new PubReviewListAdapter(getActivity(), reviews);
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
        Request request = new Request(RequestMethod.PUT, "users/pubs/"+seletedPub.getId()+"/name/"+seletedPub.getName().replaceAll(" ", "_"), null, null);
        new RequestTask(this, "add").execute(request);
    }

    //TODO
    public void addPubFinish(String json){
        System.out.println("pubs detail fragment");
    }

    public void updateDetails(Pub pub){
        seletedPub = pub;

        TextView textView = (TextView)getActivity().findViewById(R.id.text_header);
        textView.setText(pub.getName().toString());

        Request request = new Request(RequestMethod.GET, "pubs/"+pub.getId(), null, null);
        new RequestTask(this, "info").execute(request);

        getActivity().findViewById(R.id.button_add).setVisibility(View.VISIBLE);
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
}