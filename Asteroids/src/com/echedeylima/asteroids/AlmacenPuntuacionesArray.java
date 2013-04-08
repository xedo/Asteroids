package com.echedeylima.asteroids;

import java.util.Vector;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones{

	private Vector<String> puntuaciones;
	
	public AlmacenPuntuacionesArray(){
		puntuaciones = new Vector<String>();
		puntuaciones.add("12300 Pepito Dominguez");
		puntuaciones.add("111000 Pedro Mart�n");
		puntuaciones.add("011000 Paco P�rez");
		puntuaciones.add("011000 Jose P�rez");
		puntuaciones.add("133000 Marcos P�rez");
		puntuaciones.add("434000 Juan P�rez");
		puntuaciones.add("011450 Santiago P�rez");
		puntuaciones.add("011000 Gustavo P�rez");
		puntuaciones.add("011000 Daniel P�rez");
		puntuaciones.add("011000 Pedro P�rez");
		puntuaciones.add("011000 David P�rez");
		puntuaciones.add("000000 Antonio P�rez");
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
