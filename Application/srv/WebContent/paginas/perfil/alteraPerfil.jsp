<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/PerfilBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

		
<script type="text/javascript">


	$(document).ready(function() {
	
		$('input:button, radio, checkbox, button, input:submit, a,img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	     });
	     
	     $('input:[type=text]').blur(function () {
            $(this).removeClass("focusGained");
            $(this).addClass("campo");
        });
	     
	
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		$("#formPerfil").validate({
	    	rules: {
	    	descricaoL: {required:true}
	     	},
	     	messages: {
	     	descricaoL:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Descrição &eacute; um campo obrigat&oacute;rio.</span>'}
	         },                  
	         submitHandler : function(form){

	        		$("#div-botao").hide();  
	               	$("#div-load").show(); 
	               	
	         	for (i = 0; i < document.getElementById('acessos').options.length ; i++){
					document.getElementById('acessos').options[i].selected = true;
				}
				
	           	document.formPerfil.action = "perfil.do?operacao=alterarPerfil";   
	            document.formPerfil.submit();
	         }
	        	              
	    });
    			
	});
	
	// variaveis
	var MODULO = 0;
	var FUNCIONALIDADE = 1;
	var TIPOACESSO = 2;
	var totalAjaxFinalizados = 0;
	var podeLiberarBotaoSalvar = 3; // somente depois dos 3 ajax finalizados

///////////////////// inicio funcoes ajax  
	
	PerfilBusinessAjax.obtemPerfilByCod(codPerfil,
				function (perfilVO) {
					document.getElementById('codPerfilL1').value = perfilVO.idPerfil;
					document.getElementById('codPerfilL').innerHTML = perfilVO.idPerfil;
					document.getElementById('descricaoL').value = perfilVO.descricao;					
					if(perfilVO.isAtivo){
						document.getElementById('checkAtivo').checked = true;
					}
					if(perfilVO.isExibeBonus){
						document.getElementById('checkExibeBonus').checked = true;
					}
					if(perfilVO.isValidaFaixaEscala){
						document.getElementById('checkFaixaEscala').checked = true;
					}
					if(perfilVO.isReabreResultado){
     					document.getElementById('checkReabreBonus').checked = true;
					}else{
    					document.getElementById('checkReabreBonus').checked = false;
					}
					liberaBotaoSalvar();
		       }); 

	PerfilBusinessAjax.obtemListaModulos(
		function (listaModulos){
			document.getElementById("modulos").options.length = 0;
			document.getElementById("modulos").options[document.getElementById("modulos").options.length] = new Option('[SELECIONE]','');
			document.getElementById("funcionalidades").options.length = 0;
			document.getElementById("funcionalidades").options[document.getElementById("funcionalidades").options.length] = new Option('[SELECIONE]','');
	   		document.getElementById("tiposAcesso").options.length = 0;
			document.getElementById("tiposAcesso").options[document.getElementById("tiposAcesso").options.length] = new Option('[SELECIONE]','');				
			if(listaModulos != ''){
				for(i=0;i<listaModulos.length;i++){
					document.getElementById("modulos").options[document.getElementById("modulos").options.length] = new Option(listaModulos[i].descricao,listaModulos[i].codModulo);
				}
			}
	   		liberaBotaoSalvar();
   		}
  	); 
   	
   	
   	PerfilBusinessAjax.obtemListaAcessos(codPerfil,
		function (listaAcessos){
			for(var i=0;i<listaAcessos.length;i++){
			   	document.getElementById("acessos").options[document.getElementById("acessos").options.length] = new Option(listaAcessos[i].moduloVO.descricao+' - '+listaAcessos[i].funcionalidadeVO.descricao+' - '+listaAcessos[i].tipoAcessoVO.descricao,listaAcessos[i].funcionalidadeVO.codFuncionalidade+'@'+listaAcessos[i].tipoAcessoVO.codTipoAcesso);
			}
	   		liberaBotaoSalvar();
   		}
   	);
   	
///////////////////// fim funcoes ajax     	
   	
   	
   	function liberaBotaoSalvar(){
   		totalAjaxFinalizados++;
   		if(podeLiberarBotaoSalvar == totalAjaxFinalizados){
   			document.getElementById('botaoSalvar').disabled = false;
   			document.getElementById('botaoAdicionar').disabled = false;
   		}
   	}
   	
   	
   	function buscaFuncionalidade(valorModulo){
   		document.getElementById("funcionalidades").options.length = 0;
		document.getElementById("funcionalidades").options[document.getElementById("funcionalidades").options.length] = new Option('[SELECIONE]','');
		
   		document.getElementById("tiposAcesso").options.length = 0;
		document.getElementById("tiposAcesso").options[document.getElementById("tiposAcesso").options.length] = new Option('[SELECIONE]','');				
		if(valorModulo != ""){
	   		PerfilBusinessAjax.obtemListaFuncionalidades(null,valorModulo,
				function (listaFuncionalidades){
					if(listaFuncionalidades.length > 0){
						document.getElementById("funcionalidades").options[document.getElementById("funcionalidades").options.length] = new Option('[TODOS]','T');			
						for(i=0;i<listaFuncionalidades.length;i++){
							document.getElementById("funcionalidades").options[document.getElementById("funcionalidades").options.length] = new Option(listaFuncionalidades[i].descricao,listaFuncionalidades[i].codFuncionalidade);
						}
					}
				}
			);
		}
   	}
   	
   	function buscaTipoAcesso(valorFuncionalidade){
		document.getElementById("tiposAcesso").options.length = 0;
		document.getElementById("tiposAcesso").options[document.getElementById("tiposAcesso").options.length] = new Option('[SELECIONE]','');
		if(valorFuncionalidade != "" && valorFuncionalidade != "T"){
			var acessosList = obtemAcessoJaConcedidos(document.getElementById("acessos"));
	   		PerfilBusinessAjax.obtemListaTipoAcesso(valorFuncionalidade,
				function (listaTipoAcesso){
					var flagJaPassouUmaVez = false;
					for(i=0;i<listaTipoAcesso.length;i++){
						if(!isJaTemAcesso(acessosList,listaTipoAcesso[i].codTipoAcesso, valorFuncionalidade)){
							if(!flagJaPassouUmaVez){
								document.getElementById("tiposAcesso").options[document.getElementById("tiposAcesso").options.length] = new Option('[TODOS]','T');					
								flagJaPassouUmaVez = true;
							}
							document.getElementById("tiposAcesso").options[document.getElementById("tiposAcesso").options.length] = new Option(listaTipoAcesso[i].descricao,listaTipoAcesso[i].codTipoAcesso);
						}
					}
				}
			);
		}
   	}
   	
   	function isJaTemAcesso(listaAcessos,codTipoAcesso, valorFuncionalidade){
   		if(listaAcessos.length > 0 ){
   			var acessosTmp = listaAcessos.split(';');
	   		for(j=0;j<acessosTmp.length;j++){
	   			var accTmp = acessosTmp[j];
	   			var tipoFunc = accTmp.split('@');
	   			if(tipoFunc[0] == valorFuncionalidade && codTipoAcesso == tipoFunc[1]){
	   				return true;
	   			}
	   		}
	   	}
	   	return false;
   	}
   	
   	function obtemAcessoJaConcedidos(obj){
   		var acessosLocal = '';
   		for(i=0;i<obj.length;i++){
   			acessosLocal += obj.options[i].value+';';
   		}
   		return acessosLocal;
   	}
   	
   	function adicionaAcesso(funcionalidade, tipoAcesso){
	   	var acessosList = obtemAcessoJaConcedidos(document.getElementById("acessos"));
   		if(document.getElementById("funcionalidades").value == 'T'){
			//	alert(document.getElementById("modulos").value);
   			PerfilBusinessAjax.obtemAcessos(document.getElementById("modulos").value, null,
				function (listaAcessos){
					for(i=0;i<listaAcessos.length;i++){
						if(!isJaTemAcesso(acessosList,listaAcessos[i].tipoAcessoVO.codTipoAcesso, listaAcessos[i].funcionalidadeVO.codFuncionalidade)){
							document.getElementById("acessos").options[document.getElementById("acessos").options.length] = new Option(listaAcessos[i].moduloVO.descricao+' - '+listaAcessos[i].funcionalidadeVO.descricao+' - '+listaAcessos[i].tipoAcessoVO.descricao,listaAcessos[i].funcionalidadeVO.codFuncionalidade+'@'+listaAcessos[i].tipoAcessoVO.codTipoAcesso);
						}
					}
				}
			);
   		}else if(document.getElementById("tiposAcesso").value == 'T'){
	   		PerfilBusinessAjax.obtemAcessos(document.getElementById("modulos").value, document.getElementById("funcionalidades").value,
					function (listaAcessos){
						for(i=0;i<listaAcessos.length;i++){
							if(!isJaTemAcesso(acessosList,listaAcessos[i].tipoAcessoVO.codTipoAcesso, listaAcessos[i].funcionalidadeVO.codFuncionalidade)){
								document.getElementById("acessos").options[document.getElementById("acessos").options.length] = new Option(listaAcessos[i].moduloVO.descricao+' - '+listaAcessos[i].funcionalidadeVO.descricao+' - '+listaAcessos[i].tipoAcessoVO.descricao,listaAcessos[i].funcionalidadeVO.codFuncionalidade+'@'+listaAcessos[i].tipoAcessoVO.codTipoAcesso);
							}
						}
					}
				);
   		}else if(tipoAcesso != ''){
   			var descricao = obtemBoxSelecionado(MODULO) + " - ";
   			descricao += obtemBoxSelecionado(FUNCIONALIDADE)+ " - ";
   			descricao += obtemBoxSelecionado(TIPOACESSO);
		   	document.getElementById("acessos").options[document.getElementById("acessos").options.length] = new Option(descricao,funcionalidade+'@'+tipoAcesso);
		   	document.getElementById("tiposAcesso").options[obtemPosicaoSelecionado(document.getElementById("tiposAcesso"))] = null;
		}
   	}
   	
   	function obtemBoxSelecionado(valor){
   		var resp=-1;
   		switch(valor){
			case MODULO:
				resp = document.getElementById("modulos").options[obtemPosicaoSelecionado(document.getElementById("modulos"))].text;
				break;
			case FUNCIONALIDADE:
				resp = document.getElementById("funcionalidades").options[obtemPosicaoSelecionado(document.getElementById("funcionalidades"))].text;
			  break;
			case TIPOACESSO:
				resp = document.getElementById("tiposAcesso").options[obtemPosicaoSelecionado(document.getElementById("tiposAcesso"))].text;
			  break;			  
		}
		return resp;
	}
	
	function removeSelecionados(listBox){
		var tam = listBox.length;
		for(i=0; i<tam; i++) {
			for(j=0; j<listBox.length; j++) {
		      	if(listBox.options[j].selected)  {
			        listBox.options[j] = null;
			        break;
	      		}
	    	}
	    }
    	buscaTipoAcesso(document.getElementById("funcionalidades").value);
	}
	
	function obtemPosicaoSelecionado(selectBox){
		for(i=0; i<selectBox.length; i++) {
	      	if(selectBox.options[i].selected)  {
		        return i;
      		}
    	}
	}
</script>



<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 580px;">
	<form name="formPerfil" id="formPerfil" method="post"> 
			<input type='hidden' id="codPerfilL1" name="codPerfilL1" value="">
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" >
							<label class="label" for="cliCpf">Perfil:</label> 
							<span id='codPerfilL'></span>
						</td>
					</tr>
					
					<tr> 
						<td class="componente">
							<label class="label" >Descri&ccedil;&atilde;o<span class="requerido"><span class="requerido">*</span></label> 
	  						<input type="text" class="campo2" id="descricaoL" name="descricaoL" value="" size="40" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Ativo:</label>
							<input type="checkbox" class="campo" id="checkAtivo"  name="checkAtivo" />
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Acesso Resultado B&ocirc;nus:</label>
							<input type="checkbox" class="campo" id="checkExibeBonus"  name="checkExibeBonus"   />
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Reabre Resultado Bonus:</label>
							<input type="checkbox" class="campo" id="checkReabreBonus"  name="checkReabreBonus" />
						</td>
					</tr>
				</tbody>
			</table>
			<table>
				<tbody>
					<tr>
						<td class="componente">
							<label class="label" >M&oacute;dulo:</label><br>
							<select id="modulos" name="modulos" class="campo2" onChange="buscaFuncionalidade(this.value);">
	                           	<option value="" selected>[SELECIONE]</option>
	                        </select>
						</td>
						<td rowspan=3  class="componente">
							<label class="label">Acessos:</label><br>
							<select id="acessos" name="acessos" size="10" style="width:250px" class="campo2" multiple="multiple">
							</select>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Funcionalidade:</label><br>
							<select id="funcionalidades" name="funcionalidades" class="campo2" onChange="buscaTipoAcesso(this.value);">
	                           	<option value="" selected>[SELECIONE]</option>
	                        </select>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Tipo de acesso</label><br>
							<select id="tiposAcesso" name="tiposAcesso" class="campo2">
	                           	<option value="" selected>[SELECIONE]</option>
	                        </select>
						</td>
					</tr>
					<tr>
						<td colspan=2>
							<div id="botao-geral" style="width:100%;">
									<div class="div-botao" style="text-align: right;float:left;width:50%;">
										<input id="botaoAdicionar" type="button" class="button" value="+" onclick="adicionaAcesso(document.getElementById('funcionalidades').value,document.getElementById('tiposAcesso').value);" disabled=disabled />
									</div>										
									<div class="div-botao" style="text-align: left;float:right;width:50%;">
										<input id="botaoRemover" type="button" class="button" value="-" onclick="removeSelecionados(document.getElementById('acessos'));"/>					
									</div>

							</div>
						</td>
					</tr>
					<tr>
						<td colspan=2>
							<label class="label" >Valida Vlr Faixas das Escalas: </label>
							<input type="checkbox" class="campo" id="checkFaixaEscala"  name="checkFaixaEscala" />
						</td>
					</tr>
				</tbody>
			</table>
			<br/>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" disabled=disabled />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
</form>
</div> 