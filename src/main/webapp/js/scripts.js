/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global SERVER_API */

function renderAction(data, type, row)
{
	var uploadButton = jQuery("<button/>", {
		type: "button",
		className: "btn btn-primary btn-xs uploader",
		id: "upload_" + row.id,
		innerText: "Upload"
	});
	
	return uploadButton.outerHTML;
}

function uploadDocument(e)
{
	$("#file").click();
	
	var buttonId = e.target.id;	
	var myRegexp = /^upload_(\d)$/;
	var match = myRegexp.exec(buttonId);
	var id = match[1];
	alert(id);
	return;
	
	var content = new FormData();
	var ofile = document.getElementById(id).files[0];
	content.append('file', ofile);
	content.append('id', id);

	$.ajax({   
		type: 'POST',   
		url: '/api/excel/upload/',   
		data: content,
		enctype: 'multipart/form-data',
		dataType: 'json',
		processData: false, 
		contentType: false,
		cache: false,
		success: (data) =>
		{
			var status = data.status;

			if (status === 'loaded')
			{
				var templateId = parseInt($("#template_list").val());
				var name = data.name;
				var good = "Файл " + name + " загружен";
				var msg = getSuccess(good);
				
				if (id === 'tools_upload_template')
				{
					getExcelConfig(templateId, (errMsg) => {
						if (errMsg)
						{
							getProgress(errMsg);
						} else {
							getProgress(msg);
						}
					});
				} else {
					getProgress(msg);
				}
			} else {
				var msg = getFailed(
					"Произошла ошибка при загрузке файла : " + 
					data.message
				);

				$("#messages").html(msg);
			}

			$(".overlay").remove();
		},
		error: (data, textStatus) =>
		{
			handleAjaxError(data, textStatus);
			$(".overlay").remove();
		}
	});

	makeOverlay();		
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