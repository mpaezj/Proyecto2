package com.example.proyecto1;

import com.j256.ormlite.field.DatabaseField;

public class notas {

    @DatabaseField(foreign = true)
	private materias nombremateria;
	
    @DatabaseField(id=true)
	private String materianota;
    
    @DatabaseField
	private String nombre;
	@DatabaseField
	private int creditos;
	@DatabaseField
	private double nota;
	public notas (){
		
	}
	public notas(materias nombremateria, String nombre, int creditos) {
		super();
		this.materianota=nombremateria.getNombre()+nombre;
		this.nombremateria = nombremateria;
		this.nombre = nombre;
		this.creditos = creditos;
		this.nota = -1.0;
	}
	
	public String getMaterianota() {
		return materianota;
	}
	public void setMaterianota(String materianota) {
		this.materianota = materianota;
	}
	public materias getNombremateria() {
		return nombremateria;
	}
	public void setNombremateria(materias nombremateria) {
		this.nombremateria = nombremateria;
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
