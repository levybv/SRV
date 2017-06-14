package br.com.marisa.srv.tlmkt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.tlmkt.business.TelemarketingBusiness;
import br.com.marisa.srv.tlmkt.vo.TelemarketingIndicadorVO;
import br.com.marisa.srv.tlmkt.vo.TelemarketingVO;

/**
 * @author Levy Villar
 */
public class TelemarketingAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_TELEMARKETING = "telemarketing";

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

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_TLMKT);
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_TLMKT_ALTERACAO));

		String periodo = getStringParam(pRequest, "periodoF");
		if (ObjectHelper.isNotNull(periodo)) {
			TelemarketingVO telemarketingVO = TelemarketingBusiness.getInstance().obtemTlmktElegivel(DataHelper.obtemNumAno(periodo, DataHelper.PATTERN_DATE_MES_ANO), DataHelper.obtemNumMes(periodo, DataHelper.PATTERN_DATE_MES_ANO), null);
			List<TelemarketingVO> listaTlmktVO = new ArrayList<TelemarketingVO>();
			listaTlmktVO.add(telemarketingVO);
			pRequest.setAttribute("listaTlmktVO", listaTlmktVO);
		}

		pRequest.setAttribute("periodoLista", DataHelper.listaUltimosPeriodosMesAno(-6));
		pRequest.setAttribute("periodoFiltro", getStringParam(pRequest, "periodoF"));

		return pMapping.findForward(TelemarketingAction.FORWARD_TELEMARKETING);		
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
	public ActionForward incluiTelemarketing(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			TelemarketingVO tlmktVO = new TelemarketingVO();
			tlmktVO.setAno(DataHelper.obtemNumAno(getStringParam(pRequest, "periodoI"), DataHelper.PATTERN_DATE_MES_ANO));
			tlmktVO.setMes(DataHelper.obtemNumMes(getStringParam(pRequest, "periodoI"), DataHelper.PATTERN_DATE_MES_ANO));
			String[] elegiveisSelecionados = pRequest.getParameterValues("elegiveisSelecionadosI");
			List<FuncionarioVO> listaFuncionario = new ArrayList<FuncionarioVO>();
	        for (String codFuncStr : elegiveisSelecionados) {
	        	listaFuncionario.add(new FuncionarioVO(Long.parseLong(codFuncStr)));
			}
	        tlmktVO.setListaFuncionarioVO(listaFuncionario);
			tlmktVO.setUsuarioVO(obtemUsuarioDaSessao(pRequest).getUsuarioVO());

			DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
			pesquisaVO.setAtivo(Constantes.CD_VERDADEIRO);
			pesquisaVO.setIdGrupoIndicador(Constantes.COD_GRUPO_INDIC_TLMKT);
			List<DadosIndicadorVO> listaIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadores(pesquisaVO, null, Boolean.FALSE, Boolean.FALSE);

			List<TelemarketingIndicadorVO> listaTlmktIndicVO = new ArrayList<TelemarketingIndicadorVO>();
			for (DadosIndicadorVO dadosIndicadorVO : listaIndicadores) {
				TelemarketingIndicadorVO tlmktIndicVO = new TelemarketingIndicadorVO();
				tlmktIndicVO.setCodIndicador(dadosIndicadorVO.getIdIndicador());
				tlmktIndicVO.setNumMeta(getDoubleParam(pRequest, "indicMeta_"+dadosIndicadorVO.getIdIndicador().intValue(), Boolean.TRUE));
				tlmktIndicVO.setCodUnMeta(Constantes.UNIDADE_UNIDADE);
				tlmktIndicVO.setNumRealizado(getDoubleParam(pRequest, "indicRealz_"+dadosIndicadorVO.getIdIndicador().intValue(), Boolean.TRUE));
				tlmktIndicVO.setCodUnRealizado(Constantes.UNIDADE_UNIDADE);
				listaTlmktIndicVO.add(tlmktIndicVO);
			}
			tlmktVO.setListaIndicadorTlmktVO(listaTlmktIndicVO);

			TelemarketingBusiness.getInstance().excluiTlmktElegivel(tlmktVO.getAno(), tlmktVO.getMes());
			TelemarketingBusiness.getInstance().incluiTlmktElegivel(tlmktVO);
			setMensagem(pRequest,"Inclusão de telemarketing realizada com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

}