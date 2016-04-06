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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PubsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PubsDetailFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    private Pub seletedPub;

    public PubsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pubs_detail, container, false);
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
        new RequestTask(PubsDetailFragment.this).execute(request);
    }

    //TODO
    public void addPubCont(String json){
        System.out.println("pubs detail fragment");
    }

    public void updateDetails(Pub pub){
        seletedPub = pub;
        TextView textView = (TextView)getActivity().findViewById(R.id.text_detail);
        textView.setText(pub.getName().toLowerCase());

        getActivity().findViewById(R.id.add_pub_button).setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDetailFragmentInteraction(Uri uri);
    }
}
