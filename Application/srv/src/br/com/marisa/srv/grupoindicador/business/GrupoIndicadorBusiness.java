package br.com.marisa.srv.grupoindicador.business;

import java.util.List;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.grupoindicador.dao.GrupoIndicadorDAO;

/**
 * Classe para conter os métodos de negócio de grupos de indicadores
 * 
 * @author Walter Fontes
 */
public class GrupoIndicadorBusiness {
	
	
    //Instancia do Singleton
    private static GrupoIndicadorBusiness instance = new GrupoIndicadorBusiness();
    
    
    /**
     * Obtem uma instancia do objeto GrupoIndicadorBusiness
     * @return O objeto GrupoIndicadorBusiness
     */
    public static final GrupoIndicadorBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private GrupoIndicadorBusiness() {
    }
	
	
	/**
	 * Obtém grupos de indicadores corporativos
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemGruposIndicadoresCorporativos() throws NumberFormatException, SRVException {
		GrupoIndicadorDAO grupoIndicadorDAO = new GrupoIndicadorDAO();
		try {
			return grupoIndicadorDAO.obtemGruposIndicadores(Constantes.DESCR_TIPO_REM_VAR_CORPORATIVO);
		} finally {
			grupoIndicadorDAO.closeConnection();
		}
    }
    
	/**
	 * Obtém grupos de indicadores corporativos
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemGruposIndicadores() throws NumberFormatException, SRVException {
		GrupoIndicadorDAO grupoIndicadorDAO = new GrupoIndicadorDAO();
		try {
			return grupoIndicadorDAO.obtemGruposIndicadores(null);
		} finally {
			grupoIndicadorDAO.closeConnection();
		}
    }    
}
