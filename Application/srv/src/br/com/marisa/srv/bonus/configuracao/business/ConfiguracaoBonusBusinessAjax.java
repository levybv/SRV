package br.com.marisa.srv.bonus.configuracao.business;

import java.util.List;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 */
public class ConfiguracaoBonusBusinessAjax {

	/**
	 * 
	 * @param numAno
	 * @return
	 */
	public boolean isJaExisteConfiguracaoBonus(Integer numAno) {
		boolean isJaExiste = false;
		if (ObjectHelper.isNotEmpty(numAno)) {
			try {
				ConfiguracaoBonusVO vo = ConfiguracaoBonusBusiness.getInstance().obtemConfiguracaoBonus(numAno);
				if (vo != null) {
					isJaExiste = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isJaExiste;
	}

	/**
	 * 
	 * @param codIndic
	 * @param ano
	 * @return
	 */
	public boolean isRangeIndicadoresJaExiste(Integer codIndic, Integer ano) {
		boolean isRangeIndicadoresJaExiste = false;
		if (ObjectHelper.isNotEmpty(codIndic)) {
			try {
				List<ConfiguracaoBonusVO> listaConfigBonusVO = ConfiguracaoBonusBusiness.getInstance().obtemListaConfiguracaoBonus(null);
				for (ConfiguracaoBonusVO configuracaoBonusVO : listaConfigBonusVO) {
					if (codIndic.intValue() >= configuracaoBonusVO.getCodIndicIni().intValue() && codIndic.intValue() <= configuracaoBonusVO.getCodIndicFim().intValue()) {
						if (ano==null || ano!=configuracaoBonusVO.getAno().intValue()) {
							isRangeIndicadoresJaExiste = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isRangeIndicadoresJaExiste;
	}

	/**
	 * 
	 * @param ano
	 * @return
	 */
	public ConfiguracaoBonusVO obtemConfiguracaoBonus(Integer ano) {
		ConfiguracaoBonusVO configuracaoBonusVO = null;
		try {
			configuracaoBonusVO = ConfiguracaoBonusBusiness.getInstance().obtemConfiguracaoBonus(ano);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configuracaoBonusVO;
	}
	
}