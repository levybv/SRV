<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
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
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		$("#detMeta").validate({
	    	rules: {
	    		periodoImport: 		{required:true},
	    		tipoProcessamento: 	{required:true}
	    	},
	     	messages: {
	     		periodoImport:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		tipoProcessamento: 		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){
		        $("#div-botao").hide();
	           	$("#div-load").show();
		      	$("#botaoSalvar").attr("disabled", true);
	           	$("#botaoSalvar").val("Aguarde...");	           	
		      	$("#botaoCancelar").hide();
				$(document).unbind('keydown', 'esc');
		       	document.detMeta.action = "bonus.do?operacao=processarBonusAnual";
	            document.detMeta.submit();

	        }
    	});

        CalendarioComercialBusinessAjax.obtemListaPeriodoMesAno( 
   			function (periodo) {
   				if (periodo != null) {
   					var options = $('#periodoImport').attr('options');
   					for (var i=0; i<periodo.length; i++) {
   						var periodoVO = periodo[i];
   						options[options.length] = new Option(periodoVO.periodoMesAno, periodoVO.mesAno, false, false);
   						
   					}
   				}
   				
   		});

		$("#mesFiltro").val(mesF);
		$("#anoFiltro").val(anoF);
		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoIndicadorFiltro").val(descricaoIndicadorF);
		$("#idFilialFiltro").val(idFilialF);
		$("#acompMetaFilialFiltro").val($("#acompMetaFilial").val());
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 550px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
			<!--Filtro-->
			<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
			<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
			<input type='hidden' id="idIndicadorFiltro"    		name="idIndicadorF"     		value=""/>
			<input type='hidden' id="descricaoIndicadorFiltro"  name="descricaoIndicadorF"     	value=""/>
			<input type='hidden' id="idFilialFiltro"    		name="idFilialF"     			value=""/>
			<input type='hidden' id="acompMetaFilialFiltro"    	name="acompMetaFilialF"     	value=""/>
			
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" width="100%">
							<label class="label">Per&iacute;odo:</label>
							<select id="periodoImport" name="periodoImport" class="campo">
                            	<option value="" selected>[SELECIONE]</option>
                            </select>
						</td>
					</tr>				
					<tr>
						<td class="componente" width="100%">
							<label class="label">Processamento:</label>
							<select id="tipoProcessamento" name="tipoProcessamento" class="campo">
                            	<option value="" selected>[SELECIONE]</option>
                            	<option value="1">Calcular Indicadores Corporativos (Apenas em aberto)</option>
                            	<option value="2">Disponibilizar para Aceite (Apenas em aberto)</option>
                            	<option value="3">Finalizar Período (Apenas em andamento)</option>
                            </select>
							
						</td>
					</tr>				
				</tbody>
			</table>
			<br/>
		<div id="botao-geral" style="width:98%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Confirmar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
		<br/>
</form>
</div>