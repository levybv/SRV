package br.com.marisa.srv.filial.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.dao.FilialDAO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Filiais 
 * 
 * @author Walter Fontes
 */
public class FilialBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(FilialBusiness.class);    

    private static FilialBusiness instance = new FilialBusiness();

    /**
     * Obtem uma instancia do objeto FilialBusiness
     * @return O objeto FilialBusiness
     */
    public static final FilialBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private FilialBusiness() {
    }

    
    /**
     * Obtém lista de filiais conforme filtro
     * 
     * @param codEmpresa
     * @param codFilial
     * @return
     * @throws SRVException
     */
    public List obtemListaFiliais(int codEmpresa, int codFilial) throws SRVException {
    	return obtemListaFiliais( codEmpresa,  codFilial, null);
    }
    
    
    /**
     * Obtém lista de filiais conforme filtro
     * 
     * @param codEmpresa
     * @param codFilial
     * @param isAtivo
     * @return
     * @throws SRVException
     */
	public List obtemListaFiliais(int codEmpresa, int codFilial,Boolean isAtivo) throws SRVException {
		FilialDAO filialDAO = new FilialDAO();
		try {
			return filialDAO.obtemListaFiliais(codEmpresa, codFilial, isAtivo);
		}finally {
			filialDAO.closeConnection();
		}
	}

	
    /**
     * Altera os dados de uma filial
     * 
     * @param filialVO
     * @return
     * @throws SRVException
     */
	public void alterarFilial(FilialVO filialVO) throws SRVException {
		FilialDAO filialDAO = new FilialDAO();
		try {
			filialDAO.beginTrans();
			
			FilialVO filialVOAntigo = filialDAO.obtemFilial(filialVO.getCodEmpresa(), filialVO.getCodFilial());
			if (filialVOAntigo != null) {
				filialDAO.incluiFilialHistorico(filialVOAntigo);
			}
			filialDAO.alterarFilial(filialVO);

			filialDAO.commitTrans();
			
		} catch (Exception e) {
			filialDAO.rollbackTrans();
			throw new SRVException("Erro ao alterar filial", e);
			
		} finally {
			filialDAO.closeConnection();
		}
	}

    public boolean obterFilial(Integer codEmpresa, Integer codFilial) throws PersistenciaException {
        FilialVO filialEncontrado = new FilialVO();
        FilialDAO filialDAO = new FilialDAO();

        try {
			filialEncontrado = filialDAO.obtemFilial(codEmpresa, codFilial);
			if(filialEncontrado == null) {
			    return true;
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir filial", e);
		} finally {
			filialDAO.closeConnection();
		}

        return false;
    }

    public void incluirFilial(FilialVO filial) throws PersistenciaException {
            FilialDAO filialDAO = new FilialDAO();
            try {
                filialDAO.incluirFilial(filial);
            } catch(Exception e) {
                throw new PersistenciaException(log, "Não foi possível incluir filial", e);
            } finally {
                filialDAO.closeConnection();
            }
            return;
        }

    public List obterListaTipoFiliais() throws PersistenciaException {
            FilialDAO filialDAO = new FilialDAO();
            try {
            	return filialDAO.obterListaTipoFiliais();
            } catch(Exception e) {
                throw new PersistenciaException(log, "Não foi possível obter a lista de Tipo Filiais", e);
            } finally {
                filialDAO.closeConnection();
            }
     }

    public List pesquisarListaTipoFiliais(Integer codTipoFil) throws PersistenciaException {
            List listaTipoFil = new ArrayList();
            FilialDAO filialDAO = new FilialDAO();
            try {
                listaTipoFil = filialDAO.pesquisarListaTipoFiliais(codTipoFil);
            } catch(Exception e) {
                throw new PersistenciaException(log, "Não foi possível obter a lista de Tipo Filiais", e);
            } finally {
                filialDAO.closeConnection();
            }
            return listaTipoFil;
    }

    /**
	 * 
	 * @return
	 * @throws PersistenciaException
	 * @Deprecated use obtemTodasFiliais( Boolean)
	 */
    @Deprecated
    public List obterTodasFiliais() throws PersistenciaException {
        List listaFil;
        listaFil = new ArrayList();
        FilialDAO filialDAO = new FilialDAO();
        try{
            listaFil = filialDAO.obterTodasFiliais();
        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível obter a lista de Tipo Filiais", e);
        }finally{
            filialDAO.closeConnection();
        }
        return listaFil;
    }


   
//	public List<FilialVO> obtemTodasFiliais() throws PersistenciaException {
//		return obtemTodasFiliais(null);
//	}
    /**
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<FilialVO> obtemTodasFiliais(Boolean isAtivo) throws PersistenciaException {		
		FilialDAO filialDAO = new FilialDAO();
		return filialDAO.obtemTodasFiliais(isAtivo);
	}

//	public FilialVO obtemFilial(int codigoEmpresa, Integer idFilial) throws PersistenciaException{
//		return obtemFilial(codigoEmpresa, idFilial, null);
//	}

	public FilialVO obtemFilial(int codigoEmpresa, Integer idFilial, Boolean isAtivo) throws PersistenciaException {
		FilialDAO filialDAO = new FilialDAO();
		return filialDAO.obtemFilial(codigoEmpresa, idFilial, isAtivo);
	}

}
