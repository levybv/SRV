package br.com.marisa.srv.bonus.business;

import java.util.ArrayList;
import java.util.List;

import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.bonus.engine.BonusAnualEngine;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.NumeroHelper;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class RealizFuncIndicadorBusinessAjax {

	/**
	 * 
	 * @param idFuncionario
	 * @param idIndicador
	 * @param novoRealizado
	 * @return
	 * @throws SRVException
	 */
	public IndicadorFuncionarioRealizadoVO simulaAtingimento(Long idFuncionario, Integer idIndicador, Double realizadoXMeta) throws SRVException {

		IndicadorFuncionarioRealizadoVO retornoVO = null;

		ConfiguracaoBonusVO configuracaoBonusVO = BonusAnualEngine.getInstance().obtemParametroAtivoBonus();
		Integer ano = DataHelper.obtemNumAno(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO);
		Integer mes = DataHelper.obtemNumMes(configuracaoBonusVO.getPeriodoDisponivel(), DataHelper.PATTERN_DATE_MES_ANO);

		List<IndicadorFuncionarioRealizadoVO> listaRealizado = RealizFuncIndicadorBusiness.getInstance().obtemListaRealizadoFuncIndicador(idFuncionario, null, Constantes.CODIGO_EMPRESA, ano, mes, Constantes.DESCR_TIPO_REM_VAR_CORPORATIVO);
		for (IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO : listaRealizado) {
			if (indicadorFuncionarioRealizadoVO.getIdIndicador().intValue() == idIndicador.intValue()) {
				indicadorFuncionarioRealizadoVO.setRealizadoXMeta(realizadoXMeta);
				BonusAnualEngine.getInstance().calculaRealizadoPonderacao(indicadorFuncionarioRealizadoVO);
				retornoVO = indicadorFuncionarioRealizadoVO;
			}
		}
		return retornoVO;
	}

	/**
	 * 
	 * @param valor
	 * @return
	 * @throws SRVException
	 */
	public List<String> calculaDesempenho(Long idFuncionario, Double desempenhoTotal) throws SRVException {

		Double desempenhoTotalLimitado = desempenhoTotal > Constantes.CEM.doubleValue() ? Constantes.CEM.doubleValue() : desempenhoTotal;
		boolean isAtingiu = desempenhoTotalLimitado >= 80 ? true : false; 

		String desempenhoFormatado = "";
		if (isAtingiu) {
			desempenhoFormatado = "<font color=\"black\">" + NumeroHelper.formataNumero(desempenhoTotalLimitado, Constantes.UNIDADE_PERCENTUAL) + "</font>";
		} else {
			desempenhoFormatado = "<font color=\"red\">" + NumeroHelper.formataNumero(desempenhoTotalLimitado, Constantes.UNIDADE_PERCENTUAL) + "</font>";
		}

		List<String> retornoTela = new ArrayList<String>();
		retornoTela.add(desempenhoFormatado);

		return retornoTela;
	}

}