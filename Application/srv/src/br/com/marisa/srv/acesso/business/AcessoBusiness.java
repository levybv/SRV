package br.com.marisa.srv.acesso.business;

import java.util.List;
import java.util.Map;

import br.com.marisa.srv.acesso.dao.AcessoDAO;
import br.com.marisa.srv.acesso.vo.FuncionalidadeVO;
import br.com.marisa.srv.acesso.vo.ModuloVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Acesso 
 * 
 * @author Walter Fontes
 */
public class AcessoBusiness {
    
	
    //Log4J
    //private static final Logger log = Logger.getLogger(AcessoBusiness.class);    

    
    //Instancia do Singleton
    private static AcessoBusiness instance = new AcessoBusiness();
    
    
    /**
     * Obtem uma instancia do objeto AcessoBusiness
     * @return O objeto AcessoBusiness
     */
    public static final AcessoBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private AcessoBusiness() {
        // vazio
    }

    /**
     * Obtém acessos do perfil
     * 
     * @param idPerfil
     * @return
     */
    public Map<Integer, List<Integer>> obtemAcessoPerfil(int idPerfil) throws SRVException {
    	AcessoDAO acessoDAO = new AcessoDAO();
        try {
            return acessoDAO.obtemAcessoPerfil(idPerfil);
        } finally {
        	acessoDAO.closeConnection();
        }    	
    }

    /**
     * Obtém acessos do perfil
     * 
     * @param idPerfil
     * @return
     */
    public List<ModuloVO> obtemAcessoPerfilModulos(int idPerfil) throws SRVException {
    	AcessoDAO acessoDAO = new AcessoDAO();
        try {
            return acessoDAO.obtemAcessoPerfilModulos(idPerfil);
        } finally {
        	acessoDAO.closeConnection();
        }    	
    }

    public List<FuncionalidadeVO> obtemAcessoPerfilFuncionalidades(int idPerfil, int codModulo) throws SRVException {
    	AcessoDAO acessoDAO = new AcessoDAO();
        try {
            return acessoDAO.obtemAcessoPerfilFuncionalidades(idPerfil, codModulo);
        } finally {
        	acessoDAO.closeConnection();
        }    	
    }

    public boolean obtemAcessoPerfilFuncionalidadeTipoAcesso(int idPerfil, int codFuncionalidade, int codTipoAcesso) throws SRVException {
    	AcessoDAO acessoDAO = new AcessoDAO();
        try {
            return acessoDAO.obtemAcessoPerfilFuncionalidadeTipoAcesso(idPerfil, codFuncionalidade, codTipoAcesso);
        } finally {
        	acessoDAO.closeConnection();
        }    	
    }
}