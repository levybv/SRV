create or replace package body srv.pkg_srv_geral is

  /*-------------------------------------------------------------------------------------------------
  -- Author  : LEVY VILLAR
  -- Created : 11/04/2017
  -- Purpose : Package de operacoes padroes do SRV
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/


  -----------------------------------------------------------------------------------------------
  -- INSERE LOG PROCESSO
  -----------------------------------------------------------------------------------------------
  function Fnc_Insere_Log_Processo(p_cod_proc  in number,
                                   p_nome_proc in varchar2,
                                   p_num_ano   in number,
                                   p_num_mes   in number,
                                   p_cod_fil   in number,
                                   p_cod_func  in number,
                                   p_cod_indic in number,
                                   p_erro      in varchar2) return varchar2 is
  
    --
    v_sqlerrm_log_erro varchar2(2000);
  
  begin
    --
    insert into srv_processo_log_erro
      (cod_proc,
       nome_proc,
       dt_log,
       num_ano,
       num_mes,
       cod_fil,
       cod_func,
       cod_indic,
       erro)
    values
      (p_cod_proc,
       p_nome_proc,
       sysdate,
       p_num_ano,
       p_num_mes,
       p_cod_fil,
       p_cod_func,
       p_cod_indic,
       substr(p_erro, 1, 4000));
  
    --
    commit;
  
    --
    return(v_sqlerrm_log_erro);
  
  exception
    when others then
      v_sqlerrm_log_erro := sqlerrm;
      return(v_sqlerrm_log_erro);
    
  end Fnc_Insere_Log_Processo;

  -----------------------------------------------------------------------------------------------
  -- CALCULA PONDERACAO
  -----------------------------------------------------------------------------------------------
  procedure Prc_Calc_Ponderacao(p_cod_indic       in number,
                                p_cod_grp_indic   in number,
                                p_cod_cargo       in number,
                                p_cod_grp_rem_var in number,
                                p_cod_pond        out number,
                                p_num_peso        out number,
                                p_cod_un_peso     out number,
                                p_vlr_premio      out number,
                                p_cod_erro        out number,
                                p_descr_erro      out varchar2) is
  begin
    begin
      -- selecionar a ponderacao para o cargo e para o indicador
      select p.cod_pond, p.num_peso, p.cod_un_peso, p.vlr_premio
        into p_cod_pond, p_num_peso, p_cod_un_peso, p_vlr_premio
        from srv_ponderacao p
       where (p.cod_grp_rem_var = p_cod_grp_rem_var or
             p_cod_grp_rem_var is null)
         and (p.cod_cargo = p_cod_cargo or p_cod_cargo is null)
         and (p.cod_grp_indic = p_cod_grp_indic or p_cod_grp_indic is null)
         and (p.cod_indic = p_cod_indic or p_cod_indic is null);
    
    exception
      when no_data_found then
        p_cod_erro   := 1;
        p_descr_erro := 'Erro na proc prc_calc_ponderacao - ponderacao nao encontrada : ' ||
                        'cargo - ' || p_cod_cargo || ', ' ||
                        'grp rem var - ' || p_cod_grp_rem_var || ', ' ||
                        'grp indicador - ' || p_cod_grp_indic || ', ' ||
                        'indicador - ' || p_cod_indic || ', ' || 'erro - ' ||
                        sqlerrm;
        --
        p_cod_pond    := null;
        p_num_peso    := 0;
        p_cod_un_peso := null;
        p_vlr_premio  := 0;
      
      when too_many_rows then
        p_cod_erro   := 1;
        p_descr_erro := 'Erro na proc prc_calc_ponderacao - ponderacao - mais de uma linha retornada : ' ||
                        'cargo - ' || p_cod_cargo || ', ' ||
                        'grp rem var - ' || p_cod_grp_rem_var || ', ' ||
                        'grp indicador - ' || p_cod_grp_indic || ', ' ||
                        'indicador - ' || p_cod_indic || ', ' || 'erro - ' ||
                        sqlerrm;
        --
        p_cod_pond    := null;
        p_num_peso    := 0;
        p_cod_un_peso := null;
        p_vlr_premio  := 0;
    end;
  
  exception
    when others then
      p_cod_erro   := 1;
      p_descr_erro := 'Erro na proc prc_calc_ponderacao - ponderacao - erro geral : ' ||
                      'cargo - ' || p_cod_cargo || ', ' || 'grp rem var - ' ||
                      p_cod_grp_rem_var || ', ' || 'grp indicador - ' ||
                      p_cod_grp_indic || ', ' || 'indicador - ' ||
                      p_cod_indic || ', ' || 'erro - ' || sqlerrm;
      --
      p_cod_pond    := null;
      p_num_peso    := 0;
      p_cod_un_peso := null;
      p_vlr_premio  := 0;
    
  end Prc_Calc_Ponderacao;

  -----------------------------------------------------------------------------------------------
  -- CALCULA META FILIAL
  -----------------------------------------------------------------------------------------------
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
                              p_descr_erro     out varchar2) is
  
  begin
  
    -- meta da filial para o indicador
    begin
      select m.num_meta, m.cod_un_meta, m.vlr_premio_fil, m.pct_calc_meta
        into p_meta, p_cod_un_meta, p_vlr_premio_fil, p_pct_calc_meta
        from srv_meta_filial m
       where m.cod_indic = p_cod_indic
         and m.cod_emp = p_cod_emp
         and m.cod_fil = p_cod_fil
         and m.num_ano = p_ano_meta
         and m.num_mes = p_mes_meta;
    exception
      when no_data_found then
        p_cod_erro   := 1;
        p_descr_erro := 'Erro ao selecionar meta. Meta nao encontrada - p_cod_indic: ' ||
                        p_cod_indic || ' - p_cod_fil: ' || p_cod_fil ||
                        ' - p_ano: ' || p_ano_meta || ' - p_mes: ' ||
                        p_mes_meta || ' - erro: ' || sqlerrm;
      
      when too_many_rows then
        p_cod_erro   := 2;
        p_descr_erro := 'Erro ao selecionar meta. Mais de uma meta encontrada - p_cod_indic: ' ||
                        p_cod_indic || ' - p_cod_fil: ' || p_cod_fil ||
                        ' - p_ano: ' || p_ano_meta || ' - p_mes: ' ||
                        p_mes_meta || ' - erro: ' || sqlerrm;
      
      when others then
        p_cod_erro   := 3;
        p_descr_erro := 'Erro ao selecionar meta - p_cod_indic: ' ||
                        p_cod_indic || ' - p_cod_fil: ' || p_cod_fil ||
                        ' - p_ano: ' || p_ano_meta || ' - p_mes: ' ||
                        p_mes_meta || ' - erro: ' || sqlerrm;
    end;
  
  exception
    when others then
      p_cod_erro   := 4;
      p_descr_erro := 'Erro ao selecionar meta - p_cod_indic: ' ||
                      p_cod_indic || ' - p_cod_fil: ' || p_cod_fil ||
                      ' - p_ano: ' || p_ano_meta || ' - p_mes: ' ||
                      p_mes_meta || ' - erro: ' || sqlerrm;
  end Prc_Calc_Meta_Fil;

  -----------------------------------------------------------------------------------------------
  -- CALCULA ESCALA FAIXA
  -----------------------------------------------------------------------------------------------
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
                               p_descr_erro        out varchar2) is
  begin
    begin
      select e.cod_escala,
             e.num_seq_escala_fx,
             e.num_realz_fx,
             e.cod_un_realz_fx,
             e.flg_pct_100,
             e.num_limite_fx
        into p_cod_escala,
             p_num_seq_escala_fx,
             p_num_realz_fx,
             p_cod_un_realz_fx,
             p_flg_pct_100,
             p_num_limite_fx
        from srv_escala             a,
             srv_grupo_indicador    b,
             srv_grupo_rem_variavel c,
             srv_indicador          d,
             srv_escala_faixa       e
       where a.cod_grp_indic = b.cod_grp_indic(+)
         and a.cod_grp_rem_var = c.cod_grp_rem_var(+)
         and a.cod_indic = d.cod_indic(+)
         and a.cod_escala = e.cod_escala
         and ((a.cod_indic is not null and a.cod_indic = p_cod_indic and
             a.cod_grp_indic is null and a.cod_grp_rem_var is null))
         and e.num_ini_fx <= p_num_realz_x_meta
         and e.num_fim_fx >= p_num_realz_x_meta
       order by a.cod_escala, e.num_seq_escala_fx;
    exception
      when no_data_found then
        begin
          select e.cod_escala,
                 e.num_seq_escala_fx,
                 e.num_realz_fx,
                 e.cod_un_realz_fx,
                 e.flg_pct_100,
                 e.num_limite_fx
            into p_cod_escala,
                 p_num_seq_escala_fx,
                 p_num_realz_fx,
                 p_cod_un_realz_fx,
                 p_flg_pct_100,
                 p_num_limite_fx
            from srv_escala             a,
                 srv_grupo_indicador    b,
                 srv_grupo_rem_variavel c,
                 srv_indicador          d,
                 srv_escala_faixa       e
           where a.cod_grp_indic = b.cod_grp_indic(+)
             and a.cod_grp_rem_var = c.cod_grp_rem_var(+)
             and a.cod_indic = d.cod_indic(+)
             and a.cod_escala = e.cod_escala
             and ((a.cod_grp_indic is not null and
                 a.cod_grp_indic = p_cod_grp_indic and
                 a.cod_indic is null and a.cod_grp_rem_var is null))
             and e.num_ini_fx <= p_num_realz_x_meta
             and e.num_fim_fx >= p_num_realz_x_meta
           order by a.cod_escala, e.num_seq_escala_fx;
        exception
          when no_data_found then
            begin
              select e.cod_escala,
                     e.num_seq_escala_fx,
                     e.num_realz_fx,
                     e.cod_un_realz_fx,
                     e.flg_pct_100,
                     e.num_limite_fx
                into p_cod_escala,
                     p_num_seq_escala_fx,
                     p_num_realz_fx,
                     p_cod_un_realz_fx,
                     p_flg_pct_100,
                     p_num_limite_fx
                from srv_escala             a,
                     srv_grupo_indicador    b,
                     srv_grupo_rem_variavel c,
                     srv_indicador          d,
                     srv_escala_faixa       e
               where a.cod_grp_indic = b.cod_grp_indic(+)
                 and a.cod_grp_rem_var = c.cod_grp_rem_var(+)
                 and a.cod_indic = d.cod_indic(+)
                 and a.cod_escala = e.cod_escala
                 and ((a.cod_grp_rem_var is not null and
                     a.cod_grp_rem_var = p_cod_grp_rem_var and
                     a.cod_indic is null and a.cod_grp_indic is null))
                 and e.num_ini_fx <= p_num_realz_x_meta
                 and e.num_fim_fx >= p_num_realz_x_meta
               order by a.cod_escala, e.num_seq_escala_fx;
              --
            exception
              when others then
                p_cod_erro   := 1;
                p_descr_erro := 'Erro ao selecionar escala - p_cod_indic: ' ||
                                p_cod_indic || ' - p_grp_indic: ' ||
                                p_cod_grp_indic || ' - p_cod_grp_rem_var: ' ||
                                p_cod_grp_rem_var ||
                                ' - p_num_realz_x_meta: ' ||
                                p_num_realz_x_meta || ' - erro: ' ||
                                sqlerrm;
            end;
        end;
    end;
  exception
    when others then
      p_cod_erro   := 1;
      p_descr_erro := 'Erro ao selecionar escala - p_cod_indic: ' ||
                      p_cod_indic || ' - p_grp_indic: ' || p_cod_grp_indic ||
                      ' - p_cod_grp_rem_var: ' || p_cod_grp_rem_var ||
                      ' - p_num_realz_x_meta: ' || p_num_realz_x_meta ||
                      ' - erro: ' || sqlerrm;
  end Prc_Calc_Escala_Fx;

  -----------------------------------------------------------------------------------------------
  -- CALCULA PONDERACAO POR TIPO FILIAL OU FILIAL
  -----------------------------------------------------------------------------------------------
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
                                       p_descr_erro      out varchar2) is
  begin
    begin
      -- primeiro tenta selecionar a ponderacao para o cargo, indicador/grupo
      -- e para a filial
      select p.cod_pond, p.num_peso, p.cod_un_peso, p.vlr_premio
        into p_cod_pond, p_num_peso, p_cod_un_peso, p_vlr_premio
        from srv_ponderacao p
       where (p.cod_cargo = p_cod_cargo or p_cod_cargo is null)
         and (p.cod_grp_indic = p_cod_grp_indic or p_cod_grp_indic is null)
         and (p.cod_indic = p_cod_indic or p_cod_indic is null)
         and (p.cod_fil = p_cod_fil or p_cod_fil is null);
    exception
      when others then
        begin
          -- tenta a ponderacao para o grp rem var, indicador/grupo
          -- e para a filial
          select p.cod_pond, p.num_peso, p.cod_un_peso, p.vlr_premio
            into p_cod_pond, p_num_peso, p_cod_un_peso, p_vlr_premio
            from srv_ponderacao p
           where (p.cod_grp_rem_var = p_cod_grp_rem_var or
                 p_cod_grp_rem_var is null)
             and (p.cod_grp_indic = p_cod_grp_indic or
                 p_cod_grp_indic is null)
             and (p.cod_indic = p_cod_indic or p_cod_indic is null)
             and (p.cod_fil = p_cod_fil or p_cod_fil is null);
        exception
          when others then
            begin
              -- tenta a ponderacao para o cargo, indicador/grupo
              -- e para o TIPO filial
              select p.cod_pond, p.num_peso, p.cod_un_peso, p.vlr_premio
                into p_cod_pond, p_num_peso, p_cod_un_peso, p_vlr_premio
                from srv_ponderacao p
               where (p.cod_cargo = p_cod_cargo or p_cod_cargo is null)
                 and (p.cod_grp_indic = p_cod_grp_indic or
                     p_cod_grp_indic is null)
                 and (p.cod_indic = p_cod_indic or p_cod_indic is null)
                 and (p.cod_tipo_fil = p_cod_tipo_fil or
                     p_cod_tipo_fil is null);
            exception
              when others then
                begin
                  -- tenta a ponderacao para o grp rem var, indicador/grupo
                  -- e para o TIPO filial
                  select p.cod_pond,
                         p.num_peso,
                         p.cod_un_peso,
                         p.vlr_premio
                    into p_cod_pond,
                         p_num_peso,
                         p_cod_un_peso,
                         p_vlr_premio
                    from srv_ponderacao p
                   where (p.cod_grp_rem_var = p_cod_grp_rem_var or
                         p_cod_grp_rem_var is null)
                     and (p.cod_grp_indic = p_cod_grp_indic or
                         p_cod_grp_indic is null)
                     and (p.cod_indic = p_cod_indic or p_cod_indic is null)
                     and (p.cod_tipo_fil = p_cod_tipo_fil or
                         p_cod_tipo_fil is null);
                exception
                  when no_data_found then
                    p_cod_erro   := 1;
                    p_descr_erro := 'Erro na proc prc_calc_ponderacao_tp_fil - ponderacao nao encontrada : ' ||
                                    'cargo - ' || p_cod_cargo || ',  ' ||
                                    'grp rem var - ' || p_cod_grp_rem_var ||
                                    ',  ' || 'grp indicador - ' ||
                                    p_cod_grp_indic || ',  ' ||
                                    'indicador - ' || p_cod_indic || ',  ' ||
                                    'tipo fil - ' || p_cod_tipo_fil ||
                                    ',  ' || 'filial - ' || p_cod_fil ||
                                    ',  ' || 'erro - ' || sqlerrm;
                    --
                    p_cod_pond    := null;
                    p_num_peso    := 0;
                    p_cod_un_peso := null;
                    p_vlr_premio  := 0;
                    --
                  when too_many_rows then
                    p_cod_erro   := 1;
                    p_descr_erro := 'Erro na proc prc_calc_ponderacao_tp_fil - ponderacao mais de uma linha retornada : ' ||
                                    'cargo - ' || p_cod_cargo || ',  ' ||
                                    'grp rem var - ' || p_cod_grp_rem_var ||
                                    ',  ' || 'grp indicador - ' ||
                                    p_cod_grp_indic || ',  ' ||
                                    'indicador - ' || p_cod_indic || ',  ' ||
                                    'tipo fil - ' || p_cod_tipo_fil ||
                                    ',  ' || 'filial - ' || p_cod_fil ||
                                    ',  ' || 'erro - ' || sqlerrm;
                    --
                    p_cod_pond    := null;
                    p_num_peso    := 0;
                    p_cod_un_peso := null;
                    p_vlr_premio  := 0;
                  
                end;
            end;
        end;
    end;
  exception
    when others then
      p_cod_erro   := 1;
      p_descr_erro := 'Erro na proc prc_calc_ponderacao_tp_fil - ponderacao - erro geral: ' ||
                      'cargo - ' || p_cod_cargo || ',  ' ||
                      'grp rem var - ' || p_cod_grp_rem_var || ',  ' ||
                      'grp indicador - ' || p_cod_grp_indic || ',  ' ||
                      'indicador - ' || p_cod_indic || ',  ' ||
                      'tipo fil - ' || p_cod_tipo_fil || ',  ' ||
                      'filial - ' || p_cod_fil || ',  ' || 'erro - ' ||
                      sqlerrm;
      --
      p_cod_pond    := null;
      p_num_peso    := 0;
      p_cod_un_peso := null;
      p_vlr_premio  := 0;
    
  end Prc_Calc_Ponderacao_Tp_Fil;

  -----------------------------------------------------------------------------------------------
  -- CALCULA PONDERACAO POR TIPO FILIAL OU FILIAL
  -----------------------------------------------------------------------------------------------
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
                                        p_descr_erro      out varchar2) is
  begin
  
    -- primeiro tenta selecionar a ponderacao para o cargo, indicador/grupo
    -- e para a filial
    select p.cod_pond, p.num_peso, p.cod_un_peso, p.vlr_premio
      into p_cod_pond, p_num_peso, p_cod_un_peso, p_vlr_premio
      from srv_ponderacao p
     where p.cod_cargo = p_cod_cargo
       and p.cod_indic = p_cod_indic
       and p.cod_tipo_fil = p_cod_tipo_fil;
  
  exception
    when others then
      p_cod_erro   := 1;
      p_descr_erro := 'Erro na proc prc_calc_ponderacao_tp_fil - ponderacao - erro geral: ' ||
                      'cargo - ' || p_cod_cargo || ',  ' ||
                      'grp rem var - ' || p_cod_grp_rem_var || ',  ' ||
                      'grp indicador - ' || p_cod_grp_indic || ',  ' ||
                      'indicador - ' || p_cod_indic || ',  ' ||
                      'tipo fil - ' || p_cod_tipo_fil || ',  ' ||
                      'filial - ' || p_cod_fil || ',  ' || 'erro - ' ||
                      sqlerrm;
      --
      p_cod_pond    := null;
      p_num_peso    := 0;
      p_cod_un_peso := null;
      p_vlr_premio  := 0;
    
  end Prc_Calc_Ponderacao_Tp_Fil2;

  -----------------------------------------------------------------------------------------------
  -- INSERE REALIZADO FUNCIONARIO INDICADOR
  -----------------------------------------------------------------------------------------------
  procedure Prc_Insere_Realz_Func_Indic(p_rec_srv_realz_func_indic in srv_realizado_func_indicador%rowtype,
                                        p_cod_erro                 out number,
                                        p_descr_erro               out varchar2) is
  
    --
    rec_srv_realz_func_indic_hist srv_realz_func_indic_hist%rowtype;
  
  begin
  
    ------------------------------------------------------------------------------
    -- insere o realizado do funcionario pelo indicador e filial na tabela
    ------------------------------------------------------------------------------
    begin
      insert into srv_realizado_func_indicador
        (cod_func,
         cod_indic,
         cod_emp,
         cod_fil,
         num_ano,
         num_mes,
         cod_escala,
         num_seq_escala_fx,
         num_realz_fx,
         cod_un_realz_fx,
         cod_pond,
         num_peso,
         cod_un_peso,
         num_realz_pond,
         cod_un_realz_pond,
         num_meta,
         cod_un_meta,
         num_realz,
         cod_un_realz,
         num_realz_x_meta,
         cod_un_realz_x_meta,
         vlr_premio,
         vlr_premio_func_calc,
         cod_un_vlr_premio_func_calc,
         pct_calc_rateio,
         dt_ini_sit_srv,
         cod_usuario,
         vlr_desc_indicacao,
         descr_meta,
         cod_cargo,
         sta_calc_realz,
         qtd_desc_indicacao)
      values
        (p_rec_srv_realz_func_indic.cod_func,
         p_rec_srv_realz_func_indic.cod_indic,
         p_rec_srv_realz_func_indic.cod_emp,
         p_rec_srv_realz_func_indic.cod_fil,
         p_rec_srv_realz_func_indic.num_ano,
         p_rec_srv_realz_func_indic.num_mes,
         p_rec_srv_realz_func_indic.cod_escala,
         p_rec_srv_realz_func_indic.num_seq_escala_fx,
         p_rec_srv_realz_func_indic.num_realz_fx,
         p_rec_srv_realz_func_indic.cod_un_realz_fx,
         p_rec_srv_realz_func_indic.cod_pond,
         p_rec_srv_realz_func_indic.num_peso,
         p_rec_srv_realz_func_indic.cod_un_peso,
         p_rec_srv_realz_func_indic.num_realz_pond,
         p_rec_srv_realz_func_indic.cod_un_realz_pond,
         p_rec_srv_realz_func_indic.num_meta,
         p_rec_srv_realz_func_indic.cod_un_meta,
         p_rec_srv_realz_func_indic.num_realz,
         p_rec_srv_realz_func_indic.cod_un_realz,
         p_rec_srv_realz_func_indic.num_realz_x_meta,
         p_rec_srv_realz_func_indic.cod_un_realz_x_meta,
         p_rec_srv_realz_func_indic.vlr_premio,
         p_rec_srv_realz_func_indic.vlr_premio_func_calc,
         p_rec_srv_realz_func_indic.cod_un_vlr_premio_func_calc,
         p_rec_srv_realz_func_indic.pct_calc_rateio,
         p_rec_srv_realz_func_indic.dt_ini_sit_srv,
         p_rec_srv_realz_func_indic.cod_usuario,
         p_rec_srv_realz_func_indic.vlr_desc_indicacao,
         p_rec_srv_realz_func_indic.descr_meta,
         p_rec_srv_realz_func_indic.cod_cargo,
         p_rec_srv_realz_func_indic.sta_calc_realz,
         p_rec_srv_realz_func_indic.qtd_desc_indicacao);
      --
    exception
      when dup_val_on_index then
      
        -- se ja existir a apuracao inserir o registro que ja existe na tabela de historico
        -- e gravar o calculado na tabela original
        begin
          --
          select cod_func,
                 cod_indic,
                 cod_emp,
                 cod_fil,
                 num_ano,
                 num_mes,
                 cod_escala,
                 num_seq_escala_fx,
                 num_realz_fx,
                 cod_un_realz_fx,
                 cod_pond,
                 num_peso,
                 cod_un_peso,
                 num_realz_pond,
                 cod_un_realz_pond,
                 num_meta,
                 cod_un_meta,
                 num_realz,
                 cod_un_realz,
                 num_realz_x_meta,
                 cod_un_realz_x_meta,
                 vlr_premio,
                 vlr_premio_func_calc,
                 cod_un_vlr_premio_func_calc,
                 pct_calc_rateio,
                 dt_ini_sit_srv,
                 cod_usuario,
                 vlr_desc_indicacao,
                 descr_meta,
                 cod_cargo,
                 sta_calc_realz,
                 qtd_desc_indicacao
            into rec_srv_realz_func_indic_hist.cod_func,
                 rec_srv_realz_func_indic_hist.cod_indic,
                 rec_srv_realz_func_indic_hist.cod_emp,
                 rec_srv_realz_func_indic_hist.cod_fil,
                 rec_srv_realz_func_indic_hist.num_ano,
                 rec_srv_realz_func_indic_hist.num_mes,
                 rec_srv_realz_func_indic_hist.cod_escala,
                 rec_srv_realz_func_indic_hist.num_seq_escala_fx,
                 rec_srv_realz_func_indic_hist.num_realz_fx,
                 rec_srv_realz_func_indic_hist.cod_un_realz_fx,
                 rec_srv_realz_func_indic_hist.cod_pond,
                 rec_srv_realz_func_indic_hist.num_peso,
                 rec_srv_realz_func_indic_hist.cod_un_peso,
                 rec_srv_realz_func_indic_hist.num_realz_pond,
                 rec_srv_realz_func_indic_hist.cod_un_realz_pond,
                 rec_srv_realz_func_indic_hist.num_meta,
                 rec_srv_realz_func_indic_hist.cod_un_meta,
                 rec_srv_realz_func_indic_hist.num_realz,
                 rec_srv_realz_func_indic_hist.cod_un_realz,
                 rec_srv_realz_func_indic_hist.num_realz_x_meta,
                 rec_srv_realz_func_indic_hist.cod_un_realz_x_meta,
                 rec_srv_realz_func_indic_hist.vlr_premio,
                 rec_srv_realz_func_indic_hist.vlr_premio_func_calc,
                 rec_srv_realz_func_indic_hist.cod_un_vlr_premio_func_calc,
                 rec_srv_realz_func_indic_hist.pct_calc_rateio,
                 rec_srv_realz_func_indic_hist.dt_ini_sit_srv,
                 rec_srv_realz_func_indic_hist.cod_usuario,
                 rec_srv_realz_func_indic_hist.vlr_desc_indicacao,
                 rec_srv_realz_func_indic_hist.descr_meta,
                 rec_srv_realz_func_indic_hist.cod_cargo,
                 rec_srv_realz_func_indic_hist.sta_calc_realz,
                 rec_srv_realz_func_indic_hist.qtd_desc_indicacao
            from srv_realizado_func_indicador
           where cod_func = p_rec_srv_realz_func_indic.cod_func
             and cod_indic = p_rec_srv_realz_func_indic.cod_indic
             and cod_emp = p_rec_srv_realz_func_indic.cod_emp
             and cod_fil = p_rec_srv_realz_func_indic.cod_fil
             and num_ano = p_rec_srv_realz_func_indic.num_ano
             and num_mes = p_rec_srv_realz_func_indic.num_mes;
        
          if sql%rowcount > 0 then
            --------------------------------------------------------------
            -- insere registro no historico
            --------------------------------------------------------------
            insert into srv_realz_func_indic_hist
              (cod_func,
               cod_indic,
               cod_emp,
               cod_fil,
               num_ano,
               num_mes,
               cod_escala,
               num_seq_escala_fx,
               num_realz_fx,
               cod_un_realz_fx,
               cod_pond,
               num_peso,
               cod_un_peso,
               num_realz_pond,
               cod_un_realz_pond,
               num_meta,
               cod_un_meta,
               num_realz,
               cod_un_realz,
               num_realz_x_meta,
               cod_un_realz_x_meta,
               vlr_premio,
               vlr_premio_func_calc,
               cod_un_vlr_premio_func_calc,
               pct_calc_rateio,
               dt_ini_sit_srv,
               cod_usuario,
               vlr_desc_indicacao,
               descr_meta,
               cod_cargo,
               sta_calc_realz,
               qtd_desc_indicacao)
            values
              (rec_srv_realz_func_indic_hist.cod_func,
               rec_srv_realz_func_indic_hist.cod_indic,
               rec_srv_realz_func_indic_hist.cod_emp,
               rec_srv_realz_func_indic_hist.cod_fil,
               rec_srv_realz_func_indic_hist.num_ano,
               rec_srv_realz_func_indic_hist.num_mes,
               rec_srv_realz_func_indic_hist.cod_escala,
               rec_srv_realz_func_indic_hist.num_seq_escala_fx,
               rec_srv_realz_func_indic_hist.num_realz_fx,
               rec_srv_realz_func_indic_hist.cod_un_realz_fx,
               rec_srv_realz_func_indic_hist.cod_pond,
               rec_srv_realz_func_indic_hist.num_peso,
               rec_srv_realz_func_indic_hist.cod_un_peso,
               rec_srv_realz_func_indic_hist.num_realz_pond,
               rec_srv_realz_func_indic_hist.cod_un_realz_pond,
               rec_srv_realz_func_indic_hist.num_meta,
               rec_srv_realz_func_indic_hist.cod_un_meta,
               rec_srv_realz_func_indic_hist.num_realz,
               rec_srv_realz_func_indic_hist.cod_un_realz,
               rec_srv_realz_func_indic_hist.num_realz_x_meta,
               rec_srv_realz_func_indic_hist.cod_un_realz_x_meta,
               rec_srv_realz_func_indic_hist.vlr_premio,
               rec_srv_realz_func_indic_hist.vlr_premio_func_calc,
               rec_srv_realz_func_indic_hist.cod_un_vlr_premio_func_calc,
               rec_srv_realz_func_indic_hist.pct_calc_rateio,
               rec_srv_realz_func_indic_hist.dt_ini_sit_srv,
               rec_srv_realz_func_indic_hist.cod_usuario,
               rec_srv_realz_func_indic_hist.vlr_desc_indicacao,
               rec_srv_realz_func_indic_hist.descr_meta,
               rec_srv_realz_func_indic_hist.cod_cargo,
               rec_srv_realz_func_indic_hist.sta_calc_realz,
               rec_srv_realz_func_indic_hist.qtd_desc_indicacao);
          
            ----------------------------------------------------------------
            -- atualiza o registro da tabela original
            ----------------------------------------------------------------
            update srv_realizado_func_indicador
               set cod_escala                  = p_rec_srv_realz_func_indic.cod_escala,
                   num_seq_escala_fx           = p_rec_srv_realz_func_indic.num_seq_escala_fx,
                   num_realz_fx                = p_rec_srv_realz_func_indic.num_realz_fx,
                   cod_un_realz_fx             = p_rec_srv_realz_func_indic.cod_un_realz_fx,
                   cod_pond                    = p_rec_srv_realz_func_indic.cod_pond,
                   num_peso                    = p_rec_srv_realz_func_indic.num_peso,
                   cod_un_peso                 = p_rec_srv_realz_func_indic.cod_un_peso,
                   num_realz_pond              = p_rec_srv_realz_func_indic.num_realz_pond,
                   cod_un_realz_pond           = p_rec_srv_realz_func_indic.cod_un_realz_pond,
                   num_meta                    = p_rec_srv_realz_func_indic.num_meta,
                   cod_un_meta                 = p_rec_srv_realz_func_indic.cod_un_meta,
                   num_realz                   = p_rec_srv_realz_func_indic.num_realz,
                   cod_un_realz                = p_rec_srv_realz_func_indic.cod_un_realz,
                   num_realz_x_meta            = p_rec_srv_realz_func_indic.num_realz_x_meta,
                   cod_un_realz_x_meta         = p_rec_srv_realz_func_indic.cod_un_realz_x_meta,
                   vlr_premio                  = p_rec_srv_realz_func_indic.vlr_premio,
                   vlr_premio_func_calc        = p_rec_srv_realz_func_indic.vlr_premio_func_calc,
                   cod_un_vlr_premio_func_calc = p_rec_srv_realz_func_indic.cod_un_vlr_premio_func_calc,
                   pct_calc_rateio             = p_rec_srv_realz_func_indic.pct_calc_rateio,
                   dt_ini_sit_srv              = p_rec_srv_realz_func_indic.dt_ini_sit_srv,
                   vlr_desc_indicacao          = p_rec_srv_realz_func_indic.vlr_desc_indicacao,
                   descr_meta                  = p_rec_srv_realz_func_indic.descr_meta,
                   cod_cargo                   = p_rec_srv_realz_func_indic.cod_cargo,
                   sta_calc_realz              = p_rec_srv_realz_func_indic.sta_calc_realz,
                   qtd_desc_indicacao          = p_rec_srv_realz_func_indic.qtd_desc_indicacao
            --
             where cod_func = p_rec_srv_realz_func_indic.cod_func
               and cod_indic = p_rec_srv_realz_func_indic.cod_indic
               and cod_emp = p_rec_srv_realz_func_indic.cod_emp
               and cod_fil = p_rec_srv_realz_func_indic.cod_fil
               and num_ano = p_rec_srv_realz_func_indic.num_ano
               and num_mes = p_rec_srv_realz_func_indic.num_mes;
            --
          else
            p_cod_erro   := 1;
            p_descr_erro := 'erro ao selecionar realz func indic p/ inserir no hist - reg nao encontrado ' ||
                            ' - p_cod_func: ' ||
                            p_rec_srv_realz_func_indic.cod_func ||
                            ' - p_cod_indic: ' ||
                            p_rec_srv_realz_func_indic.cod_indic ||
                            ' - p_cod_fil: ' ||
                            p_rec_srv_realz_func_indic.cod_fil ||
                            ' - erro: ' || sqlerrm;
          end if;
        exception
          when others then
            p_cod_erro   := 1;
            p_descr_erro := 'erro ao selecionar e atualizar realz func indic no hist : ' ||
                            ' - p_cod_func: ' ||
                            p_rec_srv_realz_func_indic.cod_func ||
                            ' - p_cod_indic: ' ||
                            p_rec_srv_realz_func_indic.cod_indic ||
                            ' - p_cod_fil: ' ||
                            p_rec_srv_realz_func_indic.cod_fil ||
                            ' - erro: ' || sqlerrm;
        end;
        --
      when others then
        p_cod_erro   := 1;
        p_descr_erro := 'erro geral ao inserir realizado func indic ' ||
                        ' - p_cod_func: ' ||
                        p_rec_srv_realz_func_indic.cod_func ||
                        ' - p_cod_indic: ' ||
                        p_rec_srv_realz_func_indic.cod_indic ||
                        ' - p_cod_fil: ' ||
                        p_rec_srv_realz_func_indic.cod_fil || ' - erro: ' ||
                        sqlerrm;
    end;
  
    --
    commit;
  
  exception
    when others then
      p_cod_erro   := 1;
      p_descr_erro := 'Erro geral na proc prc_insere_realz_func_indic ' ||
                      ' - p_cod_func: ' ||
                      p_rec_srv_realz_func_indic.cod_func ||
                      ' - p_cod_indic: ' ||
                      p_rec_srv_realz_func_indic.cod_indic ||
                      ' - p_cod_fil: ' ||
                      p_rec_srv_realz_func_indic.cod_fil || ' - erro: ' ||
                      sqlerrm;
  end Prc_Insere_Realz_Func_Indic;

  -----------------------------------------------------------------------------------------------
  -- INSERE REALIZADO FUNCIONARIO INDICADOR
  -----------------------------------------------------------------------------------------------
  procedure Prc_Ins_Realz_Func_Indic_UltFi(p_rec_srv_realz_func_indic in srv_realizado_func_indicador%rowtype,
                                           p_cod_erro                 out number,
                                           p_descr_erro               out varchar2) is
  
    --
    v_existe_realz_func_indic_per pls_integer := 0;
  
  begin
  
    ------------------------------------------------------------------------------------------------------
    -- insere o realizado do funcionario APENAS pelo indicador e periodo, SEM considerar a FILIAL do func
    ------------------------------------------------------------------------------------------------------
    -- verifica se ja existe um registro para o funcionario, indicador, mes e ano
    begin
      select count(1)
        into v_existe_realz_func_indic_per
        from srv_realizado_func_indicador
       where cod_func = p_rec_srv_realz_func_indic.cod_func
         and cod_indic = p_rec_srv_realz_func_indic.cod_indic
         and num_ano = p_rec_srv_realz_func_indic.num_ano
         and num_mes = p_rec_srv_realz_func_indic.num_mes
         and nvl(seg_fil, 'N') =
             nvl(p_rec_srv_realz_func_indic.seg_fil, 'N');
    exception
      when others then
        v_existe_realz_func_indic_per := 0;
    end;
  
    -- se ja existir manda para historico o reg atual e atualiza o registro
    if v_existe_realz_func_indic_per > 0 then
    
      --
      for c_realz_func_indic_hist in (select cod_func,
                                             cod_indic,
                                             cod_emp,
                                             cod_fil,
                                             num_ano,
                                             num_mes,
                                             cod_escala,
                                             num_seq_escala_fx,
                                             num_realz_fx,
                                             cod_un_realz_fx,
                                             cod_pond,
                                             num_peso,
                                             cod_un_peso,
                                             num_realz_pond,
                                             cod_un_realz_pond,
                                             num_meta,
                                             cod_un_meta,
                                             num_realz,
                                             cod_un_realz,
                                             num_realz_x_meta,
                                             cod_un_realz_x_meta,
                                             vlr_premio,
                                             vlr_premio_func_calc,
                                             cod_un_vlr_premio_func_calc,
                                             pct_calc_rateio,
                                             dt_ini_sit_srv,
                                             cod_usuario,
                                             vlr_desc_indicacao,
                                             descr_meta,
                                             cod_cargo,
                                             sta_calc_realz,
                                             qtd_desc_indicacao,
                                             qtd_meses_prop,
                                             vlr_premio_func_calc_prop,
                                             cod_sit_calc_realz_func,
                                             seg_fil
                                        from srv_realizado_func_indicador
                                       where cod_func =
                                             p_rec_srv_realz_func_indic.cod_func
                                         and cod_indic =
                                             p_rec_srv_realz_func_indic.cod_indic
                                         and num_ano =
                                             p_rec_srv_realz_func_indic.num_ano
                                         and num_mes =
                                             p_rec_srv_realz_func_indic.num_mes
                                         and nvl(seg_fil, 'N') =
                                             nvl(p_rec_srv_realz_func_indic.seg_fil,
                                                 'N')) loop
      
        --------------------------------------------------------------
        -- insere registro no historico
        --------------------------------------------------------------
        begin
          insert into srv_realz_func_indic_hist
            (cod_func,
             cod_indic,
             cod_emp,
             cod_fil,
             num_ano,
             num_mes,
             cod_escala,
             num_seq_escala_fx,
             num_realz_fx,
             cod_un_realz_fx,
             cod_pond,
             num_peso,
             cod_un_peso,
             num_realz_pond,
             cod_un_realz_pond,
             num_meta,
             cod_un_meta,
             num_realz,
             cod_un_realz,
             num_realz_x_meta,
             cod_un_realz_x_meta,
             vlr_premio,
             vlr_premio_func_calc,
             cod_un_vlr_premio_func_calc,
             pct_calc_rateio,
             dt_ini_sit_srv,
             cod_usuario,
             vlr_desc_indicacao,
             descr_meta,
             cod_cargo,
             sta_calc_realz,
             qtd_desc_indicacao,
             qtd_meses_prop,
             vlr_premio_func_calc_prop,
             cod_sit_calc_realz_func,
             seg_fil)
          values
            (c_realz_func_indic_hist.cod_func,
             c_realz_func_indic_hist.cod_indic,
             c_realz_func_indic_hist.cod_emp,
             c_realz_func_indic_hist.cod_fil,
             c_realz_func_indic_hist.num_ano,
             c_realz_func_indic_hist.num_mes,
             c_realz_func_indic_hist.cod_escala,
             c_realz_func_indic_hist.num_seq_escala_fx,
             c_realz_func_indic_hist.num_realz_fx,
             c_realz_func_indic_hist.cod_un_realz_fx,
             c_realz_func_indic_hist.cod_pond,
             c_realz_func_indic_hist.num_peso,
             c_realz_func_indic_hist.cod_un_peso,
             c_realz_func_indic_hist.num_realz_pond,
             c_realz_func_indic_hist.cod_un_realz_pond,
             c_realz_func_indic_hist.num_meta,
             c_realz_func_indic_hist.cod_un_meta,
             c_realz_func_indic_hist.num_realz,
             c_realz_func_indic_hist.cod_un_realz,
             c_realz_func_indic_hist.num_realz_x_meta,
             c_realz_func_indic_hist.cod_un_realz_x_meta,
             c_realz_func_indic_hist.vlr_premio,
             c_realz_func_indic_hist.vlr_premio_func_calc,
             c_realz_func_indic_hist.cod_un_vlr_premio_func_calc,
             c_realz_func_indic_hist.pct_calc_rateio,
             c_realz_func_indic_hist.dt_ini_sit_srv,
             c_realz_func_indic_hist.cod_usuario,
             c_realz_func_indic_hist.vlr_desc_indicacao,
             c_realz_func_indic_hist.descr_meta,
             c_realz_func_indic_hist.cod_cargo,
             c_realz_func_indic_hist.sta_calc_realz,
             c_realz_func_indic_hist.qtd_desc_indicacao,
             c_realz_func_indic_hist.qtd_meses_prop,
             c_realz_func_indic_hist.vlr_premio_func_calc_prop,
             c_realz_func_indic_hist.cod_sit_calc_realz_func,
             c_realz_func_indic_hist.seg_fil);
        
        exception
          when others then
            p_cod_erro   := 1;
            p_descr_erro := 'erro ao inserir realz func indic no hist : ' ||
                            ' - p_cod_func: ' ||
                            p_rec_srv_realz_func_indic.cod_func ||
                            ' - p_cod_indic: ' ||
                            p_rec_srv_realz_func_indic.cod_indic ||
                            ' - p_cod_fil: ' ||
                            p_rec_srv_realz_func_indic.cod_fil ||
                            ' - erro: ' || sqlerrm;
        end;
      
      -- c_realz_func_indic_hist
      end loop;
    
      ----------------------------------------------------------------
      -- atualiza o registro da tabela original
      ----------------------------------------------------------------
      begin
        -- se houver mais de um registro elimina os outros e deixa so 1
        if v_existe_realz_func_indic_per > 1 then
          delete from srv_realizado_func_indicador
           where cod_func = p_rec_srv_realz_func_indic.cod_func
             and cod_indic = p_rec_srv_realz_func_indic.cod_indic
             and num_ano = p_rec_srv_realz_func_indic.num_ano
             and num_mes = p_rec_srv_realz_func_indic.num_mes
             and nvl(seg_fil, 'N') =
                 nvl(p_rec_srv_realz_func_indic.seg_fil, 'N')
             and rownum <= (v_existe_realz_func_indic_per - 1);
        
        end if;
        --
        update srv_realizado_func_indicador
           set cod_escala                  = p_rec_srv_realz_func_indic.cod_escala,
               num_seq_escala_fx           = p_rec_srv_realz_func_indic.num_seq_escala_fx,
               num_realz_fx                = p_rec_srv_realz_func_indic.num_realz_fx,
               cod_un_realz_fx             = p_rec_srv_realz_func_indic.cod_un_realz_fx,
               cod_pond                    = p_rec_srv_realz_func_indic.cod_pond,
               num_peso                    = p_rec_srv_realz_func_indic.num_peso,
               cod_un_peso                 = p_rec_srv_realz_func_indic.cod_un_peso,
               num_realz_pond              = p_rec_srv_realz_func_indic.num_realz_pond,
               cod_un_realz_pond           = p_rec_srv_realz_func_indic.cod_un_realz_pond,
               num_meta                    = p_rec_srv_realz_func_indic.num_meta,
               cod_un_meta                 = p_rec_srv_realz_func_indic.cod_un_meta,
               num_realz                   = p_rec_srv_realz_func_indic.num_realz,
               cod_un_realz                = p_rec_srv_realz_func_indic.cod_un_realz,
               num_realz_x_meta            = p_rec_srv_realz_func_indic.num_realz_x_meta,
               cod_un_realz_x_meta         = p_rec_srv_realz_func_indic.cod_un_realz_x_meta,
               vlr_premio                  = p_rec_srv_realz_func_indic.vlr_premio,
               vlr_premio_func_calc        = p_rec_srv_realz_func_indic.vlr_premio_func_calc,
               cod_un_vlr_premio_func_calc = p_rec_srv_realz_func_indic.cod_un_vlr_premio_func_calc,
               pct_calc_rateio             = p_rec_srv_realz_func_indic.pct_calc_rateio,
               dt_ini_sit_srv              = p_rec_srv_realz_func_indic.dt_ini_sit_srv,
               vlr_desc_indicacao          = p_rec_srv_realz_func_indic.vlr_desc_indicacao,
               descr_meta                  = p_rec_srv_realz_func_indic.descr_meta,
               cod_emp                     = p_rec_srv_realz_func_indic.cod_emp,
               cod_fil                     = p_rec_srv_realz_func_indic.cod_fil,
               cod_cargo                   = p_rec_srv_realz_func_indic.cod_cargo,
               sta_calc_realz              = p_rec_srv_realz_func_indic.sta_calc_realz,
               qtd_desc_indicacao          = p_rec_srv_realz_func_indic.qtd_desc_indicacao,
               qtd_meses_prop              = p_rec_srv_realz_func_indic.qtd_meses_prop,
               vlr_premio_func_calc_prop   = p_rec_srv_realz_func_indic.vlr_premio_func_calc_prop,
               cod_sit_calc_realz_func     = p_rec_srv_realz_func_indic.cod_sit_calc_realz_func
         where cod_func = p_rec_srv_realz_func_indic.cod_func
           and cod_indic = p_rec_srv_realz_func_indic.cod_indic
           and num_ano = p_rec_srv_realz_func_indic.num_ano
           and num_mes = p_rec_srv_realz_func_indic.num_mes
           and nvl(seg_fil, 'N') =
               nvl(p_rec_srv_realz_func_indic.seg_fil, 'N');
      
      exception
        when others then
          p_cod_erro   := 1;
          p_descr_erro := 'erro ao atualizar realz func indic desconsiderando a fil : ' ||
                          ' - p_cod_func: ' ||
                          p_rec_srv_realz_func_indic.cod_func ||
                          ' - p_cod_indic: ' ||
                          p_rec_srv_realz_func_indic.cod_indic ||
                          ' - p_cod_fil: ' ||
                          p_rec_srv_realz_func_indic.cod_fil || ' - erro: ' ||
                          sqlerrm;
      end;
    
      -- se nao existe o registro pelo func, indic e periodo
    else
      --
      begin
        insert into srv_realizado_func_indicador
          (cod_func,
           cod_indic,
           cod_emp,
           cod_fil,
           num_ano,
           num_mes,
           cod_escala,
           num_seq_escala_fx,
           num_realz_fx,
           cod_un_realz_fx,
           cod_pond,
           num_peso,
           cod_un_peso,
           num_realz_pond,
           cod_un_realz_pond,
           num_meta,
           cod_un_meta,
           num_realz,
           cod_un_realz,
           num_realz_x_meta,
           cod_un_realz_x_meta,
           vlr_premio,
           vlr_premio_func_calc,
           cod_un_vlr_premio_func_calc,
           pct_calc_rateio,
           dt_ini_sit_srv,
           cod_usuario,
           vlr_desc_indicacao,
           descr_meta,
           cod_cargo,
           sta_calc_realz,
           qtd_desc_indicacao,
           qtd_meses_prop,
           vlr_premio_func_calc_prop,
           cod_sit_calc_realz_func,
           seg_fil)
        values
          (p_rec_srv_realz_func_indic.cod_func,
           p_rec_srv_realz_func_indic.cod_indic,
           p_rec_srv_realz_func_indic.cod_emp,
           p_rec_srv_realz_func_indic.cod_fil,
           p_rec_srv_realz_func_indic.num_ano,
           p_rec_srv_realz_func_indic.num_mes,
           p_rec_srv_realz_func_indic.cod_escala,
           p_rec_srv_realz_func_indic.num_seq_escala_fx,
           p_rec_srv_realz_func_indic.num_realz_fx,
           p_rec_srv_realz_func_indic.cod_un_realz_fx,
           p_rec_srv_realz_func_indic.cod_pond,
           p_rec_srv_realz_func_indic.num_peso,
           p_rec_srv_realz_func_indic.cod_un_peso,
           p_rec_srv_realz_func_indic.num_realz_pond,
           p_rec_srv_realz_func_indic.cod_un_realz_pond,
           p_rec_srv_realz_func_indic.num_meta,
           p_rec_srv_realz_func_indic.cod_un_meta,
           p_rec_srv_realz_func_indic.num_realz,
           p_rec_srv_realz_func_indic.cod_un_realz,
           p_rec_srv_realz_func_indic.num_realz_x_meta,
           p_rec_srv_realz_func_indic.cod_un_realz_x_meta,
           p_rec_srv_realz_func_indic.vlr_premio,
           p_rec_srv_realz_func_indic.vlr_premio_func_calc,
           p_rec_srv_realz_func_indic.cod_un_vlr_premio_func_calc,
           p_rec_srv_realz_func_indic.pct_calc_rateio,
           p_rec_srv_realz_func_indic.dt_ini_sit_srv,
           p_rec_srv_realz_func_indic.cod_usuario,
           p_rec_srv_realz_func_indic.vlr_desc_indicacao,
           p_rec_srv_realz_func_indic.descr_meta,
           p_rec_srv_realz_func_indic.cod_cargo,
           p_rec_srv_realz_func_indic.sta_calc_realz,
           p_rec_srv_realz_func_indic.qtd_desc_indicacao,
           p_rec_srv_realz_func_indic.qtd_meses_prop,
           p_rec_srv_realz_func_indic.vlr_premio_func_calc_prop,
           p_rec_srv_realz_func_indic.cod_sit_calc_realz_func,
           p_rec_srv_realz_func_indic.seg_fil);
        --
      exception
        when others then
          p_cod_erro   := 1;
          p_descr_erro := 'erro ao inserir realizado func indic sem cosniderar a filial' ||
                          ' - p_cod_func: ' ||
                          p_rec_srv_realz_func_indic.cod_func ||
                          ' - p_cod_indic: ' ||
                          p_rec_srv_realz_func_indic.cod_indic ||
                          ' - p_ano: ' ||
                          p_rec_srv_realz_func_indic.num_ano ||
                          ' - p_mes: ' ||
                          p_rec_srv_realz_func_indic.num_mes ||
                          ' - p_cod_fil: ' ||
                          p_rec_srv_realz_func_indic.cod_fil || ' - erro: ' ||
                          sqlerrm;
        
      end;
    
    end if;
  
    --
    commit;
  
  exception
    when others then
      p_cod_erro   := 1;
      p_descr_erro := 'Erro geral na proc prc_ins_realz_func_indic_ultfi ' ||
                      ' - p_cod_func: ' ||
                      p_rec_srv_realz_func_indic.cod_func ||
                      ' - p_cod_indic: ' ||
                      p_rec_srv_realz_func_indic.cod_indic ||
                      ' - p_cod_fil: ' ||
                      p_rec_srv_realz_func_indic.cod_fil || ' - erro: ' ||
                      sqlerrm;
    
  end Prc_Ins_Realz_Func_Indic_UltFi;
end pkg_srv_geral;
/
