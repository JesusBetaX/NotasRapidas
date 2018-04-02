package com.notasrapidas.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Nota {

// Variables	
	
	private long id;
	private long fecha;
	private String texto;
	
// Constructores	
	
	public Nota() {
		// TODO Auto-generated constructor stub
	}
	
	public Nota(Cursor cursor) {
		this.id = cursor.getLong(0);
		this.fecha = cursor.getLong(1);
		this.texto = cursor.getString(2);
	}
	
// Funciones	
	
	public ContentValues values() {
		ContentValues values = new ContentValues(2);
		values.put("fecha", this.fecha);
		values.put("texto", this.texto);
		return values;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getFecha() {
		return fecha;
	}
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
}