package Controlador;

import java.io.IOException;
import java.util.List;

import Modelo.Clientes;
import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControladorPrincipio {
	private static ClientesDAO controladorclientes;
	private static List<Clientes> Lista_de_clientes;
	protected static Clientes cliente_actual;
	@FXML
	private TextField nombre_usuario;
	@FXML
	private PasswordField contrasenya;
	@FXML
	private Button iniciar_sesion;
	@FXML
	private Button registro;
	@FXML
	private Button clientes;
	@FXML
	private Button facturas;
	@FXML
	private Button grupos;
	@FXML
	private Button articulos;
	@FXML
	private Button perfil;
	
	@FXML
	public void initialize() {

		try {
			
			controladorclientes = new ClientesDAO();
			Lista_de_clientes = controladorclientes.findAll();
			clientes.setDisable(true);
			facturas.setDisable(true);
			grupos.setDisable(true);
			articulos.setDisable(true);
			perfil.setDisable(true);
			
			if (cliente_actual != null) {
				inicio_de_sesion();
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
	
	public void Iniciar_sesion() {
		String passwd=contrasenya.getText();
		String usuario=nombre_usuario.getText();
		passwd=(controladorclientes.desencriptar_contrasenya(passwd)); 
		String comprobacion="";
		for (int contador=0;contador<Lista_de_clientes.size();contador++) {
			
			comprobacion=Lista_de_clientes.get(contador).getpasswd();
			
			if ((usuario.equals(Lista_de_clientes.get(contador).getNombre()))&&(passwd.equals(comprobacion))) {
				
				cliente_actual=Lista_de_clientes.get(contador);
				
				inicio_de_sesion();
				
			}
		}
	}


	private void inicio_de_sesion() {
		if (cliente_actual.getNombre().equals("admin")) {
			clientes.setDisable(false);
			facturas.setDisable(false);
			grupos.setDisable(false);
			articulos.setDisable(false);
			perfil.setDisable(false);
			
		}
		else {
			facturas.setDisable(false);
			perfil.setDisable(false);
			registro.setDisable(true);
		}
		
	}
}
