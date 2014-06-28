$().ready(function() {
    $("#btn_tgl").click(translate);
});

function translate(ev)
{
	// Ejecutar
	var params = {};
	params.text = $("#tgl_text").val();
	sendAjax("tgl_translate", params, translateOk, translateError);
}

function translateOk(data)
{
	if (data.error) alert(data.error);
	else $("#svg_canvas").html(data.svg);
}

function translateError()
{
	alert("Error!!");
}

//----------------------
//Función de enviar ajax
//----------------------

function sendAjax(ajaxUrl, params, ajaxSuccessGeneric, ajaxErrorGeneric)
{
	ajaxQuery = $.ajax({
		timeout: 10000,
		type: "POST",
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		url: ajaxUrl,
		error: ajaxErrorGeneric,
		success: ajaxSuccessGeneric,
		data: params,
		cache: false,
		crossDomain: true
	});			
}