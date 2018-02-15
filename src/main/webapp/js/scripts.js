/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Powered by Koshka!!!

/* global SERVER_API */

var currentId = null;

function renderAction(data, type, row)
{
	var uploadButton = jQuery("<button/>", {
		type: "button",
		class: "btn btn-primary btn-xs uploader",
		id: "upload_" + row.id,
		text: "Upload"
	});
	
	return uploadButton[0].outerHTML;
}

function uploadDocument(e)
{	
	var buttonId = e.target.id;	
	var myRegexp = /^upload_(\d)$/;
	var match = myRegexp.exec(buttonId);
	currentId = match[1];
	
	$("#contract_file").click();	
};

function createContract()
{
	var url = SERVER_API + "/contract/";
	
	$.ajax({
        type: "POST",
        url: url,
		contentType: "application/json",
		error: (data, textStatus) =>
		{
			var msg = getAjaxError(data, textStatus);
			$("#messages").html(msg);
		},
		success: (data) =>
		{
			var msg = getSuccess(data.name + " created");
			$("#messages").html(msg);
		}
    });
}

function listContracts()
{
	var url = SERVER_API + "/contract/list";
	
	$.ajax({
        type: "GET",
        url: url,
		contentType: "application/json",
		error: (data, textStatus) =>
		{
			var msg = getAjaxError(data, textStatus);
			$("#messages").html(msg);
		},
		success: (data) =>
		{
			var tableDef = 
			{
				order: [[ 0, "desc" ]],
				destroy: true,
				paging: false,
				data: data,
				columnDefs: [
					{
						data: 'name', 
						targets: 0
					},
					{
						data: 'id', 
						targets: 1,
						orderable: false,
						render: renderAction
					}
				]
			};

			$("#contracts").dataTable(tableDef);
			$("#contracts").show();
			
			$(".uploader").click(uploadDocument);
		}
    });
}

$(() =>
{
	$('#contract_file').change(() => 
	{
		var url = SERVER_API + "/document";
		
		var content = new FormData();
		var ofile = document.getElementById("contract_file").files[0];
		content.append('file', ofile);
		content.append('id', currentId);

		$.ajax({   
			type: 'PUT',   
			url: url,   
			data: content,
			enctype: 'multipart/form-data',
			dataType: 'json',
			processData: false, 
			contentType: false,
			cache: false,
			success: (data) =>
			{
				var msg = getSuccess(data.fileName + " uploaded");
				$("#messages").html(msg);
				$("#overlay").remove();
			},
			error: (data, textStatus) =>
			{
				var msg = getAjaxError(data, textStatus);
				$("#messages").html(msg);
				$("#overlay").remove();
			}
		});

		makeOverlay();
	});
});