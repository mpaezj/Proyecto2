package com.example.proyecto1;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


public class helper extends OrmLiteSqliteOpenHelper {
	private static String  DATABASE_NAME = "notasestudiantes.db";
	private static final int DATABASE_VERSION = 1;
	
	private Dao<materias, String> materiaDao = null;
	private RuntimeExceptionDao<materias, String> materiaRuntimeDao = null;
	
	private Dao<notas, String> notasDao = null;
	private RuntimeExceptionDao<notas, String> notasRuntimeDao = null;
	
	public helper (Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, materias.class);
			TableUtils.createTable(connectionSource, notas.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(connectionSource, materias.class, true);
			TableUtils.dropTable(connectionSource, notas.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public void close() {
		super.close();
		materiaDao = null;
		materiaRuntimeDao = null;
		notasDao = null;
		notasRuntimeDao = null;
	}

	public Dao<materias, String> getmasteriasDao() throws SQLException {
		if(materiaDao == null) materiaDao = getDao(materias.class);
		return materiaDao;
	}
	
	
	
	public RuntimeExceptionDao<materias, String> getMateriaRuntimeDao() {
		if(materiaRuntimeDao==null) materiaRuntimeDao=getRuntimeExceptionDao(materias.class);
		return materiaRuntimeDao;
	}

	public RuntimeExceptionDao<notas, String> getNotasRuntimeDao() {
		if(notasRuntimeDao==null)notasRuntimeDao=getRuntimeExceptionDao(notas.class);
		return notasRuntimeDao;
	}

	public Dao<notas, String> getnotasDao() throws SQLException{
		if(notasDao == null) notasDao = getDao(notas.class);
		return notasDao;
	}

	
}
