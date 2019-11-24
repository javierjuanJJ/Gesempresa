package Modelo;

import java.sql.Date;

public class Vendedores {
	
	private int id;
	private Date fecha_ingreso;
	private double salario;
	private String nombre;
	
	public Vendedores(int id, Date fecha_ingreso, double salario, String nombre) {
		this.id = id;
		this.fecha_ingreso = fecha_ingreso;
		this.salario = salario;
		this.nombre = nombre;
	}
	
	public Vendedores() {
		this.id = 0;
		this.fecha_ingreso = new Date(0);
		this.salario = 0;
		this.nombre = "";
	}
	
	public Vendedores(Vendedores vendedor) {
		this.id = vendedor.getId();
		this.fecha_ingreso = vendedor.getFecha_ingreso();
		this.salario = vendedor.getSalario();
	}

	public int getId() {
		return id;
	}
	
	public String getNombre() {
		return this.nombre;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	@Override
	public String toString() {
		return getNombre();
	}

}
