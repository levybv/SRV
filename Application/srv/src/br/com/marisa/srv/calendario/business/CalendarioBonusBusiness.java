package br.com.marisa.srv.calendario.business;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.calendario.dao.CalendarioBonusDAO;
import br.com.marisa.srv.calendario.vo.CalendarioBonusVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 */
public class CalendarioBonusBusiness {

    private static final Logger log = Logger.getLogger(CalendarioBonusBusiness.class);    

    private static CalendarioBonusBusiness instance = new CalendarioBonusBusiness();
    
    
    /**
     * 
     * @return
     */
    public static final CalendarioBonusBusiness getInstance() {
        return instance;
    }
    

    /**
     * 
     */
    private CalendarioBonusBusiness() {}

    /**
     * 
     * @param paramVO
     * @return
     * @throws SRVException
     */
    public List<CalendarioBonusVO> obtemCalendarioBonus(CalendarioBonusVO paramVO) throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
            return dao.obtemCalendarioBonus(paramVO);
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @param ano
     * @param mes
     * @return
     * @throws SRVException
     */
    public CalendarioBonusVO obtemCalendarioBonus(Integer ano, Integer mes) throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
            return dao.obtemCalendarioBonus(ano, mes);
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<Integer> obtemAnosCalendarioBonus() throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
            return dao.obtemAnosCalendarioBonus();
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<CalendarioBonusVO> obtemMesesCalendarioBonus() throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
            return dao.obtemMesesCalendarioBonus();
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @param paramDate
     * @param ano
     * @param mes
     * @return
     * @throws SRVException
     */
    public Boolean existeIntervaloCalendarioBonus(Date paramDate, Integer ano, Integer mes) throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
            return dao.existeIntervaloCalendarioBonus(paramDate, ano, mes);
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @param paramVO
     * @throws SRVException
     */
    public void incluiCalendarioBonus(CalendarioBonusVO paramVO) throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
            dao.incluiCalendarioBonus(paramVO);
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @param paramVO
     * @throws SRVException
     */
    public void alteraCalendarioBonus(CalendarioBonusVO paramVO) throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
        	dao.beginTrans();
        	dao.incluiCalendarioBonusHistorico(paramVO.getAno(), paramVO.getMes(), paramVO.getCodUsuario());
            dao.alteraCalendarioBonus(paramVO);
            dao.commitTrans();
        } finally {
        	dao.closeConnection();
        }    	
    }

    /**
     * 
     * @param paramVO
     * @throws SRVException
     */
    public void excluiCalendarioBonus(CalendarioBonusVO paramVO) throws SRVException {
    	CalendarioBonusDAO dao = new CalendarioBonusDAO();
        try {
        	dao.beginTrans();
        	dao.incluiCalendarioBonusHistorico(paramVO.getAno(), paramVO.getMes(), paramVO.getCodUsuario());
            dao.excluiCalendarioBonus(paramVO);
            dao.commitTrans();
        } finally {
        	dao.closeConnection();
        }    	
    }
}