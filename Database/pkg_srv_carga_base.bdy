create or replace package body srv.PKG_SRV_CARGA_BASE is

  function fnc_abre_arquivo    (p_arq_txt   in out utl_file.file_type
                               ,p_caminho   in  varchar2
                               ,p_nome_txt  in  varchar2
                               ,p_mode      in  varchar2
                               ,p_sqlcode   out number
                               ,p_sqlerrm   out varchar2 ) return number is
    --
    v_retorno  number(2);

  begin
     begin
        p_arq_txt := utl_file.fopen(p_caminho
                                   ,p_nome_txt
                                   ,p_mode);
        v_retorno    := 0;
     exception
        when utl_file.invalid_path then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := 'UTL_FILE INVALID_PATH';
           v_retorno   := 1;
        when utl_file.invalid_mode then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := 'UTL_FILE INVALID_MODE';
           v_retorno   := 1;
        when utl_file.invalid_filehandle then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := 'UTL_FILE INVALID_FILEHANDLE';
           v_retorno   := 1;
        when utl_file.invalid_filename then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := 'UTL_FILE INVALID_FILENAME';
           v_retorno   := 1;
        when utl_file.invalid_operation then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := 'UTL_FILE INVALID_OPERATION';
           v_retorno   := 1;
        when utl_file.internal_error then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := 'UTL_FILE INTERNAL_ERROR';
           v_retorno   := 1;
        when others then
           p_sqlcode      := sqlcode;
           p_sqlerrm      := substr('UTL_FILE: ' || sqlerrm, 1, 500);
           v_retorno   := 1;
     end;
     return(v_retorno);

  end fnc_abre_arquivo;

  ------------------------------------------------------------------------------------------------------------------------
  -- Silvia 16/01/2014 - Processo que carrega os arquivos para fechamento
  ------------------------------------------------------------------------------------------------------------------------
  procedure prc_carrega_arquivos (p_cod_carga in srv_agendamento.cod_carga%type
                                 ,p_retorno  out number) is

   v_cod_func  srv_func_base_rem_var.cod_func%type;
   v_cod_carga srv_agendamento.cod_carga%type;

   cursor cagd is
     select sa.cod_carga
       from srv_agendamento sa
      where sa.flg_ativa = 1;

   begin

      for rcagd in cagd loop

        if p_cod_carga is null then
           v_cod_carga := rcagd.cod_carga;
        else
           v_cod_carga := p_cod_carga;
        end if;

        --
         if v_cod_carga in(1) then
              prc_carrega_meta;
         elsif
            v_cod_carga in(2) then
              prc_metas_psf;
         elsif
            v_cod_carga in(3) then
              prc_carrega_funcionario;
          elsif
            v_cod_carga in(5) then
              prc_lojas_lider;
         elsif
            v_cod_carga in(6) then
              prc_agrupa_filial_vm;
         elsif
            v_cod_carga in(7) then
              prc_carrega_digitalizado_pl;
         elsif
            v_cod_carga in(8) then
              prc_carrega_digitalizado_sax;
         elsif
            v_cod_carga in(9) then
              prc_ox_ativacao;
         end if;

         commit;

      end loop;

      --commit;

   exception
      when others then
        dbms_output.put_line(sqlerrm || ' cod_func ' || v_cod_func);
         p_retorno := 1;

   end prc_carrega_arquivos;


------------------------------------------------------------------------------------------------------------------------
   -- Silvia 15/01/2014 - Funcao que verifica se a carga sera efetuada conforme agendamento
   -- p_retorno 0-OK Carrega  1-Nao Carrega
   ------------------------------------------------------------------------------------------------------------------------
   procedure prc_consulta_agendamento ( p_cod_carga in srv_agendamento.cod_carga%type
                                      , p_num_ano out srv_agendamento.num_ano%type
                                      , p_num_mes out srv_agendamento.num_mes%type
                                      , p_num_arq out srv_agendamento.num_arq%type
                                      , p_nome_arq out srv_agendamento.nome_arq%type
                                      , p_dir_origem out srv_agendamento.dir_origem%type
                                      , p_dir_destino out srv_agendamento.dir_destino%type
                                      , p_retorno out number
                                       ) is

   v_dt_agendamento srv_agendamento.dt_agendamento%type;
   v_num_ano srv_agendamento.num_ano%type;
   v_num_mes srv_agendamento.num_mes%type;
   v_num_arq srv_agendamento.num_arq%type;
   v_nome_arq srv_agendamento.nome_arq%type;
   v_dir_origem srv_agendamento.dir_origem%type;
   v_dir_destino srv_agendamento.dir_destino%type;
--   v_retorno boolean;
   begin
      p_retorno := 1;
      begin
        select
          case
            when trunc(sysdate) = sa.dt_limite_proc then
              sa.dt_limite_proc
            when trunc(sysdate) = sa.dt_agendamento then
              sa.dt_agendamento
           end
           , sa.num_ano
           , sa.num_mes
           , nvl(sa.num_arq,0) + 1 num_arq
           , sa.nome_arq
           , sa.dir_origem
           , sa.dir_destino
        into v_dt_agendamento
           , v_num_ano
           , v_num_mes
           , v_num_arq
           , v_nome_arq
           , v_dir_origem
           , v_dir_destino
        from srv_agendamento sa
       where sa.cod_carga = p_cod_carga
         and sa.flg_ativa = 1
         and(sa.dt_limite_proc = trunc(sysdate)
          or sa.dt_agendamento = trunc(sysdate));

         p_retorno := 0;
         p_num_ano := v_num_ano;
         p_num_mes := v_num_mes;
         p_num_arq := v_num_arq;
         p_nome_arq := v_nome_arq;
         p_dir_origem := v_dir_origem;
         p_dir_destino := v_dir_destino;

      exception
         when no_data_found then
           p_retorno := 1;
      end;

   exception
      when others then
         p_retorno := 1;
   --
   end prc_consulta_agendamento;

--
  procedure prc_carga_erro (p_processo in varchar2
                           ,p_msg_erro in varchar2
                           ,p_retorno out number) is

  begin

    p_retorno := 0;

    --Insere log
    insert into srv_carga_erro
                    (descr_carga
                    ,memo_dth_geracao
                    ,memo_obs)
              values(p_processo
                    ,sysdate
                    ,p_msg_erro);
    commit;

  exception
    when others then
      p_retorno := 1;

  end prc_carga_erro;

  --Procedure ultilizada pelo job
  procedure prc_executa_job is

    v_retorno number;

  begin

    prc_carrega_arquivos(p_cod_carga => null, p_retorno => v_retorno);
    dbms_output.put_line(v_retorno ||' - Ocorreu o erro '||sqlerrm);

  end prc_executa_job;

 --############################################################################################
 --prc_carrega_digitalizado_sax
 --############################################################################################
 procedure prc_carrega_digitalizado_pl  is

   v_num_mes      srv_agendamento.num_mes%type;
   v_num_ano      srv_agendamento.num_ano%type;
   v_num_arq      srv_agendamento.num_arq%type;
   v_nome_arq     srv_agendamento.nome_arq%type;
   v_dir_origem   srv_agendamento.dir_origem%type;
   v_dir_destino  srv_agendamento.dir_destino%type;
   v_cod_fil      srv_meta_filial.cod_fil%type;
   v_cod_carga    srv_agendamento.cod_carga%type := 7;
   v_arq_txt_in   utl_file.file_type;
   v_retorno      number;
   v_linha        varchar2(2000);
   v_sqlcode      number;
   v_sqlerrm      varchar2(2000);
   v_cli_cpf      number(11);
   v_vnd_cpf      number(11);
   v_msg          varchar2(255);
   v_query        varchar2(2000);

  begin

    prc_consulta_agendamento(p_cod_carga   => v_cod_carga
                           , p_num_ano     => v_num_ano
                           , p_num_mes     => v_num_mes
                           , p_num_arq     => v_num_arq
                           , p_nome_arq    => v_nome_arq
                           , p_dir_origem  => v_dir_origem
                           , p_dir_destino => v_dir_destino
                           , p_retorno     => v_retorno
                             );
    if v_retorno = 0 then

      v_retorno := fnc_abre_arquivo(v_arq_txt_in
                                   ,v_dir_origem
                                   ,v_nome_arq || '_' || v_num_ano || lpad(v_num_mes,2,0) || '.txt'
                                   ,'r'
                                   ,v_sqlcode
                                   ,v_sqlerrm);
      if v_retorno = 0 then

        utl_file.get_line(v_arq_txt_in, v_linha);--Header

        loop
          begin
            utl_file.get_line(v_arq_txt_in, v_linha);

            v_linha := replace(replace(replace(v_linha,CHR(10),''),CHR(13),''),CHR(9),'');

            v_cod_fil := trim(substr(v_linha, 1, instr(v_linha, ';', 1, 1) -1));

            v_vnd_cpf := trim(substr(v_linha, instr(v_linha, ';', 1, 1) + 1,
                         instr(v_linha, ';', 1, 2) -
                         instr(v_linha, ';', 1, 1) - 1));

            v_cli_cpf := trim(substr(v_linha, instr(v_linha, ';', -1) +1));

            v_query := 'update srv_realzfu_cart_aprov_' || v_num_ano || lpad(v_num_mes,2,0) ||
                       '   set flg_doc_digitalizado = ''N''' ||
                       ' where cpf_vendedor = ' || v_vnd_cpf ||
                       '   and fil_cod      = ' || v_cod_fil ||
                       '   and cli_cpf      = ' || v_cli_cpf;

            execute immediate v_query;

          exception
            when no_data_found then
              v_msg := 'O arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)||' foi processado as '||to_char(sysdate,'dd/mm/yyyy hh24:mi:ss');
              v_retorno := 0;
              commit;
              exit;

            when others then
              begin
                v_msg := 'Erro no processamento do arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0);
                prc_carga_erro (p_processo => upper('prc_carrega_digitalizado_pl')
                               ,p_msg_erro => v_msg
                               ,p_retorno  => v_retorno);
              exception
                when others then
                  null;
              end;

              v_retorno := 1;
              rollback;
              exit;
          end;
        end loop;

        if v_retorno = 0 then
          begin
            pkg_ccm_func_proc_geral.prc_shell_move(p_var_dir_origem      => v_dir_origem
                                                  ,p_var_dir_destino     => v_dir_destino
                                                  ,p_var_arquivo_origem  => v_nome_arq || '_' || v_num_ano || lpad(v_num_mes,2,0) || '.txt'
                                                  ,p_var_arquivo_destino => v_nome_arq || '_' || v_num_ano || lpad(v_num_mes,2,0) || '.txt'
                                                  ,p_bln_decript         => false
                                                  ,p_var_erro            => v_retorno);
          exception
            when others then
              null;
          end;

          begin
             insert into srv_agendamento_log
                  ( cod_carga
                  , interface
                  , num_ano
                  , num_mes
                  , num_arq
                  , dt_inclusao)
             values
                  ( v_cod_carga
                  , v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)
                  , v_num_ano
                  , v_num_mes
                  , v_num_arq
                  , sysdate);
          exception
            when others then
              null;
          end;

          begin
            update srv_agendamento sa
               set sa.dt_agendamento = to_date('12'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
                 , sa.dt_ult_proc    = trunc(sysdate)
                 , sa.dt_limite_proc = to_date('15'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
                 , sa.num_ano        = to_char(sysdate,'yyyy')
                 , sa.num_mes        = to_char(sysdate,'mm')
                 , sa.num_arq        = v_num_arq
             where sa.cod_carga      = v_cod_carga;

          exception
            when others then
              null;
          end;

        end if;

        begin
          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);
        exception
          when others then
            null;
        end;

      elsif v_retorno = 1 then

        begin
          v_msg := 'O arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)||' nao foi encontrado.';

          prc_carga_erro (p_processo => upper('prc_carrega_digitalizado_pl')
                         ,p_msg_erro => v_msg
                         ,p_retorno  => v_retorno);

          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);

          update srv_agendamento sa
             set sa.dt_agendamento = sa.dt_agendamento + 1
               , sa.dt_limite_proc = sa.dt_limite_proc + 1
           where sa.cod_carga      = v_cod_carga;

        exception
          when others then
            dbms_output.put_line('Retorno: '||v_retorno||'/ Erro: '||SQLERRM);
        end;

      end if;

      commit;

    end if;

  exception
    when others then
      dbms_output.put_line('Ocorreu o erro: '||sqlerrm);

  end prc_carrega_digitalizado_pl;

 --############################################################################################
 --prc_carrega_digitalizado_sax
 --############################################################################################
 procedure prc_carrega_digitalizado_sax  is

   v_num_mes      srv_agendamento.num_mes%type;
   v_num_ano      srv_agendamento.num_ano%type;
   v_num_arq      srv_agendamento.num_arq%type;
   v_nome_arq     srv_agendamento.nome_arq%type;
   v_dir_origem   srv_agendamento.dir_origem%type;
   v_dir_destino  srv_agendamento.dir_destino%type;
   v_cod_fil      srv_meta_filial.cod_fil%type;
   v_contrato     number(20);
   v_cod_carga    srv_agendamento.cod_carga%type := 8;
   v_arq_txt_in   utl_file.file_type;
   v_retorno      number;
   v_linha        varchar2(2000);
   v_sqlcode      number;
   v_sqlerrm      varchar2(2000);
   v_cli_cpf      number(11);
   v_msg          varchar2(255);
   v_query        varchar2(2000);

  begin

    prc_consulta_agendamento(p_cod_carga   => v_cod_carga
                           , p_num_ano     => v_num_ano
                           , p_num_mes     => v_num_mes
                           , p_num_arq     => v_num_arq
                           , p_nome_arq    => v_nome_arq
                           , p_dir_origem  => v_dir_origem
                           , p_dir_destino => v_dir_destino
                           , p_retorno     => v_retorno
                             );
    if v_retorno = 0 then

      v_retorno := fnc_abre_arquivo(v_arq_txt_in
                                   ,v_dir_origem
                                   ,v_nome_arq || '_' || v_num_ano || lpad(v_num_mes,2,0) || '.txt'
                                   ,'r'
                                   ,v_sqlcode
                                   ,v_sqlerrm);
      if v_retorno = 0 then

        utl_file.get_line(v_arq_txt_in, v_linha);--Header

        loop
          begin
            utl_file.get_line(v_arq_txt_in, v_linha);

            v_linha := replace(replace(replace(v_linha,CHR(10),''),CHR(13),''),CHR(9),'');

            v_contrato := trim(substr(v_linha, 1, instr(v_linha, ';', 1, 1) -1));

            v_cli_cpf  := trim(substr(v_linha, instr(v_linha, ';', 1, 1) + 1,
                          instr(v_linha, ';', 1, 2) -
                          instr(v_linha, ';', 1, 1) - 1));

            v_cod_fil  := trim(substr(v_linha, instr(v_linha, ';', -1) +1));

            v_query := 'update srv_realzfu_emp_seg_sax_' || v_num_ano || lpad(v_num_mes,2,0) ||
                       '   set flg_doc_digitalizado = ''N''' ||
                       ' where fil_cod      = ' || v_cod_fil ||
                       '   and cod_contrato = ' || v_contrato ||
                       '   and cli_cpf      = ' || v_cli_cpf;

            execute immediate v_query;

          exception
            when no_data_found then
              v_msg := 'O arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)||' foi processado as '||to_char(sysdate,'dd/mm/yyyy hh24:mi:ss');
              v_retorno := 0;
              commit;
              exit;

            when others then
              begin
                v_msg := 'Erro no processamento do arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0);
                prc_carga_erro (p_processo => upper('prc_carrega_digitalizado_sax')
                               ,p_msg_erro => v_msg
                               ,p_retorno  => v_retorno);
              exception
                when others then
                  null;
              end;

              v_retorno := 1;
              rollback;
              exit;
          end;
        end loop;

        if v_retorno = 0 then
          begin
            pkg_ccm_func_proc_geral.prc_shell_move(p_var_dir_origem      => v_dir_origem
                                                  ,p_var_dir_destino     => v_dir_destino
                                                  ,p_var_arquivo_origem  => v_nome_arq || '_' || v_num_ano || lpad(v_num_mes,2,0) || '.txt'
                                                  ,p_var_arquivo_destino => v_nome_arq || '_' || v_num_ano || lpad(v_num_mes,2,0) || '.txt'
                                                  ,p_bln_decript         => false
                                                  ,p_var_erro            => v_retorno);
          exception
            when others then
              null;
          end;

          begin
             insert into srv_agendamento_log
                  ( cod_carga
                  , interface
                  , num_ano
                  , num_mes
                  , num_arq
                  , dt_inclusao)
             values
                  ( v_cod_carga
                  , v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)
                  , v_num_ano
                  , v_num_mes
                  , v_num_arq
                  , sysdate);
          exception
            when others then
              null;
          end;

          begin
            update srv_agendamento sa
               set sa.dt_agendamento = to_date('12'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
                 , sa.dt_ult_proc    = trunc(sysdate)
                 , sa.dt_limite_proc = to_date('15'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
                 , sa.num_ano        = to_char(sysdate,'yyyy')
                 , sa.num_mes        = to_char(sysdate,'mm')
                 , sa.num_arq        = v_num_arq
             where sa.cod_carga      = v_cod_carga;

          exception
            when others then
              null;
          end;

        end if;

        begin
          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);
        exception
          when others then
            null;
        end;

      elsif v_retorno = 1 then

        begin
          v_msg := 'O arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)||' nao foi encontrado.';

          prc_carga_erro (p_processo => upper('prc_carrega_digitalizado_sax')
                         ,p_msg_erro => v_msg
                         ,p_retorno  => v_retorno);

          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);

          update srv_agendamento sa
             set sa.dt_agendamento = sa.dt_agendamento + 1
               , sa.dt_limite_proc = sa.dt_limite_proc + 1
           where sa.cod_carga      = v_cod_carga;

        exception
          when others then
            dbms_output.put_line('Retorno: '||v_retorno||'/ Erro: '||SQLERRM);
        end;

      end if;

      commit;

    end if;

  exception
    when others then
      dbms_output.put_line('Ocorreu o erro: '||sqlerrm);

  end prc_carrega_digitalizado_sax;

  --#################################################################################
  --Carga de Funcionarios
  --#################################################################################
  procedure prc_carrega_funcionario is

    cursor c_func is
      select con_cdicontratado                                        cod_func
           , 1                                                        cod_emp
           , loc_cdiempresa                                           cod_fil
           , loc_d1slocal                                             cod_fil_rh
           , substr(loc_d1sapelido,1,50)                              descr_fil_rh
           , car_cdicargo                                             cod_cargo
           , cex_coscrachabase                                        cod_cracha
           , replace(replace(con_nuscicnumero,'-',''),'.','')         num_cpf_func
           , substr(con_dssnomecompleto,1,60)                         nome_func
           , sit_cdisituacao                                          cod_sit_rh
           , substr(sit_d1ssituacao,1,100)                            descr_sit_rh
           , to_date(con_dtdiniciosituacao,'dd/mm/yyyy')              dt_ini_sit_rh
           , emp_cdiempresa                                           cod_emp_rh
           , substr(emp_dssempresa,1,60)                              descr_emp_rh
           , substr(ccu_cosestrutura,1,40)                            cod_ccst_func
           , substr(ccu_d1scentrocusto,1,40)                          descr_ccst_func
           , 1                                                        cod_func_superior
           , 1                                                        cod_fun_avaliador
           , to_date(con_dtdadmissao,'dd/mm/yyyy')                    dt_admissao
           , to_date(con_dtdrescisao,'dd/mm/yyyy')                    dt_demissao
           --, sysdate                                                dt_ini_sit_srv
           , 1                                                        cod_usuario
           , sit_cdisituacao2                                         cod_sit_rh_ant
           , substr(sit_d1ssituacao2,1,100)                           descr_sit_rh_ant
           , to_date(cst_dtdsituacaoinicio,'dd/mm/yyyy')              dt_ini_sit_rh_ant
           , crc_qtnverba                                             qtd_dias_trab_per
           , to_date(cgc_dtdiniciograde,'dd/mm/yyyy')                 dt_promo_eleg
           , decode(con_cdidesligamento,'0',null,con_cdidesligamento) cod_mot_demissao
           , diasafastados                                            qtd_dias_afastam
           , upper(cug_d1sccustocalculogrupo)                         flg_ccst_comercial
           , trim(estrutura)                                          estrutura
           , trim(estrutura_gestor)                                   estrutura_gestor
        from srv_funcionario_apdata
       where st_carga in('0','2');

    v_sequencia    number := 0;
    v_commit       number := 0;
    v_retorno      number := 0;
    v_aux          number := 0;
    v_processo     varchar2(255) := upper('prc_importa_func');
    v_num_mes      srv_agendamento.num_mes%type;
    v_num_ano      srv_agendamento.num_ano%type;
    v_num_arq      srv_agendamento.num_arq%type;
    v_nome_arq     srv_agendamento.nome_arq%type;
    v_dir_origem   srv_agendamento.dir_origem%type;
    v_dir_destino  srv_agendamento.dir_destino%type;
    v_cod_carga    srv_agendamento.cod_carga%type := 3;
    v_msg          varchar2(255);
    v_dt_atual     date := sysdate;
    v_nm_tab_realz_fu varchar2(255);

  begin

    prc_consulta_agendamento(p_cod_carga   => v_cod_carga
                           , p_num_ano     => v_num_ano
                           , p_num_mes     => v_num_mes
                           , p_num_arq     => v_num_arq
                           , p_nome_arq    => v_nome_arq
                           , p_dir_origem  => v_dir_origem
                           , p_dir_destino => v_dir_destino
                           , p_retorno     => v_retorno
                             );
    if v_retorno = 0 then

      for r_func in c_func loop

        --Guarda Historico
        begin

          select count(1)
            into v_aux
            from srv_funcionario
           where cod_func = r_func.cod_func;

          if v_aux > 0 then

            select nvl(max(func_seq),0) + 1
              into v_sequencia
              from srv_funcionario_hist
             where cod_func = r_func.cod_func;

            insert into srv_funcionario_hist
                   (cod_func
                   ,func_seq
                   ,cod_emp
                   ,cod_fil
                   ,cod_fil_rh
                   ,descr_fil_rh
                   ,cod_cargo
                   ,cod_cracha
                   ,num_cpf_func
                   ,nome_func
                   ,cod_sit_rh
                   ,descr_sit_rh
                   ,dt_ini_sit_rh
                   ,cod_emp_rh
                   ,descr_emp_rh
                   ,cod_ccst_func
                   ,descr_ccst_func
                   ,cod_func_superior
                   ,cod_fun_avaliador
                   ,dt_admissao
                   ,dt_demissao
                   ,dt_ini_sit_srv
                   ,cod_usuario
                   ,cod_sit_rh_ant
                   ,descr_sit_rh_ant
                   ,dt_ini_sit_rh_ant
                   ,qtd_dias_trab_per
                   ,dt_promo_eleg
                   ,cod_mot_demissao
                   ,qtd_dias_afastam
                   ,flg_ccst_comercial
                   ,dt_inclusao
                   ,estrutura
                   ,estrutura_gestor)
             select cod_func
                   ,v_sequencia
                   ,cod_emp
                   ,cod_fil
                   ,cod_fil_rh
                   ,descr_fil_rh
                   ,cod_cargo
                   ,cod_cracha
                   ,num_cpf_func
                   ,nome_func
                   ,cod_sit_rh
                   ,descr_sit_rh
                   ,dt_ini_sit_rh
                   ,cod_emp_rh
                   ,descr_emp_rh
                   ,cod_ccst_func
                   ,descr_ccst_func
                   ,cod_func_superior
                   ,cod_fun_avaliador
                   ,dt_admissao
                   ,dt_demissao
                   ,dt_ini_sit_srv
                   ,cod_usuario
                   ,cod_sit_rh_ant
                   ,descr_sit_rh_ant
                   ,dt_ini_sit_rh_ant
                   ,qtd_dias_trab_per
                   ,dt_promo_eleg
                   ,cod_mot_demissao
                   ,qtd_dias_afastam
                   ,flg_ccst_comercial
                   ,v_dt_atual
                   ,estrutura
                   ,estrutura_gestor
               from srv_funcionario
              where cod_func = r_func.cod_func;

          end if;

          v_aux := 0;

        exception
          when others then--Erro ao inserir historico

             v_aux := 1;

            v_msg := 'COD_FUNC: ' ||r_func.COD_FUNC ||' - ERRO AO INSERIR HISTORICO DO FUNCIONARIO - ' ||sqlerrm;
            prc_carga_erro(p_processo => v_processo
                          ,p_msg_erro => v_msg
                          ,p_retorno  => v_retorno);

            update srv_funcionario_apdata
               set st_carga = 2
             where to_number(con_cdicontratado) = r_func.COD_FUNC
               and st_carga = 0;


        end;


        if v_aux <> 1 then
          begin
            insert into srv_funcionario
                       (cod_func
                       ,cod_emp
                       ,cod_fil
                       ,cod_fil_rh
                       ,descr_fil_rh
                       ,cod_cargo
                       ,cod_cracha
                       ,num_cpf_func
                       ,nome_func
                       ,cod_sit_rh
                       ,descr_sit_rh
                       ,dt_ini_sit_rh
                       ,cod_emp_rh
                       ,descr_emp_rh
                       ,cod_ccst_func
                       ,descr_ccst_func
                       ,cod_func_superior
                       ,cod_fun_avaliador
                       ,dt_admissao
                       ,dt_demissao
                       ,dt_ini_sit_srv
                       ,cod_usuario
                       ,cod_sit_rh_ant
                       ,descr_sit_rh_ant
                       ,dt_ini_sit_rh_ant
                       ,qtd_dias_trab_per
                       ,dt_promo_eleg
                       ,cod_mot_demissao
                       ,qtd_dias_afastam
                       ,flg_ccst_comercial
                       ,estrutura
                       ,estrutura_gestor)
                 values(r_func.cod_func
                       ,r_func.cod_emp
                       ,r_func.cod_fil
                       ,r_func.cod_fil_rh
                       ,r_func.descr_fil_rh
                       ,r_func.cod_cargo
                       ,r_func.cod_cracha
                       ,r_func.num_cpf_func
                       ,r_func.nome_func
                       ,r_func.cod_sit_rh
                       ,r_func.descr_sit_rh
                       ,r_func.dt_ini_sit_rh
                       ,r_func.cod_emp_rh
                       ,r_func.descr_emp_rh
                       ,r_func.cod_ccst_func
                       ,r_func.descr_ccst_func
                       ,r_func.cod_func_superior
                       ,r_func.cod_fun_avaliador
                       ,r_func.dt_admissao
                       ,r_func.dt_demissao
                       ,v_dt_atual
                       ,r_func.cod_usuario
                       ,r_func.cod_sit_rh_ant
                       ,r_func.descr_sit_rh_ant
                       ,r_func.dt_ini_sit_rh_ant
                       ,r_func.qtd_dias_trab_per
                       ,r_func.dt_promo_eleg
                       ,r_func.cod_mot_demissao
                       ,r_func.qtd_dias_afastam
                       ,r_func.flg_ccst_comercial
                       ,r_func.estrutura
                       ,r_func.estrutura_gestor);

            update srv_funcionario_apdata
               set st_carga = 1
             where to_number(con_cdicontratado) = r_func.COD_FUNC
               and st_carga in(0,2);

          exception
            --Exception: dup_val_on_index
            when dup_val_on_index then
              begin
                update srv_funcionario
                   set cod_emp            = r_func.cod_emp
                      ,cod_fil            = r_func.cod_fil
                      ,cod_fil_rh         = r_func.cod_fil_rh
                      ,descr_fil_rh       = r_func.descr_fil_rh
                      ,cod_cargo          = r_func.cod_cargo
                      ,cod_cracha         = r_func.cod_cracha
                      ,num_cpf_func       = r_func.num_cpf_func
                      ,nome_func          = r_func.nome_func
                      ,cod_sit_rh         = r_func.cod_sit_rh
                      ,descr_sit_rh       = r_func.descr_sit_rh
                      ,dt_ini_sit_rh      = r_func.dt_ini_sit_rh
                      ,cod_emp_rh         = r_func.cod_emp_rh
                      ,descr_emp_rh       = r_func.descr_emp_rh
                      ,cod_ccst_func      = r_func.cod_ccst_func
                      ,descr_ccst_func    = r_func.descr_ccst_func
                      ,cod_func_superior  = r_func.cod_func_superior
                      ,cod_fun_avaliador  = r_func.cod_fun_avaliador
                      ,dt_admissao        = r_func.dt_admissao
                      ,dt_demissao        = r_func.dt_demissao
                      ,dt_ini_sit_srv     = v_dt_atual
                      ,cod_usuario        = r_func.cod_usuario
                      ,cod_sit_rh_ant     = r_func.cod_sit_rh_ant
                      ,descr_sit_rh_ant   = r_func.descr_sit_rh_ant
                      ,dt_ini_sit_rh_ant  = r_func.dt_ini_sit_rh_ant
                      ,qtd_dias_trab_per  = r_func.qtd_dias_trab_per
                      ,dt_promo_eleg      = r_func.dt_promo_eleg
                      ,cod_mot_demissao   = r_func.cod_mot_demissao
                      ,qtd_dias_afastam   = r_func.qtd_dias_afastam
                      ,flg_ccst_comercial = r_func.flg_ccst_comercial
                      ,estrutura          = r_func.estrutura
                      ,estrutura_gestor   = r_func.estrutura_gestor
                 where cod_func           = r_func.cod_func;

               update srv_funcionario_apdata
                  set st_carga = 1
                where to_number(con_cdicontratado) = r_func.COD_FUNC
                  and st_carga in(0,2);

              exception
                when others then--Erro ao atualizar
                  v_msg := 'COD_FUNC: ' ||r_func.COD_FUNC ||' - ERRO AO ATUALIZAR DADOS DO FUNCIONARIO - '||sqlerrm;
                  prc_carga_erro(p_processo => v_processo
                                  ,p_msg_erro => v_msg
                                  ,p_retorno  => v_retorno);

                  update srv_funcionario_apdata
                     set st_carga = 2
                   where to_number(con_cdicontratado) = r_func.COD_FUNC
                     and st_carga = 0;
              end;

            --Exception: Others
            when others then--Erro ao inserir
              v_msg := 'COD_FUNC: ' ||r_func.COD_FUNC ||' - ERRO AO INSERIR DADOS DO FUNCIONARIO - '||sqlerrm;
              prc_carga_erro(p_processo => v_processo
                            ,p_msg_erro => v_msg
                            ,p_retorno => v_retorno);

              update srv_funcionario_apdata
                 set st_carga = 2
               where to_number(con_cdicontratado) = r_func.COD_FUNC
                 and st_carga = 0;
          end;

        end if;

        v_aux := 0;

        --Commit Parcial
        v_commit := v_commit + 1;

        if mod(v_commit,1000) = 0 then
          commit;
        end if;

      end loop;

      --Cria tablela temporaria de funcionarios
      v_nm_tab_realz_fu  := 'tmp_funcionario_'
                           ||trim(to_char(v_num_ano,'0000'))
                           ||trim(to_char(v_num_mes,'00'));

      begin
        execute immediate 'drop table '||v_nm_tab_realz_fu;
      exception
        when others then
          null;
      end;
      --
      begin
        execute immediate'
          create table '||v_nm_tab_realz_fu||' as
            select *
              from srv_funcionario
             where dt_admissao <= last_day(to_date(''01/''||trim(to_char('||v_num_mes||',''00''))||''/''||trim(to_char('||v_num_ano||',''0000''))||''23:59:59'',''dd/mm/yyyyhh24:mi:ss''))
               and(dt_demissao is null
                or dt_demissao > to_date(''01/''||trim(to_char('||v_num_mes||',''00''))||''/''||trim(to_char('||v_num_ano||',''0000''))||''00:00:00'',''dd/mm/yyyyhh24:mi:ss''))
               and(cod_sit_rh = 1 or nvl(qtd_dias_trab_per,0) > 0
                or(cod_sit_rh = 9
                or(nvl(cod_sit_rh_ant,1) = 9
               and nvl(dt_ini_sit_rh,to_date(''01/''||trim(to_char('||v_num_mes||',''00''))||''/''||trim(to_char('||v_num_ano||',''0000''))||''00:00:00'',''dd/mm/yyyyhh24:mi:ss'')) >=
                                     to_date(''02/''||trim(to_char('||v_num_mes||',''00''))||''/''||trim(to_char('||v_num_ano||',''0000''))||''00:00:00'',''dd/mm/yyyyhh24:mi:ss''))))';
      exception
        when others then
          null;
      end;
      --
      begin
        execute immediate 'grant select on '||v_nm_tab_realz_fu||' to SRV_SEL';
      exception
        when others then
          null;
      end;
      --
      begin
        update srv_agendamento sa
           set sa.dt_agendamento = to_date('07'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
             , sa.dt_ult_proc    = trunc(sysdate)
             , sa.dt_limite_proc = to_date('10'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
             , sa.num_ano        = to_char(sysdate,'yyyy')
             , sa.num_mes        = to_char(sysdate,'mm')
             , sa.num_arq        = v_num_arq
         where sa.cod_carga      = v_cod_carga;

      exception
        when others then
          null;
      end;

    end if;

    commit;

  exception
    when others then
      v_msg := 'FALHA NA EXECUCAO DA PROCEDURE PRC_CARREGA_FUNCIONARIO - '||sqlerrm;
      prc_carga_erro(p_processo => v_processo
                    ,p_msg_erro => v_msg
                    ,p_retorno  => v_retorno);
      rollback;

  end prc_carrega_funcionario;

  --
  procedure prc_carrega_meta is

    v_num_mes     srv_agendamento.num_mes%type;
    v_num_ano     srv_agendamento.num_ano%type;
    v_num_arq     srv_agendamento.num_arq%type;
    v_nome_arq    srv_agendamento.nome_arq%type;
    v_dir_origem  srv_agendamento.dir_origem%type;
    v_dir_destino srv_agendamento.dir_destino%type;
    v_retorno     number;
    v_cod_carga   srv_agendamento.cod_carga%type := 1;
    v_cod_fil_ini srv_meta_filial.cod_fil%type := 1;
    v_cod_fil_fim srv_meta_filial.cod_fil%type := 999;
    v_msg         varchar2(255);

  begin

    prc_consulta_agendamento(p_cod_carga   => v_cod_carga
                           , p_num_ano     => v_num_ano
                           , p_num_mes     => v_num_mes
                           , p_num_arq     => v_num_arq
                           , p_nome_arq    => v_nome_arq
                           , p_dir_origem  => v_dir_origem
                           , p_dir_destino => v_dir_destino
                           , p_retorno     => v_retorno
                             );
    if v_retorno = 0 then
      begin
        insert into srv_meta_filial_hist
                  (  cod_indic
                   , cod_emp
                   , cod_fil
                   , num_ano
                   , num_mes
                   , seq_meta
                   , num_meta
                   , cod_un_meta
                   , vlr_premio_fil
                   , dt_ini_sit_srv
                   , cod_usuario
                   , pct_calc_meta
                   , dt_inclusao
                   )
        (select a.cod_indic,
                a.cod_emp,
                a.cod_fil,
                a.num_ano,
                a.num_mes,
                nvl(hist.seq,1) seq,
                a.num_meta,
                a.cod_un_meta,
                a.vlr_premio_fil,
                a.dt_ini_sit_srv,
                a.cod_usuario,
                a.pct_calc_meta,
                sysdate
           from srv_meta_filial a
              , srv_indicador b
              , srv_filial c
              , (select smfh.num_ano
                      , smfh.num_mes
                      , smfh.cod_indic
                      , smfh.cod_emp
                      , smfh.cod_fil
                      , nvl(max(smfh.seq_meta),0) + 1 seq
                   from srv_meta_filial_hist smfh
                  group by smfh.num_ano, smfh.num_mes,smfh.cod_indic, smfh.cod_emp, smfh.cod_fil
                  order by smfh.num_ano, smfh.num_mes,smfh.cod_indic, smfh.cod_emp, smfh.cod_fil
                ) hist
          where a.cod_indic = b.cod_indic
            and a.cod_emp   = c.cod_emp
            and a.cod_fil   = c.cod_fil
            and a.num_ano   = hist.num_ano (+)
            and a.num_mes   = hist.num_mes (+)
            and a.cod_indic = hist.cod_indic (+)
            and a.cod_emp   = hist.cod_emp (+)
            and a.cod_fil   = hist.cod_fil (+)
            and a.cod_indic = 1
            and a.num_ano   = v_num_ano
            and a.num_mes   = v_num_mes
            and a.cod_fil between v_cod_fil_ini and v_cod_fil_fim
        );
      exception
         when others then
         -- tratar erro
         null;
      end;
     -- Deletar da tabela (srv_meta_filial)
      begin
        delete from srv_meta_filial smf
         where smf.num_ano   = v_num_ano
           and smf.num_mes   = v_num_mes
           and smf.cod_indic = 1
           and smf.cod_fil between v_cod_fil_ini and v_cod_fil_fim;
      end;
     --- METAS POR FILIAL VENDAS
      begin
        insert into srv_meta_filial
                  ( cod_indic
                  , cod_emp
                  , cod_fil
                  , num_ano
                  , num_mes
                  , num_meta
                  , cod_un_meta
                  , vlr_premio_fil
                  , dt_ini_sit_srv
                  , cod_usuario
                  , pct_calc_meta
                        )
             select smc.cod_indic
                  , 1 cod_emp
                  , smc.fil_cod
                  , smc.num_ano
                  , smc.num_mes
                  , smc.vlr_meta
                  , smc.cod_unid_meta
                  , smc.vlr_premio
                  , sysdate dt_ini_sit_srv
                  , 1 cod_usuario
                  , null
               from srv_metas_carga smc
              where smc.num_ano = v_num_ano
                and smc.num_mes = v_num_mes
                and smc.st_carga = 0;
      exception
        when others then
          v_msg := 'Erro no processamento do arquivo '||v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0);
          prc_carga_erro (p_processo => upper('prc_carrega_meta')
                         ,p_msg_erro => v_msg
                         ,p_retorno  => v_retorno);

          begin
            v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                  p_subject => 'SRV',
                                                                  p_body    => v_msg);
          exception
            when others then
              dbms_output.put_line(v_retorno);
          end;

          v_retorno := 1;
      end;
      --
      if v_retorno = 0 then
        --
        begin
          v_msg := 'O processo PRC_CARREGA_META (CARGA ' || v_num_ano || lpad(v_num_mes, 2, 0)||') foi executado as '||to_char(sysdate,'dd/mm/yyyy hh24:mi:ss');

          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);
        exception
          when others then
          dbms_output.put_line(v_retorno ||' - Ocorreu o erro '|| sqlerrm);
        end;
        --
        --
        begin
          update srv_metas_carga smc
             set smc.st_carga = 1
           where smc.num_ano  = v_num_ano
             and smc.num_mes  = v_num_mes
             and smc.st_carga = 0;
        exception
          when others then
            null;
        end;
        --
        begin
          insert into srv_agendamento_log
               ( cod_carga
               , interface
               , num_ano
               , num_mes
               , num_arq
               , dt_inclusao)
          values
               ( v_cod_carga
               , v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)
               , v_num_ano
               , v_num_mes
               , v_num_arq
               , sysdate);
        exception
          when others then
            null;
        end;
        --
        begin
          update srv_agendamento sa
             set sa.dt_agendamento = to_date('07'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
               , sa.dt_ult_proc    = trunc(sysdate)
               , sa.dt_limite_proc = to_date('10'||to_char(add_months(sysdate,1),'mmyyyy'),'ddmmyyyy')
               , sa.num_ano        = to_char(sysdate,'yyyy')
               , sa.num_mes        = to_char(sysdate,'mm')
               , sa.num_arq        = v_num_arq
           where sa.cod_carga      = v_cod_carga;

        exception
          when others then
            null;
        end;
        --
      end if;
     --
    end if;

  end prc_carrega_meta;
  --

  procedure prc_agrupa_filial_vm is

    arquivo_ler   utl_file.file_type;
    linha         varchar2(2000);
    v_num_mes     srv_agendamento.num_mes%type;
    v_num_ano     srv_agendamento.num_ano%type;
    v_num_arq     srv_agendamento.num_arq%type;
    v_nome_arq    srv_agendamento.nome_arq%type;
    v_dir_origem  srv_agendamento.dir_origem%type;
    v_dir_destino srv_agendamento.dir_destino%type;
    v_cod_fil     srv_func_base_rem_var.cod_fil%type;
    v_cod_func    srv_func_base_rem_var.cod_func%type;
    v_cod_carga   srv_agendamento.cod_carga%type := 6;
    v_retorno     number;
    v_msg         varchar2(255);

  BEGIN

    --Variaveis
    pkg_srv_carga_base.prc_consulta_agendamento(p_cod_carga   => v_cod_carga,
                                                p_num_ano     => v_num_ano,
                                                p_num_mes     => v_num_mes,
                                                p_num_arq     => v_num_arq,
                                                p_nome_arq    => v_nome_arq,
                                                p_dir_origem  => v_dir_origem,
                                                p_dir_destino => v_dir_destino,
                                                p_retorno     => v_retorno);

    if v_retorno = 0 then

      begin
        delete from srv_func_base_rem_var
         where cod_func in(select a.cod_func
                             from srv_func_base_rem_var       a
                            inner join srv_funcionario        b on b.cod_func = a.cod_func
                            inner join srv_grupo_cargo        c on c.cod_cargo = b.cod_cargo
                            inner join srv_grupo_rem_variavel d on d.cod_grp_rem_var = c.cod_grp_rem_var
                                                               and d.descr_grp_rem_var = 'VISUAL_MERCHANDISING_LOJA'
                            where a.num_ano = v_num_ano
                              and a.num_mes = v_num_mes)
           and num_ano = v_num_ano
           and num_mes = v_num_mes;
      exception
        when others then
          null;
      end;

      v_nome_arq := v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0) || '.txt';

      arquivo_ler := UTL_File.Fopen(v_dir_origem, v_nome_arq, 'r');

      Loop
        begin

          UTL_File.Get_Line(arquivo_ler, Linha);

       linha := replace(replace(replace(linha,CHR(10),''),CHR(13),''),CHR(9),'');

       v_cod_fil := trim(substr(Linha||';', instr(Linha||';', ';', 1, 2) + 1,
                           instr(Linha||';', ';', 1, 3) -
                           instr(Linha||';', ';', 1, 2) - 1));

       v_cod_func := trim(substr(linha, instr(linha, ';', 1, 1) + 1,
                           instr(linha, ';', 1, 2) -
                           instr(linha, ';', 1, 1) - 1));


       insert into srv_func_base_rem_var
         (cod_emp
         ,cod_fil
         ,cod_func
         ,num_ano
         ,num_mes
         ,vlr_sal_base_rem_var
         ,dt_ini_sit_srv
         ,cod_usuario)
       values
         (1
         ,v_cod_fil
         ,v_cod_func
         ,v_num_ano
         ,v_num_mes
         ,null
         ,sysdate
         ,1);


        exception
          when others then
            exit;
            v_retorno := 1;
        end;

      End Loop;

      if v_retorno = 0 then

        begin
          update srv_agendamento sa
             set sa.dt_agendamento = to_date('07' || to_char(add_months(sysdate, 1),'mmyyyy'),'ddmmyyyy'),
                 sa.dt_ult_proc    = trunc(sysdate),
                 sa.dt_limite_proc = to_date('10' || to_char(add_months(sysdate, 1),'mmyyyy'),'ddmmyyyy'),
                 sa.num_ano        = to_char(sysdate, 'yyyy'),
                 sa.num_mes        = to_char(sysdate, 'mm'),
                 sa.num_arq        = v_num_arq
           where sa.cod_carga = v_cod_carga;

        exception
          when others then
            null;
        end;

        begin
          pkg_ccm_func_proc_geral.prc_shell_move(p_var_dir_origem      => v_dir_origem,
                                                 p_var_dir_destino     => v_dir_destino,
                                                 p_var_arquivo_origem  => v_nome_arq,
                                                 p_var_arquivo_destino => v_nome_arq,
                                                 p_bln_decript         => false,
                                                 p_var_erro            => v_retorno);
        exception
          when others then
            null;
        end;

        begin
          insert into srv_agendamento_log
            (cod_carga
            ,interface
            ,num_ano
            ,num_mes
            ,num_arq
            ,dt_inclusao)
          values
            (v_cod_carga
            ,v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0)
            ,v_num_ano
            ,v_num_mes
            ,v_num_arq
            ,sysdate);
        exception
          when others then
            null;
        end;

      elsif v_retorno = 1 then

        begin
          v_msg := 'O arquivo ' || v_nome_arq || '_' || v_num_ano ||lpad(v_num_mes, 2, 0) || ' nao foi encontrado.';

          PKG_SRV_CARGA_BASE.prc_carga_erro(p_processo => upper('prc_carrega_digitalizado_pl'),
                                            p_msg_erro => v_msg,
                                            p_retorno  => v_retorno);

          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);

          update srv_agendamento sa
             set sa.dt_agendamento = sa.dt_agendamento + 1,
                 sa.dt_limite_proc = sa.dt_limite_proc + 1
           where sa.cod_carga = v_cod_carga;

        exception
          when others then
            dbms_output.put_line('Retorno: ' || v_retorno || '/ Erro: ' ||
                                 SQLERRM);
        end;

      end if;

    end if;

    commit;
    UTL_File.Fclose(arquivo_ler);
    Dbms_Output.Put_Line('Arquivo processado com sucesso.');
  EXCEPTION
    WHEN Others THEN
      Dbms_Output.Put_Line('Problemas na leitura do arquivo.' || Sqlerrm);
      UTL_File.Fclose(arquivo_ler);
  END;


  --
  procedure prc_lojas_lider is

    arquivo_ler   UTL_File.File_Type;
    Linha         Varchar2(2000);
    v_num_mes     srv_agendamento.num_mes%type;
    v_num_ano     srv_agendamento.num_ano%type;
    v_num_arq     srv_agendamento.num_arq%type;
    v_nome_arq    srv_agendamento.nome_arq%type;
    v_dir_origem  srv_agendamento.dir_origem%type;
    v_dir_destino srv_agendamento.dir_destino%type;
    v_cod_fil     srv_func_base_rem_var.cod_fil%type;
    v_cod_func    srv_func_base_rem_var.cod_func%type;
    v_cod_carga   srv_agendamento.cod_carga%type := 5;
    v_retorno     number;
    v_msg         varchar2(255);

  BEGIN

    --Variaveis
    PKG_SRV_CARGA_BASE.prc_consulta_agendamento(p_cod_carga   => v_cod_carga,
                                                p_num_ano     => v_num_ano,
                                                p_num_mes     => v_num_mes,
                                                p_num_arq     => v_num_arq,
                                                p_nome_arq    => v_nome_arq,
                                                p_dir_origem  => v_dir_origem,
                                                p_dir_destino => v_dir_destino,
                                                p_retorno     => v_retorno);

    if v_retorno = 0 then

      begin
        delete from srv_func_base_rem_var
         where cod_func in(select a.cod_func
                             from srv_func_base_rem_var       a
                            inner join srv_funcionario        b on b.cod_func = a.cod_func
                            inner join srv_grupo_cargo        c on c.cod_cargo = b.cod_cargo
                            inner join srv_grupo_rem_variavel d on d.cod_grp_rem_var = c.cod_grp_rem_var
                                                               and d.descr_grp_rem_var = 'LIDERANCA_LOJA'
                            where a.num_ano = v_num_ano
                              and a.num_mes = v_num_mes)
           and num_ano = v_num_ano
           and num_mes = v_num_mes;
      exception
        when others then
          null;
      end;

      v_nome_arq := v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0) || '.txt';

      arquivo_ler := UTL_File.Fopen(v_dir_origem, v_nome_arq, 'r');

      Loop
        begin

          UTL_File.Get_Line(arquivo_ler, Linha);

       linha := replace(replace(replace(linha,CHR(10),''),CHR(13),''),CHR(9),'');

       v_cod_fil := trim(substr(Linha||';', instr(Linha||';', ';', 1, 2) + 1,
                           instr(Linha||';', ';', 1, 3) -
                           instr(Linha||';', ';', 1, 2) - 1));


       v_cod_func := trim(substr(linha, instr(linha, ';', 1, 1) + 1,
                           instr(linha, ';', 1, 2) -
                           instr(linha, ';', 1, 1) - 1));


       insert into Srv_Func_Base_Rem_Var (cod_emp,cod_fil,cod_func, num_ano, num_mes, vlr_sal_base_rem_var, dt_ini_sit_srv,cod_usuario)
       values (1,v_cod_fil,v_cod_func,v_num_ano,v_num_mes,null,sysdate,1);


        exception
          when others then
            exit;
            v_retorno := 1;
        end;

      End Loop;

      if v_retorno = 0 then

        begin
          update srv_agendamento sa
             set sa.dt_agendamento = to_date('07' || to_char(add_months(sysdate, 1),'mmyyyy'),'ddmmyyyy'),
                 sa.dt_ult_proc    = trunc(sysdate),
                 sa.dt_limite_proc = to_date('10' || to_char(add_months(sysdate, 1),'mmyyyy'),'ddmmyyyy'),
                 sa.num_ano        = to_char(sysdate, 'yyyy'),
                 sa.num_mes        = to_char(sysdate, 'mm'),
                 sa.num_arq        = v_num_arq
           where sa.cod_carga = v_cod_carga;

        exception
          when others then
            null;
        end;

        begin
          pkg_ccm_func_proc_geral.prc_shell_move(p_var_dir_origem      => v_dir_origem,
                                                 p_var_dir_destino     => v_dir_destino,
                                                 p_var_arquivo_origem  => v_nome_arq,
                                                 p_var_arquivo_destino => v_nome_arq,
                                                 p_bln_decript         => false,
                                                 p_var_erro            => v_retorno);
        exception
          when others then
            null;
        end;

        begin
          insert into srv_agendamento_log
            (cod_carga, interface, num_ano, num_mes, num_arq, dt_inclusao)
          values
            (v_cod_carga,
             v_nome_arq || '_' || v_num_ano || lpad(v_num_mes, 2, 0),
             v_num_ano,
             v_num_mes,
             v_num_arq,
             sysdate);
        exception
          when others then
            null;
        end;

      elsif v_retorno = 1 then

        begin
          v_msg := 'O arquivo ' || v_nome_arq || '_' || v_num_ano ||lpad(v_num_mes, 2, 0) || ' nao foi encontrado.';

          PKG_SRV_CARGA_BASE.prc_carga_erro(p_processo => upper('prc_carrega_digitalizado_pl'),
                                            p_msg_erro => v_msg,
                                            p_retorno  => v_retorno);

          v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202,
                                                                p_subject => 'SRV',
                                                                p_body    => v_msg);

          update srv_agendamento sa
             set sa.dt_agendamento = sa.dt_agendamento + 1,
                 sa.dt_limite_proc = sa.dt_limite_proc + 1
           where sa.cod_carga = v_cod_carga;

        exception
          when others then
            dbms_output.put_line('Retorno: ' || v_retorno || '/ Erro: ' ||
                                 SQLERRM);
        end;

      end if;

    end if;

    commit;
    UTL_File.Fclose(arquivo_ler);
    Dbms_Output.Put_Line('Arquivo processado com sucesso.');
  EXCEPTION
    WHEN Others THEN
      Dbms_Output.Put_Line('Problemas na leitura do arquivo.' || Sqlerrm);
      UTL_File.Fclose(arquivo_ler);
  END;

  --##############################################################
  procedure prc_metas_psf is

    --srv_meta_filial
    v_cod_fil        srv_meta_filial.cod_fil%type;
    v_cod_indic      srv_meta_filial.cod_indic%type;
    v_num_meta       srv_meta_filial.num_meta%type;
    v_cod_un_meta    srv_meta_filial.cod_un_meta%type;
    v_vlr_premio_fil srv_meta_filial.vlr_premio_fil%type;

    --srv_agendamento
    v_num_mes        srv_agendamento.num_mes%type;
    v_num_ano        srv_agendamento.num_ano%type;
    v_nome_arq       srv_agendamento.nome_arq%type;
    v_dir_origem     srv_agendamento.dir_origem%type;
    v_dir_destino    srv_agendamento.dir_destino%type;

    --others
    arquivo_ler      utl_file.file_type;
    linha            varchar2(2000);
    v_msg            varchar2(255);
    v_retorno        number;
    v_count          number := 1;

    --exception
    e_erro exception;
    pragma exception_init(e_erro, -02291);

  begin

    select nome_arq, dir_origem, dir_destino
      into v_nome_arq, v_dir_origem, v_dir_destino
      from srv_agendamento
     where cod_carga = 2
       and flg_ativa = 1;

    arquivo_ler := utl_file.fopen(v_dir_origem,v_nome_arq||'.txt','r');

    Loop
      begin
        utl_file.get_line(arquivo_ler, linha);

        linha := replace(replace(replace(linha,chr(10),''),chr(13),''),chr(9),'');

        --delete dados anteriores
        begin

          if v_count = 1 then

            delete from srv_meta_filial
             where num_ano = substr(linha,4,4)
               and num_mes = substr(linha,1,2)
               and cod_indic in(select cod_indic
                                  from srv_indicador
                                 where(cod_grp_indic = 3
                                   and cod_indic_sis is null)
                                    or cod_indic_sis = 'SAX_PSF');

            commit;
            v_count := 2;

          end if;

        exception
          when others then
            v_retorno := 1;
            rollback;
            exit;
        end;

        --num_mes
        v_num_mes :=        substr(linha,1,2);

        --num_ano
        v_num_ano :=        substr(linha,4,4);

        --cod_fil
        v_cod_fil :=        trim(substr(linha,
                            instr(linha, ';', 1, 1) + 1,
                            instr(linha, ';', 1, 2) -
                            instr(linha, ';', 1, 1) - 1));

        --cod_indic
        v_cod_indic :=      trim(substr(linha||';',
                            instr(linha||';', ';', 1, 2) + 1,
                            instr(linha||';', ';', 1, 3) -
                            instr(linha||';', ';', 1, 2) - 1));

        --cod_un_meta
        v_cod_un_meta :=    trim(substr(linha||';',
                            instr(linha||';', ';', 1, 3) + 1,
                            instr(linha||';', ';', 1, 4) -
                            instr(linha||';', ';', 1, 3) - 1));

        --num_mes
        v_num_meta :=       replace(trim(substr(linha||';',
                            instr(linha||';', ';', 1, 4) + 1,
                            instr(linha||';', ';', 1, 5) -
                            instr(linha||';', ';', 1, 4) - 1)),'.',',');

        --vlr_premio_fil
        v_vlr_premio_fil := trim(substr(linha||';',
                            instr(linha||';', ';', 1, 5) + 1,
                            instr(linha||';', ';', 1, 6) -
                            instr(linha||';', ';', 1, 5) - 1));
        --
        insert into srv_meta_filial
          (cod_indic
          ,cod_emp
          ,cod_fil
          ,num_ano
          ,num_mes
          ,num_meta
          ,cod_un_meta
          ,vlr_premio_fil
          ,dt_ini_sit_srv
          ,cod_usuario
          ,pct_calc_meta)
        values
          (v_cod_indic
          ,1
          ,v_cod_fil
          ,v_num_ano
          ,v_num_mes
          ,case when v_cod_un_meta <> 2 then v_num_meta else 0 end
          ,v_cod_un_meta
          ,v_vlr_premio_fil
          ,sysdate
          ,1
          ,case when v_cod_un_meta = 2 then v_num_meta else null end);

      exception
        --1 exception: dup_val_on_index
        when dup_val_on_index then
          begin
            update srv_meta_filial
               set num_meta       = case when v_cod_un_meta <> 2 then v_num_meta else 0 end
                  ,cod_un_meta    = v_cod_un_meta
                  ,vlr_premio_fil = v_vlr_premio_fil
                  ,dt_ini_sit_srv = sysdate
                  ,cod_usuario    = 1
                  ,pct_calc_meta  = case when v_cod_un_meta = 2 then v_num_meta else null end
             where cod_indic = v_cod_indic
               and cod_emp = 1
               and cod_fil = v_cod_fil
               and num_ano = v_num_ano
               and num_mes = v_num_mes;
          exception
            when others then
              v_retorno := 1;
              exit;
          end;

        --2 exception: no_data_found
        when no_data_found then
          v_retorno := 0;
          exit;

        when e_erro then
          null;

        --3 exception: others
        when others then
          v_retorno := 1;
          rollback;
          exit;
      end;
    end Loop;

    if v_retorno = 0 then
      begin
        pkg_ccm_func_proc_geral.prc_shell_move(p_var_dir_origem      => v_dir_origem
                                              ,p_var_dir_destino     => v_dir_destino
                                              ,p_var_arquivo_origem  => v_nome_arq||'.txt'
                                              ,p_var_arquivo_destino => v_nome_arq||'_'||v_num_ano||lpad(v_num_mes, 2, 0)||'.txt'
                                              ,p_bln_decript         => false
                                              ,p_var_erro            => v_retorno);

        --Envia e-mail
        v_msg := 'O arquivo ' || v_nome_arq || '_' || lpad(v_num_mes, 2, 0) || v_num_ano || ' foi processado com sucesso.';
        v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202, p_subject => 'SRV', p_body => v_msg);

      exception
        when others then
          null;
      end;

    elsif v_retorno = 1 then

      --Envia e-mail
      v_msg := 'Erro no processamento do arquivo ' || v_nome_arq || '_' || lpad(v_num_mes, 2, 0) || v_num_ano || ': ' || sqlerrm;
      v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202, p_subject => 'SRV', p_body => v_msg);

      --Registra erro
      pkg_srv_carga_base.prc_carga_erro(p_processo => upper('prc_metas_psf')
                                       ,p_msg_erro => v_msg
                                       ,p_retorno  => v_retorno);
    end if;

    --
    commit;
    utl_file.fclose(arquivo_ler);
    --
  exception
    when others then
      rollback;
      utl_file.fclose(arquivo_ler);
      dbms_output.put_line('Erro: '||sqlerrm);

  end prc_metas_psf;

  --
  procedure prc_ox_ativacao is

    --srv_ox_ativacao
    v_cli_cpf              srv_ox_ativacao.cli_cpf%type;
    v_cpf_vendedor         srv_ox_ativacao.cpf_vendedor%type;
    v_cpf_func_indicacao   srv_ox_ativacao.cpf_func_indicacao%type;
    v_data_aprovacao       srv_ox_ativacao.data_aprovacao%type;
    v_data_ativacao        srv_ox_ativacao.data_ativacao%type;
    v_cod_filial           srv_ox_ativacao.cod_filial%type;
    v_cod_filial_itau      srv_ox_ativacao.cod_filial_itau%type;
    v_canal                srv_ox_ativacao.canal%type;
    v_sub_canal            srv_ox_ativacao.sub_canal%type;
    v_flg_ativacao_voucher srv_ox_ativacao.flg_ativacao_voucher%type;
    v_flg_ativacao_cartao  srv_ox_ativacao.flg_ativacao_cartao%type;

    v_nome_arq       srv_agendamento.nome_arq%type;
    v_dir_origem     srv_agendamento.dir_origem%type;
    v_dir_destino    srv_agendamento.dir_destino%type;

    --others
    arquivo_ler      utl_file.file_type;
    linha            varchar2(2000);
    v_msg            varchar2(255);
    v_retorno        number;
    v_dt_atual       varchar2(10);

  begin

    select nome_arq, dir_origem, dir_destino
      into v_nome_arq, v_dir_origem, v_dir_destino
      from srv_agendamento
     where cod_carga = 9
       and flg_ativa = 1;

    select to_char(sysdate,'yyyymmdd')
      into v_dt_atual
      from dual;

    arquivo_ler := utl_file.fopen(v_dir_origem,v_nome_arq||'.txt','r');

    utl_file.get_line(arquivo_ler, linha);--Header

    Loop
      begin
        utl_file.get_line(arquivo_ler, linha);

        linha := replace(replace(replace(linha,chr(10),''),chr(13),''),chr(9),'');

        --cli_cpf
        v_cli_cpf :=              trim(substr(linha, 1,
                                  instr(linha, ';', 1, 1) -1));

        --cpf_vendedor
        v_cpf_vendedor :=         trim(substr(linha,
                                  instr(linha, ';', 1, 1) + 1,
                                  instr(linha, ';', 1, 2) -
                                  instr(linha, ';', 1, 1) - 1));
        --cpf_func_indicacao
        v_cpf_func_indicacao :=   trim(substr(linha,
                                  instr(linha, ';', 1, 2) + 1,
                                  instr(linha, ';', 1, 3) -
                                  instr(linha, ';', 1, 2) - 1));

        --data_aprovacao
        v_data_aprovacao :=       trim(substr(linha,
                                  instr(linha, ';', 1, 3) + 1,
                                  instr(linha, ';', 1, 4) -
                                  instr(linha, ';', 1, 3) - 1));

        --data_ativacao
        v_data_ativacao :=        trim(substr(linha,
                                  instr(linha, ';', 1, 4) + 1,
                                  instr(linha, ';', 1, 5) -
                                  instr(linha, ';', 1, 4) - 1));

        --cod_filial
        v_cod_filial :=           trim(substr(linha,
                                  instr(linha, ';', 1, 5) + 1,
                                  instr(linha, ';', 1, 6) -
                                  instr(linha, ';', 1, 5) - 1));

        --cod_filial_itau
        v_cod_filial_itau :=      trim(substr(linha,
                                  instr(linha, ';', 1, 6) + 1,
                                  instr(linha, ';', 1, 7) -
                                  instr(linha, ';', 1, 6) - 1));

        --canal
        v_canal :=                trim(substr(linha,
                                  instr(linha, ';', 1, 7) + 1,
                                  instr(linha, ';', 1, 8) -
                                  instr(linha, ';', 1, 7) - 1));

        --sub_canal
        v_sub_canal :=            trim(substr(linha,
                                  instr(linha, ';', 1, 8) + 1,
                                  instr(linha, ';', 1, 9) -
                                  instr(linha, ';', 1, 8) - 1));

        --flg_ativacao_voucher
        v_flg_ativacao_voucher := trim(substr(linha,
                                  instr(linha, ';', 1, 9) + 1,
                                  instr(linha, ';', 1, 10) -
                                  instr(linha, ';', 1, 9) - 1));

        --flg_ativacao_cartao
        v_flg_ativacao_cartao :=  trim(substr(linha||';',
                                  instr(linha||';', ';', 1, 10) + 1,
                                  instr(linha||';', ';', 1, 11) -
                                  instr(linha||';', ';', 1, 10) - 1));
        --
        insert into srv_ox_ativacao
          (cli_cpf
          ,cpf_vendedor
          ,cpf_func_indicacao
          ,data_aprovacao
          ,data_ativacao
          ,dt_ini_sit_srv
          ,cod_filial
          ,cod_filial_itau
          ,canal
          ,sub_canal
          ,flg_ativacao_voucher
          ,flg_ativacao_cartao)
        values
          (v_cli_cpf
          ,v_cpf_vendedor
          ,v_cpf_func_indicacao
          ,v_data_aprovacao
          ,v_data_ativacao
          ,sysdate
          ,v_cod_filial
          ,v_cod_filial_itau
          ,v_canal
          ,v_sub_canal
          ,v_flg_ativacao_voucher
          ,v_flg_ativacao_cartao);

      exception
        --1 exception: dup_val_on_index
        when dup_val_on_index then
          begin
            update srv_ox_ativacao
               set cpf_vendedor         = v_cpf_vendedor
                  ,cpf_func_indicacao   = v_cpf_func_indicacao
                  ,data_aprovacao       = v_data_aprovacao
                  ,data_ativacao        = v_data_ativacao
                  ,dt_ini_sit_srv       = sysdate
                  ,cod_filial           = v_cod_filial
                  ,cod_filial_itau      = v_cod_filial_itau
                  ,canal                = v_canal
                  ,sub_canal            = v_sub_canal
                  ,flg_ativacao_voucher = v_flg_ativacao_voucher
                  ,flg_ativacao_cartao  = v_flg_ativacao_cartao
             where cli_cpf = v_cli_cpf;
          exception
            when others then
              v_retorno := 1;
              exit;
          end;

        --2 exception: no_data_found
        when no_data_found then
          v_retorno := 0;
          exit;

        --3 exception: others
        when others then
          v_retorno := 1;
          rollback;
          exit;
      end;
    end Loop;

    if v_retorno = 0 then
      begin
        pkg_ccm_func_proc_geral.prc_shell_move(p_var_dir_origem      => v_dir_origem
                                              ,p_var_dir_destino     => v_dir_destino
                                              ,p_var_arquivo_origem  => v_nome_arq||'.txt'
                                              ,p_var_arquivo_destino => v_nome_arq||'_'||v_dt_atual||'.txt'
                                              ,p_bln_decript         => false
                                              ,p_var_erro            => v_retorno);

        --Envia e-mail
        v_msg := 'O arquivo ' || v_nome_arq || '_' || v_dt_atual ||' foi processado com sucesso.';
        v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202, p_subject => 'SRV', p_body => v_msg);

      exception
        when others then
          null;
      end;

    elsif v_retorno = 1 then

      --Envia e-mail
      v_msg := 'Erro no processamento do arquivo ' || v_nome_arq || '_' || v_dt_atual || ': ' || sqlerrm;
      v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202, p_subject => 'SRV', p_body => v_msg);

      --Registra erro
      pkg_srv_carga_base.prc_carga_erro(p_processo => upper('prc_metas_psf')
                                       ,p_msg_erro => v_msg
                                       ,p_retorno  => v_retorno);
    end if;

    --
    commit;
    utl_file.fclose(arquivo_ler);
    --
  exception
    when others then
      rollback;
      utl_file.fclose(arquivo_ler);
      dbms_output.put_line('Erro: '||sqlerrm);

  end prc_ox_ativacao;
  --
--
/*  procedure prc_gerente_filial is

    v_num_mes      number := extract(month from sysdate);
    v_num_ano      number := extract(year  from sysdate);
    v_num_mes_ant  number := extract(month from add_months(sysdate,-1));
    v_num_ano_ant  number := extract(year  from add_months(sysdate,-1));
    v_dia_processo number(2);
    v_retorno      number(1);
    v_msg          varchar2(255);
    e_erro         exception;

  begin

    begin
      select prm_vlr_prm_1
        into v_dia_processo
        from srv_parametros
       where prm_nome_processo = 'PRC_GERENTE_FILIAL'
         and prm_nome_prm = 'PRM_DIA_PROCESSAMENTO'
         and stat_cod = 1;
    exception
      when others then
        raise e_erro;
    end;

    if extract(day from sysdate) = 4\*v_dia_processo*\ then

      begin
        for i in(select cod_func
                       ,cod_atuacao
                       ,num_ano
                       ,num_mes
                       ,cod_fil
                       ,cod_fil_2
                       ,dt_incl_fil
                       ,cod_usuario
                       ,st_carga
                   from srv_gerente_filial
                  where num_ano = v_num_ano_ant
                    and num_mes = v_num_mes_ant)
        loop
          begin
            insert into srv_gerente_filial
              values(i.cod_func
                    ,i.cod_atuacao
                    ,v_num_ano
                    ,v_num_mes
                    ,i.cod_fil
                    ,i.cod_fil_2
                    ,i.dt_incl_fil
                    ,i.cod_usuario
                    ,i.st_carga);
          exception
            when dup_val_on_index then
              null;
          end;
        end loop;

        update srv_parametros
           set prm_vlr_prm_1 = v_num_ano
              ,prm_vlr_prm_2 = v_num_mes
         where prm_nome_processo = upper('PRC_GERENTE_FILIAL')
           and prm_nome_prm = upper('PRM_PERIODO_PROCESSO')
           and stat_cod = 1;

        commit;

      exception
        when others then
          raise e_erro;
      end;

      begin
        v_msg := upper('processo prc_gerente_filial executado com sucesso em '||sysdate);

        v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202
                                                             ,p_subject => upper('srv')
                                                             ,p_body    => v_msg);
      exception
        when others then
          raise e_erro;
      end;

      commit;

    end if;

  exception
    when e_erro then
      rollback;

      v_msg := 'Falha no processo prc_gerente_filial em '||sysdate;

      pkg_srv_carga_base.prc_carga_erro(p_processo => upper('prc_gerente_filial')
                                       ,p_msg_erro => v_msg
                                       ,p_retorno  => v_retorno);

      v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202
                                                           ,p_subject => upper('srv')
                                                           ,p_body    => v_msg);
    when others then
      rollback;

      v_msg := 'Falha no processo prc_gerente_filial em '||sysdate;

      pkg_srv_carga_base.prc_carga_erro(p_processo => upper('prc_gerente_filial')
                                       ,p_msg_erro => v_msg
                                       ,p_retorno  => v_retorno);

      v_retorno := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 202
                                                           ,p_subject => upper('srv')
                                                           ,p_body    => v_msg);

  end prc_gerente_filial;*/

end pkg_srv_carga_base;
/
