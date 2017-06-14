package br.com.marisa.srv.geral.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.util.vo.PeriodoMesAnoVO;

/**
 * Classe utilitária para tratamentos genéricos de datas
 * 
 * @author Walter Fontes
 *
 */
public class DataHelper {

	//Log4J
	private static final Logger log = Logger.getLogger(DataHelper.class);

	public static final String PATTERN_DATE_ANO_MES = "yyyyMM";
	public static final String PATTERN_DATE_MES_ANO = "MM/yyyy";

	//Construtor privado. Os métodos devem ser estáticos
	private DataHelper() {}

	/**
	 * Obtém objeto date no último milissegundo do dia passado
	 * por parãmetro
	 * 
	 * @param data
	 * @return
	 */
	public static Date obtemUltimoMomentoDia(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 97);
		return calendar.getTime();
	}

	/**
	 * Obtém objeto date no primeiro milissegundo do dia passado
	 * por parãmetro
	 * 
	 * @param data
	 * @return
	 */
	public static Date obtemPrimeiroMomentoDia(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}


	/**
	 * Obtém data por extenso
	 * 
	 * @param data
	 * @return
	 */
	public static String obtemDataPorExtenso(Date data) {  

		String mesf = null;  
		String retorno = null;  

		Calendar calendar = new GregorianCalendar();  
		calendar.setTime(data);  

		int mes 	= calendar.get(Calendar.MONTH);  
		int dia 	= calendar.get(Calendar.DAY_OF_MONTH);  
		int ano 	= calendar.get(Calendar.YEAR);  

		// mês  
		switch(mes) {  
		case 0:  mesf = "Janeiro"; 		break;
		case 1:  mesf = "Fevereiro";	break;  
		case 2:  mesf = "Março";  		break;
		case 3:  mesf = "Abril";  		break;
		case 4:  mesf = "Maio";  		break;
		case 5:  mesf = "Junho";  		break;
		case 6:  mesf = "Julho";  		break;
		case 7:  mesf = "Agosto";  		break;
		case 8:  mesf = "Setembro";  	break;
		case 9:  mesf = "Outubro";		break;  		
		case 10: mesf = "Novembro";  	break;
		case 11: mesf = "Dezembro";		break;  
		}  
		retorno = dia+" de "+mesf+" de "+ano;  
		return retorno;  
	}

	public static String obtemMesAnoPorExtenso(Date data) {  

		String mesf = null;  
		String retorno = null;  

		Calendar calendar = new GregorianCalendar();  
		calendar.setTime(data);  

		int mes 	= calendar.get(Calendar.MONTH);  
		int ano 	= calendar.get(Calendar.YEAR);  

		// mês  
		switch(mes) {  
		case 0:  mesf = "Janeiro"; 		break;
		case 1:  mesf = "Fevereiro";	break;  
		case 2:  mesf = "Março";  		break;
		case 3:  mesf = "Abril";  		break;
		case 4:  mesf = "Maio";  		break;
		case 5:  mesf = "Junho";  		break;
		case 6:  mesf = "Julho";  		break;
		case 7:  mesf = "Agosto";  		break;
		case 8:  mesf = "Setembro";  	break;
		case 9:  mesf = "Outubro";		break;  		
		case 10: mesf = "Novembro";  	break;
		case 11: mesf = "Dezembro";		break;  
		}  
		retorno = mesf+" de "+ano;  
		return retorno;  
	}


	/**
	 * Obtém o ano de uma data
	 * 
	 * @param data
	 * @return
	 */
	public static int obtemAnoData(Date data) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		return calendar.get(Calendar.YEAR);
	}


	/**
	 * Obtém diferença entre horas
	 * 
	 * @param horaIncial (HH:mm)
	 * @param horaFinal (HH:mm)
	 * @return
	 */
	public static String obtemDiferencaHoras(String horaIncialFormatada, String horaFinalFormatada) throws Exception {

		String diferencaHorasStr = null;

		try {
			int totalMinutosInicial = (Integer.parseInt(horaIncialFormatada.split(":")[0]) * 60) + Integer.parseInt(horaIncialFormatada.split(":")[1]);
			int totalMinutosFinal   = (Integer.parseInt(horaFinalFormatada.split(":")[0])  * 60) + Integer.parseInt(horaFinalFormatada.split(":")[1]);

			int diferencaMinutos = totalMinutosFinal - totalMinutosInicial;
			if (diferencaMinutos < 0) {
				return "00:00";
			}

			int diferencaHoras = diferencaMinutos / 60;
			diferencaMinutos = diferencaMinutos % 60;  

			diferencaHorasStr = ((diferencaHoras   < 10) ? "0" + diferencaHoras:   String.valueOf(diferencaHoras))      + ":" +
			((diferencaMinutos < 10) ? "0" + diferencaMinutos: String.valueOf(diferencaMinutos));

		} catch (Exception e) {
			log.error("Ocorreu erro ao obter a diferença entre as horas " + horaIncialFormatada + " e " + horaFinalFormatada);
			throw e;
		}
		return diferencaHorasStr;
	}


	/**
	 * Obtém diferente entre a data passada por parâmetro e data atual em anos (idade) 
	 * 
	 * @param dataInicialFormatada (dd/MM/yyyy)
	 * @return
	 */
	public static int obtemDiferencaAnos(String dataInicialFormatada) throws Exception {

		int diferencaAnos = 0;

		try {
			Calendar dataInicial = new GregorianCalendar();
			dataInicial.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(dataInicialFormatada));

			Calendar dataAtual = new GregorianCalendar();

			int anoInicial = dataInicial.get(Calendar.YEAR);
			int mesInicial = dataInicial.get(Calendar.MONTH);
			int diaInicial = dataInicial.get(Calendar.DATE);

			int anoAtual = dataAtual.get(Calendar.YEAR);
			int mesAtual = dataAtual.get(Calendar.MONTH);
			int diaAtual = dataAtual.get(Calendar.DATE);

			diferencaAnos = anoAtual - anoInicial;

			if (mesAtual < mesInicial) {  
				diferencaAnos --;

			} else if (mesAtual == mesInicial) {
				if (diaAtual < diaInicial) {
					diferencaAnos --;
				}
			}

		} catch (Exception e) {
			log.error("Ocorreu erro ao obter a diferença entre a data " + dataInicialFormatada + " e a data atual.");
			throw e;
		}
		return diferencaAnos;
	}    


	/**
	 * Formatar data no formato dd/MM/yyyy
	 * @param data
	 * @return
	 */
	public static String formataData(Date data) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").format(data);
		} catch(Exception e) {
			return "";
		}
	}
	
	/**
	 * Formatar data no formato dd/MM/yyyy HH:mm
	 * @param data
	 * @return
	 */
	public static String formataDataHora(Date data) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
		} catch(Exception e) {
			return "";
		}
	}	
	
	
	/**
	 * Obtém diferente entre dias 
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static int obtemDiferencaDias(Date dataInicial, Date dataFinal) throws Exception {

		int diferencaDias = 0;

		try {
			
			dataInicial = obtemPrimeiroMomentoDia(dataInicial);
			dataFinal = obtemPrimeiroMomentoDia(dataFinal);
			
			if (dataInicial.before(dataFinal)) {
				Calendar cDataInicial = new GregorianCalendar();
				cDataInicial.setTime(dataInicial);
				
				while (cDataInicial.getTime().before(dataFinal)) {
					diferencaDias++;
					cDataInicial.add(Calendar.DATE, 1);
				}
				return diferencaDias; 
			
			} else if (dataInicial.after(dataFinal)) {
				Calendar cDataFinal = new GregorianCalendar();
				cDataFinal.setTime(dataFinal);
				
				while (dataInicial.after(cDataFinal.getTime())) {
					diferencaDias++;
					cDataFinal.add(Calendar.DATE, 1);
				}
				return diferencaDias * -1; 
				
			} else {
				return diferencaDias;
			}

		} catch (Exception e) {
			log.error("Ocorreu erro ao obter a diferença entre de dias entre duas datas.");
			throw e;
		}
	}

	
	/**
	 * Obtém data atual
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date hoje() throws ParseException {
		return new Date();
	} 	
	

	/**
	 * Obtém ano atual
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static int anoAtual() throws ParseException {
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.YEAR);
	} 	
	
	
	/**
	 * Verifica se a data é a data atual
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static boolean dataAtual(Date date) throws Exception {
		return (obtemDiferencaDias(date, new Date()) == 0);
	}	
	
	/**
	 * obtem o mes por extenso
	 * @param mes
	 * @param formatoCase
	 * @return
	 */
	public static java.lang.String obtemMesExtenso(int mes,String formatoCase) {
		String mesExt= null;
		if(Constantes.CAPTALIZADO_CASE.equals(formatoCase)) {
			mesExt = Constantes.MESES[mes-1];
		}else if(Constantes.UPPER_CASE.equals(formatoCase)) {
			mesExt = Constantes.MESES[mes-1].toUpperCase();
		}else if(Constantes.LOW_CASE.equals(formatoCase)) {
			mesExt = Constantes.MESES[mes-1].toLowerCase();
		}
		return mesExt;
	}
	
	
	/**
	 * Obtém data no formato dd/MM/yyyy
	 * @param data
	 * @return
	 */
	public static Date obtemData(String data) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(data);
		} catch(Exception e) {
			return null;
		}
	}	

	/**
	 * 
	 * @return
	 */
	public static Date obtemDataAtualDiaMesAno() {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		} catch(Exception e) {
			return null;
		}
	}	

	/**
	 * Retorna uma lista com o ano dentro de um intervalo de meses, baseado na data atual.
	 * 
	 * Parametro zero: retorna apenas o ano atual
	 * Parametro positivo: retorna ano atual ate ano da qtd meses a frente
	 * Parametro negativo: retorna ano atual ate ano da qtd meses atras
	 * 
	 * 
	 * @param qtdMeses
	 * @return
	 */
	public static List<Integer> listaAnosQtdMeses(int qtdMeses) {
		List<Integer> listaAno = new ArrayList<Integer>();
		
		Calendar c = Calendar.getInstance();
		int anoCorrente = c.get(Calendar.YEAR);
		c.add(Calendar.MONTH, qtdMeses);
		int anoCalculo = c.get(Calendar.YEAR);
		if (anoCorrente > anoCalculo) {
			listaAno.add(anoCorrente);
			while (anoCorrente > anoCalculo) {
				listaAno.add(--anoCorrente);
			}
		} else if (anoCorrente < anoCalculo) {
			listaAno.add(anoCalculo);
			while (anoCalculo > anoCorrente) {
				listaAno.add(--anoCalculo);
			}
		} else {
			listaAno.add(anoCorrente);
		}
		return listaAno;
	}

	/**
	 * 
	 * @param qtdMeses
	 * @return
	 */
	public static List<PeriodoMesAnoVO> listaUltimosPeriodosMesAno(int qtdMeses) {
		List<PeriodoMesAnoVO> listaPeriodos = new ArrayList<PeriodoMesAnoVO>();
		for (int i = 0; i >= qtdMeses; i--) {
			PeriodoMesAnoVO periodo = new PeriodoMesAnoVO();
			Calendar data = Calendar.getInstance();
			data.add(Calendar.MONTH, i);
			periodo.setPeriodo(new SimpleDateFormat("MM/yyyy").format(data.getTime()));
			periodo.setPeriodoDesc(obtemMesExtenso(data.get(Calendar.MONTH)+1, Constantes.CAPTALIZADO_CASE) + "/" + data.get(Calendar.YEAR));
			listaPeriodos.add(periodo);
		}
		return listaPeriodos;
	}

	/**
	 * 
	 * @param valor
	 * @param mask
	 * @return
	 */
	public static int obtemNumAno(String valor, String mask) {
		int numAno = -1;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(mask);
			Date data = sdf.parse(valor);
			Calendar c = Calendar.getInstance();
			c.setTime(data);
			numAno = c.get(Calendar.YEAR);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return numAno;
	}

	/**
	 * 
	 * @param valor
	 * @param mask
	 * @return
	 */
	public static int obtemNumMes(String valor, String mask) {
		int numMes = -1;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(mask);
			Date data = sdf.parse(valor);
			Calendar c = Calendar.getInstance();
			c.setTime(data);
			numMes = c.get(Calendar.MONTH) + 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return numMes;
	}

}