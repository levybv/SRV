package br.com.marisa.srv.acesso.business;

import java.util.List;

import br.com.marisa.srv.acesso.dao.TipoAcessoDAO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os m�todos de neg�cio do m�dulo de Acesso 
 * 
 * @author Walter Fontes
 */
public class TipoAcessoBusiness {
    
	

    
    //Instancia do Singleton
    private static TipoAcessoBusiness instance = new TipoAcessoBusiness();
    
    
    /**
     * Obtem uma instancia do objeto AcessoBusiness
     * @return O objeto AcessoBusiness
     */
    public static final TipoAcessoBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o m�todo getInstance();
     */
    private TipoAcessoBusiness() {
        // vazio
    }

    /**
     * Obt�m acessos do perfil
     * @param acessosJaConcedidos 
     * 
     * @param idPerfil
     * @return
     */
    public List obtemListaTiposAcesso(Integer idFuncionalidade, Integer tipoAcesso/*, String acessosJaConcedidos*/) throws SRVException {
    	TipoAcessoDAO tipoAcessoDAO = new TipoAcessoDAO();
        try {
            return tipoAcessoDAO.obtemListaTipoAcesso(idFuncionalidade,tipoAcesso);
        } finally {
        	tipoAcessoDAO.closeConnection();
        }    	
    }
}