package br.com.marisa.srv.meta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import br.com.marisa.srv.filtro.vo.FiltroMetaLiderVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.helper.LoggerHelper;
import br.com.marisa.srv.meta.vo.MetaLiderVO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

public class MetaLiderDAO extends BasicDAO{
	private static java.util.logging.Logger log = LoggerHelper.getInstance().getLogger(MetaLiderDAO.class.getName());

	public List<MetaLiderVO> obtemListaMetasLider(FiltroMetaLiderVO filtroMetaLiderVO) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				" select num_ano*100+num_mes anoMesSort,f.nome_func , f.cod_func,  ML.num_ano, ML.num_mes, ML.equipe, ML.meta, ml.cod_indic, i.descr_indic "
				+ " from srv_funcionario f, srv_indicador i, SRV_CCENTER_META_LIDER ML where f.cod_func = ml.cod_func    "
				+ " and i.cod_indic =  ml.cod_indic ");
				
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
	    if (filtroMetaLiderVO.getMes() != null) {
	    	query.append(" AND ml.num_mes = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, filtroMetaLiderVO.getMes())); 
	    }
	    if (filtroMetaLiderVO.getAno() != null) {
	    	query.append(" AND ml.num_ano = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, filtroMetaLiderVO.getAno())); 
	    }
	    if (filtroMetaLiderVO.getCodFuncionario() != null) {
	    	query.append(" AND f.cod_func = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, filtroMetaLiderVO.getCodFuncionario())); 
	    }
	    if (filtroMetaLiderVO.getEquipe() != null) {
	    	query.append(" AND ml.equipe = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, filtroMetaLiderVO.getEquipe())); 
	    }
	    if (filtroMetaLiderVO.getCodIndicador() != null) {
	    	query.append(" AND ml.cod_indic = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, filtroMetaLiderVO.getCodIndicador() )); 
	    }
	   
	    query.append(" ORDER BY anoMesSort desc, f.nome_func, equipe ");	
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<MetaLiderVO> metasFiliais = new ArrayList<MetaLiderVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				MetaLiderVO metaLiderVO = new MetaLiderVO();
				metaLiderVO.setCodFuncionario(getInteger(rs, "cod_func"));
				metaLiderVO.setCodIndicador(getInteger(rs, "cod_indic"));
				metaLiderVO.setAno(getInteger(rs, "NUM_ANO"));
				metaLiderVO.setMes(getInteger(rs, "NUM_MES"));
				metaLiderVO.setMeta(getDouble(rs, "meta"));
				metaLiderVO.setEquipe(getString(rs, "equipe"));
				metaLiderVO.setNomeFuncionario(getString(rs, "nome_func"));
				metaLiderVO.setDescricaoIndicador(getString(rs, "descr_indic"));
				metasFiliais.add(metaLiderVO);
			}
		
		} catch (Exception e) {
            throw new PersistenciaException( "Não foi possível obter a lista de metas lideres"  , e,log,Level.SEVERE,this.getClass(),"List<MetaLiderVO> obtemListaMetasLider(FiltroMetaLiderVO filtroMetaLiderVO)  " +query.toString());
		} finally {
			closeStatementAndResultSet(stmt, rs,conn);
		}
		return metasFiliais;
	}

	/**
	 * 
	 * @param metaLiderVO
	 * @param usuarioVO
	 * @throws PersistenciaException
	 */
	public void incluiMetaLider(MetaLiderVO metaLiderVO, UsuarioVO usuarioVO) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				" insert into SRV_CCENTER_META_LIDER (COD_FUNC,COD_INDIC,NUM_ANO,NUM_MES,EQUIPE,META,COD_USUARIO,DT_OCORRENCIA) values"
				+ " (?,?,?,?,?,?,?,sysdate) ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			int columnIndex = 1;
			stmt = conn.prepareStatement(query.toString());
			setInteger(stmt, columnIndex++, metaLiderVO.getCodFuncionario());
			setInteger(stmt, columnIndex++, metaLiderVO.getCodIndicador());
			setInteger(stmt, columnIndex++, metaLiderVO.getAno());
			setInteger(stmt, columnIndex++, metaLiderVO.getMes());
			setString(stmt, columnIndex++, metaLiderVO.getEquipe());
			setDouble(stmt, columnIndex++, metaLiderVO.getMeta());
			setInteger(stmt, columnIndex++, usuarioVO.getIdUsuario());
			stmt.executeUpdate();
			
		
		} catch (Exception e) {
            throw new PersistenciaException( "Não foi possível inserir meta para o lider " +metaLiderVO.getCodFuncionario() , e,log,Level.SEVERE,this.getClass(),"incluiMetaLider(MetaLiderVO metaLiderVO, UsuarioVO usuarioVO) throws PersistenciaException " +query.toString());
		} finally {
			closeStatementAndResultSet(stmt, rs,conn);
		}
		
	}

	public List<String> validaMetaLider(MetaLiderVO metaLiderVO) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				" select EQUIPE  from SRV_CCENTER_META_LIDER "
				+ " where cod_func = ?"
				+ " and num_ano = ?"
				+ " and num_mes = ? ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			int columnIndex = 1;
			List<String> lista = new ArrayList<String>();
			stmt = conn.prepareStatement(query.toString());
			setInteger(stmt, columnIndex++, metaLiderVO.getCodFuncionario());
			setInteger(stmt, columnIndex++, metaLiderVO.getAno());
			setInteger(stmt, columnIndex++, metaLiderVO.getMes());
			rs = stmt.executeQuery();
			while(rs.next()){
				lista.add(getString(rs, "equipe"));
			}
			return lista;
		} catch (Exception e) {
            throw new PersistenciaException( "Não foi obter as equipes para o lider parametrizado nesta data " +metaLiderVO.getCodFuncionario()+"-"+metaLiderVO.getAno()+"-"+metaLiderVO.getMes() , e,log,Level.SEVERE,this.getClass(),"List<String> validaMetaLider(MetaLiderVO metaLiderVO) throws PersistenciaException" +query.toString());
		} finally {
			closeStatementAndResultSet(stmt, rs,conn);
		}
	}

	public Boolean validaDuplicidade(MetaLiderVO metaLiderVO) throws PersistenciaException {
			StringBuffer query =  new StringBuffer(
					" select EQUIPE  from SRV_CCENTER_META_LIDER "
					+ " where cod_func = ?"
					+ " and num_ano = ?"
					+ " and num_mes = ? "
					+ " and cod_indic = ?"
					+ " and equipe = ? ");
					
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				conn = getConn();
				int columnIndex = 1;
				stmt = conn.prepareStatement(query.toString());
				setInteger(stmt, columnIndex++, metaLiderVO.getCodFuncionario());
				setInteger(stmt, columnIndex++, metaLiderVO.getAno());
				setInteger(stmt, columnIndex++, metaLiderVO.getMes());
				setInteger(stmt, columnIndex++, metaLiderVO.getCodIndicador());
				setString(stmt, columnIndex++, metaLiderVO.getEquipe());
				rs = stmt.executeQuery();
				return !rs.next();
			} catch (Exception e) {
	            throw new PersistenciaException( "Não foi obter as equipes para o lider parametrizado nesta data " +metaLiderVO.getCodFuncionario()+"-"+metaLiderVO.getAno()+"-"+metaLiderVO.getMes() , e,log,Level.SEVERE,this.getClass(),"Boolean validaDuplicidade(MetaLiderVO metaLiderVO) throws PersistenciaException  " +query.toString());
			} finally {
				closeStatementAndResultSet(stmt, rs,conn);
			}
	}

	public void removeMetaLider(MetaLiderVO metaLiderVO) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				 "delete from SRV_CCENTER_META_LIDER where COD_FUNC = ?"
				+ "and COD_INDIC = ? "
				+ " and NUM_ANO = ? "
				+ " and NUM_MES = ? ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConn();
			int columnIndex = 1;
			stmt = conn.prepareStatement(query.toString());
			setInteger(stmt, columnIndex++, metaLiderVO.getCodFuncionario());
			setInteger(stmt, columnIndex++, metaLiderVO.getCodIndicador());
			setInteger(stmt, columnIndex++, metaLiderVO.getAno());
			setInteger(stmt, columnIndex++, metaLiderVO.getMes());
			stmt.executeUpdate();
			
		
		} catch (Exception e) {
            throw new PersistenciaException( "Não foi possível apagar meta de lider - " +metaLiderVO.getCodFuncionario() , e,log,Level.SEVERE,this.getClass(),"removeMetaLider(MetaLiderVO metaLiderVO) throws PersistenciaException" +query.toString());
		} finally {
			closeStatementAndResultSet(stmt, rs,conn);
		}
		
	}

}
