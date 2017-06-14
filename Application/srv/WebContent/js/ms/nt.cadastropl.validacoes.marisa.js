
// variaveis da tela de matriz checagem ==========
var checaIdent = 0;
var checaEnd = 0;
var checaRenda = 0;
var checaEmancipado = 0;
var existenIdent = null; 
var existenEnd = null;
var existenRenda = null; 
var existenEmancipado = null;
//=================================================


listAllError = [];
listaErroValidacoes = [];
$(document).ready(function() {
	// VALIDAÇÕES E CONDIÇÕES - POR ABA
	$("#form").validate({
	   	rules: {
	   		// CAMPOS DE TEXTO
			cliNome: {validaNome:true,required:true, naoConterCaracEspecial:true},
			cliApelido: {naoConterCaracEspecial:true},
			cliDtNascto: {dateBR: true,dataMaior:true,validaIdadeMarisa: true,required: true,validarEmancipado:true},
			cliResFone: {validaDDDMarisa: true, validaTelefoneMarisa: true, required: true},
	   		cliPai:{required: true,validaNomeZeroFill:true,naoConterCaracEspecial:true},
	   		cliCampData:{dateBR:true,dataMaiorQueAtual:true,required:true},
	   		cliNumCelular:{validaDDDMarisa: true,validaCelularMarisa:true, validarCelEmailAbaPessoalProfissional: true},
	   		cliCampCPF:{cpf:true,cpfContraint:true,required:true},
	   		cliDocIdentificacao:{required:true},
	   		codComunicacao:{required: true},
	   		cliCelularSMS:{validaCelularMarisa:true,required: true},
	   		cliMailFatura:{email:true,required:true,caracEspecial:true},
	   		cliEmail:{email:true, validarCelEmailAbaPessoalProfissional: true, caracEspecial: true},
			cliSexoDomi:{required: true},
			cliIdentidade: {required: true, naoConterCaracEspecial:true},
			cliIdeOrgaoEmi:{required: true, naoConterCaracEspecial:true},
			cliDependentes:{required: true, number:true},
			cliMae: {validaNome:true,required: true, naoConterCaracEspecial:true},
			cliResCep: {cepRequerido:true},
			cliResUf:{required: true},
			cliResCid:{required: true},
			cliResBai:{required: true},
			cliResRua:{required: true},
			cliResNum:{required:true,naoConterCaracEspecialNumero:true,validarEnderecoAssalariado: true},
			cliResDtDesdeMes: {campoRequerido:true, validarDataAnoMes:true},
			cliEmpEmpresa: {required:true, naoConterCaracEspecial:true},
			cliEmpComp: {naoConterCaracEspecial:true},
			cliEmpDtAdmMes: {campoRequerido:true, validarDataAnoMes:true},
			cliEmpSalario:{required:true, validaNumRenda: true, validarValorMinimoRenda: true},
			cliOutrasRendas:{validaNumRenda: true, validarOutrasRendas:true},
			cliEmpCep:{cepRequerido: true},
			cliEmpUf:{required: true},
			cliEmpCid:{required: true},
			cliEmpBai:{required: true},
			cliEmpRua:{required: true},
			cliResComp:{ naoConterCaracEspecial:true},
			cliCgcCnpj:{cnpj:true,required:true},
			cliCorrespondencia:{required:true},
			cliVencimento:{required:true},
			cliViaDocumento:{number:true},
			cliEmpRamal:{number:true},
			cliResRamal:{number:true},
			cliTempoContaDtDesdeMes:{campoRequerido:true, validarDataAnoMes:true},
			cliTipoContaDomi:{tipoContaRequerido:true},
			// COMBOS
			cliIdeUfEmi:{required:true},
			cliEcDomi:{required:true},
			cliResCasaDomi:{required:true},
			cliResFoneDomi:{required:true},
			cargoNome:{required:true},
			cliEmpOcupacao:{required:true},
			cliOutrosCarDomi:{required:true},
			cliCrediarioDomi:{required:true},
			cliCPFConj:{cpf:true,cpfContraint:true,required:true},
			cliIdentidadeConj:{required:true, naoConterCaracEspecial:true},
			cliNomeConj:{validaNome:true,required:true,naoConterCaracEspecial:true},
			cliDtNascConj:{dateBR:true,dataMaior:true,required:true},
			cliRamalConj:{number:true},
			cliCampCaptacao:{required:true},
			//cliFilialRetira:{required:true},
			cliEmpNum:{required:true,naoConterCaracEspecialNumero:true,validarEnderecoAssalariado:true},
			cliEmpFone:{validaDDDMarisa: true, required:true, validaTelefoneIgualMarisa:true},
			segCpfVendedor:{cpf:true,cpfContraint:true,required:true},
			// aba adicional
			adiCliCpf1:{cpf:true,cpfContraint:true,validarAdicionalExistente:true},
			adiCliCpf2:{cpf:true,cpfContraint:true,validarAdicionalExistente:true},
			adiCliCpf3:{cpf:true,cpfContraint:true,validarAdicionalExistente:true},
			adiCliCpf4:{cpf:true,cpfContraint:true,validarAdicionalExistente:true},
			adiNome1:{validaNome:true,required:true, naoConterCaracEspecial:true},
			adiNome2:{validaNome:true,required:true, naoConterCaracEspecial:true},
			adiNome3:{validaNome:true,required:true, naoConterCaracEspecial:true},
			adiNome4:{validaNome:true,required:true, naoConterCaracEspecial:true},
			adiSexo1:{required:true},
			adiSexo2:{required:true},
			adiSexo3:{required:true},
			adiSexo4:{required:true},
			adiParentesco1:{required:true},
			adiParentesco2:{required:true},
			adiParentesco3:{required:true},
			adiParentesco4:{required:true},
			adiDtNascimento1:{dateBR:true,dataMaior:true,required:true,validarEmancipadoAdicional:true},
			adiDtNascimento2:{dateBR:true,dataMaior:true,required:true,validarEmancipadoAdicional:true},
			adiDtNascimento3:{dateBR:true,dataMaior:true,required:true,validarEmancipadoAdicional:true},
			adiDtNascimento4:{dateBR:true,dataMaior:true,required:true,validarEmancipadoAdicional:true},
			adiTelefone1: {validaDDDMarisa: true, required: true, validaTelefoneIgualMarisa:true},
			adiTelefone2: {validaDDDMarisa: true, required: true, validaTelefoneIgualMarisa:true},
			adiTelefone3: {validaDDDMarisa: true, required: true, validaTelefoneIgualMarisa:true},
			adiTelefone4: {validaDDDMarisa: true, required: true, validaTelefoneIgualMarisa:true},
			adiRamal1:{number:true},
			adiRamal2:{number:true},
			adiRamal3:{number:true},
			adiRamal4:{number:true},
			cpfVendedor:{cpf:true,cpfContraint:true}, 
			cliDtValidadeDocumento:{dateBR:true,dataMenor:true,validadeDocumentoHabilitacao:true},
			cliDtExpedicaoDocumento:{dateBR:true,dataMaior:true,validaDataMaiorNasc:true,validarExpedicaoDocumentoRequerido:true},
			cliDtEmissaoCpf:{dateBR:true,dataMaior:true,validaDataMaiorNasc:true},
			cliTelefoneConj: {validaDDDMarisa: true, required: true, validaTelefoneIgualMarisa:true},
			cliCodBeneficio:{required: true, validaCodigoBeneficio: true},
			// CAMPOS MATRIZ-CHECAGEM
			cliMatrizPessoaTelefoneIdent:{validaDDDMarisa: true, validaTelefoneMarisa: true},
			cliMatrizPessoaTelefoneEnd:{validaDDDMarisa: true, validaTelefoneMarisa: true,required: true},
			cliMatrizPessoaTelefoneRenda:{validaDDDMarisa: true, validaTelefoneMarisa: true,required: true},
			cliMatrizNomePessoaAtendeuRenda:{required: true},
			cliMatrizPessoaObservacaoRenda:{required: true},
			cliMatrizNomePessoaAtendeuEnd:{required: true},
			cliMatrizPessoaObservacaoEnd:{required: true},
			cliMatrizNomePessoaAtendeuEnd:{required: true},
			cliObservacaoGeral:{required: true},
			cliFalarCom:{required: true,naoConterCaracEspecial:true},
			cliCrediarioLoja:{validaCreditoLoja:true}
			
	   	},
	   	messages: {
	   		cliNome: {validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> O campo Nome é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliApelido: {naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliDtNascto: {dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data inválida.</span>',dataMaior: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>', validaIdadeMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Idade do cliente menor que a idade mínima para Titular.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem"/>O campo Data de Nascimento é obrigatório.</span>',validarEmancipado:'  <span class="erro"><img src="images/ico_error.gif" class="imagem"/>Idade válida apenas para emancipados.</span>'},
       		cliResFone: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', validaTelefoneMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone Residencial válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone Residencial é obrigatório.</span>'},
	   		cliPai:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> O Campo nome do Pai é obrigatório.</span>', validaNomeZeroFill:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome do Pai precisa ter pelo menos duas palavras.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliCampData:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaiorQueAtual:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior que a data atual.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Data é obrigatório.</span>'},
	   		cliNumCelular:{validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', validaCelularMarisa:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Número de Celular Inválido.</span>',validarCelEmailAbaPessoalProfissional: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Celular é obrigatório.</span>'},
	   		cliCampCPF:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo CPF é obrigatório.</span>'},
	   		cliDocIdentificacao:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe o Tipo do documento de identificação.</span>'},
	   		codComunicacao:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Formas de Comunicação deve ser informado.</span>'},
	   		cliCelularSMS:{validaCelularMarisa:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Celular Inválido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Celular é obrigatório.</span>'},
	   		cliMailFatura:{email:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Email Inválido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Email é obrigatório.</span>', caracEspecial:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Email não pode conter caracter especial.</span>'},
	   		cliEmail:{email:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Email Inválido.</span>',validarCelEmailAbaPessoalProfissional: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Email é obrigatório.</span>', caracEspecial:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Email não pode conter caracter especial.</span>'},
	   		cliSexoDomi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione o Sexo.</span>'},
	   		cliViaDocumento:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			cliEmpRamal:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			cliResRamal:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			cliTempoContaDtDesdeMes:{campoRequerido: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Tempo de Conta é obrigatório.</span>', validarDataAnoMes:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Valores aceitos para ano (0 a 99) e para mês (0 a 11).</span>'},
			cliTipoContaDomi:{tipoContaRequerido:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Tipo da Conta é obrigatório.</span>'},
			cliIdentidade: {required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo RG é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliIdeOrgaoEmi: {required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Órgão Emissor é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliDependentes: {required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Dependentes é obrigatório.</span>', number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
	   		cliMae:{validaNome:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome da Mãe precisa ter pelo menos duas palavras.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Nome da Mãe é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
		   	cliResCep: {cepRequerido: ''},
		   	cliResUf:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo UF é obrigatório.</span>'},
			cliResCid:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Cidade é obrigatório.</span>'},
			cliResBai:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Bairro é obrigatório.</span>'},
			cliResRua:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Rua é obrigatório.</span>'},
			cliResNum: {required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Numero é obrigatório.</span>',naoConterCaracEspecialNumero: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>',validarEnderecoAssalariado: "<script>modal(500,110,'paginas/modal/endereco-assalariado-invalido.jsp','Endereço Inválido',true,null,true);</script>"},
	   		cliResDtDesdeMes: {campoRequerido: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Tempo Residência é obrigatório.</span>',validarDataAnoMes:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Valores aceitos para ano (0 a 99) e para mês (0 a 11).</span>'},
	   		cliEmpEmpresa: {required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Empresa é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliEmpComp: {naoConterCaracEspecial:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliEmpDtAdmMes: {campoRequerido: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Tempo de Empresa é obrigatório.</span>', validarDataAnoMes:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Valores aceitos para ano (0 a 99) e para mês (0 a 11).</span>'},
	   		cliEmpSalario:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Renda Mensal é obrigatório.</span>', validaNumRenda: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>', validarValorMinimoRenda: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O valor da Renda Mensal tem que ser maior que zero.</span>'},
			cliOutrasRendas:{validaNumRenda: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>',validarOutrasRendas:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O valor de outras rendas não pode ultrapassar o valor de renda.</span>'},
	   		cliEmpCep:{cepRequerido: ''},
	   		cliEmpUf:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo UF é obrigatório.</span>'},
			cliEmpCid:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Cidade é obrigatório.</span>'},
			cliEmpBai:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Bairro é obrigatório.</span>'},
			cliEmpRua:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Rua é obrigatório.</span>'},
			cliResComp:{ naoConterCaracEspecial:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
	   		cliCorrespondencia:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Endereço para correspondência é obrigatório.</span>'},
	   		cliVencimento:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione o dia que deseja para o vencimento do cartão.</span>'},
	   		cliIdeUfEmi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um Estado.</span>'},
	   		cliEcDomi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um Estado Civil.</span>'},
	   		cliResCasaDomi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um Tipo de Residência.</span>'},
	   		cliResFoneDomi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um Tipo de Telefone.</span>'},
	   		cargoNome:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma Classe Profissional.</span>'},
	   		cliEmpOcupacao:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma das Atividades.</span>'},
	   		cliOutrosCarDomi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma das opções.</span>'},
	   		cliCrediarioDomi:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma das opções.</span>'},
			cliCPFConj:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo CPF do Cônjuge é obrigatório.</span>'},
			cliIdentidadeConj:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo RG do Cônjuge é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliNomeConj:{validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome do Cônjuge precisa ter pelo menos duas palavras.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome do Cônjuge é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliDtNascConj:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>', dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data de Nascimento do cônjuge deve ser preenchida.</span>'},
			cliRamalConj:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			cliCampCaptacao:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma campanha.</span>'},
			//cliFilialRetira:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma filial para a retirada.</span>'},
			cliEmpNum:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Número é obrigatório.</span>',naoConterCaracEspecialNumero: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>',validarEnderecoAssalariado:"<script>modal(500,110,'paginas/modal/endereco-assalariado-invalido.jsp','Endereço Inválido',true,null,true);</script>"},
			cliEmpFone:{validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone é obrigatório.</span>', validaTelefoneIgualMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>'},
			cliCgcCnpj:{cnpj:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CGC/CNPJ Inválido.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo CGC/CNPJ é obrigatório.</span>'},
			segCpfVendedor:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo CPF do vendedor é obrigatório.</span>'},
			cpfVendedor: {cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>'},
			// validações aba Adicional
			adiCliCpf1:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',validarAdicionalExistente:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está cadastrado como Adicional.</span>'},
			adiCliCpf2:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',validarAdicionalExistente:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está cadastrado como Adicional.</span>'},
			adiCliCpf3:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',validarAdicionalExistente:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está cadastrado como Adicional.</span>'},
			adiCliCpf4:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>',cpfContraint:' <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>',validarAdicionalExistente:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está cadastrado como Adicional.</span>'},
			adiNome1:{validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			adiNome2:{validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			adiNome3:{validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			adiNome4:{validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			adiSexo1:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo sexo é obrigatório.</span>'},
			adiSexo2:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo sexo é obrigatório.</span>'},
			adiSexo3:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo sexo é obrigatório.</span>'},
			adiSexo4:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo sexo é obrigatório.</span>'},
			adiParentesco1:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione o grau de parentesco.</span>'},
			adiParentesco2:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione o grau de parentesco.</span>'},
			adiParentesco3:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione o grau de parentesco.</span>'},
			adiParentesco4:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione o grau de parentesco.</span>'},
			adiDtNascimento1:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Data de nascimento é obrigatório.</span>', validarEmancipadoAdicional:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Adicional não pode ter idade menor que 16 anos.</span>'},
			adiDtNascimento2:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Data de nascimento é obrigatório.</span>', validarEmancipadoAdicional:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Adicional não pode ter idade menor que 16 anos.</span>'},
			adiDtNascimento3:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Data de nascimento é obrigatório.</span>', validarEmancipadoAdicional:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Adicional não pode ter idade menor que 16 anos.</span>'},
			adiDtNascimento4:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Data de nascimento é obrigatório.</span>', validarEmancipadoAdicional:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Adicional não pode ter idade menor que 16 anos.</span>'},
			adiTelefone1: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone é obrigatório.</span>', validaTelefoneIgualMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>'},
			adiTelefone2: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone é obrigatório.</span>', validaTelefoneIgualMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>'},
			adiTelefone3: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone é obrigatório.</span>', validaTelefoneIgualMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>'},
			adiTelefone4: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone é obrigatório.</span>', validaTelefoneIgualMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>'},
			adiRamal1:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			adiRamal2:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			adiRamal3:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			adiRamal4:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			cliDtValidadeDocumento:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMenor:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser menor que data atual.</span>',validadeDocumentoHabilitacao:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Validade do documento é obrigatório.</span>'},
			cliDtExpedicaoDocumento:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>', validaDataMaiorNasc: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser menor que a Data de Nascimento.</span>',validarExpedicaoDocumentoRequerido:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo Expedição do RG é obrigatório.</span>'},
			cliDtEmissaoCpf:{dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data Inválida.</span>',dataMaior:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior ou igual que a data atual.</span>', validaDataMaiorNasc: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser menor que a Data de Nascimento.</span>'},
			cliTelefoneConj: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone é obrigatório.</span>', validaTelefoneIgualMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>'},
			cliCodBeneficio:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Número do Benefício é obrigatório.</span>', validaCodigoBeneficio: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um Número do Benefício válido.</span>'},
			// CAMPOS MATRIZ-CHCAGEM
			cliMatrizPessoaTelefoneIdent:{validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', validaTelefoneMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de telefone.</span>'},
			cliMatrizPessoaTelefoneEnd:{validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', validaTelefoneMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de telefone.</span>'},
			cliMatrizPessoaTelefoneRenda:{validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um número de DDD válido.</span>', validaTelefoneMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de telefone.</span>'},
			cliMatrizNomePessoaAtendeuRenda:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo Nome da Pessoa é obrigatório.</span>'},
			cliMatrizPessoaObservacaoRenda:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo observação é obrigatório.</span>'},
			cliMatrizNomePessoaAtendeuEnd:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo Nome da Pessoa é obrigatório.</span>'},
			cliMatrizPessoaObservacaoEnd:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo observação é obrigatório.</span>'},
			cliObservacaoGeral:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo observação geral é obrigatório.</span>'},
			cliFalarCom:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo Falar Com é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliCrediarioLoja:{validaCreditoLoja: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Selecione uma Loja.</span>'}
	},
	invalidHandler: function(form, validator) {
		SubmissaoForm = true;
		$('#barBottomCheck input').show();
		$('#barBottomCheck #load').hide();
		listaErroValidacoes = [];
		if($('#erroCliResCepDDD').html() != '')
			listaErroValidacoes.push($('#cliResCep'));
		validarForm();
		validarAba();
		validarAbasComErro();
	},
	submitHandler : function(form){
		listaErroValidacoes = [];
			if($('#erroCliResCepDDD').html() != '')
				listaErroValidacoes.push($('#cliResCep'));		
			docsSelected();
			var residenciaCep = $.trim($("#cliResCep").val());
			var empresaCep = $.trim($("#cliEmpCep").val());
			if (!eval('validarCepEstatico("'+residenciaCep+'")')){

				$("#erroCliResCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
				$("#erroCliResCep").show();
				opentab(2);
			}else if (!eval('validarCepEstatico("'+empresaCep+'")')){
				$("#erroCliEmpCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
				$("#erroCliEmpCep").show();
				opentab(3);
			}else{
				
				$("#erroCliResCep").html('');
				$("#erroCliResCep").hide();
				$("#erroCliEmpCep").html('');
				$("#erroCliEmpCep").hide();
				if (tipoFormulario == 'ANALISE') {
			    		
					habilitarSelectsSubmissao();
					
			    	modalProcessoRequisicao();
			    	limparTimeout();
			    	
					document.form.action = 'cadastroPL.do?operacao=executarFluxoP2';
					document.form.submit();
				} else if (tipoFormulario == 'MATRIZ') {
					
					SubmissaoForm = true;
					
					$("#barBottomCheck input").show();
					$("#barBottomCheck #load").hide();
					
					var retorno = validarForm();
					validarAba();
					validarAbasComErro();
					if(retorno) {
						modal(400,120,'paginas/modal/concluir-matriz.jsp','Marisa - Matriz Checagem',true,'',false);
					}
					else {
						return retorno;
					}
				} else if (tipoFormulario == 'DERIVAR') {	
					var retorno = validarForm();
					validarAba();
					validarAbasComErro();
					if(retorno){
						
						if($("#derivacaoAutomatica").val() == 'E') {
							habilitarSelectsSubmissao();
							habilitarCamposMatrizChecagem(true);
							
							modalProcessoRequisicao();
							limparTimeout();
							
						 	// motivo da derivação quando vem de uma falha do fluxo
							document.form.action = "cadastroPL.do?operacao=aprovarDerivarProposta&motivoDerivacao=" + $("#codigoDerivacaoRepescagem").val();
							document.form.submit();
						} else if($("#derivacaoAutomatica").val() == 'S') {
							habilitarSelectsSubmissao();
							habilitarCamposMatrizChecagem(true);
							
							modalProcessoRequisicao();
							limparTimeout();
						 	// motivo da derivação ser automático
							document.form.action = "cadastroPL.do?operacao=aprovarDerivarProposta&motivoDerivacao=" + $("#codigoDerivacaoRepescagem").val();
							document.form.submit();
						} else if($("#derivacaoRepescagem").val() == 'S') {
							modal(360,275, 'paginas/modal/motivo-derivacao-repescagem.jsp', 'Marisa - Motivo de Derivação', true, '',false);
						} else {
							modal(360,275, 'paginas/modal/motivo-derivacao.jsp', 'Marisa - Motivo de Derivação', true, '',false);
						}
					}else
						return retorno;
				} else if (tipoFormulario == 'APROVAR_CENTRAL') {		
					var retorno = validarForm();
					validarAba();
					validarAbasComErro();
					
					if(retorno){
			    		habilitarSelectsSubmissao();
			    		habilitarCamposMatrizChecagem(true);
			    		
			    		modalProcessoRequisicao();
			    		limparTimeout();
			    		
				        document.form.action = 'cadastroPL.do?operacao=aprovarPropostaCentral';
				        document.form.submit();
					}else {
						return retorno;
					}
				} else if (tipoFormulario == 'REPROVAR_CENTRAL') {
					
					var retorno = validarForm();
					validarAba();
					validarAbasComErro();
					if(retorno){
						modal(360,275,'paginas/modal/motivo-rejeicao-central.jsp','Marisa - Motivo de Rejeição',true,'',false);
					} else {
						return retorno;
					}
				}  else if(tipoFormulario == 'REJEITAR_SALA') {
					modal(360,275,'paginas/modal/motivo-rejeicao.jsp','Marisa - Motivo de Rejeição',true,'',false);
				}  else if(tipoFormulario == 'REJEITAR_MATRIZ') {
					var retorno = validarForm();
					validarAba();
					validarAbasComErro();
					if(retorno){
						
						if($("#derivacaoRepescagem").val() == 'S') {
							modal(360,275,'paginas/modal/motivo-rejeicao-matriz-repescagem.jsp','Marisa - Motivo de Rejeição',true,'',false);
						} else {
							modal(360,275,'paginas/modal/motivo-rejeicao-matriz.jsp','Marisa - Motivo de Rejeição',true,'',false);
						}
					} else {
						return retorno;
					}
				} else if (tipoFormulario == null) {
					$("#barBottom input").show();
					$("#barBottom #load").hide();
					if ($("#isPL").val() == 'S') {
						modal(350, 120, 'paginas/modal/concluir-cad-pl.jsp', 'Marisa - Cadastro PL', true, '', false);
					}else {
						modal(350, 120, 'paginas/modal/concluir-cad-itau.jsp', 'Marisa - Cadastro Itaú', true, '', false);
					}
				}else{
					return false;
				}
			}					
		}
	});

	// BLOQUEIO DE CARACTERES
	$('#cliNome, #cliFalarCom, #cliPai, #cliApelido, #cliMae, #cliNomeConj, #adiNome, #adiNome1, #adiNome2, #adiNome3, #adiNome4').alphanumeric({ichars :",;:!¨&?-@[]{}()<>\\/#|*+=§%$1234567890"});
	
	$('#cliEmpRamal, #cliResRamal, #cliCpf, #cliDtNascto, #cliDependentes, #cliNumCelular, #cliViaDocumento, #cliRamalConj, #adiRamal1, #adiRamal2, #adiRamal3, #adiRamal4,#cliResDtDesdeAno, #cliResDtDesdeMes, #cliTempoContaDtDesdeAno, #cliTempoContaDtDesdeMes, #cliEmpDtAdmAno, #cliEmpDtAdmMes').numeric();
	
	$('#cliIdentidade, #cliIdeOrgaoEmi, #cliIdentidadeConj').alpha({allow:"QWERTYUIOPLKJHGFDSAZXCVBNM 0123456789/.-"});
	
	$('#cliIdeOrgaoEmi').alpha({allow:"QWERTYUIOPLKJHGFDSAZXCVBNM /.-"});
	
	$('#cliResNum, #cliResComp, #cliEmpNum ,#cliEmpComp').alpha({allow:"QWERTYUIOPLKJHGFDSAZXCVBNMÇ 0123456789/"});
	
	// CAMPOS OCLUSOS NO CARREGAMENTO DO CADASTRO PL [ABA CONJUGE]
    $('#cliCPFConj').addClass('readonly');
	$('#cliCPFConj').attr("readonly", "readonly");
	$('#cliNomeConj').addClass('readonly');
	$('#cliNomeConj').attr("readonly", "readonly");
	$('#cliIdentidadeConj').addClass('readonly');
	$('#cliIdentidadeConj').attr("readonly", "readonly");
	$('#cliDtNascConj').addClass('readonly');
	$('#cliDtNascConj').attr("readonly", "readonly");
	
	iniCamposDataReadOnly();
	habilitarDataDocumentoChecado();
});

function docsSelected() {
	
	if ($('#cliMatrizDocIdent').val()!='N'){
		$('#cliMatrizDocIdent').val('#');
		$('#cliMatrizDocIdentDataEmissao').val('#');
		
		$('input[name=checkDocIdent]').each(function(){
			if($(this).is(':checked')){
				$('#cliMatrizDocIdent').val($('#cliMatrizDocIdent').val() + $(this).val() + '#');
				$('#cliMatrizDocIdentDataEmissao').val($('#cliMatrizDocIdentDataEmissao').val() + $(this).parent().parent().children().eq(1).children('input').val() + '#');
			}
		});
	}
	
	if ($('#cliMatrizDocEnd').val()!='N'){
		$('#cliMatrizDocEnd').val('#');
		$('#cliMatrizDocEndDataEmissao').val('#');
		
		$('input[name=checkDocEnd]').each(function(){
			if($(this).is(':checked')){
				$('#cliMatrizDocEnd').val($('#cliMatrizDocEnd').val() + $(this).val() + '#');
				$('#cliMatrizDocEndDataEmissao').val($('#cliMatrizDocEndDataEmissao').val() + $(this).parent().parent().children().eq(1).children('input').val() + '#');
			}
		});
	}
	
	if ($('#cliMatrizDocRenda').val()!='N'){
		$('#cliMatrizDocRenda').val('#');
		$('#cliMatrizDocRendaDataEmissao').val('#');
		
		$('input[name=checkDocRenda]').each(function(){
			if($(this).is(':checked')){
				$('#cliMatrizDocRenda').val($('#cliMatrizDocRenda').val() + $(this).val() + '#');
				$('#cliMatrizDocRendaDataEmissao').val($('#cliMatrizDocRendaDataEmissao').val() + $(this).parent().parent().children().eq(1).children('input').val() + '#');
			}
		});
	}
	
	if ($('#cliMatrizDocEmancipado').val()!='N'){
		$('#cliMatrizDocEmancipado').val('#');
		$('#cliMatrizDocEmancipadoDataEmissao').val('#');
		
		$('input[name=checkDocEmancipado]').each(function(){
			if($(this).is(':checked')){
				$('#cliMatrizDocEmancipado').val($('#cliMatrizDocEmancipado').val() + $(this).val() + '#');
				$('#cliMatrizDocEmancipadoDataEmissao').val($('#cliMatrizDocEmancipadoDataEmissao').val() + $(this).parent().parent().children().eq(1).children('input').val() + '#');
			}
		});
	}
}

function iniCamposDataReadOnly(){
	$('input[id *= docIdentData],[id *= docEndData],[id *= docRendaData],[id *= docEmancipadoData]').each(function(){
		if($(this).val() == ''){
			$(this).attr('readonly', true);
			$(this).addClass('readonly');
		}else{
			$(this).unmask();
			$(this).mask("99/99/9999");
		}
	});	
}

function habilitarDataDocumentoChecado(){	
	$('input[id *= docIdentData],[id *= docEndData],[id *= docRendaData],[id *= docEmancipadoData]').parent().parent().children().children('input[type=checkbox]').click(function(){
		if($(this).is(':checked')){
			$(this).parent().parent().children().eq(1).children('input').removeAttr('readonly');
			$(this).parent().parent().children().eq(1).children('input').removeClass('readonly');
			$(this).parent().parent().children().eq(1).children('input').mask("99/99/9999");
			$(this).parent().parent().children().eq(1).children('input').focus();
		}else{
			$(this).parent().parent().children().eq(1).children('input').attr('readonly','readonly');
			$(this).parent().parent().children().eq(1).children('input').unmask();
			$(this).parent().parent().children().eq(1).children('input').addClass('readonly');
			$(this).parent().parent().children().children('.erro-ini').remove();
		}
		
	});	
}

var linkErro = '';
function validarForm(){
	linkErro = '';
	listaErro = [];
	var temErro = true;
	var retorno = false;
	var idCampoErro = '';
	existenIdent = $('#cliMatrizDocIdent').val();
	existenEnd = $('#cliMatrizDocEnd').val();
	existenRenda = $('#cliMatrizDocRenda').val();
	existenEmancipado = $('#cliMatrizDocEmancipado').val();
			
	if ((checaIdent>0 || existenIdent=='N' || $('#cliMatrizProcedenciaIdent').is(':checked'))
			&& (checaEnd>0 || existenEnd=='N' || $('#cliMatrizProcedenciaEnd').is(':checked'))
			&& (checaRenda>0 || existenRenda=='N' || $('#cliMatrizProcedenciaRenda').is(':checked'))
			&& (checaEmancipado>0 || existenEmancipado=='N')){
	 	$('#divErroIdent').hide();
	 	$('#divErroEnd').hide();
	 	$('#divErroRenda').hide();
	 	$('#divErroEmancipado').hide();
	 	temErro = false;
	} else {

		if (checaIdent<=0 && existenIdent!='N' && !$('#cliMatrizProcedenciaIdent').is(':checked')){
			idCampoErro = '#cliMatrizProcedenciaIdent';
			linkErro = '#Ident';
			$('#divErroIdent').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Você deve selecionar ao menos um dos itens.</span>');
			$('#divErroIdent').show();
			temErro = true;
		}
		
		if (checaEnd<=0 && existenEnd!='N' && !$('#cliMatrizProcedenciaEnd').is(':checked')){
			if(linkErro == ''){
				idCampoErro = '#cliMatrizProcedenciaEnd';
				linkErro = '#End';
			}
			$('#divErroEnd').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Você deve selecionar ao menos um dos itens.</span>');
			$('#divErroEnd').show();
			temErro = true;
		}
		
		if (checaRenda<=0 && existenRenda!='N' && !$('#cliMatrizProcedenciaRenda').is(':checked')){
			if(linkErro == ''){
				idCampoErro = '#cliMatrizProcedenciaRenda';
				linkErro = '#Renda';
			}
			$('#divErroRenda').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Você deve selecionar ao menos um dos itens.</span>');
			$('#divErroRenda').show();
			temErro = true;
		}

		if (checaEmancipado<=0 && existenEmancipado != 'N'){
			if(linkErro == '')
				linkErro = '#Emancipado';
			$('#divErroEmancipado').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Você deve selecionar ao menos um dos itens.</span>');
			$('#divErroEmancipado').show();
			temErro = true;
		}
			
	}
		if(!validarReferencia()){
			temErro = true;
		}

		if(!validarDocumentoCampoData()){
			temErro = true;
		}
		
		if(!validarFilialCampoRequerido('cliFilialRetira','tdErroFilial')){
			listaErroValidacoes.push($('#cliFilialRetira'));	
			temErro = true;
		}
		
	if (tipoFormulario=="MATRIZ"){
		$("#cliSexoDomi").attr({"disabled":"disabled"});
		$("#cliEcDomi").attr({"disabled":"disabled"});
		$("#cliResCasaDomi").attr({"disabled":"disabled"});
		$("#cliCrediarioDomi").attr({"disabled":"disabled"});
		$("#cliOutrosCarDomi").attr({"disabled":"disabled"});
		$("#cliFilialRetira").attr({"disabled":"disabled"});
		$("#cliCrediarioLoja").attr({"disabled":"disabled"});
	}

	if(temErro)
		listaErroValidacoes.push($('#cliMatrizClasseProfissional'));	
	
	if(temErro){
		if(linkErro != ''){
			window.location = linkErro;
		}
		return false;
	}else{
		return true;
	}
}

function validarDocumentoCampoData(){
	var retorno = true;
	var NaoAchouCampoInvalido = true;

	$('input[id *= docIdentData],[id *= docEndData],[id *= docRendaData],[id *= docEmancipadoData]').each(function(){
		if($(this).parent().parent().children().children('input').is(':checked')){
			if(!DataRequerida($(this).val())){
				$(this).parent().children('.erro-ini').remove();
				$(this).parent().append('<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" />Data obrigatória.</span>');
				retorno = false;
			}else if(!validarData($(this).val())){
				$(this).parent().children('.erro-ini').remove();
				$(this).parent().append('<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" />Informe uma Data válida.</span>');
				retorno = false;
			}else if(!validarDataMenor($(this).val())){
				$(this).parent().children('.erro-ini').remove();
				$(this).parent().append('<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" />Informe uma Data menor ou igual a atual.</span>');
				retorno = false;
			}else if(validarDataMenorDataNascimento($(this).val(), $('#cliDtNascto').val())){
				$(this).parent().children('.erro-ini').remove();
				$(this).parent().append('<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" />Informe uma Data maior que a data de nascimento.</span>');
				retorno = false;
			}else
				$(this).parent().children('.erro-ini').remove();
		}else
			$(this).parent().children('.erro-ini').remove();
		
		if(NaoAchouCampoInvalido && !retorno){
			var idLink = '#' + $(this).attr('id').substring(3,8);
			if(idLink == '#EndDa')
				idLink = '#End';
			NaoAchouCampoInvalido = false;
			
			if(linkErro != ''){
				if(linkErro == '#End' && idLink != '#Renda')
					linkErro = idLink;
				else if(linkErro == '#Renda')
					linkErro = idLink;
			}else
				linkErro = idLink;
		}
			
	});
	return retorno;
}

function DataRequerida(value){
	dateValue = value;
	
	dateValue = retirarMascara(dateValue);
		
	if(dateValue.length == 0) {
		return false;
	}
	return true;
}

function validarReferencia(){
	var retorno = true;

	if( $.trim($('#cliNumFoneRef1').val()) != "" || $.trim($('#cliNomeRef1').val()) != "" || $.trim($('#cliObservacaoRef1').val()) != ""){
		if($.trim($('#cliNumFoneRef1').val()) == ""){
			$('#divErroCliNumFoneRef1').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone.</span>');
			$('#divErroCliNumFoneRef1').show();	
			retorno = false;
		}else{
			$('#divErroCliNumFoneRef1').hide();
			if(validarFone($.trim($('#cliNumFoneRef1').val()))){
				$('#divErroCliNumFoneRef1').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>');
				$('#divErroCliNumFoneRef1').show();
				retorno = false;
			}
		}
		
		if($.trim($('#cliNomeRef1').val()) == ""){
			$('#divErroCliNomeRef1').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um Nome de Referência.</span>');
			$('#divErroCliNomeRef1').show();	
			retorno = false;
		}else if(!validarCaracteresEspeciais($.trim($('#cliNomeRef1').val()))){
			$('#divErroCliNomeRef1').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>');
			$('#divErroCliNomeRef1').show();	
			retorno = false;
		}else{
			$('#divErroCliNomeRef1').hide();	
		}
		
		if($.trim($('#cliObservacaoRef1').val()) == ""){
			$('#divErroCliObservacaoRef1').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe uma Observação.</span>');
			$('#divErroCliObservacaoRef1').show();	
			retorno = false;
		}else{
			$('#divErroCliObservacaoRef1').hide();
		}	
	}else{
		$('#divErroCliNumFoneRef1').hide();
		$('#divErroCliNomeRef1').hide();	
		$('#divErroCliObservacaoRef1').hide();
	}
		
			
	
	if( $.trim($('#cliNumFoneRef2').val()) != "" || $.trim($('#cliNomeRef2').val()) != "" || $.trim($('#cliObservacaoRef2').val()) != ""){
		
		if($.trim($('#cliNumFoneRef2').val()) == ""){
			$('#divErroCliNumFoneRef2').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone.</span>');
			$('#divErroCliNumFoneRef2').show();		
			retorno = false;
		}else{
			$('#divErroCliNumFoneRef2').hide();
			if(validarFone($.trim($('#cliNumFoneRef2').val()))){
				$('#divErroCliNumFoneRef2').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um número de Telefone válido.</span>');
				$('#divErroCliNumFoneRef2').show();
				retorno = false;
			}
		}
			
		if($.trim($('#cliNomeRef2').val()) == ""){
			$('#divErroCliNomeRef2').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um Nome de Referência.</span>');
			$('#divErroCliNomeRef2').show();
			retorno = false;
		}else if(!validarCaracteresEspeciais($.trim($('#cliNomeRef2').val()))){
			$('#divErroCliNomeRef2').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>');
			$('#divErroCliNomeRef2').show();	
			retorno = false;
		}else{
			$('#divErroCliNomeRef2').hide();
		}

		if($.trim($('#cliObservacaoRef2').val()) == ""){
			$('#divErroCliObservacaoRef2').html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe uma Observação.</span>');
			$('#divErroCliObservacaoRef2').show();	
			retorno = false;
		}else{
			$('#divErroCliObservacaoRef2').hide();
		}			
	}else{
		$('#divErroCliNumFoneRef2').hide();
		$('#divErroCliNomeRef2').hide();	
		$('#divErroCliObservacaoRef2').hide();
	}

	return retorno;
}


function validarFone(value) {
	telefoneValue = value;

	telefoneValue = retirarMascara(telefoneValue);
	
	if(telefoneValue.length == 0) {
		return false;
	}
	
	numeroTelefone = value.substring(5,14);
	if ((numeroTelefone.indexOf("6") == 0) || (numeroTelefone.indexOf("7") == 0) 
			|| (numeroTelefone.indexOf("8") == 0) || (numeroTelefone.indexOf("9") == 0)) {
		return true;
	}

	numeroTelefone = retirarMascara(numeroTelefone);
		
	if(numeroTelefone.length != 8) {
		return true;
	}

	// todos os números iguais
	if (numeroTelefone != null && numeroTelefone.length > 0) {
		numero = numeroTelefone.substring(0,1);
		for (i = 1; i < numeroTelefone.length; i++) {
			if (numero != numeroTelefone.substring(i,(i+1))) {
				return false;
			}
		}
		
		return true;
	}
	return false;
	
}


function validarCepEstatico(value){
	numeroCEP = value;
	
	numeroCEP = retirarMascara(numeroCEP);
	
	if(numeroCEP.length == 0 || numeroCEP.length == 8) {
		return true;
	}
	return false;
}

function validarAbasComErro(){
	if(listAllError.length > 0){
		for ( var index = 0; index < listAllError.length; index++) {
			var any = listAllError[index];
			var idItem = $($(listAllError[index]).attr('element')).attr('id');
			var idTable = $("#"+idItem).parent().parent().parent().parent().attr('id');
			idTable = parseInt(idTable);
			var idCargo = $('#cliEmpOcupacao').val();
			$('#tabs > a').eq(idTable-1).css('background-color','#C0A0A0');
		}
	}
	
	if(listaErroValidacoes.length > 0){
		for ( var index = 0; index < listaErroValidacoes.length; index++) {
			var any = listaErroValidacoes[index];
			var idItem = $(listaErroValidacoes[index]).attr('id');
			var idTable = $("#"+idItem).parent().parent().parent().parent().attr('id');
			idTable = parseInt(idTable);
			var idCargo = $('#cliEmpOcupacao').val();
			$('#tabs > a').eq(idTable-1).css('background-color','#C0A0A0');
		}
	}
}


function validarAba(){
	if(listAllError.length>0){
		for ( i = 0; i < listAllError.length; i++) {
			var any = listAllError[i];
			var idItem = $($(listAllError[i]).attr('element')).attr('id');
			// ABAIXO, BUSCO ATRAVÉS DE PARENT A TABELA A QUAL O INPUT, SELECT
			// OU TEXTAREA
			// PERTENCE E RETORNO SEU ID QUE DEVERÁ SER UM INTEGER
			// (1,2,3,4,5,12,100...).
			var idTable = $("#"+idItem).parent().parent().parent().parent().attr('id');
			idTable = parseInt(idTable);
			
			var idCargo = $('#cliEmpOcupacao').val();
			
			if(idTable != 5 || ( idTable == 5 && idCargo == '6#3')){
				opentab(idTable);
			}else{
				opentab(idTable-1);
			}
			$('#'+idItem).focus();
			return false;				
		}
	}else{
		return true;
	}
}

function validarData(value) {
	
	dateValue = value;
	
	dateValue = retirarMascara(dateValue);
	
	if(dateValue.length == 0) {
		return true;
	}
	
	 //contando chars
	if(value.length!=10) return false;
	// verificando data
	var data 		= value;
	var dia 		= data.substr(0,2);
	var barra1		= data.substr(2,1);
	var mes 		= data.substr(3,2);
	var barra2		= data.substr(5,1);
	var ano 		= data.substr(6,4);
	if(data.length!=10||barra1!="/"||barra2!="/"||isNaN(dia)||isNaN(mes)||isNaN(ano)||dia>31||mes>12)return false;
	if((mes==4||mes==6||mes==9||mes==11) && dia==31)return false;
	if(mes==2  &&  (dia>29||(dia==29 && ano%4!=0)))return false;
	if(ano < 1900)return false;
	if(ano > 2100)return false;
	return true;
}

function validarDataMenor(value) {
	var vl = value;
	var aS = vl.split("/");
	var dataValue = value;
	
	dataValue = retirarMascara(dataValue);
	
	var atualDate = new Date();
	var newDate = new Date();
	
	if (dataValue == ""){
		return true;
	}else{
		if (aS.length == 3){
			newDate.setDate(aS[0]);
			newDate.setMonth(aS[1]-1);
			newDate.setFullYear(aS[2]);
		}else{
			return true;
		}
		if(atualDate >= newDate){
			return true;
		}
		return false;
	}		
}

function validarDataMenorDataNascimento(value, dataNasc) {
	var vl = value;
	var aS = vl.split("/");
	var dataValue = value;

	var vlNasc = dataNasc;
	var aSNasc = vlNasc.split("/");
	var dataValueNasc = dataNasc;

	dataValue = retirarMascara(dataValue);
	
	dataValueNasc = retirarMascara(dataValueNasc);
	
	var newDate = new Date();
	
	var DateNasc = new Date();
	
	if (dataValue == ""){
		return true;
	}else{
		if (aSNasc.length == 3){
			newDate.setDate(aS[0]);
			newDate.setMonth(aS[1]-1);
			newDate.setFullYear(aS[2]);
	
			DateNasc.setDate(aSNasc[0]);
			DateNasc.setMonth(aSNasc[1]-1);
			DateNasc.setFullYear(aSNasc[2]);
		}else{
			return true;
		}
		
		if(DateNasc > newDate){
			return true;
		}
		return false;
	}		
}

function validarEnderecoCEPUF(){
	var validacao = true;
	var numeroFone = $('#cliResFone').val();
	
	if(numeroFone.length < 10) {
		return;
	}

	if($('#isCEPResGenerico').val() == 'S'){
		$('#erroCliResCepDDD').hide();	
		return true;
	}
	
	var DDD = numeroFone.substring(1,3);
	var numeroCEP = $('#cliResCep').val();
	
	numeroCEP = retirarMascara(numeroCEP);

	if(numeroCEP.length < 8) {
		return;
	}
	
	$.ajax({
		type : 'POST',
		url : 'telefone.do?operacao=validarTelefoneEndereco',
		data : {cep:numeroCEP,ddd:DDD},
		cache: false, 
		async: false,
		error: function(){    
  			mensagem("Erro ao tentar consultar o endereço.", true);
  			$('#erroCliResCepDDD').hide();
  		}, 
		success : function(retorno){
			retorno = $.trim(retorno);
			if(retorno == 'E'){			
				listaErroValidacoes.push($('#cliResCep'));
				validacao = false;
				modal(400, 130,'paginas/modal/cep-invalido-ddd.jsp','Marisa - Cadastro PL',true,'', false);
			}
		}
	});	
	return validacao; 
}


function validarFilialCampoRequerido(id,idContent){
	if($('#' + id).val() == ""){
		$('#' + idContent).html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma Filial para a retirada.</span>');
		return false;
	}else{
		$('#' + idContent).html('');
	}
	return true;
}

