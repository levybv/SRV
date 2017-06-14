

// BLOCO DE VARI�VEIS GLOBAIS
	var isNN = (navigator.appName.indexOf("Netscape") != -1);
	var abaAtual = 1;
	var SETAS_ESQUERDA = String.fromCharCode(171);
	var SETAS_DIREITA = String.fromCharCode(187);
	var vIE = "DEFAULT";
	var isSAC = false; 
	var shiftTab = false;
	var vSegundos = 0;
	var vMinutos = 0;
	var idContentModal ='';
	var chamada1 = true;
	var fieldsall;
	var paginaRedirect = '';
	var minutosRedirect = '';
	var segundosRedirect = '';
	var operacaoRedirect = '';
	var contador1;
	//array para os modais Posi��es:{0 - Modal, 1 - MiniModal, 2 - ModalSimNao, 3 - Mensagem}
	var controleModais = new Array(false, false, false, false);
// FIM BVG
// -------|
	
function liberarBotao(){
	$("#botao-avancar").removeAttr("disabled"); 
}

function maxLength(id,maxLength){
	id = '#' + id;
	
	$(id).keydown(function(event){
		event.keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if($(this).text().length > maxLength && event.keyCode != 8 && event.keyCode != 9 && event.keyCode != 27
			&& event.keyCode != 37 && event.keyCode != 38 && event.keyCode != 39 && event.keyCode != 40){
			return false;
		}
		
	});
	
	$(id).keyup(function(event){
		event.keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if(event.ctrlKey && event.keyCode == 86){
			if($(this).text().length > maxLength){
				$(this).text($(this).text().substring(0,maxLength));
			}
		}
		$(this).text($(this).text().toUpperCase());
	});
}
	
function habilitarAbaConjuge(){
	var idClasse = $('#cargoNome').val();
	var idCargo = $('#cliEmpOcupacao').val();
	var idEstadoCivil = $('#cliEcDomi').val();
	if(idClasse != '6') {
		$("#tabConjuge").hide();
		$('#conteudo > div:eq(4)').find('input').attr('disabled','disabled');
	} else if((idCargo == '6#3')&& idEstadoCivil=='4#2'){
		$("#tabConjuge").show();
		$('#conteudo > div:eq(4)').find('input').removeAttr('disabled');
	}else{
		$("#tabConjuge").hide();
		$('#conteudo > div:eq(4)').find('input').attr('disabled','disabled');
	}
}

function desabilitarComponentesAba(indexAba, isDisable){
	$('#conteudo > div:eq('+indexAba+')').find('input').attr('readonly', isDisable);
	$('#conteudo > div:eq('+indexAba+')').find('select, input:button, input:checkbox, input:radio, textarea').attr('disabled', isDisable);
	
	if(isDisable){
		$('#conteudo > div:eq('+indexAba+')').find('input:not(:button)').addClass("readonly");
		$('#conteudo > div:eq('+indexAba+')').find('input:not(:button)').removeClass("campo");
	}else{
		$('#conteudo > div:eq('+indexAba+')').find('select').removeClass("readonly");
		$('#conteudo > div:eq('+indexAba+')').find('select').addClass("campo");
	}
}
	
function removerMensagemErro(element){
	$('label[for="' +$(element).attr('id')+'"][class="error"]').remove();
}

function focusCampoModal(event){
		event.keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		var key_shift = event.shiftKey; 
		var key_tab = 9;
		var key_enter = 13;

		if (event.keyCode == 8 && (event.target.type == 'button' 
			|| event.target.type == 'select-one' 
			|| event.target.type == 'select-multiple' 
			|| event.target.type == 'radio' 
			|| event.target.type == 'checkbox')){
				return false;
			}
		
		if(idContentModal != '' && chamada1){
			fields = $('#'+idContentModal).find('select,input,button,textarea,a');
			chamada1 = false;
			desabilitarALT(fields);
		}
		
		currentFieldNumber = fields.index(this);

		$(this).focusout(function(){
			if(fields == null || fields.length == 0 || fields == undefined ){
				if(fieldsall == null || fieldsall.length == 0 || fieldsall == undefined)
				fields = $('#'+idContentModal).find('select,input,button,textarea,a');
				desabilitarALT(fields);
			}
			currentFieldNumber = fields.index(this);			
		});

		if (key_shift && key_tab == event.keyCode){
			if($(fields[currentFieldNumber]).attr('id') ==  $(fields).first().attr('id'))
				return;
			else
				buscarElemento(fields,currentFieldNumber -1, false);
		}else if (key_tab == event.keyCode || key_enter == event.keyCode){
				if(($(fields[currentFieldNumber]).attr('type') == 'button') && key_enter == event.keyCode)
					$(fields[currentFieldNumber]).click();
				else
					buscarElemento(fields,currentFieldNumber + 1, true);
		}else if($('#'+idContentModal).find($(fields[currentFieldNumber]).attr('id'))){
				return;
		}
	}
	
	function buscarElemento(element,index, next){
		if(next){
			if($(element[index-1]).attr('id') == $(fields).last().attr('id') ){
				for(i = 0; i < element.length; i++){
					if($(element[i]).attr("readonly") != true && $(element[i]).attr("disabled") != true && $(element[i]).attr('type') != 'hidden' && $(element[i]).is(':visible') != false){
						$(element[i]).focus();
						return;
					}
				}
			}else{
				for(i = index; i < element.length; i++){
					if($(element[i]).attr("readonly") != true && $(element[i]).attr("disabled") != true && $(element[i]).attr('type') != 'hidden' && $(element[i]).is(':visible') != false){
						$(element[i]).focus();
						return;
					}
				}
			}
		}else{
			for(i = index; i >= 0; i--){
				if($(element[i]).attr("readonly") != true && $(element[i]).attr("disabled") != true && $(element[i]).attr('type') != 'hidden'  && $(element[i]).is(':visible') != false){
					$(element[i]).focus();
					return;
				}
			}
		}
	}
	
// M�SCARA PARA CPF, CNPJ, CEP, DATA ... (MENOS TELEFONE)
function formatar(src, mask){
  var i = src.value.length;
  var saida = mask.substring(0,1);
  var texto = mask.substring(i);
  if (texto.substring(0,1) != saida){
    src.value += texto.substring(0,1);
  }
}

//	M�SCARA PARA TELEFONE (99)9999-9999
function formatarFone(objeto){ 
	if(objeto.value.length == 0)
		objeto.value = '(' + objeto.value;

	if(objeto.value.length == 3)
		objeto.value = objeto.value + ')';

	if(objeto.value.length == 8)
		objeto.value = objeto.value + '-';
}

// TABULA��O AUTOM�TICA
function autoTab(input, length, e,proximaAba) {
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		var filter = (isNN) ? [0, 8, 9] : [0, 8, 9, 16, 17, 18, 37, 38, 39, 40, 46];
		var inpValue = input.value;
		
		while (inpValue.indexOf("_") != -1) { inpValue = inpValue.replace("_", "");}
		
		if (inpValue.length >= length && !containsElement(filter, keyCode)) {
			currentFieldNumber = fields.index(input);
			setFocusNoProximoCampoHabilitado();
		}
		
}
function containsElement(arr, ele) {
    var found = false,
        index = 0;
    while (!found && index < arr.length)
    if (arr[index] == ele) found = true;
    else index++;
    return found;
}
function getIndex(input) {
    var index = -1,
        i = 0,
        found = false;
    while (i < input.form.length && index == -1)
    if (input.form[i] == input) index = i;
    else i++;
    return index;
}
// FIM TABULA��O AUTOM�TICA

// CONTADOR REGRESSIVO
function contador(minutos, segundos, pagina) {
	var segundoTotal = segundos + minutos*60; 
	paginaRedirect = pagina;
	minutosRedirect = minutos;
	segundosRedirect = segundos;
	contador1 = setTimeout('redirecionar(\''+paginaRedirect+'\')', segundoTotal*1000);
	atualizar(minutos, segundos); 
}

// Impede submiss�o do formul�rio ao precionar a tecla [ENTER], jogando o focus para o proximo �ten
function checkForEnter (event) {
	event.keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if(fields == null || fields.length == 0 || fields == undefined ){
		if(fieldsall == null || fieldsall.length == 0 || fieldsall == undefined)
			return;
		fields = fieldsall;
		desabilitarALT(fields);
	}
	
	if (event.keyCode == 8 && (event.target.type == 'button' 
						|| event.target.type == 'select-one' 
						|| event.target.type == 'select-multiple' 
						|| event.target.type == 'radio' 
						|| event.target.type == 'checkbox')){
		return false;
	}
		
	if (event.keyCode == 9 && event.shiftKey || shiftTab){
		shiftTab = false;
		if(typeof event == 'number'){
    		currentFieldNumber = event;
    	}else{
    		currentFieldNumber = fields.index(this);
    	}
	    	
		
		var tipoInput = fields[currentFieldNumber].type;
		var campoAtual = fields[currentFieldNumber];
		
		 setFocusCampoAnteriorHabilitado();
		 return false;

	}else if (event.keyCode == 9 || event.keyCode == 13 || typeof event == 'number' ) { 
		if(typeof event == 'number'){
	    	currentFieldNumber = event;
    	}else{
      		currentFieldNumber = fields.index(this);
    	}
		
		if(currentFieldNumber == -1)
			currentFieldNumber = 0;
		
		var tipoInput = fields[currentFieldNumber].type;
		var campoAtual = fields[currentFieldNumber];
		
		
		if (tipoInput == 'button' && event.keyCode == 13) {
			campoAtual.click();
			return false;
		}
		/*
		else if (tipoInput == 'textarea' && event.keyCode == 13) {
			return false;
		}
		*/
		else{
			setFocusNoProximoCampoHabilitado();
			return false;
		}
	}		
}

function addZeroCPF(element){
	var valor = element.value;
	var addZero = false;
	valor = retirarMascara(valor);	
	
	while(valor.length < 11 && valor.length > 6){
		addZero = true;
		valor = '0' + valor;
	}
	
	if(addZero)
		element.value = valor;
	
	return;
}

function mascararCpf(valor){
	
	while(valor.length < 11 && valor.length > 6){
		valor = '0' + valor;
	}
	
	valor = valor.substring(0, 3) + '.' + valor.substring(3, 6) + '.' + valor.substring(6, 9) + '-' + valor.substring(9, 11);
	
	return valor;
}

function addZeroBeneficio(element){
	var valor = element.value;
	var addZero = false;
	valor = retirarMascara(valor);	
	
	while(valor.length < 10 && valor.length > 6){
		addZero = true;
		valor = '0' + valor;
	}
	
	if(addZero)
		element.value = valor;
	
	return;
}

function geralElementos(){
	if(typeof fieldsall != "undefined"){
		fields = fieldsall;
		desabilitarALT(fields);
	}
}

function setFocusCampoAnteriorHabilitado(){
	if (fields[currentFieldNumber - 1] != null) {
		currentField = fields[currentFieldNumber];
		nextField = fields[currentFieldNumber - 1];
		
		if(verificarCampoCPF(currentField.id))
			addZeroCPF(currentField);
		else if(currentField.id == 'cliCodBeneficio')
			addZeroBeneficio(currentField);
		
		removerVariosEspacos(currentField);
		
		if(!focusPrimeiroCampoAba(currentField,true)){
			if($(nextField).is('input')){
				if($(nextField).attr("disabled") == true || $(nextField).attr("readonly") == true ||  $(nextField).attr('type') == 'hidden' || $(nextField).is(':visible') == false){
					shiftTab = true;
					checkForEnter(currentFieldNumber - 1); 
				}else{
					nextField.focus();
				}
			}else if($(nextField).is('select')){
				if($(nextField).attr("disabled") == true || $(nextField).is(':visible') == false){
					shiftTab = true;
					checkForEnter(currentFieldNumber - 1); 
				}else{
					nextField.focus();		}
			}
		}
	}
}

function desabilitarALT(pFields){
//	var digitsOnly = /[1234567890]/g;
//	var floatOnly = /[0-9\.]/g;
//	var alphaOnly = /[A-Za-z]/g;
//	var ExprReg = /(^([0-9A-Za-z\-\.#@(),:;_?\/\s]){1,50})+$/;
//
//	$(pFields).keypress(function(e){
//		
//		if (e.keyCode) code = e.keyCode;
//		else if (e.which) code = e.which;
//		var character = String.fromCharCode(code);
//	
//	
//		// if they pressed esc... remove focus from field...
//		if (code==27) { this.blur(); return false; }
//		// ignore if they are press other keys
//		// strange because code: 39 is the down key AND ' key...
//		// and DEL also equals .
//		if (!e.ctrlKey && code!=9 && code!=8 && code!=36 && code!=37 && code!=38 && (code!=39 || (code==39 && character=="'")) && code!=40) {
//			if (character.match(ExprReg)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//	});
	return true;
	
}	

function setFocusNoProximoCampoHabilitado(index){
	if (fields[currentFieldNumber + 1] != null) {
		currentField = fields[currentFieldNumber];
		nextField = fields[currentFieldNumber + 1];
		
		if(verificarCampoCPF(currentField.id))
			addZeroCPF(currentField);
		else if(currentField.id == 'cliCodBeneficio')
			addZeroBeneficio(currentField);
		
		removerVariosEspacos(currentField);
		
		if(!focusPrimeiroCampoAba(currentField,false)){
			
			if($(nextField).is('input,textarea')){
				if( $(nextField).attr("disabled") == true || $(nextField).attr("readonly") == true ||  $(nextField).attr('type') == 'hidden'  || $(nextField).is(':visible') == false){
					checkForEnter(currentFieldNumber + 1); 
				}else{
					nextField.focus();
				}
				
			} else if($(nextField).is('select')){
				if($(nextField).attr("disabled") == true || $(nextField).is(':visible') == false){
					checkForEnter(currentFieldNumber + 1); 
				}else{
					nextField.focus();		
				}
			}
		}
	}
}

function focusPrimeiroCampoAba(currentField,anterior){
	if(anterior){
		if($("#conteudo > div:eq(" + (abaAtual - 1)+ ")").find('select,input,textarea').first().attr('id') == $(currentField).attr('id')){
			voltarAba();
			currentFieldNumber = fields.index(currentField);
			setFocusCampoAnteriorHabilitado();
			return true;
		}else{
			return false;
		}
	}
	if($("#conteudo > div:eq(" + (abaAtual - 1)+ ")").find('select,input').last().attr('id') == $(currentField).attr('id')){
		
		var ultimaAba = avancarAba();
		
		if(ultimaAba){
			$('#contentButtons  input[type="button"]').last().focus();
//			if($('#submeterForm').attr('id') != undefined)
//				$('#submeterForm').focus();
//			else if($('#botaoFluxo').attr('id') != undefined)
//				$('#botaoFluxo').focus();
		}else{
			currentFieldNumber = fields.index(currentField);
			setFocusNoProximoCampoHabilitado();
		}
		return true;
	}else{
		return false;
	}
}


/*
Contador regressivo
*/
function contador(minutos, segundos, pagina, operacao) {
		paginaRedirect = pagina;
		minutosRedirect = minutos;
		segundosRedirect = segundos;
		operacaoRedirect = operacao;
    	var segundoTotal = segundos + minutos*60; 
        contador1 = setTimeout('redirecionar()', segundoTotal*1000);  
        atualizar(minutos, segundos);    
}

function atualizar(minutos, segundos) {
	if(segundos > 0) { 
		if(segundos >= 10) { 
			$("#tempo").html(minutos + ':' + segundos);
		} else {
			$("#tempo").html(minutos + ':0' + segundos);
		}
		segundos = segundos-1;
	} else if(segundos == 0 && minutos == 0) {
		segundos = 0;
		minutos = 0; 
	} else {    
		segundos = 59; 
		minutos = minutos-1;   
	} 
	
	vSegundos = segundos;
	vMinutos = minutos;
	contador2 = setTimeout('atualizar('+ minutos +','+ segundos +')', 1000);       
}
// END 
// REDIRECIONA P�GINA
function redirecionar() {    	
	modal(350,120,'paginas/modal/modal-tempo-esgotado.jsp','Marisa - Tempo de Processamento Esgotado',true,'',false);
} 
 
function enviaAjax(path) { 
	 $.ajax({ 
		  	url: path,   
		  	type:'POST', 
		  	cache: false,							
		  	error: function(){    
				mensagem("Erro na atualiza��o.", true);
	 		}
  
	  });  
}
 
function identificacao(campo) { 
    var valorMatrizDocIdentificao = document.getElementById('cliMatrizDocIdentificao');
    if(valorMatrizDocIdentificao.value == '') {
        valorMatrizDocIdentificao.value = '#' + campo.value + '#';
    } else {
        if(valorMatrizDocIdentificao.value.lastIndexOf(campo.value + '#') == -1) {
            valorMatrizDocIdentificao.value += campo.value + '#';
        } else {
            valorMatrizDocIdentificao.value = valorMatrizDocIdentificao.value.replace(campo.value + '#', '');

            if(valorMatrizDocIdentificao.value == '#') {
                valorMatrizDocIdentificao.value = '';
            }
        }

    }
    
}

function retirarCaracteresEspeciasELetras(str){

	var valorF = str;
    var valor = "";
    var numbers = "0123456789";  

    for(i=0;i<valorF.length;i++){    
        if(numbers.indexOf(valorF.charAt(i)) >= 0){
            valor += valorF.charAt(i);
        }        
    }
    
    return valor;
}

function mascara(o,f){
    v_obj=o;
    v_fun=f;
    setTimeout("execMascara()",1);
} 

function execMascara(){
    v_obj.value=v_fun(v_obj.value);
}

function moeda(v){
	v = v.replace(/^(R\$ 0,0)/,"");				                        //Remove m�scara 'R$ 0,0#'
	v = v.replace(/^(R\$ 0,)/,"");				                        //Remove m�scara 'R$ 0,##'
	v = v.replace(/\D/g,"");                   	                        //Remove tudo o que n�o � d�gito

    v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4.$5,$6"); //Coloca a m�scara '###.###.###.###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{3})(\d{2})/,"R\$ $1.$2.$3.$4,$5"); 	//Coloca a m�scara '##.###.###.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{3})(\d{2})/,"R\$ $1.$2.$3.$4,$5"); 	//Coloca a m�scara '#.###.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})/,"R\$ $1.$2.$3,$4"); 			//Coloca a m�scara '###.###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{2})/,"R\$ $1.$2.$3,$4");				//Coloca a m�scara '##.###.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{2})/,"R\$ $1.$2.$3,$4");				//Coloca a m�scara '#.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{2})/,"R\$ $1.$2,$3");					    //Coloca a m�scara '###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{2})/,"R\$ $1.$2,$3");					    //Coloca a m�scara '##.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{2})/,"R\$ $1.$2,$3");					    //Coloca a m�scara '#.###,##
    v=v.replace(/^(\d{3})(\d{2})/,"R\$ $1,$2");									//Coloca a m�scara '###,##
    v=v.replace(/^(\d{2})(\d{2})/,"R\$ $1,$2");									//Coloca a m�scara '##,##
    v=v.replace(/^(\d{1})(\d{2})/,"R\$ $1,$2");									//Coloca a m�scara '#,##
    v=v.replace(/^(\d{2})/,"R\$ 0,$1");											//Coloca a m�scara '0,##
    v=v.replace(/^(\d{1})/,"R\$ 0,0$1");										//Coloca a m�scara '0,0#
 
	return v;
}

function moedaSemRS(v){

	v = v.replace(/^(0,0)/,"");				                        	//Remove m�scara '0,0#'
	v = v.replace(/^(0,)/,"");				                        	//Remove m�scara '0,##'
	v = v.replace(/\D/g,"");                   	                        //Remove tudo o que n�o � d�gito

	v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4.$5,$6"); 	//Coloca a m�scara '###.###.###.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4,$5"); 	//Coloca a m�scara '###.###.###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4,$5"); 	//Coloca a m�scara '##.###.###.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4,$5"); 	//Coloca a m�scara '#.###.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3,$4"); 			//Coloca a m�scara '###.###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{2})/," $1.$2.$3,$4");			//Coloca a m�scara '##.###.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{2})/," $1.$2.$3,$4");			//Coloca a m�scara '#.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{2})/," $1.$2,$3");					    //Coloca a m�scara '###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{2})/," $1.$2,$3");					    //Coloca a m�scara '##.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{2})/," $1.$2,$3");					    //Coloca a m�scara '#.###,##
    v=v.replace(/^(\d{3})(\d{2})/," $1,$2");								//Coloca a m�scara '###,##
    v=v.replace(/^(\d{2})(\d{2})/," $1,$2");								//Coloca a m�scara '##,##
    v=v.replace(/^(\d{1})(\d{2})/," $1,$2");								//Coloca a m�scara '#,##
    v=v.replace(/^(\d{2})/," 0,$1");										//Coloca a m�scara '0,##
    v=v.replace(/^(\d{1})/," 0,0$1");										//Coloca a m�scara '0,0#
	v=v.substring(1);
	return v;
}



function moedaSemRSNegativo(v){

	var inicio = v;

	var negativo = false;
	if (v.substring(0,1) == '-') {
		v = v.substring(1);
		negativo = true;
	}
	
	//alert("processo 1 => " + inicio + " => " + v);
	
	v = v.replace(/^(0,0)/,"");				                        	//Remove m�scara '0,0#'
	v = v.replace(/^(0,)/,"");				                        	//Remove m�scara '0,##'
	v = v.replace(/\D/g,"");                   	                        //Remove tudo o que n�o � d�gito

	inicio = v;


    v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4.$5,$6"); 	//Coloca a m�scara '###.###.###.###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4,$5"); 	//Coloca a m�scara '##.###.###.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3.$4,$5"); 	//Coloca a m�scara '#.###.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})/," $1.$2.$3,$4"); 			//Coloca a m�scara '###.###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{2})/," $1.$2.$3,$4");			//Coloca a m�scara '##.###.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{2})/," $1.$2.$3,$4");			//Coloca a m�scara '#.###.###,##
    v=v.replace(/^(\d{3})(\d{3})(\d{2})/," $1.$2,$3");					    //Coloca a m�scara '###.###,##
    v=v.replace(/^(\d{2})(\d{3})(\d{2})/," $1.$2,$3");					    //Coloca a m�scara '##.###,##
    v=v.replace(/^(\d{1})(\d{3})(\d{2})/," $1.$2,$3");					    //Coloca a m�scara '#.###,##
    v=v.replace(/^(\d{3})(\d{2})/," $1,$2");								//Coloca a m�scara '###,##
    v=v.replace(/^(\d{2})(\d{2})/," $1,$2");								//Coloca a m�scara '##,##
    v=v.replace(/^(\d{1})(\d{2})/," $1,$2");								//Coloca a m�scara '#,##
    v=v.replace(/^(\d{2})/," 0,$1");										//Coloca a m�scara '0,##
    v=v.replace(/^(\d{1})/," 0,0$1");										//Coloca a m�scara '0,0#


	v=v.substring(1);
	
	
	//alert("processo 2 => " + inicio + " => " + v);
	
	if (negativo) {
		v = "-" + v;
	}
	
	return v;
}


function moedaSemRS15Int4Dec(v){

	var inicio = v;

	var negativo = false;
	if (v.substring(0,1) == '-') {
		v = v.substring(1);
		negativo = true;
	}
	
	v = v.replace(/^(0,0)/,"");				                        	//Remove m�scara '0,0#'
	v = v.replace(/^(0,)/,"");				                        	//Remove m�scara '0,##'
	v = v.replace(/\D/g,"");                   	                        //Remove tudo o que n�o � d�gito

	inicio = v;

	v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3.$4.$5,$6");
	v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3.$4.$5,$6");
	v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3.$4.$5,$6");
	v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3.$4,$5");
	v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3.$4,$5");
	v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3.$4,$5");
	v=v.replace(/^(\d{3})(\d{3})(\d{3})(\d{4})/," $1.$2.$3,$4");
	v=v.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})/," $1.$2.$3,$4");
	v=v.replace(/^(\d{1})(\d{3})(\d{3})(\d{4})/," $1.$2.$3,$4");
	v=v.replace(/^(\d{3})(\d{3})(\d{4})/," $1.$2,$3");
	v=v.replace(/^(\d{2})(\d{3})(\d{4})/," $1.$2,$3");
	v=v.replace(/^(\d{1})(\d{3})(\d{4})/," $1.$2,$3");
	v=v.replace(/^(\d{3})(\d{4})/," $1,$2");
	v=v.replace(/^(\d{2})(\d{4})/," $1,$2");
  	v=v.replace(/^(\d{1})(\d{4})/," $1,$2");
    v=v.replace(/^(\d{4})/," 0,$1");
    v=v.replace(/^(\d{3})/," 0,0$1");
    v=v.replace(/^(\d{2})/," 0,00$1");
    v=v.replace(/^(\d{1})/," 0,000$1");

    v=v.substring(1);
	
	if (negativo) {
		v = "-" + v;
	}
	
	return v;
}


function removerVariosEspacos(element){
	element.value = $.trim(element.value.replace(/\s+/g," ")); 
}

function verificarCampoCPF(value){
	var exp = /cpf|CPF|Cpf/;
	
	if(exp.test(value)){
		if($('#' + value).attr("maxlength") == 14){
			return true;
		}
	}
	return false;
}

function opentab(num) {
	abaAtual = num;
    $("#conteudo > div").hide();
    $("#conteudo > div:eq(" + (num - 1) + ")").fadeIn(0);
    $("#tabs > a").css("background-color", "#B3C9D0");
    validarAbasComErro();
    $("#tabs > a:eq(" + (num - 1) + ")").css("background-color", "87CEEB");
    $('#barBottom').css('width','100%');
    
    if(num == 1){
    	$('#botaoVoltarAba,#btn01').hide();
    }else{
    	$('#botaoVoltarAba,#btn01').show();
    }
        
    if(abaAtual == $("#conteudo > div").length) {
		if($("#tabs > a:eq("+ ($("#conteudo > div").length-1) +")").is(':visible')){
			$('#botaoAvancarAba,#btn02').hide();
		}else{
			$('#botaoAvancarAba,#btn02').show();
		}
	}else{
		$('#botaoAvancarAba,#btn02').show();
	}
}

function avancarAba() {
	if(abaAtual == $("#conteudo > div").length) {
		
		if($("#tabs > a:eq("+ ($("#conteudo > div").length-1) +")").is(':visible')){
			opentab($("#conteudo > div").length);
			return true;
		}
	} else {
		
		abaAtual++;
		
		if($("#tabs > a:eq("+ (abaAtual-1) +")").is(':visible')){
			opentab(abaAtual);
			return false;
		}else{
			for(var i = abaAtual; i <=  $("#conteudo > div").length; i++){
				if($("#tabs > a:eq("+ (i-1) +")").is(':visible')){
					opentab(i);
					return false;
				}
			}
				
		}
		
	}
}

function voltarAba() {
	if(abaAtual != 1) {
		abaAtual--;
			
		if($("#tabs > a:eq("+ (abaAtual-1) +")").is(':visible')){
			opentab(abaAtual);
			return true;
		}else{
			for(var i = abaAtual; i >= 1; i--){
				if($("#tabs > a:eq("+ (i-1) +")").is(':visible')){
					opentab(i);
					return true;
				}
			}
				
		}
	}
}

$(document).ready(function(){

	/*
	$(document).keydown(function(e){
		e.keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
		var key_f5 = 116; 
		var key_ctrl = e.ctrlKey; 
		var key_r = 82; 
		if (key_f5 == e.keyCode || (key_ctrl && key_r == e.keyCode) ){
			mensagem("Teclas desabilitadas(F5, Ctrl+F5 e Ctrl+R).",true);
			if(event)
				event.keyCode = 0;
			else
				e.keyCode = 0;
			return false;
		}
	});
	*/	
	
	$('.itemenuSub').click().toggle(
		function(){
			$('.itemenuSub ul li').css('visibility', 'visible');
		},
		function(){
			$('.itemenuSub ul li').css('visibility', 'hidden');
	});
	
	$('.subItemenu').click(function(){
		var URL = $("a", this).attr('href');
		$(location).attr('href',URL);
	});
	
	$('.table tbody tr').mouseover(function(){
		$(this).addClass("trBgHover");
	});
	
	$('.table tbody tr').mouseout(function(){
		$(this).removeClass("trBgHover");
	});
	
	$('.dataGrid tbody tr').live("mousout", function(){
		$(this).addClass("trBgHover");
	});
	
	$('.dataGrid tbody tr').live("mousout", function(){
		$(this).removeClass("trBgHover");
	});
	
		
	// FOCO DOS CAMPOS
	$('input:[type=text],input:[type=password], select').focus(function () {
		jQuery(this).removeClass("campo2");
		jQuery(this).addClass("focusGained2");   
	});
	$('input:[type=text],input:[type=password], select').blur(function () {
		jQuery(this).removeClass("focusGained2");
		jQuery(this).addClass("campo2");
	});
	
	fields = $('input, password, select');
	fieldsall = fields;
	if ($.browser.mozilla) {
		$(fields).keypress(checkForEnter);
	} else {
		$(fields).keydown(checkForEnter);
	}	
	
	//AUTO-TAB
	$('input').keyup(
		function(event){
			var size = $(this).attr('maxlength');
			autoTab(this,size,event,false);
		}
	);
});

function validarCaracteresEspeciais(value) {
	//var ExprReg = /(^([0-9A-Za-z�-��-��-��-��-��-��-��-�\-\.\s]){1,50})+$/;
	var ExprReg = /(^([0-9A-Za-z\-\.\s]){1,50})+$/;
	
	if(value == '')
		return true;
	
	if(ExprReg.test(value))
		return true;
	else
		return false;
}




function validarFiltro(idDataIni, idDataFin, idContent){
	var dataIniVal = $('#'+idDataIni).val();
	var dataFinVal = $('#'+idDataFin).val();
	var dataIniSplit = dataIniVal.split('/');
	var dataFinSplit = dataFinVal.split('/');
		
	
	dataIniVal = retirarMascara(dataIniVal);
	
	dataFinVal = retirarMascara(dataFinVal);

	if (dataIniVal == "" && dataFinVal == ""){
		return true;
	}else{
		if (!validarData(dataIniSplit) && dataIniVal != ''){
			
			$('#'+ idContent).html('<span class="erro" style="font-size:1.5em;"><img src="images/ico_error.gif" class="imagem" /> Data inicial inv�lida.</span>');
			$('#'+ idContent).show();
			return false;
		}
				
		if (!validarData(dataFinSplit) && dataFinVal != ''){
			$('#'+ idContent).html('<span class="erro" style="font-size:1.5em;"><img src="images/ico_error.gif" class="imagem" /> Data final inv�lida.</span>');
			$('#'+ idContent).show();
			return false;
		}

		if(!validarIntervaloDatas(dataIniSplit, dataFinSplit)){
			$('#'+ idContent).html('<span class="erro" style="font-size:1.5em;"><img src="images/ico_error.gif" class="imagem" /> Data inicial maior que data final.</span>');
			$('#'+ idContent).show();
			return false;
		}
	}
	$('#'+ idContent).html('');
	$('#'+ idContent).hide();
	return true;
}
	function validarIntervaloDatas(dataIniSplit, dataFinSplit){
		var dataIni = new Date();
		var dataFin = new Date();

		
		dataIni.setDate(dataIniSplit[0]);
		dataIni.setMonth(dataIniSplit[1]-1);
		dataIni.setFullYear(dataIniSplit[2]);

		dataFin.setDate(dataFinSplit[0]);
		dataFin.setMonth(dataFinSplit[1]-1);
		dataFin.setFullYear(dataFinSplit[2]);

		if(dataIni > dataFin)
			return false;

		return true;		
	}
			
 function validarData(dataSplit){
		var dia = dataSplit[0];
		var mes = dataSplit[1];
		var ano = dataSplit[2];
		var data = dia + mes + ano;
		
		if(data.length!=8||isNaN(dia)||isNaN(mes)||isNaN(ano)||dia>31||mes>12)return false;
		if((mes==4||mes==6||mes==9||mes==11) && dia==31)return false;
		if(mes==2  &&  (dia>29||(dia==29 && ano%4!=0)))return false;
		if(ano < 1900)return false;
		if(ano > 2100)return false;
		return true;
}
 
 function carregarComboTempo(id){
	 id = '#' + id;
	 var idAno =  id + 'Ano';
	 var idMes =  id + 'Mes';
	 var bigStringAno = "<option value=''>   </option>";
	 var bigStringMes = "<option value=''>   </option>";
	 
	 for(var i = 1; i < 100; i++ ){
		 var valor = '';
		 if(i < 10)
			 valor = '0'+ i;
		 else
			 valor = i;
		 
		 bigStringAno += "<option value='"+valor+"'>"+valor+"</option>";
		 if(i <= 11)
			 bigStringMes += "<option value='"+valor+"'>"+valor+"</option>";
	 }
	 $(idAno).html(bigStringAno);
	 $(idMes).html(bigStringMes);
 }
 
 function carregarDataTempo(id){
	 var valorAno = '';
	 var valorMes = '';
	 var valorData = '';
	 
	if(id == 'cliResDtDesdeMes'){
		 valorAno = $('#cliResDtDesdeAno').val();
		 valorMes = $('#cliResDtDesdeMes').val();
		
	}else if(id == 'cliEmpDtAdmMes'){
		 valorAno = $('#cliEmpDtAdmAno').val();
		 valorMes = $('#cliEmpDtAdmMes').val();
	}else if(id == 'cliTempoContaDtDesdeMes'){
		 valorAno = $('#cliTempoContaDtDesdeAno').val();
		 valorMes = $('#cliTempoContaDtDesdeMes').val();
	}
	 
	if($.trim(valorAno) == '')
		valorData = '00/';
	else
		valorData = valorAno + '/';
	
	if($.trim(valorMes) == '')
		valorData += '00';
	else
		valorData += valorMes;
	 
	 $('#' + id.substring(0 ,id.length -3)).val(valorData);
 }
 
 function carregarDataTempoRequest(id){
	 var valorData = $('#' + id).val();
	 var valorDataSplit = $('#' + id).val().split('/');
	 if(valorData != '' && valorDataSplit.length > 0) {
		 if(id == 'cliResDtDesde'){
			$('#cliResDtDesdeAno').val(valorDataSplit[0]);
			$('#cliResDtDesdeMes').val(valorDataSplit[1]);
		}else if(id == 'cliEmpDtAdm'){
			$('#cliEmpDtAdmAno').val(valorDataSplit[0]);
			$('#cliEmpDtAdmMes').val(valorDataSplit[1]);
		}else if(id == 'cliTempoContaDtDesde'){
			$('#cliTempoContaDtDesdeAno').val(valorDataSplit[0]);
			$('#cliTempoContaDtDesdeMes').val(valorDataSplit[1]);
		}
 	}
 }
 
 function retirarMascara(Valorcampo){
		
		var valor = Valorcampo;

		while (valor.indexOf("_") != -1) { valor = valor.replace("_", "");}
		while (valor.indexOf("-") != -1) { valor = valor.replace("-", "");}
		while (valor.indexOf(".") != -1) { valor = valor.replace(".", "");}
		while (valor.indexOf(",") != -1) { valor = valor.replace(",", "");}
		while (valor.indexOf("(") != -1) { valor = valor.replace("(", "");}
		while (valor.indexOf(")") != -1) { valor = valor.replace(")", "");}
		while (valor.indexOf("/") != -1) { valor = valor.replace("/", "");}
		while (valor.indexOf(" ") != -1) { valor = valor.replace(" ", "");}
		while (valor.indexOf("R") != -1) { valor = valor.replace("R", "");}
		while (valor.indexOf("$") != -1) { valor = valor.replace("$", "");}
		
		return valor;
	}

 
 function habilitarCampo(id,isEnable,mascara,limparCampo){
	 id = '#' + id;

	 if($(id).is(':text')){
		 if(isEnable){
			$(id).removeClass("readonly");
			$(id).addClass("campo");
			$(id).removeAttr("readonly");
			if(limparCampo){
				$(id).val('');
				$(id).unmask();
			}
			if(mascara) {
				$(id).unmask();
				$(id).mask(mascara);
			}
		 }else{
			$(id).removeClass("campo");
			$(id).addClass("readonly");
			$(id).attr({readonly:"readonly"});
			if(limparCampo){
				$(id).val('');
				$(id).unmask();
			}
			removerMensagemErro($(id));
	 	}
	 }else{
		 if(isEnable){
				$(id).removeAttr("disabled");
			 }else{
				$(id).attr("disabled","disabled");
				removerMensagemErro($(id));
		 	}
	 }
		 
}
 
function habilitarTodosOsCamposSubmissao(isDisabled){
	 $('input, select, radio, textarea, button').attr('disabled',isDisabled);
}

function modalProcessoRequisicao(){
	modalProcessamento();
	processo();
	window.scroll(0,0);
}

function processo(){			
	$("#processando").html("Processando");
	setTimeout('$("#processando").html("Processando.");'   ,400);
	setTimeout('$("#processando").html("Processando..");'  ,800);
	setTimeout('$("#processando").html("Processando...");' ,1200);
	setTimeout('$("#processando").html("Processando...");' ,1600);
	setTimeout('processo();' ,2000);
		
}
function configCampoCEPGenerico(id,valor){
	valor = $.trim(valor);
   $('#'+id).val(valor);
   if(valor == ''){
   		habilitarCampo(id,true,false,true);
   }else{
	   habilitarCampo(id,false,false,false);
   }      
}

function limparTimeout(){
	clearTimeout(contador1);
	clearTimeout(contador2);
}


function validarCPF(value){
	value = retirarMascara(value);
	
	if(value.length == 0 || value == ''){
		$('#div-erro-cpf').html('');
		return true;
	}
	
	cpf = value;
		
	while(cpf.length < 11) cpf = "0"+ cpf;
	var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
	var a = [];
	var b = new Number;
	var c = 11;
	for (i=0; i<11; i++){
		a[i] = cpf.charAt(i);
		if (i < 9) b += (a[i] * --c);
	}
	if ((x = b % 11) < 2) { a[9] = 0; } else { a[9] = 11-x; }
	b = 0;
	c = 11;
	for (y=0; y<10; y++) b += (a[y] * c--);
	if ((x = b % 11) < 2) { a[10] = 0; } else { a[10] = 11-x; }
	if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg)){
		$('#div-erro-cpf').html(' <span class="erro"><img src="images/ico_error.gif" class="imagem" /> CPF inv�lido.</span>');
		return false;
	}
	$('#div-erro-cpf').html('');
	return true;
}

function validarFormFiltro(){
	 var valorCPF = $("#cliCpf").val();
	if(validarCPF(valorCPF)){
    	if(validarFiltro('periodoInicial', 'periodoFinal', 'ContentMsgErro')){	 
		}else
			return false;
	}else
		return false;
	return true;
}

function chamarTelaCCM(pJson){
	
	$.ajax({
		type : 'POST',
		url  : pJson.url,
		data : {
			sessao       : pJson.sessao,
			webapplip    : pJson.webapplip,
			ipCobol      : pJson.ipCobol,
			ipmenu       : pJson.ipmenu,
			weblinkageweb: pJson.weblinkageweb,
			webfilcod    : pJson.webfilcod
		},
		cache: false, 
		async: false,
		error: function(){    
  			mensagem("Erro ao tentar acessar o menu do CCM.", true);
  		}
	});
}

function consultarImagem(cpf) {
	var urlAcessoDigital = "https://www.acessodigital.com.br/marisa/external/GetDocument.aspx?username=mastermarisa&userpwd=acesso1213&CLASS=PROCARD&SOURCE=LOJASMARISA&TYPE=DOC&IX_CPFCNPJ="+cpf+"&ShowFile=True";
	var largura   = screen.width;
	var altura    = screen.height;
	window.open(urlAcessoDigital,"_blank","toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,menubar=no,width="+(largura-10)+",height="+altura+",top=0,left=0");	
}

function habilitarCamposMatrizChecagem(isEnable){

	habilitarCampo('cliEmpOcupacao',isEnable, false, false);
	habilitarCampo('cargoNome', isEnable, false, false);
	//habilitarCampo('cliCpf', isEnable, false, false);
	habilitarCampo('cliEcDomi', isEnable, false, false);
	habilitarCampo('cliCrediarioDomi', isEnable, false, false);				
	habilitarCampo('cliFilialRetira', isEnable, false, false); 
	habilitarCampo('imgPesquisarFilial', isEnable, false, false);
	habilitarCampo('cliDtNascto', isEnable, false, false);
	habilitarCampo('cliOutrasRendas', isEnable, false, false);
	habilitarCampo('cliOutrosCarDomi', isEnable, false, false);
	habilitarCampo('cliDependentes', isEnable, false, false);
	habilitarCampo('cliEmpSalario', isEnable, false, false);
	habilitarCampo('cliSexoDomi', isEnable, false, false);
	habilitarCampo('cliEmpDtAdmMes', isEnable, false, false); 
	habilitarCampo('cliEmpDtAdmAno', isEnable, false, false);
	habilitarCampo('cliResDtDesdeMes', isEnable, false, false); 
	habilitarCampo('cliResDtDesdeAno', isEnable, false, false);
	habilitarCampo('cliResFoneDomi', isEnable, false, false);
	habilitarCampo('cliResCasaDomi', isEnable, false, false);
	habilitarCampo('cliResCep' , isEnable, false, false);
	habilitarCampo('botaoEndereco', isEnable, false, false); 
	habilitarCampo('cliResUf', isEnable, false, false);
}