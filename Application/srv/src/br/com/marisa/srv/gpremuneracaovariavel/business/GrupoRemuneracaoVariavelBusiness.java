package br.com.marisa.srv.gpremuneracaovariavel.business;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.gpremuneracaovariavel.dao.GrupoRemuneracaoVariavelDAO;
import br.com.marisa.srv.gpremuneracaovariavel.vo.GrupoRemuneracaoVariavelVO;

/**
 * Classe para conter os métodos de negócio do módulo de Grupo de Remuneração Variável 
 * 
 * @author Walter Fontes
 */
public class GrupoRemuneracaoVariavelBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(GrupoRemuneracaoVariavelBusiness.class);    

    
    //Instancia do Singleton
    private static GrupoRemuneracaoVariavelBusiness instance = new GrupoRemuneracaoVariavelBusiness();
    
    
    /**
     * Obtem uma instancia do objeto GrupoRemuneracaoVariavelBusiness
     * @return O objeto GrupoRemuneracaoVariavelBusiness
     */
    public static final GrupoRemuneracaoVariavelBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private GrupoRemuneracaoVariavelBusiness() {
        // vazio
    }

    /**
     * 
     * @param pesquisaVO
     * @return
     * @throws SRVException
     */
	public List<GrupoRemuneracaoVariavelVO> obtemListaGrupoRemuneracaoVariavel(GrupoRemuneracaoVariavelVO pesquisaVO) throws SRVException {
		GrupoRemuneracaoVariavelDAO grupoRemuneracaoVariavelDAO = new GrupoRemuneracaoVariavelDAO();
		try {
			return grupoRemuneracaoVariavelDAO.obtemListaGrupoRemuneracaoVariavel(pesquisaVO);
		} finally {
			grupoRemuneracaoVariavelDAO.closeConnection();
		}
	}
    
    /**
     * Pesquisa Grupos de Remuneracao
     * 
     * @param codigo
     * @param descricao
     * @return
     */
    public List<GrupoRemuneracaoVariavelVO> obtemGruposRemuneracao(int codigo, String descricao) throws SRVException {
    	GrupoRemuneracaoVariavelDAO grupoRemuneracaoVariavelDAO = new GrupoRemuneracaoVariavelDAO();
        try {
            return grupoRemuneracaoVariavelDAO.obtemGruposRemuneracaoVariavel(codigo, descricao);
        } finally {
        	grupoRemuneracaoVariavelDAO.closeConnection();
        }
    }
    
    
    /**
     * Pesquisa Grupos de Remuneracao
     * 
     * @param codigo
     * @return
     */
    public GrupoRemuneracaoVariavelVO obtemGrupoRemuneracaoVariavel(int codigo) throws SRVException {
    	GrupoRemuneracaoVariavelDAO grupoRemuneracaoVariavelDAO = new GrupoRemuneracaoVariavelDAO();
        try {
            return grupoRemuneracaoVariavelDAO.obtemGrupoRemuneracaoVariavel(codigo);
        } finally {
        	grupoRemuneracaoVariavelDAO.closeConnection();
        }
    }    
    
    
    /**
     * Realiza alteração do Grupos de Remuneracao
     * 
     * @param grupoRemuneracaoVariavelVO
     * @return
     */
    public void alteraGrupoRemuneracaoVariavel(GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelVO) throws SRVException {
    	GrupoRemuneracaoVariavelDAO grupoRemuneracaoVariavelDAO = new GrupoRemuneracaoVariavelDAO();
        try {
        	grupoRemuneracaoVariavelDAO.beginTrans();
        	
        	//Grava historico da situacao anterior
        	GrupoRemuneracaoVariavelVO grupoRemuneracaoVariavelAnteriorVO = grupoRemuneracaoVariavelDAO.obtemGrupoRemuneracaoVariavel(grupoRemuneracaoVariavelVO.getIdGrupoRemuneracao().intValue());
        	grupoRemuneracaoVariavelDAO.incluiGrupoRemuneracaoVariavelHistorico(grupoRemuneracaoVariavelAnteriorVO);
        	
        	//Efetiva a alteração
        	grupoRemuneracaoVariavelDAO.alteraGrupoRemuneracaoVariavel(grupoRemuneracaoVariavelVO);
        	
        	grupoRemuneracaoVariavelDAO.commitTrans();
        	
        } catch (Exception e) {
        	grupoRemuneracaoVariavelDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao do Grupos de Remuneracao", e);
        } finally {
        	grupoRemuneracaoVariavelDAO.closeConnection();
        }    		
    }
}
