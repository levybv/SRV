package br.com.marisa.srv.funcionario.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.funcionario.dao.FuncionarioDAO;
import br.com.marisa.srv.funcionario.dao.SalarioBaseDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.funcionario.vo.SalarioBaseVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Salário Base
 * 
 * @author Walter Fontes
 */
public class SalarioBaseBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(SalarioBaseBusiness.class);    

    
    //Instancia do Singleton
    private static SalarioBaseBusiness instance = new SalarioBaseBusiness();
    
    
    /**
     * Obtem uma instancia do objeto SalarioBaseBusiness
     * @return O objeto SalarioBaseBusiness
     */
    public static final SalarioBaseBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private SalarioBaseBusiness() {
    }


	/**
	 * Obtém salários base
	 * 
	 * @param idFilial
	 * @param idFuncionario
	 * @param nomeFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public List obtemSalariosBase(Integer idFilial, Long idFuncionario, String nomeFuncionario, Integer ano, Integer mes, Boolean salarioBase) throws SRVException {
		
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		try {
			return salarioBaseDAO.obtemSalariosBase(idFilial, idFuncionario, nomeFuncionario, ano, mes, salarioBase);
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	


	/**
	 * Obtém filiais de um funcionário
	 * 
	 * @param idFuncionario
	 * @return
	 * @throws SRVException
	 */
	public List obtemIdsFiliais(Long idFuncionario) throws SRVException {
		
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		try {
			return salarioBaseDAO.obtemIdsFiliais(idFuncionario);
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém salário base
	 * 
	 * @param idEmpresa
	 * @param idFilial
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public SalarioBaseVO obtemSalarioBase(Integer idEmpresa, Integer idFilial, Long idFuncionario, Integer ano, Integer mes) throws SRVException {
		
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		try {
			return salarioBaseDAO.obtemSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém código da filial do funcionário.
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public FuncionarioVO obterCodigoFilialFuncionario(Long idFuncionario) throws SRVException {
		
		FuncionarioVO funcionarioVO = new FuncionarioVO();
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		try {
			funcionarioVO.setIdFilial(salarioBaseDAO.obterCodigoFilialFuncionario(idFuncionario));
			return funcionarioVO;
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
	
	/**
	 * Inclui salário base
	 * 
	 * @param salarioBaseVO
	 * @return
	 * @throws SRVException
	 */
	
	public boolean incluiSalarioBase(SalarioBaseVO salarioBaseVO) throws SRVException {
		
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		
		try {
			
			//Valoriza a filial com a filial do funcionário
			if (salarioBaseVO.getIdFilial() == null) {
				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				funcionarioDAO.setConn(salarioBaseDAO.getConn());
				FuncionarioVO funcionarioVO = funcionarioDAO.obtemFuncionario(salarioBaseVO.getIdFuncionario());
				
				if (funcionarioVO == null) {
					throw new SRVException("Funcionário não encontrado.");
				}
				salarioBaseVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
				salarioBaseVO.setIdFilial(funcionarioVO.getIdFilial());
			}
			
			salarioBaseDAO.incluiSalarioBase(salarioBaseVO);
		
		} finally {
			salarioBaseDAO.closeConnection();
		}
		return false;
	}	
	
	/**
	 * Danilo
	 * buscar salário base
	 * 
	 * @param salarioBaseVO
	 * @return
	 * @throws SRVException
	 */
	
	public SalarioBaseVO buscarSalarioBase(SalarioBaseVO salarioBaseVO)throws SRVException{
		
		SalarioBaseVO salarioVO = new SalarioBaseVO();
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		
		try{
			salarioBaseDAO.beginTrans();
			
			salarioVO = salarioBaseDAO.buscarSalarioBase(salarioBaseVO.getIdFuncionario(),salarioBaseVO.getMes(),salarioBaseVO.getAno()); 
			
		}catch (Exception e) {
			throw new SRVException("Erro ao pesquisar salário base.");
		}finally{
			salarioBaseDAO.closeConnection();
		}
		return salarioVO;
	}
	
	
	/**
	 * Altera salário base
	 * 
	 * @param salarioBaseVO
	 * @return
	 * @throws SRVException
	 */
	
    public void alteraSalarioBase(SalarioBaseVO salarioBaseVO) throws SRVException {
		
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		SalarioBaseVO salarioBaseAntigo = new SalarioBaseVO();
		Integer codFilialFuncionario = null; 
		
		try {
			salarioBaseDAO.beginTrans();
			codFilialFuncionario = salarioBaseDAO.obterCodigoFilialFuncionario(salarioBaseVO.getIdFuncionario());
			salarioBaseAntigo = salarioBaseDAO.buscarSalarioBase(salarioBaseVO.getIdFuncionario(), salarioBaseVO.getMes(), salarioBaseVO.getAno());
			
			if(salarioBaseAntigo.getIdFuncionario() != null){
				salarioBaseVO.setIdFilial(codFilialFuncionario);
			    salarioBaseDAO.incluiSalarioBaseHistorico(salarioBaseAntigo);
				salarioBaseDAO.alterarSalarioBase(salarioBaseVO);
			}else{
				salarioBaseVO.setIdFilial(codFilialFuncionario);
				salarioBaseDAO.alterarSalarioBase(salarioBaseVO);
			}
			
			salarioBaseDAO.commitTrans();
			
		} catch (SRVException e) {
			salarioBaseDAO.rollbackTrans();
			throw e;
			
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
	
	/**
	 * Exclui salário base
	 * 
	 * @param idEmpresa
	 * @param idFilial
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public void excluiSalarioBase(Integer idEmpresa, Integer idFilial, Long idFuncionario, Integer ano, Integer mes) throws SRVException {
		
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		try {
			salarioBaseDAO.beginTrans();
			
			SalarioBaseVO salarioBaseAntigoVO = salarioBaseDAO.obtemSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
			if (salarioBaseAntigoVO != null) {
				salarioBaseDAO.incluiSalarioBaseHistorico(salarioBaseAntigoVO);
				salarioBaseDAO.excluiSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
			}
			salarioBaseDAO.commitTrans();
			
		} catch (SRVException e) {
			salarioBaseDAO.rollbackTrans();
			throw e;
			
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
	
	/**
	 * Altera salários base
	 * 
	 * @param salariosBase
	 * @return
	 * @throws SRVException
	 */
	public List alteraSalarioBase(List salariosBase) throws SRVException {
		
		List erros = new ArrayList();
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		SalarioBaseVO salarioBaseAntigo = new SalarioBaseVO();
		Integer codFilialFuncionario = null;
		
		
		try {
			salarioBaseDAO.beginTrans();
			
			if (salariosBase != null && salariosBase.size() > 0) {
				
				Iterator itSalariosBase = salariosBase.iterator();
				while (itSalariosBase.hasNext()) {
					
					SalarioBaseVO salarioBaseVO = (SalarioBaseVO)itSalariosBase.next();
					
					System.out.println(salarioBaseVO.getIdFilial());
					
					boolean podeProcessar = true;
					
					if (salarioBaseVO.getMes().intValue() < 1 || salarioBaseVO.getMes().intValue() > 12) { 
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": O mês " + salarioBaseVO.getMes().intValue() + " está inválido.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": O mês " + salarioBaseVO.getMes().intValue() + " está inválido.");
						podeProcessar = false;
					}
					
					if (salarioBaseVO.getAno().intValue() < 2000 || salarioBaseVO.getAno().intValue() > 2100) { 
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": O ano " + salarioBaseVO.getAno().intValue() + " está inválido.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": O ano " + salarioBaseVO.getAno().intValue() + " está inválido.");
						podeProcessar = false;
					}					
					
					FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(salarioBaseVO.getIdFuncionario());
					if (funcionarioVO == null) {
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": O funcionário " + salarioBaseVO.getIdFuncionario().intValue() + " não foi encontrado.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": O funcionário " + salarioBaseVO.getIdFuncionario().intValue() + " não foi encontrado.");
						podeProcessar = false;
					}

					if (salarioBaseVO.getIdFilial() != null) {
						List filiais = FilialBusiness.getInstance().obtemListaFiliais(salarioBaseVO.getIdEmpresa().intValue(), salarioBaseVO.getIdFilial().intValue());
						if (filiais == null || filiais.size() == 0) {
							erros.add("Linha " + salarioBaseVO.getNroLinha() + ": A Filial " + salarioBaseVO.getIdFilial().intValue() + " não foi encontrada.");
							log.debug("Linha " + salarioBaseVO.getNroLinha() + ": A Filial " + salarioBaseVO.getIdFilial().intValue() + " não foi encontrada.");
							podeProcessar = false;
						}
					}
					
					if (podeProcessar) {
					
						//Valoriza a filial com a filial do funcionário
						if (salarioBaseVO.getIdFilial() == null) {
							FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
							funcionarioDAO.setConn(salarioBaseDAO.getConn());
							salarioBaseVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
							salarioBaseVO.setIdFilial(funcionarioVO.getIdFilial());
						}
						
						salarioBaseAntigo = salarioBaseDAO.buscarSalarioBase(salarioBaseVO.getIdFuncionario(), salarioBaseVO.getMes(), salarioBaseVO.getAno());
						codFilialFuncionario = salarioBaseDAO.obterCodigoFilialFuncionario(salarioBaseVO.getIdFuncionario());
						
						if(salarioBaseAntigo.getIdFuncionario() == null){
							System.out.println("Salário Base Não Encontrado!");
							salarioBaseDAO.incluiSalarioBase(salarioBaseVO);
							
						}else{
							System.out.println("Salário Base Encontrado!");
							salarioBaseVO.setIdFilial(codFilialFuncionario);
						    salarioBaseDAO.incluiSalarioBaseHistorico(salarioBaseAntigo);
							salarioBaseDAO.alterarSalarioBase(salarioBaseVO);
						}
						 	
					}
				}
			}

			salarioBaseDAO.commitTrans();
			return erros;
			
		} catch (SRVException e) {
			salarioBaseDAO.rollbackTrans();
			throw e;
			
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
	
	/**
	 * Danilo
	 * Método utilizado para importar agrupamento de filiais por funcionários.
	 * @param List salariosBase
	 * @return List
	 * @throws SRVException
	 */
	public List importaAgrupamentoFilias(List salariosBase)throws SRVException{
		
		List erros = new ArrayList();
		SalarioBaseDAO salarioBaseDAO = new SalarioBaseDAO();
		SalarioBaseVO salarioBaseAntigo = new SalarioBaseVO();
		
		
		
		try {
			salarioBaseDAO.beginTrans();
			
			if (salariosBase != null && salariosBase.size() > 0) {
				
				Iterator itSalariosBase = salariosBase.iterator();
				while (itSalariosBase.hasNext()) {
					
					SalarioBaseVO salarioBaseVO = (SalarioBaseVO)itSalariosBase.next();
			
					boolean podeProcessar = true;
					
					if (salarioBaseVO.getMes().intValue() < 1 || salarioBaseVO.getMes().intValue() > 12) { 
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": O mês " + salarioBaseVO.getMes().intValue() + " está inválido.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": O mês " + salarioBaseVO.getMes().intValue() + " está inválido.");
						podeProcessar = false;
					}
					
					if (salarioBaseVO.getAno().intValue() < 2000 || salarioBaseVO.getAno().intValue() > 2100) { 
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": O ano " + salarioBaseVO.getAno().intValue() + " está inválido.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": O ano " + salarioBaseVO.getAno().intValue() + " está inválido.");
						podeProcessar = false;
					}					
					
					FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(salarioBaseVO.getIdFuncionario());
					if (funcionarioVO == null) {
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": O funcionário " + salarioBaseVO.getIdFuncionario().intValue() + " não foi encontrado.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": O funcionário " + salarioBaseVO.getIdFuncionario().intValue() + " não foi encontrado.");
						podeProcessar = false;
					}

					if (salarioBaseVO.getIdFilial() != null) {
						List filiais = FilialBusiness.getInstance().obtemListaFiliais(salarioBaseVO.getIdEmpresa().intValue(), salarioBaseVO.getIdFilial().intValue());
						if (filiais == null || filiais.size() == 0) {
							erros.add("Linha " + salarioBaseVO.getNroLinha() + ": A Filial " + salarioBaseVO.getIdFilial().intValue() + " não foi encontrada.");
							log.debug("Linha " + salarioBaseVO.getNroLinha() + ": A Filial " + salarioBaseVO.getIdFilial().intValue() + " não foi encontrada.");
							podeProcessar = false;
						}
					}
					
					salarioBaseAntigo = salarioBaseDAO.buscarSalarioBaseParaAgrupamentoFilial(salarioBaseVO.getIdFuncionario(),salarioBaseVO.getMes(),salarioBaseVO.getAno(),salarioBaseVO.getIdFilial());
					
					if(salarioBaseAntigo.getIdFuncionario() != null){
						erros.add("Linha " + salarioBaseVO.getNroLinha() + ": Registro já Cadastrado na Base.");
						log.debug("Linha " + salarioBaseVO.getNroLinha() + ": Registro já Cadastrado na Base.");
						podeProcessar = false;
					}
					
					if (podeProcessar) {
						if(salarioBaseAntigo.getIdFuncionario() != null){
							salarioBaseDAO.incluiSalarioBaseHistorico(salarioBaseAntigo);
						}
						salarioBaseDAO.incluiSalarioBase(salarioBaseVO);
					}
				}
			}

			salarioBaseDAO.commitTrans();
			return erros;
			
		} catch (SRVException e) {
			salarioBaseDAO.rollbackTrans();
			throw e;
			
		} finally {
			salarioBaseDAO.closeConnection();
		}
	}
	
}