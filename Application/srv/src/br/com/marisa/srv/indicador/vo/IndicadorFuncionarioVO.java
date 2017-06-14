package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;
import java.util.Date;

public class IndicadorFuncionarioVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer idFuncionario;
	private Integer idIndicador;
	private String  descricaoIndicador;
	private Integer idEmpresa;
	private Integer codFilial;
	private Integer ano;
	private Integer mes;
	private String  nomeUsuario;
	private String  cargoUsuario;
	private Date    dataAdimissao;
	private Date    dataDemissao;
	private Double  basePremio;
	private Double  rateio;
	private Double  premio;
	private Double  valorPremio; //lideres VALOR FIXO
	private Double  meta;// lideres
	private Double  valorRealizado;
	private Double  numRealizadoMeta;
	private Double	valorPremioSomar;
	
	
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Integer getIdIndicador() {
		return idIndicador;
	}
	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Double getNumRealizadoMeta() {
		return numRealizadoMeta;
	}
	public void setNumRealizadoMeta(Double numRealizadoMeta) {
		this.numRealizadoMeta = numRealizadoMeta;
	}
	public Double getBasePremio() {
		return basePremio;
	}
	public void setBasePremio(Double basePremio) {
		this.basePremio = basePremio;
	}
	public String getCargoUsuario() {
		return cargoUsuario;
	}
	public void setCargoUsuario(String cargoUsuario) {
		this.cargoUsuario = cargoUsuario;
	}
	public Integer getCodFilial() {
		return codFilial;
	}
	public void setCodFilial(Integer codFilial) {
		this.codFilial = codFilial;
	}
	public Date getDataAdimissao() {
		return dataAdimissao;
	}
	public void setDataAdimissao(Date dataAdimissao) {
		this.dataAdimissao = dataAdimissao;
	}
	public Date getDataDemissao() {
		return dataDemissao;
	}
	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public Double getRateio() {
		return rateio;
	}
	public void setRateio(Double rateio) {
		this.rateio = rateio;
	}
	public Double getValorPremio() {
		return valorPremio;
	}
	public void setValorPremio(Double valorPremio) {
		this.valorPremio = valorPremio;
	}
	public Double getMeta() {
		return meta;
	}
	public void setMeta(Double meta) {
		this.meta = meta;
	}
	public Double getPremio() {
		return premio;
	}
	public void setPremio(Double premio) {
		this.premio = premio;
	}
	public Double getValorRealizado() {
		return valorRealizado;
	}
	public void setValorRealizado(Double valorRealizado) {
		this.valorRealizado = valorRealizado;
	}
	public Double getValorPremioSomar() {
		return valorPremioSomar;
	}
	public void setValorPremioSomar(Double valorPremioSomar) {
		this.valorPremioSomar = valorPremioSomar;
	}
	
}
	
