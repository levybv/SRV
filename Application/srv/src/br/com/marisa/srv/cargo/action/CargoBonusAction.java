package br.com.marisa.srv.cargo.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.cargo.business.CargoBusiness;
import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.cargo.vo.IndicadorCargoRealizadoVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;

/**
 * Action para tratar as requisições de geração de reprocessamento por Cargo
 * 
 * @author Walter Fontes
 */
public class CargoBonusAction extends BasicAction {

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
		
		CargoVO pesquisaVO = new CargoVO();
		pesquisaVO.setIdCargo(getIntegerParam(pRequest, "idCargoF"));
		pesquisaVO.setDescricaoCargo(getStringParam(pRequest, "descricaoF"));
		
		List cargos = CargoBusiness.getInstance().obtemListaCargo(pesquisaVO);
		pRequest.setAttribute("cargos", cargos);
		
		return pMapping.findForward(CargoBonusAction.FORWARD_CARGO);		
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
//		IndicadorCargoRealizadoVO indicadorCargoRealizadoVO = new IndicadorCargoRealizadoVO();
//		indicadorCargoRealizadoVO.setIdCargo(getIntegerParam(pRequest, "idCargoC"));
//		indicadorCargoRealizadoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorC"));
//		indicadorCargoRealizadoVO.setAno(getIntegerParam(pRequest, "idAnoC"));
//		indicadorCargoRealizadoVO.setMes(getIntegerParam(pRequest, "idMesC"));
//		indicadorCargoRealizadoVO.setUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoC"));
//		indicadorCargoRealizadoVO.setRealizado(getDoubleParam(pRequest, "realizadoC", true));
//		indicadorCargoRealizadoVO.setUnidadeRealizadoXMeta(getIntegerParam(pRequest, "idUnidadeAtingimentoC"));
//		indicadorCargoRealizadoVO.setRealizadoXMeta(getDoubleParam(pRequest, "atingimentoC", true));
//		indicadorCargoRealizadoVO.setDataUltimaAlteracao(new Date());
//		indicadorCargoRealizadoVO.setIdUsuarioAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
//		
//		IndicadorBusiness.getInstance().alteraRealizadoIndicadorBonusPorCargo(indicadorCargoRealizadoVO);
//		setMensagem(pRequest, "Indicador processado com sucesso para o cargo selecionado.");
//		
//		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//			
//		return pMapping.findForward(PAGINA_INICIAL);
//	}
}