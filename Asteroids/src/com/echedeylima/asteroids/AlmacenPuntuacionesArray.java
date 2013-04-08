package com.echedeylima.asteroids;

import java.util.Vector;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones{

	private Vector<String> puntuaciones;
	
	public AlmacenPuntuacionesArray(){
		puntuaciones = new Vector<String>();
		puntuaciones.add("12300 Pepito Dominguez");
		puntuaciones.add("111000 Pedro Mart’n");
		puntuaciones.add("011000 Paco PŽrez");
		puntuaciones.add("011000 Jose PŽrez");
		puntuaciones.add("133000 Marcos PŽrez");
		puntuaciones.add("434000 Juan PŽrez");
		puntuaciones.add("011450 Santiago PŽrez");
		puntuaciones.add("011000 Gustavo PŽrez");
		puntuaciones.add("011000 Daniel PŽrez");
		puntuaciones.add("011000 Pedro PŽrez");
		puntuaciones.add("011000 David PŽrez");
		puntuaciones.add("000000 Antonio PŽrez");
	}
	@Override
	public void guardarPuntuacion(int puntos, String nombre, long fecha) {
		puntuaciones.add(0, puntos + " " + nombre);
		
	}

	@Override
	public Vector<String> listaPuntuaciones(int cantidad) {
		Vector<String> listaLimitada = new Vector<String>();
		for (int i = 0; i < cantidad; i++) {
			listaLimitada.add(puntuaciones.elementAt(i));
		}
		
		return listaLimitada;
		//return puntuaciones;
	}

}
