package br.com.marisa.srv.cargo.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.cargo.business.CargoBusiness;
import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.classehay.business.ClasseHayBusiness;
import br.com.marisa.srv.classehay.vo.ClasseHayVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.gpremuneracaovariavel.business.GrupoRemuneracaoVariavelBusiness;
import br.com.marisa.srv.gpremuneracaovariavel.vo.GrupoRemuneracaoVariavelVO;
import br.com.marisa.srv.processo.action.ProcessoPeriodoAction;

/**
 * Action para tratar as requisições de Cargo
 * 
 * @author Walter Fontes
 */
public class CargoAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_CARGO = "cargo";
		
	
	/**
	 * Realiza a consulta dos cargos
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
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_CARGO);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CARGO_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CARGO_ALTERACAO));

		CargoVO pesquisaCargoVO = new CargoVO();
		pesquisaCargoVO.setIdCargo(getIntegerParam(pRequest, "idCargoF"));
		pesquisaCargoVO.setDescricaoCargo(getStringParam(pRequest, "descricaoF"));
		pesquisaCargoVO.setIdClasseHay(getIntegerParam(pRequest, "idClasseHayF"));
		pesquisaCargoVO.setIdGrpRemVar(getIntegerParam(pRequest, "idGrupoRemuneracaoF"));
		String agrupaFilial = getStringParam(pRequest, "agrupaFilialF");
		if (ObjectHelper.isNotEmpty(agrupaFilial)) {
			pesquisaCargoVO.setAgrupaFiliais(getBooleanParam(pRequest, "agrupaFilialF"));
		}
		
		List<CargoVO> listaCargoVO = CargoBusiness.getInstance().obtemListaCargoComGrupoRemVar(pesquisaCargoVO);
		List<ClasseHayVO> listaClasseHayVO = ClasseHayBusiness.getInstance().obtemListaClassesHay(new ClasseHayVO());
		List<GrupoRemuneracaoVariavelVO> listaGrupoRemuneracaoVariavelVO = GrupoRemuneracaoVariavelBusiness.getInstance().obtemListaGrupoRemuneracaoVariavel(new GrupoRemuneracaoVariavelVO());

		pRequest.setAttribute("listaCargoVO", listaCargoVO);
		pRequest.setAttribute("pesquisaCargoVO", pesquisaCargoVO);
		pRequest.setAttribute("listaClasseHayVO", listaClasseHayVO);
		pRequest.setAttribute("listaGrupoRemuneracaoVariavelVO", listaGrupoRemuneracaoVariavelVO);
		
		return pMapping.findForward(CargoAction.FORWARD_CARGO);		
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
	public ActionForward alteraCargo(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			CargoVO cargoVO = new CargoVO();
			cargoVO.setIdCargo(getIntegerParam(pRequest, "idCargoC"));
			cargoVO.setDescricaoCargo(getStringParam(pRequest, "descricaoC"));
			cargoVO.setIdClasseHay(getIntegerParam(pRequest, "idClasseHayC"));
			cargoVO.setAgrupaFiliais(getBooleanParam(pRequest, "agrupaFiliaisC"));
			cargoVO.setDataUltimaAlteracao(new Date());
			cargoVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			
			Integer[] idsGrupos = getIntegerParamArray(pRequest, "idGrupoRemuneracao");
			List<Integer> gruposCargo = null;
			if (idsGrupos != null) {
				gruposCargo = Arrays.asList(idsGrupos);
			}
			
			CargoBusiness.getInstance().alteraCargo(cargoVO, gruposCargo);
			setMensagem(pRequest,"Cargo alterado com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
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
	public ActionForward incluiCargo(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			CargoVO cargoVO = new CargoVO();
			cargoVO.setIdCargo(getIntegerParam(pRequest, "idCargoC"));
			cargoVO.setDescricaoCargo(getStringParam(pRequest, "descricaoC"));
			cargoVO.setIdClasseHay(getIntegerParam(pRequest, "idClasseHayC"));
			cargoVO.setAgrupaFiliais(getBooleanParam(pRequest, "agrupaFiliaisC"));
			cargoVO.setDataUltimaAlteracao(new Date());
			cargoVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			
			Integer[] idsGrupos = getIntegerParamArray(pRequest, "idGrupoRemuneracao");
			List<Integer> gruposCargo = null;
			if (idsGrupos != null) {
				gruposCargo = Arrays.asList(idsGrupos);
			}
			
			CargoBusiness.getInstance().incluiCargo(cargoVO, gruposCargo);
			setMensagem(pRequest,"Cargo incluído com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
	}	
}