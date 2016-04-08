package nl.samtimo.pubcrawl;

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

public class RequestTask extends AsyncTask<Request, Integer, String> {
    private static CookieManager cookieManager;

    static{
        cookieManager = new CookieManager();
    }

    private String callback;
    private Fragment fragment;
    private Activity activity;

    public RequestTask(Activity activity){
        this.activity = activity;
    }

    public RequestTask(Activity activity, String callback){
        this.activity = activity;
        this.callback = callback;
    }

    public RequestTask(Fragment fragment){
        this.fragment = fragment;
    }

    public RequestTask(Fragment fragment, String callback){
        this.fragment = fragment;
        this.callback = callback;
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

            if(request.getMethod()==RequestMethod.POST || request.getMethod()==RequestMethod.PUT){
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
            if(activity!=null){
                if(activity instanceof LoginActivity) ((LoginActivity)activity).login(result);
                else if(activity instanceof SignUpActivity) ((SignUpActivity)activity).signUp(result);
                else if(activity instanceof MenuActivity) ((MenuActivity)activity).logoutFinish();
            }else if(fragment!=null){
                if(fragment instanceof RacesListFragment) ((RacesListFragment)fragment).loadRaces(result);
                else if(fragment instanceof PubsListFragment) ((PubsListFragment)fragment).loadPubs(result);
                else if(fragment instanceof PubsDetailFragment){
                    PubsDetailFragment pubsDetailFragment = (PubsDetailFragment)fragment;
                    switch(callback){
                        case "add": pubsDetailFragment.addPubFinish(result); break;
                        case "info": pubsDetailFragment.loadInfo(result); break;
                    }
                }
                else if(fragment instanceof MyRacesPubsListFragment) ((MyRacesPubsListFragment)fragment).loadPubs(result);
                else if(fragment instanceof RacesUsersListFragment) ((RacesUsersListFragment)fragment).loadUsersFinish(result);
                else if(fragment instanceof MyRacesListFragment){
                    MyRacesListFragment myRacesListFragment = (MyRacesListFragment)fragment;
                    switch(callback){
                        case "add": myRacesListFragment.addRaceFinish(result); break;
                        case "load": myRacesListFragment.loadRaces(result); break;
                    }
                }else if(fragment instanceof MyRacesDetailFragment){
                    MyRacesDetailFragment myRacesDetailFragment = (MyRacesDetailFragment)fragment;
                    switch(callback){
                        case "remove": myRacesDetailFragment.removeRaceFinish(); break;
                        case "save": myRacesDetailFragment.saveRaceFinish(); break;
                        case "start": myRacesDetailFragment.startRaceFinish(); break;
                        case "stop": myRacesDetailFragment.stopRaceFinish(); break;
                    }
                }else if(fragment instanceof RacesDetailFragment){
                    RacesDetailFragment racesDetailFragment = (RacesDetailFragment)fragment;
                    switch (callback){
                        case "join": racesDetailFragment.joinRaceFinish(); break;
                        case "leave": racesDetailFragment.leaveRaceFinish(); break;
                        case "tag": racesDetailFragment.updateTagFinish(result); break;
                        case "untag": racesDetailFragment.updateUntagFinish(result); break;
                    }
                }
            }//else System.out.println(result);
        }else System.out.println("result is empty");
    }
}
