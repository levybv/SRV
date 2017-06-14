package br.com.marisa.srv.calendario.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.marisa.srv.calendario.vo.CalendarioBonusVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 */
public class CalendarioBonusBusinessAjax {

	/**
	 * 
	 * @param mes
	 * @param ano
	 * @return
	 * @throws SRVException
	 */
	public CalendarioBonusVO obtemCalendarioBonus(Integer mes, Integer ano) throws SRVException {
		CalendarioBonusVO returnVO = null;
		CalendarioBonusVO paramVO = new CalendarioBonusVO();
		paramVO.setAno(ano);
		paramVO.setMes(mes);
		List<CalendarioBonusVO> lista = CalendarioBonusBusiness.getInstance().obtemCalendarioBonus(paramVO);
		if (lista!=null && lista.size() == 1) {
			returnVO = lista.get(0);
		}
		return returnVO;
	}

	/**
	 * 
	 * @param mes
	 * @param ano
	 * @return
	 * @throws SRVException
	 */
	public Boolean validaCalendarioBonus(Integer mes, Integer ano) throws SRVException {
		Boolean result = true;
		CalendarioBonusVO paramVO = new CalendarioBonusVO();
		paramVO.setAno(ano);
		paramVO.setMes(mes);
		List<CalendarioBonusVO> lista = CalendarioBonusBusiness.getInstance().obtemCalendarioBonus(paramVO);
		if (lista!=null && lista.size() > 0) {
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param param
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public Boolean existeIntervaloCalendarioBonus(String param, Integer ano, Integer mes) throws SRVException {
		Boolean result = true;
		try {
			result = CalendarioBonusBusiness.getInstance().existeIntervaloCalendarioBonus(new SimpleDateFormat("dd/MM/yyyy").parse(param), ano, mes);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return !result;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Integer> obtemAnosCalendarioBonus() throws Exception {
		return CalendarioBonusBusiness.getInstance().obtemAnosCalendarioBonus();
	}

}