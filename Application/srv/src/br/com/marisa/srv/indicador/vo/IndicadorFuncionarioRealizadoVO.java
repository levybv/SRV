package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.helper.NumeroHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;

public class IndicadorFuncionarioRealizadoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFuncionario;
	private Integer idEmpresa;
	private Integer idFilial;
	private Integer ano;
	private Integer mes;
	private Integer idGrupoIndicador;
	private String descricaoGrupoIndicador;
	private Integer idCargo;
	private Integer idIndicador;
	private String descricaoIndicador;
	private Integer idEscala;
	private Integer sequencialEscala;
	private Double realizadoFaixa;
	private Integer unidadeRealizadoFaixa;
	private Integer idPonderacao;
	private Double peso;
	private Integer unidadePeso;
	private Double realizadoPonderacao;
	private Integer unidadeRealizadoPonderacao;
	private Double meta;
	private Integer unidadeMeta;
	private Double realizado;
	private String realizadoFormatado = "0,00";
	private Integer unidadeRealizado;
	private Double realizadoXMeta;
	private Integer unidadeRealizadoXMeta;
	private Double valorPremio;
	private Double valorPremioCalculado;
	private String valorPremioCalculadoStr;
	private Integer unidadeValorPremioCalculado;
	private Double percentualRateio;
	private Date dataUltimaAlteracao;
	private Integer idUsuarioAlteracao;
	private String descricaoMeta;
	private String realizadoString;
	private String formulaIndicador;
	private String descFormulaIndicador;
	private String statusCalcRelz;
	private Integer idStatusCalcRelz;
	private Integer numEscala;
	private String flgPrrenchAtingIgualRealiz;
	private String descFonte;
	private Integer idIndicadorPai;
	private Integer codSitCalcRealzFunc;
	private String flgSentido;

	public String getDescricaoMetaFormatada() {

		if ((ObjectHelper.isNotEmpty(this.descricaoMeta)) && (ObjectHelper.isNotEmpty(this.meta))) {
			return getMetaFormatada() + " - " + this.descricaoMeta;
		}
		if (ObjectHelper.isNotEmpty(this.descricaoMeta)) {
			return this.descricaoMeta;
		}
		if (ObjectHelper.isNotEmpty(this.meta)) {
			return getMetaFormatada();
		}
		return "";
	}

	public String getDescricaoMeta() {

		return this.descricaoMeta;
	}

	public void setDescricaoMeta(String descricaoMeta) {

		this.descricaoMeta = descricaoMeta;
	}

	public String getChave() {

		return this.idIndicador + ";" + this.idFuncionario + ";" + this.idEmpresa + ";" + this.idFilial + ";" + this.ano + ";" + this.mes;
	}

	public Integer getIdPonderacao() {

		return this.idPonderacao;
	}

	public void setIdPonderacao(Integer idPonderacao) {

		this.idPonderacao = idPonderacao;
	}

	public Double getPercentualRateio() {

		return this.percentualRateio;
	}

	public void setPercentualRateio(Double percentualRateiro) {

		this.percentualRateio = percentualRateiro;
	}

	public Double getRealizadoPonderacao() {

		return this.realizadoPonderacao;
	}

	public String getRealizadoPonderacaoFormatado() {

		return NumeroHelper.formataNumero(this.realizadoPonderacao, this.unidadeRealizadoPonderacao);
	}

	public void setRealizadoPonderacao(Double realizadoPonderacao) {

		this.realizadoPonderacao = realizadoPonderacao;
	}

	public Integer getSequencialEscala() {

		return this.sequencialEscala;
	}

	public void setSequencialEscala(Integer sequencialEscala) {

		this.sequencialEscala = sequencialEscala;
	}

	public Integer getUnidadeRealizadoPonderacao() {

		return this.unidadeRealizadoPonderacao;
	}

	public void setUnidadeRealizadoPonderacao(Integer unidadeRealizadoPonderacao) {

		this.unidadeRealizadoPonderacao = unidadeRealizadoPonderacao;
	}

	public Double getValorPremioCalculado() {
		getValorPremioCalculadoStr();
		return this.valorPremioCalculado;
	}

	public void setValorPremioCalculado(Double valorPremioCalculado) {
		getValorPremioCalculadoStr();
		this.valorPremioCalculado = valorPremioCalculado;
	}

	public Date getDataUltimaAlteracao() {

		return this.dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {

		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public Integer getIdUsuarioAlteracao() {

		return this.idUsuarioAlteracao;
	}

	public void setIdUsuarioAlteracao(Integer idUsuarioAlteracao) {

		this.idUsuarioAlteracao = idUsuarioAlteracao;
	}

	public Integer getIdEmpresa() {

		return this.idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {

		this.idEmpresa = idEmpresa;
	}

	public Integer getIdFilial() {

		return this.idFilial;
	}

	public void setIdFilial(Integer idFilial) {

		this.idFilial = idFilial;
	}

	public Integer getAno() {

		return this.ano;
	}

	public void setAno(Integer ano) {

		this.ano = ano;
	}

	public String getDescricaoGrupoIndicador() {

		return this.descricaoGrupoIndicador;
	}

	public void setDescricaoGrupoIndicador(String descricaoGrupoIndicador) {

		this.descricaoGrupoIndicador = descricaoGrupoIndicador;
	}

	public String getDescricaoIndicador() {

		return this.descricaoIndicador;
	}

	public void setDescricaoIndicador(String descricaoIndicador) {

		this.descricaoIndicador = descricaoIndicador;
	}

	public Integer getIdEscala() {

		return this.idEscala;
	}

	public void setIdEscala(Integer idEscala) {

		this.idEscala = idEscala;
	}

	public Long getIdFuncionario() {

		return this.idFuncionario;
	}

	public void setIdFuncionario(Long idFuncionario) {

		this.idFuncionario = idFuncionario;
	}

	public Integer getIdGrupoIndicador() {

		return this.idGrupoIndicador;
	}

	public void setIdGrupoIndicador(Integer idGrupoIndicador) {

		this.idGrupoIndicador = idGrupoIndicador;
	}

	public Integer getIdIndicador() {

		return this.idIndicador;
	}

	public void setIdIndicador(Integer idIndicador) {

		this.idIndicador = idIndicador;
	}

	public Integer getMes() {

		return this.mes;
	}

	public void setMes(Integer mes) {

		this.mes = mes;
	}

	public Double getMeta() {

		return this.meta;
	}

	public String getMetaFormatada() {

		return NumeroHelper.formataNumero(this.meta, this.unidadeMeta);
	}

	public void setMeta(Double meta) {

		this.meta = meta;
	}

	public Double getPeso() {

		return this.peso;
	}

	public String getPesoFormatado() {

		return NumeroHelper.formataNumero(this.peso, this.unidadePeso);
	}

	public void setPeso(Double peso) {

		this.peso = peso;
	}

	public Double getRealizado() {

		return this.realizado;
	}

	public String getRealizadoFormatado() {
		if (realizado==null || unidadeRealizado==null) {
			realizadoFormatado = "";
		} else {
			realizadoFormatado = NumeroHelper.formataValor(realizado, new Integer(4), unidadeRealizado);
		}
		return realizadoFormatado;
	}

	public void setRealizadoFormatado(String param) {
		realizadoFormatado = param;
	}

	public void setRealizado(Double realizado) {

		this.realizado = realizado;
	}

	public Double getRealizadoFaixa() {

		return this.realizadoFaixa;
	}

	public String getRealizadoFaixaFormatado() {

		return NumeroHelper.formataNumero(this.realizadoFaixa, this.unidadeRealizadoFaixa);
	}

	public void setRealizadoFaixa(Double realizadoFaixa) {

		this.realizadoFaixa = realizadoFaixa;
	}

	public Double getRealizadoXMeta() {

		return this.realizadoXMeta;
	}

	public String getRealizadoXMetaFormatado() {

		return NumeroHelper.formataNumero(this.realizadoXMeta, this.unidadeRealizadoXMeta);
	}

	public void setRealizadoXMeta(Double realizadoXMeta) {

		this.realizadoXMeta = realizadoXMeta;
	}

	public Integer getUnidadeMeta() {

		return this.unidadeMeta;
	}

	public void setUnidadeMeta(Integer unidadeMeta) {

		this.unidadeMeta = unidadeMeta;
	}

	public Integer getUnidadePeso() {

		return this.unidadePeso;
	}

	public void setUnidadePeso(Integer unidadePeso) {

		this.unidadePeso = unidadePeso;
	}

	public Integer getUnidadeRealizado() {

		return this.unidadeRealizado;
	}

	public void setUnidadeRealizado(Integer unidadeRealizado) {

		this.unidadeRealizado = unidadeRealizado;
	}

	public Integer getUnidadeRealizadoFaixa() {

		return this.unidadeRealizadoFaixa;
	}

	public void setUnidadeRealizadoFaixa(Integer unidadeRealizadoFaixa) {

		this.unidadeRealizadoFaixa = unidadeRealizadoFaixa;
	}

	public Integer getUnidadeRealizadoXMeta() {

		return this.unidadeRealizadoXMeta;
	}

	public void setUnidadeRealizadoXMeta(Integer unidadeRealizadoXMeta) {

		this.unidadeRealizadoXMeta = unidadeRealizadoXMeta;
	}

	public Integer getUnidadeValorPremioCalculado() {

		return this.unidadeValorPremioCalculado;
	}

	public void setUnidadeValorPremioCalculado(Integer unidadeValorPremio) {

		this.unidadeValorPremioCalculado = unidadeValorPremio;
	}

	public Double getValorPremio() {

		return this.valorPremio;
	}

	public String getValorPremioCalculadoFormatado() {

		return NumeroHelper.formataNumero(this.valorPremioCalculado, this.unidadeValorPremioCalculado);
	}

	public void setValorPremio(Double valorPremio) {

		this.valorPremio = valorPremio;
	}

	public String getRealizadoString() {

		return this.realizadoString;
	}

	public void setRealizadoString(String realizadoString) {

		this.realizadoString = realizadoString;
	}

	public String getFormulaIndicador() {

		return this.formulaIndicador;
	}

	public void setFormulaIndicador(String formulaIndicador) {

		this.formulaIndicador = formulaIndicador;
	}

	public String getStatusCalcRelz() {

		return this.statusCalcRelz;
	}

	public void setStatusCalcRelz(String statusCalcRelz) {

		this.statusCalcRelz = statusCalcRelz;
	}

	public Integer getNumEscala() {

		return this.numEscala;
	}

	public void setNumEscala(Integer numEscala) {

		this.numEscala = numEscala;
	}

	public String getFlgPrrenchAtingIgualRealiz() {

		return flgPrrenchAtingIgualRealiz;
	}

	public void setFlgPrrenchAtingIgualRealiz(String flgPrrenchAtingIgualRealiz) {

		this.flgPrrenchAtingIgualRealiz = flgPrrenchAtingIgualRealiz;
	}

	
	public String getValorPremioCalculadoStr() {
		valorPremioCalculadoStr = NumeroHelper.formataValor(valorPremioCalculado);
		return valorPremioCalculadoStr;
	}

	
	public void setValorPremioCalculadoStr(String valorPremioCalculadoStr) {
	
		this.valorPremioCalculadoStr = valorPremioCalculadoStr;
	}

	public String getDescFormulaIndicador() {
		return descFormulaIndicador;
	}

	public void setDescFormulaIndicador(String descFormulaIndicador) {
		this.descFormulaIndicador = descFormulaIndicador;
	}

	public String getDescFonte() {
		return descFonte;
	}

	public void setDescFonte(String descFonte) {
		this.descFonte = descFonte;
	}

	public Integer getIdIndicadorPai() {
		return idIndicadorPai;
	}

	public void setIdIndicadorPai(Integer idIndicadorPai) {
		this.idIndicadorPai = idIndicadorPai;
	}

	public Integer getIdStatusCalcRelz() {
		return idStatusCalcRelz;
	}

	public void setIdStatusCalcRelz(Integer idStatusCalcRelz) {
		this.idStatusCalcRelz = idStatusCalcRelz;
	}

	public Integer getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(Integer idCargo) {
		this.idCargo = idCargo;
	}

	public Integer getCodSitCalcRealzFunc() {
		return codSitCalcRealzFunc;
	}

	public void setCodSitCalcRealzFunc(Integer codSitCalcRealzFunc) {
		this.codSitCalcRealzFunc = codSitCalcRealzFunc;
	}

	public String getFlgSentido() {
		return flgSentido;
	}

	public void setFlgSentido(String flgSentido) {
		this.flgSentido = flgSentido;
	}
}