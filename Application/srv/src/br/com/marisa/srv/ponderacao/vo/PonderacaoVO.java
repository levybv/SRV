package br.com.marisa.srv.ponderacao.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * Vo para armazenar os dados de Ponderação
 * 
 * @author Walter Fontes
 */
public class PonderacaoVO implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private Integer idPonderacao;
	private Integer idGrupoRemVar;
	private String  descricaoGrupoRemVar;
	private Integer idCargo;
	private String  descricaoCargo;
	private Integer idGrupoIndicador;
	private String  descricaoGrupoIndicador;
	private Integer idIndicador;
	private String  descricaoIndicador;
	private Double  peso;
	private Integer unidadePeso;
	private Double  valorPremio;
	private Date 	dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	private Integer idFilial;
	private Integer idTipoFilial;
	private String descrTipoFil;
	private String descrFil;
	
	
	//Formatados
	public String getValorPremioFormatado() {
		if (valorPremio == null) return "";
		return NumeroHelper.formataNumero(valorPremio, new Integer(Constantes.UNIDADE_VALOR));
	}
	public String getPesoFormatado() {
		if (peso == null) return "";
		return NumeroHelper.formataNumero(peso, unidadePeso);
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

	public String getDescricaoGrupoIndicador() {
		return descricaoGrupoIndicador;
	}

	public void setDescricaoGrupoIndicador(String descricaoGrupoIndicador) {
		this.descricaoGrupoIndicador = descricaoGrupoIndicador;
	}

	public String getDescricaoGrupoRemVar() {
		return descricaoGrupoRemVar;
	}

	public void setDescricaoGrupoRemVar(String descricaoGrupoRemVar) {
		this.descricaoGrupoRemVar = descricaoGrupoRemVar;
	}

	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}

	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}

	public Integer getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(Integer idCargo) {
		this.idCargo = idCargo;
	}

	public Integer getIdGrupoIndicador() {
		return idGrupoIndicador;
	}

	public void setIdGrupoIndicador(Integer idGrupoIndicador) {
		this.idGrupoIndicador = idGrupoIndicador;
	}

	public Integer getIdGrupoRemVar() {
		return idGrupoRemVar;
	}

	public void setIdGrupoRemVar(Integer idGrupoRemVar) {
		this.idGrupoRemVar = idGrupoRemVar;
	}

	public Integer getIdIndicador() {
		return idIndicador;
	}

	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}

	public Integer getIdPonderacao() {
		return idPonderacao;
	}

	public void setIdPonderacao(Integer idPonderacao) {
		this.idPonderacao = idPonderacao;
	}

	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}

	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Integer getUnidadePeso() {
		return unidadePeso;
	}

	public void setUnidadePeso(Integer unidadePeso) {
		this.unidadePeso = unidadePeso;
	}

	public Double getValorPremio() {
		return valorPremio;
	}

	public void setValorPremio(Double valorPremio) {
		this.valorPremio = valorPremio;
	}
	public Integer getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}
	public Integer getIdTipoFilial() {
		return idTipoFilial;
	}
	public void setIdTipoFilial(Integer idTipoFilial) {
		this.idTipoFilial = idTipoFilial;
	}
	public String getDescrTipoFil() {
		return descrTipoFil;
	}
	public void setDescrTipoFil(String descrTipoFil) {
		this.descrTipoFil = descrTipoFil;
	}
	public String getDescrFil() {
		return descrFil;
	}
	public void setDescrFil(String descrFil) {
		this.descrFil = descrFil;
	}	
}