package br.com.marisa.srv.funcionario.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.CNPJCPFHelper;
import br.com.marisa.srv.geral.helper.NumeroHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioVO;

/**
 * Action para lidar com requisicoes de extrato de funcionarios
 * 
 * @author Walter Fontes
 */
public class ExtratoFuncionarioAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_PESQUISA_FUNCIONARIOS 	= "funcionario";
	private static final String FORWARD_EXTRATO_FUNCIONARIO 	= "extratoFuncionario";

	
	/**
	 * Cria um novo objeto ExtratoFuncionarioAction
	 * 
	 * @throws Exception
	 */
	public ExtratoFuncionarioAction() throws Exception {
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
	
		Long    idFuncionario   = getLongParam(pRequest, "idFuncionario");
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
			
			Long matricula = null;
			if (obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula() != null) {
				matricula = new Long(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula());
			}			
			
			//List funcionarios = FuncionarioBusiness.getInstance().obtemFuncionariosPorFiliais(idFuncionario, nomeFuncionario, cracha, cpfFuncionarioL, matricula);
			pRequest.setAttribute("funcionarios", new ArrayList<FuncionarioVO>());//funcionarios);
		}
		
		return pMapping.findForward(FORWARD_PESQUISA_FUNCIONARIOS);
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
	public ActionForward pesquisaExtratoFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
	
		Integer idFuncionarioSelecionado 			= getIntegerParam(pRequest, "idFuncionarioSelecionado");
		String  nomeFuncionarioSelecionado 			= getStringParam(pRequest,  "nomeFuncionarioSelecionado");
		String  cargoFuncionarioSelecionado 		= getStringParam(pRequest,  "cargoFuncionarioSelecionado");
		String  centroCustoFuncionarioSelecionado 	= getStringParam(pRequest,  "centroCustoFuncionarioSelecionado");
		Integer idFuncionarioFiltro 				= getIntegerParam(pRequest, "idFuncionario");
		String  nomeFuncionarioFiltro 				= getStringParam(pRequest,  "nomeFuncionario");
		String  crachaFiltro 						= getStringParam(pRequest,  "cracha");
		String  cpfFuncionarioFiltro 				= getStringParam(pRequest,  "cpfFuncionario");
		Integer mesFiltro  							= getIntegerParam(pRequest,  "mes");
		Integer anoFiltro  							= getIntegerParam(pRequest,  "ano");

		pRequest.setAttribute("idFuncionarioSelecionado", 			idFuncionarioSelecionado);
		pRequest.setAttribute("nomeFuncionarioSelecionado", 		nomeFuncionarioSelecionado);
		pRequest.setAttribute("cargoFuncionarioSelecionado", 		cargoFuncionarioSelecionado);
		pRequest.setAttribute("centroCustoFuncionarioSelecionado", 	centroCustoFuncionarioSelecionado);		
		pRequest.setAttribute("idFuncionario", 						idFuncionarioFiltro);
		pRequest.setAttribute("nomeFuncionario", 					nomeFuncionarioFiltro);
		pRequest.setAttribute("cracha", 							crachaFiltro);
		pRequest.setAttribute("cpfFuncionario", 					cpfFuncionarioFiltro);
		pRequest.setAttribute("mesSelecionado",		 				mesFiltro);
		pRequest.setAttribute("anoSelecionado",		 				anoFiltro);

		List listaAno = CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true);
		List listaMes = CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false);
		pRequest.setAttribute("listaAno", listaAno);
		pRequest.setAttribute("listaMes", listaMes);

		if (ObjectHelper.isNotEmpty(mesFiltro) && ObjectHelper.isNotEmpty(anoFiltro)) {
			
			double totalMeta 			= 0;
			double totalRealizado 		= 0;
			double totalRealizadoXMeta 	= 0;
			double totalPremio 			= 0;
			
			List indicadores = IndicadorBusiness.getInstance().obtemRelatorioOperacionalPorFuncionario(idFuncionarioSelecionado, mesFiltro, anoFiltro);
			pRequest.setAttribute("lista", indicadores);
			
			if (ObjectHelper.isNotEmpty(indicadores)) {
				Iterator itIndicadores = indicadores.iterator();
				while (itIndicadores.hasNext()) {
					IndicadorFuncionarioVO indicadorFuncionarioVO = (IndicadorFuncionarioVO)itIndicadores.next(); 
					if (ObjectHelper.isNotEmpty(indicadorFuncionarioVO.getMeta())) {
						totalMeta += indicadorFuncionarioVO.getMeta().doubleValue();
					}
					if (ObjectHelper.isNotEmpty(indicadorFuncionarioVO.getValorRealizado())) {
						totalRealizado += indicadorFuncionarioVO.getValorRealizado().doubleValue();
					}
					if (ObjectHelper.isNotEmpty(indicadorFuncionarioVO.getPremio())) {
            totalPremio += indicadorFuncionarioVO.getPremio().doubleValue();
					}
				}
				if (totalMeta > 0) {
					totalRealizadoXMeta = (totalRealizado / totalMeta) * 100;
				}
			}
			
			pRequest.setAttribute("totalMeta", 				NumeroHelper.formataNumero(new Double(totalMeta), new Integer(Constantes.UNIDADE_UNIDADE)));
			pRequest.setAttribute("totalRealizado", 		NumeroHelper.formataNumero(new Double(totalRealizado), new Integer(Constantes.UNIDADE_UNIDADE)));
			
			pRequest.setAttribute("totalRealizadoXMeta", 	NumeroHelper.formataValor(new Double(totalRealizadoXMeta)));
			pRequest.setAttribute("totalPremio", 			NumeroHelper.formataValor(new Double(totalPremio)));
		}
		return pMapping.findForward(FORWARD_EXTRATO_FUNCIONARIO);
	}
}