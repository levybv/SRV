$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

		$(".painel-filtro").show(100);

    	$("#codigo").numeric();

		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	         submitHandler : function(form){
        		$("#div-botao").hide();  
               	$("#div-load").show(); 
	           	document.form.action = "perfil.do?operacao=inicio";   
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
    
	    $("#botao-avancar").click(function(){
			 $("#formBusca").submit();
		});	
		
		$("#atualizar").click(function() {   
		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Perfil',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Perfil',true,'Selecione um registro.',false); 
	    	} else {
	    		codPerfil = lista[0].value;
				modal(600,480,'paginas/perfil/alteraPerfil.jsp','Marisa - Alterar Perfil',true,'',true);        
			}
		}); 

		$("#botaoLimpar").click(function() {
			$("#codigo").val("");
			$("#descricao").val("");
			$("#ativo").val("");
			$("#acessoResultadoBonus").val("");
			$("#reabreResultadoBonus").val("");
			$("#validaVlrFxEscala").val("");
			$("#ativaIndicador").val("");
		});
		
		$("#codigo").focus();
});