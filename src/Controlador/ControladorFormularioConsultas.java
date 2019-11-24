package Controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorFormularioConsultas {

	@FXML
	private TableView tabla_consulta;
	@FXML
	private TableView tabla_consulta1;
	@FXML
	private TableView tabla_consulta2;
	@FXML
	private Button boton_consultar;
	@FXML
	private Button boton_reiniciar_por_defecto;

	@FXML
	private CheckBox checkbox_factura;
	@FXML
	private CheckBox checkbox_vendedor;
	@FXML
	private CheckBox checkbox_cliente;
	@FXML
	private CheckBox checkbox_cantidad;
	@FXML
	private CheckBox checkbox_fecha;

	@FXML
	private ComboBox<Facturas> combobox_factura_desde;
	@FXML
	private ComboBox<Vendedores> combobox_vendedor_desde;
	@FXML
	private ComboBox<Clientes> combobox_cliente_desde;
	@FXML
	private TextField combobox_cantidad_desde;
	@FXML
	private DatePicker combobox_fecha_desde;

	@FXML
	private ComboBox<Facturas> checkbox_factura_hasta;
	@FXML
	private ComboBox<Vendedores> checkbox_vendedor_hasta;
	@FXML
	private ComboBox<Clientes> checkbox_cliente_hasta;
	@FXML
	private TextField checkbox_cantidad_hasta;
	@FXML
	private DatePicker checkbox_fecha_hasta;

	private static FacturasDAO controladorfacturas;
	private static List<Facturas> Lista_de_Facturas;
	private static List<Vendedores> Lista_de_Vendedores;
	private static List<Clientes> Lista_de_Clientes;
	private static Clientes cliente_actual;
	private static ClientesDAO controladorclientes;
	private static VendedoresDAO controladorVendedores;
	private static boolean es_admin;

	@FXML
	public void initialize() {

		try {
			cliente_actual = ControladorPrincipio.cliente_actual;
			controladorfacturas = new FacturasDAO();
			controladorclientes = new ClientesDAO();
			controladorVendedores = new VendedoresDAO();

			es_admin = (cliente_actual.getNombre().toLowerCase().equals("admin"));
			Lista_de_Facturas = (es_admin) ? controladorfacturas.findAll()
					: controladorfacturas.findAll2(cliente_actual);

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
			Platform.exit();
		}

	}

	public void actualizar_clientes() {
		combobox_cliente_desde.getItems().clear();
		
		try {
			combobox_cliente_desde.getItems().addAll(controladorclientes.findAll());
		} catch (Exception e) {
			
		}
		
	}
	public void actualizar_clientes2() {
		checkbox_cliente_hasta.getItems().clear();

		try {
			checkbox_cliente_hasta.getItems().addAll(controladorclientes.findAll());
		} catch (Exception e) {
			
		}
		
	}

	public void actualizar_facturas() {
		combobox_factura_desde.getItems().clear();
		combobox_factura_desde.getItems().addAll(Lista_de_Facturas);
	}
	
	public void actualizar_facturas2() {
		checkbox_factura_hasta.getItems().clear();
		checkbox_factura_hasta.getItems().addAll(Lista_de_Facturas);
	}

	public void actualizar_vendedor() {
		combobox_vendedor_desde.getItems().clear();
		try {
			combobox_vendedor_desde.getItems().addAll(controladorVendedores.findAll());
		} catch (Exception e) {
			
		}
	}
	
	public void actualizar_vendedor2() {
		checkbox_vendedor_hasta.getItems().clear();
		try {
			checkbox_vendedor_hasta.getItems().addAll(controladorVendedores.findAll());
		} catch (Exception e) {
			
		}
	}


	public void consultar() {
		StringBuilder query = new StringBuilder();
		String part = "";
		tabla_consulta.getItems().clear();
		tabla_consulta.getColumns().clear();
		
		tabla_consulta1.getItems().clear();
		tabla_consulta1.getColumns().clear();
		
		tabla_consulta2.getItems().clear();
		tabla_consulta2.getColumns().clear();
		List<Facturas> Facturas_recibidas = null;
		Facturas_recibidas = new ArrayList<Facturas>();

		query.append("SELECT * FROM v_empresa_ad_p1.facturas, v_empresa_ad_p1.clientes , v_empresa_ad_p1.vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id ");

		part = (checkbox_factura.isSelected()) ? "and (facturas.id >= "
				+ this.combobox_factura_desde.getSelectionModel().getSelectedItem().getId() + " and facturas.id <= "
				+ this.checkbox_factura_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";

		query.append(part);

		part = (checkbox_cliente.isSelected()) ? "and (facturas.cliente >= "
				+ this.combobox_cliente_desde.getSelectionModel().getSelectedItem().getId()
				+ " and facturas.cliente <= "
				+ this.checkbox_cliente_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";

		query.append(part);

		part = (checkbox_vendedor.isSelected()) ? "and (facturas.vendedor >= "
				+ this.combobox_vendedor_desde.getSelectionModel().getSelectedItem().getId()
				+ " and facturas.vendedor <= "
				+ this.checkbox_vendedor_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";

		query.append(part);

		part = (checkbox_fecha.isSelected())
				? "and (facturas.fecha >= " + this.combobox_fecha_desde.getValue().toString()
						+ " and facturas.fecha <= " + this.checkbox_fecha_hasta.toString() + " ) "
				: "";

		query.append(part);

		int numero_desde = 0;
		int numero_hasta = 0;

		TableColumn<String, Facturas> id = new TableColumn<>("id");
		id.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Date, Facturas> fecha = new TableColumn<>("fecha");
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

		TableColumn<String, Clientes> cliente = new TableColumn<>("cliente");
		cliente.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<String, Clientes> nombre = new TableColumn<>("nombre");
		nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

		TableColumn<String, Vendedores> vendedor = new TableColumn<>("vendedor");
		vendedor.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<String, Vendedores> nombre_vendedor = new TableColumn<>("nombre");
		nombre_vendedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));

		TableColumn<String, Facturas> total_factura = new TableColumn<>("total");
		total_factura.setCellValueFactory(new PropertyValueFactory<>("Total"));


		tabla_consulta.getColumns().addAll(id, fecha, total_factura);
		tabla_consulta1.getColumns().addAll(cliente, nombre);
		tabla_consulta2.getColumns().addAll(vendedor, nombre_vendedor);

		try {
			Facturas_recibidas.addAll(controladorfacturas.findBySQL(query.toString()));
			System.out.println(query);
			boolean cantidad=this.checkbox_cantidad.isSelected();
			if (cantidad) {
				numero_desde = Integer.parseInt(combobox_cantidad_desde.getText());
				numero_hasta = Integer.parseInt(checkbox_cantidad_hasta.getText());
				for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
					Facturas factura = new Facturas(Facturas_recibidas.get(contador));
					factura.calcular_total_factura();
					if (((factura.getTotal() >= numero_desde) && (factura.getTotal() <= numero_hasta))) {
						tabla_consulta.getItems().add(factura);
						tabla_consulta1.getItems().add(factura.getVendedor());
						tabla_consulta2.getItems().add(factura.getCliente());
					}
				}
			}
			else {
				for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
					Facturas factura = new Facturas(Facturas_recibidas.get(contador));
					factura.calcular_total_factura();
					tabla_consulta.getItems().add(factura);
					tabla_consulta1.getItems().add(factura.getVendedor());
					tabla_consulta2.getItems().add(factura.getCliente());
				}
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
