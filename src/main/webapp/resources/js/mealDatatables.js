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
    makeEditable();
 });

function updateTable(clearFilter) {

    clearFilter = (typeof clearFilter !== 'undefined') ? clearFilter : false;
    var form = $("#filter");

    if (clearFilter===true){
        $(form).find(":input").val("");
    }

    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: form.serialize(),
        success: function (data) {
            datatableApi.clear().rows.add(data).draw();
        }
    });
}
