<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/PonderacaoBusinessAjax.js'></script>
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
		
		/*PonderacaoBusinessAjax.obtemPonderacao(idPonderacao,
			function (ponderacaoVO)	{
				$("#idPonderacaoP").val(ponderacaoVO.idPonderacao);
				$("#grupoRemVarDIV").html(ponderacaoVO.descricaoGrupoRemVar);
				$("#cargoDIV").html(ponderacaoVO.descricaoCargo);
				$("#grupoIndicadorDIV").html(ponderacaoVO.descricaoGrupoIndicador);
				$("#indicadorDIV").html(ponderacaoVO.descricaoIndicador);
				$("#pesoDIV").html(ponderacaoVO.pesoFormatado);
				$("#valorPremioDIV").html(ponderacaoVO.valorPremioFormatado);
		});	*/		
		
		$("#detMeta").validate({
	    	rules: {},
		    messages: {},
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "ponderacao.do?operacao=excluiPonderacao";   
	            document.detMeta.submit();
	        }
    	});
    	
		/*$("#idGrupoRemVarFiltro").val(idGrupoRemVarF);
		$("#idCargoFiltro").val(idCargoF);
		$("#idGrupoIndicadorFiltro").val(idGrupoIndicadorF);
		$("#idIndicadorFiltro").val(idIndicadorF);*/
		
		
		$("#grupoRemVarDIV").html(descrGrupoRemVar);
		$("#cargoDIV").html(descrCargo);
		$("#grupoIndicadorDIV").html(descrGrupoIndicador);
		$("#indicadorDIV").html(descrIndicador);
		$("#pesoDIV").html(pesoFormatado);
		$("#valorPremioDIV").html(valorPremioFormatado);
		$("#filialDIV").html(descrFilial);
		$("#tipoFilialDIV").html(descrTipoFilial);
		$("#idPonderacaoP").val(idPonderacao);
		
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 670px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idGrupoRemVarFiltro"  	  name="idGrupoRemVarF"   	value=""/>
		<input type='hidden' id="idCargoFiltro"    		  name="idCargoF"     		value=""/>
		<input type='hidden' id="idIndicadorFiltro"    	  name="idGrupoIndicadorF"  value=""/>
		<input type='hidden' id="idGrupoIndicadorFiltro"  name="idIndicadorF"     	value=""/>
		
		<!--Chave-->
		<input type='hidden' id="idPonderacaoP"  		  name="idPonderacaoP"   	value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="25%">
						<label class="label">Grupo de Remuneração:</label> 
					</td>
					<td class="componente" >
						<div id="grupoRemVarDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Cargo:</label> 
					</td>
					<td class="componente" >
						<div id="cargoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
						<div id="filialDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Tipo Filial:</label> 
					</td>
					<td class="componente" >
						<div id="tipoFilialDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Grupo de Indicador:</label> 
					</td>
					<td class="componente" >
						<div id="grupoIndicadorDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<div id="indicadorDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Peso:</label> 
					</td>
					<td class="componente" >
						<div id="pesoDIV"></div>
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Valor Prêmio:</label> 
					</td>
					<td class="componente" >
						<div id="valorPremioDIV"></div>
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Excluir" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>