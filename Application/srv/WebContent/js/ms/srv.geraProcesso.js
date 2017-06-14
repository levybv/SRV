$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){
	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
	           	document.form.action = "processoPeriodo.do?operacao=geraProcesso";   
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
	    	 if ($("#param1").val() == "") {
		     	 modal(450,120,null,'Marisa - Períodos de Processamento - Geração',true,'Informe os parâmetros para geração.',false);
		     } else {
				 $("#form").submit();
		     }
		});	
		
		$("#botaoLimpar").click(function() {
			$("#param1").val("");
		});

		$('#param1').focus();
});