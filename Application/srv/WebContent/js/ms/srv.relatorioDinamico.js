$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);

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

		$('#idRelatorioDinamicoF').numeric();

		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	           	document.form.action = "relatorioDinamico.do?operacao=inicio";   
	            document.form.submit();
			}
	    });

	    $("#botaoConsultar").click(function(){
			 $("#form").submit();
		});	

		$("#botaoLimpar").click(function() {
			$("#idRelatorioDinamicoF").val("");
			$("#nomeRelatorioDinamicoF").val("");
			$("#descricaoRelatorioDinamicoF").val("");
			$("#ativoRelatorioDinamicoF").val("");
			$("#tipoRelatorioDinamicoF").val("");
			$("#periodoRelatorioDinamicoF").val("");
		});

		$("#atualizar").click(function() {
		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Relatório Dinâmico',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Relatório Dinâmico',true,'Selecione um registro.',false); 
	    	} else {
	    		idRelatorio = lista[0].value;
				modal(800,580,'paginas/relatorioDinamico/alteraRelatorioDinamico.jsp','Marisa - Alterar Relatório Dinâmico',true,'',true);        
			}
		});

		$("#excluir").click(function() {
		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Relatório Dinâmico',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Relatório Dinâmico',true,'Selecione um registro.',false); 
	    	} else {
	    		idRelatorio = lista[0].value;
				modal(650,250,'paginas/relatorioDinamico/excluiRelatorioDinamico.jsp','Marisa - Excluir Relatório Dinâmico',true,'',true);        
			}
		});

		$("#incluir").click(function() {   
			modal(800,550,'paginas/relatorioDinamico/incluiRelatorioDinamico.jsp','Marisa - Incluir Relatório Dinâmico',true,'',true);        
		});

		$('#idRelatorioDinamicoF').focus();
});