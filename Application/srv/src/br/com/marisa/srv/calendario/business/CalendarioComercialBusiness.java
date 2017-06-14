package br.com.marisa.srv.calendario.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.calendario.dao.CalendarioComercialDAO;
import br.com.marisa.srv.calendario.vo.PeriodoCalendarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.indicador.vo.IndicadorPeriodoVO;

/**
 * Classe para conter os métodos de negócio do módulo de Cargos 
 * 
 * @author Walter Fontes
 */
public class CalendarioComercialBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(CalendarioComercialBusiness.class);    

    private static CalendarioComercialBusiness instance = new CalendarioComercialBusiness();
    
    
    /**
     * Obtem uma instancia do objeto CalendarioBusiness
     * @return O objeto CalendarioBusiness
     */
    public static final CalendarioComercialBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private CalendarioComercialBusiness() {
        // vazio
    }

    /**
     * Obtém periodos de um calendário de um ano específico
     * 
     * @param ano
     * @return
     */
    public List<PeriodoCalendarioVO> obtemPeriodosCalendario(int ano) throws SRVException {
    	CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
        try {
            return calendarioDAO.obtemPeriodosCalendario(ano);
        } finally {
        	calendarioDAO.closeConnection();
        }    	
    }


	public List<Integer> obtemListaAnos() throws PersistenciaException {
    	CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
        try {
            return calendarioDAO.obtemAnosCalendario();
        } finally {
        	calendarioDAO.closeConnection();
        }    	
	}
	

	public List<IndicadorPeriodoVO> obtemListaPeriodos() throws SRVException {
		CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
		try {
			List<IndicadorPeriodoVO> lista =  calendarioDAO.obtemListaPeriodos();
			formataPeriodo(lista);
			return lista;
		}finally {
			calendarioDAO.closeConnection();
		}
	}


	public List<PeriodoCalendarioVO> obtemListaPeriodoMesAno() throws SRVException {
		List<PeriodoCalendarioVO> lista = new ArrayList<PeriodoCalendarioVO>();
		CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
		try {
			lista = calendarioDAO.obtemListaPeriodoMesAno();
		} finally {
			calendarioDAO.closeConnection();
		}
		return lista;
	}


    private void formataPeriodo(List<IndicadorPeriodoVO> lista) {
		if(lista != null) {
			for(int i=0;i<lista.size();i++) {
				IndicadorPeriodoVO indicadorPeriodoVO = lista.get(i);
				indicadorPeriodoVO.setDataFormatada(DataHelper.obtemMesExtenso(indicadorPeriodoVO.getMes().intValue(),Constantes.CAPTALIZADO_CASE)+" / "+indicadorPeriodoVO.getAno().intValue());
				indicadorPeriodoVO.setMesFormatado(DataHelper.obtemMesExtenso(indicadorPeriodoVO.getMes().intValue(),Constantes.CAPTALIZADO_CASE));
			}
		}
	}

    public List<IndicadorPeriodoVO> obtemCalendarioComercialListaAno(boolean isOrderByDesc) throws PersistenciaException {
		List<IndicadorPeriodoVO> lista = new ArrayList<IndicadorPeriodoVO>();
		CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
		try {
			lista = calendarioDAO.obtemCalendarioComercialListaAno(isOrderByDesc);
		} finally {
			calendarioDAO.closeConnection();
		}
		return lista;
    }

    public List<IndicadorPeriodoVO> obtemCalendarioComercialListaMes(boolean isOrderByDesc) throws PersistenciaException {
		List<IndicadorPeriodoVO> lista = new ArrayList<IndicadorPeriodoVO>();
		CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
		try {
			lista = calendarioDAO.obtemCalendarioComercialListaMes(isOrderByDesc);
		} finally {
			calendarioDAO.closeConnection();
		}
		return lista;
    }


    /**
     */
    public PeriodoCalendarioVO obtemCalendarioComercial(Integer codPeriodo, Integer ano) throws SRVException {
    	CalendarioComercialDAO calendarioDAO = new CalendarioComercialDAO();
        try {
            return calendarioDAO.obtemCalendarioComercial(codPeriodo, ano);
        } finally {
        	calendarioDAO.closeConnection();
        }    	
    }

    /**
     */
    public void alteraCalendarioComercial(PeriodoCalendarioVO paramVO) throws SRVException {
    	CalendarioComercialDAO dao = new CalendarioComercialDAO();
        try {
        	dao.beginTrans();
        	dao.incluiCalendarioComercialHistorico(paramVO.getAno(), paramVO.getPeriodo(), paramVO.getCodUsuario());;
        	dao.alteraCalendarioComercial(paramVO);
            dao.commitTrans();
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na alteracao do calendário comercial.", e);
        } finally {
        	dao.closeConnection();
        }    		
    }

    /**
     */
    public void incluiCalendarioComercial(PeriodoCalendarioVO paramVO) throws SRVException {
    	CalendarioComercialDAO dao = new CalendarioComercialDAO();
        try {
        	dao.incluiCalendarioComercial(paramVO);
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na inclusão do calendário comercial.", e);
        } finally {
        	dao.closeConnection();
        }    		
    }

    /**
     */
    public void excluiCalendarioComercial(PeriodoCalendarioVO paramVO) throws SRVException {
    	CalendarioComercialDAO dao = new CalendarioComercialDAO();
        try {
        	dao.beginTrans();
        	dao.incluiCalendarioComercialHistorico(paramVO.getAno(), paramVO.getPeriodo(), paramVO.getCodUsuario());;
        	dao.excluiCalendarioComercial(paramVO);
            dao.commitTrans();
        } catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro na exclusão do calendário comercial.", e);
        } finally {
        	dao.closeConnection();
        }    		
    }

}