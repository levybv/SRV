package br.com.marisa.srv.relatorio.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioVmDAO;
import br.com.marisa.srv.relatorio.vo.RelatorioLojaVO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

@Deprecated
public class RelatorioVmBusiness
{
  private static final Logger log = Logger.getLogger(RelatorioVmBusiness.class);

  private static RelatorioVmBusiness instance = new RelatorioVmBusiness();

  public static final RelatorioVmBusiness getInstance()
  {
    return instance;
  }

  public List obtemRelatorioPagamentoVm(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioVmDAO dao = new RelatorioVmDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioPagamentoVm(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "Não foi possível obter relatório de Pagamento VM.", e);
    } finally {
      dao.closeConnection();
    }

    return listaRelatorio;
  }
}