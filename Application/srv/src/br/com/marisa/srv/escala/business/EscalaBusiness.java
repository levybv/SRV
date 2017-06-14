package br.com.marisa.srv.escala.business;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.escala.dao.EscalaDAO;
import br.com.marisa.srv.escala.dao.FaixaEscalaDAO;
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.escala.vo.FaixaEscalaVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Escala
 * 
 * @author Walter Fontes
 */
public class EscalaBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(EscalaBusiness.class);    

    
    //Instancia do Singleton
    private static EscalaBusiness instance = new EscalaBusiness();
    
    
    /**
     * Obtem uma instancia do objeto EscalaBusiness
     * @return O objeto EscalaBusiness
     */
    public static final EscalaBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private EscalaBusiness() {
        // vazio
    }
    
    /**
     * Pesquisa Escalas
     * 
     * @param idEscala
     * @param descricao
     * @return
     */
    public List<EscalaVO> obtemEscalas(Integer idEscala, String descricao, Integer numEscala) throws SRVException {
    	EscalaDAO escalaDAO = new EscalaDAO();
        try {
            return escalaDAO.obtemEscalas(idEscala, descricao, numEscala);
        } finally {
        	escalaDAO.closeConnection();
        }
    }
    
    
    /**
     * Pesquisa Escala
     * 
     * @param idEscala
     * @return
     */
    public EscalaVO obtemEscala(Integer idEscala) throws SRVException {
    	EscalaDAO escalaDAO = new EscalaDAO();
        try {
            return escalaDAO.obtemEscala(idEscala);
        } finally {
        	escalaDAO.closeConnection();
        }
    }    
    
    
    /**
     * Realiza alteração da escala
     * 
     * @param escalaVO
     * @return
     */
    public void alteraEscala(EscalaVO escalaVO) throws SRVException {
    	EscalaDAO escalaDAO = new EscalaDAO();
    	FaixaEscalaDAO faixaEscalaDAO = new FaixaEscalaDAO();
        try {
        	escalaDAO.beginTrans();
        	faixaEscalaDAO.setConn(escalaDAO.getConn());
        	
        	//Grava historico da situacao anterior
        	EscalaVO escalaAnteriorVO = escalaDAO.obtemEscala(escalaVO.getIdEscala());
        	if (escalaAnteriorVO != null) {
            	escalaDAO.incluiEscalaHistorico(escalaAnteriorVO);
        	}
        	
        	//Efetiva a alteração
        	escalaDAO.alteraEscala(escalaVO);
        	
        	//Obtém faixas da escala
        	List<FaixaEscalaVO> faixasEscala = faixaEscalaDAO.obtemFaixasEscalas(escalaVO.getIdEscala());
        	FaixaEscalaBusiness.getInstance().alteraFaixasEscala(escalaVO.getIdEscala(), faixasEscala, escalaDAO.getConn());
        	
        	escalaDAO.commitTrans();
        	
        } catch (Exception e) {
        	escalaDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao da escala", e);
        } finally {
        	escalaDAO.closeConnection();
        }    		
    }
    
    
    /**
     * Realiza inclusao de Escala
     * 
     * @param escalaVO
     * @return
     */
    public void incluiEscala(EscalaVO escalaVO) throws SRVException {
    	EscalaDAO escalaDAO = new EscalaDAO();
        try {
        	escalaDAO.incluiEscala(escalaVO);
        } finally {
        	escalaDAO.closeConnection();
        }    		
    }     
    
    
    /**
     * Realiza exclusao de Escala
     * 
     * @param idEscala
     * @return
     */
    public String excluiEscala(Integer idEscala) throws SRVException {
    	EscalaDAO escalaDAO = new EscalaDAO();
    	FaixaEscalaDAO faixaEscalaDAO = new FaixaEscalaDAO();
        try {
        	escalaDAO.beginTrans();
        	faixaEscalaDAO.setConn(escalaDAO.getConn());
        	
        	faixaEscalaDAO.excluiFaixasEscala(idEscala);
        	escalaDAO.excluiEscala(idEscala);
        	
        	escalaDAO.commitTrans();
        	
        } catch (Exception e) {
			log.error("Ocorreu erro ao excluir a escala " + idEscala, e);
        	escalaDAO.rollbackTrans();
			return "Escala já está sendo utilizada, não pode mais ser excluída.";
        } finally {
        	escalaDAO.closeConnection();
        }    		
        return "";
    }       

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<EscalaVO> obtemListaEscalaBonus() throws SRVException {
    	EscalaDAO escalaDAO = new EscalaDAO();
        try {
            return escalaDAO.obtemListaEscalaBonus();
        } finally {
        	escalaDAO.closeConnection();
        }
    }

}
