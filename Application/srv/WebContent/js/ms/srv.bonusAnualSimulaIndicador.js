$(document).ready(function() {

        dwr.engine.setAsync(false);

		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
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

		$(".painel-filtro").show(100);
		
		$("#botaoVoltar").click(function() {
		   	document.form.action = "bonusAnualFuncionario.do?operacao=pesquisaIndicadorFuncionario";
		    document.form.submit();
		});

		$("'input:text[name^=realizadoXMeta_]'").maskMoney({decimal:",", thousands:".", allowNegative:false});
});

