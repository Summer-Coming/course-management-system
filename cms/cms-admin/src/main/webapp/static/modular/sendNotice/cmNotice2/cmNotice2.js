/**
 * 发送通知管理初始化
 */
var CmNotice2 = {
    id: "CmNotice2Table",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CmNotice2.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '主键', field: 'id', visible: true, align: 'center', valign: 'middle'},
            // {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            // {title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            // {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            // {title: '创建人', field: 'creater', visible: true, align: 'center', valign: 'middle'},
            // {title: '', field: 'deptid', visible: true, align: 'center', valign: 'middle'}
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '内容', field: 'content', align: 'center', valign: 'middle', sortable: true},
        {title: '部门', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
        {title: '发布者', field: 'createrName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
CmNotice2.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CmNotice2.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加发送通知
 */
CmNotice2.openAddCmNotice2 = function () {
    var index = layer.open({
        type: 2,
        title: '添加发送通知',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cmNotice2/cmNotice2_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看发送通知详情
 */
CmNotice2.openCmNotice2Detail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '发送通知详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/cmNotice2/cmNotice2_update/' + CmNotice2.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除发送通知
 */
CmNotice2.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/cmNotice2/delete", function (data) {
            Feng.success("删除成功!");
            CmNotice2.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("cmNotice2Id",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询发送通知列表
 */
CmNotice2.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    CmNotice2.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = CmNotice2.initColumn();
    var table = new BSTable(CmNotice2.id, "/cmNotice2/list", defaultColunms);
    table.setPaginationType("client");
    CmNotice2.table = table.init();
});
