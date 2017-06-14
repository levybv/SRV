$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {
	     	},
	     	messages: {
	        },                  
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				document.form.action = "gerenteFilial.do?operacao=executarFiltro";   
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
   		
	    $("#botaoLimpar").click(function(){
			 $("#idFuncF").val("");
			 $("#nomeFuncF").val("");
			 $("#codAtuacaoF").val("");
			 $('#idFuncF').focus();
		});	   		
    
		$("#incluir").click(function(){
			modal(700,250,'paginas/gerenteFilial/incluiGerenteFilial.jsp','Marisa - Incluir Gerente de Loja',true,'',true);
		});

		$("#alterar").click(function() {   

		   	var lista = $('input[type=radio]:checked');
		
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Loja de Gerente',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Loja de Gerente',true,'Selecione um registro.',false); 
	    	} else {
	    		selecionado = lista[0].value;
	    		idFilialA = selecionado.substring(0,selecionado.indexOf(";"));
	    		idFuncA = selecionado.substring(selecionado.indexOf(";")+1);
				modal(700,230,'paginas/gerenteFilial/alteraGerenteFilial.jsp','Marisa - Alterar Loja de Gerente',true,'',true);        
			}
		}); 

		$("#excluir").click(function() {   

		   	var lista = $('input[type=radio]:checked');
		
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Loja de Gerente',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Loja de Gerente',true,'Selecione um registro.',false); 
	    	} else {
	    		selecionado = lista[0].value;
	    		idFilialE = selecionado.substring(0,selecionado.indexOf(";"));
	    		idFuncE = selecionado.substring(selecionado.indexOf(";")+1);
				modal(600,300,'paginas/gerenteFilial/excluiGerenteFilial.jsp','Marisa - Excluir Loja de Gerente',true,'',true);        
			}
		}); 
		
		$('#idFuncF').focus();
});