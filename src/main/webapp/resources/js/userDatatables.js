var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
                "asc"
            ]
        ]
    });
    makeEditable();
});

function changeActive(id, enabled){
    $.ajax({
        url: ajaxUrl + "changeActive/" + id,
        type: "POST",
        data: "enabled=" + enabled,
        success: function () {
            updateTable();
            successNoty("Change active");
        }
    });
}

function updateTable() {

    $.get({
        url: ajaxUrl,
        success: function (data) {
            datatableApi.clear().rows.add(data).draw();
        }
    });
}