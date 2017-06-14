package br.com.marisa.srv.relatorio.vo;

@Deprecated
public class RelatorioLojaVO
{
  private Integer codFilial;
  private Integer codFunc;
  private Integer codIndic;
  private String nomeFuncionario;
  private String descrCargo;
  private String vlPremioFuncCalc;
  private String mes;
  private String ano;
  private String dataInicio;
  private String dataFim;

  public String getAno()
  {
    return this.ano;
  }
  public void setAno(String ano) {
    this.ano = ano;
  }
  public Integer getCodFilial() {
    return this.codFilial;
  }
  public void setCodFilial(Integer codFilial) {
    this.codFilial = codFilial;
  }
  public Integer getCodFunc() {
    return this.codFunc;
  }
  public void setCodFunc(Integer codFunc) {
    this.codFunc = codFunc;
  }
  public Integer getCodIndic() {
    return this.codIndic;
  }
  public void setCodIndic(Integer codIndic) {
    this.codIndic = codIndic;
  }
  public String getDescrCargo() {
    return this.descrCargo;
  }
  public void setDescrCargo(String descrCargo) {
    this.descrCargo = descrCargo;
  }
  public String getMes() {
    return this.mes;
  }
  public void setMes(String mes) {
    this.mes = mes;
  }
  public String getVlPremioFuncCalc() {
    return this.vlPremioFuncCalc;
  }
  public void setVlPremioFuncCalc(String vlPremioFuncCalc) {
    this.vlPremioFuncCalc = vlPremioFuncCalc;
  }
  public String getNomeFuncionario() {
    return this.nomeFuncionario;
  }
  public void setNomeFuncionario(String nomeFuncionario) {
    this.nomeFuncionario = nomeFuncionario;
  }
  public String getDataFim() {
    return this.dataFim;
  }
  public void setDataFim(String dataFim) {
    this.dataFim = dataFim;
  }
  public String getDataInicio() {
    return this.dataInicio;
  }
  public void setDataInicio(String dataInicio) {
    this.dataInicio = dataInicio;
  }
}