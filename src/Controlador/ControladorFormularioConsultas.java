package Controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.control.Label;
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
	@FXML
	private Label cliente;
	@FXML
	private Label vendedor;

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

			this.combobox_cliente_desde.setVisible(es_admin);
			this.checkbox_cliente_hasta.setVisible(es_admin);
			this.combobox_vendedor_desde.setVisible(es_admin);
			this.checkbox_vendedor_hasta.setVisible(es_admin);
			cliente.setVisible(es_admin);
			vendedor.setVisible(es_admin);

			iniciar_datos();

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
			e.printStackTrace();
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

	public void coordinar() {

		int seleccionado_tabla_1 = (tabla_consulta.getSelectionModel().getSelectedIndex());
		int seleccionado_tabla_2 = (tabla_consulta1.getSelectionModel().getSelectedIndex());
		int seleccionado_tabla_3 = (tabla_consulta2.getSelectionModel().getSelectedIndex());

		int posicion = -1;

		if (seleccionado_tabla_1 >= 0) {
			posicion = seleccionado_tabla_1;
			tabla_consulta.getSelectionModel().clearSelection();
		} else if (seleccionado_tabla_2 >= 0) {
			posicion = seleccionado_tabla_2;
			tabla_consulta1.getSelectionModel().clearSelection();
		} else if (seleccionado_tabla_3 >= 0) {
			posicion = seleccionado_tabla_3;
			tabla_consulta2.getSelectionModel().clearSelection();
		}
		tabla_consulta.scrollTo(posicion);
		tabla_consulta1.scrollTo(posicion);
		tabla_consulta2.scrollTo(posicion);

	}

	public void consultar() {

		try {

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
			String campos_otros_admin = "v_empresa_ad_p1.facturas.id as id, v_empresa_ad_p1.facturas.fecha as fecha, '' as total";
			String campos_admin = "v_empresa_ad_p1.facturas.id as id, v_empresa_ad_p1.facturas.fecha as fecha, '' as total, v_empresa_ad_p1.clientes.id as cliente, v_empresa_ad_p1.clientes.nombre as nombre, v_empresa_ad_p1.vendedores.id as vendedor, v_empresa_ad_p1.vendedores.nombre as nombre";
			query.append("SELECT " + ((es_admin) ? campos_admin : campos_otros_admin)
					+ " FROM v_empresa_ad_p1.facturas, v_empresa_ad_p1.clientes , v_empresa_ad_p1.vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id ");

			part = (checkbox_factura.isSelected()) ? "and (facturas.id >= "
					+ this.combobox_factura_desde.getSelectionModel().getSelectedItem().getId() + " and facturas.id <= "
					+ this.checkbox_factura_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";

			query.append(part);

			part = (!es_admin) ? "and (facturas.cliente = " + this.cliente_actual.getId() + " ) " : "";

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

			LocalDate dateToConvert = this.combobox_fecha_desde.getValue();
			java.sql.Date fecha_sql = java.sql.Date.valueOf(dateToConvert);

			LocalDate dateToConvert2 = this.checkbox_fecha_hasta.getValue();
			java.sql.Date fecha_sql2 = java.sql.Date.valueOf(dateToConvert2);

			part = (checkbox_fecha.isSelected()) ? "and (facturas.fecha >= '" + fecha_sql.toString()
					+ "' and facturas.fecha <= '" + fecha_sql2.toString() + "' ) " : "";

			query.append(part);

			double numero_desde = 0.0;
			double numero_hasta = 0.0;
			ArrayList campos = controladorfacturas.campos;

			Facturas_recibidas.addAll(controladorfacturas.findBySQL(query.toString()));
			tabla_consulta1.setVisible(es_admin);
			tabla_consulta2.setVisible(es_admin);

			if (es_admin) {
				for (int contador = 0; contador < 3; contador++) {
					tabla_consulta.getColumns().add(new TableColumn<Facturas, String>(campos.get(contador).toString()));
				}

				for (int contador = 0; contador < 2; contador++) {
					tabla_consulta1.getColumns()
							.add(new TableColumn<Vendedores, String>(campos.get(contador).toString()));
				}

				for (int contador = 0; contador < 2; contador++) {
					tabla_consulta2.getColumns()
							.add(new TableColumn<Clientes, String>(campos.get(contador).toString()));
				}

				((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(0))
						.setCellValueFactory(new PropertyValueFactory<>("id"));
				((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(1))
						.setCellValueFactory(new PropertyValueFactory<>("fecha"));
				((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(2))
						.setCellValueFactory(new PropertyValueFactory<>("Total"));

				((TableColumn<Clientes, String>) tabla_consulta1.getColumns().get(0))
						.setCellValueFactory(new PropertyValueFactory<>("id"));
				((TableColumn<Clientes, String>) tabla_consulta1.getColumns().get(1))
						.setCellValueFactory(new PropertyValueFactory<>("nombre"));

				((TableColumn<Vendedores, String>) tabla_consulta2.getColumns().get(0))
						.setCellValueFactory(new PropertyValueFactory<>("id"));
				((TableColumn<Vendedores, String>) tabla_consulta2.getColumns().get(1))
						.setCellValueFactory(new PropertyValueFactory<>("nombre"));

				boolean cantidad = this.checkbox_cantidad.isSelected();
				if (cantidad) {
					numero_desde = Double.parseDouble(combobox_cantidad_desde.getText());
					numero_hasta = Double.parseDouble(checkbox_cantidad_hasta.getText());
					for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
						Facturas factura = new Facturas(Facturas_recibidas.get(contador));
						factura.calcular_total_factura();
						if (((factura.getTotal() >= numero_desde) && (factura.getTotal() <= numero_hasta))) {
							tabla_consulta.getItems().add(contador, factura);
							tabla_consulta1.getItems().add(contador, factura.getCliente());
							tabla_consulta2.getItems().add(contador, factura.getVendedor());
						}
					}
				} else {
					for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
						Facturas factura = new Facturas(Facturas_recibidas.get(contador));
						factura.calcular_total_factura();
						tabla_consulta.getItems().add(contador, factura);
						tabla_consulta1.getItems().add(contador, factura.getCliente());
						tabla_consulta2.getItems().add(contador, factura.getVendedor());
					}
				}
			} else {
				for (int contador = 0; contador < 3; contador++) {
					tabla_consulta.getColumns().add(new TableColumn<String, String>(campos.get(contador).toString()));
				}

				((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(0))
						.setCellValueFactory(new PropertyValueFactory<>("id"));
				((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(1))
						.setCellValueFactory(new PropertyValueFactory<>("fecha"));
				((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(2))
						.setCellValueFactory(new PropertyValueFactory<>("Total"));

				boolean cantidad = this.checkbox_cantidad.isSelected();
				if (cantidad) {
					numero_desde = Double.parseDouble(combobox_cantidad_desde.getText());
					numero_hasta = Double.parseDouble(checkbox_cantidad_hasta.getText());
					for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
						Facturas factura = new Facturas(Facturas_recibidas.get(contador));
						factura.calcular_total_factura();
						if (((factura.getTotal() >= numero_desde) && (factura.getTotal() <= numero_hasta))) {
							tabla_consulta.getItems().add(factura);
						}
					}
				} else {
					for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
						Facturas factura = new Facturas(Facturas_recibidas.get(contador));
						factura.calcular_total_factura();
						tabla_consulta.getItems().add(factura);	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void iniciar_datos() {

		try {

			Facturas factura_del_principio = null;
			Facturas factura_del_final = null;

			String campos_otros_admin = "v_empresa_ad_p1.facturas.id as id, v_empresa_ad_p1.facturas.fecha as fecha, '' as total";
			String campos_admin = "v_empresa_ad_p1.facturas.id as id, v_empresa_ad_p1.facturas.fecha as fecha, '' as total, v_empresa_ad_p1.clientes.id as cliente, v_empresa_ad_p1.clientes.nombre as nombre, v_empresa_ad_p1.vendedores.id as vendedor, v_empresa_ad_p1.vendedores.nombre as nombre";

			String principio_sql = "SELECT " + ((es_admin) ? campos_admin : campos_otros_admin)
					+ " FROM v_empresa_ad_p1.facturas, " + "v_empresa_ad_p1.clientes , " + "v_empresa_ad_p1.vendedores "
					+ "WHERE facturas.cliente=clientes.id " + "AND facturas.vendedor=vendedores.id ";
			StringBuilder query_principio = new StringBuilder(principio_sql);
			StringBuilder query_final = new StringBuilder(principio_sql);
			String modificador_sql_max = "MAX";
			String modificador_sql_min = "MIN";
			String part_min = (!es_admin)
					? "AND facturas.id= " + "(SELECT MIN(v_empresa_ad_p1.facturas.id) FROM "
							+ "v_empresa_ad_p1.facturas,v_empresa_ad_p1.clientes "
							+ "WHERE facturas.cliente=clientes.id and clientes.id=" + cliente_actual.getId() + ") "
					: "AND facturas.id=(SELECT " + modificador_sql_min + "(v_empresa_ad_p1.facturas.id) "
							+ "FROM v_empresa_ad_p1.facturas) ";

			String part_max = (!es_admin)
					? "AND facturas.id= " + "(SELECT MAX(v_empresa_ad_p1.facturas.id) FROM "
							+ "v_empresa_ad_p1.facturas,v_empresa_ad_p1.clientes "
							+ "WHERE facturas.cliente=clientes.id and clientes.id=" + cliente_actual.getId() + ") "
					: "AND facturas.id=(SELECT " + modificador_sql_max + "(v_empresa_ad_p1.facturas.id) "
							+ "FROM v_empresa_ad_p1.facturas) ";

			query_principio.append(part_min);
			query_final.append(part_max);

			try {
				factura_del_principio = new Facturas(controladorfacturas.findBySQL(query_principio.toString()).get(0));
				factura_del_final = new Facturas(controladorfacturas.findBySQL(query_final.toString()).get(0));
				factura_del_principio.calcular_total_factura();
				factura_del_final.calcular_total_factura();
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.combobox_cantidad_desde.setText(factura_del_principio.getTotal() + "");
			this.checkbox_cantidad_hasta.setText(factura_del_final.getTotal() + "");
			this.combobox_factura_desde.getSelectionModel().select(factura_del_principio);
			this.checkbox_factura_hasta.getSelectionModel().select(factura_del_final);
			this.combobox_cliente_desde.getSelectionModel().select(factura_del_principio.getCliente());
			this.checkbox_cliente_hasta.getSelectionModel().select(factura_del_final.getCliente());
			this.combobox_vendedor_desde.getSelectionModel().select(factura_del_principio.getVendedor());
			this.checkbox_vendedor_hasta.getSelectionModel().select(factura_del_final.getVendedor());

			java.sql.Date date2 = (java.sql.Date) factura_del_principio.getFecha();

			LocalDate date = date2.toLocalDate();

			this.combobox_fecha_desde.setValue(date);

			java.sql.Date date1 = (java.sql.Date) factura_del_final.getFecha();

			LocalDate date3 = date1.toLocalDate();

			this.checkbox_fecha_hasta.setValue(date3);

		} catch (Exception e) {

		}

	}

}
