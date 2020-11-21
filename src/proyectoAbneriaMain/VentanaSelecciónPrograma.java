package proyectoAbneriaMain;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jface.window.Window;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class VentanaSelecciónPrograma {
	
	static Properties prop = null;
	
	static Logger logger = Logger.getLogger(VentanaSelecciónPrograma.class.getName());
    
	static Connection conMain = null;
	
	protected static Shell shell;
	
	protected static final BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
	
	protected static LinkedList<Usuarios> UsuariosActuales = new LinkedList<Usuarios>();
	protected static LinkedList<Empleados> EmpleadosActuales = new LinkedList<Empleados>();
	
	public static Usuarios currentUser;
	static String currentName = "";
	static String currentUName = "";
	static int currentLvl = 4;
	static File main;
	
	private static String route = "";
	private static String routeProp = "";
	
	static FileHandler fh = null;
	
	private static void cargaControlador()
	{
		logger.fine("Inicializar: Controlador JDBC con MySQL...");
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		
		catch (ClassNotFoundException e) 
		{
			logger.severe("El controlador no pudo ser inicializado correctamente.");
			logger.severe(e.toString());
			MessageDialog mensaje = new MessageDialog(shell, "Error al iniciar", null, "El controlador no fue inicializado. Por favor consulte el build path del proyecto.", MessageDialog.ERROR, new String[] {"Cerrar"}, 0);
			mensaje.open();
			System.exit(3306);
		}
		finally
		{
			logger.finer("Controlador inicializado con éxito.");
		}
		
	}
	
	public static Connection conectarABaseDeDatos(String dir, String username, String password)
	{
		Connection conn = null;
		try
		{
			logger.log(Level.FINE, "Inicializar: Controlador conexión con MySQL...");
			conn = DriverManager.getConnection(dir, username, password);
		}
		catch (SQLException eSQL)
		{
			logger.log(Level.SEVERE, "Hubo un error al inicializar el controlador.");
			logger.log(Level.SEVERE, eSQL.toString());
			
			MessageDialog mensaje = new MessageDialog(shell, "Error al iniciar", null, "La conexión con MySQL ha fallado debido a un error en la información utilizada. Por favor consulte el log del programa y actualize el archivo config.properties.", MessageDialog.ERROR, new String[] {"Cerrar"}, 0);
			mensaje.open();
			System.exit(2020);
		}
		finally
		{
			logger.log(Level.FINER, "Controlador inicializado con éxito.");
		}
		
		return conn;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, SQLException 
	{	
		String home = System.getProperty("user.home");
		
		fh = new FileHandler("log.txt", true);
		SimpleFormatter format = new SimpleFormatter();
		fh.setFormatter(format);
		fh.setLevel(Level.ALL);
		
		logger.addHandler(fh);
		
		prop = new Properties();
		File fileProp = FileUtils.toFile(ClassLoader.getSystemClassLoader().getResource("resources/config.properties"));
		FileInputStream fileIProp = new FileInputStream(fileProp);
		
		prop.load(fileIProp);
		fileIProp.close();
		
		Display display = new Display();
		Shell shell = new Shell(display);
				
		shell.setLayout(new FillLayout());
		
		cargaControlador();

		entradaUsuarioContraseña login = new entradaUsuarioContraseña(shell);
		
		
		String routeMaker1 = prop.getProperty("SuffixFileRoute");
		String routeMaker2 = prop.getProperty("SuffixFileName");
		String routeMaker3 = "//config.properties";
		
		route = home + routeMaker1 + routeMaker2;
		routeProp = home + routeMaker1 + routeMaker3;
		
		File temp = new File (home + routeMaker1);
		boolean direct = temp.exists();
		
		if (!direct)
		{
			temp.mkdir();
		}

		File checkMain = new File (home + routeMaker1 + routeMaker2);
		File checkProp = new File (home + routeMaker1 + routeMaker3);

		boolean mainExist = checkMain.exists();
		
		if  (!mainExist)
		{
			logger.log(Level.WARNING, "No se ha encontrado un archivo de nómina en el sistema. Generando...");
			File defaultFile = FileUtils.toFile(ClassLoader.getSystemClassLoader().getResource("resources/PAPEL DE TRABAJO IMSS CUOTAS 123.50 2020.xlsm"));
			FileInputStream act1 = new FileInputStream (defaultFile);
			
			XSSFWorkbook transfer = new XSSFWorkbook(act1);
			
			act1.close();
			
			FileOutputStream act2 = new FileOutputStream(route);
			
			transfer.write(act2);
			transfer.close();
			act2.close();
			
			main = new File(route);
		}
		else
		{
			logger.log(Level.INFO, "Se ha encontrado un archivo de nómina de sistema.");
			main = new File(route);
		}
		
		boolean propExists = checkProp.exists();
		
		if  (!propExists)
		{
			logger.log(Level.WARNING, "No se ha encontrado un archivo de propiedades en el sistema. Generando...");
			File defaultFile = FileUtils.toFile(ClassLoader.getSystemClassLoader().getResource("resources/config.properties"));
			File resultFile = new File(routeProp);
			
			FileUtils.copyFile(defaultFile, resultFile);
			FileInputStream fileIPropUpdate = new FileInputStream(resultFile);
			prop.load(fileIPropUpdate);
			fileIPropUpdate.close();
		}
		else
		{
			logger.log(Level.INFO, "Se ha encontrado un archivo de propiedades de sistema.");
			File resultFile = new File(routeProp);
			FileInputStream fileIPropUpdate = new FileInputStream(resultFile);
			prop.load(fileIPropUpdate);
			fileIPropUpdate.close();
		}
		
		Connection conn = null;
		
		try 
		{
			conn = conectarABaseDeDatos(prop.getProperty("MySQLDir"), prop.getProperty("MySQLUser"), prop.getProperty("MySQLPass"));
		}
		finally
		{
			logger.log(Level.FINER, "La conexión fue establecida exitosamente.");
			conMain = conn;
		}
		
		
		
		
		PreparedStatement userLoader1 = null;
		ResultSet userLoader2 = null;
		
		try
		{
			logger.log(Level.FINE, "Inicializando usuarios...");
			userLoader1 = conn.prepareStatement("SELECT * FROM usuarios;");
			userLoader2 = userLoader1.executeQuery();
			
			int li = 0;
			while (userLoader2.next())
			{
				Usuarios u = new Usuarios(userLoader2.getString("nombreUsuario"), userLoader2.getString("idUsuario"), userLoader2.getString("passUsuario"), userLoader2.getInt("autorizaciónUsuario"), false);
				UsuariosActuales.add(li, u);
				
				li++;
			}
			li = 0;
			
		}
		finally
		{
			userLoader1.close();
			userLoader2.close();
			logger.log(Level.FINEST, "Todos los usuarios han sido inicializados con éxito.");
			logger.log(Level.INFO, "Usuarios: " + UsuariosActuales.size());
		}
		
		Usuarios currentUser = null;
		
		try
		{
			logger.log(Level.FINE, "Inicializando empleados...");
			PreparedStatement employeeLoader1 = conn.prepareStatement("SELECT * FROM empleados;");
			ResultSet employeeLoader2 = employeeLoader1.executeQuery();
			
			while (employeeLoader2.next())
			{
				Empleados e = new Empleados(employeeLoader2.getInt("clave"), employeeLoader2.getString("nombre"), DeterminarNoDepartamento(employeeLoader2.getString("departamento")), employeeLoader2.getString("departamento"), employeeLoader2.getString("NSS"), employeeLoader2.getString("RFC"), employeeLoader2.getString("CURP"), employeeLoader2.getString("fechaRegistro"), employeeLoader2.getBigDecimal("salario"), false);
				EmpleadosActuales.addLast(e);
			}
		}
		finally
		{
			logger.log(Level.FINEST, "Todos los empleados de la base de datos han sido inicializados con éxito.");
			logger.log(Level.INFO, "Empleados de la base: " + EmpleadosActuales.size());
		}
		
		File employeeLoaderXL = new File (route);
		
		FileInputStream employeeLoaderIS = new FileInputStream(employeeLoaderXL);
		
		XSSFWorkbook employeeLoader3 = new XSSFWorkbook(employeeLoaderIS);
		XSSFSheet employeeLoaderSheet = employeeLoader3.getSheet("Empleados");
		
		logger.log(Level.FINE, "Iniciando comparación con datos de la tabla...");
		
		int crtd = 0;
		
		for (Row r : employeeLoaderSheet)
		{

			if (EmpleadosActuales.isEmpty())
			{
				logger.log(Level.INFO, "No se detectó a ningun empleado en la base de datos. Inicializando a través de la tabla Excel...");
				int newClave = (int)r.getCell(0).getNumericCellValue();
				
				String newNombre = r.getCell(1).getStringCellValue();

				String newDepartamento = r.getCell(2).getStringCellValue();
				int newidDepartamento = DeterminarNoDepartamento(newDepartamento);
				
				r.getCell(3).setCellType(CellType.STRING);
				String newNSS = r.getCell(3).getStringCellValue();

				String newRFC = r.getCell(4).getStringCellValue();

				String newCURP = r.getCell(5).getStringCellValue();

				Date Buffer = r.getCell(6).getDateCellValue();
				SimpleDateFormat myFormatObj = new SimpleDateFormat("dd/MM/yyyy");
				String newRegistro = myFormatObj.format(Buffer);
				
				BigDecimal newSalario = BigDecimal.valueOf(r.getCell(7).getNumericCellValue());
				newSalario.setScale(2, RoundingMode.HALF_EVEN);
				
				Empleados e = new Empleados(newClave, newNombre, newidDepartamento, newDepartamento, newNSS, newRFC, newCURP, newRegistro, newSalario, true);
				EmpleadosActuales.addLast(e);
			}
			
			boolean create = false;
			
			
			for (Empleados emp : EmpleadosActuales)
			{
				create = true;
				int test = (int)r.getCell(0).getNumericCellValue();
				
				boolean result = (test == emp.getClave());
				
				if ((result))
				{
					logger.log(Level.FINE, "Se detectó una coincidencia con la base de datos. Saltando creación de empleado clave " + emp.getClave() +"...");
					
					create = false;
					break;
				}
				
			}
			
			
			if (create)
			{
				crtd++;
				int newClave = (int)r.getCell(0).getNumericCellValue();

				String newNombre = r.getCell(1).getStringCellValue();

				String newDepartamento = r.getCell(2).getStringCellValue();
				int newidDepartamento = DeterminarNoDepartamento(newDepartamento);
				
				r.getCell(3).setCellType(CellType.STRING);
				String newNSS = r.getCell(3).getStringCellValue();

				String newRFC = r.getCell(4).getStringCellValue();
	
				String newCURP = r.getCell(5).getStringCellValue();
		
				Date Buffer = r.getCell(6).getDateCellValue();
				SimpleDateFormat myFormatObj = new SimpleDateFormat("dd/MM/yyyy");
				String newRegistro = myFormatObj.format(Buffer);
			
				BigDecimal newSalario = BigDecimal.valueOf(r.getCell(7).getNumericCellValue());
				newSalario.setScale(2, RoundingMode.HALF_EVEN);
			
				Empleados e = new Empleados (newClave, newNombre, newidDepartamento, newDepartamento, newNSS, newRFC, newCURP, newRegistro, newSalario, true);
				EmpleadosActuales.addLast(e);
			}
		}
		
		logger.log(Level.FINER, "Se inicializaron "+ crtd + " usuarios desde la tabla.");
		logger.log(Level.FINEST, "Se ha inicializado a todos los empleados de manera exitosa.");
		logger.log(Level.INFO, "Empleados: " + EmpleadosActuales.size());
		
		employeeLoader3.close();
		
		boolean relog;
		do
		{
			relog = false;
			switch(login.open())
			{
				case IDialogConstants.CANCEL_ID:
					System.exit(0);
					break;
				case IDialogConstants.CLIENT_ID + 2:
					Nuevo_usuario newUserManager = new Nuevo_usuario(shell);
					if(newUserManager.open() == Window.OK)
					{
						Usuarios u = new Usuarios(newUserManager.getNCompleto(), newUserManager.getUsername(), newUserManager.getPassword(), newUserManager.getRol(), true);
						UsuariosActuales.addLast(u);
						relog = true;
					}
					else
					{
						relog = true;
					}
					break;
				case IDialogConstants.OK_ID:
					for (Usuarios user : UsuariosActuales)
					{
						if (user.getNUsuario().equals(login.getUsuario()) && Encriptación_de_contraseñas.verificarContraseña(login.getContraseña(), user.getPassUsuario()))
						{
							currentUser = user;
							currentName = currentUser.getNCompleto();
							currentUName = currentUser.getNUsuario();
							currentLvl = currentUser.getLvlUsuario();
							break;
						}
				}
				
				if (currentUser == null)
				{
					MessageDialog.openError(shell, "Iniciar sesión", "El usuario o la contraseña no son correctos. Por favor, intente de nuevo.");
					relog = true;
				}
				break;
				
			}
			
		}
		while (relog);
			
		logger.log(Level.INFO, "El usuario " + currentUName + " (" + currentName + ") ha iniciado sesión." );
		
		try {
			VentanaSelecciónPrograma window = new VentanaSelecciónPrograma();
			window.open();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
		}
	}
	
	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	protected void createContents() {
		shell = new Shell();
		shell.setSize(640, 480);
		shell.setText("Nómina - CalleQatro (" + currentUName +")");
		shell.setLayout(new GridLayout(1, false));
		
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		MenuItem mntmArchivo = new MenuItem(menuBar, SWT.CASCADE);
		mntmArchivo.setText("Archivo");
		
		Menu menuFile = new Menu(mntmArchivo);
		mntmArchivo.setMenu(menuFile);
		
		MenuItem mntmNuevoEmpleado = new MenuItem(menuFile, SWT.NONE);
		mntmNuevoEmpleado.setText("Nuevo empleado");
		mntmNuevoEmpleado.setEnabled(currentLvl < 3);
		
		MenuItem mntmUserList = new MenuItem(menuFile, SWT.NONE);
		mntmUserList.setText("Lista de usuarios");
		mntmUserList.setEnabled(currentLvl < 3);
		
		new MenuItem(menuFile, SWT.SEPARATOR);
		
		MenuItem mntmSalir = new MenuItem(menuFile, SWT.NONE);
		mntmSalir.setText("Salir");
		
		MenuItem mntmEditar = new MenuItem(menuBar, SWT.CASCADE);
		mntmEditar.setText("Editar");
		
		Menu menuEdit = new Menu(mntmEditar);
		mntmEditar.setMenu(menuEdit);
		
		MenuItem mntmDirecciones = new MenuItem(menuEdit, SWT.NONE);
		mntmDirecciones.setText("Direcciones de archivos de n\u00F3mina");
		mntmDirecciones.setEnabled(currentLvl == 1);
		
		MenuItem mntmAyuda = new MenuItem(menuBar, SWT.CASCADE);
		mntmAyuda.setText("Ayuda");
		
		Menu menu_1 = new Menu(mntmAyuda);
		mntmAyuda.setMenu(menu_1);
		
		MenuItem mntmAcercaDe = new MenuItem(menu_1, SWT.NONE);
		mntmAcercaDe.setText("Acerca de...");
		
		mntmDirecciones.addListener(SWT.Selection, event ->
		{
			DirectoryDialog newDir = new DirectoryDialog(shell);
			String newURL = newDir.open();
			
			if (newURL != null)
			{
				MessageDialog confirmar = new MessageDialog(shell, "Confirmar información", null, "¿Desea cambiar la ruta de salida de archivo de " + getRouteFiles() + " a " + newURL + "?", MessageDialog.CONFIRM, new String[] {"Actualizar", "Cancelar"}, 0);
				int eval = confirmar.open();
				
				if (eval == 0)
				{
					setRouteFiles(newURL);
				}
			}
		});
		
		mntmSalir.addListener(SWT.Selection, event ->
				{
					shell.getDisplay().dispose();
					try {
						logger.log(Level.FINE, "El usuario ha cerrado el programa. Terminando conexión con MySQL...");
						conMain.close();
					} catch (SQLException e) {
						logger.log(Level.SEVERE,"Hubo un error al cerrar la conexión a MySQL.");
						logger.log(Level.SEVERE, e.toString());
						System.exit(1100);
					}
					finally
					{
						logger.log(Level.FINE, "La conexión fue cerrada con éxito.");
						logger.log(Level.FINE, currentUName + " ha cerrrado sesión.");
						currentUser = null;
						currentName = "";
						currentLvl = 4;
						logger.log(Level.INFO, "Programa cerrado con éxito.");
						System.exit(0);
					}				
				});
		
		mntmNuevoEmpleado.addListener(SWT.Selection, event ->
		{
			Diálogo_de_registro newEmp = new Diálogo_de_registro(shell);
			if (newEmp.open() == Window.OK)
			{
				try {
					Empleados e = new Empleados(newEmp.getClave(), newEmp.getNombre(), DeterminarNoDepartamento(newEmp.getDepartamento()), newEmp.getDepartamento(), newEmp.getNSS(), newEmp.getRFC(), newEmp.getCURP(), newEmp.getFecha(), newEmp.getSalario(), true);
					EmpleadosActuales.addLast(e);
				} catch (SQLException e) {
					logger.log(Level.SEVERE, "Error al registrar nuevo usuario, tipo entrada/salida.");
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error al registrar nuevo usuario, tipo MySQL.");
				}
			}
		});
		
		mntmAcercaDe.addListener(SWT.Selection, event ->
		{
			Acerca_de info = new Acerca_de(shell);
			info.open();
		});
		
		mntmUserList.addListener(SWT.Selection, event ->
		{
			ListaUsuarios list = new ListaUsuarios(shell);
			list.open();
		});
		
		
		Label greeting = new Label(shell, SWT.NONE);
		greeting.setText("Bienvenido/a, " + currentName + ".");
		
		LocalDateTime ahora = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE dd 'de' MMM 'de' YYYY 'a las' hh:mm'.'");
		
		Label timeDate = new Label (shell, SWT.NONE);
		timeDate.setText(ahora.format(formato));
		
		greeting.pack();
		timeDate.pack();
		new Label(shell, SWT.NONE);
		
		Button btnRegistrarDatosDe = new Button(shell, SWT.NONE);
		btnRegistrarDatosDe.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRegistrarDatosDe.setText("Datos de n\u00F3mina de empleados");
		
		Button btnGenerarReporte = new Button(shell, SWT.NONE);
		btnGenerarReporte.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGenerarReporte.setText("Generar reporte de un empleado");
		
		Button btnGenerarReporteTodos = new Button(shell, SWT.NONE);
		btnGenerarReporteTodos.setText("Generar reporte de todos los empleados");
		
		btnRegistrarDatosDe.addListener(SWT.Selection, event ->
		{
			ListaReporteNomina lista = new ListaReporteNomina(shell);
			if (lista.open() == 0)
			{
				DeterminarDiasLaborados ventana = new DeterminarDiasLaborados(shell, lista.getIndex());
				if (ventana.open() == 0)
				{
					FileInputStream XLDiasInput = null;
					File XLDiasFile = new File (route);
					try {
						XLDiasInput = new FileInputStream(XLDiasFile);
					} catch (Exception e) {
						logger.log(Level.SEVERE, e.getCause().toString());
					}
					XSSFWorkbook XLDiasLibro = null;
					
					try {
						XLDiasLibro = new XSSFWorkbook(XLDiasInput);
					} catch (Exception e) {
						logger.log(Level.SEVERE, e.getCause().toString());
					}
					
					
					XSSFSheet XLDiasHoja = XLDiasLibro.getSheet("CALCULO Y BASE");
					
					for(Row r : XLDiasHoja)
					{
						if (r.getCell(0) == null)
						{
							continue;
						}
						
						if (r.getCell(0).getCellType() == CellType.NUMERIC && (int)r.getCell(0).getNumericCellValue() == EmpleadosActuales.get(lista.getIndex()).getClave())
						{
							r.getCell(12).setCellValue(ventana.getAbsencias());
							r.getCell(13).setCellValue(ventana.getIncapacidades());
							r.getCell(14).setCellValue(ventana.getVacaciones());
							break;
						}
					}
					
					try {
						XLDiasInput.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE, e.getCause().toString());
					}
					
					FileOutputStream XLDiasOutput = null;
					
					try {
						XLDiasOutput = new FileOutputStream(XLDiasFile);
					} catch (FileNotFoundException e) {
						logger.log(Level.SEVERE, e.getCause().toString());
					}
					
					try {
						XLDiasLibro.write(XLDiasOutput);
					} 
					catch (Exception e) 
					{
						logger.log(Level.SEVERE, e.getCause().toString());
					}
					finally
					{
						try {
							XLDiasOutput.close();
							XLDiasLibro.close();
						} catch (IOException e) {
							logger.log(Level.SEVERE, e.getCause().toString());
						}
						
					}
					
				}
			}
		});
		
		btnGenerarReporte.addListener(SWT.Selection, event ->
		{
			ListaReporteNomina lista = new ListaReporteNomina(shell);
			if (lista.open() == 0)
			{
				int reporte = lista.getIndex();
				try {
					EmpleadosActuales.get(reporte).getSalarioFinal();
				} catch (IOException e) {

					e.printStackTrace();
				}
				finally
				{
					MessageDialog end = new MessageDialog(shell, "Éxito", null, ("Se registró la información del trabajador con éxito"), MessageDialog.INFORMATION, new String[] {"Aceptar"}, 0);
					end.open();
				}
			}
		});
		
		btnGenerarReporteTodos.addListener(SWT.Selection, event ->
		{
			MessageDialog confirmar = new MessageDialog(shell, "Confirmar información", null, "¿Desea conseguir la información de nómina de los " + EmpleadosActuales.size() + " empleados?", MessageDialog.CONFIRM, new String[] {"Confirmar", "Cancelar"}, 0);
			if(confirmar.open() == 0)
			{
				for (int i = 0; i < EmpleadosActuales.size(); i++)
				{
					try {
						EmpleadosActuales.get(i).getSalarioFinal();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				MessageDialog end = new MessageDialog(shell, "Éxito", null, ("Se registró la información de los empleados con éxito."), MessageDialog.INFORMATION, new String[] {"Aceptar"}, 0);
				end.open();
			}
				
			
		});
	}
	
	private static int DeterminarNoDepartamento(String Linea)
	{
		int result = 0;
		
		if (Linea.equals("SSJ"))
		{
			result = 1;
		}
		else
		if (Linea.equals("SPD"))
		{
			result = 2;
		}
		else
		if (Linea.equals("SAJ"))
		{
			result = 3;
		}
		else
		if (Linea.equals("SPC"))
		{
			result = 4;
		}
		else
		if (Linea.equals("SLC"))
		{
			result = 5;
		}
		else
		if (Linea.equals("SSF"))
		{
			result = 6;
		}
		else
		if (Linea.equals("SMH"))
		{
			result = 7;
		}
		else
		if (Linea.equals("SN1"))
		{
			result = 8;
		}
		else
		if (Linea.equals("SN2"))
		{
			result = 9;
		}
		else
		if (Linea.equals("OPE"))
		{
			result = 10;
		}
		else
		if (Linea.equals("CGE"))
		{
			result = 11;
		}
		else
		if (Linea.equals("ACF"))
		{
			result = 12;
		}
		else
		if (Linea.equals("CAB"))
		{
			result = 13;
		}
		else
		if (Linea.equals("FYD"))
		{
			result = 14;
		}
		if (Linea.equals("MPD"))
		{
			result = 15;
		}
		else
		if (Linea.equals("SCCC"))
		{
			result = 16;
		}
		else
		{
			result = 0;
		}
		
		return result;
	}
	
	public static LinkedList<Usuarios> getUsuariosActuales()
	{
		return UsuariosActuales;
	}
	
	public static LinkedList<Empleados> getEmpleadosActuales()
	{
		return EmpleadosActuales;
	}
	
	public static void setRouteFiles(String url)
	{
		File propOut = new File(routeProp);
		FileOutputStream fileOProp;
		try
		{
			fileOProp = new FileOutputStream (propOut);
			prop.setProperty("URLReporteEmpleado", url);
			prop.store(fileOProp, null);
			fileOProp.close();
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Error al actualizar la ruta de salida");
			logger.log(Level.SEVERE, e.toString());
		}
	}
	
	public static String getRouteFiles()
	{
		return prop.getProperty("URLReporteEmpleado");
	}
	
	public static String getMainRoute()
	{
		return route;
	}
	
	public static Properties getProps()
	{
		return prop;
	}
	
	
}
