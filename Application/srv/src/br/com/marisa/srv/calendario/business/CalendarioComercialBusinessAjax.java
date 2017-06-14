package br.com.marisa.srv.calendario.business;

import java.util.List;

import br.com.marisa.srv.calendario.vo.PeriodoCalendarioVO;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.vo.IndicadorPeriodoVO;

/**
 * Classe para conter os m�todos de neg�cio acess�veis por Ajax do m�dulo de Calend�rio
 * 
 * @author Walter Fontes
 */
public class CalendarioComercialBusinessAjax {
	
	/**
	 * Obt�m a lista de per�odos
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<IndicadorPeriodoVO> obtemListaPeriodos() throws SRVException {
		return CalendarioComercialBusiness.getInstance().obtemListaPeriodos();
	}

	public List<PeriodoCalendarioVO> obtemListaPeriodoMesAno() throws SRVException {
		return CalendarioComercialBusiness.getInstance().obtemListaPeriodoMesAno();
	}

	public List<IndicadorPeriodoVO> obtemCalendarioComercialListaAno() throws SRVException {
		return CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true);
	}
	
	public List<IndicadorPeriodoVO> obtemCalendarioComercialListaMes() throws SRVException {
		return CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false);
	}

	public PeriodoCalendarioVO obtemCalendarioComercial(Integer id, Integer ano) throws SRVException {
		return CalendarioComercialBusiness.getInstance().obtemCalendarioComercial(id, ano);
	}

	public Boolean validaCalendarioComercial(Integer id, Integer ano) throws SRVException {
		Boolean result = true;
		PeriodoCalendarioVO item = CalendarioComercialBusiness.getInstance().obtemCalendarioComercial(id, ano);
		if (item!=null) {
			result = false;
		}
		return result;
	}

}