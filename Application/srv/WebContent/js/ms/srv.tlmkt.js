$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#idCargoF').numeric();

		$("#form").validate({
	    	rules: {periodoF: {required:true}},
	     	messages: {periodoF: {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione o per&iacute;odo.</span>'}},                  
            submitHandler : function(form){
	        	$("#div-botao").hide();
	        	$("#div-load").show();
	           	document.form.action = "telemarketing.do?operacao=inicio";   
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
			$("#periodoF").val("");
		});    
    
	    $("#botao-avancar").click(function(){
			 $("#formBusca").submit();
		});	

		$("#btIncluir").click(function() {   
			modal(750,550,'paginas/tlmkt/incluiTelemarketing.jsp','Marisa - Incluir Telemarketing',true,'',true);        
		});
		
		$("#periodoF").focus();
});