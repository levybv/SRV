$(document).ready(function() {
	
		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	    });

    	$(".painel-filtro").show(100);
		
		
		$("#form").validate({
	    	rules: {
	    		//idFuncionario: {required:false}
	    	},
	     	messages: {
	     		//idFuncionario: {required: '<span class="erro"><img src="images/ico_error.gif" class="imagem" />O campo não pode conter caracter especial.</span>'}
	     	},                  
	        submitHandler : function(form){
	        	
	        	//$("#div-botao").hide();  
	            //$("#div-load").show(); 
				
//	           	document.form.action = "tarefa.do?operacao=pesquisa";   
	            document.form.submit();
			}
	    });
		
		$("#botaoLimpar").click(function() {
			$("#idCodFunF").val("");
			$("#idFuncionario").val("");
		});	
		
		$("#idFuncionario").change(function() {
			$("#idFuncionarioE").html("");
		});
		
		$("#botaoFiltrar").click(function() {
			if($("#idFuncionario").val() ==""){
				$("#idFuncionarioE").html(" Preenchimento deste campo é obrigatorio");
			}else{
				$("#div-botao").hide();  
	            $("#div-load").show(); 
				document.form.action ="agrupaFilialRemVar.do?operacao=montaTelaPrincipal";
				$("#form").submit();
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
		    
		
		$("#botaoVincular").click(function(e){
			var selectedOpts = $('#filiaisDisponiveis > option:selected');
			 $('#filiaisUtilizadas').append($(selectedOpts).clone());
			 selectedOpts.remove();
			e.preventDefault();
		}); 
		
		$("#botaoDesvinvular").click(function(e){
			var selectedOpts = $('#filiaisUtilizadas > option:selected');
			 $('#filiaisDisponiveis').append($(selectedOpts).clone());
			 selectedOpts.remove();
			e.preventDefault();
		}); 
		$("#idCodFunF").blur(function(){
			$("#idFuncionario option[value="+$(this).val()+"]").attr('selected', 'selected');
			$('#filiaisDisponiveis').empty();
			$('#filiaisUtilizadas').empty();
		}); 
		
		$("#idFuncionario").change(function(){
			$("#idCodFunF").val($("#idFuncionario").val());
			$('#filiaisDisponiveis').empty();
			$('#filiaisUtilizadas').empty();
		});
		
		$("#botaoAtualizar").click(function() {
			
			if($("#idFuncionario").val() ==""){
				$("#idFuncionarioE").html(" Preenchimento deste campo é obrigatorio");
			}else{
				if($('#filiaisUtilizadas').children('option').length==0){
					if(confirm('Você confirma a desvinculação de todas as filiais para esse colaborador?')){
						$("#div-botao").hide();  
			            $("#div-load").show();
			            $(this).attr("disabled", true);
			            $("#filiaisUtilizadas > option").attr('selected', 'selected');
						document.form.action ="agrupaFilialRemVar.do?operacao=incluiAgrupamentoFilial";
						$("#form").submit();
					}
				}else{
					$("#div-botao").hide();  
		            $("#div-load").show(); 
		            $("#filiaisUtilizadas > option").attr('selected', 'selected');
					document.form.action ="agrupaFilialRemVar.do?operacao=incluiAgrupamentoFilial";
					$(this).attr("disabled", true);
					$("#form").submit();
				}
				
			}
		});
});

