package nl.samtimo.pubcrawl;

import android.os.AsyncTask;
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

    private LoginActivity loginActivity;

    public RequestTask(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
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

            if(request.getMethod()==RequestMethod.POST){
                connection.setDoOutput(true);
                connection.setUseCaches(false);
            }

            connection.setRequestProperty("Cookie", TextUtils.join(";", cookieManager.getCookieStore().getCookies()));

            if(request.getParameters()!=null) {
                byte[] postData = request.getParameters().getBytes(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT ? StandardCharsets.UTF_8 : Charset.forName("UTF-8"));
                connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
            e.printStackTrace();
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
        if(loginActivity!=null && result!=null && result.equals("authorized"))
            loginActivity.openMenu();
    }
}
