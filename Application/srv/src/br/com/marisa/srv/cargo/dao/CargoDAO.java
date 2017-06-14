package br.com.marisa.srv.cargo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;


/**
 * Classe para tratar dos métodos de acesso a dados do Cargo
 * 
 * @author Walter Fontes
 */
public class CargoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(CargoDAO.class);

    /**
     * 
     * @param pesquisaVO
     * @return
     * @throws PersistenciaException
     */
	public List<CargoVO> obtemListaCargo(CargoVO pesquisaVO) throws PersistenciaException {

		StringBuffer query =  new StringBuffer(
			" SELECT A.COD_CARGO, " +
			"        A.DESCR_CARGO, " +
			"        A.COD_CLAS_HAY, " +
			"        B.DESCR_CLA_HAY, " +
			"        NVL(A.FLG_AGRUPA_FIL_LIDER,'N') AS FLG_AGRUPA_FIL_LIDER, " +
			"        A.DT_INI_SIT_SRV, " +
			"        A.COD_USUARIO " +
			"   FROM SRV_CARGO A, " +
			"        SRV_CLASSE_HAY B " +
			"  WHERE A.COD_CLAS_HAY = B.COD_CLAS_HAY(+) ");

		if (pesquisaVO != null) {
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdCargo())) {
				query.append("   AND A.COD_CARGO = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoCargo())) {
				query.append("   AND TRIM(UPPER(A.DESCR_CARGO)) LIKE '%'||?||'%' ");
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getIdClasseHay())) {
				if (pesquisaVO.getIdClasseHay() != -1) {
					query.append("   AND A.COD_CLAS_HAY = ? ");
				} else {
					query.append("   AND A.COD_CLAS_HAY IS NULL ");
				}
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getAgrupaFiliais())) {
				query.append("   AND A.FLG_AGRUPA_FIL_LIDER = ? ");
			}
		}
		query.append(" ORDER BY A.DESCR_CARGO");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<CargoVO> listaCargoVO = new ArrayList<CargoVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;

			if (pesquisaVO != null) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getIdCargo())) {
					setInteger(stmt, i++, pesquisaVO.getIdCargo());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoCargo())) {
					setString(stmt, i++, pesquisaVO.getDescricaoCargo().trim().toUpperCase());
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIdClasseHay())) {
					if (pesquisaVO.getIdClasseHay() != -1) {
						setInteger(stmt, i++, pesquisaVO.getIdClasseHay());
					}
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getAgrupaFiliais())) {
					setBoolean(stmt, i++, pesquisaVO.getAgrupaFiliais());
				}
			}
            rs = stmt.executeQuery();

            while (rs.next()) {
            	CargoVO cargoVO = new CargoVO();
            	cargoVO.setIdCargo(getInteger(rs, "COD_CARGO"));
            	cargoVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
            	cargoVO.setIdClasseHay(getInteger(rs, "COD_CLAS_HAY"));
            	cargoVO.setDescricaoClasseHay(getString(rs, "DESCR_CLA_HAY"));
            	cargoVO.setAgrupaFiliais(getBoolean(rs, "FLG_AGRUPA_FIL_LIDER"));
            	cargoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
            	cargoVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
            	listaCargoVO.add(cargoVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter os Cargos: ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return listaCargoVO;
    }

    /**
     * Obtém cargo por id
     * 
     * @param idCargo
     * @return
     */
    public CargoVO obtemCargo(int idCargo) throws PersistenciaException {

        String query =  "SELECT A.COD_CARGO, A.DESCR_CARGO, A.COD_CLAS_HAY, B.DESCR_CLA_HAY, A.FLG_AGRUPA_FIL_LIDER, A.DT_INI_SIT_SRV, A.COD_USUARIO " +
                        "  FROM SRV_CARGO A, SRV_CLASSE_HAY B     " +
                        " WHERE A.COD_CARGO = ?  ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CargoVO cargoVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, idCargo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	cargoVO = new CargoVO();
            	cargoVO.setIdCargo(getInteger(rs, "COD_CARGO"));
            	cargoVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
            	cargoVO.setIdClasseHay(getInteger(rs, "COD_CLAS_HAY"));
            	cargoVO.setDescricaoClasseHay(getString(rs, "DESCR_CLA_HAY"));
            	cargoVO.setAgrupaFiliais(getBoolean(rs, "FLG_AGRUPA_FIL_LIDER"));
            	cargoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
            	cargoVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o cargo " + idCargo, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return cargoVO;
    }
    
    
    /**
     * Altera o cargo
     * 
     * @param cargoVO
     * @return
     */
    public void alteraCargo(CargoVO cargoVO) throws PersistenciaException {

        String query =  "UPDATE SRV_CARGO           		 " +
        				"   SET DESCR_CARGO 			= ?, " +
        				"		COD_CLAS_HAY 			= ?, " +
        				"		FLG_AGRUPA_FIL_LIDER 	= ?, " +
        				"		DT_INI_SIT_SRV 			= ?, " +
        				"       COD_USUARIO 			= ?  " +
        				" WHERE COD_CARGO        		= ?  ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setString 	(stmt, ordemCampos++, cargoVO.getDescricaoCargo().trim());
            setInteger	(stmt, ordemCampos++, cargoVO.getIdClasseHay());
            setBoolean	(stmt, ordemCampos++, cargoVO.getAgrupaFiliais());
            setTimestamp(stmt, ordemCampos++, cargoVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, cargoVO.getIdUsuarioUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, cargoVO.getIdCargo());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar o cargo.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
    
    /**
     * Inclui cargo
     * 
     * @param cargoVO
     * @return
     */
    public void incluiCargo(CargoVO cargoVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_CARGO            " +
        				"      (COD_CARGO          		, " +
        				"       DESCR_CARGO 		  	, " +
        				"		COD_CLAS_HAY 	  		, " +
        				"		FLG_AGRUPA_FIL_LIDER 	, " +
        				"		DT_INI_SIT_SRV 		    , " +
        				"		COD_USUARIO)  		      " +
                        " VALUES (?, ?, ?, ?, ?, ?)       ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, cargoVO.getIdCargo());
            setString 	(stmt, ordemCampos++, cargoVO.getDescricaoCargo().trim());
            setInteger 	(stmt, ordemCampos++, cargoVO.getIdClasseHay());
            setBoolean	(stmt, ordemCampos++, cargoVO.getAgrupaFiliais());
            setTimestamp(stmt, ordemCampos++, cargoVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, cargoVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o cargo", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }    
    

    /**
     * Inclui o historico do cargo
     * 
     * @param cargoVO
     * @return
     */
    public void incluiCargoHistorico(CargoVO cargoVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_CARGO_HIST 	  " +
						"      (COD_CARGO          		, " +
						"       SEQ_CARGO 		  		, " +
						"       DESCR_CARGO 		  	, " +
						"       COD_CLAS_HAY 		  	, " +
						"		FLG_AGRUPA_FIL_LIDER 	, " +
						"		DT_INI_SIT_SRV 		    , " +
						"		COD_USUARIO)  		      " +
				        " VALUES (?, ?, ?, ?, ?, ?, ?)    ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, cargoVO.getIdCargo());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(cargoVO.getIdCargo().intValue()));
            setString 	(stmt, ordemCampos++, cargoVO.getDescricaoCargo().trim());
            setInteger 	(stmt, ordemCampos++, cargoVO.getIdClasseHay());
            setBoolean	(stmt, ordemCampos++, cargoVO.getAgrupaFiliais());
            setTimestamp(stmt, ordemCampos++, cargoVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, cargoVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o cargo historico", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idCargo
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idCargo) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_CARGO)     " +
                        "  FROM SRV_CARGO_HIST     " +
                        " WHERE COD_CARGO =?       ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idCargo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do cargo " + idCargo, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
}