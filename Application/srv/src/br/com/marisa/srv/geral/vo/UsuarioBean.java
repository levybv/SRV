package br.com.marisa.srv.geral.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.marisa.srv.geral.action.GenericFilter;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Bean que possui as informa��es do usu�rio, assim como um <code>java.util.Set</code>  
 * com todas as funcionalidades que o usu�rio tem permiss�o.
 * 
 * @author Walter Fontes
 */
public class UsuarioBean implements Serializable {
    
    
    private Map             permissoes;
    private String          ip;
    private Boolean         senhaExpirada;
    private Integer         qtdeTentativasSemSucesso;
    private UsuarioVO       usuarioVO;
    private GenericFilter   filtroGenerico;
    
    

    public GenericFilter getFiltroGenerico() {
		return filtroGenerico;
	}
	public void setFiltroGenerico(GenericFilter filtroGenerico) {
		this.filtroGenerico = filtroGenerico;
	}
	
    public UsuarioVO getUsuarioVO() {
		return usuarioVO;
	}
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
    /**
     * Retorna um <code>Set</code> de <code>Integer</code> com as permiss�es do usu�rio
     * 
     * @return Um <code>Set</code> de permiss�es
     */
    public Map getPermissoes() {
        return permissoes;
    }
    /**
     * Configura um <code>Set</code> de <code>Integer</code> com as permiss�es do usu�rio
     * 
     * @param permissoes
     */
    public void setPermissoes(Map permissoes) {
        this.permissoes = permissoes;
    }
    
    /**
     * Verifica se o usu�rio tem permiss�o para a funcionalidade requisitada.
     * 
     * @return Retorna <code>true</code> se o usu�rio tem permiss�o para tal funcionalidade. Caso contr�rio,
     * retorna <code>false</code>
     * @param nivel
     */
    public boolean verificaPermissao(Integer idFuncionalidade, Integer idTipoAcesso) {
        
        // Se o usu�rio est� com a senha expirada, n�o tem permiss�o alguma
        if (senhaExpirada != null && senhaExpirada.booleanValue()) {
            return false;
        }
        if (permissoes == null || permissoes.size() == 0) {
            return false;
        }
        if (permissoes.containsKey(idFuncionalidade)) {
        	
        	if (idTipoAcesso != null) {
        		
        		List tiposAcesso = (List)permissoes.get(idFuncionalidade);
        		if (tiposAcesso.contains(idTipoAcesso)) {
        			return true;
        		}
        		
        	} else {
        		return true;
        	}
        }
        return false;
    }
    
    /**
     * @return Returns the ip.
     */
    public String getIp() {
        return ip;
    }
    /**
     * @param ip The ip to set.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    /**
     * @return Returns the senhaExpirada.
     */
    public Boolean isSenhaExpirada() {
        return senhaExpirada;
    }
    /**
     * @param senhaExpirada The senhaExpirada to set.
     */
    public void setSenhaExpirada(Boolean senhaExpirada) {
        this.senhaExpirada = senhaExpirada;
    }
    /**
     * @return Returns the qtdeTentativasSemSucesso.
     */
    public Integer getQtdeTentativasSemSucesso() {
        return qtdeTentativasSemSucesso;
    }
    /**
     * @param qtdeTentativasSemSucesso The qtdeTentativasSemSucesso to set.
     */
    public void setQtdeTentativasSemSucesso(Integer qtdeTentativasSemSucesso) {
        this.qtdeTentativasSemSucesso = qtdeTentativasSemSucesso;
    }
}
