package br.com.marisa.srv.relatorio.business;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioBonusStatusDAO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class RelatorioBonusStatusBusiness
{
  private static final Logger log = Logger.getLogger(RelatorioBonusStatusBusiness.class);

  private static RelatorioBonusStatusBusiness instance = new RelatorioBonusStatusBusiness();

  public static final RelatorioBonusStatusBusiness getInstance()
  {
    return instance;
  }

  public List pesquisaRelatorioBonusStatus(String status, Integer mes, Integer ano, boolean isExcel)
    throws SRVException
  {
    List lista = new ArrayList();
    RelatorioBonusStatusDAO dao = new RelatorioBonusStatusDAO();
    try
    {
      lista = dao.pesquisaRelatorioBonusStatus(status, mes, ano, isExcel);
    } catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
    } finally {
      dao.closeConnection();
    }

    return lista;
  }
}