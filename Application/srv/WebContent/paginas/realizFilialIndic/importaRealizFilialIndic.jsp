<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

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
	    		arquivoUpload: 		{required:true}
	    	},
	     	messages: {
	     		arquivoUpload:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
	           	
		      	$("#botaoSalvar").attr("disabled", true);
	           	$("#botaoSalvar").val("Aguarde...");	           	

		      	$("#botaoCancelar").hide();
		      	
				$(document).unbind('keydown', 'esc');
	           	
		       	document.detMeta.action = "servlet/uploadRealizFilialIndic";
	            document.detMeta.submit();
	        }
    	});
    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 550px;">
	<form name="detMeta" id="detMeta" method="post" enctype="multipart/form-data"> 
			
			<br/>
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" width="100%">
							<label class="label">Arquivo Realizado Filial:</label> 
						</td>
					</tr>				
					<tr>
						<td class="componente" >
							<input type="file" class="campo2" name="arquivoUpload" id="arquivoUpload" size="70" >
						</td>
					</tr>
				</tbody>
			</table>
			<br/>
			<span class="requerido"><b>Layout do Arquivo</b></span><br/>
			<b>Tipo:</b> Arquivo TXT. Campos delimitados por ponto e v&iacute;rgula. Sem cabe&ccedil;alho.<br/>
			<b>Coluna 1:</b> M&ecirc;s de refer&ecirc;ncia<br/>
			<b>Coluna 2:</b> Ano de refer&ecirc;ncia<br/>
			<b>Coluna 3:</b> C&oacute;digo da Loja<br/>
			<b>Coluna 4:</b> C&oacute;digo do Indicador<br/>
			<b>Coluna 5:</b> Valor realizado (Casas decimais delimitadas por v&iacute;rgula)<br/>
			<br/>
			<span class="requerido"><b>Exemplo:</b></span> 12;2010;10;55;1580,02<br/>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Importar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
</form>
</div>