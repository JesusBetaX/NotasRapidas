package com.notasrapidas.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.notasrapidas.model.Nota;

public class NotaDAO {

// Constantes	
	
	private static NotaDAO instance;
	
	static void createTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE nota ("
				+ "id 		INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
				+ "fecha 	INTEGER, "
				+ "texto 	TEXT)");
	}
	
	static void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS nota");
	}
	
// Variables
	
	protected final SQLite sql;	
	
// Constructor
	
	private NotaDAO(Activity activity) {
		this.sql = SQLite.getInstance(activity);
	}
	
// Funciones
	
	/**
	 * @return una lista del recurso.
	 */
	public List<Nota> getAll() {
		SQLiteDatabase db = sql.getReadableDatabase();
		Cursor cursor = db.query("nota", null, null, null,
				null, null, "fecha desc");
		
		final List<Nota> list = new ArrayList<Nota>(cursor.getCount());
		
		try {
			while (cursor.moveToNext()) {
				list.add(new Nota(cursor));
			}
			return list;
		} finally {
			cursor.close();
			sql.close();
		}
	}
	
	/** 
	 * Guarda el registro.
	 * 
	 * @param o modelo
	 * 
	 * @return boolean <b>TRUE</b> exito <b>FALSE</b> fallo
	 */
	public boolean save(Nota o) {
		return (o.getId() == 0) ? insert(o) : update(o);
	}

	/** 
	 * Inserta un registro en la db.
	 * 
	 * @param o modelo
	 * 
	 * @return boolean <b>TRUE</b> exito <b>FALSE</b> fallo
	 */
	public boolean insert(Nota o) {
		ContentValues values = o.values();
		try {
			SQLiteDatabase db = sql.getWritableDatabase();
			o.setId( db.insert("nota", null, values) );
			return o.getId() != -1;			
		} finally {
			sql.close();
		}
	}

	/** 
	 * Actualiza un registro en la db.
	 * 
	 * @param o Modelo
	 * 
	 * @return boolean <b>TRUE</b> exito <b>FALSE</b> fallo
	 */
	public boolean update(Nota o) {
		ContentValues values = o.values();
		final String whereClause = "id = ?";
		final String[] whereArgs = { Long.toString(o.getId()) };
		try {
			SQLiteDatabase db = sql.getWritableDatabase();
			return db.update("nota", values, whereClause, whereArgs) == 1;
		} finally {
			sql.close();
		}
	}

	/** 
	 * Elimina un registro. 
	 * 
	 * @param id identificador
	 * 
	 * @return boolean <b>TRUE</b> exito <b>FALSE</b> fallo
	 */
	public boolean delete(long id) {
		final String whereClause = "id = ?";
		final String[] whereArgs = { Long.toString(id) };
		try {
			SQLiteDatabase db = sql.getWritableDatabase();
			return db.delete("nota", whereClause, whereArgs) == 1;
		} finally {
			sql.close();
		}
	}

	public static NotaDAO getInstance(Activity activity) {
		if (instance == null) {
			instance = new NotaDAO(activity);
		}
		return instance;
	}
}