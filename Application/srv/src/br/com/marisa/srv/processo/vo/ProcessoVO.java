package br.com.marisa.srv.processo.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Vo para armazenar os dados de Processos
 * 
 * @author Walter Fontes
 */
public class ProcessoVO implements Serializable{

	
	private Integer idProcesso;
	private Integer ordem;
	private String  nome;
	private String  descricao;
	private Boolean ativo;
	private Date 	dataCriacaoProcesso;
	
	
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public Date getDataCriacaoProcesso() {
		return dataCriacaoProcesso;
	}
	public void setDataCriacaoProcesso(Date dataCriacaoProcesso) {
		this.dataCriacaoProcesso = dataCriacaoProcesso;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(Integer idProcesso) {
		this.idProcesso = idProcesso;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}