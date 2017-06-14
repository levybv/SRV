package br.com.marisa.srv.util.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.util.vo.MesVO;
import br.com.marisa.srv.util.vo.PeriodoMesAnoVO;


/**
 * 
 * @author levy.villar
 *
 */
public class DataBusinessAjax {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<MesVO> obtemListaMes() throws Exception {

		List<MesVO> listaMes = new ArrayList<MesVO>();
		for (int i = 1; i <= 12; i++) {
			listaMes.add(new MesVO(i, DataHelper.obtemMesExtenso(i, Constantes.CAPTALIZADO_CASE)));
		}
		return listaMes;

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Integer> listaAnosQtdMeses(int qtdMeses) throws Exception {
		return DataHelper.listaAnosQtdMeses(qtdMeses);
	}

	/**
	 * 
	 * @param qtdMeses
	 * @return
	 * @throws Exception
	 */
	public List<PeriodoMesAnoVO> listaUltimosPeriodosMesAno(int qtdMeses) throws Exception {
		return DataHelper.listaUltimosPeriodosMesAno(qtdMeses);
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public boolean isDataInvalida(String data) {
		boolean isDataInvalida = true;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			sdf.parse(data);
			isDataInvalida = false;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return isDataInvalida;		
	}

}