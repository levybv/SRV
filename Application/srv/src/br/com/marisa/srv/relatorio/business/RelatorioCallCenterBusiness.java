package br.com.marisa.srv.relatorio.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.ApresentacaoException;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioCallCenterDAO;

/**
 * 
 * @author levy.villar
 *
 */
@Deprecated
public class RelatorioCallCenterBusiness {

	private static final Logger log = Logger.getLogger(RelatorioCallCenterBusiness.class);

	/**
	 * 
	 */
	private static RelatorioCallCenterBusiness instance = new RelatorioCallCenterBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RelatorioCallCenterBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioPremiacaoCallCenterLideranca(String ano, String mes) throws SRVException {

		RelatorioCallCenterDAO dao = new RelatorioCallCenterDAO();
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

		try {
			listaRelatorio = dao.obtemRelatorioPremiacaoCallCenterLideranca(ano, mes);
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível obter relatório Premiacao Call Center Lideranca.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioPremiacaoCallCenterOperacional(String ano, String mes) throws SRVException {

		RelatorioCallCenterDAO dao = new RelatorioCallCenterDAO();
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

		try {
			listaRelatorio = dao.obtemRelatorioPremiacaoCallCenterOperacional(ano, mes);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Premiacao Call Center Operacional.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioPremiacaoCallCenterTrimestral(String ano, String mes) throws SRVException {

		RelatorioCallCenterDAO dao = new RelatorioCallCenterDAO();
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

		try {
			listaRelatorio = dao.obtemRelatorioPremiacaoCallCenterTrimestral(ano, mes);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Premiacao Call Center Trimestral.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	public List<Map<Object,Object>> obtemRelatorioCallCenterRealizado(String ano, String mes, Date inicio, Date fim) throws PersistenciaException, ApresentacaoException {

		RelatorioCallCenterDAO dao = new RelatorioCallCenterDAO();
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

		try {
			listaRelatorio = dao.obtemRelatorioCallCenterRealizado(ano, mes, inicio, fim);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

}