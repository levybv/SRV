package br.com.marisa.srv.demissao.vo;

import java.io.Serializable;

public class MotivoDemissaoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codMotDemissao;
	private String dscMotDemissao;
	private String origemDemissao;

	public Long getCodMotDemissao() {

		return codMotDemissao;
	}

	public void setCodMotDemissao(Long codMotDemissao) {

		this.codMotDemissao = codMotDemissao;
	}

	public String getDscMotDemissao() {

		return dscMotDemissao;
	}

	public void setDscMotDemissao(String dscMotDemissao) {

		this.dscMotDemissao = dscMotDemissao;
	}

	public String getOrigemDemissao() {

		return origemDemissao;
	}

	public void setOrigemDemissao(String origemDemissao) {

		this.origemDemissao = origemDemissao;
	}

}