package br.com.marisa.srv.funcionario.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.helper.CNPJCPFHelper;
import br.com.marisa.srv.geral.helper.DataHelper;

/**
 * VO para conter os dados de funcionários
 * 
 * @author Walter Fontes
 */
public class FuncionarioVO implements Comparable<FuncionarioVO>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2356339227634085567L;

	private Long    idFuncionario;
	private Integer idEmpresa;
	private Integer idFilial;
	private String  descricaoFilial;
	private String  idFilialRH;
	private String  descricaoFilialRH;
	private Integer idCargo;
	private String  descricaoCargo;
	private String  cracha;
	private Long    cpfFuncionario;
	private String  nomeFuncionario;
	private Integer idSituacaoRH;
	private String  descricaoSituacaoRH;
	private Date	dataSituacaoRH;
	private Integer idEmpresaRH;
	private String  descricaoEmpresaRH;
	private String  idCentroCusto;
	private String  descricaoCentroCusto;
	private Long    idFuncionarioSuperior;
	private Long    idFuncionarioAvaliador;
	private Date	dataAdmissao;
	private Date	dataDemissao;
	private Integer idUsuarioUltimaAlteracao;
	private Date	dataUltimaAlteracao;
	private Integer nroLinha;
	private Date dataPromocaoElegivel;
	private Integer codigoMotivoDEmissao;
	private Integer quantidadeDiasAfastamento;
	private String flagIndicadorCentroCusto;
	private String situacaoColaborador;
	private Integer idSituacaoAnterior;
	private Integer qtdDiasTrabalhadosMes;
	private String descricaoSituacaoAnterior;
	private Date dataInicioSituacaoAnterior;
	private String dataInicioSituacaoAnteriorFormatada;
	//private String descDiretoria;

	public FuncionarioVO() {
		super();
	}

	public FuncionarioVO(Long idFuncionario) {
		super();
		this.idFuncionario = idFuncionario;
	}

	public String getDataInicioSituacaoAnteriorFormatada() {
		if (dataInicioSituacaoAnterior != null) {
			return DataHelper.formataData(dataInicioSituacaoAnterior);
		}
		return "";
	}

	public void setDataInicioSituacaoAnteriorFormatada(String dataInicioSituacaoAnteriorFormatada) {
		this.dataInicioSituacaoAnteriorFormatada = dataInicioSituacaoAnteriorFormatada;
	}
	public String getSituacaoColaborador() {
		return situacaoColaborador;
	}
	public void setSituacaoColaborador(String situacaoColaborador) {
		this.situacaoColaborador = situacaoColaborador;
	}
	public Date getDataPromocaoElegivel() {
		return dataPromocaoElegivel;
	}
	public void setDataPromocaoElegivel(Date dataPromocaoElegivel) {
		this.dataPromocaoElegivel = dataPromocaoElegivel;
	}
	public Integer getCodigoMotivoDEmissao() {
		return codigoMotivoDEmissao;
	}
	public void setCodigoMotivoDEmissao(Integer codigoMotivoDEmissao) {
		this.codigoMotivoDEmissao = codigoMotivoDEmissao;
	}
	public Integer getQuantidadeDiasAfastamento() {
		return quantidadeDiasAfastamento;
	}
	public void setQuantidadeDiasAfastamento(Integer quantidadeDiasAfastamento) {
		this.quantidadeDiasAfastamento = quantidadeDiasAfastamento;
	}
	public String getFlagIndicadorCentroCusto() {
		return flagIndicadorCentroCusto;
	}
	public void setFlagIndicadorCentroCusto(String flagIndicadorCentroCusto) {
		this.flagIndicadorCentroCusto = flagIndicadorCentroCusto;
	}
	//Formatado
	public String getCpfFuncionarioFormatado() {
		if (cpfFuncionario != null) {
			return CNPJCPFHelper.formataCPF(cpfFuncionario.longValue());
		}
		return "";
	}
	public String getDataSituacaoRHFormatada() {
		if (dataSituacaoRH != null) {
			return DataHelper.formataData(dataSituacaoRH);
		}
		return "";		
	}
	public String getDataAdmissaoFormatada() {
		if (dataAdmissao != null) {
			return DataHelper.formataData(dataAdmissao);
		}
		return "";		
	}
	public String getDataDemissaoFormatada() {
		if (dataDemissao != null) {
			return DataHelper.formataData(dataDemissao);
		}
		return "";
	}
	
	
	//Getters e Setters
	public Long getCpfFuncionario() {
		return cpfFuncionario;
	}
	public void setCpfFuncionario(Long cpfFuncionario) {
		this.cpfFuncionario = cpfFuncionario;
	}
	public String getCracha() {
		return cracha;
	}
	public void setCracha(String cracha) {
		this.cracha = cracha;
	}
	public Date getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	public Date getDataDemissao() {
		return dataDemissao;
	}
	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}
	public Date getDataSituacaoRH() {
		return dataSituacaoRH;
	}
	public void setDataSituacaoRH(Date dataSituacaoRH) {
		this.dataSituacaoRH = dataSituacaoRH;
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
	public String getDescricaoCentroCusto() {
		return descricaoCentroCusto;
	}
	public void setDescricaoCentroCusto(String descricaoCentroCusto) {
		this.descricaoCentroCusto = descricaoCentroCusto;
	}
	public String getDescricaoEmpresaRH() {
		return descricaoEmpresaRH;
	}
	public void setDescricaoEmpresaRH(String descricaoEmpresaRH) {
		this.descricaoEmpresaRH = descricaoEmpresaRH;
	}
	public String getDescricaoFilial() {
		return descricaoFilial;
	}
	public void setDescricaoFilial(String descricaoFilial) {
		this.descricaoFilial = descricaoFilial;
	}
	public String getDescricaoFilialRH() {
		return descricaoFilialRH;
	}
	public void setDescricaoFilialRH(String descricaoFilialRH) {
		this.descricaoFilialRH = descricaoFilialRH;
	}
	public String getDescricaoSituacaoRH() {
		return descricaoSituacaoRH;
	}
	public void setDescricaoSituacaoRH(String descricaoSituacaoRH) {
		this.descricaoSituacaoRH = descricaoSituacaoRH;
	}
	public Integer getIdCargo() {
		return idCargo;
	}
	public void setIdCargo(Integer idCargo) {
		this.idCargo = idCargo;
	}
	public String getIdCentroCusto() {
		return idCentroCusto;
	}
	public void setIdCentroCusto(String idCentroCusto) {
		this.idCentroCusto = idCentroCusto;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Integer getIdEmpresaRH() {
		return idEmpresaRH;
	}
	public void setIdEmpresaRH(Integer idEmpresaRH) {
		this.idEmpresaRH = idEmpresaRH;
	}
	public Integer getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}
	public String getIdFilialRH() {
		return idFilialRH;
	}
	public void setIdFilialRH(String idFilialRH) {
		this.idFilialRH = idFilialRH;
	}
	public Long getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Long getIdFuncionarioAvaliador() {
		return idFuncionarioAvaliador;
	}
	public void setIdFuncionarioAvaliador(Long idFuncionarioAvaliador) {
		this.idFuncionarioAvaliador = idFuncionarioAvaliador;
	}
	public Long getIdFuncionarioSuperior() {
		return idFuncionarioSuperior;
	}
	public void setIdFuncionarioSuperior(Long idFuncionarioSuperior) {
		this.idFuncionarioSuperior = idFuncionarioSuperior;
	}
	public Integer getIdSituacaoRH() {
		return idSituacaoRH;
	}
	public void setIdSituacaoRH(Integer idSituacaoRH) {
		this.idSituacaoRH = idSituacaoRH;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public Integer getNroLinha() {
		return nroLinha;
	}
	public void setNroLinha(Integer nroLinha) {
		this.nroLinha = nroLinha;
	}
	public Integer getIdSituacaoAnterior() {
		return idSituacaoAnterior;
	}
	public void setIdSituacaoAnterior(Integer idSituacaoAnterior) {
		this.idSituacaoAnterior = idSituacaoAnterior;
	}
	public Integer getQtdDiasTrabalhadosMes() {
		return qtdDiasTrabalhadosMes;
	}
	public void setQtdDiasTrabalhadosMes(Integer qtdDiasTrabalhadosMes) {
		this.qtdDiasTrabalhadosMes = qtdDiasTrabalhadosMes;
	}
	public String getDescricaoSituacaoAnterior() {
		return descricaoSituacaoAnterior;
	}
	public void setDescricaoSituacaoAnterior(String descricaoSituacaoAnterior) {
		this.descricaoSituacaoAnterior = descricaoSituacaoAnterior;
	}
	public Date getDataInicioSituacaoAnterior() {
		return dataInicioSituacaoAnterior;
	}
	public void setDataInicioSituacaoAnterior(Date dataInicioSituacaoAnterior) {
		this.dataInicioSituacaoAnterior = dataInicioSituacaoAnterior;
	}

//	public String getDescDiretoria() {
//		return descDiretoria;
//	}
//
//	public void setDescDiretoria(String descDiretoria) {
//		this.descDiretoria = descDiretoria;
//	}

	@Override
	public int compareTo(FuncionarioVO o) {
		return getNomeFuncionario().compareTo(o.nomeFuncionario);
	}
	
	
}