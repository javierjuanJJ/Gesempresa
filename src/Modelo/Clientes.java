package Modelo;

public class Clientes {
	private int id;
	private String nombre;
	private String direccion;
	private String passwd;
	
	public Clientes(int id, String nombre, String direccion, String passwd) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.passwd=passwd;
	}
	
	public Clientes() {
		this.id = 0;
		this.nombre = "";
		this.direccion = "";
		this.passwd="";
	}
	
	public Clientes(Clientes clientes) {
		this.id = clientes.getId();
		this.nombre = clientes.getNombre();
		this.direccion = clientes.getDireccion();
		this.passwd = clientes.getpasswd();
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}
	public String getpasswd() {
		return passwd;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public void setpasswd(String passwd) {
		this.passwd = passwd;
	}
	@Override
	public String toString() {
		return this.getId() + " - " +getNombre();
	}

}
