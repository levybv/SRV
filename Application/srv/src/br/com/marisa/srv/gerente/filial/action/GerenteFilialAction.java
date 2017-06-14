package br.com.marisa.srv.gerente.filial.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.gerente.filial.business.GerenteFilialBusiness;
import br.com.marisa.srv.gerente.filial.vo.GerenteVO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class GerenteFilialAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_GERENTE_FILIAL = "gerenteFilial";

	/**
	 * 
	 */
	public GerenteFilialAction() throws Exception {
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
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		pRequest.removeAttribute("idFuncF");
		pRequest.removeAttribute("nomeFuncF");

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
	public ActionForward incluiGerenteFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		FuncionarioVO funcionarioVO = FuncionarioBusiness.getInstance().obtemFuncionario(getLongParam(pRequest, "matriculaI"));
		if (funcionarioVO!=null) {
			GerenteVO gerenteVO = new GerenteVO();
			gerenteVO.setIdFuncionario(funcionarioVO.getIdFuncionario());
			gerenteVO.setFilialOrigem(new FilialVO(funcionarioVO.getIdFilial()));
			gerenteVO.setFilial(new FilialVO(getIntegerParam(pRequest, "filialI")));
			gerenteVO.setCodAtuacao(getIntegerParam(pRequest, "codAtuacaoI"));
			gerenteVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			GerenteFilialBusiness.getInstance().incluiGerenteFilial(gerenteVO);

			setMensagem(pRequest, "Loja de gerente incluída com sucesso!");
		}

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
	public ActionForward executarFiltro(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		GerenteVO pesquisaVO = new GerenteVO();
		pesquisaVO.setIdFuncionario(getLongParam(pRequest, "idFuncF"));
		pesquisaVO.setNomeFuncionario(getStringParam(pRequest, "nomeFuncF"));
		pesquisaVO.setCodAtuacao(getIntegerParam(pRequest, "codAtuacaoF"));
		if ( !AcessoBusiness.getInstance().obtemAcessoPerfilFuncionalidadeTipoAcesso(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil(),
																						ConstantesFuncionalidades.FUNCIONALIDADE_GERENTE_LOJA, 
																						ConstantesFuncionalidades.TIPO_ACESSO_GERENTE_LOJA_MASTER)) {
			pesquisaVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
		}

		List<GerenteVO> lista = GerenteFilialBusiness.getInstance().obtemListaGerenteLoja(pesquisaVO);
		pRequest.setAttribute("gerenteFilial", lista);
		pRequest.setAttribute("idFuncF", pesquisaVO.getIdFuncionario());
		pRequest.setAttribute("nomeFuncF", pesquisaVO.getNomeFuncionario());
		pRequest.setAttribute("codAtuacaoF", pesquisaVO.getCodAtuacao());

		return pMapping.findForward(GerenteFilialAction.FORWARD_GERENTE_FILIAL);
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
	public ActionForward excluiGerenteFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		Long codFuncionario = getLongParam(pRequest, "idFuncE");
		Integer codFilial = getIntegerParam(pRequest, "idFilialE");

		GerenteFilialBusiness.getInstance().excluiGerenteFilial(codFuncionario, codFilial, obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

		setMensagem(pRequest, "Loja de gerente excluída com sucesso!");

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

		UsuarioVO usuarioVO = obtemUsuarioDaSessao(pRequest).getUsuarioVO();

		GerenteVO pesquisaVO = null;
		if ( !AcessoBusiness.getInstance().obtemAcessoPerfilFuncionalidadeTipoAcesso(usuarioVO.getIdPerfil(),
																						ConstantesFuncionalidades.FUNCIONALIDADE_GERENTE_LOJA, 
																						ConstantesFuncionalidades.TIPO_ACESSO_GERENTE_LOJA_MASTER)) {
			pesquisaVO = new GerenteVO();
			pesquisaVO.setCodUsuario(usuarioVO.getIdUsuario());
		}

		List<GerenteVO> lista = GerenteFilialBusiness.getInstance().obtemListaGerenteLoja(pesquisaVO);
		pRequest.setAttribute("gerenteFilial", lista);
		return pMapping.findForward(GerenteFilialAction.FORWARD_GERENTE_FILIAL);

	}

}