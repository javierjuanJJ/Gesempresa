package application;
	
import java.io.IOException;

import Controlador.ArticulosDAO;
import Controlador.ClientesDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;


public class Main extends Application {

    private static Scene scene;
    private static Stage Stage;

    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/Resources/Principio"));
        stage.setScene(scene);
        Stage=stage;
        Stage.setTitle("Pantalla inicial");
        Stage.getIcons().clear();
		Stage.getIcons().add(new Image("/Resources/"+"principio.png"));
        Stage.show();
        
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    /**
	 * Cambia la escena de la ventana.
	 */
	
	public void Cambiar_Pantalla(String id) throws IOException {
		FXMLLoader fxmlLoader = null;
		StringBuilder archivo=new StringBuilder();
		StringBuilder icono=new StringBuilder();
		String[] titulo=null;
		
		archivo.append("/Resources/");
		icono.append("/Resources/");
		switch (id) {
		case "Pantalla_Clientes":
			archivo.append("Form_clientes");
			icono.append("cliente.jpg");
		break;
		case "Pantalla_Articulos":
			archivo.append("Form_articulos");
			icono.append("articulo.jpg");
			break;
		case "Pantalla_Grupos":
			archivo.append("Form_grupos");
			icono.append("grupo.jpg");
			break;
		case "Pantalla_Principio":
			archivo.append("Principio");
			icono.append("principio.png");
			break;
		case "Pantalla_Perfil":
			archivo.append("Perfil");
			icono.append("principio.png");
			break;
		case "Pantalla_Facturas":
			archivo.append("Facturas");
			icono.append("principio.png");
			break;
		case "Pantalla_Consultas":
			archivo.append("Consultas");
			icono.append("principio.png");
			break;
		default:
			break;
		}
		archivo.append(".fxml");
		
		fxmlLoader = new FXMLLoader(Main.class.getResource(archivo.toString()));
		scene = new Scene((Parent) fxmlLoader.load());
		Stage.setScene(scene);
		titulo=id.split("_");
		Stage.setTitle(titulo[0] + " de " + titulo[1]);
		Stage.getIcons().clear();
		Stage.getIcons().add(new Image(icono.toString()));
		Stage.show();
        
	}
	
	public void mensajeExcepcion(Exception ex, String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error de excepci�n");
		alert.setHeaderText(msg);
		alert.setContentText(ex.getMessage());

		String exceptionText = "";
		StackTraceElement[] stackTrace = ex.getStackTrace();
		for (StackTraceElement ste : stackTrace) {
			exceptionText = exceptionText + ste.toString() + System.getProperty("line.separator");
		}

		Label label = new Label("La traza de la excepci�n ha sido: ");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	public void mensajeConfirmacion(String Titulo, String msg, String tipo) {
		String exceptionText = "";
		StringBuilder exception=new StringBuilder();
		
		int comienzo_orden_sql=0;
		comienzo_orden_sql="com.mysql.cj.jdbc.ClientPreparedStatement: ".length();
		String orden_SQL="";
		
		if( (tipo.equalsIgnoreCase("Articulo"))||(tipo.equalsIgnoreCase("Grupo")) ){
			orden_SQL=ArticulosDAO.preparedstatement.toString();
			exception.append("La orden SQL ha sido ");
			exception.append(System.lineSeparator());
			exception.append(orden_SQL.substring(comienzo_orden_sql));
			exception.append(System.lineSeparator());
		}
		else if (tipo.equalsIgnoreCase("batch")) {
			orden_SQL="Importacion completa";
			exception.append(msg);
			exception.append(System.lineSeparator());
			msg="";
		}
		else {
			orden_SQL=ClientesDAO.preparedstatement.toString();
			exception.append("La orden SQL ha sido ");
			exception.append(System.lineSeparator());
			exception.append(orden_SQL.substring(comienzo_orden_sql));
			exception.append(System.lineSeparator());
		}
	
		
		exceptionText=exception.toString();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Exito de la operacion");
		alert.setHeaderText(msg);
		
		
		Label label = new Label("La operacion ha sido un exito");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	

}
