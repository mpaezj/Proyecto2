package com.example.proyecto1;

import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class adamaterias extends BaseAdapter implements  OnClickListener {
	private Context context;
	private List<materias> materias;
	private Handler handler = new Handler();
	private int lastFocussedPosition = -1;
	
	public adamaterias(Context context, List<materias> materias) {
		
		this.context = context;
		this.materias = materias;
	}
	@Override
	public int getCount() {
		return materias.size();
	}

	@Override
	public Object getItem(int position) {
		return materias.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final materias entry = materias.get(position);
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowinicial, null);
		}
        TextView tvContact = (TextView) convertView.findViewById(R.id.textView1);
        tvContact.setText(entry.getNombre());
        
        

        TextView tvPhone = (TextView) convertView.findViewById(R.id.textView3);
        tvPhone.setText(entry.getCreditos()+"");     
        
        helper helper= OpenHelperManager.getHelper(context,helper.class);
		RuntimeExceptionDao<notas, String> notasDao = helper.getNotasRuntimeDao();
		List<notas> listnot = notasDao.queryForEq("nombremateria_id", entry);
		double nocre = 0;
		double snocre = 0;
		int i=0;
		
		for (notas notas : listnot) {
			if(notas.getNota()==-1.0){
				
			}else{
				i++;
				nocre+=notas.getCreditos();
				snocre+=notas.getCreditos()*notas.getNota();
			}
		}
		double pro = snocre/nocre;
		if(i==listnot.size()&&i!=0){
			entry.setNota(pro);
		}else{
			entry.setNota(-1.0);
		}
		
		try{
			
			//helper helper = OpenHelperManager.getHelper(context,helper.class);
			RuntimeExceptionDao<materias, String> materiasDao = helper.getMateriaRuntimeDao();
			materiasDao.update(entry);
			//notassDao.update(entry);
		}catch(Exception e){
			Toast.makeText(context,e.getMessage() , Toast.LENGTH_SHORT).show();
		}
        
        
        final TextView tvPhone2 = (TextView) convertView.findViewById(R.id.textView4);
        if(entry.getNota()!=-1.0){
        	if(i==listnot.size()){
        		tvPhone2.setText(entry.getNota()+"*");
        	}else{
        		
        	}
        	
        }else {

    		tvPhone2.setText("");
    		if(pro!=0 && i!=0)
    			tvPhone2.setText(pro+"");
        }
        
		
       
        Button btnRemove = (Button) convertView.findViewById(R.id.button1);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnRemove.setOnClickListener(this);
        btnRemove.setTag(entry);
          
        
        return convertView;
          
	}
	@Override
	public void onClick(View view) {
	     materias entry = (materias) view.getTag();
	     materias.remove(entry);
	     notifyDataSetChanged();
	     helper helper= OpenHelperManager.getHelper(context,helper.class);
		 RuntimeExceptionDao<materias, String> materiasDao = helper.getMateriaRuntimeDao();
		 RuntimeExceptionDao<notas, String> notasDao = helper.getNotasRuntimeDao();
		 List<notas> nts =notasDao.queryForEq("nombremateria_id", entry);
		 notasDao.delete(nts);
		 materiasDao.delete(entry);
	     

	}

	
	

}
