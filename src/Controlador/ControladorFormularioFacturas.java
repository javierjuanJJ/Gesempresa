package Controlador;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import Modelo.Articulos;
import Modelo.Clientes;
import Modelo.Facturas;
import Modelo.Lineas_Facturas;
import Modelo.Vendedores;
import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControladorFormularioFacturas {
	private Stage escenario;
	private static FacturasDAO controladorfacturas;
	private static List<Facturas> Lista_de_Facturas;
	private static List<Articulos> Lista_de_Articulos;
	private static List<Vendedores> Lista_de_Vendedores;
	private static List<Clientes> Lista_de_Clientes;
	private static Clientes cliente_actual;
	private static ClientesDAO controladorclientes;
	private static ArticulosDAO controladorArticulos;
	private static VendedoresDAO controladorVendedores;
	private static int contador_modificador=0; 
	private static boolean es_admin;

	@FXML
	private ComboBox<Facturas> ComboBox_Facturas;

	@FXML
	private ComboBox<Articulos> cambiar_articulo;

	@FXML
	private ComboBox<Vendedores> cambiar_vendedor;

	@FXML
	private ComboBox<Clientes> cambiar_cliente;

	@FXML
	private TableView facturas;
	
	@FXML
	private DatePicker Fecha_factura2;

	@FXML
	private Label cliente;
	
	@FXML
	private TextField cambiar_cantidad;

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
	private Label Cliente;

	@FXML
	private Label Vendedor;

	@FXML
	private Label Articulo;

	@FXML
	private Label Fecha_factura;

	@FXML
	private Label Cantidad;
	
	@FXML
	private Button boton_actualizar;
	
	

	@FXML
	public void initialize() {

		try {
			cliente_actual = ControladorPrincipio.cliente_actual;
			controladorfacturas = new FacturasDAO();
			controladorclientes = new ClientesDAO();
			controladorArticulos = new ArticulosDAO();
			controladorVendedores = new VendedoresDAO();
			
			es_admin = (cliente_actual.getNombre().toLowerCase().equals("admin"));
			
			Cliente.setVisible(es_admin);
			cambiar_cliente.setVisible(es_admin);
			Vendedor.setVisible(es_admin);
			cambiar_vendedor.setVisible(es_admin);
			Articulo.setVisible(es_admin);
			Fecha_factura.setVisible(es_admin);
			Fecha_factura2.setVisible(es_admin);
			Cantidad.setVisible(es_admin);
			cambiar_cantidad.setVisible(es_admin);
			boton_actualizar.setVisible(es_admin);
			
			Lista_de_Facturas =  (es_admin)? controladorfacturas.findAll() : controladorfacturas.findAll2(cliente_actual);

		
			if (!es_admin) {
				Lista_de_Articulos = controladorArticulos.findAll();
				Lista_de_Vendedores = controladorVendedores.findAll();
				Lista_de_Clientes = controladorclientes.findAll();
			}
		
		
		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
			Platform.exit();
		}

	}

	
	public void Cambiar_Pantalla(ActionEvent action) throws IOException {
		String id_boton = "";
		id_boton = ((Button) action.getSource()).getId();
		Main main = new Main();
		main.Cambiar_Pantalla(id_boton);
	}

	
	public void actualizar() {
		
		ComboBox_Facturas.getItems().clear();
		ComboBox_Facturas.getItems().addAll(Lista_de_Facturas);

	}

	public void actualizar_cliente() {

		cambiar_cliente.getItems().clear();
		cambiar_cliente.getItems().addAll(Lista_de_Clientes);

	}

	public void actualizar_vendedores() {

		cambiar_vendedor.getItems().clear();
		cambiar_vendedor.getItems().addAll(Lista_de_Vendedores);

	}

	public void actualizar_articulos() {

		cambiar_articulo.getItems().clear();
		cambiar_articulo.getItems().addAll(Lista_de_Articulos);

	}

	public void poner_informacion() {
		facturas.getColumns().clear();
		facturas.getItems().clear();
		double precio_iva = 0.0;
		Facturas factura = new Facturas(ComboBox_Facturas.getSelectionModel().getSelectedItem());
		TableColumn<String, Lineas_Facturas> linea = new TableColumn<>("Linea de factura");
		linea.setCellValueFactory(new PropertyValueFactory<>("linea"));
		double precio_total = 0.0;
		
		TableColumn<Articulos, Lineas_Facturas> articulo = new TableColumn<>("Articulo");
		articulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
		
		
		
		TableColumn<String, Lineas_Facturas> cantidad = new TableColumn<>("Cantidad de productos");
		cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

		TableColumn<String, Lineas_Facturas> importe = new TableColumn<>("Precio del producto");
		importe.setCellValueFactory(new PropertyValueFactory<>("importe"));

		TableColumn<String, Lineas_Facturas> total_producto = new TableColumn<>("Total");
		total_producto.setCellValueFactory(new PropertyValueFactory<>("total_importe"));
		facturas.getColumns().addAll(linea, articulo, cantidad, importe, total_producto);

		for (int contador = 0; contador < factura.getLineas_de_la_factura().size(); contador++) {
			factura.getLineas_de_la_factura().get(contador).set_total_Importe();
			facturas.getItems().add(factura.getLineas_de_la_factura().get(contador));
			precio_total += factura.getLineas_de_la_factura().get(contador).getTotal_importe();
		}

		cliente.setText(factura.getCliente().getNombre() + System.lineSeparator() + factura.getCliente().getDireccion()
				+ System.lineSeparator());
		vendedor.setText(factura.getVendedor().getNombre() + System.lineSeparator());
		numero_factura.setText(factura.getId() + "");
		fecha_factura.setText(factura.getFecha().toString());
		subtotal.setText(String.valueOf(precio_total));
		precio_iva = (precio_total * 21) / 100;
		iva.setText(String.valueOf(precio_iva));
		total_de_la_factura.setText(String.valueOf(precio_iva + precio_total));
		
		

	}

	public void Exportar_factura_a_PDF() {
		double precio_iva = 0.0;
		Facturas factura = new Facturas(ComboBox_Facturas.getSelectionModel().getSelectedItem());

		FileChooser fc = creaFileChooser("Guardar pdf");
		File file = (fc.showSaveDialog(escenario));

		// Se crea el documento
		Document documento = new Document();

		// Se crea el OutputStream para el fichero donde queremos dejar el pdf.
		FileOutputStream ficheroPdf;
		try {
			ficheroPdf = new FileOutputStream(file);
			// Se asocia el documento al OutputStream y se indica que el espaciado entre
			// lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
			PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

			// Se abre el documento.
			documento.open();

			documento.add(new Paragraph("Factura", FontFactory.getFont("arial", // fuente
					22, // tamaño
					Font.ITALIC, // estilo
					BaseColor.CYAN))); // color

			documento.add(new Paragraph("Facturar a         Enviar a           Nº de factura          "
					+ factura.getId() + System.lineSeparator()));
			documento.add(new Paragraph(
					factura.getCliente().getNombre() + "       " + "         " + factura.getVendedor().getNombre()
							+ "         " + "         " + factura.getFecha().toString() + System.lineSeparator()));
			documento.add(new Paragraph(
					factura.getCliente().getDireccion() + "         " + "         " + System.lineSeparator()));

			documento.add(new Paragraph(System.lineSeparator()));
			documento.add(new Paragraph(System.lineSeparator()));
			documento.add(new Paragraph(System.lineSeparator()));
			documento.add(new Paragraph(System.lineSeparator()));
			documento.add(new Paragraph(System.lineSeparator()));
			documento.add(new Paragraph(System.lineSeparator()));
			PdfPTable tabla = new PdfPTable(4);
			int columnas = (factura.getLineas_de_la_factura().size());

			tabla.addCell("Cantidad");
			tabla.addCell("Descripcion");
			tabla.addCell("Precio unitario");
			tabla.addCell("Importe");
			double precio_total = 0.0;

			for (int i = 0; i < columnas; i++) {
				tabla.addCell(factura.getLineas_de_la_factura().get(i).getCantidad() + "");
				tabla.addCell(factura.getLineas_de_la_factura().get(i).getArticulo().toString());
				tabla.addCell(factura.getLineas_de_la_factura().get(i).getImporte() + "");
				tabla.addCell(factura.getLineas_de_la_factura().get(i).getTotal_importe() + "");
				precio_total += factura.getLineas_de_la_factura().get(i).getTotal_importe();
			}

			tabla.addCell("");
			tabla.addCell("");
			tabla.addCell("Subtotal");
			tabla.addCell(precio_total + "");

			tabla.addCell("");
			tabla.addCell("");
			tabla.addCell("IVA: 21%");
			tabla.addCell((precio_total * 21 / 100) + "");

			tabla.addCell("");
			tabla.addCell("");
			tabla.addCell("Total: ");
			tabla.addCell((precio_total + (precio_total * 21 / 100)) + "");

			documento.add(tabla);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		documento.close();

	}

	private FileChooser creaFileChooser(String titulo) {
		FileChooser fc = new FileChooser();
		fc.setTitle(titulo);
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		FileChooser.ExtensionFilter filtro1 = new FileChooser.ExtensionFilter("Archivos pdf *.pdf", "*.pdf");
		fc.getExtensionFilters().add(filtro1);
		return fc;
	}
	
	public void poner_datos() {
		
		contador_modificador= facturas.getSelectionModel().getSelectedIndex();
		
		Facturas factura = new Facturas(ComboBox_Facturas.getSelectionModel().getSelectedItem());
		
		cambiar_cliente.getItems().clear();
		cambiar_cliente.getItems().add(factura.getCliente());
		cambiar_cliente.getSelectionModel().select(0);
		
		cambiar_articulo.getItems().clear();
		cambiar_articulo.getItems().add(((Lineas_Facturas) facturas.getSelectionModel().getSelectedItem()).getArticulo());
		cambiar_articulo.getSelectionModel().select(0);
		
		cambiar_vendedor.getItems().clear();
		cambiar_vendedor.getItems().add(factura.getVendedor());
		cambiar_vendedor.getSelectionModel().select(0);
		
		Lineas_Facturas l= new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador_modificador));
		this.cambiar_cantidad.setText(l.getCantidad()+"");
		
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		//toString() append +8 automatically.
		java.sql.Date date2 = (java.sql.Date) factura.getFecha();
		
		LocalDate date = date2.toLocalDate();
		
		this.Fecha_factura2.setValue(date);
	}
	
	public void cambiar_cliente() {
		
		cliente.setText(cambiar_cliente.getSelectionModel().getSelectedItem().toString());
	
	}
	
	public void cambiar_vendedor() {
	
		vendedor.setText(cambiar_vendedor.getSelectionModel().getSelectedItem().toString());
	
	}
	
	public void cambiar_articulo() {
		contador_modificador= facturas.getSelectionModel().getSelectedIndex();
		Lineas_Facturas l= new Lineas_Facturas(((Lineas_Facturas) facturas.getItems().get(contador_modificador)));
		l.setArticulo(cambiar_articulo.getSelectionModel().getSelectedItem());
		facturas.getItems().set(contador_modificador, l);
	}
	
	public void cambiar_cantidad() {
		int cantidad=0;
		contador_modificador= facturas.getSelectionModel().getSelectedIndex();
		
		
		
		cantidad=Integer.parseInt(cambiar_cantidad.getText());
		
		//Lineas_Facturas l= new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador_modificador));
		//l.setCantidad(cantidad);
		
		//facturas.getItems().set(contador_modificador, l);
	
	}
	
	public void actualizar_linea() {
		int cantidad=0;
		
		Facturas factura = new Facturas(ComboBox_Facturas.getSelectionModel().getSelectedItem());
	
		cantidad=Integer.parseInt(cambiar_cantidad.getText());
		
		Lineas_Facturas l2= factura.getLineas_de_la_factura().get(contador_modificador);
		l2.setArticulo(cambiar_articulo.getSelectionModel().getSelectedItem());
		
		l2.setCantidad(cantidad);
		l2.set_total_Importe();
		facturas.getItems().set(contador_modificador, l2);
		double precio_total=0.0;
		facturas.getSelectionModel().select(contador_modificador);
		
		Lineas_Facturas l= new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador_modificador));
		
		
		for (int contador = 0; contador < factura.getLineas_de_la_factura().size(); contador++) {
			l=new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador));
			
			if (!l.equals(l2)) {
				l.set_total_Importe();
				precio_total += l.getTotal_importe();
			}
		}
		precio_total += l2.getTotal_importe();
		
		LocalDate dateToConvert=Fecha_factura2.getValue();
		java.sql.Date fecha_sql=java.sql.Date.valueOf(dateToConvert);
		java.util.Date fecha_util=(java.util.Date) fecha_sql;
		
		factura.setFecha(fecha_util);
		
		fecha_factura.setText(fecha_util.toString());
		
		factura.setCliente(new Clientes(cambiar_cliente.getSelectionModel().getSelectedItem()));
		factura.setVendedor(new Vendedores(cambiar_vendedor.getSelectionModel().getSelectedItem()));
		
		cliente.setText(factura.getCliente().toString());
		vendedor.setText(factura.getVendedor().toString());
		
		subtotal.setText(String.valueOf(precio_total));
		double precio_iva = (precio_total * 21) / 100;
		iva.setText(String.valueOf(precio_iva));
		total_de_la_factura.setText(String.valueOf(precio_iva + precio_total));
		
		try {

			if (controladorfacturas.update(factura)) {
				System.out.println("Hecho");
			}
			else {
				System.out.println("No Hecho");
			}
			
		} catch (Exception e) {
			
		}
		
	}
}
