package br.com.marisa.srv.indicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;


/**
 * Classe para tratar dos métodos de acesso a dados de indicadores
 * 
 * @author levy.villar
 *
 */
public class RealizFuncIndicadorDAO extends BasicDAO {

	/**
	 * 
	 */
    private static final Logger log = Logger.getLogger(RealizFuncIndicadorDAO.class);

    /**
     * 
     * @param idFuncionario
     * @param idIndicador
     * @param idEmpresa
     * @param idFilial
     * @param ano
     * @param mes
     * @return
     * @throws PersistenciaException
     */
	public IndicadorFuncionarioRealizadoVO obtemRealizadoFuncIndicador(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
					"SELECT A.COD_EMP									" +
					"      ,A.COD_FIL									" +
					"      ,B.COD_GRP_INDIC								" +
					"      ,C.DESCR_GRP_INDIC							" +
					"	   ,A.COD_INDIC 								" +
					"      ,B.DESCR_INDIC								" +
					"      ,A.COD_ESCALA								" +
					"      ,A.NUM_SEQ_ESCALA_FX							" +
					"      ,A.COD_UN_REALZ_FX							" +
					"      ,A.NUM_REALZ_FX								" +
					"      ,A.COD_POND									" +
					"      ,A.COD_UN_PESO								" +
					"      ,A.NUM_PESO									" +
					"      ,A.NUM_REALZ_POND							" +
					"      ,A.COD_UN_REALZ_POND							" +
					"      ,A.COD_UN_META								" +
					"      ,A.NUM_META									" +
					"      ,A.COD_UN_REALZ								" +
					"      ,A.NUM_REALZ									" +
					"      ,A.COD_UN_REALZ_X_META						" +
					"      ,A.NUM_REALZ_X_META							" +
					"      ,A.VLR_PREMIO								" +
					"      ,A.COD_UN_VLR_PREMIO_FUNC_CALC				" +
					"      ,A.VLR_PREMIO_FUNC_CALC						" +
					"      ,A.PCT_CALC_RATEIO							" +	
					"      ,A.DT_INI_SIT_SRV							" +
					"      ,A.COD_USUARIO								" +
					"      ,B.FLG_PREENCH_ATING_IGUAL_REALZ				" +
					"      ,B.COD_INDIC_PAI								" +
					"  FROM SRV_REALIZADO_FUNC_INDICADOR A 				" +
					"      ,SRV_INDICADOR                B 				" +
					"      ,SRV_GRUPO_INDICADOR 		 C 				" +
					" WHERE A.COD_INDIC          = B.COD_INDIC			" +
					"   AND B.COD_GRP_INDIC      = C.COD_GRP_INDIC		" +
					"   AND A.COD_FUNC     	     = ?             		" +
					"   AND A.COD_INDIC     	 = ?             		" +
					"   AND A.COD_EMP     	     = ?             		" +
					"   AND A.COD_FIL	     	 = ?             		" +
					"   AND A.NUM_ANO 		     = ?             		" +
					"   AND A.NUM_MES 		     = ?             		");

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, new Long(idFuncionario)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idIndicador)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idEmpresa)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idFilial)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(ano)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(mes)));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			if (rs.next()) {
				indicadorFuncionarioBonusVO = new IndicadorFuncionarioRealizadoVO();
				indicadorFuncionarioBonusVO.setIdFuncionario(new Long(idFuncionario));
				indicadorFuncionarioBonusVO.setIdEmpresa(new Integer(idEmpresa));
				indicadorFuncionarioBonusVO.setIdFilial(new Integer(idFilial));				
				indicadorFuncionarioBonusVO.setAno(new Integer(ano));
				indicadorFuncionarioBonusVO.setMes(new Integer(mes));
				indicadorFuncionarioBonusVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				indicadorFuncionarioBonusVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				indicadorFuncionarioBonusVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				indicadorFuncionarioBonusVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				indicadorFuncionarioBonusVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				indicadorFuncionarioBonusVO.setSequencialEscala(getInteger(rs, "NUM_SEQ_ESCALA_FX"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoFaixa(getInteger(rs, "COD_UN_REALZ_FX"));
				indicadorFuncionarioBonusVO.setRealizadoFaixa(getDouble(rs, "NUM_REALZ_FX"));
				indicadorFuncionarioBonusVO.setIdPonderacao(getInteger(rs, "COD_POND"));
				indicadorFuncionarioBonusVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				indicadorFuncionarioBonusVO.setPeso(getDouble(rs, "NUM_PESO"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoPonderacao(getInteger(rs, "COD_UN_REALZ_POND"));
				indicadorFuncionarioBonusVO.setRealizadoPonderacao(getDouble(rs, "NUM_REALZ_POND"));
				indicadorFuncionarioBonusVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				indicadorFuncionarioBonusVO.setMeta(getDouble(rs, "NUM_META"));
				indicadorFuncionarioBonusVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
				indicadorFuncionarioBonusVO.setRealizado(getDouble(rs, "NUM_REALZ"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				indicadorFuncionarioBonusVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				indicadorFuncionarioBonusVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				indicadorFuncionarioBonusVO.setUnidadeValorPremioCalculado(getInteger(rs, "COD_UN_VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioBonusVO.setValorPremioCalculado(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioBonusVO.setPercentualRateio(getDouble(rs, "PCT_CALC_RATEIO"));
				indicadorFuncionarioBonusVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				indicadorFuncionarioBonusVO.setIdUsuarioAlteracao(getInteger(rs, "COD_USUARIO"));
				indicadorFuncionarioBonusVO.setFlgPrrenchAtingIgualRealiz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
				indicadorFuncionarioBonusVO.setIdIndicadorPai(getInteger(rs, "COD_INDIC_PAI"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter o realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return indicadorFuncionarioBonusVO;
	}	    

    /**
     * 
     * @param indicadorFuncionarioBonusVO
     * @throws PersistenciaException
     */
	public void incluiRealizadoFuncIndicador(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			//Exclui registro gerando historico
			this.excluiRealizadoFuncIndicador(indicadorFuncionarioBonusVO.getIdFuncionario(), indicadorFuncionarioBonusVO.getIdIndicador(), 
													 indicadorFuncionarioBonusVO.getIdEmpresa(), indicadorFuncionarioBonusVO.getIdFilial(), 
													 indicadorFuncionarioBonusVO.getAno(), indicadorFuncionarioBonusVO.getMes());

			//Inclui novos dados
			String query =  
					" INSERT INTO SRV_REALIZADO_FUNC_INDICADOR 		" +
					"      (COD_FUNC								" +
					"      ,COD_INDIC								" +
					"      ,COD_EMP									" +
					"      ,COD_FIL									" +
					"	   ,NUM_ANO									" +
					"      ,NUM_MES									" +
					"      ,COD_UN_REALZ							" +
					"      ,NUM_REALZ								" +
					"      ,COD_UN_META								" +
					"      ,NUM_META								" +
					"      ,COD_UN_PESO								" +
					"      ,NUM_PESO								" +
					"      ,COD_UN_REALZ_X_META						" +
					"      ,NUM_REALZ_X_META						" +
					"      ,COD_UN_REALZ_POND						" +
					"      ,NUM_REALZ_POND							" +
					"      ,COD_ESCALA								" +
					"      ,STA_CALC_REALZ							" +
					"      ,COD_CARGO								" +
					"      ,NUM_SEQ_ESCALA_FX						" +
					"      ,NUM_REALZ_FX							" +
					"      ,COD_UN_REALZ_FX							" +
					"      ,VLR_PREMIO								" +
					"      ,VLR_PREMIO_FUNC_CALC					" +
					"      ,COD_UN_VLR_PREMIO_FUNC_CALC				" +
					"      ,DT_INI_SIT_SRV							" +
					"      ,COD_USUARIO)							" +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

			conn = getConn();
			stmt = conn.prepareStatement(query);
			int ordemCampos = 1;

			setLong(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizado());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizado());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeMeta());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMeta());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadePeso());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPeso());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoXMeta());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoXMeta());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoPonderacao());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoPonderacao());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEscala());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdStatusCalcRelz());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdCargo());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getSequencialEscala());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoFaixa());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoFaixa());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremio());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremioCalculado());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeValorPremioCalculado());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel incluir o realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param indicadorFuncionarioBonusVO
	 * @throws PersistenciaException
	 */
	public void alteraRealizadoFuncIndicador(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {
		this.incluiRealizadoFuncIndicador(indicadorFuncionarioBonusVO);
	}

    /**
     * 
     * @param indicadorFuncionarioBonusVO
     * @throws PersistenciaException
     */
	public void incluiHistoricoRealizadoFuncIndicador(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

		String query =  "INSERT INTO SRV_REALZ_FUNC_INDIC_HIST 			" +
						"      (COD_FUNC								" +
						"      ,COD_INDIC								" +
						"      ,COD_EMP									" +
						"      ,COD_FIL									" +
						"	   ,NUM_ANO									" +
						"      ,NUM_MES									" +
						"      ,COD_ESCALA								" +
						"      ,NUM_SEQ_ESCALA_FX						" +
						"      ,NUM_REALZ_FX							" +
						"      ,COD_UN_REALZ_FX							" +
						"      ,COD_POND								" +
						"      ,NUM_PESO								" +
						"      ,COD_UN_PESO								" +
						"      ,NUM_REALZ_POND							" +
						"      ,COD_UN_REALZ_POND						" +
						"      ,NUM_META								" +
						"      ,COD_UN_META								" +
						"      ,NUM_REALZ								" +
						"      ,COD_UN_REALZ							" +
						"      ,NUM_REALZ_X_META						" +
						"      ,COD_UN_REALZ_X_META						" +
						"      ,VLR_PREMIO								" +
						"      ,VLR_PREMIO_FUNC_CALC					" +
						"      ,COD_UN_VLR_PREMIO_FUNC_CALC				" +
						"      ,PCT_CALC_RATEIO							" +
						"      ,DT_INI_SIT_SRV							" +
						"      ,COD_USUARIO)							" +
						" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 		" +
						"         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 		" +
						"         ?, ?, ?, ?, ?, ?, ?) 				    ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int ordemCampos = 1;

			setLong(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEscala());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getSequencialEscala());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoFaixa());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoFaixa());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdPonderacao());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPeso());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadePeso());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoPonderacao());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoPonderacao());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMeta());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeMeta());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizado());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizado());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoXMeta());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoXMeta());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremio());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremioCalculado());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeValorPremioCalculado());
			setDouble(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPercentualRateio());
			setTimestamp(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getDataUltimaAlteracao());
			setInteger(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());

			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel incluir o historico do realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
    }

	/**
	 * 
	 * @param idFuncionario
	 * @param idIndicador
	 * @param idEmpresa
	 * @param idFilial
	 * @param ano
	 * @param mes
	 * @throws PersistenciaException
	 */
	public void excluiRealizadoFuncIndicador(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {

			//Gerar historico
			IndicadorFuncionarioRealizadoVO antigoVO = this.obtemRealizadoFuncIndicador(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes);
			if (antigoVO != null) {
				this.incluiHistoricoRealizadoFuncIndicador(antigoVO);
			}

			//Exclui registro
			StringBuffer query = new StringBuffer(
					" DELETE FROM SRV_REALIZADO_FUNC_INDICADOR A 	    " +
					" WHERE A.COD_FUNC     	     = ?             		" +
					"   AND A.COD_INDIC     	 = ?             		" +
					"   AND A.COD_EMP     	     = ?             		" +
					"   AND A.COD_FIL	     	 = ?             		" +
					"   AND A.NUM_ANO 		     = ?             		" +
					"   AND A.NUM_MES 		     = ?             		");

			List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
			parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, new Long(idFuncionario)));
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idIndicador)));
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idEmpresa)));
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idFilial)));
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(ano)));
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(mes)));
			
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel excluir o realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codigoIndicador
	 * @param codigoFuncionario
	 * @return
	 * @throws PersistenciaException
	 */
	public IndicadorFuncionarioRealizadoVO obtemPesoMeta(int ano, int mes, int codigoIndicador, long codigoFuncionario) throws PersistenciaException {

		IndicadorFuncionarioRealizadoVO vo = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT COD_INDIC, COD_UN_PESO, NUM_PESO, COD_UN_META, NUM_META FROM SRV_REALIZADO_FUNC_INDICADOR WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_INDIC = ? AND COD_FUNC = ? ";
			stmt = getConn().prepareStatement(sql);
			int i = 0;
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setInteger(stmt, ++i, codigoIndicador);
			setLong(stmt, ++i, codigoFuncionario);
			rs = stmt.executeQuery();
			if (rs.next()) {
				vo = new IndicadorFuncionarioRealizadoVO();
				vo.setIdIndicador(getInteger(rs, "COD_INDIC"));
				vo.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				vo.setPeso(getDouble(rs, "NUM_PESO"));
				vo.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				vo.setMeta(getDouble(rs, "NUM_META"));
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter o peso e meta do realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return vo;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codigoPresidente
	 * @param codIndicIni
	 * @param codIndicFim
	 * @return
	 * @throws PersistenciaException
	 */
	public List<FuncionarioVO> obtemListaFuncionarioBonusEmAberto(int ano, int mes, long codigoPresidente, int codIndicIni, int codIndicFim) throws PersistenciaException {

		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = 
				" SELECT FI.COD_FUNC, FI.COD_FIL, FI.COD_EMP " +
				"   FROM SRV_REALIZADO_FUNC_INDICADOR FI, SRV_INDICADOR I " +
				"  WHERE FI.COD_INDIC = I.COD_INDIC " +
				"    AND FI.NUM_ANO = ? " +
				"    AND FI.NUM_MES = ? " +
				"    AND I.COD_INDIC BETWEEN ? AND ? " +
				"    AND (I.COD_GRP_INDIC = ? OR " +
				"        (I.COD_GRP_INDIC = ? AND FI.COD_FUNC = ?)) " +
				" MINUS " +
				" SELECT FI.COD_FUNC, FI.COD_FIL, FI.COD_EMP " +
				"   FROM SRV_REALIZADO_FUNC_INDICADOR FI, SRV_INDICADOR I " +
				"  WHERE FI.COD_INDIC = I.COD_INDIC " +
				"    AND FI.NUM_ANO = ? " +
				"    AND FI.NUM_MES = ? " +
				"    AND I.COD_INDIC = ? " +
				"    AND NVL(STA_CALC_REALZ, ?) > ? ";

			stmt = getConn().prepareStatement(sql);
			int i = 0;
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codIndicIni);
			setLong(stmt, ++i, codIndicFim);
			setInteger(stmt, ++i, Constantes.COD_GRUPO_INDIC_INDIVIDUAL);
			setInteger(stmt, ++i, Constantes.COD_GRUPO_INDIC_CORPORATIVO);
			setLong(stmt, ++i, codigoPresidente);
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setInteger(stmt, ++i, Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
			setInteger(stmt, ++i, StatCalcRealzEnum.INICIADO.getCodigo());
			setInteger(stmt, ++i, StatCalcRealzEnum.INICIADO.getCodigo());
			rs = stmt.executeQuery();
			while (rs.next()) {
				FuncionarioVO funcVO = new FuncionarioVO();
				funcVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				funcVO.setIdFilial(getInteger(rs, "COD_FIL"));
				funcVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				lista.add(funcVO);
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter a lista de funcionarios em aberto do bonus: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codigoFuncionario
	 * @param codFilial
	 * @param codGrpIndic
	 * @param codGrpIndicOpcional
	 * @return
	 * @throws PersistenciaException
	 */
	public double obtemSomaPesoIndicadorFuncionarioBonus(int ano, int mes, long codigoFuncionario, int codEmpresa, int codFilial, int codGrpIndic, int codGrpIndicOpcional) throws PersistenciaException {

		double soma = 0.0D;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer(" SELECT SUM(FI.NUM_PESO) AS SOMA_PESO " +
												"   FROM SRV_REALIZADO_FUNC_INDICADOR FI, " +
												"        SRV_INDICADOR I " +
												"  WHERE FI.COD_INDIC = I.COD_INDIC " +
												"    AND FI.NUM_ANO = ? " +
												"    AND FI.NUM_MES = ? " +
												"    AND FI.COD_FUNC = ? " +
												"    AND FI.COD_EMP = ? " +
												"    AND FI.COD_FIL = ? " +
												"	 AND FI.COD_INDIC <> ? ");

			if (codGrpIndicOpcional > 0) {
				sql.append("    AND (I.COD_GRP_INDIC = ? OR I.COD_GRP_INDIC = ?) ");
			} else {
				sql.append("    AND I.COD_GRP_INDIC = ? ");
			}

			stmt = getConn().prepareStatement(sql.toString());
			int i = 0;
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codigoFuncionario);
			setInteger(stmt, ++i, codEmpresa);
			setInteger(stmt, ++i, codFilial);
			setInteger(stmt, ++i, Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
			if (codGrpIndicOpcional > 0) {
				setInteger(stmt, ++i, codGrpIndic);
				setInteger(stmt, ++i, codGrpIndicOpcional);
			} else {
				setInteger(stmt, ++i, codGrpIndic);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				soma = getDouble(rs, "SOMA_PESO");
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter a soma de pesos dos indicadores de funcionario no bonus: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return soma;
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @param codGrpRemVar
	 * @return
	 * @throws PersistenciaException
	 */
	public List<Integer> obtemListaIdFilialRealizadoFuncIndicador(Long idFuncionario, Integer ano, Integer mes, Integer codGrpRemVar) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT R.COD_FIL " +
						"   FROM SRV_REALIZADO_FUNC_INDICADOR R, " +
						"        SRV_INDICADOR I, " +
						"        SRV_GRUPO_INDICADOR GI " +
						"  WHERE R.COD_INDIC = I.COD_INDIC " +
						"    AND GI.COD_GRP_INDIC = I.COD_GRP_INDIC " +
						"    AND GI.COD_TIPO_REM_VAR = ? " +
						"    AND R.NUM_ANO = ? " +
						"    AND R.NUM_MES = ? " +
						"    AND R.COD_FUNC = ? " +
						"  GROUP BY R.COD_FIL " +
						"  ORDER BY R.COD_FIL ");

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codGrpRemVar));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Integer> listaIdFilial = new ArrayList<Integer>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			while (rs.next()) {
				listaIdFilial.add(getInteger(rs, "COD_FIL"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter a lista de filial do realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaIdFilial;
	}	    

	/**
	 * 
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @param idEmpresa
	 * @param idFilial
	 * @param idGrupoIndic
	 * @param idIndicPai
	 * @return
	 * @throws PersistenciaException
	 */
	public List<IndicadorFuncionarioRealizadoVO> obtemResultadoRealizadoFuncIndicador(Long idFuncionario, Integer ano, Integer mes, Integer idEmpresa, Integer idFilial, Integer idGrupoIndic, Integer idIndicPai) throws PersistenciaException {

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
		
		StringBuffer query = new StringBuffer(
				
					"SELECT A.COD_EMP									" +
					"      ,A.COD_FIL									" +
					"      ,B.COD_GRP_INDIC								" +
					"      ,C.DESCR_GRP_INDIC							" +
					"	   ,A.COD_INDIC 								" +
					"      ,B.DESCR_INDIC								" +
					"      ,A.COD_UN_PESO								" +
					"      ,A.NUM_PESO									" +
					"      ,A.COD_UN_REALZ_POND							" +
					"      ,A.NUM_REALZ_POND							" +
					"      ,A.COD_UN_META								" +
					"      ,A.NUM_META									" +
					"      ,A.COD_UN_REALZ								" +
					"      ,A.NUM_REALZ									" +
					"      ,A.COD_UN_REALZ_X_META						" +
					"      ,A.NUM_REALZ_X_META							" +
					"      ,A.COD_ESCALA								" +
					"      ,A.COD_UN_REALZ_FX							" +
					"      ,A.NUM_REALZ_FX								" +
					"      ,A.COD_UN_VLR_PREMIO_FUNC_CALC				" +
					"      ,A.STA_CALC_REALZ							" +
					"      ,A.VLR_PREMIO_FUNC_CALC						" +
					"      ,A.DT_INI_SIT_SRV							" +
					"      ,A.COD_USUARIO								" +
					"      ,A.DESCR_META								" +
					"      ,B.FLG_PREENCH_ATING_IGUAL_REALZ				" +
					"      ,B.FORMULA_INDIC								" +
					"      ,E.NUM_ESCALA								" +
					"      ,B.DESCR_FORMULA_INDIC						" +
					"      ,B.DESCR_FONTE								" +
					"      ,B.COD_INDIC_PAI								" +
					"      ,B.FLG_SENTIDO								" +
					"  FROM SRV_REALIZADO_FUNC_INDICADOR A 				" +
					"      ,SRV_INDICADOR                B 				" +
					"      ,SRV_GRUPO_INDICADOR 		 C 				" +
					"      ,SRV_TIPO_REM_VAR 			 D				" +
					"      ,SRV_ESCALA              	 E				" +
					" WHERE A.COD_INDIC          = B.COD_INDIC			" +
					"   AND B.COD_GRP_INDIC      = C.COD_GRP_INDIC		" +
					"   AND C.COD_TIPO_REM_VAR   = D.COD_TIPO_REM_VAR	" +
					"   AND A.COD_ESCALA 		 = E.COD_ESCALA(+)		" +
					"   AND D.DESCR_TIPO_REM_VAR = ?             		" +
					"   AND A.COD_FUNC     	     = ?             		" +
					"   AND A.NUM_ANO 		     = ?             		" +
					"   AND A.NUM_MES 		     = ?             		");
		if (ObjectHelper.isNotEmpty(idEmpresa)) {
			query.append("   AND A.COD_EMP = ? ");
		}
		if (ObjectHelper.isNotEmpty(idFilial)) {
			query.append("   AND A.COD_FIL = ? ");
		}
		if (ObjectHelper.isNotEmpty(idGrupoIndic)) {
			query.append("   AND B.COD_GRP_INDIC = ?            		");
		}
		if (ObjectHelper.isNotEmpty(idIndicPai)) {
			query.append("   AND B.COD_INDIC_PAI = ?            		");
			query.append(" ORDER BY C.DESCR_GRP_INDIC, B.DESCR_INDIC	");
		} else {
			query.append("   AND B.COD_INDIC_PAI IS NULL         		");
			query.append(" ORDER BY A.NUM_PESO DESC	");
		}

		
		parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING,  "CORPORATIVO"));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, 	 idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		if (ObjectHelper.isNotEmpty(idEmpresa)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa));
		}
		if (ObjectHelper.isNotEmpty(idFilial)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial));
		}
		if (ObjectHelper.isNotEmpty(idGrupoIndic)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupoIndic));
		}
		if (ObjectHelper.isNotEmpty(idIndicPai)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicPai));
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndicadorFuncionarioRealizadoVO> lista = new ArrayList<IndicadorFuncionarioRealizadoVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = new IndicadorFuncionarioRealizadoVO();
				indicadorFuncionarioRealizadoVO.setIdFuncionario(idFuncionario);
				indicadorFuncionarioRealizadoVO.setAno(ano);
				indicadorFuncionarioRealizadoVO.setMes(mes);
				indicadorFuncionarioRealizadoVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				indicadorFuncionarioRealizadoVO.setIdFilial(getInteger(rs, "COD_FIL"));
				indicadorFuncionarioRealizadoVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				indicadorFuncionarioRealizadoVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				indicadorFuncionarioRealizadoVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				if (ObjectHelper.isNotEmpty(idIndicPai)) {
					indicadorFuncionarioRealizadoVO.setDescricaoIndicador(" - " + getString(rs, "DESCR_INDIC"));
				} else {
					indicadorFuncionarioRealizadoVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				}
				indicadorFuncionarioRealizadoVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				indicadorFuncionarioRealizadoVO.setPeso(getDouble(rs, "NUM_PESO"));
				indicadorFuncionarioRealizadoVO.setUnidadeRealizadoPonderacao(getInteger(rs, "COD_UN_REALZ_POND"));
				indicadorFuncionarioRealizadoVO.setRealizadoPonderacao(getDouble(rs, "NUM_REALZ_POND"));
				indicadorFuncionarioRealizadoVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				indicadorFuncionarioRealizadoVO.setMeta(getDouble(rs, "NUM_META"));
				indicadorFuncionarioRealizadoVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
				indicadorFuncionarioRealizadoVO.setRealizado(getDouble(rs, "NUM_REALZ"));
				indicadorFuncionarioRealizadoVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				indicadorFuncionarioRealizadoVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				indicadorFuncionarioRealizadoVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				indicadorFuncionarioRealizadoVO.setNumEscala(getInteger(rs, "COD_ESCALA"));//getInteger(rs, "NUM_ESCALA"));
				indicadorFuncionarioRealizadoVO.setUnidadeRealizadoFaixa(getInteger(rs, "COD_UN_REALZ_FX"));
				indicadorFuncionarioRealizadoVO.setRealizadoFaixa(getDouble(rs, "NUM_REALZ_FX"));
				indicadorFuncionarioRealizadoVO.setUnidadeValorPremioCalculado(getInteger(rs, "COD_UN_VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioRealizadoVO.setValorPremio(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioRealizadoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				indicadorFuncionarioRealizadoVO.setIdUsuarioAlteracao(getInteger(rs, "COD_USUARIO"));
				indicadorFuncionarioRealizadoVO.setDescricaoMeta(getString(rs, "DESCR_META"));
				indicadorFuncionarioRealizadoVO.setFlgPrrenchAtingIgualRealiz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
				indicadorFuncionarioRealizadoVO.setFormulaIndicador(getString(rs, "FORMULA_INDIC"));
				indicadorFuncionarioRealizadoVO.setDescFormulaIndicador(getString(rs, "DESCR_FORMULA_INDIC"));
				indicadorFuncionarioRealizadoVO.setDescFonte(getString(rs, "DESCR_FONTE"));
				indicadorFuncionarioRealizadoVO.setIdIndicadorPai(getInteger(rs, "COD_INDIC_PAI"));
				indicadorFuncionarioRealizadoVO.setIdStatusCalcRelz(getInteger(rs, "STA_CALC_REALZ"));
				indicadorFuncionarioRealizadoVO.setFlgSentido(getString(rs, "FLG_SENTIDO"));
				lista.add(indicadorFuncionarioRealizadoVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter o resultado do realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}	

	public DetalheCalculoVO obterStatusCalculoRealizadoFuncIndicador(Long codFunc, Integer mes, Integer ano, Integer idEmpresa, Integer idFilial) throws PersistenciaException {

		DetalheCalculoVO detalheCalculoVO = new DetalheCalculoVO();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			StringBuffer query = new StringBuffer(" SELECT NVL(STA_CALC_REALZ,1) AS STA_CALC_REALZ, COD_FUNC, DT_INI_SIT_SRV, NUM_PESO, COD_UN_PESO " +
													" FROM SRV_REALIZADO_FUNC_INDICADOR " +
													" WHERE COD_FUNC = ? " +
													" AND NUM_MES = ? " +
													" AND NUM_ANO = ? " +
													" AND COD_INDIC = ? ");

			if (ObjectHelper.isNotEmpty(idEmpresa)) {
				query.append(" AND COD_EMP = ? ");
			}
			if (ObjectHelper.isNotEmpty(idFilial)) {
				query.append(" AND COD_FIL = ? ");
			}
			stmt = getConn().prepareStatement(query.toString());
			int nroCampo = 1;
			setLong(stmt, nroCampo++, codFunc);
			setInteger(stmt, nroCampo++, mes);
			setInteger(stmt, nroCampo++, ano);
			setInteger(stmt, nroCampo++, Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
			if (ObjectHelper.isNotEmpty(idEmpresa)) {
				setInteger(stmt, nroCampo++, idEmpresa);
			}
			if (ObjectHelper.isNotEmpty(idFilial)) {
				setInteger(stmt, nroCampo++, idFilial);
			}
			rs = stmt.executeQuery();
			if (rs.next()) {
				detalheCalculoVO.setIdFuncionario(getInteger(rs, "COD_FUNC"));
				detalheCalculoVO.setStatusCalculoRealizado(getInt(rs, "STA_CALC_REALZ"));
				detalheCalculoVO.setPeso(getDouble(rs, "NUM_PESO"));
				detalheCalculoVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				detalheCalculoVO.setDtIniSit(getTimestamp(rs, "DT_INI_SIT_SRV"));
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter status de calculo realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return detalheCalculoVO;
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param idFilial
	 * @param idEmpresa
	 * @param ano
	 * @param mes
	 * @param descrTipoRemVar
	 * @return
	 * @throws PersistenciaException
	 */
	public List<IndicadorFuncionarioRealizadoVO> obtemListaRealizadoFuncIndicador(Long idFuncionario, Integer idFilial, Integer idEmpresa, Integer ano, Integer mes, String descrTipoRemVar) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT RFI.* " +
				"   FROM SRV_REALIZADO_FUNC_INDICADOR RFI, SRV_INDICADOR I " +
				"  WHERE RFI.COD_INDIC = I.COD_INDIC " +
				"    AND I.COD_GRP_INDIC IN " +
				"        (SELECT GI.COD_GRP_INDIC " +
				"           FROM SRV_GRUPO_INDICADOR GI, " +
				"                SRV_GRUPO_REM_VARIAVEL GRV " +
				"          WHERE GRV.DESCR_GRP_REM_VAR = ? " +
				"            AND GI.COD_TIPO_REM_VAR = GRV.COD_GRP_REM_VAR) " +
				"    AND RFI.NUM_ANO = ? " +
				"    AND RFI.NUM_MES = ? ");
				if (ObjectHelper.isNotEmpty(idFuncionario)) {
					query.append("    AND RFI.COD_FUNC = ? ");
				}
				if (ObjectHelper.isNotEmpty(idFilial)) {
					query.append("    AND RFI.COD_FIL = ? ");
				}
				if (ObjectHelper.isNotEmpty(idEmpresa)) {
					query.append("    AND RFI.COD_EMP = ? ");
				}
				query.append(" ORDER BY RFI.COD_FUNC, RFI.COD_FIL, RFI.COD_INDIC ");//Ordenacao FUNC e FILIAL nao deve ser alterada (calculo bonus)

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, descrTipoRemVar));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		if (ObjectHelper.isNotEmpty(idFuncionario)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		}
		if (ObjectHelper.isNotEmpty(idFilial)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial));
		}
		if (ObjectHelper.isNotEmpty(idEmpresa)) {
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa));
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<IndicadorFuncionarioRealizadoVO> lista = new ArrayList<IndicadorFuncionarioRealizadoVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO = new IndicadorFuncionarioRealizadoVO();
				indicadorFuncionarioBonusVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				indicadorFuncionarioBonusVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				indicadorFuncionarioBonusVO.setIdFilial(getInteger(rs, "COD_FIL"));
				indicadorFuncionarioBonusVO.setAno(getInteger(rs, "NUM_ANO"));
				indicadorFuncionarioBonusVO.setMes(getInteger(rs, "NUM_MES"));
				indicadorFuncionarioBonusVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				indicadorFuncionarioBonusVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				indicadorFuncionarioBonusVO.setIdCargo(getInteger(rs, "COD_CARGO"));
				indicadorFuncionarioBonusVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				indicadorFuncionarioBonusVO.setPeso(getDouble(rs, "NUM_PESO"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoPonderacao(getInteger(rs, "COD_UN_REALZ_POND"));
				indicadorFuncionarioBonusVO.setRealizadoPonderacao(getDouble(rs, "NUM_REALZ_POND"));
				indicadorFuncionarioBonusVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				indicadorFuncionarioBonusVO.setMeta(getDouble(rs, "NUM_META"));
				indicadorFuncionarioBonusVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
				indicadorFuncionarioBonusVO.setRealizado(getDouble(rs, "NUM_REALZ"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				indicadorFuncionarioBonusVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				indicadorFuncionarioBonusVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				lista.add(indicadorFuncionarioBonusVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter a lista realizado indicador funcionario: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}	    

}