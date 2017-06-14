package br.com.marisa.srv.acesso.business;

import java.util.List;

import br.com.marisa.srv.acesso.dao.FuncionalidadeDAO;
import br.com.marisa.srv.acesso.vo.FuncionalidadeVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Acesso 
 * 
 * @author Walter Fontes
 */
public class FuncionalidadeBusiness {
    
	

    
    //Instancia do Singleton
    private static FuncionalidadeBusiness instance = new FuncionalidadeBusiness();
    
    
    /**
     * Obtem uma instancia do objeto AcessoBusiness
     * @return O objeto AcessoBusiness
     */
    public static final FuncionalidadeBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private FuncionalidadeBusiness() {
        // vazio
    }

    /**
     * Obtém acessos do perfil
     * 
     * @param idPerfil
     * @return
     */
    public List<FuncionalidadeVO> obtemListaFuncionalidades(Integer idFuncionalidade, Integer idModulo) throws SRVException {
    	FuncionalidadeDAO funcionalidadeDAO = new FuncionalidadeDAO();
        try {
            return funcionalidadeDAO.obtemListaFuncionalidades(idFuncionalidade,idModulo);
        } finally {
        	funcionalidadeDAO.closeConnection();
        }    	
    }

    /**
     */
    public List<FuncionalidadeVO> obtemListaFuncionalidadesPai(Integer idModulo) throws SRVException {
    	FuncionalidadeDAO funcionalidadeDAO = new FuncionalidadeDAO();
        try {
            return funcionalidadeDAO.obtemListaFuncionalidadesPai(idModulo);
        } finally {
        	funcionalidadeDAO.closeConnection();
        }    	
    }

    /**
     */
    public List<FuncionalidadeVO> obtemFuncionalidadesFilha(Integer idFuncionalidade) throws SRVException {
    	FuncionalidadeDAO funcionalidadeDAO = new FuncionalidadeDAO();
        try {
            return funcionalidadeDAO.obtemFuncionalidadesFilha(idFuncionalidade);
        } finally {
        	funcionalidadeDAO.closeConnection();
        }    	
    }
}