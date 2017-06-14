package br.com.marisa.srv.funcionario.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.vo.PeriodoCalendarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Action para lidar com requisicoes do módulo de bônus
 * 
 * @author Walter Fontes
 */
public class AllPageBonusAction extends BasicAction {

	
	//Log4J
	private static final Logger log = Logger.getLogger(ExtratoFuncionarioBonusAction.class);

	
	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_PESQUISA_PAGAMENTO 		= "pagamento";
	private static final String FORWARD_PESQUISA_CONFERENCIA 	= "conferencia";
	private static final String FORWARD_PESQUISA_DESEMPENHO 	= "desempenho";

	
	/**
	 * Cria um novo objeto ExtratoFuncionarioAction
	 * 
	 * @throws Exception
	 */
	public AllPageBonusAction() throws Exception {
		//
	}


	/**
	 * Inicia página de extrato de funcionários
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaFuncionario(pMapping, pForm, pRequest, pResponse);
	}
	
	
	/**
	 * Monta os dados da tela
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
	private ActionForward montaTelaFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
	
		Long    idFuncionario   = getLongParam  (pRequest, "idFuncionario");
		String  nomeFuncionario = getStringParam(pRequest, "nomeFuncionario");
		String  cracha 			= getStringParam(pRequest, "cracha");
		String  cpfFuncionario  = getStringParam(pRequest, "cpfFuncionario");
		
		pRequest.setAttribute("idFuncionario",   idFuncionario);
		pRequest.setAttribute("nomeFuncionario", nomeFuncionario);
		pRequest.setAttribute("cracha", 		 cracha);
		pRequest.setAttribute("cpfFuncionario",  cpfFuncionario);
		
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		List listaAno = new ArrayList ();
		List listaMes = new ArrayList ();
		

		   String meses[] = {"Janeiro", "Fevereiro", 
		              		"Março", "Abril", "Maio", "Junho", 
		              		"Julho", "Agosto", "Setembro", "Outubro",
		              		"Novembro", "Dezembro"};

		   PeriodoCalendarioVO vo = new PeriodoCalendarioVO();
		   
		for (int i = 0; i < meses.length; i++) {
			vo = new PeriodoCalendarioVO();
			vo.setId(new Integer(i+1));
			vo.setMesStr(meses[i]);
			listaMes.add(vo);
		}
		
		Integer dataIncial = new Integer(1999);
		SimpleDateFormat ff = new SimpleDateFormat("yyyy");
		Date data = new Date();
		Integer dataFinal = Integer.valueOf(ff.format(data));
		
		
		for (int i = dataIncial.intValue(); i < dataFinal.intValue(); i++) {
			dataIncial = new Integer(dataIncial.intValue()+1);
			vo = new PeriodoCalendarioVO();
			vo.setId(dataIncial);
			vo.setAno(dataIncial);
			listaAno.add(vo);
		}
		
		
		pRequest.setAttribute("listaAno", listaAno);
		pRequest.setAttribute("listaMes", listaMes);
		
		
		/*
		if ((idFuncionario   != null && idFuncionario.intValue() > 0) ||
			(nomeFuncionario != null && nomeFuncionario.length() > 0) ||
			(cracha          != null && cracha.length()          > 0) ||
			(cpfFuncionario  != null && cpfFuncionario.length()  > 0)) {
			*/
			
//			Long cpfFuncionarioL = null;
//			if (cpfFuncionario  != null && cpfFuncionario.length()  > 0) {
//				cpfFuncionarioL = new Long(CNPJCPFHelper.retiraCaracteresAlpha(cpfFuncionario));				
//			}
//			
//			Long matricula = null;
//			if (obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula() != null) {
//				try {
//					matricula = new Long(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula());
//				} catch (Exception e) {
//					log.warn("Erro ao obter a matrícula do usuário " + obtemUsuarioDaSessao(pRequest).getUsuarioVO().getNome(), e);
//				}
//			}
//			
//			List<FuncionarioVO> funcionarios = FuncionarioBusiness.getInstance().obtemFuncionarios(idFuncionario, nomeFuncionario, cracha, cpfFuncionarioL, matricula);
//			pRequest.setAttribute("funcionarios", funcionarios);
//			for(FuncionarioVO vo : funcionarios){
//				pRequest.setAttribute("situacaoColaborador",vo.getSituacaoColaborador());
//			}
//			
//		//}
		System.out.println("Teste");
		return pMapping.findForward(FORWARD_PESQUISA_PAGAMENTO); 
	}
	
	
	/**
	 * Monta os dados da tela
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
	public ActionForward pesquisa(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		//Tela atual
		Long    idFuncionarioSelecionado 			= getLongParam	(pRequest, "idFuncionarioSelecionado");
		String  nomeFuncionarioSelecionado 			= getStringParam(pRequest,  "nomeFuncionarioSelecionado");
		String  cargoFuncionarioSelecionado 		= getStringParam(pRequest,  "cargoFuncionarioSelecionado");
		String  centroCustoFuncionarioSelecionado 	= getStringParam(pRequest,  "centroCustoFuncionarioSelecionado");
		String  periodoFiltro  						= getStringParam(pRequest,  "periodos");

		//Tela Anterior
		Long    idFuncionarioFiltro 				= getLongParam  (pRequest, "idFuncionario");
		String  nomeFuncionarioFiltro 				= getStringParam(pRequest,  "nomeFuncionario");
		String  crachaFiltro 						= getStringParam(pRequest,  "cracha");
		String  cpfFuncionarioFiltro 				= getStringParam(pRequest,  "cpfFuncionario");	
		
		pRequest.setAttribute("situacaoColaborador", 				getStringParam(pRequest,  "situacaoColaborador"));
		pRequest.setAttribute("idFuncionarioSelecionado", 			idFuncionarioSelecionado);
		pRequest.setAttribute("nomeFuncionarioSelecionado", 		nomeFuncionarioSelecionado);
		pRequest.setAttribute("cargoFuncionarioSelecionado", 		cargoFuncionarioSelecionado);
		pRequest.setAttribute("centroCustoFuncionarioSelecionado", 	centroCustoFuncionarioSelecionado);
		pRequest.setAttribute("idFuncionario", 						idFuncionarioFiltro);
		pRequest.setAttribute("nomeFuncionario", 					nomeFuncionarioFiltro);
		pRequest.setAttribute("cracha", 							crachaFiltro);
		pRequest.setAttribute("cpfFuncionario", 					cpfFuncionarioFiltro);
		pRequest.setAttribute("periodoSelecionado", 				periodoFiltro);
		
//		List listaPeriodos = CalendarioBusiness.getInstance().obtemListaPeriodos();
//		pRequest.setAttribute("listaPeriodos", listaPeriodos);
//		
//		if (ObjectHelper.isNotEmpty(periodoFiltro)) {
//			RelatorioBonusVO relatorioBonusVO = IndicadorBusiness.getInstance().obtemRelatorioBonusOperacionalPorFuncionario(idFuncionarioSelecionado, periodoFiltro);
//			pRequest.setAttribute("lista", relatorioBonusVO.getIndicadoresRealizados());
//			pRequest.setAttribute("total", relatorioBonusVO.getTotalRealizadoVO());
//		}
//		
//		Boolean exibeBonus = PerfilBusiness.getInstance().obtemPerfilByCod(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil()).getIsExibeBonus();
//		if (exibeBonus == null) {
//			exibeBonus = Boolean.FALSE;
//		}
//		pRequest.setAttribute("exibeBonus", exibeBonus);
		System.out.println("Teste");
		return pMapping.findForward(FORWARD_PESQUISA_PAGAMENTO);
	}
	
	
	/**
	 * Inclui indicador para o funcionário
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
//	public ActionForward incluiIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
//		
//		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
//		
//			IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = new IndicadorFuncionarioRealizadoVO();
//			indicadorFuncionarioRealizadoVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioID"));
//			indicadorFuncionarioRealizadoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorID"));
//			indicadorFuncionarioRealizadoVO.setAno(getIntegerParam(pRequest, "anoID"));
//			indicadorFuncionarioRealizadoVO.setMes(getIntegerParam(pRequest, "mesID"));
//			indicadorFuncionarioRealizadoVO.setUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoID"));
//			indicadorFuncionarioRealizadoVO.setRealizado(getDoubleParam(pRequest, "realizadoID", true));
//			indicadorFuncionarioRealizadoVO.setUnidadeRealizadoXMeta(getIntegerParam(pRequest, "idUnidadeAtingimentoID"));
//			indicadorFuncionarioRealizadoVO.setRealizadoXMeta(getDoubleParam(pRequest, "atingimentoID", true));
//			indicadorFuncionarioRealizadoVO.setDataUltimaAlteracao(new Date());
//			indicadorFuncionarioRealizadoVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
//			
//			IndicadorBusiness.getInstance().incluiRealizadoIndicadorBonus(indicadorFuncionarioRealizadoVO);
//			setMensagem(pRequest, "Indicador incluído com sucesso para o funcionário.");		
//			
//			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//		}
//		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
//	}	
	
	
	
	/**
	 * Altera indicador para o funcionário
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @throws SRVException 
	 */
//	public ActionForward alteraIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
//	
//		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
//		
//			IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = new IndicadorFuncionarioRealizadoVO();
//			indicadorFuncionarioRealizadoVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioID"));
//			indicadorFuncionarioRealizadoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorID"));
//			indicadorFuncionarioRealizadoVO.setIdEmpresa(getIntegerParam(pRequest, "idEmpresaID"));
//			indicadorFuncionarioRealizadoVO.setIdFilial(getIntegerParam(pRequest, "idFilialID"));
//			indicadorFuncionarioRealizadoVO.setAno(getIntegerParam(pRequest, "anoID"));
//			indicadorFuncionarioRealizadoVO.setMes(getIntegerParam(pRequest, "mesID"));
//			indicadorFuncionarioRealizadoVO.setUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoID"));
//			indicadorFuncionarioRealizadoVO.setRealizado(getDoubleParam(pRequest, "realizadoID", true));
//			indicadorFuncionarioRealizadoVO.setUnidadeRealizadoXMeta(getIntegerParam(pRequest, "idUnidadeAtingimentoID"));
//			indicadorFuncionarioRealizadoVO.setRealizadoXMeta(getDoubleParam(pRequest, "atingimentoID", true));
//			indicadorFuncionarioRealizadoVO.setDataUltimaAlteracao(new Date());
//			indicadorFuncionarioRealizadoVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
//			
//			IndicadorBusiness.getInstance().alteraRealizadoIndicadorBonus(indicadorFuncionarioRealizadoVO);
//			setMensagem(pRequest, "Indicador alterado com sucesso para o funcionário.");		
//			
//			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//		}
//		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
//	}
	
	
	/**
	 * Exclui indicador para o funcionário
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponseaW
	 * @throws SRVException 
	 */
//	public ActionForward excluiIndicadorFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
//	
//		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
//		
//        	int idFuncionario	= getIntParam(pRequest, "idFuncionarioID");
//        	int idIndicador		= getIntParam(pRequest, "idIndicadorID");
//        	int idEmpresa		= getIntParam(pRequest, "idEmpresaID");
//        	int idFilial 		= getIntParam(pRequest, "idFilialID");
//        	int ano 			= getIntParam(pRequest, "anoID");
//        	int mes				= getIntParam(pRequest, "mesID");
//			
//			IndicadorBusiness.getInstance().excluiRealizadoIndicadorBonus(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
//			setMensagem(pRequest, "Indicador excluído com sucesso para o funcionário.");		
//			
//			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//		}
//		return pesquisaExtratoFuncionario(pMapping, pForm, pRequest, pResponse);
//	}
	
}