package Controlador;

import java.util.List;

import Modelo.Articulos;
import Modelo.Clientes;
import Modelo.Facturas;
import Modelo.Lineas_Facturas;
import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorFormularioFacturas {
	
	private static FacturasDAO controladorfacturas;
	private static List<Facturas> Lista_de_Facturas;
	
	@FXML
	private ComboBox<Facturas> ComboBox_Facturas;
	
	@FXML
	private TableView facturas;
	
	@FXML
	private Label cliente;
	
	@FXML
	private Label vendedor;
	
	@FXML
	private Label numero_factura;
	
	@FXML
	private Label fecha_factura;
	
	
	
	
	@FXML
	private Label subtotal;
	
	@FXML
	private Label iva;
	
	@FXML
	private Label total_de_la_factura;
	
	@FXML
	public void initialize() {

		try {
			controladorfacturas = new FacturasDAO();
			Lista_de_Facturas = controladorfacturas.findAll();
			
			
			
		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
			Platform.exit();
		}

	}
	
	public void actualizar() {
		ComboBox_Facturas.getItems().clear();
		for (int contador=0;contador<Lista_de_Facturas.size();contador++) {
			ComboBox_Facturas.getItems().add(Lista_de_Facturas.get(contador));
		}
	}
	
	public void poner_informacion() {
		facturas.getColumns().clear();
		facturas.getItems().clear();
		Facturas factura= new Facturas(ComboBox_Facturas.getSelectionModel().getSelectedItem());
		TableColumn<String, Lineas_Facturas> linea = new TableColumn<>("Linea de factura");
		linea.setCellValueFactory(new PropertyValueFactory<>("linea"));
		double precio_total_producto=0.0;
		double precio_total=0.0;

        TableColumn<Articulos, Lineas_Facturas> articulo = new TableColumn<>("Articulo");
        articulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        
        TableColumn<String, Lineas_Facturas> cantidad = new TableColumn<>("Cantidad de productos");
        cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		
		TableColumn<String, Lineas_Facturas> importe = new TableColumn<>("Precio del producto");
		importe.setCellValueFactory(new PropertyValueFactory<>("importe"));
		
		TableColumn<String, Lineas_Facturas> total_producto = new TableColumn<>("Total");
		total_producto.setCellValueFactory(new PropertyValueFactory<>("total_importe"));
		facturas.getColumns().addAll(linea,articulo,cantidad,importe,total_producto);
		
		
		for (int contador=0;contador<factura.getLineas_de_la_factura().size();contador++) {
			factura.getLineas_de_la_factura().get(contador).set_total_Importe();
			facturas.getItems().add(factura.getLineas_de_la_factura().get(contador));
			precio_total += factura.getLineas_de_la_factura().get(contador).getTotal_importe();
		}
		
		cliente.setText(factura.getCliente().getNombre() + System.lineSeparator() + factura.getCliente().getDireccion() + System.lineSeparator());
		vendedor.setText(factura.getVendedor().getNombre() + System.lineSeparator());
		numero_factura.setText(factura.getId() + "");
		fecha_factura.setText(factura.getFecha().toString());
		subtotal.setText(String.valueOf(precio_total));
		double precio_iva= (precio_total*21)/100;
		iva.setText(String.valueOf(precio_iva));
		total_de_la_factura.setText(String.valueOf(precio_iva+precio_total));

	}
}
