package br.com.marisa.srv.filial.realizado.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.filial.realizado.business.RealizadoFilialIndicBusiness;
import br.com.marisa.srv.filial.realizado.vo.RealizadoFilialIndicVO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.DataHelper;

/**
 * 
 * @author Levy Villar
 *
 */
public class RealizadoFilialIndicAction extends BasicAction {

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
		pRequest.removeAttribute("listaFilial");
		pRequest.setAttribute("listaFilial", FilialBusiness.getInstance().obtemTodasFiliais(true));

		return pMapping.findForward(RealizadoFilialIndicAction.PAGINA_INICIAL);
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
	public ActionForward executarFiltro(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_REALIZADO_FILIAL);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_REALIZADO_FILIAL_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_REALIZADO_FILIAL_ALTERACAO));

		pRequest.removeAttribute("listaFilial");

		Integer idFilial = getIntegerParam(pRequest, "idFilialF");
		String periodo = getStringParam(pRequest, "periodoF");

		RealizadoFilialIndicVO pesquisaVO = new RealizadoFilialIndicVO();
		pesquisaVO.setAno(DataHelper.obtemNumAno(periodo, DataHelper.PATTERN_DATE_MES_ANO));
		pesquisaVO.setMes(DataHelper.obtemNumMes(periodo, DataHelper.PATTERN_DATE_MES_ANO));
		FilialVO filialVO = new FilialVO();
		filialVO.setCodFilial(idFilial);
		filialVO.setCodEmpresa(Constantes.CODIGO_EMPRESA);
		pesquisaVO.setFilialVO(filialVO);
		List<RealizadoFilialIndicVO> listaRealFilIndic = RealizadoFilialIndicBusiness.getInstance().obtemListaRealizadoFilialIndic(pesquisaVO);

		pRequest.setAttribute("periodoFiltro", periodo);
		pRequest.setAttribute("listaFilial", FilialBusiness.getInstance().obtemTodasFiliais(true));
		pRequest.setAttribute("idFilialF",idFilial);
		pRequest.setAttribute("listaRealFilIndic", listaRealFilIndic);

		return pMapping.findForward(RealizadoFilialIndicAction.PAGINA_INICIAL);
	}

}