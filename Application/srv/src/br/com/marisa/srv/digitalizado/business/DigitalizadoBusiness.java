package br.com.marisa.srv.digitalizado.business;

import org.apache.log4j.Logger;

import br.com.marisa.srv.digitalizado.dao.DigitalizadoDAO;
import br.com.marisa.srv.digitalizado.vo.DigitalizadoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

public class DigitalizadoBusiness {

    private static final Logger log = Logger.getLogger(DigitalizadoBusiness.class);    

    private static DigitalizadoBusiness instance = new DigitalizadoBusiness();

    
    private DigitalizadoBusiness() {}

    public static final DigitalizadoBusiness getInstance() {
        return instance;
    }

    public boolean existeTabelaPeriodoSax(String ano, String mes) throws SRVException {
    	DigitalizadoDAO digitalizadoDAO = new DigitalizadoDAO();
        try {
            return digitalizadoDAO.existeTabelaPeriodoSax(ano, mes);
        } finally {
        	digitalizadoDAO.closeConnection();
        }
    }

    public boolean existeDigitalizadoSax(DigitalizadoVO digitalizadoVO) throws SRVException {
    	DigitalizadoDAO digitalizadoDAO = new DigitalizadoDAO();
        try {
            return digitalizadoDAO.existeDigitalizadoSax(digitalizadoVO);
        } finally {
        	digitalizadoDAO.closeConnection();
        }
    }

    public void alteraFlgDocDigitalizadoSAX(DigitalizadoVO digitalizadoVO) throws SRVException {
    	DigitalizadoDAO digitalizadoDAO = new DigitalizadoDAO();

    	try {
        	digitalizadoDAO.beginTrans();
        	digitalizadoDAO.alteraFlgDocDigitalizadoSAX(digitalizadoVO);
        	digitalizadoDAO.commitTrans();
        } catch (Exception e) {
        	digitalizadoDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao da flag digitalizados SAX.", e);
        } finally {
        	digitalizadoDAO.closeConnection();
        }    		
    }

    public boolean existeTabelaPeriodoPL(String ano, String mes) throws SRVException {
    	DigitalizadoDAO digitalizadoDAO = new DigitalizadoDAO();
        try {
            return digitalizadoDAO.existeTabelaPeriodoPL(ano, mes);
        } finally {
        	digitalizadoDAO.closeConnection();
        }
    }

    public boolean existeDigitalizadoPL(DigitalizadoVO digitalizadoVO) throws SRVException {
    	DigitalizadoDAO digitalizadoDAO = new DigitalizadoDAO();
        try {
            return digitalizadoDAO.existeDigitalizadoPL(digitalizadoVO);
        } finally {
        	digitalizadoDAO.closeConnection();
        }
    }

    public void alteraFlgDocDigitalizadoPL(DigitalizadoVO digitalizadoVO) throws SRVException {
    	DigitalizadoDAO digitalizadoDAO = new DigitalizadoDAO();

    	try {
        	digitalizadoDAO.beginTrans();
        	digitalizadoDAO.alteraFlgDocDigitalizadoPL(digitalizadoVO);
        	digitalizadoDAO.commitTrans();
        } catch (Exception e) {
        	digitalizadoDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao da flag digitalizados PL.", e);
        } finally {
        	digitalizadoDAO.closeConnection();
        }    		
    }

}
