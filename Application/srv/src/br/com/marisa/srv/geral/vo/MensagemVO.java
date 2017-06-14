package br.com.marisa.srv.geral.vo;

import java.io.Serializable;

/**
 * 
 * @author Levy Villar
 *
 */
public class MensagemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3424338266941155181L;

	private String tituloMensagem;
	private String textoMensagem;
	private boolean exibeMensagem;
	private String textoPopup;
	private boolean exibePopup;

	public String getTituloMensagem() {
		return tituloMensagem;
	}

	public void setTituloMensagem(String tituloMensagem) {
		this.tituloMensagem = tituloMensagem;
	}

	public String getTextoMensagem() {
		return textoMensagem;
	}

	public void setTextoMensagem(String textoMensagem) {
		this.textoMensagem = textoMensagem;
	}

	public boolean getExibeMensagem() {
		return exibeMensagem;
	}

	public void setExibeMensagem(boolean exibeMensagem) {
		this.exibeMensagem = exibeMensagem;
	}

	public String getTextoPopup() {
		return textoPopup;
	}

	public void setTextoPopup(String textoPopup) {
		this.textoPopup = textoPopup;
	}

	public boolean getExibePopup() {
		return exibePopup;
	}

	public void setExibePopup(boolean exibePopup) {
		this.exibePopup = exibePopup;
	}

}
