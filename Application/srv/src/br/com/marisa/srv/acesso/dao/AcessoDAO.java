package br.com.marisa.srv.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.acesso.vo.FuncionalidadeVO;
import br.com.marisa.srv.acesso.vo.ModuloVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;


/**
 * Classe para tratar dos métodos de acesso a dados de usuários
 * 
 * @author Walter Fontes
 */
public class AcessoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(AcessoDAO.class);
    
    /**
     * Obtém o acesso de um perfil
     * 
     * @param idPerfil
     * @return
     */
    public Map<Integer, List<Integer>> obtemAcessoPerfil(int idPerfil) throws PersistenciaException {

        String query =  " SELECT " +
        				"	A.COD_FUNCIONALIDADE, " +
        				"	A.COD_TIPO_ACESSO " +
						" FROM " +
        				"	SRV_ACESSO A, " +
						"	SRV_FUNCIONALIDADE F, " +
        				"	SRV_PERFIL P " +
						" WHERE A.COD_FUNCIONALIDADE = F.COD_FUNCIONALIDADE " +
						" AND A.COD_PERFIL = P.COD_PERFIL " +
						" AND P.FLG_ATIVO = ? " +
						" AND F.FLG_ATIVO = ? " +
						" AND A.COD_PERFIL = ? ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<Integer, List<Integer>> acessoMap = new HashMap<Integer, List<Integer>>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setBoolean(stmt, ordemCampos++, true);
            setBoolean(stmt, ordemCampos++, true);
            setInteger(stmt, ordemCampos++, idPerfil);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
            	
            	Integer idFuncionalidade = getInteger(rs, "COD_FUNCIONALIDADE");
            	Integer idTipoAcesso     = getInteger(rs, "COD_TIPO_ACESSO");
            	
            	if (acessoMap.containsKey(idFuncionalidade)) {
            		List<Integer> tiposAcessos = (List<Integer>)acessoMap.get(idFuncionalidade);
            		tiposAcessos.add(idTipoAcesso);
            		acessoMap.put(idFuncionalidade, tiposAcessos);
            	} else {
            		List<Integer> tiposAcessos = new ArrayList<Integer>();
            		tiposAcessos.add(idTipoAcesso);
            		acessoMap.put(idFuncionalidade, tiposAcessos);
            	}
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter as funcionalidades do perfil " + idPerfil, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return acessoMap;
    }

	public void salvarAcessos(Map mapaAcessos, Integer idPerfil, Integer codUsuario) throws PersistenciaException {
		String query =  "INSERT INTO " +
        " SRV_ACESSO  (COD_FUNCIONALIDADE, COD_TIPO_ACESSO, COD_PERFIL, " +
        " DT_INCLUSAO, COD_USUARIO_INCLUSAO) VALUES(?,?,?,?,?) ";
        
//		COD_FUNCIONALIDADE, COD_TIPO_ACESSO, COD_PERFIL, SEQ_ALT_ACESSO
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			Iterator it = mapaAcessos.keySet().iterator();
			int ordemCampos = 1;
			Date dataInclusao = new Date();
			while (it.hasNext()) {
				Integer idFuncionalidade = (Integer)it.next();
				List listaAcessos = (List)mapaAcessos.get(idFuncionalidade);
				for(int i=0;i<listaAcessos.size();i++) {
					ordemCampos = 1;
					Integer acesso = (Integer)listaAcessos.get(i);
					setInteger(stmt, ordemCampos++, idFuncionalidade);
					setInteger(stmt, ordemCampos++, acesso);
					setInteger(stmt, ordemCampos++, idPerfil);
					setTimestamp(stmt, ordemCampos++, dataInclusao);
					setInteger(stmt, ordemCampos++, codUsuario);
					stmt.execute();
				}
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível salvar lista de acessos " , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}	
	}

	public void salvarHistorico(Map mapaAcessosRemovidos, Integer idPerfil, Integer codUsuario) throws PersistenciaException {
		String query =  "INSERT INTO " +
        "  SRV_ACESSO_HIST  (COD_FUNCIONALIDADE, COD_TIPO_ACESSO, COD_PERFIL, SEQ_ALT_ACESSO," +
        " DT_EXCLUSAO, COD_USUARIO_EXCLUSAO) VALUES(?,?,?,?,?,?) ";
        
//		COD_FUNCIONALIDADE, COD_TIPO_ACESSO, COD_PERFIL, SEQ_ALT_ACESSO
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			Iterator it = mapaAcessosRemovidos.keySet().iterator();
			int ordemCampos = 1;
			Date dataExclusao = new Date();
			while (it.hasNext()) {
				Integer idFuncionalidade = (Integer)it.next();
				List listaAcessos = (List)mapaAcessosRemovidos.get(idFuncionalidade);
				for(int i=0;i<listaAcessos.size();i++) {
					ordemCampos = 1;
					Integer acesso = (Integer)listaAcessos.get(i);
					setInteger(stmt, ordemCampos++, idFuncionalidade);
					setInteger(stmt, ordemCampos++, acesso);
					setInteger(stmt, ordemCampos++, idPerfil);
					setInteger(stmt, ordemCampos++, obtemSequenciaHistorico(idFuncionalidade,acesso,idPerfil));
					setTimestamp(stmt, ordemCampos++, dataExclusao);
					setInteger(stmt, ordemCampos++, codUsuario);
					stmt.execute();
				}
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível salvar lista de historico de acesso " , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
	}

	private int obtemSequenciaHistorico(Integer idFuncionalidade, Integer idAcesso, Integer idPerfil) throws PersistenciaException {
		
		String query =  "SELECT MAX(SEQ_ALT_ACESSO)  SEQ_ALT_ACESSO  " +
        " FROM SRV_ACESSO_HIST   " +
        " WHERE COD_FUNCIONALIDADE = ?  " +
        " AND COD_TIPO_ACESSO = ? " +
        " AND COD_PERFIL = ?   ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int proximaSequencia = 1;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int ordemCampos = 1;
			setInteger(stmt, ordemCampos++, idFuncionalidade);
			setInteger(stmt, ordemCampos++, idAcesso);
			setInteger(stmt, ordemCampos++, idPerfil);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				proximaSequencia = getInt(rs, "SEQ_ALT_ACESSO") + 1;
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico acesso " + idPerfil, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
			return proximaSequencia;
		}

	public void removeTodosAcessosPerfil(Integer idPerfil) throws PersistenciaException, SQLException {
		String query =  " DELETE " +
        "  FROM SRV_ACESSO WHERE COD_PERFIL = ? ";
        
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int ordemCampos = 1;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			setInteger(stmt, ordemCampos++, idPerfil);
			stmt.execute();
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível remover acessos do perfil "+idPerfil.intValue() , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
	}

    /**
     * Obtém o acesso de um perfil
     * 
     * @param idPerfil
     * @return
     */
    public List<ModuloVO> obtemAcessoPerfilModulos(int idPerfil) throws PersistenciaException {

        String query =  " SELECT DISTINCT M.COD_MODULO, M.DESCR_MODULO " +
        				"	FROM SRV_ACESSO A, " +
        				"		 SRV_FUNCIONALIDADE F, " +
        				"		 SRV_MODULO M " +
        				"	WHERE " +
        				"		 A.COD_PERFIL = ? " +
        				"	AND  F.COD_FUNCIONALIDADE = A.COD_FUNCIONALIDADE " +
        				"	AND M.COD_MODULO = F.COD_MODULO ORDER BY COD_MODULO ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ModuloVO> listaModulos = new ArrayList<ModuloVO>();

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idPerfil);
            rs = stmt.executeQuery();

            while (rs.next()) {
            	ModuloVO moduloVO = new ModuloVO();
            	moduloVO.setCodModulo(getInteger(rs, "COD_MODULO"));
            	moduloVO.setDescricao(getString(rs, "DESCR_MODULO"));
            	listaModulos.add(moduloVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter as funcionalidades do perfil " + idPerfil, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return listaModulos;
    }

    public List<FuncionalidadeVO> obtemAcessoPerfilFuncionalidades(int idPerfil, int codModulo) throws PersistenciaException {

        String query =  " 	SELECT " +
        				"		F.COD_MODULO, " +
        				"		F.COD_FUNCIONALIDADE, " +
        				"		F.DESCR_FUNCIONALIDADE, " +
        				"		F.URL_FUNCIONALIDADE " +
        				"	FROM " +
        				"		SRV_ACESSO A, " +
        				"		SRV_FUNCIONALIDADE F " +
        				"	WHERE " +
        				"		A.COD_PERFIL = ? " +
        				"	AND F.COD_MODULO = ? " +
        				"	AND F.COD_FUNCIONALIDADE = A.COD_FUNCIONALIDADE " +
        				"	GROUP BY F.COD_MODULO, F.COD_FUNCIONALIDADE, F.DESCR_FUNCIONALIDADE, F.URL_FUNCIONALIDADE" +
        				"	ORDER BY COD_FUNCIONALIDADE ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<FuncionalidadeVO> listaFuncionalidades = new ArrayList<FuncionalidadeVO>();

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idPerfil);
            setInteger(stmt, ordemCampos++, codModulo);
            rs = stmt.executeQuery();

            while (rs.next()) {
            	FuncionalidadeVO funcionalidadeVO = new FuncionalidadeVO();
            	funcionalidadeVO.setCodModulo(codModulo);
            	funcionalidadeVO.setCodFuncionalidade(getInteger(rs, "COD_FUNCIONALIDADE"));
            	funcionalidadeVO.setDescricao(getString(rs, "DESCR_FUNCIONALIDADE"));
            	funcionalidadeVO.setUrl(getString(rs, "URL_FUNCIONALIDADE"));
            	listaFuncionalidades.add(funcionalidadeVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter as funcionalidades do perfil " + idPerfil, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return listaFuncionalidades;
    }

    public boolean obtemAcessoPerfilFuncionalidadeTipoAcesso(int idPerfil, int codFuncionalidade, int codTipoAcesso) throws PersistenciaException {

        String query =  " SELECT * " +
		        		"   FROM SRV_ACESSO                     A, " +
		        		"        SRV_FUNCIONALIDADE_TIPO_ACESSO FTA, " +
		        		"        SRV_FUNCIONALIDADE             F " +
		        		"  WHERE FTA.COD_FUNCIONALIDADE = A.COD_FUNCIONALIDADE " +
		        		"    AND FTA.COD_TIPO_ACESSO = A.COD_TIPO_ACESSO " +
		        		"    AND A.COD_FUNCIONALIDADE = F.COD_FUNCIONALIDADE " +
		        		"    AND A.COD_FUNCIONALIDADE = ? " +
		        		"    AND A.COD_PERFIL = ? " +
		        		"    AND FTA.COD_TIPO_ACESSO = ? ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<FuncionalidadeVO> listaFuncionalidades = new ArrayList<FuncionalidadeVO>();
        boolean result  =false;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, codFuncionalidade);
            setInteger(stmt, ordemCampos++, idPerfil);
            setInteger(stmt, ordemCampos++, codTipoAcesso);
            rs = stmt.executeQuery();

            while (rs.next()) {
            	result = true;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter as funcionalidades do perfil/funcionalidade/tipo acesso ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return result;
    }

}