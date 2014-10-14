package com.example.proyecto1;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class inicio extends Fragment {
	private Button botonagregar;
	private Button botoncalcular;
	private agregarmateria agregarmateria;
	List<materias> listmat;
	private EditText dee;
	private EditText dee2;
	private EditText dee3;
	private SharedPreferences mSharedPreferences;
	private String skey = "PREF_CHECKBOX_STRING";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.inicial, container,
				false);

		dee = (EditText) rootView.findViewById(R.id.editText3);
		dee2 = (EditText) rootView.findViewById(R.id.editText2);
		dee3 = (EditText) rootView.findViewById(R.id.editText1);

		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(rootView.getContext());
		String text = mSharedPreferences.getString("deseado", "");
		dee.setText(text);
		String text2 = mSharedPreferences.getString("actual", "");
		dee2.setText(text2);
		String text3 = mSharedPreferences.getString("creditos", "");
		dee3.setText(text3);

		// ----

		botonagregar = (Button) rootView.findViewById(R.id.button1);
		botoncalcular = (Button) rootView.findViewById(R.id.button2);

		ListView list = (ListView) rootView.findViewById(R.id.listView1);

		helper helper = OpenHelperManager.getHelper(rootView.getContext(),
				helper.class);
		RuntimeExceptionDao<materias, String> materiasDao = helper
				.getMateriaRuntimeDao();
		listmat = materiasDao.queryForAll();
		if (listmat.size() == 0) {
			List<String> esta = new ArrayList<String>();
			esta.add("Agrege materias para empezar a trabajar");
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					rootView.getContext(), android.R.layout.simple_list_item_1,
					esta);
			list.setAdapter(arrayAdapter);

		} else {

			// Toast.makeText(rootView.getContext(), listmat.get(0).getNombre(),
			// Toast.LENGTH_SHORT).show();
			adamaterias ada = new adamaterias(rootView.getContext(), listmat);
			list.setAdapter(ada);

			list.setClickable(true);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long index) {
					trajenota trajenota = new trajenota();
					Bundle args = new Bundle();
					args.putString("nombre", listmat.get(position).getNombre());
					args.putInt("creditos", listmat.get(position).getCreditos());
					args.putDouble("nota", listmat.get(position).getNota());
					trajenota.setArguments(args);
					FragmentTransaction ft = getActivity()
							.getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.container, trajenota)
							.addToBackStack("trajenota").commit();

				}
			});
		}
		botonagregar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				agregarmateria = new agregarmateria();
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.container, agregarmateria)
						.addToBackStack("agregarmateria").commit();
			}
		});

		botoncalcular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(rootView.getContext());
				Editor editor = sharedPreferences.edit();
				editor.putString("deseado", dee.getText().toString());
				editor.putString("actual", dee2.getText().toString());
				editor.putString("creditos", dee3.getText().toString());
				editor.commit();
				try {
					prosemestre prosemestre = new prosemestre();
					Bundle args = new Bundle();
					args.putDouble("creditos",
							Double.parseDouble(dee3.getText().toString()));
					args.putDouble("deseada",
							Double.parseDouble(dee.getText().toString()));
					args.putDouble("actual",
							Double.parseDouble(dee2.getText().toString()));

					prosemestre.setArguments(args);
					FragmentTransaction ft = getActivity()
							.getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.container, prosemestre)
							.addToBackStack("prosemestre").commit();
				} catch (Exception e) {
					Toast.makeText(rootView.getContext(),
							"Todos los campos deben estar llenos",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		return rootView;
	}

}
