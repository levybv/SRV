<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<%  
response.setHeader("Cache-Control","no-cache, expire"); 
response.setHeader("max-age","0");  
response.setHeader("Pragma","no-cache");        
response.setDateHeader ("expires", 0); 
response.setHeader("Last-Modified","Sun, 06 Nov 2012 15:32:08 GMT"); 
%>  

<meta http-equiv="X-UA-Compatible" content="IE=8" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 
<head> 
	<title><tiles:getAsString name="titulo"/></title> 
	<link rel="stylesheet" type="text/css" href="css/default.css" />
	<link rel="stylesheet" type="text/css" href="css/displaytag.css" />
	<link rel="stylesheet" type="text/css" href="css/datePicker.css" /> 
	<link rel="stylesheet" type="text/css" href="css/menu.css" /> 
	
	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/default-ie6.css" />
		<script src="js/lib/DD_belatedPNG.js" type="text/javascript"></script>
		<script type="text/javascript">
		  DD_belatedPNG.fix('img');
		</script>
	<![endif]-->
	
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/default-ie7.css" />
	<![endif]-->
	
	<!--[if IE 8]>
		<link rel="stylesheet" type="text/css" href="css/default-ie8.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="css/abas.css" />
	<link rel="stylesheet" type="text/css" href="css/tipsy.css" />
	<link rel="stylesheet" type="text/css" href="css/picklist.css" /> 

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
	<!--[if IE 6]>
		<script type="text/javascript" src="js/ns/neuro.modal.ie6-1.0.js"  charset="UTF8" ></script>
		<script type="text/javascript" src="js/lib/jquery.bgiframe.js"  charset="UTF8" ></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#modal').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#modal-block').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#critical-msg').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#miniModal').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#msg-block').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('.tipsy').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
			});
		</script>
	<![endif]-->
	<!--[if gte IE 7]>
		<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>
	<![endif]-->
	<!--[if !IE]>--> 
		<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>
	<!--<![endif]-->
	<script type="text/javascript" src="js/lib/jquery.autotab-1.1b.js"  charset="UTF8" ></script>	
	<script type="text/javascript" src="js/lib/datePicker.date.js" charset="UTF8"></script>   
	<script type="text/javascript" src="js/lib/jquery.datePicker.js" charset="UTF8"></script>  
	<script type="text/javascript">
		$(document).ready(function(){
			 $(document).bind('keydown', 'esc', function () {
			    	fecharTodosOsModais();
			 });
			
			/*
		 	$(document).keydown(function(e){
				e.keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
				var key_f5 = 116; 
				var key_ctrl = e.ctrlKey; 
				var key_r = 82; 
				if (key_f5 == e.keyCode || (key_ctrl && key_r == e.keyCode) ){
					mensagem("Teclas desabilitadas(F5, Ctrl+F5 e Ctrl+R).",true);
					if(event)
						event.keyCode = 0;
					else
						e.keyCode = 0;
						
					return false;
				}
			});
			*/	

			if(isNN){
				mensagem("Aplicativo não habilitado para esse browser.",true);
			}   
			
			<c:if test="${mensagemErro != null}">
				mensagem('<img src="images/error.png" class="imagem" /> <span class="erro"><c:out value="${mensagemErro}"/></span>', true);
				<c:remove var="mensagemErro"/>
				<c:remove var="mensagem"/>
			</c:if>
			<c:if test="${mensagem != null}">
				mensagem('<img src="images/about.png" class="imagem" /> <c:out value="${mensagem}"/>', true);
				<c:remove var="mensagem"/>
			</c:if>
			<c:if test="${erros != null}">
				var textoErro = "<c:out value="${fn:length(erros)}"/> Problema(s) encontrado(s):<BR>";
				<c:forEach items="${erros}" var="erro">
					textoErro += "<c:out value="${erro}"/><BR>";
				</c:forEach>			
				modal(600,200,null,"Marisa - Erros",true,textoErro,true);
				<c:remove var="erros"/>
			</c:if>
			
		});
		
	</script>
</head> 

<body>

	<div id="header">
		<div id="logo">
			<h1><img class="png_bg" src="images/novo-logo.png" /></h1>
			<h3><tiles:getAsString name="titulo"/></h3>
		</div>
	</div> 

	<%=request.getSession().getAttribute("menuSRV")%>
	
	<div id="content">	
		<tiles:insert attribute="conteudo" />
	</div>

	<div id="foo">	
		<tiles:insert attribute="footer" />
	</div>
	
	<div id="modal-block"></div>
	<div id="modal">
		<div id="modal-title"></div>
		<div id="modal-content"></div>
	</div>
	
	<div id="modal-block-processamento"></div>
	<div id="modal-processamento">
		<div id="modal-title-processamento"></div>
		<div id="modal-content-processamento">
			<hr />
				<div >
				<img src="images/logo_marisa_mulher.gif" style="float: left;"/>
				<div style="float: left;padding: 8px;">
					<span id="processando" style="font-size: 15pt;font-weight: bold;color: #BB1166;">Processando...</span>
					<br /><br />Favor Aguarde
				</div>
			</div>
			<br />
			<p></p>
		</div>
	</div>
	
	<div id="critical-msg" onclick="fecharMensagem();">
		<div id="critical-content"></div>
		<span class="xclose-msg">Clique na mensagem para fechar.</span>
	</div>
	
	<div id="msg-block"></div>
	<div class="miniModal-block"></div>
	
	<div id="miniModal" class="miniModal">
		<div id="mmContent" class="mmContent">
		</div>
	</div>
	
</body>
</html>