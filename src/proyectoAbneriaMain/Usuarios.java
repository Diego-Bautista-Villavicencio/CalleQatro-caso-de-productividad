package proyectoAbneriaMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuarios {
	private String nombreCompleto;
	private String nombreUsuario;
	private String contraseñaUsuario;
	private int permisosUsuario;
	
	public static Connection conectarABaseDeDatos() throws SQLException
	{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyectoabneriaAlpha?useTimezone=true&serverTimezone=CST", "CalleQatro", "Abneria2020");
		return conn;
	}
	
	public Usuarios(String nombreC, String nombreU, String pass, int lvl, boolean newStatus) throws SQLException
	{
		this.nombreCompleto = nombreC;
		this.nombreUsuario = nombreU;
		this.contraseñaUsuario = pass;
		this.permisosUsuario = lvl;
		
		if(newStatus)
		{
			Connection conn = conectarABaseDeDatos();
			PreparedStatement preparedStatement = null;
			ResultSet rset = null;
			
			preparedStatement = conn.prepareStatement("INSERT INTO usuarios VALUES (?, ?, ?, ?);");
			
			preparedStatement.setString(1, this.nombreCompleto);
			preparedStatement.setString(2, this.nombreUsuario);
			preparedStatement.setString(3, this.contraseñaUsuario);
			preparedStatement.setInt(4, this.permisosUsuario);
			
			preparedStatement.executeUpdate();
			
			preparedStatement = conn.prepareStatement("SELECT nombreUsuario, idUsuario FROM usuarios;");
			rset = preparedStatement.executeQuery();
			writeResultSetBasic(rset);
			
			preparedStatement.close();
			rset.close();
			conn.close();
		}
	}
	
	public String getNCompleto()
	{
		return this.nombreCompleto;
	}
	
	public String getNUsuario()
	{
		return this.nombreUsuario;
	}
	
	public String getPassUsuario()
	{
		return this.contraseñaUsuario;
	}
	
	public int getLvlUsuario()
	{
		return this.permisosUsuario;
	}
	
	private void writeResultSetBasic(ResultSet rset) throws SQLException
	{
		while (rset.next())
		{
			String NC = rset.getString("nombreUsuario");
			String NR = rset.getString("idUsuario");
			
			System.out.println("Usuarios\n");
			
			System.out.println("Nombre completo: " + NC);
			System.out.println("Nombre de usuario: " + NR + "\n");
		}
	}
}
