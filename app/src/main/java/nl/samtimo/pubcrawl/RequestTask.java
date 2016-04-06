package nl.samtimo.pubcrawl;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;

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

    //TODO code review

    private String callback;

    private LoginActivity loginActivity;
    private SignUpActivity signUpActivity;
    private RacesListFragment racesListFragment;
    private PubsListFragment pubsListFragment;
    private PubsDetailFragment pubsDetailFragment;
    private MyRacesPubsListFragment myRacesPubsListFragment;
    private MyRacesListFragment myRacesListFragment;
    private MyRacesDetailFragment myRacesDetailFragment;
    private RacesUsersListFragment racesUsersListFragment;
    private RacesDetailFragment racesDetailFragment;
    private MenuActivity menuActivity;

    private Fragment fragment;//TODO

    public RequestTask(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
    }

    public RequestTask(SignUpActivity signUpActivity){
        this.signUpActivity = signUpActivity;
    }

    public RequestTask(RacesListFragment racesListFragment){
        this.racesListFragment = racesListFragment;
    }

    public RequestTask(PubsListFragment pubsListFragment){
        this.pubsListFragment = pubsListFragment;
    }

    public RequestTask(PubsDetailFragment pubsDetailFragment){
        this.pubsDetailFragment = pubsDetailFragment;
    }

    public RequestTask(MyRacesPubsListFragment myRacesPubsListFragment){
        this.myRacesPubsListFragment = myRacesPubsListFragment;
    }

    public RequestTask(MyRacesListFragment myRacesListFragment){
        this.myRacesListFragment = myRacesListFragment;
    }

    public RequestTask(MyRacesListFragment myRacesListFragment, String callback){
        this.myRacesListFragment = myRacesListFragment;
        this.callback = callback;
    }

    public RequestTask(MyRacesDetailFragment myRacesDetailFragment){
        this.myRacesDetailFragment = myRacesDetailFragment;
    }

    public RequestTask(MyRacesDetailFragment myRacesDetailFragment, String callback){
        this.myRacesDetailFragment = myRacesDetailFragment;
        this.callback = callback;
    }

    public RequestTask(RacesUsersListFragment racesUsersListFragment){
        this.racesUsersListFragment = racesUsersListFragment;
    }

    public RequestTask(RacesDetailFragment racesDetailFragment, String callback){
        this.racesDetailFragment = racesDetailFragment;
        this.callback = callback;
    }

    public RequestTask(MenuActivity menuActivity, String callback){
        this.menuActivity = menuActivity;
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
            //connection.setRequestProperty("Accept", "*/*");

            //connection.connect();

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
            if (loginActivity!=null) loginActivity.login(result);
            else if (signUpActivity!=null && result.equals("signed up")) signUpActivity.openMenu();
            else if(racesListFragment!=null) racesListFragment.loadRaces(result);
            else if(pubsListFragment!=null) pubsListFragment.loadPubs(result);
            else if(pubsDetailFragment!=null) pubsDetailFragment.addPubCont(result);
            else if(myRacesPubsListFragment !=null) myRacesPubsListFragment.loadPubs(result);
            else if(myRacesListFragment!=null){
                if(callback!=null && callback=="add") myRacesListFragment.addRaceFinish(result);
                else myRacesListFragment.loadRaces(result);
            }
            else if(myRacesDetailFragment!=null){
                if(callback!=null && callback=="remove") myRacesDetailFragment.removeRaceFinish();
                else myRacesDetailFragment.saveRaceFinish();
            }
            else if(racesUsersListFragment!=null) racesUsersListFragment.loadUsersFinish(result);
            else if(racesDetailFragment!=null){
                if(callback!=null){
                    switch (callback){
                        case "join": racesDetailFragment.joinRaceFinish(); break;
                        case "leave": racesDetailFragment.leaveRaceFinish(); break;
                        case "tag": racesDetailFragment.updateTagFinish(result); break;
                        case "untag": racesDetailFragment.updateUntagFinish(result); break;
                    }
                }
            }
            else if(menuActivity!=null && callback=="logout"){
                menuActivity.logoutFinish();
            }
            else System.out.println(result);
        }else System.out.println("result is empty");
    }
}
