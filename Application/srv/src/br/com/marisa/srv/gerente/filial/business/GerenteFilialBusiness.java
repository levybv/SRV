package br.com.marisa.srv.gerente.filial.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.gerente.filial.dao.GerenteFilialDAO;
import br.com.marisa.srv.gerente.filial.vo.GerenteVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class GerenteFilialBusiness {

	//private static final Logger log = Logger.getLogger(GerenteFilialBusiness.class);
	
	private static GerenteFilialBusiness instance = new GerenteFilialBusiness();

	/**
	 * 
	 * @return
	 */
	public static final GerenteFilialBusiness getInstance() {
		return instance;
	}

	/**
     * 
     */
	private GerenteFilialBusiness() {
	}

	/**
	 * 
	 * @param codFuncionario
	 * @param codFilial
	 * @param codUsuario
	 * @throws PersistenciaException
	 */
	public void excluiGerenteFilial(Long codFuncionario, Integer codFilial, Integer codUsuario) throws PersistenciaException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			gerenteFilialDAO.beginTrans();
			gerenteFilialDAO.excluiGerenteFilial(codFuncionario, codFilial, codUsuario);
			gerenteFilialDAO.commitTrans();
		} catch (Exception e) {
			gerenteFilialDAO.rollbackTrans();
			e.printStackTrace();
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param vo
	 * @throws PersistenciaException
	 */
	public void incluiGerenteFilial(GerenteVO vo) throws PersistenciaException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			gerenteFilialDAO.incluiGerenteFilial(vo);
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param vo
	 * @throws PersistenciaException
	 */
	public void alteraGerenteFilial(Long codFuncionario, Integer codFilial, Integer codUsuario, GerenteVO vo) throws PersistenciaException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			gerenteFilialDAO.beginTrans();
			gerenteFilialDAO.excluiGerenteFilial(codFuncionario, codFilial, codUsuario);
			gerenteFilialDAO.incluiGerenteFilial(vo);
			gerenteFilialDAO.commitTrans();
		} catch (Exception e) {
			gerenteFilialDAO.rollbackTrans();
			e.printStackTrace();
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws SRVException
	 */
	public List<GerenteVO> obtemListaGerenteLoja(GerenteVO pesquisaVO) throws SRVException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			return gerenteFilialDAO.obtemListaGerenteLoja(pesquisaVO);
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param codFilial
	 * @return
	 * @throws SRVException
	 */
	public Long lojaCadastrada(Integer codFilial) throws SRVException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			return gerenteFilialDAO.lojaCadastrada(codFilial);
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param codFuncionario
	 * @param codFilial
	 * @return
	 * @throws PersistenciaException
	 */
	public GerenteVO obtemGerenteLoja(Long codFuncionario, Integer codFilial) throws PersistenciaException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			return gerenteFilialDAO.obtemGerenteLoja(codFuncionario, codFilial);
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param codFilialOrigem
	 * @return
	 * @throws SRVException
	 */
	public List<GerenteVO> obtemFuncionariosPorFilial(Integer codFilialOrigem)  throws SRVException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			return gerenteFilialDAO.obtemFuncionariosPorFilial(codFilialOrigem);
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param codFuncionario
	 * @return
	 * @throws PersistenciaException
	 */
	public Integer obtemLojaCadastro(Long codFuncionario) throws PersistenciaException {
		GerenteFilialDAO gerenteFilialDAO = new GerenteFilialDAO();
		try {
			return gerenteFilialDAO.obtemLojaCadastro(codFuncionario);
		} finally {
			gerenteFilialDAO.closeConnection();
		}
	}

}
