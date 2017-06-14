$(document).ready(function() {

        dwr.engine.setAsync(false);
        
        CalendarioComercialBusinessAjax.obtemListaPeriodoMesAno( 
			function (periodo) {
				if (periodo != null) {
					var options = $('#periodos').attr('options');
					for (var i=0; i<periodo.length; i++) {
						var periodoVO = periodo[i];
						options[options.length] = new Option(periodoVO.periodoMesAno, periodoVO.mesAno, false, false);
						
					}
				}
				
		});
        
		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		$("#form").validate({
	    	rules: {
	    		periodos:  {required:true},
	    	},
	     	messages: {
		     	periodos:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione um per&iacute;odo.</span>'},
	     	},
	        submitHandler : function(form) {
	        	$("#div-botao").hide();
	            $("#div-load").show();
	           	document.form.action = "bonus.do?operacao=pesquisaExtratoFuncionario";
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
			$("#periodos").val("");
			$('#periodos').focus();
		});
		
		$("#botaoVoltar").click(function() {
		   	document.form.action = "bonus.do?operacao=inicio";
		    document.form.submit();
		});		

		$("#calcular").click(function() {
		   	document.form.action = "bonus.do?operacao=calculaIndicadorFuncionario";
		    document.form.submit();
		});	

		$("#status").click(function() {
			modal(580,340,'paginas/bonus/alteraStatusBonus.jsp','Marisa - Alterar Status B&ocirc;nus Funcion&aacute;rio',true,'',true);
		});			
		
		$("#incluir").click(function() {   
			if ($('#periodos').val() == "") {
				modal(450,120,null,'Marisa - Incluir Indicador para Funcion&aacute;rio',true,'Selecione o período.',false);
				return;
			}
			modal(800,340,'paginas/bonus/incluiIndicadorFuncionario.jsp','Marisa - Incluir Indicador para Funcion&aacute;rio',true,'',true);        
		});	

		$("#imprimir").click(function() {   
		   	document.form.action = "bonus.do?operacao=imprimiIndicadores";
		    document.form.submit();
		});

		$("#alterar").click(function() {   
		
			var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Alterar Indicador para Funcion&aacute;rio',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Alterar Indicador para Funcion&aacute;rio',true,'Selecione um registro.',false); 
	    	} else {
				if ($('#periodos').val() == "") {
					modal(450,120,null,'Marisa - Alterar Indicador para Funcion&aacute;rio',true,'Selecione o per&iacute;odo.',false);
					return;
				}
				modal(700,300,'paginas/bonus/alteraIndicadorFuncionario.jsp','Marisa - Alterar Indicador para Funcion&aacute;rio',true,'',true);        
			}
		});			
		
		$("#excluir").click(function() {   
		
			var lista = $('input[type=radio]:checked');
			if(lista.length > 1) {  
				modal(450,120,null,'Marisa - Excluir Indicador para Funcion&aacute;rio',true,'Selecione apenas um registro.',false);
			} else if(lista.length < 1) {  
				modal(450,120,null,'Marisa - Excluir Indicador para Funcion&aacute;rio',true,'Selecione um registro.',false); 
	    	} else {
				if ($('#periodos').val() == "") {
					modal(450,120,null,'Marisa - Excluir Indicador para Funcion&aacute;rio',true,'Selecione o per&iacute;odo.',false);
					return;
				}
				modal(600,230,'paginas/bonus/excluiIndicadorFuncionario.jsp','Marisa - Excluir Indicador para Funcion&aacute;rio',true,'',true);        
			}
		});			
		
		$('#periodos').focus();
		
		
		var per = $('#periodoSelecionado').val();
		
		if(per != null){
		    $('#periodos').val(per);
		}
		
		
});


function consultaDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes) {

	this.idFuncionario 	= idFuncionario;
	this.idIndicador 	= idIndicador;
	this.idEmpresa 		= idEmpresa;
	this.idFilial 		= idFilial;
	this.ano			= ano;
	this.mes 			= mes;
	
	modal(470,500,'paginas/bonus/detalheCalculo.jsp','Marisa - Detalhe do C&aacute;lculo',true,'',true);	
}