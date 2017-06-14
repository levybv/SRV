// VARIÁVEL GLOBAL
var formCEP; 
var abaAtual = 1;
var tipoFormulario = null;
var idDaRenda;
var tipoCadastro = 'P';
$(document).ready(function () {
	
	$('#cliFalarCom').attr("disabled", "disabled");
	$("#cliApelido").focus();
	
	$('#cliResFoneDomi').change(function(){
		var valor = $(this).val();
		if(valor=='7#2'){
			$('#cliFalarCom').removeAttr("disabled");
		}else{
			$('#cliFalarCom').attr("disabled", "disabled");
			$('#cliFalarCom').val('');
		}
	});
	
		$("#cliEmpSalario").keypress(function(){
			return mascara(this, moeda);
		});
		$("#cliOutrasRendas").keypress(function(){
			return mascara(this, moeda);
		});
		
		//AUTO-TAB
		$('input').keyup(function(event){
			var id = $(this).attr('id');
		    var size = $(this).attr('maxlength');
			
			switch(id) {
			case 'cliEmail':
				autoTab(this,size,event,true);
				break;
			case 'cliRamalConj':
				autoTab(this,size,event,true);
				break;
			case 'cliResRamal':
				autoTab(this,size,event,true);
				break;
			case 'cliEmpRamal':
				autoTab(this,size,event,true);
				break;
			default: 
				autoTab(this,size,event,false);
			}
		});

    $(document).bind('keydown', 'esc', function () {fecharTodosOsModais(); });
    	
	jQuery(function ($) {
		$("#cliDtNascto").mask("99/99/9999");
	    $("#cliCampData").mask("99/99/9999");
	    $("#adiDtNascimento").mask("99/99/9999");
	    $("#cliEmpFone").mask("(99) 9999-9999");
	    $("#cliResFone").mask("(99) 9999-9999");
	    $("#cliNumCelular").mask("(99) 9999-9999");
	    $("#cliNumFone").mask("(99) 9999-9999");
	    $("#cliNumFoneRef1").mask("(99) 9999-9999");
	    $("#cliNumFoneRef2").mask("(99) 9999-9999");
	    $("#cliCpf").mask("999.999.999-99");
	    $("#cliCampCPF").mask("999.999.999-99");
	    $("#adiCliCpf").mask("999.999.999-99");
	    $("#cpfVendedor").mask("999.999.999-99");
	    $("#cpfVendedorSeguro").mask("999.999.999-99");
	    $("#cliEmpCep").mask("99999-999");
	    $("#cliResCep").mask("99999-999");
	    $("#cliCelularSMS").mask("(99) 9999-9999");
	    $("#cliResDtDesde").mask("99/99");
	    $("#cliDtEmissaoCpfExibicao").mask("99/9999");
		$("#cliDtValidadeDocumento, #cliDtExpedicaoDocumento, #cliDtEmissaoCpf").mask("99/99/9999");
	});
		
    $("#conteudo > div").hide();
    $("#conteudo > div:eq(0)").show();
    $("#tabs > a:eq(0)").css("background-color", "#E9EBE8");
    
    $('input:[type=text], select').focus(function () {
        $(this).removeClass("campo");
        $(this).addClass("focusGained");
    });
    
    $('input:[type=text], select').blur(function () {
        $(this).removeClass("focusGained");
        $(this).addClass("campo");
    });
    
    $(".linkDetalhe").click(function () {
        if ($(".panelDetalhe").is(":visible")) {
            $(".panelDetalhe").hide(100);
        } else {
            $(".panelDetalhe").show(100);
        }
    });
    
    $(".panelDetalhe").hide(100);
    
    $("#cliCrediarioDomi").change(function () {
        $crediarioLoja = $("#cliCrediarioDomi").val();
        if ($crediarioLoja == '8#1') {
            $("#cliCrediarioLoja").removeAttr("disabled");
            $("#cliCrediarioLoja").focus();
        } else {
            $("#cliCrediarioLoja").attr("disabled", "disabled");
            $("#cliVencimento").focus();
        }
    });

    $("#cliResFoneDomi").change(function () {
        $telefoneRecado = $("#cliResFoneDomi").val();
        if ($telefoneRecado == '7#2') {
            $("#L2_sub_1").show();
        } else {
            $("#L2_sub_1").hide();
            $("#cliFalarCom").val('');
        }
    });
    
    $("#L2_sub_1").hide();
    	
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
									modal(400, 130,'paginas/modal/cep-nao-encontrado.jsp','Marisa - Cadastro PL',true,'', false);
									document.form.cliResUf.value = "";
									document.form.cliResCid.value = "";
									document.form.cliResBai.value = "";
									document.form.cliResRua.value = "";
									
								}
								else {
									if(validarEnderecoCEPUF()){
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
							}
						});
					}
				}
    });

    $("#cliEmpCep").change(function () {
    	$('#isCEPEmpGenerico').val('N');
        $cep = $("#cliEmpCep").val();
        
		var profissionalCep = $.trim($("#cliEmpCep").val());
		var resultante = eval('validarCepEstatico("' + profissionalCep + '")');
				if (!resultante) {
					$("#erroCliEmpCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
					$("#erroCliEmpCep").show();
				}else{
					$("#erroCliEmpCep").html();
					$("#erroCliEmpCep").hide();
					var checar = $cep;

					checar = retirarMascara(checar);
					
					if (checar.length < 8) {
						document.form.cliEmpUf.value = "";
						document.form.cliEmpCid.value = "";
						document.form.cliEmpBai.value = "";
						document.form.cliEmpRua.value = "";
						return false;
					}else {
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
									formCEP="PROFIS";
									fecharModal();
									
									modal(400, 130,'paginas/modal/cep-nao-encontrado.jsp','Marisa - Cadastro PL',true,'', false);
								}else {
									configCampoCEPGenerico('cliEmpUf',valor[1]);
							    	configCampoCEPGenerico('cliEmpCid',valor[2]);
							    	configCampoCEPGenerico('cliEmpBai',valor[3]);
							    	configCampoCEPGenerico('cliEmpRua',valor[4]);
							    	
							    	if(valor[1] == ''){
							    		$('#cliEmpUf').focus();    		
							    	}else if(valor[2] == ''){
							    		$('#cliEmpCid').focus();
							    	}else if(valor[3] == ''){
							    		$('#cliEmpBai').focus();
							    	}else if(valor[4] == ''){
							    		$('#cliEmpRua').focus();
							    	}else{
							    		$("#cliEmpNum").focus();
							    	}
								}
							}
						});
					}
				}		
    });

    // HABILITA OU DESABILITA CAMPOS REQUERIDOS AO CLICK EM CHECKBOX
    $("#saldoSMS").click(function () {
        var chk = document.getElementById("saldoSMS").checked;
        if (chk){ 
        	$("#cliCelularSMS").removeAttr("disabled");
        } else {
        	$("#cliCelularSMS").attr("disabled", "disabled");
        	$("#cliCelularSMS").val("");
        }
    });

    $("#faturaEmail").click(function () {
        var chk = document.getElementById("faturaEmail").checked;
        if (chk){ 
        	$("#cliMailFatura").removeAttr("disabled");
        } else {
        	$("#cliMailFatura").attr("disabled", "disabled");
        	$("#cliMailFatura").val("");
        }
    });

    //-------------------------------------------------------------------------
    
	fields = $('input, select');
	fieldsall = fields ;
	desabilitarALT(fields);
	
	if ($.browser.mozilla) {
        $(fields).keypress(checkForEnter);
    } else {
        $(fields).keydown(checkForEnter);
    }

    $('#botaoFluxo').click(function () {
    	habilitarSelectsSubmissao();
		tipoFormulario = 'ANALISE';
		modal(300, 110, 'paginas/modal/concluir-sala-analise.jsp', "MARISA - Sala de Análise", true, '', false);		
    });
    
    $('#botaoPendenciado').click(function(){
		modal(360, 120,'paginas/modal/concluir-pendenciamento.jsp','Marisa - Confirmar Pendenciado',true,'',false);
	});

    $('#botaoAprovar').click(function () {
    	tipoFormulario = 'MATRIZ';
    	$('form').submit();			
	});

    $('#botaoReprovar').click(function () {
		
		if($("#reprovarAnalise").val() == 'S') {
			tipoFormulario = 'REJEITAR_SALA';
		} else {
			tipoFormulario = 'REJEITAR_MATRIZ';
		}
					
		$('form').submit();
    });
    
    $('#btnAprovarCentral').click(function () {
    	tipoFormulario = 'APROVAR_CENTRAL';
    	$('form').submit();		
    });
    
    $('#btnReprovarCentral').click(function () {    	
    	tipoFormulario = 'REPROVAR_CENTRAL';
    	$('form').submit();		
    });

    $('#botaoCancelar').click(function () {
        document.form.action = 'cadastroPL.do?operacao=cancelarProposta';
        document.form.submit();
    });
    
    $('#botaoCancelarCredito').click(function () {
        document.form.action = 'mesa.do?operacao=cancelarProposta'; 
        document.form.submit(); 
    });
    
    $('#botaoCancelarCreditoCoordenador').click(function () {
        document.form.action = 'mesa.do?operacao=cancelarProposta&mesaCoordenador=S';  
        document.form.submit(); 
    });

    $('#botaoDerivar').click(function () {
		tipoFormulario = 'DERIVAR';
		$('form').submit();
    });
    
    //ACERTA OS CAMPOS DA ABA PROFISSIONAL
    changeCampos(false);
    
	//ACERTA OS CAMPOS DA ABA CONJUGE
	$('#cliCPFConj').addClass('readonly');
	$('#cliCPFConj').attr("readonly", "readonly");
	$('#cliNomeConj').addClass('readonly');
	$('#cliNomeConj').attr("readonly", "readonly");
	$('#cliIdentidadeConj').addClass('readonly');
	$('#cliIdentidadeConj').attr("readonly", "readonly");
	$('#cliDtNascConj').addClass('readonly');
	$('#cliDtNascConj').attr("readonly", "readonly");
	$('#cliTelefoneConj').attr("readonly", "readonly");
	$('#cliTelefoneConj').addClass('readonly');
	$('#cliRamalConj').attr("readonly", "readonly");
	$('#cliRamalConj').addClass('readonly');
	
	$("#cliEmpSalario").change(function(){
		checarRenda(this);
	});
	
	$("#cliDtNascto").change(function(){
		$("#isEmancipado").val('N');
		checarEmancipado(this, 'S');
	});
	
	$("#cliDtNascConj").change(function(){
		$("#isEmancipadoConjuge").val('N');
		checarEmancipado(this, 'N');
	});
	
	$("#botaoEndereco").click(function(){
		tipoForm("0");
		modal(500,360,'paginas/modal/busca-endereco.jsp','Marisa - Buscar CEP por logradouro',true,'',false);
		return true;
	});
	
	$("#botaoEmpEndereco").click(function() {
		tipoForm("1");
		modal(500,360,"paginas/modal/busca-endereco.jsp","Marisa - Buscar CEP por logradouro",true,"",false);
	});
	
	$("#imgPesquisarFilial").click(function(){
		modal(510,370,'paginas/modal/busca-filial.jsp','Marisa - Buscar Filial',true,'',false);
		return true;
	});
	
	$(document).ready(function(){
		$('.date-picker').datePicker({startDate:'01/01/1900'});  
	});
	
	$('#cliApelido').focus();
	
	$('#openChatPopup').click(function () {

		if($("#isWindowOpenCentral").val() == 'N') {
		
		  $.ajax({ 
			  	url: 'chat.do?operacao=isOpenCentral&isWindowOpenCentral=' + 'S',  
			  	type:'POST', 
			  	cache: false,
			  	async:false,
			  	error: function(){    
			  		mensagem("Erro ao tentar conectar o chat.", true);
  		  		},  
			  	success: function (resp) {
			  		window.open('paginas/modal/chat-central.jsp?cliCpfChat='+$("#cliCpf").val(),'page','toolbar=no,location=no,status=yes,menubar=no,scrollbars=no,resizable=no,width=620,height=380');
			  		$("#isWindowOpenCentral").val('S');
			 	} 
	   	  });

		} else {
			  $.ajax({ 
				  	url: 'chat.do?operacao=isOpenCentral&isWindowOpenCentral=' + 'F',  
				  	type:'POST', 
				  	cache: false,
				  	async:false,
				  	error: function(){    
				  		mensagem("Erro ao tentar conectar o chat.", true);
	  		  		},  
				  	success: function (resp) {
				  		if($.trim(resp) == 'N') {
					  		window.open('paginas/modal/chat-central.jsp?cliCpfChat='+$("#cliCpf").val(),'page','toolbar=no,location=no,status=yes,menubar=no,scrollbars=no,resizable=no,width=620,height=380');
					  		$("#isWindowOpenCentral").val('S');
				  		}
					 } 
		   	  });
		}
		
	});
	addHintCampos();
	
});

function buscarEnderecoSemCEP(){
			var uf = document.getElementById("cliBuscaUf").value.toUpperCase();
			var cidade = document.getElementById("cliBuscaCidade").value.toUpperCase();
			var logradouro = document.getElementById("cliBuscaLogradouro").value.toUpperCase();
		
			$.ajax({ 
				url: 'endereco.do?operacao=consultarSemCEP&uf='+uf+'&cidade='+cidade+'&logradouro='+logradouro,
				type:'POST', 
				cache: false, 
				async: false, 
				error: function(){    
		  			mensagem("Erro ao tentar consultar o endereço.", true);
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
						StrMount = 	"<tr class='"+css+"'>" + "<td><a href='#' id='"+valor[i+4]+"'>"+valor[i+4]+"</a></td>" + "<td>"+valor[i]+"</td>" + "<td>"+valor[i+3]+"</td>" + "<td>"+valor[i+2]+"</td>" + "</tr>";
		 				$("#corpoLista").append(StrMount);
					}
					$("#aguardeCep").html('Busca Encerrada, '+(valor.length - 1)/5+' logradouro(s) encontrados.');
					$.getScript('js/ms/nt.cadastropl.busca.marisa.js');
				}
			});
}

function showItens(id, id_chk) {
    var id = '#'+id_chk;
	var chk = $(id).val();

    var str = "'#" + id + "'";

    if (chk) {
        $(str).show();
    } else {
        $(str).hide();
    }
}
function tipoForm(flag){
	formCEP = flag;
	return true;
}
function valueReplicante(out,input){
	var outSrc = "#"+out;
	var inputSrc = "#"+input;
		
		$(inputSrc).val($(outSrc).val());
		removerMensagemErro($(inputSrc));
		
}

function mostraItens(id,ch){
	
	flag = document.getElementById(ch).checked;
	stc = "#"+id;
	
	if (id=="divCelularSMS"){
		
		var cell = document.getElementById("cliNumCelular").value;
		$("#cliCelularSMS").val(cell);
		
		if (flag){	
			$(stc).show();
			$("#cliCelularSMS").removeAttr("readonly");
			$(function($){
				$('#cliCelularSMS').unmask();
				$('#cliCelularSMS').mask("(99) 9999-9999");
			});
		}else{
			$(stc).hide();
			$("#cliCelularSMS").attr("readonly", "readonly");
			$("#cliCelularSMS").val("");
			$(function($){
				$('#cliCelularSMS').unmask();
			});
		}
		
	}
	
	if (id=="divMailFatura"){
		
		var mail = document.getElementById("cliEmail").value;
		$("#cliMailFatura").val(mail);
		
		if (flag){	
			$(stc).show();
			$("#cliMailFatura").removeAttr("readonly");
		}else{
			$(stc).hide();
			$("#cliMailFatura").attr("readonly", "readonly");
			$("#cliMailFatura").val("");
		}
	}
		
}

//MODIFICA PERMISSÕES DOS CAMPOS DO FORMULÁRIO DO CONJUGE
function changeFields(val) {
	
		var valor = val;
		
		if (valor == 'S'){
			$('#cliCPFConj').removeClass('readonly');
			$('#cliCPFConj').removeAttr("readonly");
			
			$('#cliNomeConj').removeClass('readonly');
			$('#cliNomeConj').removeAttr("readonly");
			
			$('#cliIdentidadeConj').removeClass('readonly');
			$('#cliIdentidadeConj').removeAttr("readonly");
			
			$('#cliDtNascConj').removeClass('readonly');
			$('#cliDtNascConj').removeAttr("readonly");
			
			$('#cliTelefoneConj').removeAttr("readonly");
			$('#cliTelefoneConj').removeClass('readonly');
			
			$('#cliRamalConj').removeAttr("readonly");
			$('#cliRamalConj').removeClass('readonly');
			//	ADD MASK
			$(function($){
				$('#cliCPFConj').mask("999.999.999-99");
				$('#cliDtNascConj').mask("99/99/9999");
				$('#cliTelefoneConj').mask("(99) 9999-9999");	
			});
			$('#cliCPFConj').focus();
		} else if (valor == 'N'){
			$('#cliCPFConj').val('');
			$('#cliCPFConj').addClass('readonly');
			$('#cliCPFConj').attr("readonly", "readonly");
			removerMensagemErro($('#cliCPFConj'));
			 
			$('#cliNomeConj').val('');
			$('#cliNomeConj').addClass('readonly');
			$('#cliNomeConj').attr("readonly", "readonly");
			removerMensagemErro($('#cliNomeConj'));
			
			$('#cliIdentidadeConj').val('');
			$('#cliIdentidadeConj').addClass('readonly');
			$('#cliIdentidadeConj').attr("readonly", "readonly");
			removerMensagemErro($('#cliIdentidadeConj'));
			
			$('#cliDtNascConj').val('');
			$('#cliDtNascConj').addClass('readonly');
			$('#cliDtNascConj').attr("readonly", "readonly");
			removerMensagemErro($('#cliDtNascConj'));
			
			$('#cliTelefoneConj').val('');
			$('#cliTelefoneConj').attr("readonly", "readonly");
			$('#cliTelefoneConj').addClass('readonly');
			removerMensagemErro($('#cliTelefoneConj'));
			
			$('#cliRamalConj').val('');
			$('#cliRamalConj').attr("readonly", "readonly");
			$('#cliRamalConj').addClass('readonly');
			removerMensagemErro($('#cliRamalConj'));
			
			//	REMOVE MASK
			$('#cliCPFConj').unmask();
			$('#cliDtNascConj').unmask();
			$('#cliTelefoneConj').unmask();
		}
}

function trataAdicional(index){
	var adiCliCpf = document.getElementById('adiCliCpf'+index);
	var adiNome = document.getElementById('adiNome'+index);
	var adiDtNascimento = document.getElementById('adiDtNascimento'+index);
	var adiParentesco = document.getElementById('adiParentesco'+index);
	var adiSexo = document.getElementById('adiSexo'+index);
	var adiCartaoAdicional = document.getElementsByName('adiCartaoAdicional'+index);
	var adiTelefone = document.getElementsByName('adiTelefone'+index);
	var adiRamal = document.getElementsByName('adiRamal'+index);
	
	
	if (adiCartaoAdicional[0].checked){
		$(adiCliCpf).removeAttr("readonly");
		$(adiNome).removeAttr("readonly");
		$(adiDtNascimento).removeAttr("readonly");
		$(adiTelefone).removeAttr("readonly");
		$(adiRamal).removeAttr("readonly");
		$(adiParentesco).removeAttr("disabled");
		$("input[name='adiSexo"+index+"']").removeAttr("disabled"); 
		$(adiCliCpf).removeClass("readonly");
		$(adiNome).removeClass("readonly");
		$(adiDtNascimento).removeClass("readonly");
		$("input[name='adiSexo"+index+"']").removeClass("readonly");
		$(adiTelefone).removeClass("readonly");
		$(adiRamal).removeClass("readonly");
		$(adiTelefone).addClass("campo");
		$(adiRamal).addClass("campo");
		$(adiCliCpf).addClass("campo");
		$(adiNome).addClass("campo");
		$(adiDtNascimento).addClass("campo");
		$("input[name='adiSexo"+index+"']").addClass("campo");
		$(function($){
				$(adiCliCpf).unmask();
				$(adiDtNascimento).unmask();
				$(adiTelefone).unmask();
				$(adiCliCpf).mask("999.999.999-99");
				$(adiDtNascimento).mask("99/99/9999");
				$(adiTelefone).mask("(99) 9999-9999");
			});
		$(adiCliCpf).focus();
	} else {
		$(adiCliCpf).attr({readonly: "readonly"});
		$(adiCliCpf).val("");
		 removerMensagemErro(adiCliCpf);
		 
		$(adiNome).attr("readonly", "readonly");
		$(adiNome).val("");
		 removerMensagemErro(adiNome);
		 
		$(adiDtNascimento).attr("readonly", "readonly");
		$(adiDtNascimento).val("");
		 removerMensagemErro(adiDtNascimento);
	
		$(adiTelefone).attr("readonly", "readonly");
		$(adiTelefone).val("");
		removerMensagemErro(adiTelefone);
		
		$(adiRamal).attr("readonly", "readonly");
		$(adiRamal).val("");
		removerMensagemErro(adiRamal);
		
		$(adiParentesco).attr("disabled", "disabled");
		$(adiParentesco).children().eq(0).attr({"selected":"selected"});
		removerMensagemErro(adiParentesco);
	
		$("input[name='adiSexo"+index+"']").attr("disabled", "disabled");
		removerMensagemErro($("input[name='adiSexo"+index+"']"));
			
		$(adiCliCpf).removeClass("campo");
		$(adiNome).removeClass("campo");
		$(adiDtNascimento).removeClass("campo");
		$("input[name='adiSexo"+index+"']").removeClass("campo");
		$(adiTelefone).removeClass("campo");
		$(adiRamal).removeClass("campo");
		$(adiCliCpf).addClass("readonly");
		$(adiNome).addClass("readonly");
		$(adiDtNascimento).addClass("readonly");
		$("input[name='adiSexo"+index+"']").addClass("readonly");
		$(adiTelefone).addClass("readonly");
		$(adiRamal).addClass("readonly");
		$(function($){
				$(adiCliCpf).unmask();
				$(adiDtNascimento).unmask();
				$(adiTelefone).unmask();
		});
	}
	
}

function checarRenda(campo){
	var valor = $(campo).val();
	idDaRenda = $(campo).attr('id');
	
	valor = retirarMascara(valor);
	
	if (valor >= 10000){
		modal(500,120,'paginas/modal/confirma-renda.jsp','Marisa - Cadastro PL',true,'',false);
	}else{
		return true;
	}
}

function checarEmancipado(campo, operacao) {
	
	var value = $(campo).val();
	var dataValue = $(campo).val();
	
	dataValue = retirarMascara(dataValue);
	
	if(dataValue == "" || dataValue.length != 8) {
		return true;
	}
	
	var hoje = new Date();
	 
	var arrayData = value.split("/");
	 
	if (arrayData.length == 3) {
	  // Decompoem a data em array
	  var ano = parseInt( arrayData[2] );
	  var mes = parseInt( arrayData[1] );
	  var dia = parseInt( arrayData[0] );
	  
	  // Valida a data informada
	  if ( arrayData[0] > 31 || arrayData[1] > 12 ) {
	   return false;
	  }  
	  
	  ano = ( ano.length == 2 ) ? ano += 1900 : ano;
	  
	  // Subtrai os anos das duas datas
	  
	  var idade = ( hoje.getFullYear()) - ano;
	  var nMes 	= (hoje.getMonth() + 1) - mes;
	  var nDias = hoje.getDate() - dia;
	  var emancipado = false;
	  
	  if(idade > 18){
	  	emancipado = false;
	  }else if(idade == 18){
	  	if(nMes > 0 || ( nMes == 0 && nDias >= 0))
			emancipado = false;
		else
			emancipado = true;
	  }else if(idade == 17){
			emancipado = true;
	  }else if(idade == 16){
	  	if(nMes > 0 || ( nMes == 0 && nDias >= 0))
			emancipado = true;
		else
			emancipado = false;
	  }

	if (emancipado){
		
		if(operacao == 'S') {
			modal(250,110,'paginas/modal/confirma-emancipado.jsp','Marisa - Cadastro PL',true,'',false);
		} else {
			modal(275,110,'paginas/modal/confirma-emancipado-conjuge.jsp','Marisa - Cadastro PL',true,'',false);
		}
		
	} else {
		return true;
	}
	
	}
	
	 return false;
}

function habilitaCpfVendedor(radio){
	var flag = $(radio).val();
	if (flag=="S"){
		habilitarCampo('segCpfVendedor',true,'999.999.999-99',true);
		$('#segCpfVendedor').focus();
	} else if(flag=="N"){
		habilitarCampo('segCpfVendedor',false,false,true);
	}
}

function habilitaCombo(radio){
		var flag = $(radio).val();
		if(flag=="S"){
			habilitarCampo('codComunicacao',true,false,true);
	   	}else if (flag=="N"){
	   		habilitarCampo('codComunicacao',false,false,true);
			$("#codComunicacao").children().eq(0).attr("selected");
		}
}

function submitForm(){
	fecharModal();
	$("#botao").empty().html('<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif"> ');
    if ($.browser.mozilla) {
    	document.forms[0].submit();
    } else {
    	form.submit();	
    }  
}

function habilitarSelectsSubmissao(){
	$("#cliSexoDomi").removeAttr("disabled");
	$("#cliEcDomi").removeAttr("disabled");
	$("#cliResCasaDomi").removeAttr("disabled");
	$("#cliCrediarioDomi").removeAttr("disabled");
	$("#cliOutrosCarDomi").removeAttr("disabled");
	$("#cliFilialRetira").removeAttr("disabled");
	$("#cliCrediarioLoja").removeAttr("disabled");
	
	if($("#cargoNome").attr("disabled")) {
		$("#cargoNome").removeAttr("disabled");
	}
	
	if($("#cliEmpOcupacao").attr("disabled")) {
		$("#cliEmpOcupacao").removeAttr("disabled");
	}	
}

function addHintCampos(){
	$(function() {
		$('#cliNome')                 .tipsy({trigger: 'focus', gravity: 's', fallback: 'Digitar Nome Conforme Documento.'});
		$('#cliDependentes')          .tipsy({trigger: 'focus', gravity: 'w', fallback: 'Digitar o Número de Dependentes.'});
		$('#cliMae')                  .tipsy({trigger: 'focus', gravity: 'w', fallback: 'Digitar Nome Conforme Documento.'});	
		$('#cliIdentidade')           .tipsy({trigger: 'focus', gravity: 's', fallback: 'Digitar Exatamente como no Documento Apresentado.'});
		$('#cliIdeUfEmi')             .tipsy({trigger: 'focus', gravity: 's', fallback: 'UF de Emissão do Documento Apresentado.'});
		$('#cliDtValidadeDocumento')  .tipsy({trigger: 'focus', gravity: 'w', fallback: 'Existe data de Validade? Informe.'});
		$('#cliIdentidadeConj')   	  .tipsy({trigger: 'focus', gravity: 'w', fallback: 'Digitar Exatamente como no Documento Apresentado.'});
		$('#cliEmpEmpresa')           .tipsy({trigger: 'focus', gravity: 'w', fallback: 'Informe Corretamente o Nome da Empresa.'});
		$('#cliEmpSalario')           .tipsy({trigger: 'focus', gravity: 'w', fallback: 'Informe a Renda Bruta.'});
	});
}

