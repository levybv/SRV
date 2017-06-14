package br.com.marisa.srv.cargo.vo;

import java.util.Date;

/**
 * VO para manter os dados de Cargos
 * 
 * @author Walter Fontes
 */
public class CargoVO {

	private Integer idCargo;
	private String	descricaoCargo;
	private Integer idClasseHay;
	private String	descricaoClasseHay;
	private Boolean agrupaFiliais;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;

	//
	private Integer idGrpRemVar;
	private String descricaoTodosGrpRemVar;
	
	public Boolean getAgrupaFiliais() {
		return agrupaFiliais;
	}
	public String getAgrupaFiliaisFormatado() {
		if (agrupaFiliais != null && agrupaFiliais.booleanValue()) {
			return "Sim";
		} else {
			return "Não";
		}
	}
	public void setAgrupaFiliais(Boolean agrupaFiliais) {
		this.agrupaFiliais = agrupaFiliais;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public String getDescricaoCargo() {
		return descricaoCargo;
	}
	public void setDescricaoCargo(String descricaoCargo) {
		this.descricaoCargo = descricaoCargo;
	}
	public String getDescricaoClasseHay() {
		return descricaoClasseHay;
	}
	public void setDescricaoClasseHay(String descricaoClasseHay) {
		this.descricaoClasseHay = descricaoClasseHay;
	}
	public Integer getIdCargo() {
		return idCargo;
	}
	public void setIdCargo(Integer idCargo) {
		this.idCargo = idCargo;
	}
	public Integer getIdClasseHay() {
		return idClasseHay;
	}
	public void setIdClasseHay(Integer idClasseHay) {
		this.idClasseHay = idClasseHay;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public String getDescricaoTodosGrpRemVar() {
		return descricaoTodosGrpRemVar;
	}
	public void setDescricaoTodosGrpRemVar(String descricaoTodosGrpRemVar) {
		this.descricaoTodosGrpRemVar = descricaoTodosGrpRemVar;
	}
	public Integer getIdGrpRemVar() {
		return idGrpRemVar;
	}
	public void setIdGrpRemVar(Integer idGrpRemVar) {
		this.idGrpRemVar = idGrpRemVar;
	}
	
}
