package com.notasrapidas.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

// Constantes

	private static SQLite mSQLite = null;

	public static final String NAME = "com.notasrapidas.db";//"notas.db";
	public static final int VERSION = 1;
	

// Constructor

	private SQLite(Context context) {
		super(context, NAME, null, VERSION);
	}

// Funciones

	@Override
	public void onCreate(SQLiteDatabase db) {
		NotaDAO.createTable(db);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			NotaDAO.dropTable(db);
			this.onCreate(db);
		}
	}

	public static SQLite getInstance(Context context) {
		if (mSQLite == null) {
			mSQLite = new SQLite(context.getApplicationContext());
		}
		return mSQLite;
	}
}