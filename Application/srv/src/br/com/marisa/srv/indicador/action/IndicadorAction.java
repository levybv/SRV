package br.com.marisa.srv.indicador.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.bonus.configuracao.business.ConfiguracaoBonusBusiness;
import br.com.marisa.srv.bonus.configuracao.vo.ConfiguracaoBonusVO;
import br.com.marisa.srv.calendario.business.CalendarioBonusBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.grupoindicador.business.GrupoIndicadorBusiness;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;

/**
 * Action para tratar o CRUD de indicadores
 */
public class IndicadorAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_INDICADOR = "indicador";

	
	/**
	 * Realiza a consulta dos indicadores
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}
	
	
	/**
	 * Monta os dados e chama a tela principal
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	private ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_INDICADOR);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_INDICADOR_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_INDICADOR_ALTERACAO));

		DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
		pesquisaVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorF"));
		pesquisaVO.setDescricaoIndicador(getStringParam(pRequest, "descricaoF"));
		pesquisaVO.setIdGrupoIndicador(getIntegerParam(pRequest, "grupoIndicadorF"));
		pesquisaVO.setDescricaoFonte(getStringParam(pRequest, "fonteF"));
		pesquisaVO.setDescricaoDiretoria(getStringParam(pRequest, "diretoriaF"));
		if (getStringParam(pRequest, "ativoF") != null) {
			pesquisaVO.setAtivo(getStringParam(pRequest, "ativoF"));
			pRequest.setAttribute("ativoF", pesquisaVO.getAtivo());
		}

		List<DadosIndicadorVO> listaIndicadores = new ArrayList<DadosIndicadorVO>();
		List<Integer> listCodGrpRemVar = null;
		int ano = -1;

		String periodoFiltro = getStringParam(pRequest, "periodoF");
		if (ObjectHelper.isNotEmpty(periodoFiltro)) {
			ano = Integer.parseInt(periodoFiltro);
			if (ano != -1) {
				ConfiguracaoBonusVO configuracaoBonusVO = ConfiguracaoBonusBusiness.getInstance().obtemConfiguracaoBonus(ano);
				if (configuracaoBonusVO != null) {
					pesquisaVO.setUtilizaRange(true);
					pesquisaVO.setCodIndicInicio(configuracaoBonusVO.getCodIndicIni());
					pesquisaVO.setCodIndicFim(configuracaoBonusVO.getCodIndicFim());
					listCodGrpRemVar = new ArrayList<Integer>();
					listCodGrpRemVar.add(Constantes.ID_TIPO_REM_VAR_CORPORATIVO);
				}
			}
		}
		listaIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadores(pesquisaVO, listCodGrpRemVar, (ano == Constantes.NUM_ANO_SUBINDICADOR), Boolean.TRUE);

		pRequest.setAttribute("idIndicadorF", pesquisaVO.getIdIndicador());
		pRequest.setAttribute("descricaoF", pesquisaVO.getDescricaoIndicador());
		pRequest.setAttribute("grupoIndicadorF", pesquisaVO.getIdGrupoIndicador());
		pRequest.setAttribute("fonteF", pesquisaVO.getDescricaoFonte());
		pRequest.setAttribute("diretoriaF", pesquisaVO.getDescricaoDiretoria());
		pRequest.setAttribute("periodoF", periodoFiltro);
		pRequest.setAttribute("grupoIndicadores", GrupoIndicadorBusiness.getInstance().obtemGruposIndicadores());
		pRequest.setAttribute("listaFonte", IndicadorBusiness.getInstance().obtemListaFonte());
		pRequest.setAttribute("listaDiretoria", IndicadorBusiness.getInstance().obtemListaDiretoria());
		pRequest.setAttribute("listaPeriodo", CalendarioBonusBusiness.getInstance().obtemAnosCalendarioBonus());
		pRequest.setAttribute("indicadores", listaIndicadores);

		return pMapping.findForward(FORWARD_INDICADOR);
	}
	

	/**
	 * Efetiva alteração
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward alteraIndicador(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			DadosIndicadorVO dadosIndicadorVO = new DadosIndicadorVO();
			dadosIndicadorVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorID"));
			dadosIndicadorVO.setDescricaoIndicador(getStringParam(pRequest, "descricaoID"));
			dadosIndicadorVO.setFormulaConceito(getStringParam(pRequest, "formulaConceitoID"));
			dadosIndicadorVO.setFormulaIndicador(getStringParam(pRequest, "formulaID"));
			dadosIndicadorVO.setIdGrupoIndicador(getIntegerParam(pRequest, "idGrupoID"));
			dadosIndicadorVO.setVerbaRH(getIntegerParam(pRequest, "verbaRHID"));
			dadosIndicadorVO.setFlgSentido(getStringParam(pRequest, "sentidoIndicadorID"));
			dadosIndicadorVO.setAtivo(getStringParam(pRequest, "ativoID"));
			dadosIndicadorVO.setIdEscala(getIntegerParam(pRequest, "idEscalaID"));
			dadosIndicadorVO.setDescricaoFonte(getStringParam(pRequest, "fonteID"));
			dadosIndicadorVO.setDescricaoDiretoria(getStringParam(pRequest, "diretoriaID"));
			dadosIndicadorVO.setDataUltimaAlteracao(new Date());
			dadosIndicadorVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			if (getBooleanParam(pRequest, "flg_Preench_Ating_igual_RealzID").booleanValue()) {
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz("S"); 
			} else {
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz("N"); 
			}

			if (getBooleanParam(pRequest, "checkOutraFonteID").booleanValue()) {
				dadosIndicadorVO.setDescricaoFonte(getStringParam(pRequest, "outraFonteID")); 
			} else {
				dadosIndicadorVO.setDescricaoFonte(getStringParam(pRequest, "fonteID"));
			}
			
			IndicadorBusiness.getInstance().alteraIndicador(dadosIndicadorVO);
			setMensagem(pRequest, "Indicador alterado com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(PAGINA_INICIAL);
	}

	
	/**
	 * Efetiva inclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward incluiIndicador(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			DadosIndicadorVO dadosIndicadorVO = new DadosIndicadorVO();
			dadosIndicadorVO.setDescricaoIndicador(getStringParam(pRequest, "descricaoID"));
			dadosIndicadorVO.setFormulaConceito(getStringParam(pRequest, "formulaConceitoID"));
			dadosIndicadorVO.setFormulaIndicador(getStringParam(pRequest, "formulaID"));
			dadosIndicadorVO.setFlgSentido(getStringParam(pRequest, "sentidoIndicadorID"));
			dadosIndicadorVO.setIdGrupoIndicador(getIntegerParam(pRequest, "idGrupoID"));
			dadosIndicadorVO.setVerbaRH(getIntegerParam(pRequest, "verbaRHID"));
			dadosIndicadorVO.setAtivo(getStringParam(pRequest, "ativoID"));
			dadosIndicadorVO.setIdEscala(getIntegerParam(pRequest, "idEscalaID"));
			dadosIndicadorVO.setDescricaoFonte(getStringParam(pRequest, "fonteID"));
			dadosIndicadorVO.setDescricaoDiretoria(getStringParam(pRequest, "diretoriaID"));
			dadosIndicadorVO.setDataUltimaAlteracao(new Date());
			dadosIndicadorVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			if (getBooleanParam(pRequest, "flg_Preench_Ating_igual_RealzID").booleanValue()) {
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz("S"); 
			} else {
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz("N"); 
			}			

			if (getBooleanParam(pRequest, "checkOutraFonteID").booleanValue()) {
				dadosIndicadorVO.setDescricaoFonte(getStringParam(pRequest, "outraFonteID")); 
			} else {
				dadosIndicadorVO.setDescricaoFonte(getStringParam(pRequest, "fonteID"));
			}

			if (IndicadorBusiness.getInstance().existeIndicadorMesmoNome(dadosIndicadorVO.getDescricaoIndicador())) {
				setMensagemErro(pRequest, "Descrição do Indicador já existe!");
			} else {
				IndicadorBusiness.getInstance().incluiIndicador(dadosIndicadorVO);
				setMensagem(pRequest, "Indicador incluído com sucesso");
			}
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(PAGINA_INICIAL);
	}
}