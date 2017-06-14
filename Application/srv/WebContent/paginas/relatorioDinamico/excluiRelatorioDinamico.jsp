<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/RelatorioBusinessAjax.js'></script>
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
		
		RelatorioBusinessAjax.obtemRelatorio(idRelatorio,
			function (itemVO) {
				if (itemVO != null) {
					$("#idRelatorioE").val(itemVO.codigo);
					$("#codigoDIV").html(itemVO.codigo);
					$("#nomeDIV").html(itemVO.nome);
					$("#descricaoDIV").html(itemVO.descricao);
					$("#tituloDIV").html(itemVO.titulo);
					$("#nomeArquivoDIV").html(itemVO.nomeArquivo);
				}
		});
		
		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "relatorioDinamico.do?operacao=excluiRelatorioDinamico";   
		            document.detMeta.submit();
	         }
    	});

    });
    
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 620px;">
	<form name="detMeta" id="detMeta" method="post"> 

		<!--Chave-->
		<input type='hidden' id="idRelatorioE" name="idRelatorioE" value=""/>

		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Código:</label> 
					</td>
					<td class="componente" >
						<div id="codigoDIV"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome:</label> 
					</td>
					<td class="componente" >
						<div id="nomeDIV"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Descrição:</label> 
					</td>
					<td class="componente" >
						<div id="descricaoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Título:</label> 
					</td>
					<td class="componente" >
						<div id="tituloDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome do Arquivo:</label> 
					</td>
					<td class="componente" >
						<div id="nomeArquivoDIV"></div>
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