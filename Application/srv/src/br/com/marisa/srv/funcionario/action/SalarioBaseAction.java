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
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Action para tratar as requisições de Salário Base
 * 
 * @author Walter Fontes
 */
public class SalarioBaseAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_SALARIO_BASE = "salarioBase";
		
	
	/**
	 * Realiza a consulta de salários base
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
		
		Long    idFuncionario		= getLongParam	 (pRequest, "idFuncionarioF");
		String  nomeFuncionario		= getStringParam (pRequest, "nomeFuncionarioF");
		Integer ano					= getIntegerParam(pRequest, "anoF");
		Integer mes 				= getIntegerParam(pRequest, "mesF");
		
		pRequest.setAttribute("idFuncionarioF", 	 idFuncionario);
		pRequest.setAttribute("nomeFuncionarioF", 	 nomeFuncionario);
		pRequest.setAttribute("anoF", 				 ano);
		pRequest.setAttribute("mesF",				 mes);
		
		if (idFuncionario != null || nomeFuncionario != null || ano != null || mes != null) {
			List salariosBase = SalarioBaseBusiness.getInstance().obtemSalariosBase(null, idFuncionario, nomeFuncionario, ano, mes, Boolean.TRUE);
			pRequest.setAttribute("salariosBase", salariosBase);
		}
		
		return pMapping.findForward(SalarioBaseAction.FORWARD_SALARIO_BASE);		
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
	public ActionForward alteraSalarioBase(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			SalarioBaseVO salarioBaseVO = new SalarioBaseVO();
      SalarioBaseVO salarioBaseAntigo = new SalarioBaseVO();
			
			salarioBaseVO.setIdEmpresa(getIntegerParam(pRequest, "idEmpresaSB"));
			salarioBaseVO.setIdFilial(getIntegerParam(pRequest, "idFilialSB"));
			salarioBaseVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioSB"));
			
			String periodo = getStringParam(pRequest, "idPeriodoSB");
			int posicaoDivisao = periodo.indexOf("-");
			int ano = Integer.parseInt(periodo.substring(0, posicaoDivisao));
			int mes = Integer.parseInt(periodo.substring(posicaoDivisao+1));
			salarioBaseVO.setAno(new Integer(ano));
			salarioBaseVO.setMes(new Integer(mes));
			
			salarioBaseVO.setSalarioBase(getDoubleParam(pRequest, "salarioBaseSB", true));
			salarioBaseVO.setDataUltimaAlteracao(new Date());
			salarioBaseVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

      salarioBaseAntigo = SalarioBaseBusiness.getInstance().buscarSalarioBase(salarioBaseVO);
			
      if (salarioBaseAntigo.getIdFuncionario() != null)
      {
        SalarioBaseBusiness.getInstance().alteraSalarioBase(salarioBaseVO);
        setMensagem(pRequest, "SalÃ¡rio Base alterado com sucesso");
      }
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(SalarioBaseAction.PAGINA_INICIAL);		
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
	public ActionForward incluiSalarioBase(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			SalarioBaseVO salarioBaseVO = new SalarioBaseVO();
			salarioBaseVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioSB"));
			
			String periodo = getStringParam(pRequest, "idPeriodoSB");
			int posicaoDivisao = periodo.indexOf("-");
			int ano = Integer.parseInt(periodo.substring(0, posicaoDivisao));
			int mes = Integer.parseInt(periodo.substring(posicaoDivisao+1));
			salarioBaseVO.setAno(new Integer(ano));
			salarioBaseVO.setMes(new Integer(mes));
			
			salarioBaseVO.setSalarioBase(getDoubleParam(pRequest, "salarioBaseSB", true));
			salarioBaseVO.setDataUltimaAlteracao(new Date());
			salarioBaseVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			if (SalarioBaseBusiness.getInstance().buscarSalarioBase(salarioBaseVO).getIdFuncionario() != null) {
				setMensagem(pRequest, "SalÃ¡rio base jÃ¡ cadastrado para o perÃ­odo.");
			} else {
				SalarioBaseBusiness.getInstance().incluiSalarioBase(salarioBaseVO);
				setMensagem(pRequest, "Salário Base incluído com sucesso");			
				pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
			}
		}
		return pMapping.findForward(SalarioBaseAction.PAGINA_INICIAL);		
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
	public ActionForward excluiSalarioBase(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			Integer idEmpresa 		= getIntegerParam(pRequest, "idEmpresaSB");
			Integer idFilial 		= getIntegerParam(pRequest, "idFilialSB");
			Long    idFuncionario 	= getLongParam(pRequest, "idFuncionarioSB");
			
			String periodo = getStringParam(pRequest, "idPeriodoSB");
			int posicaoDivisao = periodo.indexOf("-");
			Integer ano = new Integer(periodo.substring(0, posicaoDivisao));
			Integer mes = new Integer(periodo.substring(posicaoDivisao+1));			

			SalarioBaseBusiness.getInstance().excluiSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
			setMensagem(pRequest, "Salário Base excluído com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(SalarioBaseAction.PAGINA_INICIAL);		
	}		
}