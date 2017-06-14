package br.com.marisa.srv.gpremuneracaovariavel.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.gpremuneracaovariavel.business.GrupoRemuneracaoVariavelBusiness;
import br.com.marisa.srv.gpremuneracaovariavel.vo.IndicadorGpRemVarRealizadoVO;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;

/**
 * Action para tratar as requisições de geração de reprocessamento por Grupo de Remuneração Variável
 * 
 * @author Walter Fontes
 */
public class GrupoRemVarBonusAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_GRUPO_REM_VAR = "grupoRemVar";
		
	
	/**
	 * Realiza a consulta dos Grupos de Remuneração Variável
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
		
		int idGrupoF = -1; 
		if (getIntegerParam(pRequest, "idGrupoRemVarF") != null) {
			idGrupoF = getIntegerParam(pRequest, "idGrupoRemVarF").intValue();
			pRequest.setAttribute("idGrupoRemVarF", String.valueOf(idGrupoF));
		}
		String descricao = getStringParam(pRequest, "descricaoF");
		pRequest.setAttribute("descricaoF", descricao);
		
		List gruposRemuneracao = GrupoRemuneracaoVariavelBusiness.getInstance().obtemGruposRemuneracao(idGrupoF, descricao);
		pRequest.setAttribute("gruposRemuneracao", gruposRemuneracao);
		
		return pMapping.findForward(GrupoRemVarBonusAction.FORWARD_GRUPO_REM_VAR);		
	}
	

	/**
	 * Efetiva processamento de bonus
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
//	public ActionForward processaBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
//
//		IndicadorGpRemVarRealizadoVO indicadorGpRemVarRealizadoVO = new IndicadorGpRemVarRealizadoVO();
//		indicadorGpRemVarRealizadoVO.setIdGrupoRemuneracaoVariavel(getIntegerParam(pRequest, "idGrupoRemVarC"));
//		indicadorGpRemVarRealizadoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorC"));
//		indicadorGpRemVarRealizadoVO.setAno(getIntegerParam(pRequest, "idAnoC"));
//		indicadorGpRemVarRealizadoVO.setMes(getIntegerParam(pRequest, "idMesC"));
//		indicadorGpRemVarRealizadoVO.setUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoC"));
//		indicadorGpRemVarRealizadoVO.setRealizado(getDoubleParam(pRequest, "realizadoC", true));
//		indicadorGpRemVarRealizadoVO.setUnidadeRealizadoXMeta(getIntegerParam(pRequest, "idUnidadeAtingimentoC"));
//		indicadorGpRemVarRealizadoVO.setRealizadoXMeta(getDoubleParam(pRequest, "atingimentoC", true));
//		indicadorGpRemVarRealizadoVO.setDataUltimaAlteracao(new Date());
//		indicadorGpRemVarRealizadoVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
//		
//		IndicadorBusiness.getInstance().alteraRealizadoIndicadorBonusPorGpRemVar(indicadorGpRemVarRealizadoVO);
//		setMensagem(pRequest, "Indicador processado com sucesso para o grupo de remuneração selecionado.");
//		
//		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//		
//		return pMapping.findForward(PAGINA_INICIAL);
//	}
}