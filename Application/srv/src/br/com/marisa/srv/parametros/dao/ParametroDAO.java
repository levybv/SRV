package br.com.marisa.srv.parametros.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.parametros.vo.ParametroVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class ParametroDAO extends BasicDAO {

	// Log4J
	private static final Logger log = Logger.getLogger(ParametroDAO.class);

	/**
	 * 
	 * @param nomeProcesso
	 * @param nomeParametro
	 * @param fechaConexao
	 * @return
	 * @throws PersistenciaException
	 */
	public Map<String, String> obtemParametro(String nomeProcesso, String nomeParametro) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(" SELECT * FROM SRV_PARAMETROS P WHERE P.PRM_NOME_PROCESSO = ? AND P.PRM_NOME_PRM = ? AND P.STAT_COD = 1 ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, String> parametro = null;
		

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			int i = 0;
			setString(stmt, ++i, nomeProcesso);
			setString(stmt, ++i, nomeParametro);
			rs = stmt.executeQuery();

			if (rs.next()) {
				parametro = new HashMap<String, String>();
            	for (int x=1; x<=30; x++) {
            		parametro.put(getString(rs, "PRM_DESC_PRM_"+x), getString(rs, "PRM_VLR_PRM_"+x)); 
            	}
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter o parametro informado.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return parametro;
	}

	public Map<String, Map<String, String>> obtemParametro(String nomeProcesso) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(" SELECT * FROM SRV_PARAMETROS P WHERE P.PRM_NOME_PROCESSO = ? AND P.STAT_COD = 1 ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Map<String, String>> parametro = null;
		Map<String, String> parametroItem = null;
		

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			int i = 0;
			setString(stmt, ++i, nomeProcesso);
			rs = stmt.executeQuery();

			while (rs.next()) {
				if (parametro == null) {
					parametro = new HashMap<String, Map<String, String>>();
				}
				parametroItem = new HashMap<String, String>();
            	for (int x=1; x<=30; x++) {
            		parametroItem.put(getString(rs, "PRM_DESC_PRM_"+x), getString(rs, "PRM_VLR_PRM_"+x)); 
            	}
            	parametro.put(getString(rs, "PRM_NOME_PRM"), parametroItem);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter o parametro informado.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return parametro;
	}

	public void incluiCargo(ParametroVO parametroVO) throws PersistenciaException {

		String query = 	" INSERT INTO SRV_PARAMETROS " +
						"   (PRM_NOME_PROCESSO, " +
						"    PRM_NOME_PRM, " +
						"    PRM_DESC_PRM, " +
						"    STAT_COD, " +
						"    PRM_DESC_PRM_1, " +
						"    PRM_VLR_PRM_1, " +
						"    PRM_DESC_PRM_2, " +
						"    PRM_VLR_PRM_2, " +
						"    PRM_DESC_PRM_3, " +
						"    PRM_VLR_PRM_3, " +
						"    PRM_DESC_PRM_4, " +
						"    PRM_VLR_PRM_4, " +
						"    PRM_DESC_PRM_5, " +
						"    PRM_VLR_PRM_5, " +
						"    PRM_DESC_PRM_6, " +
						"    PRM_VLR_PRM_6, " +
						"    PRM_DESC_PRM_7, " +
						"    PRM_VLR_PRM_7, " +
						"    PRM_DESC_PRM_8, " +
						"    PRM_VLR_PRM_8, " +
						"    PRM_DESC_PRM_9, " +
						"    PRM_VLR_PRM_9, " +
						"    PRM_DESC_PRM_10, " +
						"    PRM_VLR_PRM_10, " +
						"    PRM_DESC_PRM_11, " +
						"    PRM_VLR_PRM_11, " +
						"    PRM_DESC_PRM_12, " +
						"    PRM_VLR_PRM_12, " +
						"    PRM_DESC_PRM_13, " +
						"    PRM_VLR_PRM_13, " +
						"    PRM_DESC_PRM_14, " +
						"    PRM_VLR_PRM_14, " +
						"    PRM_DESC_PRM_15, " +
						"    PRM_VLR_PRM_15, " +
						"    PRM_DESC_PRM_16, " +
						"    PRM_VLR_PRM_16, " +
						"    PRM_DESC_PRM_17, " +
						"    PRM_VLR_PRM_17, " +
						"    PRM_DESC_PRM_18, " +
						"    PRM_VLR_PRM_18, " +
						"    PRM_DESC_PRM_19, " +
						"    PRM_VLR_PRM_19, " +
						"    PRM_DESC_PRM_20, " +
						"    PRM_VLR_PRM_20, " +
						"    PRM_DESC_PRM_21, " +
						"    PRM_VLR_PRM_21, " +
						"    PRM_DESC_PRM_22, " +
						"    PRM_VLR_PRM_22, " +
						"    PRM_DESC_PRM_23, " +
						"    PRM_VLR_PRM_23, " +
						"    PRM_DESC_PRM_24, " +
						"    PRM_VLR_PRM_24, " +
						"    PRM_DESC_PRM_25, " +
						"    PRM_VLR_PRM_25, " +
						"    PRM_DESC_PRM_26, " +
						"    PRM_VLR_PRM_26, " +
						"    PRM_DESC_PRM_27, " +
						"    PRM_VLR_PRM_27, " +
						"    PRM_DESC_PRM_28, " +
						"    PRM_VLR_PRM_28, " +
						"    PRM_DESC_PRM_29, " +
						"    PRM_VLR_PRM_29, " +
						"    PRM_DESC_PRM_30, " +
						"    PRM_VLR_PRM_30) " +
						" VALUES " +
						"   (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
						"    ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
						"    ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
						"    ?,?,?,?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int ordemCampos = 1;
			setString(stmt, ordemCampos++, parametroVO.getNomeProcesso());
			setString(stmt, ordemCampos++, parametroVO.getNomeParametro());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro());
			setInteger(stmt, ordemCampos++, parametroVO.getStatCod());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro1());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro1());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro2());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro2());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro3());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro3());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro4());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro4());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro5());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro5());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro6());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro6());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro7());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro7());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro8());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro8());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro9());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro9());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro10());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro10());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro11());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro11());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro12());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro12());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro13());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro13());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro14());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro14());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro15());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro15());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro16());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro16());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro17());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro17());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro18());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro18());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro19());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro19());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro20());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro20());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro21());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro21());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro22());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro22());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro23());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro23());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro24());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro24());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro25());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro25());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro26());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro26());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro27());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro27());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro28());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro28());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro29());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro29());
			setString(stmt, ordemCampos++, parametroVO.getDescricaoParametro30());
			setString(stmt, ordemCampos++, parametroVO.getValorParametro30());

			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir os parametros: ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

}