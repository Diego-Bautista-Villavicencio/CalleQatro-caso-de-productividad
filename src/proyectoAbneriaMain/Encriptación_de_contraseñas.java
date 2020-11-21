package proyectoAbneriaMain;

import java.security.MessageDigest;



public class Encriptaci�n_de_contrase�as {

	private static final String algoritmo = "SHA";
	
	
	public static String generarContrase�aSegura(String password)
	{
		byte[] plaintext = password.getBytes();
		
		String contrase�aFinal = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			
			md.reset();
			md.update(plaintext);
			
			byte[] contrase�aCodificada = md.digest();
			
			StringBuilder CC = new StringBuilder();
			
			for (int i = 0; i < contrase�aCodificada.length; i++)
			{
				if ((contrase�aCodificada[i] & 0xff) < 0x10)
				{
					CC.append("0");
				}
				
				CC.append(Long.toString(contrase�aCodificada[i] & 0xff, 16));
			}
			
			contrase�aFinal = CC.toString();	
		}
		catch (Exception e)
		{
			System.err.println("Error en la creaci�n o encriptaci�n de una contrase�a.\n");
			e.printStackTrace();
		}
		return contrase�aFinal;
	}	
	
	public static boolean verificarContrase�a(String contrase�aProvista, String contrase�aSegura)
	{
		boolean resultado = false;
			
		String nuevaCSegura = generarContrase�aSegura(contrase�aProvista);
		resultado = nuevaCSegura.equalsIgnoreCase(contrase�aSegura);
		return resultado;
	}
}
