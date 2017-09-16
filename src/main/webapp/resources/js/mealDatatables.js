var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (date, type, row) {
                    if (type === "display") {
                        return Date.parse(date).toString('dd-MM-yyyy hh:mm');
                    }
                    return date;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "Delete",
                "orderable": false,
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass(data.exceed ? "exceeded" : "normal");
        },
        "initComplete": makeEditable
    });
});

$('#startDate').datetimepicker({
    timepicker:false,
    format:'Y-m-d'
});

$('#startTime').datetimepicker({
    datepicker:false,
    format:'H:i'
});

$('#endDate').datetimepicker({
    timepicker:false,
    format:'Y-m-d'
});

$('#endTime').datetimepicker({
    datepicker:false,
    format:'H:i'
});

$('#dateTime').datetimepicker({
    format:'Y-m-d H:i'
});
