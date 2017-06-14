$(document).ready(function () {     
	
	//AUTO-TAB
	$('input').keyup(
		function(event){
			var size = $(this).attr('maxlength');
			autoTab(this,size,event);
		}
	);
	
	$("#aguardeCargo").hide();
	$('#lojaPercentual').numeric();	 
	$('#lojaPontos').numeric();
	$("#filial").attr('value', filialCod); // coloca o novo valor na input     
	$("#empCod").attr('value', empCod); // coloca o novo valor na input   
		
	// (OPTIONAL) adjust the width of the select box to the bigger of the two 	
	$("select[name='available']").multiSelect("select[name='selected']", {
		trigger: "input[name='remove']",
		triggerAll: "input[name='removeAll']", 
		sortOptions:false,
		autoSubmit:false   
		});
	
	$("select[name='availableTipo']").multiSelect("select[name='selectedTipo']", {
		trigger: "input[name='removeTipo']",
		triggerAll: "input[name='removeAllTipo']", 
		sortOptions:false,
		autoSubmit:false   
		}); 
	
	
	$("select[name='selected']").multiSelect("select[name='available']", {
		trigger: "input[name='add']",
		triggerAll: "input[name='addAll']",
		sortOptions:false,
		autoSubmit:true 
		});
	
	$("select[name='selectedTipo']").multiSelect("select[name='availableTipo']", {
		trigger: "input[name='addTipo']",
		triggerAll: "input[name='addAllTipo']",
		sortOptions:false,
		autoSubmit:true 
		});  
		
	$("input:radio[name=lojaQG]").change(function(){  
		if($("input:radio[name=lojaQG]:checked").val() == "S") { 
			$("#available").removeAttr("disabled");
			$("#selected").removeAttr("disabled");
			$("#add").removeAttr("disabled");
			$("#remove").removeAttr("disabled");
			$("#addAll").removeAttr("disabled");
			$("#removeAll").removeAttr("disabled");  
			 
		} else {
			$("input[name='addAll']").click();      
			
			$("#available").attr("disabled","disabled"); 
			$("#selected").attr("disabled","disabled");
			$("#add").attr("disabled","disabled");
			$("#remove").attr("disabled","disabled");
			$("#addAll").attr("disabled","disabled");
			$("#removeAll").attr("disabled","disabled");     
		}
	});   
	
	$.ajax({  
		url: 'filial.do?operacao=popularModalFilial&filial=' + filialCod + '&empCod=' + empCod,    
		type:'POST', 
		cache: false,  
		async: false,
		beforeSend: function(){
			$("#aguardeCargo").show();  
		},
		error: function(){    
  			mensagem("Erro ao tentar atualizar o cadastro.", true);
  		}, 
		success: function (resp) { 
			var arrayListas = resp.split("/");
			$("#selected").html("");
			$("#available").html("");
			$("#selectedTipo").html("");
			$("#availableTipo").html("");

			var bigString = "";
			var bigStringDispo = "";
			var bigStringTipo = "";
			var bigStringDispoTipo = ""; 

			if(arrayListas[0] != "NENHUM"){
				var arrayObjects = arrayListas[0].split("#"); 

				for ( i = 1; i < arrayObjects.length; i++) { 
					var elemento = arrayObjects[i].split(";"); 
					bigString += "<option value='"+elemento[0]+"'>"+elemento[1]+"</option>"; 
				}   

				$("#selected").html(bigString); 
			}

			if(arrayListas[1] != "NENHUM"){ 
				var arrayListaDisponivel = arrayListas[1].split("#");
				 
				for ( i = 1; i < arrayListaDisponivel.length; i++) { 
					var elemento = arrayListaDisponivel[i].split(";"); 
					bigStringDispo += "<option value='"+elemento[0]+"'>"+elemento[1]+"</option>"; 
				}

				$("#available").html(bigStringDispo); 
			}

			if(arrayListas[2] == "S") {
				$("#lojaQGSim").attr("checked", "checked");   
			} else {
				$("#lojaQGNao").attr("checked", "checked");  
			}

			if(arrayListas[3] == "S") {
				$("#lojaVirtualSim").attr("checked", "checked");   
			} else {
				$("#lojaVirtualNao").attr("checked", "checked");  
			}

			if(arrayListas[4] == "S") {
				$("#lojaDigitalizaSim").attr("checked", "checked");   
			} else {
				$("#lojaDigitalizaNao").attr("checked", "checked");  
			}

			if(arrayListas[5] != "NENHUM") { 
				$("#lojaCustodiaSim").attr("checked", "checked");   
			} else {
				$("#lojaCustodiaNao").attr("checked", "checked");   
			}

			if(arrayListas[6] != "NENHUM") {
				$("#lojaPercentual").val(arrayListas[6]); 
			}else{
				$("#lojaPercentual").val('0');
			}
				
			if(arrayListas[7] != "NENHUM") { 
				$("#filialNome").val(arrayListas[7]);
			} 
			
			if(arrayListas[8] != "NENHUM"){
				var arrayObjects = arrayListas[8].split("#"); 

				for ( i = 1; i < arrayObjects.length; i++) { 
					var elemento = arrayObjects[i].split(";"); 
					bigStringTipo += "<option value='"+elemento[0]+"'>"+elemento[1]+"</option>"; 
				}   

				$("#selectedTipo").html(bigStringTipo); 
			}

			if(arrayListas[9] != "NENHUM"){ 
				var arrayListaDisponivel = arrayListas[9].split("#");
				 
				for ( i = 1; i < arrayListaDisponivel.length; i++) { 
					var elemento = arrayListaDisponivel[i].split(";"); 
					bigStringDispoTipo += "<option value='"+elemento[0]+"'>"+elemento[1]+"</option>"; 
				}

				$("#availableTipo").html(bigStringDispoTipo); 
			}
			
			if(arrayListas[10] != "NENHUM") { 
				$("#lojaPontos").val(arrayListas[10]);   
			} 
 
			$("#aguardeCargo").hide();
			
			
		}
	});	

	if($("input:radio[name=lojaQG]:checked").val() == "S") { 
		$("#available").removeAttr("disabled");
		$("#selected").removeAttr("disabled");
		$("#add").removeAttr("disabled");
		$("#remove").removeAttr("disabled");
		$("#addAll").removeAttr("disabled");
		$("#removeAll").removeAttr("disabled");  
		 
	} else {
		$("input[name='addAll']").click();      
		
		$("#available").attr("disabled","disabled"); 
		$("#selected").attr("disabled","disabled");
		$("#add").attr("disabled","disabled");
		$("#remove").attr("disabled","disabled");
		$("#addAll").attr("disabled","disabled");
		$("#removeAll").attr("disabled","disabled");     
	}

}); 
    	              