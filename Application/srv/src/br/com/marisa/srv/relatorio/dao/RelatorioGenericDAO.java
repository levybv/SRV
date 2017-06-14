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

public class RelatorioGenericDAO extends BasicDAO
{
  private static final Logger log = Logger.getLogger(RelatorioGenericDAO.class);

  public List pesquisaRelatorioBonusPagamento(String status, Integer mes, Integer ano, boolean isExcel)
    throws PersistenciaException, SQLException
  {
    List lista = new ArrayList();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    RelatorioBonusStatusVO relatorio = null;
    StringBuffer query = new StringBuffer();
    if (ano != null) {
      query.append(
    		  "select b.cod_fil_rh                  as FILIAL, " +
			  "       b.cod_ccst_func               as CENTRO_CUSTO, " +
			  "       a.cod_func                    as matricula, " +
			  "       b.nome_func                   as funcionario, " +
			  "       a.cod_cargo                   as cargo, " +
			  "       c.descr_cargo                 as descr_cargo, " +
			  "       d.descr_indic                 as indicador, " +
			  "       a.num_realz_pond              as PESO_RESULT, " +
			  "       a.vlr_premio_func_calc        as QTD_SAL_RECEBER, " +
			  "       d.cod_verba_rh                as VERBA_RH, " +
			  "       a.num_ano                     as ano, " +
			  "       a.num_mes                     as mes " +
			  " from srv.SRV_realizado_func_indicador a, " +
			  "       srv.SRV_funcionario           b, " +
			  "       srv.SRV_cargo                 c, " +
			  "       srv.SRV_indicador             d, " +
			  "       srv.SRV_grupo_rem_variavel    e, " +
			  "       srv.SRV_grp_indic_grp_rem_var f, " +
			  "       srv.SRV_grupo_cargo           g " +
			  " where a.cod_func = b.cod_func " +
			  "   and a.cod_cargo = c.cod_cargo " +
			  "   and c.cod_cargo = g.cod_cargo " +
			  "   and e.cod_grp_rem_var = g.cod_grp_rem_var " +
			  "   and a.cod_indic = d.cod_indic " +
			  "   and d.cod_grp_indic = f.cod_grp_indic " +
			  "   and e.cod_grp_rem_var = f.cod_grp_rem_var " +
			  "   and d.cod_grp_indic in (1, 7) " +
			  "   and e.cod_grp_rem_var = 1 " +
			  "   and a.num_ano = ? " +
			  "   and a.num_mes = ? " +
			  "   and a.cod_indic = 48 " +
			  "   and d.cod_indic_sis is not null " +
			  " order by b.cod_fil_rh, a.cod_func, a.cod_indic ");
      try
      {
        conn = getConn();
        stmt = conn.prepareStatement(query.toString());

        int index = 1;

        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);

        rs = stmt.executeQuery();

        if (isExcel) {
          List localList1 = Tools.montaRelatorio(rs, true, true);
          return localList1;
        }
        while (rs.next()) {
          relatorio = new RelatorioBonusStatusVO();

          relatorio.setFilial(getString(rs, "FILIAL"));
          relatorio.setC_custo(getString(rs, "CENTRO_CUSTO"));
          relatorio.setVerbaRh(getString(rs, "VERBA_RH"));
          relatorio.setQtdSalReceber(getString(rs, "QTD_SAL_RECEBER"));
          relatorio.setNumRealzPond(getString(rs, "PESO_RESULT"));
          relatorio.setIndicador(getString(rs, "INDICADOR"));
          relatorio.setCodCargo(getInteger(rs, "CARGO"));
          relatorio.setDescrCargo(getString(rs, "DESCR_CARGO"));
          relatorio.setCodFunc(getInteger(rs, "MATRICULA"));
          relatorio.setNomeFunc(getString(rs, "FUNCIONARIO"));
          relatorio.setAno(getInteger(rs, "ANO"));
          relatorio.setMes(getInteger(rs, "MES"));
          lista.add(relatorio);
        }
      }
      catch (Exception e)
      {
        throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
      } finally {
        closeStatementAndResultSet(stmt, rs); } closeStatementAndResultSet(stmt, rs);
    }

    return lista;
  }

  public List pesquisaRelatorioBonusPagamentoProporcionalidade(String status, Integer mes, Integer ano, boolean isExcel)
    throws PersistenciaException, SQLException
  {
    List lista = new ArrayList();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    RelatorioBonusStatusVO relatorio = null;
    StringBuffer query = new StringBuffer();
    if (ano != null) {
      query.append(
    		  "select b.cod_fil_rh as FILIAL, " +
			  "       b.cod_ccst_func as CENTRO_CUSTO, " +
			  "       a.cod_func as matricula, " +
			  "       b.nome_func as funcionario, " +
			  "       a.cod_cargo as cargo, " +
			  "       c.descr_cargo as descr_cargo, " +
			  "       d.descr_indic as indicador, " +
			  "       a.num_realz_pond as PESO_RESULT, " +
			  "       a.vlr_premio_func_calc as QTD_SAL_RECEBER, " +
			  "       d.cod_verba_rh as VERBA_RH, " +
			  "       a.num_ano as ano, " +
			  "       a.num_mes as mes, " +
			  "       a.vlr_premio as TARGET, " +
			  "       a.qtd_meses_prop as MESES_PROP, " +
			  "       a.num_realz_fx as PROG_REDUC, " +
			  "       cast(((a.vlr_premio * a.num_realz_pond) / 100) as number(11, 2)) as valor_gatilho, " +
			  "       a.VLR_PREMIO_FUNC_CALC_PROP as PROPORCIONALIDADE " +
			  " from srv.SRV_realizado_func_indicador a, " +
			  "       srv.SRV_funcionario b, " +
			  "       srv.SRV_cargo c, " +
			  "       srv.SRV_indicador d, " +
			  "       srv.SRV_grupo_rem_variavel e, " +
			  "       srv.SRV_grp_indic_grp_rem_var f, " +
			  "       srv.SRV_grupo_cargo g " +
			  " where a.cod_func = b.cod_func " +
			  "   and a.cod_cargo = c.cod_cargo " +
			  "   and c.cod_cargo = g.cod_cargo " +
			  "   and e.cod_grp_rem_var = g.cod_grp_rem_var " +
			  "   and a.cod_indic = d.cod_indic " +
			  "   and d.cod_grp_indic = f.cod_grp_indic " +
			  "   and e.cod_grp_rem_var = f.cod_grp_rem_var " +
			  "   and d.cod_grp_indic in (1, 7) " +
			  "   and e.cod_grp_rem_var = 1 " +
			  "   and a.num_ano = ? " +
			  "   and a.num_mes = ? " +
			  "   and a.cod_indic = 48 " +
			  "   and d.cod_indic_sis is not null " +
			  " order by b.cod_fil_rh, a.cod_func, a.cod_indic ");
      try
      {
        conn = getConn();
        stmt = conn.prepareStatement(query.toString());

        int index = 1;

        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);

        rs = stmt.executeQuery();

        if (isExcel) {
          List localList1 = Tools.montaRelatorio(rs, true, true);
          return localList1;
        }
        while (rs.next()) {
          relatorio = new RelatorioBonusStatusVO();
          relatorio.setFilial(getString(rs, "FILIAL"));
          relatorio.setC_custo(getString(rs, "CENTRO_CUSTO"));
          relatorio.setVerbaRh(getString(rs, "VERBA_RH"));
          relatorio.setQtdSalReceber(getString(rs, "QTD_SAL_RECEBER"));
          relatorio.setNumRealzPond(getString(rs, "PESO_RESULT"));
          relatorio.setIndicador(getString(rs, "INDICADOR"));
          relatorio.setCodCargo(getInteger(rs, "CARGO"));
          relatorio.setDescrCargo(getString(rs, "DESCR_CARGO"));
          relatorio.setCodFunc(getInteger(rs, "MATRICULA"));
          relatorio.setNomeFunc(getString(rs, "FUNCIONARIO"));
          relatorio.setAno(getInteger(rs, "ANO"));
          relatorio.setProporcionalidade(getString(rs, "PROPORCIONALIDADE"));
          relatorio.setMes(getInteger(rs, "MES"));
          relatorio.setTarget(getString(rs, "TARGET"));

          relatorio.setValorAntesGatilho(getString(rs, "valor_gatilho"));
          relatorio.setProgressaoReducao(getString(rs, "PROG_REDUC"));
          relatorio.setMesesProp(getString(rs, "MESES_PROP"));

          lista.add(relatorio);
        }
      }
      catch (Exception e)
      {
        throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por status.", e);
      } finally {
        closeStatementAndResultSet(stmt, rs); } closeStatementAndResultSet(stmt, rs);
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
   * @throws PersistenciaException
   */
	public List pesquisaRelatorioBonusConferencia(String status, Integer mes, Integer ano, boolean isExcel) throws PersistenciaException {
		List lista = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer();
		if (ano != null) {
			query.append(
					"select b.cod_func as matricula, " +
					"       b.cod_cracha as cracha, " +
					"       b.nome_func as funcionario, " +
					"       b.cod_cargo as cargo, " +
					"       c.descr_cargo as descr_cargo, " +
					"       b.cod_fil as filial, " +
					"       b.dt_demissao as Demissao, " +
					"       b.descr_sit_rh as situacao_RH, " +
					"       b.descr_ccst_func as c_custo, " +
					"       h.descr_grp_indic as GRP_INDIC, " +
					"       g.descr_indic as indicador, " +
					"       f.num_peso as peso, " +
					"       nvl(nvl(f.descr_meta, j.descr_meta),f.num_meta) as descr_meta, " +
					"       f.cod_escala as cod_escala, " +
					"       k.descr_escala as escala, " +
					"       f.num_realz as realizado, " +
					"       f.num_realz_x_meta as atingimento, " +
					"       f.num_realz_fx as RESULT_ESCALA, " +
					"       f.num_realz_pond as PESO_RESULT, " +
					"       g.formula_indic as formula, " +
					"       g.descr_formula_indic as conceito_formula, " +
					"       e.descr_grp_rem_var as grp_remuneracao, " +
					"       f.num_ano as ano, " +
					"       f.num_mes as mes " +
					"  from srv.SRV_funcionario              b, " +
					"       srv.SRV_cargo                    c, " +
					"       srv.SRV_grupo_cargo              d, " +
					"       srv.SRV_grupo_rem_variavel       e, " +
					"       srv.SRV_realizado_func_indicador f, " +
					"       srv.SRV_indicador                g, " +
					"       srv.SRV_grupo_indicador          h, " +
					"       srv.SRV_grp_indic_grp_rem_var    i, " +
					"       srv.SRV_meta_func_bonus          j, " +
					"       srv.SRV_escala                   k " +
					" where b.cod_cargo = c.cod_cargo " +
					"   and c.cod_cargo = d.cod_cargo " +
					"   and d.cod_grp_rem_var = e.cod_grp_rem_var " +
					"   and b.cod_func = f.cod_func " +
					"   and f.cod_indic = g.cod_indic " +
					"   and g.cod_grp_indic = h.cod_grp_indic " +
					"   and h.cod_grp_indic = i.cod_grp_indic " +
					"   and i.cod_grp_rem_var = e.cod_grp_rem_var " +
					"   and e.cod_grp_rem_var in (1) " +
					"   and f.cod_indic = j.cod_indic(+) " +
					"   and f.cod_func = j.cod_func(+) " +
					"   and f.num_ano = j.num_ano(+) " +
					"   and f.num_mes = j.num_mes(+) " +
					"   and f.cod_escala = k.cod_escala(+) " +
					"   and f.num_ano = ? " +
					"   and f.num_mes = ? " +
					" order by f.cod_func ");

			try {
				conn = getConn();
				stmt = conn.prepareStatement(query.toString());
				RelatorioBonusStatusVO relatorio = null;
				int index = 1;
				setInteger(stmt, index++, ano);
				setInteger(stmt, index++, mes);
				rs = stmt.executeQuery();
				if (isExcel) {
					List localList1 = Tools.montaRelatorio(rs, true, true);
					return localList1;
				}
				while (rs.next()) {
					relatorio = new RelatorioBonusStatusVO();
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
					relatorio.setCracha(getString(rs, "cracha"));
					relatorio.setC_custo(getString(rs, "c_custo"));
					relatorio.setDescr_meta(getString(rs, "descr_meta"));
					relatorio.setCod_escala(getString(rs, "cod_escala"));
					relatorio.setConceito_formula(getString(rs, "conceito_formula"));
					relatorio.setGrp_remuneracao(getString(rs, "grp_remuneracao"));
					relatorio.setIndicador(getString(rs, "indicador"));
					relatorio.setPeso(getString(rs, "peso"));
					relatorio.setEscala(getString(rs, "escala"));
					relatorio.setRelaizado(getString(rs, "realizado"));
					relatorio.setAtingimento(getString(rs, "atingimento"));
					relatorio.setFormula(getString(rs, "formula"));
					relatorio.setResult_conf_escala(getString(rs, "RESULT_ESCALA"));
					relatorio.setPesoResultado(getString(rs, "PESO_RESULT"));
					relatorio.setGrpIndic(getString(rs, "GRP_INDIC"));

					lista.add(relatorio);
				}
			} catch (Exception e) {
				throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por conferencia.", e);
			} finally {
				closeStatementAndResultSet(stmt, rs);
			}
			closeStatementAndResultSet(stmt, rs);
		} else {
			lista = new ArrayList();
		}
		return lista;
	}

  public List pesquisaRelatorioBonusDesemepnho(String status, Integer mes, Integer ano, boolean isExcel) throws PersistenciaException, SQLException
  {
    List lista = new ArrayList();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    RelatorioBonusStatusVO relatorio = null;
    StringBuffer query = new StringBuffer();
    if (ano != null) {
      query.append(
    		  "SELECT R.COD_FUNC COD_FUNC, " +
			  "       S.NOME_FUNC NOME_FUNC, " +
			  "       R.FORMULA, " +
			  "       R.INDICADOR, " +
			  "       R.PESO, " +
			  "       R.DESCRICAO_META, " +
			  "       R.REALIZADO, " +
			  "       R.ATINGIMENTO_META, " +
			  "       R.ESCALA, " +
			  "       R.RESULTADO_CONFORME_ESCALA, " +
			  "       R.PESO_X_RESULTADO, " +
			  "       R.ORDEM FROM (SELECT A.COD_FUNC, " +
			  "                           B.FORMULA_INDIC AS FORMULA, " +
			  "                           B.DESCR_INDIC AS INDICADOR, " +
			  "                           A.NUM_PESO AS PESO, " +
			  "                           A.DESCR_META AS DESCRICAO_META, " +
			  "                           A.NUM_REALZ AS REALIZADO, " +
			  "                           A.NUM_REALZ_X_META AS ATINGIMENTO_META, " +
			  "                           E.NUM_ESCALA AS ESCALA, " +
			  "                           A.NUM_REALZ_FX AS RESULTADO_CONFORME_ESCALA, " +
			  "                           A.NUM_REALZ_POND AS PESO_X_RESULTADO, " +
			  "                           1 AS ORDEM " +
			  "                      FROM srv.SRV_REALIZADO_FUNC_INDICADOR A, " +
			  "                           srv.SRV_INDICADOR                B, " +
			  "                           srv.SRV_GRUPO_INDICADOR          C, " +
			  "                           srv.SRV_TIPO_REM_VAR             D, " +
			  "                           srv.SRV_ESCALA                   E " +
			  "                     WHERE A.COD_INDIC = B.COD_INDIC " +
			  "                       AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
			  "                       AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
			  "                       AND A.COD_ESCALA = E.COD_ESCALA(+) " +
			  "                       AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
			  "                       AND C.DESCR_GRP_INDIC = 'CORPORATIVO' " +
			  "                       AND A.NUM_ANO = ? " +
			  "                       AND A.NUM_MES = ? " +
			  "                       AND B.COD_INDIC_SIS IS NULL " +
			  "                    UNION ALL " +
			  "                    SELECT A.COD_FUNC, " +
			  "                           NULL AS FORMULA, " +
			  "                           'TOTAL' AS INDICADOR, " +
			  "                           SUM(A.NUM_PESO) AS PESO, " +
			  "                           NULL AS DESCRICAO_META, " +
			  "                           NULL AS REALIZADO, " +
			  "                           NULL AS ATINGIMENTO_META, " +
			  "                           NULL AS ESCALA, " +
			  "                           NULL AS RESULTADO_CONFORME_ESCALA, " +
			  "                           NULL AS PESO_X_RESULTADO, " +
			  "                           2 AS ORDEM " +
			  "                      FROM srv.SRV_REALIZADO_FUNC_INDICADOR A, " +
			  "                           srv.SRV_INDICADOR                B, " +
			  "                           srv.SRV_GRUPO_INDICADOR          C, " +
			  "                           srv.SRV_TIPO_REM_VAR             D, " +
			  "                           srv.SRV_ESCALA                   E " +
			  "                     WHERE A.COD_INDIC = B.COD_INDIC " +
			  "                       AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
			  "                       AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
			  "                       AND A.COD_ESCALA = E.COD_ESCALA(+) " +
			  "                       AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
			  "                       AND C.DESCR_GRP_INDIC = 'CORPORATIVO' " +
			  "                       AND A.NUM_ANO = ? " +
			  "                       AND A.NUM_MES = ? " +
			  "                       AND B.COD_INDIC_SIS IS NULL " +
			  "                     group by A.COD_FUNC " +
			  "                    UNION ALL " +
			  "                    SELECT A.COD_FUNC, " +
			  "                           B.FORMULA_INDIC AS FORMULA, " +
			  "                           B.DESCR_INDIC AS INDICADOR, " +
			  "                           A.NUM_PESO AS PESO, " +
			  "                           A.DESCR_META AS DESCRICAO_META, " +
			  "                           A.NUM_REALZ AS REALIZADO, " +
			  "                           A.NUM_REALZ_X_META AS ATINGIMENTO_META, " +
			  "                           E.NUM_ESCALA AS ESCALA, " +
			  "                           A.NUM_REALZ_FX AS RESULTADO_CONFORME_ESCALA, " +
			  "                           A.NUM_REALZ_POND AS PESO_X_RESULTADO, " +
			  "                           3 AS ORDEM " +
			  "                      FROM srv.SRV_REALIZADO_FUNC_INDICADOR A, " +
			  "                           srv.SRV_INDICADOR                B, " +
			  "                           srv.SRV_GRUPO_INDICADOR          C, " +
			  "                           srv.SRV_TIPO_REM_VAR             D, " +
			  "                           srv.SRV_ESCALA                   E " +
			  "                     WHERE A.COD_INDIC = B.COD_INDIC " +
			  "                       AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
			  "                       AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
			  "                       AND A.COD_ESCALA = E.COD_ESCALA(+) " +
			  "                       AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
			  "                       AND C.DESCR_GRP_INDIC = 'INDIVIDUAL' " +
			  "                       AND A.NUM_ANO = ? " +
			  "                       AND A.NUM_MES = ? " +
			  "                       AND B.COD_INDIC_SIS IS NULL " +
			  "                    UNION ALL " +
			  "                    SELECT A.COD_FUNC, " +
			  "                           NULL AS FORMULA, " +
			  "                           'TOTAL' AS INDICADOR, " +
			  "                           SUM(A.NUM_PESO) AS PESO, " +
			  "                           NULL AS DESCRICAO_META, " +
			  "                           NULL AS REALIZADO, " +
			  "                           NULL AS ATINGIMENTO_META, " +
			  "                           NULL AS ESCALA, " +
			  "                           NULL AS RESULTADO_CONFORME_ESCALA, " +
			  "                           NULL AS PESO_X_RESULTADO, " +
			  "                           4 AS ORDEM " +
			  "                      FROM srv.SRV_REALIZADO_FUNC_INDICADOR A, " +
			  "                           srv.SRV_INDICADOR                B, " +
			  "                           srv.SRV_GRUPO_INDICADOR          C, " +
			  "                           srv.SRV_TIPO_REM_VAR             D, " +
			  "                           srv.SRV_ESCALA                   E " +
			  "                     WHERE A.COD_INDIC = B.COD_INDIC " +
			  "                       AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
			  "                       AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
			  "                       AND A.COD_ESCALA = E.COD_ESCALA(+) " +
			  "                       AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
			  "                       AND C.DESCR_GRP_INDIC = 'INDIVIDUAL' " +
			  "                       AND A.NUM_ANO = ? " +
			  "                       AND A.NUM_MES = ? " +
			  "                       AND B.COD_INDIC_SIS IS NULL " +
			  "                     group by A.COD_FUNC " +
			  "                    UNION ALL " +
			  "                    SELECT A.COD_FUNC, " +
			  "                           NULL AS FORMULA, " +
			  "                           'RESULTADO' AS INDICADOR, " +
			  "                           A.NUM_PESO AS PESO, " +
			  "                           NULL AS DESCRICAO_META, " +
			  "                           NULL AS REALIZADO, " +
			  "                           NULL AS ATINGIMENTO_META, " +
			  "                           NULL AS ESCALA, " +
			  "                           NULL AS RESULTADO_CONFORME_ESCALA, " +
			  "                           A.NUM_REALZ_POND AS PESO_X_RESULTADO, " +
			  "                           5 AS ORDEM " +
			  "                      FROM srv.SRV_REALIZADO_FUNC_INDICADOR A, " +
			  "                           srv.SRV_INDICADOR                B, " +
			  "                           srv.SRV_GRUPO_INDICADOR          C, " +
			  "                           srv.SRV_TIPO_REM_VAR             D " +
			  "                     WHERE A.COD_INDIC = B.COD_INDIC " +
			  "                       AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
			  "                       AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
			  "                       AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
			  "                       AND A.NUM_ANO = ? " +
			  "                       AND A.NUM_MES = ? " +
			  "                       AND B.COD_INDIC_SIS IS NOT NULL) R, " +
			  "       srv.SRV_FUNCIONARIO S " +
			  " WHERE R.COD_FUNC = S.COD_FUNC " +
			  " ORDER BY R.COD_FUNC, R.ORDEM ");
	
    try {
        conn = getConn();
        stmt = conn.prepareStatement(query.toString());

        int index = 1;

        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);
        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);
        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);
        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);
        setInteger(stmt, index++, ano);
        setInteger(stmt, index++, mes);

        rs = stmt.executeQuery();

        if (isExcel) {
          List localList1 = Tools.montaRelatorio(rs, true, true);
          return localList1;
        }
        while (rs.next()) {
          relatorio = new RelatorioBonusStatusVO();
          relatorio.setOrdem(getString(rs, "ORDEM"));
          relatorio.setCodFunc(Integer.valueOf(getInt(rs, "COD_FUNC")));
          relatorio.setNomeFunc(getString(rs, "NOME_FUNC"));
          relatorio.setFormula(getString(rs, "FORMULA"));
          relatorio.setIndicador(getString(rs, "INDICADOR"));
          relatorio.setPeso(getString(rs, "PESO"));
          relatorio.setMeta(getString(rs, "DESCRICAO_META"));
          relatorio.setRelaizado(getString(rs, "REALIZADO"));
          relatorio.setAtingimentoMeta(getString(rs, "ATINGIMENTO_META"));
          relatorio.setEscala(getString(rs, "ESCALA"));
          relatorio.setAtingimentoEscala(getString(rs, "RESULTADO_CONFORME_ESCALA"));
          relatorio.setPesoResultado(getString(rs, "PESO_X_RESULTADO"));
          lista.add(relatorio);
        }
      }
      catch (Exception e)
      {
        throw new PersistenciaException(log, "Não foi possível pesquisar ou gerar relatório de bônus por desempenho.", e);
      } finally {
        closeStatementAndResultSet(stmt, rs); } closeStatementAndResultSet(stmt, rs);
    }

    return lista;
  }
}