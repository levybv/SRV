package br.com.marisa.srv.relatorio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.relatorio.vo.RelatorioBonusStatusVO;
import br.com.marisa.srv.util.tools.Tools;

public class RelatorioBonusStatusDAO extends BasicDAO {
  private static final Logger log = Logger.getLogger(RelatorioBonusStatusDAO.class);

  public List pesquisaRelatorioBonusStatus(String status, Integer mes, Integer ano, boolean isExcel) throws PersistenciaException, SQLException {
    List lista = new ArrayList();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer();

    query.append(
    		" select P.cargo, " +
			"        P.descr_cargo as DESCR_CARGO, " +
			"       P.filial, " +
			"        P.matricula, " +
			"        P.funcionario, " +
			"        P.situacao_RH, " +
			"        P.Demissao, " +
			"        P.grp_remuneracao, " +
			"        P.ano, " +
			"        P.mes, " +
			"        P.status " +
			"   from (select distinct b.cod_cargo as cargo, " +
			"                         c.descr_cargo as descr_cargo, " +
			"                         b.cod_fil as filial, " +
			"                         b.cod_func as matricula, " +
			"                         b.nome_func as funcionario, " +
			"                         b.descr_sit_rh as situacao_RH, " +
			"                         b.dt_demissao as Demissao, " +
			"                         e.descr_grp_rem_var as grp_remuneracao, " +
			"                         f.num_ano as ano, " +
			"                         f.num_mes as mes, " +
			"                         decode(f.sta_calc_realz, " +
			"                                null, " +
			"                                'NAO INICIADO', " +
			"                                1, " +
			"                                'NAO INICIADO', " +
			"                                2, " +
			"                                'EM ANDAMENTO', " +
			"                                3, " +
			"                                'FINALIZADO') as status " +
			"           from srv_funcionario              b, " +
			"                srv_cargo                    c, " +
			"                srv_grupo_cargo              d, " +
			"                srv_grupo_rem_variavel       e, " +
			"                srv_realizado_func_indicador f, " +
			"                srv_indicador                g, " +
			"                srv_grupo_indicador          h, " +
			"                srv_grp_indic_grp_rem_var    i, " +
			"                srv_meta_func_bonus          j " +
			"          where b.cod_cargo = c.cod_cargo " +
			"            and c.cod_cargo = d.cod_cargo " +
			"            and d.cod_grp_rem_var = e.cod_grp_rem_var " +
			"            and b.cod_func = f.cod_func " +
			"            and f.cod_indic = g.cod_indic " +
			"            and g.cod_grp_indic = h.cod_grp_indic " +
			"            and h.cod_grp_indic = i.cod_grp_indic " +
			"            and i.cod_grp_rem_var = e.cod_grp_rem_var " +
			"            and e.cod_grp_rem_var in (1) " +
			"            and f.cod_indic = j.cod_indic(+) " +
			"            and f.cod_func = j.cod_func(+) " +
			"            and f.num_ano = j.num_ano(+) " +
			"            and f.num_mes = j.num_mes(+) " +
			"            and f.num_ano = ? " +
			"            and f.num_mes = ? " +
			"            and f.cod_indic = 48 " +
			"         UNION " +
			"         select distinct b.cod_cargo as cargo, " +
			"                         c.descr_cargo as descr_cargo, " +
			"                         b.cod_fil as filial, " +
			"                         b.cod_func as matricula, " +
			"                         b.nome_func as funcionario, " +
			"                         b.descr_sit_rh as situacao_RH, " +
			"                         b.dt_demissao as Demissao, " +
			"                         e.descr_grp_rem_var as grp_remuneracao, " +
			"                         null as ano, " +
			"                         null as mes, " +
			"                         'NAO INICIADO' as status " +
			"           from srv_funcionario           b, " +
			"                srv_cargo                 c, " +
			"                srv_grupo_cargo           d, " +
			"                srv_grupo_rem_variavel    e, " +
			"                srv_indicador             g, " +
			"                srv_grupo_indicador       h, " +
			"                srv_grp_indic_grp_rem_var i " +
			"          where b.cod_cargo = c.cod_cargo " +
			"            and c.cod_cargo = d.cod_cargo " +
			"            and d.cod_grp_rem_var = e.cod_grp_rem_var " +
			"            and g.cod_grp_indic = h.cod_grp_indic " +
			"            and h.cod_grp_indic = i.cod_grp_indic " +
			"            and i.cod_grp_rem_var = e.cod_grp_rem_var " +
			"            and e.cod_grp_rem_var in (1) " +
			"            and not exists (select 1 " +
			"                   from srv_realizado_func_indicador f " +
			"                  where f.cod_func = b.cod_func " +
			"                    and f.num_ano = ? " +
			"                    and f.num_mes = ? " +
			"                    and f.cod_indic = 48)) P " +
			"  where (P.status = ? or ? is null) " +
			"  order by DESCR_CARGO, filial, funcionario");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      int index = 1;

      setInteger(stmt, index++, ano);
      setInteger(stmt, index++, mes);
      setInteger(stmt, index++, ano);
      setInteger(stmt, index++, mes);
      setString(stmt, index++, status);
      setString(stmt, index++, status);

      rs = stmt.executeQuery();

      if (isExcel) {
          return Tools.montaRelatorio(rs, true, true);
      }

      while (rs.next()) {
    	RelatorioBonusStatusVO relatorio = new RelatorioBonusStatusVO();
        relatorio.setCodCargo(getInteger(rs, "CARGO"));
        relatorio.setDescrCargo(getString(rs, "DESCR_CARGO"));
        relatorio.setCodFil(getInteger(rs, "FILIAL"));
        relatorio.setCodFunc(getInteger(rs, "MATRICULA"));
        relatorio.setNomeFunc(getString(rs, "FUNCIONARIO"));
        relatorio.setSituacaoRh(getString(rs, "SITUACAO_RH"));
        relatorio.setDataDemissao(getDate(rs, "DEMISSAO"));
        relatorio.setGrupoRemVar(getString(rs, "GRP_REMUNERACAO"));
        relatorio.setAno(getInteger(rs, "ANO"));
        relatorio.setMes(getInteger(rs, "MES"));
        relatorio.setStatus(getString(rs, "STATUS"));
        lista.add(relatorio);
      }

      List localList1 = lista;
      return localList1;
    }
    catch (Exception e)
    {
      throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }
}