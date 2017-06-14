create or replace package srv.pkg_srv_carga_base is

  procedure prc_executa_job;

  procedure prc_carga_erro(p_processo in varchar2,
                           p_msg_erro in varchar2,
                           p_retorno  out number);

  procedure prc_consulta_agendamento(p_cod_carga   in srv_agendamento.cod_carga%type,
                                     p_num_ano     out srv_agendamento.num_ano%type,
                                     p_num_mes     out srv_agendamento.num_mes%type,
                                     p_num_arq     out srv_agendamento.num_arq%type,
                                     p_nome_arq    out srv_agendamento.nome_arq%type,
                                     p_dir_origem  out srv_agendamento.dir_origem%type,
                                     p_dir_destino out srv_agendamento.dir_destino%type,
                                     p_retorno     out number);

  procedure prc_carrega_arquivos(p_cod_carga in srv_agendamento.cod_carga%type,
                                 p_retorno   out number);

  procedure prc_carrega_digitalizado_pl;

  procedure prc_carrega_digitalizado_sax;

  procedure prc_carrega_funcionario;

  procedure prc_carrega_meta;

  procedure prc_agrupa_filial_vm;

  procedure prc_lojas_lider;

  procedure prc_metas_psf;

  procedure prc_ox_ativacao;

end pkg_srv_carga_base;
/
