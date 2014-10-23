package com.example.proyecto1;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class agregarmateria extends Fragment {
	
	EditText nombre;
	EditText creditos;
	Button agregar;
	Button cancelar;
	helper helper;
	Object item = null;
	
	Spinner sp;
	protected static Context mContext;
	protected JSONObject mData;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.agregarasig, container, false);
		agregar = (Button) rootView.findViewById(R.id.button1);
		creditos = (EditText) rootView.findViewById(R.id.editText2);
		sp = (Spinner) rootView.findViewById(R.id.spinner);
		
		
		agregar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String snombre= sp.getSelectedItem().toString();
				String screditos= creditos.getText().toString();
				if(snombre!="null"&&!screditos.isEmpty()){
					try {
						
						helper = OpenHelperManager.getHelper(rootView.getContext(),helper.class);
						RuntimeExceptionDao<materias, String> materiasDao = helper.getMateriaRuntimeDao();
						materias materia = new materias(snombre,Integer.parseInt(screditos));
						materiasDao.create(materia);
						//JSONArray jsonPosts = mData.getJSONArray("materias");
						
						
						int i = (int) sp.getSelectedItemId();
						JSONArray jsonPosts = mData.getJSONArray("materias").getJSONObject(i).getJSONArray("componetes");
										
						for (int j = 0;j< jsonPosts.length();j++){
							JSONObject post = jsonPosts.getJSONObject(j);
							helper = OpenHelperManager.getHelper(rootView.getContext(),helper.class);
							RuntimeExceptionDao<notas, String> notassDao = helper.getNotasRuntimeDao();
							notas nota = new notas(materia ,Html.fromHtml(post.getString("desc")).toString(), Integer.parseInt(Html.fromHtml(post.getString("peso")).toString()));
							notassDao.create(nota);
							Toast.makeText(rootView.getContext(),"Nota agregada exitosamente" , Toast.LENGTH_SHORT).show();
							getActivity().getSupportFragmentManager().popBackStack();
						}
												
						//}
						Toast.makeText(rootView.getContext(),"Materia agregada exitosamente" , Toast.LENGTH_SHORT).show();
						getActivity().getSupportFragmentManager().popBackStack();

					}
					catch (NumberFormatException e) {
						Toast.makeText(rootView.getContext(), "Los numeros no son validos", Toast.LENGTH_SHORT).show();
						OpenHelperManager.releaseHelper();
					}
					catch (Exception e) {
						
						Toast.makeText(rootView.getContext(),"La materia ya existe" , Toast.LENGTH_SHORT).show();
						OpenHelperManager.releaseHelper();
					}
			
				}else {
					Toast.makeText(rootView.getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
				}
			
			}
			
		});
		return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	mContext = getActivity().getApplicationContext();
    	
		if (isNetworkAvailable()) {
			GetDataTask getDataTask = new GetDataTask();
			getDataTask.execute();
		}
    }
	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(mContext.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		boolean isNetworkAvaible = false;
		if (networkInfo != null && networkInfo.isConnected()) {
			isNetworkAvaible = true;
			Toast.makeText(mContext, "Network is available ", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(mContext, "Network not available ", Toast.LENGTH_LONG)
					.show();
		}
		return isNetworkAvaible;
	}
	
	public void handleBlogResponse() {
		if (mData == null){
			//updateDisplayForError();
		} else {
			try {
				JSONArray jsonPosts = mData.getJSONArray("materias");
				ArrayList<HashMap<String, String>> blogPosts = new ArrayList<HashMap<String,String>>();
				ArrayList<String> nombres= new ArrayList<String>();
				for (int i = 0;i< jsonPosts.length();i++){
					JSONObject post = jsonPosts.getJSONObject(i);
					String nombre = post.getString("nombre_materia");
					nombre = Html.fromHtml(nombre).toString();
					String periodo = post.getString("periodo");
					periodo = Html.fromHtml(periodo).toString();
					nombres.add(nombre+" "+periodo);
										
				}
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, nombres); //selected item will look like a spinner set from XML
				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp.setAdapter(spinnerArrayAdapter);
				
				
			} catch (JSONException e) {
				//Log.e(TAG,"Exception caught!",e);
			}
		}
		 
	}

	
public static JSONObject getJson(String url){
		
		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;
		
		// HTTP
		try {	    	
			HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
			HttpGet httppost = new HttpGet(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch(Exception e) {
			return null;
		}
	    
		// Read response to string
		try {	    	
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();	            
		} catch(Exception e) {
			return null;
		}
 
		// Convert string to object
		try {
			jsonObject = new JSONObject(result);            
		} catch(JSONException e) {
			return null;
		}
    
		return jsonObject;
 
	}
	
	public class GetDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			
			return getJson("http://augustodesarrollador.com/promedio_app/read.php");
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			mData = result;
			handleBlogResponse();
		}

	}

}
