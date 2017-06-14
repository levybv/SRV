$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idFuncionario').numeric();
		$('#cracha').numeric();
		$('#cpfFuncionario').mask("999.999.999-99");  
				
		$("#form").validate({
	    	rules: {
		    	idFuncionario:   	{number:true},
		    	cracha:   			{number:true}
	    	},
	     	messages: {
	     		idFuncionario:  	{number:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo precisa ser num�rico.</span>'},
	     		cracha:  			{number:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo precisa ser num�rico.</span>'}
	     	},
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	           	document.form.action = "allPageBonus.do?operacao=inicio";   
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
    
	    $("#botaoConsultar").click(function(){
	    	if ($('#idFuncionario').val() == "" && $('#cracha').val() == "" && $('#cpfFuncionario').val() == "" && $('#nomeFuncionario').val().length < 4) {
	    		modal(450,120,null,'Marisa - Consulta Funcion�rios',true,'Informe a matr�cula, o crach�, o CPF, ou pelo menos 4 letras do nome do funcion�rio.',false); 
	    		return;
	    	}
			$("#form").submit();
		});
		
		$("#botaoLimpar").click(function() {
			$("#idFuncionario").val("");
			$("#nomeFuncionario").val("");
			$("#cracha").val("");
			$("#cpfFuncionario").val("");
			$('#idFuncionario').focus();
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Funcion�rio',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Funcion�rio',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		idFuncionario 		= lista[0].value;
	    		
	    		//Filtro
				idFuncionarioF		= $("#idFuncionario").val();
				nomeFuncionarioF	= $("#nomeFuncionario").val();
				crachaF				= $("#cracha").val();
				cpfFuncionarioF		= $("#cpfFuncionario").val();
				
				modal(610,610,'paginas/funcionario/alteraFuncionario.jsp','Marisa - Alterar Funcion�rio',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Funcion�rio',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Funcion�rio',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		idFuncionario 		= lista[0].value;
	    		
	    		//Filtro
				idFuncionarioF		= $("#idFuncionario").val();
				nomeFuncionarioF	= $("#nomeFuncionario").val();
				crachaF				= $("#cracha").val();
				cpfFuncionarioF		= $("#cpfFuncionario").val();
				
				modal(610,570,'paginas/funcionario/excluiFuncionario.jsp','Marisa - Excluir Funcion�rio',true,'',true);        
			}
		});
		
		
		$("#incluir").click(function() {
		
    		//Filtro
			idFuncionarioF		= $("#idFuncionario").val();
			nomeFuncionarioF	= $("#nomeFuncionario").val();
			crachaF				= $("#cracha").val();
			cpfFuncionarioF		= $("#cpfFuncionario").val();
		
			modal(610,610,'paginas/funcionario/incluiFuncionario.jsp','Marisa - Incluir Funcion�rio',true,'',true);        
		});
		
		
		$("#importar").click(function() {   
		
    		//Filtro
			idFuncionarioF		= $("#idFuncionario").val();
			nomeFuncionarioF	= $("#nomeFuncionario").val();
			crachaF				= $("#cracha").val();
			cpfFuncionarioF		= $("#cpfFuncionario").val();
		
			modal(580,160,'paginas/funcionario/importaFuncionario.jsp','Marisa - Importar Funcion�rio',true,'',true);        
		});			
		
		$('#idFuncionario').focus();
});