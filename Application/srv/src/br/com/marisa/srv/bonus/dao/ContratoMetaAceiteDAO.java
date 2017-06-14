package br.com.marisa.srv.bonus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import br.com.marisa.srv.bonus.vo.ContratoMetaAceiteVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

/**
 * 
 * @author levy.villar
 * 
 */
public class ContratoMetaAceiteDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(ContratoMetaAceiteDAO.class);

	/**
	 * 
	 * @param aceiteVO
	 * @return
	 * @throws PersistenciaException
	 */
	public ContratoMetaAceiteVO obtemContratoMetaAceite(ContratoMetaAceiteVO aceiteVO) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(
				" SELECT COD_FUNC, " +
				"        COD_INDIC, " +
				"        COD_EMP, " +
				"        COD_FIL, " +
				"        NUM_ANO, " +
				"        NUM_MES, " +
				"        DT_ACEITE, " +
				"        COD_USU_ACEITE " +
				"   FROM SRV_CONTRATO_META_ACEITE " +
				" WHERE COD_FUNC = ? " +
				"   AND COD_INDIC = ? " +
				"   AND COD_EMP = ? " +
				"   AND COD_FIL = ? " +
				"   AND NUM_ANO = ? " +
				"   AND NUM_MES = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ContratoMetaAceiteVO retornoVO = null;
		

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			int i = 0;
			setLong(stmt, ++i, aceiteVO.getCodFuncionario());
			setInteger(stmt, ++i, Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
			setInteger(stmt, ++i, aceiteVO.getIdEmpresa());
			setInteger(stmt, ++i, aceiteVO.getIdFilial());
			setInteger(stmt, ++i, aceiteVO.getNumAno());
			setInteger(stmt, ++i, Constantes.NUM_MES_DEZEMBRO);//aceiteVO.getNumMes()
			rs = stmt.executeQuery();

			if (rs.next()) {
				retornoVO = new ContratoMetaAceiteVO();
				retornoVO.setCodFuncionario(getLong(rs, "COD_FUNC"));
				retornoVO.setNumAno(getInteger(rs, "NUM_ANO"));
				retornoVO.setNumMes(getInteger(rs, "NUM_MES"));
				retornoVO.setIdFilial(getInteger(rs, "COD_FIL"));
				retornoVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				retornoVO.setDataAceite(getTimestamp(rs, "DT_ACEITE"));
				retornoVO.setCodUsuario(getInteger(rs, "COD_USU_ACEITE"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o aceite do bonus COD_FUNC=" + retornoVO.getCodFuncionario(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return retornoVO;
	}

	/**
	 * 
	 * @param aceiteVO
	 * @throws PersistenciaException
	 */
	public void incluiContratoMetaAceite(ContratoMetaAceiteVO aceiteVO) throws PersistenciaException {

		String query =  " INSERT INTO SRV_CONTRATO_META_ACEITE " +
						"	(COD_FUNC, COD_INDIC, COD_EMP, COD_FIL, NUM_ANO, NUM_MES, DT_ACEITE, COD_USU_ACEITE) " +
						" VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 1;
			setLong(stmt, i++, aceiteVO.getCodFuncionario());
			setInteger(stmt, i++, Constantes.COD_INDIC_AGRUPAMENTO_CORPORATIVO);
			setInteger(stmt, ++i, aceiteVO.getIdEmpresa());
			setInteger(stmt, ++i, aceiteVO.getIdFilial());
			setInteger(stmt, ++i, aceiteVO.getNumAno());
			setInteger(stmt, ++i, aceiteVO.getNumMes());
			setInteger(stmt, i++, aceiteVO.getCodUsuario());
			stmt.execute();
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir o aceite do bonus ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

}