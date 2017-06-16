create or replace package body srv.pkg_srv_realizado is

  /*-------------------------------------------------------------------------------------------------
  -- Author     : LEVY VILLAR
  -- Created    : 11/04/2017
  -- Purpose    : Package para apuracao de realizado (Indicadores do CCM para o SRV)
  -- SourceSafe : $/Fontes32/CCM/SRV/Package
  -- Version : 001 - Versao Inicial
  -------------------------------------------------------------------------------------------------*/
  v_DbLink varchar2(10) := '';--'@lksrv';


  /*---------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------
  -- CRIA TABELA POR COMANDO
  -----------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------*/
  procedure Prc_Cria_Tabela2(p_tabela     in varchar2,
                            p_comando1    in varchar2,
                            p_comando2    in varchar2,
                            p_index      in varchar2,
                            p_cod_erro   out number,
                            p_descr_erro out varchar2) is

    v_name_index varchar2(30);
  
    begin
  
      -- index name
      v_name_index := 'i_' || substr(p_tabela, 1, 28);
  
      -- drop table
      begin
        execute immediate 'drop table ' || p_tabela;
      exception
        when others then
          null;
      end;
  
      -- create table
      begin
        execute immediate 'create table ' || p_tabela || ' pctfree 0 nologging as ' || p_comando1 || p_comando2;
      exception
        when others then
          p_cod_erro   := 1;
          p_descr_erro := 'Erro ao criar tabela ' || p_tabela || ' erro - ' || sqlerrm;
          return;
      end;
  
      -- drop index
      begin
        execute immediate 'drop index ' || v_name_index;
      exception
        when others then
          null;
      end;
  
      -- create index
      begin
        execute immediate 'create index ' || v_name_index || ' on ' || p_tabela || ' ' || p_index;
      exception
        when others then
          p_cod_erro   := 1;
          p_descr_erro := 'Erro ao criar indice na tabela ' || p_tabela || ' - index: ' || v_name_index ||
                          ' erro - ' || sqlerrm;
      end;
  
      -- analyze table
      begin
        execute immediate 'analyze table ' || p_tabela || ' estimate statistics sample 10 percent';
      exception
        when others then
          null;
      end;
  
      -- grants
      begin
        execute immediate 'grant select on ' || p_tabela || ' to SRV_SEL';
        execute immediate 'grant select on ' || p_tabela || ' to CCM_SEL';
      exception
        when others then
          null;
      end;
  
    exception
      when others then
        p_cod_erro   := 1;
        p_descr_erro := 'Erro geral ao criar tabela realizado: ' || sqlerrm;
  end Prc_Cria_Tabela2;


procedure Prc_Cria_Tabela(p_tabela     in varchar2,
                            p_comando    in varchar2,
                            p_index      in varchar2,
                            p_cod_erro   out number,
                            p_descr_erro out varchar2) is

    v_name_index varchar2(30);
  
    begin
  
      -- index name
      v_name_index := 'i_' || substr(p_tabela, 1, 28);
  
      -- drop table
      begin
        execute immediate 'drop table ' || p_tabela;
      exception
        when others then
          null;
      end;
  
      -- create table
      begin
        execute immediate 'create table ' || p_tabela || ' pctfree 0 nologging as ' || p_comando;
      exception
        when others then
          p_cod_erro   := 1;
          p_descr_erro := 'Erro ao criar tabela ' || p_tabela || ' erro - ' || sqlerrm;
          return;
      end;
  
      -- drop index
      begin
        execute immediate 'drop index ' || v_name_index;
      exception
        when others then
          null;
      end;
  
      -- create index
      begin
        execute immediate 'create index ' || v_name_index || ' on ' || p_tabela || ' ' || p_index;
      exception
        when others then
          p_cod_erro   := 1;
          p_descr_erro := 'Erro ao criar indice na tabela ' || p_tabela || ' - index: ' || v_name_index ||
                          ' erro - ' || sqlerrm;
      end;
  
      -- analyze table
      begin
        execute immediate 'analyze table ' || p_tabela || ' estimate statistics sample 10 percent';
      exception
        when others then
          null;
      end;
  
      -- grants
      begin
        execute immediate 'grant select on ' || p_tabela || ' to SRV_SEL';
        execute immediate 'grant select on ' || p_tabela || ' to CCM_SEL';
      exception
        when others then
          null;
      end;
  
    exception
      when others then
        p_cod_erro   := 1;
        p_descr_erro := 'Erro geral ao criar tabela realizado: ' || sqlerrm;
  end Prc_Cria_Tabela;



  /*---------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------
  -- PROVISORIO: => PROCESSO TEMPORARIO PARA APURACAO (PROTECAO CELULAR e PROTECAO FINANCEIRA)
  -----------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------*/
  procedure Prc_Tmp_ProtCel_ProtFin (p_ano in varchar2,
                                     p_mes in varchar2,
                                     p_cod_erro out number,
                                     p_descr_erro out varchar2) is

    v_dt_ini varchar2(20);
    v_dt_fim varchar2(20);
    v_index varchar2(500);
    v_comando varchar2(5000);
    v_nm_tabela varchar2(30);

    begin

      -- datas
      v_dt_ini  := '01/' || p_mes || '/' || p_ano || ' ' || '00:00:00';
      select to_char(last_day(to_date(('01/'||p_mes||'/'||p_ano),'dd/mm/yyyy')),'dd/mm/yyyy') || ' 23:59:59' into v_dt_fim from dual;

      -- SRV_TMP_CTR_PSF_CANC
      v_nm_tabela := 'SRV_TMP_CTR_PSF_CANC_' || p_ano || p_mes;
      v_comando := ' select f.pss_prd_codigo
                           ,f.cli_cpf
                           ,f.pss_ctr_codigo
                           ,f.pss_lnc_dt_lanc_premio
                      from ccm_pss_lancamento_premio'||v_DbLink||'  f
                          ,ccm_pss_contrato_cliente'||v_DbLink||'   g
                     where f.pss_ctr_codigo         = g.pss_ctr_codigo
                       and g.pss_ctr_in_situacao    = 2
                       and f.pss_lnc_tipo           = 5 -- cancelamento
                       and f.pss_lnc_dt_lanc_premio > (to_date('''||v_dt_fim||''', ''dd/mm/yyyy hh24:mi:ss'') + 5)';
      v_index := '(pss_ctr_codigo)';

      Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                      p_comando => v_comando,
                      p_index => v_index,
                      p_cod_erro => p_cod_erro,
                      p_descr_erro => p_descr_erro);

      if p_cod_erro is not null then
        return;
      end if;

      -- SRV_REALZFI_PROT_CEL
      v_nm_tabela := 'SRV_REALZFI_PROT_CEL_' || p_ano || p_mes;
      v_comando := 'select e.fil_cod fil_cod,
                           21 cod_indic,
                           ''PROTECAO CELULAR MARISA'' descr_indic,
                           count(*) qtde
                      from ccm_pss_contrato_cliente'||v_DbLink||' a,
                           ccm_pss_canal_venda'||v_DbLink||' e
                     where a.pss_cnl_codigo = e.pss_cnl_codigo
                       and a.pss_prd_codigo in (119,151)
                       and (a.pss_ctr_in_situacao = 1 or
                           (a.pss_ctr_in_situacao = 2 and exists
                            (select 1
                                from SRV_TMP_CTR_PSF_CANC_' || p_ano || p_mes || ' f
                               where f.pss_ctr_codigo = a.pss_ctr_codigo)))
                       and e.fil_cod <> 900
                       and a.pss_ctr_data_adesao between
                           to_date('''||v_dt_ini||''', ''dd/mm/yyyy hh24:mi:ss'') and 
                           to_date('''||v_dt_fim||''', ''dd/mm/yyyy hh24:mi:ss'')
                     group by e.fil_cod';
      v_index := '(fil_cod)';

      Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                      p_comando => v_comando,
                      p_index => v_index,
                      p_cod_erro => p_cod_erro,
                      p_descr_erro => p_descr_erro);

      if p_cod_erro is not null then
        return;
      end if;

      -- SRV_REALZFI_PROT_FIN
      v_nm_tabela := 'SRV_REALZFI_PROT_FIN_' || p_ano || p_mes;
      v_comando := 'select e.fil_cod fil_cod,
                           47 cod_indic,
                           ''PROTECAO FINANCEIRA'' descr_indic,
                           count(*) qtde
                      from ccm_pss_contrato_cliente'||v_DbLink||' a,
                           ccm_pss_canal_venda'||v_DbLink||' e
                     where a.pss_cnl_codigo = e.pss_cnl_codigo
                       and a.pss_prd_codigo in (129,150)
                       and (a.pss_ctr_in_situacao = 1 or
                           (a.pss_ctr_in_situacao = 2 and exists
                            (select 1
                                from SRV_TMP_CTR_PSF_CANC_' || p_ano || p_mes || ' f
                               where f.pss_ctr_codigo = a.pss_ctr_codigo)))
                       and e.fil_cod <> 900
                       and a.pss_ctr_data_adesao between
                           to_date('''||v_dt_ini||''', ''dd/mm/yyyy hh24:mi:ss'') and 
                           to_date('''||v_dt_fim||''', ''dd/mm/yyyy hh24:mi:ss'')
                     group by e.fil_cod';
      v_index := '(fil_cod)';

      Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                      p_comando => v_comando,
                      p_index => v_index,
                      p_cod_erro => p_cod_erro,
                      p_descr_erro => p_descr_erro);

      if p_cod_erro is not null then
        return;
      end if;

      -- SRV_REALZFI_PR_CEL_FIN
      v_nm_tabela := 'SRV_REALZFI_PR_CEL_FIN_' || p_ano || p_mes;
      v_comando := 'select fil_cod,
                           cod_indic,
                           descr_indic,
                           qtde
                      from SRV_REALZFI_PROT_FIN_' || p_ano || p_mes ||'
                   UNION ALL
                    select fil_cod,
                           cod_indic,
                           descr_indic,
                           qtde
                      from SRV_REALZFI_PROT_CEL_' || p_ano || p_mes;
      v_index := '(fil_cod)';

      Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                      p_comando => v_comando,
                      p_index => v_index,
                      p_cod_erro => p_cod_erro,
                      p_descr_erro => p_descr_erro);

      if p_cod_erro is not null then
        return;
      end if;

      -- SRV_REALZFU_PR_CEL_FIN
      v_nm_tabela := 'SRV_REALZFU_PR_CEL_FIN_' || p_ano || p_mes;
      v_comando := ' select d.fil_cod fil_cod,
                            a.cli_cpf cli_cpf,
                            21 cod_indic,
                            ''PROTECAO CELULAR MARISA'' descr_indic,
                            c.pss_vnd_cpf cpf_vendedor,
                            a.pss_prd_codigo cod_prod,
                            ''PROTECAO CELULAR MARISA'' descr_prod,
                            a.pss_ctr_codigo contrato_titular,
                            a.pss_ctr_codigo contrato_dependente,
                            a.pss_ctr_data_adesao data_adesao_titular
                       from ccm_pss_contrato_cliente'||v_DbLink||'       a
                      inner join ccm_pss_lancamento_premio'||v_DbLink||' b on b.pss_ctr_codigo = a.pss_ctr_codigo
                                                                           and b.pss_lnc_nro_parcela = 1
                                                                           and b.pss_lnc_tipo = 3
                                                                           and b.pss_lnc_valor_pago >= b.pss_lnc_vr_lanc_premio
                                                                           and (b.pss_lnc_data_pagto - 180) <= a.pss_ctr_data_adesao
                                                                           and b.pss_lnc_data_pagto between
                                                                               to_date('''||v_dt_ini||''', ''dd/mm/yyyy hh24:mi:ss'') and 
                                                                               to_date('''||v_dt_fim||''', ''dd/mm/yyyy hh24:mi:ss'')
                       left join ccm_pss_vendedor'||v_DbLink||'          c on c.pss_vnd_codigo = a.pss_vnd_codigo
                       left join ccm_pss_canal_venda'||v_DbLink||'       d on d.pss_cnl_codigo = a.pss_cnl_codigo
                      where a.pss_prd_codigo in (119,151)
                   UNION ALL
                     select d.fil_cod fil_cod,
                            a.cli_cpf cli_cpf,
                            47 cod_indic,
                            ''PROTECAO FINANCEIRA'' descr_indic,
                            c.pss_vnd_cpf cpf_vendedor,
                            a.pss_prd_codigo cod_prod,
                            ''PROTECAO FINANCEIRA'' descr_prod,
                            a.pss_ctr_codigo contrato_titular,
                            a.pss_ctr_codigo contrato_dependente,
                            a.pss_ctr_data_adesao data_adesao_titular
                       from ccm_pss_contrato_cliente'||v_DbLink||'       a
                      inner join ccm_pss_lancamento_premio'||v_DbLink||' b on b.pss_ctr_codigo = a.pss_ctr_codigo
                                                                           and b.pss_lnc_nro_parcela = 1
                                                                           and b.pss_lnc_tipo = 3
                                                                           and b.pss_lnc_valor_pago >= b.pss_lnc_vr_lanc_premio
                                                                           and (b.pss_lnc_data_pagto - 180) <= a.pss_ctr_data_adesao
                                                                           and b.pss_lnc_data_pagto between
                                                                               to_date('''||v_dt_ini||''', ''dd/mm/yyyy hh24:mi:ss'') and 
                                                                               to_date('''||v_dt_fim||''', ''dd/mm/yyyy hh24:mi:ss'')
                       left join ccm_pss_vendedor'||v_DbLink||'          c on c.pss_vnd_codigo = a.pss_vnd_codigo
                       left join ccm_pss_canal_venda'||v_DbLink||'       d on d.pss_cnl_codigo = a.pss_cnl_codigo
                      where a.pss_prd_codigo in (129,150)';
      v_index := '(fil_cod, cpf_vendedor)';

      Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                      p_comando => v_comando,
                      p_index => v_index,
                      p_cod_erro => p_cod_erro,
                      p_descr_erro => p_descr_erro);

      if p_cod_erro is not null then
        return;
      end if;

  end Prc_Tmp_ProtCel_ProtFin;




  /*---------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------
  -- PROVISORIO: => PROCESSO TEMPORARIO PARA APURACAO (CALL CENTER)
  -----------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------*/
  procedure Prc_Tmp_CallCenter (p_ano in varchar2,
                                p_mes in varchar2,
                                p_cod_erro out number,
                                p_descr_erro out varchar2) is

    v_dt_ini varchar2(20);
    v_dt_fim varchar2(20);
    v_index varchar2(500);
    v_comando varchar2(4000);
    v_comando1 varchar2(4000);
    v_comando2 varchar2(4000);
    v_nm_tabela varchar2(30);

    begin

      -- datas
      v_dt_ini  := '01/' || p_mes || '/' || p_ano || ' ' || '00:00:00';
      select to_char(last_day(to_date(('01/'||p_mes||'/'||p_ano),'dd/mm/yyyy')),'dd/mm/yyyy') || ' 23:59:59' into v_dt_fim from dual;


     -----------------------------------------------------------------------------------------------
     -- CARTAO MARISA APROVADO PL E ITAU CALL CENTER
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CT_AP_CC
     v_nm_tabela := 'SRV_REALZFU_CT_AP_CC_' || p_ano || p_mes;
     v_comando := 'select b.cli_cpf,
                           18 cod_indic,
                          ''CARTAO MARISA APROVADO ITAU'' descr_indic,
                           b.data_aceite,
                           b.cpf_vendedor,
                           a.fil_cod
                      from ccm_usuario'||v_DbLink||' a
                     inner join ccm_itau_cliente'||v_DbLink||' b on b.cpf_vendedor = a.usu_cpf
                                                         and b.stat_cod not in (1, 8, 13, 96, 97, 98)
                                                         and b.data_aceite between to_date(''' || v_dt_ini ||''','' dd/mm/yyyy hh24:mi:ss'')
                                                                               and to_date(''' || v_dt_fim ||''','' dd/mm/yyyy hh24:mi:ss'')
                     inner join ccm_cliente'||v_DbLink||' c on c.cli_cpf = b.cli_cpf
                     where a.fil_cod in (900,1021)
                     order by 1,3';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;


     -----------------------------------------------------------------------------------------------
     -- BOLSA PROTEGIDA CALL CENTER
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_B_PROT_CC
     v_nm_tabela := 'SRV_REALZFU_B_PROT_CC_' || p_ano || p_mes;
     v_comando1 := 'SELECT E.FIL_COD                                  FIL_COD
                          ,A.CLI_CPF                                  CLI_CPF
                          ,C.PSS_VND_CPF                              CPF_VENDEDOR
                          ,A.PSS_PRD_CODIGO                           COD_INDIC
                          ,''BOLSA PROTEGIDA PL''                     DESCR_INDIC
                          ,A.PSS_CTR_CODIGO                           CONTRATO_TITULAR
                          ,TRUNC(A.PSS_CTR_DATA_ADESAO)               DATA_ADESAO_TITULAR
                          ,TO_CHAR(A.PSS_CTR_DATA_ADESAO,''MM/YYYY'') MES_ADESAO
                      FROM CCM_PSS_CONTRATO_CLIENTE'||v_DbLink||' A,
                           CCM_PSS_PRODUTO'||v_DbLink||'          B,
                           CCM_PSS_VENDEDOR'||v_DbLink||'         C,
                           CCM_PSS_CANAL_VENDA'||v_DbLink||'      E
                     WHERE E.FIL_COD IN(900,991,1021)
                       AND A.PSS_CNL_CODIGO = E.PSS_CNL_CODIGO
                       AND A.PSS_VND_CODIGO = C.PSS_VND_CODIGO(+)
                       AND A.PSS_PRD_CODIGO = B.PSS_PRD_CODIGO(+)
                       AND A.PSS_PRD_CODIGO IN (17,20,21,41,64,65,66,67,69,96,105)
                       AND A.PSS_CTR_IN_SITUACAO IN (1)
                       AND A.PSS_CTR_DATA_ADESAO between to_date('''|| v_dt_ini ||''',''dd/mm/yyyy hh24:mi:ss'')
                                                     and to_date('''|| v_dt_fim ||''',''dd/mm/yyyy hh24:mi:ss'')

                    UNION ALL

                    SELECT B.FIL_COD
                          ,A.CLI_CPF
                          ,B.USU_CPF
                          ,a.cod_prod_cartao
                          ,''BOLSA PROTEGIDA ITAU''
                          ,NULL
                          ,TRUNC(A.DATA_ACEITE)
                          ,TO_CHAR(A.DATA_ACEITE,''MM/YYYY'')
                      FROM CCM_ITAU_CLIENTE'||v_DbLink||'     A
                         , CCM_ITAU_CLIENTE_LOG'||v_DbLink||' B
                     WHERE B.FIL_COD IN(900,991,1021)
                       AND A.CLI_CPF = B.CLI_CPF
                       AND A.DATA_ACEITE = B.DT_LOG
                       AND B.STAT_COD = 2
                       AND B.STAT_COD_ANT = 1
                       AND A.COD_PROD_CARTAO = 1850
                       AND A.FLAG_SEG_PERDA_ROUBO = ''S''
                       AND B.COD_MOT_RECUSA IS NULL
                       AND A.DATA_ACEITE BETWEEN TO_DATE('''|| v_dt_ini ||''',''DD/MM/YYYY HH24:MI:SS'')
                                             AND TO_DATE('''|| v_dt_fim ||''',''DD/MM/YYYY HH24:MI:SS'')
                    UNION ALL

                    select e.fil_cod
                          ,a.cli_cpf
                          ,c.pss_vnd_cpf
                          ,a.pss_prd_codigo
                          ,''BOLSA PROTEGIDA PL''
                          ,TO_NUMBER(NULL)
                          ,trunc(f.pss_dpt_dt_adesao)
                          ,to_char(f.pss_dpt_dt_adesao,''mm/yyyy'')
                      from ccm_pss_dependente'||v_DbLink||'       f,
                           ccm_pss_vendedor'||v_DbLink||'         c,
                           ccm_pss_canal_venda'||v_DbLink||'      e,
                           ccm_pss_contrato_cliente'||v_DbLink||' a,
                           ccm_pss_produto'||v_DbLink||'          b
                     where e.fil_cod in(900,991,1021)
                       and a.pss_ctr_codigo = f.pss_ctr_codigo
                       and f.pss_vnd_codigo = c.pss_vnd_codigo
                       and a.pss_prd_codigo = b.pss_prd_codigo
                       and f.pss_cnl_codigo = e.pss_cnl_codigo
                       and f.stat_cod = 1
                       and f.pss_dpt_dt_adesao between TO_DATE('''|| v_dt_ini ||''',''DD/MM/YYYY HH24:MI:SS'')
                                                   AND TO_DATE('''|| v_dt_fim ||''',''DD/MM/YYYY HH24:MI:SS'')';
                  v_comando2 := '  UNION ALL

                    select u.fil_cod,
                           d.cli_cpf,
                           d.cpf_vendedor,
                           d.prod_cod,
                           ''BOLSA PROTEGIDA ITAU'',
                           TO_NUMBER(NULL),
                           trunc(d.data_adesao),
                           to_char(d.data_adesao,''mm/yyyy'')
                      from ccm_itau_cliente_produto_depen'||v_DbLink||' d,
                           ccm_pss_produto'||v_DbLink||'                p,
                           ccm_status'||v_DbLink||'                     sta,
                           ccm_usuario'||v_DbLink||'                    u,
                           ccm_itau_cliente'||v_DbLink||'               c
                     where u.fil_cod in (900,991,1021)
                       and d.cli_cpf = c.cli_cpf
                       and d.stat_cod = sta.stat_cod
                       and d.prod_cod = p.pss_prd_codigo
                       and d.stat_tabela = sta.stat_tabela
                       and u.usu_cpf = d.cpf_vendedor
                       and c.cod_mot_recusa is null
                       and d.stat_cod <> 2
                       and c.stat_cod in (5, 6, 7, 9, 10, 11)
                       and d.data_adesao between TO_DATE('''|| v_dt_ini ||''',''DD/MM/YYYY HH24:MI:SS'')
                                             AND TO_DATE('''|| v_dt_fim ||''',''DD/MM/YYYY HH24:MI:SS'')

       UNION ALL

                    select u.fil_cod,
                           i.cli_cpf,
                           i.cpf_vendedor,
                           i.prod_cod cod_prod_itau,
                           ''BOLSA PROTEGIDA ITAU'',
                           to_number(null),
                           trunc(i.data_adesao),
                           to_char(i.data_adesao,''mm/yyyy'')
                      from ccm_itau_cliente_produto'||v_DbLink||' i,
                           ccm_pss_produto'||v_DbLink||'          p,
                           ccm_status'||v_DbLink||'               sta,
                           ccm_usuario'||v_DbLink||'              u,
                           ccm_itau_cliente'||v_DbLink||'         c
                      where u.fil_cod in(900,991,1021)
                        and i.cli_cpf = c.cli_cpf
                        and i.stat_cod = sta.stat_cod
                        and i.prod_cod = p.pss_prd_codigo
                        and i.stat_tabela = sta.stat_tabela
                        and c.cod_mot_recusa is null
                        and i.stat_cod <> 2
                        and c.stat_cod in (5, 6, 7, 9, 10, 11)
                        and u.usu_cpf = i.cpf_vendedor
                        and i.data_adesao between TO_DATE('''|| v_dt_ini ||''',''DD/MM/YYYY HH24:MI:SS'')
                                              AND TO_DATE('''|| v_dt_fim ||''',''DD/MM/YYYY HH24:MI:SS'')';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela2(p_tabela => v_nm_tabela,
                     p_comando1 => v_comando1,
                     p_comando2 => v_comando2,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

  end Prc_Tmp_CallCenter;




  /*---------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------
   -- APURA REALIZADO INDICADORES
  -----------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------*/
  procedure Prc_Apura_Realizado ( p_num_ano      in number
                                 ,p_num_mes      in number
                                 ,p_cod_erro     out number
                                 ,p_descr_erro   out varchar2) is

      --
      v_ano varchar2(4);
      v_mes varchar2(2);
      v_index varchar2(500);
      v_comando varchar2(5000);
      v_nm_tabela varchar2(30);

   ---------------------------------------------------------------------------
   begin

     v_ano := trim(to_char(p_num_ano, '0000'));
     v_mes := trim(to_char(p_num_mes, '00'));


     -- EXEC --

     -----------------------------------------------------------------------------------------------
     -- VENDAS
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFI_VENDAS
     v_nm_tabela := 'SRV_REALZFI_VENDAS_' || v_ano || v_mes;
     v_comando := 'Select Cod_Indic, Descr_Indic, Fil_Cod, Vlr_Vdas_Bruta, Vlr_Devolucao, Vlr_Realizado
                     From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                    Where Nu_Ano = '||p_num_ano||'
                      And Nu_Mes =  '||p_num_mes||'
                      And Cod_Metrica = 1';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- VENDA COM JUROS PL
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CPR_JUR
     v_nm_tabela := 'SRV_REALZFU_CPR_JUR_' || v_ano || v_mes;
     v_comando := 'Select Cli_Cpf, Plast_Titular, Plast_Compra, Fat_Transacao_Cod, Cod_Autorizacao,
                           Fat_Dt_Transacao, Data_Adesao_Titular, Fil_Cod, Cod_Indic, Descr_Indic,
                           Cod_Prod, Descr_Produto, Vlr_Bruto_Trans, Vlr_Juros, Vlr_Liq_Trans,
                           Cpr_Prod_Cod_Comp_Tranq, Prod_Desc_Comp_Tranq, Vlr_Parc_Seguro,
                           Vlr_Total_Seguro, Matricula_Vendedor, Cod_Status_Cpra, Eve_Cod,
                           Cpr_Qt_Prc, Cpf_Vendedor
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica In (3, 56, 57)
                       And To_Char(Cod_Prod) In
                           (Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''VENDA COM JUROS PL''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CPR_JUR
     v_nm_tabela := 'SRV_REALZFI_CPR_JUR_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Sum(Qtde) Qtde,
                           Sum(Vlr_Realz_Bruto_Cpr_Jur) Vlr_Realz_Bruto_Cpr_Jur,
                           Sum(Vlr_Realz_Juros_Cpr_Jur) Vlr_Realz_Juros_Cpr_Jur,
                           Sum(Vlr_Realz_Cpr_Jur) Vlr_Realz_Cpr_Jur
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where To_Char(Cod_Prod) In
                           (Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''VENDA COM JUROS PL''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)
                       And Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 4
                     Group By Fil_Cod, Cod_Indic, Descr_Indic';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZ_TOD_FU_VJ_PL
     v_nm_tabela := 'SRV_REALZ_TOD_FU_VJ_PL_' || v_ano || v_mes;
     v_comando := 'Select Cli_Cpf, Plast_Titular, Plast_Compra, Fat_Transacao_Cod, Cod_Autorizacao,
                           Fat_Dt_Transacao, Fil_Cod, Cod_Indic, Descr_Indic, Cod_Prod,
                           Descr_Produto, Vlr_Bruto_Trans, Vlr_Juros, Vlr_Liq_Trans,
                           Cpr_Prod_Cod_Comp_Tranq, Prod_Desc_Comp_Tranq, Vlr_Parc_Seguro,
                           Vlr_Total_Seguro, Matricula_Vendedor, Cod_Status_Cpra, Eve_Cod,
                           Cpr_Qt_Prc
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 2';
     v_index := '(fil_cod, matricula_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_TMP_META_VENDAS_PL
     v_nm_tabela := 'SRV_TMP_META_VENDAS_PL_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Vlr_Liq_Vda_Total_Pl_Ita
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 5';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- COMPRA TRANQUILA PL
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CPRA_TRANQ
     v_nm_tabela := 'SRV_REALZFU_CPRA_TRANQ_' || v_ano || v_mes;
     v_comando := 'Select Cli_Cpf, Plast_Titular, Plast_Compra, Fat_Transacao_Cod, Cod_Autorizacao,
                           Fat_Dt_Transacao, Fil_Cod, Cod_Prod, Cod_Indic, Descr_Indic,
                           Vlr_Bruto_Trans, Vlr_Juros, Vlr_Liq_Trans, Cpr_Prod_Cod_Comp_Tranq,
                           Vlr_Parc_Seguro, Vlr_Total_Seguro, Matricula_Vendedor, Cod_Status_Cpra,
                           Eve_Cod, Cpr_Qt_Prc, Fat_Transacao_Cod_Pg
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 6';
     v_index := '(fil_cod, matricula_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CPRA_TRANQ
     v_nm_tabela := 'SRV_REALZFI_CPRA_TRANQ_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde, Vlr_Realz_Bruto_Cpr_Tranq,
                           Vlr_Realz_Juros_Cpr_Tranq, Vlr_Realz_Cpra_Tranq
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 7';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_TMP_META_CPR_TRANQ
     v_nm_tabela := 'SRV_TMP_META_CPR_TRANQ_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 8';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- VENDA COM JUROS ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CPR_JUR_IT
     v_nm_tabela := 'SRV_REALZFU_CPR_JUR_IT_' || v_ano || v_mes;
     v_comando := 'Select Id_Movto_Fin, Num_Nsu, Num_Cupom, Fat_Dt_Transacao, Data_Adesao_Titular,
                           Fil_Cod, Cod_Indic, Descr_Indic, Cod_Fam_Prod, Descr_Fam_Prod, Cod_Prod,
                           Descr_Produto, Vlr_Bruto_Trans, Vlr_Juros, Vlr_Liq_Trans,
                           Cpr_Prod_Cod_Comp_Tranq, Prod_Desc_Comp_Tranq, Vlr_Parc_Seguro,
                           Vlr_Total_Seguro, Matricula_Vendedor, Eve_Cod, Cpf_Vendedor, Qtd_Parc
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica In (10, 58, 59)';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CPR_JUR_IT
     v_nm_tabela := 'SRV_REALZFI_CPR_JUR_IT_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde, Vlr_Realz_Bruto_Cpr_Jur,
                           Vlr_Realz_Juros_Cpr_Jur, Vlr_Realz_Cpr_Jur
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 11';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZ_TOD_FU_VJ_IT
     v_nm_tabela := 'SRV_REALZ_TOD_FU_VJ_IT_' || v_ano || v_mes;
     v_comando := 'Select Id_Movto_Fin, Num_Nsu, Num_Cupom, Fat_Dt_Transacao, Fil_Cod, Cod_Indic,
                           Descr_Indic, Cod_Fam_Prod, Descr_Fam_Prod, Cod_Prod, Descr_Produto,
                           Vlr_Bruto_Trans, Vlr_Juros, Vlr_Liq_Trans, Cpr_Prod_Cod_Comp_Tranq,
                           Prod_Desc_Comp_Tranq, Vlr_Parc_Seguro, Vlr_Total_Seguro,
                           Matricula_Vendedor, Eve_Cod, Cpf_Vendedor, Qtd_Parc
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 9';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_TMP_META_VENDAS_I
     v_nm_tabela := 'SRV_TMP_META_VENDAS_I_' || v_ano || v_mes;
     v_comando := 'Select fil_cod, Vlr_Movto
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 12';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- COMPRA TRANQUILA ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CPRA_TRANQI
     v_nm_tabela := 'SRV_REALZFU_CPRA_TRANQI_' || v_ano || v_mes;
     v_comando := 'Select Id_Movto_Fin, Num_Nsu, Num_Cupom, Fat_Dt_Transacao, Data_Adesao_Titular,
                           Fil_Cod, Cod_Prod, Descr_Produto, Vlr_Bruto_Trans, Vlr_Juros,
                           Vlr_Liq_Trans, Cpr_Prod_Cod_Comp_Tranq, Prod_Desc_Comp_Tranq,
                           Vlr_Parc_Seguro, Vlr_Total_Seguro, Matricula_Vendedor, Cod_Indic,
                           Descr_Indic, Qtd_Parc, Cpf_Vendedor
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where To_Char(Cpr_Prod_Cod_Comp_Tranq) In
                           (Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''COMPRA TRANQUILA ITAU''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)
                       And Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 14';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CPRA_TRANQI
     v_nm_tabela := 'SRV_REALZFI_CPRA_TRANQI_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Sum(Qtde) Qtde,
                           Sum(Vlr_Realz_Bruto_Cpr_Tranq) Vlr_Realz_Bruto_Cpr_Tranq,
                           Sum(Vlr_Realz_Juros_Cpr_Tranq) Vlr_Realz_Juros_Cpr_Tranq,
                           Sum(Vlr_Realz_Cpra_Tranq) Vlr_Realz_Cpra_Tranq
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where To_Char(Cpr_Prod_Cod_Comp_Tranq) In
                           (Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''COMPRA TRANQUILA ITAU''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)
                       And Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 15
                     Group By Fil_Cod, Cod_Indic, Descr_Indic';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZ_TOD_FU_CT_IT
     v_nm_tabela := 'SRV_REALZ_TOD_FU_CT_IT_' || v_ano || v_mes;
     v_comando := 'Select Id_Movto_Fin, Num_Nsu, Num_Cupom, Fat_Dt_Transacao, Fil_Cod, Cod_Prod,
                           Descr_Produto, Vlr_Bruto_Trans, Vlr_Juros, Vlr_Liq_Trans,
                           Cpr_Prod_Cod_Comp_Tranq, Prod_Desc_Comp_Tranq, Vlr_Parc_Seguro,
                           Vlr_Total_Seguro, Matricula_Vendedor, Cod_Indic, Descr_Indic, Qtd_Parc,
                           Cpf_Vendedor
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where To_Char(Cpr_Prod_Cod_Comp_Tranq) In
                           (Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''COMPRA TRANQUILA ITAU''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)
                       And Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 13';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_TMP_META_CPR_TRANQI
     v_nm_tabela := 'SRV_TMP_META_CPR_TRANQI_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Qtde_Vds_Parc_Pl_Ita
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 16';
     v_index := '(Fil_Cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- VENDA COM JUROS PL E ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_VJ_PL_ITAU
     v_nm_tabela := 'SRV_REALZFU_VJ_PL_ITAU_' || v_ano || v_mes;
     v_comando := 'Select fil_cod, matricula_vendedor, cpf_vendedor, cod_prod, descr_produto
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where to_char(cod_prod) In
                           (Select ''0'' cod_prod
                              From dual
                            Union All
                            Select ip.cod_prod
                              From srv_tipo_rem_var t, srv_grupo_indicador g, srv_indicador i,
                                   srv_indicador_produto ip, srv_produto p
                             Where t.cod_tipo_rem_var = g.cod_tipo_rem_var
                               And t.descr_tipo_rem_var = ''REMUNERACAO_LOJA''
                               And g.cod_grp_indic = i.cod_grp_indic
                               And i.descr_indic = ''VENDA COM JUROS PL''
                               And i.flg_indic_ativ = ''S''
                               And i.cod_indic = ip.cod_indic
                               And ip.cod_orig_prod = p.cod_orig_prod
                               And ip.cod_prod = p.cod_prod)
                       And nu_ano = '||p_num_ano||'
                       And nu_mes = '||p_num_mes||'
                       And cod_metrica In (17, 60, 61)';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_VJ_PL_ITAU
     v_nm_tabela := 'SRV_REALZFI_VJ_PL_ITAU_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Sum(Qtde) Qtde,
                           Sum(Vlr_Realz_Bruto_Cpr_Jur) Vlr_Realz_Bruto_Cpr_Jur,
                           Sum(Vlr_Realz_Juros_Cpr_Jur) Vlr_Realz_Juros_Cpr_Jur,
                           Sum(Vlr_Realz_Cpr_Jur) Vlr_Realz_Cpr_Jur
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where To_Char(Cod_Prod) In
                           (Select ''0'' Cod_Prod
                              From Dual
                            Union All
                            Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''VENDA COM JUROS PL''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)
                       And Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 18
                     Group By Fil_Cod, Cod_Indic, Descr_Indic';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- COMPRA TRANQUILA PL E ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CT_PL_ITAU
     v_nm_tabela := 'SRV_REALZFU_CT_PL_ITAU_' || v_ano || v_mes;
     v_comando := 'Select Cod_Indic, Descr_Indic, Fil_Cod, Cpf_Vendedor
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where To_Char(Cpr_Prod_Cod_Comp_Tranq) In
                           (Select ''418'' Cod_Prod
                              From Dual
                            Union All
                            Select ''437'' Cod_Prod
                              From Dual
                            Union All
                            Select ''0'' Cod_Prod
                              From Dual
                            Union All
                            Select Ip.Cod_Prod
                              From Srv_Tipo_Rem_Var t, Srv_Grupo_Indicador g, Srv_Indicador i,
                                   Srv_Indicador_Produto Ip, Srv_Produto p
                             Where t.Cod_Tipo_Rem_Var = g.Cod_Tipo_Rem_Var
                               And t.Descr_Tipo_Rem_Var = ''REMUNERACAO_LOJA''
                               And g.Cod_Grp_Indic = i.Cod_Grp_Indic
                               And i.Descr_Indic = ''COMPRA TRANQUILA ITAU''
                               And i.Flg_Indic_Ativ = ''S''
                               And i.Cod_Indic = Ip.Cod_Indic
                               And Ip.Cod_Orig_Prod = p.Cod_Orig_Prod
                               And Ip.Cod_Prod = p.Cod_Prod)
                       And Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 19
                     Group By Cod_Indic, Descr_Indic, Fil_Cod, Cpf_Vendedor';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CT_PL_ITAU
     v_nm_tabela := 'SRV_REALZFI_CT_PL_ITAU_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Cpr_Prod_Cod_Comp_Tranq,
                           Prod_Desc_Comp_Tranq, Qtde, Vlr_Realz_Bruto_Cpr_Tranq,
                           Vlr_Realz_Juros_Cpr_Tranq, Vlr_Realz_Cpra_Tranq
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 20';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- ASSISTENCIA ODONTOLOGICA LOJA
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_ASS_ODO_LJ
     v_nm_tabela := 'SRV_REALZFU_ASS_ODO_LJ_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Cod_Prod,
                           Descr_Prod, Contrato_Titular, Data_Adesao_Titular
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 21';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_ASS_ODO_LJ
     v_nm_tabela := 'SRV_REALZFI_ASS_ODO_LJ_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Cod_Prod, Descr_Prod, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 22';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- BOLSA PROTEGIDA
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_MAR_PROT_LJ
     v_nm_tabela := 'SRV_REALZFU_MAR_PROT_LJ_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Cod_Prod,
                           Descr_Prod, Contrato_Titular, Contrato_Dependente, Data_Adesao_Titular
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 23';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_MAR_PROT_LJ
     v_nm_tabela := 'SRV_REALZFI_MAR_PROT_LJ_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 24';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- BOLSA PROTEGIDA ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_MAR_PROT_IT
     v_nm_tabela := 'SRV_REALZFU_MAR_PROT_IT_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Cod_Prod,
                           Descr_Prod, Contrato_Titular
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 25';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_MAR_PROT_IT
     v_nm_tabela := 'SRV_REALZFI_MAR_PROT_IT_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 51';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- BOLSA PROTEGIDA PL E ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_BPROT_PL_IT
     v_nm_tabela := 'SRV_REALZFU_BPROT_PL_IT_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Cod_Prod,
                           Data_Adesao_Titular
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 26';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_BPROT_PL_IT
     v_nm_tabela := 'SRV_REALZFI_BPROT_PL_IT_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 27';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- CARTAO MARISA APROVADO PL E ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CART_APROV
     v_nm_tabela := 'SRV_REALZFU_CART_APROV_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Data_Cadastro,
                           Flg_Doc_Digitalizado
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 29';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CART_APROV
     v_nm_tabela := 'SRV_REALZFI_CART_APROV_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 30';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- CARTAO MARISA ATIVADO PL
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CART_ATIV
     v_nm_tabela := 'SRV_REALZFU_CART_ATIV_' || v_ano || v_mes;
     v_comando := 'Select Prim_Dt_Cadastro, Cpf_Vendedor, Cli_Cpf, Num_Campanha, Fil_Cod, Cod_Indic,
                           Descr_Indic, Dt_Compra, Data_Adesao_Titular
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 32';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CART_ATIV
     v_nm_tabela := 'SRV_REALZFI_CART_ATIV_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 33';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- BONUS CELULAR
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_BONUS_CEL
     v_nm_tabela := 'SRV_REALZFU_BONUS_CEL_' || v_ano || v_mes;
     v_comando := 'Select Prim_Dt_Cadastro, Cpf_Vendedor, Cli_Cpf, Num_Campanha, Fil_Cod, Cod_Indic,
                           Descr_Indic, Dt_Compra
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 34';
     v_index := '(cli_cpf)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_BONUS_CEL
     v_nm_tabela := 'SRV_REALZFI_BONUS_CEL_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 35';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- PARTICIPACAO DO CARTAO %
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_PART_CARTAO
     v_nm_tabela := 'SRV_REALZFU_PART_CARTAO_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Matricula, Cpf_Vendedor, Fat_Dt_Transacao, Tipo_Venda,
                           Dt_Processamento
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 36';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_PART_CARTAO
     v_nm_tabela := 'SRV_REALZFI_PART_CARTAO_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Vlr_Realizado, Vlr_Liq_Trans, Vlr_Liq_Trans_i, Vnd_Pl_Ita, Part
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 37';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- TOMBAMENTO ITAUCARD
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CART_TOMB
     v_nm_tabela := 'SRV_REALZFU_CART_TOMB_' || v_ano || v_mes;
     v_comando := 'Select Cli_Cpf, Cod_Indic, Descr_Indic, Data_Aceite, Data_Adesao_Titular,
                           Cpf_Vendedor, Fil_Cod, Stat_Cod, Stat_Descr
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 38';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CART_TOMB
     v_nm_tabela := 'SRV_REALZFI_CART_TOMB_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 39';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- DESBLOQUEIO ITAUCARD
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CT_IT_DESB
     v_nm_tabela := 'SRV_REALZFU_CT_IT_DESB_' || v_ano || v_mes;
     v_comando := 'Select Cli_Cpf, Cod_Indic, Descr_Indic, Data_Aceite, Data_Adesao_Titular,
                           Cpf_Vendedor, Fil_Cod, Stat_Cod, Stat_Descr
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 40';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CT_IT_DESB
     v_nm_tabela := 'SRV_REALZFI_CT_IT_DESB_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 41';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- PROTECAO CELULAR MARISA
     -----------------------------------------------------------------------------------------------

     -- ////////////////////////////////////////////////////////////////
     -- CHAMADA TEMPORARIA - ATE IMPLEMENTACAO DE METRICAS
     -- ////////////////////////////////////////////////////////////////
     Prc_Tmp_ProtCel_ProtFin (p_ano => v_ano,
                              p_mes => v_mes,
                              p_cod_erro => p_cod_erro,
                              p_descr_erro => p_descr_erro);

     -----------------------------------------------------------------------------------------------
     -- MARISA MULHER/AUTO PROTECAO/CASA PROTEGIDA
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_SEG_MAR_MUL
     v_nm_tabela := 'SRV_REALZFU_SEG_MAR_MUL_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Cod_Prod,
                           Descr_Prod, Contrato_Titular, Contrato_Dependente, Data_Adesao_Titular
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 44';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_SEG_MAR_MUL
     v_nm_tabela := 'SRV_REALZFI_SEG_MAR_MUL_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 45';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- CARTAO ADICIONAL PL E ITAU
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_CT_ADIC
     v_nm_tabela := 'SRV_REALZFU_CT_ADIC_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Cod_Indic, Descr_Indic, Cpf_Vendedor, Adi_Dt_Cadast,
                           Tipo_Cartao
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 46';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_CT_ADIC
     v_nm_tabela := 'SRV_REALZFI_CT_ADIC_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 47';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- PARCELAMENTO DE FATURAS
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFI_PARC_FAT
     v_nm_tabela := 'SRV_REALZFI_PARC_FAT_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cod_Indic, Descr_Indic, Qtde
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 48';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -----------------------------------------------------------------------------------------------
     -- EMPRESTIMO_PESSOAL_SAX
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFU_EMP_SEG_SAX
     v_nm_tabela := 'SRV_REALZFU_EMP_SEG_SAX_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Cli_Cpf, Dt_Captura, Dt_Formaliz_Emprest, Dt_Proposta,
                           Dt_Operacao, Nome_Usu_Captura, Cpf_Vendedor, Login_Usu_Captura,
                           Cod_Contrato, Status_Liquid, Formalizacao, Vlr_Bruto_Emprest,
                           Vlr_Liquido_Emprest, Qtd_Parc, Vlr_Parcelas, Cod_Produto_Str,
                           Num_Transacao, Resaque, Situacao_Contrato, Data_Liquidacao,
                           Data_Cancelamento, Seguro, Cpf_Usu_Indicador, Mes_Proposta,
                           Flg_Doc_Digitalizado
                      From Ccm.Srv_Funcionario_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 49';
     v_index := '(fil_cod, cpf_vendedor)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;

     -- SRV_REALZFI_EMP_SEG_SAX
     v_nm_tabela := 'SRV_REALZFI_EMP_SEG_SAX_' || v_ano || v_mes;
     v_comando := 'Select Fil_Cod, Quant_Seguro_Real, Quant_Ep_Real, Vlr_Liquido_Emprest,
                           Flg_Doc_Digitalizado
                      From Ccm.Srv_Filial_Metrica'||v_DbLink||'
                     Where Nu_Ano = '||p_num_ano||'
                       And Nu_Mes = '||p_num_mes||'
                       And Cod_Metrica = 50';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;


     -----------------------------------------------------------------------------------------------
     -- CALL CENTER
     -----------------------------------------------------------------------------------------------
     -- ////////////////////////////////////////////////////////////////
     -- CHAMADA TEMPORARIA - ATE IMPLEMENTACAO DE METRICAS
     -- ////////////////////////////////////////////////////////////////
     Prc_Tmp_CallCenter (p_ano => v_ano,
                         p_mes => v_mes,
                         p_cod_erro => p_cod_erro,
                         p_descr_erro => p_descr_erro);

     -- FIM EXEC --

  end Prc_Apura_Realizado;



  /*---------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------
   -- APURA REALIZADO INDICADORES (IMPORTACAO)
  -----------------------------------------------------------------------------------------------
  ---------------------------------------------------------------------------------------------*/
  procedure Prc_Apura_RealizadoImportacao ( p_num_ano      in number
                                           ,p_num_mes      in number
                                           ,p_cod_erro     out number
                                           ,p_descr_erro   out varchar2) is

    --
    v_ano varchar2(4);
    v_mes varchar2(2);
    v_index varchar2(500);
    v_comando varchar2(5000);
    v_nm_tabela varchar2(30);

    ---------------------------------------------------------------------------
    begin

      v_ano := trim(to_char(p_num_ano, '0000'));
      v_mes := trim(to_char(p_num_mes, '00'));


      -- EXEC --

     -----------------------------------------------------------------------------------------------
     -- PPT - PECAS POR TICKET
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFI_PPT
     v_nm_tabela := 'SRV_REALZFI_PPT_' || v_ano || v_mes;
     v_comando := 'select  cod_fil fil_cod,
                           23 cod_indic,
                           ''PPT - PECAS POR TICKET'' descr_indic,
                           num_realz qtde
                      from srv_realizado_filial_indic'||v_DbLink||'
                     where num_ano = ' || p_num_ano ||'
                       and num_mes = ' || p_num_mes ||'
                       and cod_indic = 23';
     v_index := '(fil_cod)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;


     -----------------------------------------------------------------------------------------------
     -- TLMKT CARTAO ITAU APROVADO
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFI_TLMKT_APROV
     v_nm_tabela := 'SRV_REALZFI_TLMKT_APROV_' || v_ano || v_mes;
     v_comando := 'select a.cod_fil
                          ,a.cod_indic
                          ,b.descr_indic
                          ,a.num_ano
                          ,a.num_mes
                          ,a.num_realz qtde
                     from srv_realizado_filial_indic'||v_DbLink||' a
                     inner join srv_indicador       b on b.cod_indic = a.cod_indic
                                                      and b.descr_indic = ''TLMKT CARTAO ITAU APROVADO''
                      where a.num_ano = '|| p_num_ano ||'
                        and a.num_mes = '|| p_num_mes ||'
                        and a.cod_fil = 900';
     v_index := '(cod_fil)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;


     -----------------------------------------------------------------------------------------------
     -- TLMKT CARTAO ITAU ATIVADO
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFI_TLMKT_ATIV
     v_nm_tabela := 'SRV_REALZFI_TLMKT_ATIV_' || v_ano || v_mes;
     v_comando := 'select a.cod_fil
                          ,a.cod_indic
                          ,b.descr_indic
                          ,a.num_ano
                          ,a.num_mes
                          ,a.num_realz qtde
                     from srv_realizado_filial_indic'||v_DbLink||' a
                      inner join srv_indicador       b on b.cod_indic = a.cod_indic
                                                      and b.descr_indic = ''TLMKT CARTAO ITAU ATIVADO''
                      where a.num_ano = '|| p_num_ano ||'
                        and a.num_mes = '|| p_num_mes ||'
                        and a.cod_fil = 900';
     v_index := '(cod_fil)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;


     -----------------------------------------------------------------------------------------------
     -- TLMKT CARTAO ITAU ADICIONAL
     -----------------------------------------------------------------------------------------------
     -- SRV_REALZFI_TLMKT_ADIC
     v_nm_tabela := 'SRV_REALZFI_TLMKT_ADIC_' || v_ano || v_mes;
     v_comando := 'select a.cod_fil
                          ,a.cod_indic
                          ,b.descr_indic
                          ,a.num_ano
                          ,a.num_mes
                          ,a.num_realz qtde
                     from srv_realizado_filial_indic'||v_DbLink||' a
                      inner join srv_indicador       b on b.cod_indic = a.cod_indic
                                                      and b.descr_indic = ''TLMKT CARTAO ITAU ADICIONAL''
                      where a.num_ano = '|| p_num_ano ||'
                        and a.num_mes = '|| p_num_mes ||'
                        and a.cod_fil = 900';
     v_index := '(cod_fil)';

     Prc_Cria_Tabela(p_tabela => v_nm_tabela,
                     p_comando => v_comando,
                     p_index => v_index,
                     p_cod_erro => p_cod_erro,
                     p_descr_erro => p_descr_erro);

     if p_cod_erro is not null then
       return;
     end if;


     -- FIM EXEC --

  end Prc_Apura_RealizadoImportacao;

end pkg_srv_realizado;
/
