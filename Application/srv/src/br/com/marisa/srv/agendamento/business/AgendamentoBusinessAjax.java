package br.com.marisa.srv.agendamento.business;

import java.util.Iterator;
import java.util.List;

import br.com.marisa.srv.agendamento.vo.AgendamentoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * @author Levy Villar
 */
public class AgendamentoBusinessAjax {
    
    public AgendamentoBusinessAjax(){}

    public AgendamentoVO obtemAgendamento(Integer codigoCarga) throws SRVException {

    	AgendamentoVO returnVO = null;

    	AgendamentoVO paramVO = new AgendamentoVO();
    	paramVO.setCodigoCarga(codigoCarga);

    	List<AgendamentoVO> lista = AgendamentoBusiness.getInstance().obtemAgendamento(paramVO);
    	for (Iterator<AgendamentoVO> it = lista.iterator(); it.hasNext();) {
    		returnVO = it.next();
			
		}
        return returnVO;
    }

}