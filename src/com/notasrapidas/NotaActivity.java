package com.notasrapidas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.notasrapidas.model.Nota;
import com.notasrapidas.sqlite.NotaDAO;

public class NotaActivity extends Activity {

	long id;
	EditText texto;
	Button save, cancel;
	
	/**
	 * Crea las vistas.
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nota);

		texto = (EditText) findViewById(R.id.texto);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		
		save.setOnClickListener(new View.OnClickListener() {	
			@Override public void onClick(View v) {
				guardar();
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	/**
	 * Guarda la nota en la base de datos.
	 */
	private void guardar() {
		Nota nota = new Nota();
		nota.setId(id);
		nota.setFecha(System.currentTimeMillis());
		nota.setTexto(texto.getText().toString());
		
		NotaDAO dao = NotaDAO.getInstance(this);

		if (dao.save(nota)) {
			Toast.makeText(getApplicationContext(), R.string.success_save, Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			finish();
		}
	}
	
	/**
	 * Carga los datos recibidos por el intent
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Bundle extras = savedInstanceState;
		if (extras == null) {
			extras = getIntent().getExtras();
		}
		onRestoreInstanceState((extras == null) ? new Bundle() : extras);
	}
	
	/**
	 * Se restauran los datos que se guardaron en {@link #onSaveInstanceState(Bundle)}
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		id = savedInstanceState.getLong("id", 0);
		texto.setText(savedInstanceState.getCharSequence("texto"));
	}
	
	/**
	 * Cuando se gira la pantalla se guardan los datos de los componentes
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong("id", id);
		outState.putCharSequence("texto", texto.getText());
		super.onSaveInstanceState(outState);
	}
}
