package br.com.marisa.srv.indicador.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioVO;
import br.com.marisa.srv.indicador.vo.IndicadorLojaVO;

/**
 * Action para tratar as consultas de indicadores
 */
public class IndicadorRelatoriosAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_INDICADOR = "indicador";
	private static final String NOME_LISTA = "lista";
	private static final int GRUPO_VENDAS = 2;
	private static final int GRUPO_PRODUTOS_SERVICOS_FINANCEIROS = 3;
	private static final int GRUPO_EMPRESTIMO_PESSOAL = 4;
	private static final int GRUPO_EMPRESTIMO_PESSOAL_SAX = 5;
	private static final int GRUPO_CALL_CENTER = 6;
	
	/**
	 * 2 – VENDAS (slides 9, 10, 11)
       3 – PRODUTOS E SERVIÇOS FINANCEIROS (slides 17, 18, 19)
       4 – EMPRÉSTIMO PESSOAL (SAX) (slides 13, 14, 15)
       5 – SEGURO EMPRÉSTIMO PESSOAL (SAX) (slides 13, 14, 15)
	 */

	
	/**
	 * Cria um novo objeto PrincipalAction
	 * 
	 * @throws ExcecaoParametroInvalido
	 */
	public IndicadorRelatoriosAction() throws Exception {
		//
	}


	/**
	 * Exibe a consulta do calendário
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		montaPaginacao(pRequest);
		return pMapping.findForward(IndicadorRelatoriosAction.FORWARD_INDICADOR);
	}
	
	/**
	 * monta a paginacao
	 * @param pRequest
	 * @param lista
	 * @throws SRVException 
	 */
	private void montaPaginacao(HttpServletRequest pRequest) throws SRVException {

		List listaIndicadores = null;
		if(!isNuloOuBranco(pRequest.getParameter("grupos"))){
			listaIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadores(new Integer(pRequest.getParameter("grupos")));	
		}
		
		List listaLojas = FilialBusiness.getInstance().obtemListaFiliais(Constantes.TIPO_INT_NULO, Constantes.TIPO_INT_NULO, null);

		List listaMes = CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false);
		List listaAno = CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true);
		pRequest.setAttribute("listaMes", listaMes);
		pRequest.setAttribute("listaAno", listaAno);

		pRequest.setAttribute("listaLojas", listaLojas);
		pRequest.setAttribute("listaIndicadores", listaIndicadores);
		
		pRequest.setAttribute("grupoSelecionado",pRequest.getParameter("grupos"));
		pRequest.setAttribute("mesSelecionado",pRequest.getParameter("mes"));
		pRequest.setAttribute("anoSelecionado",pRequest.getParameter("ano"));
		pRequest.setAttribute("liderSelecionado",pRequest.getParameter("lider"));
		pRequest.setAttribute("indicadorSelecionado",pRequest.getParameter("indicadores"));
		
		if(!isNuloOuBranco(pRequest.getParameter("loja"))) {
			String str[] = pRequest.getParameter("loja").trim().split("@");
			Integer idLoja = new Integer(str[0]);
			Integer idEmpresa = new Integer(str[1]);
			pRequest.setAttribute("lojaSelecionada", idLoja);
			pRequest.setAttribute("empresaSelecionada", idEmpresa);
		}
	}
	
	
	public ActionForward executarFiltro(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		List lista = null;
		Integer mes = getIntegerParam(pRequest, "mes");
		Integer ano = getIntegerParam(pRequest, "ano");
		Integer idLider = isNuloOuBranco(pRequest.getParameter("lider"))?null:new Integer(pRequest.getParameter("lider"));
		Integer idLoja = null;
		Integer idEmpresa = null;
		Integer codIndicador = null;
		if(!isNuloOuBranco(pRequest.getParameter("loja"))) {
			String str[] = pRequest.getParameter("loja").trim().split("@");
			idLoja = new Integer(str[0]);
			idEmpresa = new Integer(str[1]);
		}
		Integer grupos = new Integer(pRequest.getParameter("grupos"));
		codIndicador = new Integer(pRequest.getParameter("indicadores"));
		Integer tabSelecionada = new Integer(isNuloOuBranco(pRequest.getParameter("tabSelecionada"))?"1": pRequest.getParameter("tabSelecionada"));
		if(grupos.intValue() != GRUPO_VENDAS && tabSelecionada.intValue() == 4) {
			tabSelecionada = new Integer(1);
		}
		
		Long matricula = null;
		if (obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula() != null) {
			matricula = new Long(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getMatricula());
		}
		
		if(tabSelecionada.intValue() == 1) {
			lista = IndicadorBusiness.getInstance().obtemRelatorioLoja(idLider, idLoja, idEmpresa, codIndicador, ano, mes, grupos, matricula);
			montaTotais(pRequest,lista, true);
		}else {
			lista = IndicadorBusiness.getInstance().obtemRelatorioOperacional(idLider, idLoja, idEmpresa, obtemDescricaoGrupoRemunerado(grupos.intValue(), 
																			  tabSelecionada.intValue()), codIndicador, ano, mes, grupos, matricula);
			montaTotais(pRequest,lista, false);
		}
		
		pRequest.setAttribute("tabSelecionada",tabSelecionada);
		pRequest.setAttribute(NOME_LISTA,lista);
		montaPaginacao(pRequest);
		return pMapping.findForward(IndicadorRelatoriosAction.FORWARD_INDICADOR);
	}


	private void montaTotais(HttpServletRequest request, List lista, boolean isLoja) {
		if(lista!= null) {
			
			DecimalFormat  formatadorDouble  = new DecimalFormat();
			DecimalFormat  formatadorInteiro  = new DecimalFormat();
			
			double basePremioTotal = 0;
			double premioTotal = 0;
			double valorPremioTotal = 0;

			int numMeta = 0;
			int numRealizado = 0;
			double numRealizadoMeta = 0;
			double vlrPremioFilial = 0;
			double vlrPremioFilialCalculado = 0;
			double quantidadeRealizada = 0;
			double vlrRealizadoTotal = 0;
			
			
			if(isLoja) {
				for(int i=0;i<lista.size();i++) {
					IndicadorLojaVO indicadorLojaVO = (IndicadorLojaVO)lista.get(i);
					if(indicadorLojaVO.getNumMeta() != null) {
						numMeta += indicadorLojaVO.getNumMeta().intValue();
					}
					if(indicadorLojaVO.getNumRealizado() != null) {
						numRealizado += indicadorLojaVO.getNumRealizado().intValue();
					}
					if(indicadorLojaVO.getVlrPremioFilialCalculado() != null) {
						vlrPremioFilialCalculado += indicadorLojaVO.getVlrPremioFilialCalculado().doubleValue();
					}
					if(indicadorLojaVO.getVlrPremioFilial() != null) {
						vlrPremioFilial += indicadorLojaVO.getVlrPremioFilial().doubleValue();
					}
					if(indicadorLojaVO.getQuantidadeRealizada() != null) {
						quantidadeRealizada += indicadorLojaVO.getQuantidadeRealizada().doubleValue();
					}
				}
				
				if (numMeta > 0) {
					numRealizadoMeta = ((double)numRealizado / (double)numMeta) * (double)100;
				}
				  
			}else {
			
				for(int i=0;i<lista.size();i++) {
					IndicadorFuncionarioVO indicadorFuncionarioVO = (IndicadorFuncionarioVO)lista.get(i);
					if(indicadorFuncionarioVO.getBasePremio() != null) {
						basePremioTotal += indicadorFuncionarioVO.getBasePremio().doubleValue();
					}
					if(indicadorFuncionarioVO.getPremio() != null) {
						premioTotal += indicadorFuncionarioVO.getPremio().doubleValue();	
					}
					if(indicadorFuncionarioVO.getValorPremio() != null) {
						valorPremioTotal += indicadorFuncionarioVO.getValorPremio().doubleValue();
					}
					if(indicadorFuncionarioVO.getValorRealizado() != null) {
						vlrRealizadoTotal += indicadorFuncionarioVO.getValorRealizado().doubleValue();
					}
					if(indicadorFuncionarioVO.getMeta() != null) {
						numMeta += indicadorFuncionarioVO.getMeta().intValue();
					}
					if(indicadorFuncionarioVO.getNumRealizadoMeta() != null) {
						numRealizadoMeta += indicadorFuncionarioVO.getNumRealizadoMeta().doubleValue();
					}
				}
			}
			BigDecimal basePremioTotalB = new BigDecimal(basePremioTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal premioTotalB = new BigDecimal(premioTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal valorPremioTotalB = new BigDecimal(valorPremioTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			
			BigDecimal numRealizadoMetaTotalB = new BigDecimal(numRealizadoMeta).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal vlrPremioFilialTotalB = new BigDecimal(vlrPremioFilial).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal vlrPremioFilialCalculadoTotalB = new BigDecimal(vlrPremioFilialCalculado).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal quantidadeRealizadaTotalB = new BigDecimal(quantidadeRealizada).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal vlrRealizadoTotalB = new BigDecimal(vlrRealizadoTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			  
		    formatadorDouble.applyPattern(" ###,##0.00; (###,###,###,##0.00)");
		    formatadorInteiro.applyPattern(" ###,##0; (###,##0)");
					
			request.setAttribute("basePremioTotal",formatadorDouble.format(basePremioTotalB));
			request.setAttribute("premioTotal",formatadorDouble.format(premioTotalB));
			request.setAttribute("valorPremioTotal",formatadorDouble.format(valorPremioTotalB));
			request.setAttribute("vlrRealizadoTotal",formatadorDouble.format(vlrRealizadoTotalB));
			
			request.setAttribute("numRealizadoMetaTotal",formatadorDouble.format(numRealizadoMetaTotalB));
			request.setAttribute("vlrPremioFilialTotal",formatadorDouble.format(vlrPremioFilialTotalB));
			request.setAttribute("vlrPremioFilialCalculadoTotal",formatadorDouble.format(vlrPremioFilialCalculadoTotalB));
			request.setAttribute("quantidadeRealizadaTotal",formatadorInteiro.format(quantidadeRealizadaTotalB));
			
			
			request.setAttribute("numMetaTotal",formatadorInteiro.format(numMeta));
			request.setAttribute("numRealizadoTotal",formatadorInteiro.format(numRealizado));
			
		}
		
	}


	private boolean isNuloOuBranco(String parameter) {
		return parameter == null || "".equals(parameter);
	}
	
	/**
	 * 2 – VENDAS (slides 9, 10, 11)
3 – PRODUTOS E SERVIÇOS FINANCEIROS (slides 17, 18, 19)
4 – EMPRÉSTIMO PESSOAL (SAX) (slides 13, 14, 15)
5 – SEGURO EMPRÉSTIMO PESSOAL (SAX) (slides 13, 14, 15)

	 * @param grupo
	 * @param abaSelecionada
	 * @return
	 */
	private String obtemDescricaoGrupoRemunerado(int grupo, int abaSelecionada) {
		String resp = null;
		switch (grupo) {
			case GRUPO_VENDAS:
				if(abaSelecionada == 2) {
					resp =  "OPERACIONAL_LOJA";
				}else if(abaSelecionada == 4) {
					resp = "VISUAL_MERCHANDISING_LOJA";
				}else {
					resp = "LIDERANCA_LOJA";
				}
				break;
				
			case GRUPO_PRODUTOS_SERVICOS_FINANCEIROS:
				if(abaSelecionada == 2) {
					resp =  "OPERACIONAL_LOJA";
				}else {
					resp = "LIDERANCA_LOJA";
				}
				break;	
				
			case GRUPO_EMPRESTIMO_PESSOAL:
				if(abaSelecionada == 2) {
					resp =  "OPERACIONAL_SAX_EP";
				}else {
					resp = "LIDERANCA_SAX_EP";
				}
				break;
				
			case GRUPO_EMPRESTIMO_PESSOAL_SAX:
				if(abaSelecionada == 2) {
					resp =  "OPERACIONAL_SAX_SEGURO (EP)";
				}else {
					resp = "LIDERANCA_SAX_SEGURO (EP)";
				}
				break;
				
			case GRUPO_CALL_CENTER:
				if(abaSelecionada == 2) {
					resp =  "OPERACIONAL_CALL_CENTER";
				}else {
					resp = "LIDERANCA_CALL_CENTER";
				}
				break;
		}
		return resp;
	}
	
	
}