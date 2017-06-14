package br.com.marisa.srv.geral.vo;

import java.io.Serializable;

/**
 * VO utilizado no tratamento de erros em todo o sistema
 * 
 * @author Walter Fontes
 *
 */
public class ErroVO implements Serializable {

    private String 		mensagemErro;
    private String 		stackErro;
    private String 		localErro;
    private String 		tipoErro;
    private Exception 	excecao;
    private String 		paginaDestino;
    
    public ErroVO(){}

    /**
     * @return Returns the localErro.
     */
    public String getLocalErro() {
        return localErro;
    }

    /**
     * @param localErro The localErro to set.
     */
    public void setLocalErro(String localErro) {
        this.localErro = localErro;
    }

    /**
     * @return Returns the mensagemErro.
     */
    public String getMensagemErro() {
        return mensagemErro;
    }

    /**
     * @param mensagemErro The mensagemErro to set.
     */
    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    /**
     * @return Returns the stackErro.
     */
    public String getStackErro() {
        return stackErro;
    }

    /**
     * @param stackErro The stackErro to set.
     */
    public void setStackErro(String stackErro) {
        this.stackErro = stackErro;
    }

    /**
     * @return Returns the tipoErro.
     */
    public String getTipoErro() {
        return tipoErro;
    }

    /**
     * @param tipoErro The tipoErro to set.
     */
    public void setTipoErro(String tipoErro) {
        this.tipoErro = tipoErro;
    }

    /**
     * @return Returns the excecao.
     */
    public Exception getExcecao() {
        return excecao;
    }

    /**
     * @param excecao The excecao to set.
     */
    public void setExcecao(Exception excecao) {
        this.excecao = excecao;
    }

    public String getPaginaDestino() {
        return paginaDestino;
    }

    public void setPaginaDestino(String paginaDestino) {
        this.paginaDestino = paginaDestino;
    }
    
    
}
