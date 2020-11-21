package proyectoAbneriaMain;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Acerca_de extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public Acerca_de(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Acerca de...");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblAcercaDeEste = new Label(container, SWT.NONE);
		lblAcercaDeEste.setText("Acerca de este producto");
		new Label(container, SWT.NONE);
		
		Label lblSistemaDeNmina = new Label(container, SWT.NONE);
		lblSistemaDeNmina.setText("Sistema de n\u00F3mina CalleQatro");
		
		Label lblVersion = new Label(container, SWT.NONE);
		lblVersion.setText("Versi\u00F3n 2020-03-01 (1.3)");
		
		Label lblPropiedadIntelectualDe = new Label(container, SWT.NONE);
		lblPropiedadIntelectualDe.setText("Propiedad intelectual de Abner\u00EDa S.A. de C.V.");
		
		Label lblCdigoPorDiego = new Label(container, SWT.NONE);
		lblCdigoPorDiego.setText("C\u00F3digo por Diego Bautista Villavicencio");
		
		Label lblPrograamDesarrolladoEn = new Label(container, SWT.NONE);
		lblPrograamDesarrolladoEn.setText("Programa desarrollado en el Tetramestre Empresarial TecMilenio Enero-Abril 2020");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
