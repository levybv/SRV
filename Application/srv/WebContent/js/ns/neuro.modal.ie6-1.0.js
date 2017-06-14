
fnSim = "";
fnNao = ""; 
function modal(w, h, conteudo, titulo, bloquear, msg, rolagem) {

	var idModal = "#modal";
	var idTitle = "#modal-title";
	var idContent = "#modal-content";
	var idBlock = "#modal-block";
	var rol = rolagem;
	
	$(idContent).html('');
	$(idTitle).html('');
	
	$(idTitle).html(titulo);
	
	$(idModal).width(w);
	$(idModal).height(h);
	$(idTitle).width(w - 10);

	if (conteudo != null) {
		$(idContent).load(conteudo);
	} else {
		conteudo = '';
		$(idContent).append(
				'<img src="images/about.png" class="imagem" > &nbsp;' + msg);
		$(idContent)
				.append(
						'<p><div style="text-align:right;"><input type="button" id="modalClose" class="button" onclick="fecharModal();" value="OK" /></div></p>');
		$("#modalClose").focus();
	}

	$(idModal).css('top', ($(window).height() / 2) - (h / 2));
	$(idModal).css('left', ($(window).width() / 2) - (w / 2));
	$(idModal).show();

	if (bloquear) {
		$(idBlock).show();
		$(idBlock).height(($(document).height()-20));
		$(idBlock).width(($(document).width()-20));
	}

	$(idContent).css( {
		'height' : h - 35
	});

	if (rol) {
		$(idContent).css("overflow", "auto");
	}
	
	controleModais[0] = true;
	if(conteudo.indexOf("/configuracao/") == -1 && conteudo.indexOf("modal-processando") == -1){
		if(typeof fields != "undefined"){
			$(fields).each(function(){
				if($(this).is(':button')){
					$(this).focus();
					return;
				}
					
			});
		}
	}	
}

function modalProcessamento() {
	var w = 350;
	var h = 105;
	var idModal = "#modal-processamento";
	var idTitle = "#modal-title-processamento";
	var idContent = "#modal-content-processamento";
	var idBlock = "#modal-block-processamento";
	
	$('select').hide();	
	$("#modal").hide();
	$("#modal-block").hide();
	
	$(idModal).width(w);
	$(idModal).height(h);
	$(idTitle).width(w - 10);

	$(idModal).css('top', ($(window).height() / 2) - (h / 2));
	$(idModal).css('left', ($(window).width() / 2) - (w / 2));
	$(idModal).show();

	$(idBlock).show();
	$(idBlock).height(($(document).height()-20));
	$(idBlock).width(($(document).width()-20));

	$(idContent).css( {
		'height' : h - 35
	});
}


function modalSimNao(w, h, titulo, msg, fSim, fNao, fTimeout) {
	fnSim = fSim;
	fnNao = fNao;
	fecharModal();
	var idModal = "#modal";
	var idTitle = "#modal-title";
	var idContent = "#modal-content";
	var idBlock = "#modal-block";

	$(idTitle).html(titulo);
	$(idModal).width(w);
	$(idModal).height(h);
	$(idTitle).width(w - 10);

	$(idContent).append(
			'<img src="images/about.png" class="imagem" > &nbsp;' + msg);

	var buttonSIM = '<input type="button" id="fecha-sim" class="button" onclick="fecharModal();modalExecutaSim(fnSim);" value="Sim" />';
	var buttonNAO = '<input type="button" id="fecha-nao" class="button" onclick="fecharModal();modalExecutaSim(fnNao);" value="Não" />';
	
	
	if (fTimeout == true) {
		atualizarTempoModal(1, 0);
		$(idContent)
				.append(
						'<p><div style="text-align:left;">Tempo restante: <span id="tempoModal"></span></div><div style="text-align:right;">'
								+ buttonSIM + ' ' + buttonNAO + '</div></p>');
	} else {
		$(idContent).append(
				'<p><div style="text-align:right;">' + buttonSIM + ' '
						+ buttonNAO + '</div></p>');
	}

	$(idModal).css('top', ($(window).height() / 2) - (h / 2));
	$(idModal).css('left', ($(window).width() / 2) - (w / 2));
	$(idModal).show();

	$(idBlock).show();
	$(idBlock).height($(document).height());
	$(idBlock).width($(document).width());

	$(idContent).css( {
		'height' : h - 35
	});
	
	controleModais[2] = true;//MODAL SIM-NÃO
	
	$('#fecha-sim').focus();
}

function atualizarTempoModal(minutos, segundos) {
	if (segundos > 0) {
		if (segundos >= 10) {
			$("#tempoModal").html(minutos + ':' + segundos);
		} else {
			$("#tempoModal").html(minutos + ':0' + segundos);
		}
		segundos = segundos - 1;
	} else if (segundos == 0 && minutos == 0) {
		segundos = 0;
		minutos = 0;
	} else {
		segundos = 59;
		minutos = minutos - 1;
	}
	contador2 = setTimeout('atualizarTempoModal(' + minutos + ',' + segundos	+ ');', 1000);
}

function fecharModal() {
	isOpenChat = false;
	var idModal = "#modal";
	var idTitle = "#modal-title";
	var idContent = "#modal-content";
	var idBlock = "#modal-block";
	$(idModal).hide();
	$(idBlock).hide();
	$(idContent).html('');
	$(idTitle).html('');
	
	controleModais[0] = false;
	geralElementos();
}
/** END * */

function modalSimNao(){  
	var idModal 	= "#modal";
	var idTitle 	= "#modal-title";
	var idContent	= "#modal-content";
	var idBlock		= "#modal-block";
	$(idModal).hide();
	$(idBlock).hide();
	$(idContent).html('');
	$(idTitle).html('');
	
	controleModais[2] = false;
	geralElementos();
		
}

function mensagem(texto, centro) {
	fecharMensagem();
	if ($.trim(texto) == "") {
		mensagem(
				"<font color='#ff0000'>Nenhuma texto informado para a mensagem.</font>",
				true);
		return false;
	} else {
		if (centro) {
			$("#critical-msg").css( {
				'left' : '50%',
				'margin-left' : '-150px',
				'top' : '35%'
			});
		}
		$("#critical-content").html('<img src="images/about.png" class="imagem" > '+texto);
		$("#msg-block").show();
		$("#msg-block").height($(document).height());
		$("#msg-block").width($(document).width());
		$("#critical-msg").show();
	}
	
	controleModais[3] = true;//MENSAGEM
}

function fecharMensagem() {
	if(typeof isNN != "undefined"){
		if(isNN){
			return false;
		}else{
			$("#critical-msg").hide();
			$("#msg-block").hide();
			$("#critical-content").html('');
			controleModais[3] = false;
		}
	}
	geralElementos();
}

function miniModal(sizeW, sizeH, content) {
	$(".miniModal-block").height($(document).height());
	$(".miniModal-block").width($(document).width());
	$('.miniModal-block').show();
	$('.miniModal').css( {
		"width" : sizeW,
		"height" : sizeH,
		"margin-left" : (sizeW / 2) - sizeW
	});
	$('.mmContent').load(content);
	$('.miniModal').show();
	
	controleModais[1] = true;
	

	if(content.indexOf("/configuracao/") == -1){
		if(typeof fields != "undefined"){
			$(fields).each(function(){
				if($(this).is(':button')){
					$(this).focus();
					return;
				}
					
			});
		}
	}
}

function fecharMiniModal() {
	$('.miniModal-block').hide();
	$('.mmContent').html('');
	$('.miniModal').hide();
	
	controleModais[1] = false;
	geralElementos();
}

function modalExecutaSim(fn) {
	eval(fn);
	return true;
}

function modalExecutaNao(fn) {
	eval(fn);
	return false;
}

function fecharTodosOsModais() {
	if (controleModais[3]){
		fecharMensagem();
		return true;
	}else if(controleModais[2]){
		fecharModalSimNao();
		return true;
	}else if(controleModais[1]){
		fecharMiniModal();
		return true;
	}else if(controleModais[0]){
		fecharModal();
		return true;
	}else{
		return true;		
	}
}
