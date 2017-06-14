package br.com.marisa.srv.relatorio.business;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioTipoDAO;
import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioTipoBusiness {

	private static final Logger log = Logger.getLogger(RelatorioTipoBusiness.class);

	/**
	 * 
	 */
	private static RelatorioTipoBusiness instance = new RelatorioTipoBusiness();

	/**
	 * 
	 * @return
	 */
	public static final RelatorioTipoBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws SRVException
	 */
	public RelatorioTipoVO obtemRelatorioTipoPorCodigo(Integer codigo) throws SRVException {
		RelatorioTipoDAO dao = new RelatorioTipoDAO();
		try {
			return dao.obtemRelatorioTipoPorCodigo(codigo);
		} catch (PersistenciaException ex) {
			throw new SRVException(log, "Nao foi possivel obter tipo relatorio pelo codigo: " + codigo, ex);
		} finally {
			dao.closeConnection();
		}
	}

	public List<RelatorioTipoVO> obtemRelatorioTipo(RelatorioTipoVO pesquisaVO) throws SRVException {
		RelatorioTipoDAO dao = new RelatorioTipoDAO();
		try {
			return dao.obtemRelatorioTipo(pesquisaVO);
		} catch (PersistenciaException ex) {
			throw new SRVException(log, "Nao foi possivel obter tipo relatorio: ", ex);
		} finally {
			dao.closeConnection();
		}
	}

}