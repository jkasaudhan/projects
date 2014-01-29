package com.example.helloandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.net.MailTo;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMBroadcastReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		//check if received messege is registration related information or message for users
		if("com.google.android.c2dm.intent.REGISTRATION".equals(action)){
			
			Log.w("C2DM","Receieved registration_id ");
			final String registrationID = intent.getStringExtra("registration_id");
			String error = intent.getStringExtra("error");
			Log.d("C2DM","registration_id =" + registrationID +",error =" + error);
			//sendRegistrationIDTOServer("", registrationID);
			
			
		}else if("com.google.android.c2dm.intent.RECEIVE".equals(action)){
			
			Log.w("C2DM","Received message");
			String message = intent.getStringExtra("message");
	
		}
		
	}
	
	public void sendRegistrationIDTOServer(String deviceID,String registrationID){
		

	}

}
