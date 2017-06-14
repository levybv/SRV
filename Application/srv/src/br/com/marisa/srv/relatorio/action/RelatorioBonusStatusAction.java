package br.com.marisa.srv.relatorio.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.relatorio.business.RelatorioBonusStatusBusiness;
import br.com.marisa.srv.util.tools.PoiExcel;

public class RelatorioBonusStatusAction extends BasicAction
{
  private static final String FORWARD_RELATORIO_BONUS_STATUS = "relatorio";
  private static final String FILENAME = "Relatorio.xml";
  private static final int BUFSIZE = 1024;

  public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
    throws Exception
  {
    pRequest.getSession().setAttribute("sessionTokenRefresh", Boolean.TRUE);
    return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
  }

  public ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
    throws Exception
  {
    List listaRelatorio = new ArrayList();
    List listaPeriodos = CalendarioComercialBusiness.getInstance().obtemListaPeriodoMesAno();
    pRequest.setAttribute("periodosMesAno", listaPeriodos);

    String status = getStringParam(pRequest, "status");
    String periodos = getStringParam(pRequest, "periodos");

    pRequest.setAttribute("periodos", periodos);
    pRequest.setAttribute("status", status);

    if (periodos != null) {
      Integer ano = new Integer(Integer.parseInt(periodos.substring(0, 4)));
      Integer mes = new Integer(Integer.parseInt(periodos.substring(periodos.length() - 2, periodos.length())));

      listaRelatorio = RelatorioBonusStatusBusiness.getInstance().pesquisaRelatorioBonusStatus(status, mes, ano, false);

      pRequest.setAttribute("listaRelatorioBonus", listaRelatorio);
    } else {
      listaRelatorio = RelatorioBonusStatusBusiness.getInstance().pesquisaRelatorioBonusStatus(status, null, null, false);
      pRequest.setAttribute("listaRelatorioBonus", listaRelatorio);
    }

    return pMapping.findForward(FORWARD_RELATORIO_BONUS_STATUS);
  }

  public ActionForward exportaRelatorioExcel(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse)
    throws Exception
  {
    try
    {
      String status = getStringParam(pRequest, "statusS");
      String periodos = getStringParam(pRequest, "periodosS");
      Integer ano = null;
      Integer mes = null;

      if (periodos != null) {
        ano = new Integer(Integer.parseInt(periodos.substring(0, 4)));
        mes = new Integer(Integer.parseInt(periodos.substring(periodos.length() - 2, periodos.length())));
      }

      List listaRelatorio = new ArrayList();
      listaRelatorio = RelatorioBonusStatusBusiness.getInstance().pesquisaRelatorioBonusStatus(status, mes, ano, true);

      List nomeCampos = new ArrayList();
      nomeCampos.add("Código Cargo");
      nomeCampos.add("Cargo");
      nomeCampos.add("Código Filial");
      nomeCampos.add("Código Funcionário");
      nomeCampos.add("Funcionário");
      nomeCampos.add("Situação RH");
      nomeCampos.add("Demissão");
      nomeCampos.add("Grupo Remuneração");
      nomeCampos.add("Ano");
      nomeCampos.add("Mês");
      nomeCampos.add("Status");

      List listaChaveMap = new ArrayList();
      listaChaveMap.add("CARGO");
      listaChaveMap.add("DESCR_CARGO");
      listaChaveMap.add("FILIAL");
      listaChaveMap.add("MATRICULA");
      listaChaveMap.add("FUNCIONARIO");
      listaChaveMap.add("SITUACAO_RH");
      listaChaveMap.add("DEMISSAO");
      listaChaveMap.add("GRP_REMUNERACAO");
      listaChaveMap.add("ANO");
      listaChaveMap.add("MES");
      listaChaveMap.add("STATUS");

      Map subTitulo = new HashMap();
      subTitulo.put("NOME", "Relatório Bônus por Status");

      String nomeArquivo = "bonus_status.xls";

      ByteArrayOutputStream baos = PoiExcel.montaPlanilha("PERÍODO: " + mes + "-" + ano + " / " + "STATUS: " + status, subTitulo, nomeCampos, listaRelatorio, listaChaveMap);

      int length = 0;
      ServletOutputStream op = pResponse.getOutputStream();
      pResponse.setContentType("application/octet-stream");
      pResponse.setContentLength(baos.size());

      if ((nomeArquivo == null) || (nomeArquivo.equals(""))) {
        nomeArquivo = FILENAME;
      }

      pResponse.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");

      byte[] bbuf = new byte[BUFSIZE];
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

      while ((bais != null) && ((length = bais.read(bbuf)) != -1))
      {
        op.write(bbuf, 0, length);
      }

      bais.close();
      op.flush();
      op.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}