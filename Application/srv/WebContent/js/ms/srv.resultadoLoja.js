	
	var idFuncionario;
	var idIndicador;
	var idEmpresa;
	var idFilial;
	var ano;
	var mes;

	$(document).ready(function() {
		listAllError = [];
		listaErroValidacoes = [];
		
		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });		

		$(".painel-filtro").show(100);

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

		$(document).bind('keydown', 'esc', function () {
	    	fecharTodosOsModais();
	    });

		$("#form").validate({
	    	rules: {
	    		periodoF: {required:true},
	    		idFilialF: {required:true}
	    	},
	     	messages: {
	     		periodoF: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		idFilialF: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){
        		$("#div-botao").hide();  
               	$("#div-load").show(); 
	           	document.form.action = "resultadoLoja.do?operacao=executarFiltro";   
	            document.form.submit();
	        }
	    });

		$("#botaoLimpar").click(function() {
			$("#periodoF").val("");
			$("#idFilialF").val("");
		});

		$("#idFilialF").change(function () {
	        $("#idFuncF").val("");
	        $("#idFuncF").attr("disabled","disabled");
			$("#simuladoresPPR").hide();
	    });

		$("#simuladoresPPR").click(function() {
			tipoFilialSimul = $("#tipoFilial").val();
			modal(580,320,'paginas/resultadoLoja/simuladoresPPR.jsp','Marisa - Simuladores PPR',true,'',true);
		});

		if ($("#idFilialF").val() != "") {
			$("#simuladoresPPR").show();
		} else {
			$("#simuladoresPPR").hide();
		}

	});

	DataBusinessAjax.listaUltimosPeriodosMesAno(-6,
		function (periodoReturn) {
			if (periodoReturn != null) {
				var options = $('#periodoF').attr('options');
				for (var i=0; i<periodoReturn.length; i++) {
					var periodoItem = periodoReturn[i];
					options[options.length] = new Option(periodoItem.periodoDesc, periodoItem.periodo, false, false);
				}
				$('#periodoF').val($('#periodoFiltro').val());
			}
	});

	// nao apague. serve 	
	function filtrar(){
		document.getElementById('proxTab').value = document.getElementById('tabSelecionada').value;
		$("#form").submit();
	}

	// nao apague. serve 	
	function validarAbasComErro(numAba, submeterPagina){
		if(submeterPagina){
			document.getElementById('tabSelecionada').value = numAba;
			$("#form").submit();
		}
	}
