package Controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Modelo.Articulos;
import Modelo.Grupos;
import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ControladorFormularioGrupos {
	
	private static ArticulosDAO controladorgrupos;
	static final String GRUPO="Grupo";
	
	@FXML
	private TextArea TextField_descripcion;
	@FXML
	private Label error_grupos;

	@FXML
	public void initialize() {

		try {
			controladorgrupos=new ArticulosDAO();
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

	
	public void insertar_grupo() throws Exception {
		error_grupos.setText("");
		
		try {
			Grupos grupo_seleccionado = new Grupos(0, TextField_descripcion.getText());
			if ((!(controladorgrupos.insert(grupo_seleccionado)))||(TextField_descripcion.getText().isEmpty())) {
				throw new Exception("Error en la insercion de datos del formulario");
			}
			else {
				(new Main()).mensajeConfirmacion("Insercion completada", "La operacion ha sido un exito",GRUPO);
			}
		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}
	}
}
