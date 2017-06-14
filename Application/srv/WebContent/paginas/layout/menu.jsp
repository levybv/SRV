<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<script type="text/javascript" src="../../js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript">
	
	$(document).ready(function() {
		var url = $('#url').val();
		$('#formMenuCCM').attr('action',url);
		$('#formMenuCCM').submit();
	});
	
</script>
<form id="formMenuCCM" name="formMenuCCM" method="post">
	<input id="sessao" name="sessao" type="hidden" value="<c:out value="${sessionSessao}"></c:out> "/>
	<input id="webapplip" name="webapplip" type="hidden" value="<c:out value="${sessionWebapplip}"></c:out> "/>
	<input id="ipCobol" name="ipCobol" type="hidden" value="<c:out value="${sessionIpCobol}"></c:out> "/>
	<input id="ipmenu" name="ipmenu" type="hidden" value="<c:out value="${sessionIpmenu}"></c:out> "/>
	<input id="weblinkageweb" name="weblinkageweb" type="hidden" value="<c:out value="${sessionWeblinkageweb}"></c:out> "/>
	<input id="webfilcod" name="webfilcod" type="hidden" value="<c:out value="${sessionCodigoFilial}"></c:out> "/>
	<input id="url" name="url" type="hidden" value="<c:out value="${sessionMenuUrl}"></c:out> "/>
</form>