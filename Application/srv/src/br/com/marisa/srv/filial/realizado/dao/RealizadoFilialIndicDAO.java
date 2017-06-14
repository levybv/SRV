package br.com.marisa.srv.filial.realizado.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.realizado.vo.RealizadoFilialIndicVO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.IndicadorVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class RealizadoFilialIndicDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(RealizadoFilialIndicDAO.class);

	/**
	 * 
	 * @param codIndic
	 * @param codFilial
	 * @param codEmpresa
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public RealizadoFilialIndicVO obtemRealizadoFilialIndic(Integer codIndic, Integer codFilial, Integer codEmpresa, Integer ano, Integer mes) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(
				" SELECT RFI.COD_INDIC, " +
				"        I.DESCR_INDIC, " +
				"        RFI.COD_FIL, " +
				"        F.DESCR_FIL, " +
				"        RFI.COD_EMP, " +
				"        RFI.NUM_ANO, " +
				"        RFI.NUM_MES, " +
				"        RFI.NUM_REALZ, " +
				"        RFI.DT_INI_SIT_SRV, " +
				"        RFI.COD_USUARIO " +
				"   FROM SRV_REALIZADO_FILIAL_INDIC RFI, " +
				"        SRV_INDICADOR I, " +
				"        SRV_FILIAL F " +
				"  WHERE RFI.COD_INDIC = I.COD_INDIC " +
				"    AND RFI.COD_FIL = F.COD_FIL " +
				"    AND RFI.COD_EMP = F.COD_EMP " +
				"    AND RFI.COD_INDIC = ? " +
				"    AND RFI.COD_FIL = ? " +
				"    AND RFI.COD_EMP = ? " +
				"    AND RFI.NUM_ANO = ? " +
				"    AND RFI.NUM_MES = ? " +
				"  ORDER BY RFI.NUM_ANO, RFI.NUM_MES, RFI.COD_FIL, RFI.COD_INDIC ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		RealizadoFilialIndicVO retornoVO = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			int i = 0;
			setInteger(stmt, ++i, codIndic);
			setInteger(stmt, ++i, codFilial);
			setInteger(stmt, ++i, codEmpresa);
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			rs = stmt.executeQuery();

			if (rs.next()) {
				retornoVO = new RealizadoFilialIndicVO();
				IndicadorVO indicVO = new IndicadorVO();
				indicVO.setCodIndicador(getInteger(rs, "COD_INDIC"));
				indicVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				retornoVO.setIndicadorVO(indicVO);
				FilialVO filialVO = new FilialVO();
				filialVO.setCodFilial(getInteger(rs, "COD_FIL"));
				filialVO.setDescricao(getString(rs, "DESCR_FIL"));
				filialVO.setCodEmpresa(getInteger(rs, "COD_EMP"));
				retornoVO.setFilialVO(filialVO);
				retornoVO.setAno(getInteger(rs, "NUM_ANO"));
				retornoVO.setMes(getInteger(rs, "NUM_MES"));
				retornoVO.setNumRealizado(getDouble(rs, "NUM_REALZ"));
				retornoVO.setCodUsuario(getInteger(rs, "COD_USUARIO"));
				retornoVO.setDataIniSitSrv(getTimestamp(rs, "DT_INI_SIT_SRV"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter o realizado filial indic: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return retornoVO;
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<RealizadoFilialIndicVO> obtemListaRealizadoFilialIndic(RealizadoFilialIndicVO pesquisaVO) throws PersistenciaException {		

		StringBuffer query = new StringBuffer(
				" SELECT RFI.COD_INDIC, " +
				"        I.DESCR_INDIC, " +
				"        RFI.COD_FIL, " +
				"        F.DESCR_FIL, " +
				"        RFI.COD_EMP, " +
				"        RFI.NUM_ANO, " +
				"        RFI.NUM_MES, " +
				"        RFI.NUM_REALZ, " +
				"        RFI.DT_INI_SIT_SRV, " +
				"        RFI.COD_USUARIO " +
				"   FROM SRV_REALIZADO_FILIAL_INDIC RFI, " +
				"        SRV_INDICADOR I, " +
				"        SRV_FILIAL F " +
				"  WHERE RFI.COD_INDIC = I.COD_INDIC " +
				"    AND RFI.COD_FIL = F.COD_FIL " +
				"    AND RFI.COD_EMP = F.COD_EMP ");

		if (ObjectHelper.isNotNull(pesquisaVO)) {
			if (ObjectHelper.isNotNull(pesquisaVO.getIndicadorVO())) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getIndicadorVO().getCodIndicador())) {
					query.append(getWhereAnd(query)).append(" RFI.COD_INDIC = ? ");
				}
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getFilialVO())) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getFilialVO().getCodFilial())) {
					query.append(getWhereAnd(query)).append(" RFI.COD_FIL = ? ");
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getFilialVO().getCodEmpresa())) {
					query.append(getWhereAnd(query)).append(" RFI.COD_EMP = ? ");
				}
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getAno())) {
				query.append(getWhereAnd(query)).append(" RFI.NUM_ANO = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getMes())) {
				query.append(getWhereAnd(query)).append(" RFI.NUM_MES = ? ");
			}
		}
		query.append("  ORDER BY RFI.NUM_ANO, RFI.NUM_MES, RFI.COD_FIL, RFI.COD_INDIC ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RealizadoFilialIndicVO> listaRetornoVO = new ArrayList<RealizadoFilialIndicVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			if (ObjectHelper.isNotNull(pesquisaVO)) {
				if (ObjectHelper.isNotNull(pesquisaVO.getIndicadorVO())) {
					if (ObjectHelper.isNotEmpty(pesquisaVO.getIndicadorVO().getCodIndicador())) {
						setInteger(stmt, ++i, pesquisaVO.getIndicadorVO().getCodIndicador());
					}
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getFilialVO())) {
					if (ObjectHelper.isNotEmpty(pesquisaVO.getFilialVO().getCodFilial())) {
						setInteger(stmt, ++i, pesquisaVO.getFilialVO().getCodFilial());
					}
					if (ObjectHelper.isNotEmpty(pesquisaVO.getFilialVO().getCodEmpresa())) {
						setInteger(stmt, ++i, pesquisaVO.getFilialVO().getCodEmpresa());
					}
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getAno())) {
					setInteger(stmt, ++i, pesquisaVO.getAno());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getMes())) {
					setInteger(stmt, ++i, pesquisaVO.getMes());
				}
			}
			rs = stmt.executeQuery();

			while (rs.next()) {
				RealizadoFilialIndicVO retornoVO = new RealizadoFilialIndicVO();
				IndicadorVO indicadorVO = new IndicadorVO();
				indicadorVO.setCodIndicador(getInteger(rs, "COD_INDIC"));
				indicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				retornoVO.setIndicadorVO(indicadorVO);
				FilialVO filialVO = new FilialVO();
				filialVO.setCodFilial(getInteger(rs, "COD_FIL"));
				filialVO.setDescricao(getString(rs, "DESCR_FIL"));
				filialVO.setCodEmpresa(getInteger(rs, "COD_EMP"));
				retornoVO.setFilialVO(filialVO);
				retornoVO.setAno(getInteger(rs, "NUM_ANO"));
				retornoVO.setMes(getInteger(rs, "NUM_MES"));
				retornoVO.setNumRealizado(getDouble(rs, "NUM_REALZ"));
				retornoVO.setCodUsuario(getInteger(rs, "COD_USUARIO"));
				retornoVO.setDataIniSitSrv(getTimestamp(rs, "DT_INI_SIT_SRV"));
				listaRetornoVO.add(retornoVO);
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Nao foi possivel obter a lista de realizado filial indic: " + ex.getMessage(), ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaRetornoVO;
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws PersistenciaException
	 */
	public void incluiRealizadoFilialIndic(RealizadoFilialIndicVO incluiVO) throws PersistenciaException {

		String query =  
			" INSERT INTO SRV_REALIZADO_FILIAL_INDIC " +
			"   (COD_INDIC, " +
			"    COD_FIL, " +
			"    COD_EMP, " +
			"    NUM_ANO, " +
			"    NUM_MES, " +
			"    NUM_REALZ, " +
			"    DT_INI_SIT_SRV, " +
			"    COD_USUARIO) " +
			" VALUES " +
			"   (?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, incluiVO.getIndicadorVO().getCodIndicador());
			setInteger(stmt, ++i, incluiVO.getFilialVO().getCodFilial());
			setInteger(stmt, ++i, incluiVO.getFilialVO().getCodEmpresa());
			setInteger(stmt, ++i, incluiVO.getAno());
			setInteger(stmt, ++i, incluiVO.getMes());
			setDouble(stmt, ++i, incluiVO.getNumRealizado());
			setInteger(stmt, ++i, incluiVO.getCodUsuario());
			stmt.execute();
		} catch(Exception ex) {
			throw new PersistenciaException(log, "Nao foi possivel incluir o realizado filial indic: " + ex.getMessage(), ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param excluiVO
	 * @throws PersistenciaException
	 */
	public void excluiRealizadoFilialIndic(RealizadoFilialIndicVO excluiVO) throws PersistenciaException {

		String query =  " DELETE FROM SRV_REALIZADO_FILIAL_INDIC WHERE COD_INDIC = ? AND COD_FIL = ? AND COD_EMP = ? AND NUM_ANO = ? AND NUM_MES = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, excluiVO.getIndicadorVO().getCodIndicador());
			setInteger(stmt, ++i, excluiVO.getFilialVO().getCodFilial());
			setInteger(stmt, ++i, excluiVO.getFilialVO().getCodEmpresa());
			setInteger(stmt, ++i, excluiVO.getAno());
			setInteger(stmt, ++i, excluiVO.getMes());
			stmt.execute();
		} catch(Exception ex) {
			throw new PersistenciaException(log, "Nao foi possivel excluir o realizado filial indic: " + ex.getMessage(), ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws PersistenciaException
	 */
	public void incluiRealizadoFilialIndicHist(RealizadoFilialIndicVO incluiVO) throws PersistenciaException {

		String query =  
			" INSERT INTO SRV_REALIZADO_FIL_INDIC_HIST " +
			"   (COD_INDIC, " +
			"    COD_FIL, " +
			"    COD_EMP, " +
			"    NUM_ANO, " +
			"    NUM_MES, " +
			"    NUM_SEQ, " +
			"    NUM_REALZ, " +
			"    DT_INI_SIT_SRV, " +
			"    COD_USUARIO) " +
			" VALUES " +
			"   (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, incluiVO.getIndicadorVO().getCodIndicador());
			setInteger(stmt, ++i, incluiVO.getFilialVO().getCodFilial());
			setInteger(stmt, ++i, incluiVO.getFilialVO().getCodEmpresa());
			setInteger(stmt, ++i, incluiVO.getAno());
			setInteger(stmt, ++i, incluiVO.getMes());
			setInteger(stmt, ++i, this.obtemProximaSequenciaHistorio(incluiVO.getIndicadorVO().getCodIndicador(), incluiVO.getFilialVO().getCodFilial(), incluiVO.getFilialVO().getCodEmpresa(), incluiVO.getAno(), incluiVO.getMes()));
			setDouble(stmt, ++i, incluiVO.getNumRealizado());
			setTimestamp(stmt, ++i, incluiVO.getDataIniSitSrv());
			setInteger(stmt, ++i, incluiVO.getCodUsuario());
			stmt.execute();
		} catch(Exception ex) {
			throw new PersistenciaException(log, "Nao foi possivel incluir o historico do realizado filial indic: " + ex.getMessage(), ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param codIndic
	 * @param codFilial
	 * @param codEmpresa
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	private int obtemProximaSequenciaHistorio(Integer codIndic, Integer codFilial, Integer codEmpresa, Integer ano, Integer mes) throws PersistenciaException {

		String query = 
			" SELECT MAX(NUM_SEQ) " +
			"   FROM SRV_REALIZADO_FIL_INDIC_HIST " +
			"  WHERE COD_INDIC = ? " +
			"    AND COD_FIL = ? " +
			"    AND COD_EMP = ? " +
			"    AND NUM_ANO = ? " +
			"    AND NUM_MES = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int proximaSequencia = 1;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);

			int i = 0;
			setInteger(stmt, ++i, codIndic);
			setInteger(stmt, ++i, codFilial);
			setInteger(stmt, ++i, codEmpresa);
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			rs = stmt.executeQuery();

			if (rs.next()) {
				proximaSequencia = getInt(rs, 1) + 1;
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Nao foi possivel obter a proxima sequencia de historico de realizado filial indic: " + ex.getMessage(), ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return proximaSequencia;
	}

}