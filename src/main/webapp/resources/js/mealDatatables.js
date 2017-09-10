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

function filterTable() {

    $.get(ajaxUrl + "filter", {"startDate" : $(this).attr("startDate").value, "startTime" : $(this).attr("startTime").value,
                               "endDate" : $(this).attr("endDate").value, "endTime" : $(this).attr("endTime").value}, function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}
