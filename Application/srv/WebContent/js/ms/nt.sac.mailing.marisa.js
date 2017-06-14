var tipoCadastro = 'S';
fields = $('input, select');
	fieldsall = fields ;
	desabilitarALT(fields);
	
var formCEP;
	$(document).ready(function () {
		isSAC = true;
		//AUTO-TAB
	$('input').keyup(
		function(event){
			var size = $(this).attr('maxlength');
			autoTab(this,size,event);
		}
	);
		
	jQuery(function(){
		$("#cliCgcCnpj").mask('99.999.999/9999-99');
		$("#cliEmpCep, #cliResCep").mask("99999-999");
		$("#cliResFone").mask("(99) 9999-9999");
		$("#cliCampCPF").mask("999.999.999-99");
		$("#cliDtNascto, #cliDtAberEmpresa, #cliEmpDtAdm").mask("99/99/9999");
		$("#cliResDtDesde").mask("99/99");	
	});
	
	
    $("#cliResCep").change(function () {
    	$('#isCEPResGenerico').val('N');
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
						document.form.cliResUf.value = "";
						document.form.cliResCid.value = "";
						document.form.cliResBai.value = "";
						document.form.cliResRua.value = "";
						return false;
					}
					else {
						$.ajax({
							url: 'endereco.do?operacao=consultarCEP&cep=' + retirarCaracteresEspeciasELetras($cep),
							type: 'POST',
							cache: false,
							async:false,
							error: function(){    
							mensagem("Erro ao tentar consultar o CEP.", true);
							}, 
							success: function(resp){
								var valor = resp.split("#");

								if ($.trim(valor[0]) == "") {
									formCEP="RESIDE";
									fecharModal();
									modal(400, 130,'paginas/modal/cep-nao-encontrado.jsp','Marisa - SAC Mailing',true,'', false);
									document.form.cliResUf.value = "";
									document.form.cliResCid.value = "";
									document.form.cliResBai.value = "";
									document.form.cliResRua.value = "";
									
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
	$("#cliNome, #cliIdeOrgaoEmi").alpha({allow:"QWERTYUIOPLKJHGFDSAZXCVBNM �����������������Ѻ"});
	$("#cliMae, #cliPai").alpha({allow:"0123456789QWERTYUIOPLKJHGFDSAZXCVBNM �����������������Ѻ"});
	$("#cliDtNascto, #cliEmpNum, #cliResRamal, #cliDependentes, #cliResFone").numeric(); 
	$("#cliIdentidade").alpha({allow:"0123456789QWERTYUIOPLKJHGFDSAZXCVBNM"});
	
	//VALIDA��ES DE TODOS OS CAMPOS DE TODAS AS ABAS DO CADASTRO MAILING
	$("#formMailing").validate({
		rules: {
			cliVencimento:{required:true},
			cliResFone:{validaDDDMarisa:true, validaTelefoneMarisa:true, required:true},
			cliResCep: {cepRequerido:true},
			cliResUf:{required: true},
			cliResCid:{required: true},
			cliResBai:{required: true},
			cliResRua:{required: true},
			cliCpf:{cpf:true,required:true},
			cliNome: {validaNome:true,required:true},
			cliDtNascto: {dateBR: true,dataMaior:true,validaIdadeMarisa: true,required: true},
			cliNumCelular:{validaCelularMarisa:true},
			cliSexoDomi:{required:true},
			cliEmail:{email:true},
			//cliIdentidade:{required:true},
			//cliIdeOrgaoEmi:{required:true},
			//cliIdeUfEmi:{required:true},
			cliEcDomi:{required:true},
			cliDependentes:{required:true},
			//cliMae: {validaNome:true,required: true},
			cliEmpEmpresa:{required:true},
			cliCgcCnpj:{cnpj:true,required:true},
			cliEmpCep:{cepRequerido:true},
			cliEmpNum:{required:true},
			cliEmpFone:{validaDDDMarisa:true, validaTelefoneMarisa:true, required:true},
			cliEmpSalario:{required:true, validarValorMinimoRenda: true},
			cliEmpOcupacao:{required:true},
			cliResUf:{required:true},
			cliResNum:{required:true},
			cliResDtDesde:{required:true},
			cliCampLimite:{required:true},
			cliCampCaptacao:{required:true}
		},
		messages: {
			cliVencimento:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo Dia para Vencimento obrigat�rio.</span>'},
			cliResFone: {validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um n�mero de DDD v�lido.</span>', validaTelefoneMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um n�mero de Telefone Residencial v�lido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone Residencial � obrigat�rio.</span>'},
			cliResCep: {cepRequerido: ''},
			cliResUf:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo UF � obrigat�rio.</span>'},
			cliResCid:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Cidade � obrigat�rio.</span>'},
			cliResBai:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Bairro � obrigat�rio.</span>'},
			cliResRua:{required:'<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Rua � obrigat�rio.</span>'},
			cliCpf:{cpf:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CPF inv�lido.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo CPF obrigat�rio.</span>'},
			cliNome: {validaNome: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome precisa ter pelo menos duas palavras.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> O campo Nome � obrigat�rio.</span>'},
			cliDtNascto: {dateBR: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data inv�lida.</span>',dataMaior: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Data informada n�o pode ser maior que data atual.</span>', validaIdadeMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Idade do cliente menor que a idade m�nima para Titular.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem"/>O campo Data de Nascimento � obrigat�rio.</span>'},
			cliNumCelular:{validaCelularMarisa:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />N�mero de Celular Inv�lido.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Celular � obrigat�rio.</span>'},
			cliSexoDomi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um Sexo.</span>'},
			cliEmail:{email:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Email Inv�lido.</span>'},
			//cliIdentidade:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo identidade obrigat�rio.</span>'},
			//cliIdeOrgaoEmi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo org�o obrigat�rio.</span>'},
			//cliIdeUfEmi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Campo UF obrigat�rio.</span>'},
			cliEcDomi:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione um estado civil.</span>'},
			cliDependentes:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe a quantidade de dependentes.</span>'},
			//cliMae:{validaNome:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Nome da M�e precisa ter pelo menos duas palavras.</span>',required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Nome da M�e � obrigat�rio.</span>'},
			cliEmpEmpresa:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe a Empresa.</span>'},
			cliCgcCnpj:{cnpj:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />CGC/CNPJ Inv�lido.</span>',required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo CGC/CNPJ � obrigat�rio.</span>'},
			cliEmpCep:{cepRequerido:''},
			cliEmpNum:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe o n�mero.</span>'},
			cliEmpFone:{validaDDDMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Informe um n�mero de DDD v�lido.</span>', validaTelefoneMarisa: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe um n�mero de Telefone v�lido.</span>', required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo Telefone � obrigat�rio.</span>'},
			cliEmpSalario:{required: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo Renda Mensal � obrigat�rio.</span>', validaNumRenda: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Permitido somente n�meros.</span>', validarValorMinimoRenda: '  <span class="erro"><img src="images/ico_error.gif" class="imagem" />O valor da Renda Mensal tem que ser maior que zero.</span>'},
			cliEmpOcupacao:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Selecione uma ocupa��o.</span>'},
			cliResUf:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />UF � obrigat�rio.</span>'},
			cliResNum:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe o n�mero.</span>'},
			cliResDtDesde:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Tempo de Resid�ncia obrigat�rio.</span>'},
			cliCampLimite:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Limite pr�-aprovado obrigat�rio.</span>'},
			cliCampCaptacao:{required:'  <span class="erro"><img src="images/ico_error.gif" class="imagem" />Informe a Campanha.</span>'}
		},
		submitHandler : function(form){
			
			var residenciaCep = $.trim($("#cliResCep").val());
			var empresaCep = $.trim($("#cliEmpCep").val());
						
			if (!eval('validarCepEstatico("'+residenciaCep+'")')) {
				$("#erroCliResCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
				$("#erroCliResCep").show();
				opentab(2);
			} else if (!eval('validarCepEstatico("'+empresaCep+'")')){
				$("#erroCliEmpCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
				$("#erroCliEmpCep").show();
				opentab(3);
			} else {
				$("#erroCliResCep").html('');
				$("#erroCliResCep").hide();
				$("#erroCliEmpCep").html();
				$("#erroCliEmpCep").hide();
				//fecharModal();
				//modal(350, 120, 'paginas/modal/concluir-cad-sac-mailing.jsp', 'Marisa - SAC Mailing', true, '', false);
				document.formMailing.action = "cadastroSacMailing.do?operacao=salvarCadastroSAC";
				document.formMailing.submit();
			}	
			//OUTRAS VALIDA��ES
			
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
            async: false,
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
		    	setFocusNoProximoCampoHabilitado();
            }
        });
    });
	//setTimeout("globalvar.focus()", 1);
	
	fields = $('input, select,textarea');
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
	
	$("#cliEmpDtAdm").change(function(){
		var campoData01 = $("#cliDtAberEmpresa").val();
		var campoData02 = $(this).val();
		if(campoData01==""){
			return true;
		}else{
			while (campoData01.indexOf("/") != -1) {campoData01 = campoData01.replace("/","");}
			while (campoData01.indexOf("_") != -1) {campoData01 = campoData01.replace("_","");}
			while (campoData02.indexOf("/") != -1) {campoData02 = campoData02.replace("/","");}
			while (campoData02.indexOf("_") != -1) {campoData02 = campoData02.replace("_","");}
			campoData01 = String(campoData01);
		
			var data01 = new Date();
			var data02 = new Date();
			data01.setDate(campoData01.substring(0,2));
			data01.setMonth(campoData01.substring(2,4)-1);
			data01.setFullYear(campoData01.substring(4,8));
			
			data02.setDate(campoData02.substring(0,2));
			data02.setMonth(campoData02.substring(2,4)-1);
			data02.setFullYear(campoData02.substring(4,8));
			
			if(data02 < data01){
				fecharModal();
				modal(400,150,null,'Marisa - SAC Mailing.',true,'A data de demiss�o n�o pode ser menor que a data de admiss�o.',false);
				$(this).val('');
			}
		}
	});
	
	$(document).ready(function(){
		$('.date-picker').datePicker({startDate:'01/01/1900'});  
	});

});

function avancarAbaMailing() {
	if(abaAtual == 8) {
		opentab(1);
	} else {
		abaAtual++;
		opentab(abaAtual);
	}
}

function voltarAbaMailing() {
	if(abaAtual == 1) {
		opentab(8);
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
			for ( i = 0; i < (valor.length - 1); i+=5 ) {
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
	while (numeroCEP.indexOf("_") != -1) { numeroCEP = numeroCEP.replace("_", "");}
	while (numeroCEP.indexOf("-") != -1) { numeroCEP = numeroCEP.replace("-", "");}
	if(numeroCEP.length == 0 || numeroCEP.length == 8) {
		return true;
	}
	return false;
}