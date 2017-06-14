package br.com.marisa.srv.filial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.CNPJCPFHelper;


/**
 * Classe para tratar dos métodos de acesso a dados às calendário 
 * 
 * @author Walter Fontes
 */
public class FilialDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(FilialDAO.class);
    

    /**
     * Obtem a lista de filiais
     * 
     * @param codFilial 
     * @param codEmpresa 
     * @param isAtivo 
     * @return
     * @throws PersistenciaException
     */
	public List obtemListaFiliais(int codEmpresa, int codFilial, Boolean isAtivo) throws PersistenciaException {
	
		StringBuffer query = new StringBuffer(
				"SELECT FIL.COD_EMP, " +
				"       FIL.COD_FIL, " +
				"       FIL.COD_TIPO_FIL, " +
				"       FIL.DESCR_FIL, " +
				"       FIL.NUM_CNPJ, " +
				"       FIL.COD_UF, " +
				"       FIL.FLG_ATIV, " +
				"       FIL.FLG_META_100_PCT_REALZ, " +
				"       FIL.DT_INI_SIT_SRV, " +
				"       FIL.COD_USUARIO, " +
				"       FIL.DT_INAUG_FILIAL, " +
				"       TPFIL.DESCR_TIPO_FIL " +
				" FROM  SRV_FILIAL FIL, " +
				"       SRV_TIPO_FILIAL TPFIL " +
				" WHERE FIL.COD_EMP = ? " +
				"   AND TPFIL.COD_TIPO_FIL = FIL.COD_TIPO_FIL ");

		if(codFilial != -1){
			query.append(" AND FIL.COD_FIL = ? ");
		}
		if(isAtivo != null){
			query.append(" AND FIL.FLG_ATIV = ? ");
		}
		query.append(" ORDER BY FIL.COD_FIL");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, codEmpresa != -1 ? codEmpresa : 1);
			if(codFilial != -1){
				setInteger(stmt, i++, codFilial);
			}
			if(isAtivo != null){
				setBoolean(stmt, i++, isAtivo);
			}
			FilialVO filialVO;
			for(rs = stmt.executeQuery(); rs.next(); lista.add(filialVO)){
				filialVO = new FilialVO();
				filialVO.setCodEmpresa(getInteger(rs, "COD_EMP"));
				filialVO.setCodFilial(getInteger(rs, "COD_FIL"));
				filialVO.setCodTpFilial(getInteger(rs, "COD_TIPO_FIL"));
				filialVO.setDescricao(getString(rs, "DESCR_FIL"));
				filialVO.setCnpj(getString(rs, "NUM_CNPJ"));
				filialVO.setUf(getString(rs, "COD_UF"));
				filialVO.setFlagAtivo(getBoolean(rs, "FLG_ATIV"));
				filialVO.setFlagMeta100(getBoolean(rs, "FLG_META_100_PCT_REALZ"));
				filialVO.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
				filialVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				filialVO.setDescricaoTpFil(getString(rs, "DESCR_TIPO_FIL"));
				filialVO.setDataInauguracao(getDate(rs, "DT_INAUG_FILIAL"));
			}
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de filiais ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	
	public FilialVO obtemFilial(Integer idEmpresa, Integer idFilial) throws PersistenciaException{
		return obtemFilial(idEmpresa, idFilial, null);
	}
	
	/**
     * Obtem filial
     * 
     * @param idEmpresa 
     * @param idFilial 
     * @return
     * @throws PersistenciaException
     */
	public FilialVO obtemFilial(Integer idEmpresa, Integer idFilial, Boolean isAtivo) throws PersistenciaException {
		
		 StringBuffer query =  new StringBuffer(
				"SELECT COD_EMP, COD_FIL, DESCR_FIL, NUM_CNPJ, COD_UF, FLG_ATIV, " +
				"       FLG_META_100_PCT_REALZ, DT_INI_SIT_SRV, COD_USUARIO, DT_INAUG_FILIAL, COD_TIPO_FIL " +
				"  FROM SRV_FILIAL    " +
		 		" WHERE COD_EMP  = ?  " +
				"   AND COD_FIL  = ?  ");
		 if(isAtivo != null){
				query.append(" AND FLG_ATIV = ? ");
		}
		 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FilialVO filialVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setInteger(stmt, i++, idEmpresa);
			setInteger(stmt, i++, idFilial);
			if(isAtivo != null){
				setBoolean(stmt, i++, isAtivo);
			}
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				filialVO = new FilialVO();
				filialVO.setCodEmpresa(getInteger(rs, "COD_EMP"));
				filialVO.setCodFilial(getInteger(rs, "COD_FIL"));
				filialVO.setDescricao(getString(rs, "DESCR_FIL"));
				filialVO.setCnpj(getString(rs, "NUM_CNPJ"));
				filialVO.setUf(getString(rs, "COD_UF"));
				filialVO.setFlagAtivo(getBoolean(rs, "FLG_ATIV"));
				filialVO.setFlagMeta100(getBoolean(rs, "FLG_META_100_PCT_REALZ"));
				filialVO.setCodTpFilial(getInteger(rs, "COD_TIPO_FIL"));
				filialVO.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
				filialVO.setDataInauguracao(getDate(rs, "DT_INAUG_FILIAL"));
				filialVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a filial " + idFilial, e);
		} finally {
			closeStatementAndResultSet(stmt, rs, conn);
		}
		
		return filialVO;
	}	

	
	/**
	 * Altera as informações da filial
	 * 
	 * @param filialVO
	 * @throws PersistenciaException
	 */
	public void alterarFilial(FilialVO filialVO) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				" UPDATE SRV_FILIAL      			 " +
				"	 SET DESCR_FIL 				= ?, " +
		 		" 		 NUM_CNPJ 				= ?, " +
		 		" 		 FLG_ATIV 				= ?, " +
		 		" 		 FLG_META_100_PCT_REALZ = ?, " +
		 		"        DT_INI_SIT_SRV         = ?, " +
		 		"        DT_INAUG_FILIAL        = ?, " +
		 		"        COD_USUARIO            = ?  " +
		 		"  WHERE COD_EMP 				= ?  " +
		 		"    AND COD_FIL 				= ?  " );

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setString	(stmt, i++, filialVO.getDescricao());
			setString	(stmt, i++, String.valueOf( CNPJCPFHelper.retiraCaracteresAlpha(filialVO.getCnpj())) );
			setBoolean	(stmt, i++, filialVO.getFlagAtivo());
			setBoolean	(stmt, i++, filialVO.getFlagMeta100());
			setDate		(stmt, i++, filialVO.getDataUltimaAlteracao());
			setDate		(stmt, i++, filialVO.getDataInauguracao());
			setInteger	(stmt, i++, filialVO.getIdUsuarioUltimaAlteracao());
			setInteger	(stmt, i++, filialVO.getCodEmpresa());
			setInteger	(stmt, i++, filialVO.getCodFilial());
			stmt.execute();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter salvar a alteração da filial", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}   
	
	
	/**
	 * Inclui histórico da filial
	 * 
	 * @param filialVO
	 * @throws PersistenciaException
	 */
	public void incluiFilialHistorico(FilialVO filialVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_FILIAL_HIST  (     			 									" +
				"	 		  COD_EMP, COD_FIL, FIL_SEQ, DESCR_FIL, NUM_CNPJ, COD_UF, 			" +
				"             FLG_ATIV, FLG_META_100_PCT_REALZ, DT_INI_SIT_SRV, COD_USUARIO)	" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)											");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setInteger	(stmt,i++, filialVO.getCodEmpresa());
			setInteger	(stmt,i++, filialVO.getCodFilial());
			setInteger	(stmt,i++, obtemProximaSequenciaHistorio(filialVO.getCodEmpresa(), filialVO.getCodFilial()));
			setString	(stmt,i++, filialVO.getDescricao());
			setString	(stmt,i++, filialVO.getCnpj());
			setString	(stmt,i++, filialVO.getUf());
			setBoolean	(stmt,i++, filialVO.getFlagAtivo());
			setBoolean	(stmt,i++, filialVO.getFlagMeta100());
			setDate		(stmt,i++, filialVO.getDataUltimaAlteracao());
			setInteger	(stmt,i++, filialVO.getIdUsuarioUltimaAlteracao());
			stmt.execute();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir o historico da filial", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}
	
	
	 /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idEmpresa
     * @param idFilial
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer idEmpresa, Integer idFilial) throws PersistenciaException {

        String query =  " SELECT MAX(FIL_SEQ)     " +
                        "   FROM SRV_FILIAL_HIST  " +
						"  WHERE COD_EMP   = ?    " +
						"    AND COD_FIL   = ?    ";            
						
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
						
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            preencheParametros(stmt, parametros);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da filial", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }	

    public List pesquisarListaTipoFiliais(Integer codTipoFil) throws PersistenciaException {
		List listaTipoFil = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query =  "SELECT FIL.COD_EMP, " +
	            		"       FIL.COD_FIL, " +
	            		"       FIL.DESCR_FIL, " +
	            		"       FIL.NUM_CNPJ, " +
	            		"       FIL.COD_UF, " +
	            		"       FIL.FLG_ATIV, " +
	            		"       FIL.FLG_META_100_PCT_REALZ, " +
	            		"       FIL.DT_INI_SIT_SRV, " +
	            		"       FIL.COD_USUARIO, " +
	            		"       FIL.DT_INAUG_FILIAL, " +
	            		"       TPFIL.COD_TIPO_FIL, " +
	            		"       TPFIL.DESCR_TIPO_FIL FROM SRV_FILIAL FIL, " +
	            		"       SRV_TIPO_FILIAL TPFIL " +
	            		" WHERE FIL.COD_TIPO_FIL = ? " +
	            		"   AND TPFIL.COD_TIPO_FIL = ? ";
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			setInteger(stmt, 1, codTipoFil);
			setInteger(stmt, 2, codTipoFil);

			FilialVO filialVO;
			for(rs = stmt.executeQuery(); rs.next(); listaTipoFil.add(filialVO)){
				filialVO = new FilialVO();
				filialVO.setCodEmpresa(getInteger(rs, "COD_EMP"));
				filialVO.setCodFilial(getInteger(rs, "COD_FIL"));
				filialVO.setDescricao(getString(rs, "DESCR_FIL"));
				filialVO.setCnpj(getString(rs, "NUM_CNPJ"));
				filialVO.setUf(getString(rs, "COD_UF"));
				filialVO.setFlagAtivo(getBoolean(rs, "FLG_ATIV"));
				filialVO.setFlagMeta100(getBoolean(rs, "FLG_META_100_PCT_REALZ"));
				filialVO.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
				filialVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				filialVO.setDataInauguracao(getDate(rs, "DT_INAUG_FILIAL"));
				filialVO.setCodTpFilial(getInteger(rs, "COD_TIPO_FIL"));
				filialVO.setDescricaoTpFil(getString(rs, "DESCR_TIPO_FIL"));
			}
		
		}catch(Exception e){
			throw new PersistenciaException(log, "Não foi possível obter a lista de Tipo Filiais", e);
		}finally{
			closeStatementAndResultSet(stmt, rs);
		}

		return listaTipoFil;
	}

	public List obterListaTipoFiliais() throws PersistenciaException {

		List listaTipoFil;
		listaTipoFil = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT COD_TIPO_FIL, DESCR_TIPO_FIL FROM SRV_TIPO_FILIAL";

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while( rs.next() ){
				FilialVO filial = new FilialVO();
				filial.setCodTpFilial(getInteger(rs, "COD_TIPO_FIL"));
				filial.setDescricao(getString(rs, "DESCR_TIPO_FIL"));
				listaTipoFil.add(filial);
			}
			
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de Tipo Filiais", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return listaTipoFil;
	}

	public void incluirFilial(FilialVO filial) throws PersistenciaException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = 
				"INSERT INTO SRV_FILIAL " +
						"  (COD_EMP, " +
						"   COD_FIL, " +
						"   DESCR_FIL, " +
						"   NUM_CNPJ, " +
						"   COD_UF, " +
						"   FLG_ATIV, " +
						"   DT_INI_SIT_SRV, " +
						"   COD_USUARIO, " +
						"   DT_INAUG_FILIAL, " +
						"   FLG_META_100_PCT_REALZ, " +
						"   COD_TIPO_FIL) " +
						"VALUES " +
						"  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 1;
			setInteger(stmt, i++, filial.getCodEmpresa());
			setInteger(stmt, i++, filial.getCodFilial());
			setString(stmt, i++, filial.getDescricao());
			setString(stmt, i++, String.valueOf(CNPJCPFHelper.retiraCaracteresAlpha(filial.getCnpj())));
			setString(stmt, i++, filial.getUf());
			setBoolean(stmt, i++, filial.getFlagAtivoStr());
			setTimestamp(stmt, i++, filial.getDataUltimaAlteracao());
			setInteger(stmt, i++, filial.getIdUsuarioUltimaAlteracao());
			setDate(stmt, i++, filial.getDataInauguracao());
			setBoolean(stmt, i++, filial.getFlagMeta100Formatada());
			setInteger(stmt, i++, filial.getCodTpFilial());
			stmt.execute();
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir filial", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @return
	 * @throws PersistenciaException
	 * @Deprecated use obtemTodasFiliais()
	 */
	@Deprecated
    public List obterTodasFiliais() throws PersistenciaException {
        List listaTipoFil;
        listaTipoFil = new ArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM SRV_FILIAL ORDER BY DESCR_FIL";
        try{
            conn = getConn();
            stmt = conn.prepareStatement(query);
            FilialVO filial;
            for(rs = stmt.executeQuery(); rs.next(); listaTipoFil.add(filial)){
                filial = new FilialVO();
                filial.setCodFilial(getInteger(rs, "COD_FIL"));
                filial.setDescricao(getString(rs, "DESCR_FIL"));
            }

        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível obter a lista de Tipo Filiais", e);
        }finally{
            closeStatementAndResultSet(stmt, rs);
        }
        return listaTipoFil;
    }

	/**
	 * caso seja necessário mais campos, é só acrescentar
	 * @return
	 * @throws PersistenciaException
	 */
	 public List<FilialVO> obtemTodasFiliais() throws PersistenciaException {
		 return obtemTodasFiliais(null);
	 }
		 
	 public List<FilialVO> obtemTodasFiliais(Boolean isAtivo) throws PersistenciaException {
        List<FilialVO>  listaTipoFil = new ArrayList<FilialVO> ();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT COD_FIL, DESCR_FIL, COD_TIPO_FIL FROM SRV_FILIAL ";
        if(isAtivo != null){
			query+=" where FLG_ATIV = ? ";
		}
		query+=" ORDER BY COD_FIL ";
        try{
            conn = getConn();
            stmt = conn.prepareStatement(query);
            if(isAtivo != null){
            	setBoolean(stmt, 1, isAtivo);
            }
            rs = stmt.executeQuery();
            FilialVO filial;
            while(rs.next()){
                filial = new FilialVO();
                filial.setCodFilial(getInteger(rs, "COD_FIL"));
                filial.setDescricao(getString(rs, "DESCR_FIL"));
                filial.setCodTpFilial(getInteger(rs, "COD_TIPO_FIL"));
                listaTipoFil.add(filial);
            }

        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível obter a lista que contem todas as Filiais", e);
        }finally{
            closeStatementAndResultSet(stmt, rs,conn);
        }
        return listaTipoFil;
	 }

}