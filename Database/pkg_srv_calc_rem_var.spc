create or replace package srv.pkg_srv_calc_rem_var is

 /*-------------------------------------------------------------------------------------------------
 -- Author  : MICHELE.BETAZZI
 -- Created : 10/08/2011
 -- Purpose : Package processamento remuneracao variavel

 -- Author  : ROBERTO.NASCIMENTO
 -- Created : 09/12/2016
 -- Purpose : Inclusao do SAC e TLMKT
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
 procedure prc_calc_meta_fil(p_cod_indic      in number,
                             p_cod_emp        in number,
                             p_cod_fil        in number,
                             p_ano_meta       in number,
                             p_mes_meta       in number,
                             p_meta           out number,
                             p_cod_un_meta    out number,
                             p_vlr_premio_fil out number,
                             p_pct_calc_meta  out number,
                             p_cod_erro       out number,
                             p_descr_erro     out varchar2);

 procedure prc_calc_escala_fx(p_cod_indic         in number,
                              p_cod_grp_indic     in number,
                              p_cod_grp_rem_var   in number,
                              p_num_realz_x_meta  in number,
                              p_cod_escala        out number,
                              p_num_seq_escala_fx out number,
                              p_num_realz_fx      out number,
                              p_cod_un_realz_fx   out number,
                              p_flg_pct_100       out varchar2,
                              p_num_limite_fx     out number,
                              p_cod_erro          out number,
                              p_descr_erro        out varchar2);

 procedure prc_calc_ponderacao(p_cod_indic       in number,
                               p_cod_grp_indic   in number,
                               p_cod_cargo       in number,
                               p_cod_grp_rem_var in number,
                               p_cod_pond        out number,
                               p_num_peso        out number,
                               p_cod_un_peso     out number,
                               p_vlr_premio      out number,
                               p_cod_erro        out number,
                               p_descr_erro      out varchar2);

 procedure prc_insere_realz_fil(p_rec_srv_realz_fil in srv_realizado_filial%rowtype,
                                p_cod_erro          out number,
                                p_descr_erro        out varchar2);

 procedure prc_insere_realz_func_indic(p_rec_srv_realz_func_indic in srv_realizado_func_indicador%rowtype,
                                       p_cod_erro                 out number,
                                       p_descr_erro               out varchar2);

 procedure prc_calc_realz_fil_Lj(p_num_ano     in number,
                                 p_num_mes     in number,
                                 p_cod_emp     in number default null,
                                 p_cod_fil     in number default null,
                                 p_cod_indic   in number default null,
                                 p_descr_indic in varchar2 default null,
                                 p_cod_usuario in number,
                                 p_cod_erro    out number,
                                 p_descr_erro  out varchar2);

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

 procedure prc_calc_realz_Func_Indic_Lj(p_num_ano     in number,
                                        p_num_mes     in number,
                                        p_cod_emp     in number default null,
                                        p_cod_fil     in number default null,
                                        p_cod_indic   in number default null,
                                        p_descr_indic in varchar2 default null,
                                        p_cod_usuario in number,
                                        p_cod_erro    out number,
                                        p_descr_erro  out varchar2);

 procedure prc_calc_realz_Func_Indic_SAX(p_num_ano     in number,
                                         p_num_mes     in number,
                                         p_cod_emp     in number default null,
                                         p_cod_fil     in number default null,
                                         p_cod_indic   in number default null,
                                         p_descr_indic in varchar2 default null,
                                         p_cod_usuario in number,
                                         p_cod_erro    out number,
                                         p_descr_erro  out varchar2);

 procedure prc_calc_realz_Func_Indic_Corp(p_num_ano        in number,
                                          p_num_mes        in number,
                                          p_cod_fil        in number,
                                          p_cod_cargo      in number default null,
                                          p_cod_func       in number default null,
                                          p_sta_calc_realz in number default null,
                                          p_cod_usuario    in number,
                                          p_cod_erro       out number,
                                          p_descr_erro     out varchar2);

 procedure prc_calc_realz_Func_Indic_Adm(p_num_ano     in number,
                                         p_num_mes     in number,
                                         p_cod_cargo   in number default null,
                                         p_cod_func    in number default null,
                                         p_cod_usuario in number,
                                         p_cod_erro    out number,
                                         p_descr_erro  out varchar2);

 procedure prc_gera_relatorios(p_num_ano    in number,
                               p_num_mes    in number,
                               p_cod_rel    in number,
                               p_cod_erro   out number,
                               p_descr_erro out varchar2);

 procedure prc_calc_realz_fil_CCenter(p_num_ano     in number,
                                      p_num_mes     in number,
                                      p_cod_indic   in number default null,
                                      p_descr_indic in varchar2 default null,
                                      p_cod_usuario in number,
                                      p_cod_erro    out number,
                                      p_descr_erro  out varchar2);

 procedure prc_calc_ating_fil_CCenter(p_num_ano     in number,
                                      p_num_mes     in number,
                                      p_cod_emp     in number default null,
                                      p_cod_fil     in number default null,
                                      p_cod_indic   in number default null,
                                      p_descr_indic in varchar2 default null,
                                      p_cod_usuario in number,
                                      p_cod_erro    out number,
                                      p_descr_erro  out varchar2);

 procedure prc_calc_realz_Func_CCenter(p_num_ano     in number,
                                       p_num_mes     in number,
                                       p_cod_indic   in number default null,
                                       p_descr_indic in varchar2 default null,
                                       p_cod_usuario in number,
                                       p_cod_erro    out number,
                                       p_descr_erro  out varchar2);

 procedure prc_calc_realz_ppr(p_num_ano     in number,
                              p_num_mes     in number,
                              p_cod_emp     in number default null,
                              p_cod_fil     in number default null,
                              p_cod_indic   in number default null,
                              p_descr_indic in varchar2 default null,
                              p_cod_usuario in number,
                              p_cod_erro    out number,
                              p_descr_erro  out varchar2);

end pkg_srv_calc_rem_var;
/
