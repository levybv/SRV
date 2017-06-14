package br.com.marisa.srv.meta.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * Vo para armazenar os dados de Meta de Filial
 * 
 * @author Walter Fontes
 */
public class MetaFilialVO implements Serializable{

	private Integer idIndicador;
	private Integer idEmpresa;
	private Integer idFilial;
	private Integer ano;
	private Integer mes;
	private Integer idUnidadeMeta;
	private Double  valorMeta;
	private Double  percentualMeta;
	private Double  valorPremioFilial;
	private Date 	dataAlteracao;
	private Integer idUsuario;
	private String  descricaoIndicador;
	private String  descricaoFilial;
	private Integer nroLinha;
	
	
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
	public String getPeriodoFormatado() {
		return DataHelper.obtemMesExtenso(mes.intValue(), Constantes.CAPTALIZADO_CASE)+" / "+ano;
	}	
	public String getValorMetaFormatado() {
		return NumeroHelper.formataNumero(valorMeta, idUnidadeMeta);
	}
	public String getValorPremioFilialFormatado() {
		return NumeroHelper.formataNumero(valorPremioFilial, new Integer(Constantes.UNIDADE_VALOR));
	}
	
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
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
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Double getValorMeta() {
		return valorMeta;
	}
	public void setValorMeta(Double valorMeta) {
		this.valorMeta = valorMeta;
	}
	public Double getValorPremioFilial() {
		return valorPremioFilial;
	}
	public void setValorPremioFilial(Double valorPremioFilial) {
		this.valorPremioFilial = valorPremioFilial;
	}
	public Double getPercentualMeta() {
		return percentualMeta;
	}
	public void setPercentualMeta(Double percentualMeta) {
		this.percentualMeta = percentualMeta;
	}
	public Integer getNroLinha() {
		return nroLinha;
	}
	public void setNroLinha(Integer nroLinha) {
		this.nroLinha = nroLinha;
	}
	
}