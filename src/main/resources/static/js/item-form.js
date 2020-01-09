NAT.itemForm = {};

NAT.itemForm.findAllCategories = function () 
{
    $.ajax({
        type: "GET",
        url: '/api/v1/category/categories',
        dataType: 'json',
        success: function (data) {
            var jsonObj = JSON.parse(JSON.stringify(data));
            var currentItemCategory = $("#current_category_id").val();
            var html = '<option value="0"></option>';

            $.each(jsonObj, function () {
                html += '<option value="' + this.id + '" ';
                if (currentItemCategory == this.id) {
                    html += ' selected=selected';
                }
                html += '>' + this.name + '</option>'
            });

            $("#category_select").html(html);
        }
    });
}