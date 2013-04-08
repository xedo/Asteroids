package com.echedeylima.asteroids;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class VistaJuego extends View {

	// Nave
	private Grafico nave;
	private int giroNave;
	private float aceleracionNave;
	private static final int PASO_GIRO_NAVE = 5;
	private static final float PASO_ACELERACION_NAVE = 0.5f;

	// Asteroides
	private Vector<Grafico> Asteroides; // Vector con los asteroides
	private int numAsteroides = 5; // Numero inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en los que se divide
	
	// Thread
	private ThreadJuego thread = new ThreadJuego();
	private static int PERIODO_PROCESO = 50;
	private long ultimoProceso = 0;
	
	public class ThreadJuego extends Thread {
		@Override
		public void run(){
			while (true){
				actualizaFisica();
			}
		}
	}

	public VistaJuego(Context context, AttributeSet attrs) {
		super(context, attrs);
		Drawable drawableNave, drawableAsteroide, drawableAsteroide2, drawableMisil;

		// nave
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		nave = new Grafico(this, drawableNave);

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
	}
	
	protected void actualizaFisica() {
		long ahora = System.currentTimeMillis();
		//para la ejecucion si aun no se ha pumplido el periodo de proceso
		if (ultimoProceso + PERIODO_PROCESO > ahora)
			return;
		//para una ejecucion en tiempo real calculamos el retador
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;
		//actualizamos la velocidad y direcci—n de la nave a partir de grioNave y acelereacionNave (segun la entrada del jugador)
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		//actualizamos si el modulo de la velocidad no excede del maximo
		if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()){
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}
		//actualizamos posiciones
		nave.incrementaPos(retardo);
		for (Grafico asteroide : Asteroides){
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
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// dibujar nave
		nave.dibujaGrafico(canvas);

		// dibujar asteroides
		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
	}

}
