package Modelo;

import java.util.Date;
import java.util.ArrayList;

public class Facturas {
	private int id;
	private Date fecha;
	private Clientes cliente;
	private Vendedores vendedor;
	private String forma_de_pago;
	private double Total;
	private ArrayList<Lineas_Facturas> lineas_de_la_factura;
	
	public Facturas(int id, Date fecha, Clientes cliente, Vendedores vendedor,
			ArrayList<Lineas_Facturas> lineas_de_la_factura,String forma_de_pago) {
		
		this.id = id;
		this.fecha = fecha;
		this.cliente = cliente;
		this.vendedor = vendedor;
		this.lineas_de_la_factura = lineas_de_la_factura;
		this.forma_de_pago = forma_de_pago;
		Total=calcular_total_factura();
	}
	public Facturas(int id, Date fecha, Clientes cliente, Vendedores vendedor,
			ArrayList<Lineas_Facturas> lineas_de_la_factura,String forma_de_pago,double total) {
		
		this.id = id;
		this.fecha = fecha;
		this.cliente = cliente;
		this.vendedor = vendedor;
		this.lineas_de_la_factura = lineas_de_la_factura;
		this.forma_de_pago = forma_de_pago;
		this.Total=total;
	}

	public Facturas() {
		
		this.id = 0;
		this.fecha=new Date(0);
		this.cliente = new Clientes();
		this.vendedor = new Vendedores();
		this.lineas_de_la_factura = new ArrayList();
		this.forma_de_pago = "";
		Total=0;
	}
	
	public Facturas(Facturas factura) {
		
		this.id = factura.getId();
		this.fecha = factura.getFecha();
		this.cliente = factura.getCliente();
		this.vendedor = factura.getVendedor();
		this.lineas_de_la_factura = factura.getLineas_de_la_factura();
		this.forma_de_pago = factura.getForma_de_pago();
		this.Total= factura.getTotal();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Clientes getCliente() {
		return cliente;
	}
	
	public double getTotal() {
		return Total;
	}
	
	public String getForma_de_pago() {
		return this.forma_de_pago;
	}

	public void setCliente(Clientes cliente) {
		this.cliente = cliente;
	}

	public Vendedores getVendedor() {
		return vendedor;
	}

	public void setForma_de_pago(String forma_de_pago) {
		this.forma_de_pago = forma_de_pago;
	}
	public void setVendedor(Vendedores vendedor) {
		this.vendedor = vendedor;
	}

	public ArrayList<Lineas_Facturas> getLineas_de_la_factura() {
		return lineas_de_la_factura;
	}

	public void setLineas_de_la_factura(ArrayList<Lineas_Facturas> lineas_de_la_factura) {
		this.lineas_de_la_factura = lineas_de_la_factura;
	}
	
	public double calcular_total_factura() {
		double total=0.0;
		
		for (int contador=0;contador<getLineas_de_la_factura().size();contador++) {
			getLineas_de_la_factura().get(contador).set_total_Importe();
			total += getLineas_de_la_factura().get(contador).getTotal_importe();
		}
		this.Total=total;
		return total;
	}
	
	@Override
	public String toString() {
		
		StringBuilder resultado=new StringBuilder();
		
		resultado.append(fecha + " Cliente: ");
		resultado.append(cliente.getNombre() + " Vendedor: ");
		resultado.append(vendedor.getNombre() + " ");
		
		return resultado.toString();
	}
	
}
