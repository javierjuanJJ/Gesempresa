package Controlador;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import Modelo.Articulos;
import Modelo.Clientes;
import Modelo.Facturas;
import Modelo.Lineas_Facturas;
import Modelo.Vendedores;
import javafx.application.Platform;

public class FacturasDAO implements GenericoDAO<Facturas> {
	
	protected static final String sql_select_by_PK = "SELECT * FROM v_empresa_ad_p1.facturas, v_empresa_ad_p1.clientes , v_empresa_ad_p1.vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id and clientes.id=?;";
	protected static final String sql_select_by_PK_user = "SELECT * FROM v_empresa_ad_p1.facturas, v_empresa_ad_p1.clientes , v_empresa_ad_p1.vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id and clientes.id=?;";
	protected static final String sql_select_all = "SELECT * FROM v_empresa_ad_p1.facturas, v_empresa_ad_p1.clientes , v_empresa_ad_p1.vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id;";
	protected static final String sql_UPDATE = "UPDATE `v_empresa_ad_p1`.`clientes` SET `nombre`=?, `direccion`=?, `passwd`=? WHERE `id`=?;";
	
	protected static final String sql_INSERT = "INSERT INTO `v_empresa_ad_p1`.`clientes` (`nombre`, `direccion`, `passwd`) VALUES (?, ?, ?);";
	protected static final String sql_DELETE = "DELETE FROM `v_empresa_ad_p1`.`clientes` WHERE `id`=?;";
	protected static final String sql_Lineas_factura = "SELECT * FROM v_empresa_ad_p1.lineas_factura, v_empresa_ad_p1.articulos,v_empresa_ad_p1.grupos WHERE lineas_factura.articulo=articulos.id and articulos.grupo=grupos.id and factura=?;";
	
	protected static final String sql_actualizar_factura = "UPDATE v_empresa_ad_p1.facturas SET fecha=?,cliente=?,vendedor=? WHERE id=?;";
	protected static final String sql_actualizar_linea_factura = "UPDATE v_empresa_ad_p1.lineas_factura SET articulo=?,importe=?,cantidad=? WHERE linea=? AND factura=?;";
	
	public static PreparedStatement preparedstatement = null;
	public static ArrayList<String> campos = null;
	
	public FacturasDAO() {
		try {
			Conexion.getConnection();
			
		} catch (Exception e) {
			
			Platform.exit();
		}
	}

	@Override
	public Facturas findByPK(int id) throws Exception {
		
		return null;
	}

	@Override
	public List<Facturas> findAll() throws Exception {
		List<Facturas> Facturas_recibidas = null;
		Facturas_recibidas = new ArrayList<Facturas>();
		ResultSet resultset = null;
		ResultSet resultset_lineas = null;
		PreparedStatement preparedstatement_lineas=null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_all);
		resultset = preparedstatement.executeQuery();
		
		Facturas factura=null;
		while (resultset.next()) {
			
			factura=new Facturas();
			
			factura.setId(resultset.getInt(1));
			factura.setFecha(resultset.getDate(2));
			factura.setCliente(new Clientes());
			factura.setVendedor(new Vendedores());
			factura.setForma_de_pago(resultset.getString(5));
			
			factura.getCliente().setId(resultset.getInt(6));
			factura.getCliente().setNombre(resultset.getString(7));
			factura.getCliente().setDireccion(resultset.getString(8));
			factura.getCliente().setpasswd(resultset.getString(9));
			
			factura.getVendedor().setId(resultset.getInt(10));
			factura.getVendedor().setNombre(resultset.getString(11));
			factura.getVendedor().setFecha_ingreso(resultset.getDate(12));
			factura.getVendedor().setSalario(resultset.getDouble(13));
			
			preparedstatement_lineas=Conexion.getConnection().prepareStatement(sql_Lineas_factura);
			preparedstatement_lineas.setInt(1, factura.getId());
			resultset_lineas = preparedstatement_lineas.executeQuery();
			
			while(resultset_lineas.next()) {
				
				Lineas_Facturas linea_factura=new Lineas_Facturas();
				
				linea_factura.setLinea(resultset_lineas.getInt(1));
				linea_factura.setImporte(resultset_lineas.getDouble(5));
				linea_factura.setCantidad(resultset_lineas.getInt(4));
				linea_factura.setArticulo(new Articulos());
				
				
				linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
				linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
				linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
				linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
				linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
				linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));
				
				factura.getLineas_de_la_factura().add(linea_factura);
				
			}
			Facturas_recibidas.add(factura);
		}
		return Facturas_recibidas;
	}
	
	public List<Facturas> findAll2(Clientes cliente) throws Exception {
		List<Facturas> Facturas_recibidas = null;
		Facturas_recibidas = new ArrayList<Facturas>();
		ResultSet resultset = null;
		ResultSet resultset_lineas = null;
		PreparedStatement preparedstatement_lineas=null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_by_PK_user);
		preparedstatement.setInt(1, cliente.getId());
		resultset = preparedstatement.executeQuery();
		Facturas factura=null;
		while (resultset.next()) {
			
			factura=new Facturas();
			
			factura.setId(resultset.getInt(1));
			factura.setFecha(resultset.getDate(2));
			factura.setCliente(new Clientes());
			factura.setVendedor(new Vendedores());
			factura.setForma_de_pago(resultset.getString(5));
			
			factura.getCliente().setId(resultset.getInt(6));
			factura.getCliente().setNombre(resultset.getString(7));
			factura.getCliente().setDireccion(resultset.getString(8));
			factura.getCliente().setpasswd(resultset.getString(9));
			
			factura.getVendedor().setId(resultset.getInt(10));
			factura.getVendedor().setNombre(resultset.getString(11));
			factura.getVendedor().setFecha_ingreso(resultset.getDate(12));
			factura.getVendedor().setSalario(resultset.getDouble(13));
			
			preparedstatement_lineas=Conexion.getConnection().prepareStatement(sql_Lineas_factura);
			preparedstatement_lineas.setInt(1, factura.getId());
			resultset_lineas = preparedstatement_lineas.executeQuery();
			
			while(resultset_lineas.next()) {
				
				Lineas_Facturas linea_factura=new Lineas_Facturas();
				
				linea_factura.setLinea(resultset_lineas.getInt(1));
				linea_factura.setImporte(resultset_lineas.getDouble(5));
				linea_factura.setCantidad(resultset_lineas.getInt(4));
				linea_factura.setArticulo(new Articulos());
				
				linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
				linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
				linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
				linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
				linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
				linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));
				
				factura.getLineas_de_la_factura().add(linea_factura);
				
			}
			Facturas_recibidas.add(factura);
		}
		return Facturas_recibidas;
	}

	@Override
	public List<Facturas> findBySQL(String sqlselect) throws Exception {
		List<Facturas> Facturas_recibidas = null;
		Facturas_recibidas = new ArrayList<Facturas>();
		Facturas factura=null;
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/v_empresa_ad_p1?serverTimezone=" + TimeZone.getDefault().getID();
        String usr = "root";
        String pw = "1234";
        campos = new ArrayList();
		try (JdbcRowSet resultset = RowSetProvider.newFactory().createJdbcRowSet();
				
				JdbcRowSet resultset_lineas = RowSetProvider.newFactory().createJdbcRowSet();) {            
			
			resultset.setUrl(jdbcUrl);
			resultset.setUsername(usr);
			resultset.setPassword(pw);
			
			
			resultset_lineas.setUrl(jdbcUrl);
			resultset_lineas.setUsername(usr);
			resultset_lineas.setPassword(pw);
			
			// Consultas            
			resultset.setCommand(sqlselect);           
			resultset.execute();
			
			ResultSetMetaData rsmd = resultset.getMetaData();
			
			int columnas=rsmd.getColumnCount();
			for (int cont = 1; cont <= columnas; cont++) {
				campos.add(rsmd.getColumnName(cont));
			}
			boolean mas_de_3=(columnas==3);
			
			if (mas_de_3) {
				while (resultset.next()) {
					
					factura=new Facturas();
					
					factura.setId(resultset.getInt(1));
					factura.setFecha(resultset.getDate(2));
					
					// Consultas            
					resultset_lineas.setCommand(sql_Lineas_factura); 
					resultset_lineas.setInt(1, factura.getId());
					resultset_lineas.execute();
					
					while(resultset_lineas.next()) {
						
						Lineas_Facturas linea_factura=new Lineas_Facturas();
						
						linea_factura.setLinea(resultset_lineas.getInt(1));
						linea_factura.setImporte(resultset_lineas.getDouble(5));
						linea_factura.setCantidad(resultset_lineas.getInt(4));
						linea_factura.setArticulo(new Articulos());
						
						linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
						linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
						linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
						linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
						linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
						linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));
						
						factura.getLineas_de_la_factura().add(linea_factura);
						
					}
					
					
					Facturas_recibidas.add(factura);
				}
				
				
			}
			else {
				while (resultset.next()) {
					
					factura=new Facturas();
					
					factura.setId(resultset.getInt(1));
					factura.setFecha(resultset.getDate(2));
					factura.setCliente(new Clientes());
					factura.setVendedor(new Vendedores());
					factura.setForma_de_pago(resultset.getString(5));
					
					factura.getCliente().setId(resultset.getInt(6));
					factura.getCliente().setNombre(resultset.getString(7));
					factura.getCliente().setDireccion(resultset.getString(8));
					factura.getCliente().setpasswd(resultset.getString(9));
					
					factura.getVendedor().setId(resultset.getInt(10));
					factura.getVendedor().setNombre(resultset.getString(11));
					factura.getVendedor().setFecha_ingreso(resultset.getDate(12));
					factura.getVendedor().setSalario(resultset.getDouble(13));
					
					
					
					// Consultas            
					resultset_lineas.setCommand(sql_Lineas_factura); 
					resultset_lineas.setInt(1, factura.getId());
					resultset_lineas.execute();
					
					while(resultset_lineas.next()) {
						
						Lineas_Facturas linea_factura=new Lineas_Facturas();
						
						linea_factura.setLinea(resultset_lineas.getInt(1));
						linea_factura.setImporte(resultset_lineas.getDouble(5));
						linea_factura.setCantidad(resultset_lineas.getInt(4));
						linea_factura.setArticulo(new Articulos());
						
						linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
						linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
						linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
						linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
						linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
						linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));
						
						factura.getLineas_de_la_factura().add(linea_factura);
						
					}

					
					
					
					
					Facturas_recibidas.add(factura);
					
					
					
					
					
				}
			}
			
			
            
        } catch (SQLException se) {
            System.err.println(se.getMessage());
        } 
		
		
		
		return Facturas_recibidas;
	}

	@Override
	public boolean insert(Facturas t) throws Exception {
		
		
		
		return false;
	}

	@Override
	public boolean update(Facturas t) throws Exception {
		boolean realizado=true;
		
		try {
			Conexion.getConnection().setAutoCommit(false);
			
			preparedstatement = Conexion.getConnection().prepareStatement(sql_actualizar_factura);
			
			preparedstatement.setDate(1, (Date) t.getFecha());
			preparedstatement.setInt(2, t.getCliente().getId());
			preparedstatement.setInt(3, t.getVendedor().getId());
			preparedstatement.setInt(4, t.getId());
			
			preparedstatement.addBatch();
			System.out.println(this.preparedstatement);
			System.out.println(t.getCliente() + " " + t.getCliente().getId());
			
			preparedstatement.executeBatch();
			Conexion.getConnection().commit();
			
			preparedstatement = Conexion.getConnection().prepareStatement(sql_actualizar_linea_factura);
			
			for (int contador=0;contador<t.getLineas_de_la_factura().size();contador++) {
				preparedstatement.setInt(1, t.getLineas_de_la_factura().get(contador).getArticulo().getId());
				preparedstatement.setDouble(2, t.getLineas_de_la_factura().get(contador).getImporte());
				preparedstatement.setInt(3, t.getLineas_de_la_factura().get(contador).getCantidad());
				preparedstatement.setInt(4, t.getLineas_de_la_factura().get(contador).getLinea());
				preparedstatement.setInt(5, t.getId());
				System.out.println(this.preparedstatement);
				preparedstatement.addBatch();
			}
			
			int []resultados=preparedstatement.executeBatch();
			Conexion.getConnection().commit();
			for (int contador=0;contador<resultados.length;contador++) {
				if (resultados[contador]<=0) {
					realizado=false;
					contador=resultados.length;
					throw new SQLException();
				}
			}
		}
		catch (SQLException e) {
			Conexion.getConnection().rollback();
			e.printStackTrace();
		}
		return realizado;
	}

	@Override
	public boolean delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
