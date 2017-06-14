
$(document).ready(function() {
             
		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$("#dataF").mask("99/99/9999");

		$("#form").validate({
	    	rules: {},
	     	messages: {},                  
	        submitHandler : function(form){

	        	$("#div-botao").hide();  
	            $("#div-load").show(); 
				
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

		     document.form.action = "relatorioLogProcesso.do?operacao=pesquisaLogProcesso";   
	         document.form.submit();
		  
	    
		});	

	    $("#botaoGerar").click(function(){
		     document.form.action = "processoPeriodo.do?operacao=geraProcesso";   
	         document.form.submit();
		});	

		$("#botaoLimparGerar").click(function() {
			$("#param1").val("");
		});

	    $("#botaoLimpar").click(function() {
			$("#dataF").val("");
			$("#processoF").val("");
		});
		
		var data     = $("#dataS").val();
		var processo = $("#processoS").val();
		
		if(data != null){
		   $("#dataF").val(data);
		}
		
		if(processo != null){
		   $("#processoF").val(processo);
		}
			
			
});
