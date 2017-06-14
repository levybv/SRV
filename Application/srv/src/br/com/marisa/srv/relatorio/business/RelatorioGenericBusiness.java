package br.com.marisa.srv.relatorio.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.relatorio.dao.RelatorioGenericDAO;

public class RelatorioGenericBusiness
{
  private static final Logger log = Logger.getLogger(RelatorioGenericBusiness.class);

  private static RelatorioGenericBusiness instance = new RelatorioGenericBusiness();

  public static final RelatorioGenericBusiness getInstance()
  {
    return instance;
  }

  public List pesquisaRelatorioBonusPagamento(String status, Integer mes, Integer ano, boolean isExcel)
    throws SRVException
  {
    List lista = new ArrayList();
    RelatorioGenericDAO dao = new RelatorioGenericDAO();
    try
    {
      lista = dao.pesquisaRelatorioBonusPagamento(status, mes, ano, isExcel);
    } catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
    } finally {
      dao.closeConnection();
    }

    return lista;
  }

  public List pesquisaRelatorioBonusPagamentoProporcionalidade(String status, Integer mes, Integer ano, boolean isExcel)
    throws SRVException
  {
    List lista = new ArrayList();
    RelatorioGenericDAO dao = new RelatorioGenericDAO();
    try
    {
      lista = dao.pesquisaRelatorioBonusPagamentoProporcionalidade(status, mes, ano, isExcel);
    } catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
    } finally {
      dao.closeConnection();
    }

    return lista;
  }

	/**
  	 * 
  	 * @param status
  	 * @param mes
  	 * @param ano
  	 * @param isExcel
  	 * @return
  	 * @throws SRVException
  	 */
	public List pesquisaRelatorioBonusConferencia(String status, Integer mes, Integer ano, boolean isExcel) throws SRVException {
		List lista = new ArrayList();
		RelatorioGenericDAO dao = new RelatorioGenericDAO();
		try {
			lista = dao.pesquisaRelatorioBonusConferencia(status, mes, ano, isExcel);
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por conferencia.", e);
		} finally {
			dao.closeConnection();
		}
		return lista;
	}

  public List pesquisaRelatorioBonusDesempenho(String status, Integer mes, Integer ano, boolean isExcel)
    throws SRVException
  {
    List lista = new ArrayList();
    RelatorioGenericDAO dao = new RelatorioGenericDAO();
    try
    {
      lista = dao.pesquisaRelatorioBonusDesemepnho(status, mes, ano, isExcel);
    } catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
    } finally {
      dao.closeConnection();
    }

    return lista;
  }
}