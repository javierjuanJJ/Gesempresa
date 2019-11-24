package Controlador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Modelo.Facturas;
import Modelo.Vendedores;
import javafx.application.Platform;

public class VendedoresDAO implements GenericoDAO<Vendedores> {

	protected static final String sql_select_all = "SELECT * FROM v_empresa_ad_p1.vendedores;";
	
	public static PreparedStatement preparedstatement = null;
	
	public VendedoresDAO() {
		try {
			Conexion.getConnection();
			
		} catch (Exception e) {
			
			Platform.exit();
		}
	}
	
	@Override
	public List<Vendedores> findAll() throws Exception {
		List<Vendedores> Vendedores =  new ArrayList();
		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_all);
		resultset = preparedstatement.executeQuery();
		
		while (resultset.next()) {
			Vendedores vendedor =  new Vendedores();
			vendedor.setId(resultset.getInt(1));
			vendedor.setNombre(resultset.getString(2));
			vendedor.setFecha_ingreso(resultset.getDate(3));
			vendedor.setSalario(resultset.getDouble(4));
			Vendedores.add(vendedor);
		}
		
		return Vendedores;
	}

	@Override
	public List<Vendedores> findBySQL(String sqlselect) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insert(Vendedores t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Vendedores t) throws Exception {
		
		return false;
	}

	@Override
	public boolean delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vendedores findByPK(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
