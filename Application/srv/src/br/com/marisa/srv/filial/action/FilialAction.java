package br.com.marisa.srv.filial.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;

/**
 * Data de Criação: 04/08/2011
 * 
 * @author Walter Fontes
 */
public class FilialAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_FILIAL = "filial";
	private static final String NOME_LISTA = "listaFiliais";
	
	

	
	/**
	 * Cria um novo objeto PrincipalAction
	 * 
	 * @throws ExcecaoParametroInvalido
	 */
	public FilialAction() throws Exception {
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
		
		List listaFiliais = FilialBusiness.getInstance().obtemListaFiliais(Constantes.TIPO_INT_NULO,Constantes.TIPO_INT_NULO);
		List listaTipoFiliais = FilialBusiness.getInstance().obterListaTipoFiliais();
		montaPaginacao(pRequest, listaFiliais, listaTipoFiliais);
		
		return pMapping.findForward(FilialAction.FORWARD_FILIAL);
	}
	
	/**
	 * monta a paginacao
	 * @param pRequest
	 * @param lista
	 */
	private void montaPaginacao(HttpServletRequest pRequest,List lista, List listaTipoFiliais) {
		pRequest.setAttribute(NOME_LISTA, lista);
		pRequest.setAttribute("listaTipoFil", listaTipoFiliais);
		pRequest.setAttribute("codFilial", pRequest.getParameter("codFilial"));
		pRequest.setAttribute("apenasAtivas", pRequest.getParameter("apenasAtivas"));
	}

    public ActionForward incluirFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        FilialVO filialVO = new FilialVO();
        filialVO.setCodEmpresa(new Integer(1));
        filialVO.setCodTpFilial(getIntegerParam(pRequest, "idDescrTipoFiliais"));
        filialVO.setCodFilial(getIntegerParam(pRequest, "codFil"));
        filialVO.setDescricao(getStringParam(pRequest, "descricao").toUpperCase());
        filialVO.setCnpj(getStringParam(pRequest, "cnpj"));
        filialVO.setUf(getStringParam(pRequest, "uf").toUpperCase());
        filialVO.setFlagAtivo(getBooleanParam(pRequest, "checkAtivo"));
        filialVO.setDataUltimaAlteracao(new Date());
        filialVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
        filialVO.setFlagMeta100(getBooleanParam(pRequest, "percentualMeta"));
        filialVO.setDataInauguracao(getDateParam(pRequest, "dataInaug"));
        if(FilialBusiness.getInstance().obterFilial(filialVO.getCodEmpresa(), filialVO.getCodFilial()))
        {
            FilialBusiness.getInstance().incluirFilial(filialVO);
            setMensagem(pRequest, "Filial incluída com sucesso");
        } else
        {
            setMensagem(pRequest, "Filial já cadastrada para esse código");
        }
        List listaFiliais = FilialBusiness.getInstance().obtemListaFiliais(-1, -1);
        List tipoFiliais = FilialBusiness.getInstance().obterListaTipoFiliais();
        montaPaginacao(pRequest, listaFiliais, tipoFiliais);
        pRequest.getSession().removeAttribute("sessionTokenRefresh");
        return pMapping.findForward("filial");
    }

	
	/**
	 * Altera filial
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward alterarModalFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		FilialVO filialVO = new FilialVO();
		filialVO.setCodEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
        filialVO.setCodFilial(getIntegerParam(pRequest, "codFilial1"));
        filialVO.setDescricao(getStringParam(pRequest, "descricao").toUpperCase());
        filialVO.setCnpj(getStringParam(pRequest, "cnpj"));
        filialVO.setFlagAtivo(getBooleanParam(pRequest, "checkAtivo"));
        filialVO.setFlagMeta100(getBooleanParam(pRequest, "percentualMeta"));
        filialVO.setCodTpFilial(getIntegerParam(pRequest, "idDescrTipoFiliais"));
        filialVO.setDataUltimaAlteracao(new Date());
        filialVO.setDataInauguracao(getDateParam(pRequest, "dataInaug"));
        filialVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
		
		FilialBusiness.getInstance().alterarFilial(filialVO);
		
		setMensagem(pRequest, "Filial alterada com sucesso");
		List listaFiliais = FilialBusiness.getInstance().obtemListaFiliais(Constantes.TIPO_INT_NULO,Constantes.TIPO_INT_NULO);
		List tipoFiliais = FilialBusiness.getInstance().obterListaTipoFiliais();
		montaPaginacao(pRequest, listaFiliais, tipoFiliais);
		pRequest.getSession().removeAttribute("sessionTokenRefresh");

		return pMapping.findForward(PAGINA_INICIAL);
	
	}
	
	
	/**
	 * Reliza a pesquisa de filiais conforme o filtro informado
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward executarFiltro(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
        int codFilial = getIntParam(pRequest, "codFilial");
        Integer codTipoFilial = getIntegerParam(pRequest, "idDescrTipoFil");
        boolean apenasAtivas = getBoolParam(pRequest, "apenasAtivas");
        List listaFiliais = null;
        List listaTipoFiliais = null;
        if(codTipoFilial != null)
        {
            listaFiliais = FilialBusiness.getInstance().pesquisarListaTipoFiliais(codTipoFilial);
        } else
        if(apenasAtivas)
        {
            listaFiliais = FilialBusiness.getInstance().obtemListaFiliais(Constantes.CODIGO_EMPRESA, codFilial, Boolean.TRUE);
        } else
        {
            listaFiliais = FilialBusiness.getInstance().obtemListaFiliais(Constantes.CODIGO_EMPRESA, codFilial);
        }
        if(listaFiliais.isEmpty())
        {
            setMensagem(pRequest, "Nenhum dado foi encontrato na pequisa");
        }
        if(listaFiliais.size() > 0)
        {
            listaTipoFiliais = FilialBusiness.getInstance().obterListaTipoFiliais();
            montaPaginacao(pRequest, listaFiliais, listaTipoFiliais);
        } else
        {
            listaFiliais = FilialBusiness.getInstance().obtemListaFiliais(Constantes.TIPO_INT_NULO,Constantes.TIPO_INT_NULO);
            montaPaginacao(pRequest, listaFiliais, listaTipoFiliais);
        }
        listaTipoFiliais = FilialBusiness.getInstance().obterListaTipoFiliais();
        montaPaginacao(pRequest, listaFiliais, listaTipoFiliais);
        return pMapping.findForward(FORWARD_FILIAL);
	}
	
	
}