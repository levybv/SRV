package br.com.marisa.srv.digitalizado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.digitalizado.vo.DigitalizadoVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;


public class DigitalizadoDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(DigitalizadoDAO.class);

	public boolean existeTabelaPeriodoSax(String ano, String mes) throws PersistenciaException {

		boolean exist = false;
		String tabela = "SRV_REALZFU_EMP_SEG_SAX_".concat(ano).concat(mes);

		 StringBuffer query =  new StringBuffer(
				" SELECT FLG_DOC_DIGITALIZADO FROM " + tabela);

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
		         
			if (rs.next()) {
				exist = true;
			}

		} catch (Exception e) {
			if ( e.getMessage().indexOf("ORA-00942") > -1 ) {
				exist = false;
			} else {
				e.printStackTrace();
				throw new PersistenciaException(log, "Não foi possível obter os dados de digitalizados SAX.", e);
			}
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return exist;
	}

	public boolean existeDigitalizadoSax(DigitalizadoVO digitalizadoVO) throws PersistenciaException {

		boolean exist = false;
		String tabela = "SRV_REALZFU_EMP_SEG_SAX_".concat(digitalizadoVO.getAno()).concat(digitalizadoVO.getMes());

		 StringBuffer query =  new StringBuffer(
				" SELECT FLG_DOC_DIGITALIZADO " +
				" FROM "+ tabela +
				" WHERE COD_CONTRATO = ? " +
				"   AND FIL_COD = ? " +
				" 	AND CLI_CPF = ? " );

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setString(stmt, i++, digitalizadoVO.getCodContrato());
            setInteger(stmt, i++, digitalizadoVO.getFilCod());
			setLong(stmt, i++, digitalizadoVO.getCliCpf());
			rs = stmt.executeQuery();
		         
			if (rs.next()) {
				exist = true;
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível encontrar os dados digitalizados SAX.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return exist;
	}

	public void alteraFlgDocDigitalizadoSAX(DigitalizadoVO digitalizadoVO) throws PersistenciaException {
		
		String tabela = "SRV_REALZFU_EMP_SEG_SAX_".concat(digitalizadoVO.getAno()).concat(digitalizadoVO.getMes());

		StringBuffer query =  new StringBuffer(
				" UPDATE " + tabela +
                "    SET FLG_DOC_DIGITALIZADO = 'N' " +
				"  WHERE COD_CONTRATO = ? " +
				"    AND FIL_COD = ? " +
				" 	 AND CLI_CPF = ? " );
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, digitalizadoVO.getCodContrato())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, Integer.valueOf(digitalizadoVO.getFilCod()))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG, Long.valueOf(digitalizadoVO.getCliCpf()))); 

		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar flag digitalizado SAX.", e);
		} finally {
			closeStatement(stmt);
		}
	}	

	public boolean existeTabelaPeriodoPL(String ano, String mes) throws PersistenciaException {

		boolean exist = false;
		String tabela = "SRV_REALZFU_CART_APROV_".concat(ano).concat(mes);

		 StringBuffer query =  new StringBuffer(
				" SELECT FLG_DOC_DIGITALIZADO FROM " + tabela);

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
		         
			if (rs.next()) {
				exist = true;
			}

		} catch (Exception e) {
			if ( e.getMessage().indexOf("ORA-00942") > -1 ) {
				exist = false;
			} else {
				e.printStackTrace();
				throw new PersistenciaException(log, "Não foi possível obter os dados de digitalizados PL.", e);
			}
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return exist;
	}

	public boolean existeDigitalizadoPL(DigitalizadoVO digitalizadoVO) throws PersistenciaException {

		boolean exist = false;
		String tabela = "SRV_REALZFU_CART_APROV_".concat(digitalizadoVO.getAno()).concat(digitalizadoVO.getMes());

		 StringBuffer query =  new StringBuffer(
				" SELECT FLG_DOC_DIGITALIZADO " +
				" FROM "+ tabela +
				" WHERE CPF_VENDEDOR = ? " +
				"   AND FIL_COD = ? " +
				" 	AND CLI_CPF = ? " );

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setLong(stmt, i++, digitalizadoVO.getCpfVendedor());
            setInteger(stmt, i++, digitalizadoVO.getFilCod());
			setLong(stmt, i++, digitalizadoVO.getCliCpf());
			rs = stmt.executeQuery();
		         
			if (rs.next()) {
				exist = true;
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível encontrar os dados digitalizados PL.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return exist;
	}

	public void alteraFlgDocDigitalizadoPL(DigitalizadoVO digitalizadoVO) throws PersistenciaException {
		
		String tabela = "SRV_REALZFU_CART_APROV_".concat(digitalizadoVO.getAno()).concat(digitalizadoVO.getMes());

		StringBuffer query =  new StringBuffer(
				" UPDATE " + tabela +
                "    SET FLG_DOC_DIGITALIZADO = 'N' " +
				"  WHERE CPF_VENDEDOR = ? " +
				"    AND FIL_COD = ? " +
				" 	 AND CLI_CPF = ? " );
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG, new Long(digitalizadoVO.getCpfVendedor()))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, Integer.valueOf(digitalizadoVO.getFilCod()))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG, Long.valueOf(digitalizadoVO.getCliCpf()))); 

		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar flag digitalizado PL.", e);
		} finally {
			closeStatement(stmt);
		}
	}	

}