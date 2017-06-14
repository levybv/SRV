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
	           	document.form.action = "bonusAnualFuncionario.do?operacao=inicio";   
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
			$("#form").submit();
		});

		$("#botaoLimpar").click(function() {
			$("#idFuncionario").val("");
			$("#nomeFuncionario").val("");
			$("#cracha").val("");
			$("#cpfFuncionario").val("");
			$('#idFuncionario').focus();
		});
		
		$('#idFuncionario').focus();

});


function consultaFuncionario(idFuncionarioSelecionado, nomeFuncionarioSelecionado, cargoFuncionarioSelecionado, centroCustoFuncionarioSelecionado) {
	$("#idFuncionarioSelecionado").val(idFuncionarioSelecionado);
	$("#nomeFuncionarioSelecionado").val(nomeFuncionarioSelecionado);
	$("#cargoFuncionarioSelecionado").val(cargoFuncionarioSelecionado);
	$("#centroCustoFuncionarioSelecionado").val(centroCustoFuncionarioSelecionado);
   	document.form.action = "bonusAnualFuncionario.do?operacao=pesquisaIndicadorFuncionario";
    document.form.submit();
}