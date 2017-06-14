package br.com.marisa.srv.relatorios.data;

import java.io.Serializable;

public class CelulaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1457694212128741583L;

	private String nomeCampo;
	private Integer tipoCampo;
	private Object valorCampo;

	public String getNomeCampo() {
		return this.nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public Integer getTipoCampo() {
		return this.tipoCampo;
	}

	public void setTipoCampo(Integer tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public Object getValorCampo() {
		return this.valorCampo;
	}

	public void setValorCampo(Object valorCampo) {
		this.valorCampo = valorCampo;
	}
}