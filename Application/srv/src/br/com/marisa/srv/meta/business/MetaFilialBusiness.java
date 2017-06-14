package br.com.marisa.srv.meta.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.meta.dao.MetaFilialDAO;
import br.com.marisa.srv.meta.vo.AcompMetaFilialVO;
import br.com.marisa.srv.meta.vo.MetaFilialVO;

/**
 * Classe para conter os métodos de negócio do módulo de Metas de Filiais 
 * 
 * @author Walter Fontes
 */
public class MetaFilialBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(MetaFilialBusiness.class);    

    
    //Instancia do Singleton
    private static MetaFilialBusiness instance = new MetaFilialBusiness();
    
    
    /**
     * Obtem uma instancia do objeto MetaFilialBusiness
     * @return O objeto MetaFilialBusiness
     */
    public static final MetaFilialBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private MetaFilialBusiness() {
    }


	/**
	 * Obtém metas de filiais
	 * 
	 * @param mes
	 * @param ano
	 * @param idFilial
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws SRVException
	 */
	public List obtemMetasFiliais(Integer mes, Integer ano, Integer idFilial, Integer idIndicador, String descricaoIndicador) throws SRVException {
		
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		try {
			List metas = metaFilialDAO.obtemMetasFiliais(mes, ano, idFilial, idIndicador, descricaoIndicador);
			List metasPercentual = new ArrayList();
			if (metas != null) {
				Iterator itMetas = metas.iterator();
				while (itMetas.hasNext()) {
					MetaFilialVO metaFilialVO = (MetaFilialVO)itMetas.next();
					if (metaFilialVO != null && metaFilialVO.getIdUnidadeMeta().intValue() == Constantes.UNIDADE_PERCENTUAL) {
						metaFilialVO.setValorMeta(metaFilialVO.getPercentualMeta());				
					}
					metasPercentual.add(metaFilialVO);
				}
			}
			return metasPercentual;
		} finally {
			metaFilialDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém meta de filial
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public MetaFilialVO obtemMetaFilial(Integer mes, Integer ano, Integer idEmpresa, Integer idFilial, Integer idIndicador) throws SRVException {
		
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		try {
			MetaFilialVO metaFilialVO = metaFilialDAO.obtemMetaFilial(mes, ano, idEmpresa, idFilial, idIndicador);
			if (metaFilialVO != null && metaFilialVO.getIdUnidadeMeta().intValue() == Constantes.UNIDADE_PERCENTUAL) {
				metaFilialVO.setValorMeta(metaFilialVO.getPercentualMeta());				
			}
			return metaFilialVO;
		} finally {
			metaFilialDAO.closeConnection();
		}
	}	
	
	
	
	/**
	 * Inclui meta de filial
	 * 
	 * @param metaFilialVO
	 * @return
	 * @throws SRVException
	 */
	public void incluiMetaFilial(MetaFilialVO metaFilialVO) throws SRVException {
		
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		try {
			
			if (metaFilialVO.getIdUnidadeMeta().intValue() == Constantes.UNIDADE_PERCENTUAL) {
				metaFilialVO.setPercentualMeta(metaFilialVO.getValorMeta());
				metaFilialVO.setValorMeta(new Double(0));
			}			
			
			metaFilialDAO.incluiMetaFilial(metaFilialVO);
		} finally {
			metaFilialDAO.closeConnection();
		}
	}	
	
	
	
	/**
	 * Altera meta de filial
	 * 
	 * @param metaFilialVO
	 * @return
	 * @throws SRVException
	 */
	public void alteraMetaFilial(MetaFilialVO metaFilialVO) throws SRVException {
		
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		try {
			metaFilialDAO.beginTrans();
			
			MetaFilialVO metaFilialVOAntigo = metaFilialDAO.obtemMetaFilial(metaFilialVO.getMes(), metaFilialVO.getAno(), metaFilialVO.getIdEmpresa(), metaFilialVO.getIdFilial(), metaFilialVO.getIdIndicador());
			if (metaFilialVOAntigo != null) {
				metaFilialDAO.incluiMetaFilialHistorico(metaFilialVOAntigo);
				metaFilialDAO.excluiMetaFilial(metaFilialVO.getMes(), metaFilialVO.getAno(), metaFilialVO.getIdEmpresa(), metaFilialVO.getIdFilial(), metaFilialVO.getIdIndicador());
			}
			
			if (metaFilialVO.getIdUnidadeMeta().intValue() == Constantes.UNIDADE_PERCENTUAL) {
				metaFilialVO.setPercentualMeta(metaFilialVO.getValorMeta());
				metaFilialVO.setValorMeta(new Double(0));
			}
			
			metaFilialDAO.incluiMetaFilial(metaFilialVO);
			metaFilialDAO.commitTrans();
			
		} catch (SRVException e) {
			metaFilialDAO.rollbackTrans();
			throw e;
			
		} finally {
			metaFilialDAO.closeConnection();
		}
	}
	
	
	/**
	 * Exclui meta de filial
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public void excluiMetaFilial(Integer mes, Integer ano, Integer idEmpresa, Integer idFilial, Integer idIndicador) throws SRVException {
		
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		try {
			metaFilialDAO.beginTrans();
			
			MetaFilialVO metaFilialVOAntigo = metaFilialDAO.obtemMetaFilial(mes, ano, idEmpresa, idFilial, idIndicador);
			if (metaFilialVOAntigo != null) {
				metaFilialDAO.incluiMetaFilialHistorico(metaFilialVOAntigo);
				metaFilialDAO.excluiMetaFilial(mes, ano, idEmpresa, idFilial, idIndicador);
			}
			metaFilialDAO.commitTrans();
			
		} catch (SRVException e) {
			metaFilialDAO.rollbackTrans();
			throw e;
			
		} finally {
			metaFilialDAO.closeConnection();
		}
	}
	
	
	/**
	 * Altera metas de filiais
	 * 
	 * @param metas
	 * @return Erros ocorridos na alteração
	 * @throws SRVException
	 */
	public List alteraMetaFilial(List metas) throws SRVException {
		
		MetaFilialDAO metaFilialDAO = new MetaFilialDAO();
		List erros = new ArrayList();
		
		try {
			metaFilialDAO.beginTrans();
			
			if (metas != null && metas.size() > 0) {
				
				Iterator itMetas = metas.iterator();
				while (itMetas.hasNext()) {
					
					MetaFilialVO metaFilialVO = (MetaFilialVO)itMetas.next();
					
					boolean podeProcessar = true;
					List filiais = FilialBusiness.getInstance().obtemListaFiliais(metaFilialVO.getIdEmpresa().intValue(), metaFilialVO.getIdFilial().intValue());
					if (filiais == null || filiais.size() == 0) {
						erros.add("Linha " + metaFilialVO.getNroLinha() + ": A Filial " + metaFilialVO.getIdFilial().intValue() + " não foi encontrada.");
						log.debug("Linha " + metaFilialVO.getNroLinha() + ": A Filial " + metaFilialVO.getIdFilial().intValue() + " não foi encontrada.");
						podeProcessar = false;
					}
					
					if (metaFilialVO.getMes().intValue() < 1 || metaFilialVO.getMes().intValue() > 12) { 
						erros.add("Linha " + metaFilialVO.getNroLinha() + ": O mês " + metaFilialVO.getMes().intValue() + " está inválido.");
						log.debug("Linha " + metaFilialVO.getNroLinha() + ": O mês " + metaFilialVO.getMes().intValue() + " está inválido.");
						podeProcessar = false;
					}
					
					if (metaFilialVO.getAno().intValue() < 2000 || metaFilialVO.getAno().intValue() > 2100) { 
						erros.add("Linha " + metaFilialVO.getNroLinha() + ": O ano " + metaFilialVO.getAno().intValue() + " está inválido.");
						log.debug("Linha " + metaFilialVO.getNroLinha() + ": O ano " + metaFilialVO.getAno().intValue() + " está inválido.");
						podeProcessar = false;
					}
					
					DadosIndicadorVO dadosIndicadorVO = IndicadorBusiness.getInstance().obtemIndicador(metaFilialVO.getIdIndicador().intValue());
					if (dadosIndicadorVO == null) {
						erros.add("Linha " + metaFilialVO.getNroLinha() + ": O indicador " + metaFilialVO.getIdIndicador().intValue() + " não foi encontrado.");
						log.debug("Linha " + metaFilialVO.getNroLinha() + ": O indicador " + metaFilialVO.getIdIndicador().intValue() + " não foi encontrado.");
						podeProcessar = false;
					}
					
					if (metaFilialVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_PERCENTUAL && 
						metaFilialVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_PONTOS     && 
						metaFilialVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_UNIDADE    &&
						metaFilialVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_PECAS_TKT  &&
						metaFilialVO.getIdUnidadeMeta().intValue() != Constantes.UNIDADE_VALOR) {								
						erros.add("Linha " + metaFilialVO.getNroLinha() + ": A unidade " + metaFilialVO.getIdUnidadeMeta().intValue() + " está inválida.");
						log.debug("Linha " + metaFilialVO.getNroLinha() + ": A unidade " + metaFilialVO.getIdUnidadeMeta().intValue() + " está inválida.");
						podeProcessar = false;
					}
					
					if (podeProcessar) {
					
						MetaFilialVO metaFilialVOAntigo = metaFilialDAO.obtemMetaFilial(metaFilialVO.getMes(), metaFilialVO.getAno(), metaFilialVO.getIdEmpresa(), metaFilialVO.getIdFilial(), metaFilialVO.getIdIndicador());
						if (metaFilialVOAntigo != null) {
							metaFilialDAO.incluiMetaFilialHistorico(metaFilialVOAntigo);
							metaFilialDAO.excluiMetaFilial(metaFilialVO.getMes(), metaFilialVO.getAno(), metaFilialVO.getIdEmpresa(), metaFilialVO.getIdFilial(), metaFilialVO.getIdIndicador());
						}
						
						if (metaFilialVO.getIdUnidadeMeta().intValue() == Constantes.UNIDADE_PERCENTUAL) {
							metaFilialVO.setPercentualMeta(metaFilialVO.getValorMeta());
							metaFilialVO.setValorMeta(new Double(0));
						}
						
						metaFilialDAO.incluiMetaFilial(metaFilialVO);
					}
				}
			}

			metaFilialDAO.commitTrans();
			//metaFilialDAO.rollbackTrans();
			return erros;
			
		} catch (SRVException e) {
			metaFilialDAO.rollbackTrans();
			throw e;
			
		} finally {
			metaFilialDAO.closeConnection();
		}
	}
	
	
	/**
	 * Obtém acomp. de metas de filiais
	 * 
	 * @param mes
	 * @param ano
	 * @param idFilial
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws SRVException
	 */
	public List obtemAcompMetasFiliais(Integer mes, Integer ano, Integer idFilial, Integer idIndicador, String descricaoIndicador, Boolean apenasNaoRealizadas) throws SRVException {
		
		int idFilialInt = -1;
		if (idFilial != null) {
			idFilialInt = idFilial.intValue();
		}
		
		List filiais = FilialBusiness.getInstance().obtemListaFiliais(Constantes.CODIGO_EMPRESA, idFilialInt, Boolean.TRUE);
		
		List tiposRemuneracao = new ArrayList();
		tiposRemuneracao.add(Constantes.ID_TIPO_REM_VAR_REMUNERACAO_LOJA);
		
		DadosIndicadorVO diVO = new DadosIndicadorVO();
		diVO.setIdIndicador(idIndicador);
		diVO.setDescricaoIndicador(descricaoIndicador);
		List indicadores = IndicadorBusiness.getInstance().obtemListaIndicadoresPorTipo(diVO, tiposRemuneracao, false);
		List metasFilais = obtemMetasFiliais(mes, ano, idFilial, idIndicador, descricaoIndicador);
		
		//Hash de Metas de Filais
		HashMap metasFiliaisHT = new HashMap();
		if (ObjectHelper.isNotEmpty(metasFilais)) {
			Iterator itMetasFilais = metasFilais.iterator();
			while (itMetasFilais.hasNext()) {
				MetaFilialVO metaFilialVO = (MetaFilialVO)itMetasFilais.next();
				metasFiliaisHT.put(metaFilialVO.getIdIndicador() + "-" + metaFilialVO.getIdFilial(), metaFilialVO);
			}
		}
		
		List metas = new ArrayList();
		
		if (ObjectHelper.isNotEmpty(filiais)) {
			Iterator itFiliais = filiais.iterator();
			while (itFiliais.hasNext()) {
				FilialVO filialVO = (FilialVO)itFiliais.next();
				
				if (ObjectHelper.isNotEmpty(indicadores)) {
					Iterator itIndicadores = indicadores.iterator();
					while (itIndicadores.hasNext()) {
						DadosIndicadorVO dadosIndicadorVO = (DadosIndicadorVO)itIndicadores.next();
						
						AcompMetaFilialVO acompMetaFilialVO = new AcompMetaFilialVO();
						acompMetaFilialVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
						acompMetaFilialVO.setIdFilial(filialVO.getCodFilial());
						acompMetaFilialVO.setDescricaoFilial(filialVO.getDescricao());
						acompMetaFilialVO.setIdIndicador(dadosIndicadorVO.getIdIndicador());
						acompMetaFilialVO.setDescricaoIndicador(dadosIndicadorVO.getDescricaoIndicador());
						if (metasFiliaisHT.containsKey(dadosIndicadorVO.getIdIndicador() + "-" + filialVO.getCodFilial())) {
							
							if (apenasNaoRealizadas != null && apenasNaoRealizadas.booleanValue()) {
								continue;
							}							
							
							MetaFilialVO metaFilialVO = (MetaFilialVO)metasFiliaisHT.get(dadosIndicadorVO.getIdIndicador() + "-" + filialVO.getCodFilial());
							acompMetaFilialVO.setIdUnidadeMeta(metaFilialVO.getIdUnidadeMeta());
							if (metaFilialVO.getIdUnidadeMeta().intValue() == Constantes.UNIDADE_PERCENTUAL) {
								acompMetaFilialVO.setValorMeta(metaFilialVO.getPercentualMeta());
							} else {
								acompMetaFilialVO.setValorMeta(metaFilialVO.getValorMeta());
							}
							acompMetaFilialVO.setTemMeta(Boolean.TRUE);
							
						}
						metas.add(acompMetaFilialVO);
						
					}
				}
			}
		}
		
		return metas;
	}	
}