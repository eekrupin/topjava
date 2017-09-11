var ajaxUrl = "ajax/meals/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    addMealHandlers();
    basisHandlers();
});

function addMealHandlers(){

    $("#detailsForm").submit(function () {
        saveMeal();
        return false;
    });

    $(".edit").click(function () {
        editRow($(this).attr("id"));
    });

}

function saveMeal() {
    var form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $("#editRow").modal("hide");
            filterTable();
            successNoty("Saved");
        }
    });
}

function filterTable() {

    $.get(ajaxUrl + "filter", {"startDate" : $(this).attr("startDate").value, "startTime" : $(this).attr("startTime").value,
                               "endDate" : $(this).attr("endDate").value, "endTime" : $(this).attr("endTime").value}, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function clearFilter() {
    $(this).attr("startDate").value = null;
    $(this).attr("startTime").value = null;
    $(this).attr("endDate").value = null;
    $(this).attr("endTime").value = null;
    filterTable();
}