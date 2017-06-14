$(document).ready(function() {

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		$('#anoF').numeric();
		$('#idFilialF').numeric();
		$('#idIndicadorF').numeric();
		
		
	
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
	    	document.form.action = "metaLider.do?operacao=pesquisa";
	    	$("#form").submit();
		});	
	    
	    
		$("#botaoLimpar").click(function() {
			$("#mesesF").val("");
			$("#liderF").val("");
			$("#indicadorF").val("");
			$("#equipeF").val("");
		});
		
		$("#atualizar").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Meta de Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Meta de Filial',true,'Selecione um registro.',false); 
	    	} else {
	    		MetaLiderBusinessAjax.validaEdicao($('input[name=checkbox1]:checked', '#form').val(),
					function (isEditavel) {
						if (isEditavel) {
							modal(800,180,'paginas/meta/alteraMetaLider.jsp','Marisa - Alterar Meta Lider',true,'',true);				
						}else{
							modal(450,120,null,'Marisa - Alterar Meta Lider ',true,'Mês retroativo não pode ser editado.',false); 
						}
	    		});	
			}
		});
		
		
		$("#excluir").click(function() {

		   	var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Meta de Filial',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Meta de Filial',true,'Selecione um registro.',false); 
	    	} else {
	    		MetaLiderBusinessAjax.validaEdicao($('input[name=checkbox1]:checked', '#form').val(),
					function (isEditavel) {
						if (isEditavel) {
							if(confirm("Você confirma a exclusão da meta selecionada?")){
								$('#form').attr('action', 'metaLider.do?operacao=removeMetaLider');	
								$("#form" ).submit();
							}
						}else{
							modal(450,120,null,'Marisa - Alterar Meta Lider ',true,'Mês retroativo não pode ser editado.',false); 
						}
	    		});	        
			}
		});
				
		
		$("#incluir").click(function() {   
			modal(800,180,'paginas/meta/incluiMetaLider.jsp','Marisa - Incluir Meta Lider',true,'',true);        
		});
		
		
				
		
		$('#mesF').focus();
});