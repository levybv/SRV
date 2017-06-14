package br.com.marisa.srv.filial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class TipoFilialDAO extends BasicDAO
{

    private static final Logger log = Logger.getLogger(br.com.marisa.srv.filial.dao.TipoFilialDAO.class);

    public TipoFilialDAO()
    {
    	super();
    }

    public void incluirTipoFilial(FilialVO filialVO)
        throws PersistenciaException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "INSERT INTO SRV_TIPO_FILIAL (COD_TIPO_FIL, DESCR_TIPO_FIL, DT_INI_SIT_SRV, COD_USUARIO) VALUES (?,?,?,?)";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, obtemProximaSequencia());
            setString(stmt, 2, filialVO.getDescricao());
            setTimestamp(stmt, 3, filialVO.getDataUltimaAlteracao());
            setInteger(stmt, 4, filialVO.getIdUsuarioUltimaAlteracao());
            stmt.executeQuery();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível incluir tipo filial.", e);
        }
        finally
        {
            closeStatement(stmt);
        }
        return;
    }

    public void incluirHistoricoTipoFilial(FilialVO filialVO)
        throws PersistenciaException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "INSERT INTO SRV_TIPO_FILIAL_HIST (COD_TIPO_FIL, SEQ_TIPO_FIL, DESCR_TIPO_FIL, DT_INI_SIT_SRV, COD_USUARIO) VALUES (?,?,?,?,?)";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, filialVO.getCodFilial());
            setInteger(stmt, 2, obtemProximaSequenciaHistorico(filialVO));
            setString(stmt, 3, filialVO.getDescricao());
            setDate(stmt, 4, filialVO.getDataUltimaAlteracao());
            setInteger(stmt, 5, filialVO.getIdUsuarioUltimaAlteracao());
            stmt.executeQuery();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível incluir tipo filial na tabela histórico.", e);
        }
        finally
        {
            closeStatement(stmt);
        }
        return;
    }

    public void alterarTipoFilial(FilialVO filialVO)
        throws PersistenciaException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = " UPDATE SRV_TIPO_FILIAL SET DESCR_TIPO_FIL = ?, DT_INI_SIT_SRV = ? WHERE COD_TIPO_FIL = ? AND COD_USUARIO = ? ";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setString(stmt, 1, filialVO.getDescricao());
            setTimestamp(stmt, 2, filialVO.getDataUltimaAlteracao());
            setInteger(stmt, 3, filialVO.getCodFilial());
            setInteger(stmt, 4, filialVO.getIdUsuarioUltimaAlteracao());
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível alterar tipo filial.", e);
        }
        finally
        {
            closeStatementAndResultSet(stmt, rs);
        }
        return;
    }

    public void excluirTipoFilial(FilialVO filial)
        throws PersistenciaException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = " DELETE FROM SRV_TIPO_FILIAL WHERE COD_TIPO_FIL = ? AND DESCR_TIPO_FIL = ? AND COD_USUARIO = ?";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, filial.getCodFilial());
            setString(stmt, 2, filial.getDescricao());
            setInteger(stmt, 3, filial.getIdUsuarioUltimaAlteracao());
            stmt.executeQuery();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível excluir tipo filial.", e);
        }
        finally
        {
            closeStatement(stmt);
        }
        return;
    }

    public List montaTelaTipoFilial(String descrFilial)
        throws PersistenciaException
    {
        List listaTipoFilial;
        listaTipoFilial = new ArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer query = new StringBuffer(" SELECT * FROM SRV_TIPO_FILIAL ");
        if(descrFilial != null)
        {
            query.append(" WHERE UPPER(DESCR_TIPO_FIL) LIKE UPPER ('%'||?||'%') ");
        }
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query.toString());
            if(descrFilial != null)
            {
                setString(stmt, 1, descrFilial);
            }
            FilialVO filial;
            for(rs = stmt.executeQuery(); rs.next(); listaTipoFilial.add(filial))
            {
                filial = new FilialVO();
                filial.setCodFilial(getInteger(rs, "COD_TIPO_FIL"));
                filial.setDescricao(getString(rs, "DESCR_TIPO_FIL"));
            }

        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível pesquisar tipo filial.", e);
        }
        finally
        {
            closeStatementAndResultSet(stmt, rs);
        }
        return listaTipoFilial;
    }

    public FilialVO obterUltimaFilial(FilialVO filialVO)
        throws PersistenciaException
    {
        FilialVO filial;
        filial = new FilialVO();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = " SELECT * FROM SRV_TIPO_FILIAL WHERE COD_TIPO_FIL = ? AND DESCR_TIPO_FIL = ? ";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, filialVO.getCodFilial());
            setString(stmt, 2, filialVO.getDescricao());
            rs = stmt.executeQuery();
            if(rs.next())
            {
                filial.setCodFilial(getInteger(rs, "COD_TIPO_FIL"));
                filial.setDescricao(getString(rs, "DESCR_TIPO_FIL"));
                filial.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
                filial.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
            }
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível pesquisar tipo filial.", e);
        }
        finally
        {
            closeStatementAndResultSet(stmt, rs);
        }
        return filial;
    }

    public Integer obtemProximaSequencia()
        throws PersistenciaException
    {
        Integer seq;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        seq = null;
        String query = "SELECT SEQ_SRV_TIPO_FILIAL.NEXTVAL FROM DUAL";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            if(rs.next())
            {
                seq = getInteger(rs, "NEXTVAL");
            }
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível obter a próxima sequencia.", e);
        }
        finally
        {
            closeStatementAndResultSet(stmt, rs);
        }
        return seq;
    }

    public int obtemProximaSequenciaHistorico(FilialVO filialVO)
        throws PersistenciaException
    {
        int seq;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        seq = 0;
        String query = " SELECT MAX(SEQ_TIPO_FIL) FROM SRV_TIPO_FILIAL_HIST WHERE COD_TIPO_FIL = ? ";
        try
        {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, filialVO.getCodFilial());
            rs = stmt.executeQuery();
            if(rs.next())
            {
                seq = getInt(rs, 1) + 1;
            }
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível obter a próxima sequencia.", e);
        }
        finally
        {
            closeStatementAndResultSet(stmt, rs);
        }
        return seq;
    }

}
