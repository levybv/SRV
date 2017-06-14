$(document).ready(function() {

	$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
		if(event.keyCode == 8)
			return false;
    });		

	$("#arrow-link").click(function(){
	 	if ($(".painel-filtro").is(":visible")) {
        	$(".painel-filtro").hide(100);
          	$("#arrow").attr("src","images/arrow_down_48.png");
          	$("#arrow").attr("class","arrow");   
        } else {  
	        $(".painel-filtro").show(100);
    	    $("#arrow").attr("src","images/arrow_up_48.png");
        }
	});

	$(document).bind('keydown', 'esc', function () {
    	fecharTodosOsModais();
    });


	$(".painel-filtro").show(100);

	$("#form").validate({
    	rules: {
    		periodoF: {required:true}
     	},
     	messages: {
     		periodoF: {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione o m&ecirc;s.</span>'}
        },
        submitHandler : function(form){
    		$("#div-botao").hide();
           	$("#div-load").show();
           	document.form.action = "pecaTicket.do?operacao=executarFiltro";   
            document.form.submit();
        }
    });

	DataBusinessAjax.listaUltimosPeriodosMesAno(-12,
		function (periodoReturn) {
			if (periodoReturn != null) {
				var options = $('#periodoF').attr('options');
				for (var i=0; i<periodoReturn.length; i++) {
					var periodoItem = periodoReturn[i];
					options[options.length] = new Option(periodoItem.periodoDesc, periodoItem.periodo, false, false);
				}
				$('#periodoF').val($('#periodoFiltro').val());
			}
	});

	$("#botaoLimpar").click(function() {
		$("#periodoF").val("");
		$("#idFilialF").val("");
		$("#periodoF").focus();
	});

	$("#botaoImportar").click(function() {
		modal(580,280,'paginas/pecaTicket/importaRealzPecaTicket.jsp','Marisa - Importar Realizado Loja (PPT)',true,'',true);
	});

	$("#periodoF").focus();

});
