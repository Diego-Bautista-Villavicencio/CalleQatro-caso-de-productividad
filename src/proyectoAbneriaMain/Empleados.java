package proyectoAbneriaMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;

public class Empleados {

	private int Clave;
	private String Nombre;
	private int idDepartamento;
	private String Departamento;
	private String NSS;
	private String RFC;
	private String CURP;
	private String Registro;
	private long Longevidad;
	private BigDecimal salario;
	private Properties prop;
	
	DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	DateTimeFormatter myFormatObj2 = DateTimeFormatter.ofPattern("dd-MM-yy' 'hh'H'mm'M'ss'S'");
	
	public Empleados(int clave, String nombre, int idDepartamento, String Departamento, String NSS, String RFC, String CURP, String Registro, BigDecimal salario, boolean isNew) throws SQLException, IOException
	{
		this.Clave = clave;
		this.Nombre = nombre;
		this.idDepartamento = idDepartamento;
		this.Departamento = Departamento;
		this.NSS = NSS;
		this.RFC = RFC;
		this.CURP = CURP;
		this.Registro = Registro;
		this.Longevidad = ChronoUnit.YEARS.between(LocalDate.parse(Registro, myFormatObj), LocalDate.now());
		this.salario = salario;
		
		if (isNew)
		{
			prop = VentanaSelecciónPrograma.getProps();
			
			Connection conn = VentanaSelecciónPrograma.conectarABaseDeDatos(prop.getProperty("MySQLDir"), prop.getProperty("MySQLUser"), prop.getProperty("MySQLPass"));
			PreparedStatement preparedStatement = null;
			
			preparedStatement = conn.prepareStatement("INSERT INTO empleados VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
			
			preparedStatement.setInt(1, this.Clave);
			preparedStatement.setString(2, this.Nombre);
			preparedStatement.setString(3, this.Departamento);
			preparedStatement.setString(4, this.NSS);
			preparedStatement.setString(5, this.RFC);
			preparedStatement.setString(6, this.CURP);
			preparedStatement.setBigDecimal(7, this.salario);
			preparedStatement.setString(8, this.Registro);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			conn.close();
			
			System.err.println("Buscando archivo...");
			File main = new File (VentanaSelecciónPrograma.getMainRoute());
			System.err.println("FIS...");
			FileInputStream FIS = new FileInputStream(main);
			System.err.println("Excel...");
			XSSFWorkbook libro = new XSSFWorkbook(FIS);
			System.err.println("Hoja 1...");
			XSSFSheet hoja1 = libro.getSheet("CALCULO Y BASE");
			System.err.println("Hoja 2...");
			XSSFSheet hoja2 = libro.getSheet("Empleados");
			FormulaEvaluator evaluator = libro.getCreationHelper().createFormulaEvaluator();
			
			System.err.println("Crear fila...");
			XSSFRow newRow = hoja1.createRow(hoja1.getPhysicalNumberOfRows());
			newRow.createCell(0).setCellValue(this.Clave);
			newRow.createCell(1).setCellValue(this.Nombre);
			newRow.createCell(2).setCellValue(this.NSS);
			newRow.createCell(3).setCellValue(this.RFC);
			newRow.createCell(4).setCellValue(this.CURP);
			newRow.createCell(5).setCellValue(this.Departamento);
			newRow.createCell(6).setCellValue(this.idDepartamento);
			newRow.createCell(7).setCellValue(this.Registro);
			newRow.createCell(8).setCellValue((this.salario.doubleValue()));
			newRow.createCell(11).setCellFormula("VLOOKUP($C$3,TABLAS!$A$2:$B$13,2,0)");
			newRow.createCell(12).setCellValue(0);
			newRow.createCell(13).setCellValue(0);
			newRow.createCell(14).setCellValue(0);
			newRow.createCell(15).setCellFormula("L" + (newRow.getRowNum() + 1)+ "-M" + (newRow.getRowNum() + 1) + "+N" + (newRow.getRowNum() + 1));
			newRow.createCell(16).setCellFormula("I" + (newRow.getRowNum() + 1)+ "*L" + (newRow.getRowNum() + 1));
			newRow.createCell(17).setCellFormula("VLOOKUP($Q" + (newRow.getRowNum() + 1) + ",TARIFAS!$U$7:$X$18,1)");
			newRow.createCell(18).setCellFormula("Q" + (newRow.getRowNum() + 1)+ "-R" + (newRow.getRowNum() + 1));
			newRow.createCell(19).setCellFormula("VLOOKUP($Q" + (newRow.getRowNum() + 1) + ",TARIFAS!$U$7:$X$18,4)%");
			newRow.createCell(20).setCellFormula("S" + (newRow.getRowNum() + 1)+ "*T" + (newRow.getRowNum() + 1));
			newRow.createCell(21).setCellFormula("VLOOKUP($Q" + (newRow.getRowNum() + 1) + ",TARIFAS!$U$7:$X$18,3)");
			newRow.createCell(22).setCellFormula("U" + (newRow.getRowNum() + 1)+ "+V" + (newRow.getRowNum() + 1));
			newRow.createCell(23).setCellFormula("VLOOKUP($Q"+ (newRow.getRowNum() + 1) + ",TARIFAS!$U$24:$W$34,3)");
			newRow.createCell(24).setCellFormula("Q" + (newRow.getRowNum() + 1) + "-W" + (newRow.getRowNum() + 1) + "+X" + (newRow.getRowNum() + 1));
			newRow.createCell(25).setCellFormula("(O" + (newRow.getRowNum() + 1) + "*I" + (newRow.getRowNum() + 1) + ")*0.25");
			newRow.createCell(26).setCellFormula("\'IMSS MESUAL\'!X" + (newRow.getRowNum() + 1) + "+('IMSS BIM'!O50/2)");
			newRow.createCell(27).setCellValue(0);
			newRow.createCell(28).setCellValue(0);
			newRow.createCell(29).setCellFormula(("Y" + (newRow.getRowNum() + 1) + "-AA" + (newRow.getRowNum() + 1) + "+AB" + (newRow.getRowNum() + 1)) + "-AC" + (newRow.getRowNum() + 1));
			
			XSSFRow newRow2 = hoja2.createRow(hoja2.getPhysicalNumberOfRows());
			
			newRow2.createCell(0).setCellValue(this.Clave);
			newRow2.createCell(1).setCellValue(this.Nombre);
			newRow2.createCell(2).setCellValue(this.Departamento);
			newRow2.createCell(3).setCellValue(this.NSS);
			newRow2.createCell(4).setCellValue(this.RFC);
			newRow2.createCell(5).setCellValue(this.CURP);
			newRow2.createCell(6).setCellValue(this.Registro);
			newRow2.createCell(7).setCellValue(this.salario.doubleValue());
			newRow2.createCell(8).setCellFormula("(TODAY()-G" + (newRow2.getRowNum()+1) + ")/365");
			
			evaluator.evaluateAll();
			
			FileOutputStream FOS = new FileOutputStream(main);
			
			libro.write(FOS);
			
			FOS.close();
			libro.close();
		}
	}
	
	public void getSalarioFinal() throws IOException
	{
		DecimalFormat df = new DecimalFormat("#,###,###.##");
		Locale MX = new Locale("es", "MX");
		NumberFormat transcript = new RuleBasedNumberFormat(MX, RuleBasedNumberFormat.SPELLOUT);
		
		File main = new File (VentanaSelecciónPrograma.getMainRoute());
		FileInputStream FIS = new FileInputStream(main);
		XSSFWorkbook libro = new XSSFWorkbook(FIS);
		XSSFSheet hoja = libro.getSheet("RECIBO NOMINA");
		FormulaEvaluator evaluator = libro.getCreationHelper().createFormulaEvaluator();
		
		XWPFDocument reporte = new XWPFDocument();
		
		XWPFParagraph titulo = reporte.createParagraph();
		titulo.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun titleRun = titulo.createRun();
		
		titleRun.setText("Abnería S.A. de C.V.");
		titleRun.setColor("000000");
		titleRun.setBold(true);
		titleRun.setFontFamily("Arial");
		titleRun.setFontSize(16);
		
		XWPFParagraph subtitulo = reporte.createParagraph();
		subtitulo.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun subRun = subtitulo.createRun();
		
		subRun.setText("CalleQatro Ajusco");
		subRun.setColor("2F5496");
		subRun.setBold(false);
		subRun.setFontFamily("Arial");
		subRun.setFontSize(12);
		subRun.addBreak();
		
		Cell cedula = hoja.getRow(9).getCell(3);
		cedula.setCellValue(Double.valueOf(this.Clave));
		
		
		XWPFParagraph direccion = reporte.createParagraph();
		XWPFRun dirRun = direccion.createRun();
		
		cedula = hoja.getRow(6).getCell(0);
		
		dirRun.setText("Dirección: " + cedula.getStringCellValue());
		dirRun.setColor("000000");
		dirRun.setBold(false);
		dirRun.setFontFamily("Arial");
		dirRun.setFontSize(9);
		
		XWPFParagraph regPatr = reporte.createParagraph();
		XWPFRun regRun = regPatr.createRun();
		
		cedula = hoja.getRow(7).getCell(1);
		
		regRun.setText("Reporte patronal: " + cedula.getStringCellValue());
		regRun.setColor("000000");
		regRun.setBold(false);
		regRun.setFontFamily("Arial");
		regRun.setFontSize(9);
		
		XWPFParagraph rfcPatr = reporte.createParagraph();
		XWPFRun rfcRun = rfcPatr.createRun();
		
		cedula = hoja.getRow(7).getCell(3);
		
		rfcRun.setText("R.F.C de la empresa: " + cedula.getStringCellValue());
		rfcRun.setColor("000000");
		rfcRun.setBold(false);
		rfcRun.setFontFamily("Arial");
		rfcRun.setFontSize(9);
		rfcRun.addBreak();
		
		XWPFParagraph recibo = reporte.createParagraph();
		XWPFRun recRun = recibo.createRun();
		
		
		recRun.setText("RECIBO DE NÓMINA");
		recRun.setColor("012060");
		recRun.setBold(true);
		recRun.setFontFamily("Arial");
		recRun.setFontSize(9);
		
		XWPFParagraph noTrabajador = reporte.createParagraph();
		XWPFRun noRun = noTrabajador.createRun();
		
		noRun.setText("Número de trabajador: " + this.Clave);
		noRun.setColor("000000");
		noRun.setBold(false);
		noRun.setFontFamily("Arial");
		noRun.setFontSize(9);
		
		XWPFParagraph Recibe = reporte.createParagraph();
		XWPFRun reRun1 = Recibe.createRun();
		
		reRun1.setText("Recibí la cantidad de: ");
		reRun1.setColor("000000");
		reRun1.setBold(false);
		reRun1.setFontFamily("Arial");
		reRun1.setFontSize(9);
		
		XWPFRun reRun2 = Recibe.createRun();
		
		cedula = hoja.getRow(10).getCell(2);
		evaluator.evaluateAll();
		
		BigDecimal traductor1 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor1 = traductor1.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		reRun2.setText("$" + df.format(traductor1));
		reRun2.setColor("000000");
		reRun2.setBold(true);
		reRun2.setFontFamily("Arial");
		reRun2.setFontSize(9);
		reRun2.addBreak();
		
		XWPFRun reRun3 = Recibe.createRun();
		
		reRun3.setText((" (" + transcript.format(traductor1.intValue()) +  " pesos, " + traductor1.remainder(BigDecimal.ONE).multiply(new BigDecimal(100)).intValue() + "/100)").toUpperCase());
		reRun3.setColor("000000");
		reRun3.setBold(true);
		reRun3.setFontFamily("Arial");
		reRun3.setFontSize(9);
		
		XWPFParagraph nombre = reporte.createParagraph();
		XWPFRun nomRun = nombre.createRun();
		nomRun.setText("Nombre: " + this.Nombre);
		nomRun.setColor("000000");
		nomRun.setBold(false);
		nomRun.setFontFamily("Arial");
		nomRun.setFontSize(9);
		
		Period periodoLongevidad = this.getDateRegistro().until(LocalDate.now());
		
		XWPFParagraph longev = reporte.createParagraph();
		XWPFRun longevRun = longev.createRun();
		longevRun.setText("Trabajado por: " + periodoLongevidad.getYears() + " años, " + periodoLongevidad.getMonths() + " meses, " + periodoLongevidad.getDays() + " días.");
		longevRun.setColor("000000");
		longevRun.setBold(false);
		longevRun.setFontFamily("Arial");
		longevRun.setFontSize(9);
		
		XWPFParagraph puesto = reporte.createParagraph();
		XWPFRun pstRun = puesto.createRun();
		pstRun.setText("Departamento: " + this.Departamento);
		pstRun.setColor("000000");
		pstRun.setBold(false);
		pstRun.setFontFamily("Arial");
		pstRun.setFontSize(9);
		
		cedula = hoja.getRow(9).getCell(7);
		
		XWPFParagraph dias = reporte.createParagraph();
		XWPFRun diasRun = dias.createRun();
		diasRun.setText("Días trabajados: " + (int)cedula.getNumericCellValue());
		diasRun.setColor("000000");
		diasRun.setBold(false);
		diasRun.setFontFamily("Arial");
		diasRun.setFontSize(9);
		
		cedula = hoja.getRow(10).getCell(7);
		
		XWPFParagraph faltas = reporte.createParagraph();
		XWPFRun faltasRun = faltas.createRun();
		faltasRun.setText("Faltas: " + (int)cedula.getNumericCellValue());
		faltasRun.setColor("000000");
		faltasRun.setBold(false);
		faltasRun.setFontFamily("Arial");
		faltasRun.setFontSize(9);
		
		XWPFParagraph RFCE = reporte.createParagraph();
		XWPFRun rfcERun = RFCE.createRun();
		rfcERun.setText("R.F.C: " + this.RFC);
		rfcERun.setColor("000000");
		rfcERun.setBold(false);
		rfcERun.setFontFamily("Arial");
		rfcERun.setFontSize(9);
		
		XWPFParagraph CURP = reporte.createParagraph();
		XWPFRun CURPRun = CURP.createRun();
		CURPRun.setText("CURP: " + this.CURP);
		CURPRun.setColor("000000");
		CURPRun.setBold(false);
		CURPRun.setFontFamily("Arial");
		CURPRun.setFontSize(9);
		
		XWPFParagraph NSS = reporte.createParagraph();
		XWPFRun NSSRun = NSS.createRun();
		NSSRun.setText("Número de IMSS: " + this.NSS);
		NSSRun.setColor("000000");
		NSSRun.setBold(false);
		NSSRun.setFontFamily("Arial");
		NSSRun.setFontSize(9);
		
		XWPFTable tablaOperaciones = reporte.createTable(5, 4);
		
		XWPFTableRow tablaFila1 = tablaOperaciones.getRow(0);
		
		tablaFila1.getCell(1).setText("GRATIFICACIONES");
		tablaFila1.getCell(3).setText("DEDUCCIONES");
		
		XWPFTableRow tablaFila2 = tablaOperaciones.getRow(1);
		
		cedula = hoja.getRow(19).getCell(1);
		
		BigDecimal traductor2 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor2 = traductor2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila2.getCell(0).setText("Sueldo");
		tablaFila2.getCell(1).setText((df.format(traductor2)));
		
		cedula = hoja.getRow(19).getCell(4);
		
		BigDecimal traductor3 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor3 = traductor3.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila2.getCell(2).setText("I.S.R.");
		tablaFila2.getCell(3).setText(df.format(traductor3));
		
		XWPFTableRow tablaFila3 = tablaOperaciones.getRow(2);
		
		cedula = hoja.getRow(20).getCell(1);
		
		BigDecimal traductor4 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor4 = traductor4.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila3.getCell(0).setText("Subsidio del empleo");
		tablaFila3.getCell(1).setText(df.format(traductor4));
		
		cedula = hoja.getRow(20).getCell(4);
		
		BigDecimal traductor5 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor5 = traductor5.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila3.getCell(2).setText("IMSS");
		tablaFila3.getCell(3).setText(df.format(traductor5));
		
		XWPFTableRow tablaFila4 = tablaOperaciones.getRow(3);
		
		cedula = hoja.getRow(21).getCell(1);
		
		BigDecimal traductor6 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor6 = traductor6.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila4.getCell(0).setText("Otras gratificaciones");
		tablaFila4.getCell(1).setText(df.format(traductor6));
		
		cedula = hoja.getRow(21).getCell(4);
		
		BigDecimal traductor7 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor7 = traductor7.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila4.getCell(2).setText("Otras deducciones");
		tablaFila4.getCell(3).setText(df.format(traductor7));
		
		XWPFTableRow tablaFila5 = tablaOperaciones.getRow(4);
		
		cedula = hoja.getRow(22).getCell(1);
		
		BigDecimal traductor8 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor8 = traductor8.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila5.getCell(0).setText("Total percepciones");
		tablaFila5.getCell(1).setText(df.format(traductor8));
		
		cedula = hoja.getRow(22).getCell(4);
		BigDecimal traductor9 = BigDecimal.valueOf(cedula.getNumericCellValue());
		traductor9 = traductor9.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		tablaFila5.getCell(2).setText("Total deducciones");
		tablaFila5.getCell(3).setText(df.format(traductor9));
		
		XWPFParagraph total = reporte.createParagraph();
		XWPFRun ttRun1 = total.createRun();
		
		ttRun1.addBreak();
		ttRun1.setText("Total de nómina: ");
		ttRun1.setColor("000000");
		ttRun1.setBold(false);
		ttRun1.setFontFamily("Arial");
		ttRun1.setFontSize(9);
		
		XWPFRun ttRun2 = total.createRun();
		
		ttRun2.setText("$" + df.format(traductor8.subtract(traductor9)));
		ttRun2.setColor("000000");
		ttRun2.setBold(true);
		ttRun2.setFontFamily("Arial");
		ttRun2.setFontSize(9);
		
		XWPFRun ttRun3 = total.createRun();
		
		ttRun3.setText((" (" + transcript.format((traductor8.subtract(traductor9).intValue())) + " pesos, " + (traductor8.subtract(traductor9).remainder(BigDecimal.ONE)).multiply(new BigDecimal(100)).intValue() + "/100)").toUpperCase());
		ttRun3.setColor("000000");
		ttRun3.setBold(true);
		ttRun3.setFontFamily("Arial");
		ttRun3.setFontSize(9);
		ttRun3.addBreak();
		
		XWPFParagraph firma1 = reporte.createParagraph();
		firma1.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun firRun1 = firma1.createRun();
		firRun1.setText("_________________________________");
		firRun1.setColor("000000");
		firRun1.setBold(false);
		firRun1.setFontFamily("Arial");
		firRun1.setFontSize(9);
		firRun1.addBreak();
		
		XWPFRun firRun2 = firma1.createRun();
		firRun2.setText("Firma del trabajador");
		firRun2.setColor("000000");
		firRun2.setBold(false);
		firRun2.setFontFamily("Arial");
		firRun2.setFontSize(9);
		firRun2.addBreak();
		
		XWPFRun firRun3 = firma1.createRun();
		firRun3.setText("Recibí nómina correspondiente a la");
		firRun3.setColor("000000");
		firRun3.setBold(false);
		firRun3.setFontFamily("Arial");
		firRun3.setFontSize(9);
		firRun3.addBreak();
		
		XWPFRun firRun4 = firma1.createRun();
		firRun4.setText(getQuincena());
		firRun4.setColor("000000");
		firRun4.setBold(false);
		firRun4.setFontFamily("Arial");
		firRun4.setFontSize(9);
		firRun4.addBreak();
		firRun4.addBreak();
		firRun4.addBreak();
		
		XWPFParagraph firma2 = reporte.createParagraph();
		firma2.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun fir2Run1 = firma1.createRun();
		fir2Run1.setText("_________________________________");
		fir2Run1.setColor("000000");
		fir2Run1.setBold(false);
		fir2Run1.setFontFamily("Arial");
		fir2Run1.setFontSize(9);
		fir2Run1.addBreak();
		
		XWPFRun fir2Run2 = firma1.createRun();
		fir2Run2.setText("Nombre del trabajador");
		fir2Run2.setColor("000000");
		fir2Run2.setBold(false);
		fir2Run2.setFontFamily("Arial");
		fir2Run2.setFontSize(9);
		fir2Run2.addBreak();
		
		String Route = VentanaSelecciónPrograma.getRouteFiles() + "\\NOM " + this.Nombre + " " + LocalDateTime.now().format(myFormatObj2) + ".docx";
		FileOutputStream exitFile = new FileOutputStream(Route);
		reporte.write(exitFile);
		exitFile.close();
		reporte.close();
		libro.close();
	}
	
	public int getClave()
	{
		return this.Clave;
	}
	
	public String getNombre()
	{
		return this.Nombre;
	}
	
	public int getidDepartamento()
	{
		return this.idDepartamento;
	}

	public String getDepartamento()
	{
		return this.Departamento;
	}
	
	public String getNSS()
	{
		return this.NSS;
	}
	
	public String getRFC()
	{
		return this.RFC;
	}
	
	public String getCURP()
	{
		return this.CURP;
	}
	
	public String getStringRegistro()
	{
		return this.Registro;
	}
	
	public LocalDate getDateRegistro()
	{
		return LocalDate.parse(this.Registro, myFormatObj);
	}
	
	public long getLongevidad()
	{
		return this.Longevidad;
	}
	
	public BigDecimal getSalarioDiario()
	{
		return salario;
	}
	
	public String getQuincena() 
	{
		String quincena = "Quincena indeterminada.";
		
		LocalDate medidor = LocalDate.now();
		LocalDate bisiesto = LocalDate.of(medidor.getYear(), Month.FEBRUARY, 01);
		
		int num = (medidor.getMonthValue() + (medidor.getMonthValue()-1));
		if (medidor.getDayOfMonth() > 15)
		{
			num++;
		}
		
		switch (num)
		{
			
			case 1:
				quincena = ("1ra quincena, del 01/enero/" + medidor.getYear() + " al 15/enero/" + medidor.getYear() +".");
				break;
			case 2:
				quincena = ("2da quincena, del 16/enero/" + medidor.getYear() + " al 31/enero/" + medidor.getYear() +".");
				break;
			case 3:
				quincena = ("3ra quincena, del 01/febrero/" + medidor.getYear() + " al 15/febrero/" + medidor.getYear() +".");
				break;
			case 4:
				quincena = ("4ta quincena, del 16/febrero/" + medidor.getYear() + " al " + bisiesto.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth() + "/febrero/" + medidor.getYear() +".");
				break;		
			case 5:
				quincena = ("5ta quincena, del 01/marzo/" + medidor.getYear() + " al 15/marzo/" + medidor.getYear() +".");
				break;
			case 6:
				quincena = ("6ta quincena, del 16/marzo/" + medidor.getYear() + " al 31/marzo/" + medidor.getYear() +".");
				break;
			case 7:
				quincena = ("7ma quincena, del 01/abril/" + medidor.getYear() + " al 15/abril/" + medidor.getYear() +".");
				break;
			case 8:
				quincena = ("8va quincena, del 16/abril/" + medidor.getYear() + " al 30/abril/" + medidor.getYear() +".");
				break;
			case 9:
				quincena = ("9na quincena, del 01/mayo/" + medidor.getYear() + " al 15/mayo/" + medidor.getYear() +".");
				break;
			case 10:
				quincena = ("10ma quincena, del 16/mayo/" + medidor.getYear() + " al 31/mayo/" + medidor.getYear() +".");
				break;
			case 11:
				quincena = ("11va quincena, del 01/junio/" + medidor.getYear() + " al 15/junio/" + medidor.getYear() +".");
				break;
			case 12:
				quincena = ("12va quincena, del 16/junio/" + medidor.getYear() + " al 30/junio/" + medidor.getYear() +".");
				break;
			case 13:
				quincena = ("13va quincena, del 01/julio/" + medidor.getYear() + " al 15/julio/" + medidor.getYear() +".");
				break;
			case 14:
				quincena = ("14va quincena, del 16/julio/" + medidor.getYear() + " al 31/julio/" + medidor.getYear() +".");
				break;
			case 15:
				quincena = ("15va quincena, del 01/agosto/" + medidor.getYear() + " al 15/agosto/" + medidor.getYear() +".");
				break;
			case 16:
				quincena = ("16ta quincena, del 16/agosto/" + medidor.getYear() + " al 31/agosto/" + medidor.getYear() +".");
				break;
			case 17:
				quincena = ("17ma quincena, del 01/septiembre/" + medidor.getYear() + " al 15/septiembre/" + medidor.getYear() +".");
				break;
			case 18:
				quincena = ("18va quincena, del 16/septiembre/" + medidor.getYear() + " al 30/septiembre/" + medidor.getYear() +".");
				break;
			case 19:
				quincena = ("19na quincena, del 01/octubre/" + medidor.getYear() + " al 15/octubre/" + medidor.getYear() +".");
				break;
			case 20:
				quincena = ("20va quincena, del 16/octubre/" + medidor.getYear() + " al 31/octubre/" + medidor.getYear() +".");
				break;
			case 21:
				quincena = ("21ra quincena, del 01/noviembre/" + medidor.getYear() + " al 15/noviembre/" + medidor.getYear() +".");
				break;
			case 22:
				quincena = ("22da quincena, del 16/noviembre/" + medidor.getYear() + " al 15/noviembre/" + medidor.getYear() +".");
				break;
			case 23:
				quincena = ("23ra quincena, del 01/diciembre/" + medidor.getYear() + " al 15/diciembre/" + medidor.getYear() +".");
				break;
			case 24:
				quincena = ("24ta quincena, del 16/diciembre/" + medidor.getYear() + " al 31/diciembre/" + medidor.getYear() +".");
				break;
			default:
				break;
		}
		
		
		return quincena;
	}
}

