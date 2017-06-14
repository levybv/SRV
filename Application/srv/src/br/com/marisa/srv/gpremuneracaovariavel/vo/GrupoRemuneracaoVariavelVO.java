package br.com.marisa.srv.gpremuneracaovariavel.vo;

import java.util.Date;

/**
 * VO para manter os dados do Grupo da Remuneracao Variável
 * 
 * @author Walter Fontes
 */
public class GrupoRemuneracaoVariavelVO {

	private Integer idGrupoRemuneracao;
	private String	descricao;
	private String	descricaoOnline;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	
	
	public String getDescricaoOnline() {
		return descricaoOnline;
	}
	public void setDescricaoOnline(String descricaoOnline) {
		this.descricaoOnline = descricaoOnline;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getIdGrupoRemuneracao() {
		return idGrupoRemuneracao;
	}
	public void setIdGrupoRemuneracao(Integer idRemuneracao) {
		this.idGrupoRemuneracao = idRemuneracao;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
}