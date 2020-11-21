package proyectoAbneriaMain;

import java.security.MessageDigest;



public class Encriptación_de_contraseñas {

	private static final String algoritmo = "SHA";
	
	
	public static String generarContraseñaSegura(String password)
	{
		byte[] plaintext = password.getBytes();
		
		String contraseñaFinal = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			
			md.reset();
			md.update(plaintext);
			
			byte[] contraseñaCodificada = md.digest();
			
			StringBuilder CC = new StringBuilder();
			
			for (int i = 0; i < contraseñaCodificada.length; i++)
			{
				if ((contraseñaCodificada[i] & 0xff) < 0x10)
				{
					CC.append("0");
				}
				
				CC.append(Long.toString(contraseñaCodificada[i] & 0xff, 16));
			}
			
			contraseñaFinal = CC.toString();	
		}
		catch (Exception e)
		{
			System.err.println("Error en la creación o encriptación de una contraseña.\n");
			e.printStackTrace();
		}
		return contraseñaFinal;
	}	
	
	public static boolean verificarContraseña(String contraseñaProvista, String contraseñaSegura)
	{
		boolean resultado = false;
			
		String nuevaCSegura = generarContraseñaSegura(contraseñaProvista);
		resultado = nuevaCSegura.equalsIgnoreCase(contraseñaSegura);
		return resultado;
	}
}
