package br.com.marisa.srv.ajuste.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.funcionario.action.FuncionarioAction;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.CNPJCPFHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;

public class AjusteFuncionarioAction extends BasicAction {

	private static final Logger log = Logger.getLogger(FuncionarioAction.class);	
	private static final String FORWARD_DETALHE_AJUSTE = "detalheAjuste";

	public AjusteFuncionarioAction() throws Exception {}


	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

	private ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
	
		Long    idFuncionario   = getLongParam  (pRequest, "idFuncionario");
		String  nomeFuncionario = getStringParam(pRequest, "nomeFuncionario");
		String  cracha 			= getStringParam(pRequest, "cracha");
		String  cpfFuncionario  = getStringParam(pRequest, "cpfFuncionario");
		
		pRequest.setAttribute("idFuncionario",   idFuncionario);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionario);
		pRequest.setAttribute("cracha", 		 cracha);
		pRequest.setAttribute("cpfFuncionario",  cpfFuncionario);
		
		if ((idFuncionario   != null && idFuncionario.intValue() > 0) ||
			(nomeFuncionario != null && nomeFuncionario.length() > 0) ||
			(cracha          != null && cracha.length()          > 0) ||
			(cpfFuncionario  != null && cpfFuncionario.length()  > 0)) {
			
			Long cpfFuncionarioL = null;
			if (cpfFuncionario  != null && cpfFuncionario.length()  > 0) {
				cpfFuncionarioL = new Long(CNPJCPFHelper.retiraCaracteresAlpha(cpfFuncionario));				
			}
			
			List<FuncionarioVO> funcionarios = FuncionarioBusiness.getInstance().obtemListaFuncionario(idFuncionario, nomeFuncionario, cracha, cpfFuncionarioL, null, null, null);
			pRequest.setAttribute("funcionarios", funcionarios);
		}
		
		return pMapping.findForward(PAGINA_INICIAL);
	}
	
	public ActionForward pesquisaPeriodoAjuste(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			Long idFuncionario = getLongParam(pRequest, "idFuncionario");
			Integer ano = getIntegerParam(pRequest, "ano");
			Integer mes = getIntegerParam(pRequest, "mes");

			List listIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadoresAjusteFuncionario(idFuncionario, mes, ano);

			pRequest.setAttribute("idFuncionario", getIntegerParam(pRequest, "idFuncionario"));
			pRequest.setAttribute("nomeFuncionario", getStringParam(pRequest, "nomeFuncionario"));
			pRequest.setAttribute("cargoFuncionario", getStringParam(pRequest, "cargoFuncionario"));
			pRequest.setAttribute("centroCustoFuncionario", getStringParam(pRequest, "centroCustoFuncionario"));
			pRequest.setAttribute("mesSelecionado", mes);
			pRequest.setAttribute("anoSelecionado", ano);
			pRequest.setAttribute("listIndicadores", listIndicadores);

			pRequest.setAttribute("listaMes", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false));
			pRequest.setAttribute("listaAno", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true));
		}
		
		return pMapping.findForward(AjusteFuncionarioAction.FORWARD_DETALHE_AJUSTE);
	}

	public ActionForward detalheAjusteFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(getLongParam(pRequest, "checkbox"));
			pRequest.setAttribute("idFuncionario", funcionarioVO.getIdFuncionario());
			pRequest.setAttribute("nomeFuncionario", funcionarioVO.getNomeFuncionario());
			pRequest.setAttribute("cargoFuncionario", funcionarioVO.getDescricaoCargo());
			pRequest.setAttribute("centroCustoFuncionario", funcionarioVO.getDescricaoCentroCusto());

			pRequest.setAttribute("listaMes", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false));
			pRequest.setAttribute("listaAno", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true));
		}
		
		return pMapping.findForward(AjusteFuncionarioAction.FORWARD_DETALHE_AJUSTE);
	}

	public ActionForward incluiAjuste(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			Long idFuncionario = getLongParam(pRequest, "idFuncionarioI");
			Integer idIndicador = getIntegerParam(pRequest, "indicadorI");
			Integer ano = getIntegerParam(pRequest, "anoI");
			Integer mes = getIntegerParam(pRequest, "mesI");
			Double valor = getDoubleParam(pRequest, "valorI", true);

			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);

			IndicadorFuncionarioRealizadoVO vo = new IndicadorFuncionarioRealizadoVO();
			vo.setIdEmpresa(funcionarioVO.getIdEmpresa());
			vo.setIdFilial(funcionarioVO.getIdFilial());
			vo.setIdFuncionario(idFuncionario);
			vo.setIdIndicador(idIndicador);
			vo.setValorPremioCalculado(valor);
			vo.setDataUltimaAlteracao(new Date());
			vo.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			vo.setAno(ano);
			vo.setMes(mes);
			if (IndicadorBusiness.getInstance().existeRealizadoAjuste(vo)) {
				setMensagemErro(pRequest,"Ajuste já foi cadastrado");
			} else {
				IndicadorBusiness.getInstance().incluiRealizadoAjuste(vo);
				setMensagem(pRequest,"Ajuste incluído com sucesso");
			}

			Integer anoFiltro = getIntegerParam(pRequest, "anoF");
			Integer mesFiltro = getIntegerParam(pRequest, "mesF");

			List listIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadoresAjusteFuncionario(idFuncionario, mesFiltro, anoFiltro);
			pRequest.setAttribute("listIndicadores", listIndicadores);
			pRequest.setAttribute("mesSelecionado", mesFiltro);
			pRequest.setAttribute("anoSelecionado", anoFiltro);

			pRequest.setAttribute("idFuncionario", funcionarioVO.getIdFuncionario());
			pRequest.setAttribute("nomeFuncionario", funcionarioVO.getNomeFuncionario());
			pRequest.setAttribute("cargoFuncionario", funcionarioVO.getDescricaoCargo());
			pRequest.setAttribute("centroCustoFuncionario", funcionarioVO.getDescricaoCentroCusto());

			pRequest.setAttribute("listaMes", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false));
			pRequest.setAttribute("listaAno", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true));
		}

		return pMapping.findForward(FORWARD_DETALHE_AJUSTE);
	}

	public ActionForward alteraAjuste(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			Long idFuncionario = getLongParam(pRequest, "idFuncionarioA");
			Integer idIndicador = getIntegerParam(pRequest, "indicadorA");
			Integer filial = getIntegerParam(pRequest, "idFilialA");
			Integer empresa = getIntegerParam(pRequest, "idEmpresaA");
			Double valor = getDoubleParam(pRequest, "valorA", true);
			Integer ano = getIntegerParam(pRequest, "idAnoA");
			Integer mes = getIntegerParam(pRequest, "idMesA");

			IndicadorFuncionarioRealizadoVO vo = new IndicadorFuncionarioRealizadoVO();
			vo.setIdEmpresa(empresa);
			vo.setIdFilial(filial);
			vo.setIdFuncionario(idFuncionario);
			vo.setIdIndicador(idIndicador);
			vo.setValorPremioCalculado(valor);
			vo.setDataUltimaAlteracao(new Date());
			vo.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			vo.setAno(ano);
			vo.setMes(mes);
			IndicadorBusiness.getInstance().alteraRealizadoAjuste(vo);

			setMensagem(pRequest,"Ajuste alterado com sucesso");

			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);

			List listIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadoresAjusteFuncionario(idFuncionario, mes, ano);
			pRequest.setAttribute("listIndicadores", listIndicadores);
			pRequest.setAttribute("mesSelecionado", mes);
			pRequest.setAttribute("anoSelecionado", ano);
			pRequest.setAttribute("idFuncionario", funcionarioVO.getIdFuncionario());
			pRequest.setAttribute("nomeFuncionario", funcionarioVO.getNomeFuncionario());
			pRequest.setAttribute("cargoFuncionario", funcionarioVO.getDescricaoCargo());
			pRequest.setAttribute("centroCustoFuncionario", funcionarioVO.getDescricaoCentroCusto());

			pRequest.setAttribute("listaMes", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false));
			pRequest.setAttribute("listaAno", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true));
		}

		return pMapping.findForward(FORWARD_DETALHE_AJUSTE);
	}

	public ActionForward excluiAjuste(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			Integer idFuncionario = getIntegerParam(pRequest, "idFuncionario");
			Integer idIndicador = getIntegerParam(pRequest, "idIndicadorS");
			Integer filial = getIntegerParam(pRequest, "idFilialS");
			Integer empresa = getIntegerParam(pRequest, "idEmpresaS");
			Integer ano = getIntegerParam(pRequest, "idAnoS");
			Integer mes = getIntegerParam(pRequest, "idMesS");

			IndicadorBusiness.getInstance().excluiRealizadoIndicadorBonus(idFuncionario.intValue(),idIndicador.intValue(),empresa.intValue(),filial.intValue(),ano.intValue(),mes.intValue());

			setMensagem(pRequest,"Ajuste excluído com sucesso");

			FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(new Long(idFuncionario.intValue()));

			List listIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadoresAjusteFuncionario(new Long(idFuncionario.intValue()), mes, ano);
			pRequest.setAttribute("listIndicadores", listIndicadores);
			pRequest.setAttribute("anoSelecionado", getStringParam(pRequest, "ano"));
			pRequest.setAttribute("mesSelecionado", getStringParam(pRequest, "mes"));
			pRequest.setAttribute("idFuncionario", funcionarioVO.getIdFuncionario());
			pRequest.setAttribute("nomeFuncionario", funcionarioVO.getNomeFuncionario());
			pRequest.setAttribute("cargoFuncionario", funcionarioVO.getDescricaoCargo());
			pRequest.setAttribute("centroCustoFuncionario", funcionarioVO.getDescricaoCentroCusto());

			pRequest.setAttribute("listaMes", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false));
			pRequest.setAttribute("listaAno", CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true));
		}

		return pMapping.findForward(FORWARD_DETALHE_AJUSTE);
	}

}