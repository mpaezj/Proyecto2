package com.example.proyecto1;



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
						
						JSONArray jsonPosts = mData.getJSONArray("notas");
						ArrayList<HashMap<String, String>> blogPosts = new ArrayList<HashMap<String,String>>();
						ArrayList<String> nombres= new ArrayList<String>();
						for (int i = 0;i< jsonPosts.length();i++){
							JSONObject post = jsonPosts.getJSONObject(i);
							String nombre = Html.fromHtml(post.getString("seccion_codigo")).toString();
							if(nombre.equalsIgnoreCase(sp.getSelectedItem().toString())){
								helper = OpenHelperManager.getHelper(rootView.getContext(),helper.class);
								RuntimeExceptionDao<notas, String> notassDao = helper.getNotasRuntimeDao();
								notas nota = new notas(materia ,Html.fromHtml(post.getString("nota_nombre")).toString(), Integer.parseInt(Html.fromHtml(post.getString("nota_porcentaje")).toString()));
								notassDao.create(nota);
								Toast.makeText(rootView.getContext(),"Nota agregada exitosamente" , Toast.LENGTH_SHORT).show();
								getActivity().getSupportFragmentManager().popBackStack();
							}
												
						}
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
				JSONArray jsonPosts = mData.getJSONArray("notas");
				ArrayList<HashMap<String, String>> blogPosts = new ArrayList<HashMap<String,String>>();
				ArrayList<String> nombres= new ArrayList<String>();
				for (int i = 0;i< jsonPosts.length();i++){
					JSONObject post = jsonPosts.getJSONObject(i);
					String nombre = post.getString("seccion_codigo");
					nombre = Html.fromHtml(nombre).toString();
					nombres.add(nombre);
										
				}
				HashSet<String> hashSet = new HashSet<String>(nombres);
				nombres.clear();
				nombres.addAll(hashSet);
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, nombres); //selected item will look like a spinner set from XML
				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp.setAdapter(spinnerArrayAdapter);
				
				
			} catch (JSONException e) {
				//Log.e(TAG,"Exception caught!",e);
			}
		}
		 
	}

	
	
	public class GetDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			int responseCode = -1;
			JSONObject jsonResponse = null;
			try {
				URL blogFeedUsr = new URL(
						"http://ylang-ylang.uninorte.edu.co:8080/promedioapp/read.php");
				HttpURLConnection connection = (HttpURLConnection) blogFeedUsr
						.openConnection();
				connection.connect();

				responseCode = connection.getResponseCode();

				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream inputStram = connection.getInputStream();
					Reader reader = new InputStreamReader(inputStram);
					char[] charArray = new char[connection.getContentLength()];
					reader.read(charArray);
					String responseData = new String(charArray);
					//Log.v(TAG,responseData);
					jsonResponse = new JSONObject(responseData);
				} else {
				//	Log.i(TAG,
				//			"Response code unsuccesfull "
				//					+ String.valueOf(responseCode));
				}
			} catch (MalformedURLException e) {
				//Log.e(TAG, "Exception", e);
			} catch (IOException e) {
				//Log.e(TAG, "Exception", e);
			} catch (Exception e) {
				//Log.e(TAG, "Exception", e);
			}
			return jsonResponse;
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			mData = result;
			handleBlogResponse();
		}

	}

}
