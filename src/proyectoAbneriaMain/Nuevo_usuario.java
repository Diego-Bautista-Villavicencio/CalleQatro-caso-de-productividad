package proyectoAbneriaMain;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Nuevo_usuario extends TitleAreaDialog 

{
	private Text entrada_nombre;
	private Text entrada_username;
	private Combo entrada_RolUsuario;
	private Text entrada_contrase�a;
	private Text entrada_Vcontrase�a;

	private String nombre;
	private String username;
	private int RolUsuario;
	private String contrase�a;
	private String vContrase�a;
	
	private Shell parentShellO;
	
	private String[] listaRolUsuarios = {"Superadministrador", "Administrador", "Usuario est�ndar"};
	
	public Nuevo_usuario(Shell parentShellI) 
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
		newShell.setText("Registrar nuevo usuario");
	}
	
	@Override
	public void create()
	{
		super.create();
		setTitle("Registrar a un nuevo usuario");
		setMessage("Ingrese los datos requeridos.", IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		
		createNombre(container);
		createUsername(container);
		createPassword(container);
		createVerifyPassword(container);
		createRolUsuario(container);
		
		return area;
	}
	
	private void createUsername (Composite container)
	{
		Label lbtNombreUsuario = new Label(container, SWT.NONE);
		lbtNombreUsuario.setText("Nombre de usuario");
		GridData dataNombreUsuario = new GridData();
		dataNombreUsuario.grabExcessHorizontalSpace = true;
		dataNombreUsuario.horizontalAlignment = GridData.FILL;
		entrada_username = new Text(container, SWT.BORDER);
		entrada_username.setLayoutData(dataNombreUsuario);
	}
	
	private void createNombre (Composite container)
	{
		Label lbtNombre = new Label(container, SWT.NONE);
		lbtNombre.setText("Nombre completo");
		GridData dataNombre = new GridData();
		dataNombre.grabExcessHorizontalSpace = true;
		dataNombre.horizontalAlignment = GridData.FILL;
		entrada_nombre = new Text(container, SWT.BORDER);
		entrada_nombre.setLayoutData(dataNombre);
	}
	
	private void createRolUsuario (Composite container)
	{
		Label lbtRolUsuario = new Label(container, SWT.NONE);
		lbtRolUsuario.setText("Permisos del empleado del usuario");
		GridData dataRolUsuario = new GridData();
		dataRolUsuario.grabExcessHorizontalSpace = true;
		dataRolUsuario.horizontalAlignment = GridData.FILL;
		entrada_RolUsuario = new Combo(container, SWT.READ_ONLY);
		entrada_RolUsuario.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		entrada_RolUsuario.setItems(listaRolUsuarios);
		entrada_nombre.setLayoutData(dataRolUsuario);
	}	
	
	private void createPassword (Composite container)
	{
		Label lbtPassword = new Label(container, SWT.NONE);
		lbtPassword.setText("Contrase�a");
		GridData dataPassword = new GridData();
		dataPassword.grabExcessHorizontalSpace = true;
		dataPassword.horizontalAlignment = GridData.FILL;
		entrada_contrase�a= new Text(container, SWT.BORDER | SWT.PASSWORD);
		entrada_contrase�a.setLayoutData(dataPassword);
	}	
	
	private void createVerifyPassword (Composite container)
	{
		Label lbtVPassword = new Label(container, SWT.NONE);
		lbtVPassword.setText("Confirmar contrase�a");
		GridData dataVPassword = new GridData();
		dataVPassword.grabExcessHorizontalSpace = true;
		dataVPassword.horizontalAlignment = GridData.FILL;
		entrada_Vcontrase�a= new Text(container, SWT.BORDER | SWT.PASSWORD);
		entrada_Vcontrase�a.setLayoutData(dataVPassword);
	}
	
	protected void saveInput()
	{
		nombre = entrada_nombre.getText();
		username = entrada_username.getText();
		RolUsuario = (entrada_RolUsuario.getSelectionIndex()) + 1;
		contrase�a = entrada_contrase�a.getText();
		vContrase�a = entrada_Vcontrase�a.getText();
	}
	
	@Override
	protected void okPressed()
	{
		MessageDialog confirmar = new MessageDialog(parentShellO, "Confirmar informaci�n", null, "�Desea registrar a un usuario con esta informaci�n?", MessageDialog.CONFIRM, new String[] {"Registrar", "Corregir"}, 0);
		
		int result = confirmar.open();
		
		if (result == 0)
		{
			saveInput();
			if (nombre.equals("") || username.equals("") || RolUsuario == 0 || contrase�a.equals("") || vContrase�a.equals(""))
			{
				MessageDialog.openError(getShell(), "Nuevo usuario", "Uno o m�s datos fueron llenados incorrectamente. Por favor intente nuevamente");
			}
			else
			if (!(vContrase�a.equals(contrase�a)))
			{
				MessageDialog.openError(getShell(), "Nuevo usuario", "Las contrase�as no son id�nticas. Por favor revise la informaci�n.");
			}
			else
			{	
				boolean chkUN = false;
				for (Usuarios user : VentanaSelecci�nPrograma.getUsuariosActuales())
				{
					if (username.equals(user.getNUsuario()))
					{
						chkUN = true;
						break;
					}
				}
				if (chkUN)
				{
					MessageDialog.openError(getShell(), "Nuevo usuario", "El nombre de usuario suministrado ya est� ocupado. Por favor, intente nuevamente.");
				}
				else
				{
					contrase�a = Encriptaci�n_de_contrase�as.generarContrase�aSegura(contrase�a);
					super.okPressed();
				}
			}
		}	
	}
	
	public String getNCompleto()
	{
		return nombre;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return contrase�a;
	}
	
	
	public int getRol()
	{
		return RolUsuario;
	}
	
}
