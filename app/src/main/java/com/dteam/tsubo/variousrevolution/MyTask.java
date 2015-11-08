package com.dteam.tsubo.variousrevolution;

/**
 * Created by takahiroiwatani on 15/11/08.
 */
public class MyTask extends AsyncTask<Integer, Integer, Integer> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String doInBackground(String… params)
    {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(params[0]);
        byte[] result = null;
        String str = “”;
        try{
            HttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, “UTF-8″);
            }
        }
        catch (Exception e) {
        }
        return str;
    }

    /**
     * onPostExecute
     * @param String result 通信結果
     * 結果を受け取ったときに・・・メインスレッド
     */
    @Override
    protected void onPostExecute(String result)
    {
        try {
            JSONObject json = new JSONObject(result);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
