/**
 * 助教管理管理初始化
 */
var CmAssistant = {
    id: "CmAssistantTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
CmAssistant.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            // {title: '主键id', field: 'id', visible: true, align: 'center', valign: 'middle'},
            // {title: '头像', field: 'avatar', visible: true, align: 'center', valign: 'middle'},
            {title: '账号', field: 'account', visible: true, align: 'center', valign: 'middle'},
            // {title: '密码', field: 'password', visible: true, align: 'center', valign: 'middle'},
            // {title: 'md5密码盐', field: 'salt', visible: true, align: 'center', valign: 'middle'},
            {title: '名字', field: 'name', visible: true, align: 'center', valign: 'middle'},
            // {title: '生日', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            // {title: '性别（1：男 2：女）', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            {title: '电子邮件', field: 'email', visible: true, align: 'center', valign: 'middle'},
            {title: '电话', field: 'phone', visible: true, align: 'center', valign: 'middle'},
            // {title: '角色id', field: 'roleid', visible: true, align: 'center', valign: 'middle'},
            // {title: '部门id', field: 'deptid', visible: true, align: 'center', valign: 'middle'},
            // {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            {title: '部门', field: 'deptName', visible: true, align: 'center', valign: 'middle', sortable: true},
            {title: '状态', field: 'statusName', visible: true, align: 'center', valign: 'middle', sortable: true},
        // {title: '创建时间', field: 'createtime', visible: true, align: 'center', valign: 'middle'},
            // {title: '保留字段', field: 'version', visible: true, align: 'center', valign: 'middle'},
            {title: '课程编号', field: 'classId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
CmAssistant.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        CmAssistant.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加助教管理
 */
CmAssistant.openAddCmAssistant = function () {
    var index = layer.open({
        type: 2,
        title: '添加助教管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cmAssistant/cmAssistant_add'
    });
    this.layerIndex = index;
};

/**
 * 点击批量添加助教管理
 */
CmAssistant.openBatchUploadCmAssistant = function () {
    var index = layer.open({
        type: 2,
        title: '添加助教管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/cmAssistant/cmAssistant_batchUpload'
    });
    this.layerIndex = index;
};

/**
 * 打开查看助教管理详情
 */
CmAssistant.openCmAssistantDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '助教管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/cmAssistant/cmAssistant_update/' + CmAssistant.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除助教管理
 */
CmAssistant.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/cmAssistant/delete", function (data) {
            Feng.success("删除成功!");
            CmAssistant.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("cmAssistantId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询助教管理列表
 */
CmAssistant.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    CmAssistant.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = CmAssistant.initColumn();
    var table = new BSTable(CmAssistant.id, "/cmAssistant/list", defaultColunms);
    table.setPaginationType("client");
    CmAssistant.table = table.init();
});
