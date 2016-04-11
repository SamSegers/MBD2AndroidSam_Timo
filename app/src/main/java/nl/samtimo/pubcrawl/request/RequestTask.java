package nl.samtimo.pubcrawl.request;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import nl.samtimo.pubcrawl.users.CurrentUser;
import nl.samtimo.pubcrawl.authentication.LoginActivity;
import nl.samtimo.pubcrawl.MenuActivity;
import nl.samtimo.pubcrawl.authentication.SignUpActivity;
import nl.samtimo.pubcrawl.my_races.MyRacesDetailFragment;
import nl.samtimo.pubcrawl.my_races.MyRacesListFragment;
import nl.samtimo.pubcrawl.my_races.MyRacesPubsListFragment;
import nl.samtimo.pubcrawl.pubs.PubsDetailFragment;
import nl.samtimo.pubcrawl.pubs.PubsListFragment;
import nl.samtimo.pubcrawl.races.RacesDetailFragment;
import nl.samtimo.pubcrawl.races.RacesListFragment;
import nl.samtimo.pubcrawl.races.RacesUsersListFragment;

public class RequestTask extends AsyncTask<Request, Integer, String> {
    private static CookieManager cookieManager;

    static{
        cookieManager = new CookieManager();
    }

    private int requestTypeIndex;
    private Fragment fragment;
    private Activity activity;
    private CurrentUser currentUser;

    public RequestTask(Activity activity){
        this.activity = activity;
    }

    public RequestTask(Activity activity, int requestTypeIndex){
        this.activity = activity;
        this.requestTypeIndex = requestTypeIndex;
    }

    public RequestTask(Fragment fragment){
        this.fragment = fragment;
    }

    public RequestTask(Fragment fragment, int requestTypeIndex){
        this.fragment = fragment;
        this.requestTypeIndex = requestTypeIndex;
    }

    public RequestTask(CurrentUser currentUser){
        this.currentUser = currentUser;
    }

    protected String doInBackground(Request... requests) {
        Request request = requests[0];
        HttpURLConnection connection = null;
        try {
            String targetURL = "http://10.0.2.2:3001/"+request.getPath();
            System.out.println(request.getMethod().name());
            System.out.println(targetURL);
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod().name());

            if(request.getMethod()== RequestMethod.POST || request.getMethod()==RequestMethod.PUT){
                connection.setDoOutput(true);
                connection.setUseCaches(false);
            }

            connection.setRequestProperty("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));

            if(request.getParameters()!=null) {
                byte[] postData = request.getParameters().getBytes(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT ? StandardCharsets.UTF_8 : Charset.forName("UTF-8"));
                connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
                connection.setUseCaches(false);
                if(request.getMethod()==RequestMethod.PUT) connection.setRequestProperty("Content-Type", "application/json");
                else connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                try{
                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.write( postData );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if(request.getProperties()!=null) {
                RequestProperty[] properties = request.getProperties();
                for (int i = 0; i < properties.length; i++)
                    connection.setRequestProperty(properties[i].getName(), properties[i].getValue());
            }

            saveCookie(connection);

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());

            return parseResponse(connection.getInputStream());
        } catch (Exception e) {
            //e.printStackTrace();
            try{
                System.out.println(parseResponse(connection.getErrorStream()));
            }catch(Exception ex){
                ex.printStackTrace();
            }
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    private void saveCookie(HttpURLConnection connection){
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get("Set-Cookie");

        if(cookiesHeader != null)
            for (String cookie : cookiesHeader)
                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
    }

    private String parseResponse(InputStream in) throws Exception
    {
        InputStreamReader inputStream = new InputStreamReader(in, "UTF-8" );
        BufferedReader buff = new BufferedReader(inputStream);

        StringBuilder sb = new StringBuilder();
        String line = buff.readLine();
        while (line != null )
        {
            sb.append(line);
            line = buff.readLine();
        }

        return sb.toString();
    }

    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {
        System.out.println(progress[0]);
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(String result) {
        if(result!=null) {
            //region ACTIVITIES
            if(activity!=null){
                if(activity instanceof LoginActivity) ((LoginActivity)activity).login(result);
                else if(activity instanceof SignUpActivity) ((SignUpActivity)activity).signUp(result);
                else if(activity instanceof MenuActivity) ((MenuActivity)activity).logoutFinish();
            }
            //endregion

            //region FRAGMENTS
            else if(fragment!=null) {
                if (fragment instanceof RacesListFragment)
                    ((RacesListFragment) fragment).loadRaces(result);
                else if (fragment instanceof PubsListFragment)
                    ((PubsListFragment) fragment).loadPubs(result);
                else if (fragment instanceof PubsDetailFragment) {
                    postExecutePubsDetailFragment(result);
                } else if (fragment instanceof MyRacesPubsListFragment)
                    ((MyRacesPubsListFragment) fragment).loadPubs(result);
                else if (fragment instanceof RacesUsersListFragment)
                    ((RacesUsersListFragment) fragment).loadUsersFinish(result);
                else if (fragment instanceof MyRacesListFragment) {
                    postExecuteMyRacesListFragment(result);
                } else if (fragment instanceof MyRacesDetailFragment) {
                    postExecuteMyRacesDetailFragment(result);
                } else if (fragment instanceof RacesDetailFragment) {
                    postExecuteRacesDetailFragment(result);
                }
            }else if(currentUser!=null) currentUser.initPubs(result);
            //endregion

            else System.out.println(result);
        }else System.out.println("result is empty");
    }

    //region post execution specific classes

    // all classes which have multiple callback methods should get there own method here

    private void postExecutePubsDetailFragment(String result){
        PubsDetailFragment pubsDetailFragment = (PubsDetailFragment) fragment;
        PubsDetailFragment.RequestType requestType = PubsDetailFragment.RequestType.values()[requestTypeIndex];
        switch (requestType) {
            case ADD: pubsDetailFragment.addPubFinish(result); break;
            case REMOVE: pubsDetailFragment.removePubFinish(); break;
            case INFO: pubsDetailFragment.loadInfo(result); break;
        }
    }

    private void postExecuteMyRacesListFragment(String result){
        MyRacesListFragment myRacesListFragment = (MyRacesListFragment) fragment;
        MyRacesListFragment.RequestType requestType = MyRacesListFragment.RequestType.values()[requestTypeIndex];
        switch (requestType) {
            case ADD: myRacesListFragment.addRaceFinish(result); break;
            case LOAD: myRacesListFragment.loadRaces(result); break;
        }
    }

    private void postExecuteMyRacesDetailFragment(String result){
        MyRacesDetailFragment myRacesDetailFragment = (MyRacesDetailFragment) fragment;
        MyRacesDetailFragment.RequestType requestType = MyRacesDetailFragment.RequestType.values()[requestTypeIndex];
        switch (requestType) {
            case REMOVE: myRacesDetailFragment.removeRaceFinish(); break;
            case SAVE: myRacesDetailFragment.saveRaceFinish(); break;
            case START: myRacesDetailFragment.startRaceFinish(); break;
            case STOP: myRacesDetailFragment.stopRaceFinish(); break;
        }
    }

    private void postExecuteRacesDetailFragment(String result){
        RacesDetailFragment racesDetailFragment = (RacesDetailFragment) fragment;
        RacesDetailFragment.RequestType requestType = RacesDetailFragment.RequestType.values()[requestTypeIndex];
        switch (requestType) {
            case JOIN: racesDetailFragment.joinRaceFinish(); break;
            case LEAVE: racesDetailFragment.leaveRaceFinish(); break;
            case TAG: racesDetailFragment.updateTagFinish(result); break;
            case UNTAG: racesDetailFragment.updateUntagFinish(result); break;
        }
    }

    //endregion
}
