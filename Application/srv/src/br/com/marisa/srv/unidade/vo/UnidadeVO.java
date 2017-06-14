package br.com.marisa.srv.unidade.vo;

import java.io.Serializable;

/**
 * Vo para armazenar os dados de Unidade
 * 
 * @author Walter Fontes
 */
public class UnidadeVO implements Serializable{

	private Integer idUnidade;
	private String  descricaoUnidade;
	private String  simbolo;
	
	public String getDescricaoUnidade() {
		return descricaoUnidade;
	}
	public void setDescricaoUnidade(String descricaoUnidade) {
		this.descricaoUnidade = descricaoUnidade;
	}
	public Integer getIdUnidade() {
		return idUnidade;
	}
	public void setIdUnidade(Integer idUnidade) {
		this.idUnidade = idUnidade;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
}