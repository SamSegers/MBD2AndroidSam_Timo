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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RacesDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RacesDetailFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    private Race selectedRace;

    public RacesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_races_detail, container, false);
    }

    public void updateDetails(Race race){
        selectedRace = race;

        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);
        racesUsersListFragment.loadUsers(race);

        TextView textView = (TextView)getActivity().findViewById(R.id.text_detail);
        textView.setText(race.getName());

        updateJoinButton();
    }

    public void updateJoinButton(){
        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);

        Button btnJoinRace = (Button)getActivity().findViewById(R.id.button_join_race);
        btnJoinRace.setVisibility(View.VISIBLE);
        System.out.println(racesUsersListFragment.contains(LoginActivity.user));
        if(racesUsersListFragment.contains(LoginActivity.user)) btnJoinRace.setText(R.string.button_leave_race);
        else btnJoinRace.setText(R.string.button_join_race);
    }

    public void joinRace(){
        if(selectedRace!=null){
            Request request = new Request(RequestMethod.PUT, "races/"+selectedRace.getId()+"/join", null, null);
            new RequestTask(this).execute(request);
        }else System.out.println("no race selected");
    }

    public void joinRaceFinish(){
        RacesUsersListFragment racesUsersListFragment = (RacesUsersListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_races_users_list);
        racesUsersListFragment.loadUsers(selectedRace);
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
