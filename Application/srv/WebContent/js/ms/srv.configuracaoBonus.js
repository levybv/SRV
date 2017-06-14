$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);

    	$('#anoF').numeric();

		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
            submitHandler : function(form){
	        	$("#div-botao").hide();  
	        	$("#div-load").show(); 
	           	document.form.action = "configuracaoBonus.do?operacao=inicio";   
	            document.form.submit();
	        }
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
    
		$("#botaoLimpar").click(function() {
			$('#anoF').val("");
			$('#encerradoF').val("");
		});

		$("#incluir").click(function() {
			modal(650,670,'paginas/configuracaoBonus/incluiConfiguracaoBonus.jsp','Marisa - Incluir Configura&ccedil;&atilde;o B&ocirc;nus',true,'',true);
		});

		$("#atualizar").click(function() {
		   	var lista = $('input[type=radio]:checked');
			if (lista.length > 1) {
				modal(450, 120, null, 'Marisa - Alterar Configura&ccedil;&atilde;o B&ocirc;nus', true, 'Selecione apenas um registro.', false);
			} else if (lista.length < 1) {
				modal(450, 120, null, 'Marisa - Alterar Configura&ccedil;&atilde;o B&ocirc;nus', true, 'Selecione um registro.', false);
			} else {
				idConfigBonus = lista[0].value;
				modal(650, 670, 'paginas/configuracaoBonus/alteraConfiguracaoBonus.jsp', 'Marisa - Alterar Configura&ccedil;&atilde;o B&ocirc;nus', true, '', true);
			}
		});

		$('#anoF').focus();
});