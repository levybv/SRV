package br.com.marisa.srv.meta.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.meta.dao.MetaFuncionarioDAO;
import br.com.marisa.srv.meta.vo.MetaFuncionarioVO;

/**
 * Classe para conter os métodos de negócio do módulo de Metas de Funcionarios 
 * 
 * @author Walter Fontes
 */
public class MetaFuncionarioBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(MetaFuncionarioBusiness.class);    

    
    //Instancia do Singleton
    private static MetaFuncionarioBusiness instance = new MetaFuncionarioBusiness();
    
    
    /**
     * Obtem uma instancia do objeto MetaFuncionarioBusiness
     * @return O objeto MetaFuncionarioBusiness
     */
    public static final MetaFuncionarioBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private MetaFuncionarioBusiness() {
    }


	/**
	 * Obtém metas de funcionarios
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param nomeFuncionario
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws SRVException
	 */
	public List obtemMetasFuncionarios(Integer mes, Integer ano, Long idFuncionario, String nomeFuncionario, Integer idIndicador, String descricaoIndicador) throws SRVException {
		
		MetaFuncionarioDAO metaFuncionarioDAO = new MetaFuncionarioDAO();
		try {
			return metaFuncionarioDAO.obtemMetasFuncionarios(mes, ano, idFuncionario, nomeFuncionario, idIndicador, descricaoIndicador);
		} finally {
			metaFuncionarioDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém meta de funcionario
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public MetaFuncionarioVO obtemMetaFuncionario(Integer mes, Integer ano, Long idFuncionario, Integer idIndicador) throws SRVException {
		
		MetaFuncionarioDAO metaFuncionarioDAO = new MetaFuncionarioDAO();
		try {
			return metaFuncionarioDAO.obtemMetaFuncionario(mes, ano, idFuncionario, idIndicador);
		} finally {
			metaFuncionarioDAO.closeConnection();
		}
	}	
	
	
	
	/**
	 * Inclui meta de funcionario
	 * 
	 * @param metaFuncionarioVO
	 * @return
	 * @throws SRVException
	 */
	public void incluiMetaFuncionario(MetaFuncionarioVO metaFuncionarioVO) throws SRVException {
		
		MetaFuncionarioDAO metaFuncionarioDAO = new MetaFuncionarioDAO();
		try {
			metaFuncionarioDAO.incluiMetaFuncionario(metaFuncionarioVO);
		} finally {
			metaFuncionarioDAO.closeConnection();
		}
	}	
	
	
	
	/**
	 * Altera meta de funcionario
	 * 
	 * @param metaFuncionarioVO
	 * @return
	 * @throws SRVException
	 */
	public void alteraMetaFuncionario(MetaFuncionarioVO metaFuncionarioVO) throws SRVException {
		
		MetaFuncionarioDAO metaFuncionarioDAO = new MetaFuncionarioDAO();
		try {
			metaFuncionarioDAO.beginTrans();
			
			MetaFuncionarioVO metaFuncionarioVOAntigo = metaFuncionarioDAO.obtemMetaFuncionario(metaFuncionarioVO.getMes(), metaFuncionarioVO.getAno(), metaFuncionarioVO.getIdFuncionario(), metaFuncionarioVO.getIdIndicador());
			if (metaFuncionarioVOAntigo != null) {
				metaFuncionarioDAO.incluiMetaFuncionarioHistorico(metaFuncionarioVOAntigo);
				metaFuncionarioDAO.excluiMetaFuncionario(metaFuncionarioVO.getMes(), metaFuncionarioVO.getAno(), metaFuncionarioVO.getIdFuncionario(), metaFuncionarioVO.getIdIndicador());
			}
			metaFuncionarioDAO.incluiMetaFuncionario(metaFuncionarioVO);
			metaFuncionarioDAO.commitTrans();
			
		} catch (SRVException e) {
			metaFuncionarioDAO.rollbackTrans();
			throw e;
			
		} finally {
			metaFuncionarioDAO.closeConnection();
		}
	}
	
	
	/**
	 * Altera metas de funcionario
	 * 
	 * @param metas
	 * @return
	 * @throws SRVException
	 */
	public List alteraMetaFuncionario(List metas) throws SRVException {
		
		MetaFuncionarioDAO metaFuncionarioDAO = new MetaFuncionarioDAO();
		List erros = new ArrayList();
		
		try {
			metaFuncionarioDAO.beginTrans();
			
			if (metas != null && metas.size() > 0) {
				
				Iterator itMetas = metas.iterator();
				while (itMetas.hasNext()) {			
					
					MetaFuncionarioVO metaFuncionarioVO = (MetaFuncionarioVO)itMetas.next();
					
					boolean podeProcessar = true;
					
					if (metaFuncionarioVO.getMes().intValue() < 1 || metaFuncionarioVO.getMes().intValue() > 12) { 
						erros.add("Linha " + metaFuncionarioVO.getNroLinha() + ": O mês " + metaFuncionarioVO.getMes().intValue() + " está inválido.");
						log.debug("Linha " + metaFuncionarioVO.getNroLinha() + ": O mês " + metaFuncionarioVO.getMes().intValue() + " está inválido.");
						podeProcessar = false;
					}
					
					if (metaFuncionarioVO.getAno().intValue() < 2000 || metaFuncionarioVO.getAno().intValue() > 2100) { 
						erros.add("Linha " + metaFuncionarioVO.getNroLinha() + ": O ano " + metaFuncionarioVO.getAno().intValue() + " está inválido.");
						log.debug("Linha " + metaFuncionarioVO.getNroLinha() + ": O ano " + metaFuncionarioVO.getAno().intValue() + " está inválido.");
						podeProcessar = false;
					}					

					FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(metaFuncionarioVO.getIdFuncionario());
					if (funcionarioVO == null) {
						erros.add("Linha " + metaFuncionarioVO.getNroLinha() + ": O funcionário " + metaFuncionarioVO.getIdFuncionario().intValue() + " não foi encontrado.");
						log.debug("Linha " + metaFuncionarioVO.getNroLinha() + ": O funcionário " + metaFuncionarioVO.getIdFuncionario().intValue() + " não foi encontrado.");
						podeProcessar = false;
					}
					
					DadosIndicadorVO dadosIndicadorVO = IndicadorBusiness.getInstance().obtemIndicador(metaFuncionarioVO.getIdIndicador().intValue());
					if (dadosIndicadorVO == null) {
						erros.add("Linha " + metaFuncionarioVO.getNroLinha() + ": O indicador " + metaFuncionarioVO.getIdIndicador().intValue() + " não foi encontrado.");
						log.debug("Linha " + metaFuncionarioVO.getNroLinha() + ": O indicador " + metaFuncionarioVO.getIdIndicador().intValue() + " não foi encontrado.");
						podeProcessar = false;
					}
					
					if (metaFuncionarioVO.getIdUnidadeMeta() != null &&
						metaFuncionarioVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_PERCENTUAL && 
						metaFuncionarioVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_PONTOS     && 
						metaFuncionarioVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_UNIDADE    &&
						metaFuncionarioVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_VALOR) {								
						erros.add("Linha " + metaFuncionarioVO.getNroLinha() + ": A unidade " + metaFuncionarioVO.getIdUnidadeMeta().intValue() + " está inválida.");
						log.debug("Linha " + metaFuncionarioVO.getNroLinha() + ": A unidade " + metaFuncionarioVO.getIdUnidadeMeta().intValue() + " está inválida.");
						podeProcessar = false;
					}
					
					if (podeProcessar) {
						MetaFuncionarioVO metaFuncionarioVOAntigo = metaFuncionarioDAO.obtemMetaFuncionario(metaFuncionarioVO.getMes(), metaFuncionarioVO.getAno(), metaFuncionarioVO.getIdFuncionario(), metaFuncionarioVO.getIdIndicador());
						if (metaFuncionarioVOAntigo != null) {
							metaFuncionarioDAO.incluiMetaFuncionarioHistorico(metaFuncionarioVOAntigo);
							metaFuncionarioDAO.excluiMetaFuncionario(metaFuncionarioVO.getMes(), metaFuncionarioVO.getAno(), metaFuncionarioVO.getIdFuncionario(), metaFuncionarioVO.getIdIndicador());
						}
						
						metaFuncionarioDAO.incluiMetaFuncionario(metaFuncionarioVO);
					}
				}
			}
			
			//metaFuncionarioDAO.commitTrans();
			metaFuncionarioDAO.rollbackTrans();
			return erros;
			
		} catch (SRVException e) {
			metaFuncionarioDAO.rollbackTrans();
			throw e;
			
		} finally {
			metaFuncionarioDAO.closeConnection();
		}
	}	
	
	
	/**
	 * Exclui meta de funcionário
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public void excluiMetaFuncionario(Integer mes, Integer ano, Long idFuncionario, Integer idIndicador) throws SRVException {
		
		MetaFuncionarioDAO metaFuncionarioDAO = new MetaFuncionarioDAO();
		try {
			metaFuncionarioDAO.beginTrans();
			
			MetaFuncionarioVO metaFuncionarioVOAntigo = metaFuncionarioDAO.obtemMetaFuncionario(mes, ano, idFuncionario, idIndicador);
			if (metaFuncionarioVOAntigo != null) {
				metaFuncionarioDAO.incluiMetaFuncionarioHistorico(metaFuncionarioVOAntigo);
				metaFuncionarioDAO.excluiMetaFuncionario(mes, ano, idFuncionario, idIndicador);
			}
			metaFuncionarioDAO.commitTrans();
			
		} catch (SRVException e) {
			metaFuncionarioDAO.rollbackTrans();
			throw e;
			
		} finally {
			metaFuncionarioDAO.closeConnection();
		}
	}	
}