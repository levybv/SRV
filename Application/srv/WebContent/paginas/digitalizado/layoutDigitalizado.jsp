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
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 550px;">
	<form name="detMeta" id="detMeta" method="post" enctype="multipart/form-data"> 
			
			<br/>
			<span class="requerido"><b>Layout do Arquivo "Cart&atilde;o PL"</b></span><br/>
			<b>Tipo:</b> Arquivo XLS. Primeira Linha com Cabe&ccedil;alho.<br/>
			<b>Coluna A:</b> C&oacute;digo da Loja<br/>
			<b>Coluna B:</b> CPF do Vendedor<br/>
			<b>Coluna C:</b> CPF do Cliente<br/>
			<br/><br/>
			<span class="requerido"><b>Layout do Arquivo "SAX"</b></span><br/>
			<b>Tipo:</b> Arquivo XLS. Primeira Linha com Cabe&ccedil;alho.<br/>
			<b>Coluna A:</b> Vazia<br/>
			<b>Coluna B:</b> C&oacute;digo do Contrato<br/>
			<b>Coluna C:</b> CPF do Cliente<br/>
			<b>Coluna D:</b> Vazia<br/>
			<b>Coluna E:</b> C&oacute;digo da Loja<br/>
			<br/>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
</form>
</div>