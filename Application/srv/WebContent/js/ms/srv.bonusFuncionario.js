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
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	           	document.form.action = "bonus.do?operacao=inicio";   
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
	    	//if ($('#idFuncionario').val() == "" && $('#cracha').val() == "" && $('#cpfFuncionario').val() == "" && $('#nomeFuncionario').val().length < 4) {
	    		//alert("Informe a matr�cula, o crach�, o CPF, ou pelo menos 4 letras do nome do funcion�rio.");
	    		//return;
	    	//}
			$("#form").submit();
		});
		
	
		$("#botaoLimpar").click(function() {
			$("#idFuncionario").val("");
			$("#nomeFuncionario").val("");
			$("#cracha").val("");
			$("#cpfFuncionario").val("");
			$('#idFuncionario').focus();
		});

		$("#botaoImportar").click(function() {
			modal(580,340,'paginas/bonus/importaBonusFuncionario.jsp','Marisa - Importar B&ocirc;nus Funcion&aacute;rio',true,'',true);        
		});		

		$("#botaoProcessar").click(function() {
			modal(580,340,'paginas/bonus/processaBonusFuncionario.jsp','Marisa - Processar B&ocirc;nus Funcion&aacute;rio',true,'',true);        
		});		

		$('#idFuncionario').focus();
		
		
});


function consultaFuncionario(idFuncionarioSelecionado, nomeFuncionarioSelecionado, cargoFuncionarioSelecionado, centroCustoFuncionarioSelecionado) {

	$("#idFuncionarioSelecionado").val(idFuncionarioSelecionado);
	$("#nomeFuncionarioSelecionado").val(nomeFuncionarioSelecionado);
	$("#cargoFuncionarioSelecionado").val(cargoFuncionarioSelecionado);
	$("#centroCustoFuncionarioSelecionado").val(centroCustoFuncionarioSelecionado);

   	document.form.action = "bonus.do?operacao=pesquisaExtratoFuncionario";
    document.form.submit();
}