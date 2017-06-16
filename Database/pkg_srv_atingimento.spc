create or replace package srv.pkg_srv_atingimento is

  /*-------------------------------------------------------------------------------------------------
  -- Author  : LEVY VILLAR
  -- Created : 11/04/2017
  -- Purpose : Package para calculo do atingimento por Filial (Indicadores CCM/SRV)
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/


 -- variaveis , constantes, etc.
 cn_car_bin number(10) := 603475;
 c_pto_virg constant char(1) := chr(59);
 type typ_cursor is ref cursor;

 -- variaveis gerais
 e_erro exception;
 v_data   date := sysdate;
 v_dt_ini varchar2(20);
 v_dt_fim varchar2(20);

 -- procs , funcs

 procedure prc_insere_realz_fil(p_rec_srv_realz_fil in srv_realizado_filial%rowtype,
                                p_cod_erro          out number,
                                p_descr_erro        out varchar2);

 procedure prc_calc_ating_fil_Lj(p_num_ano     in number,
                                 p_num_mes     in number,
                                 p_cod_emp     in number default null,
                                 p_cod_fil     in number default null,
                                 p_cod_indic   in number default null,
                                 p_descr_indic in varchar2 default null,
                                 p_cod_usuario in number,
                                 p_cod_erro    out number,
                                 p_descr_erro  out varchar2);

 procedure prc_calc_ating_fil_SAX(p_num_ano     in number,
                                  p_num_mes     in number,
                                  p_cod_emp     in number default null,
                                  p_cod_fil     in number default null,
                                  p_cod_indic   in number default null,
                                  p_descr_indic in varchar2 default null,
                                  p_cod_usuario in number,
                                  p_cod_erro    out number,
                                  p_descr_erro  out varchar2);

 procedure prc_calc_ating_CCenter(p_num_ano     in number,
                                  p_num_mes     in number,
                                  p_cod_emp     in number default null,
                                  p_cod_fil     in number default null,
                                  p_cod_indic   in number default null,
                                  p_cod_usuario in number,
                                  p_cod_erro    out number,
                                  p_descr_erro  out varchar2);

end pkg_srv_atingimento;
/