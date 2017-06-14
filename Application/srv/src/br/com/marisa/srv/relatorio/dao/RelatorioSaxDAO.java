package br.com.marisa.srv.relatorio.dao;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.relatorio.vo.RelatorioLojaVO;
import br.com.marisa.srv.util.tools.Tools;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

@Deprecated
public class RelatorioSaxDAO extends BasicDAO
{
  private static final Logger log = Logger.getLogger(RelatorioSaxDAO.class);

  public List obtemRelatorioIndicadoresSaxEpSeguros(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      " select a.cod_fil                                   ,a.cod_func                                  ,b.nome_func                                 ,c.descr_cargo                               ,a.cod_indic                                 ,d.descr_indic                               ,a.vlr_premio_func_calc                      ,a.vlr_desc_indicacao                   from srv_realizado_func_indicador a              ,srv_funcionario b                           ,srv_cargo c                                 ,srv_indicador d                        where a.cod_func  = b.cod_func                 and b.cod_cargo = c.cod_cargo                and a.cod_indic = d.cod_indic                and d.cod_grp_indic in (4,5)                 and d.cod_indic_sis is null                  and a.num_ano   = ?                          and num_mes     = ?                        order by a.cod_fil, a.cod_func, a.cod_indic ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório Indicadores Sax Ep Seguros", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioDemonstrativoTodasLojas(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select f.cod_fil                         ,f.cod_indic                       ,g.descr_indic                     ,f.num_meta                        ,f.num_realz                       ,f.qtd_realz                       ,f.num_realz_x_meta                ,f.num_realz_fx               from srv_realizado_filial f             ,srv_indicador g             where f.cod_indic = g.cod_indic      and f.num_ano = ?                  and f.num_mes = ?                  and f.cod_indic in (16,17)       order by f.cod_fil               ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório Demonstrativo de Todas as Lojas", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioLojasSemMetas(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      " select f.cod_fil                                       ,f.cod_indic                                     ,g.descr_indic                             \t\t,f.num_meta                               \t\t,f.num_realz                              \t\t,f.qtd_realz                              \t\t,f.num_realz_x_meta                       \t\t,f.num_realz_fx                             from srv_realizado_filial f                    \t   ,srv_indicador        g                     where f.cod_indic = g.cod_indic                    and f.num_ano = ?                                and f.num_mes = ?                                and f.cod_indic in (16,17)                       and nvl(f.num_meta,0) = 0                        and nvl(f.num_realz,0)> 0                      order by f.cod_fil                              ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório de Lojas sem Metas", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioLojasSemGerentesApuracaoSax(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select k.cod_fil                                         ,l.cod_indic                                       ,m.descr_indic                                     ,l.num_meta                                        ,l.num_realz                                       ,l.qtd_realz                                       ,l.num_realz_x_meta                                ,l.num_realz_fx                               from (select distinct f.cod_fil                    from srv_realizado_filial f                       where f.num_ano = ?                                  and f.num_mes = ?                                  and f.cod_indic in (16,17)                       MINUS                                             select distinct a.cod_fil                            from srv_realizado_func_indicador a                    ,srv_indicador        b                            ,srv_funcionario      c                            ,srv_grp_indic_grp_rem_var d                       ,srv_grupo_cargo      e                            ,srv_cargo            g                       where a.num_ano   = ?                                and a.num_mes   = ?                                and a.cod_indic = b.cod_indic                      and b.cod_grp_indic = d.cod_grp_indic              and a.cod_func      = c.cod_func                   and c.cod_cargo     = e.cod_cargo                  and e.cod_grp_rem_var = d.cod_grp_rem_var          and e.cod_cargo       = g.cod_cargo                and c.cod_cargo       = g.cod_cargo                and d.cod_grp_indic in (4,5)                       and d.cod_grp_rem_var in (6,10)                    and g.cod_cargo in (168,401,403))k                    ,srv_realizado_filial    l                         ,srv_indicador           m                    where l.cod_indic = m.cod_indic                      and k.cod_fil = l.cod_fil                          and l.num_ano = ?                                  and l.num_mes = ?                                  and l.cod_indic in (16,17)                       order by l.cod_fil,l.cod_indic");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());
      setString(stmt, 3, relatorioVO.getAno());
      setString(stmt, 4, relatorioVO.getMes());
      setString(stmt, 5, relatorioVO.getAno());
      setString(stmt, 6, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório sem Gerentes com Apuração de Sax no mês de campanha.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioLojasSemGerentesCrediarioApuracaoSax(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select k.cod_fil                                    ,l.cod_indic                                  ,m.descr_indic                                ,l.num_meta                                   ,l.num_realz                                  ,l.qtd_realz                                  ,l.num_realz_x_meta                           ,l.num_realz_fx                          from (select distinct f.cod_fil               from srv_realizado_filial f                  where f.num_ano = ?                             and f.num_mes = ?                             and f.cod_indic in (16,17)                  MINUS                                        select distinct a.cod_fil                       from srv_realizado_func_indicador a               ,srv_indicador        b                       ,srv_funcionario      c                       ,srv_grp_indic_grp_rem_var d                  ,srv_grupo_cargo      e                       ,srv_cargo            g                  where a.num_ano   =  ?                          and a.num_mes   =  ?                          and a.cod_indic = b.cod_indic                 and b.cod_grp_indic = d.cod_grp_indic         and a.cod_func      = c.cod_func              and c.cod_cargo     = e.cod_cargo             and e.cod_grp_rem_var = d.cod_grp_rem_var     and e.cod_cargo       = g.cod_cargo           and c.cod_cargo       = g.cod_cargo           and d.cod_grp_indic in (4,5)                  and d.cod_grp_rem_var in (6,10)               and g.cod_cargo in (725,934,801))k               ,srv_realizado_filial    l                    ,srv_indicador           m               where l.cod_indic = m.cod_indic                 and k.cod_fil = l.cod_fil                     and l.num_ano = ?                             and l.num_mes = ?                             and l.cod_indic in (16,17)                  order by l.cod_fil,l.cod_indic              ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());
      setString(stmt, 3, relatorioVO.getAno());
      setString(stmt, 4, relatorioVO.getMes());
      setString(stmt, 5, relatorioVO.getAno());
      setString(stmt, 6, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório sem Chefes para Apuração de Crédito e Sax no mês de campanha.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioTodosSax(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select a.cod_fil                                        ,a.cod_func                                       ,b.cod_cracha                                     ,b.num_cpf_func                                   ,b.cod_sit_rh                                     ,b.descr_sit_rh                                   ,b.nome_func                                      ,b.cod_cargo                                      ,c.descr_cargo                                    ,b.dt_admissao                                    ,b.dt_demissao                                    ,a.cod_indic                                      ,d.descr_indic                                    ,a.vlr_premio                                     ,a.cod_escala                                     ,a.num_realz_fx                                   ,a.cod_pond                                       ,a.num_peso                                       ,a.num_realz_pond                                 ,a.num_realz                                      ,a.vlr_desc_indicacao                             ,a.vlr_premio_func_calc                      from srv_realizado_func_indicador a                   ,srv_funcionario              b                   ,srv_cargo                    c                   ,srv_indicador                d              where a.cod_func  = b.cod_func                      and b.cod_cargo = c.cod_cargo                     and a.cod_indic = d.cod_indic                     and d.cod_grp_indic in (4,5)                      and d.cod_indic_sis is null                       and a.num_ano = ?                                 and a.num_mes = ?                               order by a.cod_fil, a.cod_func, a.cod_indic     ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório de Todos os SAX.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioAnaliticoOperacionalSax(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select b.cod_fil_rh                                             ,a.cod_func                                               ,b.nome_func                                              ,c.descr_cargo                                            ,a.num_realz                                              ,(a.vlr_premio_func_calc - a.vlr_desc_indicacao ) as vlr_premio_capt_direta         ,a.vlr_desc_indicacao                                     ,a.vlr_premio_func_calc                                   ,b.num_cpf_func                                           ,b.dt_demissao                                            ,b.descr_sit_rh                                           ,b.dt_ini_sit_rh                                          ,a.cod_fil                                                ,a.cod_indic                                              ,d.descr_indic                                            ,a.vlr_premio                                             ,a.num_realz_fx                                           ,a.cod_pond                                               ,a.num_peso                                               ,a.num_realz_pond                                     from srv_realizado_func_indicador a                           ,srv_funcionario              b                           ,srv_cargo                    c                           ,srv_indicador                d                           ,srv_grupo_rem_variavel       e                           ,srv_grp_indic_grp_rem_var    f                           ,srv_grupo_cargo              g                      where a.cod_func = b.cod_func                               and b.cod_cargo = c.cod_cargo                             and c.cod_cargo = g.cod_cargo                             and e.cod_grp_rem_var = g.cod_grp_rem_var                 and a.cod_indic = d.cod_indic                             and d.cod_grp_indic = f.cod_grp_indic                     and e.cod_grp_rem_var = f.cod_grp_rem_var                 and d.cod_grp_indic in (4,5)                              and e.cod_grp_rem_var in (7,11)                           and d.cod_indic_sis is null                               and a.num_ano = ?                                         and a.num_mes = ?                                       order by a.cod_fil, a.cod_func, a.cod_indic            ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório Analítico Operacional SAX.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioAnaliticoLiderUm(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select b.cod_fil_rh                                  ,a.cod_func                                    ,b.nome_func                                   ,c.descr_cargo                                 ,a.num_meta                                    ,a.num_realz                                   ,a.num_realz_x_meta                            ,a.vlr_premio_func_calc                        ,(a.vlr_premio_func_calc - a.vlr_desc_indicacao ) as vlr_premio_capt_direta        ,a.vlr_desc_indicacao                          ,b.num_cpf_func                                ,b.dt_demissao                                 ,b.descr_sit_rh                                ,b.dt_ini_sit_rh                               ,a.cod_fil                                     ,a.cod_indic                                   ,d.descr_indic                                 ,a.vlr_premio                                  ,a.num_realz_fx                                ,a.cod_pond                                    ,a.num_peso                                    ,a.num_realz_pond                         from srv_realizado_func_indicador  a                ,srv_funcionario              b                ,srv_cargo                    c                ,srv_indicador                d                ,srv_grupo_rem_variavel       e                ,srv_grp_indic_grp_rem_var    f                ,srv_grupo_cargo              g           where a.cod_func = b.cod_func                    and b.cod_cargo = c.cod_cargo                  and c.cod_cargo = g.cod_cargo                  and e.cod_grp_rem_var = g.cod_grp_rem_var      and a.cod_indic = d.cod_indic                  and d.cod_grp_indic = f.cod_grp_indic          and e.cod_grp_rem_var = f.cod_grp_rem_var      and d.cod_grp_indic in (4,5)                   and e.cod_grp_rem_var in (6,10)                and d.cod_indic_sis is null                    and a.num_ano = ?                              and a.num_mes = ?                              and nvl(c.flg_agrupa_fil_lider,'N') = 'N'    order by a.cod_fil, a.cod_func, a.cod_indic ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório Analítico Líder 1.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }

  public List obtemRelatorioAnaliticoLiderDois(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select b.cod_fil_rh                                 ,a.cod_func                                   ,b.nome_func                                  ,c.descr_cargo                                ,a.num_meta                                   ,a.num_realz                                  ,a.num_realz_x_meta                           ,a.vlr_premio_func_calc                       ,(a.vlr_premio_func_calc - a.vlr_desc_indicacao ) as vlr_premio_capt_direta        ,a.vlr_desc_indicacao                         ,b.num_cpf_func                               ,b.dt_demissao                                ,b.descr_sit_rh                               ,b.dt_ini_sit_rh                              ,a.cod_fil                                    ,a.cod_indic                                  ,d.descr_indic                                ,a.vlr_premio                                 ,a.num_realz_fx                               ,a.cod_pond                                   ,a.num_peso                                   ,a.num_realz_pond                        from srv_realizado_func_indicador  a               ,srv_funcionario              b               ,srv_cargo                    c               ,srv_indicador                d               ,srv_grupo_rem_variavel       e               ,srv_grp_indic_grp_rem_var    f               ,srv_grupo_cargo              g         where a.cod_func = b.cod_func                   and b.cod_cargo = c.cod_cargo                 and c.cod_cargo = g.cod_cargo                 and e.cod_grp_rem_var = g.cod_grp_rem_var     and a.cod_indic = d.cod_indic                 and d.cod_grp_indic = f.cod_grp_indic         and e.cod_grp_rem_var = f.cod_grp_rem_var     and d.cod_grp_indic in (4,5)                  and e.cod_grp_rem_var in (6,10)               and d.cod_indic_sis is null                   and a.num_ano = ?                             and a.num_mes = ?                             and nvl(c.flg_agrupa_fil_lider,'N') = 'S'   order by a.cod_fil, a.cod_func, a.cod_indic ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório Analítico Líder 2.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }
}