package br.com.marisa.srv.processo.business;

import java.util.Iterator;
import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.processo.dao.ProcessoDAO;
import br.com.marisa.srv.processo.dao.ProcessoPeriodoDAO;
import br.com.marisa.srv.processo.vo.ProcessoPeriodoVO;
import br.com.marisa.srv.processo.vo.ProcessoVO;

/**
 * Classe para conter os métodos de negócio do módulo de Processos por Períodos 
 * 
 * @author Walter Fontes
 */
public class ProcessoPeriodoBusiness {
    
	
    //Log4J
    //private static final Logger log = Logger.getLogger(ProcessoPeriodoBusiness.class);    

    
    //Instancia do Singleton
    private static ProcessoPeriodoBusiness instance = new ProcessoPeriodoBusiness();
    
    
    /**
     * Obtem uma instancia do objeto ProcessoPeriodoBusiness
     * @return O objeto ProcessoPeriodoBusiness
     */
    public static final ProcessoPeriodoBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private ProcessoPeriodoBusiness() {
    }

    
	/**
	 * Obtém processos por períodos
	 * 
	 * @param ano
	 * @return
	 * @throws SRVException
	 */
	public List obtemProcessosPeriodo(Integer ano) throws SRVException {
		return obtemProcessosPeriodo(ano, null, null);
	}
    
    
	/**
	 * Obtém processos por períodos
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public List obtemProcessosPeriodo(Integer ano, Integer mes, Integer idProcesso) throws SRVException {
		ProcessoPeriodoDAO processoPeriodoDAO = new ProcessoPeriodoDAO();
		try {
			return processoPeriodoDAO.obtemProcessosPeriodo(ano, mes, idProcesso);
			
		} finally {
			processoPeriodoDAO.closeConnection();
		}
	}
	
	
	
	/**
	 * Inclui processos por período
	 * 
	 * @param processoPeriodoVO
	 * @return
	 * @throws SRVException
	 */
	public void incluiProcessoPeriodo(ProcessoPeriodoVO processoPeriodoVO) throws SRVException {
		
		ProcessoPeriodoDAO processoPeriodoDAO = new ProcessoPeriodoDAO();
		ProcessoDAO processoDAO = new ProcessoDAO();
		try {
			processoPeriodoDAO.beginTrans();
			processoDAO.setConn(processoPeriodoDAO.getConn());
			
			//Inclui periodo incluído para todos os processos ativos ou para o processo selecionado (se houver)
			if (processoPeriodoVO.getIdProcesso() == null || processoPeriodoVO.getIdProcesso().intValue() == -1) {
				List processos = processoDAO.obtemProcessosAtivos();
				if (processos != null) {
					Iterator itProcessos = processos.iterator();
					while (itProcessos.hasNext()) {
						ProcessoVO processoVO = (ProcessoVO)itProcessos.next();
						if (processoVO.getAtivo().booleanValue()) {
							processoPeriodoVO.setIdProcesso(processoVO.getIdProcesso());
							processoPeriodoDAO.incluiProcessoPeriodo(processoPeriodoVO);
						}
					}
				}
			} else {
				processoPeriodoDAO.incluiProcessoPeriodo(processoPeriodoVO);
			}
			
			processoPeriodoDAO.commitTrans();
			
		} catch (Exception e) {
			processoPeriodoDAO.rollbackTrans();
			throw new SRVException("Ocorreu erro ao incluir processor por periodo.", e);
		
		} finally {
			processoPeriodoDAO.closeConnection();
		}
	}
	
	
	
	/**
	 * Altera processo por período
	 * 
	 * @param processoPeriodoVO
	 * @return
	 * @throws SRVException
	 */
	public void alteraProcessoPeriodo(ProcessoPeriodoVO processoPeriodoVO) throws SRVException {
		
		ProcessoPeriodoDAO processoPeriodoDAO = new ProcessoPeriodoDAO();
		ProcessoDAO processoDAO = new ProcessoDAO();
		try {
			processoPeriodoDAO.beginTrans();
			processoDAO.setConn(processoPeriodoDAO.getConn());
			
			//Obtém os processos já existentes, se não forem processos inativos, grava histório da alteração
			List processosPeriodo = processoPeriodoDAO.obtemProcessosPeriodo(processoPeriodoVO.getAno(), processoPeriodoVO.getMes(), processoPeriodoVO.getIdProcesso());
			if (processosPeriodo != null) {
				Iterator itProcessos = processosPeriodo.iterator();
				while (itProcessos.hasNext()) {
					ProcessoPeriodoVO processoPeriodoAnteriorVO = (ProcessoPeriodoVO)itProcessos.next();
					
					ProcessoVO processoVO = processoDAO.obtemProcesso(processoPeriodoAnteriorVO.getIdProcesso());
					if (processoVO.getAtivo().booleanValue()) {
						processoPeriodoDAO.incluiProcessoPeriodoHistorico(processoPeriodoAnteriorVO);
					}
				}
			}
			
			//Altera os processos do periodo
			processoPeriodoDAO.alteraProcessoPeriodo(processoPeriodoVO);
			processoPeriodoDAO.commitTrans();
			
		} catch (SRVException e) {
			processoPeriodoDAO.rollbackTrans();
			throw e;
			
		} finally {
			processoPeriodoDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public List obtemProcessosPeriodo(Integer ano, Integer mes) throws SRVException {
		return obtemProcessosPeriodo(ano, mes, null);
	}

	/**
	 * 
	 * @param processoPeriodoVO
	 * @param param
	 * @throws SRVException
	 */
	public void geraProcesso(ProcessoPeriodoVO processoPeriodoVO, String param) throws SRVException {
		
		ProcessoPeriodoDAO processoPeriodoDAO = new ProcessoPeriodoDAO();
		try {
			processoPeriodoDAO.beginTrans();
			processoPeriodoDAO.geraProcesso(processoPeriodoVO, param);
			processoPeriodoDAO.commitTrans();
			
		} catch (Exception e) {
			processoPeriodoDAO.rollbackTrans();
			throw new SRVException("Ocorreu erro ao incluir processor por periodo.", e);
		
		} finally {
			processoPeriodoDAO.closeConnection();
		}
	}

}