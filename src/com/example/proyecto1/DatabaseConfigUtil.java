package com.example.proyecto1;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	//private static final Class<?>[] classes = new Class[]{notas.class, materias.class};
	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt");
		// TODO Auto-generated method stub
	}

}
