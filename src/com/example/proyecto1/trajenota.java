package com.example.proyecto1;

import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class trajenota extends Fragment {
	private Button botonagregar ;
	private Button botoncalcular;
	private agregarnota agregarnota;
	private EditText tx;
	List<notas> listnot;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.notas, container, false);
		
		botonagregar = (Button) rootView.findViewById(R.id.button1);
		botoncalcular = (Button) rootView.findViewById(R.id.button2);
		tx = (EditText) rootView.findViewById(R.id.editText3);

		ListView list = (ListView) rootView.findViewById(R.id.listView1);
		list.setClickable(true);
		
		helper helper= OpenHelperManager.getHelper(rootView.getContext(),helper.class);
		RuntimeExceptionDao<notas, String> notasDao = helper.getNotasRuntimeDao();
		listnot = notasDao.queryForAll();
		String n = getArguments().getString("nombre");
		int c = getArguments().getInt("credtios");
		double nt = getArguments().getDouble("nota");
		materias materia = new materias(n,c);
		materia.setNota(nt);
		try{
			listnot = notasDao.queryForEq("nombremateria_id", materia);
		}catch(Exception e){
			Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			
		}
				
		
		//Toast.makeText(rootView.getContext(), listmat.get(0).getNombre(), Toast.LENGTH_SHORT).show();
		adanota ada = new adanota(rootView.getContext(), listnot);
		list.setAdapter(ada);
		
		
		
		botonagregar.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				agregarnota = new agregarnota();
				Bundle args = new Bundle();
				args.putString("nombre", getArguments().getString("nombre"));
				args.putInt("creditos",getArguments().getInt("credtios"));
				args.putDouble("nota", getArguments().getDouble("nota"));
				agregarnota.setArguments(args);
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, agregarnota).addToBackStack("agregarnotas")
						.commit();
			}
		});
		
		botoncalcular.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
				pronota pronota = new pronota();
				Bundle args = new Bundle();
				args.putString("nombre", getArguments().getString("nombre"));
				args.putInt("creditos",getArguments().getInt("credtios"));
				args.putDouble("nota", getArguments().getDouble("nota"));
				args.putDouble("deseada", Double.parseDouble(tx.getText().toString()));
				 
				pronota.setArguments(args);
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, pronota).addToBackStack("pronota")
						.commit();
				}catch(Exception e){
					Toast.makeText(rootView.getContext(), "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

	
		return rootView;
	}
}
