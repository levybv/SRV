package br.com.marisa.srv.tlmkt.vo;

import java.io.Serializable;

/**
 * 
 * @author Levy Villar
 *
 */
public class TelemarketingIndicadorVO implements Serializable {

	private static final long serialVersionUID = -4300127040653790559L;

	private Integer codIndicador;
	private String nomeIndicador;
	private Double numMeta;
	private Integer codUnMeta;
	private Double numRealizado;
	private Integer codUnRealizado;

	public Double getNumMeta() {
		return numMeta;
	}

	public void setNumMeta(Double numMeta) {
		this.numMeta = numMeta;
	}

	public Integer getCodUnMeta() {
		return codUnMeta;
	}

	public void setCodUnMeta(Integer codUnMeta) {
		this.codUnMeta = codUnMeta;
	}

	public Double getNumRealizado() {
		return numRealizado;
	}

	public void setNumRealizado(Double numRealizado) {
		this.numRealizado = numRealizado;
	}

	public Integer getCodUnRealizado() {
		return codUnRealizado;
	}

	public void setCodUnRealizado(Integer codUnRealizado) {
		this.codUnRealizado = codUnRealizado;
	}

	public Integer getCodIndicador() {
		return codIndicador;
	}

	public void setCodIndicador(Integer codIndicador) {
		this.codIndicador = codIndicador;
	}

	public String getNomeIndicador() {
		return nomeIndicador;
	}

	public void setNomeIndicador(String nomeIndicador) {
		this.nomeIndicador = nomeIndicador;
	}

}
