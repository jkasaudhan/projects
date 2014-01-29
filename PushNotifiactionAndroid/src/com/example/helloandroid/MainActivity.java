package com.example.helloandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	EditText msg;
	TextView tweetsView;  
	ListView msgList;
	List<String> tweetMessages ;
	ArrayAdapter<String> adapter;
	GCMBroadcastReceiver gcmr;
	int notificationID = 20;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.form);
        msg = (EditText)findViewById(R.id.editText1);
        //tweetsView = (TextView) findViewById(R.id.textView1);
        tweetMessages = new ArrayList<String>();
        msgList = (ListView) findViewById(R.id.listView1);
       
        
       
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    //on tweet button pressed
   public void send(View v){
    	System.out.println("Message sent: " + msg.getText().toString());
    	//display message on display board and clear the text input area.
    	//tweetsView.setText(msg.getText().toString());
    	tweetMessages.add(msg.getText().toString());
    	adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tweetMessages);
    	msgList.setAdapter(adapter);
    	msg.setText("");
    	//testing http post request works or not
    	/*Log.d("C2DM", "testing http post request");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://localhost/gcm_server/register.php");
		Log.d("C2DM", "testing http post request2");
		try{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			// Get the deviceID
			nameValuePairs.add(new BasicNameValuePair("name", "jitendra"));
		    nameValuePairs.add(new BasicNameValuePair("email", "jiten.ktm@gmail.com"));
		    nameValuePairs.add(new BasicNameValuePair("regID", "fsdf345"));
		    Log.d("C2DM", "testing http post request3");
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    Log.d("C2DM", "testing http post request4");
		    HttpResponse response = client.execute(post);
		   // BufferedReader buffReader = new BufferedReader( new InputStreamReader(response.getEntity().getContent()));
		    Log.d("C2DM", "testing http post request5");
		   // String line = "";
		   // while((line = buffReader.readLine())!= null){
		    //	Log.e("HttpResponse =", line);
		   // }
		}
		catch(IOException e){
			e.printStackTrace();
		}*/
    	
    	
    	//gcm registration code
    	Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");

    	registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));

    	registrationIntent.putExtra("sender", "941079637736");//Sender id = 941079637736

    	startService(registrationIntent);
    	notify("GCM notification","Hello Brother");
		
    	
    }
  
   public void notify(String title,String message)
   {
	   NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(message);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this,MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(notificationID, mBuilder.build());
   }
}
