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
public class RelatorioVmDAO extends BasicDAO
{
  private static final Logger log = Logger.getLogger(RelatorioVmDAO.class);

  public List obtemRelatorioPagamentoVm(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select a.cod_fil_rh   as filial_rh                          ,a.cod_func     as matricula                          ,a.nome_func    as nome_usu_final                     ,b.descr_cargo  as cargo                              ,f.cod_indic    as cod_indic                          ,f.descr_indic  as indicador                          ,sum(e.vlr_premio_func_calc) as vlr_premiacao         ,f.cod_verba_rh  as verba_rh                          ,e.num_ano       as ano                               ,e.num_mes       as mes                          from srv_funcionario                 a                     ,srv_cargo                      b                     ,srv_grupo_cargo                c                     ,srv_grupo_rem_variavel         d                     ,srv_realizado_func_indicador   e                     ,srv_indicador                  f                     ,srv_grupo_indicador            g                     ,srv_grp_indic_grp_rem_var      h                where a.cod_cargo       = b.cod_cargo                   and b.cod_cargo       = c.cod_cargo                   and c.cod_grp_rem_var = d.cod_grp_rem_var             and d.cod_grp_rem_var = 4                             and a.cod_func        = e.cod_func                    and a.cod_emp         = e.cod_emp                     and e.cod_indic       = f.cod_indic                   and f.cod_grp_indic   = g.cod_grp_indic               and g.cod_grp_indic   = h.cod_grp_indic               and h.cod_grp_rem_var = d.cod_grp_rem_var             and e.num_ano         = ?                             and e.num_mes         = ?                           group by a.cod_fil_rh                                         ,a.cod_func                                           ,a.nome_func                                          ,b.descr_cargo                                        ,f.cod_indic                                          ,f.descr_indic                                        ,f.cod_verba_rh                                       ,e.num_ano                                            ,e.num_mes                                    order by b.descr_cargo                                        ,a.nome_func                                          ,f.descr_indic                             ");
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
      throw new PersistenciaException(log, "Não foi possível obter relatório de Pagamento VM", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }//throw localObject;
  }
}