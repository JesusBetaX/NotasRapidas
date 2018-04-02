package com.notasrapidas.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.notasrapidas.R;
import com.notasrapidas.model.Nota;

public class NotaAdapter extends BaseAdapter {
	
// Variables
	
	/* 
	 * Inflador del la vista. 
	 */
	private final LayoutInflater mInflater;
	
	/* 
	 * Coleccion. 
	 */
	private final List<Nota> mList;
	
	/*
	 * Fecha.
	 */
	private final Calendar mCalendar;
	
	
// Constructor
	
	public NotaAdapter(Context context) {
		mInflater = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mList = new ArrayList<Nota>();
		mCalendar = new GregorianCalendar();
	}
	
// Funciones
	
	public void setAll(List<Nota> list) {
		synchronized (this) {
			mList.clear();
			mList.addAll(list);
		}
		// Notifica el cambio al ListView.
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder h;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_list, parent, false);
			h = new Holder(convertView);
			convertView.setTag(h);
		} 
		else {
			h = (Holder) convertView.getTag();
		}

		final Nota nota = mList.get(position);
		h.texto.setText(nota.getTexto());
		mCalendar.setTimeInMillis(nota.getFecha());
		h.fecha.setText(DateFormat.format("dd MMMM, yyyy", mCalendar));
		h.hora.setText(DateFormat.format("h:mm a", mCalendar));
		
		return convertView;
	}
	

// Extras
	
	class Holder {
		TextView texto, fecha, hora;
		
		public Holder(View convertView) {
			texto = (TextView) convertView.findViewById(R.id.texto);
			fecha = (TextView) convertView.findViewById(R.id.fecha);
			hora = (TextView) convertView.findViewById(R.id.hora);
		}
	}
}