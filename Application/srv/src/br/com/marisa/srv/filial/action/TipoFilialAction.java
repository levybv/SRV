package br.com.marisa.srv.filial.action;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.filial.business.TipoFilialBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.action.BasicAction;

public class TipoFilialAction extends BasicAction
{

    private static final String FORWARD_TIPO_FILIAL = "tipoFilial";

    public TipoFilialAction()
    {
    }

    public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        pRequest.getSession().setAttribute("sessionTokenRefresh", Boolean.TRUE);
        return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
    }

    private ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        String descricaoFilial = pRequest.getParameter("dscrFilial");
        java.util.List listaTipoFiliais = new ArrayList();
        listaTipoFiliais = TipoFilialBusiness.getInstance().montaTelaTipoFilial(descricaoFilial);
        pRequest.setAttribute("listaTipoFiliais", listaTipoFiliais);
        pRequest.getSession().setAttribute("sessionTokenRefresh", Boolean.TRUE);
        return pMapping.findForward("tipoFilial");
    }

    public ActionForward incluirTipoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        FilialVO filialVO = new FilialVO();
        filialVO.setDescricao(getStringParam(pRequest, "dscrFilial"));
        filialVO.setDataUltimaAlteracao(new Date());
        filialVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
        TipoFilialBusiness.getInstance().incluirTipoFilial(filialVO);
        setMensagem(pRequest, "Tipo Filial Inclu\355do com Sucesso");
        pRequest.getSession().removeAttribute("sessionTokenRefresh");
        return pMapping.findForward("paginaInicial");
    }

    public ActionForward alterarTipoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        FilialVO filialNova = new FilialVO();
        FilialVO filialSelecionada = new FilialVO();
        FilialVO filialEncontrada = new FilialVO();
        filialSelecionada.setCodFilial(getIntegerParam(pRequest, "codTipoFilialF"));
        filialSelecionada.setDescricao(getStringParam(pRequest, "descrTipoFilialF"));
        filialSelecionada.setIdUsuarioUltimaAlteracao(getIntegerParam(pRequest, "codUsuTipoFilialF"));
        filialNova.setCodFilial(getIntegerParam(pRequest, "codTipoFilialF"));
        filialNova.setDescricao(getStringParam(pRequest, "dscrTipoFilial"));
        filialNova.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
        filialNova.setDataUltimaAlteracao(new Date());
        filialEncontrada = TipoFilialBusiness.getInstance().obtemUltimaFilial(filialSelecionada);
        if(filialEncontrada.getCodFilial() != null)
        {
            TipoFilialBusiness.getInstance().incluirTipoFilialHistorico(filialEncontrada);
            TipoFilialBusiness.getInstance().alterarTipoFilial(filialNova);
            setMensagem(pRequest, "Tipo Filial Alterado com Sucesso");
            pRequest.getSession().removeAttribute("sessionTokenRefresh");
        }
        return pMapping.findForward("paginaInicial");
    }

    public ActionForward excluirTipoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        FilialVO filialVO = new FilialVO();
        FilialVO filialEncontrada = new FilialVO();
        filialVO.setCodFilial(getIntegerParam(pRequest, "codTipoFilialF"));
        filialVO.setDescricao(getStringParam(pRequest, "descrTipoFilialF"));
        filialVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
        filialEncontrada = TipoFilialBusiness.getInstance().obtemUltimaFilial(filialVO);
        if(filialEncontrada.getCodFilial() != null)
        {
            TipoFilialBusiness.getInstance().incluirTipoFilialHistorico(filialEncontrada);
            TipoFilialBusiness.getInstance().excluirTipoFilial(filialVO);
            setMensagem(pRequest, "Tipo Filial Exclu\355do com Sucesso");
            pRequest.getSession().removeAttribute("sessionTokenRefresh");
        }
        return pMapping.findForward("paginaInicial");
    }

}
