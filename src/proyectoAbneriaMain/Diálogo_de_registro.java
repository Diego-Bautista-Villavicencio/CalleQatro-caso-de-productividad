package proyectoAbneriaMain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Diálogo_de_registro extends TitleAreaDialog{
	
	private Text entrada_nombre;
	private Combo entrada_departamento;
	private Text entrada_NSS;
	private Text entrada_RFC;
	private Text entrada_CURP;
	private Text entrada_salario;
	private Text entrada_clave;
	private DateTime entrada_fecha;
	
	private int clave;
	private String nombre;
	private String departamento;
	private String NSS;
	private String RFC;
	private String CURP;
	private BigDecimal Salario;
	private BigDecimal min = new BigDecimal("123.22");
	private String Fecha;
	
	private Shell parentShellO;
	
	private String[] listaDepartamentos = {"1.- San Jerónimo", "2.- Pedregal", "3.- Ajusco", "4.- Picacho", "5.- Luis Cabrera", "6.- San Fernando", "7.- Miguel Hidalgo", "8.- (No disponible)", "9.- (No disponible)", "10.- Operación", "11.- Coordinación general", "12.- Administración, contabilidad y finanzas", "13.- Abastecimiento", "14.- Formación y desarrollo del talento humano", "15.- Marketing, publicidad y diseño", "16.- Sembrando calidad y cosechando cultura"};
	
	public Diálogo_de_registro(Shell parentShellI) 
	{
		super(parentShellI);
		parentShellO = parentShellI;
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Registrar", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
    }
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Nuevo empleado");
	}
	
	@Override
	public void create()
	{
		super.create();
		setTitle("Registrar a un nuevo empleado");
		setMessage("Ingrese los datos que van a ser registrados para el nuevo empleado", IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		
		createClave(container);
		createNombre(container);
		createDepartamento(container);
		createNSS(container);
		createRFC(container);
		createCURP(container);
		createSalary(container);
		createFecha(container);
		
		return area;
	}
	
	private void createClave (Composite container)
	{
		Label lbtClave = new Label(container, SWT.NONE);
		lbtClave.setText("Clave del empleado");
		GridData dataNombre = new GridData();
		dataNombre.grabExcessHorizontalSpace = true;
		dataNombre.horizontalAlignment = GridData.FILL;
		entrada_clave = new Text(container, SWT.BORDER);
		entrada_clave.setLayoutData(dataNombre);
		
		entrada_clave.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e)
			{
				String entrada_recibida = e.text;
				char[] chars = new char[entrada_recibida.length()];
				entrada_recibida.getChars(0, entrada_recibida.length(), chars, 0);
				
				for (int i = 0; i < entrada_recibida.length(); i++)
				{
					if (!('0' <= chars[i] && chars[i] <= '9'))
					{
						e.doit = false;
						return;
					}
				}
			}
		});
	}
	
	private void createNombre (Composite container)
	{
		Label lbtNombre = new Label(container, SWT.NONE);
		lbtNombre.setText("Nombre completo del empleado");
		GridData dataNombre = new GridData();
		dataNombre.grabExcessHorizontalSpace = true;
		dataNombre.horizontalAlignment = GridData.FILL;
		entrada_nombre = new Text(container, SWT.BORDER);
		entrada_nombre.setLayoutData(dataNombre);
	}
	
	private void createDepartamento (Composite container)
	{
		Label lbtDepartamento = new Label(container, SWT.NONE);
		lbtDepartamento.setText("Departamento del empleado");
		GridData dataDepartamento = new GridData();
		dataDepartamento.grabExcessHorizontalSpace = true;
		dataDepartamento.horizontalAlignment = GridData.FILL;
		entrada_departamento = new Combo(container, SWT.READ_ONLY);
		entrada_departamento.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		entrada_departamento.setItems(listaDepartamentos);
		entrada_departamento.setLayoutData(dataDepartamento);
	}
	
	private void createNSS (Composite container)
	{
		Label lbtNSS = new Label(container, SWT.NONE);
		lbtNSS.setText("No. de seguro social del empleado");
		GridData dataNSS = new GridData();
		dataNSS.grabExcessHorizontalSpace = true;
		dataNSS.horizontalAlignment = GridData.FILL;
		entrada_NSS = new Text(container, SWT.BORDER);
		entrada_NSS.setLayoutData(dataNSS);
		entrada_NSS.setTextLimit(11);
		
		entrada_NSS.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e)
			{
				String entrada_recibida = e.text;
				char[] chars = new char[entrada_recibida.length()];
				entrada_recibida.getChars(0, entrada_recibida.length(), chars, 0);
				
				for (int i = 0; i < entrada_recibida.length(); i++)
				{
					if (!('0' <= chars[i] && chars[i] <= '9'))
					{
						e.doit = false;
						return;
					}
				}
			}
		});
	}
	
	private void createRFC (Composite container)
	{
		Label lbtRFC = new Label(container, SWT.NONE);
		lbtRFC.setText("RFC del empleado");
		GridData dataRFC = new GridData();
		dataRFC.grabExcessHorizontalSpace = true;
		dataRFC.horizontalAlignment = GridData.FILL;
		entrada_RFC = new Text(container, SWT.BORDER);
		entrada_RFC.setLayoutData(dataRFC);
		entrada_RFC.setTextLimit(13);
	}
	
	private void createCURP (Composite container)
	{
		Label lbtCURP = new Label(container, SWT.NONE);
		lbtCURP.setText("CURP del empleado");
		GridData dataCURP = new GridData();
		dataCURP.grabExcessHorizontalSpace = true;
		dataCURP.horizontalAlignment = GridData.FILL;
		entrada_CURP = new Text(container, SWT.BORDER);
		entrada_CURP.setLayoutData(dataCURP);
		entrada_CURP.setTextLimit(18);
	}
	
	private void createSalary (Composite container)
	{
		Label lbtSalary = new Label(container, SWT.NONE);
		lbtSalary.setText("Salario inicial del empleado");
		GridData dataSalary = new GridData();
		dataSalary.grabExcessHorizontalSpace = true;
		dataSalary.horizontalAlignment = GridData.FILL;
		entrada_salario = new Text(container, SWT.BORDER);
		entrada_salario.setLayoutData(dataSalary);
		
		entrada_salario.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e)
			{
				Text texto = (Text)e.getSource();
				
				String entrada_recibida_0 = texto.getText();
				String entrada_recibida_1 = entrada_recibida_0.substring(0, e.start) + e.text + entrada_recibida_0.substring(e.end);
				boolean validez = true;
				try
				{
					@SuppressWarnings("unused")
					BigDecimal test = new BigDecimal(entrada_recibida_1);
				}
				catch (NumberFormatException ex)
				{
					validez = false;
				}
				
				if(!validez)
				{
					e.doit = false;
				}
			}
		});
	}
	
	private void createFecha (Composite container)
	{
		Label lbtFecha = new Label(container, SWT.NONE);
		lbtFecha.setText("Fecha de admisión del empleado");
		GridData dataFecha = new GridData();
		dataFecha.grabExcessHorizontalSpace = true;
		dataFecha.horizontalAlignment = GridData.FILL;
		entrada_fecha = new DateTime(container, SWT.BORDER);
		entrada_fecha.setLayoutData(dataFecha);
	}
	
	@Override
	protected boolean isResizable()
	{
		return true;
	}
	
	private void saveInput()
	{
		clave = Integer.parseInt(entrada_clave.getText());
		nombre = entrada_nombre.getText();
		departamento = DeterminarDepartamento(entrada_departamento.getSelectionIndex() + 1);
		NSS = entrada_NSS.getText();
		RFC = entrada_RFC.getText();
		CURP = entrada_CURP.getText();
		Salario = new BigDecimal(entrada_salario.getText());
		LocalDate Buffer = LocalDate.of(entrada_fecha.getYear(), entrada_fecha.getMonth(), entrada_fecha.getDay());
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Fecha = Buffer.format(myFormatObj);
	}
	
	@Override
	protected void okPressed()
	{
		MessageDialog confirmar = new MessageDialog(parentShellO, "Confirmar información", null, "¿Desea registrar a un empleado con esta información?", MessageDialog.CONFIRM, new String[] {"Registrar", "Corregir"}, 0);
		
		int result = confirmar.open();
		if (result == 0)
		{
			saveInput();
			if (clave == 0 || nombre.equals("") || departamento.equals("") || NSS.length() < 10 || RFC.length() < 12 || CURP.length() < 17)
			{
				MessageDialog.openError(getShell(), "Nuevo empleado", "Uno o más datos fueron llenados incorrectamente. Por favor intente nuevamente");
			}
			else
			if (Salario.compareTo(min) == -1)
			{
				MessageDialog.openError(getShell(), "Nuevo empleado", "El salario ingresado no es válido o es menor al salario mínimo. Por favor intente nuevamente");
			}
			else
			{
				super.okPressed();
			}
		}
	}
	
	@Override
	protected void cancelPressed()
	{
		super.cancelPressed();
	}
	
	public int getClave() 
	{
		return clave;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public String getDepartamento()
	{
		return departamento;
	}
	
	public String getNSS()
	{
		return NSS;
	}
	
	public String getRFC()
	{
		return RFC;
	}
	
	public String getCURP()
	{
		return CURP;
	}
	
	public BigDecimal getSalario()
	{
		return Salario;
	}
	public String getFecha()
	{
		return Fecha;
	}
	
	private static String DeterminarDepartamento(int value)
	{
		String result = "";
		
		if (value == 1)
		{
			result = "SSJ";
		}
		else
		if (value == 2)
		{
			result = "SPD";
		}
		else
		if (value == 3)
		{
			result = "SAJ";
		}
		else
		if (value == 4)
		{
			result = "SPC";
		}
		else
		if (value == 5)
		{
			result = "SLC";
		}
		else
		if (value == 6)
		{
			result = "SSF";
		}
		else
		if (value == 7)
		{
			result = "SMH";
		}
		else
		if (value == 8)
		{
			result = "SN1";
		}
		else
		if (value == 9)
		{
			result = "SN2";
		}
		else
		if (value == 10)
		{
			result = "OPE";
		}
		else
		if (value == 11)
		{
			result = "CGE";
		}
		else
		if (value == 12)
		{
			result = "ACF";
		}
		else
		if (value == 13)
		{
			result = "CAB";
		}
		else
		if (value == 14)
		{
			result = "FYD";
		}
		if (value == 15)
		{
			result = "MPD";
		}
		else
		if (value == 16)
		{
			result = "SCCC";
		}
		
		return result;
	}
	
}
