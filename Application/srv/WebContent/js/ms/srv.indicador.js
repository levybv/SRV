	
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

		$("#form").validate({
	    	rules: {
   		     	mes:{required:true},
   		     	ano:{required:true},
   		     	grupos:{required:true},
   		     	indicadores:{required:true}
	     	},
	     	messages: {
   		     	mes:    	  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione o m&ecirc;s.</span>'},
   		     	ano:	      {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione o ano.</span>'},
   		     	grupos:  	  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione um grupo.</span>'},   		     	
   		     	indicadores:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione um indicador.</span>'}
	        },                  
	        submitHandler : function(form){
        		$("#div-botao").hide();  
               	$("#div-load").show(); 
				$("#tabSelecionada").val($("#proxTab").val());
	           	document.form.action = "indicadorRelatorios.do?operacao=executarFiltro";   
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
	    
		$("#atualizarCotas").click(function(){
			modalProcessoRequisicao();
	       	document.form.action = "configFilial.do?operacao=atualizarQuantidadeCotas";   
	        document.form.submit();
		});
		
		$(document).bind('keydown', 'esc', function () {
	    	fecharTodosOsModais();
	    });

		$("#botaoLimpar").click(function() {
			$("#grupos").val("");
			$("#indicadores").val("");
			$("#mes").val("");
			$("#ano").val("");
			$("#loja").val("");
			$("#lider").val("");
		});
	});
	
	// variaveis globais
	var VENDAS = 2;
	var PSF = 3;
	var EMRESTIMO_PESSOAL = 4;
	var SEGUROS_EMRESTIMO_PESSOAL = 5;
	
	// nao apague. serve 	
	function filtrar(){
		document.getElementById('proxTab').value = document.getElementById('tabSelecionada').value;
		$("#form").submit();
	}
	
	// nao apague. serve 	
	function validarAbasComErro(numAba, submeterPagina){
		if(submeterPagina){
			document.getElementById('proxTab').value = numAba;
			$("#form").submit();
		}
	}
	
	function populaIndicadores(grupoSelecionado){
	
		document.getElementById("indicadores").disabled = true;
		document.getElementById("indicadores").options.length = 0;
		document.getElementById("indicadores").options[document.getElementById("indicadores").options.length] = new Option('[SELECIONE]','');
		if(document.getElementById('relatorios') != null){
			document.getElementById('relatorios').innerHTML = "";
		}
		
		if(grupoSelecionado != ''){
		
			IndicadorBusinessAjax.obtemListaIndicadoresPorGrupo(grupoSelecionado,
				function (listaIndicadores) {
				
					for(i=0;i<listaIndicadores.length;i++){
						document.getElementById("indicadores").options[document.getElementById("indicadores").options.length] = new Option(listaIndicadores[i].descricaoIndicador,listaIndicadores[i].codIndicador);
					}
					document.getElementById("indicadores").disabled = false;					
		       });
		       
			if(grupoSelecionado == 	VENDAS){		        
				document.getElementById("tabPessoal").style.width="24.1%";
				document.getElementById("tabResidencial").style.width="24.1%";
				document.getElementById("tabProfissional").style.width="24.1%";
				document.getElementById("VM").innerHTML = '<a style="width: 24.1%;" id="tabVM" href="#" onclick="validarAbasComErro(4,true);"> VM</a>';
			}else{
				document.getElementById("tabPessoal").style.width="32.4%";
				document.getElementById("tabResidencial").style.width="32.4%";
				document.getElementById("tabProfissional").style.width="32.4%";
				document.getElementById("VM").innerHTML = '';
			}
		}
		
	}
	
	
	function populaLideres(grupoSelecionado){
		document.getElementById("lider").disabled = true;
		document.getElementById("lider").options.length = 0;
		document.getElementById("lider").options[document.getElementById("lider").options.length] = new Option('[SELECIONE]','');
		if(document.getElementById('relatorios') != null){
			document.getElementById('relatorios').innerHTML = "";
		}
		if(grupoSelecionado != ''){
			FuncionarioBusinessAjax.obtemLideres(grupoSelecionado,
				function (listaFuncionarios) {
					for(i=0;i<listaFuncionarios.length;i++){
						document.getElementById("lider").options[document.getElementById("lider").options.length] = new Option(listaFuncionarios[i].descricaoCargo + " " + listaFuncionarios[i].nomeFuncionario,listaFuncionarios[i].idFuncionario);
					}
					document.getElementById("lider").disabled = false;					
	        });
		}
	}
	
	function consultaDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes) {

		this.idFuncionario 	= idFuncionario;
		this.idIndicador 	= idIndicador;
		this.idEmpresa 		= idEmpresa;
		this.idFilial 		= idFilial;
		this.ano			= ano;
		this.mes 			= mes;

		modal(470,430,'paginas/indicador/detalheCalculo.jsp','Marisa - Detalhe do Cálculo',true,'',true);	
	}
	
	
	function consultaDetalheCalculoLoja(idIndicador, idEmpresa, idFilial, ano, mes) {

		this.idIndicador 	= idIndicador;
		this.idEmpresa 		= idEmpresa;
		this.idFilial 		= idFilial;
		this.ano			= ano;
		this.mes 			= mes;

		modal(470,330,'paginas/indicador/detalheCalculoLoja.jsp','Marisa - Detalhe do Cálculo',true,'',true);	
	}	