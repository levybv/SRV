package br.com.marisa.srv.relatorio.vo;

import java.io.Serializable;
import java.util.Date;

public class RelatorioBonusStatusVO
  implements Serializable
{
  private Integer codCargo;
  private String descrCargo;
  private Integer codFil;
  private Integer codFunc;
  private String nomeFunc;
  private String situacaoRh;
  private Date dataDemissao;
  private String grupoRemVar;
  private Integer ano;
  private Integer mes;
  private String status;
  private String formula;
  private String indicador;
  private String peso;
  private String meta;
  private String relaizado;
  private String atingimentoMeta;
  private String escala;
  private String atingimentoEscala;
  private String pesoResultado;
  private String numRealzPond;
  private String qtdSalReceber;
  private String verbaRh;
  private String cracha;
  private String c_custo;
  private String descr_meta;
  private String cod_escala;
  private String atingimento;
  private String result_conf_escala;
  private String conceito_formula;
  private String grp_remuneracao;
  private String proporcionalidade;
  private String filial;
  private String grpIndic;
  private String target;
  private String ordem;
  private String valorAntesGatilho;
  private String progressaoReducao;
  private String mesesProp;

  public String getValorAntesGatilho()
  {
    return this.valorAntesGatilho;
  }
  public void setValorAntesGatilho(String valorAntesGatilho) {
    this.valorAntesGatilho = valorAntesGatilho;
  }
  public String getProgressaoReducao() {
    return this.progressaoReducao;
  }
  public void setProgressaoReducao(String progressaoReducao) {
    this.progressaoReducao = progressaoReducao;
  }
  public String getMesesProp() {
    return this.mesesProp;
  }
  public void setMesesProp(String mesesProp) {
    this.mesesProp = mesesProp;
  }
  public String getOrdem() {
    return this.ordem;
  }
  public void setOrdem(String ordem) {
    this.ordem = ordem;
  }
  public String getTarget() {
    return this.target;
  }
  public void setTarget(String target) {
    this.target = target;
  }
  public String getGrpIndic() {
    return this.grpIndic;
  }
  public void setGrpIndic(String grpIndic) {
    this.grpIndic = grpIndic;
  }
  public String getFilial() {
    return this.filial;
  }
  public void setFilial(String filial) {
    this.filial = filial;
  }
  public String getProporcionalidade() {
    return this.proporcionalidade;
  }
  public void setProporcionalidade(String proporcionalidade) {
    this.proporcionalidade = proporcionalidade;
  }
  public String getCracha() {
    return this.cracha;
  }
  public void setCracha(String cracha) {
    this.cracha = cracha;
  }
  public String getC_custo() {
    return this.c_custo;
  }
  public void setC_custo(String c_custo) {
    this.c_custo = c_custo;
  }
  public String getDescr_meta() {
    return this.descr_meta;
  }
  public void setDescr_meta(String descr_meta) {
    this.descr_meta = descr_meta;
  }
  public String getCod_escala() {
    return this.cod_escala;
  }
  public void setCod_escala(String cod_escala) {
    this.cod_escala = cod_escala;
  }
  public String getAtingimento() {
    return this.atingimento;
  }
  public void setAtingimento(String atingimento) {
    this.atingimento = atingimento;
  }
  public String getResult_conf_escala() {
    return this.result_conf_escala;
  }
  public void setResult_conf_escala(String result_conf_escala) {
    this.result_conf_escala = result_conf_escala;
  }
  public String getConceito_formula() {
    return this.conceito_formula;
  }
  public void setConceito_formula(String conceito_formula) {
    this.conceito_formula = conceito_formula;
  }
  public String getGrp_remuneracao() {
    return this.grp_remuneracao;
  }
  public void setGrp_remuneracao(String grp_remuneracao) {
    this.grp_remuneracao = grp_remuneracao;
  }
  public String getNumRealzPond() {
    return this.numRealzPond;
  }
  public void setNumRealzPond(String numRealzPond) {
    this.numRealzPond = numRealzPond;
  }
  public String getQtdSalReceber() {
    return this.qtdSalReceber;
  }
  public void setQtdSalReceber(String qtdSalReceber) {
    this.qtdSalReceber = qtdSalReceber;
  }
  public String getVerbaRh() {
    return this.verbaRh;
  }
  public void setVerbaRh(String verbaRh) {
    this.verbaRh = verbaRh;
  }
  public String getFormula() {
    return this.formula;
  }
  public void setFormula(String formula) {
    this.formula = formula;
  }
  public String getIndicador() {
    return this.indicador;
  }
  public void setIndicador(String indicador) {
    this.indicador = indicador;
  }
  public String getPeso() {
    return this.peso;
  }
  public void setPeso(String peso) {
    this.peso = peso;
  }
  public String getMeta() {
    return this.meta;
  }
  public void setMeta(String meta) {
    this.meta = meta;
  }
  public String getRelaizado() {
    return this.relaizado;
  }
  public void setRelaizado(String relaizado) {
    this.relaizado = relaizado;
  }
  public String getAtingimentoMeta() {
    return this.atingimentoMeta;
  }
  public void setAtingimentoMeta(String atingimentoMeta) {
    this.atingimentoMeta = atingimentoMeta;
  }
  public String getEscala() {
    return this.escala;
  }
  public void setEscala(String escala) {
    this.escala = escala;
  }
  public String getAtingimentoEscala() {
    return this.atingimentoEscala;
  }
  public void setAtingimentoEscala(String atingimentoEscala) {
    this.atingimentoEscala = atingimentoEscala;
  }
  public String getPesoResultado() {
    return this.pesoResultado;
  }
  public void setPesoResultado(String pesoResultado) {
    this.pesoResultado = pesoResultado;
  }
  public Integer getAno() {
    return this.ano;
  }
  public void setAno(Integer ano) {
    this.ano = ano;
  }
  public Integer getCodCargo() {
    return this.codCargo;
  }
  public void setCodCargo(Integer codCargo) {
    this.codCargo = codCargo;
  }
  public Integer getCodFil() {
    return this.codFil;
  }
  public void setCodFil(Integer codFil) {
    this.codFil = codFil;
  }
  public Integer getCodFunc() {
    return this.codFunc;
  }
  public void setCodFunc(Integer codFunc) {
    this.codFunc = codFunc;
  }
  public Date getDataDemissao() {
    return this.dataDemissao;
  }
  public void setDataDemissao(Date dataDemissao) {
    this.dataDemissao = dataDemissao;
  }
  public String getDescrCargo() {
    return this.descrCargo;
  }
  public void setDescrCargo(String descrCargo) {
    this.descrCargo = descrCargo;
  }
  public String getGrupoRemVar() {
    return this.grupoRemVar;
  }
  public void setGrupoRemVar(String grupoRemVar) {
    this.grupoRemVar = grupoRemVar;
  }
  public Integer getMes() {
    return this.mes;
  }
  public void setMes(Integer mes) {
    this.mes = mes;
  }
  public String getNomeFunc() {
    return this.nomeFunc;
  }
  public void setNomeFunc(String nomeFunc) {
    this.nomeFunc = nomeFunc;
  }
  public String getSituacaoRh() {
    return this.situacaoRh;
  }
  public void setSituacaoRh(String situacaoRh) {
    this.situacaoRh = situacaoRh;
  }
  public String getStatus() {
    return this.status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
}