package com.echedeylima.asteroids;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener {

	// Nave
	private Grafico nave;
	private int giroNave;
	private float aceleracionNave;
	private static final int PASO_GIRO_NAVE = 5;
	private static final float PASO_ACELERACION_NAVE = 0.5f;
	private float mX = 0, mY = 0; // ultimas coordenadas
	private boolean disparo = false;
	
	// Misil
	private Grafico misil;
	private static int PASO_VELOCIDAD_MISIL = 12;
	private boolean misilActivo = false;
	private int tiempoMisil;

	// Asteroides
	private Vector<Grafico> Asteroides; // Vector con los asteroides
	private int numAsteroides = 5; // Numero inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en los que se divide

	// Thread
	private ThreadJuego thread = new ThreadJuego();
	private static int PERIODO_PROCESO = 20; // tiempo de actualizacion
	private long ultimoProceso = 0;

	public VistaJuego(Context context, AttributeSet attrs) {
		super(context, attrs);
		Drawable drawableNave, drawableAsteroide, drawableAsteroide2, drawableMisil;

		// nave
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		nave = new Grafico(this, drawableNave);
		
		// misil vectorial
		ShapeDrawable dMisil = new ShapeDrawable (new RectShape());
		dMisil.getPaint().setColor (Color.WHITE);
		dMisil.getPaint().setStyle(Style.STROKE);
		dMisil.setIntrinsicWidth(15);
		dMisil.setIntrinsicHeight(3);
		drawableMisil = dMisil;
		// misil bitmap
		drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
		misil = new Grafico(this, drawableMisil);
		

		// asteroides
		drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroide1);
		drawableAsteroide2 = context.getResources().getDrawable(
				R.drawable.asteroide2);
		Asteroides = new Vector<Grafico>();
		Grafico asteroide;
		for (int i = 0; i < numAsteroides; i++) {
			if (Math.random() < 0.1)
				asteroide = new Grafico(this, drawableAsteroide2);
			else
				asteroide = new Grafico(this, drawableAsteroide);

			// velocidades
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setIncY(Math.random() * 4 - 2);

			// posiciones
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
		}
		
		//Sernsores de movimiento
		SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (!listSensors.isEmpty()){
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}

	// Aciones
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		// coordenadas del evento
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			disparo = true;
			break;
		case MotionEvent.ACTION_MOVE:
			// control de giro
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dy < 6 && dx > 6) {
				giroNave = Math.round((x - mX) / 4);
				disparo = false;
			}
			// control de velocidad
			else if (dx < 6 && dy > 6) {
				if (Math.round(mY - y) > 0)
					aceleracionNave = Math.round((mY - y) / 80);
				disparo = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			giroNave = 0;
			aceleracionNave = 0;
			if (disparo)
				activaMisil();
				break;
		}
		mX = x;
		mY = y;
		return true;
	}
	
	private void destruyeAsteroide (int i){
		Asteroides.remove(i);
		misilActivo = false;
	}
	
	private void activaMisil(){
		misil.setPosX(nave.getPosX() + nave.getAncho() / 2 - misil.getAncho() / 2);
		misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);
		misil.setAngulo(nave.getAngulo());
		misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
		misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
		tiempoMisil = (int) Math.min(this.getWidth() / Math.abs( misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
		misilActivo = true;
	}

	public class ThreadJuego extends Thread {
		@Override
		public void run() {
			while (true) {
				actualizaFisica();
				try {
					Thread.sleep(PERIODO_PROCESO);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	protected synchronized void actualizaFisica() {
		long ahora = System.currentTimeMillis();
		// para la ejecucion si aun no se ha pumplido el periodo de proceso
		if (ultimoProceso + PERIODO_PROCESO > ahora)
			return;
		// para una ejecucion en tiempo real calculamos el retador
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;
		// actualizamos la velocidad y direcci—n de la nave a partir de grioNave
		// y acelereacionNave (segun la entrada del jugador)
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		// actualizamos si el modulo de la velocidad no excede del maximo
		if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()) {
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}
		//actualizamos posicion del misil
		if (misilActivo){
			misil.incrementaPos(retardo);
			tiempoMisil -= retardo;
			if (tiempoMisil < 0){
				misilActivo = false;
			}
			else{
				for (int i = 0; i < Asteroides.size(); i++)
					if (misil.verificaColision(Asteroides.elementAt(i))){
						destruyeAsteroide(i);
						break;
					}
			}
		}
		// actualizamos posiciones
		nave.incrementaPos(retardo);
		for (Grafico asteroide : Asteroides) {
			asteroide.incrementaPos(retardo);
		}
	}

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {
		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		// centrar nave
		// ancho y alto aqui hacen referencia al tama–o de la pantalla
		nave.setPosX(ancho / 2 - nave.getAncho() / 2);
		nave.setPosY(alto / 2 - nave.getAlto() / 2);

		// colocar asteroides
		for (Grafico asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}

		ultimoProceso = System.currentTimeMillis();
		thread.start();
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// dibujar nave
		nave.dibujaGrafico(canvas);

		// dibujar asteroides
		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
		
		//dibujar misil
		if (misilActivo)
			misil.dibujaGrafico(canvas);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {	}
	
	
	private boolean hayValorInicial = false;
	private float valorInicial;

	@Override
	public void onSensorChanged(SensorEvent event) {
		// los movimientos seran relativos a la posicion inicial
		float valor = event.values[1];
		if (!hayValorInicial){
			valorInicial = valor;
			hayValorInicial = true;
		}
		giroNave=(int) (valor - valorInicial) /5;

	}

}
