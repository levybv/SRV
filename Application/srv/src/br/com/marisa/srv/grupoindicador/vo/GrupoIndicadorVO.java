package br.com.marisa.srv.grupoindicador.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Vo para armazenar os dados de um indicador
 * 
 * @author Walter Fontes
 */
public class GrupoIndicadorVO implements Serializable{

	
	private Integer idGrupoIndicador;
	private String  descricaoGrupoIndicador;
	private Integer idTipoRemuneracao;
	private String  descricaoTipoRemuneracao;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	
	
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public String getDescricaoGrupoIndicador() {
		return descricaoGrupoIndicador;
	}
	public void setDescricaoGrupoIndicador(String descricaoGrupoIndicador) {
		this.descricaoGrupoIndicador = descricaoGrupoIndicador;
	}
	public String getDescricaoTipoRemuneracao() {
		return descricaoTipoRemuneracao;
	}
	public void setDescricaoTipoRemuneracao(String descricaoTipoRemuneracao) {
		this.descricaoTipoRemuneracao = descricaoTipoRemuneracao;
	}
	public Integer getIdGrupoIndicador() {
		return idGrupoIndicador;
	}
	public void setIdGrupoIndicador(Integer idGrupoIndicador) {
		this.idGrupoIndicador = idGrupoIndicador;
	}
	public Integer getIdTipoRemuneracao() {
		return idTipoRemuneracao;
	}
	public void setIdTipoRemuneracao(Integer idTipoRemuneracao) {
		this.idTipoRemuneracao = idTipoRemuneracao;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}	
	
	
}