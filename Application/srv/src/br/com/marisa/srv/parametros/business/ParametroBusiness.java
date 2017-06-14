package br.com.marisa.srv.parametros.business;

import java.util.Map;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.parametros.dao.ParametroDAO;
import br.com.marisa.srv.parametros.vo.ParametroVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class ParametroBusiness {

	// Instancia do Singleton
	private static ParametroBusiness instance = new ParametroBusiness();

	/**
	 * Obtem uma instancia do objeto
	 * 
	 * @return
	 */
	public static final ParametroBusiness getInstance() {
		return instance;
	}

	/**
	 * Construtor default. Foi escondido como private por cargo do singleton.
	 * Utilizar o método getInstance();
	 */
	private ParametroBusiness() {
	}

	/**
	 * 
	 * @param nomeProcesso
	 * @param nomeParametro
	 * @return
	 * @throws SRVException
	 */
	public Map<String, String> obtemParametro(String nomeProcesso, String nomeParametro) throws SRVException {
		ParametroDAO dao = new ParametroDAO();
		try {
			return dao.obtemParametro(nomeProcesso, nomeParametro);
		} finally {
			dao.closeConnection();
		}
	}

	/**
	 * 
	 * @param nomeProcesso
	 * @return
	 * @throws SRVException
	 */
	public Map<String, Map<String, String>> obtemParametro(String nomeProcesso) throws SRVException {
		ParametroDAO dao = new ParametroDAO();
		try {
			return dao.obtemParametro(nomeProcesso);
		} finally {
			dao.closeConnection();
		}
	}

	/**
	 * 
	 * @param parametroVO
	 * @throws SRVException
	 */
    public void incluiParametro(ParametroVO parametroVO) throws SRVException {
    	ParametroDAO parametroDAO = new ParametroDAO();
        try {
        	parametroDAO.incluiCargo(parametroVO);
        } finally {
        	parametroDAO.closeConnection();
        }    		
    }     

}