package br.com.marisa.srv.funcionario.business;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.cargo.business.CargoBusiness;
import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.demissao.business.MotivoDemissaoBusiness;
import br.com.marisa.srv.demissao.vo.MotivoDemissaoVO;
import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.funcionario.dao.FuncionarioDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Classe para conter os métodos de negócio do módulo de Cargos 
 * 
 * @author Walter Fontes
 */
public class FuncionarioBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(FuncionarioBusiness.class);    

    
    //Instancia do Singleton
    private static FuncionarioBusiness instance = new FuncionarioBusiness();
    
    
    /**
     * Obtem uma instancia do objeto CalendarioBusiness
     * @return O objeto CalendarioBusiness
     */
    public static final FuncionarioBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private FuncionarioBusiness() {
    }

    private FuncionarioDAO obtemFuncionarioDAO() {
    	return new FuncionarioDAO();
    }

    /**
     * Obtém funcionários líderes de loja
     * 
     * @return
     * @throws SRVException
     */
	public List obtemLideresLoja() throws SRVException {
		return obtemLideres("LIDERANCA_LOJA");
	}
	
	
    /**
     * Obtém funcionários líderes por grupo de remuneracao
     * 
     * @return
     * @throws SRVException
     */
	public List obtemLideres(String grupoRemuneracao) throws SRVException {
		FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
		try {
			return funcionarioDAO.obtemLideres(grupoRemuneracao);	
		} finally {
			funcionarioDAO.closeConnection();
		}
	}	

	/**
	 * 
	 * @param idFuncionario
	 * @param nomeFuncionario
	 * @param cracha
	 * @param cpfFuncionario
	 * @param idCargo
	 * @param idsFuncs
	 * @param idsFiliais
	 * @return
	 * @throws PersistenciaException
	 */
	public List<FuncionarioVO> obtemListaFuncionario(Long idFuncionario, String nomeFuncionario, String cracha, Long cpfFuncionario, Integer idCargo, List<Long> idsFuncs, List<Integer> idsFiliais) throws PersistenciaException{
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		return funcionarioDAO.obtemListaFuncionario(idFuncionario,nomeFuncionario,cracha,cpfFuncionario,idCargo,idsFuncs,idsFiliais, null);
	}

	/**
	 * Obtém os ids de funcionários de forma recurssiva para pegar todos os subordinados
	 * de um funcionário, independente do nível do mesmo.
	 * 
	 * @param idFuncionario
	 * @param idsSubordinados
	 * @return
	 * @throws SRVException
	 */
	private List obtemIdsSubordinados(Long idFuncionario, List idsSubordinados, Connection conn) throws SRVException {
		
		if (idsSubordinados == null) {
			idsSubordinados = new ArrayList();
		}
		
		idsSubordinados.add(idFuncionario);
		
		FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
		funcionarioDAO.setConn(conn);
		List idsFuncionarios = funcionarioDAO.obtemFuncionariosSubordinados(idFuncionario);
		
		if (ObjectHelper.isNotEmpty(idsFuncionarios)) {
			Iterator itIdsFuncionarios = idsFuncionarios.iterator();
			while (itIdsFuncionarios.hasNext()) {
				
				Long idFunc = (Long)itIdsFuncionarios.next();
				if (!idsSubordinados.contains(idFunc)) {
					obtemIdsSubordinados(idFunc, idsSubordinados, conn);
				}
			}
		}
		return idsSubordinados;
	}

    /**
     * Obtém funcionário 
     * 
     * @param idFuncionario
     * @return
     * @throws SRVException
     */
	public FuncionarioVO obtemFuncionario(Long idFuncionario) throws SRVException {
		FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
		try {
			return funcionarioDAO.obtemFuncionario(idFuncionario);	
		} finally {
			funcionarioDAO.closeConnection();
		}
	}	
	
	
    /**
     * Obtém funcionário 
     * 
     * @param idFuncionario
     * @return
     * @throws SRVException
     */
	public void incluiFuncionario(FuncionarioVO funcionarioVO) throws SRVException {
		FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
		try {
			funcionarioDAO.incluiFuncionario(funcionarioVO);	
		} finally {
			funcionarioDAO.closeConnection();
		}
	}		
	
	
	/**
     * Realiza alteração do funcionário
     * 
     * @param funcionarioVO
     * @return
     */
    public void alteraFuncionario(FuncionarioVO funcionarioVO) throws SRVException {
    	FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
        try {
        	funcionarioDAO.beginTrans();
        	
        	//Inclui histórico do funcionario anterior 
        	FuncionarioVO funcionarioAntigoVO = funcionarioDAO.obtemFuncionario(funcionarioVO.getIdFuncionario());
        	if (funcionarioAntigoVO != null) {
        		funcionarioDAO.incluiFuncionarioHistorico(funcionarioAntigoVO);
        	}
        	
        	//Efetiva a alteração
        	funcionarioDAO.alteraFuncionario(funcionarioVO, true, true);
        	
        	funcionarioDAO.commitTrans();
        	
        } catch (Exception e) {
        	funcionarioDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao do funcionario", e);
        } finally {
        	funcionarioDAO.closeConnection();
        }
    }
    
    
	/**
     * Realiza alteração dos funcionários
     * 
     * @param funcionarios
	 * @return Erros ocorridos na alteração
	 * @throws SRVException 
     */
    public List importaFuncionarios(List funcionarios) throws SRVException {
    	
    	FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
    	List erros = new ArrayList();
    	
        try {
        	funcionarioDAO.beginTrans();
        	
        	if (funcionarios != null) {
        		
        		Iterator itFuncionario = funcionarios.iterator();
        		while (itFuncionario.hasNext()) {
        			
        			FuncionarioVO funcionarioVO = (FuncionarioVO)itFuncionario.next();
        			boolean podeProcessar = true;
        			
        			System.out.println("Processando linha: " + funcionarioVO.getNroLinha());
        			
        			//Verifica se a filial existe
        			List filiais = FilialBusiness.getInstance().obtemListaFiliais(funcionarioVO.getIdEmpresa().intValue(), funcionarioVO.getIdFilial().intValue()); 
        			if (filiais == null || filiais.size() == 0) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": A filial " + funcionarioVO.getIdEmpresa() + "/" + funcionarioVO.getIdFilial() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": A filial " + funcionarioVO.getIdEmpresa() + "/" + funcionarioVO.getIdFilial() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
						podeProcessar = false;
        			}        			

        			//Verifica se existe motivo demissao
        			if ( funcionarioVO.getCodigoMotivoDEmissao() != null) {
            			MotivoDemissaoVO motivoDemissaoVO = MotivoDemissaoBusiness.getInstance().obtemMotivoDemissao(funcionarioVO.getCodigoMotivoDEmissao()); 
            			if ( motivoDemissaoVO == null ) {
    						erros.add("Linha " + funcionarioVO.getNroLinha() + ": O código motivo demissão " + funcionarioVO.getCodigoMotivoDEmissao() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
    						log.debug("Linha " + funcionarioVO.getNroLinha() + ": O código motivo demissão " + funcionarioVO.getCodigoMotivoDEmissao() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
    						podeProcessar = false;
            			}        			
        			}

        			//Verifica o tamanho da filial do RH
        			if (funcionarioVO.getIdFilialRH().length() > 5) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": A Filial RH " + funcionarioVO.getIdFilialRH() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (5).");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": A Filial RH " + funcionarioVO.getIdFilialRH() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (5).");
						podeProcessar = false;
        			}

        			//Verifica o tamanho da descrição da filial do RH
        			if (funcionarioVO.getDescricaoFilialRH().length() > 50) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": A Descrição da Filial RH " + funcionarioVO.getDescricaoFilialRH() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (50).");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": A Descrição da Filial RH " + funcionarioVO.getDescricaoFilialRH() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (50).");
						podeProcessar = false;
        			}

        			//Verifica se o cargo existe
        			CargoVO cargoVO = CargoBusiness.getInstance().obtemCargo(funcionarioVO.getIdCargo().intValue()); 
        			if (cargoVO == null) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": O cargo " + funcionarioVO.getIdCargo() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": O cargo " + funcionarioVO.getIdCargo() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
						podeProcessar = false;
        			}
        			
        			//Verifica o tamanho da descrição do crachá
        			if (funcionarioVO.getCracha().length() > 20) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": O crachá " + funcionarioVO.getCracha() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (20).");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": O crachá " + funcionarioVO.getCracha() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (20).");
						podeProcessar = false;
        			}        			

        			//Verifica o tamanho do nome do funcionário
        			if (funcionarioVO.getNomeFuncionario().length() > 60) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": O nome do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (60).");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": O nome do funcionario " + funcionarioVO.getNomeFuncionario() + " possui tamanho superior ao tamanho máximo (60).");
						podeProcessar = false;
        			}

        			//Verifica a data de admissão
        			if (funcionarioVO.getDataAdmissao() == null) {
						erros.add("Linha " + funcionarioVO.getNroLinha() + ": A data de admissão " + funcionarioVO.getDataAdmissaoFormatada() + " está inválida.");
						log.debug("Linha " + funcionarioVO.getNroLinha() + ": A data de admissão " + funcionarioVO.getDataAdmissaoFormatada() + " está inválida.");
						podeProcessar = false;
        			}

        			//Valida Funcionários
            		if (funcionarioVO.getIdFuncionarioAvaliador() != null && funcionarioVO.getIdFuncionarioAvaliador().longValue() > 0) {
            			FuncionarioVO funcionarioAvaliadorVO = FuncionarioBusiness.getInstance().obtemFuncionario(funcionarioVO.getIdFuncionarioAvaliador());
            			if (funcionarioAvaliadorVO == null) {
    						erros.add("Linha " + funcionarioVO.getNroLinha() + ": O funcionário avaliador " + funcionarioVO.getIdFuncionarioAvaliador() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
    						log.debug("Linha " + funcionarioVO.getNroLinha() + ": O funcionário avaliador " + funcionarioVO.getIdFuncionarioAvaliador() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
    						podeProcessar = false;
            			}
            		}
            		if (funcionarioVO.getIdFuncionarioSuperior() != null && funcionarioVO.getIdFuncionarioSuperior().longValue() > 0) {
            			FuncionarioVO funcionarioSuperiorVO = FuncionarioBusiness.getInstance().obtemFuncionario(funcionarioVO.getIdFuncionarioSuperior());
            			if (funcionarioSuperiorVO == null) {
    						erros.add("Linha " + funcionarioVO.getNroLinha() + ": O funcionário superior " + funcionarioVO.getIdFuncionarioSuperior() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
    						log.debug("Linha " + funcionarioVO.getNroLinha() + ": O funcionário superior " + funcionarioVO.getIdFuncionarioSuperior() + " do funcionario " + funcionarioVO.getNomeFuncionario() + " não foi encontrado na base.");
    						podeProcessar = false;
            			}
            		}        			

        			
                	//Efetiva a inclusão
        			if (podeProcessar) {
        			
	                	//Inclui histórico do funcionario anterior 
	                	FuncionarioVO funcionarioAntigoVO = funcionarioDAO.obtemFuncionario(funcionarioVO.getIdFuncionario());
	                	if (funcionarioAntigoVO != null) {
	                		funcionarioDAO.incluiFuncionarioHistorico(funcionarioAntigoVO);
	                		
	                    	//Efetiva a alteração
	                    	funcionarioDAO.alteraFuncionario(funcionarioVO, false, false);                		
	                	} else {
	                		
	                    	//Efetiva a inclusão
	                		if (funcionarioVO.getIdFuncionarioAvaliador() != null && funcionarioVO.getIdFuncionarioAvaliador().longValue() == 0) {
	                			funcionarioVO.setIdFuncionarioAvaliador(null);
	                		}
	                		if (funcionarioVO.getIdFuncionarioSuperior() != null && funcionarioVO.getIdFuncionarioSuperior().longValue() == 0) {
	                			funcionarioVO.setIdFuncionarioSuperior(null);
	                		}
	                		
	                    	funcionarioDAO.incluiFuncionario(funcionarioVO);                		
	                	}
        			}
        		}
        	}
        	
        	System.out.println("Fim processamento...");
        	funcionarioDAO.commitTrans();
        	System.out.println("Commit efetuado...");
        	return erros;
        	
        } catch (Exception e) {
        	e.printStackTrace();
        	funcionarioDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao dos funcionarios", e);
        } finally {
        	funcionarioDAO.closeConnection();
        }
    }    
    
    
	/**
     * Realiza exclusão do funcionário
     * 
     * @param idFuncionario
     * @return
     */
    public String excluiFuncionario(Long idFuncionario) throws SRVException {
    	FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
        try {
        	
        	funcionarioDAO.beginTrans();
        	
        	//Inclui histórico do funcionario anterior 
        	FuncionarioVO funcionarioAntigoVO = funcionarioDAO.obtemFuncionario(idFuncionario);
        	if (funcionarioAntigoVO != null) {
        		funcionarioDAO.incluiFuncionarioHistorico(funcionarioAntigoVO);
        	}
        	
        	//Efetiva a exclusão
        	funcionarioDAO.excluiFuncionario(idFuncionario);
        	
        	funcionarioDAO.commitTrans();
        	
        } catch (Exception e) {
        	funcionarioDAO.rollbackTrans();
        	return "Não é possível excluir o funcionário devido ao mesmo já estar sendo utilizado.";
        	
        } finally {
        	funcionarioDAO.closeConnection();
        }
        return null;
    }

	public List geraRelatorioFuncionarios() throws SRVException {
		List funcionarios = new ArrayList();
		FuncionarioDAO funcionarioDAO = obtemFuncionarioDAO();
		try {
			funcionarios = funcionarioDAO.geraRelatorioFuncionarios();
		} catch (Exception e) {
			throw new SRVException(log, "Não foi possível gerar relatorio excel de funcionarios", e);
		} finally {
			funcionarioDAO.closeConnection();
		}
		return funcionarios;
	}


	public List<FuncionarioVO> obtemListaFuncionariosByIdGrupo(Integer idGrupo,
			Integer notSitRH) throws PersistenciaException {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		return funcionarioDAO.obtemListaFuncionariosByIdGrupo(idGrupo,notSitRH);
	}
}