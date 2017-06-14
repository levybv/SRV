package br.com.marisa.srv.filial.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.dao.TipoFilialDAO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;

public class TipoFilialBusiness
{

    private static final Logger log = Logger.getLogger(br.com.marisa.srv.filial.business.TipoFilialBusiness.class);
    private static TipoFilialBusiness instance = new TipoFilialBusiness();

    public static final TipoFilialBusiness getInstance(){
        return instance;
    }

    private TipoFilialBusiness() {
    	super();
    }

    public void incluirTipoFilial(FilialVO filialVO)
        throws PersistenciaException
    {
        TipoFilialDAO dao = new TipoFilialDAO();
        try
        {
            dao.incluirTipoFilial(filialVO);
            dao.commitTrans();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível incluir tipo filial.", e);
        }
        finally
        {
            dao.closeConnection();
        }
        return;
    }

    public void incluirTipoFilialHistorico(FilialVO filialVO)
        throws PersistenciaException
    {
        TipoFilialDAO dao = new TipoFilialDAO();
        try
        {
            dao.incluirHistoricoTipoFilial(filialVO);
            dao.commitTrans();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível incluir tipo filial no histórico.", e);
        }
        finally
        {
            dao.closeConnection();
        }
        return;
    }

    public void alterarTipoFilial(FilialVO filialNova)
        throws SRVException
    {
        TipoFilialDAO dao = new TipoFilialDAO();
        try
        {
            dao.alterarTipoFilial(filialNova);
            dao.commitTrans();
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível alterar tipo filial.", e);
        }
        finally
        {
            dao.closeConnection();
        }
        return;
    }

    public void excluirTipoFilial(FilialVO filial)
        throws SRVException
    {
        TipoFilialDAO dao = new TipoFilialDAO();
        try
        {
            dao.excluirTipoFilial(filial);
            dao.commitTrans();
        }
        catch(Exception e)
        {
            throw new SRVException(log, "Não foi possível excluir tipo filial.", e);
        }
        finally
        {
            dao.closeConnection();
        }
        return;
    }

    public List montaTelaTipoFilial(String descrFilial)
        throws SRVException
    {
        List listaTipoFiliais;
        listaTipoFiliais = new ArrayList();
        TipoFilialDAO dao = new TipoFilialDAO();
        try
        {
            listaTipoFiliais = dao.montaTelaTipoFilial(descrFilial);
        }
        catch(Exception e)
        {
            throw new SRVException(log, "Não foi possível montar tela tipo filial.", e);
        }
        finally
        {
            dao.closeConnection();
        }
        return listaTipoFiliais;
    }

    public FilialVO obtemUltimaFilial(FilialVO filialVO)
        throws SRVException
    {
        FilialVO filial;
        TipoFilialDAO dao = new TipoFilialDAO();
        filial = new FilialVO();
        try
        {
            filial = dao.obterUltimaFilial(filialVO);
        }
        catch(Exception e)
        {
            throw new PersistenciaException(log, "Não foi possível obter o último tipo filial.", e);
        }
        finally
        {
            dao.closeConnection();
        }
        return filial;
    }

}
