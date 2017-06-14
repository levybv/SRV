package br.com.marisa.srv.ponderacao.business;

import java.util.ArrayList;
import java.util.List;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.ponderacao.dao.PonderacaoDAO;
import br.com.marisa.srv.ponderacao.vo.PonderacaoVO;

/**
 * Classe para conter os m�todos de neg�cio do m�dulo de Pondera��o
 * 
 * @author Walter Fontes
 */
public class PonderacaoBusiness {

	// Log4J
	// private static final Logger log =
	// Logger.getLogger(PonderacaoBusiness.class);

	// Instancia do Singleton
	private static PonderacaoBusiness instance = new PonderacaoBusiness();

	/**
	 * Obtem uma instancia do objeto PonderacaoBusiness
	 * 
	 * @return O objeto PonderacaoBusiness
	 */
	public static final PonderacaoBusiness getInstance() {

		return instance;
	}

	/**
	 * Construtor default. Foi escondido como private por cargo do singleton.
	 * Utilizar o m�todo getInstance();
	 */
	private PonderacaoBusiness() {

	}

	/**
	 * Obt�m pondera��es
	 * 
	 * @param idIndicador
	 * @param idGrupoIndicador
	 * @param idGrupoRemVar
	 * @param idCargo
	 * @return
	 * @throws SRVException
	 */
	public List obtemPonderacoes(Integer idIndicador, Integer idGrupoIndicador, Integer idGrupoRemVar, Integer idCargo) throws SRVException {

		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			return ponderacaoDAO.obtemPonderacoes(idIndicador, idGrupoIndicador, idGrupoRemVar, idCargo);
		} finally {
			ponderacaoDAO.closeConnection();
		}
	}

	/**
	 * Obt�m pondera��o
	 * 
	 * @param idPonderacao
	 * @return
	 * @throws SRVException
	 */
	public PonderacaoVO obtemPonderacao(Integer idPonderacao) throws SRVException {

		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			return ponderacaoDAO.obtemPonderacao(idPonderacao);
		} finally {
			ponderacaoDAO.closeConnection();
		}
	}

	/**
	 * Inclui pondera��o
	 * 
	 * @param ponderacaoVO
	 * @return
	 * @throws SRVException
	 */
	public void incluiPonderacao(PonderacaoVO ponderacaoVO) throws SRVException {

		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			ponderacaoDAO.incluiPonderacao(ponderacaoVO);
		} finally {
			ponderacaoDAO.closeConnection();
		}
	}

	/**
	 * Altera pondera��o
	 * 
	 * @param ponderacaoVO
	 * @return
	 * @throws SRVException
	 */
	public void alteraPonderacao(PonderacaoVO ponderacaoVO) throws SRVException {

		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			ponderacaoDAO.beginTrans();

			PonderacaoVO ponderacaoAntigaVO = ponderacaoDAO.obtemPonderacao(ponderacaoVO.getIdPonderacao());
			if (ponderacaoAntigaVO != null) {
				ponderacaoDAO.incluiPonderacaoHistorico(ponderacaoAntigaVO);
			}
			ponderacaoDAO.alteraPonderacao(ponderacaoVO);
			ponderacaoDAO.commitTrans();

		} catch (SRVException e) {
			ponderacaoDAO.rollbackTrans();
			throw e;

		} finally {
			ponderacaoDAO.closeConnection();
		}
	}

	/**
	 * Exclui pondera��o
	 * 
	 * @param idPonderacao
	 * @return
	 * @throws SRVException
	 */
	public void excluiPonderacao(Integer idPonderacao) throws SRVException {

		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			ponderacaoDAO.beginTrans();

			PonderacaoVO ponderacaoAntigaVO = ponderacaoDAO.obtemPonderacao(idPonderacao);
			if (ponderacaoAntigaVO != null) {
				ponderacaoDAO.incluiPonderacaoHistorico(ponderacaoAntigaVO);
				ponderacaoDAO.excluiPonderacao(idPonderacao);
			}
			ponderacaoDAO.commitTrans();

		} catch (SRVException e) {
			ponderacaoDAO.rollbackTrans();
			throw e;

		} finally {
			ponderacaoDAO.closeConnection();
		}
	}

	public List obtemPonderacoes(Integer idIndicador, Integer idGrupoIndicador, Integer idGrupoRemVar, Integer idCargo, Integer idTipoFilial, Integer idFilial) throws SRVException {

		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			return ponderacaoDAO.obtemPonderacoes(idIndicador, idGrupoIndicador, idGrupoRemVar, idCargo, idTipoFilial, idFilial);
		} finally {
			ponderacaoDAO.closeConnection();
		}
	}

	public List obtemListaFiliais() throws PersistenciaException {

		List filiais = new ArrayList();
		PonderacaoDAO ponderacaoDAO = new PonderacaoDAO();
		try {
			filiais = ponderacaoDAO.obtemListaFiliais();
		} catch (Exception e) {
			throw new PersistenciaException("Nao foi poss�vel obter a lista de filiais.", e);
		} finally {
			ponderacaoDAO.closeConnection();
		}
		return filiais;
	}

}