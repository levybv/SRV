package br.com.marisa.srv.relatorio.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioCallCenterBusinessAjax {

	public String validaPeriodo(String paramDataInicio, String paramDataFim, String paramAno, String paramMes) throws Exception {

		String mensagem = "";
		Date dataInicio = null;
		Date dataFim = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);

		if ((paramDataInicio != null && !paramDataInicio.equalsIgnoreCase("")) && (paramDataFim != null && !paramDataFim.equalsIgnoreCase(""))) {

			try {
				dataInicio = sdf.parse(paramDataInicio);
			} catch (ParseException pe) {
				return "Data inicial do período é inválida!";
			}

			try {
				dataFim = sdf.parse(paramDataFim);
			} catch (ParseException pe) {
				return "Data final do período é inválida!";
			}

			if (dataInicio.after(dataFim)) {
				return "Data inicial não deve ser maior que a data final!";
			}

			int mes = Integer.parseInt(paramMes);

			int mesDataInicio = Integer.parseInt(paramDataInicio.substring(3,5));
			if (mes != mesDataInicio) {
				return "O mês da data inicial não condiz com o mês selecionado!";
			}

			int mesDataFim = Integer.parseInt(paramDataFim.substring(3,5));
			if (mes != mesDataFim) {
				return "O mês da data final não condiz com o mês selecionado!";
			}

			int ano = Integer.parseInt(paramAno);

			int anoDataInicio = Integer.parseInt(paramDataInicio.substring(6,10));
			if (ano != anoDataInicio) {
				return "O ano da data inicial não condiz com o mês selecionado!";
			}

			int anoDataFim = Integer.parseInt(paramDataFim.substring(6,10));
			if (ano != anoDataFim) {
				return "O ano da data final não condiz com o mês selecionado!";
			}

		}
		return mensagem;
	}

}