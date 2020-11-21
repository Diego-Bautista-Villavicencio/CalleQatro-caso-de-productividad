package proyectoAbneriaMain;

import java.util.LinkedList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class ListaReporteNomina extends Dialog
{
	private Shell parentO;
	LinkedList<Empleados> ListaEmpleados = VentanaSelecciónPrograma.getEmpleadosActuales();
	
	public ListaReporteNomina(Shell parentI) {
		super(parentI);
		parentO = parentI;
	}
	
	List list;
	int index;
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Seleccionar empleado");
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
		
		list = new List(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		list.setBounds(40, 20, 220, 100);
		
		LinkedList<Empleados> ListaEmpleados = VentanaSelecciónPrograma.getEmpleadosActuales();
		
		for (int i = 0; i < ListaEmpleados.size(); i++)
		{
			list.add("Clave " + ListaEmpleados.get(i).getClave() + ": " +  ListaEmpleados.get(i).getNombre());
		}
		
		
		return area;
	}
	
	@Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Seleccionar", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancelar", false);
	}
	

	private void saveInput()
	{
		index = list.getSelectionIndex();
	}
	
	@Override
	protected void okPressed()
	{
		saveInput();
		MessageDialog confirmar = new MessageDialog(parentO, "Confirmar información", null, ("¿Desea observar/modificar la información de " + ListaEmpleados.get(index).getNombre() +"?"), MessageDialog.CONFIRM, new String[] {"Obtener", "Corregir"}, 0);
		int result = confirmar.open();
		
		if (result == 0)
		{		
				super.okPressed();
		}
			
	}
	
	public int getIndex()
	{
		return index;
	}
	
}
