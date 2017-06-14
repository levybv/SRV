package br.com.marisa.srv.meta.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * Vo para armazenar os dados de Meta de Funcionários
 * 
 * @author Walter Fontes
 */
public class MetaFuncionarioVO implements Serializable{

	private Integer idIndicador;
	private Long    idFuncionario;
	private Integer ano;
	private Integer mes;
	private Integer idUnidadeMeta;
	private Double  valorMeta;
	private String	descricaoMeta;
	private Date 	dataAlteracao;
	private Integer idUsuario;
	private String  descricaoIndicador;
	private String  nomeFuncionario;
	private Integer nroLinha;
	
	
	public Integer getNroLinha() {
		return nroLinha;
	}

	public void setNroLinha(Integer nroLinha) {
		this.nroLinha = nroLinha;
	}

	public String getPeriodoFormatado() {
		return DataHelper.obtemMesExtenso(mes.intValue(), Constantes.CAPTALIZADO_CASE)+" / "+ano;
	}	
	
	public String getIndicadorFormatado() {
		if (idIndicador != null) {
			return idIndicador + " - " + descricaoIndicador;
		}
		return null;
	}
	public String getFuncionarioFormatado() {
		if (idFuncionario != null) {
			return idFuncionario + " - " + nomeFuncionario;
		}
		return null;
	}	
	public String getValorMetaFormatado() {
		return NumeroHelper.formataNumero(valorMeta, idUnidadeMeta);
	}
	
	
	public String getDescricaoMeta() {
		return descricaoMeta;
	}

	public void setDescricaoMeta(String descricaoMeta) {
		this.descricaoMeta = descricaoMeta;
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
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String descricaoFilial) {
		this.nomeFuncionario = descricaoFilial;
	}
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	public Long getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
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
}