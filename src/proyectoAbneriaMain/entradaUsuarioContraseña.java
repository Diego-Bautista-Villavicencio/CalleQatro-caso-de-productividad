package proyectoAbneriaMain;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class entradaUsuarioContraseña extends Dialog
{
	private static final int NEWUSER_ID = IDialogConstants.CLIENT_ID + 2;
	
	private Text entrada_usuario;
	private String usuario;
	
	private Text entrada_contraseña;
	private String contraseña;
	
	public entradaUsuarioContraseña(Shell parent) {
		super(parent);
	}
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Iniciar sesión");
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridData gd_container = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd_container.widthHint = 282;
		container.setLayoutData(gd_container);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		
		createUsuario(container);
		createContraseña(container);
		return area;
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, NEWUSER_ID, "Nuevo usuario", false);
        createButton(parent, IDialogConstants.OK_ID, "Iniciar sesión", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
    }
	
	private void createUsuario (Composite container)
	{
		Label lbtNombre = new Label(container, SWT.NONE);
		lbtNombre.setText("Usuario: ");
		GridData dataUser = new GridData();
		dataUser.grabExcessHorizontalSpace = true;
		dataUser.horizontalAlignment = GridData.FILL;
		entrada_usuario= new Text(container, SWT.BORDER);
		entrada_usuario.setLayoutData(dataUser);
	}
	
	private void createContraseña (Composite container)
	{
		Label lbtNombre = new Label(container, SWT.NONE);
		lbtNombre.setText("Contraseña: ");
		GridData dataPass= new GridData();
		dataPass.grabExcessHorizontalSpace = true;
		dataPass.horizontalAlignment = GridData.FILL;
		entrada_contraseña= new Text(container, SWT.BORDER | SWT.PASSWORD);
		entrada_contraseña.setLayoutData(dataPass);
	}
	
	@Override
	protected void buttonPressed(int id)
	{
		switch(id)
		{
		case NEWUSER_ID:
			setReturnCode(NEWUSER_ID);
			close();
			break;
		case OK:
			okPressed();
			break;
		case CANCEL:
			cancelPressed();
			break;
		}
	}
	
	@Override
	protected void okPressed()
	{
		usuario = entrada_usuario.getText();
		contraseña = entrada_contraseña.getText();
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed()
	{
		entrada_usuario.setText("");
		entrada_contraseña.setText("");
		super.cancelPressed();
	}
	
	public String getUsuario()
	{
		return usuario;
	}
	
	public String getContraseña()
	{
		return contraseña;
	}
}
