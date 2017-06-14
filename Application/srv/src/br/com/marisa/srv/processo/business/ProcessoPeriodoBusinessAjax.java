package br.com.marisa.srv.processo.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Classe para conter os m�todos de neg�cio do m�dulo de Processos por Periodo para acesso Ajax
 * 
 * @author Walter Fontes
 */
public class ProcessoPeriodoBusinessAjax {
	
    //Log4J
    //private static final Logger log = Logger.getLogger(ProcessoPeriodoBusinessAjax.class);    
    
	/**
	 * Verifica se processo por per�odo existe
	 * 
	 * @param ano
	 * @param mes
	 * @param idProcesso
	 * @return
	 * @throws SRVException
	 */
	public boolean processoPorPeriodoExiste(Integer ano, Integer mes, Integer idProcesso) throws SRVException {

		if (idProcesso != null && idProcesso.intValue() == -1) {
			idProcesso = null;
		}

		return ObjectHelper.isNotEmpty(ProcessoPeriodoBusiness.getInstance().obtemProcessosPeriodo(ano, mes, idProcesso));
	}
}