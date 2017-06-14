package br.com.marisa.srv.relatorio.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.campanha.vo.CampanhaVO;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioCampanhaDAO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioCampanhaBusiness {

	private static final Logger log = Logger.getLogger(RelatorioCampanhaBusiness.class);

	/**
	 * 
	 */
	private static RelatorioCampanhaBusiness instance = new RelatorioCampanhaBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RelatorioCampanhaBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param dataInicio
	 * @param dataFim
	 * @param tabelaFuncionario
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioCampanha(String ano, String mes, Date dataInicio, Date dataFim, String tabelaFuncionario) throws SRVException {

		RelatorioCampanhaDAO dao = new RelatorioCampanhaDAO();
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();

		try {
			listaRelatorio = dao.obtemRelatorioCampanha(ano, mes, dataInicio, dataFim, tabelaFuncionario);
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível obter o relatório de Campanha.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	public boolean isTabelaExist(String ano, String mes, String tabelaFuncionario) throws SRVException {
		RelatorioCampanhaDAO dao = new RelatorioCampanhaDAO();
		try {
			return dao.isTabelaExist(ano, mes, tabelaFuncionario);
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível validar o período de Campanha.", e);
		} finally {
			dao.closeConnection();
		}
	}

	/**
	 * 
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public List<CampanhaVO> obtemListaCampanha(Integer idIndicador) throws SRVException {

		RelatorioCampanhaDAO dao = new RelatorioCampanhaDAO();

		try {
			return dao.obtemListaCampanha(idIndicador);
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível a lista de campanhas.", e);
		} finally {
			dao.closeConnection();
		}

	}

}