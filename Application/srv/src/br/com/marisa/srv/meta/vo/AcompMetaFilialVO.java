package br.com.marisa.srv.meta.vo;

import java.io.Serializable;

import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * Vo para armazenar dados para Acompanhamento de Metas de Filiais
 * 
 * @author Walter Fontes
 */
public class AcompMetaFilialVO implements Serializable{

	private Integer idIndicador;
	private Integer idEmpresa;
	private Integer idFilial;
	private String  descricaoIndicador;
	private String  descricaoFilial;
	private Integer idUnidadeMeta;
	private Double  valorMeta;
	private Boolean temMeta;

	
	public String getIndicadorFormatado() {
		if (idIndicador != null) {
			return idIndicador + " - " + descricaoIndicador;
		}
		return null;
	}
	public String getFilialFormatado() {
		if (idFilial != null) {
			return idFilial + " - " + descricaoFilial;
		}
		return null;
	}	
	public String getValorMetaFormatado() {
		if (temMeta != null && temMeta.booleanValue()) {
			return NumeroHelper.formataNumero(valorMeta, idUnidadeMeta);
		}
		return "NÃO REALIZADA";
	}
	
	
	public Boolean getTemMeta() {
		return temMeta;
	}
	public void setTemMeta(Boolean temMeta) {
		this.temMeta = temMeta;
	}
	public String getDescricaoFilial() {
		return descricaoFilial;
	}
	public void setDescricaoFilial(String descricaoFilial) {
		this.descricaoFilial = descricaoFilial;
	}
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Integer getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}
	public Integer getIdIndicador() {
		return idIndicador;
	}
	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}
	public Integer getIdUnidadeMeta() {
		return idUnidadeMeta;
	}
	public void setIdUnidadeMeta(Integer idUnidadeMeta) {
		this.idUnidadeMeta = idUnidadeMeta;
	}
	public Double getValorMeta() {
		return valorMeta;
	}
	public void setValorMeta(Double valorMeta) {
		this.valorMeta = valorMeta;
	}

	
	
}