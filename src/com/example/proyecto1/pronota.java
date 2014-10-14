package com.example.proyecto1;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class pronota extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.deseadomateria, container, false);
		
		ListView lv = (ListView) rootView.findViewById(R.id.listView1);
		String n = getArguments().getString("nombre");
		int c = getArguments().getInt("credtios");
		double nt = getArguments().getDouble("nota");
		double deseado = getArguments().getDouble("deseada");
		materias materia = new materias(n,c);
		materia.setNota(nt);
		helper helper= OpenHelperManager.getHelper(rootView.getContext(),helper.class);
		RuntimeExceptionDao<notas, String> notasDao = helper.getNotasRuntimeDao();
		List<notas> listnot = notasDao.queryForEq("nombremateria_id", materia);
		double cre = 0;
		double nocre = 0;
		double snocre = 0;
		
		for (notas notas : listnot) {
			if(notas.getNota()==-1.0){
				cre+=notas.getCreditos();
			}else{
				nocre+=notas.getCreditos();
				snocre+=notas.getCreditos()*notas.getNota();
			}
		}
		//Toast.makeText(rootView.getContext(),deseado+"", Toast.LENGTH_SHORT).show();
		double de = ((deseado*(cre+nocre))-snocre)/cre;
		List<String> esta = new ArrayList<String>();
		boolean sw = true;
		boolean sw2 = true;
		for (notas notas : listnot) {
			sw=false;
			if(notas.getNota()==-1.0){
				sw2 = false;
				notas.setNota(de);
				esta.add(notas.getNombre()+" - "+Math.round(notas.getNota()*100.0)/100.0);
			}else{
				esta.add(notas.getNombre()+" - "+Math.round(notas.getNota()*100.0)/100.0+"*");
			}
		}
		if(sw){
			esta.add("No hay evaluciones registradas");
		}
		if(sw2){
			esta.add("No hay ninguna evaluacion editable");
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                rootView.getContext(),
                android.R.layout.simple_list_item_1, esta );
		lv.setAdapter(arrayAdapter);
		
		Button btt = (Button) rootView.findViewById(R.id.button1);
		btt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		return rootView;
	}
}
