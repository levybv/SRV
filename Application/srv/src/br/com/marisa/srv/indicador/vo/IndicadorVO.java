package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;

public class IndicadorVO implements Serializable{
	

	private Integer codIndicador;
	private String descricaoIndicador;
	
	
	public Integer getCodIndicador() {
		return codIndicador;
	}
	public void setCodIndicador(Integer codIndicador) {
		this.codIndicador = codIndicador;
	}
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	}
	
