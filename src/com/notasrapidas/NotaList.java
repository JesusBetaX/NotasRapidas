package com.notasrapidas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.notasrapidas.model.Nota;
import com.notasrapidas.sqlite.NotaDAO;
import com.notasrapidas.widget.NotaAdapter;

public class NotaList extends Activity 
	implements AdapterView.OnItemClickListener, View.OnClickListener {

	public static final int REQUEST_INSERT = 1;
	public static final int REQUEST_UPDATE = 2;
	
	private NotaAdapter mAdapter;	
	
	ListView list;
	
	/**
	 * Crea las vistas.
	 * @param savedInstanceState
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
      
        list = (ListView) findViewById(R.id.list);
        list.setEmptyView(findViewById(R.id.empty));
        list.setOnItemClickListener(this);
        
        View btnNueva = findViewById(R.id.add);
        btnNueva.setOnClickListener(this);
    }
    
    /**
	 * Carga los datos en las vistas.
	 * @param savedInstanceState
	 */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	
    	mAdapter = new NotaAdapter(this);
    	list.setAdapter(mAdapter);
    	
    	refrescarLista();
    }
    
    /**
     * Metodo para refrescar el ListView.
     */
    private void refrescarLista() {
    	NotaDAO dao = NotaDAO.getInstance(this);
		mAdapter.setAll(dao.getAll());
	}
    
    /**
	 * Lanza un Intent para agregar una nueva nota.
     */
	@Override 
	public void onClick(View v) {
		Intent i = new Intent(getApplicationContext(), NotaActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(i, REQUEST_INSERT);	
	}

    /**
     * Valida que item de ListView se a seleccionado.
     */
    @Override
    public void onItemClick(AdapterView<?> l, View view, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), NotaDetalles.class);
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final Nota nota = (Nota) l.getItemAtPosition(position);
		i.putExtra("id", nota.getId());
		i.putExtra("fecha", nota.getFecha());
		i.putExtra("texto", nota.getTexto());
		
		startActivityForResult(i, REQUEST_UPDATE);
    }
    
    /**
	 * Resultado obtenido del activity lanzado por el metodo 
	 * {@link #startActivityForResult(Intent, int)}
	 * 
	 * @param requestCode código para identificar la petición 
	 * que se lanzo en el metodo {@link #startActivityForResult(Intent, int)}
	 * 
	 * @param resultCode código de respuesta que devolvió la 
	 * actividad lanzada a través de su metodo {@link #setResult(int)}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch(requestCode) {
				// Si se agrego una nueva nota, se refresca la lista y
				// se selecciona la primera posición de la lista.
				case REQUEST_INSERT:
					refrescarLista();
					list.setSelection(0);
					break;
					
				// Si se modifico o elimino una nota, se refresca la lista.
				case REQUEST_UPDATE:
					refrescarLista();
					break;
			}
		}
	}

	/*
	for (int x=0; x < mAdapter.getCount(); x++) {
		View v = list.getChildAt(x);
		CheckBox cb = (CheckBox) v.findViewById(R.id.cb_lv);
		if (cb.isChecked()) {
			TextView txt = (TextView) v.findViewById(R.id.subtitulo_lv);
			Toast.makeText(getApplicationContext(), txt.getText(), Toast.LENGTH_SHORT).show();
		}
	}
	*/
}
