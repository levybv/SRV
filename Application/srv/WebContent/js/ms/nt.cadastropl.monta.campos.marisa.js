
// VARIÁVEL GLOBAL
overAposent = false; 



// MODIFICA OS CAMPOS ASSIM QUE A PÁGINA É CARREGADA.
function returnCod(){
	var cods = $("#cargoNome").val() +"&"+$("#cliEmpOcupacao").val();
	return cods;
}

function getCodSelect(campo){
	
	var codCargo = campo.value;
	
	$.ajax({ 
		  url: 'atividade.do?operacao=consultarCargoOcupacoes&cargo=' + codCargo,  
		  type:'POST', 
		  cache: false, 
		  async: false,
		  error: function(){    
			mensagem("Erro ao tentar consultar os cargos.", true);
		  }, 
		  success: function (resp) {
		
			  var bigString = "<option value=''>[SELECIONE]</option>";
			  
			  if(resp != '' && $.trim(resp) != ''){
				  
				  var arrayObjects = resp.split("#");
				  
				  for (var i = 1; i < arrayObjects.length; i++) {
					var elemento = arrayObjects[i].split(";");
					bigString += "<option value='"+codCargo+"#"+elemento[0]+"'>"+elemento[1]+"</option>";
				  }
			  }
			  
			  $("#cliEmpOcupacao").html(bigString);
		  } 
	});
	
	changeCampos(true);
}

function changeCampos(readyPage){
	var str = returnCod();
	var arrayCods = str.split("&");
	var arraySub = arrayCods[1].split("#");
	var estadoCivil = $("#cliEcDomi").val();
	var arrayCivil = estadoCivil.split("#");
	var estCivil = arrayCivil[1]+"/"+arrayCivil[0];
	$("#cliCodBeneficio").unmask();
	habilitarCampo('cliEmpEmpresa',true,false,readyPage);
	if (overAposent){
		limparCamposReplicantes();
		overAposent=false;
	}
	
	switch (arrayCods[0]) {
	case "1":
		// ASSALARIADO 
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCodBeneficio',false,false,readyPage);
		habilitarCampo('cliCgcCnpj',false,false,readyPage);

		// [CAMPOS INCLUSOS]
		habilitarCampo('cliEmpDtAdmAno',true,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',true,false,readyPage);

		break;

	case "2":
		// AUTÔNOMO
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCodBeneficio',false,false,readyPage);
		habilitarCampo('cliCgcCnpj',false,false,readyPage);
		
		// [CAMPOS INCLUSOS]
		habilitarCampo('cliEmpDtAdmAno',true,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',true,false,readyPage);

		break;
	
	case "3":
		// ESTABELECIDOS
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCodBeneficio',false,false,readyPage);
		
		// [CAMPOS INCLUSOS]
		habilitarCampo('cliCgcCnpj',true,'99.999.999/9999-99',readyPage);
		habilitarCampo('cliEmpDtAdmAno',true,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',true,false,readyPage);

		break;
		
	case "4":
		// LIBERAIS
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCodBeneficio',false,false,readyPage);
		habilitarCampo('cliCgcCnpj',false,false,readyPage);
		
		// [CAMPOS INCLUSOS]
		habilitarCampo('cliEmpDtAdmAno',true,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',true,false,readyPage);
		
		break;
	case "5":
		// APOSENTADOS
		// CAMPOS RETIRADOS
		habilitarCampo('cliCgcCnpj',false,false,readyPage);
		habilitarCampo('cliEmpDtAdmAno',false,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',false,false,readyPage);
		if(readyPage)
			$("#cliEmpDtAdm").val('');
		
		// [CAMPOS INCLUSOS]	
		habilitarCampo('cliCodBeneficio',true,'999.999.999-9',false,readyPage);

		var subStr = arraySub[1]+"/"+arraySub[0];

		// OUTRAS DEFINIÇÕES
			switch (subStr) {
			case "/5":
				return true;			
				break;
				
			case "1/5":
				
				overAposent = true;
				
				// [CAMPOS INCLUSOS]
				habilitarCampo('cliCodBeneficio',true,false,readyPage);

				// CAMPOS RETIRADOS
				habilitarCampo('cliEmpEmpresa',false,false,readyPage);
				
				$("#cliEmpEmpresa").val($("#cliEmpOcupacao option[value='"+arrayCods[1]+"']").text());
				camposReplicantes();
				
				break;
				
			case "2/5":
				
				overAposent = true;
				// [CAMPOS RETIRADOS]
				habilitarCampo('cliCodBeneficio',false,false,readyPage);
				habilitarCampo('cliEmpEmpresa',false,false,readyPage);
				
				$("#cliEmpEmpresa").val($("#cliEmpOcupacao option[value='"+arrayCods[1]+"']").text());
				camposReplicantes();	
				break;
				
			default:
				// [CAMPOS RETIRADOS]
				habilitarCampo('cliEmpDtAdmAno',false,false,readyPage);
				habilitarCampo('cliEmpDtAdmMes',false,false,readyPage);
								
				break;
			}

		break;
	case "6":
		// OUTROS
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCodBeneficio',false,false,readyPage);
		habilitarCampo('cliCgcCnpj',false,false,readyPage);
		
		var subStr = arraySub[1]+"/"+arraySub[0];
		if (subStr=="3/6" || subStr=="4/6" || subStr=="7/6" || subStr=="12/6" || subStr=="13/6"){
			if (subStr=="4/6"){		
				rendasReady(false);
			}else{
					if (subStr=="3/6"){	
						$("#cliEmpEmpresa").val("DO LAR");
						habilitarCampo('cliEmpEmpresa',false,false,false);
						if (estCivil=="2/4"){	
							$("#cliRendaConj")[0].checked = true;
//					$("#cliRendaConj")[1].disabled = true;
							document.getElementsByName("cliRendaConj")[1].disabled = true;
					
//				$("#cliRendaConj")[1].addClass("campo");
//				$("#cliRendaConj")[1].removeAttr("readonly");
							changeFields('S');
						}	
					}else {
						//$("#cliEmpEmpresa").val("");
						habilitarCampo('cliEmpEmpresa',true,false,false);
					}
			}	
			habilitarCampo('cliEmpDtAdmAno',false,false,readyPage);
			habilitarCampo('cliEmpDtAdmMes',false,false,readyPage);

		}else{
		// [CAMPOS INCLUSOS]
			rendasReady(true);
			habilitarCampo('cliEmpEmpresa',true,false,readyPage);
			habilitarCampo('cliEmpDtAdmAno',true,false,readyPage);
			habilitarCampo('cliEmpDtAdmMes',true,false,readyPage);
			
		}
		break;
	case "7":
		// PENSIONISTAS
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliEmpDtAdmAno',false,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',false,false,readyPage);		
		if(readyPage)
			$("#cliEmpDtAdm").val('');
		
		habilitarCampo('cliCgcCnpj',false,false,readyPage);		
		
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCodBeneficio',true,'999.999.999-9',readyPage);		
				
		var subStr = arraySub[1]+"/"+arraySub[0];
		
		// OUTRAS DEFINIÇÕES
			switch (subStr) {
			case "/7":
				return true;			
			break;
				
			case "1/7":
				overAposent = true;
				habilitarCampo('cliCodBeneficio',false,false,readyPage);
				habilitarCampo('cliEmpEmpresa',false,false,readyPage);				
				$("#cliEmpEmpresa").val($("#cliEmpOcupacao option[value='"+arrayCods[1]+"']").text());
				camposReplicantes();	
				
				break;
			case "2/7":
				overAposent = true;
				
				habilitarCampo('cliCodBeneficio',false,false,readyPage);
				habilitarCampo('cliEmpEmpresa',false,false,readyPage);				
				$("#cliEmpEmpresa").val($("#cliEmpOcupacao option[value='"+arrayCods[1]+"']").text());
				camposReplicantes();	
				break;
			case "3/7":
				overAposent = true;
				
				habilitarCampo('cliCodBeneficio',false,false,readyPage);
				habilitarCampo('cliCodBeneficio',false,false,readyPage);
				$("#cliEmpEmpresa").val($("#cliEmpOcupacao option[value='"+arrayCods[1]+"']").text());
				camposReplicantes();	
				break;
			case "4/7":
				overAposent = true;
				habilitarCampo('cliCodBeneficio',true,"999.999.999-9",readyPage);
				habilitarCampo('cliEmpEmpresa',false,false,readyPage);

				$("#cliEmpEmpresa").val($("#cliEmpOcupacao option[value='"+arrayCods[1]+"']").text());
				camposReplicantes();
				break;
			default:
				return true;
				break;
			}
		break;
	case "8":
		// COLABORADORES
		// [CAMPOS RETIRADOS]
		habilitarCampo('cliCgcCnpj',false,false,readyPage);
		habilitarCampo('cliCodBeneficio',false,false,readyPage);
		habilitarCampo('cliEmpDtAdmAno',false,false,readyPage);
		habilitarCampo('cliEmpDtAdmMes',false,false,readyPage);		
		//habilitarCampo('cliEmpSalario',false,false,readyPage);		
		//habilitarCampo('cliOutrasRendas',false,false,readyPage);		
		if(readyPage)
			$("#cliEmpDtAdm").val('');		
		habilitarCampo('cargoNome',false,false,readyPage);
		
		$("#cliEmpOcupacao").children().eq(1).attr({"selected":"selected"});
		$("#cliEmpOcupacao").attr({"disabled":"disabled"});
		$("#cliEmpEmpresa").val("MARISA");
		removerMensagemErro($("#cliEmpOcupacao"));		

		break;
	default:
		return true;
		break;
	}
}
/* 
 * NESTA FUNÇÃO OS CAMPOS CEP, NUMERO E TELEFONE (COM DDD) SÃO REPLICADOS DA ABA RESIDENCIAL.
 */
function camposReplicantes(){
	$("#cliEmpCep").val($("#cliResCep").val());
	$("#cliEmpNum").val($("#cliResNum").val());
	$("#cliEmpFone").val($("#cliResFone").val());
	$("#cliEmpUf").val($("#cliResUf").val());
	$("#cliEmpCid").val($("#cliResCid").val());
	$("#cliEmpBai").val($("#cliResBai").val());
	$("#cliEmpRua").val($("#cliResRua").val());
	$("#cliEmpComp").val($("#cliResComp").val());
	$("#cliEmpRamal").val($("#cliResRamal").val());
}

// NESTA FUNÇÃO, OS CAMPOS SUPRACITADOS SÃO LIMPOS.
function limparCamposReplicantes(){
	if(overAposent){
		$("#cliEmpCep").val("");
		$("#cliEmpNum").val("");
		$("#cliEmpFone").val("");
		$("#cliEmpUf").val("");
		$("#cliEmpCid").val("");
		$("#cliEmpBai").val("");
		$("#cliEmpRua").val("");
		$("#cliEmpComp").val("");
		$("#cliEmpRamal").val("");
		$("#cliEmpEmpresa").val("");
		$("#cliEmpEmpresa").removeAttr("readonly");
		$("#cliEmpEmpresa").removeClass("readonly");
		$("#cliEmpEmpresa").addClass("campo");
		
		overAposent=false;
	}
}

function rendasReady(flag){
	if (flag){
		habilitarCampo('cliEmpSalario',true,false,false);
		habilitarCampo('cliOutrasRendas',true,false,false);
	}else {
		habilitarCampo('cliEmpSalario',false,false,true);
		habilitarCampo('cliOutrasRendas',false,false,true);		
	}
}

