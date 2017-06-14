$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);

    	$('#codigo').numeric();
    	$('#matricula').numeric();

		$("#form").validate({
	    	rules: {
	     	},
	     	messages: {
	        },                  
	        submitHandler : function(form){
        		$("#div-botao").hide();  
               	$("#div-load").show(); 
	           	document.form.action = "usuario.do?operacao=inicio";   
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
				modal(450,120,null,'Marisa - Alterar Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Filial',true,'Selecione um registro.',false); 
	    	} else {
	    		idUsuario = lista[0].value;
				modal(470,290,'paginas/usuario/alteraUsuario.jsp','Marisa - Alterar Usu&aacute;rio',true,'',true);        
			}
		});
		
		$("#botaoLimpar").click(function() {
			$("#codigo").val("");
			$("#nome").val("");
			$("#matricula").val("");
			$("#login").val("");
			$("#ativo").val("");
			$("#perfil").val("");
			$("#autenticaAD").val("");
		});		
		
		$("#incluir").click(function() {   
			modal(470,255,'paginas/usuario/incluiUsuario.jsp','Marisa - Incluir Usu&aacute;rio',true,'',true);        
		});

		$('#codigo').focus();
});