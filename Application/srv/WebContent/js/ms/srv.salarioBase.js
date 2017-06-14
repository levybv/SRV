
$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#anoF').numeric();
		$('#idFuncionarioF').numeric();
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
	           	document.form.action = "salarioBase.do?operacao=inicio";   
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

	    	 //if ($("#mesF").val() == "" && $("#anoF").val() == "" && $("#idFuncionarioF").val() == "" && $("#nomeFuncionarioF").val() == "") {
		     //	 modal(450,120,null,'Marisa - Consultar Salários Base',true,'Informe algum filtro de pesquisa.',false);
		     	 
	    	 if ($("#idFuncionarioF").val() == "" && $("#nomeFuncionarioF").val() == "") {
		     	 modal(450,120,null,'Marisa - Consultar Agrupamentos de Filiais',true,'Informe a mátricula ou o nome de um funcionário.',false);
		     	 
		     } else {
				 $("#form").submit();
		     }
	    
		});	
		
		$("#botaoLimpar").click(function() {
			$("#mesF").val("");
			$("#anoF").val("");
			$("#idFuncionarioF").val("");
			$("#nomeFuncionarioF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Salário Base',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Salário Base',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idEmpresa = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFilial = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFuncionario = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		ano = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		mes = chave;
	    		
	    		//Filtro
				idFuncionarioF  		= $("#idFuncionarioF").val();
				nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
				anoF   					= $("#anoF").val();
				mesF 					= $("#mesF").val();
				
				modal(545,200,'paginas/funcionario/alteraSalarioBase.jsp','Marisa - Alterar Salário Base',true,'',true);        
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Salário Base',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Salário Base',true,'Selecione um registro.',false); 
	    	} else {

				//Chave	    	
	    		var chave = lista[0].value;

	    		idEmpresa = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFilial = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		idFuncionario = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		ano = chave.substring(0, chave.indexOf(";"));
	    		chave = chave.substring(chave.indexOf(";")+1);

	    		mes = chave;
	    		
	    		//Filtro
				idFuncionarioF  		= $("#idFuncionarioF").val();
				nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
				anoF   					= $("#anoF").val();
				mesF 					= $("#mesF").val();
				
				modal(550,170,'paginas/funcionario/excluiSalarioBase.jsp','Marisa - Excluir Salário Base',true,'',true);        
			}
		});
				
		
		$("#incluir").click(function() {   
		
    		//Filtro
    		idFilialFuncF           = $("#idFilialFunc").val();
			idFuncionarioF  		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			anoF   					= $("#anoF").val();
			mesF 					= $("#mesF").val();
		
			modal(480,200,'paginas/funcionario/incluiSalarioBase.jsp','Marisa - Incluir Salário Base',true,'',true);        
		});
		
		
		$("#importar").click(function() {   
		
    		//Filtro
			idFuncionarioF  		= $("#idFuncionarioF").val();
			nomeFuncionarioF   		= $("#nomeFuncionarioF").val();
			anoF   					= $("#anoF").val();
			mesF 					= $("#mesF").val();
		
			modal(580,160,'paginas/funcionario/importaSalarioBase.jsp','Marisa - Importar Salário Base',true,'',true);        
		});		
		
		$('#mesF').focus();
		
		
		
});