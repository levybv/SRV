package br.com.marisa.srv.classehay.business;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.classehay.dao.ClasseHayDAO;
import br.com.marisa.srv.classehay.vo.ClasseHayVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Classe Hay 
 * 
 * @author Walter Fontes
 */
public class ClasseHayBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(ClasseHayBusiness.class);    

    
    //Instancia do Singleton
    private static ClasseHayBusiness instance = new ClasseHayBusiness();
    
    
    /**
     * Obtem uma instancia do objeto ClasseHayBusiness
     * @return O objeto ClasseHayBusiness
     */
    public static final ClasseHayBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
	private ClasseHayBusiness() {
		// vazio
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws SRVException
	 */
	public List<ClasseHayVO> obtemListaClassesHay(ClasseHayVO pesquisaVO) throws SRVException {
		ClasseHayDAO classeHayDAO = new ClasseHayDAO();
		try {
			return classeHayDAO.obtemListaClasseHay(pesquisaVO);
		} finally {
			classeHayDAO.closeConnection();
		}
	}

    /**
     * Pesquisa Classes Hay
     * 
     * @param codigo
     * @param descricao
     * @return
     */
    public List<ClasseHayVO> obtemClassesHay(int codigo, String descricao) throws SRVException {
    	ClasseHayDAO classeHayDAO = new ClasseHayDAO();
        try {
            return classeHayDAO.obtemClassesHay(codigo, descricao);
        } finally {
        	classeHayDAO.closeConnection();
        }
    }
    
    
    /**
     * Pesquisa Classe Hay
     * 
     * @param codigo
     * @return
     */
    public ClasseHayVO obtemClasseHay(int codigo) throws SRVException {
    	ClasseHayDAO classeHayDAO = new ClasseHayDAO();
        try {
            return classeHayDAO.obtemClasseHay(codigo);
        } finally {
        	classeHayDAO.closeConnection();
        }
    }    
    
    
    /**
     * Realiza alteração da classe Hay
     * 
     * @param classeHayVO
     * @return
     */
    public void alteraClasseHay(ClasseHayVO classeHayVO) throws SRVException {
    	ClasseHayDAO classeHayDAO = new ClasseHayDAO();
        try {
        	classeHayDAO.beginTrans();
        	
        	//Grava historico da situacao anterior
        	ClasseHayVO classeHayAnteriorVO = classeHayDAO.obtemClasseHay(classeHayVO.getIdClasseHay().intValue());
        	classeHayDAO.incluiClasseHayHistorico(classeHayAnteriorVO);
        	
        	//Efetiva a alteração
        	classeHayDAO.alteraClasseHay(classeHayVO);
        	
        	classeHayDAO.commitTrans();
        	
        } catch (Exception e) {
        	classeHayDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao da classe hay", e);
        } finally {
        	classeHayDAO.closeConnection();
        }    		
    }
    
    
    /**
     * Realiza inclusao do Classe Hay
     * 
     * @param classeHayVO
     * @return
     */
    public void incluiClasseHay(ClasseHayVO classeHayVO) throws SRVException {
    	ClasseHayDAO classeHayDAO = new ClasseHayDAO();
        try {
        	classeHayDAO.incluiClasseHay(classeHayVO);
        } finally {
        	classeHayDAO.closeConnection();
        }    		
    }     
}
