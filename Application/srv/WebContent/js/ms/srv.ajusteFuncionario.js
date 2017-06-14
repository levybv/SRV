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
	     		idFuncionario:  	{number:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo precisa ser numérico.</span>'},
	     		cracha:  			{number:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo precisa ser numérico.</span>'}
	     	},
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	           	document.form.action = "ajusteFuncionario.do?operacao=inicio";   
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
	    		modal(450,120,null,'Marisa - Consulta Ajuste Funcionários',true,'Informe a matrícula, o crachá, o CPF, ou pelo menos 4 letras do nome do funcionário.',false); 
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
		
		$("#ajustar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Ajuste Funcionário',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Ajuste Funcionário',true,'Selecione um registro.',false); 
	    	} else {
	    		document.form.action = "ajusteFuncionario.do?operacao=detalheAjusteFuncionario";   
	            document.form.submit();
			}
		});

		$("#importar").click(function() {
			modal(580,160,'paginas/ajuste/importaAjusteFuncionario.jsp','Marisa - Importar Ajuste Funcionário',true,'',true);
		});

		$('#idFuncionario').focus();
});