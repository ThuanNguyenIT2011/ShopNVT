dropdownBrand = $('#brand');
let extraImageCount = 0;

$(document).ready(function() {
	$('#shortDescription').richText();
	$('#fullDescription').richText();

	dropdownBrand.change(function() {
		getCategories();
	}
	);
	getCategories();

	$("input[name='extraFileImage']").each(function(index) {
		$(this).change(function() {
			showExtraImageThumnail(this, index);
		});
	}
	);
	/*
	$("#extraFileImage1")
		.change(
			function() {
				fileSize = this.files[0].size;
				if (fileSize > 1048576) {
					this
						.setCustomValidity("You must choose an image less than 1MB!");
					this.reportValidity();
				} else {
					this.setCustomValidity("");
					showExtraImageThumnail(this);
				}
			});*/
});

function showExtraImageThumnail(fileInput, index) {
	let file = fileInput.files[0];
	
	fileName = file.name;
	imageHiddenField = $('#imageName'+index);
	if(imageHiddenField.length){
		$('#imageName'+index).val(fileName);
	}
	
	let reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	}
	reader.readAsDataURL(file);
	extraImageCount = $("input[name='extraFileImage']").length;
	if (index >= extraImageCount - 1) {
		addExtraImageSections(index + 1);
		extraImageCount++;
	}	
}

function addExtraImageSections(index) {
	htmlExtra = `
			<div class="col" id='divExtraImage${index}'>
				<div id="extraImageHeader${index}"><label>Extra Image #${index + 1}:</label></div>
				<div>
					<img class="image-fuild" src="${productDefault}" alt="Extra image #{${index + 1}} review" 
						id="extraThumbnail${index}">
				</div>
				<div>
					 <input accept="image/*" type="file" class="form-control-file" 
					 	name="extraFileImage" onchange="showExtraImageThumnail(this,${index})">
				</div>
			</div>
	`;

	htmlLinkRemove = `
			<a class="btn fas fa-times-circle float-right"
				href="javascript:removeExtraImage(${index - 1})"></a>`;

	$('#extraImageHeader' + (index - 1)).append(htmlLinkRemove);
	$('#divProductImage').append(htmlExtra);
}

function removeExtraImage(index) {
	$('#divExtraImage' + index).remove();
	extraImageCount--;
}

function addNextDetailsection() {
	allDivDetails = $("[id^='divDetail']");
	divDetailCount = allDivDetails.length;
	htmlDetaildSection = `
		<div class="form-inline" id="divDetail${divDetailCount}">
			<input type="hidden" name="detailIDs" value="0">
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255">
			
			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255">
			<a class="btn fas fa-times-circle fa-2x icon-dark"
	href = "javascript: removeDetailSectionById('divDetail${divDetailCount}')"
	title = "Remove this detail" ></a >
		</div>
	`;

	$('#divProductDetails').append(htmlDetaildSection);

	$("input [name='detailNames']").last().focus();

}

function removeDetailSectionById(id) {
	$('#'+id).remove();
}

function getCategories() {
	brandId = dropdownBrand.val();

	url = brandModuleURL + '/' + brandId + '/categories';

	$.ajax({
		type: "GET",
		url: url,
		dataType: 'json',
		success: function(response) {
			content = response.reduce((con, ele) => {
				return con + `
							<option value="${ele.id}">${ele.name}</option>
						`
			}, '');
			$('#category').html(content);
		},
		error: function(e) {
		}
	});

}

function checkUnique(form) {

	catId = $("#id").val();
	catName = $("#name").val();
	csrf = $("input[name='_csrf']").val();
	params = {
		id: catId,
		name: catName,
		_csrf: csrf,
	};
	$.post(
		urlCheckUniqueName,
		params,
		function(respone) {
			if (respone == "Ok") {
				form.submit();
				return true;
			} else if (respone == "Duplication") {
				message = "There is another Product having the name "
					+ catName;
				showModalDialog("Warning", message);
			} else {
				showModalDialog("Error",
					"Unknown reponse from server");
			}
		}).fail(function() {
			showModalDialog("Error", "Could not connect to the server");
		});
	return false;
}

function showModalDialog(title, message) {
	$("#contentModelWarning").text(message);
	$("#modalTitle").text(title);
	$("#modalDialog").modal();
}