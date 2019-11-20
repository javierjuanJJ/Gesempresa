package Controlador;

import java.util.List;

import Modelo.Facturas;

public class FacturasDAO implements GenericoDAO<Facturas> {

	@Override
	public Facturas findByPK(int id) throws Exception {
		
		return null;
	}

	@Override
	public List<Facturas> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Facturas> findBySQL(String sqlselect) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insert(Facturas t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Facturas t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
