package br.com.marisa.srv.funcionario.report.vo;

import java.io.Serializable;
import java.util.List;

import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;

public class RelatorioBonusAnualFuncionarioVO implements Serializable {

	private static final long serialVersionUID = 7749620736204147684L;

	private String nomeFuncionario;
	private Long idFuncionario;
	private Integer anoBase;
	private String cargo;
	private String periodoRecebimento;
	private String textoConsentimento;
	private String textoAprovacao;

	private List<IndicadorFuncionarioRealizadoVO> indicadoresRealizados;
	private List<IndicadorFuncionarioRealizadoVO> indicadoresCorporativos;
	private List<IndicadorFuncionarioRealizadoVO> indicadoresIndividuais;
	private IndicadorFuncionarioRealizadoVO totalRealizadoVO;
	private IndicadorFuncionarioRealizadoVO totalCorporativoRealizadoVO;
	private IndicadorFuncionarioRealizadoVO totalIndividualRealizadoVO;

	public List<IndicadorFuncionarioRealizadoVO> getIndicadoresRealizados() {
		return indicadoresRealizados;
	}

	public void setIndicadoresRealizados(
			List<IndicadorFuncionarioRealizadoVO> indicadoresRealizados) {
		this.indicadoresRealizados = indicadoresRealizados;
	}

	public IndicadorFuncionarioRealizadoVO getTotalRealizadoVO() {
		return totalRealizadoVO;
	}

	public void setTotalRealizadoVO(IndicadorFuncionarioRealizadoVO totalRealizadoVO) {
		this.totalRealizadoVO = totalRealizadoVO;
	}

	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public Long getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public Integer getAnoBase() {
		return anoBase;
	}

	public void setAnoBase(Integer anoBase) {
		this.anoBase = anoBase;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getPeriodoRecebimento() {
		return periodoRecebimento;
	}

	public void setPeriodoRecebimento(String periodoRecebimento) {
		this.periodoRecebimento = periodoRecebimento;
	}

	public String getTextoConsentimento() {
		return textoConsentimento;
	}

	public void setTextoConsentimento(String textoConsentimento) {
		this.textoConsentimento = textoConsentimento;
	}

	public List<IndicadorFuncionarioRealizadoVO> getIndicadoresCorporativos() {
		return indicadoresCorporativos;
	}

	public void setIndicadoresCorporativos(
			List<IndicadorFuncionarioRealizadoVO> indicadoresCorporativos) {
		this.indicadoresCorporativos = indicadoresCorporativos;
	}

	public List<IndicadorFuncionarioRealizadoVO> getIndicadoresIndividuais() {
		return indicadoresIndividuais;
	}

	public void setIndicadoresIndividuais(
			List<IndicadorFuncionarioRealizadoVO> indicadoresIndividuais) {
		this.indicadoresIndividuais = indicadoresIndividuais;
	}

	public IndicadorFuncionarioRealizadoVO getTotalCorporativoRealizadoVO() {
		return totalCorporativoRealizadoVO;
	}

	public void setTotalCorporativoRealizadoVO(
			IndicadorFuncionarioRealizadoVO totalCorporativoRealizadoVO) {
		this.totalCorporativoRealizadoVO = totalCorporativoRealizadoVO;
	}

	public IndicadorFuncionarioRealizadoVO getTotalIndividualRealizadoVO() {
		return totalIndividualRealizadoVO;
	}

	public void setTotalIndividualRealizadoVO(
			IndicadorFuncionarioRealizadoVO totalIndividualRealizadoVO) {
		this.totalIndividualRealizadoVO = totalIndividualRealizadoVO;
	}

	public String getTextoAprovacao() {
		return textoAprovacao;
	}

	public void setTextoAprovacao(String textoAprovacao) {
		this.textoAprovacao = textoAprovacao;
	}

}
