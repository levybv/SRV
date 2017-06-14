package br.com.marisa.srv.bonus.business;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.bonus.dao.ContratoMetaAceiteDAO;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.bonus.vo.ContratoMetaAceiteVO;
import br.com.marisa.srv.geral.enumeration.StatusContratoMetaAceiteEnum;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.DataHelper;

/**
 * 
 * @author levy.villar
 * 
 */
public class ContratoMetaAceiteBusiness {

	private static ContratoMetaAceiteBusiness instance = new ContratoMetaAceiteBusiness();

	/**
	 * 
	 * @return
	 */
	public static final ContratoMetaAceiteBusiness getInstance() {
		return instance;
	}

	/**
	 * 
	 */
	private ContratoMetaAceiteBusiness() {
	}

	/**
	 * 
	 * @param codFuncionario
	 * @param anoBonus
	 * @return
	 * @throws SRVException
	 */
	public ContratoMetaAceiteVO obtemContratoMetaAceite(ContratoMetaAceiteVO aceiteVO) throws SRVException {
		ContratoMetaAceiteVO statusAceiteVO = new ContratoMetaAceiteVO();

		ContratoMetaAceiteDAO contratoMetaAceiteDAO = new ContratoMetaAceiteDAO();

		try {

			ContratoMetaAceiteVO resultadoAceiteVO = contratoMetaAceiteDAO.obtemContratoMetaAceite(aceiteVO);
			if (resultadoAceiteVO != null) {
				statusAceiteVO.setDescStatusAceite("Aceite eletrônico em: " + DataHelper.formataDataHora(resultadoAceiteVO.getDataAceite()));
				statusAceiteVO.setIdStatusAceite(StatusContratoMetaAceiteEnum.ELETRONICO.getCodigo());
			} else {
				
				ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemConfiguracaoBonus(aceiteVO.getNumAno());
				if (DataHelper.obtemDataAtualDiaMesAno().before(configuracaoBonusVO.getDataLimiteAceite()) || DataHelper.obtemDataAtualDiaMesAno().compareTo(configuracaoBonusVO.getDataLimiteAceite()) == 0) {
					statusAceiteVO.setDescStatusAceite("Data limite para aceite: " + DataHelper.formataData(configuracaoBonusVO.getDataLimiteAceite()));
					statusAceiteVO.setIdStatusAceite(StatusContratoMetaAceiteEnum.EM_ABERTO.getCodigo());
				} else {
					statusAceiteVO.setDescStatusAceite("Aceite automático em: " + DataHelper.formataData(configuracaoBonusVO.getDataLimiteAceite()));
					statusAceiteVO.setIdStatusAceite(StatusContratoMetaAceiteEnum.AUTOMATICO.getCodigo());
				}
			}

		} catch (Exception ex) {
			throw new SRVException("Ocorreu um erro ao obter a data limite aceite!");
		} finally {
			contratoMetaAceiteDAO.closeConnection();
		}
		return statusAceiteVO;
	}

	/**
	 * 
	 * @param bonusAceiteVO
	 * @throws SRVException
	 */
	public void incluiContratoMetaAceite(ContratoMetaAceiteVO bonusAceiteVO) throws SRVException {
		ContratoMetaAceiteDAO dao = new ContratoMetaAceiteDAO();
		try {
			dao.incluiContratoMetaAceite(bonusAceiteVO);
		} finally {
			dao.closeConnection();
		}

	}

}