create or replace package body srv.pkg_srv_atingimento is

  /*-------------------------------------------------------------------------------------------------
  -- Author  : LEVY VILLAR
  -- Created : 11/04/2017
  -- Purpose : Package para calculo do atingimento por Filial (Indicadores CCM/SRV)
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/
  v_DbLink varchar2(10) := '';--'@lksrv';

   -------------------------------------------------------------------------------------------------*/
   v_ins_log_erro                   varchar2(4000);
   -- email
   v_body                           varchar2(4000);
   --v_send_email                     number;

   -----------------------------------------------------------------------------------------------
   -- ATUALIZA META FILIAL
   -----------------------------------------------------------------------------------------------
   procedure prc_atualiza_meta_fil (p_cod_indic        in  number
                                   ,p_cod_emp          in  number
                                   ,p_cod_fil          in  number
                                   ,p_ano_meta         in  number
                                   ,p_mes_meta         in  number
                                   ,p_meta             in  number
                                   ,p_cod_un_meta      in  number
                                   ,p_cod_erro         out number
                                   ,p_descr_erro       out varchar2
                                   ) is

   begin

    -- atualizar meta calculada a partir do percentual da filial para os indicadores VJ e CT
       update srv_meta_filial   m
          set m.num_meta      = p_meta
             ,m.cod_un_meta   = p_cod_un_meta
        where m.cod_indic     = p_cod_indic
          and m.cod_emp       = p_cod_emp
          and m.cod_fil       = p_cod_fil
          and m.num_ano       = p_ano_meta
          and m.num_mes       = p_mes_meta;

      --
      commit;

   exception
      when others then
         p_cod_erro     := 4;
         p_descr_erro   := 'Erro ao atualizar meta calculada - p_cod_indic: '|| p_cod_indic  ||
                                    ' - p_cod_fil: '                         || p_cod_fil    ||
                                    ' - p_ano: '                             || p_ano_meta   ||
                                    ' - p_mes: '                             || p_mes_meta   ||
                                    ' - p_meta: '                            || p_meta       ||
                                    ' - erro: '                              || sqlerrm;
   end prc_atualiza_meta_fil;

   -----------------------------------------------------------------------------------------------
   -- INSERE REALIZADO FILIAL
   -----------------------------------------------------------------------------------------------
   procedure prc_insere_realz_fil (p_rec_srv_realz_fil  in  srv_realizado_filial%rowtype
                                  ,p_cod_erro           out number
                                  ,p_descr_erro         out varchar2
                                  ) is

      --
      rec_srv_realizado_filial_hist    srv_realizado_filial_hist%rowtype;

   begin

      -----------------------------------------------------
      -- insere o realizado da filial na tabela
      -----------------------------------------------------
      begin
         insert into srv_realizado_filial
                     (cod_indic
                     ,cod_emp
                     ,cod_fil
                     ,num_ano
                     ,num_mes
                     ,num_meta
                     ,cod_un_meta
                     ,num_realz
                     ,cod_un_realz
                     ,qtd_realz
                     ,num_realz_x_meta
                     ,cod_un_realz_x_meta
                     ,vlr_premio_fil_calc
                     ,cod_un_premio_fil_calc
                     ,dt_ini_sit_srv
                     ,cod_usuario
                     ,cod_escala
                     ,num_seq_escala_fx
                     ,num_realz_fx
                     ,cod_un_realz_fx
                     )
              values (p_rec_srv_realz_fil.cod_indic
                     ,p_rec_srv_realz_fil.cod_emp
                     ,p_rec_srv_realz_fil.cod_fil
                     ,p_rec_srv_realz_fil.num_ano
                     ,p_rec_srv_realz_fil.num_mes
                     ,p_rec_srv_realz_fil.num_meta
                     ,p_rec_srv_realz_fil.cod_un_meta
                     ,p_rec_srv_realz_fil.num_realz
                     ,p_rec_srv_realz_fil.cod_un_realz
                     ,p_rec_srv_realz_fil.qtd_realz
                     ,p_rec_srv_realz_fil.num_realz_x_meta
                     ,p_rec_srv_realz_fil.cod_un_realz_x_meta
                     ,p_rec_srv_realz_fil.vlr_premio_fil_calc
                     ,p_rec_srv_realz_fil.cod_un_premio_fil_calc
                     ,p_rec_srv_realz_fil.dt_ini_sit_srv
                     ,p_rec_srv_realz_fil.cod_usuario
                     ,p_rec_srv_realz_fil.cod_escala
                     ,p_rec_srv_realz_fil.num_seq_escala_fx
                     ,p_rec_srv_realz_fil.num_realz_fx
                     ,p_rec_srv_realz_fil.cod_un_realz_fx
                    );
      --
      exception
         when dup_val_on_index then

            -- se ja existir a apuracao inserir o registro que ja existe na tabela de historico
            -- e gravar o calculado na tabela original
            begin
               --
               select  cod_indic
                      ,cod_emp
                      ,cod_fil
                      ,num_ano
                      ,num_mes
                      ,num_meta
                      ,cod_un_meta
                      ,num_realz
                      ,cod_un_realz
                      ,qtd_realz
                      ,num_realz_x_meta
                      ,cod_un_realz_x_meta
                      ,vlr_premio_fil_calc
                      ,cod_un_premio_fil_calc
                      ,dt_ini_sit_srv
                      ,cod_usuario
                      ,cod_escala
                      ,num_seq_escala_fx
                      ,num_realz_fx
                      ,cod_un_realz_fx
                 into  rec_srv_realizado_filial_hist.cod_indic
                      ,rec_srv_realizado_filial_hist.cod_emp
                      ,rec_srv_realizado_filial_hist.cod_fil
                      ,rec_srv_realizado_filial_hist.num_ano
                      ,rec_srv_realizado_filial_hist.num_mes
                      ,rec_srv_realizado_filial_hist.num_meta
                      ,rec_srv_realizado_filial_hist.cod_un_meta
                      ,rec_srv_realizado_filial_hist.num_realz
                      ,rec_srv_realizado_filial_hist.cod_un_realz
                      ,rec_srv_realizado_filial_hist.qtd_realz
                      ,rec_srv_realizado_filial_hist.num_realz_x_meta
                      ,rec_srv_realizado_filial_hist.cod_un_realz_x_meta
                      ,rec_srv_realizado_filial_hist.vlr_premio_fil_calc
                      ,rec_srv_realizado_filial_hist.cod_un_premio_fil_calc
                      ,rec_srv_realizado_filial_hist.dt_ini_sit_srv
                      ,rec_srv_realizado_filial_hist.cod_usuario
                      ,rec_srv_realizado_filial_hist.cod_escala
                      ,rec_srv_realizado_filial_hist.num_seq_escala_fx
                      ,rec_srv_realizado_filial_hist.num_realz_fx
                      ,rec_srv_realizado_filial_hist.cod_un_realz_fx
                  from srv_realizado_filial
                 where cod_indic            = p_rec_srv_realz_fil.cod_indic
                   and cod_emp              = p_rec_srv_realz_fil.cod_emp
                   and cod_fil              = p_rec_srv_realz_fil.cod_fil
                   and num_ano              = p_rec_srv_realz_fil.num_ano
                   and num_mes              = p_rec_srv_realz_fil.num_mes;

               if sql%rowcount > 0 then
                  --------------------------------------------------------------
                  -- insere registro no historico
                  --------------------------------------------------------------
                  insert into srv_realizado_filial_hist
                             (cod_indic
                             ,cod_emp
                             ,cod_fil
                             ,num_ano
                             ,num_mes
                             ,num_meta
                             ,cod_un_meta
                             ,num_realz
                             ,cod_un_realz
                             ,qtd_realz
                             ,num_realz_x_meta
                             ,cod_un_realz_x_meta
                             ,vlr_premio_fil_calc
                             ,cod_un_premio_fil_calc
                             ,dt_ini_sit_srv
                             ,cod_usuario
                             ,cod_escala
                             ,num_seq_escala_fx
                             ,num_realz_fx
                             ,cod_un_realz_fx
                             )
                      values (rec_srv_realizado_filial_hist.cod_indic
                             ,rec_srv_realizado_filial_hist.cod_emp
                             ,rec_srv_realizado_filial_hist.cod_fil
                             ,rec_srv_realizado_filial_hist.num_ano
                             ,rec_srv_realizado_filial_hist.num_mes
                             ,rec_srv_realizado_filial_hist.num_meta
                             ,rec_srv_realizado_filial_hist.cod_un_meta
                             ,rec_srv_realizado_filial_hist.num_realz
                             ,rec_srv_realizado_filial_hist.cod_un_realz
                             ,rec_srv_realizado_filial_hist.qtd_realz
                             ,rec_srv_realizado_filial_hist.num_realz_x_meta
                             ,rec_srv_realizado_filial_hist.cod_un_realz_x_meta
                             ,rec_srv_realizado_filial_hist.vlr_premio_fil_calc
                             ,rec_srv_realizado_filial_hist.cod_un_premio_fil_calc
                             ,rec_srv_realizado_filial_hist.dt_ini_sit_srv
                             ,rec_srv_realizado_filial_hist.cod_usuario
                             ,rec_srv_realizado_filial_hist.cod_escala
                             ,rec_srv_realizado_filial_hist.num_seq_escala_fx
                             ,rec_srv_realizado_filial_hist.num_realz_fx
                             ,rec_srv_realizado_filial_hist.cod_un_realz_fx
                             );

                  ----------------------------------------------------------------
                  -- atualiza o registro da tabela original
                  ----------------------------------------------------------------
                  update srv_realizado_filial
                     set num_meta                 = p_rec_srv_realz_fil.num_meta
                        ,cod_un_meta              = p_rec_srv_realz_fil.cod_un_meta
                        ,num_realz                = p_rec_srv_realz_fil.num_realz
                        ,cod_un_realz             = p_rec_srv_realz_fil.cod_un_realz
                        ,qtd_realz                = p_rec_srv_realz_fil.qtd_realz
                        ,num_realz_x_meta         = p_rec_srv_realz_fil.num_realz_x_meta
                        ,cod_un_realz_x_meta      = p_rec_srv_realz_fil.cod_un_realz_x_meta
                        ,vlr_premio_fil_calc      = p_rec_srv_realz_fil.vlr_premio_fil_calc
                        ,cod_un_premio_fil_calc   = p_rec_srv_realz_fil.cod_un_premio_fil_calc
                        ,dt_ini_sit_srv           = p_rec_srv_realz_fil.dt_ini_sit_srv
                        ,cod_usuario              = p_rec_srv_realz_fil.cod_usuario
                        ,cod_escala               = p_rec_srv_realz_fil.cod_escala
                        ,num_seq_escala_fx        = p_rec_srv_realz_fil.num_seq_escala_fx
                        ,num_realz_fx             = p_rec_srv_realz_fil.num_realz_fx
                        ,cod_un_realz_fx          = p_rec_srv_realz_fil.cod_un_realz_fx
                   --
                   where cod_indic                = p_rec_srv_realz_fil.cod_indic
                     and cod_emp                  = p_rec_srv_realz_fil.cod_emp
                     and cod_fil                  = p_rec_srv_realz_fil.cod_fil
                     and num_ano                  = p_rec_srv_realz_fil.num_ano
                     and num_mes                  = p_rec_srv_realz_fil.num_mes;
               --
               else
                  p_cod_erro     := 1;
                  p_descr_erro   := 'Erro ao selecionar realz fil p/ inserir no hist - reg nao encontrado ' ||
                                    ' - p_cod_indic: '                                   || p_rec_srv_realz_fil.cod_indic ||
                                    ' - p_cod_fil: '                                     || p_rec_srv_realz_fil.cod_fil   ||
                                    ' - erro: '                                          || sqlerrm;
               end if;
            exception
               when others then
                  p_cod_erro     := 1;
                  p_descr_erro   := 'Erro ao selecionar e atualizar realz fil no hist - p_cod_indic: '|| p_rec_srv_realz_fil.cod_indic ||
                                    ' - p_cod_fil: '                                                  || p_rec_srv_realz_fil.cod_fil   ||
                                    ' - erro: '                                                       || sqlerrm;
            end;
         --
         when others then
            p_cod_erro     := 1;
            p_descr_erro   := 'Erro geral ao inserir realizado filial - p_cod_indic: '|| p_rec_srv_realz_fil.cod_indic ||
                              ' - p_cod_fil: '                                        || p_rec_srv_realz_fil.cod_fil   ||
                              ' - erro: '                                             || sqlerrm;
      end;


      --
      commit;

   exception
      when others then
         p_cod_erro     := 1;
         p_descr_erro   := 'Erro geral na proc prc_insere_realz_fil - p_cod_indic: '|| p_rec_srv_realz_fil.cod_indic ||
                           ' - p_cod_fil: '                                         || p_rec_srv_realz_fil.cod_fil   ||
                           ' - erro: '                                              || sqlerrm;
   end prc_insere_realz_fil;

   -----------------------------------------------------------------------------------------------
   -- CALCULA ATINGIMENTO (CALCULO) FILIAL
   -----------------------------------------------------------------------------------------------
    procedure prc_calc_ating_fil_Lj (p_num_ano      in number
                                    ,p_num_mes      in number
                                    ,p_cod_emp      in number    default null
                                    ,p_cod_fil      in number    default null
                                    ,p_cod_indic    in number    default null
                                    ,p_descr_indic  in varchar2  default null
                                    ,p_cod_usuario  in number
                                    ,p_cod_erro     out number
                                    ,p_descr_erro   out varchar2
                                     ) is

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
            and i.cod_grp_indic       NOT IN (4,5,9,10) -- SAX / Seguro SAX / TLMKT
            and i.cod_indic_sis       is null
            and (i.descr_indic        = nvl(p_descr_indic, i.descr_indic))
            and i.flg_indic_ativ      = 'S'
       order by g.cod_grp_indic
               ,i.cod_indic;


      -- FILIAIS
      cursor c_fil (p_cod_emp number
                   ,p_cod_fil number) is
         select f.cod_emp
               ,f.cod_fil
               ,nvl(f.flg_meta_100_pct_realz, 'N') flg_meta_100_pct_realz
           from srv_filial               f
          where f.flg_ativ             = 'S'
            and (f.cod_emp             = nvl(p_cod_emp, f.cod_emp)) --p_cod_emp or p_cod_emp is null)
            and (f.cod_fil             = nvl(p_cod_fil, f.cod_fil)) --p_cod_fil or p_cod_fil is null);
       order by f.cod_fil;


      -- variaveis controle
      v_var_sql                        varchar2(2000);
      v_nm_tab_realz_fu                varchar2(30);
      v_nm_tab_realz_fi                varchar2(30);
      v_nm_tab_meta                    varchar2(30);
      v_nm_tab_realz_fi_pc             varchar2(30);
      v_nm_tab_realz_fi_bp             varchar2(30);
      v_nm_tab_realz_fi_odo            varchar2(30);
      v_nm_tab_realz_fi_mm             varchar2(30);

      -- variaveis calculo
      v_vlr_vdas_bruta                 number(11,2);
      v_vlr_devolucao                  number(11,2);
      v_vlr_realizado                  srv_realizado_filial.num_realz%type;
      v_qtd_realizado                  number(11,2);--srv_realizado_filial.qtd_realz%type;
      v_cod_un_realz                   srv_realizado_filial.cod_un_realz%type;
      v_meta                           srv_meta_filial.num_meta%type;
      v_cod_un_meta                    srv_meta_filial.cod_un_meta%type;
      v_vlr_premio_fil                 srv_meta_filial.vlr_premio_fil%type;
      v_pct_calc_meta                  srv_meta_filial.pct_calc_meta%type;
      v_num_realz_x_meta               srv_realizado_filial.num_realz_x_meta%type;
      v_cod_un_realz_x_meta            srv_realizado_filial.cod_un_realz_x_meta%type;
      v_vlr_premio_fil_calc            srv_realizado_filial.vlr_premio_fil_calc%type := 0;
      v_cod_escala                     srv_escala_faixa.cod_escala%type;
      v_num_seq_escala_fx              srv_escala_faixa.num_seq_escala_fx%type;
      v_num_realz_fx                   srv_escala_faixa.num_realz_fx%type;
      v_cod_un_realz_fx                srv_escala_faixa.cod_un_realz_fx%type;
      v_flg_pct_100                    srv_escala_faixa.flg_pct_100%type;
      v_num_limite_fx                  srv_escala_faixa.num_limite_fx%type;
      --v_flg_meta_100_pct_realz         srv_filial.flg_meta_100_pct_realz%type;
      v_vr_vendas_tot                  srv_meta_filial.num_meta%type;
      v_qtde_vendas_parc               srv_meta_filial.num_meta%type;

      -- var log
      v_cod_fil                        srv_filial.cod_fil%type;
      v_cod_tipo_rem_var               srv_tipo_rem_var.cod_tipo_rem_var%type;
      v_cod_grp_indic                  srv_grupo_indicador.cod_grp_indic%type;
      v_cod_indic                      srv_indicador.cod_indic%type;

      -- cursores
      cur_realz                        pkg_srv_calc_rem_var.typ_cursor;

      --
      rec_srv_realizado_filial         srv_realizado_filial%rowtype;

      -- exception
      e_erro_cria_tab                  exception;

   ---------------------------------------------------------------------------
   begin

      v_data    := sysdate;
      --
      v_dt_ini  := '01/' || trim(to_char(p_num_mes, '00')) || '/' || trim(to_char(p_num_ano, '0000')) || ' ' || '00:00:00';
      --
      select to_char(last_day(to_date(('01/' || to_char(p_num_mes, '00') || '/'||to_char(p_num_ano, '0000')), 'dd/mm/yyyy')), 'dd/mm/yyyy')|| ' ' || '23:59:59'
        into v_dt_fim
        from dual;


      -----------------------------------------------------------------------
      -- INICIO CALCULO
      -- SELECIONA FILIAIS ATIVAS
      -----------------------------------------------------------------------
      for r_fil in c_fil (p_cod_emp
                         ,p_cod_fil) loop


         v_cod_fil := r_fil.cod_fil;

         -----------------------------------------------------------------------
         -- SELECIONA INDICADORES PARA O GRUPO DE REMUNERACAO LOJAS
         -----------------------------------------------------------------------
         for r_indic_rem_loja in c_indic_rem_loja loop


            v_cod_tipo_rem_var := r_indic_rem_loja.cod_tipo_rem_var;
            v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
            v_cod_indic        := r_indic_rem_loja.cod_indic;


            -----------------------------------------------------------------------
            -- SE INDICADOR VENDAS
            -----------------------------------------------------------------------
            if r_indic_rem_loja.descr_indic = 'VENDAS' then

               --
               v_nm_tab_realz_fi := 'SRV_REALZFI_VENDAS_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_vlr_vdas_bruta                 := 0;
               v_vlr_devolucao                  := 0;
               v_vlr_realizado                  := 0;
               v_qtd_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;


               --
               v_var_sql :=              ' select nvl(r.vlr_vdas_bruta,0) vlr_vdas_bruta ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(r.vlr_devolucao,0)  vlr_devolucao '  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(r.vlr_realizado,0)  vlr_realizado '  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||'  r '           || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where r.fil_cod = '                            || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_vlr_vdas_bruta
                       ,v_vlr_devolucao
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;

               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_vlr_vdas_bruta      := nvl(v_vlr_vdas_bruta, 0);
               v_vlr_devolucao       := nvl(v_vlr_devolucao, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               --
               --
               v_cod_un_realz           := 1; -- unidade de valor


               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then

                  -- se erro ao calcular a meta utilizar o realizado
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de valor


                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --1 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               -- calcula realizado X meta da filial
               -- se a meta estiver zerada entao assumir como vlr de meta X realizado o realizado
               if v_meta > 0 then
                  v_num_realz_x_meta       := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta       := 2; -- unidade de percentual


               -- verificar o realizado x meta na escala (guardar na variavel v_num_realz_fx)
               -- para calcular o vr do premio calculado para a filial para rateio entre os cargos operacionais
               -- verificar se existe uma escala cadastrada para o indicador,
               -- se nao houver procurar para o grupo de indic
               -- se nao houver procurar para o grupo de rem var
               --
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic      -- p_cod_indic
                                  ,r_indic_rem_loja.cod_grp_indic  -- p_cod_grp_indic
                                  ,null                            -- p_cod_grp_rem_var
                                  ,v_num_realz_x_meta
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

/*                  v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                          ,p_subject => 'SRV Calculo Atingimento Filial - Escala nao encontrada: '
                                                                          ,p_body    => v_body
                                                                          );*/

                  -- logar tabela
                  v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                            ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                            ,p_num_ano     => p_num_ano
                                                            ,p_num_mes     => p_num_mes
                                                            ,p_cod_fil     => v_cod_fil
                                                            ,p_cod_func    => null
                                                            ,p_cod_indic   => v_cod_indic
                                                            ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                            );

--                  raise e_erro;
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
                  v_num_realz_x_meta := v_num_limite_fx;
               end if;

               -- vlr do premio calculado para a filial, conforme o atingimento da meta
               v_vlr_premio_fil_calc         := (v_vlr_premio_fil * v_num_realz_fx) / 100;


               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_qtd_realizado := null;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR CARTAO MARISA APROVADO PL E ITAU
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'CARTAO MARISA APROVADO PL E ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CART_APROV_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CART_APROV_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));

               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;


               --

               v_var_sql :=              ' select (nvl(a.qtde ,0) - nvl(b.qtd,0)) qtde ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||' a ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '        ,(select count(cli_cpf) qtd'  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '            from '||v_nm_tab_realz_fu||' c  ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '           where c.flg_doc_digitalizado =''N''' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '             and c.fil_cod =  '|| r_fil.cod_fil ||') b ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   where a.fil_cod = '|| r_fil.cod_fil || chr(13) || chr(10);


               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;

               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --2 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado      = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR CARTAO MARISA ATIVADO PL E ITAU
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'CARTAO MARISA ATIVADO PL E ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CART_ATIV_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CART_ATIV_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '             || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''     || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                    || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --3 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR BONUS CELULAR
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'BONUS CELULAR' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_BONUS_CEL_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_BONUS_CEL_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '             || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''     || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                    || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --3 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR PARTICIPACAO DO CARTAO %
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'PARTICIPACAO DO CARTAO %' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_PART_CARTAO_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_PART_CARTAO_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select part qtde '             || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi   || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '             || r_fil.cod_fil;

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 2; -- unidade de PERCENTUAL

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );
               v_meta          := nvl(v_pct_calc_meta,0);
               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 2; -- unidade de PERCENTUAL

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;
               --3 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;


            --------------------------------------------------------------------------------------
            -- SE INDICADOR VENDA COM JUROS PL
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'VENDA COM JUROS PL' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CPR_JUR_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CPR_JUR_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0)              qtde '               || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(vlr_realz_cpr_jur,0) vlr_realz_cpr_jur '  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi||''                     || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                   || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas total PL e Itau
               -- para ter q meta em valor
               --ALTERADO ALEXANDRE BEZERRA nome da tabela
               begin
                  execute immediate 'select vlr_liq_vda_total_PL_ita
                                       from SRV_TMP_META_VENDAS_PL_'||
                                       trim(to_char(p_num_ano, '0000'))  ||
                                       trim(to_char(p_num_mes, '00'))    ||
                                    ' where cod_fil = :cod_fil ' into v_vr_vendas_tot using r_fil.cod_fil;
               exception
                  when others then
                     v_vr_vendas_tot := 0;
                     p_cod_erro      := 3;
               end;
               v_meta          := (nvl(v_pct_calc_meta,0) * v_vr_vendas_tot) / 100;
               v_cod_un_meta   := 1; -- unidade de VALOR


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de VALOR

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );


               --4 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL


            --------------------------------------------------------------------------------------
            -- SE INDICADOR VENDA COM JUROS ITAU
            --------------------------------------------------------------------------------------

            elsif r_indic_rem_loja.descr_indic = 'VENDA COM JUROS ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CPR_JUR_IT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CPR_JUR_IT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0)              qtde '               || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(VLR_REALZ_BRUTO_CPR_JUR,0) VLR_REALZ_BRUTO_CPR_JUR '  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi||''                     || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                   || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas total Itau
               -- para ter q meta em valor
               begin
                  execute immediate 'select VLR_MOVTO
                                       from SRV_TMP_META_VENDAS_I_'||
                                       trim(to_char(p_num_ano, '0000'))  ||
                                       trim(to_char(p_num_mes, '00'))    ||
                                    ' where cod_fil = :cod_fil ' into v_vr_vendas_tot using r_fil.cod_fil;
               exception
                  when others then
                     v_vr_vendas_tot := 0;
                     p_cod_erro      := 3;
               end;
               v_meta          := (nvl(v_pct_calc_meta,0) * v_vr_vendas_tot) / 100;
               v_cod_un_meta   := 1; -- unidade de VALOR


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de VALOR

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );

               --5 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

            --------------------------------------------------------------------------------------
            -- SE INDICADOR VENDA COM JUROS PL E ITAU
            --------------------------------------------------------------------------------------

            elsif r_indic_rem_loja.descr_indic = 'VENDA COM JUROS PL E ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_VJ_PL_ITAU_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_VJ_PL_ITAU_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              'select sum(nvl(qtde,0))                                        ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '      ,sum(nvl(vlr_realz_cpr_jur,0))                           ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  from (select fil_cod                                         ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '             , qtde                                            ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '             , vlr_realz_cpr_jur                               ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '          from srv_realzfi_cpr_jur_'
                                                             ||trim(to_char(p_num_ano,'0000'))
                                                             ||trim(to_char(p_num_mes, '00'))||'         ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       union all                                               ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '        select fil_cod                                         ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '             , qtde                                            ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '             , vlr_realz_bruto_cpr_jur                         ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '          from srv_realzfi_cpr_jur_it_'
                                                             ||trim(to_char(p_num_ano,'0000'))
                                                             ||trim(to_char(p_num_mes, '00'))||')        ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || ' where fil_cod = ' || r_fil.cod_fil                              || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas total Itau
               -- para ter q meta em valor
               begin
                  execute immediate 'select sum(vlr_movto) vlr_movto
                                       from(select *
                                              from srv_tmp_meta_vendas_i_'||trim(to_char(p_num_ano,'0000'))||trim(to_char(p_num_mes,'00'))||'
                                               union all
                                            select *
                                              from srv_tmp_meta_vendas_pl_'||trim(to_char(p_num_ano,'0000'))||trim(to_char(p_num_mes,'00'))||')
                                      where fil_cod = :cod_fil ' into v_vr_vendas_tot using r_fil.cod_fil;
               exception
                  when others then
                     v_vr_vendas_tot := 0;
                     p_cod_erro      := 3;
               end;

               v_meta          := (nvl(v_pct_calc_meta,0) * v_vr_vendas_tot) / 100;
               v_cod_un_meta   := 1; -- unidade de VALOR


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de VALOR

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );

               --6 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               if v_meta > 0 then--
                  v_num_realz_x_meta      := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL


            --------------------------------------------------------------------------------------
            -- SE INDICADOR COMPRA TRANQUILA PL
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'COMPRA TRANQUILA PL' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CPRA_TRANQ_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CPRA_TRANQ_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '                                 || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(vlr_realz_cpra_tranq,0) vlr_realz_cpra_tranq ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''                         || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                        || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas com juros PL e Itau
               -- para ter a meta em valor
               v_nm_tab_meta := 'SRV_TMP_META_CPR_TRANQ_'
                                ||trim(to_char(p_num_ano, '0000'))
                                ||trim(to_char(p_num_mes, '00'));
               begin
                  execute immediate 'select qtde from ' || v_nm_tab_meta || ' where fil_cod = :cod_fil ' into v_qtde_vendas_parc using r_fil.cod_fil;
               exception
                  when others then
                     v_qtde_vendas_parc := 0;
                     p_cod_erro         := 3;
               end;

               v_meta         := (nvl(v_pct_calc_meta,0) * v_qtde_vendas_parc) / 100;
               v_cod_un_meta  := 3; -- unidade de UNIDADE


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );

               --6 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;


            --------------------------------------------------------------------------------------
            -- SE INDICADOR COMPRA TRANQUILA ITAU
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'COMPRA TRANQUILA ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CPRA_TRANQI_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CPRA_TRANQI_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '                                 || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(VLR_REALZ_BRUTO_CPR_TRANQ,0) VLR_REALZ_BRUTO_CPR_TRANQ ' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''                         || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                        || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas com juros PL e Itau
               -- para ter a meta em valor
               v_nm_tab_meta := 'SRV_TMP_META_CPR_TRANQI_'
                                ||trim(to_char(p_num_ano, '0000'))
                                ||trim(to_char(p_num_mes, '00'));
               begin
                  execute immediate 'select qtde_vds_parc_pl_ita  from ' || v_nm_tab_meta || ' where fil_cod = :cod_fil ' into v_qtde_vendas_parc using r_fil.cod_fil;
               exception
                  when others then
                     v_qtde_vendas_parc := 0;
                     p_cod_erro         := 3;
               end;

               v_meta         := (nvl(v_pct_calc_meta,0) * v_qtde_vendas_parc) / 100;
               v_cod_un_meta  := 3; -- unidade de UNIDADE


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );

               --7 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR COMPRA TRANQUILA PL E ITAU
            --------------------------------------------------------------------------------------

            elsif r_indic_rem_loja.descr_indic = 'COMPRA TRANQUILA PL E ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CPRA_TRANQI_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CPRA_TRANQI_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql := 'select sum(qtde) qtde
                                   ,sum(vlr_realz_cpra_tranq) vlr_realz_cpra_tranq
                               from(select nvl(qtde,0) qtde
                                          ,nvl(vlr_realz_cpra_tranq,0) vlr_realz_cpra_tranq
                                      from srv_realzfi_cpra_tranq_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                       union all
                                    select nvl(qtde,0)
                                          ,nvl(vlr_realz_bruto_cpr_tranq,0)
                                      from srv_realzfi_cpra_tranqi_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                   )';

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas com juros PL e Itau
               -- para ter a meta em valor
               v_nm_tab_meta := 'SRV_TMP_META_CPR_TRANQI_'
                                ||trim(to_char(p_num_ano, '0000'))
                                ||trim(to_char(p_num_mes, '00'));
               begin
                  execute immediate 'select sum(qtde) qtde
                                       from(select qtde
                                              from SRV_TMP_META_CPR_TRANQ_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))||'
                                             where fil_cod = ' || r_fil.cod_fil ||'
                                             union all
                                            select qtde_vds_parc_pl_ita
                                              from SRV_TMP_META_CPR_TRANQI_'||trim(to_char(p_num_ano, '0000'))||trim(to_char(p_num_mes, '00'))||'
                                             where fil_cod = ' || r_fil.cod_fil ||'
                                           )' into v_qtde_vendas_parc;
               exception
                  when others then
                     v_qtde_vendas_parc := 0;
                     p_cod_erro         := 3;
               end;

               v_meta         := (nvl(v_pct_calc_meta,0) * v_qtde_vendas_parc) / 100;
               v_cod_un_meta  := 3; -- unidade de UNIDADE


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );

               --7 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;


            --------------------------------------------------------------------------------------
            -- SE INDICADOR BOLSA PROTEGIDA
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'BOLSA PROTEGIDA PL' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_MAR_PROT_LJ_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_MAR_PROT_LJ_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '           || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''   || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                  || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --9 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR BOLSA PROTEGIDA ITAU
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'BOLSA PROTEGIDA ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_MAR_PROT_IT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_MAR_PROT_IT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '           || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''   || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                  || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --10 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR BOLSA PROTEGIDA PL E ITAU
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'BOLSA PROTEGIDA PL E ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_BPROT_PL_IT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_BPROT_PL_IT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '           || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''   || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                  || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --10 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;


            --------------------------------------------------------------------------------------
            -- SE INDICADOR CARTAO MARISA TOMBAMENTO ITAUCARD
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'CARTAO MARISA TOMBAMENTO ITAUCARD' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CART_TOMB_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CART_TOMB_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '          || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '||v_nm_tab_realz_fi||''    || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                 || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --12 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR CARTAO MARISA DESBLOQUEIO ITAUCARD
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'CARTAO MARISA DESBLOQUEIO ITAUCARD' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CT_IT_DESB_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CT_IT_DESB_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '          || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                 || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;

                  --
                  exit when cur_realz%notfound;

               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --13 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR ASSISTENCIA ODONTOLOGICA LOJA
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'ASSISTENCIA ODONTOLOGICA LOJA' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_ASS_ODO_LJ_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_ASS_ODO_LJ_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(sum(qtde),0) qtde '         || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||'' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --14 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR CARRO E LAR PL MARISA FAMILIA CASA PROTEGIDA
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'CARRO E LAR PL MARISA FAMILIA CASA PROTEGIDA' then
               --
               v_nm_tab_realz_fi_mm    := 'SRV_REALZFI_SEG_MAR_MUL_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));

               v_nm_tab_realz_fi_pc    := 'SRV_REALZFI_PROT_CEL_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));

               v_nm_tab_realz_fi_bp    := 'SRV_REALZFI_BPROT_PL_IT_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql := 'select sum(qtde) qtde
                               from(select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_mm||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                       union all
                                    select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_pc||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                       union all
                                    select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_bp||'
                                     where fil_cod = ' || r_fil.cod_fil ||')';


               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
              begin
                select sum(m.num_meta)
                      ,m.cod_un_meta
                      ,sum(m.vlr_premio_fil)
                      ,sum(m.pct_calc_meta)
                  into v_meta
                      ,v_cod_un_meta
                      ,v_vlr_premio_fil
                      ,v_pct_calc_meta
                  from srv_meta_filial   m
                 where m.cod_indic     in (21,24,37)
                   and m.cod_emp       = r_fil.cod_emp
                   and m.cod_fil       = r_fil.cod_fil
                   and m.num_ano       = p_num_ano
                   and m.num_mes       = p_num_mes
                   group by m.cod_un_meta;
               exception
                 when no_data_found then
                   null;
               end;

               --15 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR SEGUROS + ODONTO
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'SEGUROS + ODONTO' then
               --
               v_nm_tab_realz_fi_mm    := 'SRV_REALZFI_SEG_MAR_MUL_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));

               v_nm_tab_realz_fi_pc    := 'SRV_REALZFI_PROT_CEL_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));

               v_nm_tab_realz_fi_bp    := 'SRV_REALZFI_BPROT_PL_IT_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));

               v_nm_tab_realz_fi_odo   := 'SRV_REALZFI_ASS_ODO_LJ_'
                                       ||trim(to_char(p_num_ano, '0000'))
                                       ||trim(to_char(p_num_mes, '00'));

               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql := 'select sum(qtde) qtde
                               from(select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_mm||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                       union all
                                    select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_pc||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                       union all
                                    select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_bp||'
                                     where fil_cod = ' || r_fil.cod_fil ||'
                                       union all
                                    select nvl(qtde,0) qtde
                                      from '||v_nm_tab_realz_fi_odo||'
                                     where fil_cod = ' || r_fil.cod_fil ||')';

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
              begin
                select sum(m.num_meta)
                      ,m.cod_un_meta
                      ,sum(m.vlr_premio_fil)
                      ,sum(m.pct_calc_meta)
                  into v_meta
                      ,v_cod_un_meta
                      ,v_vlr_premio_fil
                      ,v_pct_calc_meta
                  from srv_meta_filial   m
                 where m.cod_indic     in (21,24,37,41)
                   and m.cod_emp       = r_fil.cod_emp
                   and m.cod_fil       = r_fil.cod_fil
                   and m.num_ano       = p_num_ano
                   and m.num_mes       = p_num_mes
                   group by m.cod_un_meta;
               exception
                 when no_data_found then
                   null;
               end;

               --15 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR PROTECAO CELULAR/PROTECAO FINANCEIRA
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'PROTECAO CELULAR/PROTECAO FINANCEIRA' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_PR_CEL_FIN_'
                                        ||trim(to_char(p_num_ano, '0000'))
                                        ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_PR_CEL_FIN_'
                                        ||trim(to_char(p_num_ano, '0000'))
                                        ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql := ' select nvl(SUM(qtde), 0) qtde
                              FROM (select nvl(qtde, 0) qtde
                                      from '||v_nm_tab_realz_fi||'
                                     where fil_cod = '||r_fil.cod_fil||')';

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --11 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;


            --------------------------------------------------------------------------------------
            -- SE INDICADOR MARISA MULHER/AUTO PROTECAO/CASA PROTEGIDA
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'MARISA MULHER/AUTO PROTECAO/CASA PROTEGIDA' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_SEG_MAR_MUL_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_SEG_MAR_MUL_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '           || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '||v_nm_tab_realz_fi         || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = ' || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 3; -- unidade de UNIDADE

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 3; -- unidade de UNIDADE

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --11 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR CARTAO ADICIONAL PL E ITAU
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'CARTAO ADICIONAL PL E ITAU' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_CT_ADIC_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_CT_ADIC_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
 --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0)              qtde '               || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi||''                     || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                   || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas total PL e Itau
               -- para ter q meta em valor
               --ALTERADO ALEXANDRE BEZERRA nome da tabela
               begin
                  execute immediate 'select qtde
                                       from SRV_REALZFI_CART_APROV_'||
                                       trim(to_char(p_num_ano, '0000'))  ||
                                       trim(to_char(p_num_mes, '00'))    ||
                                    ' where fil_cod = :cod_fil ' into v_vr_vendas_tot using r_fil.cod_fil;
               exception
                  when others then
                     v_vr_vendas_tot := 0;
                     p_cod_erro      := 3;
               end;
               v_meta          := (nvl(v_pct_calc_meta,0) * v_vr_vendas_tot) / 100;
               v_cod_un_meta   := 1; -- unidade de VALOR


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 1; -- unidade de VALOR

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_qtd_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_qtd_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_qtd_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );


               --4 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR PARCELAMENTO DE FATURAS
            --------------------------------------------------------------------------------------

            elsif r_indic_rem_loja.descr_indic = 'PARCELAMENTO DE FATURAS' then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_PARC_FAT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_PARC_FAT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0)              qtde '               || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi                         || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                   || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- calcula a meta, que veio em percentual, sobre o valor de vendas total Itau
               -- para ter q meta em valor
               begin
                  execute immediate ' select sum(oferta) qtde
                                        from MV_CCM_TERMOMETRO_PARC_FATURA'||v_DbLink||'
                                       where ano = '||p_num_ano||'
                                         and mes = '||p_num_mes||'
                                         and filial = :cod_fil ' into v_vr_vendas_tot using r_fil.cod_fil;
               exception
                  when others then
                     v_vr_vendas_tot := 0;
                     p_cod_erro      := 3;
               end;
               v_meta          := (nvl(v_pct_calc_meta,0) * v_vr_vendas_tot) / 100;
               v_cod_un_meta   := 1; -- unidade de VALOR


               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 1; -- unidade de VALOR

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_qtd_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_qtd_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_qtd_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               -- atualizar o valor da meta com o calculado pelo percentual
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               prc_atualiza_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                     ,p_cod_emp          => r_fil.cod_emp
                                     ,p_cod_fil          => r_fil.cod_fil
                                     ,p_ano_meta         => p_num_ano
                                     ,p_mes_meta         => p_num_mes
                                     ,p_meta             => v_meta
                                     ,p_cod_un_meta      => v_cod_un_meta
                                     ,p_cod_erro         => p_cod_erro
                                     ,p_descr_erro       => p_descr_erro
                                     );

               --5 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

             elsif r_indic_rem_loja.descr_indic = 'PPT - PECAS POR TICKET' then

               --
               v_nm_tab_realz_fi := 'SRV_REALZFI_PPT_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(qtde,0) qtde '         || chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '||v_nm_tab_realz_fi||''   || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 14; -- unidade de PECAS/TKT

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_qtd_realizado;
                  v_cod_un_meta      := 14; -- unidade de PECAS/TKT

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --11 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            end if;

            --
            rec_srv_realizado_filial.cod_indic               := r_indic_rem_loja.cod_indic;
            rec_srv_realizado_filial.cod_emp                 := r_fil.cod_emp;
            rec_srv_realizado_filial.cod_fil                 := r_fil.cod_fil;
            rec_srv_realizado_filial.num_ano                 := p_num_ano;
            rec_srv_realizado_filial.num_mes                 := p_num_mes;
            rec_srv_realizado_filial.num_meta                := v_meta;
            rec_srv_realizado_filial.cod_un_meta             := v_cod_un_meta;
            rec_srv_realizado_filial.num_realz               := v_vlr_realizado;
            rec_srv_realizado_filial.cod_un_realz            := v_cod_un_realz;
            rec_srv_realizado_filial.qtd_realz               := v_qtd_realizado;
            rec_srv_realizado_filial.num_realz_x_meta        := v_num_realz_x_meta;
            rec_srv_realizado_filial.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;
            if r_indic_rem_loja.descr_indic = 'VENDAS' then
               rec_srv_realizado_filial.vlr_premio_fil_calc     := v_vlr_premio_fil_calc;
               rec_srv_realizado_filial.cod_un_premio_fil_calc  := 1; -- unidade de VALOR
            else
               rec_srv_realizado_filial.vlr_premio_fil_calc     := null;
               rec_srv_realizado_filial.cod_un_premio_fil_calc  := null;
            end if;
            rec_srv_realizado_filial.dt_ini_sit_srv          := v_data;
            rec_srv_realizado_filial.cod_usuario             := p_cod_usuario;
            rec_srv_realizado_filial.cod_escala              := v_cod_escala;
            rec_srv_realizado_filial.num_seq_escala_fx       := v_num_seq_escala_fx;
            rec_srv_realizado_filial.num_realz_fx            := v_num_realz_fx;
            rec_srv_realizado_filial.cod_un_realz_fx         := v_cod_un_realz_fx;


            -- insere o realizado da filial para o indicador
            prc_insere_realz_fil (p_rec_srv_realz_fil  => rec_srv_realizado_filial
                                 ,p_cod_erro           => p_cod_erro
                                 ,p_descr_erro         => p_descr_erro
                                  );
            --
            if p_cod_erro is not null then

               -- enviar email
               v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*               v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                       ,p_subject => 'SRV Calculo Atingimento Filial - Erro ao inserir tabela realizado filial: '
                                                                       ,p_body    => v_body
                                                                       );*/

               -- logar tabela
               v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                         ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                         ,p_num_ano     => p_num_ano
                                                         ,p_num_mes     => p_num_mes
                                                         ,p_cod_fil     => v_cod_fil
                                                         ,p_cod_func    => null
                                                         ,p_cod_indic   => v_cod_indic
                                                         ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                         );

--               raise e_erro;
            -- if p_cod_erro is not null
            end if;



            -- commit por indicador
            commit;

         -- c_indic_rem_loja
         end loop;

         -- commit por filial
         commit;

      -- c_fil
      end loop;

      --
      commit;


   exception

      when e_erro_cria_tab then

         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*         v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                 ,p_subject => 'SRV Calculo Atingimento Filial - Erro ao criar tabelas de apuracao: '
                                                                 ,p_body    => v_body
                                                                 );*/

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                   ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => null
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
/*            v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                    ,p_subject => 'SRV Calculo Atingimento Filial - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );*/
         --v_ins_log_erro is not null
         end if;


         -- retorna
         return;


      when e_erro then
         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*         v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                 ,p_subject => 'SRV Calculo Atingimento Filial - Erro exception e_erro: '
                                                                 ,p_body    => v_body
                                                                 );*/

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                   ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => null
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
/*            v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                    ,p_subject => 'SRV Calculo Atingimento Filial - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );*/
         --v_ins_log_erro is not null
         end if;


         -- retorna
         return;
      --
      when others then
         p_cod_erro     := 1;
         p_descr_erro   := 'Erro geral ao calcular Atingimento Filiais Remun Loja: ' ||
                           'cod_fil: '           || v_cod_fil             || ' - ' ||
                           'cod_tipo_rem_var: '  || v_cod_tipo_rem_var    || ' - ' ||
                           'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                           'cod_indic: '         || v_cod_indic           || ' - ' || sqlerrm;


         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*         v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                 ,p_subject => 'SRV Calculo Atingimento Filial - Erro geral exception: '
                                                                 ,p_body    => v_body
                                                                 );*/

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                   ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => null
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
/*            v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                    ,p_subject => 'SRV Calculo Atingimento Filial - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );*/
         --v_ins_log_erro is not null
         end if;

   end prc_calc_ating_fil_Lj;



   -----------------------------------------------------------------------------------------------
   -- CALCULA ATINGIMENTO (CALCULO) SAX
   -----------------------------------------------------------------------------------------------
    procedure prc_calc_ating_fil_SAX (p_num_ano      in number
                                     ,p_num_mes      in number
                                     ,p_cod_emp      in number    default null
                                     ,p_cod_fil      in number    default null
                                     ,p_cod_indic    in number    default null
                                     ,p_descr_indic  in varchar2  default null
                                     ,p_cod_usuario  in number
                                     ,p_cod_erro     out number
                                     ,p_descr_erro   out varchar2
                                      ) is

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
            and i.cod_grp_indic       in (4,5,9,3) -- SAX e Seguro SAX
            and i.cod_indic_sis       ='SAX_PSF'
            and (i.descr_indic        = nvl(p_descr_indic, i.descr_indic))
            and i.flg_indic_ativ      = 'S'
       order by g.cod_grp_indic
               ,i.cod_indic;


      -- FILIAIS
      cursor c_fil (p_cod_emp number
                   ,p_cod_fil number) is
         select f.cod_emp
               ,f.cod_fil
               ,nvl(f.flg_meta_100_pct_realz, 'N') flg_meta_100_pct_realz
--Alt201401               ,FLOOR(MONTHS_BETWEEN(SYSDATE,trunc(nvl(f.dt_inaug_filial,sysdate)) )) mes_inauguracao
               ,0 mes_inauguracao
           from srv_filial               f
          where f.flg_ativ             = 'S'
            and (f.cod_emp             = nvl(p_cod_emp, f.cod_emp)) --p_cod_emp or p_cod_emp is null)
            and (f.cod_fil             = nvl(p_cod_fil, f.cod_fil)) --p_cod_fil or p_cod_fil is null);
       order by f.cod_fil;


      -- variaveis controle
      v_var_sql                        varchar2(2000);
      v_nm_tab_realz_fu                varchar2(30);
      v_nm_tab_realz_fi                varchar2(30);
      v_nm_tab_realz_fi_aux1           varchar2(30);
      v_mes                            number;

      v_vlr_realizado                  srv_realizado_filial.num_realz%type;
      v_qtd_realizado                  srv_realizado_filial.qtd_realz%type;
      v_cod_un_realz                   srv_realizado_filial.cod_un_realz%type;
      v_meta                           srv_meta_filial.num_meta%type;
      v_cod_un_meta                    srv_meta_filial.cod_un_meta%type;
      v_vlr_premio_fil                 srv_meta_filial.vlr_premio_fil%type;
      v_pct_calc_meta                  srv_meta_filial.pct_calc_meta%type;
      v_num_realz_x_meta               srv_realizado_filial.num_realz_x_meta%type;
      v_cod_un_realz_x_meta            srv_realizado_filial.cod_un_realz_x_meta%type;
      v_vlr_premio_fil_calc            srv_realizado_filial.vlr_premio_fil_calc%type := 0;
      v_cod_escala                     srv_escala_faixa.cod_escala%type;
      v_num_seq_escala_fx              srv_escala_faixa.num_seq_escala_fx%type;
      v_num_realz_fx                   srv_escala_faixa.num_realz_fx%type;
      v_cod_un_realz_fx                srv_escala_faixa.cod_un_realz_fx%type;
      v_flg_pct_100                    srv_escala_faixa.flg_pct_100%type;
      v_num_limite_fx                  srv_escala_faixa.num_limite_fx%type;
      --v_flg_meta_100_pct_realz         srv_filial.flg_meta_100_pct_realz%type;

      -- var log
      v_cod_fil                        srv_filial.cod_fil%type;
      v_cod_tipo_rem_var               srv_tipo_rem_var.cod_tipo_rem_var%type;
      v_cod_grp_indic                  srv_grupo_indicador.cod_grp_indic%type;
      v_cod_indic                      srv_indicador.cod_indic%type;

      -- cursores
      cur_realz                        pkg_srv_calc_rem_var.typ_cursor;

      --
      rec_srv_realizado_filial         srv_realizado_filial%rowtype;

      -- exception
      e_erro_cria_tab                  exception;

   ---------------------------------------------------------------------------
   begin

      v_data    := sysdate;
      --
      v_dt_ini  := '01/' || trim(to_char(p_num_mes, '00')) || '/' || trim(to_char(p_num_ano, '0000')) || ' ' || '00:00:00';
      --
      select to_char(last_day(to_date(('01/' || to_char(p_num_mes, '00') || '/'||to_char(p_num_ano, '0000')), 'dd/mm/yyyy')), 'dd/mm/yyyy')|| ' ' || '23:59:59'
        into v_dt_fim
        from dual;


      -----------------------------------------------------------------------
      -- INICIO CALCULO
      -- SELECIONA FILIAIS ATIVAS
      -----------------------------------------------------------------------
      for r_fil in c_fil (p_cod_emp
                         ,p_cod_fil) loop


         v_cod_fil := r_fil.cod_fil;
         v_mes     := r_fil.mes_inauguracao;
         -----------------------------------------------------------------------
         -- SELECIONA INDICADORES PARA O GRUPO DE REMUNERACAO LOJAS
         -----------------------------------------------------------------------
         for r_indic_rem_loja in c_indic_rem_loja loop


            v_cod_tipo_rem_var := r_indic_rem_loja.cod_tipo_rem_var;
            v_cod_grp_indic    := r_indic_rem_loja.cod_grp_indic;
            v_cod_indic        := r_indic_rem_loja.cod_indic;



            --------------------------------------------------------------------------------------
            -- SE INDICADOR EMPRESTIMO PESSOAL (EP) SAX
            --------------------------------------------------------------------------------------
            if r_indic_rem_loja.descr_indic = 'EMPRESTIMO PESSOAL (EP) SAX'  then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               -- nao tem meta, entao o realizado X meta sera
               -- o realizado do seguro em quantidade (nao pegar valor) dividido pelo realizado
               -- em quantidade (nao pegar valor) do emprestimo
               v_var_sql :=              ' select nvl(quant_ep_real,0) quant_ep_real '            || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(vlr_liquido_emprest,0) vlr_liquido_emprest '|| chr(13) || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''                      || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                     || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de valor

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial SAX - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial SAX - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial SAX - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - prc_calc_ating_fil_SAX
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial SAX - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --17 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL


            --------------------------------------------------------------------------------------
            -- SE INDICADOR SEGURO EMPRESTIMO PESSOAL (EP) SAX
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'SEGURO EMPRESTIMO PESSOAL (EP) SAX'  then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               -- nao tem meta, entao o realizado X meta sera
               -- o realizado do seguro em quantidade (nao pegar valor) dividido pelo realizado
               -- em quantidade (nao pegar valor) do emprestimo
               v_var_sql :=              ' select nvl(quant_seguro_real,0) quant_seguro_real' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(quant_ep_real,0) quant_seguro_real    ' || chr(13)  || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''                  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                 || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --

               --ALTERACAO PARA O INDICADOR 17
               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
--                  v_cod_un_meta      := 1; -- unidade de valor

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial EMPRESTIMO SAX - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial EMPRESTIMO SAX - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial EMPRESTIMO SAX - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - prc_calc_ating_fil_SAX
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial SAX - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;


               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
--               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               v_meta                := nvl(v_meta, 0);
               --
               v_cod_un_meta   := 3; -- unidade de UNIDADE
               v_cod_un_realz     := 3; -- unidade de UNIDADE

               --18 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

            --------------------------------------------------------------------------------------
            -- SE INDICADOR EMPRESTIMO PESSOAL (EP) SAX / PSF
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'EMPRESTIMO PESSOAL (EP) SAX / PSF'  then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               -- nao tem meta, entao o realizado X meta sera
               -- o realizado do seguro em quantidade (nao pegar valor) dividido pelo realizado
               -- em quantidade (nao pegar valor) do emprestimo

               v_nm_tab_realz_fi_aux1 := 'SRV_REALZFI_SAQUE_FACIL_'
                                         ||trim(to_char(p_num_ano, '0000'))
                                         ||trim(to_char(p_num_mes, '00'));

               v_var_sql := ' SELECT SUM(QTDE) AS QTDE, SUM(VLR_LIQUIDO_EMPREST) AS VLR_LIQUIDO
                                FROM (SELECT NVL(QUANT_EP_REAL, 0) QTDE,
                                             NVL(VLR_LIQUIDO_EMPREST, 0) VLR_LIQUIDO_EMPREST
                                        FROM '|| v_nm_tab_realz_fi ||'
                                       WHERE FIL_COD = ' || r_fil.cod_fil || ')';
--                                      UNION ALL
--                                      SELECT NVL(QTDE, 0) QTDE_SQ_FAC, NVL(VLR_LIQUIDO_EMPREST, 0) QTDE
--                                        FROM '|| v_nm_tab_realz_fi_aux1 ||'
--                                       WHERE FIL_COD =  ' || r_fil.cod_fil || ')';

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => (case when r_indic_rem_loja.cod_indic = 28 then 16 end)
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de valor

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial SAX - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial SAX - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial SAX - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - prc_calc_ating_fil_SAX
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial SAX - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --19 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

-- ini Alt201402 - Inclusao de trava para este indicadore
               -- verificar o realizado x meta na escala (guardar na variavel v_num_realz_fx)
               -- para calcular o vr do premio calculado para a filial para rateio entre os cargos operacionais
               -- verificar se existe uma escala cadastrada para o indicador,
               -- se nao houver procurar para o grupo de indic
               -- se nao houver procurar para o grupo de rem var
               --
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic      -- p_cod_indic
                                  ,r_indic_rem_loja.cod_grp_indic  -- p_cod_grp_indic
                                  ,null                            -- p_cod_grp_rem_var
                                  ,v_num_realz_x_meta
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

/*                  v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                          ,p_subject => 'SRV Calculo Atingimento Filial - Escala nao encontrada: '
                                                                          ,p_body    => v_body
                                                                          );*/

                  -- logar tabela
                  v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                            ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                            ,p_num_ano     => p_num_ano
                                                            ,p_num_mes     => p_num_mes
                                                            ,p_cod_fil     => v_cod_fil
                                                            ,p_cod_func    => null
                                                            ,p_cod_indic   => v_cod_indic
                                                            ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                            );

--                  raise e_erro;
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

               -- vlr do premio calculado para a filial, conforme o atingimento da meta
               v_vlr_premio_fil_calc         := (v_vlr_premio_fil * v_num_realz_fx) / 100;

-- fim Alt201402

            --------------------------------------------------------------------------------------
            -- SE INDICADOR SEGURO EMPRESTIMO PESSOAL (EP) SAX / PSF
            --------------------------------------------------------------------------------------
            elsif r_indic_rem_loja.descr_indic = 'SEGURO EMPRESTIMO PESSOAL (EP) SAX / PSF'  then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               -- nao tem meta, entao o realizado X meta sera
               -- o realizado do seguro em quantidade (nao pegar valor) dividido pelo realizado
               -- em quantidade (nao pegar valor) do emprestimo
               v_var_sql :=              ' select nvl(quant_seguro_real,0) quant_seguro_real' || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(quant_ep_real,0) quant_ep_real    ' || chr(13)  || chr(10);
               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||''                  || chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '                                 || r_fil.cod_fil || chr(13) || chr(10);

               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => (case when r_indic_rem_loja.cod_indic = 29 then 17 end)
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
--                  v_cod_un_meta      := 1; -- unidade de valor

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial EMPRESTIMO SAX - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial EMPRESTIMO SAX - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial EMPRESTIMO SAX - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - prc_calc_ating_fil_SAX
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial SAX - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;


               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
--               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               v_meta                := nvl(v_meta, 0);
               --
               v_cod_un_meta   := 3; -- unidade de UNIDADE
               v_cod_un_realz     := 3; -- unidade de UNIDADE

               --20 Alt201401
               if v_meta = 0 and v_qtd_realizado > 0 then
                  v_meta := v_qtd_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
               elsif v_qtd_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

               -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
               v_vlr_realizado := v_qtd_realizado;

-- ini Alt201402 - Inclusao de trava para este indicador
               -- verificar o realizado x meta na escala (guardar na variavel v_num_realz_fx)
               -- para calcular o vr do premio calculado para a filial para rateio entre os cargos operacionais
               -- verificar se existe uma escala cadastrada para o indicador,
               -- se nao houver procurar para o grupo de indic
               -- se nao houver procurar para o grupo de rem var
               --
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic      -- p_cod_indic
                                  ,r_indic_rem_loja.cod_grp_indic  -- p_cod_grp_indic
                                  ,null                            -- p_cod_grp_rem_var
                                  ,v_num_realz_x_meta
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

/*                  v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                          ,p_subject => 'SRV Calculo Atingimento Filial - Escala nao encontrada: '
                                                                          ,p_body    => v_body
                                                                          );*/

                  -- logar tabela
                  v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                            ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                            ,p_num_ano     => p_num_ano
                                                            ,p_num_mes     => p_num_mes
                                                            ,p_cod_fil     => v_cod_fil
                                                            ,p_cod_func    => null
                                                            ,p_cod_indic   => v_cod_indic
                                                            ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                            );

--                  raise e_erro;
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

               -- vlr do premio calculado para a filial, conforme o atingimento da meta
               v_vlr_premio_fil_calc         := (v_vlr_premio_fil * v_num_realz_fx) / 100;

-- fim Alt201402

            -- if r_indic_rem_loja.descr_indic = ...
            --------------------------------------------------------------------------------------
            -- SE INDICACAO EMPRESTIMO PESSOAL (EP) SAX / PSF
            --------------------------------------------------------------------------------------

            elsif r_indic_rem_loja.descr_indic = 'INDICACAO EMPRESTIMO PESSOAL (EP) SAX / PSF'  then

               --
               v_nm_tab_realz_fu := 'SRV_REALZFU_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               v_nm_tab_realz_fi := 'SRV_REALZFI_EMP_SEG_SAX_'
                                    ||trim(to_char(p_num_ano, '0000'))
                                    ||trim(to_char(p_num_mes, '00'));
               --
               v_qtd_realizado                  := 0;
               v_vlr_realizado                  := 0;
               --
               v_cod_un_realz                   := null;
               v_meta                           := 0;
               v_cod_un_meta                    := null;
               v_vlr_premio_fil                 := 0;
               v_num_realz_x_meta               := 0;
               v_cod_un_realz_x_meta            := null;
               v_vlr_premio_fil_calc            := 0;
               v_cod_escala                     := null;
               v_num_seq_escala_fx              := null;
               v_num_realz_fx                   := 0;
               v_cod_un_realz_fx                := null;
               v_flg_pct_100                    := 0;
               v_num_limite_fx                  := 0;

               --
               v_var_sql :=              ' select nvl(quant_ep_real,0) quant_ep_real '            || chr(13) || chr(10);
               v_var_sql := v_var_sql || '       ,nvl(vlr_liquido_emprest,0) vlr_liquido_emprest '|| chr(13) || chr(10);               v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi ||' a '|| chr(13) || chr(10);
               v_var_sql := v_var_sql || '  where fil_cod = '|| r_fil.cod_fil || chr(13) || chr(10);


               open cur_realz for v_var_sql;
               loop
                  fetch cur_realz
                   into v_qtd_realizado
                       ,v_vlr_realizado;
                  --
                  exit when cur_realz%notfound;
               -- cur_venda
               end loop;
               --
               close cur_realz;
               --
               v_qtd_realizado       := nvl(v_qtd_realizado, 0);
               v_vlr_realizado       := nvl(v_vlr_realizado, 0);
               --
               v_cod_un_realz           := 1; -- unidade de VALOR

               -- calcular meta da filial para o indicador
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Meta_Fil (p_cod_indic        => (case when r_indic_rem_loja.cod_indic = 30 then 16 end)
                                 ,p_cod_emp          => r_fil.cod_emp
                                 ,p_cod_fil          => r_fil.cod_fil
                                 ,p_ano_meta         => p_num_ano
                                 ,p_mes_meta         => p_num_mes
                                 ,p_meta             => v_meta
                                 ,p_cod_un_meta      => v_cod_un_meta
                                 ,p_vlr_premio_fil   => v_vlr_premio_fil
                                 ,p_pct_calc_meta    => v_pct_calc_meta
                                 ,p_cod_erro         => p_cod_erro
                                 ,p_descr_erro       => p_descr_erro
                                 );

               -- se nao exitir a meta, assumir o valor de realizado como meta
               if p_cod_erro is not null then
                  v_meta             := v_vlr_realizado;
                  v_cod_un_meta      := 1; -- unidade de valor

                  -- se nao encontrou meta para a filial => enviar email
                  if p_cod_erro in (1,2) then
                     --
                     if p_cod_erro = 1 then -- no_data_found
                        v_body       := 'SRV Calculo Atingimento Filial SAX - A Meta nao foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     elsif p_cod_erro = 2 then -- too_many_rows
                        v_body       := 'SRV Calculo Atingimento Filial SAX - Mais de uma Meta foi encontrada para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     else
                        v_body       := 'SRV Calculo Atingimento Filial SAX - Erro ao selecionar Meta para a filial: '||
                                        r_fil.cod_fil || ' , para o indicador: '||
                                        r_indic_rem_loja.descr_indic || ' , e esta sendo considerado o Vlr do realizado: ' ||
                                        to_char(v_vlr_realizado,'99G999D99') || ' como meta.';
                     end if;
                     --
/*                     v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - prc_calc_ating_fil_SAX
                                                                             ,p_subject => 'SRV Calculo Atingimento Filial SAX - Meta nao encontrada filial: '||r_fil.cod_fil
                                                                             ,p_body    => v_body
                                                                             );*/
                  -- if p_cod_erro is not null
                  end if;

               -- se retornou erro ao selecionar calcular a meta
               end if;

               --21 Alt201401
               if v_meta = 0 and v_vlr_realizado > 0 then
                  v_meta := v_vlr_realizado;
               end if;

               if v_meta > 0 then
                  v_num_realz_x_meta      := (v_vlr_realizado / v_meta) * 100;
               elsif v_vlr_realizado = 0 then
                  v_num_realz_x_meta       := 0;
               elsif r_fil.flg_meta_100_pct_realz = 'S' then
                  v_num_realz_x_meta       := 100;
               else
                  v_num_realz_x_meta       := 0;
               end if;
               v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

-- ini Alt201402 - Inclusao de trava para este indicador
               -- verificar o realizado x meta na escala (guardar na variavel v_num_realz_fx)
               -- para calcular o vr do premio calculado para a filial para rateio entre os cargos operacionais
               -- verificar se existe uma escala cadastrada para o indicador,
               -- se nao houver procurar para o grupo de indic
               -- se nao houver procurar para o grupo de rem var
               --
               p_cod_erro         := null;
               p_descr_erro       := null;
               --
               PKG_SRV_GERAL.Prc_Calc_Escala_Fx (r_indic_rem_loja.cod_indic      -- p_cod_indic
                                  ,r_indic_rem_loja.cod_grp_indic  -- p_cod_grp_indic
                                  ,null                            -- p_cod_grp_rem_var
                                  ,v_num_realz_x_meta
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

/*                  v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 72 -- SRV - PRC_CALC_ATING_FIL_LJ
                                                                          ,p_subject => 'SRV Calculo Atingimento Filial - Escala nao encontrada: '
                                                                          ,p_body    => v_body
                                                                          );*/

                  -- logar tabela
                  v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                            ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                            ,p_num_ano     => p_num_ano
                                                            ,p_num_mes     => p_num_mes
                                                            ,p_cod_fil     => v_cod_fil
                                                            ,p_cod_func    => null
                                                            ,p_cod_indic   => v_cod_indic
                                                            ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                            );

--                  raise e_erro;
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

               -- vlr do premio calculado para a filial, conforme o atingimento da meta
               v_vlr_premio_fil_calc         := (v_vlr_premio_fil * v_num_realz_fx) / 100;
-- fim Alt201402
            -- if r_indic_rem_loja.descr_indic = ...
            end if;

            --
            rec_srv_realizado_filial.cod_indic               := r_indic_rem_loja.cod_indic;
            rec_srv_realizado_filial.cod_emp                 := r_fil.cod_emp;
            rec_srv_realizado_filial.cod_fil                 := r_fil.cod_fil;
            rec_srv_realizado_filial.num_ano                 := p_num_ano;
            rec_srv_realizado_filial.num_mes                 := p_num_mes;
            rec_srv_realizado_filial.num_meta                := v_meta;
            rec_srv_realizado_filial.cod_un_meta             := v_cod_un_meta;
            rec_srv_realizado_filial.num_realz               := v_vlr_realizado;
            rec_srv_realizado_filial.cod_un_realz            := v_cod_un_realz;
            rec_srv_realizado_filial.qtd_realz               := v_qtd_realizado;
            rec_srv_realizado_filial.num_realz_x_meta        := v_num_realz_x_meta;
            rec_srv_realizado_filial.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;
            rec_srv_realizado_filial.vlr_premio_fil_calc     := null;
            rec_srv_realizado_filial.cod_un_premio_fil_calc  := null;
            rec_srv_realizado_filial.dt_ini_sit_srv          := v_data;
            rec_srv_realizado_filial.cod_usuario             := p_cod_usuario;
            rec_srv_realizado_filial.cod_escala              := v_cod_escala;
            rec_srv_realizado_filial.num_seq_escala_fx       := v_num_seq_escala_fx;
            rec_srv_realizado_filial.num_realz_fx            := v_num_realz_fx;
            rec_srv_realizado_filial.cod_un_realz_fx         := v_cod_un_realz_fx;


            -- insere o realizado da filial para o indicador
            prc_insere_realz_fil (p_rec_srv_realz_fil  => rec_srv_realizado_filial
                                 ,p_cod_erro           => p_cod_erro
                                 ,p_descr_erro         => p_descr_erro
                                  );
            --
            if p_cod_erro is not null then

               -- enviar email
               v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*               v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                       ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro ao inserir tabela realizado filial: '
                                                                       ,p_body    => v_body
                                                                       );*/

               -- logar tabela
               v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                         ,p_nome_proc   => 'prc_calc_ating_fil_SAX'
                                                         ,p_num_ano     => p_num_ano
                                                         ,p_num_mes     => p_num_mes
                                                         ,p_cod_fil     => v_cod_fil
                                                         ,p_cod_func    => null
                                                         ,p_cod_indic   => v_cod_indic
                                                         ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                         );

--               raise e_erro;
            -- if p_cod_erro is not null
            end if;



            -- commit por indicador
            commit;

         -- c_indic_rem_loja
         end loop;

         -- commit por filial
         commit;

      -- c_fil
      end loop;

      --
      commit;


   exception

      when e_erro_cria_tab then

         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*         v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                 ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro ao criar tabelas de apuracao: '
                                                                 ,p_body    => v_body
                                                                 );*/

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                   ,p_nome_proc   => 'prc_calc_ating_fil_SAX'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => null
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
/*            v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                    ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );*/
         --v_ins_log_erro is not null
         end if;


         -- retorna
         return;


      when e_erro then
         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*         v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                 ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro exception e_erro: '
                                                                 ,p_body    => v_body
                                                                 );*/

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                   ,p_nome_proc   => 'prc_calc_ating_fil_SAX'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => null
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
/*            v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                    ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );*/
         --v_ins_log_erro is not null
         end if;


         -- retorna
         return;
      --
      when others then
         p_cod_erro     := 1;
         p_descr_erro   := 'Erro geral ao calcular Atingimento Filiais Remun Loja: ' ||
                           'cod_fil: '           || v_cod_fil             || ' - ' ||
                           'cod_tipo_rem_var: '  || v_cod_tipo_rem_var    || ' - ' ||
                           'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                           'cod_indic: '         || v_cod_indic           || ' - ' || sqlerrm;


         -- enviar email
         v_body       := p_cod_erro || ' - ' || p_descr_erro;

/*         v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                 ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro geral exception: '
                                                                 ,p_body    => v_body
                                                                 );*/

         -- logar tabela
         v_ins_log_erro := PKG_SRV_GERAL.Fnc_Insere_Log_Processo (p_cod_proc    => 7
                                                   ,p_nome_proc   => 'prc_calc_ating_fil_SAX'
                                                   ,p_num_ano     => p_num_ano
                                                   ,p_num_mes     => p_num_mes
                                                   ,p_cod_fil     => v_cod_fil
                                                   ,p_cod_func    => null
                                                   ,p_cod_indic   => v_cod_indic
                                                   ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                   );

         if v_ins_log_erro is not null then
            -- enviar email
            v_body       := v_ins_log_erro;
            --
/*            v_send_email := PKG_CCM_FUNC_PROC_GERAL.Fnc_Enviar_Email(p_rot_cod => 73 -- SRV - prc_calc_ating_fil_SAX
                                                                    ,p_subject => 'SRV Calculo Atingimento Filial SAX - Erro ao inserir log de erro: '
                                                                    ,p_body    => v_body
                                                                    );
*/         --v_ins_log_erro is not null
         end if;

   end prc_calc_ating_fil_SAX;


procedure prc_calc_ating_CCenter (p_num_ano     in number,
                                  p_num_mes     in number,
                                  p_cod_emp     in number default null,
                                  p_cod_fil     in number default null,
                                  p_cod_indic   in number default null,
--                                  p_descr_indic in varchar2 default null,
                                  p_cod_usuario in number,
                                  p_cod_erro    out number,
                                  p_descr_erro  out varchar2) is

     -- REMUNERACAO LOJAS
     cursor c_indic_rem_loja (p_descr_grp_indic srv_grupo_indicador.descr_grp_indic%type) is
       select c.cod_indic
            , c.descr_indic
         from srv_tipo_rem_var          a
        inner join srv_grupo_indicador  b on b.cod_tipo_rem_var = a.cod_tipo_rem_var
        inner join srv_indicador        c on c.cod_grp_indic    = b.cod_grp_indic
                                         and upper(c.flg_indic_ativ) = 'S'
        where b.descr_grp_indic = p_descr_grp_indic;

     -- variaveis controle
     v_var_sql               varchar2(2000);
     v_nm_tab_realz_fu       varchar2(30);
     v_nm_tab_realz_fi       varchar2(30);
--     v_nm_tab_realz_fi_lr_vs varchar2(30);
     --v_nm_tab_meta           varchar2(30);
     --v_nm_tab_realz_fi_pc    varchar2(30);
     --v_nm_tab_realz_fi_bp    varchar2(30);
     --v_nm_tab_realz_fi_odo   varchar2(30);
     --v_nm_tab_realz_fi_mm    varchar2(30);

     -- variaveis calculo
     --v_vlr_vdas_bruta      number(11, 2);
     --v_vlr_devolucao       number(11, 2);
     v_vlr_realizado       srv_realizado_filial.num_realz%type;
     v_qtd_realizado       number(11, 2); --srv_realizado_filial.qtd_realz%type;
     v_cod_un_realz        srv_realizado_filial.cod_un_realz%type;
     v_meta                srv_meta_filial.num_meta%type;
     v_cod_un_meta         srv_meta_filial.cod_un_meta%type;
     v_vlr_premio_fil      srv_meta_filial.vlr_premio_fil%type;
     v_pct_calc_meta       srv_meta_filial.pct_calc_meta%type;
     v_num_realz_x_meta    srv_realizado_filial.num_realz_x_meta%type;
     v_cod_un_realz_x_meta srv_realizado_filial.cod_un_realz_x_meta%type;
     v_vlr_premio_fil_calc srv_realizado_filial.vlr_premio_fil_calc%type := 0;
     v_cod_escala          srv_escala_faixa.cod_escala%type;
     v_num_seq_escala_fx   srv_escala_faixa.num_seq_escala_fx%type;
     v_num_realz_fx        srv_escala_faixa.num_realz_fx%type;
     v_cod_un_realz_fx     srv_escala_faixa.cod_un_realz_fx%type;
     v_flg_pct_100         srv_escala_faixa.flg_pct_100%type;
     v_num_limite_fx       srv_escala_faixa.num_limite_fx%type;
     --v_vr_vendas_tot       srv_meta_filial.num_meta%type;
     --v_qtde_vendas_parc    srv_meta_filial.num_meta%type;

     -- var log
     v_cod_fil                srv_filial.cod_fil%type;
     v_cod_emp                srv_filial.cod_emp%type;
     v_flg_meta_100_pct_realz srv_filial.flg_meta_100_pct_realz%type;
     v_cod_tipo_rem_var       srv_tipo_rem_var.cod_tipo_rem_var%type;
     v_cod_grp_indic          srv_grupo_indicador.cod_grp_indic%type;
     v_cod_indic              srv_indicador.cod_indic%type;

     -- cursores
     cur_realz pkg_srv_calc_rem_var.typ_cursor;

     --
     rec_srv_realizado_filial srv_realizado_filial%rowtype;

     -- exception
     e_erro_cria_tab exception;

   -------------
   --Calc TLMKT
   -------------
   begin

     v_data := sysdate;
     --
     v_dt_ini := '01/' || trim(to_char(p_num_mes, '00')) || '/' || trim(to_char(p_num_ano, '0000')) || ' ' || '00:00:00';
     --
     select to_char(last_day(to_date(('01/' || to_char(p_num_mes, '00') || '/' || to_char(p_num_ano, '0000')),'dd/mm/yyyy')),'dd/mm/yyyy') || ' ' || '23:59:59'
       into v_dt_fim
       from dual;

     v_cod_fil                := 900;
     v_cod_emp                := 1;
     v_flg_meta_100_pct_realz := 'S';

     -----------------------------------------------------------------------
     -- SELECIONA INDICADORES PARA O GRUPO DE REMUNERACAO LOJAS
     -----------------------------------------------------------------------
     for r_indic_rem_loja in c_indic_rem_loja (p_descr_grp_indic => 'TLMKT') loop

       v_cod_indic := r_indic_rem_loja.cod_indic;

       --TLMKT CARTAO ITAU APROVADO
       if r_indic_rem_loja.descr_indic = 'TLMKT CARTAO ITAU APROVADO' then
          --
          v_nm_tab_realz_fu := 'SRV_REALZFU_TLMKT_APROV_'
                               ||trim(to_char(p_num_ano, '0000'))
                               ||trim(to_char(p_num_mes, '00'));
          v_nm_tab_realz_fi := 'SRV_REALZFI_TLMKT_APROV_'
                               ||trim(to_char(p_num_ano, '0000'))
                               ||trim(to_char(p_num_mes, '00'));
          --
          v_qtd_realizado                  := 0;
          v_vlr_realizado                  := 0;
          --
          v_cod_un_realz                   := null;
          v_meta                           := 0;
          v_cod_un_meta                    := null;
          v_vlr_premio_fil                 := 0;
          v_num_realz_x_meta               := 0;
          v_cod_un_realz_x_meta            := null;
          v_vlr_premio_fil_calc            := 0;
          v_cod_escala                     := null;
          v_num_seq_escala_fx              := null;
          v_num_realz_fx                   := 0;
          v_cod_un_realz_fx                := null;
          v_flg_pct_100                    := 0;
          v_num_limite_fx                  := 0;

          --
          v_var_sql :=              ' select nvl(qtde,0) qtde '         || chr(13) || chr(10);
          v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi      || chr(13) || chr(10);
          v_var_sql := v_var_sql || '  where cod_fil = ' || v_cod_fil   || chr(13) || chr(10);

          open cur_realz for v_var_sql;
          loop
             fetch cur_realz
              into v_qtd_realizado;
             --
             exit when cur_realz%notfound;
          -- cur_venda
          end loop;
          --
          close cur_realz;
          --
          v_qtd_realizado       := nvl(v_qtd_realizado, 0);
          --v_vlr_realizado       := nvl(v_vlr_realizado, 0);
          --
          v_cod_un_realz           := 3; -- unidade de UNIDADE

          -- calcular meta da filial para o indicador
          p_cod_erro         := null;
          p_descr_erro       := null;
          --
          Pkg_Srv_Geral.prc_calc_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                          ,p_cod_emp          => v_cod_emp
                                          ,p_cod_fil          => v_cod_fil
                                          ,p_ano_meta         => p_num_ano
                                          ,p_mes_meta         => p_num_mes
                                          ,p_meta             => v_meta
                                          ,p_cod_un_meta      => v_cod_un_meta
                                          ,p_vlr_premio_fil   => v_vlr_premio_fil
                                          ,p_pct_calc_meta    => v_pct_calc_meta
                                          ,p_cod_erro         => p_cod_erro
                                          ,p_descr_erro       => p_descr_erro
                                          );

          -- se nao exitir a meta, assumir o valor de realizado como meta
          if p_cod_erro is not null then
             v_meta          := v_qtd_realizado;
             v_cod_un_meta   := 3; -- unidade de UNIDADE
          end if;

          --3 Alt201401
          if v_meta = 0 and v_qtd_realizado > 0 then
             v_meta := v_qtd_realizado;
          end if;

          if v_meta > 0 then
             v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
          elsif v_qtd_realizado = 0 then
             v_num_realz_x_meta       := 0;
          elsif v_flg_meta_100_pct_realz = 'S' then
             v_num_realz_x_meta       := 100;
          else
             v_num_realz_x_meta       := 0;
          end if;
          v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

          -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
          v_vlr_realizado := v_qtd_realizado;

       end if;

       --TLMKT CARTAO ITAU ATIVADO
       if r_indic_rem_loja.descr_indic = 'TLMKT CARTAO ITAU ATIVADO' then
          --
          v_nm_tab_realz_fu := 'SRV_REALZFU_TLMKT_ATIV_'
                               ||trim(to_char(p_num_ano, '0000'))
                               ||trim(to_char(p_num_mes, '00'));
          v_nm_tab_realz_fi := 'SRV_REALZFI_TLMKT_ATIV_'
                               ||trim(to_char(p_num_ano, '0000'))
                               ||trim(to_char(p_num_mes, '00'));
          --
          v_qtd_realizado                  := 0;
          v_vlr_realizado                  := 0;
          --
          v_cod_un_realz                   := null;
          v_meta                           := 0;
          v_cod_un_meta                    := null;
          v_vlr_premio_fil                 := 0;
          v_num_realz_x_meta               := 0;
          v_cod_un_realz_x_meta            := null;
          v_vlr_premio_fil_calc            := 0;
          v_cod_escala                     := null;
          v_num_seq_escala_fx              := null;
          v_num_realz_fx                   := 0;
          v_cod_un_realz_fx                := null;
          v_flg_pct_100                    := 0;
          v_num_limite_fx                  := 0;

          --
          v_var_sql :=              ' select nvl(qtde,0) qtde '           || chr(13) || chr(10);
          v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi        || chr(13) || chr(10);
          v_var_sql := v_var_sql || '  where cod_fil = ' || v_cod_fil     || chr(13) || chr(10);

          open cur_realz for v_var_sql;
          loop
             fetch cur_realz
              into v_qtd_realizado;
             --
             exit when cur_realz%notfound;
          -- cur_venda
          end loop;
          --
          close cur_realz;
          --
          v_qtd_realizado       := nvl(v_qtd_realizado, 0);
          --v_vlr_realizado       := nvl(v_vlr_realizado, 0);
          --
          v_cod_un_realz           := 3; -- unidade de UNIDADE

          -- calcular meta da filial para o indicador
          p_cod_erro         := null;
          p_descr_erro       := null;
          --
          Pkg_Srv_Geral.prc_calc_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                          ,p_cod_emp          => v_cod_emp
                                          ,p_cod_fil          => v_cod_fil
                                          ,p_ano_meta         => p_num_ano
                                          ,p_mes_meta         => p_num_mes
                                          ,p_meta             => v_meta
                                          ,p_cod_un_meta      => v_cod_un_meta
                                          ,p_vlr_premio_fil   => v_vlr_premio_fil
                                          ,p_pct_calc_meta    => v_pct_calc_meta
                                          ,p_cod_erro         => p_cod_erro
                                          ,p_descr_erro       => p_descr_erro
                                          );

          -- se nao exitir a meta, assumir o valor de realizado como meta
          if p_cod_erro is not null then
             v_meta             := v_qtd_realizado;
             v_cod_un_meta      := 3; -- unidade de UNIDADE
          end if;

          --3 Alt201401
          if v_meta = 0 and v_qtd_realizado > 0 then
             v_meta := v_qtd_realizado;
          end if;

          if v_meta > 0 then
             v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
          elsif v_qtd_realizado = 0 then
             v_num_realz_x_meta       := 0;
          elsif v_flg_meta_100_pct_realz = 'S' then
             v_num_realz_x_meta       := 100;
          else
             v_num_realz_x_meta       := 0;
          end if;
          v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

          -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
          v_vlr_realizado := v_qtd_realizado;

       end if;

       --TLMKT CARTAO ITAU ADICIONAL
       if r_indic_rem_loja.descr_indic = 'TLMKT CARTAO ITAU ADICIONAL' then
          --
          v_nm_tab_realz_fu := 'SRV_REALZFU_TLMKT_ADIC_'
                               ||trim(to_char(p_num_ano, '0000'))
                               ||trim(to_char(p_num_mes, '00'));
          v_nm_tab_realz_fi := 'SRV_REALZFI_TLMKT_ADIC_'
                               ||trim(to_char(p_num_ano, '0000'))
                               ||trim(to_char(p_num_mes, '00'));
          --
          v_qtd_realizado                  := 0;
          v_vlr_realizado                  := 0;
          --
          v_cod_un_realz                   := null;
          v_meta                           := 0;
          v_cod_un_meta                    := null;
          v_vlr_premio_fil                 := 0;
          v_num_realz_x_meta               := 0;
          v_cod_un_realz_x_meta            := null;
          v_vlr_premio_fil_calc            := 0;
          v_cod_escala                     := null;
          v_num_seq_escala_fx              := null;
          v_num_realz_fx                   := 0;
          v_cod_un_realz_fx                := null;
          v_flg_pct_100                    := 0;
          v_num_limite_fx                  := 0;

          --
          v_var_sql :=              ' select nvl(qtde,0) qtde '        || chr(13) || chr(10);
          v_var_sql := v_var_sql || '   from '|| v_nm_tab_realz_fi     || chr(13) || chr(10);
          v_var_sql := v_var_sql || '  where cod_fil = ' || v_cod_fil  || chr(13) || chr(10);

          open cur_realz for v_var_sql;
          loop
             fetch cur_realz
              into v_qtd_realizado;
             --
             exit when cur_realz%notfound;
          -- cur_venda
          end loop;
          --
          close cur_realz;
          --
          v_qtd_realizado       := nvl(v_qtd_realizado, 0);
          --v_vlr_realizado       := nvl(v_vlr_realizado, 0);
          --
          v_cod_un_realz           := 3; -- unidade de UNIDADE

          -- calcular meta da filial para o indicador
          p_cod_erro         := null;
          p_descr_erro       := null;
          --
          Pkg_Srv_Geral.prc_calc_meta_fil (p_cod_indic        => r_indic_rem_loja.cod_indic
                                          ,p_cod_emp          => v_cod_emp
                                          ,p_cod_fil          => v_cod_fil
                                          ,p_ano_meta         => p_num_ano
                                          ,p_mes_meta         => p_num_mes
                                          ,p_meta             => v_meta
                                          ,p_cod_un_meta      => v_cod_un_meta
                                          ,p_vlr_premio_fil   => v_vlr_premio_fil
                                          ,p_pct_calc_meta    => v_pct_calc_meta
                                          ,p_cod_erro         => p_cod_erro
                                          ,p_descr_erro       => p_descr_erro
                                          );

          -- se nao exitir a meta, assumir o valor de realizado como meta
          if p_cod_erro is not null then
             v_meta             := v_qtd_realizado;
             v_cod_un_meta      := 3; -- unidade de UNIDADE
          end if;

          --3 Alt201401
          if v_meta = 0 and v_qtd_realizado > 0 then
             v_meta := v_qtd_realizado;
          end if;

          if v_meta > 0 then
             v_num_realz_x_meta      := (v_qtd_realizado / v_meta) * 100;
          elsif v_qtd_realizado = 0 then
             v_num_realz_x_meta       := 0;
          elsif v_flg_meta_100_pct_realz = 'S' then
             v_num_realz_x_meta       := 100;
          else
             v_num_realz_x_meta       := 0;
          end if;
          v_cod_un_realz_x_meta      := 2; -- unidade de PERCENTUAL

          -- para salvar no num_realz da realizado filial salvar o realizado correto (Vlr ou qtde)
          v_vlr_realizado := v_qtd_realizado;

       end if;
       --
       rec_srv_realizado_filial.cod_indic               := r_indic_rem_loja.cod_indic;
       rec_srv_realizado_filial.cod_emp                 := v_cod_emp;
       rec_srv_realizado_filial.cod_fil                 := v_cod_fil;
       rec_srv_realizado_filial.num_ano                 := p_num_ano;
       rec_srv_realizado_filial.num_mes                 := p_num_mes;
       rec_srv_realizado_filial.num_meta                := v_meta;
       rec_srv_realizado_filial.cod_un_meta             := v_cod_un_meta;
       rec_srv_realizado_filial.num_realz               := v_vlr_realizado;
       rec_srv_realizado_filial.cod_un_realz            := v_cod_un_realz;
       rec_srv_realizado_filial.qtd_realz               := v_qtd_realizado;
       rec_srv_realizado_filial.num_realz_x_meta        := v_num_realz_x_meta;
       rec_srv_realizado_filial.cod_un_realz_x_meta     := v_cod_un_realz_x_meta;
       rec_srv_realizado_filial.vlr_premio_fil_calc     := null;
       rec_srv_realizado_filial.cod_un_premio_fil_calc  := null;
       rec_srv_realizado_filial.dt_ini_sit_srv          := v_data;
       rec_srv_realizado_filial.cod_usuario             := p_cod_usuario;
       rec_srv_realizado_filial.cod_escala              := v_cod_escala;
       rec_srv_realizado_filial.num_seq_escala_fx       := v_num_seq_escala_fx;
       rec_srv_realizado_filial.num_realz_fx            := v_num_realz_fx;
       rec_srv_realizado_filial.cod_un_realz_fx         := v_cod_un_realz_fx;


       -- insere o realizado da filial para o indicador
       prc_insere_realz_fil (p_rec_srv_realz_fil  => rec_srv_realizado_filial
                            ,p_cod_erro           => p_cod_erro
                            ,p_descr_erro         => p_descr_erro
                             );
       --
       if p_cod_erro is not null then
          -- logar tabela
          v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo (p_cod_proc    => 7
                                                                  ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                                  ,p_num_ano     => p_num_ano
                                                                  ,p_num_mes     => p_num_mes
                                                                  ,p_cod_fil     => v_cod_fil
                                                                  ,p_cod_func    => null
                                                                  ,p_cod_indic   => v_cod_indic
                                                                  ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                                  );

       -- if p_cod_erro is not null
       end if;

       -- commit por indicador
       commit;

     -- c_indic_rem_loja
     end loop;

     --
     commit;
     --
   exception
     when others then
       p_cod_erro := 1;
       p_descr_erro   := 'Erro geral ao calcular Atingimento Filiais Remun Loja: ' ||
                         'cod_fil: '           || v_cod_fil             || ' - ' ||
                         'cod_tipo_rem_var: '  || v_cod_tipo_rem_var    || ' - ' ||
                         'cod_grp_indic: '     || v_cod_grp_indic       || ' - ' ||
                         'cod_indic: '         || v_cod_indic           || ' - ' || sqlerrm;


     -- logar tabela
     v_ins_log_erro := Pkg_Srv_Geral.fnc_insere_log_processo (p_cod_proc    => 7
                                                             ,p_nome_proc   => 'PRC_CALC_ATING_FIL_LJ'
                                                             ,p_num_ano     => p_num_ano
                                                             ,p_num_mes     => p_num_mes
                                                             ,p_cod_fil     => v_cod_fil
                                                             ,p_cod_func    => null
                                                             ,p_cod_indic   => v_cod_indic
                                                             ,p_erro        => p_cod_erro || ' - ' || p_descr_erro
                                                             );


   end prc_calc_ating_CCenter;

end pkg_srv_atingimento;
/
