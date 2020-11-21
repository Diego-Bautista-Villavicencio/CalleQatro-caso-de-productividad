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

public class entradaUsuarioContrase�a extends Dialog
{
	private static final int NEWUSER_ID = IDialogConstants.CLIENT_ID + 2;
	
	private Text entrada_usuario;
	private String usuario;
	
	private Text entrada_contrase�a;
	private String contrase�a;
	
	public entradaUsuarioContrase�a(Shell parent) {
		super(parent);
	}
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Iniciar sesi�n");
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
		createContrase�a(container);
		return area;
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, NEWUSER_ID, "Nuevo usuario", false);
        createButton(parent, IDialogConstants.OK_ID, "Iniciar sesi�n", true);
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
	
	private void createContrase�a (Composite container)
	{
		Label lbtNombre = new Label(container, SWT.NONE);
		lbtNombre.setText("Contrase�a: ");
		GridData dataPass= new GridData();
		dataPass.grabExcessHorizontalSpace = true;
		dataPass.horizontalAlignment = GridData.FILL;
		entrada_contrase�a= new Text(container, SWT.BORDER | SWT.PASSWORD);
		entrada_contrase�a.setLayoutData(dataPass);
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
		contrase�a = entrada_contrase�a.getText();
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed()
	{
		entrada_usuario.setText("");
		entrada_contrase�a.setText("");
		super.cancelPressed();
	}
	
	public String getUsuario()
	{
		return usuario;
	}
	
	public String getContrase�a()
	{
		return contrase�a;
	}
}
