// VARIÁVEL GLOBAL
var formCEP;
var abaAtual = 1;
var tipoFormulario = null;
var idDaRenda;

$(document).ready(function () {
	
    $(document).bind('keydown', 'esc', function () {fecharTodosOsModais();});
    	
	jQuery(function ($) {
	    $("#cliNumFoneRef1").mask("(99) 9999-9999");
	    $("#cliNumFoneRef2").mask("(99) 9999-9999");
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
    
    $("#L2_sub_1").hide();

    fields = $('input, select');
	fieldsall = fields ;
	
	if ($.browser.mozilla) {
        $(fields).keypress(checkForEnter);
    } else {
        $(fields).keydown(checkForEnter);
    }

	$('#botaoQuestionar').click(function () {
    	var MatrizOk = true;
    	docsSelected();
    	
    	if(!validarForm())
    		MatrizOk = false;
    	if($.trim($('#cliObservacaoGeral').val()) == ''){
    		MatrizOk = false;
    		if($('#cliObservacaoGeral').parent().find('.erro').length < 1)
    			$('#cliObservacaoGeral').parent().append(' <span class="erro"><img src="images/ico_error.gif" class="imagem" /> Campo observação geral é obrigatório.</span>');
    	}else{
    		$('#cliObservacaoGeral').parent().find('.erro').remove();
    	}
    	
    	if(MatrizOk)
    		modal(360,250, 'paginas/modal/questionar-derivacao-excecao.jsp', 'Marisa - Questionar', true, '',false);
    	else{
    		opentab(8);
    		$("#barBottomCheck input").show();
			$("#barBottomCheck #load").hide();
    	}
    });  
	
});

