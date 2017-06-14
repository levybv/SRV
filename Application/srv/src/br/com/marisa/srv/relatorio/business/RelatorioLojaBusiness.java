package br.com.marisa.srv.relatorio.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioLojaDAO;
import br.com.marisa.srv.relatorio.vo.RelatorioLojaVO;

@Deprecated
public class RelatorioLojaBusiness {

	private static final Logger log = Logger.getLogger(RelatorioLojaBusiness.class);

	/**
	 * 
	 */
	private static RelatorioLojaBusiness instance = new RelatorioLojaBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RelatorioLojaBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioAgrupamentoRemuneracaoLojaLider(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioAgrupamentoRemuneracaoLojaLider(relatorioVO);
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível obter relatório Indicador Agrupamento Remuneracao Loja Lider.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioResultadoVendasLoja(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioResultadoVendasLoja(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório resultado vendas (lojas).", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioAnaliticoVendas(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioAnaliticoVendas(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório analítico vendas.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioResultadoPsfLojas(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioResultadoPsfLojas(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório sintético indicadores PSF.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioResultadoPorLojaRegionais(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioResultadoPorLojaRegionais(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório resultado por lojas + regionais.", e);
		} finally {
			dao.closeConnection();
		}
		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioCalculoOperacionalPsf(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioCalculoOperacionalPsf(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório cálculo operacional PSF.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioCalculoLiderancaGerentesChefes(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioCalculoLiderancaGerentesChefes(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório: Calculo Lideranca Gerentes e Chefes.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioPremiacaoColoboradorOperacional(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioPremiacaoColoboradorOperacional(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório premiacao coloborador operacional.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioPremiacaoColoboradorLider(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioPremiacaoColoboradorLider(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório premiacao coloborador líder.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 * @throws ParseException
	 */
	public List<Map<Object,Object>> obtemRelatorioCancelamentos(RelatorioLojaVO relatorioVO) throws SRVException, ParseException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();

		Calendar calendar = Calendar.getInstance();

		String dtIni = "01/" + relatorioVO.getMes() + "/" + relatorioVO.getAno() + " 00:00:00";
		String dtFim = null;

		int dia = 0;
		int mes = Integer.parseInt(relatorioVO.getMes());
		int ano = Integer.parseInt(relatorioVO.getAno());

		calendar.set(ano, mes, dia);

		int lastDay = calendar.getActualMaximum(5);

		dtFim = Integer.toString(lastDay) + "/" + relatorioVO.getMes() + "/" + relatorioVO.getAno() + " 23:59:59";

		relatorioVO.setDataInicio(dtIni);
		relatorioVO.setDataFim(dtFim);
		try {
			listaRelatorio = dao.obtemRelatorioCancelamentos(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório de Cancelamentos.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioOperacionalLoja(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioOperacionalLoja(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Operacional Loja.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioLiderLoja(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioLiderLoja(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Líder Loja.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioResultadoLideranca2Regionais(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioResultadoLideranca2Regionais(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter Relatório Sintético Líder - Resultado Geral.", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws SRVException
	 */
	public List<Map<Object,Object>> obtemRelatorioCalculoLideranca2Regionais(RelatorioLojaVO relatorioVO) throws SRVException {
		List<Map<Object,Object>> listaRelatorio = new ArrayList<Map<Object,Object>>();
		RelatorioLojaDAO dao = new RelatorioLojaDAO();
		try {
			listaRelatorio = dao.obtemRelatorioCalculoLideranca2Regionais(relatorioVO);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatorio: Calculo Lideranca 2 (Regionais).", e);
		} finally {
			dao.closeConnection();
		}

		return listaRelatorio;
	}

}