package Controlador;

import java.util.List;

import Modelo.Clientes;
import javafx.fxml.FXML;

public class ControladorFormularioClientes implements ClienteDAO {
	
	@FXML
	public void initialize() {

	}

	@Override
	public Clientes findByPK(int id) throws Exception {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return null;
	}

	@Override
	public List<Clientes> findAll() throws Exception {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return null;
	}

	@Override
	public List<Clientes> findBySQL(String sqlselect) throws Exception {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return null;
	}

	@Override
	public boolean insert(Clientes t) throws Exception {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return false;
	}

	@Override
	public boolean update(Clientes t) throws Exception {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return false;
	}

	@Override
	public boolean delete(int id) throws Exception {
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return false;
	}

}
