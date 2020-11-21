package proyectoAbneriaMain;

import java.util.LinkedList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class ListaUsuarios extends Dialog
{
	List list;

	protected ListaUsuarios(Shell parentShell) 
	{
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Lista de usuarios");
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		GridData gd_container = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd_container.widthHint = 424;
		container.setLayoutData(gd_container);
		GridLayout layout = new GridLayout(1, false);
		container.setLayout(layout);
		
		list = new List(container, SWT.BORDER | SWT.NONE | SWT.V_SCROLL);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		list.setBounds(40, 20, 220, 100);
		
		LinkedList<Usuarios> ListaUsuarios = VentanaSelecciónPrograma.getUsuariosActuales();
		
		for (int i = 0; i < ListaUsuarios.size(); i++)
		{
			list.add(ListaUsuarios.get(i).getNUsuario() + " (" + ListaUsuarios.get(i).getNCompleto() + "), " + DeterminarAutoridad(ListaUsuarios.get(i).getLvlUsuario()) + ".");
		}
		
		
		return area;
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Cerrar", true);
	}
	
	private String DeterminarAutoridad(int nivel)
	{
		String returner;
		
		switch (nivel)
		{
		case 1:
			returner = "superadministrador";
			break;
		case 2:
			returner = "administrador";
			break;
		case 3:
			returner = "usuario estándar";
			break;
		default:
			returner = "rol indeterminado";
			break;
		}
		
		return returner;
	}

}
