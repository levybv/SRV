
var tipoCadastro = 'M';
fields = $('input, select');
	fieldsall = fields ;
	desabilitarALT(fields);
	
var formCEP;
	$(document).ready(function () {

		$('#cliCampCPF').focusout(consultarNomeCPFVendedor);
		
	$("#cliNome").focus();
		
	//AUTO-TAB
	$('input').keyup(
		function(event){
			var size = $(this).attr('maxlength');
			autoTab(this,size,event);
		}
	);
		
jQuery(function(){
		$("#cliNumCelular").mask("(99) 9999-9999");
		$("#cliResFone").mask("(99) 9999-9999");
		$("#cliResCep").mask("99999-999");
		$("#cliCampCPF").mask("999.999.999-99");
		$("#cliDtNascto").mask("99/99/9999");
		$("#cliResDtDesde").mask("99/99");	
	});
	
	// FOCO DOS CAMPOS
	$('input:[type=text], select').focus(function () {
		$(this).removeClass("campo");
		$(this).addClass("focusGained");   
	});
	$('input:[type=text], select').blur(function () {
		$(this).removeClass("focusGained");
		$(this).addClass("campo");
	});
	
	$("#cliResCep").change(function () {
        $cep = $("#cliResCep").val();
		var residencialCep = $.trim($("#cliResCep").val());
		var resultante = eval('validarCepEstatico("' + residencialCep + '")');
				if (!resultante) {
					$("#erroCliResCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
					$("#erroCliResCep").show();
				}else {
					$("#erroCliResCep").html('');
					$("#erroCliResCep").hide();
					var checar = $cep;
					
					checar = retirarMascara(checar);
					if (checar.length < 8) {
						$("#cliResUf").val("");
						$("#cliResCid").val("");
						$("#cliResBai").val("");
						$("#cliResRua").val("");
						return false;
					}
					else {
						$.ajax({
							url: 'endereco.do?operacao=consultarCEP&cep=' + retirarCaracteresEspeciasELetras($cep),
							type: 'POST',
							cache: false,
							async: false,
							error: function(){    
							mensagem("Erro ao tentar consultar o CEP.", true);
							}, 
							success: function(resp){
								var valor = resp.split("#");
								
								if ($.trim(valor[0]) == "") {
									formCEP="RESIDE";
									fecharModal();
									modal(400, 130,'paginas/modal/cep-nao-encontrado.jsp','Marisa - Cadastro PL',true,'', false);
									$("#cliResUf").val("");
									$("#cliResCid").val("");
									$("#cliResBai").val("");
									$("#cliResRua").val("");
									$("#cliResCep").focus();
								}
								else {
									configCampoCEPGenerico('cliResUf',valor[1]);
							    	configCampoCEPGenerico('cliResCid',valor[2]);
							    	configCampoCEPGenerico('cliResBai',valor[3]);
							    	configCampoCEPGenerico('cliResRua',valor[4]);
									$('#isCEPResGenerico').val('N');
									
									if(valor[1] == ''){
							    		$('#cliResUf').focus();    		
							    	}else if(valor[2] == ''){
							    		$('#cliResCid').focus();
							    	}else if(valor[3] == ''){
							    		$('#cliResBai').focus();
							    	}else if(valor[4] == ''){
							    		$('#cliResRua').focus();
							    	}else{
							    		$("#cliResNum").focus();
							    	}
								}
							}
						});
					}
				}
    });
	// Valida caracteres
	$("#cliNome,#cliMae, #cliPai").alphanumeric({ichars :",;:!¨&?-@[]{}()<>\\/#|*+=§%$1234567890"});
	$("#cliDependentes, #cliResRamal").numeric(); 
	$("#cliIdentidade").alpha({allow:"QWERTYUIOPLKJHGFDSAZXCVBNM0123456789/.-"});
	$("#cliEmail").alpha({allow:"QWERTYUIOPLKJHGFDSAZXCVBNM0123456789_.-@"});
	$("#cliIdeOrgaoEmi").alpha({allow:"0123456789/.-"});	
	
	//VALIDAÇÕES DE TODOS OS CAMPOS DE TODAS AS ABAS DO CADASTRO MAILING
	listAllError = [];
	$("#formMailing").validate({
		rules: {
			cliVencimento:{required:true},
			cliResCep: {cepRequerido:true},
			cliCpf:{cpf:true,required:true},
			cliCampCPF:{cpf:true,cpfCampanha:true,required:true},
			cliNome: {validaNome:true,required:true, naoConterCaracEspecial:true},
			cliDtNascto: {dateBR: true,dataMaior:true,validaIdadeMarisa: true,required: true},
			cliNumCelular:{validaCelularMarisa:true},
			cliSexoDomi:{required:true},
			cliEmail:{email:true, caracEspecial:true},
			cliIdentidade:{required:true, naoConterCaracEspecial:true},
			cliIdeOrgaoEmi:{required:true, naoConterCaracEspecial:true},
			cliIdeUfEmi:{required:true},
			cliEcDomi:{required:true},
			cliDependentes:{required:true,number:true},
			cliMae: {validaNome:true,required: true,naoConterCaracEspecial:true},
			cliPai: {required:true, naoConterCaracEspecial:true, validaNomeZeroFill:true},
			cliResRamal:{number: true},
			cliEmpSalario:{required:true, validarValorMinimoRenda: true},
			cliEmpOcupacao:{required:true},
			cliResUf:{required:true},
			cliResNum:{required:true,naoConterCaracEspecialNumero: true},
			cliResComp:{naoConterCaracEspecial:true},
			cliResDtDesdeMes: {campoRequerido:true, validarDataAnoMes:true},
			cliCampLimite:{required:true},
			cliIndAdiProtege:{required:true}
		},
		messages: {
			cliVencimento:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo Dia para Vencimento obrigatório.</span>'},
			cliResCep: {cepRequerido: ''},
			cliCpf:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Cpf inválido.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo Cpf obrigatório.</span>'},
			cliCampCPF:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF Inválido.</span>', cpfCampanha:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF informado já está sendo utilizado.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo CPF é obrigatório.</span>'},
			cliNome: {validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> O campo Nome é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliDtNascto: {dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data inválida.</span>',dataMaior: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada não pode ser maior que data atual.</span>', validaIdadeMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Idade do cliente menor que a idade mínima para Titular.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem"/>O campo Data de Nascimento é obrigatório.</span>'},
			cliNumCelular:{validaCelularMarisa:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Número de Celular Inválido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Celular é obrigatório.</span>'},
			cliSexoDomi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um Sexo.</span>'},
			cliEmail:{email:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Email Inválido.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliIdentidade:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo identidade obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliIdeOrgaoEmi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo orgão obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliIdeUfEmi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo UF obrigatório.</span>'},
			cliEcDomi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um estado civil.</span>'},
			cliDependentes:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe a quantidade de dependentes.</span>', number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
			cliMae:{validaNome:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome da Mãe precisa ter pelo menos duas palavras.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Nome da Mãe é obrigatório.</span>',naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliPai:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> O Campo nome do Pai é obrigatório.</span>', naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>', validaNomeZeroFill:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome do Pai precisa ter pelo menos duas palavras.</span>'},
			cliResRamal:{number: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente números.</span>'},
	   		cliEmpSalario:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe o salário.</span>', validarValorMinimoRenda: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O valor da Renda Mensal tem que ser maior que zero.</span>'},
			cliEmpOcupacao:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma ocupação.</span>'},
			cliResUf:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />UF é obrigatório.</span>'},
			cliResNum:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe o número.</span>',naoConterCaracEspecialNumero: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliResComp:{naoConterCaracEspecial: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'},
			cliResDtDesdeMes: {campoRequerido: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Tempo Residência é obrigatório.</span>',validarDataAnoMes:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Valores aceitos para ano (0 a 99) e para mês (0 a 11).</span>'},
			cliCampLimite:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Limite pré-aprovado obrigatório.</span>'},
			cliIndAdiProtege:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe se o cliente deseja aderir ao seguro Marisa Protege.</span>'}
		},
		invalidHandler: function(form, validator) {
			validarAba();
			validarAbasComErro();
		},
		submitHandler : function(form){
			
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
					$("#erroCliEmpCep").html();
					$("#erroCliEmpCep").hide();
					fecharModal();
					modal(350, 120, 'paginas/modal/concluir-cad-pl-mailing.jsp', 'Marisa - Mailing', true, '', false);
				}	
			//OUTRAS VALIDAÇÕES
		
		}
	});
	$("#conteudo > div").hide();
    $("#conteudo > div:eq(0)").show();
    $(".mensagemError").hide();
    $("#tabs > a:eq(0)").css("background-color", "#E9EBE8");
    
    $(".linkDetalhe").click(function () {
        if ($(".panelDetalhe").is(":visible")) {
            $(".panelDetalhe").hide(100);
        } else {
            $(".panelDetalhe").show(100);
        }
    });
    $(".panelDetalhe").hide(100);

    $("#cliResFoneDomi").change(function () {
        $nomeCargo = $("#cliResFoneDomi").val();
        if ($nomeCargo == 3) {
            $("#L2_sub_1").show();
        } else {
            $("#L2_sub_1").hide();
        }
    });
    $("#L2_sub_1").hide();

    $("#botaoCep").click(function () {
        $cep = $("#cliResCep").val();
        $.ajax({
            url: 'endereco.do?operacao=consultarCEP&cep=' + retirarCaracteresEspeciasELetras($cep),
            type: 'POST',
            cache: false,
			error: function(){    
			mensagem("Erro ao tentar consultar o CEP.", true);
			}, 
            success: function (resp) {
                var valor = resp.split("#");
                configCampoCEPGenerico('cliResUf',valor[1]);
		    	configCampoCEPGenerico('cliResCid',valor[2]);
		    	configCampoCEPGenerico('cliResBai',valor[3]);
		    	configCampoCEPGenerico('cliResRua',valor[4]);
		    	configCampoCEPGenerico('cliResNum',valor[5]);

		    	if(valor[1] == ''){
		    		$('#cliResUf').focus();    		
		    	}else if(valor[2] == ''){
		    		$('#cliResCid').focus();
		    	}else if(valor[3] == ''){
		    		$('#cliResBai').focus();
		    	}else if(valor[4] == ''){
		    		$('#cliResRua').focus();
		    	}else{
		    		$("#cliResNum").focus();
		    	}
            }
        });
    });
	
	fields = $('input, select,textarea');
	desabilitarALT(fields);
    if ($.browser.mozilla) {
        $(fields).keypress(checkForEnter);
    } else {
        $(fields).keydown(checkForEnter);
    }
	
	$("#botaoEndereco").click(function(){
		tipoForm(0);
		modal(500,355,'paginas/modal/busca-endereco.jsp','Marisa - Buscar CEP por logradouro',true,'',false);
		return true;
	});
	if ($.browser.mozilla) {
		$("#cliEmpSalario").keypress(function(){
			return mascara(this, moeda);
		});
		$("#cliCampLimite").keypress(function(){
			return mascara(this, moeda);
		});
	}
	else {
		$("#cliEmpSalario").keydown(function(){
			return mascara(this, moeda);
		});
		$("#cliCampLimite").keydown(function(){
			return mascara(this, moeda);
		});
	}
	$(document).ready(function(){
		$('.date-picker').datePicker({startDate:'01/01/1900'});  
	});
	changeCampos(true);
});

function avancarAbaMailing() {
	if(abaAtual == 3) {
		opentab(1);
	} else {
		abaAtual++;
		opentab(abaAtual);
	}
}

function voltarAbaMailing() {
	if(abaAtual == 1) {
		opentab(3);
	} else {
		abaAtual--;
		opentab(abaAtual);
	}
}

function tipoForm(flag){
	formCEP = flag;
	return true;
}

function buscarEnderecoSemCEP(){
	var uf = document.getElementById("cliBuscaUf").value.toUpperCase();
	var cidade = document.getElementById("cliBuscaCidade").value.toUpperCase();
	var logradouro = document.getElementById("cliBuscaLogradouro").value.toUpperCase();

	$.ajax({ 
		url: 'endereco.do?operacao=consultarSemCEP&uf='+uf+'&cidade='+cidade+'&logradouro='+logradouro,
		type:'POST', 
		cache: false,  
		error: function(){    
			mensagem("Erro ao tentar consultar o CEP.", true);
		}, 
		success: function (resp) { 
			$("#aguardeCep").html('<img id="aguardeCepImg" alt="Aguarde..." src="images/ajax-loader.gif" />');
			$("#corpoLista").html('');
			var valor = resp.split("#");
			for (i = 0; i < (valor.length - 1); i+=5 ) {
				var css ="";
				if ((i%2)==0){
					css = 'tr-odd';
				}else{
					css = 'tr-even';
				}
				StrMount = 	"<tr class='"+css+"'>" + "<td>"+valor[i+4]+"</td>" + "<td>"+valor[i]+"</td>" + "<td>"+valor[i+3]+"</td>" + "<td>"+valor[i+2]+"</td>" + "</tr>";
 				$("#corpoLista").append(StrMount);
			}
			$("#aguardeCep").html('Busca Encerrada, '+(valor.length - 1)/5+' logradouro(s) encontrados.');
			$.getScript('js/ms/nt.cadastropl.busca.marisa.js');
		}
	});
}

function validarCepEstatico(value){
	numeroCEP = value;
	
	numeroCEP = retirarMascara(numeroCEP);
	
	if(numeroCEP.length == 0 || numeroCEP.length == 8) {
		return true;
	}
	return false;
}
function salvarDados(){
	//QUEST_POINT - Evento aguardando posição da marisa.
	//mensagem('Aguardando Evento.',false);
	document.formMailing.action = "cadastroMailing.do?operacao=salvarCadastroMailling";
	document.formMailing.submit();	
}

function validarAbasComErro(){
	if(listAllError.length > 0){
		for ( var index = 0; index < listAllError.length; index++) {
			var any = listAllError[index];
			var idItem = $($(listAllError[index]).attr('element')).attr('id');
			var idTable = $("#"+idItem).parent().parent().parent().parent().attr('id');
			idTable = parseInt(idTable);
			
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
			opentab(idTable);
			$('#'+idItem).focus();
			return false;				
		}
	}else{
		return true;
	}
}

function consultarNomeCPFVendedor(){
	var valCpf = $('#cliCampCPF').val();
	
	valCpf = retirarMascara(valCpf);
	
	if(valCpf == '')
		return true;
	
	$.ajax({
		type:'POST',
		url:'funcionario.do?operacao=consultarFuncionario',
		data:{cliCpf:valCpf},
		cache:false,
		async:false,
		error: function(){    
			$('#cliCampNome').val('');
 		},
		success: function(retorno){
			retorno = retorno.split('#'); 
			if($.trim(retorno[0]) == 'S')
				$('#cliCampNome').val(retorno[1]);
			else
				$('#cliCampNome').val('');
		}	
	});
}


