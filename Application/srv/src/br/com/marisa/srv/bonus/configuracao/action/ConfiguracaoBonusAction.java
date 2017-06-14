package br.com.marisa.srv.bonus.configuracao.action;

import java.util.ArrayList;
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
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.processo.action.ProcessoPeriodoAction;

/**
 * 
 * @author Levy Villar
 *
 */
public class ConfiguracaoBonusAction extends BasicAction {

	private static final String FORWARD_CONFIGURACAO_BONUS = "configuracaoBonus";

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

	/**
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
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_CONFIGURACAO_BONUS);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CONFIGURACAO_BONUS_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CONFIGURACAO_BONUS_ALTERACAO));

		ConfiguracaoBonusVO configuracaoBonusVO = new ConfiguracaoBonusVO();
		configuracaoBonusVO.setAno(getIntegerParam(pRequest, "anoF"));
		if (getStringParam(pRequest, "encerradoF")!=null) {
			configuracaoBonusVO.setIsEncerrado(getBooleanParam(pRequest, "encerradoF"));
		}

		List<ConfiguracaoBonusVO> listaConfiguracaoBonusVO = ConfiguracaoBonusBusiness.getInstance().obtemListaConfiguracaoBonus(configuracaoBonusVO);

		pRequest.setAttribute("listaConfiguracaoBonusVO", listaConfiguracaoBonusVO);
		pRequest.setAttribute("configuracaoBonusVO", configuracaoBonusVO);

		return pMapping.findForward(ConfiguracaoBonusAction.FORWARD_CONFIGURACAO_BONUS);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward incluiConfiguracaoBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			ConfiguracaoBonusVO incluiVO = new ConfiguracaoBonusVO();
			incluiVO.setAno(getIntegerParam(pRequest, "anoBonusI"));
			incluiVO.setIsEncerrado(getBooleanParam(pRequest, "flgEncerradoI"));
			incluiVO.setIdFuncionarioCorporativo(getLongParam(pRequest, "idFuncCorpI"));
			incluiVO.setCodIndicIni(getIntegerParam(pRequest, "codIndicIniI"));
			incluiVO.setCodIndicFim(getIntegerParam(pRequest, "codIndicFimI"));
			incluiVO.setIsFunding(getBooleanParam(pRequest, "flgFundingI"));
			incluiVO.setFunding(getDoubleParam(pRequest, "fundingI", true));
			incluiVO.setIsContratoMeta(getBooleanParam(pRequest, "flgContratoMetaI"));
			incluiVO.setDataLimiteAceite(getDateParam(pRequest, "dataLimiteAceiteI"));
			incluiVO.setTextoConsentimento(getStringParam(pRequest, "textoConsentimentoI"));
			incluiVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			String periodoSelecionado = getStringParam(pRequest, "periodoI");
			if (ObjectHelper.isNotEmpty(periodoSelecionado)) {
				incluiVO.setPeriodoDisponivel(periodoSelecionado.substring(periodoSelecionado.length()-2) + "/" + periodoSelecionado.substring(0, 4));
			}

			String[] escalasSelecionadas = pRequest.getParameterValues("escalasSelecionadasI");
			List<EscalaVO> listaEscala = new ArrayList<EscalaVO>();
	        for (String codEscala : escalasSelecionadas) {
	        	EscalaVO escalaVO = new EscalaVO();
	        	escalaVO.setIdEscala(Integer.parseInt(codEscala));
	        	listaEscala.add(escalaVO);
			}
	        incluiVO.setListaEscala(listaEscala);

	        ConfiguracaoBonusBusiness.getInstance().incluiConfiguracaoBonus(incluiVO);
			setMensagem(pRequest, "Configuração de Bônus incluída com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward alteraConfiguracaoBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			ConfiguracaoBonusVO incluiVO = new ConfiguracaoBonusVO();
			incluiVO.setAno(getIntegerParam(pRequest, "anoBonusA"));
			incluiVO.setIsEncerrado(getBooleanParam(pRequest, "flgEncerradoA"));
			incluiVO.setIdFuncionarioCorporativo(getLongParam(pRequest, "idFuncCorpA"));
			incluiVO.setCodIndicIni(getIntegerParam(pRequest, "codIndicIniA"));
			incluiVO.setCodIndicFim(getIntegerParam(pRequest, "codIndicFimA"));
			incluiVO.setIsFunding(getBooleanParam(pRequest, "flgFundingA"));
			incluiVO.setFunding(getDoubleParam(pRequest, "fundingA", true));
			incluiVO.setIsContratoMeta(getBooleanParam(pRequest, "flgContratoMetaA"));
			incluiVO.setDataLimiteAceite(getDateParam(pRequest, "dataLimiteAceiteA"));
			incluiVO.setTextoConsentimento(getStringParam(pRequest, "textoConsentimentoA"));
			incluiVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			String periodoSelecionado = getStringParam(pRequest, "periodoA");
			if (ObjectHelper.isNotEmpty(periodoSelecionado)) {
				incluiVO.setPeriodoDisponivel(periodoSelecionado.substring(periodoSelecionado.length()-2) + "/" + periodoSelecionado.substring(0, 4));
			}

			String[] escalasSelecionadas = pRequest.getParameterValues("escalasSelecionadasA");
			List<EscalaVO> listaEscala = new ArrayList<EscalaVO>();
	        for (String codEscala : escalasSelecionadas) {
	        	EscalaVO escalaVO = new EscalaVO();
	        	escalaVO.setIdEscala(Integer.parseInt(codEscala));
	        	listaEscala.add(escalaVO);
			}
	        incluiVO.setListaEscala(listaEscala);

	        //ConfiguracaoBonusBusiness.getInstance().incluiConfiguracaoBonus(incluiVO);
			setMensagem(pRequest, "Configuração de Bônus alterada com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
	}

}