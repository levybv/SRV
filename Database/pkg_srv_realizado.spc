create or replace package srv.pkg_srv_realizado is

  /*-------------------------------------------------------------------------------------------------
  -- Author  : LEVY VILLAR
  -- Created : 11/04/2017
  -- Purpose    : Package para apuracao de realizado (Indicadores do CCM para o SRV)
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/

 -- variaveis , constantes, etc.

 -- variaveis gerais

 -- procs , funcs
 procedure Prc_Apura_Realizado(p_num_ano     in number,
                               p_num_mes     in number,
                               p_cod_erro    out number,
                               p_descr_erro  out varchar2);

 procedure Prc_Apura_RealizadoImportacao(p_num_ano     in number,
                                         p_num_mes     in number,
                                         p_cod_erro    out number,
                                         p_descr_erro  out varchar2);

end pkg_srv_realizado;
/
