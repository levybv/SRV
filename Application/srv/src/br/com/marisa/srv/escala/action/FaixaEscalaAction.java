package br.com.marisa.srv.escala.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.escala.business.FaixaEscalaBusiness;
import br.com.marisa.srv.escala.vo.FaixaEscalaVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Action para tratar as requisições de Faixas de Escalas
 * 
 * @author Walter Fontes
 */
public class FaixaEscalaAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_FAIXA_ESCALA = "faixaEscala";
		
	
	/**
	 * Realiza a consulta das escalas
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

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_ESCALA);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_ESCALA_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_ESCALA_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_ESCALA_ALTERACAO));

		Integer idEscala   = getIntegerParam(pRequest, "idEscalaF");
		pRequest.setAttribute("idEscalaF",	idEscala);
		
		if (idEscala != null) {
			List faixasEscalas = FaixaEscalaBusiness.getInstance().obtemFaixasEscala(idEscala);
			pRequest.setAttribute("faixasEscalas", faixasEscalas);
			
			HashMap faixasEscalasMap = new HashMap();
			if (ObjectHelper.isNotEmpty(faixasEscalas)) {
				Iterator itFaixasEscala = faixasEscalas.iterator();
				while (itFaixasEscala.hasNext()) {
					FaixaEscalaVO faixaEscalaVO = (FaixaEscalaVO)itFaixasEscala.next();
					faixasEscalasMap.put(faixaEscalaVO.getSequencial(), faixaEscalaVO);
				}
			}
			pRequest.getSession().setAttribute("faixasEscalasMap", faixasEscalasMap);
			pRequest.setAttribute("alteracoesPendentes", Boolean.FALSE);
		}
		
		return pMapping.findForward(FaixaEscalaAction.FORWARD_FAIXA_ESCALA);		
	}
	
	
	
	/**
	 * Monta lista através do Map
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	private ActionForward montaListaMap(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		Integer idEscala   = getIntegerParam(pRequest, "idEscalaF");
		pRequest.setAttribute("idEscalaF",	idEscala);
		
		HashMap faixasEscalas = (HashMap)pRequest.getSession().getAttribute("faixasEscalasMap");
		if (faixasEscalas == null) {
			throw new Exception("Lista de faixas de escalas não encontrada.");
		}
		
		//Organiza faixas em Lista
		List faixasEscalasLst = new ArrayList();
		if (faixasEscalas != null) {
			Iterator itKeys = faixasEscalas.keySet().iterator();
			while (itKeys.hasNext()) {
				Integer key = (Integer)itKeys.next();
				faixasEscalasLst.add(faixasEscalas.get(key));
			}
		}
		
		//Ordena por sequencia
		Collections.sort(faixasEscalasLst, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				FaixaEscalaVO fx0 = (FaixaEscalaVO)arg0;
				FaixaEscalaVO fx1 = (FaixaEscalaVO)arg1;
				return fx0.getSequencial().compareTo(fx1.getSequencial());
			}
		});		
		pRequest.setAttribute("faixasEscalas", faixasEscalasLst);
		return pMapping.findForward(FaixaEscalaAction.FORWARD_FAIXA_ESCALA);		
		
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
	public ActionForward alteraFaixaEscala(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		FaixaEscalaVO faixaEscalaVO = new FaixaEscalaVO();
		faixaEscalaVO.setIdEscala(getIntegerParam(pRequest, "idEscalaF"));
		faixaEscalaVO.setSequencial(getIntegerParam(pRequest, "sequencialES"));
		faixaEscalaVO.setFaixaInicial(getDoubleParam(pRequest, "faixaInicialES", true));
		faixaEscalaVO.setFaixaFinal(getDoubleParam(pRequest, "faixaFinalES", true));
		faixaEscalaVO.setIdUnidadeFaixa(getIntegerParam(pRequest, "idUnidadeFaixaES"));
		faixaEscalaVO.setRealizado(getDoubleParam(pRequest, "realizadoES", true));
		faixaEscalaVO.setIdUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoES"));
		//faixaEscalaVO.setLimite(getDoubleParam(pRequest, "limiteES", true));
		//faixaEscalaVO.setPercentual(getBooleanParam(pRequest, "percentualES"));
		faixaEscalaVO.setDataUltimaAlteracao(new Date());
		faixaEscalaVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

		HashMap faixasEscalasMap = (HashMap)pRequest.getSession().getAttribute("faixasEscalasMap");
		if (faixasEscalasMap == null) {
			throw new Exception("Lista de faixas de escalas não encontrada.");
		}
		
		Integer sequencialAntigo =  getIntegerParam(pRequest, "sequencial"); //O sequencial pode ter sido alterado
		faixasEscalasMap.remove(sequencialAntigo);
		
		faixasEscalasMap.put(faixaEscalaVO.getSequencial(), faixaEscalaVO);
		pRequest.getSession().setAttribute("faixasEscalasMap", faixasEscalasMap);
		//setMensagem(pRequest, "Escala alterada com sucesso");
		
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
		
		return montaListaMap(pMapping, pForm, pRequest, pResponse);
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
	public ActionForward incluiFaixaEscala(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		FaixaEscalaVO faixaEscalaVO = new FaixaEscalaVO();
		faixaEscalaVO.setIdEscala(getIntegerParam(pRequest, "idEscalaF"));
		faixaEscalaVO.setSequencial(getIntegerParam(pRequest, "sequencialES"));
		faixaEscalaVO.setFaixaInicial(getDoubleParam(pRequest, "faixaInicialES", true));
		faixaEscalaVO.setFaixaFinal(getDoubleParam(pRequest, "faixaFinalES", true));
		faixaEscalaVO.setIdUnidadeFaixa(getIntegerParam(pRequest, "idUnidadeFaixaES"));
		faixaEscalaVO.setRealizado(getDoubleParam(pRequest, "realizadoES", true));
		faixaEscalaVO.setIdUnidadeRealizado(getIntegerParam(pRequest, "idUnidadeRealizadoES"));
		//faixaEscalaVO.setLimite(getDoubleParam(pRequest, "limiteES", true));
		//faixaEscalaVO.setPercentual(getBooleanParam(pRequest, "percentualES"));
		faixaEscalaVO.setDataUltimaAlteracao(new Date());
		faixaEscalaVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

		HashMap faixasEscalasMap = (HashMap)pRequest.getSession().getAttribute("faixasEscalasMap");
		if (faixasEscalasMap == null) {
			throw new Exception("Lista de faixas de escalas não encontrada.");
		}
		faixasEscalasMap.put(faixaEscalaVO.getSequencial(), faixaEscalaVO);
		pRequest.getSession().setAttribute("faixasEscalasMap", faixasEscalasMap);
		//setMensagem(pRequest, "Escala incluída com sucesso");
		pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
		
		return montaListaMap(pMapping, pForm, pRequest, pResponse);
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
	public ActionForward excluiFaixaEscala(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		Integer sequencial = getIntegerParam(pRequest, "sequencial");
		
		HashMap faixasEscalasMap = (HashMap)pRequest.getSession().getAttribute("faixasEscalasMap");
		if (faixasEscalasMap == null) {
			throw new Exception("Lista de faixas de escalas não encontrada.");
		}
		faixasEscalasMap.remove(sequencial);
		pRequest.getSession().setAttribute("faixasEscalasMap", faixasEscalasMap);
		//setMensagem(pRequest, "Escala excluída com sucesso");			
		
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
		
		return montaListaMap(pMapping, pForm, pRequest, pResponse);
	}
	
	
	/**
	 * Salva todas as alterações
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward salvarAlteracoes(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		HashMap faixasEscalas = (HashMap)pRequest.getSession().getAttribute("faixasEscalasMap");
		if (faixasEscalas == null) {
			throw new Exception("Lista de faixas de escalas não encontrada.");
		}
		
		//Organiza faixas em Lista
		List faixasEscalasLst = new ArrayList();
		if (faixasEscalas != null) {
			Iterator itKeys = faixasEscalas.keySet().iterator();
			while (itKeys.hasNext()) {
				Integer key = (Integer)itKeys.next();
				faixasEscalasLst.add(faixasEscalas.get(key));
			}
		}
		
		//Ordena por sequencia
		Collections.sort(faixasEscalasLst, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				FaixaEscalaVO fx0 = (FaixaEscalaVO)arg0;
				FaixaEscalaVO fx1 = (FaixaEscalaVO)arg1;
				return fx0.getSequencial().compareTo(fx1.getSequencial());
			}
		});
		
		//Faz as consistências
		List escalasLst = new ArrayList();
		FaixaEscalaVO faixaEscalaAnteriorVO = null;
		
		int sequencia = 1;
		
		if (ObjectHelper.isNotEmpty(faixasEscalasLst)) {
			Iterator itFaixasEscalas = faixasEscalasLst.iterator();
			while (itFaixasEscalas.hasNext()) {
				FaixaEscalaVO faixaEscalaVO = (FaixaEscalaVO)itFaixasEscalas.next();
				
				if (faixaEscalaVO.getSequencial().intValue() == 1 &&
					faixaEscalaVO.getFaixaInicial().doubleValue() > 0) {
					setMensagem(pRequest, "A primeira faixa inicial não pode ser superior a 0,00.");
					pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
					return montaListaMap(pMapping, pForm, pRequest, pResponse);
				}
				if (faixaEscalaVO.getSequencial().intValue() != sequencia++) {
					setMensagem(pRequest, "A sequencia das faixas está inconsistente.");
					pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
					return montaListaMap(pMapping, pForm, pRequest, pResponse);
				}
				if (faixaEscalaAnteriorVO != null) {
					if (faixaEscalaVO.getFaixaInicial().doubleValue() <= faixaEscalaAnteriorVO.getFaixaFinal().doubleValue()) {
						setMensagem(pRequest, "A faixa inicial da sequencia " + faixaEscalaVO.getSequencial() + " não poder ser inferior ou igual a faixa final da sequencia " + faixaEscalaVO.getSequencial());
						pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
						return montaListaMap(pMapping, pForm, pRequest, pResponse);
					}
					double diferencaFaixa = faixaEscalaVO.getFaixaInicial().doubleValue() - faixaEscalaAnteriorVO.getFaixaFinal().doubleValue();
					
					//Garante apenas duas casas - para não ocorrer os problemas de arredondamento do double
					int diferencaFaixaInt = (int)(diferencaFaixa * 100);
					
					if (diferencaFaixaInt > 1) {
						setMensagem(pRequest, "A diferença do fim de uma faixa para o início da outra não pode ser superior a 0,01.");
						pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
						return montaListaMap(pMapping, pForm, pRequest, pResponse);
					}
					
				}
				if (faixaEscalaVO.getFaixaInicial().doubleValue() > faixaEscalaVO.getFaixaFinal().doubleValue()) {
					setMensagem(pRequest, "A faixa inicial da sequencia " + faixaEscalaVO.getSequencial() + " não poder ser superior a faixa final da sequencia " + faixaEscalaVO.getSequencial());
					pRequest.setAttribute("alteracoesPendentes", Boolean.TRUE);
					return montaListaMap(pMapping, pForm, pRequest, pResponse);
				}
				
				escalasLst.add(faixaEscalaVO);
				faixaEscalaAnteriorVO = faixaEscalaVO; 
			}
		}
		
		//Altera Faixas das escalas
		Integer idEscala = getIntegerParam(pRequest, "idEscalaF");
		FaixaEscalaBusiness.getInstance().alteraFaixasEscala(idEscala, escalasLst, null);
		
		pRequest.getSession().removeAttribute("escalasMap");
		setMensagem(pRequest, "Alterações das faixas de escala efetivadas com sucesso!");			
		
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		pRequest.setAttribute("alteracoesPendentes", Boolean.FALSE);
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}	
	
	
	/**
	 * Descarta todas as alterações
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward descartarAlteracoes(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		pRequest.getSession().removeAttribute("escalasMap");
		setMensagem(pRequest, "Alterações descartadas com sucesso!");			
		
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		pRequest.setAttribute("alteracoesPendentes", Boolean.FALSE);
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}		
}