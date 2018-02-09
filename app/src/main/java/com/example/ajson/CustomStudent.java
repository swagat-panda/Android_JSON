package com.example.ajson;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 12/24/2017.
 */

public class CustomStudent extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] userid;
    private final String[] password;
    public CustomStudent(Activity context,
                         String[] userid, String[] password) {
        super(context, R.layout.userlist, userid);

        this.context = context;
        this.userid = userid;
        this.password = password;
    }
    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.userlist, null, true);
        TextView txtUserid = (TextView) rowView.findViewById(R.id.txt_userid);
        txtUserid.setText(userid[position]);
        TextView txtPassword = (TextView) rowView.findViewById(R.id.txt_password);
        txtPassword.setText(password[position]);

        final TableRow studentrow = (TableRow) rowView.findViewById(R.id.userrow);
        Button delbtn = (Button) rowView.findViewById(R.id.btn_delete);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExecuteTask().execute(userid[position]);
                studentrow.setVisibility(view.GONE);
            }
        });


        return rowView;
    }

    class ExecuteTask extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String res=FetchData(params);
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }

    }
    public String FetchData(String[] valuse) {

        String s="";
        String url="http://192.168.43.57:8181/android/manageuser.php";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("user_id", valuse[0]));
            //end new data
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            s = readdata(httpResponse);
        } catch (Exception exception) {
        }
        return s;

    }
    public String readdata(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e) {

        }
        return return_text;
    }




}

