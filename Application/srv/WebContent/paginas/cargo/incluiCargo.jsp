<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/GrupoRemuneracaoVariavelBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/ClasseHayBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CargoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {

		dwr.engine.setAsync(false);

		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		jQuery.validator.addMethod('codigoValido', function(value, element, params) {
			CargoBusinessAjax.codigoJaExiste(value, 
				function (codigoJaExiste) {
					if (codigoJaExiste) {
						$("#codigoJaExiste").val("S");
						return false;
					} else {
						$("#codigoJaExiste").val("N");
						return true;
					} 
			});
			return ($("#codigoJaExiste").val() == "N");
		});
		
		$("#idCargoC").numeric();
		
		$("#detCargo").validate({
			rules: {
    			idCargoC:        {required:true, codigoValido:true},
    			descricaoC:      {required:true},
    			idClasseHayC:    {required:true},
	     	},
	     	messages: {
		     	idCargoC:         {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
		     	                   codigoValido:   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> C&oacute;digo j&aacute; cadastrado.</span>'},
		     	descricaoC:       {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idClasseHayC:     {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	        },
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detCargo.action = "cargo.do?operacao=incluiCargo";   
	            document.detCargo.submit();
	        }
    	});
    	
		$("#idCargoFiltro").val(idCargoF);
		$("#descricaoFiltro").val(descricaoF);

		ClasseHayBusinessAjax.obtemClassesHay(
			function (classes) {
				if (classes != null) {
					var options = $('#idClasseHayC').attr('options');
					for (var i=0; i<classes.length; i++) {
						var classeHayVO = classes[i];
						options[options.length] = new Option(classeHayVO.descricao, classeHayVO.idClasseHay, false, false);
					}
				}
		});
		
		GrupoRemuneracaoVariavelBusinessAjax.obtemGruposRemuneracao(
			function (grupos) {
				if (grupos != null) {
					var ladoEsquerdo = true;
					for (var i=0; i<grupos.length; i++) {
						var grupoVO = grupos[i];
						var check = "<INPUT TYPE='checkbox' id='idGrupoRemuneracao' name='idGrupoRemuneracao' value='" + grupoVO.idGrupoRemuneracao + "'>" + grupoVO.descricaoOnline;
						if (ladoEsquerdo) {
							ladoEsquerdo = false;
							$("#grupoLE").append(check + "<BR/>");
						} else {
							ladoEsquerdo = true;
							$("#grupoLD").append(check + "<BR/>");
						}
					}
				}
		});		
		

    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 520px">
	<form name="detCargo" id="detCargo" method="post"> 
			
		<input type='hidden' id="codigoJaExiste" 	 name="codigoJaExiste" value="">
		<input type='hidden' id="idCargoFiltro"      name="idCargoF"       value=""/>
		<input type='hidden' id="descricaoFiltro"    name="descricaoF"     value=""/>
					
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width='20%'>
						<label class="label">Código:<span class="requerido">*</span></label> 
					</td>
					<td class="componente"  width='80%'>
  						<input type="text" class="campo2" id="idCargoC" name="idCargoC" value="" size="5" maxlength="5" />
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoC" name="descricaoC" value="" size="40" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Classe Hay:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idClasseHayC" name="idClasseHayC" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Agrupa Filiais:</label> 
					</td>
					<td class="componente" >
						<input type="checkbox" name="agrupaFiliaisC" id="agrupaFiliaisC">
					</td>
				</tr>				
				<tr>
					<td class="componente" colspan="2">
						<label class="label">Grupos de Remuneração:</label> 
					</td>
				</tr>
			</tbody>
		</table>	
		<table class="tabelaComponente">
			<tbody> 
				<tr>				
					<td class="componente" id="grupoLE" width='50%'></td>
					<td class="componente" id="grupoLD" width='50%'></td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>