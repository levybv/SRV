package br.com.marisa.srv.relatorio.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.relatorio.vo.RelatorioLojaVO;
import br.com.marisa.srv.util.tools.Tools;

@Deprecated
public class RelatorioLojaDAO extends BasicDAO
{
  private static final Logger log = Logger.getLogger(RelatorioLojaDAO.class);

  public List<Map<Object,Object>> obtemRelatorioAgrupamentoRemuneracaoLojaLider(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      " select a.cod_fil                             ,a.cod_func                            ,b.nome_func                           ,c.descr_cargo                         ,a.vlr_premio_func_calc            from srv_realizado_func_indicador a        ,srv_funcionario b                     ,srv_cargo c                      where a.cod_func  = b.cod_func           and b.cod_cargo = c.cod_cargo          and a.cod_indic = 40                   and a.num_ano   = ?                    and a.num_mes   = ?                  order by a.cod_fil                             ,a.cod_func                  ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório Indicador Agrupamento Remuneracao Loja Lider.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

  public List<Map<Object,Object>> obtemRelatorioResultadoVendasLoja(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
    		"SELECT A.COD_FIL, "+
			"       D.DESCR_TIPO_FIL, "+
			"       A.NUM_META AS NUM_META, "+
			"       A.NUM_REALZ AS NUM_REALZ, "+
			"       A.NUM_REALZ_X_META AS NUM_REALZ_X_META, "+
			"       A.NUM_REALZ_FX AS NUM_REALZ_FX, "+
			"       B.VLR_PREMIO_FIL AS VLR_PREMIO_FIL, "+
			"       A.VLR_PREMIO_FIL_CALC AS VLR_PREMIO_FIL_CALC "+
			"  FROM SRV_REALIZADO_FILIAL A, SRV_META_FILIAL B, SRV_FILIAL C, SRV_TIPO_FILIAL D "+
			" WHERE A.COD_EMP = B.COD_EMP "+
			"   AND A.COD_FIL = B.COD_FIL "+
			"   AND A.NUM_ANO = B.NUM_ANO "+
			"   AND A.NUM_MES = B.NUM_MES "+
			"   AND A.COD_INDIC = B.COD_INDIC "+
			"   AND A.COD_FIL = C.COD_FIL "+
			"   AND C.COD_TIPO_FIL = D.COD_TIPO_FIL "+
			"   AND A.COD_INDIC = 1 "+
			"   AND A.NUM_ANO = ? "+
			"   AND A.NUM_MES = ? "+
			" ORDER BY A.COD_FIL ");
    try {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    } catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório resultado vendas (loja).", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

	public List<Map<Object, Object>> obtemRelatorioAnaliticoVendas(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer(
				" SELECT A.COD_FIL              AS LOJA, " +
				"        A.COD_FUNC             AS COD_FUNC, " +
				"        B.COD_CRACHA           AS COD_CRACHA, " +
				"        B.NUM_CPF_FUNC         AS NUM_CPF_FUNC, " +
				"        B.COD_SIT_RH           AS COD_SIT_RH, " +
				"        B.DESCR_SIT_RH         AS DESCR_SIT_RH, " +
				"        B.NOME_FUNC            AS NOME_FUNC, " +
				"        B.COD_CARGO            AS COD_CARGO, " +
				"        C.DESCR_CARGO          AS DESCR_CARGO, " +
				"        B.DT_ADMISSAO          AS DT_ADMISSAO, " +
				"        B.DT_ADMISSAO          AS DT_DEMISSAO, " +
				"        D.VLR_SAL_BASE_REM_VAR AS SAL_BASE_PREMIO, " +
				"        A.PCT_CALC_RATEIO      AS PERCENTUAL_RATEIO, " +
				"        A.VLR_PREMIO_FUNC_CALC AS VLR_PREMIO_CALCULADO " +
				"   FROM SRV_REALIZADO_FUNC_INDICADOR A " +
				"  INNER JOIN SRV_FUNCIONARIO B ON A.COD_FUNC = B.COD_FUNC " +
				"  INNER JOIN SRV_CARGO C ON B.COD_CARGO = C.COD_CARGO " +
				"   LEFT JOIN SRV_FUNC_BASE_REM_VAR D ON B.COD_FUNC = D.COD_FUNC " +
				"                                         AND B.COD_EMP = D.COD_EMP " +
				"                                         AND B.COD_FIL = D.COD_FIL " +
				"                                         AND A.NUM_ANO = D.NUM_ANO " +
				"                                         AND A.NUM_MES = D.NUM_MES " +
				"  WHERE A.COD_INDIC = 1 " +
				"    AND A.NUM_ANO = ? " +
				"    AND A.NUM_MES = ? " +
				"  ORDER BY A.COD_FIL, A.COD_FUNC ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, relatorioVO.getAno());
			setString(stmt, 2, relatorioVO.getMes());

			rs = stmt.executeQuery();

			List<Map<Object, Object>> localList = Tools.montaRelatorio(rs, true, true);
			return localList;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório analítico vendas.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object, Object>> obtemRelatorioResultadoPsfLojas(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<Object, Object>> lista = null;

		StringBuffer query = new StringBuffer(
				" SELECT GI.COD_GRP_INDIC AS COD_GRUPO_INDICADOR, " +
				"         GI.DESCR_GRP_INDIC AS DESCR_GRUPO_INDICADOR, " +
				"         I.COD_INDIC AS COD_INDICADOR, " +
				"         I.DESCR_INDIC AS DESCR_INDICADOR, " +
				"         RF.COD_FIL AS LOJA, " +
				"         TF.DESCR_TIPO_FIL AS TP_LOJA, " +
				"         DECODE(RF.NUM_META, 0, DECODE(RF.NUM_REALZ, 0, 1, RF.NUM_REALZ), RF.NUM_META) AS META, " +
				"         RF.NUM_REALZ AS REALIZADO, " +
				"         CASE " +
				"           WHEN RF.NUM_REALZ_X_META > 100 and I.COD_INDIC = 29 THEN 100 " +
				"           WHEN RF.NUM_REALZ_X_META > 120 THEN 120 " +
				"           ELSE RF.NUM_REALZ_X_META " +
				"         END AS REAL_X_META, " +
				"         P.NUM_PESO AS PESO, " +
				"         ROUND(((CASE " +
				"                 WHEN RF.NUM_REALZ_X_META > 100 and I.COD_INDIC = 29 THEN 100 " +
				"                 WHEN RF.NUM_REALZ_X_META > 120 THEN 120 " +
				"                 ELSE RF.NUM_REALZ_X_META " +
				"               END) * P.NUM_PESO) / 100, 2) PESO_X_REAL_X_META " +
				"    FROM SRV_REALIZADO_FILIAL RF, " +
				"         SRV_INDICADOR        I, " +
				"         SRV_GRUPO_INDICADOR  GI, " +
				"         SRV_FILIAL           F, " +
				"         SRV_TIPO_FILIAL      TF, " +
				"         SRV_PONDERACAO       P " +
				"   WHERE RF.COD_INDIC = I.COD_INDIC " +
				"     AND I.COD_GRP_INDIC = GI.COD_GRP_INDIC " +
				"     AND F.COD_FIL = RF.COD_FIL " +
				"     AND F.COD_TIPO_FIL = TF.COD_TIPO_FIL " +
				"     AND P.COD_INDIC = RF.COD_INDIC " +
				"     AND P.COD_TIPO_FIL = TF.COD_TIPO_FIL " +
				"     AND GI.COD_GRP_INDIC = 3 " +
				"     AND P.COD_GRP_REM_VAR = 3 " +
				"     AND RF.NUM_ANO = ? " +
				"     AND RF.NUM_MES = ? " +
				"   ORDER BY RF.COD_INDIC, RF.COD_FIL ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, relatorioVO.getAno());
			setString(stmt, 2, relatorioVO.getMes());

			rs = stmt.executeQuery();

			lista = Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório sintético indicadores PSF.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	public List<Map<Object, Object>> obtemRelatorioResultadoPorLojaRegionais(
			RelatorioLojaVO relatorioVO) throws PersistenciaException,
			SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
				" SELECT DISTINCT D.COD_FIL_RH, " +
				"                C.COD_FUNC, " +
				"                D.NOME_FUNC, " +
				"                E.DESCR_CARGO, " +
				"                C.COD_FIL, " +
				"                A.COD_INDIC, " +
				"                B.DESCR_INDIC, " +
				"                A.NUM_ANO, " +
				"                A.NUM_MES, " +
				"                DECODE(A.NUM_META, 0, DECODE(A.NUM_REALZ, 0, 1, A.NUM_REALZ), A.NUM_META) NUM_META, " +
				"                A.NUM_REALZ, " +
				"                A.QTD_REALZ, " +
				"                A.NUM_REALZ_X_META " +
				"  FROM SRV_REALIZADO_FILIAL   A, " +
				"       SRV_INDICADOR          B, " +
				"       SRV_FUNC_BASE_REM_VAR  C, " +
				"       SRV_FUNCIONARIO        D, " +
				"       SRV_CARGO              E, " +
				"       SRV_GRUPO_CARGO        F, " +
				"       SRV_GRUPO_REM_VARIAVEL G " +
				" WHERE A.COD_INDIC = B.COD_INDIC " +
				"   AND A.COD_FIL = C.COD_FIL " +
				"   AND A.NUM_ANO = C.NUM_ANO " +
				"   AND A.NUM_MES = C.NUM_MES " +
				"   AND C.COD_FUNC = D.COD_FUNC " +
				"   AND D.COD_CARGO = E.COD_CARGO " +
				"   AND E.COD_CARGO = F.COD_CARGO " +
				"   AND F.COD_GRP_REM_VAR = G.COD_GRP_REM_VAR " +
				"   AND G.COD_GRP_REM_VAR IN (3, 6, 10, 4) " +
				"   AND A.NUM_ANO = ? " +
				"   AND A.NUM_MES = ? " +
				" ORDER BY C.COD_FUNC, C.COD_FIL, A.COD_INDIC ");

		try {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);

      return localList;
		} catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório resultado por lojas + regionais.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

  public List<Map<Object,Object>> obtemRelatorioCalculoOperacionalPsf(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
    		" SELECT D.COD_GRP_INDIC, " +
			"        D.DESCR_GRP_INDIC, " +
			"        A.COD_FIL, " +
			"        A.COD_FUNC, " +
			"        F.NOME_FUNC, " +
			"        G.DESCR_CARGO, " +
			"        C.COD_INDIC, " +
			"        C.DESCR_INDIC, " +
			"        A.VLR_PREMIO_FUNC_CALC, " +
			"        A.NUM_REALZ, " +
			"        K.DESCR_TIPO_FIL, " +
			"        A.NUM_REALZ_X_META, " +
			"        A.NUM_REALZ_FX, " +
			"        A.VLR_PREMIO, " +
			"        A.NUM_REALZ_POND " +
			"   FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
			"        SRV_INDICADOR                C, " +
			"        SRV_GRUPO_INDICADOR          D, " +
			"        SRV_GRP_INDIC_GRP_REM_VAR    E, " +
			"        SRV_FUNCIONARIO              F, " +
			"        SRV_CARGO                    G, " +
			"        SRV_GRUPO_REM_VARIAVEL       H, " +
			"        SRV_GRUPO_CARGO              I, " +
			"        SRV_FILIAL                   J, " +
			"        SRV_TIPO_FILIAL              K " +
			"  WHERE A.COD_FUNC = F.COD_FUNC " +
			"    AND A.COD_INDIC = C.COD_INDIC " +
			"    AND C.COD_GRP_INDIC = D.COD_GRP_INDIC " +
			"    AND D.COD_GRP_INDIC = E.COD_GRP_INDIC " +
			"    AND F.COD_CARGO = G.COD_CARGO " +
			"    AND E.COD_GRP_REM_VAR = H.COD_GRP_REM_VAR " +
			"    AND G.COD_CARGO = I.COD_CARGO " +
			"    AND I.COD_GRP_REM_VAR = H.COD_GRP_REM_VAR " +
			"    AND A.COD_FIL = J.COD_FIL " +
			"    AND J.COD_TIPO_FIL = K.COD_TIPO_FIL " +
			"    AND E.COD_GRP_REM_VAR = 5 " +
			"    AND D.COD_GRP_INDIC = 3 " +
			"    AND A.NUM_ANO = ? " +
			"    AND A.NUM_MES = ? " +
			"  ORDER BY A.COD_FIL, A.COD_FUNC, A.COD_INDIC ");

    try {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório cálculo operacional PSF.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	private List<Map<Object, Object>> processaRelatorioCalculoLiderancaGerentesChefes(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {

		StringBuffer query = new StringBuffer(" { call pkg_srv_calc_rem_var.prc_gera_relatorios (?, ?, ?, ?, ?) } ");

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			CallableStatement cstmt = conn.prepareCall(query.toString());
			int nroCampo = 1;

			setInteger(cstmt, nroCampo++, Integer.valueOf(relatorioVO.getAno()));
			setInteger(cstmt, nroCampo++, Integer.valueOf(relatorioVO.getMes()));
			setInteger(cstmt, nroCampo++, 1);

			cstmt.registerOutParameter(nroCampo++, Types.INTEGER);
			cstmt.registerOutParameter(nroCampo++, Types.VARCHAR);

			cstmt.execute();
//			int idRetorno = cstmt.getInt(3);
//
//			if (idRetorno != 0) {
//				throw new PersistenciaException("Ocorreu erro ao processar o relatorio: Calculo Lideranca Gerentes e Chefes (" + relatorioVO.getMes() + "/" + relatorioVO.getAno() +")");
//			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível processar o relatorio: Calculo Lideranca Gerentes e Chefes", e);
		} finally {
			closeStatement(stmt);
		}

		return null;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object,Object>> obtemRelatorioCalculoLiderancaGerentesChefes(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {

		//Gera tabela temporaria com dados do relatorio
		this.processaRelatorioCalculoLiderancaGerentesChefes(relatorioVO);

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<Object,Object>> lista = null;

		StringBuffer query = new StringBuffer(
				" SELECT * FROM SRV_TMP_RELATORIO WHERE NUM_ANO = ? AND NUM_MES = ? ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, relatorioVO.getAno());
			setString(stmt, 2, relatorioVO.getMes());

			rs = stmt.executeQuery();
			lista = Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o relatorio: Calculo Lideranca Gerentes e Chefes", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

  public List<Map<Object,Object>> obtemRelatorioPremiacaoColoboradorOperacional(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select a.cod_fil                                           ,a.cod_func                                          ,f.nome_func                                         ,g.descr_cargo                                       ,sum(a.vlr_premio_func_calc) vr_premio_calc     from srv_realizado_func_indicador a                       ,srv_indicador               c                       ,srv_grupo_indicador         d                       ,srv_grp_indic_grp_rem_var   e                       ,srv_funcionario             f                       ,srv_cargo                   g                       ,srv_grupo_rem_variavel      h                       ,srv_grupo_cargo             i                  where a.cod_func        = f.cod_func                   and a.cod_indic       = c.cod_indic                  and c.cod_grp_indic   = d.cod_grp_indic              and d.cod_grp_indic   = e.cod_grp_indic              and f.cod_cargo       = g.cod_cargo                  and e.cod_grp_rem_var = h.cod_grp_rem_var            and g.cod_cargo       = i.cod_cargo                  and i.cod_grp_rem_var = h.cod_grp_rem_var            and e.cod_grp_rem_var = 5                            and a.num_ano         = ?                            and a.num_mes         = ?                          group by a.cod_fil                                           ,a.cod_func                                          ,f.nome_func                                         ,g.descr_cargo                              order by a.cod_fil                                           ,a.cod_func                                ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório premiacao coloborador operacional.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

  public List<Map<Object,Object>> obtemRelatorioPremiacaoColoboradorLider(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select a.cod_fil                                        ,a.cod_func                                       ,f.nome_func                                      ,g.descr_cargo                                    ,a.vlr_premio_func_calc                      from srv_realizado_func_indicador a                    ,srv_indicador               c                    ,srv_grupo_indicador         d                    ,srv_grp_indic_grp_rem_var   e                    ,srv_funcionario             f                    ,srv_cargo                   g                    ,srv_grupo_rem_variavel      h                    ,srv_grupo_cargo             i               where a.cod_func        = f.cod_func                and a.cod_indic       = c.cod_indic               and c.cod_grp_indic   = d.cod_grp_indic           and d.cod_grp_indic   = e.cod_grp_indic           and f.cod_cargo       = g.cod_cargo               and e.cod_grp_rem_var = h.cod_grp_rem_var         and g.cod_cargo       = i.cod_cargo               and i.cod_grp_rem_var = h.cod_grp_rem_var         and e.cod_grp_rem_var = 3                         and a.cod_indic       = 40                        and a.num_ano         = ?                         and a.num_mes         = ?                        order by a.cod_fil                                    ,a.cod_func                                       ,a.cod_indic                              ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório premiacao coloborador líder.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

  public List<Map<Object,Object>> obtemRelatorioCancelamentos(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select c.fil_cod                  as  cod_filial                                            ,f.fil_nome_fanta           as  filial                                                ,a2.pss_prd_codigo          as  cod_prod                                              ,p.pss_prd_nome             as  produto                                               ,a2.pss_ctr_codigo          as  contrato                                              ,b.cli_cpf                  as  cli_cpf                                               ,l.cli_nome                 as  cli_nome                                              ,a2.pss_lnc_dt_lanc_premio  as  dt_cancelam                                           ,e.usu_cpf                  as  cpf_vende                                             ,e.usu_nome                 as  nome_vende                                            ,ad.pss_acd_descricao       as  motivo_can                                            ,nvl(cv2.fil_cod, 0)        as  fil_cancel                                            ,nvl(cpv2.pss_vnd_cpf, 0)   as  cpf_cancel                                            ,usu2.usu_nome              as  usu_canc_nome                                    from ccm_pss_lancamento_premio    a2                                                       ,ccm_pss_contrato_cliente    b                                                        ,ccm_pss_canal_venda         c                                                        ,ccm_pss_vendedor            d                                                        ,ccm_usuario                 e                                                        ,ccm_pss_produto             p                                                        ,ccm_cliente                 l                                                        ,ccm_filial                  f                                                        ,ccm_pss_alteracao_contrato  ac                                                       ,ccm_pss_alteracao_cadastral ad                                                       ,ccm_pss_canal_venda         cv2                                                      ,ccm_pss_vendedor            cpv2                                                     ,ccm_usuario                 usu2                                               where a2.pss_ctr_codigo          = b.pss_ctr_codigo                                     and a2.pss_lnc_tipo            = 5                                                    and trunc(a2.pss_lnc_dt_lanc_premio) between to_date(? , 'dd/mm/yyyy hh24:mi:ss')     and to_date(?,'dd/mm/yyyy hh24:mi:ss')                                                and trunc(b.pss_ctr_data_adesao)     between to_date(?, 'dd/mm/yyyy hh24:mi:ss')      and to_date(?,'dd/mm/yyyy hh24:mi:ss')                 and b.pss_cnl_codigo     = c.pss_cnl_codigo    (+)     and b.pss_vnd_codigo     = d.pss_vnd_codigo    (+)     and d.pss_vnd_cpf        = e.usu_cpf\t\t    (+)     and b.pss_prd_codigo     = p.pss_prd_codigo            and b.cli_cpf            = l.cli_cpf                   and c.fil_cod            = f.fil_cod                   and a2.pss_ctr_codigo    = ac.pss_ctr_codigo   (+)     and ac.pss_acd_codigo    = ad.pss_acd_codigo   (+)     and a2.pss_cnl_codigo    = cv2.pss_cnl_codigo  (+)     and a2.pss_vnd_codigo\t  = cpv2.pss_vnd_codigo (+)     and cpv2.pss_vnd_cpf     = usu2.usu_cpf        (+)   order by c.fil_cod                                          ,a2.pss_lnc_dt_lanc_premio                      ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getDataInicio());
      setString(stmt, 2, relatorioVO.getDataFim());
      setString(stmt, 3, relatorioVO.getDataInicio());
      setString(stmt, 4, relatorioVO.getDataFim());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório cancelamentos.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

  public List<Map<Object,Object>> obtemRelatorioOperacionalLoja(RelatorioLojaVO relatorioVO)
    throws PersistenciaException, SQLException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    StringBuffer query = new StringBuffer(
      "  select a.cod_fil_rh as filial_rh                              ,a.cod_func as matricula                                ,a.nome_func as nome_usu_final                          ,b.descr_cargo as cargo                                 ,a.descr_sit_rh as situacao_func                        ,a.dt_ini_sit_rh as dt_ini_situacao                     ,a.dt_admissao as dt_admissao                           ,f.cod_indic as cod_indic                               ,f.descr_indic as indicador                             ,sum(e.vlr_premio_func_calc) as vlr_premiacao           ,f.cod_verba_rh as verba_rh                             ,e.num_ano as ano                                       ,e.num_mes as mes                                  from srv_funcionario a                                      ,srv_cargo b                                            ,srv_grupo_cargo c                                      ,srv_grupo_rem_variavel d                               ,srv_realizado_func_indicador e                         ,srv_indicador f                                        ,srv_grupo_indicador g                                  ,srv_grp_indic_grp_rem_var h                       where a.cod_cargo = b.cod_cargo                           and b.cod_cargo = c.cod_cargo                           and c.cod_grp_rem_var = d.cod_grp_rem_var               and d.cod_grp_rem_var = 5                               and a.cod_func = e.cod_func                             and a.cod_emp  = e.cod_emp                              and e.cod_indic = f.cod_indic                           and f.cod_grp_indic = g.cod_grp_indic                   and g.cod_grp_indic = h.cod_grp_indic                   and h.cod_grp_rem_var = d.cod_grp_rem_var               and e.num_ano = ?                                       and e.num_mes = ?                                       and nvl(e.vlr_premio_func_calc,0) >= 0               group by a.cod_fil_rh                                           ,a.cod_func                                             ,a.nome_func                                            ,b.descr_cargo                                          ,a.descr_sit_rh                                         ,a.dt_ini_sit_rh                                        ,a.dt_admissao                                          ,f.cod_indic                                            ,f.descr_indic                                          ,f.cod_verba_rh                                         ,e.num_ano                                              ,e.num_mes                                      order by b.descr_cargo                                          ,a.nome_func                                            ,f.descr_indic                                 ");
    try
    {
      conn = getConn();
      stmt = conn.prepareStatement(query.toString());

      setString(stmt, 1, relatorioVO.getAno());
      setString(stmt, 2, relatorioVO.getMes());

      rs = stmt.executeQuery();

      List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
      return localList;
    }
    catch (Exception e) {
      throw new PersistenciaException(log, "Não foi possível obter relatório operacional loja.", e);
    } finally {
      closeStatementAndResultSet(stmt, rs);
    }
  }

	public List<Map<Object,Object>> obtemRelatorioLiderLoja(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer(
				" select a.cod_fil_rh               as filial_rh                ,a.cod_func                 as matricula                ,a.nome_func                as nome_usu_final           ,b.descr_cargo              as cargo                    ,a.descr_sit_rh             as situacao_func            ,a.dt_ini_sit_rh            as dt_ini_situacao          ,a.dt_admissao              as dt_admissao              ,f.cod_indic                as cod_indic                ,f.descr_indic              as indicador                ,e.vlr_premio_func_calc     as vlr_premiacao            ,f.cod_verba_rh             as verba_rh                 ,e.num_ano                  as ano                      ,e.num_mes                  as mes                 from srv_funcionario                a                       ,srv_cargo                      b                       ,srv_grupo_cargo                c                       ,srv_grupo_rem_variavel         d                       ,srv_realizado_func_indicador   e                       ,srv_indicador                  f                       ,srv_grupo_indicador            g                       ,srv_grp_indic_grp_rem_var      h                  where a.cod_cargo             = b.cod_cargo               and b.cod_cargo             = c.cod_cargo               and c.cod_grp_rem_var       = d.cod_grp_rem_var         and d.cod_grp_rem_var       = 3                         and a.cod_func              = e.cod_func                and a.cod_emp               = e.cod_emp                 and e.cod_indic             = f.cod_indic               and f.cod_grp_indic         = g.cod_grp_indic           and g.cod_grp_indic         = h.cod_grp_indic           and h.cod_grp_rem_var       = d.cod_grp_rem_var         and e.cod_indic             in (1,39)                   and e.num_ano               = ?                         and e.num_mes               = ?                         and nvl(e.vlr_premio_func_calc,0) >= 0                order by b.descr_cargo                                          ,a.nome_func                                            ,f.descr_indic                                 ");
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, relatorioVO.getAno());
			setString(stmt, 2, relatorioVO.getMes());

			rs = stmt.executeQuery();

			List<Map<Object,Object>> localList = Tools.montaRelatorio(rs, true, true);
			return localList;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Líder loja.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object,Object>> obtemRelatorioResultadoLideranca2Regionais(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<Object,Object>> lista = null;

		StringBuffer query = new StringBuffer(
				" SELECT A.COD_FIL_RH AS COD_FIL_RH, " +
				"        A.COD_FUNC AS COD_FUNC,  " +
				"        A.NOME_FUNC AS NOME_FUNC,  " +
				"        A.DESCR_CARGO AS DESCR_CARGO,  " +
				"        A.COD_INDIC AS COD_INDIC,  " +
				"        A.DESCR_INDIC AS DESCR_INDIC,  " +
				"        SUM(A.NUM_META) AS META,  " +
				"        SUM(A.NUM_REALZ) AS RESULTADO,  " +
				"        ROUND(( CASE  " +
				"          WHEN SUM(A.NUM_REALZ) / SUM(A.NUM_META) * 100 > 120 THEN " +
				"              120 " +
				"          ELSE " +
				"              SUM(A.NUM_REALZ) / SUM(A.NUM_META) * 100 " +
				"        END " +
				"        ) ,2) PERC_INDICADOR " +
				"        ,  " +
				"        DECODE(A.COD_INDIC, 1, 0, H.NUM_PESO) AS PESO_INDICADOR,  " +
				"        DECODE(A.COD_INDIC,  " +
				"               1,  " +
				"               ROUND(( CASE  " +
				"                        WHEN SUM(A.NUM_REALZ) / SUM(A.NUM_META) * 100 > 120 THEN " +
				"                           120 " +
				"                        ELSE " +
				"                           SUM(A.NUM_REALZ) / SUM(A.NUM_META) * 100 " +
				"                        END " +
				"                      ) ,2),  " +
				"               ROUND(( ROUND(( CASE  " +
				"                        WHEN SUM(A.NUM_REALZ) / SUM(A.NUM_META) * 100 > 120 THEN " +
				"                           120 " +
				"                        ELSE " +
				"                           SUM(A.NUM_REALZ) / SUM(A.NUM_META) * 100 " +
				"                        END " +
				"                      ) ,2) *  " +
				"                     DECODE(A.COD_INDIC, 1, 0, H.NUM_PESO)) / 100,  " +
				"                     2)) AS PESO_X_PERC_INDICADOR " +
				"   FROM (SELECT DISTINCT SFN.COD_FIL_RH,  " +
				"                SFN.COD_FUNC,  " +
				"                SFN.NOME_FUNC,  " +
				"                SCG.DESCR_CARGO,  " +
				"                SRF.NUM_ANO,  " +
				"                SRF.NUM_MES,  " +
				"                SRF.COD_FIL,  " +
				"                SRF.COD_INDIC,  " +
				"                SID.DESCR_INDIC,  " +
				"                DECODE(SRF.NUM_META, 0, DECODE(SRF.NUM_REALZ, 0, 1, SRF.NUM_REALZ), SRF.NUM_META) NUM_META,  " +
				"                SRF.NUM_REALZ  " +
				"           FROM SRV_REALIZADO_FILIAL   SRF,  " +
				"                SRV_INDICADOR          SID,  " +
				"                SRV_FUNC_BASE_REM_VAR  SFB,  " +
				"                SRV_FUNCIONARIO        SFN,  " +
				"                SRV_CARGO              SCG,  " +
				"                SRV_GRUPO_CARGO        SGC,  " +
				"                SRV_GRUPO_REM_VARIAVEL SGR  " +
				"          WHERE SRF.COD_INDIC = SID.COD_INDIC  " +
				"            AND SRF.NUM_ANO = SFB.NUM_ANO  " +
				"            AND SRF.NUM_MES = SFB.NUM_MES  " +
				"            AND SRF.COD_FIL = SFB.COD_FIL  " +
				"            AND SFB.COD_FUNC = SFN.COD_FUNC  " +
				"            AND SFN.COD_CARGO = SCG.COD_CARGO  " +
				"            AND SCG.COD_CARGO = SGC.COD_CARGO  " +
				"            AND SGC.COD_GRP_REM_VAR = SGR.COD_GRP_REM_VAR  " +
				"            AND SRF.NUM_ANO = ? " +
				"            AND SRF.NUM_MES = ?  " +
				"            AND SFN.COD_CARGO NOT IN (168, 403, 401)  " +
				"            AND SGR.COD_GRP_REM_VAR IN (3, 6, 10, 4) " +
				"          ORDER BY SRF.COD_FIL, SRF.COD_INDIC) A,  " +
				"        (SELECT P.COD_POND,  " +
				"                P.COD_INDIC,  " +
				"                P.NUM_PESO,  " +
				"                P.COD_UN_PESO,  " +
				"                P.VLR_PREMIO  " +
				"           FROM SRV_PONDERACAO P  " +
				"          WHERE (P.COD_GRP_REM_VAR = 3)  " +
				"            AND P.COD_TIPO_FIL = 1) H  " +
				"  WHERE A.COD_INDIC = H.COD_INDIC(+)  " +
				"    AND A.COD_INDIC NOT IN (16,17,54,613) " +
				" GROUP BY A.COD_FIL_RH,  " +
				"           A.COD_FUNC,  " +
				"           A.NOME_FUNC,  " +
				"           A.DESCR_CARGO,  " +
				"           A.COD_INDIC,  " +
				"           A.DESCR_INDIC,  " +
				"           DECODE(A.COD_INDIC, 1, 0, H.NUM_PESO)  " +
				"  ORDER BY A.COD_FUNC, A.COD_INDIC ");

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, relatorioVO.getAno());
			setString(stmt, 2, relatorioVO.getMes());

			rs = stmt.executeQuery();

			lista = Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o relatório: Resultado Liderança 2 (Regionais).", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	/**
	 * 
	 * @param relatorioVO
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object,Object>> obtemRelatorioCalculoLideranca2Regionais(RelatorioLojaVO relatorioVO) throws PersistenciaException, SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<Object,Object>> lista = null;

		StringBuffer query = new StringBuffer(
				" SELECT Z.COD_FIL, " +
				"        Z.COD_FUNC, " +
				"        Z.NOME_FUNC, " +
				"        Z.DESCR_CARGO, " +
				"                ROUND(A.NUM_REALZ, 2) RESULTADO_PSF_LOJA, " +
				"                A.NUM_PESO NUM_PESO_39, " +
				"                A.NUM_REALZ_POND NUM_REALZ_POND_39, " +
				"                B.NUM_REALZ_X_META, " +
				"                B.NUM_PESO NUM_PESO_01, " +
				"                B.NUM_REALZ_POND NUM_REALZ_POND_01, " +
				"                K.NUM_REALZ_POND NUM_REALZ_POND_40, " +
				"                K.VLR_PREMIO, " +
				"        DECODE(K.NUM_REALZ_FX, 0, 'NAO RECEBE', K.NUM_REALZ_FX) \"A_RECEBER_POR_FAIXA\", " +
				"                K.VLR_PREMIO_FUNC_CALC, " +
				"        Z.COD_FIL_CADASTRO, " +
				"        DECODE(A.COD_FIL, Z.COD_FIL_CADASTRO, 0, L.QT_LOJAS + 1) \"ANALISE_DE_PAGAMENTO\" " +
				"   FROM (SELECT DISTINCT A.COD_FUNC, " +
				"                         A.COD_EMP, " +
				"                         A.COD_FIL, " +
				"                         A.NUM_ANO, " +
				"                         A.NUM_MES, " +
				"                         C.NOME_FUNC, " +
				"                         D.DESCR_CARGO, " +
				"                         C.COD_FIL COD_FIL_CADASTRO " +
				"           FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"                SRV_INDICADOR                B, " +
				"                SRV_FUNCIONARIO              C, " +
				"                SRV_CARGO                    D, " +
				"                SRV_GRUPO_CARGO              E, " +
				"                SRV_GRUPO_REM_VARIAVEL       F, " +
				"                SRV_GRUPO_INDICADOR          G " +
				"          WHERE A.COD_INDIC = B.COD_INDIC " +
				"            AND C.COD_FUNC = A.COD_FUNC " +
				"            AND D.COD_CARGO = C.COD_CARGO " +
				"            AND E.COD_CARGO = D.COD_CARGO " +
				"            AND F.COD_GRP_REM_VAR = E.COD_GRP_REM_VAR " +
				"            AND G.COD_GRP_INDIC = B.COD_GRP_INDIC " +
				"            AND F.COD_GRP_REM_VAR IN (3, 4) " +
				"            AND B.COD_GRP_INDIC IN (2, 3) " +
				"            AND D.FLG_AGRUPA_FIL_LIDER = 'S') Z, " +
				"        (SELECT A.COD_FUNC, " +
				"                A.COD_INDIC, " +
				"                A.COD_EMP, " +
				"                A.COD_FIL, " +
				"                A.NUM_ANO, " +
				"                A.NUM_MES, " +
				"                A.NUM_REALZ, " +
				"                A.NUM_PESO, " +
				"                A.NUM_REALZ_POND " +
				"           FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"                SRV_INDICADOR                B, " +
				"                SRV_FUNCIONARIO              C, " +
				"                SRV_CARGO                    D, " +
				"                SRV_GRUPO_CARGO              E, " +
				"                SRV_GRUPO_REM_VARIAVEL       F, " +
				"                SRV_GRUPO_INDICADOR          G " +
				"          WHERE A.COD_INDIC = B.COD_INDIC " +
				"            AND C.COD_FUNC = A.COD_FUNC " +
				"            AND D.COD_CARGO = C.COD_CARGO " +
				"            AND E.COD_CARGO = D.COD_CARGO " +
				"            AND F.COD_GRP_REM_VAR = E.COD_GRP_REM_VAR " +
				"            AND G.COD_GRP_INDIC = B.COD_GRP_INDIC " +
				"            AND F.COD_GRP_REM_VAR IN (3, 4) " +
				"            AND B.COD_GRP_INDIC IN (2, 3) " +
				"            AND D.FLG_AGRUPA_FIL_LIDER = 'S' " +
				"            AND A.COD_INDIC = 39) A, " +
				"        (SELECT A.COD_FUNC, " +
				"                A.COD_INDIC, " +
				"                A.COD_EMP, " +
				"                A.COD_FIL, " +
				"                A.NUM_ANO, " +
				"                A.NUM_MES, " +
				"                A.NUM_REALZ_X_META, " +
				"                A.NUM_PESO, " +
				"                A.NUM_REALZ_POND " +
				"           FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"                SRV_INDICADOR                B, " +
				"                SRV_FUNCIONARIO              C, " +
				"                SRV_CARGO                    D, " +
				"                SRV_GRUPO_CARGO              E, " +
				"                SRV_GRUPO_REM_VARIAVEL       F, " +
				"                SRV_GRUPO_INDICADOR          G " +
				"          WHERE A.COD_INDIC = B.COD_INDIC " +
				"            AND C.COD_FUNC = A.COD_FUNC " +
				"            AND D.COD_CARGO = C.COD_CARGO " +
				"            AND E.COD_CARGO = D.COD_CARGO " +
				"            AND F.COD_GRP_REM_VAR = E.COD_GRP_REM_VAR " +
				"            AND G.COD_GRP_INDIC = B.COD_GRP_INDIC " +
				"            AND F.COD_GRP_REM_VAR IN (3, 4) " +
				"            AND B.COD_GRP_INDIC IN (2, 3) " +
				"            AND D.FLG_AGRUPA_FIL_LIDER = 'S' " +
				"            AND A.COD_INDIC = 1) B, " +
				"        (SELECT A.COD_FUNC, " +
				"                A.COD_INDIC, " +
				"                A.COD_EMP, " +
				"                A.COD_FIL, " +
				"                A.NUM_ANO, " +
				"                A.NUM_MES, " +
				"                A.NUM_REALZ_POND, " +
				"                A.VLR_PREMIO, " +
				"                A.NUM_REALZ_FX, " +
				"                A.VLR_PREMIO_FUNC_CALC " +
				"           FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"                SRV_INDICADOR                B, " +
				"                SRV_FUNCIONARIO              C, " +
				"                SRV_CARGO                    D, " +
				"                SRV_GRUPO_CARGO              E, " +
				"                SRV_GRUPO_REM_VARIAVEL       F, " +
				"                SRV_GRUPO_INDICADOR          G " +
				"          WHERE A.COD_INDIC = B.COD_INDIC " +
				"            AND C.COD_FUNC = A.COD_FUNC " +
				"            AND D.COD_CARGO = C.COD_CARGO " +
				"            AND E.COD_CARGO = D.COD_CARGO " +
				"            AND F.COD_GRP_REM_VAR = E.COD_GRP_REM_VAR " +
				"            AND G.COD_GRP_INDIC = B.COD_GRP_INDIC " +
				"            AND F.COD_GRP_REM_VAR IN (3, 4) " +
				"            AND B.COD_GRP_INDIC IN (2, 3) " +
				"            AND D.FLG_AGRUPA_FIL_LIDER = 'S' " +
				"            AND A.COD_INDIC = 40) K, " +
				"                (SELECT SFB1.COD_FIL, " +
				"                        SFB1.COD_FUNC, " +
				"                        SFB1.NUM_ANO, " +
				"                        SFB1.NUM_MES, " +
				"                        SFB2.QT_LOJAS " +
				"                   FROM (SELECT SFB.* FROM SRV_FUNC_BASE_REM_VAR SFB) SFB1, " +
				"                        (SELECT SFB.COD_FUNC, " +
				"                                SFB.NUM_ANO, " +
				"                                SFB.NUM_MES, " +
				"                                COUNT(1) QT_LOJAS " +
				"                           FROM SRV_FUNC_BASE_REM_VAR SFB " +
				"                          GROUP BY SFB.COD_FUNC, SFB.NUM_ANO, SFB.NUM_MES) SFB2 " +
				"                  WHERE SFB2.COD_FUNC = SFB1.COD_FUNC " +
				"                    AND SFB2.NUM_ANO = SFB1.NUM_ANO " +
				"                    AND SFB2.NUM_MES = SFB1.NUM_MES) L " +
				"  WHERE Z.COD_FUNC = A.COD_FUNC(+) " +
				"    AND Z.NUM_ANO = A.NUM_ANO(+) " +
				"    AND Z.NUM_MES = A.NUM_MES(+) " +
				"    AND Z.COD_FUNC = B.COD_FUNC(+) " +
				"    AND Z.NUM_ANO = B.NUM_ANO(+) " +
				"    AND Z.NUM_MES = B.NUM_MES(+) " +
				"    AND Z.COD_FUNC = K.COD_FUNC(+) " +
				"    AND Z.NUM_ANO = K.NUM_ANO(+) " +
				"    AND Z.NUM_MES = K.NUM_MES(+) " +
				"    AND Z.NUM_ANO = L.NUM_ANO(+) " +
				"    AND Z.NUM_MES = L.NUM_MES(+) " +
				"    AND Z.COD_FUNC = L.COD_FUNC(+) " +
				"    AND Z.COD_FIL = L.COD_FIL(+) " +
				"    AND Z.NUM_ANO = ? " +
				"    AND Z.NUM_MES = ? ");

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, relatorioVO.getAno());
			setString(stmt, 2, relatorioVO.getMes());

			rs = stmt.executeQuery();

			lista = Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatorio: Calculo Lideranca 2 (Regionais).", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

}