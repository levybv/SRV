package br.com.marisa.srv.relatorio.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioSaxDAO;
import br.com.marisa.srv.relatorio.vo.RelatorioLojaVO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

@Deprecated
public class RelatorioSaxBusiness
{
  private static final Logger log = Logger.getLogger(RelatorioSaxBusiness.class);

  private static RelatorioSaxBusiness instance = new RelatorioSaxBusiness();

  public static final RelatorioSaxBusiness getInstance()
  {
    return instance;
  }

  public List obtemRelatorioIndicadoresSaxEpSeguros(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioIndicadoresSaxEpSeguros(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio Indicadores Sax Ep Seguros.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioDemonstrativoTodasLojas(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioDemonstrativoTodasLojas(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio Demonstrativo de Todas as Lojas", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioLojasSemMetas(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioLojasSemMetas(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio de Lojas sem Metas", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioLojasSemGerentesApuracaoSax(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioLojasSemGerentesApuracaoSax(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio sem gerentes com Apura��o de Sax no m�s de campanha.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioLojasSemGerentesCrediarioApuracaoSax(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioLojasSemGerentesCrediarioApuracaoSax(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio sem Chefes para Apura��o de Cr�dito e Sax no m�s de campanha.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioTodosSax(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioTodosSax(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio de Todos os SAX.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioAnaliticoOperacionalSax(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioAnaliticoOperacionalSax(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio Anal�tico Operacional SAX.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioAnaliticoLiderUm(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioAnaliticoLiderUm(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio Anal�tico L�der 1.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }

  public List obtemRelatorioAnaliticoLiderDois(RelatorioLojaVO relatorioVO)
    throws SRVException
  {
    List listaRelatorio = new ArrayList();
    RelatorioSaxDAO dao = new RelatorioSaxDAO();
    try
    {
      listaRelatorio = dao.obtemRelatorioAnaliticoLiderDois(relatorioVO);
    } catch (Exception e) {
      throw new SRVException(log, "N�o foi poss�vel obter relat�rio Anal�tico L�der 2.", e);
    } finally {
      dao.closeConnection();
    }
    return listaRelatorio;
  }
}