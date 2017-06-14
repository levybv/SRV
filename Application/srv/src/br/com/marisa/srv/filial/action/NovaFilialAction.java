package br.com.marisa.srv.filial.action;

import br.com.marisa.srv.filial.business.NovaFilialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

public class NovaFilialAction extends BasicAction
{

    private static final String FORWARD_NOVA_FILIAL = "novaFilial";
    private static final String NOME_LISTA = "listaFiliais";

    public NovaFilialAction()
        throws Exception
    {
    }

    public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
        throws Exception
    {
        java.util.List listaFiliais = NovaFilialBusiness.getInstance().montaTelaInicial(-1, null, null, null);
        pRequest.setAttribute("listaFiliais", listaFiliais);
        return pMapping.findForward("novaFilial");
    }
}
