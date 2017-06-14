package br.com.marisa.srv.resultado.loja.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.MensagemVO;
import br.com.marisa.srv.resultado.loja.business.ResultadoLojaBusiness;
import br.com.marisa.srv.resultado.loja.vo.ResultadoVO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * 
 * @author levy.villar
 *
 */
public class ResultadoLojaAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_RESULTADO_LOJA = "resultadoLoja";

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

		pRequest.getSession(true).removeAttribute("mensagemVO");
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		pRequest.removeAttribute("mesF");
		pRequest.removeAttribute("anoF");
		pRequest.removeAttribute("idFuncF");
		pRequest.removeAttribute("idFilialF");
		pRequest.removeAttribute("listaFilial");
		pRequest.removeAttribute("tipoFilial");

		boolean isMaster = AcessoBusiness.getInstance().obtemAcessoPerfilFuncionalidadeTipoAcesso(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil(),
																									ConstantesFuncionalidades.FUNCIONALIDADE_RESULTADO_PPR_LOJA, 
																									ConstantesFuncionalidades.TIPO_ACESSO_RESULTADO_PPR_LOJA_MASTER);

		UsuarioVO usuarioVO = obtemUsuarioDaSessao(pRequest).getUsuarioVO();
		if (ObjectHelper.isNotEmpty(usuarioVO.getMatricula()) || isMaster) {
			if (isMaster) {
				pRequest.setAttribute("listaFilial", FilialBusiness.getInstance().obtemTodasFiliais(true));
			} else {
				pRequest.setAttribute("listaFilial", ResultadoLojaBusiness.getInstance().obtemListaFilialPorFuncionario(Long.parseLong(usuarioVO.getMatricula())));
			}
		}

		return pMapping.findForward(ResultadoLojaAction.FORWARD_RESULTADO_LOJA);
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

		int idFilial = getIntParam(pRequest, "idFilialF");
		Integer tabSelecionada = getIntParam(pRequest, "tabSelecionada") < 0 ? 1 : getIntParam(pRequest, "tabSelecionada");
		Integer tabAnterior = getIntParam(pRequest, "tabAnterior") < 0 ? 1 : getIntParam(pRequest, "tabAnterior");
		Long idFunc = tabSelecionada.intValue() == tabAnterior.intValue() ? getLongParam(pRequest, "idFuncF") : null;

		String periodo = getStringParam(pRequest, "periodoF");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		Calendar periodoCalendar = Calendar.getInstance();
		periodoCalendar.setTime(sdf.parse(periodo));
		int ano = periodoCalendar.get(Calendar.YEAR);
		int mes = periodoCalendar.get(Calendar.MONTH)+1;

		UsuarioVO usuarioVO = obtemUsuarioDaSessao(pRequest).getUsuarioVO();

		boolean isMaster = AcessoBusiness.getInstance().obtemAcessoPerfilFuncionalidadeTipoAcesso(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil(),
																									ConstantesFuncionalidades.FUNCIONALIDADE_RESULTADO_PPR_LOJA, 
																									ConstantesFuncionalidades.TIPO_ACESSO_RESULTADO_PPR_LOJA_MASTER);

		List<FilialVO> listaFilial = null;

		if (ObjectHelper.isNotEmpty(usuarioVO.getMatricula()) || isMaster) {

			if (isMaster) {
				listaFilial = FilialBusiness.getInstance().obtemTodasFiliais(true);
			} else {
				listaFilial = ResultadoLojaBusiness.getInstance().obtemListaFilialPorFuncionario(Long.parseLong(usuarioVO.getMatricula()));
			}

			if (tabSelecionada == 1) {
				//Loja
				List<ResultadoVO> listaPremFilial = ResultadoLojaBusiness.getInstance().obterResultadoPorLoja(ano, mes, idFilial);
				pRequest.setAttribute("listaPremFilial",listaPremFilial);
			} else if (tabSelecionada == 2) {
				//Operacional
				List<ResultadoVO> listaPremOper = ResultadoLojaBusiness.getInstance().obterResultadoPorOperacional(ano, mes, idFilial, idFunc);
				pRequest.setAttribute("listaFuncionario", ResultadoLojaBusiness.getInstance().obterListaFuncionarioPorOperacional(ano, mes, idFilial));
				pRequest.setAttribute("listaPremOper",listaPremOper);
			} else if (tabSelecionada == 3) {
				//Lideres
				List<ResultadoVO> listaPremLider = ResultadoLojaBusiness.getInstance().obterResultadoPorLider(ano, mes, idFilial, idFunc);
				pRequest.setAttribute("listaFuncionario", ResultadoLojaBusiness.getInstance().obterListaFuncionarioPorLider(ano, mes, idFilial));
				pRequest.setAttribute("listaPremLider",listaPremLider);
			}

		}

		if (ano == 2016 && (mes == 7 || mes == 8 || mes == 9)) {
			MensagemVO mensagemVO = (MensagemVO)pRequest.getSession(true).getAttribute("mensagemVO");
			if (mensagemVO == null) {
				mensagemVO = new MensagemVO();
				mensagemVO.setExibeMensagem(true);
				mensagemVO.setTextoMensagem("IMPORTANTE: Para este mês pedimos que somente para o indicador 20 - PARTICIPAÇÃO DO CARTÃO considerem o resultado (% Atg) disponível no DW.");
				mensagemVO.setExibePopup(true);
				mensagemVO.setTextoPopup("ATENÇÃO: Estamos com problemas de visualização no indicador 20 - PARTICIPAÇÃO DO CARTÃO. Considerar neste momento para simulação e cálculo do PPR, o resultado (% Atg) disponível no DW.");
			}
			pRequest.getSession(true).setAttribute("mensagemVO", mensagemVO);
		} else {
			pRequest.getSession(true).removeAttribute("mensagemVO");
		}

		FilialVO filialVO = FilialBusiness.getInstance().obtemFilial(Constantes.CODIGO_EMPRESA, idFilial, true);
		if (filialVO!=null) {
			pRequest.setAttribute("tipoFilial",filialVO.getCodTpFilial());
		}

		pRequest.setAttribute("periodoFiltro", periodo);
		pRequest.setAttribute("idFilialF",idFilial);
		pRequest.setAttribute("idFuncFiltro", idFunc);
		pRequest.setAttribute("listaFilial", listaFilial);
		pRequest.setAttribute("tabAnterior",tabSelecionada);
		pRequest.setAttribute("tabSelecionada",tabSelecionada);

		return pMapping.findForward(ResultadoLojaAction.FORWARD_RESULTADO_LOJA);

	}

}