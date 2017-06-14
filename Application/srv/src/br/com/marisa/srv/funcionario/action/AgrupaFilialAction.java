package br.com.marisa.srv.funcionario.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.funcionario.business.SalarioBaseBusiness;
import br.com.marisa.srv.funcionario.vo.SalarioBaseVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Action para tratar as requisições de Agrupamento de Filiais
 * 
 * @author Walter Fontes
 */
public class AgrupaFilialAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_AGRUPA_FILIAL = "agrupaFilial";
		
	
	/**
	 * Realiza a consulta de agrupamentos de filiais
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
		
		Integer idFilial 			= getIntegerParam(pRequest, "idFilialF");
		Long    idFuncionario		= getLongParam(pRequest, "idFuncionarioF");
		String  nomeFuncionario		= getStringParam(pRequest, "nomeFuncionarioF");
		Integer ano					= getIntegerParam(pRequest, "anoF");
		Integer mes 				= getIntegerParam(pRequest, "mesF");
		
		pRequest.setAttribute("idFilialF", 			 idFilial);
		pRequest.setAttribute("idFuncionarioF", 	 idFuncionario);
		pRequest.setAttribute("nomeFuncionarioF", 	 nomeFuncionario);
		pRequest.setAttribute("anoF", 				 ano);
		pRequest.setAttribute("mesF",				 mes);
		
		if (idFilial != null || idFuncionario != null || nomeFuncionario != null || ano != null || mes != null) {
			List agrupamentos = SalarioBaseBusiness.getInstance().obtemSalariosBase(idFilial, idFuncionario, nomeFuncionario, ano, mes, Boolean.FALSE);
			pRequest.setAttribute("agrupamentos", agrupamentos);
		}
		
		return pMapping.findForward(AgrupaFilialAction.FORWARD_AGRUPA_FILIAL);		
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
	public ActionForward incluiAgrupamentoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			SalarioBaseVO salarioBaseVO = new SalarioBaseVO();
			salarioBaseVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
			salarioBaseVO.setIdFilial(getIntegerParam(pRequest, "idFilialAF"));
			salarioBaseVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioAF"));
			
			salarioBaseVO.setAno(getIntegerParam(pRequest, "idAnoAF"));
			salarioBaseVO.setMes(getIntegerParam(pRequest, "idMesAF"));
			
			salarioBaseVO.setDataUltimaAlteracao(new Date());
			salarioBaseVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			SalarioBaseBusiness.getInstance().alteraSalarioBase(salarioBaseVO);
			setMensagem(pRequest, "Agrupamento de filial incluído com sucesso");			
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(AgrupaFilialAction.PAGINA_INICIAL);		
	}	
	
	
	/**
	 * Efetiva exclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward excluiAgrupamentoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			Integer idEmpresa 		= getIntegerParam(pRequest, "idEmpresaAF");
			Integer idFilial 		= getIntegerParam(pRequest, "idFilialAF");
			Long    idFuncionario 	= getLongParam	 (pRequest, "idFuncionarioAF");
			
			String periodo = getStringParam(pRequest, "idPeriodoAF");
			int posicaoDivisao = periodo.indexOf("-");
			Integer ano = new Integer(periodo.substring(0, posicaoDivisao));
			Integer mes = new Integer(periodo.substring(posicaoDivisao+1));	

			SalarioBaseBusiness.getInstance().excluiSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
			setMensagem(pRequest, "Agrupamento de filial excluído com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(AgrupaFilialAction.PAGINA_INICIAL);		
	}		
}