package br.com.marisa.srv.agendamento.business;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.agendamento.dao.AgendamentoDAO;
import br.com.marisa.srv.agendamento.vo.AgendamentoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * @author Levy Villar
 */
public class AgendamentoBusiness {

    private static final Logger log = Logger.getLogger(AgendamentoBusiness.class);

    private static AgendamentoBusiness instance = new AgendamentoBusiness();

    /**
     */
    public static final AgendamentoBusiness getInstance() {
        return instance;
    }

    /**
     */
    private AgendamentoBusiness() {}

    /**
     * 
     * @param paramVO
     * @return
     * @throws SRVException
     */
    public List<AgendamentoVO> obtemAgendamento(AgendamentoVO paramVO) throws SRVException {
    	AgendamentoDAO dao = new AgendamentoDAO();
        try {
            return dao.obtemAgendamento(paramVO);
        } finally {
        	dao.closeConnection();
        }
    }

    /**
     * 
     * @param paramVO
     * @throws SRVException
     */
    public void alteraAgendamento(AgendamentoVO paramVO) throws SRVException {
    	AgendamentoDAO dao = new AgendamentoDAO();
        try {
        	dao.alteraAgendamento(paramVO);
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na alteracao do agendamento de processo.", e);
        } finally {
        	dao.closeConnection();
        }    		
    }

    /**
     * 
     * @throws SRVException
     */
    public void reprocessaAgendamento() throws SRVException {
    	AgendamentoDAO dao = new AgendamentoDAO();
        try {
        	dao.reprocessaAgendamento();
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro no reprocessamento do agendamento.", e);
        } finally {
        	dao.closeConnection();
        }    		
    }

    /**
     * 
     * @throws SRVException
     */
    public void reprocessaCargaArquivos() throws SRVException {
    	AgendamentoDAO dao = new AgendamentoDAO();
        try {
        	dao.reprocessaCargaArquivos();
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na carga de arquivos.", e);
        } finally {
        	dao.closeConnection();
        }    		
    }

}