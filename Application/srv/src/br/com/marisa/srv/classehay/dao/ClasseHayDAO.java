package br.com.marisa.srv.classehay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.classehay.vo.ClasseHayVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;


/**
 * Classe para tratar dos métodos de acesso a dados da Classe Hay
 * 
 * @author Walter Fontes
 */
public class ClasseHayDAO extends BasicDAO {

    //Log4J
	private static final Logger log = Logger.getLogger(ClasseHayDAO.class);    

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<ClasseHayVO> obtemListaClasseHay(ClasseHayVO pesquisaVO) throws PersistenciaException {

		StringBuffer query =  new StringBuffer(
			" SELECT COD_CLAS_HAY, " +
			"        DESCR_CLA_HAY, " +
			"        QTD_SALARIO_MIN, " +
			"        QTD_SALARIO_MAX, " +
			"        DT_INI_SIT_SRV, " +
			"        COD_USUARIO " +
			"   FROM SRV_CLASSE_HAY " +
			"  WHERE 1 = 1 ");

		if (pesquisaVO != null) {
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdClasseHay())) {
				query.append("    AND COD_CLAS_HAY = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
				query.append("    AND TRIM(UPPER(DESCR_CLA_HAY)) LIKE ? ");
			}
		}
		query.append("  ORDER BY COD_CLAS_HAY ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ClasseHayVO> listaClasseHayVO = new ArrayList<ClasseHayVO>();


		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;

			if (pesquisaVO != null) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getIdClasseHay())) {
					setInteger(stmt, i++, pesquisaVO.getIdClasseHay());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
					setString(stmt, i++, "%" + pesquisaVO.getDescricao().trim().toUpperCase() + "%");
				}
			}
            rs = stmt.executeQuery();

            while (rs.next()) {
            	ClasseHayVO classeHayVO = new ClasseHayVO();
            	classeHayVO.setIdClasseHay(getInteger(rs, "COD_CLAS_HAY"));
            	classeHayVO.setDescricao(getString(rs, "DESCR_CLA_HAY"));
            	classeHayVO.setSalarioMinimo(getDouble(rs, "QTD_SALARIO_MIN"));
            	classeHayVO.setSalarioMaximo(getDouble(rs, "QTD_SALARIO_MAX"));
            	listaClasseHayVO.add(classeHayVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a Lista de Classe Hay: ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
		return listaClasseHayVO;
	}

    /**
     * Obtém classes Hay
     * 
     * @param idClasseHay
     * @param descricao
     * @return
     */
    public List<ClasseHayVO> obtemClassesHay(int idClasseHay, String descricao) throws PersistenciaException {

        String query =  "SELECT COD_CLAS_HAY, DESCR_CLA_HAY, QTD_SALARIO_MIN, QTD_SALARIO_MAX, DT_INI_SIT_SRV, COD_USUARIO " +
                        "  FROM SRV_CLASSE_HAY              ";
        
        if (idClasseHay > 0 || !ObjectHelper.isEmpty(descricao)) {
        	query += " WHERE ";
        }
        if (idClasseHay > 0) {
        	query += " COD_CLAS_HAY = ? ";
        }
        if (!ObjectHelper.isEmpty(descricao)) {
        	 if (idClasseHay > 0) {
        		query += " AND ";
        	}
        	query += " trim(upper(DESCR_CLA_HAY)) LIKE '%'||?||'%' ";
        }
        query += " ORDER BY COD_CLAS_HAY";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ClasseHayVO> classesHay = new ArrayList<ClasseHayVO>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int nroCampo = 1;
            if (idClasseHay > 0) {
            	setInteger(stmt, nroCampo++, idClasseHay);
            }
            if (!ObjectHelper.isEmpty(descricao)) {
            	setString(stmt, nroCampo++, descricao.toUpperCase().trim());
            }
            rs = stmt.executeQuery();
            ClasseHayVO classeHayVO = null;
            
            while (rs.next()) {
            	classeHayVO = new ClasseHayVO();
            	classeHayVO.setIdClasseHay(getInteger(rs, "COD_CLAS_HAY"));
            	classeHayVO.setDescricao(getString(rs, "DESCR_CLA_HAY").trim());
            	classeHayVO.setSalarioMinimo(getDouble(rs, "QTD_SALARIO_MIN"));
            	classeHayVO.setSalarioMaximo(getDouble(rs, "QTD_SALARIO_MAX"));
            	classeHayVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
            	classeHayVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
            	classesHay.add(classeHayVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter as classes Hay", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return classesHay;
    }
    
    
    /**
     * Obtém classe Hay por id
     * 
     * @param idClasseHay
     * @return
     */
    public ClasseHayVO obtemClasseHay(int idClasseHay) throws PersistenciaException {

        String query =  "SELECT COD_CLAS_HAY, DESCR_CLA_HAY, QTD_SALARIO_MIN, QTD_SALARIO_MAX, DT_INI_SIT_SRV, COD_USUARIO " +
                        "  FROM SRV_CLASSE_HAY    " +
                        " WHERE COD_CLAS_HAY = ?  ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ClasseHayVO classeHayVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, idClasseHay);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	classeHayVO = new ClasseHayVO();
            	classeHayVO.setIdClasseHay(getInteger(rs, "COD_CLAS_HAY"));
            	classeHayVO.setDescricao(getString(rs, "DESCR_CLA_HAY").trim());
            	classeHayVO.setSalarioMinimo(getDouble(rs, "QTD_SALARIO_MIN"));
            	classeHayVO.setSalarioMaximo(getDouble(rs, "QTD_SALARIO_MAX"));
            	classeHayVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
            	classeHayVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a classe hay " + idClasseHay, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return classeHayVO;
    }
    
    
    /**
     * Altera a classe hay
     * 
     * @param classeHayVO
     * @return
     */
    public void alteraClasseHay(ClasseHayVO classeHayVO) throws PersistenciaException {

        String query =  "UPDATE SRV_CLASSE_HAY           " +
        				"   SET DESCR_CLA_HAY 		= ?, " +
        				"		QTD_SALARIO_MIN 	= ?, " +
        				"		QTD_SALARIO_MAX 	= ?, " +
        				"		DT_INI_SIT_SRV 		= ?, " +
        				"       COD_USUARIO 		= ?  " +
        				" WHERE COD_CLAS_HAY        = ?  ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setString 	(stmt, ordemCampos++, classeHayVO.getDescricao().trim());
            setDouble	(stmt, ordemCampos++, classeHayVO.getSalarioMinimo());
            setDouble	(stmt, ordemCampos++, classeHayVO.getSalarioMaximo());
            setTimestamp(stmt, ordemCampos++, classeHayVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, classeHayVO.getIdUsuarioUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, classeHayVO.getIdClasseHay());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar a classe hay.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
    
    /**
     * Inclui a classe hay
     * 
     * @param classeHayVO
     * @return
     */
    public void incluiClasseHay(ClasseHayVO classeHayVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_CLASSE_HAY     " +
        				"      (COD_CLAS_HAY          , " +
        				"       DESCR_CLA_HAY 		  , " +
        				"		QTD_SALARIO_MIN 	  , " +
        				"		QTD_SALARIO_MAX 	  , " +
        				"		DT_INI_SIT_SRV 		  , " +
        				"		COD_USUARIO)  		    " +
                        " VALUES (?, ?, ?, ?, ?, ?)     ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, classeHayVO.getIdClasseHay());
            setString 	(stmt, ordemCampos++, classeHayVO.getDescricao().trim());
            setDouble 	(stmt, ordemCampos++, classeHayVO.getSalarioMinimo());
            setDouble	(stmt, ordemCampos++, classeHayVO.getSalarioMaximo());
            setTimestamp(stmt, ordemCampos++, classeHayVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, classeHayVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir a classe hay", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }    
    

    /**
     * Inclui a classe hay
     * 
     * @param classeHayVO
     * @return
     */
    public void incluiClasseHayHistorico(ClasseHayVO classeHayVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_CLASSE_HAY_HIST " +
						"      (COD_CLAS_HAY          , " +
						"       SEQ_CLAS_HAY 		  , " +
						"       DESCR_CLA_HAY 		  , " +
						"		QTD_SALARIO_MIN 	  , " +
						"		QTD_SALARIO_MAX 	  , " +
						"		DT_INI_SIT_SRV 		  , " +
						"		COD_USUARIO)  		    " +
				        " VALUES (?, ?, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, classeHayVO.getIdClasseHay());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(classeHayVO.getIdClasseHay().intValue()));
            setString 	(stmt, ordemCampos++, classeHayVO.getDescricao().trim());
            setDouble 	(stmt, ordemCampos++, classeHayVO.getSalarioMinimo());
            setDouble	(stmt, ordemCampos++, classeHayVO.getSalarioMaximo());
            setTimestamp(stmt, ordemCampos++, classeHayVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, classeHayVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir a classe hay", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idClasseHay
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idClasseHay) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_CLAS_HAY)     " +
                        "  FROM SRV_CLASSE_HAY_HIST   " +
                        " WHERE COD_CLAS_HAY =?       ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idClasseHay);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da classe hay " + idClasseHay, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
}