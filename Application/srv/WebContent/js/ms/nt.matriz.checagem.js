
$(document).ready(function(){
	$('input[type="checkbox"]').click(function(){
		var id = $(this).attr('id');
		var checando = $(this).attr('checked');
		id = id.substring(0, 8);
		
		if (id == "docIdent") {
			if (checando) {
				checaIdent++;
				$('#divErroIdent').hide();
				$('#divErroIdent').html('');
			}
			else {
				checaIdent--;
			}
		}else if (id == "docRenda") {
				if (checando) {
					checaRenda++;
					$('#divErroRenda').hide();
					$('#divErroRenda').html('');		
				}
				else {
					checaRenda--;
				}
		}else {
			id = id.substring(0, 6);
			if (id == 'docEnd') {
				if (checando) {
					checaEnd++;
					$('#divErroEnd').hide();
					$('#divErroEnd').html('');
				}
				else {
					checaEnd--;
				}
			} else {
					
				var id = $(this).attr('id');
				id = id.substring(0, 13);
				if (id == 'docEmancipado') {
					if (checando) {
						checaEmancipado++;
						$('#divErroEmancipado').hide();
						$('#divErroEmancipado').html('');
					}
					else {
						checaEmancipado--;
					}
				}
			}
		}
		
		
	});	
	//IDENTIFICAÇÃO
	if ($('cliMatrizProcedenciaIdent').attr('checked')) {
		tratarCamposMatriz('Ident',false);
	}
	else {
		tratarCamposMatriz('Ident',true);
	}
	//ENDEREÇO
	if ($('cliMatrizProcedenciaEnd').attr('checked')) {
		tratarCamposMatriz('End',false);
	}
	else {
		tratarCamposMatriz('End',true);
	}
	//RENDA
	if ($('cliMatrizProcedenciaRenda').attr('checked')) {
		tratarCamposMatriz('Renda',false);
	}else{
		tratarCamposMatriz('Renda',true);
	}
	
	$('#cliMatrizProcedenciaIdent').click(function(){
		var flag = $(this).attr('checked');
		if (flag) {
			tratarCamposMatriz('Ident',false);
		}
		else {
			tratarCamposMatriz('Ident',true);
		}
	});

	$('#cliMatrizProcedenciaEnd').click(function(){
		var flag = $(this).attr('checked');
		if (flag) {
			tratarCamposMatriz('End',false);
		}
		else {
			tratarCamposMatriz('End',true);
		}
	});
	
	$('#cliMatrizProcedenciaRenda').click(function(){
		var flag = $(this).attr('checked');
		if (flag) {
			tratarCamposMatriz('Renda',false);
		}
		else {
			tratarCamposMatriz('Renda',true);
		}
	});
	
	$('input[id *= docIdentData]').parent().parent().children().children('input[type=checkbox]').each(function(){
		if($(this).is(':checked')){
			checaIdent++;
		}
		
	});
	$('[id *= docEndData]').parent().parent().children().children('input[type=checkbox]').each(function(){
		if($(this).is(':checked')){
			checaEnd++;
		}
		
	});
	$('[id *= docRendaData]').parent().parent().children().children('input[type=checkbox]').each(function(){
		if($(this).is(':checked')){
			checaRenda++;
		}
		
	});
	
	$('[id *= docEmancipadoData]').parent().parent().children().children('input[type=checkbox]').each(function(){
		if($(this).is(':checked')){
			checaEmancipado++;
		}
		
	});
	if($("#isMesaCredito").val() == 'true') {
		habilitarCamposMatrizChecagem(true);
		changeCampos(false);
	}else {
		habilitarCamposMatrizChecagem(false);		
	}
	
	$('#cliMatrizPessoaTelefoneEnd').val($('#cliResFone').val());

	$('#cliMatrizPessoaTelefoneRenda').val($('#cliEmpFone').val());
	
});

function tratarCamposMatriz(sufixo,bloqueia){

	var atendente = '#cliMatrizNomePessoaAtendeu'+sufixo;
	var telefone = '#cliMatrizPessoaTelefone'+sufixo;
	var obsevacao = '#cliMatrizPessoaObservacao'+sufixo;
	
	if (bloqueia) {
		//PESSOA QUE ATENDEU
		$(atendente).removeClass('campo');
		$(atendente).removeClass('error');
		$(atendente).parent().children('.error').remove();
		$(atendente).addClass('readonly');
		$(atendente).attr({
			readonly: "readonly"
		});
		//TELEFONE
		$(telefone).removeClass('campo');
		$(telefone).removeClass('error');
		$(telefone).parent().children('.error').remove();
		$(telefone).addClass('readonly');
		$(telefone).attr({
			readonly: "readonly"
		});
		$(telefone).unmask();
		//OBSERVAÇÃO
		$(obsevacao).removeClass('campo');
		$(obsevacao).removeClass('error');
		$(obsevacao).parent().children('.error').remove();
		$(obsevacao).addClass('readonly');
		$(obsevacao).attr({
			readonly: "readonly"
		});
	}
	else {
		//PESSOA QUE ATENDEU
		$(atendente).removeClass('readonly');
		$(atendente).addClass('campo');
		$(atendente).removeAttr("readonly");
		//TELEFONE
		$(telefone).removeClass('readonly');
		$(telefone).addClass('campo');
		$(telefone).removeAttr("readonly");
		$(function($){
			$(telefone).mask("(99) 9999-9999");
		});
		//OBSERVAÇÃO
		$(obsevacao).removeClass('readonly');
		$(obsevacao).addClass('campo');
		$(obsevacao).removeAttr("readonly");
	}
}


