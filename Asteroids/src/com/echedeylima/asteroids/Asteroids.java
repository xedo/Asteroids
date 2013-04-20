package com.echedeylima.asteroids;

import android.app.Activity;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
//import android.view.View.OnClickListener;
//import android.widget.Button;

public class Asteroids extends Activity {

	// private Button bAcercaDe;
	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

		// bAcercaDe =(Button) findViewById(R.id.button3);
		// bAcercaDe.setOnClickListener(new OnClickListener(){
		/*
		 * public void onClick(View view){ lanzarAcercaDe(null); } });
		 */
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "onStrat", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		/* getMenuInflater() */inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.acercaDe:
			lanzarAcercaDe(null);
			break;
		case R.id.config:
			lanzarPreferencias(null);
			break;
		}
		return true;
	}

	// Jugar
	public void lanzarJuego(View view) {
		Intent i = new Intent(this, Juego.class);
		startActivity(i);
	}

	// Acerca De
	public void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	// Configuracion
	public void lanzarPreferencias(View view) {
		Intent i = new Intent(this, Preferencias.class);
		startActivity(i);
	}

	// Aceder a preferencias (ejemplo)
	/*
	 * SharedPreferences pref =
	 * getSharedPreferences("org.example.asteroides_preferences",MODE_PRIVATE);
	 * String s = "musica: " + pref.getBoolean("musica", true) + ", gr‡ficos: "
	 * + pref.getString("graficos", "0") + ", fragmentos: "
	 * +pref.getString("fragmentos", "3");
	 */

	// Puntuaciones
	public void lanzarPuntuaciones(View view) {
		Intent i = new Intent(this, Puntuaciones.class);
		startActivity(i);
	}

	// Salir de la aplicacion
	public void exit(View view) {
		finish();
	}

}
