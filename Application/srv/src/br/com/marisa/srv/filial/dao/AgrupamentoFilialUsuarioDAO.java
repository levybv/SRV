package br.com.marisa.srv.filial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

public class AgrupamentoFilialUsuarioDAO extends BasicDAO{

    private static final Logger log = Logger.getLogger(AgrupamentoFilialUsuarioDAO.class);
    
    
	public List<FilialVO> obtemListaFiliaisUtilizadasByIdGrupo(int idGrupo, Integer idFuncionario) throws PersistenciaException {
		
		List<FilialVO>  listaTipoFil = new ArrayList<FilialVO> ();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT  cod_fil  from SRV_AGRUPAMENTO_FILIAL_USUARIO"
        		+ " where COD_GRUPO_CARGO = ?";
        
        List<ParametroSQL> listaParametro = new ArrayList<ParametroSQL>();
        listaParametro.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupo));
        
        if(idFuncionario !=null){
        	query += " and cod_func = ? ";
        	listaParametro.add(new ParametroSQL(PARAMTYPE_INTEGER, idFuncionario));
        }
        try{
            conn = getConn();
            stmt = conn.prepareStatement(query);
            preencheParametros(stmt, listaParametro);
            rs = stmt.executeQuery();
            FilialVO filial;
            while(rs.next()){
                filial = new FilialVO();
                filial.setCodFilial(getInteger(rs, "COD_FIL"));
                listaTipoFil.add(filial);
            }

        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível obter a de filiais usadas pelo grupo "+idGrupo, e);
        }finally{
            closeStatementAndResultSet(stmt, rs,conn);
        }
        return listaTipoFil;
	}


	public void incluiAgrupamentoFilial(int idFilial, int idFuncionario, int idGrupo, UsuarioVO usuarioVO) throws PersistenciaException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "insert into SRV_AGRUPAMENTO_FILIAL_USUARIO (COD_FUNC,COD_FIL,COD_GRUPO_CARGO, COD_USUARIO,DT_OCORRENCIA ) values (?,?,?,?,sysdate)";
        try{
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int columnIndex = 1;
            setInteger(stmt, columnIndex++, idFuncionario);
            setInteger(stmt, columnIndex++, idFilial);
            setInteger(stmt, columnIndex++, idGrupo);
            setInteger(stmt, columnIndex++, usuarioVO.getIdUsuario());
            stmt.executeUpdate();
            

        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível atribuir a filial | usuario | grupo|"+idFilial+"|-|"+idFuncionario+"|-|"+idGrupo, e);
        }finally{
            closeStatementAndResultSet(stmt, rs,conn);
        }
	}


	public void removeTodosAgrupamentos(int idFuncionario, int idGrupo) throws PersistenciaException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "delete from SRV_AGRUPAMENTO_FILIAL_USUARIO where COD_FUNC = ? and  COD_GRUPO_CARGO = ?";
        try{
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int columnIndex = 1;
            setInteger(stmt, columnIndex++, idFuncionario);
            setInteger(stmt, columnIndex++, idGrupo);
            stmt.executeUpdate();
            

        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível remover atribuicao de de funcionario  e grupo de lojas |"+idFuncionario+"|-|"+idGrupo, e);
        }finally{
            closeStatementAndResultSet(stmt, rs,conn);
        }
	}


	public List<Integer> obtemListaVinculadasColaborador(int idFuncionario, int idGrupo) throws PersistenciaException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "select cod_fil from SRV_AGRUPAMENTO_FILIAL_USUARIO where COD_FUNC = ? and  COD_GRUPO_CARGO = ?";
        List<Integer> lista = new ArrayList<Integer>();
        try{
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int columnIndex = 1;
            setInteger(stmt, columnIndex++, idFuncionario);
            setInteger(stmt, columnIndex++, idGrupo);
            rs = stmt.executeQuery();
            while(rs.next()){
            	lista.add(getInt(rs, "cod_fil"));
            }
            Collections.sort(lista);
            return lista;
        }catch(Exception e){
            throw new PersistenciaException(log, "Não foi possível localizar todas as lojas vinculadas ao funcionario |"+idFuncionario+"|-|"+idGrupo, e);
        }finally{
            closeStatementAndResultSet(stmt, rs,conn);
        }
	}

}
