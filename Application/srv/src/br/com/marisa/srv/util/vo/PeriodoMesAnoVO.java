package br.com.marisa.srv.util.vo;

import java.io.Serializable;

public class PeriodoMesAnoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5188034662979381743L;

	private String periodo;
	private String periodoDesc;

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getPeriodoDesc() {
		return periodoDesc;
	}

	public void setPeriodoDesc(String periodoDesc) {
		this.periodoDesc = periodoDesc;
	}

}
