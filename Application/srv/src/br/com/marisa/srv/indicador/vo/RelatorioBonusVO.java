package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Vo para armazenar o resultado do relatorio de bonus
 * 
 * @author Walter Fontes
 */
public class RelatorioBonusVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5964600459137313591L;

	private List<IndicadorFuncionarioRealizadoVO> indicadoresRealizados;
	private List<IndicadorFuncionarioRealizadoVO> indicadoresCorporativos;
	private List<IndicadorFuncionarioRealizadoVO> indicadoresIndividuais;
	private IndicadorFuncionarioRealizadoVO totalRealizadoVO;
	private IndicadorFuncionarioRealizadoVO totalCorporativoRealizadoVO;
	private IndicadorFuncionarioRealizadoVO totalIndividualRealizadoVO;
	
	private Double valorDesempenho;
	private Double salarioTarget;
	private Double qtdSalario;
	private Boolean isAtingiu;
	
	public List<IndicadorFuncionarioRealizadoVO> getIndicadoresRealizados() {
		return indicadoresRealizados;
	}
	public void setIndicadoresRealizados(List<IndicadorFuncionarioRealizadoVO> indicadoresRealizados) {
		this.indicadoresRealizados = indicadoresRealizados;
	}
	public IndicadorFuncionarioRealizadoVO getTotalRealizadoVO() {
		return totalRealizadoVO;
	}
	public void setTotalRealizadoVO(IndicadorFuncionarioRealizadoVO totalRealizadoVO) {
		this.totalRealizadoVO = totalRealizadoVO;
	}
	public List<IndicadorFuncionarioRealizadoVO> getIndicadoresCorporativos() {
		return indicadoresCorporativos;
	}
	public void setIndicadoresCorporativos(List<IndicadorFuncionarioRealizadoVO> indicadoresCorporativos) {
		this.indicadoresCorporativos = indicadoresCorporativos;
	}
	public List<IndicadorFuncionarioRealizadoVO> getIndicadoresIndividuais() {
		return indicadoresIndividuais;
	}
	public void setIndicadoresIndividuais(List<IndicadorFuncionarioRealizadoVO> indicadoresIndividuais) {
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
	public Double getValorDesempenho() {
		return valorDesempenho;
	}
	public void setValorDesempenho(Double valorDesempenho) {
		this.valorDesempenho = valorDesempenho;
	}
	public Double getSalarioTarget() {
		return salarioTarget;
	}
	public void setSalarioTarget(Double salarioTarget) {
		this.salarioTarget = salarioTarget;
	}
	public Double getQtdSalario() {
		return qtdSalario;
	}
	public void setQtdSalario(Double qtdSalario) {
		this.qtdSalario = qtdSalario;
	}
	public Boolean getIsAtingiu() {
		return isAtingiu;
	}
	public void setIsAtingiu(Boolean isAtingiu) {
		this.isAtingiu = isAtingiu;
	}
}