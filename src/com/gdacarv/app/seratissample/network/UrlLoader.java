package com.gdacarv.app.seratissample.network;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
	
public class UrlLoader {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public <T> void load(String url, Parser<T> parser, Receiver<T> receiver) {
		AsyncTask<String, Void, T> asyncTask = new UrlLoaderAsyncTask<T>(parser, receiver);
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		else
			asyncTask.execute(url);
	}

	private final class UrlLoaderAsyncTask<T> extends AsyncTask<String, Void, T> {
		
		private static final String TAG = "UrlLoaderAsyncTask";
		private Parser<T> mParser;
		private Receiver<T> mReceiver;
		
		public UrlLoaderAsyncTask(Parser<T> parser, Receiver<T> receiver) {
			mParser = parser;
			mReceiver = receiver;
		}

		@Override
		protected T doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				InputStream inputStream = urlConnection.getInputStream();
				try {
					return mParser.parse(inputStream);
				} finally {
					if(inputStream != null)
						inputStream.close();
					urlConnection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(T result) {
			super.onPostExecute(result);
			mReceiver.onReceive(result);
		}
	}

}
