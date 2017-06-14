package br.com.marisa.srv.relatorio.business;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.relatorio.dao.RelatorioDAO;
import br.com.marisa.srv.relatorio.dao.RelatorioTipoDAO;
import br.com.marisa.srv.relatorio.vo.RelatorioVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioBusiness {

	private static final Logger log = Logger.getLogger(RelatorioBusiness.class);

	/**
	 * 
	 */
	private static RelatorioBusiness instance = new RelatorioBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RelatorioBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws SRVException
	 */
	public List<RelatorioVO> obtemRelatorio(RelatorioVO pesquisaVO) throws SRVException {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		RelatorioTipoDAO relatorioTipoDAO = new RelatorioTipoDAO();
		List<RelatorioVO> listaRelatorioVO = null;

		try {
			relatorioTipoDAO.setConn(relatorioDAO.getConn());
			listaRelatorioVO = relatorioDAO.obtemRelatorio(pesquisaVO);
			for (Iterator<RelatorioVO> itRelatorioVO = listaRelatorioVO.iterator(); itRelatorioVO.hasNext();) {
				RelatorioVO relatorioVO = itRelatorioVO.next();
				if (ObjectHelper.isNotNull(relatorioVO.getRelatorioTipoVO()) && ObjectHelper.isNotEmpty(relatorioVO.getRelatorioTipoVO().getCodigo())) {
					relatorioVO.setRelatorioTipoVO(relatorioTipoDAO.obtemRelatorioTipoPorCodigo(relatorioVO.getRelatorioTipoVO().getCodigo()));
				}
			}
		} catch (SRVException ex) {
			throw new SRVException(log, "Nao foi possivel obter relatorio: ", ex);
		} finally {
			relatorioDAO.closeConnection();
		}
		return listaRelatorioVO;
	}

	/**
	 * 
	 * @param codigo
	 * @param ano
	 * @param mes
	 * @param qtdRepParam
	 * @return
	 * @throws SRVException
	 */
	public RelatorioVO obtemRelatorio(Integer codigo, Integer ano, Integer mes) throws SRVException {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		RelatorioTipoDAO relatorioTipoDAO = new RelatorioTipoDAO();
		RelatorioVO relatorioVO = null;
		try {
			relatorioTipoDAO.setConn(relatorioDAO.getConn());

			relatorioVO = relatorioDAO.obtemRelatorioPorCodigo(codigo);
			if (relatorioVO != null) {
				if (ObjectHelper.isNotEmpty(relatorioVO.getNomeTabela())) {
					relatorioVO.setLinhas(relatorioDAO.montaRelatorioPorTabela(relatorioVO.getNomeTabela(), ano, mes));
				} else if (ObjectHelper.isNotEmpty(relatorioVO.getComandoSQL())) {
					relatorioVO.setLinhas(relatorioDAO.montaRelatorioPorComando(relatorioVO.getComandoSQL(), ano, mes, relatorioVO.getQtdRepetirParametro()));
				}
			}
			
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter relatorio pelo codigo: " + codigo, e);
		} finally {
			relatorioDAO.closeConnection();
		}
		return relatorioVO;
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws SRVException
	 */
	public void incluiRelatorio(RelatorioVO incluiVO) throws SRVException {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		try {
			relatorioDAO.incluiRelatorio(incluiVO);
		} finally {
			relatorioDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param alteraVO
	 * @throws SRVException
	 */
	public void alteraRelatorio(RelatorioVO alteraVO) throws SRVException {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		try {
			relatorioDAO.alteraRelatorio(alteraVO);
		} finally {
			relatorioDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param excluiVO
	 * @throws SRVException
	 */
	public void excluiRelatorio(RelatorioVO excluiVO) throws SRVException {
		RelatorioDAO relatorioDAO = new RelatorioDAO();
		try {
			relatorioDAO.excluiRelatorio(excluiVO);
		} finally {
			relatorioDAO.closeConnection();
		}
	}

}