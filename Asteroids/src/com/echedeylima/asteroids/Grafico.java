package com.echedeylima.asteroids;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

class Grafico {

	private Drawable drawable; // Imagen
	private double posX, posY; // Posicion
	private double incX, incY; // velocidad
	private int angulo, rotacion; // angulo y velocidad de rotacion
	private int ancho, alto; // dimensiones de la imagen
	private int radioColision; // determinar colisiones (view.invalidate)
	private View view; // para determinar el espacio a borrar (view.invalidate)
	public static final int MAX_VELOCIDAD = 20;

	public Grafico(View view, Drawable drawable) {

		this.view = view;
		this.drawable = drawable;
		ancho = drawable.getIntrinsicWidth();
		alto = drawable.getIntrinsicHeight();
		radioColision = (alto + ancho) / 4;
	}

	public void dibujaGrafico(Canvas canvas) {

		canvas.save();
		int x = (int) (posX + ancho / 2);
		int y = (int) (posY + alto / 2);
		// x e y representan la esquina inferior derecha de la imagen
		canvas.rotate((float) angulo, (float) x, (float) y);
		// area dibujada
		drawable.setBounds((int) posX, (int) posY, (int) posX + ancho,
				(int) posY + alto);
		drawable.draw(canvas);
		canvas.restore();
		int rInval = (int) Math.hypot(ancho, alto) / 2 + MAX_VELOCIDAD;
		view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);

	}

	public void incrementaPos(double factor) {
		posX += incX * factor;
		// si salimos de la pantalla, corregimos posici�n
		if (posX < -ancho / 2) {
			posX = view.getWidth() - ancho / 2;
		}
		if (posX > view.getWidth() - ancho / 2) {
			posX = -ancho / 2;
		}

		posY += incY * factor;
		if (posY < -alto / 2) {
			posY = view.getHeight() - alto / 2;
		}
		if (posY > view.getHeight() - alto / 2) {
			posY = -alto / 2;
		}
		angulo += rotacion * factor; // corregimos el angulo
	}

	public double distancia(Grafico g) {
		return Math.hypot(posX - g.posX, posY - g.posY);
	}

	public boolean verificaColision(Grafico g) {
		return (distancia(g) < (radioColision + g.radioColision));
	}

	// Getters and Setters
	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getIncX() {
		return incX;
	}

	public void setIncX(double incX) {
		this.incX = incX;
	}

	public double getIncY() {
		return incY;
	}

	public void setIncY(double incY) {
		this.incY = incY;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public int getRotacion() {
		return rotacion;
	}

	public void setRotacion(int rotacion) {
		this.rotacion = rotacion;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getRadioColision() {
		return radioColision;
	}

	public void setRadioColision(int radioColision) {
		this.radioColision = radioColision;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getMaxVelocidad() {
		return MAX_VELOCIDAD;
	}

}
