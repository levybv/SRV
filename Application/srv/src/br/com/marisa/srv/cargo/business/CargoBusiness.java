package br.com.marisa.srv.cargo.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.cargo.dao.CargoDAO;
import br.com.marisa.srv.cargo.dao.GrupoCargoDAO;
import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.gpremuneracaovariavel.dao.GrupoRemuneracaoVariavelDAO;
import br.com.marisa.srv.gpremuneracaovariavel.vo.GrupoRemuneracaoVariavelVO;

/**
 * Classe para conter os métodos de negócio do módulo de Cargo
 * 
 * @author Walter Fontes
 */
public class CargoBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(CargoBusiness.class);    

    
    //Instancia do Singleton
    private static CargoBusiness instance = new CargoBusiness();
    
    
    /**
     * Obtem uma instancia do objeto CargoBusiness
     * @return O objeto CargoBusiness
     */
    public static final CargoBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private CargoBusiness() {
        // vazio
    }
    
    /**
     * 
     * @param pesquisaVO
     * @return
     * @throws SRVException
     */
	public List<CargoVO> obtemListaCargo(CargoVO pesquisaVO) throws SRVException {
    	CargoDAO cargoDAO = new CargoDAO();
        try {
			return cargoDAO.obtemListaCargo(pesquisaVO);
        } finally {
        	cargoDAO.closeConnection();
        }
    }
    
	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws SRVException
	 */
	public List<CargoVO> obtemListaCargoComGrupoRemVar(CargoVO pesquisaVO) throws SRVException {

		List<CargoVO> listaCargoComGrupoRemVar = new ArrayList<CargoVO>();

		CargoDAO cargoDAO = new CargoDAO();
		GrupoCargoDAO grupoCargoDAO = new GrupoCargoDAO();
		GrupoRemuneracaoVariavelDAO grupoRemuneracaoVariavelDAO = new GrupoRemuneracaoVariavelDAO();

		try {
			grupoCargoDAO.setConn(cargoDAO.getConn());
			grupoRemuneracaoVariavelDAO.setConn(cargoDAO.getConn());

			List<CargoVO> listaCargo = cargoDAO.obtemListaCargo(pesquisaVO);

			CargoVO ultimoCargoVO = new CargoVO();
			for (CargoVO cargoVO : listaCargo) {
				if (cargoVO.getIdCargo() != ultimoCargoVO.getIdCargo()) {
					String descGrpRemVar = "";
					List<Integer> listaIdGrpRemVar = grupoCargoDAO.obtemIdsGruposRemuneracao(cargoVO.getIdCargo());
					if (ObjectHelper.isNotNull(pesquisaVO) && ObjectHelper.isNotEmpty(pesquisaVO.getIdGrpRemVar())) {
						if (pesquisaVO.getIdGrpRemVar() == -1 && listaIdGrpRemVar.isEmpty()) {
							listaCargoComGrupoRemVar.add(cargoVO);
							ultimoCargoVO = cargoVO;
							continue;
						} else if (pesquisaVO.getIdGrpRemVar() != -1 && listaIdGrpRemVar.contains(pesquisaVO.getIdGrpRemVar())) {
							for (Integer idGrpRemVar : listaIdGrpRemVar) {
								GrupoRemuneracaoVariavelVO grpRemVarVO = grupoRemuneracaoVariavelDAO.obtemGrupoRemuneracaoVariavel(idGrpRemVar);
								descGrpRemVar += (grpRemVarVO.getDescricaoOnline() + " / ");
							}
							if (descGrpRemVar.endsWith(" / ")) {
								descGrpRemVar = descGrpRemVar.substring(0, descGrpRemVar.length()-3);
							}
							cargoVO.setDescricaoTodosGrpRemVar(descGrpRemVar);
							listaCargoComGrupoRemVar.add(cargoVO);
							ultimoCargoVO = cargoVO;
						} else {
							continue;
						}
					} else {
						for (Integer idGrpRemVar : listaIdGrpRemVar) {
							GrupoRemuneracaoVariavelVO grpRemVarVO = grupoRemuneracaoVariavelDAO.obtemGrupoRemuneracaoVariavel(idGrpRemVar);
							descGrpRemVar += (grpRemVarVO.getDescricaoOnline() + " / ");
						}
						if (descGrpRemVar.endsWith(" / ")) {
							descGrpRemVar = descGrpRemVar.substring(0, descGrpRemVar.length()-3);
						}
						cargoVO.setDescricaoTodosGrpRemVar(descGrpRemVar);
						listaCargoComGrupoRemVar.add(cargoVO);
						ultimoCargoVO = cargoVO;
					}
				}
			}

		} finally {
			cargoDAO.closeConnection();
		}

		return listaCargoComGrupoRemVar;
	}
    
    /**
     * Pesquisa Cargo
     * 
     * @param codigo
     * @return
     */
    public CargoVO obtemCargo(int codigo) throws SRVException {
    	CargoDAO cargoDAO = new CargoDAO();
        try {
            return cargoDAO.obtemCargo(codigo);
        } finally {
        	cargoDAO.closeConnection();
        }
    }
    
    
    /**
     * Pesquisa Ids de Grupos de Remuneracoes do Cargo
     * 
     * @param codigo
     * @return
     */
    public List<Integer> obtemIdsGruposRemuneracao(int codigo) throws SRVException {
    	GrupoCargoDAO grupoCargoDAO = new GrupoCargoDAO();
        try {
            return grupoCargoDAO.obtemIdsGruposRemuneracao(codigo);
        } finally {
        	grupoCargoDAO.closeConnection();
        }
    }
    
    
    /**
     * Realiza alteração do cargo
     * 
     * @param cargoVO
     * @param idsGruposRemuneracoes
     * @return
     */
    public void alteraCargo(CargoVO cargoVO, List<Integer> idsGruposRemuneracoes) throws SRVException {
    	GrupoCargoDAO grupoCargoDAO = new GrupoCargoDAO();
        try {
        	grupoCargoDAO.beginTrans();
        	
        	//Se algum grupo de cargo (cargo x grupo de remuneracao) foi retirado, exclui da base e grava historico da exclusao 
        	List<Integer> idsGruposRemuneracoesAtuais = grupoCargoDAO.obtemIdsGruposRemuneracao(cargoVO.getIdCargo().intValue());
        	if (idsGruposRemuneracoesAtuais != null) {
        		Iterator<Integer> itIdsGruposRemuneracoesAtuais = idsGruposRemuneracoesAtuais.iterator();
        		while (itIdsGruposRemuneracoesAtuais.hasNext()) {
        			
        			Integer idGrupo = itIdsGruposRemuneracoesAtuais.next();
        			if (idsGruposRemuneracoes == null || !idsGruposRemuneracoes.contains(idGrupo)) {
        				grupoCargoDAO.incluiGrupoCargoHistorico(idGrupo.intValue(), cargoVO.getIdCargo().intValue(), cargoVO.getDataUltimaAlteracao(), cargoVO.getIdUsuarioUltimaAlteracao().intValue());
        				grupoCargoDAO.excluiGrupoCargo(idGrupo.intValue(), cargoVO.getIdCargo().intValue());
        			}
        		}
        	}
        	
        	//Inclui os grupos de cargo (cargo x grupo de remuneracao) que nao existiam
        	if (idsGruposRemuneracoes != null) {
        		Iterator<Integer> itIdsGruposRemuneracoes = idsGruposRemuneracoes.iterator();
        		while (itIdsGruposRemuneracoes.hasNext()) {
        			Integer idGrupo = itIdsGruposRemuneracoes.next();
        			if (idsGruposRemuneracoesAtuais == null || !idsGruposRemuneracoesAtuais.contains(idGrupo)) {
        				grupoCargoDAO.incluiGrupoCargo(idGrupo.intValue(), cargoVO.getIdCargo().intValue(), cargoVO.getDataUltimaAlteracao(), cargoVO.getIdUsuarioUltimaAlteracao().intValue());
        			}
        		}
        	}
        	
        	//Grava historico da situacao anterior
        	CargoDAO cargoDAO = new CargoDAO();
        	cargoDAO.setConn(grupoCargoDAO.getConn());
        	CargoVO cargoAnteriorVO = cargoDAO.obtemCargo(cargoVO.getIdCargo().intValue());
        	cargoDAO.incluiCargoHistorico(cargoAnteriorVO);
        	
        	//Efetiva a alteração
        	cargoDAO.alteraCargo(cargoVO);
        	
        	grupoCargoDAO.commitTrans();
        	
        } catch (Exception e) {
        	grupoCargoDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao do cargo", e);
        } finally {
        	grupoCargoDAO.closeConnection();
        }    		
    }
    
    
    /**
     * Realiza inclusao do Cargo
     * 
     * @param cargoVO
     * @param idsGruposRemuneracoes
     * @return
     */
    public void incluiCargo(CargoVO cargoVO, List<Integer> idsGruposRemuneracoes) throws SRVException {
    	CargoDAO cargoDAO = new CargoDAO();
        try {
        	cargoDAO.beginTrans();
        	cargoDAO.incluiCargo(cargoVO);
        	
        	if (idsGruposRemuneracoes != null) {
        		
        		GrupoCargoDAO grupoCargoDAO = new GrupoCargoDAO();
        		grupoCargoDAO.setConn(cargoDAO.getConn());
        		
        		Iterator<Integer> itIdsGruposRemuneracoes = idsGruposRemuneracoes.iterator();
        		while (itIdsGruposRemuneracoes.hasNext()) {
        			Integer idGrupo = itIdsGruposRemuneracoes.next();
    				grupoCargoDAO.incluiGrupoCargo(idGrupo.intValue(), cargoVO.getIdCargo().intValue(), cargoVO.getDataUltimaAlteracao(), cargoVO.getIdUsuarioUltimaAlteracao().intValue());
        		}        		
        	}
        	cargoDAO.commitTrans();
        	
        } finally {
        	cargoDAO.closeConnection();
        }    		
    }     
}
