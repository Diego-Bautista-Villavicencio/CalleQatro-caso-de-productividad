package proyectoAbneriaMain;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class DeterminarDiasLaborados extends TitleAreaDialog{
	
	Spinner inAbsencias;
	Spinner inIncapacidad;
	Spinner inVacaciones;
	
	int absencias;
	int incapacidades;
	int vacaciones;
	
	private Shell parentShellO;
	private int index;
	
	public DeterminarDiasLaborados(Shell parentShellI, int empleadoIndex) 
	{
		super(parentShellI);
		parentShellO = parentShellI;
		index = empleadoIndex;
	}
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Modificar datos de trabajo");
	}
	
	@Override
	public void create()
	{
		super.create();
		setTitle("Modificar datos de " + VentanaSelecciónPrograma.getEmpleadosActuales().get(index).getNombre());
		setMessage("Ingrese los datos que van a ser actualizados para este empleado", IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		Label lbtAbsencias = new Label(container, SWT.NONE);
		lbtAbsencias.setText("Días de absencia");
		inAbsencias = new Spinner(container, SWT.BORDER);
		Label lbtIncapacidad = new Label(container, SWT.NONE);
		lbtIncapacidad.setText("Días de incapacidad");
		inIncapacidad = new Spinner(container, SWT.BORDER);
		Label lbtVacaciones = new Label(container, SWT.NONE);
		lbtVacaciones.setText("Días de vacaciones");
		inVacaciones = new Spinner(container, SWT.BORDER);
		
		inAbsencias.setMaximum((YearMonth.of(Year.now().getValue(), LocalDate.now().getMonthValue())).lengthOfMonth());
		inAbsencias.setMinimum(0);
		
		inIncapacidad.setMaximum((YearMonth.of(Year.now().getValue(), LocalDate.now().getMonthValue())).lengthOfMonth());
		inIncapacidad.setMinimum(0);
		
		inVacaciones.setMaximum((YearMonth.of(Year.now().getValue(), LocalDate.now().getMonthValue())).lengthOfMonth());
		inVacaciones.setMinimum(0);
		return area;
	}
	
	private void saveInput()
	{
		absencias = inAbsencias.getSelection();
		incapacidades = inIncapacidad.getSelection();
		vacaciones = inVacaciones.getSelection();
		
	}
	
	@Override
	protected void okPressed()
	{
		MessageDialog confirmar = new MessageDialog(parentShellO, "Confirmar información", null, "¿Desea registrar a un empleado con esta información?", MessageDialog.CONFIRM, new String[] {"Registrar", "Corregir"}, 0);
		if (confirmar.open() == 0)
		{
			saveInput();
			super.okPressed();
		}
	}
	
	public int getAbsencias()
	{
		return absencias;
	}
	
	public int getIncapacidades()
	{
		return incapacidades;
	}
	
	public int getVacaciones()
	{
		return vacaciones;
	}
}
