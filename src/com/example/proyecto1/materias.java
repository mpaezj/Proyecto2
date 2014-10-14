package com.example.proyecto1;

import com.j256.ormlite.field.DatabaseField;

public class materias {
	@DatabaseField(id=true)
	private String nombre;
	@DatabaseField
	private int creditos;
	@DatabaseField
	private double nota;
	public materias (){
		
	}
	public materias(String nombre, int creditos) {
		super();
		this.nombre = nombre;
		this.creditos = creditos;
		this.nota = -1.0;
	}
	@Override
	public String toString() {
		return "materias [nombre=" + nombre + ", creditos=" + creditos
				+ ", nota=" + nota + "]";
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCreditos() {
		return creditos;
	}
	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
	
	
}
