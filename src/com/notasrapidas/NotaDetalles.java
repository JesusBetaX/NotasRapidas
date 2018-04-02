package com.notasrapidas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.notasrapidas.sqlite.NotaDAO;

public class NotaDetalles extends Activity 
	implements View.OnClickListener {

	long id;
	TextView texto;
	Button edit, delete;
	
	/**
	 * Crea las vistas.
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalles);

		texto = (TextView) findViewById(R.id.texto);
		edit = (Button) findViewById(R.id.edit);
		delete = (Button) findViewById(R.id.delete);
		
		texto.setOnClickListener(this);
		edit.setOnClickListener(this);
		
		delete.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				showDialogDelete();
				((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
			}
		});
	}
	
	/**
	 * Carga los datos en las vistas.
	 * @param savedInstanceState
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		View nav = findViewById(R.id.nav);
		
		ImageView icon = (ImageView) nav.findViewById(R.id.icon);
		icon.setImageResource(R.drawable.ic_nota);
		
		TextView title = (TextView) nav.findViewById(R.id.title);
		title.setText(DateFormat.format("EEEE, dd MMMM, yyyy", getIntent().getLongExtra("fecha", System.currentTimeMillis())));
		
		id = getIntent().getLongExtra("id", 0);
		texto.setText(getIntent().getStringExtra("texto"));
	}

	/**
	 * Lanza un Intent para editar la nota.
	 */
	public void onClick(View v) {
		Intent i = new Intent(getApplicationContext(), NotaActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		i.putExtras(getIntent());
		startActivityForResult(i, NotaList.REQUEST_UPDATE);
	};

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
		setResult(resultCode, data);
		finish();
	}
	
	/**
	 * Muestra un dialogo para confirmar la eliminacion.
	 */
	private void showDialogDelete() {
		new AlertDialog.Builder(this)
			.setTitle(R.string.delete)
			.setIcon(android.R.drawable.ic_delete)
			.setMessage(R.string.question_delete)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				@Override public void onClick(DialogInterface dialog, int id) {
					delete();
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create()
			.show();
	}
	
	/**
	 * Elimina la nota.
	 */
	private void delete() {
		NotaDAO dao = NotaDAO.getInstance(this);
		
		if (dao.delete(id)) {
			Toast.makeText(getApplicationContext(), R.string.success_delete, Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			finish();
		}
	}
}
