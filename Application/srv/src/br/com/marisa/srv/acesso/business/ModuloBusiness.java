package br.com.marisa.srv.acesso.business;

import java.util.List;

import br.com.marisa.srv.acesso.dao.ModuloDAO;
import br.com.marisa.srv.acesso.vo.ModuloVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os m�todos de neg�cio do m�dulo de Acesso 
 * 
 * @author Walter Fontes
 */
public class ModuloBusiness {

    private static ModuloBusiness instance = new ModuloBusiness();

    /**
     * Obtem uma instancia do objeto AcessoBusiness
     * @return O objeto AcessoBusiness
     */
    public static final ModuloBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o m�todo getInstance();
     */
    private ModuloBusiness() {}

    /**
     * Obt�m acessos do perfil
     * 
     * @param idPerfil
     * @return
     */
    public List<ModuloVO> obtemListaModulos(Integer idModulo) throws SRVException {
    	ModuloDAO moduloDAO = new ModuloDAO();
        try {
            return moduloDAO.obtemListaModulos(idModulo);
        } finally {
        	moduloDAO.closeConnection();
        }    	
    }
}