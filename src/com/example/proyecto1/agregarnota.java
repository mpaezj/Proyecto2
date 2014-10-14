package com.example.proyecto1;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class agregarnota extends Fragment {

	EditText nombre;
	EditText porcentaje;
	Button agregar;
	Button cancelar;
	helper helper;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.agregarnotas, container, false);
		nombre = (EditText) rootView.findViewById(R.id.editText1);
		porcentaje = (EditText) rootView.findViewById(R.id.editText2);
		agregar = (Button) rootView.findViewById(R.id.button1);
		cancelar = (Button) rootView.findViewById(R.id.button2);
		
		String n = getArguments().getString("nombre");
		int c = getArguments().getInt("credtios");
		double nt = getArguments().getDouble("nota");
		final materias materia = new materias(n,c);
		materia.setNota(nt);
		
		agregar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String snombre= nombre.getText().toString();
				String screditos= porcentaje.getText().toString();
				if(!snombre.isEmpty()&&!screditos.isEmpty()){
					try {
						
						helper = OpenHelperManager.getHelper(rootView.getContext(),helper.class);
						RuntimeExceptionDao<notas, String> notassDao = helper.getNotasRuntimeDao();
						notas nota = new notas(materia ,snombre, Integer.parseInt(screditos));
						notassDao.create(nota);
						Toast.makeText(rootView.getContext(),"Nota agregada exitosamente" , Toast.LENGTH_SHORT).show();
						getActivity().getSupportFragmentManager().popBackStack();

					}
					catch (NumberFormatException e) {
						Toast.makeText(rootView.getContext(), "Los numeros no son validos", Toast.LENGTH_SHORT).show();
						OpenHelperManager.releaseHelper();
					}
					catch (Exception e) {
						
						Toast.makeText(rootView.getContext(),"La nota ya existe" , Toast.LENGTH_SHORT).show();
						OpenHelperManager.releaseHelper();
					}
			
				}else {
					Toast.makeText(rootView.getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return rootView;
	}
}
