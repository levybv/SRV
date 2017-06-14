package br.com.marisa.srv.funcionario.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para conter os dados de salários base
 * 
 * @author Walter Fontes
 */
public class SalarioBaseVO implements Serializable{
	
	private Integer idEmpresa;
	private Integer idFilial;
	private String  descricaoFilial;
	private Long    idFuncionario;
	private String  nomeFuncionario;
	private Integer ano;
	private Integer mes;
	private Double  salarioBase;
	private Date	dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	private Integer nroLinha;

	//Formatados
	public String getSalarioBaseFormatado() {
		return NumeroHelper.formataNumero(salarioBase, new Integer(Constantes.UNIDADE_VALOR));
	}
	public String getFuncionarioFormatado() {
		if (idFuncionario != null) {
			return idFuncionario + " - " + nomeFuncionario;
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
	
	//Getters and setters
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public String getDescricaoFilial() {
		return descricaoFilial;
	}
	public void setDescricaoFilial(String descricaoFilial) {
		this.descricaoFilial = descricaoFilial;
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
	public Long getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public Double getSalarioBase() {
		return salarioBase;
	}
	public void setSalarioBase(Double salarioBase) {
		this.salarioBase = salarioBase;
	}
	public Integer getNroLinha() {
		return nroLinha;
	}
	public void setNroLinha(Integer nroLinha) {
		this.nroLinha = nroLinha;
	}
}