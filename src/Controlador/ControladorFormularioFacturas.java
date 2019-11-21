package Controlador;

import java.util.List;

import Modelo.Clientes;
import Modelo.Facturas;
import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class ControladorFormularioFacturas {
	
	private static FacturasDAO controladorfacturas;
	private static List<Facturas> Lista_de_Facturas;
	
	@FXML
	private ComboBox<Facturas> ComboBox_Facturas;
	
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
		Facturas factura= new Facturas(ComboBox_Facturas.getSelectionModel().getSelectedItem());
		
	}
}
