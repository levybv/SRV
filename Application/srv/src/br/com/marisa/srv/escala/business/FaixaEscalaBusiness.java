package br.com.marisa.srv.escala.business;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.escala.dao.EscalaDAO;
import br.com.marisa.srv.escala.dao.FaixaEscalaDAO;
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.escala.vo.FaixaEscalaVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Faixa de Escala
 * 
 * @author Walter Fontes
 */
public class FaixaEscalaBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(FaixaEscalaBusiness.class);    

    
    //Instancia do Singleton
    private static FaixaEscalaBusiness instance = new FaixaEscalaBusiness();
    
    
    /**
     * Obtem uma instancia do objeto FaixaEscalaBusiness
     * @return O objeto FaixaEscalaBusiness
     */
    public static final FaixaEscalaBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private FaixaEscalaBusiness() {
        // vazio
    }
    
    /**
     * Pesquisa Faixas de Escalas
     * 
     * @param idEscala
     * @return
     */
    public List<FaixaEscalaVO> obtemFaixasEscala(Integer idEscala) throws SRVException {
    	FaixaEscalaDAO faixaEscalaDAO = new FaixaEscalaDAO();
        try {
            return faixaEscalaDAO.obtemFaixasEscalas(idEscala);
        } finally {
        	faixaEscalaDAO.closeConnection();
        }
    }
    
    
    /**
     * Pesquisa Faixa de Escala
     * 
     * @param idEscala
     * @param sequencial
     * @return
     */
    public FaixaEscalaVO obtemFaixaEscala(Integer idEscala, Integer sequencial) throws SRVException {
    	FaixaEscalaDAO faixaEscalaDAO = new FaixaEscalaDAO();
        try {
            return faixaEscalaDAO.obtemEscala(idEscala, sequencial);
        } finally {
        	faixaEscalaDAO.closeConnection();
        }
    }
    
    /**
     * 
     * @param idEscala
     * @param realizado
     * @return
     * @throws SRVException
     */
    public FaixaEscalaVO obtemFaixaEscala(Integer idEscala, Double realizado) throws SRVException {
    	FaixaEscalaDAO faixaEscalaDAO = new FaixaEscalaDAO();
        try {
            return faixaEscalaDAO.obtemFaixaEscala(idEscala, realizado);
        } finally {
        	faixaEscalaDAO.closeConnection();
        }
    }

    /**
     * Realiza alteração das faixas de escala
     * 
     * @param idEscala
     * @param faixasEscala
     * @return
     */
    public void alteraFaixasEscala(Integer idEscala, List<FaixaEscalaVO> faixasEscala, Connection conn) throws SRVException {
    	FaixaEscalaDAO faixaEscalaDAO = new FaixaEscalaDAO();
    	EscalaDAO escalaDAO = new EscalaDAO();
        try {
        	
        	if (conn != null) {
        		faixaEscalaDAO.setConn(conn);
	        	escalaDAO.setConn(conn);
        	} else {
	        	faixaEscalaDAO.beginTrans();
	        	escalaDAO.setConn(faixaEscalaDAO.getConn());
        	}
        	
        	//Grava as faixas antigas no histórico
        	List<FaixaEscalaVO> faixasAntigas = faixaEscalaDAO.obtemFaixasEscalas(idEscala);
        	if (faixasAntigas != null && faixasAntigas.size() > 0) {
        		Iterator<FaixaEscalaVO> itFaixaAntigasEscala = faixasAntigas.iterator();
        		while (itFaixaAntigasEscala.hasNext()) {
        			FaixaEscalaVO faixaEscalaVO = itFaixaAntigasEscala.next();
        			faixaEscalaDAO.incluiFaixaEscalaHistorico(faixaEscalaVO);
        		}
        	}
        	
        	//Exclui as faixas antigas
        	faixaEscalaDAO.excluiFaixasEscala(idEscala);
        	
        	//Obtém a escala
        	EscalaVO escalaVO = escalaDAO.obtemEscala(idEscala);
        	if (escalaVO == null) {
        		throw new Exception("Escala " + idEscala + " não encontrada.");
        	}
        	
        	//Grava as novas
        	if (faixasEscala != null && faixasEscala.size() > 0) {
            	int ultimaSequencia = faixasEscala.size();
        		Iterator<FaixaEscalaVO> itFaixaEscala = faixasEscala.iterator();
        		while (itFaixaEscala.hasNext()) {
        			FaixaEscalaVO faixaEscalaVO = itFaixaEscala.next();
        			
        			if (faixaEscalaVO.getSequencial().intValue() < ultimaSequencia) {
        				faixaEscalaVO.setLimite(null);
        				faixaEscalaVO.setPercentual(Boolean.FALSE);
        			} else {
        				faixaEscalaVO.setLimite(escalaVO.getLimite());
        				faixaEscalaVO.setPercentual(escalaVO.getPercentual());
        			}
        			
        			faixaEscalaDAO.incluiFaixaEscala(faixaEscalaVO);
        		}
        	}
        	
        	if (conn == null) {
        		faixaEscalaDAO.commitTrans();
        	}
        	
        } catch (Exception e) {
        	if (conn == null) {
        		faixaEscalaDAO.rollbackTrans();
        	}
			throw new SRVException(log, "Ocorreu erro na alteracao da escala", e);
        } finally {
        	if (conn == null) {
        		faixaEscalaDAO.closeConnection();
        	}
        }    		
    }
}
