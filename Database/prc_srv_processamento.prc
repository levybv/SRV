create or replace procedure srv.prc_srv_processamento is

   -- processos de periodos abertos
   cursor c_proc_per is
      select c.num_ano
            ,c.num_mes
            ,c.sta_proc
            ,a.cod_proc
            ,a.ordem_proc
            ,a.nome_proc
        from srv_processo a
            ,srv_processo_per c
       where a.cod_proc = c.cod_proc
         and a.flg_ativ_proc = 'S'
         and c.sta_proc in (1,3,4) -- 1-Aberto / 3-Reaberto / 4-Fechar
    order by c.num_ano
            ,c.num_mes
            ,a.ordem_proc
            ,a.cod_proc;

   -- parametros do processo
   cursor c_proc_param (p_cod_proc   number) is
      select a.seq_param
            ,a.nome_param
            ,a.tp_param
            ,a.tp_ret_param
        from srv_processo_param           a
       where a.flg_ativ_param           = 'S'
         and a.cod_proc                 = p_cod_proc
      order by a.seq_param;


   -- sessoes do processo
   cursor c_proc_sess (p_cod_proc   number
                      ,p_num_ano    number
                      ,p_num_mes    number) is
      select seq_sess
            ,dt_sess
        from srv_processo_per_sess a
       where a.flg_ativ_sess     = 'S'
         and a.cod_proc          = p_cod_proc
         and a.num_ano           = p_num_ano
         and a.num_mes           = p_num_mes
    order by a.seq_sess;

/*   -- parametros da sessao do processo
   cursor c_proc_param_per_sess (p_cod_proc   number
                                ,p_seq_sess   number
                                ,p_num_ano    number
                                ,p_num_mes    number ) is
      select a.seq_param
            ,a.nome_param
            ,a.tp_param
            ,a.tp_ret_param
            ,b.cont_param
        from srv_processo_param           a
            ,srv_processo_param_per_sess  b
       where a.cod_proc                 = b.cod_proc
         and a.seq_param                = b.seq_param
         and a.flg_ativ_param           = 'S'
         and b.cod_proc                 = p_cod_proc
         and b.seq_sess                 = p_seq_sess
         and b.num_ano                  = p_num_ano
         and b.num_mes                  = p_num_mes
      order by b.seq_param;    */


   --
   e_erro                       exception;
   v_ins_log_erro               varchar2(4000);
   -- email
   v_body                       varchar2(4000);
   v_send_email                 number;

   --
   v_qtde_sessoes               pls_integer := 0;
   v_sqlcode                    number;
   v_sqlerrm                    varchar2(2000);
   v_erro                       varchar2(2000);
   v_dt_proc_ini                date;
   v_dt_proc_fim                date;
   v_sqlcode_exec               number;
   v_sqlerrm_exec               varchar2(2000);
   v_seq_sess                   srv_processo_per_sess.seq_sess%type;
   v_cont_param                 srv_processo_param_per_sess.cont_param%type;
   v_sta_proc_exec              srv_processo_exec.sta_proc_exec%type;
   v_fecha_proc                 boolean := false;



   ------------------------------------------------------------
   -- PRC_INS_PROC_EXEC : INSERE EXECUCAO DO PROCESSO
   ------------------------------------------------------------
   procedure prc_ins_proc_exec (p_cod_proc       in number
                               ,p_seq_sess       in number
                               ,p_num_ano        in number
                               ,p_num_mes        in number
                               ,p_dt_proc_ini    in date
                               ,p_dt_proc_fim    in date
                               ,p_sta_proc_exec  in varchar2
                               ,p_obs            in varchar2
                               ,p_sqlcode        out number
                               ,p_sqlerrm        out varchar2) is

   begin
      insert into srv_processo_exec
                 (cod_proc
                 ,seq_sess
                 ,num_ano
                 ,num_mes
                 ,dt_proc_ini
                 ,dt_proc_fim
                 ,sta_proc_exec
                 ,obs )
          values (p_cod_proc
                 ,p_seq_sess
                 ,p_num_ano
                 ,p_num_mes
                 ,p_dt_proc_ini
                 ,sysdate --p_dt_proc_fim
                 ,p_sta_proc_exec
                 ,p_obs);
      --
      commit;

   exception
      when dup_val_on_index then
         update srv_processo_exec
            set dt_proc_fim     = p_dt_proc_fim
               ,sta_proc_exec   = p_sta_proc_exec
               ,obs             = p_obs
          where cod_proc        = p_cod_proc
            and seq_sess        = p_seq_sess
            and num_ano         = p_num_ano
            and num_mes         = p_num_mes
            and dt_proc_ini     = p_dt_proc_ini;
         --
         commit;
      --
      when others then
         --
         p_sqlcode := sqlcode;
         p_sqlerrm := substr('Erro ao inserir nova execucao processo tabela srv_processo_exec - ' ||
                             ' cod_proc: '    || p_cod_proc ||
                             ' num_ano : '    || p_num_ano  ||
                             ' num_mes : '    || p_num_mes  || sqlerrm,1,2000);
   end;


   ------------------------------------------------------------
   -- prc_ins_processo_per_sess : INSERE SESSAO PARA O PERIODO
   ------------------------------------------------------------
   procedure prc_ins_proc_per_sess (p_cod_proc       in number
                                   ,p_seq_sess       out number
                                   ,p_num_ano        in number
                                   ,p_num_mes        in number
                                   ,p_dt_sess        in date
                                   ,p_flg_ativ_sess  in varchar2
                                   ,p_sqlcode        out number
                                   ,p_sqlerrm        out varchar2) is


   --
   begin
      --
      select (nvl(max(seq_sess), 0) + 1)
        into p_seq_sess
        from srv_processo_per_sess;

      --
      insert into srv_processo_per_sess
                 (cod_proc
                 ,seq_sess
                 ,num_ano
                 ,num_mes
                 ,dt_sess
                 ,flg_ativ_sess)
          values (p_cod_proc
                 ,p_seq_sess
                 ,p_num_ano
                 ,p_num_mes
                 ,p_dt_sess
                 ,p_flg_ativ_sess);
      --
      commit;

   exception
      when dup_val_on_index then
         update srv_processo_per_sess
            set dt_sess        = p_dt_sess
               ,flg_ativ_sess  = p_flg_ativ_sess
          where cod_proc       = p_cod_proc
            and seq_sess       = p_seq_sess
            and num_ano        = p_num_ano
            and num_mes        = p_num_mes;
         --
         commit;
      --
      when others then
         --
         p_sqlcode := sqlcode;
         p_sqlerrm := substr('Erro ao inserir nova sessao tabela srv_processo_per_sess - ' ||
                             ' cod_proc: '                  || p_cod_proc ||
                             ' seq_sess: '                  || p_seq_sess ||
                             ' num_ano : '                  || p_num_ano  ||
                             ' num_mes : '                  || p_num_mes  || sqlerrm,1,2000);
   end;


   ------------------------------------------------------------
   -- prc_atu_processo_per_sess : ATUALIZA SESSAO PARA O PERIODO
   ------------------------------------------------------------
   procedure prc_atu_proc_per_sess (p_cod_proc       in number
                                   ,p_seq_sess       in number
                                   ,p_num_ano        in number
                                   ,p_num_mes        in number
                                   ,p_dt_sess        in date
                                   ,p_flg_ativ_sess  in varchar2
                                   ,p_sqlcode        out number
                                   ,p_sqlerrm        out varchar2) is


   --
   begin
      update srv_processo_per_sess
         set dt_sess        = p_dt_sess
            ,flg_ativ_sess  = p_flg_ativ_sess
       where cod_proc       = p_cod_proc
         and seq_sess       = p_seq_sess
         and num_ano        = p_num_ano
         and num_mes        = p_num_mes;
      --
      commit;
   --
   exception
      --
      when others then
         --
         p_sqlcode := sqlcode;
         p_sqlerrm := substr('Erro ao atualizar sessao na tabela srv_processo_per_sess - ' ||
                             ' cod_proc: '                  || p_cod_proc ||
                             ' seq_sess: '                  || p_seq_sess ||
                             ' num_ano : '                  || p_num_ano  ||
                             ' num_mes : '                  || p_num_mes  || sqlerrm,1,2000);
   end;


   ------------------------------------------------------------------------------
   -- PRC_INS_PROC_PARAM_PER_SESS : INSERE PARAMETROS DA SESSAO PARA O PERIODO
   ------------------------------------------------------------------------------
   procedure prc_ins_proc_param_per_sess (p_cod_proc       in number
                                         ,p_seq_sess       in number
                                         ,p_num_ano        in number
                                         ,p_num_mes        in number
                                         ,p_seq_param      in number
                                         ,p_cont_param     in varchar2
                                         ,p_sqlcode        out number
                                         ,p_sqlerrm        out varchar2) is


   --
   begin

      --
      insert into srv_processo_param_per_sess
                 (cod_proc
                 ,seq_sess
                 ,num_ano
                 ,num_mes
                 ,seq_param
                 ,cont_param)
          values (p_cod_proc
                 ,p_seq_sess
                 ,p_num_ano
                 ,p_num_mes
                 ,p_seq_param
                 ,p_cont_param);
      --
      commit;

   exception
      when dup_val_on_index then
         update srv_processo_param_per_sess
            set cont_param     = p_cont_param
          where cod_proc       = p_cod_proc
            and seq_sess       = p_seq_sess
            and num_ano        = p_num_ano
            and num_mes        = p_num_mes
            and seq_param      = p_seq_param;
         --
         commit;
      --
      when others then
         --
         p_sqlcode := sqlcode;
         p_sqlerrm := substr('Erro ao inserir novo parametro para a sessao tabela srv_processo_param_per_sess - ' ||
                             ' cod_proc: '                  || p_cod_proc  ||
                             ' seq_sess: '                  || p_seq_sess  ||
                             ' num_ano : '                  || p_num_ano   ||
                             ' num_mes : '                  || p_num_mes   ||
                             ' seq_param: '                 || p_seq_param || sqlerrm,1,2000);
   end;


   ------------------------------------------------------------
   -- PRC_ATU_PROC_PER : ATUALIZA PROCESSO PARA O PERIODO
   ------------------------------------------------------------
   procedure prc_atu_proc_per (p_cod_proc       in number
                              ,p_num_ano        in number
                              ,p_num_mes        in number
                              ,p_sta_proc       in number
                              ,p_sqlcode        out number
                              ,p_sqlerrm        out varchar2) is


   --
   begin
      --status do processo, valores: 1 - aberto, 2 - fechado, 3 - reaberto , 4 - fechar
      update srv_processo_per
         set sta_proc       = p_sta_proc
       where cod_proc       = p_cod_proc
         and num_ano        = p_num_ano
         and num_mes        = p_num_mes;
      --
      --
      /*update srv_processo p
         set p.flg_ativ_proc = 'N'
       where p.cod_proc = p_cod_proc;*/
      --
      commit;
   --
   exception
      --
      when others then
         --
         p_sqlcode := sqlcode;
         p_sqlerrm := substr('Erro ao atualizar (fechar) processo do periodo na tabela srv_processo_per - ' ||
                             ' cod_proc: '                  || p_cod_proc ||
                             ' num_ano : '                  || p_num_ano  ||
                             ' num_mes : '                  || p_num_mes  || sqlerrm,1,2000);
   end;

   -----------------------------------------------------------------------------------------------
   -- LOGA ERRO OU OBSERVACAO DO PROCESSO NA TABELA DE LOG
   -----------------------------------------------------------------------------------------------
   function fnc_insere_log_processo (p_cod_proc    in  number
                                    ,p_nome_proc   in  varchar2
                                    ,p_num_ano     in  number
                                    ,p_num_mes     in  number
                                    ,p_cod_fil     in  number
                                    ,p_cod_func    in  number
                                    ,p_cod_indic   in  number
                                    ,p_erro        in  varchar2) return varchar2 is

      --
      v_sqlerrm_log_erro varchar2(2000);

   begin
      --
      insert into srv_processo_log_erro
                  (cod_proc
                  ,nome_proc
                  ,dt_log
                  ,num_ano
                  ,num_mes
                  ,cod_fil
                  ,cod_func
                  ,cod_indic
                  ,erro
                  )
           values (p_cod_proc
                  ,p_nome_proc
                  ,sysdate
                  ,p_num_ano
                  ,p_num_mes
                  ,p_cod_fil
                  ,p_cod_func
                  ,p_cod_indic
                  ,p_erro
                  );


      --
      commit;

      --
      return (v_sqlerrm_log_erro);

   exception
      when others then
         v_sqlerrm_log_erro := sqlerrm;
         return (v_sqlerrm_log_erro);
   end fnc_insere_log_processo;

---------------------------------------------------------------------------------------------------
-- INICIO PROCESSO
---------------------------------------------------------------------------------------------------
begin
   --

   ---------------------------------
   -- cursor processos do periodo
   ---------------------------------
   for r_proc_per in c_proc_per loop

      v_dt_proc_ini := sysdate;
      v_dt_proc_fim := v_dt_proc_ini;

      -- verifica fechamento do processo
      if r_proc_per.sta_proc in (3 -- Reaberto
                                ,4 -- Fechar
                                ) then
          v_fecha_proc := true;
      else
          v_fecha_proc := false;
      end if;


      -- verifica sessoes existentes para o processo => cadastradas pelo usuario
      select count(1)
        into v_qtde_sessoes
        from srv_processo_per_sess a
       where a.flg_ativ_sess     = 'S'
         and a.cod_proc          = r_proc_per.cod_proc
         and a.num_ano           = r_proc_per.num_ano
         and a.num_mes           = r_proc_per.num_mes;

      -- se nao ha nenhuma sessao cadastrada, inserir uma sessao
      if v_qtde_sessoes = 0 then
         -- insere sessao
         v_sqlcode := null;
         v_sqlerrm := null;
         --
         prc_ins_proc_per_sess (p_cod_proc       => r_proc_per.cod_proc
                               ,p_seq_sess       => v_seq_sess
                               ,p_num_ano        => r_proc_per.num_ano
                               ,p_num_mes        => r_proc_per.num_mes
                               ,p_dt_sess        => v_dt_proc_ini
                               ,p_flg_ativ_sess  => 'S'  -- esta abrindo a sessao
                               ,p_sqlcode        => v_sqlcode
                               ,p_sqlerrm        => v_sqlerrm);

         -- se nao deu erro ao inserir sessao , inserir os parametros da sessao
         if v_sqlcode is null then
            --
            for r_proc_param in c_proc_param (p_cod_proc => r_proc_per.cod_proc)  loop
               --
               if r_proc_param.nome_param    = 'P_NUM_ANO'     then
                  v_cont_param :=  r_proc_per.num_ano;
               elsif r_proc_param.nome_param = 'P_NUM_MES'     then
                  v_cont_param :=  r_proc_per.num_mes;
               elsif r_proc_param.nome_param = 'P_COD_USUARIO' then
                  v_cont_param :=  1;
               else
                  v_cont_param :=  null;
               end if;

               --
               prc_ins_proc_param_per_sess (p_cod_proc       => r_proc_per.cod_proc
                                           ,p_seq_sess       => v_seq_sess
                                           ,p_num_ano        => r_proc_per.num_ano
                                           ,p_num_mes        => r_proc_per.num_mes
                                           ,p_seq_param      => r_proc_param.seq_param
                                           ,p_cont_param     => v_cont_param
                                           ,p_sqlcode        => v_sqlcode
                                           ,p_sqlerrm        => v_sqlerrm);


               if v_sqlcode is not null then
                  -- se retornou erro ao inserir parametro da sessao logar o erro
                  v_erro     := substr('Erro ao inserir novo parametro da sessao proc prc_ins_proc_param_per_sess'
                                       || ' : ' || v_sqlerrm,1,2000);

                  -- logar no processo
                  prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                                    ,p_seq_sess      => v_seq_sess
                                    ,p_num_ano       => r_proc_per.num_ano
                                    ,p_num_mes       => r_proc_per.num_mes
                                    ,p_dt_proc_ini   => v_dt_proc_ini
                                    ,p_dt_proc_fim   => v_dt_proc_fim
                                    ,p_sta_proc_exec => v_sta_proc_exec
                                    ,p_obs           => v_erro
                                    ,p_sqlcode       => v_sqlcode
                                    ,p_sqlerrm       => v_sqlerrm
                                    );

                  v_sqlerrm := v_erro;


                  -- enviar email
                  v_body       := v_sqlerrm;

                  v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                          ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao: '
                                                                          ,p_body    => v_body
                                                                          );

                  -- logar tabela
                  v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                            ,p_nome_proc   => r_proc_per.nome_proc
                                                            ,p_num_ano     => r_proc_per.num_ano
                                                            ,p_num_mes     => r_proc_per.num_mes
                                                            ,p_cod_fil     => null
                                                            ,p_cod_func    => null
                                                            ,p_cod_indic   => null
                                                            ,p_erro        => v_sqlerrm
                                                            );

                  if v_ins_log_erro is not null then
                     -- enviar email
                     v_body       := v_ins_log_erro;
                     --
                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                             ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao: '
                                                                             ,p_body    => v_body
                                                                             );
                  --v_ins_log_erro is not null
                  end if;

                  --
--                  raise e_erro;
                  exit;

               else
                  -- se nao retornou erro
                  v_erro     := null;
               end if;


            -- c_proc_param
            end loop;

/*            -- se nao retornou erro
            v_erro     := null; */

         -- se deu erro ao inserir sessao
         else

            -- se retornou erro ao inserir nova sessao
            v_erro     := substr('Erro ao inserir nova sessao proc prc_ins_proc_per_sess'
                                 || ' : ' || v_sqlerrm,1,2000);

            -- logar no processo
            prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                              ,p_seq_sess      => v_seq_sess
                              ,p_num_ano       => r_proc_per.num_ano
                              ,p_num_mes       => r_proc_per.num_mes
                              ,p_dt_proc_ini   => v_dt_proc_ini
                              ,p_dt_proc_fim   => v_dt_proc_fim
                              ,p_sta_proc_exec => v_sta_proc_exec
                              ,p_obs           => v_erro
                              ,p_sqlcode       => v_sqlcode
                              ,p_sqlerrm       => v_sqlerrm
                              );

            v_sqlerrm := v_erro;
            --

            -- enviar email
            v_body       := v_sqlerrm;

            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                    ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao: '
                                                                    ,p_body    => v_body
                                                                    );

            -- logar tabela
            v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                      ,p_nome_proc   => r_proc_per.nome_proc
                                                      ,p_num_ano     => r_proc_per.num_ano
                                                      ,p_num_mes     => r_proc_per.num_mes
                                                      ,p_cod_fil     => null
                                                      ,p_cod_func    => null
                                                      ,p_cod_indic   => null
                                                      ,p_erro        => v_sqlerrm
                                                      );

            if v_ins_log_erro is not null then
               -- enviar email
               v_body       := v_ins_log_erro;
               --
               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao: '
                                                                       ,p_body    => v_body
                                                                       );
            --v_ins_log_erro is not null
            end if;

            --
--            raise e_erro;


         -- se nao deu erro ao inserir sessao , inserir os parametros da sessao
         end if;


         -- se retornou erro ao inserir nova sessao ou ao inserir parametro da sessao atribuir status
         if v_erro is not null then
            v_sta_proc_exec := 2;  --PROCESSADO COM ERRO
         else
            v_sta_proc_exec := 0;  --NAO PROCESSADO
         end if;

         v_sqlcode  := null;
         v_sqlerrm  := null;
         -- logar no processo
         prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                           ,p_seq_sess      => v_seq_sess
                           ,p_num_ano       => r_proc_per.num_ano
                           ,p_num_mes       => r_proc_per.num_mes
                           ,p_dt_proc_ini   => v_dt_proc_ini
                           ,p_dt_proc_fim   => v_dt_proc_fim
                           ,p_sta_proc_exec => v_sta_proc_exec
                           ,p_obs           => v_erro
                           ,p_sqlcode       => v_sqlcode
                           ,p_sqlerrm       => v_sqlerrm
                           );
         --
         if v_sqlcode is not null then
            v_sqlerrm := substr('Erro ao logar no processo proc prc_ins_proc_exec apos inserir ou erro ao inserir nova sessao : ' ||
                                ' cod_proc: '                  || r_proc_per.cod_proc  ||
                                ' seq_sess: '                  || v_seq_sess           ||
                                ' num_ano : '                  || r_proc_per.num_ano   ||
                                ' num_mes : '                  || r_proc_per.num_mes   || v_sqlerrm,1,2000);

            -- enviar email
            v_body       := v_sqlerrm;

            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                    ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                    ,p_body    => v_body
                                                                    );

            -- logar tabela
            v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                      ,p_nome_proc   => r_proc_per.nome_proc
                                                      ,p_num_ano     => r_proc_per.num_ano
                                                      ,p_num_mes     => r_proc_per.num_mes
                                                      ,p_cod_fil     => null
                                                      ,p_cod_func    => null
                                                      ,p_cod_indic   => null
                                                      ,p_erro        => v_sqlerrm
                                                      );

            if v_ins_log_erro is not null then
               -- enviar email
               v_body       := v_ins_log_erro;
               --
               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                       ,p_body    => v_body
                                                                       );
            --v_ins_log_erro is not null
            end if;

--            raise e_erro;

         -- se retornou ero ao logar no processo
         end if;


      -- if v_qtde_sessoes = 0
      end if;

      -----------------------------------
      -- cursor sessoes do processo
      -----------------------------------
      for r_proc_sess in c_proc_sess (p_cod_proc   => r_proc_per.cod_proc
                                     ,p_num_ano    => r_proc_per.num_ano
                                     ,p_num_mes    => r_proc_per.num_mes) loop


         --
         v_erro     := null;
         v_sqlcode  := null;
         v_sqlerrm  := null;
         v_sta_proc_exec := 0;  --NAO PROCESSADO

         -- logar no processo
         prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                           ,p_seq_sess      => r_proc_sess.seq_sess
                           ,p_num_ano       => r_proc_per.num_ano
                           ,p_num_mes       => r_proc_per.num_mes
                           ,p_dt_proc_ini   => v_dt_proc_ini
                           ,p_dt_proc_fim   => v_dt_proc_fim
                           ,p_sta_proc_exec => v_sta_proc_exec
                           ,p_obs           => v_erro
                           ,p_sqlcode       => v_sqlcode
                           ,p_sqlerrm       => v_sqlerrm
                           );
         --
         if v_sqlcode is not null then

            v_sta_proc_exec := 2;  --PROCESSADO COM ERRO

            v_sqlerrm := substr('Erro ao logar no processo proc prc_ins_proc_exec no cursor c_proc_sess : ' ||
                                ' cod_proc: '                  || r_proc_per.cod_proc  ||
                                ' seq_sess: '                  || r_proc_sess.seq_sess ||
                                ' num_ano : '                  || r_proc_per.num_ano   ||
                                ' num_mes : '                  || r_proc_per.num_mes   || v_sqlerrm,1,2000);


            -- enviar email
            v_body       := v_sqlerrm;

            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                    ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                    ,p_body    => v_body
                                                                    );

            -- logar tabela
            v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                      ,p_nome_proc   => r_proc_per.nome_proc
                                                      ,p_num_ano     => r_proc_per.num_ano
                                                      ,p_num_mes     => r_proc_per.num_mes
                                                      ,p_cod_fil     => null
                                                      ,p_cod_func    => null
                                                      ,p_cod_indic   => null
                                                      ,p_erro        => v_sqlerrm
                                                      );

            if v_ins_log_erro is not null then
               -- enviar email
               v_body       := v_ins_log_erro;
               --
               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                       ,p_body    => v_body
                                                                       );
            --v_ins_log_erro is not null
            end if;

            exit;
--            raise e_erro;

         -- se deu erro logar no processo
         end if;


         -- executa o processo
         begin
            --
            if r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FIL_LJ' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FIL_LJ (p_num_ano             => r_proc_per.num_ano
                                                          ,p_num_mes             => r_proc_per.num_mes
                                                          ,p_cod_usuario         => 1
                                                          ,p_cod_erro            => v_sqlcode_exec
                                                          ,p_descr_erro          => v_sqlerrm_exec
                                                           );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_ATING_FIL_LJ' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_ATING_FIL_LJ (p_num_ano             => r_proc_per.num_ano
                                                          ,p_num_mes             => r_proc_per.num_mes
                                                          ,p_cod_usuario         => 1
                                                          ,p_cod_erro            => v_sqlcode_exec
                                                          ,p_descr_erro          => v_sqlerrm_exec
                                                           );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_LJ' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_LJ (p_num_ano             => r_proc_per.num_ano
                                                                 ,p_num_mes             => r_proc_per.num_mes
                                                                 ,p_cod_usuario         => 1
                                                                 ,p_cod_erro            => v_sqlcode_exec
                                                                 ,p_descr_erro          => v_sqlerrm_exec
                                                                  );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_ATING_FIL_SAX' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_ATING_FIL_SAX (p_num_ano             => r_proc_per.num_ano
                                                           ,p_num_mes             => r_proc_per.num_mes
                                                           ,p_cod_usuario         => 1
                                                           ,p_cod_erro            => v_sqlcode_exec
                                                           ,p_descr_erro          => v_sqlerrm_exec
                                                            );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_SAX' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_SAX (p_num_ano             => r_proc_per.num_ano
                                                                  ,p_num_mes             => r_proc_per.num_mes
                                                                  ,p_cod_usuario         => 1
                                                                  ,p_cod_erro            => v_sqlcode_exec
                                                                  ,p_descr_erro          => v_sqlerrm_exec
                                                                   );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FIL_CCENTER' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FIL_CCENTER (p_num_ano             => r_proc_per.num_ano
                                                               ,p_num_mes             => r_proc_per.num_mes
                                                               ,p_cod_usuario         => 1
                                                               ,p_cod_erro            => v_sqlcode_exec
                                                               ,p_descr_erro          => v_sqlerrm_exec
                                                                );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_ATING_FIL_CCENTER' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FIL_CCENTER (p_num_ano             => r_proc_per.num_ano
                                                               ,p_num_mes             => r_proc_per.num_mes
                                                               ,p_cod_usuario         => 1
                                                               ,p_cod_erro            => v_sqlcode_exec
                                                               ,p_descr_erro          => v_sqlerrm_exec
                                                                );

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_CCENTER' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_CCENTER (p_num_ano             => r_proc_per.num_ano
                                                                ,p_num_mes             => r_proc_per.num_mes
                                                                ,p_cod_usuario         => 1
                                                                ,p_cod_erro            => v_sqlcode_exec
                                                                ,p_descr_erro          => v_sqlerrm_exec
                                                                 );

/*            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_CORP' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_CORP (p_num_ano             => r_proc_per.num_ano
                                                                   ,p_num_mes             => r_proc_per.num_mes
                                                                   ,p_cod_usuario         => 1
                                                                   ,p_cod_erro            => v_sqlcode_exec
                                                                   ,p_descr_erro          => v_sqlerrm_exec
                                                                    );*/

            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_ADM' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_ADM (p_num_ano             => r_proc_per.num_ano
                                                                  ,p_num_mes             => r_proc_per.num_mes
                                                                  ,p_cod_usuario         => 1
                                                                  ,p_cod_erro            => v_sqlcode_exec
                                                                  ,p_descr_erro          => v_sqlerrm_exec
                                                                   );
            elsif r_proc_per.nome_proc = 'PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_PPR' then
               PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_PPR (p_num_ano             => r_proc_per.num_ano
                                                       ,p_num_mes             => r_proc_per.num_mes
                                                       ,p_cod_usuario         => 1
                                                       ,p_cod_erro            => v_sqlcode_exec
                                                       ,p_descr_erro          => v_sqlerrm_exec
                                                        );

            else
               --
               v_sta_proc_exec := 2;  --PROCESSADO COM ERRO
               v_sqlcode_exec := -1;
               v_sqlerrm_exec := 'Processo '||r_proc_per.nome_proc || ' nao encontrado para execucao.';

            --if nome do processo
            end if;


            -- fim do processo
            v_dt_proc_fim := sysdate;


            -- SE PROCESSO COM ERRO
            -- se retornou erro na variavel de retorno do processo logar o erro
            if v_sqlerrm_exec is not null then

               --
               v_sta_proc_exec := 2;  --PROCESSADO COM ERRO

               --
               v_erro        := substr('Erro retornado na execucao pelo processo '
                                       || r_proc_per.nome_proc || ' : ' || v_sqlerrm_exec,1,2000);

               -- enviar email
               v_body       := v_erro;

               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                       ,p_body    => v_body
                                                                       );

               -- logar tabela
               v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                         ,p_nome_proc   => r_proc_per.nome_proc
                                                         ,p_num_ano     => r_proc_per.num_ano
                                                         ,p_num_mes     => r_proc_per.num_mes
                                                         ,p_cod_fil     => null
                                                         ,p_cod_func    => null
                                                         ,p_cod_indic   => null
                                                         ,p_erro        => v_erro
                                                         );

               if v_ins_log_erro is not null then
                  -- enviar email
                  v_body       := v_ins_log_erro;
                  --
                  v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                          ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                          ,p_body    => v_body
                                                                          );
               --v_ins_log_erro is not null
               end if;

            -- SE PROCESSO OK
            else
               --
               v_sta_proc_exec := 1;  --PROCESSADO OK
            -- se processo OK ou com ERRO
            end if;

            --
            v_sqlcode  := null;
            v_sqlerrm  := null;
            -- logar no processo
            prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                              ,p_seq_sess      => r_proc_sess.seq_sess
                              ,p_num_ano       => r_proc_per.num_ano
                              ,p_num_mes       => r_proc_per.num_mes
                              ,p_dt_proc_ini   => v_dt_proc_ini
                              ,p_dt_proc_fim   => v_dt_proc_fim
                              ,p_sta_proc_exec => v_sta_proc_exec
                              ,p_obs           => v_erro
                              ,p_sqlcode       => v_sqlcode
                              ,p_sqlerrm       => v_sqlerrm
                              );
            --
            if v_sqlcode is not null then
                  v_sqlerrm := substr('Erro ao logar no processo apos a execucao do processo : ' ||
                                      ' cod_proc: '                  || r_proc_per.cod_proc  ||
                                      ' seq_sess: '                  || r_proc_sess.seq_sess ||
                                      ' num_ano : '                  || r_proc_per.num_ano   ||
                                      ' num_mes : '                  || r_proc_per.num_mes   || v_sqlerrm,1,2000);

               -- enviar email
               v_body       := v_sqlerrm;

               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                       ,p_body    => v_body
                                                                       );

               -- logar tabela
               v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                         ,p_nome_proc   => r_proc_per.nome_proc
                                                         ,p_num_ano     => r_proc_per.num_ano
                                                         ,p_num_mes     => r_proc_per.num_mes
                                                         ,p_cod_fil     => null
                                                         ,p_cod_func    => null
                                                         ,p_cod_indic   => null
                                                         ,p_erro        => v_sqlerrm
                                                         );

               if v_ins_log_erro is not null then
                  -- enviar email
                  v_body       := v_ins_log_erro;
                  --
                  v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                          ,p_subject => 'SRV Processamento - Erro ao inserir nova sessao ou ao inserir parametro da sessao: '
                                                                          ,p_body    => v_body
                                                                          );
               --v_ins_log_erro is not null
               end if;

--               raise e_erro;
            end if;

            --
            v_sqlcode  := null;
            v_sqlerrm  := null;
            --
            prc_atu_proc_per_sess (p_cod_proc       => r_proc_per.cod_proc
                                  ,p_seq_sess       => r_proc_sess.seq_sess
                                  ,p_num_ano        => r_proc_per.num_ano
                                  ,p_num_mes        => r_proc_per.num_mes
                                  ,p_dt_sess        => v_dt_proc_ini
                                  ,p_flg_ativ_sess  => 'N' -- esta fechando a sessao
                                  ,p_sqlcode        => v_sqlcode
                                  ,p_sqlerrm        => v_sqlerrm);

            -- se processo com erro nao fechar processo do periodo
            if v_sta_proc_exec <> 1 then
               --
               v_fecha_proc := false;
            else
               v_fecha_proc := true;
            end if;


         exception
            when others then
               -- nao fechar processo do periodo
               v_fecha_proc       := false;
               --
               v_dt_proc_fim      := sysdate;
               v_erro             := 'Erro retornado no processo ' || r_proc_per.nome_proc || ' : ' || sqlerrm;
               v_sta_proc_exec    := 2;  --PROCESSADO COM ERRO

               v_sqlcode     := null;
               v_sqlerrm     := null;

               -- enviar email
               v_body       := substr(v_erro,1,2000);

               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro retornado no processo: ' || r_proc_per.nome_proc
                                                                       ,p_body    => v_body
                                                                       );

               -- logar tabela
               v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                         ,p_nome_proc   => r_proc_per.nome_proc
                                                         ,p_num_ano     => r_proc_per.num_ano
                                                         ,p_num_mes     => r_proc_per.num_mes
                                                         ,p_cod_fil     => null
                                                         ,p_cod_func    => null
                                                         ,p_cod_indic   => null
                                                         ,p_erro        => v_erro
                                                         );

               if v_ins_log_erro is not null then
                  -- enviar email
                  v_body       := v_ins_log_erro;
                  --
                  v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                          ,p_subject => 'SRV Processamento - Erro retornado no processo: ' || r_proc_per.nome_proc
                                                                          ,p_body    => v_body
                                                                          );
               --v_ins_log_erro is not null
               end if;


               -- logar no processo
               prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                                 ,p_seq_sess      => r_proc_sess.seq_sess
                                 ,p_num_ano       => r_proc_per.num_ano
                                 ,p_num_mes       => r_proc_per.num_mes
                                 ,p_dt_proc_ini   => v_dt_proc_ini
                                 ,p_dt_proc_fim   => v_dt_proc_fim
                                 ,p_sta_proc_exec => v_sta_proc_exec
                                 ,p_obs           => v_erro
                                 ,p_sqlcode       => v_sqlcode
                                 ,p_sqlerrm       => v_sqlerrm
                                 );
               --
               if v_sqlcode is not null then
                  v_sqlerrm := substr('Erro ao logar no processo apos erro na execucao do processo : ' ||
                                      ' cod_proc: '                  || r_proc_per.cod_proc  ||
                                      ' seq_sess: '                  || r_proc_sess.seq_sess ||
                                      ' num_ano : '                  || r_proc_per.num_ano   ||
                                      ' num_mes : '                  || r_proc_per.num_mes   || v_sqlerrm,1,2000);

                  -- enviar email
                  v_body       := v_sqlerrm;

                  v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                          ,p_subject => 'SRV Processamento - Erro ao logar no processo apos erro na execucao do processo: '
                                                                          ,p_body    => v_body
                                                                          );

                  -- logar tabela
                  v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                            ,p_nome_proc   => r_proc_per.nome_proc
                                                            ,p_num_ano     => r_proc_per.num_ano
                                                            ,p_num_mes     => r_proc_per.num_mes
                                                            ,p_cod_fil     => null
                                                            ,p_cod_func    => null
                                                            ,p_cod_indic   => null
                                                            ,p_erro        => v_sqlerrm
                                                            );

                  if v_ins_log_erro is not null then
                     -- enviar email
                     v_body       := v_ins_log_erro;
                     --
                     v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                             ,p_subject => 'SRV Processamento - Erro ao logar no processo apos erro na execucao do processo: '
                                                                             ,p_body    => v_body
                                                                             );
                  --v_ins_log_erro is not null
                  end if;

--                  raise e_erro;
               end if;

         -- execption executa processo
         end;

      -- c_proc_sess
      end loop;

      -- fechar processo do periodo
      if v_fecha_proc and v_sta_proc_exec = 1 then  --PROCESSADO ok
         --
         v_sqlcode  := null;
         v_sqlerrm  := null;
         --
         prc_atu_proc_per (p_cod_proc       => r_proc_per.cod_proc
                          ,p_num_ano        => r_proc_per.num_ano
                          ,p_num_mes        => r_proc_per.num_mes
                          ,p_sta_proc       => 2 --Status do Processo: 1 = Aberto / 2 = Fechado
                          ,p_sqlcode        => v_sqlcode
                          ,p_sqlerrm        => v_sqlerrm);


         -- se houve erro ao fechar processo do periodo
         if v_sqlcode is not null then

            v_sqlerrm := substr('Erro ao fechar o processo do periodo : ' ||
                                ' cod_proc: '                  || r_proc_per.cod_proc  ||
                                ' num_ano : '                  || r_proc_per.num_ano   ||
                                ' num_mes : '                  || r_proc_per.num_mes   || v_sqlerrm,1,2000);

            --
            v_erro        := v_sqlerrm;

            -- enviar email
            v_body       := v_sqlerrm;

            v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                    ,p_subject => 'SRV Processamento - Erro ao fechar o processo: '
                                                                    ,p_body    => v_body
                                                                    );

            -- logar tabela
            v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                      ,p_nome_proc   => r_proc_per.nome_proc
                                                      ,p_num_ano     => r_proc_per.num_ano
                                                      ,p_num_mes     => r_proc_per.num_mes
                                                      ,p_cod_fil     => null
                                                      ,p_cod_func    => null
                                                      ,p_cod_indic   => null
                                                      ,p_erro        => v_sqlerrm
                                                      );

            if v_ins_log_erro is not null then
               -- enviar email
               v_body       := v_ins_log_erro;
               --
               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao fechar o processo: '
                                                                       ,p_body    => v_body
                                                                       );
            --v_ins_log_erro is not null
            end if;



            v_sqlcode     := null;
            v_sqlerrm     := null;
            -- logar no processo
            prc_ins_proc_exec (p_cod_proc      => r_proc_per.cod_proc
                              ,p_seq_sess      => null
                              ,p_num_ano       => r_proc_per.num_ano
                              ,p_num_mes       => r_proc_per.num_mes
                              ,p_dt_proc_ini   => v_dt_proc_ini
                              ,p_dt_proc_fim   => v_dt_proc_fim
                              ,p_sta_proc_exec => v_sta_proc_exec
                              ,p_obs           => v_erro
                              ,p_sqlcode       => v_sqlcode
                              ,p_sqlerrm       => v_sqlerrm
                              );
            --
            if v_sqlcode is not null then
               v_sqlerrm := substr('Erro ao logar no processo apos erro ao fechar o processo do periodo : ' ||
                                   ' cod_proc: '                  || r_proc_per.cod_proc  ||
                                   ' num_ano : '                  || r_proc_per.num_ano   ||
                                   ' num_mes : '                  || r_proc_per.num_mes   || v_sqlerrm,1,2000);

               -- enviar email
               v_body       := v_sqlerrm;

               v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                       ,p_subject => 'SRV Processamento - Erro ao logar no processo apos erro ao fechar o processo do periodo: '
                                                                       ,p_body    => v_body
                                                                       );

               -- logar tabela
               v_ins_log_erro := fnc_insere_log_processo (p_cod_proc    => r_proc_per.cod_proc
                                                         ,p_nome_proc   => r_proc_per.nome_proc
                                                         ,p_num_ano     => r_proc_per.num_ano
                                                         ,p_num_mes     => r_proc_per.num_mes
                                                         ,p_cod_fil     => null
                                                         ,p_cod_func    => null
                                                         ,p_cod_indic   => null
                                                         ,p_erro        => v_sqlerrm
                                                         );

               if v_ins_log_erro is not null then
                  -- enviar email
                  v_body       := v_ins_log_erro;
                  --
                  v_send_email := pkg_ccm_func_proc_geral.fnc_enviar_email(p_rot_cod => 71 -- SRV - PRC_SRV_PROCESSAMENTO
                                                                          ,p_subject => 'SRV Processamento - Erro ao logar no processo apos erro ao fechar o processo do periodo: '
                                                                          ,p_body    => v_body
                                                                          );
               --v_ins_log_erro is not null
               end if;

--               raise e_erro;

            -- erro ao logar no processo
            end if;

         -- se houve erro ao fechar processo do periodo
         end if;

      -- if v_fecha_proc
      end if;

   -- c_proc_per => processos do periodo
   end loop;

--
exception
   when e_erro then
      dbms_output.put_line('Exception e_erro ao executar prc_srv_processamento: ' || v_sqlerrm);

   when others then
      dbms_output.put_line('Exception geral ao executar prc_srv_processamento: ' || sqlerrm);
end;
/
