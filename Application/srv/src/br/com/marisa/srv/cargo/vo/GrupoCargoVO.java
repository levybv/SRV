package br.com.marisa.srv.cargo.vo;

import java.util.Date;

/**
 * VO para manter os dados de Grupos de Cargos
 * 
 * @author Walter Fontes
 */
public class GrupoCargoVO {

	private Integer idCargo;
	private Integer idGrupoRemuneracao;
	private Date    dataExclusao;
	private Integer idUsuarioExclusao;
	
	
	public Date getDataExclusao() {
		return dataExclusao;
	}
	public void setDataExclusao(Date dataUltimaAlteracao) {
		this.dataExclusao = dataUltimaAlteracao;
	}
	public Integer getIdCargo() {
		return idCargo;
	}
	public void setIdCargo(Integer idCargo) {
		this.idCargo = idCargo;
	}
	public Integer getIdGrupoRemuneracao() {
		return idGrupoRemuneracao;
	}
	public void setIdGrupoRemuneracao(Integer idGrupoRemuneracao) {
		this.idGrupoRemuneracao = idGrupoRemuneracao;
	}
	public Integer getIdUsuarioExclusao() {
		return idUsuarioExclusao;
	}
	public void setIdUsuarioExclusao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioExclusao = idUsuarioUltimaAlteracao;
	}

	
	
}
