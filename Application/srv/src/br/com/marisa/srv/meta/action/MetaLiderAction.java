package br.com.marisa.srv.meta.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.filtro.vo.FiltroMetaLiderVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.excecoes.ApresentacaoException;
import br.com.marisa.srv.meta.business.MetaLiderBusiness;
import br.com.marisa.srv.meta.vo.MetaLiderVO;

/**
 * Action para tratar as requisições de Acompanhamento de Metas de Filiais
 * 
 * @author Walter Fontes
 */
public class MetaLiderAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_META_LIDER = "metaLider";
		
	
	/**
	 * Realiza a consulta dos acompanhamentos das metas de filiais
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse, null);
	}
	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param req
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward removeMetaLider(ActionMapping pMapping, ActionForm pForm, HttpServletRequest req, HttpServletResponse pResponse) throws Exception {
		String[] dadosRemover = getStringParam(req, "checkbox1").split(";");
		MetaLiderVO metaLiderVO = new MetaLiderVO();
		metaLiderVO.setCodFuncionario(new Integer(dadosRemover[0]));
		metaLiderVO.setCodIndicador(new Integer(dadosRemover[1]));
		metaLiderVO.setAno(new Integer(dadosRemover[2]));
		metaLiderVO.setMes(new Integer(dadosRemover[3]));
		MetaLiderBusiness.getInstance().removeMetaLider(metaLiderVO);
		setMensagem(req, "Meta excluida com sucesso.");
		return montaTelaPrincipal(pMapping, pForm, req, pResponse, obtemFiltro(req,Boolean.FALSE));
	}
	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param req
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward altualiza(ActionMapping pMapping, ActionForm pForm, HttpServletRequest req, HttpServletResponse pResponse) throws Exception {
		MetaLiderVO metaLiderVO = new MetaLiderVO();
		String mesAno = getStringParam(req, "mesesAlteracao");
		metaLiderVO.setMes(Integer.parseInt(mesAno.substring(4))+1);
		metaLiderVO.setAno(Integer.parseInt(mesAno.substring(0,4)));
		metaLiderVO.setCodFuncionario( getIntegerParam(req, "idLideresAlteracao"));
		metaLiderVO.setCodIndicador( getIntegerParam(req, "idIndicadorAlteracao"));
		metaLiderVO.setEquipe(getStringParam(req, "idEquipeAlteracao"));
		metaLiderVO.setMeta(getDoubleParam(req, "valorMetaAlteracao",Boolean.TRUE));
		String dadosAnteriores = getStringParam(req, "dadosAnteriores");
		MetaLiderBusiness.getInstance().atualizaMetaLider(metaLiderVO,obtemUsuarioDaSessao(req).getUsuarioVO(),dadosAnteriores);
		setMensagem(req, "Meta salva com sucesso.");
		return montaTelaPrincipal(pMapping, pForm, req, pResponse, obtemFiltro(req,Boolean.FALSE));
	}
	
	/**
	 * Realiza a consulta dos acompanhamentos das metas de filiais
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward pesquisa(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse,obtemFiltro(pRequest,Boolean.TRUE));
	}
	
	/**
	 * Realiza pesquisa com o filtro da sessao dando refresh na tela
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward pesquisaRetorno(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse,obtemFiltro(pRequest,Boolean.FALSE));
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
	public ActionForward inclui(ActionMapping pMapping, ActionForm pForm, HttpServletRequest req, HttpServletResponse pResponse) throws Exception {
		MetaLiderVO metaLiderVO = new MetaLiderVO();
		String mesAno = getStringParam(req, "mesesInclusao");
		metaLiderVO.setMes(Integer.parseInt(mesAno.substring(4))+1);
		metaLiderVO.setAno(Integer.parseInt(mesAno.substring(0,4)));
		metaLiderVO.setCodFuncionario( getIntegerParam(req, "idLideresInclusao"));
		metaLiderVO.setCodIndicador( getIntegerParam(req, "idIndicador"));
		metaLiderVO.setEquipe(getStringParam(req, "idEquipeInclusao"));
		metaLiderVO.setMeta(getDoubleParam(req, "valorMetaInclusao",Boolean.TRUE));
		MetaLiderBusiness.getInstance().incluiMetaLider(metaLiderVO,obtemUsuarioDaSessao(req).getUsuarioVO());
		setMensagem(req, "Meta salva com sucesso.");
		return montaTelaPrincipal(pMapping, pForm, req, pResponse,obtemFiltro(req,Boolean.FALSE));
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
	private ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse, FiltroMetaLiderVO filtroMetaLiderVO) throws Exception {
		if(filtroMetaLiderVO != null){
			pRequest.setAttribute("listaMetas",MetaLiderBusiness.getInstance().obtemListaMetasLider(filtroMetaLiderVO));
		}
		pRequest.setAttribute("listaLideres", MetaLiderBusiness.getInstance().obtemListaLideres());
		pRequest.setAttribute("filtro",filtroMetaLiderVO);
		pRequest.setAttribute("listaIndicador",MetaLiderBusiness.getInstance().obtemListaIndicadores());
		pRequest.setAttribute("listaEquipes",MetaLiderBusiness.getInstance().obtemListaEquipes());
		pRequest.setAttribute("mesPermissaoAlteracao",MetaLiderBusiness.getInstance().podeEditarMesAnterior()?1:0);
		return pMapping.findForward(MetaLiderAction.FORWARD_META_LIDER);		
	}


	private FiltroMetaLiderVO obtemFiltro(HttpServletRequest pRequest,Boolean isFromPagina) throws ApresentacaoException {
		FiltroMetaLiderVO filtroMetaLiderVO = null;
		if(isFromPagina){
			filtroMetaLiderVO = new FiltroMetaLiderVO();
			String mesAno = getStringParam(pRequest, "mesesF");
			if(mesAno != null){
				filtroMetaLiderVO.setMes(Integer.parseInt(mesAno.substring(4))+1);
				filtroMetaLiderVO.setAno(Integer.parseInt(mesAno.substring(0,4)));
			}
			filtroMetaLiderVO.setMesAno(mesAno);
			filtroMetaLiderVO.setCodIndicador(getIntegerParam(pRequest, "indicadorF"));
			filtroMetaLiderVO.setCodFuncionario(getIntegerParam (pRequest, "liderF"));
			filtroMetaLiderVO.setEquipe(getStringParam(pRequest, "equipeF"));
			obtemUsuarioDaSessao(pRequest).setFiltroGenerico(filtroMetaLiderVO);
		}else{
			filtroMetaLiderVO = (FiltroMetaLiderVO) obtemUsuarioDaSessao(pRequest).getFiltroGenerico();
		}
		return filtroMetaLiderVO;
	}
	
}