create or replace package srv.pkg_srv_geral is

  /*-------------------------------------------------------------------------------------------------
  -- Author  : LEVY VILLAR
  -- Created : 11/04/2017
  -- Purpose : Package de operacoes padroes do SRV
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/

  function Fnc_Insere_Log_Processo(p_cod_proc  in number,
                                   p_nome_proc in varchar2,
                                   p_num_ano   in number,
                                   p_num_mes   in number,
                                   p_cod_fil   in number,
                                   p_cod_func  in number,
                                   p_cod_indic in number,
                                   p_erro      in varchar2) return varchar2;

  procedure Prc_Calc_Ponderacao(p_cod_indic       in number,
                                p_cod_grp_indic   in number,
                                p_cod_cargo       in number,
                                p_cod_grp_rem_var in number,
                                p_cod_pond        out number,
                                p_num_peso        out number,
                                p_cod_un_peso     out number,
                                p_vlr_premio      out number,
                                p_cod_erro        out number,
                                p_descr_erro      out varchar2);

  procedure Prc_Calc_Meta_Fil(p_cod_indic      in number,
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

  procedure Prc_Calc_Escala_Fx(p_cod_indic         in number,
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

  procedure Prc_Calc_Ponderacao_Tp_Fil(p_cod_indic       in number,
                                       p_cod_grp_indic   in number,
                                       p_cod_cargo       in number,
                                       p_cod_grp_rem_var in number,
                                       p_cod_tipo_fil    in number,
                                       p_cod_emp         in number,
                                       p_cod_fil         in number,
                                       p_cod_pond        out number,
                                       p_num_peso        out number,
                                       p_cod_un_peso     out number,
                                       p_vlr_premio      out number,
                                       p_cod_erro        out number,
                                       p_descr_erro      out varchar2);

  procedure Prc_Calc_Ponderacao_Tp_Fil2(p_cod_indic       in number,
                                        p_cod_grp_indic   in number,
                                        p_cod_cargo       in number,
                                        p_cod_grp_rem_var in number,
                                        p_cod_tipo_fil    in number,
                                        p_cod_emp         in number,
                                        p_cod_fil         in number,
                                        p_cod_pond        out number,
                                        p_num_peso        out number,
                                        p_cod_un_peso     out number,
                                        p_vlr_premio      out number,
                                        p_cod_erro        out number,
                                        p_descr_erro      out varchar2);

  procedure Prc_Insere_Realz_Func_Indic(p_rec_srv_realz_func_indic in srv_realizado_func_indicador%rowtype,
                                        p_cod_erro                 out number,
                                        p_descr_erro               out varchar2);

  procedure Prc_Ins_Realz_Func_Indic_UltFi(p_rec_srv_realz_func_indic in srv_realizado_func_indicador%rowtype,
                                           p_cod_erro                 out number,
                                           p_descr_erro               out varchar2);

end pkg_srv_geral;
/
