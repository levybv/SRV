
var valido = false;
function neuroValida(funcao,campo,div,mensagem,requerido,mensagemSeRequerido){
		 /** Esta fun��o atua como um Gatway, direcionando para cada fun��o JS
		 *   suas respectivas valida��es. */
		
		var value = $.trim($(campo).val());
		var valid = false;
		var rq = false;
		if (requerido){
			if (value==""){
				printDivMsg('erro',mensagemSeRequerido,true,div,false);
				rq = false;
			}else{
				rq = true;
			}
		}
		if (fnc==null){
			valid = true;
		}else{
			if (eval(funcao+"('"+value+"')")){
				valid = true;
			}else{
				printDivMsg('erro',mensagem,true,div,false);
				valid = false;
			}
		}
		if (valid && rq){
			divBlind(div);
			valido = true;
			return true;
		}else{
			valido = false;
			return false;
		}
	}

function printDivMsg(tipo,msg,icon,div,open){
		 /** Esta function � respins�vel por tratar as mens�gens que ser�o exibidas dentro das DIVS!*/
		var divString = '';
		var divID = "#"+div;
		if (tipo=='erro'){
			if (icon){
				divString = '<span class="erro"><img src="images/ico_error.gif" class="imagem" />'+msg+'</span>';
			}else{
				divString = '<span class="erro">'+msg+'</span>';
			}
		}else if(tipo=='alert'){
			if (icon){
				divString = '<img src="images/icon-alert.gif" /><span class="requerido">'+msg+'</span>';
			}else{
				divString = '<span class="alert-text">'+msg+'</span>';
			}		
		}else if(tipo=='msg'){
			if (icon){
				divString = '<img src="images/icon-msg.gif" /><span class="requerido">'+msg+'</span>';
			}else{
				divString = '<span class="msg-text">'+msg+'</span>';
			}
		}else{
			divString = '<span>'+msg+'</span>';
		}

		$(divID).html(divString);
		$(divID).slideDown(10);
	}

// ESCONDE DIV DO ERRO E LIMPA O CONTE�DO!
function divBlind(div){
		var divID = "#"+div;
		$(divID).slideUp(10);
		$(divID).html('');
}

function neuroAutoTab(campo,jQueryId,tipoMoeda){
		var valor = $(campo).val();
		var maxlength = $(campo).attr('maxlength');
		var novoValor = valor;
		
		
		while (novoValor.indexOf("_") != -1) { novoValor = novoValor.replace("_", "");return false}
		if(tipoMoeda){
			while (novoValor.indexOf("R") != -1) { novoValor = novoValor.replace("R", "");return false}
			while (novoValor.indexOf("$") != -1) { novoValor = novoValor.replace("$", "");return false}
		}
		if(valor.length == maxlength){
			$(jQueryId).focus();
		}else{
			return false;
		}
	}
