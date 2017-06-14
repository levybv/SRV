<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<br/> <br/>
<div class="boxIndex"> 
	<div class="boxCenter">	
		<div id="" class="panelErro">
				<div> 
                    <div id="" class="barTitle"><c:out value="${titulo}"></c:out></div>
                </div> 
                	
				<div id="painel-interno" style="font-variant: small-caps;">
				<br/>
					<img src="images/error.png" class="imagem" /> <c:out value="${mensagem}"></c:out>  
					<br>
					<br>
						<div class="barTitleInterno">Detalhamento do Erro</div><br />
						<div class="painel-interno" style="font-variant: small-caps; margin-left: 0.9cm">
							<c:out value="${erro.mensagemErro}"></c:out><BR><BR>
							Detalhamento:<BR>							
							<c:out value="${erro.stackErro}"></c:out><BR>
						</div>
					<br/> 
					<br/>
				</div> 
			
			 <div id="botao-geral">
                   <div id="botao" class="barBottom"> 
                     	<input style="text-decoration: none;" id="botao-avancar" type="button" class="button" value="Finalizar" onclick="location.href='<c:out value="${acaoBotao}"></c:out>';" />
                     	<input id="isButtonHidden" type="hidden" value="${isButtonHidden}"  />
                   </div>
              </div> 
         </div> 
	</div>      
</div>

<script type="text/javascript">
	$("#botao-avancar").ready(function() {
		$('input:button, select, input:radio, input:checkbox, button, input:submit, a, img').keydown(function(event){
			if(event.keyCode == 8)
				return false;
	     });
		$("#botao-avancar").focus();
	});

	if($("#isButtonHidden").val() == 'S') {
		$("#botao-avancar").hide(); 
	}
</script>  
<!--[if IE 6]>
		<script src="js/lib/DD_belatedPNG.js" type="text/javascript"></script>
		<script type="text/javascript">
		  DD_belatedPNG.fix('img');
		</script>
	<![endif]-->