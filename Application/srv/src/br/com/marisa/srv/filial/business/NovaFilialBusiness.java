package br.com.marisa.srv.filial.business;

import br.com.marisa.srv.filial.dao.NovaFilialDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import java.util.List;
import org.apache.log4j.Logger;

public class NovaFilialBusiness {

    private static final Logger log = Logger.getLogger(br.com.marisa.srv.filial.business.NovaFilialBusiness.class);
    private static NovaFilialBusiness instance = new NovaFilialBusiness();

    public static final NovaFilialBusiness getInstance() {
        return instance;
    }

    private NovaFilialBusiness() {
    	super();
    }

    public List montaTelaInicial(int codEmpresa, Integer codFilial, Integer codTipoFilial, String isAtivo) throws SRVException {
        List listaFiliais;
        listaFiliais = null;
        NovaFilialDAO filialDAO = new NovaFilialDAO();
        try {
            listaFiliais = filialDAO.montaTelaInicial(codEmpresa, codFilial, codTipoFilial, isAtivo);
        } catch(Exception e) {
            throw new PersistenciaException(log, "Não foi possível montar a tela inicial", e);
        } finally {
            filialDAO.closeConnection();
        }
        return listaFiliais;
    }

}
