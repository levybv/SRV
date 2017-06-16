create or replace package body srv.pkg_srv_calc_resultado is

  /*-------------------------------------------------------------------------------------------------
  -- Author  : LEVY VILLAR
  -- Created : 11/04/2017
  -- Purpose : Package para calculo de resultado por funcionario (Indicadores SRV)
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/

   -------------------------------------------------------------------------------------------------*/
   v_ins_log_erro                   varchar2(4000);
   -- email
   v_body                           varchar2(4000);
   v_send_email                     number;

   -----------------------------------------------------------------------------------------------
   -- CALCULA REALIZADO FUNCIONARIO LJ
   -----------------------------------------------------------------------------------------------
   procedure prc_calc_realz_Func_Indic_Lj (p_num_ano      in number
                                          ,p_num_mes      in number
                                          ,p_cod_emp      in number     default null
                                          ,p_cod_fil      in number     default null
                                          ,p_cod_indic    in number     default null
                                          ,p_descr_indic  in varchar2   default null
                                          ,p_cod_usuario  in number
                                          ,p_cod_erro     out number
                                          ,p_descr_erro   out varchar2
                                           ) is

      -- FILIAIS
      cursor c_fil (p_cod_emp number
                   ,p_cod_fil number) is
         select distinct f.cod_emp
                        ,f.cod_fil
                        ,f.cod_tipo_fil
           from srv_filial               f
               ,srv_realizado_filial     r
          where f.cod_emp              = r.cod_emp
            and f.cod_fil              = r.cod_fil
            and f.flg_ativ             = 'S'
            --and f.cod_fil              >= 48
            and (f.cod_emp             = p_cod_emp or p_cod_emp is null)
            and (f.cod_fil             = p_cod_fil or p_cod_fil is null)
            and r.num_ano              = p_num_ano
            and r.num_mes              = p_num_mes
       order by f.cod_fil;


      -- Salario Base dos Funcionarios (Operacionais)
      cursor c_func_base_rem_var (p_cur_cod_emp           number
                                 ,p_cur_cod_fil           number
                                 ,p_cod_grp_rem_var       number) is
         select b.cod_func
               ,b.cod_cargo
               ,b.cod_fil
           from srv_funcionario                     b
               ,srv_cargo                           c
               ,srv_grupo_cargo                     d
               ,srv_grupo_rem_variavel              e
          where b.cod_cargo                       = c.cod_cargo
            and c.cod_cargo                       = d.cod_cargo
            and d.cod_grp_rem_var                 = e.cod_grp_rem_var
            and(b.cod_emp                         = p_cur_cod_emp          or p_cur_cod_emp is null)
            and(b.cod_fil                         = p_cur_cod_fil          or p_cur_cod_fil is null)
            and e.cod_grp_rem_var                 = p_cod_grp_rem_var
            and (nvl(c.flg_agrupa_fil_lider, 'N') = 'N')
             -- funcionario esteve empregado pelo menos 1 dia no mes
            and b.dt_admissao                    <= last_day(to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                       '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                       ' 23:59:59', 'dd/mm/yyyy hh24:mi:ss'))
            and (b.dt_demissao                    is null
                 OR
                 b.dt_demissao                    > to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                              '/' || trim(to_char(p_num_ano, '0000')) ||
                                                              ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                )
            -- situacao do funcionario
            and (b.cod_sit_rh                     = 1 -- Em Atividade
                 OR
                 nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                 OR
                   -- situacao atual Gozando Ferias OU
                   -- situacao anterior Gozando Ferias E
                   -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                 ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                  OR
                  (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                   and
                   nvl(b.dt_ini_sit_rh, to_date('01/' || trim(to_char(p_num_mes, '00'))||
                                                  '/' || trim(to_char(p_num_ano, '0000')) ||
                                                  ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss'))
                                                  >= to_date('02/' || trim(to_char(p_num_mes, '00'))   ||
                                                               '/' || trim(to_char(p_num_ano, '0000')) ||
                                                               ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                  )
                 )
                )
       order by b.cod_func
               ,b.cod_fil;


      -- cursor Cargos para os grupos de Remuneracao Variavel LIDERANCA
      cursor c_cargo_grp_rem_var (p_descr_grp_rem_var     varchar2
                                 ,p_flg_agrupa_fil_lider  varchar2) is
         select b.cod_cargo
               ,b.cod_grp_rem_var
           from srv_grupo_rem_variavel             a
               ,srv_grupo_cargo                    b
               ,srv_cargo                          c
          where a.cod_grp_rem_var                = b.cod_grp_rem_var
            and b.cod_cargo                      = c.cod_cargo
            and nvl(c.flg_agrupa_fil_lider, 'N') = p_flg_agrupa_fil_lider
            and a.descr_grp_rem_var              = p_descr_grp_rem_var
       order by b.cod_cargo;



      -- cursor dos funcionarios para o cargo LIDERANCA para a filial
      cursor c_func_cargo (p_cod_cargo              number
                          ,p_cur_cod_emp            number
                          ,p_cur_cod_fil            number
                          ,p_flg_agrupa_fil_lider   varchar2) is
         select b.cod_func
               ,b.cod_cargo
           from srv_cargo                         a
               ,srv_funcionario                   b
          where a.cod_cargo                      = b.cod_cargo
            and a.cod_cargo                      = p_cod_cargo
            and b.cod_emp                        = p_cur_cod_emp
            and b.cod_fil                        = p_cur_cod_fil
            and nvl(a.flg_agrupa_fil_lider, 'N') = p_flg_agrupa_fil_lider
            -- funcionario esteve empregado pelo menos 1 dia no mes
            and b.dt_admissao                    <= last_day(to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                       '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                       ' 23:59:59', 'dd/mm/yyyy hh24:mi:ss'))
            and (b.dt_demissao                    is null
                 OR
                 b.dt_demissao                    > to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                              '/' || trim(to_char(p_num_ano, '0000')) ||
                                                              ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                )
            -- situacao do funcionario
            and (b.cod_sit_rh                     = 1 -- Em Atividade
                 OR
                 nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                 OR
                   -- situacao atual Gozando Ferias OU
                   -- situacao anterior Gozando Ferias E
                   -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                 ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                   OR
                  (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                   and
                   nvl(b.dt_ini_sit_rh, to_date('01/' || trim(to_char(p_num_mes, '00'))||
                                                  '/' || trim(to_char(p_num_ano, '0000')) ||
                                                  ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss'))
                                                  >= to_date('02/' || trim(to_char(p_num_mes, '00'))   ||
                                                               '/' || trim(to_char(p_num_ano, '0000')) ||
                                                               ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                  )
                 )
                )
       order by b.cod_func;

      -- cursor dos funcionarios para o cargo LIDERANCA 2 Lojas
      cursor c_func_cargo_2_fil (p_cod_cargo              number
                                ,p_cur_cod_emp            number
                                ,p_cur_cod_fil            number
                                ,p_flg_agrupa_fil_lider   varchar2) is
         select b.cod_func
               ,b.cod_cargo
           from srv_gerente_2_lojas   a
               ,srv_funcionario       b
               ,srv_cargo             c
          where c.cod_cargo                      = b.cod_cargo
            and a.cod_func                       = b.cod_func
            and a.cod_fil                       <> b.cod_fil
            and c.cod_cargo                      = p_cod_cargo
            and b.cod_emp                        = p_cur_cod_emp
            and a.cod_fil                        = p_cur_cod_fil
            --and c.cod_atuacao                    = 1
            and nvl(c.flg_agrupa_fil_lider, 'N') = p_flg_agrupa_fil_lider
            -- funcionario esteve empregado pelo menos 1 dia no mes
            and b.dt_admissao                    <= last_day(to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                       '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                       ' 23:59:59', 'dd/mm/yyyy hh24:mi:ss'))
            and (b.dt_demissao                    is null
                 OR
                 b.dt_demissao                    > to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                              '/' || trim(to_char(p_num_ano, '0000')) ||
                                                              ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                )
            -- situacao do funcionario
            and (b.cod_sit_rh                     = 1 -- Em Atividade
                 OR
                 nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                 OR
                   -- situacao atual Gozando Ferias OU
                   -- situacao anterior Gozando Ferias E
                   -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                 ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                   OR
                  (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                   and
                   nvl(b.dt_ini_sit_rh, to_date('01/' || trim(to_char(p_num_mes, '00'))||
                                                  '/' || trim(to_char(p_num_ano, '0000')) ||
                                                  ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss'))
                                                  >= to_date('02/' || trim(to_char(p_num_mes, '00'))   ||
                                                               '/' || trim(to_char(p_num_ano, '0000')) ||
                                                               ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                  )
                 )
                )
       order by b.cod_func;

      -- Funcionarios LIDERANCA que recebem premio por 2 filiais
      -- fazer o calculo das outras filiais pelas quais que receberao premio
      -- pegar apenas as filiais diferentes da filial de cadastro
         cursor c_func_Lid_premio_2_fil (p_descr_indic        varchar2
                                        ,p_cod_fil            number
                                        ,p_descr_grp_rem_var  varchar2) is
           select a.cod_func          cod_func
                 ,b.cod_emp           cod_emp
                 ,a.cod_fil           cod_fil
                 ,b.cod_cargo         cod_cargo
                 ,e.cod_grp_rem_var   cod_grp_rem_var
                 ,f.num_meta          num_meta
                 ,f.num_realz         num_realz
                 ,f.qtd_realz         qtd_realz
             from srv_gerente_2_lojas                 a
                 ,srv_funcionario                     b
                 ,srv_cargo                           c
                 ,srv_grupo_cargo                     d
                 ,srv_grupo_rem_variavel              e
                 ,srv_realizado_filial                f
                 ,srv_indicador                       i
             where a.cod_func                        = b.cod_func
               and b.cod_cargo                       = c.cod_cargo
               and c.cod_cargo                       = d.cod_cargo
               and d.cod_grp_rem_var                 = e.cod_grp_rem_var
               and f.cod_emp                         = 1
               and a.cod_fil                         = p_cod_fil
               and a.cod_fil                        <> b.cod_fil
               --and a.cod_atuacao                     = 1
               and f.cod_fil                         = a.cod_fil
               and f.num_ano                         = p_num_ano
               and f.num_mes                         = p_num_mes
               and f.cod_indic                       = i.cod_indic
               and i.descr_indic                     = p_descr_indic
               and e.descr_grp_rem_var               = p_descr_grp_rem_var
               and (nvl(c.flg_agrupa_fil_lider, 'N') = 'N')
               -- funcionario esteve empregado pelo menos 1 dia no mes
               and b.dt_admissao                    <= last_day(to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                          '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                          ' 23:59:59', 'dd/mm/yyyy hh24:mi:ss'))
               and (b.dt_demissao                    is null
                    OR
                    b.dt_demissao                    > to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                 '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                 ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                   )
               -- situacao do funcionario
               and (b.cod_sit_rh                     = 1 -- Em Atividade
                    OR
                    nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                    OR
                      -- situacao atual Gozando Ferias OU
                      -- situacao anterior Gozando Ferias E
                      -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                    ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                      OR
                     (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                      and
                      nvl(b.dt_ini_sit_rh, to_date('01/' || trim(to_char(p_num_mes, '00'))||
                                                     '/' || trim(to_char(p_num_ano, '0000')) ||
                                                     ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss'))
                                                     >= to_date('02/' || trim(to_char(p_num_mes, '00'))   ||
                                                                  '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                  ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                     )
                    )
                   )
             order by a.cod_func;


      -- REMUNERACAO LOJAS
      cursor c_indic_rem_loja is
         select t.cod_tipo_rem_var
               ,t.descr_tipo_rem_var
               ,g.cod_grp_indic
               ,g.descr_grp_indic
               ,i.cod_indic
               ,i.descr_indic
           from srv_tipo_rem_var        t
               ,srv_grupo_indicador     g
               ,srv_indicador           i
          where t.cod_tipo_rem_var    = g.cod_tipo_rem_var
            and t.descr_tipo_rem_var  = 'REMUNERACAO_LOJA'
            and g.cod_grp_indic       = i.cod_grp_indic
            and i.cod_grp_indic       NOT IN (4,5,9) -- SAX e Seguro SAX
            and (i.cod_indic_sis       is null or i.cod_indic = 28)
            and (i.descr_indic        = p_descr_indic or p_descr_indic is null)
            and i.flg_indic_ativ      = 'S'
       order by g.cod_grp_indic
               ,i.cod_indic;


      -- AGRUPAMENTOS LIDERANCA (PSF E VENDAS)
      cursor c_indic_sistemico (p_cod_indic_sistemico varchar2) is
         select t.cod_tipo_rem_var
               ,t.descr_tipo_rem_var
               ,g.cod_grp_indic
               ,g.descr_grp_indic
               ,i.cod_indic
               ,i.descr_indic
           from srv_tipo_rem_var        t
               ,srv_grupo_indicador     g
               ,srv_indicador           i
          where t.cod_tipo_rem_var    = g.cod_tipo_rem_var
            and t.descr_tipo_rem_var  = 'REMUNERACAO_LOJA'
            and g.cod_grp_indic       = i.cod_grp_indic
            and i.cod_indic_sis       = p_cod_indic_sistemico
            and i.flg_indic_ativ      = 'S'
       order by g.cod_grp_indic
               ,i.cod_indic;


         -- LIDER REGIONAL / NACIONAL
         -- realizado e meta de todas as filiais do Lider Regional / Nacional pelo indicador
         -- usado para indicador VENDAS, VM e PSF
         cursor c_lid_reg_nac (p_descr_indic        varchar2
                              ,p_descr_grp_rem_var  varchar2) is
            select distinct a.cod_func                 cod_func
                  ,b.cod_emp                           cod_emp
                  ,b.cod_fil                           cod_fil
                  ,b.cod_cargo                         cod_cargo
                  ,e.cod_grp_rem_var                   cod_grp_rem_var
                  ,sum(f.num_meta)                     num_meta
                  ,sum(f.num_realz)                    num_realz
                  ,sum(f.qtd_realz)                    qtd_realz
              from srv_func_base_rem_var               a
                  ,srv_funcionario                     b
                  ,srv_cargo                           c
                  ,srv_grupo_cargo                     d
                  ,srv_grupo_rem_variavel              e
                  ,srv_realizado_filial                f
                  ,srv_indicador                       i
             where a.cod_func                        = b.cod_func
               and b.cod_cargo                       = c.cod_cargo
               and c.cod_cargo                       = d.cod_cargo
               and d.cod_grp_rem_var                 = e.cod_grp_rem_var
               and a.cod_emp                         = f.cod_emp
               and a.cod_fil                         = f.cod_fil
               and a.num_ano                         = f.num_ano
               and a.num_mes                         = f.num_mes
               and f.cod_indic                       = i.cod_indic
               and i.descr_indic                     = p_descr_indic
               and e.descr_grp_rem_var               = p_descr_grp_rem_var
               and a.num_ano                         = p_num_ano
               and a.num_mes                         = p_num_mes
               and (nvl(c.flg_agrupa_fil_lider, 'N') = 'S')
               -- funcionario esteve empregado pelo menos 1 dia no mes
               and b.dt_admissao                    <= last_day(to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                          '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                          ' 23:59:59', 'dd/mm/yyyy hh24:mi:ss'))
               and (b.dt_demissao                    is null
                    OR
                    b.dt_demissao                    > to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                 '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                 ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                   )
               -- situacao do funcionario
               and (b.cod_sit_rh                     = 1 -- Em Atividade
                    OR
                    nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                    OR
                      -- situacao atual Gozando Ferias OU
                      -- situacao anterior Gozando Ferias E
                      -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                    ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                      OR
                     (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                      and
                      nvl(b.dt_ini_sit_rh, to_date('01/' || trim(to_char(p_num_mes, '00'))||
                                                     '/' || trim(to_char(p_num_ano, '0000')) ||
                                                     ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss'))
                                                     >= to_date('02/' || trim(to_char(p_num_mes, '00'))   ||
                                                                  '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                  ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                     )
                    )
                   )
          group by a.cod_func
                  ,b.cod_emp
                  ,b.cod_fil
                  ,b.cod_cargo
                  ,e.cod_grp_rem_var
          order by a.cod_func;



      -- var controle
      v_var_sql                        varchar2(4000);

      -- variaveis
      v_cod_grp_rem_var               srv_grupo_rem_variavel.cod_grp_rem_var%type;

      v_vlr_premio_fil_calc           srv_realizado_filial.vlr_premio_fil_calc%type;
      v_meta                          srv_realizado_filial.num_meta%type;
      v_cod_un_meta                   srv_realizado_filial.cod_un_meta%type;
      v_num_realz_fil                 srv_realizado_filial.num_realz%type;
      v_cod_un_realz                  srv_realizado_filial.cod_un_realz%type;
      --v_num_realz                     srv_realizado_filial.num_realz%type;
      v_qtd_realz                     srv_realizado_filial.qtd_realz%type;
      v_num_realz_x_meta              srv_realizado_filial.num_realz_x_meta%type;
      v_cod_un_realz_x_meta           srv_realizado_filial.cod_un_realz_x_meta%type;
      v_num_realz_x_meta_limitador    srv_realizado_filial.num_realz_x_meta%type;
      v_vlr_premio_fil                srv_meta_filial.vlr_premio_fil%type;


      v_cod_pond                      srv_ponderacao.cod_pond%type;
      v_num_peso                      srv_ponderacao.num_peso%type;
      v_cod_un_peso                   srv_ponderacao.cod_un_peso%type;
      v_vlr_premio                    srv_ponderacao.vlr_premio%type;

      --v_pct_calc_rateio               srv_realizado_func_indicador.pct_calc_rateio%type;
      v_vlr_premio_func_calc          srv_realizado_func_indicador.vlr_premio_func_calc%type;
      v_num_realz_pond                srv_realizado_func_indicador.num_realz_pond%type;
      v_num_realz_func                srv_realizado_func_indicador.num_realz%type;
      v_num_realz_func_desc           srv_realizado_func_indicador.vlr_premio_func_calc%type;

      v_cod_func                      srv_funcionario.cod_func%type;
      v_cod_cargo_func                srv_funcionario.cod_cargo%type;
      v_cpf_func                      srv_funcionario.num_cpf_func%type;

      v_cod_escala                    srv_escala_faixa.cod_escala%type;
      v_num_seq_escala_fx             srv_escala_faixa.num_seq_escala_fx%type;
      v_num_realz_fx                  srv_escala_faixa.num_realz_fx%type;
      v_cod_un_realz_fx               srv_escala_faixa.cod_un_realz_fx%type;
      v_flg_pct_100                   srv_escala_faixa.flg_pct_100%type;
      v_num_limite_fx                 srv_escala_faixa.num_limite_fx%type;

      v_nm_tab_realz_fu               varchar2(30);
      v_nm_tab_realz_fi               varchar2(30);

      v_cod_grp_indic                 srv_grupo_indicador.cod_grp_indic%type;

      -- agrupamentos
      v_vr_soma_num_realz_pond_psf    srv_realizado_func_indicador.num_realz_pond%type;
      v_vr_soma_num_realz_pond_v_p    srv_realizado_func_indicador.num_realz_pond%type;
      vlr_soma_premio_v_p             srv_realizado_func_indicador.vlr_premio%type;
      vlr_soma_premio_func_calc_v_p   srv_realizado_func_indicador.vlr_premio_func_calc%type;
      --v_qtde_fil_pond                 number(11);

      -- var log
      --v_cod_emp                        srv_filial.cod_emp%type;
      v_cod_fil                        srv_filial.cod_fil%type;
      v_cod_indic                      srv_indicador.cod_indic%type;
      v_cod_cargo                      srv_cargo.cod_cargo%type;

      -- records
      rec_srv_realizado_func_indic     srv_realizado_func_indicador%rowtype;

      -- cursores
      cur_realz                        pkg_srv_calc_rem_var.typ_cursor;

      -- exception
      e_erro_cria_tab                  exception;

      --
      v_num_realz_pond_geral  number(10,4);
      v_vlr_premio_calc       number(10,4);
      v_num_peso_psf          number(10,4);


   begin

      v_data    := sysdate;
      --
      v_dt_ini  := '01/' || trim(to_char(p_num_mes, '00')) || '/' || trim(to_char(p_num_ano, '0000')) || ' ' || '00:00:00';
      --
      select to_char(last_day(to_date(('01/' || to_char(p_num_mes, '00') || '/'||to_char(p_num_ano, '0000')), 'dd/mm/yyyy')), 'dd/mm/yyyy')|| ' ' || '23:59:59'
        into v_dt_fim
        from dual;

      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      --Operacional
      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      Begin
        for r_fil in c_fil (p_cod_emp
                           ,p_cod_fil) loop

           v_cod_fil          := r_fil.cod_fil;

           -----------------------------------------------------------------------
           -- SELECIONA INDICADORES PARA O GRUPO DE REMUNERACAO LOJAS
           -----------------------------------------------------------------------
           for r_indic_rem_loja in c_indic_rem_loja loop

              v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
              v_cod_indic        := r_indic_rem_loja.cod_indic;

              --------------------------------------------------------
              -- SE INDICADOR VENDAS
              --------------------------------------------------------
              if r_indic_rem_loja.descr_indic = 'VENDAS' then

                -- seleciona vlr premio calculado da filial
                begin
                  select a.vlr_premio_fil_calc
                        ,a.num_meta
                        ,a.cod_un_meta
                        ,a.num_realz
                        ,a.cod_un_realz
                        ,a.qtd_realz
                        ,a.num_realz_x_meta
                        ,a.cod_un_realz_x_meta
                        ,c.vlr_premio_fil
                    into v_vlr_premio_fil_calc
                        ,v_meta
                        ,v_cod_un_meta
                        ,v_num_realz_fil
                        ,v_cod_un_realz
                        ,v_qtd_realz
                        ,v_num_realz_x_meta
                        ,v_cod_un_realz_x_meta
                        ,v_vlr_premio_fil
                    from srv_realizado_filial a
                        ,srv_indicador        b
                        ,srv_meta_filial      c
                   where a.cod_indic   = b.cod_indic
                     and a.cod_emp     = r_fil.cod_emp
                     and a.cod_fil     = r_fil.cod_fil
                     and b.descr_indic = 'VENDAS'
                     and a.num_ano     = p_num_ano
                     and a.num_mes     = p_num_mes
                     and a.cod_emp     = c.cod_emp
                     and a.cod_fil     = c.cod_fil
                     and a.cod_indic   = c.cod_indic
                     and a.num_ano     = c.num_ano
                     and a.num_mes     = c.num_mes;
               exception
                  when others then
                     v_vlr_premio_fil_calc := 0;
                     v_meta                := 0;
                     v_cod_un_meta         := null;
                     v_num_realz_fil       := 0;
                     v_cod_un_realz        := null;
                     v_qtd_realz           := 0;
                     v_num_realz_x_meta    := 0;
                     v_cod_un_realz_x_meta := null;
               end;

               -- selecionar a ponderacao para o grupo rem var e para o indicador
               p_cod_erro   := null;
               p_descr_erro := null;

               --CARGOS OPERACIONAIS - VENDAS
               begin
                 select a.cod_grp_rem_var
                   into v_cod_grp_rem_var
                   from srv_grupo_rem_variavel a
                  where a.descr_grp_rem_var = 'OPERACIONAL_LOJA';
               exception
                 when others then
                 v_cod_grp_rem_var := 5;
               end;

               PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => r_indic_rem_loja.cod_indic
                                   ,p_cod_grp_indic     => null
                                   ,p_cod_cargo         => null
                                   ,p_cod_grp_rem_var   => v_cod_grp_rem_var
                                   ,p_cod_pond          => v_cod_pond
                                   ,p_num_peso          => v_num_peso
                                   ,p_cod_un_peso       => v_cod_un_peso
                                   ,p_vlr_premio        => v_vlr_premio
                                   ,p_cod_erro          => p_cod_erro
                                   ,p_descr_erro        => p_descr_erro
                                   );

               if p_cod_erro is not null then
                 v_num_peso   := 0;
                 v_vlr_premio := 0;
               end if;

               p_cod_erro         := null;
               p_descr_erro       := null;

               --
               PKG_SRV_GERAL.Prc_Calc_Escala_Fx (null               --p_cod_indic
                                  ,null               --p_cod_grp_indic
                                  ,v_cod_grp_rem_var  --p_cod_grp_rem_var
                                  ,v_num_realz_x_meta --p_num_realz_x_meta
                                  ,v_cod_escala
                                  ,v_num_seq_escala_fx
                                  ,v_num_realz_fx
                                  ,v_cod_un_realz_fx
                                  ,v_flg_pct_100
                                  ,v_num_limite_fx
                                  ,p_cod_erro
                                  ,p_descr_erro
                                  );

               if p_cod_erro is not null then
                  v_num_realz_fx := 0;
               end if;

               -- verifica se a faixa deve seguir a proporcionalidade apos 100%
               -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
               if nvl(v_flg_pct_100, 'N') = 'S' and v_num_realz_x_meta > 100 then

                  v_num_realz_fx := v_num_realz_x_meta;

               end if;

               -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
               -- entao assumir o valor limite
               if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                  v_num_realz_fx := v_num_limite_fx;
               end if;

               -- calcula valor unitario para multiplicar pela qtde
               v_vlr_premio_func_calc := (v_vlr_premio * v_num_realz_fx)/100;

               begin
                 -- cursor funcionarios base rem var
                 for r_func_base_rem_var in c_func_base_rem_var (p_cur_cod_emp          => r_fil.cod_emp
                                                                ,p_cur_cod_fil          => r_fil.cod_fil
                                                                ,p_cod_grp_rem_var      => v_cod_grp_rem_var) loop


                    v_cod_func                                           := r_func_base_rem_var.cod_func;
                    rec_srv_realizado_func_indic.cod_func                := r_func_base_rem_var.cod_func;
                    rec_srv_realizado_func_indic.cod_cargo               := r_func_base_rem_var.cod_cargo;
                    rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                    rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                    rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                    rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                    rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                    rec_srv_realizado_func_indic.cod_escala              := null;--v_cod_escala;
                    rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                    rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                    rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                    rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                    rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                    rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                    rec_srv_realizado_func_indic.num_realz_pond          := null;
                    rec_srv_realizado_func_indic.cod_un_realz_pond       := null;

                    rec_srv_realizado_func_indic.num_meta                := v_meta;
                    rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                    rec_srv_realizado_func_indic.num_realz               := v_num_realz_fil;
                    rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                    rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                    rec_srv_realizado_func_indic.cod_un_realz_x_meta     := 2; -- unidade PERCENTUAL
                    rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;

                    rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                    rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                    rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                    rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                    rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                    rec_srv_realizado_func_indic.seg_fil                 := null;

                    -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                    prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                   ,p_cod_erro                  => p_cod_erro
                                                   ,p_descr_erro                => p_descr_erro
                                                    );

                 end loop;

                 commit;

               exception
                 when others then
                 null;
               end;

              --------------------------------------------------------
              -- SE INDICADORES DO GRUPO PSF_PROD_SERV_FINANCEIROS
              --------------------------------------------------------
              elsif r_indic_rem_loja.descr_grp_indic = 'PSF_PROD_SERV_FINANCEIROS' then

                 -- seleciona realz X meta e vlr premio calculado da filial
                 begin
                    select a.vlr_premio_fil_calc
                          ,a.num_meta
                          ,a.cod_un_meta
                          ,a.num_realz
                          ,a.cod_un_realz
                          ,a.qtd_realz
                          ,a.num_realz_x_meta
                          ,a.cod_un_realz_x_meta
                      into v_vlr_premio_fil_calc
                          ,v_meta
                          ,v_cod_un_meta
                          ,v_num_realz_fil
                          ,v_cod_un_realz
                          ,v_qtd_realz
                          ,v_num_realz_x_meta
                          ,v_cod_un_realz_x_meta
                      from srv_realizado_filial a
                          ,srv_indicador        b
                     where a.cod_indic   = b.cod_indic
                       and a.cod_emp     = r_fil.cod_emp
                       and a.cod_fil     = r_fil.cod_fil
                       and b.descr_indic = r_indic_rem_loja.descr_indic
                       and a.num_ano     = p_num_ano
                       and a.num_mes     = p_num_mes;
                 exception
                    when no_data_found then
                       v_vlr_premio_fil_calc := 0;
                       v_meta                := 0;
                       v_cod_un_meta         := null;
                       v_num_realz_fil       := 0;
                       v_cod_un_realz        := null;
                       v_qtd_realz           := 0;
                       v_num_realz_x_meta    := 0;
                       v_cod_un_realz_x_meta := null;

                    when others then
                       v_vlr_premio_fil_calc := 0;
                       v_meta                := 0;
                       v_cod_un_meta         := null;
                       v_num_realz_fil       := 0;
                       v_cod_un_realz        := null;
                       v_qtd_realz           := 0;
                       v_num_realz_x_meta    := 0;
                       v_cod_un_realz_x_meta := null;

                       --
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro ao selecionar a tabela srv_realizado_filial ' ||
                                         ' cod_fil -  '                                       || r_fil.cod_fil ||
                                         ' cod_indic -  '                                     || r_indic_rem_loja.descr_indic ||
                                         ' erro - '                                           || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;
               end;

               if r_indic_rem_loja.descr_indic in( 'CARTAO MARISA ATIVADO PL E ITAU'
                                                  ,'CARTAO MARISA TOMBAMENTO ITAUCARD'
                                                  ,'PARTICIPACAO DO CARTAO %'
                                                  ,'PROTECAO CELULAR/PROTECAO FINANCEIRA'
                                                  ,'COMPRA TRANQUILA PL E ITAU'
                                                  ,'CARTAO ADICIONAL PL E ITAU'
                                                  ,'VENDA COM JUROS PL E ITAU'
                                                  ,'BOLSA PROTEGIDA PL E ITAU'
                                                  ,'MARISA MULHER/AUTO PROTECAO/CASA PROTEGIDA'
                                                  ,'ASSISTENCIA ODONTOLOGICA LOJA') then

                 --------------------------------------------------------------------------
                 -- CARGOS OPERACIONAIS - GRUPO DE INDICADORES PSF_PROD_SERV_FINANCEIROS
                 --------------------------------------------------------------------------
                 begin

                    -- seleciona o cod grupo rem var para OPERACIONAL_LOJA
                    select a.cod_grp_rem_var
                      into v_cod_grp_rem_var
                      from srv_grupo_rem_variavel a
                     where a.descr_grp_rem_var = 'OPERACIONAL_LOJA';

                    -- selecionar a ponderacao para o grupo rem var e para o indicador
                    p_cod_erro         := null;
                    p_descr_erro       := null;
                    --
                    PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => r_indic_rem_loja.cod_indic
                                        ,p_cod_grp_indic     => null
                                        ,p_cod_cargo         => null
                                        ,p_cod_grp_rem_var   => v_cod_grp_rem_var
                                        ,p_cod_pond          => v_cod_pond
                                        ,p_num_peso          => v_num_peso
                                        ,p_cod_un_peso       => v_cod_un_peso
                                        ,p_vlr_premio        => v_vlr_premio
                                        ,p_cod_erro          => p_cod_erro
                                        ,p_descr_erro        => p_descr_erro
                                        );
                    --
                    if  p_cod_erro is not null then

                       --
                       v_num_peso   := 0;
                       v_vlr_premio := 0;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                      raise e_erro;

                    -- erro ao calcular ponderacao
                    end if;

                    --
                    p_cod_erro         := null;
                    p_descr_erro       := null;
                    --
                    PKG_SRV_GERAL.Prc_Calc_Escala_Fx (null                           --p_cod_indic
                                       ,r_indic_rem_loja.cod_grp_indic --p_cod_grp_indic
                                       ,null                           --p_cod_grp_rem_var
                                       ,v_num_realz_x_meta             --p_num_realz_x_meta
                                       ,v_cod_escala
                                       ,v_num_seq_escala_fx
                                       ,v_num_realz_fx
                                       ,v_cod_un_realz_fx
                                       ,v_flg_pct_100
                                       ,v_num_limite_fx
                                       ,p_cod_erro
                                       ,p_descr_erro
                                       );
                    if p_cod_erro is not null then

                       --
                       v_num_realz_fx := 0;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;

                    -- erro ao calcular escala
                    end if;

                    -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                    -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                    if (nvl(v_flg_pct_100, 'N') = 'S' and v_num_realz_x_meta > 100) then
                       --
                       v_num_realz_fx := v_num_realz_x_meta;
                    end if;

                    -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                    -- entao assumir o valor limite
                    if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                       --
                       v_num_realz_fx := v_num_limite_fx;
                    end if;

                    begin
                       -- calcula valor unitario para multiplicar pela qtde
                       v_num_realz_pond  := (v_vlr_premio * v_num_realz_fx)/100;
                    exception
                       when others then
                          p_cod_erro     := 1;
                          p_descr_erro   := 'Erro ao calcular num realz pond para indicadores PSF Cargos Operacionais' ||
                                            ' cod_fil:  '    || v_cod_fil ||
                                            ' cod_indic:  '  || v_cod_indic ||
                                              ' erro: '        || sqlerrm;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                    -- erro ao calcular vr unitario
                    end;

                    -- tabela gerada de apuracao realizado para o indicador
                    select a.nm_tab_realz_fil  ||'_'|| trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                          ,a.nm_tab_realz_func ||'_'|| trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                      into v_nm_tab_realz_fi
                          ,v_nm_tab_realz_fu
                      from srv_realizado_tab_tmp a
                     where a.cod_indic         = r_indic_rem_loja.cod_indic;

                    begin

                       -- selecionar todos os funcionarios desse cargo para essa filial e
                       v_var_sql :=              '   select b.cod_func '                      || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '         ,b.cod_cargo '                     || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '         ,a.cpf_vendedor '                  || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '         ,count(1) qtde '                   || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '     from SRV.' || v_nm_tab_realz_fu   || ' a ' || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '         ,srv_funcionario               b ' || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '    where a.cpf_vendedor = b.num_cpf_func ' || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '      and a.fil_cod = '    || r_fil.cod_fil || chr(13) || chr(10);

                       v_var_sql := v_var_sql || '
               -- funcionario esteve empregado pelo menos 1 dia no mes
              and b.dt_admissao                    <= last_day(to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'23:59:59'',''dd/mm/yyyy hh24:mi:ss''))
              and (b.dt_demissao                    is null
                   OR
                   b.dt_demissao                    > to_date(''01/'||trim(to_char(p_num_mes, '00'))||'/'||trim(to_char(p_num_ano, '0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss'')
                  )
              -- situacao do funcionario
              and (b.cod_sit_rh                     = 1 -- Em Atividade
                   OR
                   nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                   OR
                     -- situacao atual Gozando Ferias OU
                     -- situacao anterior Gozando Ferias E
                     -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                   ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                    OR
                    (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                     and
                     nvl(b.dt_ini_sit_rh, to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss''))
                                                    >= to_date(''02/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss'')
                    )
                   )
                  )' || chr(13) || chr(10);

                       v_var_sql := v_var_sql || 'group by b.cod_func '                       || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '        ,b.cod_cargo '                      || chr(13) || chr(10);
                       v_var_sql := v_var_sql || '        ,a.cpf_vendedor '                   || chr(13) || chr(10);

                       open cur_realz for v_var_sql;
                       loop
                          fetch cur_realz
                           into  v_cod_func
                                ,v_cod_cargo_func
                                ,v_cpf_func
                                ,v_num_realz_func;
                          --
                          exit when cur_realz%notfound;

                          --
                          v_cod_un_realz := 3; --unidade UNIDADE
                          --
                          v_vlr_premio_func_calc := (v_num_realz_func - nvl(v_num_realz_func_desc,0)) * v_num_realz_pond;

                          -- gravar na srv_realizado_func_indicador
                          rec_srv_realizado_func_indic.cod_func                := v_cod_func;
                          rec_srv_realizado_func_indic.cod_cargo               := v_cod_cargo_func;
                          rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                          rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                          rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                          rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                          rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                          rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                          rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                          rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                          rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                          rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                          rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                          rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                          rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                          rec_srv_realizado_func_indic.cod_un_realz_pond       := 1; -- unidade VALOR

                          rec_srv_realizado_func_indic.num_meta                := v_meta;
                          rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                          rec_srv_realizado_func_indic.num_realz               := v_num_realz_func - nvl(v_num_realz_func_desc,0);
                          rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                          rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                          rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                          rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                          rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                          rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                          rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                          rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                          rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;


                          -- inserir o realizado do funcionario pelo indicador
                          PKG_SRV_GERAL.Prc_Insere_Realz_Func_Indic (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                      ,p_cod_erro                  => p_cod_erro
                                                      ,p_descr_erro                => p_descr_erro
                                                       );
                          --
                          if p_cod_erro is not null then

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;

                          -- erro ao inserir realizado func indicador
                          end if;


                       -- cur_venda
                       end loop;
                       --
                       close cur_realz;

                       --
                       commit;
                    --
                    exception
                       /*when e_erro then
                          raise e_erro;*/
                       when others then
                          p_cod_erro     := 1;
                          p_descr_erro   := 'Erro ao inserir indicadores PSF Cargos Operacionais' ||
                                            ' num_ano: '     || p_num_ano   ||
                                            ' num_mes: '     || p_num_mes   ||
                                            ' cod_fil:  '    || v_cod_fil   ||
                                            ' cod_indic:  '  || v_cod_indic ||
                                            ' cod_func:  '   || v_cod_func  ||
                                            ' erro: '        || sqlerrm;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                    --
                    end;

                 exception
                   /* when e_erro then
                       raise e_erro;*/
                    when others then
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro Geral ao calcular indicadores PSF Cargos Operacionais' ||
                                         ' cod_fil:  '    || v_cod_fil   ||
                                         ' cod_indic:  '  || v_cod_indic ||
                                         ' erro: '        || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

                 --
                 end;

               end if;

              ---------------------------------------------
              -- if p_descr_indic = VENDAS, etc...
              ---------------------------------------------
              end if;

              -- commit por indicador
              commit;

           ---------------------------------------------
           -- c_indic_rem_loja
           ---------------------------------------------
           end loop;

           ---------------------------------------------
           -- commit por filial
           ---------------------------------------------
           commit;

        ---------------------------------------------
        -- c_fil
        ---------------------------------------------
        end loop;

        commit;

      Exception
        When others then
          null;

      end;

      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      -- Lider 1
      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      Begin
        for r_fil in c_fil (p_cod_emp
                           ,p_cod_fil) loop

           v_cod_fil          := r_fil.cod_fil;

           -----------------------------------------------------------------------
           -- SELECIONA INDICADORES PARA O GRUPO DE REMUNERACAO LOJAS
           -----------------------------------------------------------------------
           for r_indic_rem_loja in c_indic_rem_loja loop

              v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
              v_cod_indic        := r_indic_rem_loja.cod_indic;

              --------------------------------------------------------
              -- SE INDICADOR VENDAS
              --------------------------------------------------------
              if r_indic_rem_loja.descr_indic = 'VENDAS' then

                -- seleciona vlr premio calculado da filial
                begin
                  select a.vlr_premio_fil_calc
                        ,a.num_meta
                        ,a.cod_un_meta
                        ,a.num_realz
                        ,a.cod_un_realz
                        ,a.qtd_realz
                        ,a.num_realz_x_meta
                        ,a.cod_un_realz_x_meta
                        ,c.vlr_premio_fil
                    into v_vlr_premio_fil_calc
                        ,v_meta
                        ,v_cod_un_meta
                        ,v_num_realz_fil
                        ,v_cod_un_realz
                        ,v_qtd_realz
                        ,v_num_realz_x_meta
                        ,v_cod_un_realz_x_meta
                        ,v_vlr_premio_fil
                    from srv_realizado_filial a
                        ,srv_indicador        b
                        ,srv_meta_filial      c
                   where a.cod_indic   = b.cod_indic
                     and a.cod_emp     = r_fil.cod_emp
                     and a.cod_fil     = r_fil.cod_fil
                     and b.descr_indic = 'VENDAS'
                     and a.num_ano     = p_num_ano
                     and a.num_mes     = p_num_mes
                     and a.cod_emp     = c.cod_emp
                     and a.cod_fil     = c.cod_fil
                     and a.cod_indic   = c.cod_indic
                     and a.num_ano     = c.num_ano
                     and a.num_mes     = c.num_mes;
               exception
                  when others then
                     v_vlr_premio_fil_calc := 0;
                     v_meta                := 0;
                     v_cod_un_meta         := null;
                     v_num_realz_fil       := 0;
                     v_cod_un_realz        := null;
                     v_qtd_realz           := 0;
                     v_num_realz_x_meta    := 0;
                     v_cod_un_realz_x_meta := null;
               end;

               -- selecionar a ponderacao para o grupo rem var e para o indicador
               p_cod_erro   := null;
               p_descr_erro := null;

               ---------------------------------------------------------------
               -- CARGOS LIDERANCA - VENDAS
               ---------------------------------------------------------------
               begin
                  -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
                  for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                                 ,p_flg_agrupa_fil_lider => 'N') loop

                    v_cod_cargo           := r_cargo_grp_rem_var.cod_cargo;

                  -- selecionar a ponderacao para o cargo e para o indicador VENDAS
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => r_indic_rem_loja.cod_indic
                                      ,p_cod_grp_indic     => null
                                      ,p_cod_cargo         => r_cargo_grp_rem_var.cod_cargo
                                      ,p_cod_grp_rem_var   => null
                                      ,p_cod_pond          => v_cod_pond
                                      ,p_num_peso          => v_num_peso
                                      ,p_cod_un_peso       => v_cod_un_peso
                                      ,p_vlr_premio        => v_vlr_premio
                                      ,p_cod_erro          => p_cod_erro
                                      ,p_descr_erro        => p_descr_erro
                                      );
                  --
                  if  p_cod_erro is not null then

                     --
                     v_num_peso   := 0;
                     v_vlr_premio := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

  --                      raise e_erro;

                  -- erro ao calcular ponderacao
                  end if;

                  --
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic     --p_cod_indic
                                     ,null                           --p_cod_grp_indic
                                     ,null                           --p_cod_grp_rem_var
                                     ,v_num_realz_x_meta             --p_num_realz_x_meta
                                     ,v_cod_escala
                                     ,v_num_seq_escala_fx
                                     ,v_num_realz_fx
                                     ,v_cod_un_realz_fx
                                     ,v_flg_pct_100
                                     ,v_num_limite_fx
                                     ,p_cod_erro
                                     ,p_descr_erro
                                     );
                  if p_cod_erro is not null then

                     --
                     v_num_realz_x_meta := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

  --                     raise e_erro;

                  -- erro ao calcular escala
                  end if;

                  -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                  -- entao assumir o valor limite
                  if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_x_meta > v_num_limite_fx then

                     v_num_realz_x_meta := v_num_limite_fx;

                  end if;

                  begin
                    --
                    v_num_realz_pond := (v_num_realz_x_meta * v_num_peso)/100;
                    --
                  exception
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao calcular num realz pond para indicadores PSF Cargos Operacionais' ||
                                          ' cod_fil:  '    || v_cod_fil ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                            ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;

                  -- erro ao calcular vr unitario
                  end;

                    -- selecionar todos os funcionarios desse cargo para essa filial e
                    begin
                       for r_func_cargo in c_func_cargo (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                        ,p_cur_cod_emp          => r_fil.cod_emp
                                                        ,p_cur_cod_fil          => r_fil.cod_fil
                                                        ,p_flg_agrupa_fil_lider => 'N') loop

                           v_cod_func           := r_func_cargo.cod_func;

                           -- gravar na srv_realizado_func_indicador
                           rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                           rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                           rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                           rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                           rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                           rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                           rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                           rec_srv_realizado_func_indic.cod_escala              := null;
                           rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                           rec_srv_realizado_func_indic.num_realz_fx            := null;
                           rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                           rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                           rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                           rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                           rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                           rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                           rec_srv_realizado_func_indic.num_meta                := v_meta;
                           rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                           rec_srv_realizado_func_indic.num_realz               := v_num_realz_fil;
                           rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                           rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                           rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                           rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                           rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                           rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null;
                           rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                           rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                           rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                           rec_srv_realizado_func_indic.seg_fil                 := null;

                           -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                           prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                          ,p_cod_erro                  => p_cod_erro
                                                          ,p_descr_erro                => p_descr_erro
                                                           );

                           --
                           if p_cod_erro is not null then

                              -- enviar email
                              v_body       := p_cod_erro || ' - ' || p_descr_erro;

                              v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                      ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                      ,p_body    => v_body
                                                                                      );

                              -- logar tabela
                              v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                        ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                        ,p_num_ano     => p_num_ano
                                                                        ,p_num_mes     => p_num_mes
                                                                        ,p_cod_fil     => v_cod_fil
                                                                        ,p_cod_func    => v_cod_func
                                                                        ,p_cod_indic   => v_cod_indic
                                                                        ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                        );

                              if v_ins_log_erro is not null then
                                 -- enviar email
                                 v_body       := v_ins_log_erro;
                                 --
                                 v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                         ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                         ,p_body    => v_body
                                                                                         );
                              --v_ins_log_erro is not null
                              end if;

  --                              raise e_erro;

                           -- erro ao inserir realizado func indicador
                           end if;


                       -- c_func_cargo
                       end loop;

                    exception
                       /*when e_erro then
                          raise e_erro;*/
                       when others then
                          p_cod_erro     := 1;
                          p_descr_erro   := 'Erro ao inserir realizado funcionario Vendas Cargos Lideranca' ||
                                            ' cod_fil:  '    || v_cod_fil ||
                                            ' cod_indic:  '  || v_cod_indic ||
                                            ' cod_cargo:  '  || v_cod_cargo ||
                                            ' cod_func:  '   || v_cod_func ||
                                            ' erro: '        || sqlerrm;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                          raise e_erro;
                    end;

                  -- c_cargo_grp_rem_var
                  end loop;

                  --
                  commit;

               exception
                  when others then
                     p_cod_erro     := 1;
                     p_descr_erro   := 'Erro Geral ao calcular indicador Vendas para Cargos Lideranca ' ||
                                       ' cod_fil:  '    || v_cod_fil   ||
                                       ' cod_indic:  '  || v_cod_indic ||
                                       ' cod_cargo:  '  || v_cod_cargo ||
                                       ' cod_func:  '   || v_cod_func  ||
                                       ' erro: '        || sqlerrm;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

               end;

              --------------------------------------------------------
              -- SE INDICADORES DO GRUPO PSF_PROD_SERV_FINANCEIROS
              --------------------------------------------------------
              elsif r_indic_rem_loja.descr_grp_indic = 'PSF_PROD_SERV_FINANCEIROS' then

                 -- seleciona realz X meta e vlr premio calculado da filial
                 begin
                    select a.vlr_premio_fil_calc
                          ,a.num_meta
                          ,a.cod_un_meta
                          ,a.num_realz
                          ,a.cod_un_realz
                          ,a.qtd_realz
                          ,a.num_realz_x_meta
                          ,a.cod_un_realz_x_meta
                      into v_vlr_premio_fil_calc
                          ,v_meta
                          ,v_cod_un_meta
                          ,v_num_realz_fil
                          ,v_cod_un_realz
                          ,v_qtd_realz
                          ,v_num_realz_x_meta
                          ,v_cod_un_realz_x_meta
                      from srv_realizado_filial a
                          ,srv_indicador        b
                     where a.cod_indic   = b.cod_indic
                       and a.cod_emp     = r_fil.cod_emp
                       and a.cod_fil     = r_fil.cod_fil
                       and b.descr_indic = r_indic_rem_loja.descr_indic
                       and a.num_ano     = p_num_ano
                       and a.num_mes     = p_num_mes;
                 exception
                    when no_data_found then
                       v_vlr_premio_fil_calc := 0;
                       v_meta                := 0;
                       v_cod_un_meta         := null;
                       v_num_realz_fil       := 0;
                       v_cod_un_realz        := null;
                       v_qtd_realz           := 0;
                       v_num_realz_x_meta    := 0;
                       v_cod_un_realz_x_meta := null;

                    when others then
                       v_vlr_premio_fil_calc := 0;
                       v_meta                := 0;
                       v_cod_un_meta         := null;
                       v_num_realz_fil       := 0;
                       v_cod_un_realz        := null;
                       v_qtd_realz           := 0;
                       v_num_realz_x_meta    := 0;
                       v_cod_un_realz_x_meta := null;

                       --
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro ao selecionar a tabela srv_realizado_filial ' ||
                                         ' cod_fil -  '                                       || r_fil.cod_fil ||
                                         ' cod_indic -  '                                     || r_indic_rem_loja.descr_indic ||
                                         ' erro - '                                           || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;
                 end;

                 -----------------------------------------------------------------------
                 -- CARGOS LIDERANCA - GRUPO DE INDICADORES PSF_PROD_SERV_FINANCEIROS
                 -----------------------------------------------------------------------
                 begin
                    -- Limitador de Atingimento da Meta para Cargos Lideranca por indicador
                    p_cod_erro         := null;
                    p_descr_erro       := null;
                    --
                    PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic     --p_cod_indic
                                       ,null                           --p_cod_grp_indic
                                       ,null                           --p_cod_grp_rem_var
                                       ,v_num_realz_x_meta             --p_num_realz_x_meta
                                       ,v_cod_escala
                                       ,v_num_seq_escala_fx
                                       ,v_num_realz_fx
                                       ,v_cod_un_realz_fx
                                       ,v_flg_pct_100
                                       ,v_num_limite_fx
                                       ,p_cod_erro
                                       ,p_descr_erro
                                       );

                    -- armazena vr original de atingimento
                    v_num_realz_x_meta_limitador := v_num_realz_x_meta;

                    -- se nao houver escala de limitador cadastrada ou
                    -- se der erro manter o valor do atingimento original (sem limitador)
                    if p_cod_erro is not null then

                       --
                       v_num_realz_fx := v_num_realz_x_meta;

                       -- zera var de escala
                       v_cod_escala         := null;
                       v_num_seq_escala_fx  := null;
                       v_num_realz_fx       := null;
                       v_cod_un_realz_fx    := null;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;

                    -- se nao houve erro ao buscar escala
                    else
                       -- se o atingimento passou o valor do limitador, entao pegar o valor
                       -- do limitador como atingimento
                       if v_num_realz_x_meta > v_num_realz_fx then
                          --
                          v_num_realz_x_meta_limitador := v_num_realz_fx;

                       -- se o atingimento nao passou a fx limitador
                       else
                          v_num_realz_x_meta_limitador := v_num_realz_x_meta;

                          -- zera var de escala
                          v_cod_escala         := null;
                          v_num_seq_escala_fx  := null;
                          v_num_realz_fx       := null;
                          v_cod_un_realz_fx    := null;

                       end if;

                    -- erro ao calcular escala
                    end if;


                    begin
                       -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
                       for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                                      ,p_flg_agrupa_fil_lider => 'N') loop

                          -- selecionar a ponderacao para o grupo rem var ou cargo,
                          -- indicador e tipo filial e filial
                          p_cod_erro         := null;
                          p_descr_erro       := null;
                          --


                          PKG_SRV_GERAL.Prc_Calc_Ponderacao_Tp_Fil2(p_cod_indic         => r_indic_rem_loja.cod_indic
                                                     ,p_cod_grp_indic     => null
                                                     ,p_cod_cargo         => r_cargo_grp_rem_var.cod_cargo
                                                     ,p_cod_grp_rem_var   => null
                                                     ,p_cod_tipo_fil      => r_fil.cod_tipo_fil
                                                     ,p_cod_emp           => r_fil.cod_emp
                                                     ,p_cod_fil           => r_fil.cod_fil
                                                     ,p_cod_pond          => v_cod_pond
                                                     ,p_num_peso          => v_num_peso
                                                     ,p_cod_un_peso       => v_cod_un_peso
                                                     ,p_vlr_premio        => v_vlr_premio
                                                     ,p_cod_erro          => p_cod_erro
                                                     ,p_descr_erro        => p_descr_erro
                                                     );
                          --
                          if  p_cod_erro is not null then
                             --
                             v_num_peso   := 0;
                             v_vlr_premio := 0;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

        --                      raise e_erro;
                          -- erro ao calcular ponderacao
                          end if;


                          begin
                             -- realz X meta da filial (COM LIMITADOR SE HOUVER) multiplicar pelo peso

                             --v_num_realz_pond := (v_num_realz_x_meta * v_num_peso)/100;
                             v_num_realz_pond := (v_num_realz_x_meta_limitador * v_num_peso)/100;

                          exception
                             when others then
                                --
                                v_num_realz_pond := 0;

                                --
                                p_cod_erro     := 1;
                                p_descr_erro   := 'Erro ao calcular realz pond dos indicadores PSF Cargos Lideranca Loja' ||
                                                  ' cod_fil:  '    || v_cod_fil   ||
                                                  ' cod_indic:  '  || v_cod_indic ||
                                                  ' erro: '        || sqlerrm;

                                -- enviar email
                                v_body       := p_cod_erro || ' - ' || p_descr_erro;

                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                        ,p_body    => v_body
                                                                                        );

                                -- logar tabela
                                v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                          ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                          ,p_num_ano     => p_num_ano
                                                                          ,p_num_mes     => p_num_mes
                                                                          ,p_cod_fil     => v_cod_fil
                                                                          ,p_cod_func    => v_cod_func
                                                                          ,p_cod_indic   => v_cod_indic
                                                                          ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                          );

                                if v_ins_log_erro is not null then
                                   -- enviar email
                                   v_body       := v_ins_log_erro;
                                   --
                                   v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                           ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                           ,p_body    => v_body
                                                                                           );
                                --v_ins_log_erro is not null
                                end if;

        --                        raise e_erro;
                          -- erro ao calcular realz pond
                          end;


                        -- selecionar todos os funcionarios desse cargo para essa filial e cargo
                        for r_func_cargo in c_func_cargo (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                         ,p_cur_cod_emp          => r_fil.cod_emp
                                                         ,p_cur_cod_fil          => r_fil.cod_fil
                                                         ,p_flg_agrupa_fil_lider => 'N') loop

                             -- gravar na srv_realizado_func_indicador
                             rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                             rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                             rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                             rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                             rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                             rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                             rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                             rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                             rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                             rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                             rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                             rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                             rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                             rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                             rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                             rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                             rec_srv_realizado_func_indic.num_meta                := v_meta;
                             rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                             --
                             --ALTERADO ALEXANDRE BEZERRA
                             if r_indic_rem_loja.descr_indic in ('VENDA COM JUROS PL', 'VENDA COM JUROS ITAU') then
                                rec_srv_realizado_func_indic.num_realz            := v_qtd_realz;
                                rec_srv_realizado_func_indic.cod_un_realz         := 3;  --unidade UNIDADE v_cod_un_realz;
                             else
                                rec_srv_realizado_func_indic.num_realz            := v_num_realz_fil;
                                rec_srv_realizado_func_indic.cod_un_realz         := v_cod_un_realz;
                             end if;
                             --
                             rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                             rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta_limitador; --v_num_realz_x_meta;
                             rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                             rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                             rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                             rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null; -- unidade VALOR
                             rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                             rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                             rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                             rec_srv_realizado_func_indic.seg_fil                 := null;

                             -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                             prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                            ,p_cod_erro                  => p_cod_erro
                                                            ,p_descr_erro                => p_descr_erro
                                                             );

                             --
                             if p_cod_erro is not null then

                                -- enviar email
                                v_body       := p_cod_erro || ' - ' || p_descr_erro;

                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                        ,p_body    => v_body
                                                                                        );

                                -- logar tabela
                                v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                          ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                          ,p_num_ano     => p_num_ano
                                                                          ,p_num_mes     => p_num_mes
                                                                          ,p_cod_fil     => v_cod_fil
                                                                          ,p_cod_func    => v_cod_func
                                                                          ,p_cod_indic   => v_cod_indic
                                                                          ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                          );

                                if v_ins_log_erro is not null then
                                   -- enviar email
                                   v_body       := v_ins_log_erro;
                                   --
                                   v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                           ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                           ,p_body    => v_body
                                                                                           );
                                --v_ins_log_erro is not null
                                end if;

  --                              raise e_erro;
                             -- erro ao inserir realizado func indicador
                             end if;


                         -- c_func_cargo
                         end loop;

                       -- c_cargo_grp_rem_var
                       end loop;

                       --
                       commit;
                   --
                   exception
                       /*when e_erro then
                          raise e_erro;*/
                      when others then
                         p_cod_erro     := 1;
                         p_descr_erro   := 'Erro Geral ao inserir indicadores PSF Cargos Lideranca Loja' ||
                                           ' cod_fil:  '    || v_cod_fil   ||
                                           ' cod_indic:  '  || v_cod_indic ||
                                           ' cod_cargo:  '  || v_cod_cargo ||
                                           ' cod_func:  '   || v_cod_func  ||
                                           ' erro: '        || sqlerrm;

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                   --
                   end;
                 --
                 exception
                    /*when e_erro then
                       raise e_erro;*/
                    when others then
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro Geral ao calcular indicadores PSF Cargos Lideranca' ||
                                         ' cod_fil:  '    || v_cod_fil   ||
                                         ' cod_indic:  '  || v_cod_indic ||
                                         ' erro: '        || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

                 end;
              ---------------------------------------------------------
              -- if p_descr_indic = VENDAS, etc...
              ---------------------------------------------------------
              end if;

              -- commit por indicador
              commit;

           ------------------------------------------------------------
           -- c_indic_rem_loja
           ------------------------------------------------------------
           end loop;

           --
           commit;

           -----------------------------------------------------------------------
           -- CALCULAR O AGRUPAMENTO PSF
           -----------------------------------------------------------------------
           begin
              for r_indic_sistemico in c_indic_sistemico (p_cod_indic_sistemico => 'AGRUPA_PSF_LOJA_LID') loop

                 v_cod_grp_indic    := r_indic_sistemico.cod_grp_indic;
                 v_cod_indic        := r_indic_sistemico.cod_indic;

                 -----------------------------------------------------------------------------------
                 -- somar todos os pesos dos resultados dos PSF
                 -----------------------------------------------------------------------------------
                 -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
                 for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                                ,p_flg_agrupa_fil_lider => 'N') loop

                   -- selecionar todos os funcionarios desse cargo para essa filial, cargos e funcionarios
                   for r_func_cargo in c_func_cargo (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                    ,p_cur_cod_emp          => r_fil.cod_emp
                                                    ,p_cur_cod_fil          => r_fil.cod_fil
                                                    ,p_flg_agrupa_fil_lider => 'N') loop

                      v_cod_cargo := r_func_cargo.cod_cargo;
                      v_cod_func  := r_func_cargo.cod_func;

                      -- somar os resultados dos pesos das ponderacoes para todos os indicadores PSF
                      begin
                         select c.cod_grp_indic
                               ,sum(a.num_realz_pond)
                           into v_cod_grp_indic
                               ,v_vr_soma_num_realz_pond_psf
                           from srv_realizado_func_indicador a
                               ,srv_indicador                b
                               ,srv_grupo_indicador          c
                          where a.cod_indic                = b.cod_indic
                            and b.cod_grp_indic            = c.cod_grp_indic
                            and a.cod_func                 = r_func_cargo.cod_func
                            and a.cod_emp                  = r_fil.cod_emp
                            and a.cod_fil                  = r_fil.cod_fil
                            and a.num_ano                  = p_num_ano
                            and a.num_mes                  = p_num_mes
                            and c.descr_grp_indic          = 'PSF_PROD_SERV_FINANCEIROS'
                            and (b.cod_indic_sis            is null or cod_indic_sis ='SAX_PSF')
                       group by c.cod_grp_indic;
                      exception
                         when others then
                            v_cod_grp_indic               := 3;
                            v_vr_soma_num_realz_pond_psf  := 0;
                      end;

                      -- selecionar a ponderacao para o cargo e para o grupo indicador
                      PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                          ,p_cod_grp_indic     => r_indic_sistemico.cod_grp_indic
                                          ,p_cod_cargo         => r_cargo_grp_rem_var.cod_cargo
                                          ,p_cod_grp_rem_var   => null
                                          ,p_cod_pond          => v_cod_pond
                                          ,p_num_peso          => v_num_peso
                                          ,p_cod_un_peso       => v_cod_un_peso
                                          ,p_vlr_premio        => v_vlr_premio
                                          ,p_cod_erro          => p_cod_erro
                                          ,p_descr_erro        => p_descr_erro
                                          );
                      --
                      if p_cod_erro is not null then
                         --
                         v_num_peso   := 0;
                         v_vlr_premio := 0;

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                      -- erro ao calcular ponderacao
                      end if;

                      -- aplicar a ponderacao sobre a soma de todas as ponderacoes de todos os indicadores PSF
                      v_num_realz_pond := (v_vr_soma_num_realz_pond_psf * v_num_peso)/100;

                      -- gravar na srv_realizado_func_indicador
                      rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                      rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                      rec_srv_realizado_func_indic.cod_indic               := r_indic_sistemico.cod_indic;
                      rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                      rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                      rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                      rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                      rec_srv_realizado_func_indic.cod_escala              := null;
                      rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                      rec_srv_realizado_func_indic.num_realz_fx            := null;
                      rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                      rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                      rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                      rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                      rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                      rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                      rec_srv_realizado_func_indic.num_meta                := null;
                      rec_srv_realizado_func_indic.cod_un_meta             := null;
                      rec_srv_realizado_func_indic.num_realz               := v_vr_soma_num_realz_pond_psf; --null;
                      rec_srv_realizado_func_indic.cod_un_realz            := 2; -- unidade PERCENTUAL --null;
                      rec_srv_realizado_func_indic.num_realz_x_meta        := 0;
                      rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                      rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                      rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                      rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null; -- unidade VALOR
                      rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                      rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                      rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                      rec_srv_realizado_func_indic.seg_fil                 := null;

                      -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                      prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                     ,p_cod_erro                  => p_cod_erro
                                                     ,p_descr_erro                => p_descr_erro
                                                      );

                      --
                      if p_cod_erro is not null then

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                      -- erro ao inserir realizado func indicador
                      end if;

                    -- c_func_cargo
                   end loop;

                 -- c_cargo_grp_rem_var
                 end loop;

              -- c_indic_sistemico AGRUPAMENTO LIDER PSF
              end loop;

              -- commit por indicador
              commit;

           --
           exception
              /*when e_erro then
                 raise e_erro;*/
              when others then
                 p_cod_erro     := 1;
                 p_descr_erro   := 'Erro Geral ao calcular O AGRUPAMENTO PSF Lideranca Loja' ||
                                   ' cod_fil:  '    || v_cod_fil   ||
                                   ' cod_indic:  '  || v_cod_indic ||
                                   ' cod_func:  '   || v_cod_func ||
                                   ' erro: '        || sqlerrm;

                 -- enviar email
                 v_body       := p_cod_erro || ' - ' || p_descr_erro;

                 v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                         ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                         ,p_body    => v_body
                                                                         );

                 -- logar tabela
                 v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                           ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                           ,p_num_ano     => p_num_ano
                                                           ,p_num_mes     => p_num_mes
                                                           ,p_cod_fil     => v_cod_fil
                                                           ,p_cod_func    => v_cod_func
                                                           ,p_cod_indic   => v_cod_indic
                                                           ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                           );

                 if v_ins_log_erro is not null then
                    -- enviar email
                    v_body       := v_ins_log_erro;
                    --
                    v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                            ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                            ,p_body    => v_body
                                                                            );
                 --v_ins_log_erro is not null
                 end if;
           --
           end;


           -----------------------------------------------------------------------
           -- CALCULAR O AGRUPAMENTO REM LOJA LIDER
           -----------------------------------------------------------------------
           begin

              for r_indic_sistemico in c_indic_sistemico (p_cod_indic_sistemico => 'AGRUPA_REM_LOJA_LID') loop

                 v_cod_grp_indic    := r_indic_sistemico.cod_grp_indic;
                 v_cod_indic        := r_indic_sistemico.cod_indic;

                 -----------------------------------------------------------------------------------
                 -- somar todos os pesos dos resultados dos PSF
                 -----------------------------------------------------------------------------------
                 -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
                 for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                                ,p_flg_agrupa_fil_lider => 'N') loop

                    -- selecionar todos os funcionarios desse cargo para essa filial, cargos e funcionarios
                    for r_func_cargo in c_func_cargo (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                     ,p_cur_cod_emp          => r_fil.cod_emp
                                                     ,p_cur_cod_fil          => r_fil.cod_fil
                                                     ,p_flg_agrupa_fil_lider => 'N') loop


                      v_cod_cargo := r_func_cargo.cod_cargo;
                      v_cod_func  := r_func_cargo.cod_func;

                       -- somar os indicadores VENDAS E AGRUPAMENTO PSF
                       begin
                          select sum(a.num_realz_pond)        vr_soma_num_realz_pond_v_p
                                ,sum(a.vlr_premio)            vlr_soma_premio_v_p
                                ,sum(a.vlr_premio_func_calc)  vlr_soma_premio_func_calc_v_p
                            into v_vr_soma_num_realz_pond_v_p
                                ,vlr_soma_premio_v_p
                                ,vlr_soma_premio_func_calc_v_p
                            from srv_realizado_func_indicador a
                                ,srv_indicador                b
                           where a.cod_indic                = b.cod_indic
                             and a.cod_emp                  = r_fil.cod_emp
                             and a.cod_fil                  = r_fil.cod_fil
                             and a.cod_func                 = r_func_cargo.cod_func
                             and a.num_ano                  = p_num_ano
                             and a.num_mes                  = p_num_mes
                             and b.descr_indic              in ('VENDAS', 'AGRUPAMENTO PSF_LOJA LIDER');
                       --
                       exception
                          when others then
                             --
                             v_vr_soma_num_realz_pond_v_p  := 0;
                             vlr_soma_premio_v_p           := 0;
                             vlr_soma_premio_func_calc_v_p := 0;

                             --
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao selecionar somatoria dos realz pond no agrupameento Rem Loja Lider ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;


                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       end;

                       -- aplicar a escala do indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       p_cod_erro         := null;
                       p_descr_erro       := null;
                       --
                       PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_sistemico.cod_indic     -- p_cod_indic
                                          ,null                            -- p_cod_grp_indic
                                          ,null                            -- p_cod_grp_rem_var
                                          ,v_vr_soma_num_realz_pond_v_p
                                          ,v_cod_escala
                                          ,v_num_seq_escala_fx
                                          ,v_num_realz_fx
                                          ,v_cod_un_realz_fx
                                          ,v_flg_pct_100
                                          ,v_num_limite_fx
                                          ,p_cod_erro
                                          ,p_descr_erro
                                          );
                       --
                       if p_cod_erro is not null then
                          --
                          v_num_realz_fx := 0;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                       -- erro ao calcular escala
                       end if;

                       -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                       -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                       if (nvl(v_flg_pct_100, 'N') = 'S' and v_vr_soma_num_realz_pond_v_p > 100) then
                          --
                          v_num_realz_fx := v_vr_soma_num_realz_pond_v_p;
                       end if;

                       -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                       -- entao assumir o valor limite
                       if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                          --
                          v_num_realz_fx := v_num_limite_fx;
                       end if;

                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao
                       -- para o cargo e indicador VENDAS
                       -- gravar o vlr premio calc na realz func para o indicador VENDAS
                       begin
                          update srv_realizado_func_indicador a
                             set a.vlr_premio_func_calc = (a.vlr_premio * v_num_realz_fx)/100
                           where a.cod_indic            = (select cod_indic
                                                             from srv_indicador
                                                            where descr_indic = 'VENDAS')
                             and a.cod_func             = r_func_cargo.cod_func
                             and a.cod_emp              = r_fil.cod_emp
                             and a.cod_fil              = r_fil.cod_fil
                             and a.num_ano              = p_num_ano
                             and a.num_mes              = p_num_mes;
                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao atualizar na srv_realizado_func_indicador o vlr premio calc para o indicador VENDAS - Agrupamento Rem Loja Lider' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao - VENDAS
                       end;

                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao
                       -- para o cargo e grupo de indicadores PSF
                       -- gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO PSF_LOJA LIDER
                       begin
                          update srv_realizado_func_indicador a
                             set a.vlr_premio_func_calc = (a.vlr_premio * v_num_realz_fx)/100
                           where a.cod_indic            = (select cod_indic
                                                             from srv_indicador
                                                            where descr_indic = 'AGRUPAMENTO PSF_LOJA LIDER')
                             and a.cod_func             = r_func_cargo.cod_func
                             and a.cod_emp              = r_fil.cod_emp
                             and a.cod_fil              = r_fil.cod_fil
                             and a.num_ano              = p_num_ano
                             and a.num_mes              = p_num_mes;
                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao atualizar na srv_realizado_func_indicador o vlr premio calc npara o indicador AGRUPAMENTO PSF_LOJA LIDER ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao - PSF
                       end;

                       -- gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       begin
                          vlr_soma_premio_func_calc_v_p := (vlr_soma_premio_v_p * v_num_realz_fx)/100;
                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao calcular o vlr premio calc para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       end;

                       -- inserir o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       -- gravar na srv_realizado_func_indicador
                       rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                       rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                       rec_srv_realizado_func_indic.cod_indic               := r_indic_sistemico.cod_indic;
                       rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                       rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                       rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                       rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                       rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                       rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                       rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                       rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                       rec_srv_realizado_func_indic.cod_pond                := null;
                       rec_srv_realizado_func_indic.num_peso                := null;
                       rec_srv_realizado_func_indic.cod_un_peso             := null;
                       rec_srv_realizado_func_indic.num_realz_pond          := v_vr_soma_num_realz_pond_v_p;
                       rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                       rec_srv_realizado_func_indic.num_meta                := null;
                       rec_srv_realizado_func_indic.cod_un_meta             := null;
                       rec_srv_realizado_func_indic.num_realz               := null;
                       rec_srv_realizado_func_indic.cod_un_realz            := null;
                       rec_srv_realizado_func_indic.num_realz_x_meta        := 0;
                       rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                       rec_srv_realizado_func_indic.vlr_premio              := vlr_soma_premio_v_p;
                       rec_srv_realizado_func_indic.vlr_premio_func_calc    := vlr_soma_premio_func_calc_v_p;
                       rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                       rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                       rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                       rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                       rec_srv_realizado_func_indic.seg_fil                 := null;

                       -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                       prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                      ,p_cod_erro                  => p_cod_erro
                                                      ,p_descr_erro                => p_descr_erro
                                                       );

                       --
                       if p_cod_erro is not null then

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

                       --raise e_erro;
                       --erro ao inserir realizado func indicador
                       end if;

                       --################################################
                       --Calcula Valor por Incicador
                       --################################################
                       Begin

                         begin
                           select (num_peso / 100)
                             into v_num_peso_psf
                             from srv_realizado_func_indicador
                            where num_ano   = p_num_ano
                              and num_mes   = p_num_mes
                              and cod_func  = r_func_cargo.cod_func
                              and cod_fil   = r_fil.cod_fil
                              and cod_indic = 39;
                         exception
                           when others then
                             v_num_peso_psf := 0;
                         end;
                         --
                         begin
                           select vlr_premio, num_realz_fx, num_realz_pond
                             into v_vlr_premio, v_num_realz_fx, v_num_realz_pond_geral
                             from srv_realizado_func_indicador
                            where num_ano   = p_num_ano
                              and num_mes   = p_num_mes
                              and cod_func  = r_func_cargo.cod_func
                              and cod_fil   = r_fil.cod_fil
                              and cod_indic = 40;
                         exception
                           when others then
                             v_vlr_premio := 0;
                             v_num_realz_fx := 0;
                             v_num_realz_pond_geral := 0;
                         end;
                         --
                         begin
                           for i in (select a.*
                                       from srv_realizado_func_indicador a
                                      where a.num_ano = p_num_ano
                                        and a.num_mes = p_num_mes
                                        and cod_func  = r_func_cargo.cod_func
                                        and cod_fil   = r_fil.cod_fil
                                        and a.cod_indic not in (39,40)) loop

                             if i.cod_indic = 1 then
                               --VENDAS
                               select nvl(num_realz_pond,0)
                                 into v_num_realz_pond
                                 from srv_realizado_func_indicador
                                where cod_func  = i.cod_func
                                  and num_ano   = i.num_ano
                                  and num_mes   = i.num_mes
                                  and cod_fil   = i.cod_fil
                                  and cod_indic = 1;

                             else
                               --PSF/EP
                               select nvl((num_realz_pond * v_num_peso_psf),0)
                                 into v_num_realz_pond
                                 from srv_realizado_func_indicador
                                where cod_func  = i.cod_func
                                  and num_ano   = i.num_ano
                                  and num_mes   = i.num_mes
                                  and cod_fil   = i.cod_fil
                                  and cod_indic = i.cod_indic;

                             end if;
                             --
                             begin
                               v_num_realz_pond := (v_num_realz_pond / v_num_realz_pond_geral) * v_num_realz_fx;
                             exception
                               when others then
                                 v_num_realz_pond := 0;
                             end;
                             --
                             begin
                               v_vlr_premio_calc := (v_vlr_premio * v_num_realz_pond) / 100;
                             exception
                               when others then
                                 v_vlr_premio_calc := 0;
                             end;
                             --
                             begin
                               update srv_realizado_func_indicador
                                  set vlr_premio_func_calc = v_vlr_premio_calc
                                where cod_func  = i.cod_func
                                  and num_ano   = i.num_ano
                                  and num_mes   = i.num_mes
                                  and cod_fil   = i.cod_fil
                                  and cod_indic = i.cod_indic;
                             exception
                               when others then
                                 null;
                             end;
                             --
                           end loop;

                          exception
                           when others then
                             null;

                         end;
                         --
                         Commit;
                         --
                       Exception
                         When Others Then
                           null;

                       End;
                       --################

                    -- c_func_cargo
                    end loop;

                 -- c_cargo_grp_rem_var
                 end loop;

                 -- commit por indicador
                 commit;

              -- c_indic_sistemico AGRUPAMENTO LOJA LIDER
              end loop;

           --
           exception
              /*when e_erro then
                 raise e_erro;*/
              when others then
                 p_cod_erro     := 1;
                 p_descr_erro   := 'Erro Geral ao calcular O AGRUPAMENTO LOJA LIDER Lideranca Loja' ||
                                   ' cod_fil:  '    || v_cod_fil   ||
                                   ' cod_indic:  '  || v_cod_indic ||
                                   ' cod_func:  '   || v_cod_func ||
                                   ' erro: '        || sqlerrm;

                 -- enviar email
                 v_body       := p_cod_erro || ' - ' || p_descr_erro;

                 v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                         ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                         ,p_body    => v_body
                                                                         );

                 -- logar tabela
                 v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                           ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                           ,p_num_ano     => p_num_ano
                                                           ,p_num_mes     => p_num_mes
                                                           ,p_cod_fil     => v_cod_fil
                                                           ,p_cod_func    => v_cod_func
                                                           ,p_cod_indic   => v_cod_indic
                                                           ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                           );

                 if v_ins_log_erro is not null then
                    -- enviar email
                    v_body       := v_ins_log_erro;
                    --
                    v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                            ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                            ,p_body    => v_body
                                                                            );
                 --v_ins_log_erro is not null
                 end if;


           --
           end;


           ---------------------------------------------
           -- commit por filial
           ---------------------------------------------
           commit;

        ------------------------------------------------
        -- c_fil
        ------------------------------------------------
        end loop;

        commit;

      exception
        when others then
          null;
      end;

      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      -- CALCULO DO PREMIO DOS GERENTES POR 2 LOJAS
      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      Begin
        for r_fil in c_fil (p_cod_emp
                           ,p_cod_fil) loop

           v_cod_fil          := r_fil.cod_fil;

          for r_indic_rem_loja in c_indic_rem_loja loop

             v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
             v_cod_indic        := r_indic_rem_loja.cod_indic;

             --------------------------------------------------------
             -- SE INDICADOR VENDAS
             --------------------------------------------------------
             if r_indic_rem_loja.descr_indic = 'VENDAS' then

                -- LIDERANCA_LOJA
                -- realizado e meta de todas as filiais do Lider Regional / Nacional pelo indicador
                begin
                   for r_lid_reg_nac in c_func_Lid_premio_2_fil (p_descr_indic       => r_indic_rem_loja.descr_indic
                                                                ,p_cod_fil           => v_cod_fil
                                                                ,p_descr_grp_rem_var => 'LIDERANCA_LOJA') loop

                       v_cod_cargo := r_lid_reg_nac.cod_cargo;
                       v_cod_func  := r_lid_reg_nac.cod_func;

                      ---------------------------------------------------------------
                      -- CARGOS LIDERANCA - VENDAS
                      ---------------------------------------------------------------
                      if r_lid_reg_nac.num_meta > 0 then
                         v_num_realz_x_meta := (r_lid_reg_nac.num_realz / r_lid_reg_nac.num_meta) * 100;
                      else
                         v_num_realz_x_meta := 100; --r_lid_reg_nac.num_realz;
                      end if;
                      v_cod_un_meta         := 1; -- unidade VALOR
                      v_cod_un_realz        := 1; -- unidade VALOR
                      v_cod_un_realz_x_meta := 2; -- unidade PERCENTUAL

                      -- selecionar a ponderacao para o cargo e para o indicador VENDAS
                      PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => r_indic_rem_loja.cod_indic
                                          ,p_cod_grp_indic     => null
                                          ,p_cod_cargo         => r_lid_reg_nac.cod_cargo
                                          ,p_cod_grp_rem_var   => null
                                          ,p_cod_pond          => v_cod_pond
                                          ,p_num_peso          => v_num_peso
                                          ,p_cod_un_peso       => v_cod_un_peso
                                          ,p_vlr_premio        => v_vlr_premio
                                          ,p_cod_erro          => p_cod_erro
                                          ,p_descr_erro        => p_descr_erro
                                          );

                      if p_cod_erro is not null then
                        v_num_peso   := 0;
                        v_vlr_premio := 0;
                      end if;

                      PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic     --p_cod_indic
                                         ,null                           --p_cod_grp_indic
                                         ,null                           --p_cod_grp_rem_var
                                         ,v_num_realz_x_meta             --p_num_realz_x_meta
                                         ,v_cod_escala
                                         ,v_num_seq_escala_fx
                                         ,v_num_realz_fx
                                         ,v_cod_un_realz_fx
                                         ,v_flg_pct_100
                                         ,v_num_limite_fx
                                         ,p_cod_erro
                                         ,p_descr_erro
                                         );

                      if p_cod_erro is not null then
                         --
                         v_num_realz_x_meta := 0;

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                      end if;

                      -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                      -- entao assumir o valor limite
                      if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_x_meta > v_num_limite_fx then

                         v_num_realz_x_meta := v_num_limite_fx;

                      end if;

                      -- realz X meta da filial multiplicar pelo peso (referente a vendas)
                      v_num_realz_pond := (v_num_realz_x_meta * v_num_peso)/100;

                      -- gravar na srv_realizado_func_indicador
                      rec_srv_realizado_func_indic.cod_func                := r_lid_reg_nac.cod_func;
                      rec_srv_realizado_func_indic.cod_cargo               := r_lid_reg_nac.cod_cargo;
                      rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                      rec_srv_realizado_func_indic.cod_emp                 := r_lid_reg_nac.cod_emp;
                      rec_srv_realizado_func_indic.cod_fil                 := r_lid_reg_nac.cod_fil;
                      rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                      rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                      rec_srv_realizado_func_indic.cod_escala              := null;
                      rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                      rec_srv_realizado_func_indic.num_realz_fx            := null;
                      rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                      rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                      rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                      rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                      rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                      rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                      rec_srv_realizado_func_indic.num_meta                := r_lid_reg_nac.num_meta;
                      rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                      rec_srv_realizado_func_indic.num_realz               := r_lid_reg_nac.num_realz;
                      rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                      rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                      rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                      rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                      rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                      rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null;
                      rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                      rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                      rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                      rec_srv_realizado_func_indic.seg_fil                 := 'S';

                      -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                      prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                     ,p_cod_erro                  => p_cod_erro
                                                     ,p_descr_erro                => p_descr_erro
                                                      );

                      --
                      if p_cod_erro is not null then

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );
                      end if;

                   end loop;
                   --
                   commit;

                --
                exception
                   when others then
                      p_cod_erro     := 1;
                      p_descr_erro   := 'Erro Geral ao calcular realizado e meta de todas as filiais do Lider Regional / Nacional LIDERANCA LOJA para o indicador VENDAS ' ||
                                        ' cod_fil:  '    || v_cod_fil   ||
                                        ' cod_indic:  '  || v_cod_indic ||
                                        ' cod_cargo:  '  || v_cod_cargo ||
                                        ' cod_func:  '   || v_cod_func  ||
                                        ' erro: '        || sqlerrm;

                      -- enviar email
                      v_body       := p_cod_erro || ' - ' || p_descr_erro;

                      v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                              ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                              ,p_body    => v_body
                                                                              );

                      -- logar tabela
                      v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                ,p_num_ano     => p_num_ano
                                                                ,p_num_mes     => p_num_mes
                                                                ,p_cod_fil     => v_cod_fil
                                                                ,p_cod_func    => v_cod_func
                                                                ,p_cod_indic   => v_cod_indic
                                                                ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                );

                      if v_ins_log_erro is not null then
                         -- enviar email
                         v_body       := v_ins_log_erro;
                         --
                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                 ,p_body    => v_body
                                                                                 );
                      --v_ins_log_erro is not null
                      end if;
                --
                end;

             --------------------------------------------------------
             -- SE INDICADORES DO GRUPO PSF_PROD_SERV_FINANCEIROS
             --------------------------------------------------------
             elsif r_indic_rem_loja.descr_grp_indic = 'PSF_PROD_SERV_FINANCEIROS' then

                -- LIDERANCA_LOJA
                -- realizado e meta de todas as filiais do Lider Regional / Nacional pelo indicador
                begin
                   for r_lid_reg_nac in c_func_Lid_premio_2_fil (p_descr_indic       => r_indic_rem_loja.descr_indic
                                                                ,p_cod_fil           => v_cod_fil
                                                                ,p_descr_grp_rem_var => 'LIDERANCA_LOJA') loop

                      v_cod_cargo := r_lid_reg_nac.cod_cargo;
                      v_cod_func  := r_lid_reg_nac.cod_func;


                      -----------------------------------------------------------------------
                      -- CARGOS LIDERANCA - GRUPO DE INDICADORES PSF_PROD_SERV_FINANCEIROS
                      -----------------------------------------------------------------------
                      if r_lid_reg_nac.num_meta > 0 then
                         v_num_realz_x_meta := (r_lid_reg_nac.num_realz / r_lid_reg_nac.num_meta) * 100;
                      else
                         v_num_realz_x_meta := 100; --r_lid_reg_nac.num_realz;
                      end if;
                      if r_indic_rem_loja.descr_indic in ('VENDA COM JUROS PL', 'VENDA COM JUROS ITAU') then
                         v_cod_un_meta         := 1; -- unidade VALOR
                         v_cod_un_realz        := 1; -- unidade VALOR
                      else
                         v_cod_un_meta         := 3; -- unidade UNIDADE
                         v_cod_un_realz        := 3; -- unidade UNIDADE
                      end if;
                      v_cod_un_realz_x_meta    := 2; -- unidade PERCENTUAL

                      -- Limitador de Atingimento da Meta para Cargos Lideranca por indicador
                      p_cod_erro         := null;
                      p_descr_erro       := null;

                      --
                      PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic     --p_cod_indic
                                         ,null                           --p_cod_grp_indic
                                         ,null                           --p_cod_grp_rem_var
                                         ,v_num_realz_x_meta             --p_num_realz_x_meta
                                         ,v_cod_escala
                                         ,v_num_seq_escala_fx
                                         ,v_num_realz_fx
                                         ,v_cod_un_realz_fx
                                         ,v_flg_pct_100
                                         ,v_num_limite_fx
                                         ,p_cod_erro
                                         ,p_descr_erro
                                         );
                      -- armazena vr original de atingimento
                      v_num_realz_x_meta_limitador := v_num_realz_x_meta;

                      -- se nao houver escala de limitador cadastrada ou
                      -- se der erro manter o valor do atingimento original (sem limitador)
                      if p_cod_erro is not null then

                         --
                         v_num_realz_fx := v_num_realz_x_meta;

                         -- zera var de escala
                         v_cod_escala         := null;
                         v_num_seq_escala_fx  := null;
                         v_num_realz_fx       := null;
                         v_cod_un_realz_fx    := null;

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

    --                     raise e_erro;

                      -- se nao houve erro ao buscar escala
                      else
                         -- se o atingimento passou o valor do limitador, entao pegar o valor
                         -- do limitador como atingimento
                         if v_num_realz_x_meta > v_num_realz_fx then
                            --
                            v_num_realz_x_meta_limitador := v_num_realz_fx;

                         -- se o atingimento nao passou a fx limitador
                         else
                            v_num_realz_x_meta_limitador := v_num_realz_x_meta;

                            -- zera var de escala
                            v_cod_escala         := null;
                            v_num_seq_escala_fx  := null;
                            v_num_realz_fx       := null;
                            v_cod_un_realz_fx    := null;

                         end if;

                      -- erro ao calcular escala
                      end if;


                      -- selecionar a ponderacao para o grupo rem var e para o indicador
                      p_cod_erro         := null;
                      p_descr_erro       := null;
                      --
                      PKG_SRV_GERAL.Prc_Calc_Ponderacao_Tp_Fil2(p_cod_indic         => r_indic_rem_loja.cod_indic
                                                 ,p_cod_grp_indic     => null
                                                 ,p_cod_cargo         => r_lid_reg_nac.cod_cargo
                                                 ,p_cod_grp_rem_var   => null
                                                 ,p_cod_tipo_fil      => 1 --Tipo Filial Loja
                                                 ,p_cod_emp           => null
                                                 ,p_cod_fil           => null
                                                 ,p_cod_pond          => v_cod_pond
                                                 ,p_num_peso          => v_num_peso
                                                 ,p_cod_un_peso       => v_cod_un_peso
                                                 ,p_vlr_premio        => v_vlr_premio
                                                 ,p_cod_erro          => p_cod_erro
                                                 ,p_descr_erro        => p_descr_erro
                                                 );
                      --
                      if  p_cod_erro is not null then
                         --
                         v_num_peso   := 0;
                         v_vlr_premio := 0;

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

    --                      raise e_erro;
                      -- erro ao calcular ponderacao
                      end if;


                      -- realz X meta da filial multiplicar pelo peso
                      begin
                         -- realz X meta da filial (COM LIMITADOR SE HOUVER) multiplicar pelo peso

                         --v_num_realz_pond := (v_num_realz_x_meta * v_num_peso)/100;
                         v_num_realz_pond := (v_num_realz_x_meta_limitador * v_num_peso)/100;

                      exception
                         when others then
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao calcular num realz pond - PSF Lideranca Loja do Lider Regional / Nacional ' ||
                                              ' cod_fil:  '    || v_cod_fil   ||
                                              ' cod_indic:  '  || v_cod_indic ||
                                              ' cod_func:  '   || v_cod_func ||
                                              ' erro: '        || sqlerrm;

                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

    --                        raise e_erro;
                      --realz X meta da filial multiplicar pelo peso
                      end;

                      -- gravar na srv_realizado_func_indicador
                      rec_srv_realizado_func_indic.cod_func                := r_lid_reg_nac.cod_func;
                      rec_srv_realizado_func_indic.cod_cargo               := r_lid_reg_nac.cod_cargo;
                      rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                      rec_srv_realizado_func_indic.cod_emp                 := r_lid_reg_nac.cod_emp;
                      rec_srv_realizado_func_indic.cod_fil                 := r_lid_reg_nac.cod_fil;
                      rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                      rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                      rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                      rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                      rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                      rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                      rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                      rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                      rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                      rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                      rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                      rec_srv_realizado_func_indic.num_meta                := r_lid_reg_nac.num_meta;
                      rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                      rec_srv_realizado_func_indic.num_realz               := r_lid_reg_nac.num_realz;
                      rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                      rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta_limitador; --v_num_realz_x_meta;
                      rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                      rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                      rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                      rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null; -- unidade VALOR
                      rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                      rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                      rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                      rec_srv_realizado_func_indic.seg_fil                 := 'S';

                      -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                      prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                     ,p_cod_erro                  => p_cod_erro
                                                     ,p_descr_erro                => p_descr_erro
                                                      );

                      --
                      if p_cod_erro is not null then

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

    --                     raise e_erro;
                      -- erro ao inserir realizado func indicador
                      end if;

                   -- c_lid_reg_nac
                   end loop;

                   --
                   commit;

                --
                exception
                   when others then
                      p_cod_erro     := 1;
                      p_descr_erro   := 'Erro Geral ao calcular realizado e meta de todas as filiais do Lider Regional / Nacional LIDERANCA LOJA para os indicadores PSF ' ||
                                        ' cod_fil:  '    || v_cod_fil   ||
                                        ' cod_indic:  '  || v_cod_indic ||
                                        ' cod_cargo:  '  || v_cod_cargo ||
                                        ' cod_func:  '   || v_cod_func  ||
                                        ' erro: '        || sqlerrm;


                      -- enviar email
                      v_body       := p_cod_erro || ' - ' || p_descr_erro;

                      v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                              ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                              ,p_body    => v_body
                                                                              );

                      -- logar tabela
                      v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                ,p_num_ano     => p_num_ano
                                                                ,p_num_mes     => p_num_mes
                                                                ,p_cod_fil     => v_cod_fil
                                                                ,p_cod_func    => v_cod_func
                                                                ,p_cod_indic   => v_cod_indic
                                                                ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                );

                      if v_ins_log_erro is not null then
                         -- enviar email
                         v_body       := v_ins_log_erro;
                         --
                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                 ,p_body    => v_body
                                                                                 );
                      --v_ins_log_erro is not null
                      end if;

    --                  raise e_erro;
                --
                end;


             ------------------------------------------
             -- if p_descr_indic = VENDAS, etc...
             ------------------------------------------
             end if;

           end loop;

           -- commit por indicador
           commit;

           for r_indic_sistemico in c_indic_sistemico (p_cod_indic_sistemico => 'AGRUPA_PSF_LOJA_LID') loop

              v_cod_grp_indic    := r_indic_sistemico.cod_grp_indic;
              v_cod_indic        := r_indic_sistemico.cod_indic;

              -----------------------------------------------------------------------------------
              -- somar todos os pesos dos resultados dos PSF
              -----------------------------------------------------------------------------------
              -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
              for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                             ,p_flg_agrupa_fil_lider => 'N' ) loop

                begin
                   -- selecionar todos os funcionarios desse cargo para essa filial, cargos e funcionarios
                   for r_func_cargo in c_func_cargo_2_fil (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                          ,p_cur_cod_emp          => r_fil.cod_emp
                                                          ,p_cur_cod_fil          => r_fil.cod_fil
                                                          ,p_flg_agrupa_fil_lider => 'N' ) loop

                      v_cod_cargo := r_func_cargo.cod_cargo;
                      v_cod_func  := r_func_cargo.cod_func;


                      -- somar os resultados dos pesos das ponderacoes para todos os indicadores PSF
                      begin
                         select c.cod_grp_indic
                               ,sum(a.num_realz_pond)
                           into v_cod_grp_indic
                               ,v_vr_soma_num_realz_pond_psf
                           from srv_realizado_func_indicador a
                               ,srv_indicador                b
                               ,srv_grupo_indicador          c
                          where a.cod_indic                = b.cod_indic
                            and b.cod_grp_indic            = c.cod_grp_indic
                            and a.cod_func                 = r_func_cargo.cod_func
                            and a.cod_emp                  = r_fil.cod_emp
                            and a.cod_fil                  = r_fil.cod_fil
                            and a.num_ano                  = p_num_ano
                            and a.num_mes                  = p_num_mes
                            and c.descr_grp_indic          = 'PSF_PROD_SERV_FINANCEIROS'
                            and (b.cod_indic_sis            is null or cod_indic_sis ='SAX_PSF')
                       group by c.cod_grp_indic;
                      exception
                         when no_data_found then
                            v_cod_grp_indic              := null;
                            v_vr_soma_num_realz_pond_psf := 0;
                         when others then
                            --
                            v_cod_grp_indic              := null;
                            v_vr_soma_num_realz_pond_psf := 0;
                            --
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao selecionar a soma dos num_realz_pond para o indicador ' ||
                                              ' sistemico AGRUPA_PSF_LOJA_LID para os cargos com Agrupamento Filiais '   ||
                                              ' cod_fil -  '                                         || r_fil.cod_fil ||
                                              ' indic -  '                                           || 'AGRUPA_PSF_LOJA_LID' ||
                                              ' cod_func - '                                         || r_func_cargo.cod_func ||
                                              ' erro - '                                             || sqlerrm;

                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

  --                          raise e_erro;
                      --
                      end;

                      -- selecionar a ponderacao para o cargo e para o grupo indicador
                      PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                          ,p_cod_grp_indic     => r_indic_sistemico.cod_grp_indic
                                          ,p_cod_cargo         => r_cargo_grp_rem_var.cod_cargo
                                          ,p_cod_grp_rem_var   => null
                                          ,p_cod_pond          => v_cod_pond
                                          ,p_num_peso          => v_num_peso
                                          ,p_cod_un_peso       => v_cod_un_peso
                                          ,p_vlr_premio        => v_vlr_premio
                                          ,p_cod_erro          => p_cod_erro
                                          ,p_descr_erro        => p_descr_erro
                                          );
                      --
                      if p_cod_erro is not null then

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                      -- erro ao calcular ponderacao
                      end if;

                      -- aplicar a ponderacao sobre a soma de todas as ponderacoes de todos os indicadores PSF
                      begin
                         v_num_realz_pond := (v_vr_soma_num_realz_pond_psf * v_num_peso)/100;
                      exception
                         when others then
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao calcular num realz pond do PSF do Agrupamento PSF Lider Regional / Nacional ' ||
                                              ' cod_fil:  '    || v_cod_fil   ||
                                              ' cod_indic:  '  || v_cod_indic ||
                                              ' cod_func:  '   || v_cod_func ||
                                              ' erro: '        || sqlerrm;

                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

  --                          raise e_erro;

                      --aplicar a ponderacao sobre a soma de todas as ponderacoes de todos os indicadores PSF
                      end;

                      -- gravar na srv_realizado_func_indicador
                      rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                      rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                      rec_srv_realizado_func_indic.cod_indic               := r_indic_sistemico.cod_indic;
                      rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                      rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                      rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                      rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                      rec_srv_realizado_func_indic.cod_escala              := null;
                      rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                      rec_srv_realizado_func_indic.num_realz_fx            := null;
                      rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                      rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                      rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                      rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                      rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                      rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                      rec_srv_realizado_func_indic.num_meta                := null;
                      rec_srv_realizado_func_indic.cod_un_meta             := null;
                      rec_srv_realizado_func_indic.num_realz               := null;
                      rec_srv_realizado_func_indic.cod_un_realz            := null;
                      rec_srv_realizado_func_indic.num_realz_x_meta        := 0;
                      rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                      rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                      rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                      rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null; -- unidade VALOR
                      rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                      rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                      rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                      rec_srv_realizado_func_indic.seg_fil                 := 'S';

                      -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                      prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                     ,p_cod_erro                  => p_cod_erro
                                                     ,p_descr_erro                => p_descr_erro
                                                      );

                      --
                      if p_cod_erro is not null then
                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                      -- erro ao inserir realizado func indicador
                      end if;

                    -- c_func_cargo
                   end loop;

                 --
                 exception
                    /*when e_erro then
                       raise e_erro;*/
                    when others then
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro Geral ao calcular O AGRUPAMENTO PSF DOS LIDERES REGIONAIS / NACIONAIS  ' ||
                                         ' cod_fil:  '    || v_cod_fil   ||
                                         ' cod_indic:  '  || v_cod_indic ||
                                         ' cod_cargo:  '  || v_cod_cargo ||
                                         ' cod_func:  '   || v_cod_func  ||
                                         ' erro: '        || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                 --
                 end;

              -- c_cargo_grp_rem_var
              end loop;

           -- c_indic_sistemico AGRUPAMENTO LIDER PSF
           end loop;

           -- commit por indicador
           commit;

           ----------------------------------------------------------------------------
           -- CALCULAR O AGRUPAMENTO REM LOJA GERENTE POR 2 LOJAS
           ----------------------------------------------------------------------------
           for r_indic_sistemico in c_indic_sistemico (p_cod_indic_sistemico => 'AGRUPA_REM_LOJA_LID') loop

              v_cod_grp_indic    := r_indic_sistemico.cod_grp_indic;
              v_cod_indic        := r_indic_sistemico.cod_indic;

              -----------------------------------------------------------------------------------
              -- somar todos os pesos dos resultados dos PSF
              -----------------------------------------------------------------------------------
              -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
              for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                             ,p_flg_agrupa_fil_lider => 'N' ) loop


                 begin
                    -- selecionar todos os funcionarios desse cargo para essa filial, cargos e funcionarios
                    for r_func_cargo in c_func_cargo_2_fil (p_cod_cargo             => r_cargo_grp_rem_var.cod_cargo
                                                            ,p_cur_cod_emp          => r_fil.cod_emp
                                                            ,p_cur_cod_fil          => r_fil.cod_fil
                                                            ,p_flg_agrupa_fil_lider => 'N' ) loop

                      v_cod_cargo := r_func_cargo.cod_cargo;
                      v_cod_func  := r_func_cargo.cod_func;

                       -- somar os indicadores VENDAS E AGRUPAMENTO PSF
                       begin
                          select sum(a.num_realz_pond)        vr_soma_num_realz_pond_v_p
                                ,sum(a.vlr_premio)            vlr_soma_premio_v_p
                                ,sum(a.vlr_premio_func_calc)  vlr_soma_premio_func_calc_v_p
                            into v_vr_soma_num_realz_pond_v_p
                                ,vlr_soma_premio_v_p
                                ,vlr_soma_premio_func_calc_v_p
                            from srv_realizado_func_indicador a
                                ,srv_indicador         b
                           where a.cod_indic                = b.cod_indic
                             and a.cod_emp                  = r_fil.cod_emp
                             and a.cod_fil                  = r_fil.cod_fil
                             and a.cod_func                 = r_func_cargo.cod_func
                             and a.num_ano                  = p_num_ano
                             and a.num_mes                  = p_num_mes
                             and b.descr_indic              in ('VENDAS', 'AGRUPAMENTO PSF_LOJA LIDER');
                      exception
                         when no_data_found then
                            v_vr_soma_num_realz_pond_v_p  := 0;
                            vlr_soma_premio_v_p           := 0;
                            vlr_soma_premio_func_calc_v_p := 0;
                         when others then
                            --
                            v_vr_soma_num_realz_pond_v_p  := 0;
                            vlr_soma_premio_v_p           := 0;
                            vlr_soma_premio_func_calc_v_p := 0;
                            --
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao selecionar a soma dos indicadores VENDAS E AGRUPAMENTO PSF ' ||
                                              ' para o indic sistemico AGRUPA_REM_LOJA_LID para os cargos com Agrupamento Filiais '   ||
                                              ' cod_fil -  '                                         || r_fil.cod_fil ||
                                              ' indic -  '                                           || 'AGRUPA_REM_LOJA_LID' ||
                                              ' cod_func - '                                         || r_func_cargo.cod_func ||
                                              ' erro - '                                             || sqlerrm;


                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

  --                          raise e_erro;

                      --somar os indicadores VENDAS E AGRUPAMENTO PSF
                      end;

                       -- aplicar a escala do indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       p_cod_erro         := null;
                       p_descr_erro       := null;
                       --
                       PKG_SRV_GERAL.Prc_Calc_Escala_Fx (null                            -- p_cod_indic
                                          ,null                            -- p_cod_grp_indic
                                          ,15                              -- p_cod_grp_rem_var
                                          ,v_vr_soma_num_realz_pond_v_p
                                          ,v_cod_escala
                                          ,v_num_seq_escala_fx
                                          ,v_num_realz_fx
                                          ,v_cod_un_realz_fx
                                          ,v_flg_pct_100
                                          ,v_num_limite_fx
                                          ,p_cod_erro
                                          ,p_descr_erro
                                          );

                       if p_cod_erro is not null then
                          --
                          v_num_realz_fx := 0;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                       -- erro ao calcular
                       end if;

                       -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                       -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                       if (nvl(v_flg_pct_100, 'N') = 'S' and v_vr_soma_num_realz_pond_v_p > 100) then
                          --
                          v_num_realz_fx := v_vr_soma_num_realz_pond_v_p;
                       end if;

                       -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                       -- entao assumir o valor limite
                       if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                          --
                          v_num_realz_fx := v_num_limite_fx;
                       end if;



                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao
                       -- para o cargo e indicador VENDAS
                       -- gravar o vlr premio calc na realz func para o indicador VENDAS
                       begin
                          update srv_realizado_func_indicador a
                             set a.vlr_premio_func_calc = (a.vlr_premio * v_num_realz_fx)/100
                           where a.cod_indic            = (select cod_indic
                                                             from srv_indicador
                                                            where descr_indic = 'VENDAS')
                             and a.cod_func             = r_func_cargo.cod_func
                             and a.cod_emp              = r_fil.cod_emp
                             and a.cod_fil              = r_fil.cod_fil
                             and a.num_ano              = p_num_ano
                             and a.num_mes              = p_num_mes;
                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao atualizar na srv_realizado_func_indicador o vlr premio calc para o indicador VENDAS - Lideres Regionais e Nacionais ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao - VENDAS
                       end;

                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao
                       -- para o cargo e grupo de indicadores PSF
                       -- gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO PSF_LOJA LIDER
                       begin
                          update srv_realizado_func_indicador a
                             set a.vlr_premio_func_calc = (a.vlr_premio * v_num_realz_fx)/100
                           where a.cod_indic            = (select cod_indic
                                                             from srv_indicador
                                                            where descr_indic = 'AGRUPAMENTO PSF_LOJA LIDER')
                             and a.cod_func             = r_func_cargo.cod_func
                             and a.cod_emp              = r_fil.cod_emp
                             and a.cod_fil              = r_fil.cod_fil
                             and a.num_ano              = p_num_ano
                             and a.num_mes              = p_num_mes;
                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao atualizar na srv_realizado_func_indicador o vlr premio calc para o indicador AGRUPAMENTO PSF_LOJA LIDER  - Lideres Regionais e Nacionais ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao - AGRUPAMENTO PSF_LOJA LIDER
                       end;

                       -- gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       begin
                          vlr_soma_premio_func_calc_v_p := (vlr_soma_premio_v_p * v_num_realz_fx)/100;
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao calcular soma do premio calculado para Agrupamento Rem Loja Lider - Lider Regional / Nacional ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       end;

                       -- inserir o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       -- gravar na srv_realizado_func_indicador
                       rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                       rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                       rec_srv_realizado_func_indic.cod_indic               := r_indic_sistemico.cod_indic;
                       rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                       rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                       rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                       rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                       rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                       rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                       rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                       rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                       rec_srv_realizado_func_indic.cod_pond                := null;
                       rec_srv_realizado_func_indic.num_peso                := null;
                       rec_srv_realizado_func_indic.cod_un_peso             := null;
                       rec_srv_realizado_func_indic.num_realz_pond          := v_vr_soma_num_realz_pond_v_p;
                       rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                       rec_srv_realizado_func_indic.num_meta                := null;
                       rec_srv_realizado_func_indic.cod_un_meta             := null;
                       rec_srv_realizado_func_indic.num_realz               := null;
                       rec_srv_realizado_func_indic.cod_un_realz            := null;
                       rec_srv_realizado_func_indic.num_realz_x_meta        := 0;
                       rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                       rec_srv_realizado_func_indic.vlr_premio              := vlr_soma_premio_v_p;
                       rec_srv_realizado_func_indic.vlr_premio_func_calc    := vlr_soma_premio_func_calc_v_p;
                       rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                       rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                       rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                       rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                       rec_srv_realizado_func_indic.seg_fil                 := 'S';

                       -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                       prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                      ,p_cod_erro                  => p_cod_erro
                                                      ,p_descr_erro                => p_descr_erro
                                                       );

                       --
                       if p_cod_erro is not null then

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

                        end if;

                    -- c_func_cargo
                    end loop;
                 --
                 exception
                    when others then
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro Geral ao calcular AGRUPAMENTO REM LOJA LIDER DOS LIDERES REGIONAIS / NACIONAIS ' ||
                                         ' cod_fil:  '    || v_cod_fil   ||
                                         ' cod_indic:  '  || v_cod_indic ||
                                         ' cod_cargo:  '  || v_cod_cargo ||
                                         ' cod_func:  '   || v_cod_func  ||
                                         ' erro: '        || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                 --
                 end;

              -- c_cargo_grp_rem_var
              end loop;

              -- commit por indicador
              commit;

           -----------------------------------------------------
           -- c_indic_sistemico AGRUPAMENTO LOJA LIDER
           -----------------------------------------------------
           end loop;

        ----------------------------------------------------
        -- c_fil
        ----------------------------------------------------
        end loop;

        commit;

      Exception
        When Others Then
          Null;
      end;



      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      -- AGRUPAMENTO DE FILIAIS REGIONAL / NACIONAL E VISUAL MERCHANDISING LOJA
      ------------------------------------------------------------------------------------------
      ------------------------------------------------------------------------------------------
      Begin
        for r_indic_rem_loja in c_indic_rem_loja loop

           v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
           v_cod_indic        := r_indic_rem_loja.cod_indic;

           --------------------------------------------------------
           -- SE INDICADOR VENDAS
           --------------------------------------------------------
           if r_indic_rem_loja.descr_indic = 'VENDAS' then

              -- LIDERANCA_LOJA
              -- realizado e meta de todas as filiais do Lider Regional / Nacional pelo indicador
              begin
                 for r_lid_reg_nac in c_lid_reg_nac (p_descr_indic         => r_indic_rem_loja.descr_indic
                                                    ,p_descr_grp_rem_var => 'LIDERANCA_LOJA'
                                                    ) loop

                     v_cod_cargo := r_lid_reg_nac.cod_cargo;
                     v_cod_func  := r_lid_reg_nac.cod_func;

                    ---------------------------------------------------------------
                    -- CARGOS LIDERANCA - VENDAS
                    ---------------------------------------------------------------
                    if r_lid_reg_nac.num_meta > 0 then
                       v_num_realz_x_meta := (r_lid_reg_nac.num_realz / r_lid_reg_nac.num_meta) * 100;
                    else
                       v_num_realz_x_meta := 100; --r_lid_reg_nac.num_realz;
                    end if;
                    v_cod_un_meta         := 1; -- unidade VALOR
                    v_cod_un_realz        := 1; -- unidade VALOR
                    v_cod_un_realz_x_meta := 2; -- unidade PERCENTUAL

                    -- selecionar a ponderacao para o cargo e para o indicador VENDAS
                    PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => r_indic_rem_loja.cod_indic
                                        ,p_cod_grp_indic     => null
                                        ,p_cod_cargo         => r_lid_reg_nac.cod_cargo
                                        ,p_cod_grp_rem_var   => null
                                        ,p_cod_pond          => v_cod_pond
                                        ,p_num_peso          => v_num_peso
                                        ,p_cod_un_peso       => v_cod_un_peso
                                        ,p_vlr_premio        => v_vlr_premio
                                        ,p_cod_erro          => p_cod_erro
                                        ,p_descr_erro        => p_descr_erro
                                        );
                    --
                    if p_cod_erro is not null then
                       --
                       v_num_peso   := 0;
                       v_vlr_premio := 0;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;


  --                     raise e_erro;
                    -- erro ao calcular ponderacao
                    end if;

                    -- realz X meta da filial multiplicar pelo peso (referente a vendas)
                    v_num_realz_pond := (v_num_realz_x_meta * v_num_peso)/100;

                    -- gravar na srv_realizado_func_indicador
                    rec_srv_realizado_func_indic.cod_func                := r_lid_reg_nac.cod_func;
                    rec_srv_realizado_func_indic.cod_cargo               := r_lid_reg_nac.cod_cargo;
                    rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                    rec_srv_realizado_func_indic.cod_emp                 := r_lid_reg_nac.cod_emp;
                    rec_srv_realizado_func_indic.cod_fil                 := r_lid_reg_nac.cod_fil;
                    rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                    rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                    rec_srv_realizado_func_indic.cod_escala              := null;
                    rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                    rec_srv_realizado_func_indic.num_realz_fx            := null;
                    rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                    rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                    rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                    rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                    rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                    rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                    rec_srv_realizado_func_indic.num_meta                := r_lid_reg_nac.num_meta;
                    rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                    rec_srv_realizado_func_indic.num_realz               := r_lid_reg_nac.num_realz;
                    rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                    rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                    rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                    rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                    rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                    rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null;
                    rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                    rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                    rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                    rec_srv_realizado_func_indic.seg_fil                 := null;

                    -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                    prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                   ,p_cod_erro                  => p_cod_erro
                                                   ,p_descr_erro                => p_descr_erro
                                                    );

                    --
                    if p_cod_erro is not null then

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                    -- erro ao inserir realizado func indicador
                    end if;

                 -- c_lid_reg_nac
                 end loop;

                 --
                 commit;

              --
              exception
                 /*when e_erro then
                    raise e_erro;*/
                 when others then
                    p_cod_erro     := 1;
                    p_descr_erro   := 'Erro Geral ao calcular realizado e meta de todas as filiais do Lider Regional / Nacional LIDERANCA LOJA para o indicador VENDAS ' ||
                                      ' cod_fil:  '    || v_cod_fil   ||
                                      ' cod_indic:  '  || v_cod_indic ||
                                      ' cod_cargo:  '  || v_cod_cargo ||
                                      ' cod_func:  '   || v_cod_func  ||
                                      ' erro: '        || sqlerrm;

                    -- enviar email
                    v_body       := p_cod_erro || ' - ' || p_descr_erro;

                    v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                            ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                            ,p_body    => v_body
                                                                            );

                    -- logar tabela
                    v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                              ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                              ,p_num_ano     => p_num_ano
                                                              ,p_num_mes     => p_num_mes
                                                              ,p_cod_fil     => v_cod_fil
                                                              ,p_cod_func    => v_cod_func
                                                              ,p_cod_indic   => v_cod_indic
                                                              ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                              );

                    if v_ins_log_erro is not null then
                       -- enviar email
                       v_body       := v_ins_log_erro;
                       --
                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                               ,p_body    => v_body
                                                                               );
                    --v_ins_log_erro is not null
                    end if;

  --                  raise e_erro;
              --
              end;

              -- VISUAL_MERCHANDISING_LOJA
              -- realizado e meta de todas as filiais do Lider VISUAL_MERCHANDISING_LOJA
              begin
                 for r_lid_reg_nac in c_lid_reg_nac (p_descr_indic       => r_indic_rem_loja.descr_indic
                                                    ,p_descr_grp_rem_var => 'VISUAL_MERCHANDISING_LOJA'
                                                    ) loop

                     v_cod_cargo := r_lid_reg_nac.cod_cargo;
                     v_cod_func  := r_lid_reg_nac.cod_func;

                    ---------------------------------------------------------------
                    -- VISUAL_MERCHANDISING_LOJA - VENDAS
                    ---------------------------------------------------------------
                    if r_lid_reg_nac.num_meta > 0 then
                       v_num_realz_x_meta := (r_lid_reg_nac.num_realz / r_lid_reg_nac.num_meta) * 100;
                    else
                       v_num_realz_x_meta := 100; --r_lid_reg_nac.num_realz;
                    end if;
                    v_cod_un_meta         := 1; -- unidade VALOR
                    v_cod_un_realz        := 1; -- unidade VALOR
                    v_cod_un_realz_x_meta := 2; -- unidade PERCENTUAL

                    -- selecionar a ponderacao para o cargo e para o indicador VENDAS
                    PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => r_indic_rem_loja.cod_indic
                                        ,p_cod_grp_indic     => null
                                        ,p_cod_cargo         => r_lid_reg_nac.cod_cargo
                                        ,p_cod_grp_rem_var   => null
                                        ,p_cod_pond          => v_cod_pond
                                        ,p_num_peso          => v_num_peso
                                        ,p_cod_un_peso       => v_cod_un_peso
                                        ,p_vlr_premio        => v_vlr_premio
                                        ,p_cod_erro          => p_cod_erro
                                        ,p_descr_erro        => p_descr_erro
                                        );
                    --
                    if p_cod_erro is not null then
                       --
                       v_num_peso   := 0;
                       v_vlr_premio := 0;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                    -- erro ao calcular ponderacao
                    end if;

                    --
                    p_cod_erro         := null;
                    p_descr_erro       := null;
                    --
                    PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic     --p_cod_indic
                                       ,null                           --p_cod_grp_indic
                                       ,null                           --p_cod_grp_rem_var
                                       ,v_num_realz_x_meta             --p_num_realz_x_meta
                                       ,v_cod_escala
                                       ,v_num_seq_escala_fx
                                       ,v_num_realz_fx
                                       ,v_cod_un_realz_fx
                                       ,v_flg_pct_100
                                       ,v_num_limite_fx
                                       ,p_cod_erro
                                       ,p_descr_erro
                                       );
                    if p_cod_erro is not null then

                       --
                       v_num_realz_fx := 0;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                    -- erro ao calcular escala
                    end if;

                    -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                    -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                    if (nvl(v_flg_pct_100, 'N') = 'S' and v_num_realz_x_meta > 100) then
                       --
                       v_num_realz_fx := v_num_realz_x_meta;
                    end if;

                    -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                    -- entao assumir o valor limite
                    if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                       --
                       v_num_realz_fx := v_num_limite_fx;
                    end if;

                    -- calcula valor do premio pela faixa %
                    begin
                    v_vlr_premio_func_calc  := (v_vlr_premio * v_num_realz_fx)/100;
                    --
                    exception
                       when others then
                          p_cod_erro     := 1;
                          p_descr_erro   := 'Erro ao calcular vlr premio func calculado - Lider VISUAL_MERCHANDISING_LOJA' ||
                                            ' cod_fil:  '    || v_cod_fil   ||
                                            ' cod_indic:  '  || v_cod_indic ||
                                            ' cod_func:  '   || v_cod_func ||
                                            ' erro: '        || sqlerrm;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                    --calcula valor do premio pela faixa %
                    end;

                    -- gravar na srv_realizado_func_indicador
                    rec_srv_realizado_func_indic.cod_func                := r_lid_reg_nac.cod_func;
                    rec_srv_realizado_func_indic.cod_cargo               := r_lid_reg_nac.cod_cargo;
                    rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                    rec_srv_realizado_func_indic.cod_emp                 := r_lid_reg_nac.cod_emp;
                    rec_srv_realizado_func_indic.cod_fil                 := r_lid_reg_nac.cod_fil;
                    rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                    rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                    rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                    rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                    rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                    rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                    rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                    rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                    rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                    rec_srv_realizado_func_indic.num_realz_pond          := null;
                    rec_srv_realizado_func_indic.cod_un_realz_pond       := null;

                    rec_srv_realizado_func_indic.num_meta                := r_lid_reg_nac.num_meta;
                    rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                    rec_srv_realizado_func_indic.num_realz               := r_lid_reg_nac.num_realz;
                    rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                    rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                    rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                    rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                    rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                    rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                    rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                    rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                    rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                    rec_srv_realizado_func_indic.seg_fil                 := null;

                    -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                    prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                   ,p_cod_erro                  => p_cod_erro
                                                   ,p_descr_erro                => p_descr_erro
                                                    );

                    --
                    if p_cod_erro is not null then

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                    -- erro ao inserir realizado func indicador
                    end if;

                 -- c_lid_reg_nac
                 end loop;

                 --
                 commit;

              --
              exception
                 /*when e_erro then
                    raise e_erro;*/
                 when others then
                    p_cod_erro     := 1;
                    p_descr_erro   := 'Erro Geral ao calcular realizado e meta de todas as filiais do Lider VISUAL_MERCHANDISING_LOJA' ||
                                      ' cod_fil:  '    || v_cod_fil   ||
                                      ' cod_indic:  '  || v_cod_indic ||
                                      ' cod_cargo:  '  || v_cod_cargo ||
                                      ' cod_func:  '   || v_cod_func  ||
                                      ' erro: '        || sqlerrm;

                    -- enviar email
                    v_body       := p_cod_erro || ' - ' || p_descr_erro;

                    v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                            ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                            ,p_body    => v_body
                                                                            );

                    -- logar tabela
                    v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                              ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                              ,p_num_ano     => p_num_ano
                                                              ,p_num_mes     => p_num_mes
                                                              ,p_cod_fil     => v_cod_fil
                                                              ,p_cod_func    => v_cod_func
                                                              ,p_cod_indic   => v_cod_indic
                                                              ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                              );

                    if v_ins_log_erro is not null then
                       -- enviar email
                       v_body       := v_ins_log_erro;
                       --
                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                               ,p_body    => v_body
                                                                               );
                    --v_ins_log_erro is not null
                    end if;

  --                  raise e_erro;
              --
              end;

           --------------------------------------------------------
           -- SE INDICADORES DO GRUPO PSF_PROD_SERV_FINANCEIROS
           --------------------------------------------------------
           elsif r_indic_rem_loja.descr_grp_indic = 'PSF_PROD_SERV_FINANCEIROS' then

              -- LIDERANCA_LOJA
              -- realizado e meta de todas as filiais do Lider Regional / Nacional pelo indicador
              begin
                 for r_lid_reg_nac in c_lid_reg_nac (p_descr_indic       => r_indic_rem_loja.descr_indic
                                                    ,p_descr_grp_rem_var => 'LIDERANCA_LOJA'
                                                    ) loop

                    v_cod_cargo := r_lid_reg_nac.cod_cargo;
                    v_cod_func  := r_lid_reg_nac.cod_func;


                    -----------------------------------------------------------------------
                    -- CARGOS LIDERANCA - GRUPO DE INDICADORES PSF_PROD_SERV_FINANCEIROS
                    -----------------------------------------------------------------------
                    if r_lid_reg_nac.num_meta > 0 then
                       v_num_realz_x_meta := (r_lid_reg_nac.num_realz / r_lid_reg_nac.num_meta) * 100;
                    else
                       v_num_realz_x_meta := 100; --r_lid_reg_nac.num_realz;
                    end if;
                    if r_indic_rem_loja.descr_indic in ('VENDA COM JUROS PL', 'VENDA COM JUROS ITAU') then
                       v_cod_un_meta         := 1; -- unidade VALOR
                       v_cod_un_realz        := 1; -- unidade VALOR
                    else
                       v_cod_un_meta         := 3; -- unidade UNIDADE
                       v_cod_un_realz        := 3; -- unidade UNIDADE
                    end if;
                    v_cod_un_realz_x_meta    := 2; -- unidade PERCENTUAL



                    -- Limitador de Atingimento da Meta para Cargos Lideranca por indicador
                    p_cod_erro         := null;
                    p_descr_erro       := null;
                    --

                    PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic     --p_cod_indic
                                       ,null                           --p_cod_grp_indic
                                       ,null                           --p_cod_grp_rem_var
                                       ,v_num_realz_x_meta             --p_num_realz_x_meta
                                       ,v_cod_escala
                                       ,v_num_seq_escala_fx
                                       ,v_num_realz_fx
                                       ,v_cod_un_realz_fx
                                       ,v_flg_pct_100
                                       ,v_num_limite_fx
                                       ,p_cod_erro
                                       ,p_descr_erro
                                       );
                    -- armazena vr original de atingimento
                    v_num_realz_x_meta_limitador := v_num_realz_x_meta;

                    -- se nao houver escala de limitador cadastrada ou
                    -- se der erro manter o valor do atingimento original (sem limitador)
                    if p_cod_erro is not null then

                       --
                       v_num_realz_fx := v_num_realz_x_meta;

                       -- zera var de escala
                       v_cod_escala         := null;
                       v_num_seq_escala_fx  := null;
                       v_num_realz_fx       := null;
                       v_cod_un_realz_fx    := null;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;

                    -- se nao houve erro ao buscar escala
                    else
                       -- se o atingimento passou o valor do limitador, entao pegar o valor
                       -- do limitador como atingimento
                       if v_num_realz_x_meta > v_num_realz_fx then
                          --
                          v_num_realz_x_meta_limitador := v_num_realz_fx;

                       -- se o atingimento nao passou a fx limitador
                       else
                          v_num_realz_x_meta_limitador := v_num_realz_x_meta;

                          -- zera var de escala
                          v_cod_escala         := null;
                          v_num_seq_escala_fx  := null;
                          v_num_realz_fx       := null;
                          v_cod_un_realz_fx    := null;

                       end if;

                    -- erro ao calcular escala
                    end if;


                    -- selecionar a ponderacao para o grupo rem var e para o indicador
                    p_cod_erro         := null;
                    p_descr_erro       := null;
                    --

                    --
                    PKG_SRV_GERAL.Prc_Calc_Ponderacao_Tp_Fil2(p_cod_indic         => r_indic_rem_loja.cod_indic
                                               ,p_cod_grp_indic     => null
                                               ,p_cod_cargo         => r_lid_reg_nac.cod_cargo
                                               ,p_cod_grp_rem_var   => null
                                               ,p_cod_tipo_fil      => 1  -- tipo fil Loja
                                               ,p_cod_emp           => null
                                               ,p_cod_fil           => null
                                               ,p_cod_pond          => v_cod_pond
                                               ,p_num_peso          => v_num_peso
                                               ,p_cod_un_peso       => v_cod_un_peso
                                               ,p_vlr_premio        => v_vlr_premio
                                               ,p_cod_erro          => p_cod_erro
                                               ,p_descr_erro        => p_descr_erro
                                               );
                    --
                    if  p_cod_erro is not null then
                       --
                       v_num_peso   := 0;
                       v_vlr_premio := 0;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                      raise e_erro;
                    -- erro ao calcular ponderacao
                    end if;


                    -- realz X meta da filial multiplicar pelo peso
                    begin
                       -- realz X meta da filial (COM LIMITADOR SE HOUVER) multiplicar pelo peso

                       --v_num_realz_pond := (v_num_realz_x_meta * v_num_peso)/100;
                       v_num_realz_pond := (v_num_realz_x_meta_limitador * v_num_peso)/100;

                    exception
                       when others then
                          p_cod_erro     := 1;
                          p_descr_erro   := 'Erro ao calcular num realz pond - PSF Lideranca Loja do Lider Regional / Nacional ' ||
                                            ' cod_fil:  '    || v_cod_fil   ||
                                            ' cod_indic:  '  || v_cod_indic ||
                                            ' cod_func:  '   || v_cod_func ||
                                            ' erro: '        || sqlerrm;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                    --realz X meta da filial multiplicar pelo peso
                    end;


                    -- gravar na srv_realizado_func_indicador
                    rec_srv_realizado_func_indic.cod_func                := r_lid_reg_nac.cod_func;
                    rec_srv_realizado_func_indic.cod_cargo               := r_lid_reg_nac.cod_cargo;
                    rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                    rec_srv_realizado_func_indic.cod_emp                 := r_lid_reg_nac.cod_emp;
                    rec_srv_realizado_func_indic.cod_fil                 := r_lid_reg_nac.cod_fil;
                    rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                    rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                    rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                    rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                    rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                    rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                    rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                    rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                    rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                    rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                    rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                    rec_srv_realizado_func_indic.num_meta                := r_lid_reg_nac.num_meta;
                    rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                    rec_srv_realizado_func_indic.num_realz               := r_lid_reg_nac.num_realz;
                    rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                    rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta_limitador; --v_num_realz_x_meta;
                    rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                    rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                    rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                    rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null; -- unidade VALOR
                    rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                    rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                    rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                    rec_srv_realizado_func_indic.seg_fil                 := null;

                    -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                    prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                   ,p_cod_erro                  => p_cod_erro
                                                   ,p_descr_erro                => p_descr_erro
                                                    );

                    --
                    if p_cod_erro is not null then

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                    -- erro ao inserir realizado func indicador
                    end if;

                 -- c_lid_reg_nac
                 end loop;

                 --
                 commit;

              --
              exception
                 /*when e_erro then
                    raise e_erro;*/
                 when others then
                    p_cod_erro     := 1;
                    p_descr_erro   := 'Erro Geral ao calcular realizado e meta de todas as filiais do Lider Regional / Nacional LIDERANCA LOJA para os indicadores PSF ' ||
                                      ' cod_fil:  '    || v_cod_fil   ||
                                      ' cod_indic:  '  || v_cod_indic ||
                                      ' cod_cargo:  '  || v_cod_cargo ||
                                      ' cod_func:  '   || v_cod_func  ||
                                      ' erro: '        || sqlerrm;


                    -- enviar email
                    v_body       := p_cod_erro || ' - ' || p_descr_erro;

                    v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                            ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                            ,p_body    => v_body
                                                                            );

                    -- logar tabela
                    v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                              ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                              ,p_num_ano     => p_num_ano
                                                              ,p_num_mes     => p_num_mes
                                                              ,p_cod_fil     => v_cod_fil
                                                              ,p_cod_func    => v_cod_func
                                                              ,p_cod_indic   => v_cod_indic
                                                              ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                              );

                    if v_ins_log_erro is not null then
                       -- enviar email
                       v_body       := v_ins_log_erro;
                       --
                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                               ,p_body    => v_body
                                                                               );
                    --v_ins_log_erro is not null
                    end if;

  --                  raise e_erro;
              --
              end;


           ------------------------------------------
           -- if p_descr_indic = VENDAS, etc...
           ------------------------------------------
           end if;

           -- commit por indicador
           commit;

        --------------------------------------------
        -- c_indic_rem_loja
        --------------------------------------------
        end loop;

        -----------------------------------------------------------------------
        -- CALCULAR O AGRUPAMENTO PSF DOS LIDERES REGIONAIS / NACIONAIS
        -----------------------------------------------------------------------
        for r_fil in c_fil (p_cod_emp  => null
                           ,p_cod_fil  => null
                           ) loop

           v_cod_fil          := r_fil.cod_fil;

           for r_indic_sistemico in c_indic_sistemico (p_cod_indic_sistemico => 'AGRUPA_PSF_LOJA_LID') loop

              v_cod_grp_indic    := r_indic_sistemico.cod_grp_indic;
              v_cod_indic        := r_indic_sistemico.cod_indic;

              -----------------------------------------------------------------------------------
              -- somar todos os pesos dos resultados dos PSF
              -----------------------------------------------------------------------------------
              -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
              for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                             ,p_flg_agrupa_fil_lider => 'S' ) loop

                begin
                   -- selecionar todos os funcionarios desse cargo para essa filial, cargos e funcionarios
                   for r_func_cargo in c_func_cargo (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                    ,p_cur_cod_emp          => r_fil.cod_emp
                                                    ,p_cur_cod_fil          => r_fil.cod_fil
                                                    ,p_flg_agrupa_fil_lider => 'S' ) loop

                      v_cod_cargo := r_func_cargo.cod_cargo;
                      v_cod_func  := r_func_cargo.cod_func;


                      -- somar os resultados dos pesos das ponderacoes para todos os indicadores PSF
                      begin
                         select c.cod_grp_indic
                               ,sum(a.num_realz_pond)
                           into v_cod_grp_indic
                               ,v_vr_soma_num_realz_pond_psf
                           from srv_realizado_func_indicador a
                               ,srv_indicador                b
                               ,srv_grupo_indicador          c
                          where a.cod_indic                = b.cod_indic
                            and b.cod_grp_indic            = c.cod_grp_indic
                            and a.cod_func                 = r_func_cargo.cod_func
                            and a.cod_emp                  = r_fil.cod_emp
                            and a.cod_fil                  = r_fil.cod_fil
                            and a.num_ano                  = p_num_ano
                            and a.num_mes                  = p_num_mes
                            and c.descr_grp_indic          = 'PSF_PROD_SERV_FINANCEIROS'
                            and (b.cod_indic_sis            is null or cod_indic_sis ='SAX_PSF')
                       group by c.cod_grp_indic;
                      exception
                         when no_data_found then
                            v_cod_grp_indic              := null;
                            v_vr_soma_num_realz_pond_psf := 0;
                         when others then
                            --
                            v_cod_grp_indic              := null;
                            v_vr_soma_num_realz_pond_psf := 0;
                            --
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao selecionar a soma dos num_realz_pond para o indicador ' ||
                                              ' sistemico AGRUPA_PSF_LOJA_LID para os cargos com Agrupamento Filiais '   ||
                                              ' cod_fil -  '                                         || r_fil.cod_fil ||
                                              ' indic -  '                                           || 'AGRUPA_PSF_LOJA_LID' ||
                                              ' cod_func - '                                         || r_func_cargo.cod_func ||
                                              ' erro - '                                             || sqlerrm;

                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

  --                          raise e_erro;
                      --
                      end;

                      -- selecionar a ponderacao para o cargo e para o grupo indicador
                      PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                          ,p_cod_grp_indic     => r_indic_sistemico.cod_grp_indic
                                          ,p_cod_cargo         => r_cargo_grp_rem_var.cod_cargo
                                          ,p_cod_grp_rem_var   => null
                                          ,p_cod_pond          => v_cod_pond
                                          ,p_num_peso          => v_num_peso
                                          ,p_cod_un_peso       => v_cod_un_peso
                                          ,p_vlr_premio        => v_vlr_premio
                                          ,p_cod_erro          => p_cod_erro
                                          ,p_descr_erro        => p_descr_erro
                                          );
                      --
                      if p_cod_erro is not null then

                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                      -- erro ao calcular ponderacao
                      end if;

                      -- aplicar a ponderacao sobre a soma de todas as ponderacoes de todos os indicadores PSF
                      begin
                         v_num_realz_pond := (v_vr_soma_num_realz_pond_psf * v_num_peso)/100;
                      exception
                         when others then
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao calcular num realz pond do PSF do Agrupamento PSF Lider Regional / Nacional ' ||
                                              ' cod_fil:  '    || v_cod_fil   ||
                                              ' cod_indic:  '  || v_cod_indic ||
                                              ' cod_func:  '   || v_cod_func ||
                                              ' erro: '        || sqlerrm;

                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

  --                          raise e_erro;

                      --aplicar a ponderacao sobre a soma de todas as ponderacoes de todos os indicadores PSF
                      end;

                      -- gravar na srv_realizado_func_indicador
                      rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                      rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                      rec_srv_realizado_func_indic.cod_indic               := r_indic_sistemico.cod_indic;
                      rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                      rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                      rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                      rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                      rec_srv_realizado_func_indic.cod_escala              := null;
                      rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                      rec_srv_realizado_func_indic.num_realz_fx            := null;
                      rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                      rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                      rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                      rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                      rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                      rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                      rec_srv_realizado_func_indic.num_meta                := null;
                      rec_srv_realizado_func_indic.cod_un_meta             := null;
                      rec_srv_realizado_func_indic.num_realz               := null;
                      rec_srv_realizado_func_indic.cod_un_realz            := null;
                      rec_srv_realizado_func_indic.num_realz_x_meta        := 0;
                      rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                      rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                      rec_srv_realizado_func_indic.vlr_premio_func_calc    := null;
                      rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := null; -- unidade VALOR
                      rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                      rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                      rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                      rec_srv_realizado_func_indic.seg_fil                 := null;

                      -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                      prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                     ,p_cod_erro                  => p_cod_erro
                                                     ,p_descr_erro                => p_descr_erro
                                                      );

                      --
                      if p_cod_erro is not null then
                         -- enviar email
                         v_body       := p_cod_erro || ' - ' || p_descr_erro;

                         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                 ,p_body    => v_body
                                                                                 );

                         -- logar tabela
                         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );

                         if v_ins_log_erro is not null then
                            -- enviar email
                            v_body       := v_ins_log_erro;
                            --
                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                    ,p_body    => v_body
                                                                                    );
                         --v_ins_log_erro is not null
                         end if;

  --                       raise e_erro;
                      -- erro ao inserir realizado func indicador
                      end if;

                    -- c_func_cargo
                   end loop;

                 --
                 exception
                    /*when e_erro then
                       raise e_erro;*/
                    when others then
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro Geral ao calcular O AGRUPAMENTO PSF DOS LIDERES REGIONAIS / NACIONAIS  ' ||
                                         ' cod_fil:  '    || v_cod_fil   ||
                                         ' cod_indic:  '  || v_cod_indic ||
                                         ' cod_cargo:  '  || v_cod_cargo ||
                                         ' cod_func:  '   || v_cod_func  ||
                                         ' erro: '        || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                 --
                 end;

              -- c_cargo_grp_rem_var
              end loop;

           -- c_indic_sistemico AGRUPAMENTO LIDER PSF
           end loop;

           -- commit por indicador
           commit;

           ----------------------------------------------------------------------------
           -- CALCULAR O AGRUPAMENTO REM LOJA LIDER DOS LIDERES REGIONAIS / NACIONAIS
           ----------------------------------------------------------------------------
           for r_indic_sistemico in c_indic_sistemico (p_cod_indic_sistemico => 'AGRUPA_REM_LOJA_LID') loop

              v_cod_grp_indic    := r_indic_sistemico.cod_grp_indic;
              v_cod_indic        := r_indic_sistemico.cod_indic;

              -----------------------------------------------------------------------------------
              -- somar todos os pesos dos resultados dos PSF
              -----------------------------------------------------------------------------------
              -- pegar todos os grupos de cargos e cargos para o Grupo Rem Var = Lideranca Loja
              for r_cargo_grp_rem_var in c_cargo_grp_rem_var (p_descr_grp_rem_var    => 'LIDERANCA_LOJA'
                                                             ,p_flg_agrupa_fil_lider => 'S' ) loop


                 begin
                    -- selecionar todos os funcionarios desse cargo para essa filial, cargos e funcionarios
                    for r_func_cargo in c_func_cargo (p_cod_cargo            => r_cargo_grp_rem_var.cod_cargo
                                                     ,p_cur_cod_emp          => r_fil.cod_emp
                                                     ,p_cur_cod_fil          => r_fil.cod_fil
                                                     ,p_flg_agrupa_fil_lider => 'S' ) loop

                      v_cod_cargo := r_func_cargo.cod_cargo;
                      v_cod_func  := r_func_cargo.cod_func;

                       -- somar os indicadores VENDAS E AGRUPAMENTO PSF
                       begin
                          select sum(a.num_realz_pond)        vr_soma_num_realz_pond_v_p
                                ,sum(a.vlr_premio)            vlr_soma_premio_v_p
                                ,sum(a.vlr_premio_func_calc)  vlr_soma_premio_func_calc_v_p
                            into v_vr_soma_num_realz_pond_v_p
                                ,vlr_soma_premio_v_p
                                ,vlr_soma_premio_func_calc_v_p
                            from srv_realizado_func_indicador a
                                ,srv_indicador                b
                           where a.cod_indic                = b.cod_indic
                             and a.cod_emp                  = r_fil.cod_emp
                             and a.cod_fil                  = r_fil.cod_fil
                             and a.cod_func                 = r_func_cargo.cod_func
                             and a.num_ano                  = p_num_ano
                             and a.num_mes                  = p_num_mes
                             and b.descr_indic              in ('VENDAS', 'AGRUPAMENTO PSF_LOJA LIDER');
                      exception
                         when no_data_found then
                            v_vr_soma_num_realz_pond_v_p  := 0;
                            vlr_soma_premio_v_p           := 0;
                            vlr_soma_premio_func_calc_v_p := 0;
                         when others then
                            --
                            v_vr_soma_num_realz_pond_v_p  := 0;
                            vlr_soma_premio_v_p           := 0;
                            vlr_soma_premio_func_calc_v_p := 0;
                            --
                            p_cod_erro     := 1;
                            p_descr_erro   := 'Erro ao selecionar a soma dos indicadores VENDAS E AGRUPAMENTO PSF ' ||
                                              ' para o indic sistemico AGRUPA_REM_LOJA_LID para os cargos com Agrupamento Filiais '   ||
                                              ' cod_fil -  '                                         || r_fil.cod_fil ||
                                              ' indic -  '                                           || 'AGRUPA_REM_LOJA_LID' ||
                                              ' cod_func - '                                         || r_func_cargo.cod_func ||
                                              ' erro - '                                             || sqlerrm;


                            -- enviar email
                            v_body       := p_cod_erro || ' - ' || p_descr_erro;

                            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                    ,p_body    => v_body
                                                                                    );

                            -- logar tabela
                            v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );

                            if v_ins_log_erro is not null then
                               -- enviar email
                               v_body       := v_ins_log_erro;
                               --
                               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                       ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                       ,p_body    => v_body
                                                                                       );
                            --v_ins_log_erro is not null
                            end if;

  --                          raise e_erro;

                      --somar os indicadores VENDAS E AGRUPAMENTO PSF
                      end;

                       -- aplicar a escala do indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       p_cod_erro         := null;
                       p_descr_erro       := null;
                       --
                       PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_sistemico.cod_indic     -- p_cod_indic
                                          ,null                            -- p_cod_grp_indic
                                          ,null                            -- p_cod_grp_rem_var
                                          ,v_vr_soma_num_realz_pond_v_p
                                          ,v_cod_escala
                                          ,v_num_seq_escala_fx
                                          ,v_num_realz_fx
                                          ,v_cod_un_realz_fx
                                          ,v_flg_pct_100
                                          ,v_num_limite_fx
                                          ,p_cod_erro
                                          ,p_descr_erro
                                          );

                       if p_cod_erro is not null then
                          --
                          v_num_realz_fx := 0;

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

  --                        raise e_erro;
                       -- erro ao calcular
                       end if;

                       -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                       -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                       if (nvl(v_flg_pct_100, 'N') = 'S' and v_vr_soma_num_realz_pond_v_p > 100) then
                          --
                          v_num_realz_fx := v_vr_soma_num_realz_pond_v_p;
                       end if;

                       -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                       -- entao assumir o valor limite
                       if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                          --
                          v_num_realz_fx := v_num_limite_fx;
                       end if;

                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao
                       -- para o cargo e indicador VENDAS
                       -- gravar o vlr premio calc na realz func para o indicador VENDAS
                       begin
                          update srv_realizado_func_indicador a
                             set a.vlr_premio_func_calc = (a.vlr_premio * v_num_realz_fx)/100
                           where a.cod_indic            = (select cod_indic
                                                             from srv_indicador
                                                            where descr_indic = 'VENDAS')
                             and a.cod_func             = r_func_cargo.cod_func
                             and a.cod_emp              = r_fil.cod_emp
                             and a.cod_fil              = r_fil.cod_fil
                             and a.num_ano              = p_num_ano
                             and a.num_mes              = p_num_mes;
                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao atualizar na srv_realizado_func_indicador o vlr premio calc para o indicador VENDAS - Lideres Regionais e Nacionais ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao - VENDAS
                       end;

                       -- pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao
                       -- para o cargo e grupo de indicadores PSF
                       -- gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO PSF_LOJA LIDER
                       begin
                          update srv_realizado_func_indicador a
                             set a.vlr_premio_func_calc = (a.vlr_premio * v_num_realz_fx)/100
                           where a.cod_indic            = (select cod_indic
                                                             from srv_indicador
                                                            where descr_indic = 'AGRUPAMENTO PSF_LOJA LIDER')
                             and a.cod_func             = r_func_cargo.cod_func
                             and a.cod_emp              = r_fil.cod_emp
                             and a.cod_fil              = r_fil.cod_fil
                             and a.num_ano              = p_num_ano
                             and a.num_mes              = p_num_mes;


                       --
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao atualizar na srv_realizado_func_indicador o vlr premio calc para o indicador AGRUPAMENTO PSF_LOJA LIDER  - Lideres Regionais e Nacionais ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --pegar o result da fx da escala e multiplicar pelo vlr do premio da ponderacao - AGRUPAMENTO PSF_LOJA LIDER
                       end;

                       -- gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       begin
                          vlr_soma_premio_func_calc_v_p := (vlr_soma_premio_v_p * v_num_realz_fx)/100;
                       exception
                          when others then
                             p_cod_erro     := 1;
                             p_descr_erro   := 'Erro ao calcular soma do premio calculado para Agrupamento Rem Loja Lider - Lider Regional / Nacional ' ||
                                               ' cod_fil:  '    || v_cod_fil   ||
                                               ' cod_indic:  '  || v_cod_indic ||
                                               ' cod_func:  '   || v_cod_func ||
                                               ' erro: '        || sqlerrm;

                             -- enviar email
                             v_body       := p_cod_erro || ' - ' || p_descr_erro;

                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                     ,p_body    => v_body
                                                                                     );

                             -- logar tabela
                             v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                       ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                       ,p_num_ano     => p_num_ano
                                                                       ,p_num_mes     => p_num_mes
                                                                       ,p_cod_fil     => v_cod_fil
                                                                       ,p_cod_func    => v_cod_func
                                                                       ,p_cod_indic   => v_cod_indic
                                                                       ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                       );

                             if v_ins_log_erro is not null then
                                -- enviar email
                                v_body       := v_ins_log_erro;
                                --
                                v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                        ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                        ,p_body    => v_body
                                                                                        );
                             --v_ins_log_erro is not null
                             end if;

  --                           raise e_erro;
                       --gravar o vlr premio calc na realz func para o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       end;

                       -- inserir o indicador AGRUPAMENTO REMUNERACAO LOJA LIDER
                       -- gravar na srv_realizado_func_indicador
                       rec_srv_realizado_func_indic.cod_func                := r_func_cargo.cod_func;
                       rec_srv_realizado_func_indic.cod_cargo               := r_func_cargo.cod_cargo;
                       rec_srv_realizado_func_indic.cod_indic               := r_indic_sistemico.cod_indic;
                       rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                       rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                       rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                       rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                       rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                       rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                       rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                       rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                       rec_srv_realizado_func_indic.cod_pond                := null;
                       rec_srv_realizado_func_indic.num_peso                := null;
                       rec_srv_realizado_func_indic.cod_un_peso             := null;
                       rec_srv_realizado_func_indic.num_realz_pond          := v_vr_soma_num_realz_pond_v_p;
                       rec_srv_realizado_func_indic.cod_un_realz_pond       := 2; -- unidade PERCENTUAL

                       rec_srv_realizado_func_indic.num_meta                := null;
                       rec_srv_realizado_func_indic.cod_un_meta             := null;
                       rec_srv_realizado_func_indic.num_realz               := null;
                       rec_srv_realizado_func_indic.cod_un_realz            := null;
                       rec_srv_realizado_func_indic.num_realz_x_meta        := 0;
                       rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                       rec_srv_realizado_func_indic.vlr_premio              := vlr_soma_premio_v_p;
                       rec_srv_realizado_func_indic.vlr_premio_func_calc    := vlr_soma_premio_func_calc_v_p;
                       rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                       rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                       rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                       rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;
                       rec_srv_realizado_func_indic.seg_fil                 := null;

                       -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                       prc_ins_realz_func_indic_ultfi (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                      ,p_cod_erro                  => p_cod_erro
                                                      ,p_descr_erro                => p_descr_erro
                                                       );

                       --
                       if p_cod_erro is not null then

                          -- enviar email
                          v_body       := p_cod_erro || ' - ' || p_descr_erro;

                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                  ,p_body    => v_body
                                                                                  );

                          -- logar tabela
                          v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                    ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                    ,p_num_ano     => p_num_ano
                                                                    ,p_num_mes     => p_num_mes
                                                                    ,p_cod_fil     => v_cod_fil
                                                                    ,p_cod_func    => v_cod_func
                                                                    ,p_cod_indic   => v_cod_indic
                                                                    ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                    );

                          if v_ins_log_erro is not null then
                             -- enviar email
                             v_body       := v_ins_log_erro;
                             --
                             v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                     ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                     ,p_body    => v_body
                                                                                     );
                          --v_ins_log_erro is not null
                          end if;

   --                       raise e_erro;
                       -- erro ao inserir realizado func indicador
                        end if;

                    -- c_func_cargo
                    end loop;

                 --
                 exception
                    /*when e_erro then
                       raise e_erro;*/
                    when others then
                       p_cod_erro     := 1;
                       p_descr_erro   := 'Erro Geral ao calcular AGRUPAMENTO REM LOJA LIDER DOS LIDERES REGIONAIS / NACIONAIS ' ||
                                         ' cod_fil:  '    || v_cod_fil   ||
                                         ' cod_indic:  '  || v_cod_indic ||
                                         ' cod_cargo:  '  || v_cod_cargo ||
                                         ' cod_func:  '   || v_cod_func  ||
                                         ' erro: '        || sqlerrm;

                       -- enviar email
                       v_body       := p_cod_erro || ' - ' || p_descr_erro;

                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                               ,p_body    => v_body
                                                                               );

                       -- logar tabela
                       v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

                       if v_ins_log_erro is not null then
                          -- enviar email
                          v_body       := v_ins_log_erro;
                          --
                          v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                                  ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                  ,p_body    => v_body
                                                                                  );
                       --v_ins_log_erro is not null
                       end if;

  --                     raise e_erro;
                 --
                 end;

              -- c_cargo_grp_rem_var
              end loop;

              -- commit por indicador
              commit;

           -----------------------------------------------------
           -- c_indic_sistemico AGRUPAMENTO LOJA LIDER
           -----------------------------------------------------
           end loop;

        ----------------------------------------------------
        -- c_fil
        ----------------------------------------------------
        end loop;

        commit;

      Exception
        When others then
          null;
      end;

   exception
      when e_erro then

         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                 ,p_body    => v_body
                                                                 );

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => v_cod_func
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );
         --v_ins_log_erro is not null
         end if;

         --
         return;
      when others then
         p_cod_erro     := 1;
         p_descr_erro   := 'Erro geral na procedure prc_calc_realz_Func_Indic_Lj: Calculo do realizado Funcionario Indicador Loja: ' ||
                           'cod_fil: '           || v_cod_fil             || ' - ' ||
                           'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                           'cod_indic: '         || v_cod_indic           || ' - ' ||
                           'cod_cargo: '         || v_cod_cargo           || ' - ' ||
                           'cod_func: '          || v_cod_func            || ' - ' || sqlerrm;

         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception geral: '
                                                                 ,p_body    => v_body
                                                                 );

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => v_cod_func
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 69 -- SRV - PRC_CALC_REALZ_FUNC_INDIC_LJ
                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );
         --v_ins_log_erro is not null
         end if;


   end prc_calc_realz_Func_Indic_Lj;



   -----------------------------------------------------------------------------------------------
   -- CALCULA REALIZADO FUNCIONARIO SAX
   -----------------------------------------------------------------------------------------------
   procedure prc_calc_realz_Func_Indic_SAX (p_num_ano      in number
                                           ,p_num_mes      in number
                                           ,p_cod_emp      in number     default null
                                           ,p_cod_fil      in number     default null
                                           ,p_cod_indic    in number     default null
                                           ,p_descr_indic  in varchar2   default null
                                           ,p_cod_usuario  in number
                                           ,p_cod_erro     out number
                                           ,p_descr_erro   out varchar2
                                            ) is

      -- FILIAIS
      cursor c_fil (p_cod_emp number
                   ,p_cod_fil number) is
         select distinct f.cod_emp
                        ,f.cod_fil
                        ,0 as mes_inauguracao
                        ,f.cod_tipo_fil
           from srv_filial               f
               ,srv_realizado_filial     r
          where f.cod_emp              = r.cod_emp
            and f.cod_fil              = r.cod_fil
            and f.flg_ativ             = 'S'
            and (f.cod_emp             = p_cod_emp or p_cod_emp is null)
            and (f.cod_fil             = p_cod_fil or p_cod_fil is null)
            and r.num_ano              = p_num_ano
            and r.num_mes              = p_num_mes
       order by f.cod_fil;



      -- REMUNERACAO LOJAS
      cursor c_indic_rem_loja is
         select t.cod_tipo_rem_var
               ,t.descr_tipo_rem_var
               ,g.cod_grp_indic
               ,g.descr_grp_indic
               ,i.cod_indic
               ,i.descr_indic
           from srv_tipo_rem_var        t
               ,srv_grupo_indicador     g
               ,srv_indicador           i
          where t.cod_tipo_rem_var    = g.cod_tipo_rem_var
            and t.descr_tipo_rem_var  = 'REMUNERACAO_LOJA'
            and g.cod_grp_indic       = i.cod_grp_indic
            and i.cod_grp_indic       in (4,5,9,3) -- EP SAX , Seguro SAX e Indicacao EP
            and i.cod_indic_sis       = 'SAX_PSF'
            and (i.descr_indic        = p_descr_indic or p_descr_indic is null)
            and i.flg_indic_ativ      = 'S'
       order by g.cod_grp_indic
               ,i.cod_indic;



      -- var controle
      v_var_sql                        varchar2(4000);

      -- variaveis
      v_cod_grp_rem_var               srv_grupo_rem_variavel.cod_grp_rem_var%type;

      v_vlr_premio_fil_calc           srv_realizado_filial.vlr_premio_fil_calc%type;
      v_meta                          srv_realizado_filial.num_meta%type;
      v_cod_un_meta                   srv_realizado_filial.cod_un_meta%type;
      v_num_realz_fil                 srv_realizado_filial.num_realz%type;
      v_cod_un_realz                  srv_realizado_filial.cod_un_realz%type;
      v_qtd_realz                     srv_realizado_filial.qtd_realz%type;
      v_num_realz_x_meta              srv_realizado_filial.num_realz_x_meta%type;
      v_cod_un_realz_x_meta           srv_realizado_filial.cod_un_realz_x_meta%type;

      v_cod_pond                      srv_ponderacao.cod_pond%type;
      v_num_peso                      srv_ponderacao.num_peso%type;
      v_cod_un_peso                   srv_ponderacao.cod_un_peso%type;
      v_vlr_premio                    srv_ponderacao.vlr_premio%type;

      v_vlr_premio_func_calc          srv_realizado_func_indicador.vlr_premio_func_calc%type;
      v_num_realz_pond                srv_realizado_func_indicador.num_realz_pond%type;
      v_num_realz_func                srv_realizado_func_indicador.num_realz%type;

      v_cod_func                      srv_funcionario.cod_func%type;
      v_cod_cargo_func                srv_funcionario.cod_cargo%type;
      v_quant_indic_func              srv_realizado_func_indicador.num_realz%type;

      -- indicacoes
      v_vr_desc_indic_func            srv_realizado_func_indicador.vlr_premio_func_calc%type;
      v_cod_grp_indic_indicacao       srv_grupo_indicador.cod_grp_indic%type;
      v_cod_grp_rem_var_indicacao     srv_grupo_rem_variavel.cod_grp_rem_var%type;
      v_cod_pond_indicacao            srv_ponderacao.cod_pond%type;
      v_num_peso_indicacao            srv_ponderacao.num_peso%type;
      v_cod_un_peso_indicacao         srv_ponderacao.cod_un_peso%type;
      v_num_realz_pond_indicacao      srv_realizado_func_indicador.num_realz_pond%type;
      v_vlr_premio_indicacao          srv_ponderacao.vlr_premio%type;
      --v_num_realz_x_meta_limitador    srv_realizado_filial.num_realz_x_meta%type;

      v_cod_escala                    srv_escala_faixa.cod_escala%type;
      v_num_seq_escala_fx             srv_escala_faixa.num_seq_escala_fx%type;
      v_num_realz_fx                  srv_escala_faixa.num_realz_fx%type;
      v_cod_un_realz_fx               srv_escala_faixa.cod_un_realz_fx%type;
      v_flg_pct_100                   srv_escala_faixa.flg_pct_100%type;
      v_num_limite_fx                 srv_escala_faixa.num_limite_fx%type;

      v_nm_tab_realz_fu                varchar2(30);
      v_nm_tab_realz_aux               varchar2(30);
      v_nm_tab_realz_fi                varchar2(30);
      v_mes                            number;

      v_cod_grp_indic                 srv_grupo_indicador.cod_grp_indic%type;

      -- var log
      v_cod_fil                        srv_filial.cod_fil%type;
      v_cod_indic                      srv_indicador.cod_indic%type;
      v_descr_indic                    srv_indicador.descr_indic%type;
      v_cod_cargo                      srv_cargo.cod_cargo%type;


      -- records
      rec_srv_realizado_func_indic     srv_realizado_func_indicador%rowtype;

      -- cursores
      cur_realz                        pkg_srv_calc_rem_var.typ_cursor;





   begin

      v_data    := sysdate;
      --
      v_dt_ini  := '01/' || trim(to_char(p_num_mes, '00')) || '/' || trim(to_char(p_num_ano, '0000')) || ' ' || '00:00:00';
      --
      select to_char(last_day(to_date(('01/' || to_char(p_num_mes, '00') || '/'||to_char(p_num_ano, '0000')), 'dd/mm/yyyy')), 'dd/mm/yyyy')|| ' ' || '23:59:59'
        into v_dt_fim
        from dual;

      -----------------------------------------------------------------------
      -- SELECIONA FILIAIS ATIVAS
      -----------------------------------------------------------------------
      for r_fil in c_fil (p_cod_emp
                         ,p_cod_fil) loop


         v_cod_fil          := r_fil.cod_fil;
         v_mes              := r_fil.mes_inauguracao;

         -----------------------------------------------------------------------
         -- SELECIONA INDICADORES PARA O GRUPO DE REMUNERACAO LOJAS
         -----------------------------------------------------------------------
         for r_indic_rem_loja in c_indic_rem_loja loop

            v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
            v_cod_indic        := r_indic_rem_loja.cod_indic;


            ---------------------------------------------------------------------------
            -- SELECIONA META E REALIZADO DA FILIAL PARA EP SAX
            -- E PARA INDICACAO EP SAX E O MESMO DO EP SAX
            --------------------------------------------------------------------------
            if r_indic_rem_loja.descr_indic in ('EMPRESTIMO PESSOAL (EP) SAX / PSF'
                                               ,'INDICACAO EMPRESTIMO PESSOAL (EP) SAX / PSF') then

               -- seleciona realz X meta e vlr premio calculado da filial
               -- SEMPRE do indicador SAX EP
               begin
                  v_descr_indic := 'EMPRESTIMO PESSOAL (EP) SAX';
                  --v_descr_indic := r_indic_rem_loja.descr_indic;

                  select a.vlr_premio_fil_calc
                        ,a.num_meta
                        ,a.cod_un_meta
                        ,a.num_realz
                        ,a.cod_un_realz
                        ,a.qtd_realz
                        ,a.num_realz_x_meta
                        ,a.cod_un_realz_x_meta
                    into v_vlr_premio_fil_calc
                        ,v_meta
                        ,v_cod_un_meta
                        ,v_num_realz_fil
                        ,v_cod_un_realz
                        ,v_qtd_realz
                        ,v_num_realz_x_meta
                        ,v_cod_un_realz_x_meta
                    from srv_realizado_filial a
                        ,srv_indicador        b
                   where a.cod_indic        = b.cod_indic
                     and a.cod_emp          = r_fil.cod_emp
                     and a.cod_fil          = r_fil.cod_fil
                     and b.descr_indic      = v_descr_indic --r_indic_rem_loja.descr_indic
                     and a.num_ano          = p_num_ano
                     and a.num_mes          = p_num_mes;
               exception
                  when no_data_found then
                     v_vlr_premio_fil_calc := 0;
                     v_meta                := 0;
                     v_cod_un_meta         := null;
                     v_num_realz_fil       := 0;
                     v_cod_un_realz        := null;
                     v_qtd_realz           := 0;
                     v_num_realz_x_meta    := 0;
                     v_cod_un_realz_x_meta := null;
                  when others then
                     --
                     v_vlr_premio_fil_calc := 0;
                     v_meta                := 0;
                     v_cod_un_meta         := null;
                     v_num_realz_fil       := 0;
                     v_cod_un_realz        := null;
                     v_qtd_realz           := 0;
                     v_num_realz_x_meta    := 0;
                     v_cod_un_realz_x_meta := null;

                     --
                     p_cod_erro     := 1;
                     p_descr_erro   := 'Erro ao selecionar a tabela srv_realizado_filial ' ||
                                       ' cod_fil -  '                                       || r_fil.cod_fil ||
                                       ' descr_indic -  '                                   || v_descr_indic || --r_indic_rem_loja.descr_indic ||
                                       ' erro - '                                           || sqlerrm;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                     raise e_erro;
               -- seleciona realz X meta e vlr premio calculado da filial
               end;

            -- if indicador EP SAX OU INDICACAO EP SAX
            end if;

            --------------------------------------------------------
            -- SE INDICADOR EMPRESTIMO PESSOAL (EP) SAX
            --------------------------------------------------------
            if r_indic_rem_loja.descr_indic in ('EMPRESTIMO PESSOAL (EP) SAX / PSF') then

               --------------------------------------------------------------------------
               -- CARGOS OPERACIONAIS - EMPRESTIMO PESSOAL (EP) SAX
               --------------------------------------------------------------------------
               begin
                  -- seleciona o cod grupo rem var para OPERACIONAL_SAX EP
                  select a.cod_grp_rem_var
                    into v_cod_grp_rem_var--7
                    from srv_grupo_rem_variavel a
                   where a.descr_grp_rem_var = 'OPERACIONAL_SAX_EP';

                  -- selecionar a ponderacao para o grupo rem var e para o indicador
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                      ,p_cod_grp_indic     => (case when r_indic_rem_loja.cod_grp_indic = 3 then 4 end)
                                      ,p_cod_cargo         => null
                                      ,p_cod_grp_rem_var   => v_cod_grp_rem_var
                                      ,p_cod_pond          => v_cod_pond
                                      ,p_num_peso          => v_num_peso
                                      ,p_cod_un_peso       => v_cod_un_peso
                                      ,p_vlr_premio        => v_vlr_premio
                                      ,p_cod_erro          => p_cod_erro
                                      ,p_descr_erro        => p_descr_erro
                                      );
                  --
                  if  p_cod_erro is not null then

                     --
                     v_num_peso   := 0;
                     v_vlr_premio := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                      raise e_erro;
                  -- erro o calcular ponderacao
                  end if;



                  -----------------------------------------------------------------------
                  -- SELECIONA A O VALOR DO PREMIO DA INDICACAO
                  -----------------------------------------------------------------------
                  -- seleciona o cod grupo rem var para OPERACIONAL_SAX_INDICACAO_EP
                  begin
                     select a.cod_grp_rem_var
                       into v_cod_grp_rem_var_indicacao
                       from srv_grupo_rem_variavel a
                      where a.descr_grp_rem_var = 'OPERACIONAL_SAX_INDICACAO_EP'; -- 12
                  exception
                     when others then
                        v_cod_grp_rem_var_indicacao := 12;
                  end;
                  --
                  begin
                     select cod_grp_indic
                       into v_cod_grp_indic_indicacao
                       from srv_grupo_indicador
                      where descr_grp_indic  = 'INDICACAO_EMPRESTIMO_PESSOAL_SAX'; -- 9
                  exception
                     when others then
                        v_cod_grp_indic_indicacao := 9;
                  end;

                  -- selecionar a ponderacao para o grupo rem var e para o indicador
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                      ,p_cod_grp_indic     => v_cod_grp_indic_indicacao
                                      ,p_cod_cargo         => null
                                      ,p_cod_grp_rem_var   => v_cod_grp_rem_var_indicacao
                                      ,p_cod_pond          => v_cod_pond_indicacao
                                      ,p_num_peso          => v_num_peso_indicacao
                                      ,p_cod_un_peso       => v_cod_un_peso_indicacao
                                      ,p_vlr_premio        => v_vlr_premio_indicacao
                                      ,p_cod_erro          => p_cod_erro
                                      ,p_descr_erro        => p_descr_erro
                                      );
                  --
                  if  p_cod_erro is not null then

                     --
                     v_num_peso   := 0;
                     v_vlr_premio := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => 54 --v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                      raise e_erro;
                  -- erro o calcular ponderacao INDICACAO
                  end if;


                  ------------------------
                  -- SELECIONAR A ESCALA
                  ------------------------
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Escala_Fx (null                           -- ALT201402 p_cod_indic
                                     ,null                           --p_cod_grp_indic
                                     ,v_cod_grp_rem_var              --p_cod_grp_rem_var
                                     ,v_num_realz_x_meta             --p_num_realz_x_meta
                                     ,v_cod_escala
                                     ,v_num_seq_escala_fx
                                     ,v_num_realz_fx
                                     ,v_cod_un_realz_fx
                                     ,v_flg_pct_100
                                     ,v_num_limite_fx
                                     ,p_cod_erro
                                     ,p_descr_erro
                                     );
                  if p_cod_erro is not null then

                     --
                     v_num_realz_fx := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                     raise e_erro;
                  -- erro ao calcular escala
                  end if;

                  -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                  -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                  if (nvl(v_flg_pct_100, 'N') = 'S' and v_num_realz_x_meta > 100) then
                     --
                     v_num_realz_fx := v_num_realz_x_meta;
                  end if;

                  -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                  -- entao assumir o valor limite
                  if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx and v_mes <= 6 then
                     --
                     v_num_realz_fx := v_num_limite_fx;
                  end if;

                  -- calcula valor unitario para multiplicar pela qtde
                  begin
                     v_num_realz_pond            := (v_vlr_premio           * v_num_realz_fx)/100;
                     v_num_realz_pond_indicacao  := (v_vlr_premio_indicacao * v_num_realz_fx)/100;

                  --
                  exception
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao calcular num realz pond para EP Sax Cargos Operacionais' ||
                                          ' cod_fil:  '    || v_cod_fil   ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                          ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;

--                        raise e_erro;
                  --calcula valor unitario para multiplicar pela qtde
                  end;


                  begin
                     -- tabela gerada de apuracao realizado para o indicador
                     select a.nm_tab_realz_fil  ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                           ,a.nm_tab_realz_func ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                       into v_nm_tab_realz_fi
                           ,v_nm_tab_realz_fu
                       from srv_realizado_tab_tmp a
                      where a.cod_indic         = r_indic_rem_loja.cod_indic;

                     v_nm_tab_realz_aux := 'SRV_REALZFU_SAQUE_FACIL'||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'));

                     -- selecionar todos os funcionarios dos cargos do grupo de remuneracao SAX EP para a filial,
/*
                     v_var_sql := ' select b.cod_func,
                                           b.cod_cargo,
                                           a.qtde,
                                           a.quant_indicacoes
                                      from (select x1.cpf_vendedor, sum(x1.qtde) as qtde, sum(x1.quant_indicacoes) as quant_indicacoes from 
                                             (select a1.cpf_vendedor, 
                                                     nvl(count(a1.cod_contrato), 0) as qtde,
                                                     nvl(f1.quant_indicacoes, 0) as quant_indicacoes
                                                from '||v_nm_tab_realz_fu||' a1,
                                                     (select g1.fil_cod,
                                                             g1.cpf_vendedor,
                                                             nvl(count(g1.cpf_usu_indicador), 0) as quant_indicacoes
                                                        from '||v_nm_tab_realz_fu||' g1
                                                       where g1.cpf_usu_indicador is not null
                                                         and g1.cpf_usu_indicador <> g1.cpf_vendedor
                                                       group by g1.fil_cod, g1.cpf_vendedor) f1
                                              where a1.cpf_vendedor = f1.cpf_vendedor(+)
                                                and a1.flg_doc_digitalizado = ''S''
                                              group by a1.cpf_vendedor, nvl(f1.quant_indicacoes, 0)
                                              UNION ALL 
                                              select a1.cpf_vendedor, 
                                                     nvl(count(*), 0) as qtde,
                                                     0
                                                from '||v_nm_tab_realz_aux||' a1
                                              group by a1.cpf_vendedor) x1
                                            group by x1.cpf_vendedor ) a,
                                           srv_funcionario b,
                                           srv_cargo c,
                                           srv_grupo_cargo d,
                                           srv_grupo_rem_variavel e
                                     where a.cpf_vendedor = b.num_cpf_func
                                       and b.cod_cargo = c.cod_cargo
                                       and c.cod_cargo = d.cod_cargo
                                       and d.cod_grp_rem_var = e.cod_grp_rem_var
                                       and nvl(c.flg_agrupa_fil_lider, ''N'') = ''N''
                                       and e.cod_grp_rem_var = '||v_cod_grp_rem_var||'
                                       and b.cod_fil = '||r_fil.cod_fil||'
                                       and b.dt_admissao <= last_day(to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'23:59:59'',''dd/mm/yyyy hh24:mi:ss''))
                                       and (b.dt_demissao is null OR b.dt_demissao > to_date(''01/'||trim(to_char(p_num_mes, '00'))||'/'||trim(to_char(p_num_ano, '0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss''))
                                       and (b.cod_sit_rh = 1
                                           OR nvl(b.qtd_dias_trab_per, 0) > 0
                                           OR
                                           (b.cod_sit_rh = 9
                                           OR (nvl(b.cod_sit_rh_ant, 1) = 9
                                           and nvl(b.dt_ini_sit_rh, to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'', ''dd/mm/yyyy hh24:mi:ss'')) >= to_date(''02/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'', ''dd/mm/yyyy hh24:mi:ss''))))';
*/

                     v_var_sql := ' select b.cod_func,
                                           b.cod_cargo,
                                           a.qtde,
                                           a.quant_indicacoes
                                      from (select x1.cpf_vendedor, sum(x1.qtde) as qtde, sum(x1.quant_indicacoes) as quant_indicacoes from 
                                             (select a1.cpf_vendedor, 
                                                     nvl(count(a1.cod_contrato), 0) as qtde,
                                                     nvl(f1.quant_indicacoes, 0) as quant_indicacoes
                                                from '||v_nm_tab_realz_fu||' a1,
                                                     (select g1.fil_cod,
                                                             g1.cpf_vendedor,
                                                             nvl(count(g1.cpf_usu_indicador), 0) as quant_indicacoes
                                                        from '||v_nm_tab_realz_fu||' g1
                                                       where g1.cpf_usu_indicador is not null
                                                         and g1.cpf_usu_indicador <> g1.cpf_vendedor
                                                       group by g1.fil_cod, g1.cpf_vendedor) f1
                                              where a1.cpf_vendedor = f1.cpf_vendedor(+)
                                                and a1.flg_doc_digitalizado = ''S''
                                              group by a1.cpf_vendedor, nvl(f1.quant_indicacoes, 0)) x1
                                            group by x1.cpf_vendedor ) a,
                                           srv_funcionario b,
                                           srv_cargo c,
                                           srv_grupo_cargo d,
                                           srv_grupo_rem_variavel e
                                     where a.cpf_vendedor = b.num_cpf_func
                                       and b.cod_cargo = c.cod_cargo
                                       and c.cod_cargo = d.cod_cargo
                                       and d.cod_grp_rem_var = e.cod_grp_rem_var
                                       and nvl(c.flg_agrupa_fil_lider, ''N'') = ''N''
                                       and e.cod_grp_rem_var = '||v_cod_grp_rem_var||'
                                       and b.cod_fil = '||r_fil.cod_fil||'
                                       and b.dt_admissao <= last_day(to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'23:59:59'',''dd/mm/yyyy hh24:mi:ss''))
                                       and (b.dt_demissao is null OR b.dt_demissao > to_date(''01/'||trim(to_char(p_num_mes, '00'))||'/'||trim(to_char(p_num_ano, '0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss''))
                                       and (b.cod_sit_rh = 1
                                           OR nvl(b.qtd_dias_trab_per, 0) > 0
                                           OR
                                           (b.cod_sit_rh = 9
                                           OR (nvl(b.cod_sit_rh_ant, 1) = 9
                                           and nvl(b.dt_ini_sit_rh, to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'', ''dd/mm/yyyy hh24:mi:ss'')) >= to_date(''02/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'', ''dd/mm/yyyy hh24:mi:ss''))))';

                     open cur_realz for v_var_sql;
                     loop
                        fetch cur_realz
                         into  v_cod_func
                              ,v_cod_cargo_func
                              ,v_num_realz_func
                              ,v_quant_indic_func;

                        --
                        exit when cur_realz%notfound;


                        --
                        v_cod_un_realz := 3; --unidade UNIDADE

                        -- calcula o valor das indicacoes para subtrair do valor do cpf_vendedor
                        v_vr_desc_indic_func    := v_quant_indic_func * v_num_realz_pond_indicacao; --v_vlr_premio_indicacao;
                        --
                        v_vlr_premio_func_calc  := v_num_realz_func   * v_num_realz_pond;

                        -- subtrair o valor do descontos do valor das indicacoes
                        v_vlr_premio_func_calc := v_vlr_premio_func_calc - v_vr_desc_indic_func;
                        -- se valor negativo considerar zero
                        if v_vlr_premio_func_calc < 0 then
                           v_vlr_premio_func_calc := 0;
                        end if;

                        -- gravar na srv_realizado_func_indicador
                        rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                        rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                        rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                        rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                        rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                        -- indicacoes
                        rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                        rec_srv_realizado_func_indic.vlr_desc_indicacao      := v_vr_desc_indic_func;
                        rec_srv_realizado_func_indic.qtd_desc_indicacao      := v_quant_indic_func;
                        --
                        rec_srv_realizado_func_indic.cod_func                := v_cod_func;
                        rec_srv_realizado_func_indic.cod_cargo               := v_cod_cargo_func;
                        rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                        rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                        rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                        rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                        rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                        rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                        rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                        rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                        rec_srv_realizado_func_indic.cod_un_realz_pond       := 1; -- unidade VALOR

                        rec_srv_realizado_func_indic.num_meta                := v_meta;
                        rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                        rec_srv_realizado_func_indic.num_realz               := v_num_realz_func;
                        rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                        rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                        rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                        rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                        rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                        rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                        rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                        rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;


                        -- inserir o realizado do funcionario pelo indicador
                        PKG_SRV_GERAL.Prc_Insere_Realz_Func_Indic (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                    ,p_cod_erro                  => p_cod_erro
                                                    ,p_descr_erro                => p_descr_erro
                                                     );
                        --
                        if p_cod_erro is not null then

                           -- enviar email
                           v_body       := p_cod_erro || ' - ' || p_descr_erro;

                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                   ,p_body    => v_body
                                                                                   );

                           -- logar tabela
                           v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                     ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                     ,p_num_ano     => p_num_ano
                                                                     ,p_num_mes     => p_num_mes
                                                                     ,p_cod_fil     => v_cod_fil
                                                                     ,p_cod_func    => v_cod_func
                                                                     ,p_cod_indic   => v_cod_indic
                                                                     ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                     );

                           if v_ins_log_erro is not null then
                              -- enviar email
                              v_body       := v_ins_log_erro;
                              --
                              v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                      ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                      ,p_body    => v_body
                                                                                      );
                           --v_ins_log_erro is not null
                           end if;

--                           raise e_erro;
                        -- erro ao inserir realizado func indicador
                        end if;

                        --

                     -- cur_venda
                     end loop;
                     --
                     close cur_realz;

                     --
                     commit;

                  --
                  exception
                     /*when e_erro then
                        raise e_erro;*/
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao inserir Emprestimo Pessoal SAX Cargos Operacionais' ||
                                          ' cod_fil:  '    || v_cod_fil   ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                          ' cod_func:  '   || v_cod_func  ||
                                          ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;


--                        raise e_erro;
                  --
                  end;

               --
               exception
                  /*when e_erro then
                     raise e_erro;*/
                  when others then
                     p_cod_erro     := 1;
                     p_descr_erro   := 'Erro Geral ao calcular indicador Emprestimo Pessoal SAX Cargos Operacionais' ||
                                       ' cod_fil:  '    || v_cod_fil   ||
                                       ' cod_indic:  '  || v_cod_indic ||
                                       ' erro: '        || sqlerrm;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

               end;


            --------------------------------------------------------
            -- SE INDICADOR INDICACAO EMPRESTIMO PESSOAL (EP) SAX
            --------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic in('INDICACAO EMPRESTIMO PESSOAL (EP) SAX / PSF') then

               --------------------------------------------------------------------------
               -- CARGOS OPERACIONAIS - INDICACAO EMPRESTIMO PESSOAL (EP) SAX
               --------------------------------------------------------------------------
               begin
                  -- seleciona o cod grupo rem var para OPERACIONAL_SAX_INDICACAO_EP
                  select a.cod_grp_rem_var
                    into v_cod_grp_rem_var
                    from srv_grupo_rem_variavel a
                   where a.descr_grp_rem_var = 'OPERACIONAL_SAX_INDICACAO_EP'; -- 12

                  -- selecionar a ponderacao para o grupo rem var e para o indicador
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                      ,p_cod_grp_indic     => (case when r_indic_rem_loja.cod_grp_indic = 3 then 9 end)
                                      ,p_cod_cargo         => null
                                      ,p_cod_grp_rem_var   => v_cod_grp_rem_var
                                      ,p_cod_pond          => v_cod_pond
                                      ,p_num_peso          => v_num_peso
                                      ,p_cod_un_peso       => v_cod_un_peso
                                      ,p_vlr_premio        => v_vlr_premio
                                      ,p_cod_erro          => p_cod_erro
                                      ,p_descr_erro        => p_descr_erro
                                      );
                  --
                  if  p_cod_erro is not null then

                     --
                     v_num_peso   := 0;
                     v_vlr_premio := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                      raise e_erro;
                  -- erro o calcular ponderacao
                  end if;


                  ------------------------
                  -- SELECIONAR A ESCALA
                  ------------------------
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Escala_Fx (null--r_indic_rem_loja.cod_indic                           -- Alt201402 p_cod_indic
                                     ,null                           --p_cod_grp_indic
                                     ,v_cod_grp_rem_var              --p_cod_grp_rem_var
                                     ,v_num_realz_x_meta             --p_num_realz_x_meta
                                     ,v_cod_escala
                                     ,v_num_seq_escala_fx
                                     ,v_num_realz_fx
                                     ,v_cod_un_realz_fx
                                     ,v_flg_pct_100
                                     ,v_num_limite_fx
                                     ,p_cod_erro
                                     ,p_descr_erro
                                     );
                  if p_cod_erro is not null then

                     --
                     v_num_realz_fx := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                     raise e_erro;
                  -- erro ao calcular escala
                  end if;

                  -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                  -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                  if (nvl(v_flg_pct_100, 'N') = 'S' and v_num_realz_x_meta > 100) then
                     --
                     v_num_realz_fx := v_num_realz_x_meta;
                  end if;

                  -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                  -- entao assumir o valor limite
                  if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                     --
                     v_num_realz_fx := v_num_limite_fx;
                  end if;

                  -- calcula valor unitario para multiplicar pela qtde
                  begin
                     v_num_realz_pond            := (v_vlr_premio           * v_num_realz_fx)/100;
                  --
                  exception
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao calcular num realz pond para INDICACAO EP Sax Cargos Operacionais' ||
                                          ' cod_fil:  '    || v_cod_fil   ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                          ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;

--                        raise e_erro;
                  --calcula valor unitario para multiplicar pela qtde
                  end;


                  begin
                     -- tabela gerada de apuracao realizado para o indicador
                     select a.nm_tab_realz_fil  ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                           ,a.nm_tab_realz_func ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                       into v_nm_tab_realz_fi
                           ,v_nm_tab_realz_fu
                       from srv_realizado_tab_tmp a
                      where a.cod_indic         = r_indic_rem_loja.cod_indic;


                     -- selecionar todos os funcionarios dos cargos do grupo de remuneracao INDICACAO SAX EP para a filial,
                     v_var_sql :=              '  select b.cod_func                                                    ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,b.cod_cargo                                                   ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,nvl(count(a.cod_contrato),0) as qtde                          ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    from ' || v_nm_tab_realz_fu || '  a                                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,srv_funcionario              b                                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,srv_cargo                    c                                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,srv_grupo_cargo              d                                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,srv_grupo_rem_variavel       e                                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '   where a.cpf_usu_indicador              = b.num_cpf_func             ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and b.cod_cargo                      = c.cod_cargo                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and c.cod_cargo                      = d.cod_cargo                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and d.cod_grp_rem_var                = e.cod_grp_rem_var          ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and nvl(c.flg_agrupa_fil_lider, ''N'') = ''N''                    ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and e.cod_grp_rem_var                  =     ' || v_cod_grp_rem_var || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and a.cpf_usu_indicador                is not null                ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and a.cpf_usu_indicador               <> a.cpf_vendedor           ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '     and a.fil_cod                          =         ' || r_fil.cod_fil || chr(13) || chr(10);

                     v_var_sql := v_var_sql || '
             -- funcionario esteve empregado pelo menos 1 dia no mes
            and b.dt_admissao                    <= last_day(to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'23:59:59'',''dd/mm/yyyy hh24:mi:ss''))
            and (b.dt_demissao                    is null
                 OR
                 b.dt_demissao                    > to_date(''01/'||trim(to_char(p_num_mes, '00'))||'/'||trim(to_char(p_num_ano, '0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss'')
                )
            -- situacao do funcionario
            and (b.cod_sit_rh                     = 1 -- Em Atividade
                 OR
                 nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                 OR
                   -- situacao atual Gozando Ferias OU
                   -- situacao anterior Gozando Ferias E
                   -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                 ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                  OR
                  (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                   and
                   nvl(b.dt_ini_sit_rh, to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss''))
                                                  >= to_date(''02/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss'')
                  )
                 )
                )' || chr(13) || chr(10);

                     v_var_sql := v_var_sql || ' group by b.cod_func                                                   ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '         ,b.cod_cargo                                                  ' || chr(13) || chr(10);


                     open cur_realz for v_var_sql;
                     loop
                        fetch cur_realz
                         into  v_cod_func
                              ,v_cod_cargo_func
                              ,v_num_realz_func;

                        --
                        exit when cur_realz%notfound;

                        -- calcula a apuracao feita pelo indicador
                        v_cod_un_realz := 3; --unidade UNIDADE
                        --
                        v_vlr_premio_func_calc                               := v_num_realz_func * v_num_realz_pond;
                        -- se valor negativo considerar zero
                        if v_vlr_premio_func_calc < 0 then
                           v_vlr_premio_func_calc := 0;
                        end if;

                        -- gravar na srv_realizado_func_indicador
                        rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                        rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                        rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                        rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                        rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;

                        rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                        rec_srv_realizado_func_indic.vlr_desc_indicacao      := null;
                        rec_srv_realizado_func_indic.qtd_desc_indicacao      := null;

                        rec_srv_realizado_func_indic.cod_func                := v_cod_func;
                        rec_srv_realizado_func_indic.cod_cargo               := v_cod_cargo_func;
                        rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                        rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                        rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                        rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                        rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                        rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                        rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                        rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                        rec_srv_realizado_func_indic.cod_un_realz_pond       := 1; -- unidade VALOR

                        rec_srv_realizado_func_indic.num_meta                := v_meta;
                        rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                        rec_srv_realizado_func_indic.num_realz               := v_num_realz_func;
                        rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                        rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                        rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                        rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                        rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                        rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                        rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                        rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;


                        -- inserir o realizado do funcionario pelo indicador
                        PKG_SRV_GERAL.Prc_Insere_Realz_Func_Indic (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                    ,p_cod_erro                  => p_cod_erro
                                                    ,p_descr_erro                => p_descr_erro
                                                     );
                        --
                        if p_cod_erro is not null then

                           -- enviar email
                           v_body       := p_cod_erro || ' - ' || p_descr_erro;

                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                   ,p_body    => v_body
                                                                                   );

                           -- logar tabela
                           v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                     ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                     ,p_num_ano     => p_num_ano
                                                                     ,p_num_mes     => p_num_mes
                                                                     ,p_cod_fil     => v_cod_fil
                                                                     ,p_cod_func    => v_cod_func
                                                                     ,p_cod_indic   => v_cod_indic
                                                                     ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                     );

                           if v_ins_log_erro is not null then
                              -- enviar email
                              v_body       := v_ins_log_erro;
                              --
                              v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                      ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                      ,p_body    => v_body
                                                                                      );
                           --v_ins_log_erro is not null
                           end if;

--                           raise e_erro;
                        -- erro ao inserir realizado func indicador
                        end if;

                        --

                     -- cur_venda
                     end loop;
                     --
                     close cur_realz;

                     --
                     commit;

                  --
                  exception
                     /*when e_erro then
                        raise e_erro;*/
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao inserir INDICACAO EP SAX Cargos Operacionais' ||
                                          ' cod_fil:  '    || v_cod_fil   ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                          ' cod_func:  '   || v_cod_func  ||
                                          ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;


--                        raise e_erro;
                  --
                  end;

               --
               exception
                  /*when e_erro then
                     raise e_erro;*/
                  when others then
                     p_cod_erro     := 1;
                     p_descr_erro   := 'Erro Geral ao calcular indicador INDICACAO EP SAX Cargos Operacionais' ||
                                       ' cod_fil:  '    || v_cod_fil   ||
                                       ' cod_indic:  '  || v_cod_indic ||
                                       ' erro: '        || sqlerrm;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                     raise e_erro;
               --
               end;



            --------------------------------------------------------
            -- SE INDICADOR SEGURO EMPRESTIMO PESSOAL (EP) SAX
            --------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic in('SEGURO EMPRESTIMO PESSOAL (EP) SAX / PSF') then

               v_descr_indic := 'SEGURO EMPRESTIMO PESSOAL (EP) SAX';
               -- seleciona realz X meta e vlr premio calculado da filial
               begin
                  select a.vlr_premio_fil_calc
                        ,a.num_meta
                        ,a.cod_un_meta
                        ,a.num_realz
                        ,a.cod_un_realz
                        ,a.qtd_realz
                        ,a.num_realz_x_meta
                        ,a.cod_un_realz_x_meta
                    into v_vlr_premio_fil_calc
                        ,v_meta
                        ,v_cod_un_meta
                        ,v_num_realz_fil
                        ,v_cod_un_realz
                        ,v_qtd_realz
                        ,v_num_realz_x_meta
                        ,v_cod_un_realz_x_meta
                    from srv_realizado_filial a
                        ,srv_indicador        b
                   where a.cod_indic        = b.cod_indic
                     and a.cod_emp          = r_fil.cod_emp
                     and a.cod_fil          = r_fil.cod_fil
                     and b.descr_indic      = v_descr_indic--r_indic_rem_loja.descr_indic
                     and a.num_ano          = p_num_ano
                     and a.num_mes          = p_num_mes;
               exception
                  when no_data_found then
                     v_vlr_premio_fil_calc := 0;
                     v_meta                := 0;
                     v_cod_un_meta         := null;
                     v_num_realz_fil       := 0;
                     v_cod_un_realz        := null;
                     v_qtd_realz           := 0;
                     v_num_realz_x_meta    := 0;
                     v_cod_un_realz_x_meta := null;
                  when others then
                     --
                     v_vlr_premio_fil_calc := 0;
                     v_meta                := 0;
                     v_cod_un_meta         := null;
                     v_num_realz_fil       := 0;
                     v_cod_un_realz        := null;
                     v_qtd_realz           := 0;
                     v_num_realz_x_meta    := 0;
                     v_cod_un_realz_x_meta := null;

                     --
                     p_cod_erro     := 1;
                     p_descr_erro   := 'Erro ao selecionar a tabela srv_realizado_filial ' ||
                                       ' cod_fil -  '                                       || r_fil.cod_fil ||
                                       ' cod_indic -  '                                     || r_indic_rem_loja.descr_indic ||
                                       ' erro - '                                           || sqlerrm;


                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                     raise e_erro;

               --seleciona realz X meta e vlr premio calculado da filial
               end;

               --------------------------------------------------------------------------
               -- CARGOS OPERACIONAIS - SEGURO EMPRESTIMO PESSOAL (EP) SAX
               --------------------------------------------------------------------------
               begin
                  -- seleciona o cod grupo rem var
                  begin
                     select a.cod_grp_rem_var
                      into v_cod_grp_rem_var
                      from srv_grupo_rem_variavel a
                     where a.descr_grp_rem_var = 'OPERACIONAL_SAX_SEGURO (EP)';
                  exception
                     when others then
                        v_cod_grp_rem_var := 11;
                  end;

                  -- selecionar a ponderacao para o cod_grp_rem_var e para o cod_grp_indic
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Ponderacao (p_cod_indic         => null
                                      ,p_cod_grp_indic     => (case when r_indic_rem_loja.cod_grp_indic = 3 then 5 end)
                                      ,p_cod_cargo         => null
                                      ,p_cod_grp_rem_var   => v_cod_grp_rem_var
                                      ,p_cod_pond          => v_cod_pond
                                      ,p_num_peso          => v_num_peso
                                      ,p_cod_un_peso       => v_cod_un_peso
                                      ,p_vlr_premio        => v_vlr_premio
                                      ,p_cod_erro          => p_cod_erro
                                      ,p_descr_erro        => p_descr_erro
                                      );



                  --
                  if  p_cod_erro is not null then
                     --
                     v_num_peso   := 0;
                     v_vlr_premio := 0;

                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

--                      raise e_erro;
                  -- erro ao calcular ponderacao
                  end if;


                  --
                  p_cod_erro         := null;
                  p_descr_erro       := null;
                  --
                  PKG_SRV_GERAL.Prc_Calc_Escala_Fx (null --r_indic_rem_loja.cod_indic                           -- Alt201402 p_cod_indic
                                     ,null                           --p_cod_grp_indic
                                     ,v_cod_grp_rem_var              --p_cod_grp_rem_var
                                     ,v_num_realz_x_meta             --p_num_realz_x_meta
                                     ,v_cod_escala
                                     ,v_num_seq_escala_fx
                                     ,v_num_realz_fx
                                     ,v_cod_un_realz_fx
                                     ,v_flg_pct_100
                                     ,v_num_limite_fx
                                     ,p_cod_erro
                                     ,p_descr_erro
                                     );
                  if p_cod_erro is not null then
                     --
                     v_num_realz_fx := 0;

                    -- enviar email
                    v_body       := p_cod_erro || ' - ' || p_descr_erro;

                    v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                            ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                            ,p_body    => v_body
                                                                            );

                    -- logar tabela
                    v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                              ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                              ,p_num_ano     => p_num_ano
                                                              ,p_num_mes     => p_num_mes
                                                              ,p_cod_fil     => v_cod_fil
                                                              ,p_cod_func    => v_cod_func
                                                              ,p_cod_indic   => v_cod_indic
                                                              ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                              );

                    if v_ins_log_erro is not null then
                       -- enviar email
                       v_body       := v_ins_log_erro;
                       --
                       v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                               ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                               ,p_body    => v_body
                                                                               );
                    --v_ins_log_erro is not null
                    end if;

--                     raise e_erro;
                  -- erro ao calcular escala
                  end if;

                  -- verifica se a faixa deve seguir a proporcionalidade apos 100%
                  -- se sim entao assumir o valor proporcional superior a 100% que deu no resultado do realz X meta
                  if (nvl(v_flg_pct_100, 'N') = 'S' and v_num_realz_x_meta > 100) then
                     --
                     v_num_realz_fx := v_num_realz_x_meta;
                  end if;

                  -- verifica se ha limite superior da faixa, e se a faixa resultado foi maior que o limite
                  -- entao assumir o valor limite
                  if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_fx > v_num_limite_fx then
                     --
                     v_num_realz_fx := v_num_limite_fx;
                  end if;

                  begin
                     -- calcula valor unitario para multiplicar pela qtde
                     v_num_realz_pond  := (v_vlr_premio * v_num_realz_fx)/100;
                  --
                  exception
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao calcular valor unitario para Seguro EP Sax Cargos Operacionais' ||
                                          ' cod_fil:  '    || v_cod_fil   ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                          ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;


--                        raise e_erro;
                  -- calcula valor unitario para multiplicar pela qtde
                  end;

                  --
                  begin
                     -- tabela gerada de apuracao realizado para o indicador
                     select a.nm_tab_realz_fil  ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                           ,a.nm_tab_realz_func ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                       into v_nm_tab_realz_fi
                           ,v_nm_tab_realz_fu
                       from srv_realizado_tab_tmp a
                      where a.cod_indic         = r_indic_rem_loja.cod_indic;


                     -- selecionar todos os funcionarios desse cargo para essa filial e
                     v_var_sql :=              ' select b.cod_func '                      || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '       ,b.cod_cargo '                     || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '       ,nvl(count(a.cod_contrato),0) as qtde ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '   from ' || v_nm_tab_realz_fu || '   a ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '       ,srv_funcionario               b ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '       ,srv_cargo                     c ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '       ,srv_grupo_cargo               d ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '       ,srv_grupo_rem_variavel        e ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '  where a.cpf_vendedor    = b.num_cpf_func ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and b.cod_cargo       = c.cod_cargo ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and c.cod_cargo       = d.cod_cargo ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and d.cod_grp_rem_var = e.cod_grp_rem_var ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and a.seguro          = ''S''                  ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and a.flg_doc_digitalizado = ''S''             ' || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and nvl(c.flg_agrupa_fil_lider, ''N'') = ''N'''  || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and e.cod_grp_rem_var =   ' || v_cod_grp_rem_var || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '    and a.fil_cod =           ' || r_fil.cod_fil     || chr(13) || chr(10);

                     v_var_sql := v_var_sql || '
             -- funcionario esteve empregado pelo menos 1 dia no mes
            and b.dt_admissao                    <= last_day(to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'23:59:59'',''dd/mm/yyyy hh24:mi:ss''))
            and (b.dt_demissao                    is null
                 OR
                 b.dt_demissao                    > to_date(''01/'||trim(to_char(p_num_mes, '00'))||'/'||trim(to_char(p_num_ano, '0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss'')
                )
            -- situacao do funcionario
            and (b.cod_sit_rh                     = 1 -- Em Atividade
                 OR
                 nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                 OR
                   -- situacao atual Gozando Ferias OU
                   -- situacao anterior Gozando Ferias E
                   -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                 ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                  OR
                  (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                   and
                   nvl(b.dt_ini_sit_rh, to_date(''01/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss''))
                                                  >= to_date(''02/'||trim(to_char(p_num_mes,'00'))||'/'||trim(to_char(p_num_ano,'0000'))||'00:00:00'',''dd/mm/yyyy hh24:mi:ss'')
                  )
                 )
                )' || chr(13) || chr(10);

                     v_var_sql := v_var_sql || 'group by b.cod_func '           || chr(13) || chr(10);
                     v_var_sql := v_var_sql || '        ,b.cod_cargo '          || chr(13) || chr(10);


                     open cur_realz for v_var_sql;
                     loop
                        fetch cur_realz
                         into  v_cod_func
                              ,v_cod_cargo_func
                              ,v_num_realz_func;
                        --
                        exit when cur_realz%notfound;

                        --
                        v_cod_un_realz := 3; --unidade UNIDADE
                        --
                        v_vlr_premio_func_calc := v_num_realz_func * v_num_realz_pond;

                        -- gravar na srv_realizado_func_indicador
                        rec_srv_realizado_func_indic.cod_func                := v_cod_func;
                        rec_srv_realizado_func_indic.cod_cargo               := v_cod_cargo_func;
                        rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                        rec_srv_realizado_func_indic.cod_emp                 := r_fil.cod_emp;
                        rec_srv_realizado_func_indic.cod_fil                 := r_fil.cod_fil;
                        rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                        rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                        rec_srv_realizado_func_indic.cod_escala              := v_cod_escala;
                        rec_srv_realizado_func_indic.num_seq_escala_fx       := v_num_seq_escala_fx;
                        rec_srv_realizado_func_indic.num_realz_fx            := v_num_realz_fx;
                        rec_srv_realizado_func_indic.cod_un_realz_fx         := v_cod_un_realz_fx;

                        rec_srv_realizado_func_indic.cod_pond                := v_cod_pond;
                        rec_srv_realizado_func_indic.num_peso                := v_num_peso;
                        rec_srv_realizado_func_indic.cod_un_peso             := v_cod_un_peso;
                        rec_srv_realizado_func_indic.num_realz_pond          := v_num_realz_pond;
                        rec_srv_realizado_func_indic.cod_un_realz_pond       := 1; -- unidade VALOR

                        rec_srv_realizado_func_indic.num_meta                := v_meta;
                        rec_srv_realizado_func_indic.cod_un_meta             := v_cod_un_meta;
                        rec_srv_realizado_func_indic.num_realz               := v_num_realz_func;
                        rec_srv_realizado_func_indic.cod_un_realz            := v_cod_un_realz;
                        rec_srv_realizado_func_indic.num_realz_x_meta        := v_num_realz_x_meta;
                        rec_srv_realizado_func_indic.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;

                        rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                        -- se valor negativo considerar zero
                        if v_vlr_premio_func_calc < 0 then
                           v_vlr_premio_func_calc := 0;
                        end if;
                        rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                        rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                        rec_srv_realizado_func_indic.pct_calc_rateio         := null;
                        rec_srv_realizado_func_indic.vlr_desc_indicacao      := null;
                        rec_srv_realizado_func_indic.qtd_desc_indicacao      := null;

                        rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                        rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;


                        -- inserir o realizado do funcionario pelo indicador
                        PKG_SRV_GERAL.Prc_Insere_Realz_Func_Indic (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                    ,p_cod_erro                  => p_cod_erro
                                                    ,p_descr_erro                => p_descr_erro
                                                     );
                        --
                        if p_cod_erro is not null then

                           -- enviar email
                           v_body       := p_cod_erro || ' - ' || p_descr_erro;

                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                   ,p_body    => v_body
                                                                                   );

                           -- logar tabela
                           v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                     ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                     ,p_num_ano     => p_num_ano
                                                                     ,p_num_mes     => p_num_mes
                                                                     ,p_cod_fil     => v_cod_fil
                                                                     ,p_cod_func    => v_cod_func
                                                                     ,p_cod_indic   => v_cod_indic
                                                                     ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                     );

                           if v_ins_log_erro is not null then
                              -- enviar email
                              v_body       := v_ins_log_erro;
                              --
                              v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                      ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                      ,p_body    => v_body
                                                                                      );
                           --v_ins_log_erro is not null
                           end if;

--                           raise e_erro;
                        -- erro ao inserir realizado func indicador
                        end if;

                        --


                     -- cur_venda
                     end loop;
                     --
                     close cur_realz;

                     --
                     commit;

                  --
                  exception
                     /*when e_erro then
                        raise e_erro;*/
                     when others then
                        p_cod_erro     := 1;
                        p_descr_erro   := 'Erro ao inserir Seguro EP SAX Cargos Operacionais' ||
                                          ' num_ano: '     || p_num_ano ||
                                          ' num_mes: '     || p_num_mes ||
                                          ' cod_fil:  '    || v_cod_fil   ||
                                          ' cod_indic:  '  || v_cod_indic ||
                                          ' cod_func:  '   || v_cod_func  ||
                                          ' erro: '        || sqlerrm;

                        -- enviar email
                        v_body       := p_cod_erro || ' - ' || p_descr_erro;

                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                                ,p_body    => v_body
                                                                                );

                        -- logar tabela
                        v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

                        if v_ins_log_erro is not null then
                           -- enviar email
                           v_body       := v_ins_log_erro;
                           --
                           v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                   ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                   ,p_body    => v_body
                                                                                   );
                        --v_ins_log_erro is not null
                        end if;

--                        raise e_erro;
                  --
                  end;

               --
               exception
                  /*when e_erro then
                     raise e_erro;*/
                  when others then
                     p_cod_erro     := 1;
                     p_descr_erro   := 'Erro Geral ao calcular indicador Seguro EP SAX Cargos Operacionais' ||
                                       ' cod_fil:  '    || v_cod_fil   ||
                                       ' cod_indic:  '  || v_cod_indic ||
                                       ' erro: '        || sqlerrm;


                     -- enviar email
                     v_body       := p_cod_erro || ' - ' || p_descr_erro;

                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                             ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                             ,p_body    => v_body
                                                                             );

                     -- logar tabela
                     v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                               ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                               ,p_num_ano     => p_num_ano
                                                               ,p_num_mes     => p_num_mes
                                                               ,p_cod_fil     => v_cod_fil
                                                               ,p_cod_func    => v_cod_func
                                                               ,p_cod_indic   => v_cod_indic
                                                               ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                               );

                     if v_ins_log_erro is not null then
                        -- enviar email
                        v_body       := v_ins_log_erro;
                        --
                        v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                                ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                                ,p_body    => v_body
                                                                                );
                     --v_ins_log_erro is not null
                     end if;

               end;


            ---------------------------------------------------------
            -- if p_descr_indic = EMPRESTIMO PESSOAL (EP) SAX, etc...
            ---------------------------------------------------------
            end if;

            -- commit por indicador
            commit;

         ------------------------------------------------------------
         -- c_indic_rem_loja
         ------------------------------------------------------------
         end loop;


         ---------------------------------------------
         -- commit por filial
         ---------------------------------------------
         commit;

      ------------------------------------------------
      -- c_fil
      ------------------------------------------------
      end loop;

      --
      commit;

   exception
      when e_erro then

         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception e_erro: '
                                                                 ,p_body    => v_body
                                                                 );

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                   ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => v_cod_func
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );
         --v_ins_log_erro is not null
         end if;

         --
         return;
      when others then
         p_cod_erro     := 1;
         p_descr_erro   := 'Erro geral na procedure prc_calc_realz_Func_Indic_SAX: Calculo do realizado Funcionario Indicador Loja: ' ||
                           'cod_fil: '           || v_cod_fil             || ' - ' ||
                           'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                           'cod_indic: '         || v_cod_indic           || ' - ' ||
                           'cod_cargo: '         || v_cod_cargo           || ' - ' ||
                           'cod_func: '          || v_cod_func            || ' - ' || sqlerrm;

         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

         v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                 ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro exception geral: '
                                                                 ,p_body    => v_body
                                                                 );

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 3
                                                   ,p_nome_proc   => 'prc_calc_realz_Func_Indic_SAX'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => v_cod_func
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 74 -- SRV - prc_calc_realz_Func_Indic_SAX
                                                                    ,p_subject => 'SRV Calculo Realizado Func Indicador - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );
         --v_ins_log_erro is not null
         end if;


   end prc_calc_realz_Func_Indic_SAX;


   procedure prc_calc_realz_Func_CCenter (p_num_ano      in number
                                         ,p_num_mes      in number
                                         ,p_cod_indic    in number     default null
--                                         ,p_descr_indic  in varchar2   default null
                                         ,p_cod_usuario  in number
                                         ,p_cod_erro     out number
                                         ,p_descr_erro   out varchar2
                                          ) is

      -- cursor Cargos para os grupos de Remuneracao Variavel LIDERANCA
      cursor c_cargo_grp_rem_var (p_descr_grp_rem_var varchar2) is
         select b.cod_cargo
               ,b.cod_grp_rem_var
           from srv_grupo_rem_variavel a
               ,srv_grupo_cargo        b
          where a.cod_grp_rem_var = b.cod_grp_rem_var
            and a.descr_grp_rem_var = p_descr_grp_rem_var
       order by b.cod_cargo;

      -- cursor dos funcionarios para o cargo LIDERANCA para a filial
      cursor c_func_cargo (p_cod_cargo   number
                          ,p_cur_cod_emp number
                          ,p_cur_cod_fil number) is
         select b.cod_func
               ,b.cod_cargo
           from srv_cargo       a
               ,srv_funcionario b
               ,srv_func_eleg_tlmkt c
          where a.cod_cargo = b.cod_cargo
            and b.cod_func = c.cod_func
            and a.cod_cargo = p_cod_cargo
            and b.cod_emp = p_cur_cod_emp
            and b.cod_fil = p_cur_cod_fil
            and c.num_ano = p_num_ano
            and c.num_mes = p_num_mes

            -- funcionario esteve empregado pelo menos 1 dia no mes
            and b.dt_admissao                    <= last_day(to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                                       '/' || trim(to_char(p_num_ano, '0000')) ||
                                                                       ' 23:59:59', 'dd/mm/yyyy hh24:mi:ss'))
            and (b.dt_demissao                    is null
                 OR
                 b.dt_demissao                    > to_date('01/' || trim(to_char(p_num_mes, '00'))   ||
                                                              '/' || trim(to_char(p_num_ano, '0000')) ||
                                                              ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                )
            -- situacao do funcionario
            and (b.cod_sit_rh                     = 1 -- Em Atividade
                 OR
                 nvl(b.qtd_dias_trab_per,0)       > 0  -- trabalhou pelo menos 1 dia
                 OR
                   -- situacao atual Gozando Ferias OU
                   -- situacao anterior Gozando Ferias E
                   -- situacao atual >= 2o. dia do periodo (esteve pelo menos 1 dia Gozando Ferias)
                 ( b.cod_sit_rh                    = 9 -- Gozando Ferias
                   OR
                  (nvl(b.cod_sit_rh_ant,1)         = 9 -- Gozando Ferias
                   and
                   nvl(b.dt_ini_sit_rh, to_date('01/' || trim(to_char(p_num_mes, '00'))||
                                                  '/' || trim(to_char(p_num_ano, '0000')) ||
                                                  ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss'))
                                                  >= to_date('02/' || trim(to_char(p_num_mes, '00'))   ||
                                                               '/' || trim(to_char(p_num_ano, '0000')) ||
                                                               ' 00:00:00', 'dd/mm/yyyy hh24:mi:ss')
                  )
                 )
                )
          order by b.cod_func;

      -- REMUNERACAO LOJAS
      cursor c_indic_rem_loja (p_descr_grp_indic srv_grupo_indicador.descr_grp_indic%type) is
        select c.cod_indic
             , c.descr_indic
          from srv_tipo_rem_var          a
         inner join srv_grupo_indicador  b on b.cod_tipo_rem_var = a.cod_tipo_rem_var
         inner join srv_indicador        c on c.cod_grp_indic    = b.cod_grp_indic
                                          and upper(c.flg_indic_ativ) = 'S'
         where b.descr_grp_indic = p_descr_grp_indic;

      --Variables
      v_meta                          srv_realizado_filial.num_meta%type;
      v_cod_un_meta                   srv_realizado_filial.cod_un_meta%type;
      v_num_realz_fil                 srv_realizado_filial.num_realz%type;
      v_cod_un_realz                  srv_realizado_filial.cod_un_realz%type;
      v_num_realz_x_meta              srv_realizado_filial.num_realz_x_meta%type;
      v_cod_un_realz_x_meta           srv_realizado_filial.cod_un_realz_x_meta%type;
      v_num_realz_x_meta_limitador    srv_realizado_filial.num_realz_x_meta%type;
      v_cod_pond                      srv_ponderacao.cod_pond%type;
      v_num_peso                      srv_ponderacao.num_peso%type;
      v_cod_un_peso                   srv_ponderacao.cod_un_peso%type;
      v_vlr_premio                    srv_ponderacao.vlr_premio%type;
      v_vlr_premio_func_calc          srv_realizado_func_indicador.vlr_premio_func_calc%type;
      v_num_realz_pond                srv_realizado_func_indicador.num_realz_pond%type;
      v_cod_func                      srv_funcionario.cod_func%type;
      v_cod_escala                    srv_escala_faixa.cod_escala%type;
      v_num_seq_escala_fx             srv_escala_faixa.num_seq_escala_fx%type;
      v_num_realz_fx                  srv_escala_faixa.num_realz_fx%type;
      v_cod_un_realz_fx               srv_escala_faixa.cod_un_realz_fx%type;
      v_flg_pct_100                   srv_escala_faixa.flg_pct_100%type;
      v_num_limite_fx                 srv_escala_faixa.num_limite_fx%type;
      v_cod_grp_indic                 srv_grupo_indicador.cod_grp_indic%type;
      v_cod_emp                       srv_filial.cod_emp%type;
      v_cod_fil                       srv_filial.cod_fil%type;
      v_cod_indic                     srv_indicador.cod_indic%type;
      v_cod_cargo                     srv_cargo.cod_cargo%type;
      rec_srv_realizado_func_indic    srv_realizado_func_indicador%rowtype;

      --v_cod_grp_rem_var               srv_grupo_rem_variavel.cod_grp_rem_var%type;
      v_nm_tab_realz_fi               srv_realizado_tab_tmp.nm_tab_realz_fil%type;
      v_nm_tab_realz_fu               srv_realizado_tab_tmp.nm_tab_realz_func%type;
      v_var_sql                       varchar2(2000);
      cur_realz                       pkg_srv_calc_rem_var.typ_cursor;
      --v_cpf_vendedor                  number(11);
      v_cod_cargo_func                srv_funcionario.cod_cargo%type;
      v_num_realz_func                srv_realizado_func_indicador.num_realz%type;

   begin

      v_data    := sysdate;
      --
      v_dt_ini  := '01/' || trim(to_char(p_num_mes, '00')) || '/' || trim(to_char(p_num_ano, '0000')) || ' ' || '00:00:00';
      --
      select to_char(last_day(to_date(('01/' || to_char(p_num_mes, '00') || '/'||to_char(p_num_ano, '0000')), 'dd/mm/yyyy')), 'dd/mm/yyyy')|| ' ' || '23:59:59'
        into v_dt_fim
        from dual;

      -------------------------------
      -- TLMKT
      -------------------------------
      Begin

        for r_indic_rem_loja in c_indic_rem_loja(p_descr_grp_indic => 'TLMKT') loop

          v_cod_indic     := r_indic_rem_loja.cod_indic;
          v_cod_fil       := 900;
          v_cod_emp       := 1;

          -- seleciona realz X meta e vlr premio calculado da filial
          begin
            select a.num_meta,
                   a.cod_un_meta,
                   a.num_realz,
                   a.cod_un_realz,
                   a.num_realz_x_meta,
                   a.cod_un_realz_x_meta
              into v_meta,
                   v_cod_un_meta,
                   v_num_realz_fil,
                   v_cod_un_realz,
                   v_num_realz_x_meta,
                   v_cod_un_realz_x_meta
              from srv_realizado_filial a
                 , srv_indicador        b
             where a.cod_indic = b.cod_indic
               and a.cod_emp = v_cod_emp
               and a.cod_fil = v_cod_fil
               and b.descr_indic = r_indic_rem_loja.descr_indic
               and a.num_ano = p_num_ano
               and a.num_mes = p_num_mes;
          exception
            when others then
              v_meta                := 0;
              v_cod_un_meta         := null;
              v_num_realz_fil       := 0;
              v_cod_un_realz        := null;
              v_num_realz_x_meta    := 0;
              v_cod_un_realz_x_meta := null;
          end;

          -----------------------------------------------------------------------
          -- CARGOS LIDERANCA DO TLMKT
          -----------------------------------------------------------------------
          begin

            for r_cargo_grp_rem_var in c_cargo_grp_rem_var(p_descr_grp_rem_var => 'TLMKT') loop

              v_cod_cargo := r_cargo_grp_rem_var.cod_cargo;

              -- Extrai Poderacao
              begin
                Pkg_Srv_Geral.prc_calc_ponderacao_tp_fil2(p_cod_indic       => r_indic_rem_loja.cod_indic,
                                                          p_cod_grp_indic   => null,
                                                          p_cod_cargo       => r_cargo_grp_rem_var.cod_cargo,
                                                          p_cod_grp_rem_var => null,
                                                          p_cod_tipo_fil    => 1,
                                                          p_cod_emp         => v_cod_emp,
                                                          p_cod_fil         => v_cod_fil,
                                                          p_cod_pond        => v_cod_pond,
                                                          p_num_peso        => v_num_peso,
                                                          p_cod_un_peso     => v_cod_un_peso,
                                                          p_vlr_premio      => v_vlr_premio,
                                                          p_cod_erro        => p_cod_erro,
                                                          p_descr_erro      => p_descr_erro);
              exception
                when others then
                  null;
              end;

              -- Extrai escala
              begin
                Pkg_Srv_Geral.prc_calc_escala_fx(r_indic_rem_loja.cod_indic, --p_cod_indic
                                                 null,                       --p_cod_grp_indic
                                                 null,                       --p_cod_grp_rem_var
                                                 v_num_realz_x_meta,         --p_num_realz_x_meta
                                                 v_cod_escala,
                                                 v_num_seq_escala_fx,
                                                 v_num_realz_fx,
                                                 v_cod_un_realz_fx,
                                                 v_flg_pct_100,
                                                 v_num_limite_fx,
                                                 p_cod_erro,
                                                 p_descr_erro);
              exception
                when others then
                  null;
              end;

              -- armazena vr original de atingimento
              v_num_realz_x_meta_limitador := v_num_realz_x_meta;

              -- Limitar 100% a faixa para o cargo 752(ANL MIS E PLANEJAMENTO PL)
              if r_cargo_grp_rem_var.cod_cargo = 752 and nvl(v_num_limite_fx, 0) > 0 then

                v_num_limite_fx := 100;

              end if;

              /*verifica se ha limite superior da faixa, e se a faixa resultado
              for maior que o limite entao assumir o valor limite*/
              if nvl(v_num_limite_fx, 0) > 0 and v_num_realz_x_meta_limitador > v_num_limite_fx then

                v_num_realz_x_meta_limitador := v_num_limite_fx;

              end if;

              if nvl(v_num_realz_fx, 0) <> 0 then

                v_num_realz_fx := v_num_realz_x_meta_limitador;

              end if;

              begin
                v_num_realz_pond := (v_num_realz_x_meta * v_num_peso) / 100;
              exception
                when others then
                  null;
              end;

              v_vlr_premio_func_calc := ((v_vlr_premio * v_num_realz_fx) / 100);

              -- selecionar todos os funcionarios desse cargo para essa filial e
              Begin
                for r_func_cargo in c_func_cargo(p_cod_cargo   => r_cargo_grp_rem_var.cod_cargo
                                                ,p_cur_cod_emp => v_cod_emp
                                                ,p_cur_cod_fil => v_cod_fil) loop

                  v_cod_func := r_func_cargo.cod_func;

                  -- gravar na srv_realizado_func_indicador
                  rec_srv_realizado_func_indic.cod_func                      := r_func_cargo.cod_func;
                  rec_srv_realizado_func_indic.cod_cargo                     := r_func_cargo.cod_cargo;
                  rec_srv_realizado_func_indic.cod_indic                     := r_indic_rem_loja.cod_indic;
                  rec_srv_realizado_func_indic.cod_emp                       := v_cod_emp;
                  rec_srv_realizado_func_indic.cod_fil                       := v_cod_fil;
                  rec_srv_realizado_func_indic.num_ano                       := p_num_ano;
                  rec_srv_realizado_func_indic.num_mes                       := p_num_mes;

                  rec_srv_realizado_func_indic.cod_escala                    := v_cod_escala;
                  rec_srv_realizado_func_indic.num_seq_escala_fx             := v_num_seq_escala_fx;
                  rec_srv_realizado_func_indic.num_realz_fx                  := v_num_realz_fx;
                  rec_srv_realizado_func_indic.cod_un_realz_fx               := v_cod_un_realz_fx;

                  rec_srv_realizado_func_indic.cod_pond                      := v_cod_pond;
                  rec_srv_realizado_func_indic.num_peso                      := v_num_peso;
                  rec_srv_realizado_func_indic.cod_un_peso                   := v_cod_un_peso;
                  rec_srv_realizado_func_indic.num_realz_pond                := v_num_realz_pond;
                  rec_srv_realizado_func_indic.cod_un_realz_pond             := 2; -- unidade PERCENTUAL

                  rec_srv_realizado_func_indic.num_meta                      := v_meta;
                  rec_srv_realizado_func_indic.cod_un_meta                   := v_cod_un_meta;
                  rec_srv_realizado_func_indic.num_realz                     := v_num_realz_fil;
                  rec_srv_realizado_func_indic.cod_un_realz                  := v_cod_un_realz;
                  rec_srv_realizado_func_indic.num_realz_x_meta              := v_num_realz_x_meta;
                  rec_srv_realizado_func_indic.cod_un_realz_x_meta           := v_cod_un_realz_x_meta;

                  rec_srv_realizado_func_indic.vlr_premio                    := v_vlr_premio;
                  rec_srv_realizado_func_indic.vlr_premio_func_calc          := v_vlr_premio_func_calc;
                  rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc   := null;
                  rec_srv_realizado_func_indic.pct_calc_rateio               := null;

                  rec_srv_realizado_func_indic.dt_ini_sit_srv                := v_data;
                  rec_srv_realizado_func_indic.cod_usuario                   := p_cod_usuario;
                  rec_srv_realizado_func_indic.seg_fil                       := 'N';

                  -- alteracao, manter apenas um registro para o funcionario se a filial mudou
                  prc_ins_realz_func_indic_ultfi(p_rec_srv_realz_func_indic => rec_srv_realizado_func_indic,
                                                 p_cod_erro                 => p_cod_erro,
                                                 p_descr_erro               => p_descr_erro);

                -- c_func_cargo
                end loop;

              Exception
                When Others Then
                  Null;
              End;

            -- c_cargo_grp_rem_var
            end loop;

            --
            commit;

          exception
            when others then
              p_cod_erro   := 1;
              p_descr_erro := 'Erro Geral ao calcular indicador Vendas para Cargos Lideranca ' ||
                              ' cod_fil:  ' || v_cod_fil ||
                              ' cod_indic:  ' || v_cod_indic ||
                              ' cod_cargo:  ' || v_cod_cargo ||
                              ' cod_func:  ' || v_cod_func || ' erro: ' ||
                              sqlerrm;

              -- logar tabela
              v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo(p_cod_proc  => 3,
                                                                      p_nome_proc => 'PRC_CALC_REALZ_FUNC_INDIC_LJ',
                                                                      p_num_ano   => p_num_ano,
                                                                      p_num_mes   => p_num_mes,
                                                                      p_cod_fil   => v_cod_fil,
                                                                      p_cod_func  => v_cod_func,
                                                                      p_cod_indic => v_cod_indic,
                                                                      p_erro      => p_cod_erro || ' - ' || p_descr_erro);

          end;

          -- commit por indicador
          commit;

        ------------------------------------------------
        -- c_indic_rem_loja
        ------------------------------------------------
        end loop;

        ------------------------------------------------
        -- commit por filial
        ------------------------------------------------
        commit;

      exception
        when others then
           p_cod_erro     := 1;
           p_descr_erro   := 'Erro geral na procedure prc_calc_realz_Func_Indic_Lj: Calculo do realizado Funcionario Indicador Loja: ' ||
                             'cod_fil: '           || v_cod_fil             || ' - ' ||
                             'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                             'cod_indic: '         || v_cod_indic           || ' - ' ||
                             'cod_cargo: '         || v_cod_cargo           || ' - ' ||
                             'cod_func: '          || v_cod_func            || ' - ' || sqlerrm;

           -- logar tabela
           v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo (p_cod_proc    => 3
                                                                   ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                   ,p_num_ano     => p_num_ano
                                                                   ,p_num_mes     => p_num_mes
                                                                   ,p_cod_fil     => v_cod_fil
                                                                   ,p_cod_func    => v_cod_func
                                                                   ,p_cod_indic   => v_cod_indic
                                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                   );
      end;


      -------------------------------
      -- SAC
      -------------------------------
      begin

        for r_indic_rem_loja in c_indic_rem_loja(p_descr_grp_indic => 'CALL_CENTER') loop

          --Variables
          v_cod_emp       := 1;
          v_cod_fil       := 900;
          v_cod_indic     := r_indic_rem_loja.cod_indic;

          -- calcula valor unitario para multiplicar pela qtde
          v_vlr_premio    := (50 / 100);
          v_num_limite_fx := 400;

          -- tabela gerada de apuracao realizado para o indicador
          select a.nm_tab_realz_fil  ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
                ,a.nm_tab_realz_func ||'_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))
            into v_nm_tab_realz_fi
                ,v_nm_tab_realz_fu
            from srv_realizado_tab_tmp a
           where a.cod_indic = r_indic_rem_loja.cod_indic;

          -- selecionar todos os funcionarios desse cargo para essa filial e
          begin
            v_var_sql :=              'select b.cod_func'                                                      || chr(13) || chr(10);
            v_var_sql := v_var_sql || '      ,b.cod_cargo'                                                     || chr(13) || chr(10);
            v_var_sql := v_var_sql || '      ,count(*) num_realz_func'                                         || chr(13) || chr(10);
            v_var_sql := v_var_sql || '  from '||v_nm_tab_realz_fu ||' a'                                      || chr(13) || chr(10);
            v_var_sql := v_var_sql || ' inner join srv_funcionario b on b.num_cpf_func = a.cpf_vendedor' || chr(13) || chr(10);
            v_var_sql := v_var_sql || '                                   and b.cod_sit_rh <> 2'               || chr(13) || chr(10);
            v_var_sql := v_var_sql || ' where a.fil_cod = '|| v_cod_fil                                        || chr(13) || chr(10);
            v_var_sql := v_var_sql || ' group by a.cpf_vendedor'                                               || chr(13) || chr(10);
            v_var_sql := v_var_sql || '         ,b.cod_func'                                                   || chr(13) || chr(10);
            v_var_sql := v_var_sql || '         ,b.cod_cargo';

            open cur_realz for v_var_sql;
              loop
                fetch cur_realz
                into v_cod_func
                    ,v_cod_cargo_func
                    ,v_num_realz_func;

                exit when cur_realz%notfound;
                --
                v_vlr_premio_func_calc := v_num_realz_func * v_vlr_premio;

                if v_vlr_premio_func_calc  > v_num_limite_fx then
                   v_vlr_premio_func_calc := v_num_limite_fx;
                end if;

                -- gravar na srv_realizado_func_indicador
                rec_srv_realizado_func_indic.cod_func                := v_cod_func;
                rec_srv_realizado_func_indic.cod_cargo               := v_cod_cargo_func;
                rec_srv_realizado_func_indic.cod_indic               := r_indic_rem_loja.cod_indic;
                rec_srv_realizado_func_indic.cod_emp                 := v_cod_emp;
                rec_srv_realizado_func_indic.cod_fil                 := v_cod_fil;
                rec_srv_realizado_func_indic.num_ano                 := p_num_ano;
                rec_srv_realizado_func_indic.num_mes                 := p_num_mes;

                rec_srv_realizado_func_indic.cod_escala              := null;
                rec_srv_realizado_func_indic.num_seq_escala_fx       := null;
                rec_srv_realizado_func_indic.num_realz_fx            := null;
                rec_srv_realizado_func_indic.cod_un_realz_fx         := null;

                rec_srv_realizado_func_indic.cod_pond                := null;
                rec_srv_realizado_func_indic.num_peso                := null;
                rec_srv_realizado_func_indic.cod_un_peso             := null;
                rec_srv_realizado_func_indic.num_realz_pond          := null;
                rec_srv_realizado_func_indic.cod_un_realz_pond       := null;

                rec_srv_realizado_func_indic.num_meta                := null;
                rec_srv_realizado_func_indic.cod_un_meta             := null;
                rec_srv_realizado_func_indic.num_realz               := v_num_realz_func;
                rec_srv_realizado_func_indic.cod_un_realz            := 1;
                rec_srv_realizado_func_indic.num_realz_x_meta        := null;
                rec_srv_realizado_func_indic.cod_un_realz_x_meta     := null;

                rec_srv_realizado_func_indic.vlr_premio              := v_vlr_premio;
                rec_srv_realizado_func_indic.vlr_premio_func_calc    := v_vlr_premio_func_calc;
                rec_srv_realizado_func_indic.cod_un_vlr_premio_func_calc := 1; -- unidade VALOR
                rec_srv_realizado_func_indic.pct_calc_rateio         := null;

                rec_srv_realizado_func_indic.dt_ini_sit_srv          := v_data;
                rec_srv_realizado_func_indic.cod_usuario             := p_cod_usuario;

                -- inserir o realizado do funcionario pelo indicador
                Pkg_Srv_Geral.prc_insere_realz_func_indic (p_rec_srv_realz_func_indic  => rec_srv_realizado_func_indic
                                                          ,p_cod_erro                  => p_cod_erro
                                                          ,p_descr_erro                => p_descr_erro
                                                           );

              -- cur_venda
              end loop;
            --
            close cur_realz;

          exception
            when others then
              p_cod_erro     := 1;
              p_descr_erro   := 'Erro ao inserir realizado funcionario SAC' ||
                                ' cod_fil:  '    || v_cod_fil   ||
                                ' cod_indic:  '  || v_cod_indic ||
                                ' cod_func:  '   || v_cod_func  ||
                                ' erro: '        || sqlerrm;

              v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo (p_cod_proc    => 3
                                                                      ,p_nome_proc   => 'SAC'
                                                                      ,p_num_ano     => p_num_ano
                                                                      ,p_num_mes     => p_num_mes
                                                                      ,p_cod_fil     => v_cod_fil
                                                                      ,p_cod_func    => v_cod_func
                                                                      ,p_cod_indic   => v_cod_indic
                                                                      ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                      );
          end;

          -- commit por indicador
          commit;

        --c_indic_rem_loja
        end loop;

      exception
        when others then
          p_cod_erro     := 1;
          p_descr_erro   := 'Erro ao calcular indicadores SAC' ||
                            ' cod_fil:  '    || v_cod_fil   ||
                            ' cod_indic:  '  || v_cod_indic ||
                            ' cod_func:  '   || v_cod_func  ||
                            ' erro: '        || sqlerrm;

          v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo (p_cod_proc    => 3
                                                                  ,p_nome_proc   => 'SAC'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => v_cod_func
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );
      --FIM do Calculo do SAC
      end;


   exception
      when others then
         p_cod_erro     := 1;
         p_descr_erro   := 'Erro geral na procedure prc_calc_realz_Func_Indic_Lj: Calculo do realizado Funcionario Indicador Loja: ' ||
                           'cod_fil: '           || v_cod_fil             || ' - ' ||
                           'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                           'cod_indic: '         || v_cod_indic           || ' - ' ||
                           'cod_cargo: '         || v_cod_cargo           || ' - ' ||
                           'cod_func: '          || v_cod_func            || ' - ' || sqlerrm;

         -- logar tabela
         v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo (p_cod_proc    => 3
                                                                 ,p_nome_proc   => 'PRC_CALC_REALZ_FUNC_INDIC_LJ'
                                                                 ,p_num_ano     => p_num_ano
                                                                 ,p_num_mes     => p_num_mes
                                                                 ,p_cod_fil     => v_cod_fil
                                                                 ,p_cod_func    => v_cod_func
                                                                 ,p_cod_indic   => v_cod_indic
                                                                 ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                 );

   end prc_calc_realz_Func_CCenter;

end pkg_srv_calc_resultado;
/
