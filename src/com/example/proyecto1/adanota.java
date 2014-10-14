package com.example.proyecto1;

import java.util.List;

import android.content.Context;
import android.sax.TextElementListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class adanota extends BaseAdapter implements  OnClickListener {
	private Context context;
	private List<notas> nota;
	
	public adanota(Context context, List<notas> nota) {
		
		this.context = context;
		this.nota = nota;
	}
	@Override
	public int getCount() {
		return nota.size();
	}

	@Override
	public Object getItem(int position) {
		return nota.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final notas entry = nota.get(position);
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rownota, null);
		}
        TextView tvContact = (TextView) convertView.findViewById(R.id.textView1);
        tvContact.setText(entry.getNombre());

        TextView tvPhone = (TextView) convertView.findViewById(R.id.textView3);
        tvPhone.setText(entry.getCreditos()+"");     
        
        final EditText tvPhone2 = (EditText) convertView.findViewById(R.id.editText1);
        if(entry.getNota()!=-1.0){
        	tvPhone2.setText(entry.getNota()+"");
        }else {
        	tvPhone2.setText("");
        }
        
        tvPhone2.setOnEditorActionListener(
        	    new EditText.OnEditorActionListener() {
        	    	@Override
        	    	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        	    	   
        						try{
        							
        							helper helper = OpenHelperManager.getHelper(context,helper.class);
            						RuntimeExceptionDao<notas, String> notassDao = helper.getNotasRuntimeDao();
            						if("".equalsIgnoreCase(tvPhone2.getText().toString())){
            							entry.setNota(-1.0);
            						}else{
            							entry.setNota(Double.parseDouble(tvPhone2.getText().toString()));
            						}
            						
        							notassDao.update(entry);
        						}catch(Exception e){
        							Toast.makeText(context,e.getMessage() , Toast.LENGTH_SHORT).show();
        						}
        						
        	    	       return false; 
        	    	}
        	    	});
        
        Button btnRemove = (Button) convertView.findViewById(R.id.button1);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnRemove.setOnClickListener(this);
        btnRemove.setTag(entry);
        
        
          
        
        return convertView;
          
	}
	@Override
	public void onClick(View view) {
	     notas entry = (notas) view.getTag();
	     nota.remove(entry);
	     notifyDataSetChanged();
	     helper helper= OpenHelperManager.getHelper(context,helper.class);
		 RuntimeExceptionDao<notas, String> notasDao = helper.getNotasRuntimeDao();
		 notasDao.delete(entry);
	     

	}

	
	

}
